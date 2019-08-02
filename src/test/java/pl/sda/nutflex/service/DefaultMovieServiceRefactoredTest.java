package pl.sda.nutflex.service;

import org.testng.annotations.Test;
import pl.sda.nutflex.domain.Movie;
import pl.sda.nutflex.domain.MovieGenre;

import java.time.LocalDate;

import static org.testng.Assert.*;

public class DefaultMovieServiceRefactoredTest {

    MovieServiceRefactored movieService = new DefaultMovieServiceRefactored();

    @Test
    public void create_movies_via_movieservice() {
        movieService.findOrCreateMovie("Smierc w Wenecji",
                MovieGenre.ACTION,
                LocalDate.of(2001, 4, 1));

        Movie movie = movieService.findMovie("Smierc w Wenecji");
        assertNotNull(movie.getMovieId());
        assertEquals(movie.getTitle(), "Smierc w Wenecji");
        assertEquals(movie.getGenre(), MovieGenre.ACTION);
        assertEquals(movie.getReleaseDate(), LocalDate.of(2001, 4, 1));
        assertNotNull(movie.getRentedTimes());
        assertEquals(movie.getRentedTimes(), 0);
    }

    @Test(dependsOnMethods = "create_movies_via_movieservice")
    public void find_created_movie() {
        Movie movie = movieService.findMovie("Smierc w Wenecji");

        assertNotNull(movie.getMovieId());
        assertEquals(movie.getTitle(), "Smierc w Wenecji");
        assertEquals(movie.getGenre(), MovieGenre.ACTION);
        assertEquals(movie.getReleaseDate(), LocalDate.of(2001, 4, 1));
        assertNotNull(movie.getRentedTimes());
        assertEquals(movie.getRentedTimes(), 0);
    }

    @Test(dependsOnMethods = "create_movies_via_movieservice")
    public void update_movie_stats() {
        Movie movie = movieService.findMovie("Smierc w Wenecji");
        assertEquals(movie.getRentedTimes(), 0);

        movieService.updateMovie("Smierc w Wenecji", 5, 8.9);

        movie = movieService.findMovie("Smierc w Wenecji");
        assertEquals(movie.getRentedTimes(), 5);
        assertEquals(movie.getAvgScore(), 8.9);
    }

    //wykonaj ten test na koniec (inne testy potrzebuja filmu w DB)
    @Test(dependsOnMethods = {"create_movies_via_movieservice", "update_movie_stats"})
    public void remove_movie() {
        Movie movie = movieService.findMovie("Smierc w Wenecji");
        assertNotNull(movie);

        movie = movieService.removeMovie("Smierc w Wenecji");
        //obiekt JVM nadal istnieje
        assertNotNull(movie);

        movie = movieService.findMovie("Smierc w Wenecji");
        assertNull(movie);
    }

}
