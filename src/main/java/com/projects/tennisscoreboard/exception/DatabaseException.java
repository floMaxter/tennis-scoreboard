package com.projects.tennisscoreboard.exception;

import jakarta.servlet.http.HttpServletResponse;

public class DatabaseException extends GenericApplicationException {

    public DatabaseException(String message) {
        super(message, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }
}
