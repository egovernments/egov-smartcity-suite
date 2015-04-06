package org.egov.infra.persistence.validator;

import java.util.regex.Pattern;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.egov.infra.persistence.validator.annotation.OptionalPattern;

public class OptionalPatternValidator implements ConstraintValidator<OptionalPattern, Object> {

    private OptionalPattern optionalPattern;

    @Override
    public void initialize(final OptionalPattern optionalPattern) {
        this.optionalPattern = optionalPattern;
    }

    @Override
    public boolean isValid(final Object value, final ConstraintValidatorContext arg1) {
        if (value == null || org.apache.commons.lang.StringUtils.isBlank(String.valueOf(value)))
            return true;
        return Pattern.compile(optionalPattern.regex(), optionalPattern.flags()).matcher(String.valueOf(value))
                .matches();
    }

}
