package com.cyreno.util;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.validation.constraints.NotNull;
import java.time.Duration;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Stack;

import static java.util.Arrays.asList;
import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;

public class Interval {

    @Getter
    private final Instant start;
    @Getter
    private final Instant end;

    public static Interval of(Instant start, Instant end) {
        return new Interval(start, end);
    }

    private Interval(Instant start, Instant end) {

        if (start.isAfter(end)) {
            throw new IllegalArgumentException("Start parameter must be before (in time) than end parameter!");
        }

        this.start = start;
        this.end = end;

    }

    public List<Interval> minus(@NotNull Interval subtrahend) {

        // Contains
        //          +--------------------------------------+
        //                         +-----------+
        if (start.isBefore(subtrahend.start) && end.isAfter(subtrahend.end)) {
            return asList(of(start, subtrahend.start), of(subtrahend.end, end));
        }

        // Contains
        //          +--------------------------------------+
        //          +-----------+
        if (start.equals(subtrahend.start) && end.isAfter(subtrahend.end)) {
            return asList(of(subtrahend.end, end));
        }

        // Contains
        //          +--------------------------------------+
        //                                     +-----------+
        if (start.isBefore(subtrahend.start) && end.equals(subtrahend.end)) {
            return asList(of(start, subtrahend.start));
        }

        // Overlaps at start
        //          +--------------------------------------+
        //     +-----------+
        if (start.isAfter(subtrahend.start) && start.isBefore(subtrahend.end) && end.isAfter(subtrahend.end)) {
            return asList(of(subtrahend.end, end));
        }


        // Overlaps at end
        //          +--------------------------------------+
        //                                            +-----------+
        if (start.isBefore(subtrahend.start) && end.isAfter(subtrahend.start) && end.isBefore(subtrahend.end)) {
            return asList(of(start, subtrahend.start));
        }

        // Is contained
        //          +--------------------------------------+
        //     +------------------------------------------------+
        if (start.isAfter(subtrahend.start) && end.isBefore(subtrahend.end)) {
            return Collections.emptyList();
        }

        // Is contained (equals)
        //          +--------------------------------------+
        //          +--------------------------------------+
        if (start.equals(subtrahend.start) && end.equals(subtrahend.end)) {
            return Collections.emptyList();
        }

        // No Overlaps
        //          +---------------------------+
        //                                            +-----------+
        return asList(of(start, end));

    }

    public static List<Interval> minus(List<Interval> minuends, List<Interval> subtrahends) {

        Stack<Interval> minuendsStack = new Stack<>();
        Stack<Interval> minuendsStackAux = new Stack<>();
        minuendsStack.addAll(minuends);

        for (Interval subtrahend : subtrahends) {

            while (!minuendsStack.isEmpty()) {
                Interval minuend = minuendsStack.pop();
                List<Interval> result = minuend.minus(subtrahend);
                minuendsStackAux.addAll(result);
            }

            minuendsStack = minuendsStackAux;
            minuendsStackAux = new Stack<>();

        }

        return minuendsStack
                .stream()
                .sorted(comparing(Interval::getStart))
                .collect(toList());

    }

    public Long getOfflineTimeInSeconds() {
        return getDuration().getSeconds();
    }

    @JsonIgnore
    public Duration getDuration() {
        return Duration.between(start, end);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Interval that = (Interval) o;
        return Objects.equals(start, that.start) &&
                Objects.equals(end, that.end);
    }

    @Override
    public int hashCode() {
        return Objects.hash(start, end);
    }

    public String toString() {
        return new ToStringBuilder(this)
                .append("start", start)
                .append("end", end)
                .toString();
    }

}
