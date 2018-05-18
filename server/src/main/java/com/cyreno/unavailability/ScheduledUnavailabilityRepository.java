package com.cyreno.unavailability;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.Instant;
import java.util.List;

public interface ScheduledUnavailabilityRepository extends JpaRepository<ScheduledUnavailability, String> {

    @Query("select a from ScheduledUnavailability a where a.restaurantId = ?1 and a.startTime >= ?2 order by a.startTime desc ")
    List<ScheduledUnavailability> findByRestaurant(Long restaurantId, Instant start, Instant end);

}
