package com.michaelcamp.risknarrative.controller.validation;

import com.michaelcamp.risknarrative.data.CompanySearchRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CompanySearchRequestValidator implements ConstraintValidator<ValidCompanySearch, CompanySearchRequest> {

    public void initialize(ValidCompanySearch constraintAnnotation) {
    }

    public boolean isValid(CompanySearchRequest searchRequest, ConstraintValidatorContext constraintContext) {
        return searchRequest != null && searchRequest.checkValid();
    }
}
