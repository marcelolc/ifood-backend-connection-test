package com.cyreno.restaurant;

import com.cyreno.restaurant.RestaurantDto.Status;
import com.cyreno.availability.AvailabilityService;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.cyreno.restaurant.RestaurantDto.Status.ONLINE;
import static com.cyreno.restaurant.RestaurantDto.Status.OFFLINE;

@RestController
@RequestMapping("/api/v1/restaurant")
public class RestaurantController {

    private final RestaurantService restaurantService;
    private final AvailabilityService availabilityService;

    public RestaurantController(RestaurantService restaurantService, AvailabilityService availabilityService) {
        this.restaurantService = restaurantService;
        this.availabilityService = availabilityService;
    }

    @GetMapping("/{restaurantId}")
    public RestaurantDto getRestaurant(@PathVariable Long restaurantId) {
        return getRestaurantDto(restaurantId);
    }

    @GetMapping
    public List<RestaurantDto> get(@RequestParam("restaurantId") List<Long> restaurantIds) {
        return restaurantIds
                .stream()
                .map(this::getRestaurantDto)
                .collect(Collectors.toList());
    }

    private RestaurantDto getRestaurantDto(Long restaurantId) {

        Optional<Restaurant> optionalRestaurant = restaurantService.findById(restaurantId);
        if (!optionalRestaurant.isPresent()) {
            throw new EntityNotFoundException();
        }

        Status status = availabilityService.isOnline(restaurantId) ? ONLINE : OFFLINE;

        return new RestaurantDto(optionalRestaurant.get(), status);

    }

}
