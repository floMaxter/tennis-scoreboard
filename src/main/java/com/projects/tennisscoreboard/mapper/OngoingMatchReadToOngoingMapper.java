package com.projects.tennisscoreboard.mapper;

import com.projects.tennisscoreboard.dto.OngoingMatchDto;
import com.projects.tennisscoreboard.dto.OngoingMatchReadDto;

public class OngoingMatchReadToOngoingMapper implements Mapper<OngoingMatchReadDto, OngoingMatchDto> {

    private static final OngoingMatchReadToOngoingMapper INSTANCE = new OngoingMatchReadToOngoingMapper();

    private OngoingMatchReadToOngoingMapper() {
    }

    @Override
    public OngoingMatchDto mapFrom(OngoingMatchReadDto object) {
        return OngoingMatchDto.builder()
                .firstPlayerId(object.firstPlayer().getId())
                .secondPlayerId(object.secondPlayer().getId())
                .matchScoreDto(object.matchScoreDto())
                .build();
    }

    public static OngoingMatchReadToOngoingMapper getInstance() {
        return INSTANCE;
    }
}
