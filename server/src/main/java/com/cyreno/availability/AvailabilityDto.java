package com.cyreno.availability;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;
import java.time.Instant;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class AvailabilityDto {

    private String id;
    private Long restaurantId;
    private Boolean online;

    public AvailabilityDto(Long restaurantId, Boolean online) {
        this.restaurantId = restaurantId;
        this.online = online;
    }

}
