package com.projects.tennisscoreboard.mapper.match;

import com.projects.tennisscoreboard.dto.match.MatchScoreDto;
import com.projects.tennisscoreboard.dto.match.ScoreDto;
import com.projects.tennisscoreboard.dto.match.ongoing.MatchProgressDto;
import com.projects.tennisscoreboard.dto.match.ongoing.OngoingMatchDto;

public class MatchProgressMapper {

    private static final MatchProgressMapper INSTANCE = new MatchProgressMapper();

    private MatchProgressMapper() {
    }

    public MatchProgressDto mapToMatchProgress(OngoingMatchDto ongoingMatchDto, Long pointWinnerId) {
        var firstPlayerId = ongoingMatchDto.getFirstPlayer().id();
        var matchScoreDto = ongoingMatchDto.getMatchScoreDto();
        var firstPlayerScore = matchScoreDto.getFirstPlayerScore();
        var secondPlayerScore = matchScoreDto.getSecondPlayerScore();
        var matchState = ongoingMatchDto.getMatchState();

        var winnerScore = isPointWinnerId(firstPlayerId, pointWinnerId)
                ? copyScoreDto(firstPlayerScore)
                : copyScoreDto(secondPlayerScore);
        var loserScore = isPointWinnerId(firstPlayerId, pointWinnerId)
                ? copyScoreDto(secondPlayerScore)
                : copyScoreDto(firstPlayerScore);
                
        return MatchProgressDto.builder()
                .pointWinnerId(pointWinnerId)
                .winnerScore(winnerScore)
                .loserScore(loserScore)
                .matchState(matchState)
                .build();
    }

    private ScoreDto copyScoreDto(ScoreDto scoreDto) {
        return ScoreDto.builder()
                .pointsScore(scoreDto.getPointsScore())
                .gamesScore(scoreDto.getGamesScore())
                .setsScore(scoreDto.getSetsScore())
                .hasAdvantage(scoreDto.isHasAdvantage())
                .build();
    }

    public OngoingMatchDto mapToOngoingMatch(MatchProgressDto matchProgressDto, OngoingMatchDto baseMatch) {
        var firstPlayerId = baseMatch.getFirstPlayer().id();
        var winnerScore = matchProgressDto.getWinnerScore();
        var loserScore = matchProgressDto.getLoserScore();

        var updatedMatchScoreDto = isFirstPlayerWinPoint(firstPlayerId, matchProgressDto)
                ? buildMatchScoreDto(winnerScore, loserScore)
                : buildMatchScoreDto(loserScore, winnerScore);

        return OngoingMatchDto.builder()
                .firstPlayer(baseMatch.getFirstPlayer())
                .secondPlayer(baseMatch.getSecondPlayer())
                .matchScoreDto(updatedMatchScoreDto)
                .matchState(matchProgressDto.getMatchState())
                .build();
    }

    private boolean isPointWinnerId(Long firstPlayerId, Long pointWinnerId) {
        return firstPlayerId.equals(pointWinnerId);
    }
    
    private boolean isFirstPlayerWinPoint(Long firstPlayerId, MatchProgressDto matchProgressDto) {
        return matchProgressDto.getPointWinnerId().equals(firstPlayerId);
    }

    private MatchScoreDto buildMatchScoreDto(ScoreDto firstPlayerScore, ScoreDto secondPlayerScore) {
        return MatchScoreDto.builder()
                .firstPlayerScore(copyScoreDto(firstPlayerScore))
                .secondPlayerScore(copyScoreDto(secondPlayerScore))
                .build();
    }

    public static MatchProgressMapper getInstance() {
        return INSTANCE;
    }
}
