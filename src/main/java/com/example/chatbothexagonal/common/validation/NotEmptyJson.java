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
@Constraint(validatedBy = NotEmptyJsonValidator.class)
@Documented
public @interface NotEmptyJson {
    String message() default "JSON inválido o vacío";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

