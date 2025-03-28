package com.projects.tennisscoreboard.dto.match;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MatchScoreDto {

    private final ScoreDto firstPlayerScore;
    private final ScoreDto secondPlayerScore;
}
