package com.projects.tennisscoreboard.repository;

import com.projects.tennisscoreboard.entity.Match;

import java.util.List;

public class MatchRepository extends BaseRepository<Long, Match> {

    private static final MatchRepository INSTANCE = new MatchRepository();

    public MatchRepository() {
        super(Match.class);
    }

    public List<Match> findAllByPlayerName(String name) {
        try (var session = sessionFactory.openSession()) {
            return session.createQuery("select m from Match m inner " +
                                       "join Player p on m.firstPlayer.id = p.id " +
                                       "or m.secondPlayer.id = p.id where p.name = :name", Match.class)
                    .setParameter("name", name)
                    .getResultList();
        }
    }

    public static MatchRepository getInstance() {
        return INSTANCE;
    }
}
