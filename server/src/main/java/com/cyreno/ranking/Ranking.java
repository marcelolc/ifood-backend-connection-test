package com.cyreno.ranking;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@Data
@NoArgsConstructor
public class Ranking {

    private LocalDate start;
    private LocalDate end;
    private List<UnavailabilityByRestaurant> unavailabilityByRestaurants;

    Ranking(LocalDate start, LocalDate end, List<UnavailabilityByRestaurant> unavailabilityByRestaurants) {
        this.start = start;
        this.end = end;
        this.unavailabilityByRestaurants = unavailabilityByRestaurants;
    }

    public List<UnavailabilityByRestaurant> getUnavailabilityByRestaurants() {
        return Collections.unmodifiableList(unavailabilityByRestaurants);
    }

}
