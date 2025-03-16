package com.projects.tennisscoreboard.service;

import com.projects.tennisscoreboard.Utils.ScoreUtil;
import com.projects.tennisscoreboard.dto.MatchProgressDto;
import com.projects.tennisscoreboard.dto.MatchScoreDto;
import com.projects.tennisscoreboard.dto.MatchState;
import com.projects.tennisscoreboard.dto.OngoingMatchReadDto;
import com.projects.tennisscoreboard.dto.ScoreDto;

public class MatchScoreCalculationService {

    private static final MatchScoreCalculationService INSTANCE = new MatchScoreCalculationService();

    private MatchScoreCalculationService() {
    }

    public OngoingMatchReadDto calculateScore(OngoingMatchReadDto ongoingMatchReadDto, Long pointWinnerId) {
        var matchProgressDto = buildMatchProgressDto(ongoingMatchReadDto, pointWinnerId);
        increaseScore(matchProgressDto);
        return buildOngoingMatchReadDto(matchProgressDto, ongoingMatchReadDto);
    }

    private MatchProgressDto buildMatchProgressDto(OngoingMatchReadDto ongoingMatchDto, Long pointWinnerId) {
        var matchScoreDto = ongoingMatchDto.getMatchScoreDto();
        var firstPlayerScore = matchScoreDto.getFirstPlayerScore();
        var secondPlayerScore = matchScoreDto.getSecondPlayerScore();
        var matchState = ongoingMatchDto.getMatchState();

        MatchProgressDto matchProgressDto = MatchProgressDto.builder()
                .pointWinnerId(pointWinnerId)
                .matchState(matchState)
                .build();

        if (ongoingMatchDto.getFirstPlayer().getId().equals(pointWinnerId)) {
            matchProgressDto.setWinnerScore(firstPlayerScore);
            matchProgressDto.setLoserScore(secondPlayerScore);
        } else {
            matchProgressDto.setWinnerScore(secondPlayerScore);
            matchProgressDto.setLoserScore(firstPlayerScore);
        }
        return matchProgressDto;
    }

    private OngoingMatchReadDto buildOngoingMatchReadDto(MatchProgressDto matchProgressDto,
                                                         OngoingMatchReadDto baseMatchReadDto) {
        var firstPlayerId = baseMatchReadDto.getFirstPlayer().getId();
        var winnerScore = matchProgressDto.getWinnerScore();
        var loserScore = matchProgressDto.getLoserScore();

        var updatedMatchReadDto = OngoingMatchReadDto.builder()
                .firstPlayer(baseMatchReadDto.getFirstPlayer())
                .secondPlayer(baseMatchReadDto.getSecondPlayer())
                .matchState(matchProgressDto.getMatchState())
                .build();

        if (matchProgressDto.getPointWinnerId().equals(firstPlayerId)) {
            updatedMatchReadDto.setMatchScoreDto(buildMatchScoreDto(winnerScore, loserScore));
        } else {
            updatedMatchReadDto.setMatchScoreDto(buildMatchScoreDto(loserScore, winnerScore));
        }

        return updatedMatchReadDto;
    }

    private MatchScoreDto buildMatchScoreDto(ScoreDto firstPlayerScore, ScoreDto secondPlayerScore) {
        return MatchScoreDto.builder()
                .firstPlayerScore(firstPlayerScore)
                .secondPlayerScore(secondPlayerScore)
                .build();
    }

    private void increaseScore(MatchProgressDto matchProgressDto) {
        switch (matchProgressDto.getMatchState()) {
            case REGULAR -> increaseRegularPointScore(matchProgressDto);
            case DEUCE -> increaseAdvantagePointScore(matchProgressDto);
            case TIEBREAK -> increaseTieBreakPointScore(matchProgressDto);
            default -> throw new RuntimeException("Illegal match state");
        }
        updateMatchState(matchProgressDto);
    }

    private void increaseRegularPointScore(MatchProgressDto matchProgressDto) {
        var winnerScore = matchProgressDto.getWinnerScore();
        var winnerPointsScore = winnerScore.getPointsScore();

        if (winnerPointsScore == ScoreUtil.ZERO_POINT) {
            winnerScore.setPointsScore(ScoreUtil.FIRST_POINT);
        } else if (winnerPointsScore == ScoreUtil.FIRST_POINT) {
            winnerScore.setPointsScore(ScoreUtil.SECOND_POINT);
        } else if (winnerPointsScore == ScoreUtil.SECOND_POINT) {
            winnerScore.setPointsScore(ScoreUtil.THIRD_POINT);
        } else {
            increaseGameScore(matchProgressDto);
        }
    }

    private void increaseAdvantagePointScore(MatchProgressDto matchProgressDto) {
        incrementAdvantagePointScore(matchProgressDto.getWinnerScore());
        if (isDeuceFinished(matchProgressDto)) {
            increaseGameScore(matchProgressDto);
        }
    }

    private boolean isDeuceFinished(MatchProgressDto matchProgressDto) {
        var winnerAdvantagePointScore = matchProgressDto.getWinnerScore().getAdvantagePointScore();
        var loserAdvantagePointScore = matchProgressDto.getLoserScore().getAdvantagePointScore();
        return hasSufficientAdvantagePointDifference(winnerAdvantagePointScore, loserAdvantagePointScore);
    }

    private boolean hasSufficientAdvantagePointDifference(int winnerAdvantagePointScore, int loserAdvantagePointScore) {
        return winnerAdvantagePointScore - loserAdvantagePointScore == ScoreUtil.DEUCE_MIN_ADVANTAGE_DIFFERENCE;
    }

    private void increaseTieBreakPointScore(MatchProgressDto matchProgressDto) {
        incrementPointScore(matchProgressDto.getWinnerScore());
        if (isTiebreakFinished(matchProgressDto)) {
            increaseSetScore(matchProgressDto);
        }
    }

    private boolean isTiebreakFinished(MatchProgressDto matchProgressDto) {
        var winnerPointScore = matchProgressDto.getWinnerScore().getPointsScore();
        var loserPointScore = matchProgressDto.getLoserScore().getPointsScore();
        return hasMinimumWinningPoints(winnerPointScore)
               && hasSufficientRegularPointDifference(winnerPointScore, loserPointScore);
    }

    private boolean hasMinimumWinningPoints(int winnerPointScore) {
        return winnerPointScore >= ScoreUtil.TIEBREAK_MIN_POINTS_TO_WIN;
    }

    private boolean hasSufficientRegularPointDifference(int winnerPointScore, int loserPointScore) {
        return winnerPointScore - loserPointScore >= ScoreUtil.TIEBREAK_MIN_POINT_DIFFERENCE;
    }

    private void increaseGameScore(MatchProgressDto matchProgressDto) {
        resetPoints(matchProgressDto);
        resetAdvantagePoints(matchProgressDto);
        incrementGameScore(matchProgressDto.getWinnerScore());

        if (matchProgressDto.getWinnerScore().getGamesScore() >= ScoreUtil.SET_MIN_GAMES_TO_WIN) {
            increaseSetScore(matchProgressDto);
        }
    }

    private void increaseSetScore(MatchProgressDto matchProgressDto) {
        resetPoints(matchProgressDto);
        resetGames(matchProgressDto);
        incrementSetScore(matchProgressDto.getWinnerScore());
    }

    private void updateMatchState(MatchProgressDto matchProgressDto) {
        if (isMatchFinished(matchProgressDto.getWinnerScore())) {
            matchProgressDto.setMatchState(MatchState.FINISHED);
        } else if (isTieBreakScore(matchProgressDto)) {
            matchProgressDto.setMatchState(MatchState.TIEBREAK);
        } else if (isDeuceScore(matchProgressDto)) {
            matchProgressDto.setMatchState(MatchState.DEUCE);
        } else {
            matchProgressDto.setMatchState(MatchState.REGULAR);
        }
    }

    private boolean isDeuceScore(MatchProgressDto matchProgressDto) {
        var firstPointsScore = matchProgressDto.getWinnerScore().getPointsScore();
        var secondPointsScore = matchProgressDto.getLoserScore().getPointsScore();

        return firstPointsScore == ScoreUtil.THIRD_POINT
               && secondPointsScore == ScoreUtil.THIRD_POINT;
    }

    private boolean isTieBreakScore(MatchProgressDto matchProgressDto) {
        var firstGamesScore = matchProgressDto.getWinnerScore().getGamesScore();
        var secondGamesScore = matchProgressDto.getLoserScore().getGamesScore();

        return firstGamesScore == ScoreUtil.GAMES_TO_TIEBREAK
               && secondGamesScore == ScoreUtil.GAMES_TO_TIEBREAK;
    }

    private boolean isMatchFinished(ScoreDto scoreDto) {
        return scoreDto.getSetsScore() == ScoreUtil.SETS_TO_WIN;
    }

    private void incrementPointScore(ScoreDto scoreDto) {
        scoreDto.setPointsScore(scoreDto.getPointsScore() + 1);
    }

    private void incrementGameScore(ScoreDto scoreDto) {
        scoreDto.setGamesScore(scoreDto.getGamesScore() + 1);
    }

    private void incrementSetScore(ScoreDto scoreDto) {
        scoreDto.setSetsScore(scoreDto.getSetsScore() + 1);
    }

    private void incrementAdvantagePointScore(ScoreDto scoreDto) {
        scoreDto.setAdvantagePointScore(scoreDto.getAdvantagePointScore() + 1);
    }

    private void resetPoints(MatchProgressDto matchProgressDto) {
        matchProgressDto.getWinnerScore().setPointsScore(0);
        matchProgressDto.getLoserScore().setPointsScore(0);
    }

    private void resetAdvantagePoints(MatchProgressDto matchProgressDto) {
        matchProgressDto.getWinnerScore().setAdvantagePointScore(0);
        matchProgressDto.getLoserScore().setAdvantagePointScore(0);
    }

    private void resetGames(MatchProgressDto matchProgressDto) {
        matchProgressDto.getWinnerScore().setGamesScore(0);
        matchProgressDto.getLoserScore().setGamesScore(0);
    }

    public static MatchScoreCalculationService getInstance() {
        return INSTANCE;
    }
}
