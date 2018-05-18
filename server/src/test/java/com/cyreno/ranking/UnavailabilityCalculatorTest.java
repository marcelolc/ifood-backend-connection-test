package com.cyreno.ranking;

import com.cyreno.availability.Availability;
import com.cyreno.availability.AvailabilityService;
import com.cyreno.keepalive.KeepAliveUtils;
import com.cyreno.restaurant.Restaurant;
import com.cyreno.unavailability.ScheduledUnavailability;
import com.cyreno.unavailability.ScheduledUnavailabilityService;
import com.cyreno.util.Interval;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static java.util.Arrays.asList;
import static java.util.concurrent.TimeUnit.HOURS;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
public class UnavailabilityCalculatorTest {

    private static final Long RESTAURANT_1_ID = 1L;
    private static final LocalDate DATE_TODAY = LocalDate.now();
    private static final LocalDate DATE_1_DAY_AGO = DATE_TODAY.minusDays(1);
    private static final LocalDate DATE_2_DAY_AGO = DATE_TODAY.minusDays(2);

    private Restaurant restaurant;
    private KeepAliveUtils keepAliveUtils;

    @MockBean
    private AvailabilityService availabilityService;
    @MockBean
    private ScheduledUnavailabilityService scheduledUnavailabilityService;

    private UnavailabilityCalculator unavailabilityCalculator;

    @Before
    public void setUp() {

        restaurant = new Restaurant("TestRestaurant_1");
        restaurant.setId(1L);

        keepAliveUtils = new KeepAliveUtils(
                "10:00",
                "23:00",
                "tcp://localhost:1883",
                "ifood/keepalive",
                30,
                2);

        unavailabilityCalculator = new UnavailabilityCalculator(keepAliveUtils, availabilityService, scheduledUnavailabilityService);

        // Restaurant 1 / 5h Offline
        Mockito.when(availabilityService.getAvailabilitiesByRestaurant(RESTAURANT_1_ID, DATE_TODAY)).thenReturn(asList(
                availability(DATE_TODAY, 10_00, 11_00),
                availability(DATE_TODAY, 12_00, 14_00),
                availability(DATE_TODAY, 16_00, 20_00),
                availability(DATE_TODAY, 21_00, 22_00)
        ));

        // Restaurant 1 / 4h Offline
        Mockito.when(availabilityService.getAvailabilitiesByRestaurant(RESTAURANT_1_ID, DATE_1_DAY_AGO)).thenReturn(asList(
                availability(DATE_1_DAY_AGO, 10_30, 12_00),
                availability(DATE_1_DAY_AGO, 13_00, 17_30),
                availability(DATE_1_DAY_AGO, 18_00, 20_00),
                availability(DATE_1_DAY_AGO, 21_00, 22_00)
        ));

        // Restaurant 1 / Scheduled unavailability (- 1h offline)
        Mockito.when(scheduledUnavailabilityService.getUnavailabilitiesByRestaurant(RESTAURANT_1_ID, DATE_1_DAY_AGO)).thenReturn(asList(
                unavailability(DATE_1_DAY_AGO, 22_00, 23_00)
        ));

        // Restaurant 1 / 3h 10m Offline
        Mockito.when(availabilityService.getAvailabilitiesByRestaurant(RESTAURANT_1_ID, DATE_2_DAY_AGO)).thenReturn(asList(
                availability(DATE_2_DAY_AGO, 11_00, 15_00),
                availability(DATE_2_DAY_AGO, 15_30, 18_00),
                availability(DATE_2_DAY_AGO, 18_30, 20_00),
                availability(DATE_2_DAY_AGO, 21_00, 22_00),
                availability(DATE_2_DAY_AGO, 22_10, 23_10)
        ));


    }

    @Test
    public void testGetRestaurantUnavailabilitiesByDay() {

        UnavailabilityByDay group = unavailabilityCalculator.getRestaurantUnavailabilitiesByDay(restaurant, DATE_TODAY);
        assertThat(group, notNullValue());
        assertThat(group.getOfflineTimeInSeconds(), equalTo(HOURS.toSeconds(5)));

        List<Interval> intervals = group.getIntervals();
        assertThat(intervals, hasSize(4));
        assertThat(intervals.get(0).getDuration().getSeconds(), equalTo(HOURS.toSeconds(1)));
        assertThat(intervals.get(1).getDuration().getSeconds(), equalTo(HOURS.toSeconds(2)));
        assertThat(intervals.get(2).getDuration().getSeconds(), equalTo(HOURS.toSeconds(1)));
        assertThat(intervals.get(3).getDuration().getSeconds(), equalTo(HOURS.toSeconds(1)));

    }

    @Test
    public void testGetRankingItem() {

        UnavailabilityByRestaurant unavailabilityByRestaurant = unavailabilityCalculator.getRankingItem(restaurant, DATE_2_DAY_AGO, DATE_TODAY);

        assertThat(unavailabilityByRestaurant, notNullValue());
        List<UnavailabilityByDay> unavailabilityGroups = unavailabilityByRestaurant.getUnavailabilityByDays();
        assertThat(unavailabilityGroups, hasSize(3));

        assertThat(unavailabilityGroups.get(0).getOfflineTimeInSeconds(), equalTo(HOURS.toSeconds(5)));
        assertThat(unavailabilityGroups.get(1).getOfflineTimeInSeconds(), equalTo(HOURS.toSeconds(3)));
        assertThat(unavailabilityGroups.get(2).getOfflineTimeInSeconds(), equalTo(HOURS.toSeconds(3) + 600));

    }

    //
    // Helper Functions
    //

    private ScheduledUnavailability unavailability(LocalDate date, int startTimeHHmm, int endTimeHHmm) {
        return new ScheduledUnavailability(RESTAURANT_1_ID, "Testing", instant(date, startTimeHHmm), instant(date, endTimeHHmm));
    }

    private Availability availability(LocalDate date, int startTimeHHmm, int endTimeHHmm) {
        return new Availability(RESTAURANT_1_ID, instant(date, startTimeHHmm), instant(date, endTimeHHmm));
    }

    private Instant instant(LocalDate date, int timeHHmm) {
        return LocalDateTime.of(date, LocalTime.of(timeHHmm / 100, timeHHmm % 100)).toInstant(keepAliveUtils.getZoneOffset());
    }

}