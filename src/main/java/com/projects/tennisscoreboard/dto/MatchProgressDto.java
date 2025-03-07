package com.projects.tennisscoreboard.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
public class MatchProgressDto {

    private final Long pointWinnerId;
    private final ScoreDto winnerScore;
    private final ScoreDto loserScore;

    @Setter
    private MatchState matchState;
    @Setter
    private Long advantagePlayerId;
}
