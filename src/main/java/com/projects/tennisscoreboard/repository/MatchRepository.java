package com.projects.tennisscoreboard.repository;

import com.projects.tennisscoreboard.entity.Match;

public class MatchRepository extends BaseRepository<Long, Match> {

    private static final MatchRepository INSTANCE = new MatchRepository();

    public MatchRepository() {
        super(Match.class);
    }

    public static MatchRepository getInstance() {
        return INSTANCE;
    }
}
