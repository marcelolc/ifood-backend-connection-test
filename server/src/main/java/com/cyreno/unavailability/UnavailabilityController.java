package com.cyreno.unavailability;

import com.cyreno.ranking.UnavailabilityByRestaurant;
import com.cyreno.ranking.UnavailabilityCalculator;
import com.cyreno.restaurant.Restaurant;
import com.cyreno.restaurant.RestaurantService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/v1/restaurant/{restaurantId}/unavailability")
public class UnavailabilityController {

    private final RestaurantService restaurantService;
    private final UnavailabilityCalculator unavailabilityCalculator;

    public UnavailabilityController(RestaurantService restaurantService, UnavailabilityCalculator unavailabilityCalculator) {
        this.restaurantService = restaurantService;
        this.unavailabilityCalculator = unavailabilityCalculator;
    }


    @GetMapping("/{since}")
    public UnavailabilityByRestaurant getUnavailabilitiesByRestaurant(@PathVariable Long restaurantId, @PathVariable Instant since) {

        log.info("UnavailabilityController.getUnavailabilitiesByRestaurant");

        Optional<Restaurant> restaurantOptional = restaurantService.findById(restaurantId);
        if (!restaurantOptional.isPresent()) {
            throw new IllegalArgumentException("Restaurant not found!");
        }

        LocalDateTime start = LocalDateTime.ofInstant(since, ZoneOffset.UTC);
        return unavailabilityCalculator.getRankingItem(restaurantOptional.get(), start.toLocalDate(), LocalDate.now());
    }

}
