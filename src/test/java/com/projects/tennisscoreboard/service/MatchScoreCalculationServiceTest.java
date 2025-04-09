package com.projects.tennisscoreboard.service;

import com.projects.tennisscoreboard.dto.match.MatchScoreDto;
import com.projects.tennisscoreboard.dto.match.MatchState;
import com.projects.tennisscoreboard.dto.match.ScoreDto;
import com.projects.tennisscoreboard.dto.match.ongoing.OngoingMatchDto;
import com.projects.tennisscoreboard.dto.player.PlayerDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class MatchScoreCalculationServiceTest {

    private static final PlayerDto FIRST_PLAYER = new PlayerDto(1L, "Max");
    private static final PlayerDto SECOND_PLAYER = new PlayerDto(2L, "Ivan");

    private OngoingMatchDto baseMatch;
    private OngoingMatchDto expectedMatch;
    private final MatchScoreCalculationService matchScoreCalculationService = MatchScoreCalculationService.getInstance();

    @Test
    void calculateScore_WhenFirstPlayerWinsFromZeroZero_ShouldIncreaseFirstPlayerPoints() {
        var pointWinnerId = 1L;
        baseMatch = buildOngoingMatch(
                buildScoreDto(0, 0, 0),
                buildScoreDto(0, 0, 0),
                MatchState.REGULAR
        );
        expectedMatch = buildOngoingMatch(
                buildScoreDto(15, 0, 0),
                buildScoreDto(0, 0, 0),
                MatchState.REGULAR
        );

        var updatedMatch = matchScoreCalculationService.calculateScore(baseMatch, pointWinnerId);

        Assertions.assertEquals(expectedMatch, updatedMatch);
    }

    @Test
    void calculateScore_WhenSecondPlayerWinsFromThirtyForty_ShouldResetPointsAndIncreaseGames() {
        var pointWinnerId = 2L;
        baseMatch = buildOngoingMatch(
                buildScoreDto(30, 0, 0),
                buildScoreDto(40, 0, 0),
                MatchState.REGULAR
        );
        expectedMatch = buildOngoingMatch(
                buildScoreDto(0, 0, 0),
                buildScoreDto(0, 1, 0),
                MatchState.REGULAR
        );

        var updatedMatch = matchScoreCalculationService.calculateScore(baseMatch, pointWinnerId);

        Assertions.assertEquals(expectedMatch, updatedMatch);
    }


    @Test
    void calculateScore_WhenFirstPlayerWinsFromThirtyForty_ShouldEnterDeuce() {
        var pointWinnerId = 1L;
        baseMatch = buildOngoingMatch(
                buildScoreDto(30, 0, 0),
                buildScoreDto(40, 0, 0),
                MatchState.REGULAR
        );
        expectedMatch = buildOngoingMatch(
                buildScoreDto(40, 0, 0),
                buildScoreDto(40, 0, 0),
                MatchState.DEUCE
        );

        var updatedMatch = matchScoreCalculationService.calculateScore(baseMatch, pointWinnerId);

        Assertions.assertEquals(expectedMatch, updatedMatch);
    }

    @Test
    void calculateScore_WhenSecondPlayerWinsFromDeuce_ShouldEnterAdvantage() {
        var pointWinnerId = 2L;
        baseMatch = buildOngoingMatch(
                buildScoreDto(40, 0, 0, false),
                buildScoreDto(40, 0, 0, false),
                MatchState.DEUCE
        );
        expectedMatch = buildOngoingMatch(
                buildScoreDto(40, 0, 0, false),
                buildScoreDto(40, 0, 0, true),
                MatchState.DEUCE
        );

        var updatedMatch = matchScoreCalculationService.calculateScore(baseMatch, pointWinnerId);

        Assertions.assertEquals(expectedMatch, updatedMatch);
    }

    @Test
    void calculateScore_WhenSecondPlayerWinsFromDeuceAndAdvantageFirstPlayer_ShouldEnterAdvantage() {
        var pointWinnerId = 2L;
        baseMatch = buildOngoingMatch(
                buildScoreDto(40, 0, 0, true),
                buildScoreDto(40, 0, 0, false),
                MatchState.DEUCE
        );
        expectedMatch = buildOngoingMatch(
                buildScoreDto(40, 0, 0, false),
                buildScoreDto(40, 0, 0, false),
                MatchState.DEUCE
        );

        var updatedMatch = matchScoreCalculationService.calculateScore(baseMatch, pointWinnerId);

        Assertions.assertEquals(expectedMatch, updatedMatch);
    }

    @Test
    void calculateScore_WhenFirstPlayerHasAdvantageAndScores_FirstPlayerShouldWinGame() {
        var pointWinnerId = 1L;
        baseMatch = buildOngoingMatch(
                buildScoreDto(40, 0, 0, true),
                buildScoreDto(40, 0, 0, false),
                MatchState.DEUCE
        );
        expectedMatch = buildOngoingMatch(
                buildScoreDto(0, 1, 0, false),
                buildScoreDto(0, 0, 0, false),
                MatchState.REGULAR
        );

        var updatedMatch = matchScoreCalculationService.calculateScore(baseMatch, pointWinnerId);

        Assertions.assertEquals(expectedMatch, updatedMatch);
    }

    @Test
    void calculateScore_WhenFirstPlayerWinsFromDeuceAndAdvantageFirstPlayerAndGamesFiveSix_ShouldEnterTiebreak() {
        var pointWinnerId = 1L;
        baseMatch = buildOngoingMatch(
                buildScoreDto(40, 5, 0, true),
                buildScoreDto(30, 6, 0, false),
                MatchState.DEUCE
        );
        expectedMatch = buildOngoingMatch(
                buildScoreDto(0, 6, 0, false),
                buildScoreDto(0, 6, 0, false),
                MatchState.TIEBREAK
        );

        var updatedMatch = matchScoreCalculationService.calculateScore(baseMatch, pointWinnerId);

        Assertions.assertEquals(expectedMatch, updatedMatch);
    }

    @Test
    void calculateScore_WhenFirstPlayerWinsFromGamesFiveSix_ShouldEnterTiebreak() {
        var pointWinnerId = 1L;
        baseMatch = buildOngoingMatch(
                buildScoreDto(40, 5, 0),
                buildScoreDto(0, 6, 0),
                MatchState.REGULAR
        );
        expectedMatch = buildOngoingMatch(
                buildScoreDto(0, 6, 0),
                buildScoreDto(0, 6, 0),
                MatchState.TIEBREAK
        );

        var updatedMatch = matchScoreCalculationService.calculateScore(baseMatch, pointWinnerId);

        Assertions.assertEquals(expectedMatch, updatedMatch);
    }

    @Test
    void calculateScore_WhenFirstPlayerWinsPointAtFiveSixInTiebreak_ShouldWinSet() {
        var pointWinnerId = 1L;
        baseMatch = buildOngoingMatch(
                buildScoreDto(6, 6, 0),
                buildScoreDto(5, 6, 0),
                MatchState.TIEBREAK
        );
        expectedMatch = buildOngoingMatch(
                buildScoreDto(0, 0, 1),
                buildScoreDto(0, 0, 0),
                MatchState.REGULAR
        );

        var updatedMatch = matchScoreCalculationService.calculateScore(baseMatch, pointWinnerId);

        Assertions.assertEquals(expectedMatch, updatedMatch);
    }

    @Test
    void calculateScore_WhenFirstPlayerWinsFromTiebreakAndPointsSixSix_ShouldEnterTiebreak() {
        var pointWinnerId = 1L;
        baseMatch = buildOngoingMatch(
                buildScoreDto(6, 6, 0),
                buildScoreDto(6, 6, 0),
                MatchState.TIEBREAK
        );
        expectedMatch = buildOngoingMatch(
                buildScoreDto(7, 6, 0),
                buildScoreDto(6, 6, 0),
                MatchState.TIEBREAK
        );

        var updatedMatch = matchScoreCalculationService.calculateScore(baseMatch, pointWinnerId);

        Assertions.assertEquals(expectedMatch, updatedMatch);
    }

    @Test
    void calculateScore_WhenFirstPlayerWinsFromGamesFiveZero_ShouldWinsSet() {
        var pointWinnerId = 1L;
        baseMatch = buildOngoingMatch(
                buildScoreDto(40, 5, 0),
                buildScoreDto(0, 0, 0),
                MatchState.REGULAR
        );
        expectedMatch = buildOngoingMatch(
                buildScoreDto(0, 0, 1),
                buildScoreDto(0, 0, 0),
                MatchState.REGULAR
        );

        var updatedMatch = matchScoreCalculationService.calculateScore(baseMatch, pointWinnerId);

        Assertions.assertEquals(expectedMatch, updatedMatch);
    }

    @Test
    void calculateScore_WhenSecondPlayerWinsFromPointsThirtyFortyGamesZeroFiveSetsZeroOne_ShouldFinishedMatch() {
        var pointWinnerId = 2L;
        baseMatch = buildOngoingMatch(
                buildScoreDto(30, 0, 0),
                buildScoreDto(40, 5, 1),
                MatchState.REGULAR
        );
        expectedMatch = buildOngoingMatch(
                buildScoreDto(0, 0, 0),
                buildScoreDto(0, 6, 2),
                MatchState.FINISHED
        );

        var updatedMatch = matchScoreCalculationService.calculateScore(baseMatch, pointWinnerId);

        Assertions.assertEquals(expectedMatch, updatedMatch);
    }

    private OngoingMatchDto buildOngoingMatch(ScoreDto firstPlayerScore,
                                              ScoreDto secondPlayerScore,
                                              MatchState matchState) {
        return OngoingMatchDto.builder()
                .firstPlayer(FIRST_PLAYER)
                .secondPlayer(SECOND_PLAYER)
                .matchScoreDto(MatchScoreDto.builder()
                        .firstPlayerScore(firstPlayerScore)
                        .secondPlayerScore(secondPlayerScore)
                        .build())
                .matchState(matchState)
                .build();
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