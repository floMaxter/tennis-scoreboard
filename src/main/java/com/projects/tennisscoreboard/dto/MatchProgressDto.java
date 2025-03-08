package com.projects.tennisscoreboard.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class MatchProgressDto {

    private final Long pointWinnerId;
    private ScoreDto winnerScore;
    private ScoreDto loserScore;
    private MatchState matchState;
    private Long advantagePlayerId;
}
