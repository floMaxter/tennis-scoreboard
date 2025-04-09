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
        var matchScoreDto = ongoingMatchDto.getMatchScoreDto();
        var firstPlayerScore = matchScoreDto.getFirstPlayerScore();
        var secondPlayerScore = matchScoreDto.getSecondPlayerScore();
        var matchState = ongoingMatchDto.getMatchState();

        MatchProgressDto matchProgressDto = MatchProgressDto.builder()
                .pointWinnerId(pointWinnerId)
                .matchState(matchState)
                .build();

        if (ongoingMatchDto.getFirstPlayer().id().equals(pointWinnerId)) {
            matchProgressDto.setWinnerScore(copyScoreDto(firstPlayerScore));
            matchProgressDto.setLoserScore(copyScoreDto(secondPlayerScore));
        } else {
            matchProgressDto.setWinnerScore(copyScoreDto(secondPlayerScore));
            matchProgressDto.setLoserScore(copyScoreDto(firstPlayerScore));
        }
        return matchProgressDto;
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

//        var updatedMatchDto = OngoingMatchDto.builder()
//                .firstPlayer(baseMatch.getFirstPlayer())
//                .secondPlayer(baseMatch.getSecondPlayer())
//                .matchState(matchProgressDto.getMatchState())
//                .build();
//
//        if (matchProgressDto.getPointWinnerId().equals(firstPlayerId)) {
//            updatedMatchDto.setMatchScoreDto(buildMatchScoreDto(winnerScore, loserScore));
//        } else {
//            updatedMatchDto.setMatchScoreDto(buildMatchScoreDto(loserScore, winnerScore));
//        }
//
//        return updatedMatchDto;

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
