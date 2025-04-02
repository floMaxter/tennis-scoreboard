package com.projects.tennisscoreboard.dto.match.ongoing;

import com.projects.tennisscoreboard.dto.match.MatchScoreDto;
import com.projects.tennisscoreboard.dto.match.MatchState;
import com.projects.tennisscoreboard.dto.player.PlayerDto;
import lombok.Builder;
import lombok.Data;
import lombok.Setter;

@Data
@Builder
public class OngoingMatchDto {

    private final PlayerDto firstPlayer;
    private final PlayerDto secondPlayer;

    @Setter
    private MatchScoreDto matchScoreDto;

    @Setter
    private MatchState matchState;
}
