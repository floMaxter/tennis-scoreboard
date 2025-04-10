package com.projects.tennisscoreboard.exception;

import jakarta.servlet.http.HttpServletResponse;

public class IllegalStateException extends GenericApplicationException {

    public IllegalStateException(String message) {
        super(message, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }
}
