package com.projects.tennisscoreboard.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MatchScoreDto {

    private final ScoreDto firstPlayerScore;
    private final ScoreDto secondPlayerScore;
}
