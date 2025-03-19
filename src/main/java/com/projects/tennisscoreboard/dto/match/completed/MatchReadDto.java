package com.projects.tennisscoreboard.dto.match.completed;

import com.projects.tennisscoreboard.entity.Player;
import lombok.Builder;

@Builder
public record MatchReadDto(Long id,
                           Player firstPlayer,
                           Player seconaPlayer,
                           Player winner) {
}
