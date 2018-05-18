package com.cyreno.unavailability;

import com.cyreno.keepalive.KeepAliveUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
public class ScheduledUnavailabilityService {

    private final ScheduledUnavailabilityRepository scheduledUnavailabilityRepository;
    private final KeepAliveUtils keepAliveUtils;

    public ScheduledUnavailabilityService(ScheduledUnavailabilityRepository scheduledUnavailabilityRepository,
                                          KeepAliveUtils keepAliveUtils) {
        this.scheduledUnavailabilityRepository = scheduledUnavailabilityRepository;
        this.keepAliveUtils = keepAliveUtils;
    }

    public List<ScheduledUnavailability> getUnavailabilitiesByRestaurant(Long restaurantId, LocalDate since) {
        Instant start = keepAliveUtils.getOpening(since);
        Instant end = keepAliveUtils.getClosing(since);
        return getUnavailabilitiesByRestaurant(restaurantId, start, end);
    }

    List<ScheduledUnavailability> getUnavailabilitiesByRestaurant(Long restaurantId, Instant start, Instant end) {
        return scheduledUnavailabilityRepository.findByRestaurant(restaurantId, start, end);
    }

    void deleteUnavailability(Long restaurantId, String id) {

        Optional<ScheduledUnavailability> unavailabilityOptional = scheduledUnavailabilityRepository.findById(id);
        if (!unavailabilityOptional.isPresent()) {
            throw new IllegalArgumentException("scheduledUnavailability not found!");
        }

        ScheduledUnavailability scheduledUnavailability = unavailabilityOptional.get();
        if (!Objects.equals(scheduledUnavailability.getRestaurantId(), restaurantId)) {
            throw new IllegalArgumentException("Invalid unavailabilityId!");
        }

        scheduledUnavailabilityRepository.delete(scheduledUnavailability);

    }

    void saveUnavailability(ScheduledUnavailability scheduledUnavailability) {

        Instant startTime = scheduledUnavailability.getStartTime();
        Instant endTime = scheduledUnavailability.getEndTime();

        Duration scheduledUnavailabilityDuration = Duration.between(startTime, endTime);
        Duration maxDuration = Duration.between(keepAliveUtils.getTodayOpening(), keepAliveUtils.getTodayClosing());

        if (scheduledUnavailabilityDuration.toMillis() > maxDuration.toMillis()) {
            throw new IllegalArgumentException("Maximum duration exceeded : " + maxDuration.getSeconds() + "s");
        }

        LocalDateTime start = LocalDateTime.ofInstant(startTime, keepAliveUtils.getZoneOffset());
        LocalDateTime end = LocalDateTime.ofInstant(endTime, keepAliveUtils.getZoneOffset());
        LocalDateTime opening = keepAliveUtils.getOpening(start);
        LocalDateTime closing = keepAliveUtils.getClosing(end);

        if (start.isBefore(opening)) {
            throw new IllegalArgumentException("Too early, ScheduledUnavailability ignored!");
        }

        if (start.isAfter(closing)) {
            throw new IllegalArgumentException("Too late, ScheduledUnavailability ignored!");
        }

        if (end.isAfter(closing)) {
            throw new IllegalArgumentException("Too late, ScheduledUnavailability ignored!");
        }

        scheduledUnavailabilityRepository.save(scheduledUnavailability);

    }


}
