package com.projects.tennisscoreboard.service;

import com.projects.tennisscoreboard.dto.OngoingMatchDto;
import com.projects.tennisscoreboard.dto.ScoreDto;
import com.projects.tennisscoreboard.mapper.OngoingMatchReadToOngoingMapper;

public class MatchScoreCalculationService {

    private final OngoingMatchesService ongoingMatchesService;
    private final OngoingMatchReadToOngoingMapper ongoingMatchReadToOngoingMapper;
    private static final MatchScoreCalculationService INSTANCE = new MatchScoreCalculationService();

    private MatchScoreCalculationService() {
        ongoingMatchesService = OngoingMatchesService.getInstance();
        ongoingMatchReadToOngoingMapper = OngoingMatchReadToOngoingMapper.getInstance();
    }

    public void calculateScore(String matchId, String pointWinnerIdStr) {
        //TODO: validate

        var ongoingMatchDto = ongoingMatchReadToOngoingMapper.mapFrom(ongoingMatchesService.findByUUID(matchId));
        var pointWinnerId = Long.valueOf(pointWinnerIdStr);
        processIncreaseScore(ongoingMatchDto, pointWinnerId);
        ongoingMatchesService.updateOngoingMatch(matchId, ongoingMatchDto);
    }

    private void processIncreaseScore(OngoingMatchDto ongoingMatchDto, Long pointWinnerId) {
        var matchScoreDto = ongoingMatchDto.matchScoreDto();
        var firstScore = matchScoreDto.firstPlayerScore();
        var secondScore = matchScoreDto.secondPlayerScore();
        if (ongoingMatchDto.firstPlayerId().equals(pointWinnerId)) {
            increaseScore(firstScore, secondScore);
        } else {
            increaseScore(secondScore, firstScore);
        }
    }

    private void increaseScore(ScoreDto winnerScore, ScoreDto loserScore) {
        if (isAdvantageScore(winnerScore, loserScore)) {
            increaseAdvantagePointScore(winnerScore, loserScore);
        } else if (isTieBreak(winnerScore, loserScore)) {
            increaseTieBreakPointScore(winnerScore, loserScore);
        } else {
            increaseRegularPointScore(winnerScore, loserScore);
        }
    }

    private boolean isAdvantageScore(ScoreDto winnerScore, ScoreDto loserScore) {
        var firstPointsScore = winnerScore.getPointsScore();
        var secondPointsScore = loserScore.getPointsScore();
        return firstPointsScore >= 40 && secondPointsScore >= 40;
    }

    private boolean isTieBreak(ScoreDto winnerScore, ScoreDto loserScore) {
        var firstGamesScore = winnerScore.getGamesScore();
        var secondGamesScore = loserScore.getGamesScore();
        return firstGamesScore == 6 && secondGamesScore == 6;
    }

    private void increaseRegularPointScore(ScoreDto winnerScore, ScoreDto loserScore) {
        var pointsScore = winnerScore.getPointsScore();
        if (pointsScore == 0) {
            winnerScore.setPointsScore(15);
        } else if (pointsScore == 15) {
            winnerScore.setPointsScore(30);
        } else if (pointsScore == 30) {
            winnerScore.setPointsScore(40);
        } else {
            increaseGameScore(winnerScore, loserScore);
        }
    }

    private void increaseAdvantagePointScore(ScoreDto winnerScore, ScoreDto loserScore) {
        if (isAdvantagePoint(winnerScore, loserScore)) {
            increaseGameScore(winnerScore, loserScore);
        } else {
            incrementPointScore(winnerScore);
        }
    }

    private boolean isAdvantagePoint(ScoreDto winnerScore, ScoreDto loserScore) {
        return winnerScore.getPointsScore() - loserScore.getPointsScore() == 1;
    }

    private void increaseTieBreakPointScore(ScoreDto winnerScore, ScoreDto loserScore) {
        if (isCrucialTieBreakPoint(winnerScore, loserScore)) {
            increaseSetScore(winnerScore, loserScore);
        } else {
            incrementPointScore(winnerScore);
        }
    }

    private boolean isCrucialTieBreakPoint(ScoreDto winnerScore, ScoreDto loserScore) {
        return isAdvantagePoint(winnerScore, loserScore)
                && winnerScore.getPointsScore() >= 6;
    }

    private void increaseGameScore(ScoreDto winnerScore, ScoreDto loserScore) {
        resetPoints(winnerScore, loserScore);
        incrementGameScore(winnerScore);

        if (winnerScore.getGamesScore() > 6) {
            increaseSetScore(winnerScore, loserScore);
        }
    }

    private void increaseSetScore(ScoreDto winnerScore, ScoreDto loserScore) {
        resetPoints(winnerScore, loserScore);
        resetGames(winnerScore, loserScore);
        incrementSetScore(winnerScore);

        if (isWinningMatch(winnerScore, 2)) {
            setWinningValues(winnerScore, loserScore);
        }
    }

    private boolean isWinningMatch(ScoreDto scoreDto, int requiredSetsToWin) {
        return scoreDto.getSetsScore() == requiredSetsToWin;
    }

    private void setWinningValues(ScoreDto winnerScore, ScoreDto loserScore) {
        winnerScore.setPointsScore(9);
        winnerScore.setGamesScore(9);
        winnerScore.setSetsScore(9);
        resetScore(loserScore);
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

    private void resetPoints(ScoreDto winnerScore, ScoreDto loserScore) {
        winnerScore.setPointsScore(0);
        loserScore.setPointsScore(0);
    }

    private void resetGames(ScoreDto winnerScore, ScoreDto loserScore) {
        winnerScore.setGamesScore(0);
        loserScore.setGamesScore(0);
    }

    private void resetScore(ScoreDto scoreDto) {
        scoreDto.setPointsScore(0);
        scoreDto.setGamesScore(0);
        scoreDto.setSetsScore(0);
    }

    public static MatchScoreCalculationService getInstance() {
        return INSTANCE;
    }
}
