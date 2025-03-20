package com.projects.tennisscoreboard.mapper;

import com.projects.tennisscoreboard.dto.match.completed.MatchReadDto;
import com.projects.tennisscoreboard.entity.Match;

public class MatchReadMapper implements Mapper<Match, MatchReadDto> {

    private static final MatchReadMapper INSTANCE = new MatchReadMapper();

    private MatchReadMapper() {
    }

    @Override
    public MatchReadDto mapFrom(Match object) {
        return MatchReadDto.builder()
                .id(object.getId())
                .firstPlayer(object.getFirstPlayer())
                .secondPlayer(object.getSecondPlayer())
                .winner(object.getWinner())
                .build();
    }

    public static MatchReadMapper getInstance() {
        return INSTANCE;
    }
}
