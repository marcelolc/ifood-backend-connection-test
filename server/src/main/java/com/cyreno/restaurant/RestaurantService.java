package com.cyreno.restaurant;

import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

@Service
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;

    public RestaurantService(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    public Optional<Restaurant> findById(@NotNull Long restaurantId) {
        return restaurantRepository.findById(restaurantId);
    }

    public List<Restaurant> findAll() {
        return restaurantRepository.findAll();
    }

    public List<Restaurant> findByIds(List<Long> ids) {
        return restaurantRepository.findAllByIdIn(ids);
    }

}
