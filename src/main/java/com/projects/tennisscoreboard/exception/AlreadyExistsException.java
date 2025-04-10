package com.projects.tennisscoreboard.exception;

import jakarta.servlet.http.HttpServletResponse;

public class AlreadyExistsException extends GenericApplicationException {

    public AlreadyExistsException(String message) {
        super(message, HttpServletResponse.SC_CONFLICT);
    }
}
