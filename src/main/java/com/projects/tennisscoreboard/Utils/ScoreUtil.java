package com.projects.tennisscoreboard.Utils;

import com.projects.tennisscoreboard.dto.MatchScoreDto;
import com.projects.tennisscoreboard.dto.ScoreDto;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ScoreUtil {

    public static final int ZERO_POINT = 0;
    public static final int FIRST_POINT = 15;
    public static final int SECOND_POINT = 30;
    public static final int THIRD_POINT = 40;
    public static final int GAMES_TO_TIEBREAK = 6;
    public static final int TIEBREAK_MIN_POINT_DIFFERENCE = 2;
    public static final int TIEBREAK_MIN_POINTS_TO_WIN = 7;
    public static final int DEUCE_MIN_ADVANTAGE_DIFFERENCE = 2;
    public static final int SET_MIN_GAMES_TO_WIN = 7;
    public static final int SETS_TO_WIN = 2;

    public static MatchScoreDto createInitialMatchScore() {
        return MatchScoreDto.builder()
                .firstPlayerScore(new ScoreDto())
                .secondPlayerScore(new ScoreDto())
                .build();
    }
}
