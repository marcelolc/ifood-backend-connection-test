package com.cyreno.ranking;

import com.cyreno.restaurant.Restaurant;
import org.apache.ignite.IgniteException;
import org.apache.ignite.compute.ComputeJob;
import org.apache.ignite.compute.ComputeJobAdapter;
import org.apache.ignite.compute.ComputeJobResult;
import org.apache.ignite.compute.ComputeTaskSplitAdapter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

class RankingCalculatorTask extends ComputeTaskSplitAdapter<RankingCalculatorTask.RankingCalculatorTaskArgs, Ranking> {

    static class RankingCalculatorTaskArgs {

        private LocalDate start;
        private LocalDate end;
        private List<Restaurant> restaurants;
        private UnavailabilityCalculator unavailabilityCalculator;

        RankingCalculatorTaskArgs(LocalDate start, LocalDate end, List<Restaurant> restaurants, UnavailabilityCalculator unavailabilityCalculator) {
            this.unavailabilityCalculator = unavailabilityCalculator;
            this.restaurants = restaurants;
            this.start = start;
            this.end = end;
        }
    }

    private RankingCalculatorTaskArgs args;

    @Override
    protected List<ComputeJob> split(int gridSize, RankingCalculatorTaskArgs args) throws IgniteException {
        this.args = args;
        return args.restaurants.stream()
                .map(this::getComputeJobAdapter)
                .collect(Collectors.toList());
    }

    @NotNull
    private ComputeJobAdapter getComputeJobAdapter(Restaurant restaurant) {
        return new ComputeJobAdapter() {
            @Override
            public Object execute() throws IgniteException {
                return args.unavailabilityCalculator.getRankingItem(restaurant, args.start, args.end);
            }
        };
    }

    @Nullable
    @Override
    public Ranking reduce(List<ComputeJobResult> results) throws IgniteException {

        List<UnavailabilityByRestaurant> unavailabilityByRestaurants = results
                .stream()
                .map(computeJobResult -> (UnavailabilityByRestaurant) computeJobResult.getData())
                .sorted(Comparator.comparing(UnavailabilityByRestaurant::getOfflineTimeInSeconds, Comparator.reverseOrder()))
                .collect(Collectors.toList());

        Ranking ranking = new Ranking(args.start, args.end, unavailabilityByRestaurants);

        return ranking;
    }

}
