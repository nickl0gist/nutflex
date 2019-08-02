package pl.sda.nutflex.service;

import pl.sda.nutflex.domain.Movie;
import pl.sda.nutflex.domain.MovieGenre;

import java.time.LocalDate;

public interface MovieServiceRefactored {

    /**
     * Finds movie with given title in database or returns null.
     * @param title
     * @return
     */
    Movie findMovie(String title);

    /**
     * Finds movie with given title in database or creates new movie with given parameters and returns it.
     * @param title
     * @param genre
     * @param releaseDate
     * @return
     */
    Movie findOrCreateMovie(String title, MovieGenre genre, LocalDate releaseDate);

    /**
     * Creates movie with given parameters and returns it.
     * @param title
     * @param genre
     * @param releaseDate
     * @param description
     * @return
     */
    Movie createMovie(String title, MovieGenre genre, LocalDate releaseDate, String description);

    /**
     * Updates rentedTimes and/or avgScore of movie with given title or returns null if no such movie exists in a database.
     * @param title
     * @param rentedTimes
     * @param avgScore
     * @return
     */
    Movie updateMovie(String title, int rentedTimes, double avgScore);

    /**
     * Removes movie with given title from database and returns 'removed' entity or returns null if no such movie exists in a database.
     * @param title
     * @return
     */
    Movie removeMovie(String title);

}
