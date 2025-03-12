package com.projects.tennisscoreboard.service;

import com.projects.tennisscoreboard.dto.MatchCreateDto;
import com.projects.tennisscoreboard.dto.OngoingMatchDto;
import com.projects.tennisscoreboard.dto.OngoingMatchReadDto;
import com.projects.tennisscoreboard.entity.Player;
import com.projects.tennisscoreboard.mapper.MatchCreateToOngoingMapper;
import com.projects.tennisscoreboard.mapper.OngoingMatchToReadMapper;
import com.projects.tennisscoreboard.repository.PlayerRepository;
import com.projects.tennisscoreboard.repository.factory.RepositoryFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class OngoingMatchesService {

    private final PlayerRepository playerRepository;
    private final MatchCreateToOngoingMapper matchCreateToOngoingMapper;
    private final OngoingMatchToReadMapper ongoingMatchToReadMapper;
    private final Map<UUID, OngoingMatchDto> ongoingMatches;
    private static final OngoingMatchesService INSTANCE = new OngoingMatchesService();

    private OngoingMatchesService() {
        ongoingMatches = new HashMap<>();
        playerRepository = RepositoryFactory.getPlayerRepository();
        matchCreateToOngoingMapper = new MatchCreateToOngoingMapper(playerRepository);
        ongoingMatchToReadMapper = new OngoingMatchToReadMapper(playerRepository);
    }

    public OngoingMatchReadDto findByUUID(String matchId) {
        // TODO: validate

        var ongoingMatchDto = ongoingMatches.get(UUID.fromString(matchId));
        return ongoingMatchToReadMapper.mapFrom(ongoingMatchDto);
    }

    public UUID create(MatchCreateDto matchCreateDto) {
        // TODO: validate

        createPlayersIfNotExist(matchCreateDto);

        var ongoingMatchDto = matchCreateToOngoingMapper.mapFrom(matchCreateDto);
        var uuid = UUID.randomUUID();
        ongoingMatches.put(uuid, ongoingMatchDto);
        return uuid;
    }

    private void createPlayersIfNotExist(MatchCreateDto matchCreateDto) {
        var maybeFirstPlayer = playerRepository.findByName(matchCreateDto.firstPlayerName());
        var maybeSecondPlayer = playerRepository.findByName(matchCreateDto.secondPlayerName());
        if (maybeFirstPlayer.isEmpty()) {
            playerRepository.save(new Player(matchCreateDto.firstPlayerName()));
        }
        if (maybeSecondPlayer.isEmpty()) {
            playerRepository.save(new Player(matchCreateDto.secondPlayerName()));
        }
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
