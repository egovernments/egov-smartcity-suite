package org.egov.restapi.validator;

import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.infra.utils.StringUtils;
import org.egov.restapi.validator.annotation.ThisCityCode;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CityCodeValidator implements ConstraintValidator<ThisCityCode, String> {

    ThisCityCode constraintAnnotation;

    @Override
    public void initialize(final ThisCityCode constraintAnnotation) {
        this.constraintAnnotation = constraintAnnotation;
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (StringUtils.isEmpty(value))
            return false;
        else {
            return ApplicationThreadLocals.getCityCode().equals(value.trim());
        }
    }
}
