package com.projects.tennisscoreboard.mapper.match;

import com.projects.tennisscoreboard.dto.match.ongoing.OngoingMatchDto;
import com.projects.tennisscoreboard.entity.Match;
import com.projects.tennisscoreboard.mapper.Mapper;
import com.projects.tennisscoreboard.utils.ScoreUtil;

public class OngoingMatchMapper implements Mapper<OngoingMatchDto, Match> {

    private static final OngoingMatchMapper INSTANCE = new OngoingMatchMapper();

    private OngoingMatchMapper() {
    }

    @Override
    public Match mapFrom(OngoingMatchDto object) {
        var matchScore = object.getMatchScoreDto();
        var firstPlayerSetsScore = matchScore.getFirstPlayerScore().getSetsScore();
        var match = Match.builder()
                .firstPlayer(object.getFirstPlayer())
                .secondPlayer(object.getSecondPlayer())
                .build();

        if (firstPlayerSetsScore == ScoreUtil.SETS_TO_WIN) {
            match.setWinner(object.getFirstPlayer());
        } else {
            match.setWinner(object.getSecondPlayer());
        }

        return match;
    }

    public static OngoingMatchMapper getInstance() {
        return INSTANCE;
    }
}
