package org.egov.infra.persistence.validator;

import java.math.BigDecimal;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.egov.infra.persistence.validator.annotation.GreaterThan;

public class GreaterThanValidator implements ConstraintValidator<GreaterThan, Object> {

    private GreaterThan greaterThan;

    @Override
    public void initialize(final GreaterThan greaterThan) {
        this.greaterThan = greaterThan;
    }

    @Override
    public boolean isValid(final Object value, final ConstraintValidatorContext arg1) {
        if (value == null)
            return true;
        if (value instanceof String) {
            final BigDecimal dv = new BigDecimal((String) value);
            return dv.compareTo(BigDecimal.valueOf(greaterThan.value())) > 0;
        } else if (value instanceof Double || value instanceof Float) {
            final double dv = ((Number) value).doubleValue();
            return dv > greaterThan.value();
        } else if (value instanceof Number) {
            final long lv = ((Number) value).longValue();
            return lv > greaterThan.value();
        } else
            return false;
    }
}
