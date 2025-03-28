package com.projects.tennisscoreboard.dto.match.completed;

import com.projects.tennisscoreboard.entity.Player;
import lombok.Builder;

@Builder
public record MatchReadDto(Player firstPlayer,
                           Player secondPlayer,
                           Player winner) {
}
