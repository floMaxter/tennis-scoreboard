package com.projects.tennisscoreboard.controller;

import com.projects.tennisscoreboard.exception.ValidationException;
import com.projects.tennisscoreboard.service.FinishedMatchesPersistenceService;
import com.projects.tennisscoreboard.utils.JspHelper;
import com.projects.tennisscoreboard.utils.ValidationUtil;
import com.projects.tennisscoreboard.validator.ValidationError;
import com.projects.tennisscoreboard.validator.impl.FilterByPlayerNameValidator;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Collections;

@WebServlet("/matches")
public class MatchesController extends HttpServlet {

    private final FilterByPlayerNameValidator filterByPlayerNameValidator = FilterByPlayerNameValidator.getInstance();
    private final FinishedMatchesPersistenceService finishedMatchesPersistenceService = FinishedMatchesPersistenceService.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var playerName = req.getParameter("filter_by_player_name");
        var page = parseOrDefault(req.getParameter("page"));
        ValidationUtil.validate(filterByPlayerNameValidator.isValid(playerName));

        var matchPageDto = finishedMatchesPersistenceService.getPaginatedMatches(playerName, page);

        req.setAttribute("matchPage", matchPageDto);
        req.getRequestDispatcher(JspHelper.getPath("matches"))
                .forward(req, resp);
    }

    private Integer parseOrDefault(String page) {
        if (page == null) return 1;

        try {
            return Integer.parseInt(page);
        } catch (NumberFormatException e) {
            throw new ValidationException(Collections
                    .singletonList(ValidationError.of("Current page number can not parse to Long: " + page)));
        }
    }
}
