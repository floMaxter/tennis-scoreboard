package com.projects.tennisscoreboard.controller;

import com.projects.tennisscoreboard.utils.JspHelper;
import com.projects.tennisscoreboard.service.FinishedMatchesPersistenceService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/matches")
public class MatchesController extends HttpServlet {

    private final FilterByPlayerNameValidator filterByPlayerNameValidator = FilterByPlayerNameValidator.getInstance();
    private final FinishedMatchesPersistenceService finishedMatchesPersistenceService = FinishedMatchesPersistenceService.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var playerName = req.getParameter("filter_by_player_name");
        var page = req.getParameter("page");
        ValidationUtil.validate(filterByPlayerNameValidator.isValid(playerName));

        var totalPages = finishedMatchesPersistenceService.getTotalPages(playerName);
        var currentPage = normalizePageNumber(page, totalPages);
        var matches = finishedMatchesPersistenceService.findMatches(playerName, currentPage);

        req.setAttribute("totalPages", totalPages);
        req.setAttribute("currentPage", currentPage);
        req.setAttribute("matches", matches);
        req.getRequestDispatcher(JspHelper.getPath("matches"))
                .forward(req, resp);
    }

    private Long normalizePageNumber(String page, Long totalPage) {
        var currentPage = page == null ? 1 : Long.parseLong(page);
        return Math.max(1, Math.min(currentPage, totalPage));
    }
}
