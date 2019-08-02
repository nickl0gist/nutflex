package pl.sda.nutflex.service;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;
import pl.sda.nutflex.domain.Movie;
import pl.sda.nutflex.domain.MovieGenre;

import java.time.LocalDate;

import static org.testng.Assert.*;

public class DefaultMovieServiceTest {

    SessionFactory sessionFactory;
    MovieService movieService = new DefaultMovieService();

    @BeforeSuite
    public void setup() {
        StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure()
                .build();

        sessionFactory = new MetadataSources(registry)
                .buildMetadata()
                .buildSessionFactory();
    }

    //test nr 1
    //findOrCreateMovie
    //otworzyc nowa sesje i odczytac po Id ten film
    //sprawdzacie czy zwrocil poprawny obiekt Movie

    //test nr 2
    //utworzony w test nr 1 film jest juz w bazie
    //findMovie
    //sprawdzacie czy zwrocil poprawny obiekt Movie

    @Test
    public void create_movies_via_movieservice() {
        //test nr 1
        try(Session session = sessionFactory.openSession()) {
            movieService.findOrCreateMovie("Smierc w Wenecji",
                    MovieGenre.ACTION, LocalDate.of(2001, 4, 1),
                    session);
        }
        //test nr 2
        try(Session session = sessionFactory.openSession()) {
            Movie movie = movieService.findMovie("Smierc w Wenecji", session);

            assertNotNull(movie.getMovieId());
            assertEquals(movie.getTitle(), "Smierc w Wenecji");
            assertEquals(movie.getGenre(), MovieGenre.ACTION);
            //test time
            assertEquals(movie.getReleaseDate(), LocalDate.of(2001, 4, 1));
            assertNotNull(movie.getRentedTimes());
            //test default
            assertEquals(movie.getRentedTimes(), 0);
        }
    }

    @Test(dependsOnMethods = "create_movies_via_movieservice")
    public void find_created_movie() {
        try(Session session = sessionFactory.openSession()) {
            Movie movie = movieService.findMovie("Smierc w Wenecji", session);

            assertNotNull(movie.getMovieId());
            assertEquals(movie.getTitle(), "Smierc w Wenecji");
            assertEquals(movie.getGenre(), MovieGenre.ACTION);
            //test time
            assertEquals(movie.getReleaseDate(), LocalDate.of(2001, 4, 1));
            assertNotNull(movie.getRentedTimes());
            //test default
            assertEquals(movie.getRentedTimes(), 0);
        }
    }

    @Test(dependsOnMethods = "create_movies_via_movieservice")
    public void update_movie_stats() {
        //wczytac Smierc w Wenecji
        try(Session session = sessionFactory.openSession()) {
            Movie movie = movieService.findMovie("Smierc w Wenecji", session);
            //sprawdzic, ze ma rentedTimes = 0
            assertEquals(movie.getRentedTimes(), 0);
            //zrobic modyfikacje metoda serwisu obu parametrow
            movieService.updateMovie("Smierc w Wenecji", 5, 8.9, session);
        }
        //wczytac w nowej sesji SwW i sprawdzic wartosci pol
        try(Session session = sessionFactory.openSession()) {
            Movie movie = movieService.findMovie("Smierc w Wenecji", session);
            assertEquals(movie.getRentedTimes(), 5);
            assertEquals(movie.getAvgScore(), 8.9);
        }
    }

    //wykonaj ten test na koniec (inne testy potrzebuja filmu w DB)
    @Test(dependsOnMethods = {"create_movies_via_movieservice", "update_movie_stats"})
    public void remove_movie() {
        try(Session session = sessionFactory.openSession()) {
            Movie movie = movieService.findMovie("Smierc w Wenecji", session);
            assertNotNull(movie);
            movie = movieService.removeMovie("Smierc w Wenecji", session);
            //obiekt JVM nadal istnieje
            assertNotNull(movie);
        }
        try(Session session = sessionFactory.openSession()) {
            Movie movie = movieService.findMovie("Smierc w Wenecji", session);
            assertNull(movie);
        }
    }

}
