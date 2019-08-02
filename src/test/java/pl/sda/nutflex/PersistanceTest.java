package pl.sda.nutflex;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Ignore;
import org.testng.annotations.Test;
import pl.sda.nutflex.domain.Movie;
import pl.sda.nutflex.domain.MovieGenre;
import pl.sda.nutflex.domain.SimpleMovie;

import java.time.LocalDate;
import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

public class PersistanceTest {

    SessionFactory sessionFactory;

    @BeforeSuite
    public void setup() {
        StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure()
                .build();

        sessionFactory = new MetadataSources(registry)
                .buildMetadata()
                .buildSessionFactory();
    }

    @Ignore
    @Test
    public void saveSimpleMovies() {
        SimpleMovie movie = new SimpleMovie("Ogniem i mieczem");
        SimpleMovie movie2 = new SimpleMovie("Planet Earth II");
        try(Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            session.persist(movie);
            session.persist(movie2);
            tx.commit();
        }
    }

    @Ignore
    @Test(dependsOnMethods = "saveSimpleMovies")
    public void readSimpleMovies() {
        try(Session session = sessionFactory.openSession()) {
            List<SimpleMovie> movies = session.createQuery("from SimpleMovie", SimpleMovie.class).list();
            assertEquals(movies.size(), 2);
            for(SimpleMovie movie : movies)
                System.out.println(movie.getTitle());
        }
    }

    //Cwiczenie 7
    @Test
    public void saveMovies() {
        Movie movie = new Movie();
        movie.setTitle("Smierc w Wenecji");
        movie.setDescription("Zajebisty film :) 1234567890 1234567890 1234567890 1234567890 1234567890 1234567890 1234567890 1234567890 1234567890");
        movie.setGenre(MovieGenre.ACTION);
        movie.setReleaseDate(LocalDate.of(2001, 4, 1));

        try(Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            session.persist(movie);
            tx.commit();
        }
    }

    @Test(dependsOnMethods = "saveMovies")
    public void readMovies() {
        try(Session session = sessionFactory.openSession()) {
            List<Movie> movies = session.createQuery("from Movie", Movie.class).list();
            assertEquals(movies.size(), 1);
            Movie movie = movies.get(0);
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

}


