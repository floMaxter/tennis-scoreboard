package com.projects.tennisscoreboard.validator;

import com.projects.tennisscoreboard.Utils.PropertiesUtil;
import com.projects.tennisscoreboard.dto.MatchCreateDto;

import java.util.regex.Pattern;

public class CreateMatchValidator implements Validator<MatchCreateDto> {

    private static final CreateMatchValidator INSTANCE = new CreateMatchValidator();

    private CreateMatchValidator() {
    }

    @Override
    public ValidationResult isValid(MatchCreateDto object) {
        var validationResult = new ValidationResult();
        var firstPlayerName = object.firstPlayerName();
        var secondPlayerName = object.secondPlayerName();

        validationResult.addAll(validateName(firstPlayerName));
        validationResult.addAll(validateName(secondPlayerName));

        if (firstPlayerName.equals(secondPlayerName)) {
            validationResult.add(ValidationError.of(PropertiesUtil.get("validation.player_name.must_be_different")));
        }

        return validationResult;
    }

    private ValidationResult validateName(String name) {
        var validationResult = new ValidationResult();

        if (isBlankName(name)) {
            validationResult.add(ValidationError.of(PropertiesUtil.get("validation.player_name.not_empty")));
        } else {
            if (!isNameFormatValid(name)) {
                validationResult.add(ValidationError.of(PropertiesUtil.get("validation.player_name.invalid_format")));
            }
        }

        return validationResult;
    }

    private boolean isNameFormatValid(String name) {
        int maxLengthOfName = Integer.parseInt(PropertiesUtil.get("validation.player_name.max_length"));
        return name.length() < maxLengthOfName &&
               Pattern.matches("^[A-Za-zА-Яа-яЁё]+(?:\\s[A-Za-zА-Яа-яЁё]+)*$", name);
    }

    private boolean isBlankName(String name) {
        return name == null || name.trim().isEmpty();
    }

    public static CreateMatchValidator getInstance() {
        return INSTANCE;
    }
}
