package com.projects.tennisscoreboard.exception;

import com.projects.tennisscoreboard.validator.ValidationError;
import lombok.Getter;

import java.util.List;

@Getter
public class ValidationException extends RuntimeException {

    private final List<ValidationError> errors;

    public ValidationException(List<ValidationError> errors) {
        this.errors = errors;
    }
}
