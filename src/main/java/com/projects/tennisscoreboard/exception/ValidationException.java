package com.projects.tennisscoreboard.exception;

import com.projects.tennisscoreboard.validator.ValidationError;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class ValidationException extends GenericApplicationException {

    private final List<ValidationError> errors;

    public ValidationException(List<ValidationError> errors) {
        super(buildMessage(errors), HttpServletResponse.SC_BAD_REQUEST);
        this.errors = errors;
    }

    private static String buildMessage(List<ValidationError> errors) {
        return errors.stream()
                .map(ValidationError::getMessage)
                .collect(Collectors.joining("\n"));
    }
}
