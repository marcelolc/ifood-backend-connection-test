package com.cyreno.ranking;

import com.cyreno.restaurant.Restaurant;
import lombok.Data;
import lombok.Getter;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.ArrayList;
import java.util.List;

import static java.util.Comparator.comparing;
import static java.util.Comparator.reverseOrder;
import static java.util.stream.Collectors.toList;

@Data
public class UnavailabilityByRestaurant {

    @Getter
    private final Long restaurantId;

    @Getter
    private final String restaurantName;

    private final List<UnavailabilityByDay> unavailabilityByDays = new ArrayList<>();

    @Getter
    private Long offlineTimeInSeconds = 0L;

    UnavailabilityByRestaurant(Restaurant restaurant) {
        this.restaurantId = restaurant.getId();
        this.restaurantName = restaurant.getName();
    }

    void addUnavailabilityGroup(UnavailabilityByDay group) {
        offlineTimeInSeconds += group.getOfflineTimeInSeconds();
        unavailabilityByDays.add(group);
    }

    public List<UnavailabilityByDay> getUnavailabilityByDays() {
        return unavailabilityByDays.stream()
                .sorted(comparing(UnavailabilityByDay::getDay, reverseOrder()))
                .collect(toList());
    }

    public String toString() {
        return new ToStringBuilder(this)
                .append("restaurantId", restaurantId)
                .append("restaurantName", restaurantName)
                .append("offlineTimeInSeconds", offlineTimeInSeconds)
                .append("unavailabilityByDays", unavailabilityByDays)
                .toString();
    }

}
