package pl.sda.nutflex.domain;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.testng.annotations.Test;
import pl.sda.nutflex.util.SessionUtil;

import javax.validation.ConstraintViolationException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

public class MovieTest {

    @Test
    public void cascade_operations() {
        //TESTING CASCADE PERSISTING
        Long movieId, copyId1, copyId2;
        Movie movie;
        Copy copy1, copy2;

        try(Session session = SessionUtil.getSession()) {
            Transaction tx = session.beginTransaction();

            movie = new Movie();
            movie.setTitle("Smierc w Wenecji");
            movie.setGenre(MovieGenre.ACTION);
            movie.setReleaseDate(LocalDate.of(2001, 4, 1));

            copy1 = new Copy();
            copy2 = new Copy();

            //Copy zarzadza relacja z Movie
            copy1.setMovie(movie);
            copy2.setMovie(movie);

            //kaskadowe zapisanie filmu przy zapisie kopii
            //for the save() operation to be cascaded, you need to enable CascadeType.SAVE_UPDATE
            //czyli: @Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
            //session.save(movie);
            session.persist(copy1);
            session.persist(copy2);

            //po zapisie do bazy, ID zostaje nadane
            copyId1 = copy1.getCopyId();
            copyId2 = copy2.getCopyId();

            //jezeli zapisano kaskadowo, powinno zostac nadane ID dla filmu
            movieId = movie.getMovieId();

            tx.commit();
        }

        //odswiezenie obiektu JVM danymi z bazy
        //baza przechowuje FK z copy na movie
        //i relacja jest odwzorowana na obiekty JVM
        try(Session session = SessionUtil.getSession()) {
            //#get zwraca null jesli nie znaleziono rekordu
            movie = session.get(Movie.class, movieId);
            copy1 = session.get(Copy.class, copyId1);
            copy2 = session.get(Copy.class, copyId2);
        }

        assertNotNull(movie);
        //nie ustawilismy listy copies na movie explicite
        //zostalo to zarzadzone przez relacje Hibernate
        assertNotNull(movie.getCopies());
        //assertEquals(movie.getCopies().size(), 2);
        //assertEquals(movie.getCopies().get(0).getCopyId(), copyId1.longValue());
        //assertEquals(movie.getCopies().get(1).getCopyId(), copyId2.longValue());
        assertNotNull(copy1.getMovie());
        assertNotNull(copy2.getMovie());

        //TESTING ORPHAN REMOVAL
        try(Session session = SessionUtil.getSession()) {
            Transaction tx = session.beginTransaction();
            movie = session.load(Movie.class, movieId);
            assertEquals(movie.getCopies().size(), 2);
            session.delete(movie);

            tx.commit();
        }
        try(Session session = SessionUtil.getSession()) {
            Query<Copy> query = session.createQuery("from Copy c", Copy.class);
            List<Copy> copies = query.list();
            assertEquals(copies.size(), 0);
        }
    }

    @Test
    public void init_days_from_release_on_load() {
        Long movieId;
        Movie movie;

        try(Session session = SessionUtil.getSession()) {
            Transaction tx = session.beginTransaction();

            movie = new Movie();
            movie.setTitle("Smierc w Wenecji");
            movie.setGenre(MovieGenre.ACTION);
            movie.setReleaseDate(LocalDate.of(2001, 4, 1));

            session.save(movie);
            movieId = movie.getMovieId();
            tx.commit();
        }

        try(Session session = SessionUtil.getSession()) {
            movie = session.get(Movie.class, movieId);
            assertNotNull(movie);
            assertNotNull(movie.getDaysFromRelease());
            assertEquals(movie.getDaysFromRelease(),
                    ChronoUnit.DAYS.between(movie.getReleaseDate(), LocalDate.now()));
        }
    }

    @Test (expectedExceptions = ConstraintViolationException.class)
    public void test_short_description(){
        Long movieId, copyId1, copyId2;
        Movie movie;
        try(Session session = SessionUtil.getSession()) {
            Transaction tx = session.beginTransaction();
            movie = new Movie();
            movie.setTitle("Smierc w Wenecji");
            movie.setGenre(MovieGenre.ACTION);
            movie.setReleaseDate(LocalDate.of(2001, 4, 1));
            movie.setDescription("0215487");
            session.persist(movie);
            tx.commit();
        }
    }

    @Test (expectedExceptions = ConstraintViolationException.class)
    public void test_long_title(){
        Long movieId, copyId1, copyId2;
        Movie movie;
        try(Session session = SessionUtil.getSession()) {
            Transaction tx = session.beginTransaction();
            movie = new Movie();
            movie.setTitle("Smierc w Wenecji 0215487dpiaosivboiecbaoiebcaodbcpxjbcaposbcaosicb;ajxbckzlxncbzkxbckasjdbcksdbvkfvbkfjf");
            movie.setGenre(MovieGenre.ACTION);
            movie.setReleaseDate(LocalDate.of(2001, 4, 1));
            //movie.setDescription("0215487dpiaosivboiecbaoiebcaodbcpxjbcaposbcaosicb;ajxbckzlxncbzkxbckasjdbcksdbvkfvbkfjf");
            session.persist(movie);
            tx.commit();
        }
    }

    @Test //(expectedExceptions = ConstraintViolationException.class)
    public void test_wrong_score(){
        Long movieId, copyId1, copyId2;
        Movie movie;
        try(Session session = SessionUtil.getSession()) {
            Transaction tx = session.beginTransaction();
            movie = new Movie();
            movie.setTitle("Smierc w Wenecji");
            movie.setGenre(MovieGenre.ACTION);
            movie.setReleaseDate(LocalDate.of(2001, 4, 1));
            movie.setDescription("12345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890");
            session.persist(movie);
            tx.commit();
        }
    }

    @Test (expectedExceptions = ConstraintViolationException.class)
    public void test_my_annotation_with_wrong_date(){
        Long movieId, copyId1, copyId2;
        Movie movie;
        try(Session session = SessionUtil.getSession()) {
            Transaction tx = session.beginTransaction();
            movie = new Movie();
            movie.setTitle("Smierc w Wenecji");
            movie.setGenre(MovieGenre.ACTION);
            movie.setReleaseDate(LocalDate.of(1996, 12, 19));
            movie.setDescription("02154870215487dpiaosivboiecbaoiebcaodbcpxjbcaposbcaosicb0215487dpiaosivboiecbaoiebcaodbcpxjbcaposbcaosicb0215487dpiaosivboiecbaoiebcaodbcpxjbcaposbcaosicb");
            session.persist(movie);
            tx.commit();
        }
    }

    @Test
    public void test_my_annotation_with_good_date(){
        Long movieId, copyId1, copyId2;
        Movie movie;
        try(Session session = SessionUtil.getSession()) {
            Transaction tx = session.beginTransaction();
            movie = new Movie();
            movie.setTitle("Smierc w Wenecji");
            movie.setGenre(MovieGenre.ACTION);
            movie.setReleaseDate(LocalDate.of(1996, 12, 21));
            movie.setDescription("02154870215487dpiaosivboiecbaoiebcaodbcpxjbcaposbcaosicb0215487dpiaosivboiecbaoiebcaodbcpxjbcaposbcaosicb0215487dpiaosivboiecbaoiebcaodbcpxjbcaposbcaosicb");
            session.persist(movie);
            tx.commit();
        }
    }
}
