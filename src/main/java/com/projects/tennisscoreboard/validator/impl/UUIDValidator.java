package com.projects.tennisscoreboard.validator.impl;

import com.projects.tennisscoreboard.utils.PropertiesUtil;
import com.projects.tennisscoreboard.validator.ValidationError;
import com.projects.tennisscoreboard.validator.ValidationResult;
import com.projects.tennisscoreboard.validator.Validator;

import java.util.regex.Pattern;

public class UUIDValidator implements Validator<String> {

    private static final UUIDValidator INSTANCE = new UUIDValidator();

    private UUIDValidator() {
    }

    @Override
    public ValidationResult isValid(String object) {
        var validationResult = new ValidationResult();
        if (!isValidUUID(object)) {
            validationResult.add(ValidationError.of(PropertiesUtil.get("validation.ongoing_match.invalid_id") + " " + object));
        }

        return validationResult;
    }

    private boolean isValidUUID(String uuid) {
        return Pattern.matches("^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[1-5][0-9a-fA-F]{3}-[89abAB][0-9a-fA-F]{3}-[0-9a-fA-F]{12}$", uuid);
    }

    public static UUIDValidator getInstance() {
        return INSTANCE;
    }
}
