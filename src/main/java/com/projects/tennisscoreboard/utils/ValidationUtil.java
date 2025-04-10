package com.projects.tennisscoreboard.utils;

import com.projects.tennisscoreboard.exception.ValidationException;
import com.projects.tennisscoreboard.validator.ValidationError;
import com.projects.tennisscoreboard.validator.ValidationResult;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class ValidationUtil {

    public static void validate(ValidationResult validationResult) {
        if (!validationResult.isValid()) {
            log.warn("Validation error(s):\n{}", formatErrors(validationResult.getErrors()));
            throw new ValidationException(validationResult.getErrors());
        }
    }

    private static String formatErrors(List<ValidationError> errors) {
        return errors.stream()
                .map(error -> String.format("- %s", error.getMessage()))
                .collect(Collectors.joining("\n"));
    }
}
