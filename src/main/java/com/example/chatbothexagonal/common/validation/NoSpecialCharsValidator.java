package com.example.chatbothexagonal.common.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class NoSpecialCharsValidator implements ConstraintValidator<NoSpecialChars, String> {

    private static final String ALLOWED = "^[a-zA-Z0-9_-]+$";

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) return true;
        return value.matches(ALLOWED);
    }
}

