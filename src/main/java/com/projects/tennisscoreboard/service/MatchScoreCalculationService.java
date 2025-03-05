package com.projects.tennisscoreboard.service;

import com.projects.tennisscoreboard.dto.MatchScoreDto;
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
        increaseScore(ongoingMatchDto, pointWinnerId);
        ongoingMatchesService.updateOngoingMatch(matchId, ongoingMatchDto);
    }

    private void increaseScore(OngoingMatchDto ongoingMatchDto, Long pointWinnerId) {
        var matchScoreDto = ongoingMatchDto.matchScoreDto();
        if (isRegularScore(matchScoreDto)) {
            increaseRegularScore(ongoingMatchDto, pointWinnerId);
        } else {
            increaseAdvantageScore(ongoingMatchDto, pointWinnerId);
        }
    }

    private boolean isRegularScore(MatchScoreDto matchScoreDto) {
        var firstPointsScore = matchScoreDto.firstPlayerScore().getPointsScore();
        var secondPointsScore = matchScoreDto.secondPlayerScore().getPointsScore();
        return firstPointsScore < 40 || secondPointsScore < 40;
    }

    private void increaseRegularScore(OngoingMatchDto ongoingMatchDto, Long pointWinnerId) {
        var matchScoreDto = ongoingMatchDto.matchScoreDto();
        var firstScore = matchScoreDto.firstPlayerScore();
        var secondScore = matchScoreDto.secondPlayerScore();
        if (ongoingMatchDto.firstPlayerId().equals(pointWinnerId)) {
            increasePointScore(firstScore, secondScore);
        } else {
            increasePointScore(secondScore, firstScore);
        }
    }

    private void increaseAdvantageScore(OngoingMatchDto ongoingMatchDto, Long pointWinnerId) {
        var matchScoreDto = ongoingMatchDto.matchScoreDto();
        var firstScore = matchScoreDto.firstPlayerScore();
        var secondScore = matchScoreDto.secondPlayerScore();
        if (ongoingMatchDto.firstPlayerId().equals(pointWinnerId)) {
            increaseAdvantagePoint(firstScore, secondScore);
        } else {
            increaseAdvantagePoint(secondScore, firstScore);
        }
    }

    private void increasePointScore(ScoreDto winnerScore, ScoreDto loserScore) {
        var pointsScore = winnerScore.getPointsScore();
        if (pointsScore == 0) {
            winnerScore.setPointsScore(15);
        } else if (pointsScore == 15) {
            winnerScore.setPointsScore(30);
        } else if (pointsScore == 30) {
            winnerScore.setPointsScore(40);
        } else {
            increaseGamesScore(winnerScore, loserScore);
        }
    }

    private void increaseAdvantagePoint(ScoreDto winnerScore, ScoreDto loserScore) {
        if (isCrucialAdvantagePoint(winnerScore, loserScore)) {
            increaseGamesScore(winnerScore, loserScore);
        } else {
            winnerScore.setPointsScore(winnerScore.getPointsScore() + 1);
        }
    }

    private boolean isCrucialAdvantagePoint(ScoreDto winnerScore, ScoreDto loserScore) {
        return winnerScore.getPointsScore() - loserScore.getPointsScore() == 1;
    }

    private void increaseGamesScore(ScoreDto winnerScore, ScoreDto loserScore) {
        winnerScore.setPointsScore(0);
        loserScore.setPointsScore(0);
        winnerScore.setGamesScore(winnerScore.getGamesScore() + 1);

        if (winnerScore.getGamesScore() > 6) {
            increaseSetsScore(winnerScore, loserScore);
        }
    }

    private void increaseSetsScore(ScoreDto winnerScore, ScoreDto loserScore) {
        winnerScore.setGamesScore(0);
        loserScore.setGamesScore(0);
        winnerScore.setSetsScore(winnerScore.getSetsScore() + 1);

        if (winnerScore.getSetsScore() == 2) {
            setWinningValues(winnerScore, loserScore);
        }
    }

    private void setWinningValues(ScoreDto winnerScore, ScoreDto loserScore) {
        winnerScore.setPointsScore(9);
        winnerScore.setGamesScore(9);
        winnerScore.setSetsScore(9);

        loserScore.setPointsScore(0);
        loserScore.setGamesScore(0);
        loserScore.setSetsScore(0);
    }

    public static MatchScoreCalculationService getInstance() {
        return INSTANCE;
    }
}
