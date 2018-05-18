package com.cyreno.ranking;

import com.cyreno.ranking.RankingCalculatorTask.RankingCalculatorTaskArgs;
import com.cyreno.restaurant.Restaurant;
import org.apache.ignite.Ignite;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

/**
 * https://apacheignite.readme.io/docs/compute-tasks
 */
@Service
public class RankingCalculator {

    private final Ignite ignite;
    private final UnavailabilityCalculator unavailabilityCalculator;

    public RankingCalculator(UnavailabilityCalculator unavailabilityCalculator, Ignite ignite) {
        this.unavailabilityCalculator = unavailabilityCalculator;
        this.ignite = ignite;
    }

    Ranking compute(List<Restaurant> restaurants, LocalDate start, LocalDate end) {

        RankingCalculatorTaskArgs args = new RankingCalculatorTaskArgs(start, end, restaurants, unavailabilityCalculator);
        Ranking ranking = ignite.compute().execute(RankingCalculatorTask.class, args);

        return ranking;

    }

}