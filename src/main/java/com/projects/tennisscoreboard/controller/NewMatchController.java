package com.projects.tennisscoreboard.controller;

import com.projects.tennisscoreboard.Utils.JspHelper;
import com.projects.tennisscoreboard.dto.MatchCreateDto;
import com.projects.tennisscoreboard.service.OngoingMatchesService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/new-match")
public class NewMatchController extends HttpServlet {

    private final OngoingMatchesService ongoingMatchesService = OngoingMatchesService.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher(JspHelper.getPath("/new_match"))
                .forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        var matchId = ongoingMatchesService.create(new MatchCreateDto(
                req.getParameter("firstPlayerName"),
                req.getParameter("secondPlayerName"))
        );

        resp.sendRedirect(String.format(req.getContextPath() + "/match-score?uuid=%s", matchId));
    }
}
