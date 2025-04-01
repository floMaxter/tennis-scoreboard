package com.projects.tennisscoreboard.exception;

import lombok.Getter;

@Getter
public class DatabaseException extends RuntimeException {

    private final String message;

    public DatabaseException(String message) {
        this.message = message;
    }
}
