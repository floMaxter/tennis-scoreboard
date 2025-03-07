package com.projects.tennisscoreboard.mapper;

import com.projects.tennisscoreboard.dto.OngoingMatchDto;
import com.projects.tennisscoreboard.dto.OngoingMatchReadDto;
import com.projects.tennisscoreboard.repository.PlayerRepository;

public class OngoingMatchToReadMapper implements Mapper<OngoingMatchDto, OngoingMatchReadDto> {

    private final PlayerRepository playerRepository;

    public OngoingMatchToReadMapper(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    @Override
    public OngoingMatchReadDto mapFrom(OngoingMatchDto object) {
        var firstPlayer = playerRepository.findById(object.getFirstPlayerId())
                .orElseThrow(IllegalArgumentException::new);
        var secondPlayer = playerRepository.findById(object.getSecondPlayerId())
                .orElseThrow(IllegalArgumentException::new);

        return OngoingMatchReadDto.builder()
                .firstPlayer(firstPlayer)
                .secondPlayer(secondPlayer)
                .matchScoreDto(object.getMatchScoreDto())
                .matchState(object.getMatchState())
                .build();
    }
}
