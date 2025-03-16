package com.projects.tennisscoreboard.service;

import com.projects.tennisscoreboard.Utils.ScoreUtil;
import com.projects.tennisscoreboard.dto.MatchScoreDto;
import com.projects.tennisscoreboard.dto.MatchState;
import com.projects.tennisscoreboard.dto.OngoingMatchReadDto;
import com.projects.tennisscoreboard.dto.ScoreDto;
import com.projects.tennisscoreboard.entity.Player;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MatchScoreCalculationServiceTest {

    private OngoingMatchReadDto baseMatch;
    private MatchScoreCalculationService matchScoreCalculationService;

    @BeforeEach
    void prepare() {
        this.matchScoreCalculationService = MatchScoreCalculationService.getInstance();
        this.baseMatch = OngoingMatchReadDto.builder()
                .firstPlayer(new Player(1L, "Max"))
                .secondPlayer(new Player(2L, "Ivan"))
                .matchScoreDto(ScoreUtil.createInitialMatchScore())
                .matchState(MatchState.REGULAR)
                .build();
    }

    @Test
    void calculateScore_WhenInitialScoreIsZero_ShouldIncreaseFirstPlayerPoints() {
        var pointWinnerId = 1L;
        var expectedMatch = OngoingMatchReadDto.builder()
                .firstPlayer(new Player(1L, "Max"))
                .secondPlayer(new Player(2L, "Ivan"))
                .matchScoreDto(MatchScoreDto.builder()
                        .firstPlayerScore(ScoreDto.builder()
                                .pointsScore(15)
                                .gamesScore(0)
                                .setsScore(0)
                                .build())
                        .secondPlayerScore(ScoreDto.builder()
                                .pointsScore(0)
                                .gamesScore(0)
                                .setsScore(0)
                                .build())
                        .build())
                .matchState(MatchState.REGULAR)
                .build();

        var updatedMatch = matchScoreCalculationService.calculateScore(baseMatch, pointWinnerId);

        Assertions.assertEquals(expectedMatch, updatedMatch);
    }

    @Test
    void calculateScore_WhenSecondPlayerWinsFromThirtyForty_ShouldResetPointsAndIncreaseGames() {
        var pointWinnerId = 2L;
        baseMatch.setMatchScoreDto(MatchScoreDto.builder()
                .firstPlayerScore(ScoreDto.builder()
                        .pointsScore(30)
                        .gamesScore(0)
                        .setsScore(0)
                        .build())
                .secondPlayerScore(ScoreDto.builder()
                        .pointsScore(40)
                        .gamesScore(0)
                        .setsScore(0)
                        .build())
                .build()
        );

        var expectedMatch = OngoingMatchReadDto.builder()
                .firstPlayer(new Player(1L, "Max"))
                .secondPlayer(new Player(2L, "Ivan"))
                .matchScoreDto(MatchScoreDto.builder()
                        .firstPlayerScore(ScoreDto.builder()
                                .pointsScore(0)
                                .gamesScore(0)
                                .setsScore(0)
                                .build())
                        .secondPlayerScore(ScoreDto.builder()
                                .pointsScore(0)
                                .gamesScore(1)
                                .setsScore(0)
                                .build())
                        .build())
                .matchState(MatchState.REGULAR)
                .build();

        var updatedMatch = matchScoreCalculationService.calculateScore(baseMatch, pointWinnerId);

        Assertions.assertEquals(expectedMatch, updatedMatch);
    }


    @Test
    void calculateScore_WhenFirstPlayerWinsFromThirtyForty_ShouldEnterDeuce() {
        var pointWinnerId = 1L;
        baseMatch.setMatchScoreDto(MatchScoreDto.builder()
                .firstPlayerScore(ScoreDto.builder()
                        .pointsScore(30)
                        .gamesScore(0)
                        .setsScore(0)
                        .build())
                .secondPlayerScore(ScoreDto.builder()
                        .pointsScore(40)
                        .gamesScore(0)
                        .setsScore(0)
                        .build())
                .build()
        );

        var expectedMatch = OngoingMatchReadDto.builder()
                .firstPlayer(new Player(1L, "Max"))
                .secondPlayer(new Player(2L, "Ivan"))
                .matchScoreDto(MatchScoreDto.builder()
                        .firstPlayerScore(ScoreDto.builder()
                                .pointsScore(40)
                                .gamesScore(0)
                                .setsScore(0)
                                .build())
                        .secondPlayerScore(ScoreDto.builder()
                                .pointsScore(40)
                                .gamesScore(0)
                                .setsScore(0)
                                .build())
                        .build())
                .matchState(MatchState.DEUCE)
                .build();

        var updatedMatch = matchScoreCalculationService.calculateScore(baseMatch, pointWinnerId);

        Assertions.assertEquals(expectedMatch, updatedMatch);
    }

    @Test
    void calculateScore_WhenSecondPlayerWinsFromDeuce_ShouldEnterAdvantage() {
        var pointWinnerId = 2L;
        baseMatch.setMatchScoreDto(MatchScoreDto.builder()
                .firstPlayerScore(ScoreDto.builder()
                        .pointsScore(40)
                        .gamesScore(0)
                        .setsScore(0)
                        .build())
                .secondPlayerScore(ScoreDto.builder()
                        .pointsScore(40)
                        .gamesScore(0)
                        .setsScore(0)
                        .build())
                .build()
        );
        baseMatch.setMatchState(MatchState.DEUCE);

        var expectedMatch = OngoingMatchReadDto.builder()
                .firstPlayer(new Player(1L, "Max"))
                .secondPlayer(new Player(2L, "Ivan"))
                .matchScoreDto(MatchScoreDto.builder()
                        .firstPlayerScore(ScoreDto.builder()
                                .pointsScore(40)
                                .gamesScore(0)
                                .setsScore(0)
                                .build())
                        .secondPlayerScore(ScoreDto.builder()
                                .pointsScore(40)
                                .gamesScore(0)
                                .setsScore(0)
                                .advantagePointScore(1)
                                .build())
                        .build())
                .matchState(MatchState.DEUCE)
                .build();

        var updatedMatch = matchScoreCalculationService.calculateScore(baseMatch, pointWinnerId);

        Assertions.assertEquals(expectedMatch, updatedMatch);
    }

    @Test
    void calculateScore_WhenSecondPlayerWinsFromDeuceAndAdvantageFirstPlayer_ShouldEnterAdvantage() {
        var pointWinnerId = 2L;
        baseMatch.setMatchScoreDto(MatchScoreDto.builder()
                .firstPlayerScore(ScoreDto.builder()
                        .pointsScore(40)
                        .gamesScore(0)
                        .setsScore(0)
                        .advantagePointScore(1)
                        .build())
                .secondPlayerScore(ScoreDto.builder()
                        .pointsScore(40)
                        .gamesScore(0)
                        .setsScore(0)
                        .advantagePointScore(0)
                        .build())
                .build()
        );
        baseMatch.setMatchState(MatchState.DEUCE);

        var expectedMatch = OngoingMatchReadDto.builder()
                .firstPlayer(new Player(1L, "Max"))
                .secondPlayer(new Player(2L, "Ivan"))
                .matchScoreDto(MatchScoreDto.builder()
                        .firstPlayerScore(ScoreDto.builder()
                                .pointsScore(40)
                                .gamesScore(0)
                                .setsScore(0)
                                .advantagePointScore(1)
                                .build())
                        .secondPlayerScore(ScoreDto.builder()
                                .pointsScore(40)
                                .gamesScore(0)
                                .setsScore(0)
                                .advantagePointScore(1)
                                .build())
                        .build())
                .matchState(MatchState.DEUCE)
                .build();

        var updatedMatch = matchScoreCalculationService.calculateScore(baseMatch, pointWinnerId);

        Assertions.assertEquals(expectedMatch, updatedMatch);
    }

    @Test
    void calculateScore_WhenFirstPlayerHasAdvantageAndScores_FirstPlayerShouldWinGame() {
        var pointWinnerId = 1L;
        baseMatch.setMatchScoreDto(MatchScoreDto.builder()
                .firstPlayerScore(ScoreDto.builder()
                        .pointsScore(40)
                        .gamesScore(0)
                        .setsScore(0)
                        .advantagePointScore(1)
                        .build())
                .secondPlayerScore(ScoreDto.builder()
                        .pointsScore(40)
                        .gamesScore(0)
                        .setsScore(0)
                        .advantagePointScore(0)
                        .build())
                .build()
        );
        baseMatch.setMatchState(MatchState.DEUCE);

        var expectedMatch = OngoingMatchReadDto.builder()
                .firstPlayer(new Player(1L, "Max"))
                .secondPlayer(new Player(2L, "Ivan"))
                .matchScoreDto(MatchScoreDto.builder()
                        .firstPlayerScore(ScoreDto.builder()
                                .pointsScore(0)
                                .gamesScore(1)
                                .setsScore(0)
                                .advantagePointScore(0)
                                .build())
                        .secondPlayerScore(ScoreDto.builder()
                                .pointsScore(0)
                                .gamesScore(0)
                                .setsScore(0)
                                .advantagePointScore(0)
                                .build())
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
                .firstPlayerScore(ScoreDto.builder()
                        .pointsScore(40)
                        .gamesScore(5)
                        .setsScore(0)
                        .advantagePointScore(1)
                        .build())
                .secondPlayerScore(ScoreDto.builder()
                        .pointsScore(30)
                        .gamesScore(6)
                        .setsScore(0)
                        .advantagePointScore(0)
                        .build())
                .build()
        );
        baseMatch.setMatchState(MatchState.DEUCE);

        var expectedMatch = OngoingMatchReadDto.builder()
                .firstPlayer(new Player(1L, "Max"))
                .secondPlayer(new Player(2L, "Ivan"))
                .matchScoreDto(MatchScoreDto.builder()
                        .firstPlayerScore(ScoreDto.builder()
                                .pointsScore(0)
                                .gamesScore(6)
                                .setsScore(0)
                                .advantagePointScore(0)
                                .build())
                        .secondPlayerScore(ScoreDto.builder()
                                .pointsScore(0)
                                .gamesScore(6)
                                .setsScore(0)
                                .advantagePointScore(0)
                                .build())
                        .build())
                .matchState(MatchState.TIEBREAK)
                .build();

        var updatedMatch = matchScoreCalculationService.calculateScore(baseMatch, pointWinnerId);

        Assertions.assertEquals(expectedMatch, updatedMatch);
    }

    @Test
    void calculateScore_WhenFirstPlayerWinsFromGamesFiveSix_ShouldEnterTiebreak() {
        var pointWinnerId = 1L;
        baseMatch.setMatchScoreDto(MatchScoreDto.builder()
                .firstPlayerScore(ScoreDto.builder()
                        .pointsScore(40)
                        .gamesScore(5)
                        .setsScore(0)
                        .advantagePointScore(0)
                        .build())
                .secondPlayerScore(ScoreDto.builder()
                        .pointsScore(0)
                        .gamesScore(6)
                        .setsScore(0)
                        .advantagePointScore(0)
                        .build())
                .build()
        );
        baseMatch.setMatchState(MatchState.REGULAR);

        var expectedMatch = OngoingMatchReadDto.builder()
                .firstPlayer(new Player(1L, "Max"))
                .secondPlayer(new Player(2L, "Ivan"))
                .matchScoreDto(MatchScoreDto.builder()
                        .firstPlayerScore(ScoreDto.builder()
                                .pointsScore(0)
                                .gamesScore(6)
                                .setsScore(0)
                                .advantagePointScore(0)
                                .build())
                        .secondPlayerScore(ScoreDto.builder()
                                .pointsScore(0)
                                .gamesScore(6)
                                .setsScore(0)
                                .advantagePointScore(0)
                                .build())
                        .build())
                .matchState(MatchState.TIEBREAK)
                .build();

        var updatedMatch = matchScoreCalculationService.calculateScore(baseMatch, pointWinnerId);

        Assertions.assertEquals(expectedMatch, updatedMatch);
    }

    @Test
    void calculateScore_WhenFirstPlayerWinsPointAtFiveSixInTiebreak_ShouldWinSet() {
        var pointWinnerId = 1L;
        baseMatch.setMatchScoreDto(MatchScoreDto.builder()
                .firstPlayerScore(ScoreDto.builder()
                        .pointsScore(6)
                        .gamesScore(6)
                        .setsScore(0)
                        .advantagePointScore(0)
                        .build())
                .secondPlayerScore(ScoreDto.builder()
                        .pointsScore(5)
                        .gamesScore(6)
                        .setsScore(0)
                        .advantagePointScore(0)
                        .build())
                .build()
        );
        baseMatch.setMatchState(MatchState.TIEBREAK);

        var expectedMatch = OngoingMatchReadDto.builder()
                .firstPlayer(new Player(1L, "Max"))
                .secondPlayer(new Player(2L, "Ivan"))
                .matchScoreDto(MatchScoreDto.builder()
                        .firstPlayerScore(ScoreDto.builder()
                                .pointsScore(0)
                                .gamesScore(0)
                                .setsScore(1)
                                .advantagePointScore(0)
                                .build())
                        .secondPlayerScore(ScoreDto.builder()
                                .pointsScore(0)
                                .gamesScore(0)
                                .setsScore(0)
                                .advantagePointScore(0)
                                .build())
                        .build())
                .matchState(MatchState.REGULAR)
                .build();

        var updatedMatch = matchScoreCalculationService.calculateScore(baseMatch, pointWinnerId);

        Assertions.assertEquals(expectedMatch, updatedMatch);
    }

    @Test
    void calculateScore_WhenFirstPlayerWinsFromTiebreakAndPointsSixSix_ShouldEnterTiebreak() {
        var pointWinnerId = 1L;
        baseMatch.setMatchScoreDto(MatchScoreDto.builder()
                .firstPlayerScore(ScoreDto.builder()
                        .pointsScore(6)
                        .gamesScore(6)
                        .setsScore(0)
                        .advantagePointScore(0)
                        .build())
                .secondPlayerScore(ScoreDto.builder()
                        .pointsScore(6)
                        .gamesScore(6)
                        .setsScore(0)
                        .advantagePointScore(0)
                        .build())
                .build()
        );
        baseMatch.setMatchState(MatchState.TIEBREAK);

        var expectedMatch = OngoingMatchReadDto.builder()
                .firstPlayer(new Player(1L, "Max"))
                .secondPlayer(new Player(2L, "Ivan"))
                .matchScoreDto(MatchScoreDto.builder()
                        .firstPlayerScore(ScoreDto.builder()
                                .pointsScore(7)
                                .gamesScore(6)
                                .setsScore(0)
                                .advantagePointScore(0)
                                .build())
                        .secondPlayerScore(ScoreDto.builder()
                                .pointsScore(6)
                                .gamesScore(6)
                                .setsScore(0)
                                .advantagePointScore(0)
                                .build())
                        .build())
                .matchState(MatchState.TIEBREAK)
                .build();

        var updatedMatch = matchScoreCalculationService.calculateScore(baseMatch, pointWinnerId);

        Assertions.assertEquals(expectedMatch, updatedMatch);
    }

    @Test
    void calculateScore_WhenSecondPlayerWinsFromPointsThirtyFortyGamesZeroSixSetsZeroOne_ShouldFinishedMatch() {
        var pointWinnerId = 2L;
        baseMatch.setMatchScoreDto(MatchScoreDto.builder()
                .firstPlayerScore(ScoreDto.builder()
                        .pointsScore(30)
                        .gamesScore(0)
                        .setsScore(0)
                        .build())
                .secondPlayerScore(ScoreDto.builder()
                        .pointsScore(40)
                        .gamesScore(6)
                        .setsScore(1)
                        .build())
                .build()
        );

        var expectedMatch = OngoingMatchReadDto.builder()
                .firstPlayer(new Player(1L, "Max"))
                .secondPlayer(new Player(2L, "Ivan"))
                .matchScoreDto(MatchScoreDto.builder()
                        .firstPlayerScore(ScoreDto.builder()
                                .pointsScore(0)
                                .gamesScore(0)
                                .setsScore(0)
                                .build())
                        .secondPlayerScore(ScoreDto.builder()
                                .pointsScore(0)
                                .gamesScore(0)
                                .setsScore(2)
                                .build())
                        .build())
                .matchState(MatchState.FINISHED)
                .build();

        var updatedMatch = matchScoreCalculationService.calculateScore(baseMatch, pointWinnerId);

        Assertions.assertEquals(expectedMatch, updatedMatch);
    }
}