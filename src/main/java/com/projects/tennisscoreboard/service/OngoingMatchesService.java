package com.projects.tennisscoreboard.service;

import com.projects.tennisscoreboard.dto.MatchCreateDto;
import com.projects.tennisscoreboard.dto.MatchScoreDto;
import com.projects.tennisscoreboard.dto.MatchState;
import com.projects.tennisscoreboard.dto.OngoingMatchDto;
import com.projects.tennisscoreboard.dto.OngoingMatchReadDto;
import com.projects.tennisscoreboard.dto.ScoreDto;
import com.projects.tennisscoreboard.entity.Player;
import com.projects.tennisscoreboard.mapper.OngoingMatchToReadMapper;
import com.projects.tennisscoreboard.repository.PlayerRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class OngoingMatchesService {

    private final PlayerRepository playerRepository;
    private final OngoingMatchToReadMapper ongoingMatchToReadMapper;
    private final Map<UUID, OngoingMatchDto> ongoingMatches;
    private static final OngoingMatchesService INSTANCE = new OngoingMatchesService();

    private OngoingMatchesService() {
        ongoingMatches = new HashMap<>();
        playerRepository = PlayerRepository.getInstance();
        ongoingMatchToReadMapper = new OngoingMatchToReadMapper(playerRepository);
    }

    public OngoingMatchReadDto findByUUID(String matchId) {
        // TODO: validate

        var ongoingMatchDto = ongoingMatches.get(UUID.fromString(matchId));
        return ongoingMatchToReadMapper.mapFrom(ongoingMatchDto);
    }

    public UUID create(MatchCreateDto matchCreateDto) {
        // TODO: validate

        var ongoingMatchDto = buildOngoingMatch(matchCreateDto);
        var matchId = UUID.randomUUID();
        ongoingMatches.put(matchId, ongoingMatchDto);
        return matchId;
    }

    private OngoingMatchDto buildOngoingMatch(MatchCreateDto matchCreateDto) {
        var firstPlayer = getOrCreatePlayer(matchCreateDto.firstPlayerName());
        var secondPlayer = getOrCreatePlayer(matchCreateDto.secondPlayerName());

        return OngoingMatchDto.builder()
                .firstPlayerId(firstPlayer.getId())
                .secondPlayerId(secondPlayer.getId())
                .matchScoreDto(createInitialMatchScore())
                .matchState(MatchState.REGULAR)
                .build();
    }

    private Player getOrCreatePlayer(String name) {
        var maybePlayer = playerRepository.findByName(name);
        return maybePlayer.orElseGet(() -> playerRepository.save(new Player(name)));
    }

    private MatchScoreDto createInitialMatchScore() {
        return MatchScoreDto.builder()
                .firstPlayerScore(new ScoreDto())
                .secondPlayerScore(new ScoreDto())
                .build();
    }

    public void updateOngoingMatch(String matchId, OngoingMatchDto ongoingMatchDto) {
        // TODO: validate
        var uuid = UUID.fromString(matchId);
        if (ongoingMatches.containsKey(uuid)) {
            this.ongoingMatches.put(uuid, ongoingMatchDto);
        }
    }

    public static OngoingMatchesService getInstance() {
        return INSTANCE;
    }
}
