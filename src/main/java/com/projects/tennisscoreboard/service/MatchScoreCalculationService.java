package com.projects.tennisscoreboard.service;

import com.projects.tennisscoreboard.Utils.ScoreUtil;
import com.projects.tennisscoreboard.dto.MatchProgressDto;
import com.projects.tennisscoreboard.dto.MatchScoreDto;
import com.projects.tennisscoreboard.dto.MatchState;
import com.projects.tennisscoreboard.dto.OngoingMatchDto;
import com.projects.tennisscoreboard.dto.OngoingMatchReadDto;
import com.projects.tennisscoreboard.dto.OngoingMatchUpdateDto;
import com.projects.tennisscoreboard.dto.ScoreDto;

public class MatchScoreCalculationService {

    private final OngoingMatchesService ongoingMatchesService;
    private static final MatchScoreCalculationService INSTANCE = new MatchScoreCalculationService();

    private MatchScoreCalculationService() {
        ongoingMatchesService = OngoingMatchesService.getInstance();
    }

    public void calculateScore(OngoingMatchUpdateDto ongoingMatchUpdateDto) {
        //TODO: validate

        var matchId = ongoingMatchUpdateDto.matchId();
        var pointWinnerId = Long.valueOf(ongoingMatchUpdateDto.pointWinnerIdStr());

        var ongoingMatchReadDto = ongoingMatchesService.findByUUID(matchId);
        var matchProgressDto = buildMatchProgressDto(ongoingMatchReadDto, pointWinnerId);

        increaseScore(matchProgressDto);
        var ongoingMatchDto = buildOngoingMatchDto(matchProgressDto, ongoingMatchReadDto);

        ongoingMatchesService.updateOngoingMatch(matchId, ongoingMatchDto);
    }

    private MatchProgressDto buildMatchProgressDto(OngoingMatchReadDto ongoingMatchDto, Long pointWinnerId) {
        var matchScoreDto = ongoingMatchDto.getMatchScoreDto();
        var firstPlayerScore = matchScoreDto.getFirstPlayerScore();
        var secondPlayerScore = matchScoreDto.getSecondPlayerScore();
        var advantagePlayerId = matchScoreDto.getAdvantagePlayerId();
        var matchState = ongoingMatchDto.getMatchState();

        MatchProgressDto matchProgressDto = MatchProgressDto.builder()
                .pointWinnerId(pointWinnerId)
                .matchState(matchState)
                .advantagePlayerId(advantagePlayerId)
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

    private OngoingMatchDto buildOngoingMatchDto(MatchProgressDto matchProgressDto,
                                                 OngoingMatchReadDto baseOngoingMatchDto) {
        var firstPlayerId = baseOngoingMatchDto.getFirstPlayer().getId();
        var secondPlayerId = baseOngoingMatchDto.getSecondPlayer().getId();
        var winnerScore = matchProgressDto.getWinnerScore();
        var loserScore = matchProgressDto.getLoserScore();
        var advantagePlayerId = matchProgressDto.getAdvantagePlayerId();

        var updatedOngoingMatchDto = OngoingMatchDto.builder()
                .firstPlayerId(firstPlayerId)
                .secondPlayerId(secondPlayerId)
                .matchState(matchProgressDto.getMatchState())
                .build();

        if (matchProgressDto.getPointWinnerId().equals(firstPlayerId)) {
            updatedOngoingMatchDto.setMatchScoreDto(MatchScoreDto.builder()
                    .firstPlayerScore(winnerScore)
                    .secondPlayerScore(loserScore)
                    .advantagePlayerId(advantagePlayerId)
                    .build());
        } else {
            updatedOngoingMatchDto.setMatchScoreDto(MatchScoreDto.builder()
                    .firstPlayerScore(loserScore)
                    .secondPlayerScore(winnerScore)
                    .advantagePlayerId(advantagePlayerId)
                    .build());
        }
        return updatedOngoingMatchDto;
    }

    private void increaseScore(MatchProgressDto matchProgressDto) {
        switch (matchProgressDto.getMatchState()) {
            case REGULAR -> increaseRegularPointScore(matchProgressDto);
            case DEUCE -> increaseAdvantagePointScore(matchProgressDto);
            case TIEBREAK -> increaseTieBreakPointScore(matchProgressDto);
            default -> throw new RuntimeException("Illegal match state");
        }
    }

    private void increaseRegularPointScore(MatchProgressDto matchProgressDto) {
        if (isRegularPoint(matchProgressDto.getWinnerScore())) {
            increaseRegularPoint(matchProgressDto);
        } else {
            increaseGameScore(matchProgressDto);
        }
        updateMatchState(matchProgressDto);
    }

    private boolean isRegularPoint(ScoreDto score) {
        return score.getPointsScore() >= ScoreUtil.ZERO_POINT
               && score.getPointsScore() < ScoreUtil.THIRD_POINT;
    }

    private void increaseRegularPoint(MatchProgressDto matchProgressDto) {
        var winnerScore = matchProgressDto.getWinnerScore();
        var winnerPointsScore = winnerScore.getPointsScore();

        if (winnerPointsScore == ScoreUtil.ZERO_POINT) {
            winnerScore.setPointsScore(ScoreUtil.FIRST_POINT);
        } else if (winnerPointsScore == ScoreUtil.FIRST_POINT) {
            winnerScore.setPointsScore(ScoreUtil.SECOND_POINT);
        } else if (winnerPointsScore == ScoreUtil.SECOND_POINT) {
            winnerScore.setPointsScore(ScoreUtil.THIRD_POINT);
        }
    }

    private void increaseAdvantagePointScore(MatchProgressDto matchProgressDto) {
        if (isAdvantagePlayerScoring(matchProgressDto)) {
            increaseGameScore(matchProgressDto);
            matchProgressDto.setAdvantagePlayerId(null);
        } else {
            increaseAdvantagePoint(matchProgressDto);
        }
        updateMatchState(matchProgressDto);
    }

    private boolean isAdvantagePlayerScoring(MatchProgressDto matchProgressDto) {
        return matchProgressDto.getAdvantagePlayerId() != null &&
               matchProgressDto.getAdvantagePlayerId().equals(matchProgressDto.getPointWinnerId());
    }

    private void increaseAdvantagePoint(MatchProgressDto matchProgressDto) {
        if (matchProgressDto.getAdvantagePlayerId() == null) {
            matchProgressDto.setAdvantagePlayerId(matchProgressDto.getPointWinnerId());
        } else {
            matchProgressDto.setAdvantagePlayerId(null);
        }
    }

    private void increaseTieBreakPointScore(MatchProgressDto matchProgressDto) {
        incrementPointScore(matchProgressDto.getWinnerScore());
        if (isTiebreakFinished(matchProgressDto)) {
            increaseSetScore(matchProgressDto);
        }
        updateMatchState(matchProgressDto);
    }

    private boolean isTiebreakFinished(MatchProgressDto matchProgressDto) {
        var winnerPointScore = matchProgressDto.getWinnerScore().getPointsScore();
        var loserPointScore = matchProgressDto.getLoserScore().getPointsScore();
        return hasMinimumWinningPoints(winnerPointScore)
               && hasSufficientPointDifference(winnerPointScore, loserPointScore);
    }

    private boolean hasSufficientPointDifference(int winnerPointScore, int loserPointScore) {
        return winnerPointScore - loserPointScore >= ScoreUtil.TIEBREAK_MIN_POINT_DIFFERENCE;
    }

    private boolean hasMinimumWinningPoints(int winnerPointScore) {
        return winnerPointScore >= ScoreUtil.TIEBREAK_MIN_POINTS_TO_WIN;
    }

    private void increaseGameScore(MatchProgressDto matchProgressDto) {
        resetPoints(matchProgressDto);
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

    private void resetPoints(MatchProgressDto matchProgressDto) {
        matchProgressDto.getWinnerScore().setPointsScore(0);
        matchProgressDto.getLoserScore().setPointsScore(0);
    }

    private void resetGames(MatchProgressDto matchProgressDto) {
        matchProgressDto.getWinnerScore().setGamesScore(0);
        matchProgressDto.getLoserScore().setGamesScore(0);
    }

    public static MatchScoreCalculationService getInstance() {
        return INSTANCE;
    }
}
