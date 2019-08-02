package pl.sda.nutflex.listener;

import pl.sda.nutflex.domain.Movie;

import javax.persistence.PostLoad;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class MovieEntityListener {
    @PostLoad
    public void calculateDaysFromRelease(Object o) {
        if(o instanceof Movie) {
            Movie movie = (Movie) o;
            movie.setDaysFromRelease((int) ChronoUnit.DAYS.between(movie.getReleaseDate(), LocalDate.now()));
        }
    }
}
