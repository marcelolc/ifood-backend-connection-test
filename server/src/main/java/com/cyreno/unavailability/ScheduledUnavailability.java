package com.cyreno.unavailability;

import com.cyreno.util.Interval;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.Instant;

@Entity
@NoArgsConstructor
public class ScheduledUnavailability {

    @Id
    @Getter
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    private String id;

    @Getter
    private Long restaurantId;

    @Getter
    @Setter
    private String reason;

    @Getter
    @Setter
    private Instant startTime;

    @Getter
    @Setter
    private Instant endTime;

    public ScheduledUnavailability(Long restaurantId, String reason, Instant startTime, Instant endTime) {
        this.restaurantId = restaurantId;
        this.reason = reason;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public Interval getInterval() {
        return Interval.of(startTime, endTime);
    }

    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("restaurantId", restaurantId)
                .append("reason", reason)
                .append("startTime", startTime)
                .append("endTime", endTime)
                .toString();
    }

}
