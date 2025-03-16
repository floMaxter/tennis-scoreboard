package com.projects.tennisscoreboard.controller;

import com.projects.tennisscoreboard.Utils.JspHelper;
import com.projects.tennisscoreboard.mapper.OngoingMatchReadMapper;
import com.projects.tennisscoreboard.service.MatchScoreCalculationService;
import com.projects.tennisscoreboard.service.OngoingMatchesService;
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
    private final OngoingMatchReadMapper ongoingMatchReadMapper = OngoingMatchReadMapper.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var ongoingMatchReadDto = ongoingMatchesService.findById(req.getParameter("uuid"));

        req.setAttribute("ongoingMatch", ongoingMatchReadDto);
        req.getRequestDispatcher(JspHelper.getPath("/match_score"))
                .forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        var matchId = req.getParameter("uuid");
        var pointWinnerId = Long.valueOf(req.getParameter("pointWinnerId"));

        var findMatch = ongoingMatchesService.findById(matchId);
        var updatedMatch = matchScoreCalculationService.calculateScore(findMatch, pointWinnerId);
        ongoingMatchesService.updateOngoingMatch(matchId, ongoingMatchReadMapper.mapFrom(updatedMatch));

        resp.sendRedirect(String.format(req.getContextPath() + "/match-score?uuid=%s", matchId));
    }
}
