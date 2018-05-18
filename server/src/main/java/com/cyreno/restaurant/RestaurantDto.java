package com.cyreno.restaurant;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RestaurantDto {

    public enum Status {ONLINE, OFFLINE}

    private Long id;
    private String name;
    private Status status;

    public RestaurantDto(Restaurant restaurant, Status status) {
        this.id = restaurant.getId();
        this.name = restaurant.getName();
        this.status = status;
    }

}
