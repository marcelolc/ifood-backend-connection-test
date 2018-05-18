package com.cyreno.util;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.time.Instant;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class IntervalTest {

    private static final ZoneOffset ZONE_OFFSET = OffsetDateTime.now().getOffset();

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void test_instantiateWithInvalidValues() {
        expectedException.expect(IllegalArgumentException.class);
        interval(10_00, 9_00);
    }

    @Test
    public void testMinus__ContainsIntervalInTheMiddle() {

        Interval source = interval(10_00, 22_00);
        List<Interval> result = source.minus(interval(12_00, 14_00));

        assertThat(result, hasSize(2));
        assertThat(result.get(0), equalTo(interval(10_00, 12_00)));
        assertThat(result.get(1), equalTo(interval(14_00, 22_00)));

    }

    @Test
    public void testMinus__ContainsIntervalInTheBeginning() {

        Interval source = interval(10_00, 22_00);
        List<Interval> result = source.minus(interval(10_00, 14_00));

        assertThat(result, hasSize(1));
        assertThat(result.get(0), equalTo(interval(14_00, 22_00)));
    }

    @Test
    public void testMinus__ContainsIntervalInTheEnd() {

        Interval source = interval(10_00, 22_00);
        List<Interval> result = source.minus(interval(20_00, 22_00));

        assertThat(result, hasSize(1));
        assertThat(result.get(0), equalTo(interval(10_00, 20_00)));
    }

    @Test
    public void testMinus__overlapsAtTheBeginning() {

        Interval source = interval(10_00, 22_00);
        List<Interval> result = source.minus(interval(9_00, 12_00));

        assertThat(result, hasSize(1));
        assertThat(result.get(0), equalTo(interval(12_00, 22_00)));
    }

    @Test
    public void testMinus__overlapsAtTheEnd() {

        Interval source = interval(10_00, 22_00);
        List<Interval> result = source.minus(interval(20_00, 23_00));

        assertThat(result, hasSize(1));
        assertThat(result.get(0), equalTo(interval(10_00, 20_00)));
    }

    @Test
    public void testMinus__noOverlaps() {

        Interval source = interval(16_00, 22_00);
        List<Interval> result = source.minus(interval(10_00, 15_00));

        assertThat(result, hasSize(1));
        assertThat(result.get(0), equalTo(interval(16_00, 22_00)));
    }

    @Test
    public void testMinus__isContained() {

        Interval source = interval(10_00, 22_00);
        List<Interval> result = source.minus(interval(9_00, 23_00));

        assertThat(result, hasSize(0));
    }

    @Test
    public void testMinus__equals() {

        Interval source = interval(10_00, 22_00);
        List<Interval> result = source.minus(interval(10_00, 22_00));

        assertThat(result, hasSize(0));
    }

    @Test
    public void testMinus_multipleOperations() {

        List<Interval> minuends = singletonList(interval(10_00, 23_00));
        List<Interval> subtrahends = asList(
                interval(10_00, 11_00),
                interval(12_00, 14_00),
                interval(16_00, 20_00),
                interval(21_00, 22_00)
        );

        List<Interval> result = Interval.minus(minuends, subtrahends);
        assertThat(result, hasSize(4));
        assertThat(result.get(0), equalTo(interval(11_00, 12_00)));
        assertThat(result.get(1), equalTo(interval(14_00, 16_00)));
        assertThat(result.get(2), equalTo(interval(20_00, 21_00)));
        assertThat(result.get(3), equalTo(interval(22_00, 23_00)));

    }

    @Test
    public void testGetDuration() {
        Interval interval = interval(10_30, 14_00);
        assertThat(interval.getDuration().toMinutes(), equalTo(210L));
    }

    @Test
    public void testEquals() {
        boolean equals = interval(10, 12).equals(interval(10, 12));
        assertThat(equals, is(true));
    }

    //
    // Helper Functions
    //

    private Interval interval(int startHHmm, int endHHmm) {
        return Interval.of(
                getInstant(startHHmm / 100, startHHmm % 100),
                getInstant(endHHmm / 100, endHHmm % 100)
        );
    }

    private Instant getInstant(int hour, int minute) {
        return LocalDate.now().atTime(hour, minute).toInstant(ZONE_OFFSET);
    }

}