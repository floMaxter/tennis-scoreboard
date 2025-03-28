package com.projects.tennisscoreboard.service;

import com.projects.tennisscoreboard.exception.IllegalStateException;
import com.projects.tennisscoreboard.utils.PropertiesUtil;
import com.projects.tennisscoreboard.utils.ScoreUtil;
import com.projects.tennisscoreboard.dto.match.ongoing.MatchProgressDto;
import com.projects.tennisscoreboard.dto.match.MatchScoreDto;
import com.projects.tennisscoreboard.dto.match.MatchState;
import com.projects.tennisscoreboard.dto.match.ongoing.OngoingMatchReadDto;
import com.projects.tennisscoreboard.dto.match.ScoreDto;

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
            default -> throw new IllegalStateException(PropertiesUtil.get("exception.illegal_state_message"));
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
        var winnerScore = matchProgressDto.getWinnerScore();
        var loserScore = matchProgressDto.getLoserScore();

        if (!winnerScore.isHasAdvantage() && !loserScore.isHasAdvantage()) {
            winnerScore.setHasAdvantage(true);
        } else if (!winnerScore.isHasAdvantage()) {
            loserScore.setHasAdvantage(false);
        } else {
            increaseGameScore(matchProgressDto);
        }
    }

    private void increaseTieBreakPointScore(MatchProgressDto matchProgressDto) {
        incrementPointScore(matchProgressDto.getWinnerScore());
        if (isTiebreakOver(matchProgressDto)) {
            increaseSetScore(matchProgressDto);
        }
    }

    private boolean isTiebreakOver(MatchProgressDto matchProgressDto) {
        var winnerPointScore = matchProgressDto.getWinnerScore().getPointsScore();
        var loserPointScore = matchProgressDto.getLoserScore().getPointsScore();
        return hasPointsToWinTiebreak(winnerPointScore)
               && hasRequiredRegularPointDifference(winnerPointScore, loserPointScore);
    }

    private boolean hasPointsToWinTiebreak(int winnerPointScore) {
        return winnerPointScore >= ScoreUtil.TIEBREAK_MIN_POINTS_TO_WIN;
    }

    private boolean hasRequiredRegularPointDifference(int winnerPointScore, int loserPointScore) {
        return winnerPointScore - loserPointScore >= ScoreUtil.MIN_POINT_DIFFERENCE_FOR_WIN_TIEBREAK;
    }

    private void increaseGameScore(MatchProgressDto matchProgressDto) {
        resetPoints(matchProgressDto);
        resetAdvantage(matchProgressDto);
        incrementGameScore(matchProgressDto.getWinnerScore());
        if (isSetOver(matchProgressDto)) {
            increaseSetScore(matchProgressDto);
        }
    }

    private boolean isSetOver(MatchProgressDto matchProgressDto) {
        var winnerGamesScore = matchProgressDto.getWinnerScore().getGamesScore();
        var loserGameScore = matchProgressDto.getLoserScore().getGamesScore();
        return hasGamesToWinSet(winnerGamesScore)
               && hasRequiredGamesDifference(winnerGamesScore, loserGameScore);
    }

    private boolean hasGamesToWinSet(int winnerGamesScore) {
        return winnerGamesScore >= ScoreUtil.SET_MIN_GAMES_TO_WIN;
    }

    private boolean hasRequiredGamesDifference(int winnerGamesScore, int loserGameScore) {
        return winnerGamesScore - loserGameScore >= ScoreUtil.MIN_GAMES_DIFFERENCE_FOR_WIN;
    }


    private void increaseSetScore(MatchProgressDto matchProgressDto) {
        var winnerScore = matchProgressDto.getWinnerScore();
        incrementSetScore(winnerScore);

        if (!isMatchFinished(winnerScore)) {
            resetPoints(matchProgressDto);
            resetGames(matchProgressDto);
        }
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

    private void resetPoints(MatchProgressDto matchProgressDto) {
        matchProgressDto.getWinnerScore().setPointsScore(0);
        matchProgressDto.getLoserScore().setPointsScore(0);
    }

    private void resetAdvantage(MatchProgressDto matchProgressDto) {
        matchProgressDto.getWinnerScore().setHasAdvantage(false);
        matchProgressDto.getLoserScore().setHasAdvantage(false);
    }

    private void resetGames(MatchProgressDto matchProgressDto) {
        matchProgressDto.getWinnerScore().setGamesScore(0);
        matchProgressDto.getLoserScore().setGamesScore(0);
    }

    public static MatchScoreCalculationService getInstance() {
        return INSTANCE;
    }
}
