package com.projects.tennisscoreboard.service;

import com.projects.tennisscoreboard.dto.match.completed.MatchReadDto;
import com.projects.tennisscoreboard.dto.match.ongoing.OngoingMatchDto;
import com.projects.tennisscoreboard.mapper.match.MatchReadMapper;
import com.projects.tennisscoreboard.mapper.match.OngoingMatchMapper;
import com.projects.tennisscoreboard.repository.MatchRepository;
import com.projects.tennisscoreboard.utils.PaginationUtil;

import java.util.List;

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

    public MatchReadDto save(OngoingMatchDto completedMatch) {
        var match = ongoingMatchMapper.mapFrom(completedMatch);
        var savedMatch = matchRepository.save(match);
        return matchReadMapper.mapFrom(savedMatch);
    }

    public List<MatchReadDto> findMatches(String playerName, Integer page) {
        var matches = isPlayerNameSpecified(playerName)
                ? matchRepository.findAllByPlayerName(playerName, page)
                : matchRepository.findAll(page);

        return matches.stream()
                .map(matchReadMapper::mapFrom)
                .toList();
    }

    public Long getTotalPages(String playerName) {
        var totalPages = isPlayerNameSpecified(playerName)
                ? matchRepository.countAllByPlayerName(playerName)
                : matchRepository.countAll();
        return calculateTotalPages(totalPages);
    }

    private Long calculateTotalPages(Long numberOfRecords) {
        return (numberOfRecords + PaginationUtil.RECORDS_PER_PAGE - 1) / PaginationUtil.RECORDS_PER_PAGE;
    }

    private boolean isPlayerNameSpecified(String name) {
        return name != null && !name.trim().isEmpty();
    }

    public static FinishedMatchesPersistenceService getInstance() {
        return INSTANCE;
    }
}
