package pl.sda.nutflex.domain;

import pl.sda.nutflex.listener.MovieEntityListener;

import javax.persistence.*;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;

@Entity
@EntityListeners({MovieEntityListener.class})
@Table(name = "movies")
@ReleaseDateValidation
public class Movie {

    @Id
    @GeneratedValue
    Long movieId;

    @Column(nullable = false)
    @Size(max = 50)
    String title;

    @Enumerated //by default works on ordinal
    @Column(nullable = false)
    MovieGenre genre;


    @Column(nullable = false)
    LocalDate releaseDate;  //potencjalny problem z mapowaniem

    @Column
    @Size(min = 100, max = 1000)
    String description;

    @Column(nullable = false, columnDefinition = "integer default 0")
    int rentedTimes;

    @DecimalMax("10.0")
    @DecimalMin("0.0")
    double avgScore;

    @OneToMany(orphanRemoval = true, mappedBy = "movie")
    List<Copy> copies;

    @Transient
    int daysFromRelease;

    public Long getMovieId() {
        return movieId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public MovieGenre getGenre() {
        return genre;
    }

    public void setGenre(MovieGenre genre) {
        this.genre = genre;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getRentedTimes() {
        return rentedTimes;
    }

    public void setRentedTimes(int rentedTimes) {
        this.rentedTimes = rentedTimes;
    }

    public double getAvgScore() {
        return avgScore;
    }

    public void setAvgScore(double avgScore) {
        this.avgScore = avgScore;
    }

    public List<Copy> getCopies() {
        return copies;
    }

    public void setCopies(List<Copy> copies) {
        this.copies = copies;
    }

    public int getDaysFromRelease() {
        return daysFromRelease;
    }

    public void setDaysFromRelease(int daysFromRelease) {
        this.daysFromRelease = daysFromRelease;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "title='" + title + '\'' +
                ", avgScore=" + avgScore +
                '}';
    }
}
