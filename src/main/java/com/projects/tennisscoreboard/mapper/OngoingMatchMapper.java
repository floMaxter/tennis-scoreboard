package com.projects.tennisscoreboard.mapper;

import com.projects.tennisscoreboard.Utils.ScoreUtil;
import com.projects.tennisscoreboard.dto.match.ongoing.OngoingMatchReadDto;
import com.projects.tennisscoreboard.entity.Match;

public class OngoingMatchMapper implements Mapper<OngoingMatchReadDto, Match> {

    private static final OngoingMatchMapper INSTANCE = new OngoingMatchMapper();

    private OngoingMatchMapper() {
    }

    @Override
    public Match mapFrom(OngoingMatchReadDto object) {
        var firstPlayerSetsScore = object.getMatchScoreDto().getFirstPlayerScore().getSetsScore();
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
