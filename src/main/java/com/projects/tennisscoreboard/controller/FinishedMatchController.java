package com.projects.tennisscoreboard.controller;

import com.projects.tennisscoreboard.dto.match.ongoing.OngoingMatchDto;
import com.projects.tennisscoreboard.dto.player.PlayerDto;
import com.projects.tennisscoreboard.service.FinishedMatchesPersistenceService;
import com.projects.tennisscoreboard.service.OngoingMatchesService;
import com.projects.tennisscoreboard.utils.JspHelper;
import com.projects.tennisscoreboard.utils.ScoreUtil;
import com.projects.tennisscoreboard.utils.ValidationUtil;
import com.projects.tennisscoreboard.validator.impl.UUIDValidator;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/finished-match")
public class FinishedMatchController extends HttpServlet {

    private final OngoingMatchesService ongoingMatchesService = OngoingMatchesService.getInstance();
    private final FinishedMatchesPersistenceService finishedMatchesPersistenceService = FinishedMatchesPersistenceService.getInstance();
    private final UUIDValidator uuidValidator = UUIDValidator.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var matchId = req.getParameter("uuid");
        ValidationUtil.validate(uuidValidator.isValid(matchId));

        var ongoingMatch = ongoingMatchesService.findById(req.getParameter("uuid"));
        var winner = determineWinner(ongoingMatch);

        req.setAttribute("ongoingMatch", ongoingMatch);
        req.setAttribute("winner", winner);
        req.getRequestDispatcher(JspHelper.getPath("finished_match"))
                .forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        var matchId = req.getParameter("uuid");
        ValidationUtil.validate(uuidValidator.isValid(matchId));

        var findMatch = ongoingMatchesService.findById(matchId);

        finishedMatchesPersistenceService.save(findMatch);
        ongoingMatchesService.delete(matchId);

        resp.sendRedirect(String.format(req.getContextPath() + "/"));
    }

    private PlayerDto determineWinner(OngoingMatchDto ongoingMatchDto) {
        var matchScoreDto = ongoingMatchDto.getMatchScoreDto();
        var firstPlayerSetsScore = matchScoreDto.getFirstPlayerScore().getSetsScore();
        return firstPlayerSetsScore == ScoreUtil.SETS_TO_WIN
                ? ongoingMatchDto.getFirstPlayer()
                : ongoingMatchDto.getSecondPlayer();
    }
}
