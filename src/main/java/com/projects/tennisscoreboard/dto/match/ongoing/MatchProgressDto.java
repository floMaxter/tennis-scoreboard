package com.projects.tennisscoreboard.dto.match.ongoing;

import com.projects.tennisscoreboard.dto.match.MatchState;
import com.projects.tennisscoreboard.dto.match.ScoreDto;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MatchProgressDto {

    private final Long pointWinnerId;
    private ScoreDto winnerScore;
    private ScoreDto loserScore;
    private MatchState matchState;
}
