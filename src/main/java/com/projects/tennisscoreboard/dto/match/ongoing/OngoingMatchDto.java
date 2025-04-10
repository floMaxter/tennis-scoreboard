package com.projects.tennisscoreboard.dto.match.ongoing;

import com.projects.tennisscoreboard.dto.match.MatchScoreDto;
import com.projects.tennisscoreboard.dto.match.MatchState;
import com.projects.tennisscoreboard.dto.player.PlayerDto;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OngoingMatchDto {

    private final PlayerDto firstPlayer;
    private final PlayerDto secondPlayer;
    private final MatchScoreDto matchScoreDto;
    private final MatchState matchState;
}
