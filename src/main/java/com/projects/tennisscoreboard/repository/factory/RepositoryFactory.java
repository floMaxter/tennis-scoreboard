package com.projects.tennisscoreboard.repository.factory;

import com.projects.tennisscoreboard.repository.PlayerRepository;
import lombok.Getter;

public abstract class RepositoryFactory {

    @Getter
    private static final PlayerRepository playerRepository = new PlayerRepository();

}
