package com.projects.tennisscoreboard.dto.match.ongoing;

import com.projects.tennisscoreboard.dto.match.MatchScoreDto;
import com.projects.tennisscoreboard.dto.match.MatchState;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
public class OngoingMatchDto {

    private final Long firstPlayerId;
    private final Long secondPlayerId;

    @Setter
    private MatchScoreDto matchScoreDto;

    @Setter
    private MatchState matchState;
}
