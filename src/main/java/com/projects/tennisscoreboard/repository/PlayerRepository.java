package com.projects.tennisscoreboard.repository;

import com.projects.tennisscoreboard.entity.Player;

import java.util.Optional;


public class PlayerRepository extends BaseRepository<Long, Player> {

    private static final PlayerRepository INSTANCE = new PlayerRepository();

    private PlayerRepository() {
        super(Player.class);
    }

    public Optional<Player> findByName(String name) {
        try (var session = sessionFactory.openSession()) {
            return session.createQuery("select p from Player p where p.name = :name", Player.class)
                    .setParameter("name", name)
                    .uniqueResultOptional();
        }
    }

    public static PlayerRepository getInstance() {
        return INSTANCE;
    }
}
