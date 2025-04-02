package com.projects.tennisscoreboard.service;

import com.projects.tennisscoreboard.dto.match.MatchState;
import com.projects.tennisscoreboard.dto.match.ongoing.MatchCreateDto;
import com.projects.tennisscoreboard.dto.match.ongoing.OngoingMatchDto;
import com.projects.tennisscoreboard.entity.Player;
import com.projects.tennisscoreboard.exception.NotFoundException;
import com.projects.tennisscoreboard.mapper.player.PlayerDtoMapper;
import com.projects.tennisscoreboard.repository.PlayerRepository;
import com.projects.tennisscoreboard.utils.ScoreUtil;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class OngoingMatchesService {

    private final PlayerRepository playerRepository;
    private final Map<UUID, OngoingMatchDto> ongoingMatches;
    private final PlayerDtoMapper playerDtoMapper;
    private static final OngoingMatchesService INSTANCE = new OngoingMatchesService();

    private OngoingMatchesService() {
        ongoingMatches = new ConcurrentHashMap<>();
        playerRepository = PlayerRepository.getInstance();
        playerDtoMapper = PlayerDtoMapper.getInstance();
    }

    public OngoingMatchDto findById(String matchId) {
        var matchUuid = UUID.fromString(matchId);
        var ongoingMatchDto = ongoingMatches.get(matchUuid);
        if (ongoingMatchDto == null) {
            throw new NotFoundException("Match with ID " + matchId + " not found");
        }

        return ongoingMatchDto;
    }

    public UUID create(MatchCreateDto matchCreateDto) {
        var ongoingMatchDto = buildOngoingMatchDto(matchCreateDto);
        var matchId = UUID.randomUUID();
        ongoingMatches.put(matchId, ongoingMatchDto);

        return matchId;
    }

    private OngoingMatchDto buildOngoingMatchDto(MatchCreateDto matchCreateDto) {
        var firstPlayer = getOrCreatePlayer(matchCreateDto.firstPlayerName());
        var secondPlayer = getOrCreatePlayer(matchCreateDto.secondPlayerName());

        return OngoingMatchDto.builder()
                .firstPlayer(playerDtoMapper.mapFrom(firstPlayer))
                .secondPlayer(playerDtoMapper.mapFrom(secondPlayer))
                .matchScoreDto(ScoreUtil.createInitialMatchScore())
                .matchState(MatchState.REGULAR)
                .build();
    }

    private Player getOrCreatePlayer(String name) {
        var maybePlayer = playerRepository.findByName(name);
        return maybePlayer.orElseGet(() -> playerRepository.save(new Player(name)));
    }

    public void updateOngoingMatch(String matchId, OngoingMatchDto ongoingMatchDto) {
        var matchUuid = UUID.fromString(matchId);
        if (ongoingMatches.containsKey(matchUuid)) {
            ongoingMatches.put(matchUuid, ongoingMatchDto);
        }
    }

    public void delete(String matchId) {
        var matchUuid = UUID.fromString(matchId);
        ongoingMatches.remove(matchUuid);
    }

    public static OngoingMatchesService getInstance() {
        return INSTANCE;
    }
}
