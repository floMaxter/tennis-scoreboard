package com.projects.tennisscoreboard.dto;

import com.projects.tennisscoreboard.entity.Player;
import lombok.Builder;

@Builder
public record OngoingMatchReadDto(Player firstPlayer,
                                  Player secondPlayer,
                                  GameScoreDto gameScoreDto) {
}
