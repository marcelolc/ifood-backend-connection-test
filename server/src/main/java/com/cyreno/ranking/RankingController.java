package com.cyreno.ranking;

import com.cyreno.restaurant.Restaurant;
import com.cyreno.restaurant.RestaurantService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/ranking")
public class RankingController {

    private final RankingCalculator rankingCalculator;
    private final RestaurantService restaurantService;

    public RankingController(RankingCalculator rankingCalculator, RestaurantService restaurantService) {
        this.rankingCalculator = rankingCalculator;
        this.restaurantService = restaurantService;
    }

    @GetMapping(value = "/{since}", params = "full=true")
    public Ranking getRanking(@PathVariable String since) {
        log.info("RankingController.getFullRanking");
        return buildRanking(since, restaurantService.findAll());
    }

    @GetMapping("/{since}")
    public Ranking getRanking(@PathVariable String since, @RequestParam("restaurantId") List<Long> restaurantIds) {
        log.info("RankingController.getFullRanking");
        return buildRanking(since, restaurantService.findByIds(restaurantIds));
    }

    private Ranking buildRanking(String since, List<Restaurant> restaurants) {
        return rankingCalculator.compute(restaurants, getStartDate(since), LocalDate.now());
    }

    private LocalDate getStartDate(String since) {
        return LocalDate.parse(since);
    }

}
