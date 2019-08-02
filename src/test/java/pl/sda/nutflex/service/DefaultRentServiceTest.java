package pl.sda.nutflex.service;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;
import pl.sda.nutflex.domain.*;
import pl.sda.nutflex.util.SessionUtil;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
import static org.testng.AssertJUnit.assertFalse;

public class DefaultRentServiceTest {

    DefaultRentService defaultRentService = new DefaultRentService();
    Customer customer = new Customer();
    Copy copy1 = new Copy();
    Copy copy2 = new Copy();
    Copy copy3 = new Copy();
    Copy copy4 = new Copy();
    Copy copy5 = new Copy();

    @BeforeSuite
    public void addMovies() {

        Movie movie1 = new Movie();
        Movie movie2 = new Movie();
        Movie movie3 = new Movie();

        movie1.setTitle("Matrix");
        movie2.setTitle("Mr. Bin");
        movie3.setTitle("X");

        String desc = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, " +
                "sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. " +
                "Dolor sed viverra ipsum nunc aliquet bibendum enim. Pellentesque id " +
                "nibh tortor id aliquet. Morbi tristique senectus et netus. A diam " +
                "maecenas sed enim ut. Id volutpat lacus laoreet non curabitur gravida " +
                "arcu. Interdum posuere lorem ipsum dolor sit. Morbi non arcu risus quis " +
                "varius quam quisque id diam. Felis donec et odio pellentesque diam. Eget " +
                "gravida cum sociis natoque penatibus et magnis dis. Massa tincidunt nunc " +
                "pulvinar sapien et ligula ullamcorper.";

        movie1.setDescription(desc);
        movie2.setDescription(desc);
        movie3.setDescription(desc);

        movie1.setGenre(MovieGenre.ACTION);
        movie2.setGenre(MovieGenre.ACTION);
        movie3.setGenre(MovieGenre.ACTION);

        movie1.setReleaseDate(LocalDate.of(2001, 4, 1));
        movie2.setReleaseDate(LocalDate.of(2010, 10, 21));
        movie3.setReleaseDate(LocalDate.of(2011, 11, 11));

        copy1.setMovie(movie1);
        copy2.setMovie(movie1);
        copy3.setMovie(movie2);
        copy4.setMovie(movie2);
        copy5.setMovie(movie3);

        customer.setFullName("PW");
        customer.setPhone("+48 777 77 77");

        try (Session session = SessionUtil.getSession()) {
            Transaction tx = session.beginTransaction();
            session.persist(copy1);
            session.persist(copy2);
            session.persist(copy3);
            session.persist(copy4);
            session.persist(copy5);
            session.save(customer);
            tx.commit();
        }
    }

    @Test(dependsOnMethods = {"rent_Movie_test"})
    void get_List_of_available_copies() {
        List<Copy> copies = defaultRentService.getAvailableCopies();
        assertFalse(copies.isEmpty());
        assertEquals(copies.size(), 3);
    }

    @Test
    void rent_Movie_test() {

        List<Rent> rents;
        defaultRentService.rentMovie(customer, copy1);
        defaultRentService.rentMovie(customer, copy5);

        try (Session session = SessionUtil.getSession()) {
            Query<Rent> query = session.createQuery("from Rent r where r.status =:status", Rent.class);
            query.setParameter("status", RentStatus.IN_RENT);
            rents = query.list();
        }
        assertEquals(rents.size(), 2);
    }

    @Test
    void return_Movie_test() {
        Copy testCopy;
        Rent rent = new Rent();
        Long rentDays = 5L;
        BigDecimal cost = new BigDecimal(2.5);

        try (Session session = SessionUtil.getSession()) {
            Transaction tx = session.beginTransaction();
            rent.setRentPricePerDay(cost);
            rent.setBorrowedDate(LocalDate.now().minusDays(rentDays));
            rent.setCopy(copy3);
            rent.setCustomer(customer);
            copy3.setRented(true);
            session.persist(rent);
            session.saveOrUpdate(copy3);
            tx.commit();
        }
        defaultRentService.returnMovie(customer, copy3);

        try (Session session = SessionUtil.getSession()) {
            List<?> list;

            Query<Object[]> query2 = session.createQuery("from Rent r left join r.copy where r.status =:status and r.copy.copyId=:copy");
            query2.setParameter("status", RentStatus.RETURNED);
            query2.setParameter("copy", copy3.getCopyId());
            list = query2.list();
            Object[] row = (Object[]) list.get(0);
            rent = (Rent) row[0];
            testCopy = (Copy) row[1];
        }
        assertFalse(testCopy.isRented());
        assertTrue(rent.getTotal().stripTrailingZeros().equals(BigDecimal.valueOf(12.5)));
    }

    @Test(dependsOnMethods = {"rent_Movie_test", "return_Movie_test"})
    void get_Rents_By_Customer_test() {
        List<Rent> rents;
        rents = defaultRentService.getRentsByCustomer(customer);
        rents.stream().forEach(System.out::println);
        assertEquals(rents.size(), 3);

    }

    @Test(dependsOnMethods = {"get_Rents_By_Customer_test"})
    void get_Not_Yet_Rated_Movies_test() {
        Map<Movie, Rent> map;
        map = defaultRentService.getNotYetRatedMovies(customer);
        map.entrySet().forEach(System.out::println);
        assertEquals(map.size(), 3);
    }
}
