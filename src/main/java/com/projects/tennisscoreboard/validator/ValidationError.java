package com.projects.tennisscoreboard.validator;

import lombok.Value;

@Value(staticConstructor = "of")
public class ValidationError {

    String message;
}
