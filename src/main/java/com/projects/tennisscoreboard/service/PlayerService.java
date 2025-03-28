package com.projects.tennisscoreboard.service;

import com.projects.tennisscoreboard.dto.player.PlayerDto;
import com.projects.tennisscoreboard.exception.NotFoundException;
import com.projects.tennisscoreboard.mapper.player.PlayerDtoMapper;
import com.projects.tennisscoreboard.repository.PlayerRepository;
import com.projects.tennisscoreboard.utils.PropertiesUtil;

public class PlayerService {

    private final PlayerRepository playerRepository;
    private final PlayerDtoMapper playerDtoMapper;
    private static final PlayerService INSTANCE = new PlayerService();

    private PlayerService() {
        playerRepository = PlayerRepository.getInstance();
        playerDtoMapper = PlayerDtoMapper.getInstance();
    }

    public PlayerDto findById(Long id) {
        return playerRepository.findById(id)
                .map(playerDtoMapper::mapFrom)
                .orElseThrow(() -> new NotFoundException(PropertiesUtil.get("exception.player_not_found") + id));
    }

    public static PlayerService getInstance() {
        return INSTANCE;
    }
}
