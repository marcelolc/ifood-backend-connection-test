package com.cyreno.ranking;

import com.cyreno.availability.Availability;
import com.cyreno.availability.AvailabilityService;
import com.cyreno.keepalive.KeepAliveUtils;
import com.cyreno.restaurant.Restaurant;
import com.cyreno.unavailability.ScheduledUnavailability;
import com.cyreno.unavailability.ScheduledUnavailabilityService;
import com.cyreno.util.Interval;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;

@Service
public class UnavailabilityCalculator {

    private final KeepAliveUtils keepAliveInfo;
    private final AvailabilityService availabilityService;
    private final ScheduledUnavailabilityService scheduledUnavailabilityService;

    public UnavailabilityCalculator(KeepAliveUtils keepAliveInfo,
                                    AvailabilityService availabilityService,
                                    ScheduledUnavailabilityService scheduledUnavailabilityService) {
        this.keepAliveInfo = keepAliveInfo;
        this.availabilityService = availabilityService;
        this.scheduledUnavailabilityService = scheduledUnavailabilityService;
    }

    public UnavailabilityByRestaurant getRankingItem(Restaurant restaurant, LocalDate start, LocalDate end) {

        System.out.println("RankingCalculatorTask.execute" + restaurant);

        UnavailabilityByRestaurant unavailabilityByRestaurant = new UnavailabilityByRestaurant(restaurant);

        LocalDate day = start;
        while (!day.isAfter(end)) {

            UnavailabilityByDay unavailabilityByDay = getRestaurantUnavailabilitiesByDay(restaurant, day);
            unavailabilityByRestaurant.addUnavailabilityGroup(unavailabilityByDay);
            day = day.plusDays(1);

        }

        return unavailabilityByRestaurant;

    }

    UnavailabilityByDay getRestaurantUnavailabilitiesByDay(Restaurant restaurant, LocalDate day) {

        KeepAliveUtils settings = keepAliveInfo;

        List<Interval> availabilities = availabilityService.getAvailabilitiesByRestaurant(restaurant.getId(), day)
                .stream()
                .map(Availability::getInterval)
                .collect(Collectors.toList());

        List<Interval> scheduledUnavailabilities = scheduledUnavailabilityService.getUnavailabilitiesByRestaurant(restaurant.getId(), day)
                .stream()
                .map(ScheduledUnavailability::getInterval)
                .collect(Collectors.toList());

        // For non-scheduled unavailabilities intervals calculation,
        // Scheduled Unavailabilities has the same "effect" of an availability
        availabilities.addAll(scheduledUnavailabilities);

        Instant start = settings.getOpening(day);
        Instant end = settings.getClosing(day);

        // We start considering the whole day as unavailable
        List<Interval> unavailabilities = asList(Interval.of(start, end));

        // Then we subtract the all the availability and all the scheduled unavailability
        unavailabilities = Interval.minus(unavailabilities, availabilities);

        UnavailabilityByDay unavailabilityByDay = new UnavailabilityByDay(day);
        unavailabilityByDay.addIntervals(unavailabilities);

        return unavailabilityByDay;

    }
}
