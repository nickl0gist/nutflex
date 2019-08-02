package pl.sda.nutflex.domain;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity //default: Entity.name = Customer, Table.name = Customer
@Table(name = "customers")  //Table.name = customers
public class Customer {

    @Id //PK
    @GeneratedValue
    Long customerId;

    @Column(nullable = false)
    String fullName;

    //+48 777 777 777
    @Column(nullable = false, length = 15)
    String phone;

    @Column(length = 510)
    String address;

    @OneToMany(mappedBy="customer")
    List<Rent> rents;

    public Long getCustomerId() {
        return customerId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<Rent> getRents() {
        return rents;
    }

    public void setRents(List<Rent> rents) {
        this.rents = rents;
    }

    //przy implementacji equals dla encji Hibernate korzystamy z getterow a nie pol bezposrednio

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Customer customer = (Customer) o;
        return Objects.equals(getCustomerId(), customer.getCustomerId()) &&
                Objects.equals(getFullName(), customer.getFullName()) &&
                Objects.equals(getPhone(), customer.getPhone()) &&
                Objects.equals(getAddress(), customer.getAddress());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCustomerId(), getFullName(), getPhone(), getAddress());
    }
}
