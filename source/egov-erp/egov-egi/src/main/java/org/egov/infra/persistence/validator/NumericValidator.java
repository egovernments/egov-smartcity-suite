package org.egov.infra.persistence.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.egov.infra.persistence.validator.annotation.Numeric;
import org.egov.infra.validation.regex.Constants;

public class NumericValidator implements ConstraintValidator<Numeric, Object> {

    @Override
    public void initialize(final Numeric numeric) {
        // Unused
    }

    @Override
    public boolean isValid(final Object value, final ConstraintValidatorContext arg1) {

        if (value == null)
            return true;
        else {
            final String stringVal = String.valueOf(value);
            if (org.apache.commons.lang.StringUtils.isBlank(stringVal))
                return true;

            return stringVal.trim().matches(Constants.NUMERIC);
        }

    }

}
