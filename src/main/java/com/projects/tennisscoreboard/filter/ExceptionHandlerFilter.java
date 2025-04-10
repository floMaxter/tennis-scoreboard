package com.projects.tennisscoreboard.filter;

import com.projects.tennisscoreboard.exception.ExceptionRequestDto;
import com.projects.tennisscoreboard.exception.GenericApplicationException;
import com.projects.tennisscoreboard.utils.JspHelper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebFilter("/*")
public class ExceptionHandlerFilter extends HttpFilter {

    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        try {
            super.doFilter(req, res, chain);
        } catch (Throwable throwable) {
            int statusCode;
            if (throwable instanceof GenericApplicationException) {
                statusCode = ((GenericApplicationException) throwable).getStatusCode();
            } else {
                statusCode = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
            }

            req.setAttribute("error", new ExceptionRequestDto(statusCode, throwable.getMessage()));
            req.getRequestDispatcher(JspHelper.getPath("error_page")).forward(req, res);
        }
    }
}
