package com.projects.tennisscoreboard.validator.impl;

import com.projects.tennisscoreboard.utils.PropertiesUtil;
import com.projects.tennisscoreboard.validator.ValidationError;
import com.projects.tennisscoreboard.validator.ValidationResult;
import com.projects.tennisscoreboard.validator.Validator;

import java.text.MessageFormat;
import java.util.regex.Pattern;

public class FilterByPlayerNameValidator implements Validator<String> {

    private static final FilterByPlayerNameValidator INSTANCE = new FilterByPlayerNameValidator();

    private FilterByPlayerNameValidator() {
    }

    @Override
    public ValidationResult isValid(String object) {
        var validationResult = new ValidationResult();

        if (!isBlankName(object)) {
            if (!isNameLengthValid(object)) {
                processInvalidNameLength(validationResult);
            }
            if (!isNameFormatValid(object)) {
                validationResult.add(ValidationError.of(PropertiesUtil.get("validation.player_name.invalid_format")));
            }
        }

        return validationResult;
    }

    private void processInvalidNameLength(ValidationResult validationResult) {
        var invalidLengthMessageTemplate = PropertiesUtil.get("validation.player_name.invalid_length");
        int maxLengthOfName = Integer.parseInt(PropertiesUtil.get("validation.player_name.max_length"));
        var invalidLengthMessage = MessageFormat.format(invalidLengthMessageTemplate, maxLengthOfName);
        validationResult.add(ValidationError.of(invalidLengthMessage));
    }

    private boolean isNameLengthValid(String name) {
        int maxLengthOfName = Integer.parseInt(PropertiesUtil.get("validation.player_name.max_length"));
        return name.length() < maxLengthOfName;
    }

    private boolean isNameFormatValid(String name) {
        return Pattern.matches("^[A-Za-zА-Яа-яЁё]+(?:\\s[A-Za-zА-Яа-яЁё]+)*$", name);
    }

    private boolean isBlankName(String name) {
        return name == null || name.trim().isEmpty();
    }

    public static FilterByPlayerNameValidator getInstance() {
        return INSTANCE;
    }
}
