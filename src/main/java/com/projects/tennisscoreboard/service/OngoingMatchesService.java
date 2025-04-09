package com.projects.tennisscoreboard.service;

import com.projects.tennisscoreboard.dto.match.MatchState;
import com.projects.tennisscoreboard.dto.match.ongoing.MatchCreateDto;
import com.projects.tennisscoreboard.dto.match.ongoing.OngoingMatchDto;
import com.projects.tennisscoreboard.entity.Player;
import com.projects.tennisscoreboard.exception.NotFoundException;
import com.projects.tennisscoreboard.mapper.player.PlayerDtoMapper;
import com.projects.tennisscoreboard.repository.InMemoryOngoingMatchRepository;
import com.projects.tennisscoreboard.repository.PlayerRepository;
import com.projects.tennisscoreboard.utils.MatchLockManager;
import com.projects.tennisscoreboard.utils.ScoreUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OngoingMatchesService {

    private final MatchScoreCalculationService matchScoreCalculationService;
    private final PlayerRepository playerRepository;
    private final InMemoryOngoingMatchRepository ongoingMatchRepository;
    private final PlayerDtoMapper playerDtoMapper;
    private static final OngoingMatchesService INSTANCE = new OngoingMatchesService();

    private OngoingMatchesService() {
        playerRepository = PlayerRepository.getInstance();
        playerDtoMapper = PlayerDtoMapper.getInstance();
        matchScoreCalculationService = MatchScoreCalculationService.getInstance();
        ongoingMatchRepository = InMemoryOngoingMatchRepository.getInstance();
    }

    public OngoingMatchDto findById(String matchId) {
        return ongoingMatchRepository.findById(matchId)
                .orElseThrow(() -> {
                    log.error("Error when searching ongoing match by id {}", matchId);
                    return new NotFoundException("Match with ID " + matchId + " not found");
                });
    }

    public String create(MatchCreateDto matchCreateDto) {
        var ongoingMatchDto = buildOngoingMatchDto(matchCreateDto);
        return ongoingMatchRepository.save(ongoingMatchDto);
    }

    private OngoingMatchDto buildOngoingMatchDto(MatchCreateDto matchCreateDto) {
        var firstPlayer = playerRepository.findByName(matchCreateDto.firstPlayerName())
                .orElse(playerRepository.save(new Player(matchCreateDto.firstPlayerName())));

        var secondPlayer = playerRepository.findByName(matchCreateDto.secondPlayerName())
                .orElse(playerRepository.save(new Player(matchCreateDto.secondPlayerName())));

        return OngoingMatchDto.builder()
                .firstPlayer(playerDtoMapper.mapFrom(firstPlayer))
                .secondPlayer(playerDtoMapper.mapFrom(secondPlayer))
                .matchScoreDto(ScoreUtil.createInitialMatchScore())
                .matchState(MatchState.REGULAR)
                .build();
    }

    public OngoingMatchDto updateOngoingMatch(String matchId, Long pointWinnerId) {
        var lock = MatchLockManager.getLock(matchId).writeLock();
        lock.lock();
        try {
            ensureMatchPresent(matchId);

            var findMatchDto = findById(matchId);
            return ScoreUtil.isMatchFinished(findMatchDto)
                    ? findMatchDto
                    : calculateAndUpdateScore(matchId, pointWinnerId, findMatchDto);
        } finally {
            lock.unlock();
        }
    }

    private void ensureMatchPresent(String matchId) {
        if (!ongoingMatchRepository.isMatchPresent(matchId)) {
            log.error("Tried to update match that has already been deleted: {}", matchId);
            throw new NotFoundException("The match with the ID " + matchId + " has been deleted");
        }
    }

    private OngoingMatchDto calculateAndUpdateScore(String matchId, Long pointWinnerId, OngoingMatchDto findMatchDto) {
        var updatedMatchDto = matchScoreCalculationService.calculateScore(findMatchDto, pointWinnerId);
        ongoingMatchRepository.update(matchId, updatedMatchDto);
        return updatedMatchDto;
    }

    public void delete(String matchId) {
        var lock = MatchLockManager.getLock(matchId).writeLock();
        lock.lock();
        try {
            ongoingMatchRepository.delete(matchId);
        } finally {
            lock.unlock();
            MatchLockManager.removeLock(matchId);
        }

    }

    public static OngoingMatchesService getInstance() {
        return INSTANCE;
    }
}
