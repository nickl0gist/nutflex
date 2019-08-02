package pl.sda.nutflex.service;

import pl.sda.nutflex.domain.Customer;
import pl.sda.nutflex.domain.Movie;
import pl.sda.nutflex.domain.Ranks;
import pl.sda.nutflex.domain.Rent;

import java.util.List;

public interface RankService {

    void rankMovie(Customer customer, Movie movie, Rent rent, Double score, String opinion);

    List<Ranks> getRanksByMovie(Movie movie);

    List<Ranks> getRanksByCustomer(Customer customer);

}
