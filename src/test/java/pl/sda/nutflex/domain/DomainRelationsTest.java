package pl.sda.nutflex.domain;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.testng.annotations.Test;
import pl.sda.nutflex.util.SessionUtil;

import java.time.LocalDate;

import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;

public class DomainRelationsTest {

    @Test
    public void testManagedRelationship() {
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

            session.save(movie);
            session.save(copy1);
            session.save(copy2);

            //po zapisie do bazy, ID zostaje nadane
            movieId = movie.getMovieId();
            copyId1 = copy1.getCopyId();
            copyId2 = copy2.getCopyId();

            tx.commit();
        }

        //relacje na obiekcie JVM tak jak ustawione wyzej
        assertNull(movie.getCopies());
        assertNotNull(copy1.getMovie());
        assertNotNull(copy2.getMovie());

        //odswiezenie obiektu JVM danymi z bazy
        //baza przechowuje FK z copy na movie
        //i relacja jest odwzorowana na obiekty JVM
        try(Session session = SessionUtil.getSession()) {
            movie = session.get(Movie.class, movieId);
            copy1 = session.get(Copy.class, copyId1);
            copy2 = session.get(Copy.class, copyId2);
        }

        //nie ustawilismy listy copies na movie explicite
        //zostalo to zarzadzone przez relacje Hibernate
        assertNotNull(movie.getCopies());
        //assertEquals(movie.getCopies().size(), 2);
        //assertEquals(movie.getCopies().get(0).getCopyId(), copyId1.longValue());
        //assertEquals(movie.getCopies().get(1).getCopyId(), copyId2.longValue());
        assertNotNull(copy1.getMovie());
        assertNotNull(copy2.getMovie());
    }

}
