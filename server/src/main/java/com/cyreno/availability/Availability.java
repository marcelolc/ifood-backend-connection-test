package com.cyreno.availability;

import com.cyreno.util.Interval;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.time.Instant;

@Entity
@NoArgsConstructor
public class Availability {

    @Id
    @Getter
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    private String id;

    @Getter
    private Long restaurantId;

    @Getter
    @Setter
    private Instant startTime;

    @Getter
    @Setter
    private Instant endTime;

    public Availability(Long restaurantId, Instant startTime, Instant endTime) {
        this.restaurantId = restaurantId;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public Interval getInterval() {
        return Interval.of(startTime, endTime);
    }

    public boolean contains(@NotNull Instant instant) {
        return startTime.isBefore(instant) && endTime.isAfter(instant);
    }

    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("restaurantId", restaurantId)
                .append("startTime", startTime)
                .append("endTime", endTime)
                .toString();
    }

}
