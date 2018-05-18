package com.cyreno.unavailability;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/v1/restaurant/{restaurantId}/scheduled-unavailability")
public class ScheduledUnavailabilityController {

    private final ScheduledUnavailabilityService scheduledUnavailabilityService;

    public ScheduledUnavailabilityController(ScheduledUnavailabilityService scheduledUnavailabilityService) {
        this.scheduledUnavailabilityService = scheduledUnavailabilityService;
    }

    @GetMapping("/{since}")
    public List<ScheduledUnavailabilityDto> getUnavailabilitiesByRestaurant(@PathVariable Long restaurantId, @PathVariable Instant since) {

        log.info("ScheduledUnavailabilityController.getUnavailabilitiesByRestaurant");
        List<ScheduledUnavailability> unavailabilities = scheduledUnavailabilityService.getUnavailabilitiesByRestaurant(restaurantId, since, Instant.MAX);

        return unavailabilities.stream().map(ScheduledUnavailabilityDto::new).collect(Collectors.toList());

    }

    @PostMapping
    public ScheduledUnavailabilityDto saveUnavailability(@PathVariable Long restaurantId, @RequestBody ScheduledUnavailabilityDto scheduledUnavailabilityDto) {

        log.info("UnavailabilityController.saveUnavailability");

        ScheduledUnavailability scheduledUnavailability = new ScheduledUnavailability(restaurantId, scheduledUnavailabilityDto.getReason(), scheduledUnavailabilityDto.getStart(), scheduledUnavailabilityDto.getEnd());
        scheduledUnavailabilityService.saveUnavailability(scheduledUnavailability);

        return new ScheduledUnavailabilityDto(scheduledUnavailability);

    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/{unavailabilityId}")
    public void deleteUnavailability(@PathVariable Long restaurantId, @PathVariable String unavailabilityId) {

        log.info("UnavailabilityController.deleteUnavailability");

        scheduledUnavailabilityService.deleteUnavailability(restaurantId, unavailabilityId);

    }

}
