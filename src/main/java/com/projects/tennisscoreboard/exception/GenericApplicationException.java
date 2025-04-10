package com.projects.tennisscoreboard.exception;

import lombok.Getter;

@Getter
public abstract class GenericApplicationException extends RuntimeException {

    private final int statusCode;

    public GenericApplicationException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }
}
