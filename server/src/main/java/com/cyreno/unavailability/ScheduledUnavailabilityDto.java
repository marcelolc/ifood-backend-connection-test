package com.cyreno.unavailability;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ScheduledUnavailabilityDto {

    private String id;
    private Long restaurantId;
    private String reason;
    private Instant start;
    private Instant end;

    public ScheduledUnavailabilityDto(ScheduledUnavailability scheduledUnavailability) {
        this.id = scheduledUnavailability.getId();
        this.restaurantId = scheduledUnavailability.getRestaurantId();
        this.reason = scheduledUnavailability.getReason();
        this.start = scheduledUnavailability.getStartTime();
        this.end = scheduledUnavailability.getEndTime();

    }

}
