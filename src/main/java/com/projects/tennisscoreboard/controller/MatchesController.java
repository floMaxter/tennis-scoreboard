package com.projects.tennisscoreboard.controller;

import com.projects.tennisscoreboard.Utils.JspHelper;
import com.projects.tennisscoreboard.dto.match.completed.MatchReadDto;
import com.projects.tennisscoreboard.service.FinishedMatchesPersistenceService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/matches")
public class MatchesController extends HttpServlet {

    private final FinishedMatchesPersistenceService finishedMatchesPersistenceService = FinishedMatchesPersistenceService.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var playerName = req.getParameter("filter_by_player_name");
        var page = req.getParameter("page") == null ? 1 : Integer.parseInt(req.getParameter("page"));
        List<MatchReadDto> matches;
        Long totalPages;

        if (playerName != null && !playerName.trim().isEmpty()) {
            matches = finishedMatchesPersistenceService.findAllByPlayerName(playerName, page);
            totalPages = finishedMatchesPersistenceService.getTotalNumberOfPagesByName(playerName);
        } else {
            matches = finishedMatchesPersistenceService.findAllMatches(page);
            totalPages = finishedMatchesPersistenceService.getTotalNumberOfPages();
        }

        req.setAttribute("matches", matches);
        req.setAttribute("totalPages", totalPages);
        req.setAttribute("currentPage", page);

        req.getRequestDispatcher(JspHelper.getPath("matches"))
                .forward(req, resp);
    }
}
