package com.projects.tennisscoreboard.dto;

import lombok.Builder;

@Builder
public record MatchScoreDto(ScoreDto firstPlayerScore,
                            ScoreDto secondPlayerScore) {
}
