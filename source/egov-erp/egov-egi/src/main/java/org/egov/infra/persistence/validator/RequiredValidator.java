package org.egov.infra.persistence.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.egov.infra.persistence.validator.annotation.Required;

public class RequiredValidator implements ConstraintValidator<Required, Object> {

    @Override
    public void initialize(final Required parameters) {
        // Unused
    }

    @Override
    public boolean isValid(final Object value, final ConstraintValidatorContext arg1) {
        if (value == null)
            return false;
        else if (value instanceof String)
            return org.apache.commons.lang.StringUtils.isNotBlank((String) value);
        else
            return true;
    }
}
