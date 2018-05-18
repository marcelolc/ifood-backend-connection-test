package com.cyreno.ranking;

import com.cyreno.util.Interval;
import lombok.Getter;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class UnavailabilityByDay {

    @Getter
    private LocalDate day;
    @Getter
    private Long offlineTimeInSeconds = 0L;

    private final List<Interval> intervals = new ArrayList<>();

    UnavailabilityByDay(LocalDate day) {
        this.day = day;
    }

    void addIntervals(List<Interval> intervals) {
        intervals.forEach(this::addInterval);
    }

    private void addInterval(Interval interval) {
        offlineTimeInSeconds += interval.getDuration().getSeconds();
        intervals.add(interval);
    }

    public List<Interval> getIntervals() {
        return new ArrayList<>(intervals);
    }

    public String toString() {
        return new ToStringBuilder(this)
                .append("day", day)
                .append("intervals", intervals)
                .append("offlineTimeInSeconds", offlineTimeInSeconds)
                .toString();
    }

}
