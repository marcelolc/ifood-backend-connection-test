package com.cyreno.unavailability;

import com.cyreno.keepalive.KeepAliveUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.boot.test.mock.mockito.MockBean;

public class ScheduledUnavailabilityServiceTest {

    private static final long RESTAURANT_ID = 1L;
    private static final String REASON = "TEST";

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @MockBean
    private ScheduledUnavailabilityRepository scheduledUnavailabilityRepository;
    private ScheduledUnavailabilityService scheduledUnavailabilityService;
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

        scheduledUnavailabilityService = new ScheduledUnavailabilityService(scheduledUnavailabilityRepository, keepAliveUtils);

    }

    @Test
    public void testSaveUnavailability__MaxDurationExceeded() {

        expectedException.expect(IllegalArgumentException.class);

        ScheduledUnavailability scheduledUnavailability = new ScheduledUnavailability(
                RESTAURANT_ID,
                REASON,
                keepAliveUtils.getTodayOpening().minusSeconds(10),
                keepAliveUtils.getTodayClosing());

        scheduledUnavailabilityService.saveUnavailability(scheduledUnavailability);

    }

    @Test
    public void testSaveUnavailability__TooEarly() {

        expectedException.expect(IllegalArgumentException.class);

        ScheduledUnavailability scheduledUnavailability = new ScheduledUnavailability(
                RESTAURANT_ID,
                REASON,
                keepAliveUtils.getTodayOpening().minusSeconds(10),
                keepAliveUtils.getTodayOpening().plusSeconds(10));

        scheduledUnavailabilityService.saveUnavailability(scheduledUnavailability);

    }

    @Test
    public void testSaveUnavailability__TooLate() {

        expectedException.expect(IllegalArgumentException.class);

        ScheduledUnavailability scheduledUnavailability = new ScheduledUnavailability(
                RESTAURANT_ID,
                REASON,
                keepAliveUtils.getTodayClosing().minusSeconds(10),
                keepAliveUtils.getTodayClosing().plusSeconds(10));

        scheduledUnavailabilityService.saveUnavailability(scheduledUnavailability);

    }


}