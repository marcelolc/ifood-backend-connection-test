package com.cyreno.availability;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface AvailabilityRepository extends JpaRepository<Availability, String> {

    Optional<Availability> findFirstByRestaurantIdOrderByStartTimeDesc(Long restaurantId);

    @Query("select a from Availability a where a.restaurantId=?1 and a.startTime >= ?2 and a.endTime<=?3 order by a.startTime")
    List<Availability> findByRestaurant(Long restaurantId, Instant start, Instant end);

}
