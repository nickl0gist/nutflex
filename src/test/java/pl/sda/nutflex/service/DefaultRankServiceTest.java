package pl.sda.nutflex.service;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;
import pl.sda.nutflex.domain.*;
import pl.sda.nutflex.util.SessionUtil;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

import static org.testng.Assert.assertEquals;

public class DefaultRankServiceTest {

    DefaultRankService defaultRankService = new DefaultRankService();
    DefaultRentService defaultRentService = new DefaultRentService();
    DefaultMovieServiceRefactored movieService = new DefaultMovieServiceRefactored();

    Customer customer = new Customer();
    Movie movie1 = new Movie();
    Rent rent = new Rent();
    Copy copy1 = new Copy();
    Copy copy2 = new Copy();


    @BeforeSuite
    public void addMovies() {

        movie1.setTitle("Matrix");

        String desc = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, " +
                "sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. " +
                "Dolor sed viverra ipsum nunc aliquet bibendum enim. Pellentesque id " +
                "nibh tortor id aliquet. Morbi tristique senectus et netus. A diam " +
                "maecenas sed enim ut. Id volutpat lacus laoreet non curabitur gravida " +
                "arcu. Interdum posuere lorem ipsum dolor sit. Morbi non arcu risus quis " +
                "varius quam quisque id diam. Felis donec et odio pellentesque diam. Eget " +
                "gravida cum sociis natoque penatibus et magnis dis. Massa tincidunt nunc " +
                "pulvinar sapien et ligula ullamcorper.";

        movie1.setDescription(desc);
        movie1.setGenre(MovieGenre.ACTION);
        movie1.setAvgScore(5.0);
        movie1.setRentedTimes(3);
        movie1.setReleaseDate(LocalDate.of(2001, 4, 1));

        copy1.setMovie(movie1);
        copy2.setMovie(movie1);

        customer.setFullName("PW");
        customer.setPhone("+48 777 77 77");

        rent.setRentPricePerDay(BigDecimal.valueOf(5));
        rent.setBorrowedDate(LocalDate.now());
        rent.setCopy(copy1);
        rent.setCustomer(customer);
        copy1.setRented(true);


        try (Session session = SessionUtil.getSession()) {
            Transaction tx = session.beginTransaction();
            session.persist(customer);
            session.persist(copy1);
            session.persist(copy2);
            session.persist(rent);
            tx.commit();
        }
    }

    @Test
    void rank_Movie_test() {
        defaultRankService.rankMovie(customer, movie1, rent, 10.0, "bla bla bla");
        Map<Movie, Rent> map = defaultRentService.getNotYetRatedMovies(customer);
        Movie testMovie = movieService.findMovie("Matrix");
        assertEquals(map.size(), 0);
        assertEquals(testMovie.getAvgScore(), 6.25);

    }
}
