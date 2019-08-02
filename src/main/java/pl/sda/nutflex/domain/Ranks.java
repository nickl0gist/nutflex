package pl.sda.nutflex.domain;

import javax.persistence.*;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Size;

@Entity
@Table(name = "ranks")
public class Ranks {

    @Id
    @GeneratedValue
    Long rankId;

    @Column
    @Size(min = 100, max = 1000)
    String opinion;

    @Column(columnDefinition = "double default 0.0")
    @DecimalMax("10.0")
    @DecimalMin("0.0")
    double score;

    @OneToOne(mappedBy = "ranks")
    Rent rent;

    public Long getRankId() {
        return rankId;
    }

    public String getOpinion() {
        return opinion;
    }

    public void setOpinion(String opinion) {
        this.opinion = opinion;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public Rent getRent() {
        return rent;
    }

    public void setRent(Rent rent) {
        this.rent = rent;
    }

    @Override
    public String toString() {
        return "Ranks{" +
                "score=" + score +
                '}';
    }
}
