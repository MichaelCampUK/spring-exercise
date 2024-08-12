package com.michaelcamp.risknarrative.controller.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CompanySearchRequestValidator.class)
public @interface ValidCompanySearch {

    String message() default "Company name or number must be provided";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
