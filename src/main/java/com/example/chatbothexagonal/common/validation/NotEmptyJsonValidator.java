package com.example.chatbothexagonal.common.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class NotEmptyJsonValidator implements ConstraintValidator<NotEmptyJson, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) return false;
        return value.trim().startsWith("{") && value.trim().endsWith("}");
    }
}

