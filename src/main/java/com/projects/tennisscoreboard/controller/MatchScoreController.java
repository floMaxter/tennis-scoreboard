package com.projects.tennisscoreboard.controller;

import com.projects.tennisscoreboard.dto.match.MatchState;
import com.projects.tennisscoreboard.dto.match.ongoing.OngoingMatchReadDto;
import com.projects.tennisscoreboard.mapper.OngoingMatchReadMapper;
import com.projects.tennisscoreboard.service.FinishedMatchesPersistenceService;
import com.projects.tennisscoreboard.service.MatchScoreCalculationService;
import com.projects.tennisscoreboard.service.OngoingMatchesService;
import com.projects.tennisscoreboard.utils.JspHelper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.NoSuchElementException;

@WebServlet("/match-score")
public class MatchScoreController extends HttpServlet {

    private final OngoingMatchesService ongoingMatchesService = OngoingMatchesService.getInstance();
    private final MatchScoreCalculationService matchScoreCalculationService = MatchScoreCalculationService.getInstance();
    private final FinishedMatchesPersistenceService finishedMatchesPersistenceService = FinishedMatchesPersistenceService.getInstance();
    private final OngoingMatchReadMapper ongoingMatchReadMapper = OngoingMatchReadMapper.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            var ongoingMatchReadDto = ongoingMatchesService.findById(req.getParameter("uuid"));

            req.setAttribute("ongoingMatch", ongoingMatchReadDto);
            req.getRequestDispatcher(JspHelper.getPath("/match_score"))
                    .forward(req, resp);
        } catch (NoSuchElementException e) {
            req.setAttribute("errorMessage", e.getMessage());
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            req.getRequestDispatcher(JspHelper.getPath("/error_page"))
                    .forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        var matchId = req.getParameter("uuid");
        var pointWinnerId = Long.valueOf(req.getParameter("pointWinnerId"));

        var findMatch = ongoingMatchesService.findById(matchId);
        var updatedMatch = matchScoreCalculationService.calculateScore(findMatch, pointWinnerId);
        if (isMatchFinished(updatedMatch)) {
            finishedMatchesPersistenceService.save(updatedMatch);
            ongoingMatchesService.delete(matchId);

            req.setAttribute("ongoingMatch", updatedMatch);
            req.getRequestDispatcher(JspHelper.getPath("/match_score"))
                    .forward(req, resp);
        } else {
            ongoingMatchesService.updateOngoingMatch(matchId, ongoingMatchReadMapper.mapFrom(updatedMatch));
            resp.sendRedirect(String.format(req.getContextPath() + "/match-score?uuid=%s", matchId));
        }
    }

    private boolean isMatchFinished(OngoingMatchReadDto updatedMatch) {
        return updatedMatch.getMatchState().equals(MatchState.FINISHED);
    }
}
