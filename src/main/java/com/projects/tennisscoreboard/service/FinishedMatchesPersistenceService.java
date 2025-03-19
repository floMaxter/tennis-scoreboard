package com.projects.tennisscoreboard.service;

import com.projects.tennisscoreboard.dto.match.completed.MatchReadDto;
import com.projects.tennisscoreboard.dto.match.ongoing.OngoingMatchReadDto;
import com.projects.tennisscoreboard.mapper.MatchReadMapper;
import com.projects.tennisscoreboard.mapper.OngoingMatchMapper;
import com.projects.tennisscoreboard.repository.MatchRepository;

public class FinishedMatchesPersistenceService {

    private final MatchRepository matchRepository;
    private final OngoingMatchMapper ongoingMatchMapper;
    private final MatchReadMapper matchReadMapper;
    private static final FinishedMatchesPersistenceService INSTANCE = new FinishedMatchesPersistenceService();

    private FinishedMatchesPersistenceService() {
        matchRepository = MatchRepository.getInstance();
        ongoingMatchMapper = OngoingMatchMapper.getInstance();
        matchReadMapper = MatchReadMapper.getInstance();
    }

    public MatchReadDto save(OngoingMatchReadDto completedMatch) {
        var match = ongoingMatchMapper.mapFrom(completedMatch);
        var savedMatch = matchRepository.save(match);
        return matchReadMapper.mapFrom(savedMatch);
    }

    public static FinishedMatchesPersistenceService getInstance() {
        return INSTANCE;
    }
}
