package com.projects.tennisscoreboard.repository;

import com.projects.tennisscoreboard.entity.Match;
import com.projects.tennisscoreboard.exception.DatabaseException;
import com.projects.tennisscoreboard.utils.PaginationUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class MatchRepository extends BaseRepository<Long, Match> {

    private static final MatchRepository INSTANCE = new MatchRepository();

    public MatchRepository() {
        super(Match.class);
    }

    public List<Match> findAllByPlayerName(String name, Integer page) {
        var offset = (page - 1) * PaginationUtil.RECORDS_PER_PAGE;

        try {
            log.info("Search for all entities of type {} by player name {} for page {}", this.getClass(), name, page);
            var session = sessionFactory.getCurrentSession();
            return session.createQuery("select m from Match m " +
                                       "inner join Player p on m.firstPlayer.id = p.id " +
                                       "or m.secondPlayer.id = p.id where p.name = :name", Match.class)
                    .setParameter("name", name)
                    .setFirstResult(offset)
                    .setMaxResults(PaginationUtil.RECORDS_PER_PAGE)
                    .getResultList();
        } catch (RuntimeException e) {
            log.error("Error when searching for for all entities of type {} by player name {} for page {}",
                    this.getClass(), name, page);
            throw new DatabaseException("Database error.");
        }
    }

    public Long countAllByPlayerName(String name) {
        try {
            var session = sessionFactory.getCurrentSession();
            return session.createQuery("select count(m.id) from Match m " +
                                       "inner join Player p on m.firstPlayer.id = p.id " +
                                       "or m.secondPlayer.id = p.id where p.name = :name", Long.class)
                    .setParameter("name", name)
                    .uniqueResult();
        } catch (RuntimeException e) {
            log.error("Error during the counting of all records of type {} by player name {}", this.getClass(), name);
            throw new DatabaseException("Database error.");
        }
    }

    public Long countAll() {
        try {
            var session = sessionFactory.getCurrentSession();
            return session.createQuery("select count(*) from Match m", Long.class)
                    .uniqueResult();
        } catch (RuntimeException e) {
            log.error("Error during the counting of all records of type {}", this.getClass());
            throw new DatabaseException("Database error.");
        }
    }

    public static MatchRepository getInstance() {
        return INSTANCE;
    }
}
