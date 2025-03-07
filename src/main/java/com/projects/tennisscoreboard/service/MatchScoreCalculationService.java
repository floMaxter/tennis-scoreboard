package com.projects.tennisscoreboard.service;

import com.projects.tennisscoreboard.dto.MatchProgressDto;
import com.projects.tennisscoreboard.dto.MatchScoreDto;
import com.projects.tennisscoreboard.dto.MatchState;
import com.projects.tennisscoreboard.dto.OngoingMatchDto;
import com.projects.tennisscoreboard.dto.OngoingMatchReadDto;
import com.projects.tennisscoreboard.dto.ScoreDto;

public class MatchScoreCalculationService {

    private final OngoingMatchesService ongoingMatchesService;
    private static final MatchScoreCalculationService INSTANCE = new MatchScoreCalculationService();

    private MatchScoreCalculationService() {
        ongoingMatchesService = OngoingMatchesService.getInstance();
    }

    public void calculateScore(String matchId, String pointWinnerIdStr) {
        //TODO: validate

        var ongoingMatchReadDto = ongoingMatchesService.findByUUID(matchId);
        var pointWinnerId = Long.valueOf(pointWinnerIdStr);
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
        MatchProgressDto matchProgressDto;

        if (ongoingMatchDto.getFirstPlayer().getId().equals(pointWinnerId)) {
            matchProgressDto = MatchProgressDto.builder()
                    .pointWinnerId(pointWinnerId)
                    .winnerScore(firstPlayerScore)
                    .loserScore(secondPlayerScore)
                    .matchState(matchState)
                    .advantagePlayerId(advantagePlayerId)
                    .build();
        } else {
            matchProgressDto = MatchProgressDto.builder()
                    .pointWinnerId(pointWinnerId)
                    .winnerScore(secondPlayerScore)
                    .loserScore(firstPlayerScore)
                    .matchState(matchState)
                    .advantagePlayerId(advantagePlayerId)
                    .build();
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
            case FINISHED -> setWinningValues(matchProgressDto);
        }
    }

    // A-state
    private void increaseRegularPointScore(MatchProgressDto matchProgressDto) {
        var winnerScore = matchProgressDto.getWinnerScore();
        var winnerPointsScore = winnerScore.getPointsScore();

        if (winnerPointsScore == 0) {
            // A->A
            winnerScore.setPointsScore(15);
        } else if (winnerPointsScore == 15) {
            // A->A
            winnerScore.setPointsScore(30);
        } else if (winnerPointsScore == 30) {
            winnerScore.setPointsScore(40);
            // A->A | A->B
            if (isDeuceScore(matchProgressDto)) {
                matchProgressDto.setMatchState(MatchState.DEUCE);
            }
        } else {
            increaseGameScore(matchProgressDto);
            // A->A | A->C | A->D

            // A->C
            if (isTieBreak(matchProgressDto)) {
                matchProgressDto.setMatchState(MatchState.TIEBREAK);
            }
            // A->D
            if (isWinningMatch(winnerScore, 2)) {
                matchProgressDto.setMatchState(MatchState.FINISHED);
            }
        }
    }

    private boolean isDeuceScore(MatchProgressDto matchProgressDto) {
        var firstPointsScore = matchProgressDto.getWinnerScore().getPointsScore();
        var secondPointsScore = matchProgressDto.getLoserScore().getPointsScore();
        return firstPointsScore == 40 && secondPointsScore == 40;
    }

    private boolean isTieBreak(MatchProgressDto matchProgressDto) {
        var firstGamesScore = matchProgressDto.getWinnerScore().getGamesScore();
        var secondGamesScore = matchProgressDto.getLoserScore().getGamesScore();
        return firstGamesScore == 6 && secondGamesScore == 6;
    }

    // B-state
    private void increaseAdvantagePointScore(MatchProgressDto matchProgressDto) {
        if (isAdvantagePlayerScoring(matchProgressDto)) {
            // B->A | B->C
            matchProgressDto.setAdvantagePlayerId(null);
            increaseGameScore(matchProgressDto);

            // B->C
            if (isTieBreak(matchProgressDto)) {
                matchProgressDto.setMatchState(MatchState.TIEBREAK);
            }
            // B->A
            else {
                matchProgressDto.setMatchState(MatchState.REGULAR);
            }
        } else {
            // B->B
            if (matchProgressDto.getAdvantagePlayerId() == null) {
                matchProgressDto.setAdvantagePlayerId(matchProgressDto.getPointWinnerId());
            } else {
                matchProgressDto.setAdvantagePlayerId(null);
            }
        }
    }

    private boolean isAdvantagePlayerScoring(MatchProgressDto matchProgressDto) {
        return matchProgressDto.getAdvantagePlayerId() != null &&
               matchProgressDto.getAdvantagePlayerId().equals(matchProgressDto.getPointWinnerId());
    }

    // C-state
    private void increaseTieBreakPointScore(MatchProgressDto matchProgressDto) {
        if (isCrucialTieBreakPoint(matchProgressDto)) {
            increaseSetScore(matchProgressDto);
            // C->A | C->D

            // C->D
            if (isWinningMatch(matchProgressDto.getWinnerScore(), 2)) {
                matchProgressDto.setMatchState(MatchState.FINISHED);
            }
            // C->A
            else {
                matchProgressDto.setMatchState(MatchState.REGULAR);
            }
        } else {
            // C->C
            incrementPointScore(matchProgressDto.getWinnerScore());
        }
    }

    private boolean isCrucialTieBreakPoint(MatchProgressDto matchProgressDto) {
        var winnerPointScore = matchProgressDto.getWinnerScore().getPointsScore();
        var loserPointScore = matchProgressDto.getLoserScore().getPointsScore();
        return winnerPointScore - loserPointScore >= 1
               && winnerPointScore >= 6;
    }

    private void increaseGameScore(MatchProgressDto matchProgressDto) {
        resetPoints(matchProgressDto);
        incrementGameScore(matchProgressDto.getWinnerScore());

        if (matchProgressDto.getWinnerScore().getGamesScore() > 6) {
            increaseSetScore(matchProgressDto);
        }
    }

    private void increaseSetScore(MatchProgressDto matchProgressDto) {
        resetPoints(matchProgressDto);
        resetGames(matchProgressDto);
        incrementSetScore(matchProgressDto.getWinnerScore());

        if (isWinningMatch(matchProgressDto.getWinnerScore(), 2)) {
            setWinningValues(matchProgressDto);
        }
    }

    private boolean isWinningMatch(ScoreDto scoreDto, int requiredSetsToWin) {
        return scoreDto.getSetsScore() == requiredSetsToWin;
    }

    // D-state
    private void setWinningValues(MatchProgressDto matchProgressDto) {
        matchProgressDto.getWinnerScore().setPointsScore(9);
        matchProgressDto.getWinnerScore().setGamesScore(9);
        matchProgressDto.getWinnerScore().setSetsScore(9);
        resetScore(matchProgressDto.getLoserScore());
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

    private void resetScore(ScoreDto scoreDto) {
        scoreDto.setPointsScore(0);
        scoreDto.setGamesScore(0);
        scoreDto.setSetsScore(0);
    }

    public static MatchScoreCalculationService getInstance() {
        return INSTANCE;
    }
}
