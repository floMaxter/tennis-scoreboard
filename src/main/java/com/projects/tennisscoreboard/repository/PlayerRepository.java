package com.projects.tennisscoreboard.repository;

import com.projects.tennisscoreboard.entity.Player;
import jakarta.persistence.EntityManager;


public class PlayerRepository extends BaseRepository<Long, Player> {

    public PlayerRepository(EntityManager entityManager) {
        super(Player.class, entityManager);
    }
}
