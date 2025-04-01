package com.projects.tennisscoreboard.repository;

import com.projects.tennisscoreboard.entity.Match;
import com.projects.tennisscoreboard.exception.DatabaseException;

import java.util.List;

public class MatchRepository extends BaseRepository<Long, Match> {

    private static final MatchRepository INSTANCE = new MatchRepository();

    public MatchRepository() {
        super(Match.class);
    }

    public List<Match> findAllByPlayerName(String name) {
        try (var session = sessionFactory.openSession()) {
            return session.createQuery("select m from Match m " +
                                       "inner join Player p on m.firstPlayer.id = p.id " +
                                       "or m.secondPlayer.id = p.id where p.name = :name", Match.class)
                    .setParameter("name", name)
                    .getResultList();
        } catch (RuntimeException e) {
            throw new DatabaseException("Database error.");
        }
    }

    public Long countAllByPlayerName(String name) {
        try (var session = sessionFactory.openSession()) {
            return session.createQuery("select count(m.id) from Match m " +
                                       "inner join Player p on m.firstPlayer.id = p.id " +
                                       "or m.secondPlayer.id = p.id where p.name = :name", Long.class)
                    .setParameter("name", name)
                    .uniqueResult();
        } catch (RuntimeException e) {
            throw new DatabaseException("Database error.");
        }
    }

    public Long countAll() {
        try (var session = sessionFactory.openSession()) {
            return session.createQuery("select count(*) from Match m", Long.class)
                    .uniqueResult();
        } catch (RuntimeException e) {
            throw new DatabaseException("Database error.");
        }
    }

    public static MatchRepository getInstance() {
        return INSTANCE;
    }
}
