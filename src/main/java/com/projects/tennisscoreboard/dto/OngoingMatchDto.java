package com.projects.tennisscoreboard.dto;

import lombok.Builder;

@Builder
public record OngoingMatchDto(Long firstPlayerId,
                              Long secondPlayerId,
                              MatchScoreDto matchScoreDto) {
}
