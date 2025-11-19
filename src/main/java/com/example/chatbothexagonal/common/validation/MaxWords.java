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
@Constraint(validatedBy = MaxWordsValidator.class)
@Documented
public @interface MaxWords {
    int value();
    String message() default "Supera el número máximo de palabras";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
