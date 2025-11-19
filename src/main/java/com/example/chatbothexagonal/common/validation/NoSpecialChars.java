package com.example.chatbothexagonal.common.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ FIELD })
@Retention(RUNTIME)
@Constraint(validatedBy = NoSpecialCharsValidator.class)
@Documented
public @interface NoSpecialChars {
    String message() default "No debe contener caracteres especiales";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

