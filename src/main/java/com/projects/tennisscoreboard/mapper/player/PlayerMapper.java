package com.projects.tennisscoreboard.mapper.player;

import com.projects.tennisscoreboard.dto.player.PlayerDto;
import com.projects.tennisscoreboard.entity.Player;
import com.projects.tennisscoreboard.mapper.Mapper;

public class PlayerMapper implements Mapper<PlayerDto, Player> {

    private static final PlayerMapper INSTANCE = new PlayerMapper();

    private PlayerMapper() {
    }

    @Override
    public Player mapFrom(PlayerDto object) {
        return new Player(object.id(), object.name());
    }

    public static PlayerMapper getInstance() {
        return INSTANCE;
    }
}
