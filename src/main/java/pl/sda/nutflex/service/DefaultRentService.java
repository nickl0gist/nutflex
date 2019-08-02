package pl.sda.nutflex.service;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import pl.sda.nutflex.domain.*;
import pl.sda.nutflex.util.SessionUtil;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.time.temporal.ChronoUnit.DAYS;

public class DefaultRentService implements RentService {

    private final BigDecimal cost = new BigDecimal(5);

    public List<Copy> getAvailableCopies() {
        try (Session session = SessionUtil.getSession()) {
            Query<Copy> query = session.createQuery("from Copy c where c.isRented=false", Copy.class);
            return query.getResultList();
        }
    }

    @Override
    public void rentMovie(Customer customer, Copy copy) {
        try (Session session = SessionUtil.getSession()) {
            Transaction tx = session.beginTransaction();
            Rent rent = new Rent();
            rent.setRentPricePerDay(cost);
            rent.setBorrowedDate(LocalDate.now());
            rent.setCopy(copy);
            rent.setCustomer(customer);
            copy.setRented(true);
            session.persist(rent);
            session.saveOrUpdate(copy);
            tx.commit();
        }
    }

    @Override
    public void returnMovie(Customer customer, Copy copy) {
        try (Session session = SessionUtil.getSession()) {
            Transaction tx = session.beginTransaction();
            Query<Customer> query = session.createQuery("from Customer where customerId=:id", Customer.class);
            query.setParameter("id", customer.getCustomerId());
            customer = query.list().get(0);

            Rent customerRent = customer.getRents().stream()
                    .filter(r -> r.getStatus().equals(RentStatus.IN_RENT) && r.getCopy().getCopyId() == copy.getCopyId())
                    .findFirst().orElse(new Rent());
            if (customerRent.getRentId() == 0) {
                return;
            }

            customerRent.setReturnedDate(LocalDate.now());
            customerRent.setStatus(RentStatus.RETURNED);
            customerRent.setTotal(customerRent.getRentPricePerDay().multiply(new BigDecimal(DAYS.between(customerRent.getBorrowedDate(), customerRent.getReturnedDate()))));
            customerRent.getCopy().setRented(false);

            session.saveOrUpdate(customerRent);
            tx.commit();
        }
    }

    @Override
    public List<Rent> getRentsByCustomer(Customer customer) {
        List<Object[]> list;
        try (Session session = SessionUtil.getSession()) {
            Query<Object[]> query = session.createQuery("from Rent r left join r.customer where r.customer.customerId=:customerId");
            query.setParameter("customerId", customer.getCustomerId());
            list = query.list();
            return list.stream()
                    .map(o -> (Rent) o[0])
                    .collect(Collectors.toList());
        }
    }

    @Override
    public Map<Movie, Rent> getNotYetRatedMovies(Customer customer) {
        List<Object[]> list;
        Map<Movie, Rent> resultMap = new HashMap<>();
        String hql = "from Rent r " +
                "left join r.copy c " +
                "left join c.movie " +
                "left join r.ranks " +
                "left join r.customer " +
                "where r.ranks=null and r.customer.customerId=:customerId";

        try (Session session = SessionUtil.getSession()) {
            Query<Object[]> query = session.createQuery(hql);
            query.setParameter("customerId", customer.getCustomerId());
            list = query.list();

            list.stream()
                    .forEach(o -> resultMap.put((Movie) o[2], (Rent) o[0]));

            return  resultMap;
        }
    }
}
