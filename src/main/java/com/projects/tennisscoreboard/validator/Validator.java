package com.projects.tennisscoreboard.validator;

public interface Validator<T> {

    ValidationResult isValid(T object);
}
