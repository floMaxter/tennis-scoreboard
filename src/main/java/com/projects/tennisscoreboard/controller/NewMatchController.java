package com.projects.tennisscoreboard.controller;

import com.projects.tennisscoreboard.utils.JspHelper;
import com.projects.tennisscoreboard.exception.ValidationException;
import com.projects.tennisscoreboard.mapper.match.MatchCreateMapper;
import com.projects.tennisscoreboard.service.OngoingMatchesService;
import com.projects.tennisscoreboard.utils.ValidationUtil;
import com.projects.tennisscoreboard.validator.impl.CreateMatchValidator;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/new-match")
public class NewMatchController extends HttpServlet {

    private final CreateMatchValidator createMatchValidator = CreateMatchValidator.getInstance();
    private final OngoingMatchesService ongoingMatchesService = OngoingMatchesService.getInstance();
    private final MatchCreateMapper matchCreateMapper = MatchCreateMapper.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher(JspHelper.getPath("new_match"))
                .forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        var matchCreateDto = matchCreateMapper.mapFrom(req);

        try {
            ValidationUtil.validate(createMatchValidator.isValid(matchCreateDto));
            var matchId = ongoingMatchesService.create(matchCreateDto);
            resp.sendRedirect(String.format(req.getContextPath() + "/match-score?uuid=%s", matchId));
        } catch (ValidationException e) {
            req.setAttribute("errors", e.getErrors());
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            doGet(req, resp);
        }
    }
}
