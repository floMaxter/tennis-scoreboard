package com.projects.tennisscoreboard.repository;

import com.projects.tennisscoreboard.dto.match.ongoing.OngoingMatchDto;
import com.projects.tennisscoreboard.exception.NotFoundException;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class InMemoryOngoingMatchRepository {

    private final Map<String, OngoingMatchDto> ongoingMatches;
    private static final InMemoryOngoingMatchRepository INSTANCE = new InMemoryOngoingMatchRepository();

    private InMemoryOngoingMatchRepository() {
        ongoingMatches = new ConcurrentHashMap<>();
    }

    public Optional<OngoingMatchDto> findById(String id) {
        return Optional.ofNullable(ongoingMatches.get(id));
    }

    public String save(OngoingMatchDto ongoingMatchDto) {
        var matchId = UUID.randomUUID().toString();
        ongoingMatches.put(matchId, ongoingMatchDto);
        return matchId;
    }

    public void update(String id, OngoingMatchDto ongoingMatchDto) {
        if (ongoingMatches.containsKey(id)) {
            ongoingMatches.put(id, ongoingMatchDto);
        } else {
            log.error("Error when searching ongoing match by id={} in update method", id);
            throw new NotFoundException("Match with ID " + id + " not found during the update ongoing match");
        }
    }

    public void delete(String id) {
        ongoingMatches.remove(id);
    }

    public boolean isMatchPresent(String id) {
        return ongoingMatches.containsKey(id);
    }

    public static InMemoryOngoingMatchRepository getInstance() {
        return INSTANCE;
    }
}
