package com.projects.tennisscoreboard.service;

import com.projects.tennisscoreboard.Utils.PropertiesUtil;
import com.projects.tennisscoreboard.dto.match.completed.MatchReadDto;
import com.projects.tennisscoreboard.dto.match.ongoing.OngoingMatchReadDto;
import com.projects.tennisscoreboard.mapper.MatchReadMapper;
import com.projects.tennisscoreboard.mapper.OngoingMatchMapper;
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

    public List<MatchReadDto> findAllByPlayerName(String name, Integer page) {
        var skipElements = RECORDS_PER_PAGES * (page - 1);
        return matchRepository.findAllByPlayerName(name).stream()
                .skip(skipElements)
                .limit(RECORDS_PER_PAGES)
                .map(matchReadMapper::mapFrom)
                .toList();
    }

    public List<MatchReadDto> findAllMatches(Integer page) {
        var skipElements = RECORDS_PER_PAGES * (page - 1);
        return matchRepository.findAll().stream()
                .skip(skipElements)
                .limit(RECORDS_PER_PAGES)
                .map(matchReadMapper::mapFrom)
                .toList();
    }
    
    public Long getTotalNumberOfPagesByName(String name) {
        var numberOfRecords = matchRepository.countAllByPlayerName(name);
        return calculateTotalPages(numberOfRecords);
    }

    public Long getTotalNumberOfPages() {
        var numberOfRecords = matchRepository.countAll();
        return calculateTotalPages(numberOfRecords);
    }

    private Long calculateTotalPages(Long numberOfRecords) {
        return (numberOfRecords + RECORDS_PER_PAGES - 1) / RECORDS_PER_PAGES;
    }

    public static FinishedMatchesPersistenceService getInstance() {
        return INSTANCE;
    }
}
