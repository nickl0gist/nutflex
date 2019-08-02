package pl.sda.nutflex.service;

import pl.sda.nutflex.domain.Customer;
import pl.sda.nutflex.domain.Movie;
import pl.sda.nutflex.domain.Ranks;
import pl.sda.nutflex.domain.Rent;

import java.util.List;

public class DefaultRankService implements RankService {

    @Override
    public void rankMovie(Customer customer, Movie movie, Rent rent, Double score, String opinion) {

    }

    @Override
    public List<Ranks> getRanksByMovie(Movie movie) {
        return null;
    }

    @Override
    public List<Ranks> getRanksByCustomer(Customer customer) {
        return null;
    }
}
