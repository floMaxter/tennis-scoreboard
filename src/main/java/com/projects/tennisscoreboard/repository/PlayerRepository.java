package com.projects.tennisscoreboard.repository;

import com.projects.tennisscoreboard.entity.Player;
import com.projects.tennisscoreboard.exception.DatabaseException;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;


@Slf4j
public class PlayerRepository extends BaseRepository<Long, Player> {

    private static final PlayerRepository INSTANCE = new PlayerRepository();

    private PlayerRepository() {
        super(Player.class);
    }

    public Optional<Player> findByName(String name) {
        try (var session = sessionFactory.openSession()) {
            log.info("Search for a player by name {}", name);
            return session.createQuery("select p from Player p where p.name = :name", Player.class)
                    .setParameter("name", name)
                    .uniqueResultOptional();
        } catch (RuntimeException e) {
            log.error("Error when searching for player by name {}", name);
            throw new DatabaseException("Database error.");
        }
    }

    public static PlayerRepository getInstance() {
        return INSTANCE;
    }
}
