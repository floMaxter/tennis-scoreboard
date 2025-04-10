package com.projects.tennisscoreboard.dto.match.completed;

import com.projects.tennisscoreboard.dto.player.PlayerDto;
import lombok.Builder;

@Builder
public record MatchReadDto(PlayerDto firstPlayer,
                           PlayerDto secondPlayer,
                           PlayerDto winner) {
}
