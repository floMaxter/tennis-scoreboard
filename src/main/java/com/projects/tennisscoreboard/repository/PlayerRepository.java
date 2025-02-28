package com.projects.tennisscoreboard.repository;

import com.projects.tennisscoreboard.entity.Player;

import java.util.List;


public class PlayerRepository extends BaseRepository<Long, Player> {

    public PlayerRepository() {
        super(Player.class);
    }

    public List<Player> findByName(String name) {
        return sessionFactory.getCurrentSession()
                .createQuery("select p from Player p where p.name = :name", Player.class)
                .setParameter("name", name)
                .getResultList();
    }
}
