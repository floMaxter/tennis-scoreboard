package com.projects.tennisscoreboard.service;

import com.projects.tennisscoreboard.Utils.ValidationUtil;
import com.projects.tennisscoreboard.dto.MatchCreateDto;
import com.projects.tennisscoreboard.dto.MatchScoreDto;
import com.projects.tennisscoreboard.dto.MatchState;
import com.projects.tennisscoreboard.dto.OngoingMatchDto;
import com.projects.tennisscoreboard.dto.OngoingMatchReadDto;
import com.projects.tennisscoreboard.dto.ScoreDto;
import com.projects.tennisscoreboard.entity.Player;
import com.projects.tennisscoreboard.repository.PlayerRepository;
import com.projects.tennisscoreboard.validator.CreateMatchValidator;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.UUID;

public class OngoingMatchesService {

    private final PlayerRepository playerRepository;
    private final Map<UUID, OngoingMatchDto> ongoingMatches;
    private final CreateMatchValidator createMatchValidator;
    private static final OngoingMatchesService INSTANCE = new OngoingMatchesService();

    private OngoingMatchesService() {
        ongoingMatches = new HashMap<>();
        playerRepository = PlayerRepository.getInstance();
        createMatchValidator = CreateMatchValidator.getInstance();
    }

    public OngoingMatchReadDto findById(String matchId) {
        // TODO: validate

        var matchUuid = UUID.fromString(matchId);
        var ongoingMatchDto = ongoingMatches.get(matchUuid);
        if (ongoingMatchDto == null) {
            throw new NoSuchElementException("Match with ID " + matchId + " not fount");
        }

        return buildOngoingMatchReadDto(ongoingMatchDto);
    }

    private OngoingMatchReadDto buildOngoingMatchReadDto(OngoingMatchDto ongoingMatchDto) {
        var firstPlayer = playerRepository.findById(ongoingMatchDto.getFirstPlayerId())
                .orElseThrow(IllegalArgumentException::new);
        var secondPlayer = playerRepository.findById(ongoingMatchDto.getSecondPlayerId())
                .orElseThrow(IllegalArgumentException::new);

        return OngoingMatchReadDto.builder()
                .firstPlayer(firstPlayer)
                .secondPlayer(secondPlayer)
                .matchScoreDto(ongoingMatchDto.getMatchScoreDto())
                .matchState(ongoingMatchDto.getMatchState())
                .build();
    }

    public UUID create(MatchCreateDto matchCreateDto) {
        ValidationUtil.validate(createMatchValidator.isValid(matchCreateDto));

        var ongoingMatchDto = buildOngoingMatchDto(matchCreateDto);
        var matchId = UUID.randomUUID();
        ongoingMatches.put(matchId, ongoingMatchDto);

        return matchId;
    }

    private OngoingMatchDto buildOngoingMatchDto(MatchCreateDto matchCreateDto) {
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
        var matchUuid = UUID.fromString(matchId);
        if (ongoingMatches.containsKey(matchUuid)) {
            this.ongoingMatches.put(matchUuid, ongoingMatchDto);
        }
    }

    public static OngoingMatchesService getInstance() {
        return INSTANCE;
    }
}
