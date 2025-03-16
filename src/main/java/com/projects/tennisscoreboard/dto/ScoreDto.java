package com.projects.tennisscoreboard.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ScoreDto {

    private int pointsScore;
    private int gamesScore;
    private int setsScore;
    private int advantagePointScore;
}
