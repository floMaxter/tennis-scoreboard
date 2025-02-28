package com.projects.tennisscoreboard.mapper;

import com.projects.tennisscoreboard.dto.OngoingMatchDto;
import com.projects.tennisscoreboard.dto.OngoingMatchReadDto;
import com.projects.tennisscoreboard.repository.PlayerRepository;

public class MatchReadMapper implements Mapper<OngoingMatchDto, OngoingMatchReadDto> {

    private final PlayerRepository playerRepository;

    public MatchReadMapper(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    @Override
    public OngoingMatchReadDto mapFrom(OngoingMatchDto object) {
        var firstPlayer = playerRepository.findById(object.firstPlayerId())
                .orElseThrow(IllegalArgumentException::new);
        var secondPlayer = playerRepository.findById(object.secondPlayerId())
                .orElseThrow(IllegalArgumentException::new);

        return OngoingMatchReadDto.builder()
                .firstPlayer(firstPlayer)
                .secondPlayer(secondPlayer)
                .gameScoreDto(object.gameScoreDto())
                .build();
    }
}
