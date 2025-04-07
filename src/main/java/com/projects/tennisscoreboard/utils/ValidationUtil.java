package com.projects.tennisscoreboard.utils;

import com.projects.tennisscoreboard.exception.ValidationException;
import com.projects.tennisscoreboard.validator.ValidationResult;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ValidationUtil {

    public static void validate(ValidationResult validationResult) {
        if (!validationResult.isValid()) {
            log.warn("Validation error: {}", validationResult.getErrors());
            throw new ValidationException(validationResult.getErrors());
        }
    }
}
