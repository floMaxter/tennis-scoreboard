package com.projects.tennisscoreboard.mapper.player;

import com.projects.tennisscoreboard.dto.player.PlayerDto;
import com.projects.tennisscoreboard.entity.Player;
import com.projects.tennisscoreboard.mapper.Mapper;

public class PlayerDtoMapper implements Mapper<Player, PlayerDto> {

    private static final PlayerDtoMapper INSTANCE = new PlayerDtoMapper();

    private PlayerDtoMapper() {
    }

    @Override
    public PlayerDto mapFrom(Player object) {
        return new PlayerDto(object.getName());
    }

    public static PlayerDtoMapper getInstance() {
        return INSTANCE;
    }
}
