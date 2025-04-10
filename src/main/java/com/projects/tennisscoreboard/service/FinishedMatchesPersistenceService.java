package com.projects.tennisscoreboard.service;

import com.projects.tennisscoreboard.dto.match.MatchPageDto;
import com.projects.tennisscoreboard.dto.match.ongoing.OngoingMatchDto;
import com.projects.tennisscoreboard.mapper.match.MatchReadMapper;
import com.projects.tennisscoreboard.mapper.match.OngoingMatchMapper;
import com.projects.tennisscoreboard.repository.MatchRepository;
import com.projects.tennisscoreboard.utils.HibernateUtil;
import com.projects.tennisscoreboard.utils.PaginationUtil;
import org.hibernate.SessionFactory;

public class FinishedMatchesPersistenceService {

    private final MatchRepository matchRepository;
    private final OngoingMatchMapper ongoingMatchMapper;
    private final MatchReadMapper matchReadMapper;
    private final SessionFactory sessionFactory;
    private static final FinishedMatchesPersistenceService INSTANCE = new FinishedMatchesPersistenceService();

    private FinishedMatchesPersistenceService() {
        matchRepository = MatchRepository.getInstance();
        ongoingMatchMapper = OngoingMatchMapper.getInstance();
        matchReadMapper = MatchReadMapper.getInstance();
        sessionFactory = HibernateUtil.getSessionFactory();
    }

    public void save(OngoingMatchDto completedMatch) {
        var match = ongoingMatchMapper.mapFrom(completedMatch);
        matchRepository.save(match);
    }

    public MatchPageDto getPaginatedMatches(String playerName, Integer page) {
        var session = sessionFactory.getCurrentSession();
        session.beginTransaction();

        var matchPageDto = buildMatchPage(playerName, page);

        session.getTransaction().commit();

        return matchPageDto;
    }

    private MatchPageDto buildMatchPage(String playerName, Integer page) {
        var rowsAmount = isPlayerNameSpecified(playerName)
                ? matchRepository.countAllByPlayerName(playerName)
                : matchRepository.countAll();

        var totalPages = Math.max(1, calculateTotalPages(rowsAmount));
        var currentPage = normalizePageNumber(page, totalPages);

        var matches = isPlayerNameSpecified(playerName)
                ? matchRepository.findAllByPlayerName(playerName, currentPage)
                : matchRepository.findAll(currentPage);

        return MatchPageDto.builder()
                .currentPage(currentPage)
                .totalPages(totalPages)
                .matches(matchReadMapper.mapFrom(matches))
                .build();
    }

    private boolean isPlayerNameSpecified(String name) {
        return name != null && !name.trim().isEmpty();
    }

    private Long calculateTotalPages(Long numberOfRecords) {
        return (numberOfRecords + PaginationUtil.RECORDS_PER_PAGE - 1) / PaginationUtil.RECORDS_PER_PAGE;
    }

    private Integer normalizePageNumber(Integer page, Long totalPage) {
        return Math.toIntExact(Math.max(1, Math.min(page, totalPage)));
    }

    public static FinishedMatchesPersistenceService getInstance() {
        return INSTANCE;
    }
}
