package com.projects.tennisscoreboard.repository;

import com.projects.tennisscoreboard.entity.Match;
import jakarta.persistence.EntityManager;

public class MatchRepository extends BaseRepository<Long, Match> {

    public MatchRepository() {
        super(Match.class);
    }
}
