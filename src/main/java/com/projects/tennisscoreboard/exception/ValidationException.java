package com.projects.tennisscoreboard.exception;

import com.projects.tennisscoreboard.validator.ValidationError;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class ValidationException extends RuntimeException {

    private final List<ValidationError> errors;

    public ValidationException(List<ValidationError> errors) {
        this.errors = errors;
    }

    @Override
    public String getMessage() {
        return errors.stream()
                .map(error -> error.getMessage() + "\n")
                .collect(Collectors.joining());
    }
}
