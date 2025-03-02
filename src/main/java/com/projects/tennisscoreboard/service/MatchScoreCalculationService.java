package com.projects.tennisscoreboard.service;

import com.projects.tennisscoreboard.dto.OngoingMatchDto;
import com.projects.tennisscoreboard.mapper.OngoingMatchReadToOngoingMapper;

import java.util.UUID;

public class MatchScoreCalculationService {

    private final OngoingMatchesService ongoingMatchesService;
    private final OngoingMatchReadToOngoingMapper ongoingMatchReadToOngoingMapper;
    private static final MatchScoreCalculationService INSTANCE = new MatchScoreCalculationService();

    private MatchScoreCalculationService() {
        ongoingMatchesService = OngoingMatchesService.getInstance();
        ongoingMatchReadToOngoingMapper = OngoingMatchReadToOngoingMapper.getInstance();
    }

    public void calculateScore(String matchId, String winnerIdStr) {
        //TODO: validate

        var ongoingMatchDto = ongoingMatchReadToOngoingMapper.mapFrom(ongoingMatchesService.findByUUID(matchId));
        var pointWinnerId = Long.valueOf(winnerIdStr);
        updateScore(ongoingMatchDto, pointWinnerId);
        ongoingMatchesService.updateOngoingMatch(matchId, ongoingMatchDto);
    }

    private void updateScore(OngoingMatchDto ongoingMatchDto, Long pointWinnerId) {
        var matchScoreDto = ongoingMatchDto.matchScoreDto();
        var pointsScore = matchScoreDto.pointsScore();
        var gamesScore = matchScoreDto.gamesScore();
        var setsScore = matchScoreDto.setsScore();

        if (ongoingMatchDto.firstPlayerId().equals(pointWinnerId)) {
            pointsScore.setFirstPlayerPoints(pointsScore.getFirstPlayerPoints() + 1);
            if (pointsScore.getFirstPlayerPoints() > 1) {
                pointsScore.setFirstPlayerPoints(0);
                gamesScore.setFirstPlayerGames(gamesScore.getFirstPlayerGames() + 1);
            }
            if (gamesScore.getFirstPlayerGames() > 1) {
                gamesScore.setFirstPlayerGames(0);
                setsScore.setFirstPlayerSets(setsScore.getFirstPlayerSets() + 1);
            }
            if (setsScore.getFirstPlayerSets() > 1) {
                setsScore.setFirstPlayerSets(0);
            }
        } else {
            pointsScore.setSecondPlayerPoints(pointsScore.getSecondPlayerPoints() + 1);
            if (pointsScore.getSecondPlayerPoints() > 1) {
                pointsScore.setSecondPlayerPoints(0);
                gamesScore.setSecondPlayerGames(gamesScore.getSecondPlayerGames() + 1);
            }
            if (gamesScore.getSecondPlayerGames() > 1) {
                gamesScore.setSecondPlayerGames(0);
                setsScore.setSecondPlayerSets(setsScore.getSecondPlayerSets() + 1);
            }
            if (setsScore.getSecondPlayerSets() > 1) {
                setsScore.setSecondPlayerSets(0);
            }
        }
    }

    public static MatchScoreCalculationService getInstance() {
        return INSTANCE;
    }
}
