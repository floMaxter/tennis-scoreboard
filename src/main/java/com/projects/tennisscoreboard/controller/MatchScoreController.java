package com.projects.tennisscoreboard.controller;

import com.projects.tennisscoreboard.dto.match.MatchState;
import com.projects.tennisscoreboard.dto.match.ongoing.OngoingMatchReadDto;
import com.projects.tennisscoreboard.exception.IllegalStateException;
import com.projects.tennisscoreboard.exception.NotFoundException;
import com.projects.tennisscoreboard.mapper.match.OngoingMatchReadMapper;
import com.projects.tennisscoreboard.service.FinishedMatchesPersistenceService;
import com.projects.tennisscoreboard.service.MatchScoreCalculationService;
import com.projects.tennisscoreboard.service.OngoingMatchesService;
import com.projects.tennisscoreboard.service.PlayerService;
import com.projects.tennisscoreboard.utils.JspHelper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/match-score")
public class MatchScoreController extends HttpServlet {

    private final OngoingMatchesService ongoingMatchesService = OngoingMatchesService.getInstance();
    private final MatchScoreCalculationService matchScoreCalculationService = MatchScoreCalculationService.getInstance();
    private final FinishedMatchesPersistenceService finishedMatchesPersistenceService = FinishedMatchesPersistenceService.getInstance();
    private final PlayerService playerService = PlayerService.getInstance();
    private final OngoingMatchReadMapper ongoingMatchReadMapper = OngoingMatchReadMapper.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            var ongoingMatchReadDto = ongoingMatchesService.findById(req.getParameter("uuid"));

            req.setAttribute("ongoingMatch", ongoingMatchReadDto);
            req.getRequestDispatcher(JspHelper.getPath("match_score"))
                    .forward(req, resp);
        } catch (NotFoundException e) {
            req.setAttribute("errorMessage", e.getMessage());
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            req.getRequestDispatcher(JspHelper.getPath("error_page"))
                    .forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        var matchId = req.getParameter("uuid");
        var pointWinnerId = Long.valueOf(req.getParameter("pointWinnerId"));

        try {
            var findMatch = ongoingMatchesService.findById(matchId);
            var updatedMatch = matchScoreCalculationService.calculateScore(findMatch, pointWinnerId);
            if (isMatchFinished(updatedMatch)) {
                var savedMatch = finishedMatchesPersistenceService.save(updatedMatch);
                ongoingMatchesService.delete(matchId);
                var winner = playerService.findById(savedMatch.winner().getId());

                req.setAttribute("ongoingMatch", updatedMatch);
                req.setAttribute("winner", winner);
                req.getRequestDispatcher(JspHelper.getPath("match_score"))
                        .forward(req, resp);
            } else {
                ongoingMatchesService.updateOngoingMatch(matchId, ongoingMatchReadMapper.mapFrom(updatedMatch));
                resp.sendRedirect(String.format(req.getContextPath() + "/match-score?uuid=%s", matchId));
            }
        } catch (NotFoundException | IllegalStateException e) {
            req.setAttribute("errorMessage", e.getMessage());
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            req.getRequestDispatcher(JspHelper.getPath("error_page"))
                    .forward(req, resp);
        }
    }

    private boolean isMatchFinished(OngoingMatchReadDto updatedMatch) {
        return updatedMatch.getMatchState().equals(MatchState.FINISHED);
    }
}
