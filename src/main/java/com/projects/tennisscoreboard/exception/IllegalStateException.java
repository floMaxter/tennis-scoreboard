package com.projects.tennisscoreboard.exception;

import lombok.Getter;

@Getter
public class IllegalStateException extends RuntimeException {

    private final String message;

    public IllegalStateException(String message) {
        this.message = message;
    }
}
