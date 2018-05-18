package com.cyreno;

import com.cyreno.model.Restaurant;
import com.cyreno.model.RestaurantRepository;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class KeepAliveService {

    public static final int INTERVAL_MILLIS = (int) TimeUnit.SECONDS.toMillis(20);
    private final KeepAliveSender keepAliveSender;
    private final RestaurantRepository restaurantRepository;

    public KeepAliveService(
            KeepAliveSender keepAliveSender,
            RestaurantRepository restaurantRepository) {
        this.keepAliveSender = keepAliveSender;
        this.restaurantRepository = restaurantRepository;
    }


    @PostConstruct
    public void start() {
        List<Restaurant> restaurants = restaurantRepository.findAll().stream().limit(2).collect(Collectors.toList());
        restaurants.forEach(this::restaurantLoop);
    }

    private void restaurantLoop(Restaurant restaurant) {
        new Thread(() -> restaurantKeepAliveLoop(restaurant)).start();
    }

    private void restaurantKeepAliveLoop(Restaurant restaurant) {

        while (true) {

            System.out.println(restaurant.getId() + " - " + restaurant.getName());
            String mqttClientId = UUID.randomUUID().toString();
            keepAliveSender.sendMsg(mqttClientId, restaurant.getId().toString().getBytes());

            try {
                Thread.sleep(INTERVAL_MILLIS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

    }

}
