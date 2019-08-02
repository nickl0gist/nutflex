package pl.sda.nutflex.service;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import pl.sda.nutflex.domain.Movie;
import pl.sda.nutflex.domain.MovieGenre;
import pl.sda.nutflex.util.SessionUtil;

import java.time.LocalDate;

public class DefaultMovieServiceRefactored implements MovieServiceRefactored {

    @Override
    public Movie findMovie(String title) {
        try(Session session = SessionUtil.getSession()) {
            Query<Movie> query = session.createQuery("from Movie m where m.title=:title", Movie.class);
            query.setParameter("title", title);
            return query.uniqueResult();
        }
    }

    @Override
    public Movie findOrCreateMovie(String title, MovieGenre genre, LocalDate releaseDate) {
        Movie movie = findMovie(title);
        if(movie == null) {
            movie = createMovie(title, genre, releaseDate, null);
        }
        return movie;
    }

    @Override
    public Movie createMovie(String title, MovieGenre genre, LocalDate releaseDate, String description) {
        Movie movie = new Movie();
        movie.setTitle(title);
        movie.setGenre(genre);
        movie.setReleaseDate(releaseDate);
        movie.setDescription(description);

        try(Session session = SessionUtil.getSession()) {
            Transaction tx = session.beginTransaction();
            session.persist(movie);
            tx.commit();
        }

        return movie;
    }

    @Override
    public Movie updateMovie(String title, int rentedTimes, double avgScore) {
        Movie movie = findMovie(title);
        //movie is detached from session here
        if(movie == null)
            return null;

        try(Session session = SessionUtil.getSession()) {

            Transaction tx = session.beginTransaction();
            movie.setRentedTimes(rentedTimes);
            movie.setAvgScore(avgScore);
            session.saveOrUpdate(movie);    //re-attach detached object to current session
            tx.commit();

            return movie;
        }
    }

    @Override
    public Movie removeMovie(String title) {
        Movie movie = findMovie(title);

        if(movie == null)
            return null;

        try(Session session = SessionUtil.getSession()) {
            Transaction tx = session.beginTransaction();
            session.delete(movie);
            tx.commit();

            return movie;
        }
    }
}
