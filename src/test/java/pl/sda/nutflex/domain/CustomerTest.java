package pl.sda.nutflex.domain;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.testng.annotations.Test;
import pl.sda.nutflex.util.SessionUtil;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

public class CustomerTest {

    @Test
    public void compare_between_persisted_customers() {
        //persis Customer to DB
        Customer customer = new Customer();
        customer.setFullName("PW");
        customer.setPhone("+48 777 77 77");
        Long customerId;

        try(Session session = SessionUtil.getSession()) {
            Transaction tx = session.beginTransaction();
            session.save(customer);
            customerId = customer.getCustomerId();
            tx.commit();
        }

        //wczytuje pod referencje customer klienta o ID X
        try(Session session = SessionUtil.getSession()) {
            customer = session.get(Customer.class, customerId);
        }

        //wczytuje pod referencje customer2 tego samego klienta o tym samym ID X
        Customer customer2;
        try(Session session = SessionUtil.getSession()) {
            customer2 = session.get(Customer.class, customerId);
        }

        //porownanie przez referencje daje FALSE jakby to byly rozne obiekty
        assertFalse(customer == customer2);

        //porownanie przez equals daje TRUE, bo maja te same pola (dla bazy sa jednym i tym samym rekordem)
        assertTrue(customer.equals(customer2));
    }

}
