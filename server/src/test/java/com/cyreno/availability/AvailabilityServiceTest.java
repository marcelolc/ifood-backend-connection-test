package com.cyreno.availability;

import com.cyreno.keepalive.KeepAliveUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
public class AvailabilityServiceTest {

    private static final long RESTAURANT_ID = 1L;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @MockBean
    private AvailabilityRepository availabilityRepository;

    private AvailabilityService availabilityService;
    private KeepAliveUtils keepAliveUtils;

    @Before
    public void setUp() {

        keepAliveUtils = new KeepAliveUtils(
                "10:00",
                "23:00",
                "tcp://localhost:1883",
                "ifood/keepalive",
                30,
                2);

        availabilityService = new AvailabilityService(availabilityRepository, keepAliveUtils);

    }

    @Test
    public void testSaveAvailability__tooEarly() {

        expectedException.expect(IllegalArgumentException.class);

        Availability availability = new Availability(RESTAURANT_ID, todayAt(8_00), todayAt(23_30));
        availabilityService.saveAvailability(availability);

        verify(availabilityRepository, never()).save(availability);

    }

    @Test
    public void testSaveAvailability__tooLate() {

        expectedException.expect(IllegalArgumentException.class);

        Availability availability = new Availability(RESTAURANT_ID, todayAt(23_10), todayAt(23_30));
        availabilityService.saveAvailability(availability);

        verify(availabilityRepository, never()).save(availability);

    }

    @Test
    public void testSaveAvailability__withinOpenTime() {

        Availability availability = new Availability(RESTAURANT_ID, todayAt(11_10), todayAt(11_30));
        availabilityService.saveAvailability(availability);

        verify(availabilityRepository, times(1)).save(availability);

    }

    @Test
    public void testSaveAvailability__AdjustStart() {

        Availability availability = new Availability(RESTAURANT_ID, keepAliveUtils.getTodayOpening().minusSeconds(10), todayAt(11_30));
        availabilityService.saveAvailability(availability);

        assertThat(availability.getStartTime(), equalTo(keepAliveUtils.getTodayOpening()));
        verify(availabilityRepository, times(1)).save(availability);
    }

    @Test
    public void testSaveAvailability__AdjustEnd() {

        Availability availability = new Availability(RESTAURANT_ID, todayAt(11_10), keepAliveUtils.getTodayClosing().plusSeconds(10));
        availabilityService.saveAvailability(availability);

        assertThat(availability.getEndTime(), equalTo(keepAliveUtils.getTodayClosing()));
        verify(availabilityRepository, times(1)).save(availability);

    }


    //
    // Helper Functions
    //

    private Instant todayAt(int timeHHmm) {
        return LocalDateTime.of(LocalDate.now(), LocalTime.of(timeHHmm / 100, timeHHmm % 100)).toInstant(keepAliveUtils.getZoneOffset());
    }

}