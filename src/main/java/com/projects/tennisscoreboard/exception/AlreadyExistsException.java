package com.projects.tennisscoreboard.exception;

import lombok.Getter;

@Getter
public class AlreadyExistsException extends RuntimeException {

    private final String message;

    public AlreadyExistsException(String message) {
        this.message = message;
    }
}
