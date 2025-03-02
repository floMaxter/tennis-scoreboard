package com.projects.tennisscoreboard.dto;

import lombok.Builder;

@Builder
public record MatchScoreDto(PointScore pointsScore,
                            GameScore gamesScore,
                            SetScore setsScore) {
}
