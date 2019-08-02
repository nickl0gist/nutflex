package pl.sda.nutflex.domain;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "copies")
public class Copy {

    @Id
    @GeneratedValue
    long copyId;

    @Column(nullable = false, columnDefinition = "boolean default false")
    boolean isRented;

    @ManyToOne(cascade = {CascadeType.PERSIST})
    Movie movie;

    @OneToMany(mappedBy = "copy", cascade ={CascadeType.ALL})
    List<Rent> rent;

    public long getCopyId() {
        return copyId;
    }

    public boolean isRented() {
        return isRented;
    }

    public void setRented(boolean rented) {
        isRented = rented;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public List<Rent> getRent() {
        return rent;
    }

    public void setRent(List<Rent> rent) {
        this.rent = rent;
    }

    @Override
    public String toString() {
        return "Copy{" +
                "copyId=" + copyId +
                '}';
    }
}
