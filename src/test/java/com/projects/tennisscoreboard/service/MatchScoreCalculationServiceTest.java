package com.projects.tennisscoreboard.service;

import com.projects.tennisscoreboard.dto.match.MatchScoreDto;
import com.projects.tennisscoreboard.dto.match.MatchState;
import com.projects.tennisscoreboard.dto.match.ScoreDto;
import com.projects.tennisscoreboard.dto.match.ongoing.OngoingMatchDto;
import com.projects.tennisscoreboard.dto.player.PlayerDto;
import com.projects.tennisscoreboard.utils.ScoreUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MatchScoreCalculationServiceTest {

    private static final PlayerDto FIRST_PLAYER = new PlayerDto(1L, "Max");
    private static final PlayerDto SECOND_PLAYER = new PlayerDto(2L, "Ivan");

    private OngoingMatchDto baseMatch;
    private OngoingMatchDto expectedMatch;
    private MatchScoreCalculationService matchScoreCalculationService;

    @BeforeEach
    void prepare() {
        this.matchScoreCalculationService = MatchScoreCalculationService.getInstance();
        this.baseMatch = OngoingMatchDto.builder()
                .firstPlayer(FIRST_PLAYER)
                .secondPlayer(SECOND_PLAYER)
                .matchScoreDto(ScoreUtil.createInitialMatchScore())
                .matchState(MatchState.REGULAR)
                .build();
        this.expectedMatch = OngoingMatchDto.builder()
                .firstPlayer(FIRST_PLAYER)
                .secondPlayer(SECOND_PLAYER)
                .build();
    }

    @Test
    void calculateScore_WhenFirstPlayerWinsFromZeroZero_ShouldIncreaseFirstPlayerPoints() {
        var pointWinnerId = 1L;
        expectedMatch.setMatchScoreDto(MatchScoreDto.builder()
                .firstPlayerScore(buildScoreDto(15, 0, 0))
                .secondPlayerScore(buildScoreDto(0, 0, 0))
                .build());
        expectedMatch.setMatchState(MatchState.REGULAR);

        var updatedMatch = matchScoreCalculationService.calculateScore(baseMatch, pointWinnerId);

        Assertions.assertEquals(expectedMatch, updatedMatch);
    }

    @Test
    void calculateScore_WhenSecondPlayerWinsFromThirtyForty_ShouldResetPointsAndIncreaseGames() {
        var pointWinnerId = 2L;
        baseMatch.setMatchScoreDto(MatchScoreDto.builder()
                .firstPlayerScore(buildScoreDto(30, 0, 0))
                .secondPlayerScore(buildScoreDto(40, 0, 0))
                .build());

        expectedMatch.setMatchScoreDto(MatchScoreDto.builder()
                .firstPlayerScore(buildScoreDto(0, 0, 0))
                .secondPlayerScore(buildScoreDto(0, 1, 0))
                .build());
        expectedMatch.setMatchState(MatchState.REGULAR);

        var updatedMatch = matchScoreCalculationService.calculateScore(baseMatch, pointWinnerId);

        Assertions.assertEquals(expectedMatch, updatedMatch);
    }


    @Test
    void calculateScore_WhenFirstPlayerWinsFromThirtyForty_ShouldEnterDeuce() {
        var pointWinnerId = 1L;
        baseMatch.setMatchScoreDto(MatchScoreDto.builder()
                .firstPlayerScore(buildScoreDto(30, 0, 0))
                .secondPlayerScore(buildScoreDto(40, 0, 0))
                .build()
        );

        expectedMatch.setMatchScoreDto(MatchScoreDto.builder()
                .firstPlayerScore(buildScoreDto(40, 0, 0))
                .secondPlayerScore(buildScoreDto(40, 0, 0))
                .build());
        expectedMatch.setMatchState(MatchState.DEUCE);

        var updatedMatch = matchScoreCalculationService.calculateScore(baseMatch, pointWinnerId);

        Assertions.assertEquals(expectedMatch, updatedMatch);
    }

    @Test
    void calculateScore_WhenSecondPlayerWinsFromDeuce_ShouldEnterAdvantage() {
        var pointWinnerId = 2L;
        baseMatch.setMatchScoreDto(MatchScoreDto.builder()
                .firstPlayerScore(buildScoreDto(40, 0, 0))
                .secondPlayerScore(buildScoreDto(40, 0, 0))
                .build()
        );
        baseMatch.setMatchState(MatchState.DEUCE);

        expectedMatch.setMatchScoreDto(MatchScoreDto.builder()
                .firstPlayerScore(buildScoreDto(40, 0, 0))
                .secondPlayerScore(buildScoreDto(40, 0, 0, true))
                .build());
        expectedMatch.setMatchState(MatchState.DEUCE);

        var updatedMatch = matchScoreCalculationService.calculateScore(baseMatch, pointWinnerId);

        Assertions.assertEquals(expectedMatch, updatedMatch);
    }

    @Test
    void calculateScore_WhenSecondPlayerWinsFromDeuceAndAdvantageFirstPlayer_ShouldEnterAdvantage() {
        var pointWinnerId = 2L;
        baseMatch.setMatchScoreDto(MatchScoreDto.builder()
                .firstPlayerScore(buildScoreDto(40, 0, 0, true))
                .secondPlayerScore(buildScoreDto(40, 0, 0, false))
                .build()
        );
        baseMatch.setMatchState(MatchState.DEUCE);

        expectedMatch.setMatchScoreDto(MatchScoreDto.builder()
                .firstPlayerScore(buildScoreDto(40, 0, 0, false))
                .secondPlayerScore(buildScoreDto(40, 0, 0, false))
                .build());
        expectedMatch.setMatchState(MatchState.DEUCE);

        var updatedMatch = matchScoreCalculationService.calculateScore(baseMatch, pointWinnerId);

        Assertions.assertEquals(expectedMatch, updatedMatch);
    }

    @Test
    void calculateScore_WhenFirstPlayerHasAdvantageAndScores_FirstPlayerShouldWinGame() {
        var pointWinnerId = 1L;
        baseMatch.setMatchScoreDto(MatchScoreDto.builder()
                .firstPlayerScore(buildScoreDto(40, 0, 0, true))
                .secondPlayerScore(buildScoreDto(40, 0, 0, false))
                .build()
        );
        baseMatch.setMatchState(MatchState.DEUCE);

        var expectedMatch = OngoingMatchDto.builder()
                .firstPlayer(FIRST_PLAYER)
                .secondPlayer(SECOND_PLAYER)
                .matchScoreDto(MatchScoreDto.builder()
                        .firstPlayerScore(buildScoreDto(0, 1, 0, false))
                        .secondPlayerScore(buildScoreDto(0, 0, 0, false))
                        .build())
                .matchState(MatchState.REGULAR)
                .build();

        var updatedMatch = matchScoreCalculationService.calculateScore(baseMatch, pointWinnerId);

        Assertions.assertEquals(expectedMatch, updatedMatch);
    }

    @Test
    void calculateScore_WhenFirstPlayerWinsFromDeuceAndAdvantageFirstPlayerAndGamesFiveSix_ShouldEnterTiebreak() {
        var pointWinnerId = 1L;
        baseMatch.setMatchScoreDto(MatchScoreDto.builder()
                .firstPlayerScore(buildScoreDto(40, 5, 0, true))
                .secondPlayerScore(buildScoreDto(30, 6, 0, false))
                .build()
        );
        baseMatch.setMatchState(MatchState.DEUCE);

        expectedMatch.setMatchScoreDto(MatchScoreDto.builder()
                .firstPlayerScore(buildScoreDto(0, 6, 0, false))
                .secondPlayerScore(buildScoreDto(0, 6, 0, false))
                .build());
        expectedMatch.setMatchState(MatchState.TIEBREAK);

        var updatedMatch = matchScoreCalculationService.calculateScore(baseMatch, pointWinnerId);

        Assertions.assertEquals(expectedMatch, updatedMatch);
    }

    @Test
    void calculateScore_WhenFirstPlayerWinsFromGamesFiveSix_ShouldEnterTiebreak() {
        var pointWinnerId = 1L;
        baseMatch.setMatchScoreDto(MatchScoreDto.builder()
                .firstPlayerScore(buildScoreDto(40, 5, 0))
                .secondPlayerScore(buildScoreDto(0, 6, 0))
                .build()
        );
        baseMatch.setMatchState(MatchState.REGULAR);

        expectedMatch.setMatchScoreDto(MatchScoreDto.builder()
                .firstPlayerScore(buildScoreDto(0, 6, 0))
                .secondPlayerScore(buildScoreDto(0, 6, 0))
                .build());
        expectedMatch.setMatchState(MatchState.TIEBREAK);

        var updatedMatch = matchScoreCalculationService.calculateScore(baseMatch, pointWinnerId);

        Assertions.assertEquals(expectedMatch, updatedMatch);
    }

    @Test
    void calculateScore_WhenFirstPlayerWinsPointAtFiveSixInTiebreak_ShouldWinSet() {
        var pointWinnerId = 1L;
        baseMatch.setMatchScoreDto(MatchScoreDto.builder()
                .firstPlayerScore(buildScoreDto(6, 6, 0))
                .secondPlayerScore(buildScoreDto(5, 6, 0))
                .build()
        );
        baseMatch.setMatchState(MatchState.TIEBREAK);

        expectedMatch.setMatchScoreDto(MatchScoreDto.builder()
                .firstPlayerScore(buildScoreDto(0, 0, 1))
                .secondPlayerScore(buildScoreDto(0, 0, 0))
                .build());
        expectedMatch.setMatchState(MatchState.REGULAR);

        var updatedMatch = matchScoreCalculationService.calculateScore(baseMatch, pointWinnerId);

        Assertions.assertEquals(expectedMatch, updatedMatch);
    }

    @Test
    void calculateScore_WhenFirstPlayerWinsFromTiebreakAndPointsSixSix_ShouldEnterTiebreak() {
        var pointWinnerId = 1L;
        baseMatch.setMatchScoreDto(MatchScoreDto.builder()
                .firstPlayerScore(buildScoreDto(6, 6, 0))
                .secondPlayerScore(buildScoreDto(6, 6, 0))
                .build()
        );
        baseMatch.setMatchState(MatchState.TIEBREAK);

        expectedMatch.setMatchScoreDto(MatchScoreDto.builder()
                .firstPlayerScore(buildScoreDto(7, 6, 0))
                .secondPlayerScore(buildScoreDto(6, 6, 0))
                .build());
        expectedMatch.setMatchState(MatchState.TIEBREAK);

        var updatedMatch = matchScoreCalculationService.calculateScore(baseMatch, pointWinnerId);

        Assertions.assertEquals(expectedMatch, updatedMatch);
    }

    @Test
    void calculateScore_WhenFirstPlayerWinsFromGamesFiveZero_ShouldWinsSet() {
        var pointWinnerId = 1L;
        baseMatch.setMatchScoreDto(MatchScoreDto.builder()
                .firstPlayerScore(buildScoreDto(40, 5, 0))
                .secondPlayerScore(buildScoreDto(0, 0, 0))
                .build()
        );
        baseMatch.setMatchState(MatchState.REGULAR);

        expectedMatch.setMatchScoreDto(MatchScoreDto.builder()
                .firstPlayerScore(buildScoreDto(0, 0, 1))
                .secondPlayerScore(buildScoreDto(0, 0, 0))
                .build());
        expectedMatch.setMatchState(MatchState.REGULAR);

        var updatedMatch = matchScoreCalculationService.calculateScore(baseMatch, pointWinnerId);

        Assertions.assertEquals(expectedMatch, updatedMatch);
    }

    @Test
    void calculateScore_WhenSecondPlayerWinsFromPointsThirtyFortyGamesZeroFiveSetsZeroOne_ShouldFinishedMatch() {
        var pointWinnerId = 2L;
        baseMatch.setMatchScoreDto(MatchScoreDto.builder()
                .firstPlayerScore(buildScoreDto(30, 0, 0))
                .secondPlayerScore(buildScoreDto(40, 5, 1))
                .build()
        );

        expectedMatch.setMatchScoreDto(MatchScoreDto.builder()
                .firstPlayerScore(buildScoreDto(0, 0, 0))
                .secondPlayerScore(buildScoreDto(0, 6, 2))
                .build());
        expectedMatch.setMatchState(MatchState.FINISHED);

        var updatedMatch = matchScoreCalculationService.calculateScore(baseMatch, pointWinnerId);

        Assertions.assertEquals(expectedMatch, updatedMatch);
    }

    private ScoreDto buildScoreDto(int points, int games, int sets) {
        return ScoreDto.builder()
                .pointsScore(points)
                .gamesScore(games)
                .setsScore(sets)
                .build();
    }

    private ScoreDto buildScoreDto(int points, int games, int sets, boolean hasAdvantage) {
        return ScoreDto.builder()
                .pointsScore(points)
                .gamesScore(games)
                .setsScore(sets)
                .hasAdvantage(hasAdvantage)
                .build();
    }
}