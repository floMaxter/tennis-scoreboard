package com.projects.tennisscoreboard.mapper.match;

import com.projects.tennisscoreboard.dto.match.ongoing.OngoingMatchDto;
import com.projects.tennisscoreboard.entity.Match;
import com.projects.tennisscoreboard.mapper.Mapper;
import com.projects.tennisscoreboard.mapper.player.PlayerMapper;
import com.projects.tennisscoreboard.utils.ScoreUtil;

public class OngoingMatchMapper implements Mapper<OngoingMatchDto, Match> {

    private final PlayerMapper playerMapper = PlayerMapper.getInstance();
    private static final OngoingMatchMapper INSTANCE = new OngoingMatchMapper();

    private OngoingMatchMapper() {
    }

    @Override
    public Match mapFrom(OngoingMatchDto object) {
        var matchScore = object.getMatchScoreDto();
        var firstPlayerSetsScore = matchScore.getFirstPlayerScore().getSetsScore();
        var firstPlayer = playerMapper.mapFrom(object.getFirstPlayer());
        var secondPlayer = playerMapper.mapFrom(object.getSecondPlayer());

        var match = Match.builder()
                .firstPlayer(firstPlayer)
                .secondPlayer(secondPlayer)
                .build();

        if (firstPlayerSetsScore == ScoreUtil.SETS_TO_WIN) {
            match.setWinner(firstPlayer);
        } else {
            match.setWinner(secondPlayer);
        }

        return match;
    }

    public static OngoingMatchMapper getInstance() {
        return INSTANCE;
    }
}
