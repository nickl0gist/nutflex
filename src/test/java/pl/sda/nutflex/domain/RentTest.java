package pl.sda.nutflex.domain;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.testng.annotations.Test;
import pl.sda.nutflex.util.SessionUtil;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

public class RentTest {

    @Test
    public void check_one_to_one_rel_between_copy_and_rent() {

        Long copyId, rentId, customerId;

        //1-wsze wypozyczenie
        try(Session session = SessionUtil.getSession()) {
            Transaction tx = session.beginTransaction();
            Movie movie = new Movie();
            movie.setTitle("Smierc w Wenecji");
            movie.setGenre(MovieGenre.ACTION);
            movie.setReleaseDate(LocalDate.of(2001, 4, 1));
            session.save(movie);

            Copy copy = new Copy();
            copy.setMovie(movie);
            session.save(copy);
            copyId = copy.getCopyId();
            System.out.println("Copy1 = " + copyId);

            Customer customer = new Customer();
            customer.setFullName("PW");
            customer.setPhone("+48 777 77 77");
            session.save(customer);
            customerId = customer.getCustomerId();
            System.out.println("Customer1 = " + customerId);

            Rent rent1 = new Rent();
            assertEquals(rent1.getStatus(), RentStatus.IN_RENT);
            rent1.setRentPricePerDay(new BigDecimal(5));
            rent1.setBorrowedDate(LocalDate.now());
            rent1.setCopy(copy);
            rent1.setCustomer(customer);
            session.save(rent1);
            rentId = rent1.getRentId();
            System.out.println("Rent1 = " + rentId);
            tx.commit();
        }

        //2-gie wypozyczenie JEST NIEMOZLIWE PRZY RELACJI ONE-TO-ONE
/*        try(Session session = SessionUtil.getSession()) {
            Transaction tx = session.beginTransaction();

            Rent rent2 = new Rent();
            rent2.setRentPricePerDay(new BigDecimal(5));
            rent2.setBorrowedDate(LocalDate.now());
            Copy copy = session.get(Copy.class, copyId);
            assertNotNull(copy);
            rent2.setCopy(copy);
            Customer customer = session.get(Customer.class, customerId);
            assertNotNull(customer);
            rent2.setCustomer(customer);

            session.save(rent2); //???
            System.out.println("Rent2 = " + rent2.getRentId());
            tx.commit();
        }*/

        try(Session session = SessionUtil.getSession()) {
            Copy copy = session.get(Copy.class, copyId);
            assertNotNull(copy);
        }

    }

}
