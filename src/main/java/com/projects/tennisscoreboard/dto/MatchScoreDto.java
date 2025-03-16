package com.projects.tennisscoreboard.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MatchScoreDto {

    private final ScoreDto firstPlayerScore;
    private final ScoreDto secondPlayerScore;
}
