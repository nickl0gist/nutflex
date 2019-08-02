package pl.sda.nutflex.service;

import pl.sda.nutflex.domain.Copy;
import pl.sda.nutflex.domain.Customer;
import pl.sda.nutflex.domain.Movie;
import pl.sda.nutflex.domain.Rent;

import java.util.List;
import java.util.Map;

public interface RentService {

    List<Copy> getAvailableCopies();

    void rentMovie(Customer customer, Copy copy);

    void returnMovie(Customer customer, Copy copy);

    List<Rent> getRentsByCustomer(Customer customer);

    Map<Movie, Rent> getNotYetRatedMovies(Customer customer);

}
