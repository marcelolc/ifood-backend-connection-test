package com.cyreno.availability;

import com.cyreno.keepalive.KeepAliveUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class AvailabilityService {

    private final AvailabilityRepository availabilityRepository;
    private final KeepAliveUtils keepAliveUtils;

    public AvailabilityService(AvailabilityRepository availabilityRepository,
                               KeepAliveUtils keepAliveUtils) {
        this.availabilityRepository = availabilityRepository;
        this.keepAliveUtils = keepAliveUtils;
    }

    public boolean isOnline(@NotNull Long restaurantId) {

        Optional<Availability> availabilityOptional = availabilityRepository.findFirstByRestaurantIdOrderByStartTimeDesc(restaurantId);
        if (!availabilityOptional.isPresent()) {
            return false;
        }
        return availabilityOptional.get().contains(Instant.now());

    }

    public List<Availability> getAvailabilitiesByRestaurant(Long restaurantId, LocalDate since) {
        Instant start = keepAliveUtils.getOpening(since);
        Instant end = keepAliveUtils.getClosing(since);
        return availabilityRepository.findByRestaurant(restaurantId, start, end);
    }

    public void saveNewOrExtendAvailability(Long restaurantId) {

        Instant now = Instant.now();

        Availability availability = getLatestAvailabilityByRestaurant(restaurantId);
        if (availability != null && availability.contains(now)) {
            extendAvailability(availability);
            return;
        }

        saveNewAvailability(restaurantId);

    }

    private Availability getLatestAvailabilityByRestaurant(Long restaurantId) {

        Optional<Availability> availabilityOptional = availabilityRepository.findFirstByRestaurantIdOrderByStartTimeDesc(restaurantId);
        return availabilityOptional.orElse(null);

    }

    private void extendAvailability(Availability availability) {
        availability.setEndTime(Instant.now().plusSeconds(keepAliveUtils.getTtlInSeconds()));
        saveAvailability(availability);
    }

    private void saveNewAvailability(Long restaurantId) {
        saveAvailability(new Availability(restaurantId, Instant.now(), Instant.now().plusSeconds(keepAliveUtils.getTtlInSeconds())));
    }

    void saveAvailability(Availability availability) {

        Instant opening = keepAliveUtils.getTodayOpening();
        Instant closing = keepAliveUtils.getTodayClosing();
        int ttlInSeconds = keepAliveUtils.getTtlInSeconds();

        Instant startTime = availability.getStartTime();
        if (startTime.plusSeconds(ttlInSeconds).isBefore(opening)) {
            throw new IllegalArgumentException("AvailabilityService.saveAvailability - Msg ignored: Too early!");
        }

        if (startTime.isAfter(closing)) {
            throw new IllegalArgumentException("AvailabilityService.saveAvailability - Msg ignored: Too late!");
        }

        if (startTime.isBefore(opening)) {
            availability.setStartTime(opening);
        }

        Instant endTime = availability.getEndTime();
        if (endTime.isAfter(closing)) {
            availability.setEndTime(closing);
        }

        availabilityRepository.save(availability);

    }

}
