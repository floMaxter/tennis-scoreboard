package com.projects.tennisscoreboard.service;

import com.projects.tennisscoreboard.Utils.HibernateUtil;
import com.projects.tennisscoreboard.dto.MatchCreateDto;
import com.projects.tennisscoreboard.dto.OngoingMatchDto;
import com.projects.tennisscoreboard.entity.Player;
import com.projects.tennisscoreboard.mapper.MatchCreateMapper;
import com.projects.tennisscoreboard.repository.PlayerRepository;
import com.projects.tennisscoreboard.repository.factory.RepositoryFactory;
import jakarta.persistence.NoResultException;
import org.hibernate.HibernateException;
import org.hibernate.Transaction;


import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class OngoingMatchesService {

    private final PlayerRepository playerRepository;
    private final MatchCreateMapper matchCreateMapper;
    private final Map<UUID, OngoingMatchDto> ongoingMatches;
    private static final OngoingMatchesService INSTANCE = new OngoingMatchesService();

    private OngoingMatchesService() {
        ongoingMatches = new HashMap<>();
        playerRepository = RepositoryFactory.getPlayerRepository();
        matchCreateMapper = new MatchCreateMapper(playerRepository);
    }

    public UUID create(MatchCreateDto matchCreateDto) {
        //TODO: validate

        Transaction transaction = HibernateUtil.getTransaction();
        try {
            createPlayersIfNotExist(matchCreateDto);

            var ongoingMatchDto = matchCreateMapper.mapFrom(matchCreateDto);
            UUID ongoingMatchId = UUID.randomUUID();
            ongoingMatches.put(ongoingMatchId, ongoingMatchDto);

            transaction.commit();

            return ongoingMatchId;
        } catch (HibernateException | NoResultException e) {
            HibernateUtil.transactionRollback(transaction);
            throw new RuntimeException();
        }
    }

    private void createPlayersIfNotExist(MatchCreateDto matchCreateDto) {
        var maybeFirstPlayer = playerRepository.findByName(matchCreateDto.firstPlayerName());
        var maybeSecondPlayer = playerRepository.findByName(matchCreateDto.secondPlayerName());
        if (maybeFirstPlayer.isEmpty()) {
            playerRepository.save(Player.builder()
                    .name(matchCreateDto.firstPlayerName())
                    .build());
        }
        if (maybeSecondPlayer.isEmpty()) {
            playerRepository.save(Player.builder()
                    .name(matchCreateDto.secondPlayerName())
                    .build());
        }
    }

    public static OngoingMatchesService getInstance() {
        return INSTANCE;
    }
}
