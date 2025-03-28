package com.projects.tennisscoreboard.mapper;

import com.projects.tennisscoreboard.dto.match.ongoing.OngoingMatchDto;
import com.projects.tennisscoreboard.dto.match.ongoing.OngoingMatchReadDto;

public class OngoingMatchReadMapper implements Mapper<OngoingMatchReadDto, OngoingMatchDto> {

    private static final OngoingMatchReadMapper INSTANCE = new OngoingMatchReadMapper();

    private OngoingMatchReadMapper() {
    }

    @Override
    public OngoingMatchDto mapFrom(OngoingMatchReadDto object) {
        return OngoingMatchDto.builder()
                .firstPlayerId(object.getFirstPlayer().getId())
                .secondPlayerId(object.getSecondPlayer().getId())
                .matchScoreDto(object.getMatchScoreDto())
                .matchState(object.getMatchState())
                .build();
    }

    public static OngoingMatchReadMapper getInstance() {
        return INSTANCE;
    }
}
