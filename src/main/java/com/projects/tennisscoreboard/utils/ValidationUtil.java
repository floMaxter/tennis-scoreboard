package com.projects.tennisscoreboard.utils;

import com.projects.tennisscoreboard.exception.ValidationException;
import com.projects.tennisscoreboard.validator.ValidationResult;

public class ValidationUtil {

    public static void validate(ValidationResult validationResult) {
        if (!validationResult.isValid()) {
            throw new ValidationException(validationResult.getErrors());
        }
    }
}
