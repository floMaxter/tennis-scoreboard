package com.projects.tennisscoreboard.mapper;

import com.projects.tennisscoreboard.dto.GameScore;
import com.projects.tennisscoreboard.dto.MatchCreateDto;
import com.projects.tennisscoreboard.dto.MatchScoreDto;
import com.projects.tennisscoreboard.dto.OngoingMatchDto;
import com.projects.tennisscoreboard.dto.PointScore;
import com.projects.tennisscoreboard.dto.SetScore;
import com.projects.tennisscoreboard.entity.Player;
import com.projects.tennisscoreboard.repository.PlayerRepository;

import java.util.List;

public class MatchCreateToOngoingMapper implements Mapper<MatchCreateDto, OngoingMatchDto> {

    private final PlayerRepository playerRepository;

    public MatchCreateToOngoingMapper(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    @Override
    public OngoingMatchDto mapFrom(MatchCreateDto object) {
        List<Player> firstPlayer = playerRepository.findByName(object.firstPlayerName());
        List<Player> secondPlayer = playerRepository.findByName(object.secondPlayerName());

        return OngoingMatchDto.builder()
                .firstPlayerId(firstPlayer.getFirst().getId())
                .secondPlayerId(secondPlayer.getFirst().getId())
                .matchScoreDto(new MatchScoreDto(
                        new PointScore(),
                        new GameScore(),
                        new SetScore()
                ))
                .build();
    }
}
