package com.projects.tennisscoreboard.service;

import com.projects.tennisscoreboard.utils.PropertiesUtil;
import com.projects.tennisscoreboard.dto.match.completed.MatchReadDto;
import com.projects.tennisscoreboard.dto.match.ongoing.OngoingMatchReadDto;
import com.projects.tennisscoreboard.mapper.match.MatchReadMapper;
import com.projects.tennisscoreboard.mapper.match.OngoingMatchMapper;
import com.projects.tennisscoreboard.repository.MatchRepository;

import java.util.List;

public class FinishedMatchesPersistenceService {

    private final MatchRepository matchRepository;
    private final OngoingMatchMapper ongoingMatchMapper;
    private final MatchReadMapper matchReadMapper;
    private static final FinishedMatchesPersistenceService INSTANCE = new FinishedMatchesPersistenceService();
    private static final Integer RECORDS_PER_PAGES = Integer.parseInt(PropertiesUtil.get("pagination.matches_per_page"));

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

    public List<MatchReadDto> findMatches(String playerName, Long page) {
        var skipElements = RECORDS_PER_PAGES * (page - 1);
        var matches = isPlayerNameSpecified(playerName)
                ? matchRepository.findAllByPlayerName(playerName)
                : matchRepository.findAll();
        return matches.stream()
                .skip(skipElements)
                .limit(RECORDS_PER_PAGES)
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
        return (numberOfRecords + RECORDS_PER_PAGES - 1) / RECORDS_PER_PAGES;
    }

    private boolean isPlayerNameSpecified(String name) {
        return name != null && !name.trim().isEmpty();
    }

    public static FinishedMatchesPersistenceService getInstance() {
        return INSTANCE;
    }
}
