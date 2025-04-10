package com.projects.tennisscoreboard.exception;

import jakarta.servlet.http.HttpServletResponse;

public class NotFoundException extends GenericApplicationException {

    public NotFoundException(String message) {
        super(message, HttpServletResponse.SC_NOT_FOUND);
    }
}
