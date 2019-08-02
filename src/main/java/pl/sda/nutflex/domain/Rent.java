package pl.sda.nutflex.domain;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "rents")
public class Rent {

    @Id
    @GeneratedValue
    Long rentId;

    @Enumerated
    @Column(nullable = false)
    RentStatus status = RentStatus.IN_RENT;

    @Column(nullable = false)
    BigDecimal rentPricePerDay;

    @Column(nullable = false)
    LocalDate borrowedDate;

    LocalDate returnedDate;

    BigDecimal total;

    @ManyToOne
    Copy copy;

    @OneToOne
    Ranks ranks;

    @ManyToOne
    Customer customer;

    public Long getRentId() {
        return rentId;
    }

    public RentStatus getStatus() {
        return status;
    }

    public void setStatus(RentStatus status) {
        this.status = status;
    }

    public BigDecimal getRentPricePerDay() {
        return rentPricePerDay;
    }

    public void setRentPricePerDay(BigDecimal rentPricePerDay) {
        this.rentPricePerDay = rentPricePerDay;
    }

    public LocalDate getBorrowedDate() {
        return borrowedDate;
    }

    public void setBorrowedDate(LocalDate borrowedDate) {
        this.borrowedDate = borrowedDate;
    }

    public LocalDate getReturnedDate() {
        return returnedDate;
    }

    public void setReturnedDate(LocalDate returnedDate) {
        this.returnedDate = returnedDate;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public Copy getCopy() {
        return copy;
    }

    public void setCopy(Copy copy) {
        this.copy = copy;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Ranks getRanks() {
        return ranks;
    }

    public void setRanks(Ranks ranks) {
        this.ranks = ranks;
    }

    @Override
    public String toString() {
        return "Rent{" +
                "status=" + status +
                ", borrowedDate=" + borrowedDate +
                ", copy=" + copy +
                ", ranks=" + ranks +
                '}';
    }
}
