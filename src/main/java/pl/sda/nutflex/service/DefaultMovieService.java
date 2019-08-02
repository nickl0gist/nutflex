package pl.sda.nutflex.service;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import pl.sda.nutflex.domain.Movie;
import pl.sda.nutflex.domain.MovieGenre;

import java.time.LocalDate;

public class DefaultMovieService implements MovieService {

    @Override
    public Movie findMovie(String title, Session session) {
        Query<Movie> query = session.createQuery("from Movie m where m.title=:title", Movie.class);
        query.setParameter("title", title);
        return query.uniqueResult();
    }

    @Override
    public Movie findOrCreateMovie(String title, MovieGenre genre, LocalDate releaseDate, Session session) {
        Movie movie = findMovie(title, session);
        if(movie == null) {
            movie = createMovie(title, genre, releaseDate, null, session);
        }
        return movie;
    }

    @Override
    public Movie createMovie(String title, MovieGenre genre, LocalDate releaseDate, String description, Session session) {
        Movie movie = new Movie();
        movie.setTitle(title);
        movie.setGenre(genre);
        movie.setReleaseDate(releaseDate);
        movie.setDescription(description);
        Transaction tx = session.beginTransaction();
        session.persist(movie);
        tx.commit();
        return movie;
    }

    @Override
    public Movie updateMovie(String title, int rentedTimes, double avgScore, Session session) {
        Movie movie = findMovie(title, session);

        if(movie == null)
            return null;

        Transaction tx = session.beginTransaction();
        movie.setRentedTimes(rentedTimes);
        movie.setAvgScore(avgScore);
        tx.commit();
        return movie;
    }

    @Override
    public Movie removeMovie(String title, Session session) {
        Movie movie = findMovie(title, session);

        if(movie == null)
            return null;

        Transaction tx = session.beginTransaction();
        session.delete(movie);
        tx.commit();
        //zwracamy obiekt JVM - encja Hibernate w stanie 'removed'
        return movie;
    }
}
