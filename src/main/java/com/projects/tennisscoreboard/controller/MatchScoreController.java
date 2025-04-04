package com.projects.tennisscoreboard.controller;

import com.projects.tennisscoreboard.dto.match.MatchState;
import com.projects.tennisscoreboard.dto.match.ongoing.OngoingMatchDto;
import com.projects.tennisscoreboard.service.MatchScoreCalculationService;
import com.projects.tennisscoreboard.service.OngoingMatchesService;
import com.projects.tennisscoreboard.utils.JspHelper;
import com.projects.tennisscoreboard.utils.ValidationUtil;
import com.projects.tennisscoreboard.validator.impl.LongIDValidator;
import com.projects.tennisscoreboard.validator.impl.UUIDValidator;
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
    private final UUIDValidator uuidValidator = UUIDValidator.getInstance();
    private final LongIDValidator longIDValidator = LongIDValidator.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var matchId = req.getParameter("uuid");
        ValidationUtil.validate(uuidValidator.isValid(matchId));

        var ongoingMatch = ongoingMatchesService.findById(matchId);

        req.setAttribute("ongoingMatch", ongoingMatch);
        req.getRequestDispatcher(JspHelper.getPath("match_score"))
                .forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        var matchId = req.getParameter("uuid");
        var pointWinnerIdParam = req.getParameter("pointWinnerId");
        ValidationUtil.validate(uuidValidator.isValid(matchId));
        ValidationUtil.validate(longIDValidator.isValid(pointWinnerIdParam));

        var findMatch = ongoingMatchesService.findById(matchId);
        var pointWinnerId = Long.valueOf(pointWinnerIdParam);

        var updatedMatch = matchScoreCalculationService.calculateScore(findMatch, pointWinnerId);
        ongoingMatchesService.updateOngoingMatch(matchId, updatedMatch);

        if (isMatchFinished(updatedMatch)) {
            resp.sendRedirect(String.format(req.getContextPath() + "/finished-match?uuid=%s", matchId));
        } else {
            resp.sendRedirect(String.format(req.getContextPath() + "/match-score?uuid=%s", matchId));
        }
    }

    private boolean isMatchFinished(OngoingMatchDto updatedMatch) {
        return updatedMatch.getMatchState().equals(MatchState.FINISHED);
    }
}
