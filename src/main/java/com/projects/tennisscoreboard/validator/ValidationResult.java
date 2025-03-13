package com.projects.tennisscoreboard.validator;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ValidationResult {

    private final List<ValidationError> errors = new ArrayList<>();

    public void add(ValidationError error) {
        errors.add(error);
    }

    public void addAll(ValidationResult validationResult) {
        errors.addAll(validationResult.getErrors());
    }

    public boolean isValid() {
        return errors.isEmpty();
    }
}
