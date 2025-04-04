package com.projects.tennisscoreboard.mapper.match;

import com.projects.tennisscoreboard.dto.match.completed.MatchReadDto;
import com.projects.tennisscoreboard.entity.Match;
import com.projects.tennisscoreboard.mapper.Mapper;
import com.projects.tennisscoreboard.mapper.player.PlayerDtoMapper;

public class MatchReadMapper implements Mapper<Match, MatchReadDto> {

    private final PlayerDtoMapper playerDtoMapper = PlayerDtoMapper.getInstance();
    private static final MatchReadMapper INSTANCE = new MatchReadMapper();

    private MatchReadMapper() {
    }

    @Override
    public MatchReadDto mapFrom(Match object) {
        return MatchReadDto.builder()
                .firstPlayer(playerDtoMapper.mapFrom(object.getFirstPlayer()))
                .secondPlayer(playerDtoMapper.mapFrom(object.getSecondPlayer()))
                .winner(playerDtoMapper.mapFrom(object.getWinner()))
                .build();
    }



    public static MatchReadMapper getInstance() {
        return INSTANCE;
    }
}
