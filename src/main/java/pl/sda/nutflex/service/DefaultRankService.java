package pl.sda.nutflex.service;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import pl.sda.nutflex.domain.Customer;
import pl.sda.nutflex.domain.Movie;
import pl.sda.nutflex.domain.Ranks;
import pl.sda.nutflex.domain.Rent;
import pl.sda.nutflex.util.SessionUtil;

import java.util.List;

public class DefaultRankService implements RankService {

    @Override
    public void rankMovie(Movie movie, Rent rent, Double score, String opinion) {


            Ranks rank = new Ranks();
            rank.setOpinion(opinion);
            rank.setScore(score);
            rank.setRent(rent);
        try (Session session = SessionUtil.getSession()) {
            Transaction tx = session.beginTransaction();
            Query<Object[]> query = session.createQuery("from Rent r join r.copy c join c.movie where r.ranks = null and c.movie.movieId=:movieId");
            query.setParameter("movieId", movie.getMovieId());
            List<?> list = query.list();
            if(list.isEmpty()){
                return;
            }

            Object[] row = query.list().get(0);
            movie = (Movie) row[2];
            rent = (Rent) row[0];
            rent.setRanks(rank);
            movie.setAvgScore((movie.getAvgScore() * movie.getRentedTimes() + score) / (1 + movie.getRentedTimes()));
            movie.setRentedTimes(movie.getRentedTimes() + 1);
            session.save(rank);
            session.update(rent);
            session.update(movie);

            tx.commit();
        }

    }

    @Override
    public List<Ranks> getRanksByMovie(Movie movie) {
        return null;
    }

    @Override
    public List<Ranks> getRanksByCustomer(Customer customer) {
        return null;
    }
}
