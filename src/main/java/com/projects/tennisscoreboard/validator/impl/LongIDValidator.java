package com.projects.tennisscoreboard.validator.impl;

import com.projects.tennisscoreboard.utils.PropertiesUtil;
import com.projects.tennisscoreboard.validator.ValidationError;
import com.projects.tennisscoreboard.validator.ValidationResult;
import com.projects.tennisscoreboard.validator.Validator;

public class LongIDValidator implements Validator<String> {

    private static final LongIDValidator INSTANCE = new LongIDValidator();

    private LongIDValidator() {
    }

    @Override
    public ValidationResult isValid(String object) {
        var validationResult = new ValidationResult();
        if (!canBeParsedToLong(object)) {
            validationResult.add(ValidationError.of(PropertiesUtil.get("validation.long_id.invalid_id")));
        }
        return validationResult;
    }

    private boolean canBeParsedToLong(String value) {
        try {
            Long.parseLong(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static LongIDValidator getInstance() {
        return INSTANCE;
    }
}
