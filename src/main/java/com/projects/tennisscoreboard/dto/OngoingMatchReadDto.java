package com.projects.tennisscoreboard.dto;

import com.projects.tennisscoreboard.entity.Player;
import lombok.Builder;
import lombok.Data;
import lombok.Setter;

@Data
@Builder
public class OngoingMatchReadDto {

    private final Player firstPlayer;
    private final Player secondPlayer;

    @Setter
    private MatchScoreDto matchScoreDto;

    @Setter
    private MatchState matchState;
}
