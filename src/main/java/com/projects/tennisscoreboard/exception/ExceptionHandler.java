package com.projects.tennisscoreboard.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ExceptionHandler {

    public static void handleException(HttpServletRequest req, Throwable throwable) {
        int statusCode = getStatusCode(throwable);
        req.setAttribute("error", new ExceptionRequestDto(statusCode, throwable.getMessage()));
    }

    private static int getStatusCode(Throwable throwable) {
        return switch (throwable.getClass().getSimpleName()) {
            case "NotFoundException" -> HttpServletResponse.SC_NOT_FOUND;
            case "AlreadyExistsException" -> HttpServletResponse.SC_CONFLICT;
            case "ValidationException" -> HttpServletResponse.SC_BAD_REQUEST;
            default -> HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
        };
    }
}
