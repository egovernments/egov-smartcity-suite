package org.egov.infra.persistence.validator;

import java.beans.BeanInfo;
import java.beans.PropertyDescriptor;
import java.util.Date;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infra.persistence.validator.annotation.CompareDates;

public class CompareDatesValidator implements ConstraintValidator<CompareDates, Date> {

    private CompareDates compareDates;

    @Override
    public void initialize(final CompareDates compareDates) {
        this.compareDates = compareDates;
    }

    @Override
    public boolean isValid(final Date date, final ConstraintValidatorContext arg1) {
        if (compareDates.fromDate() == null || compareDates.toDate() == null || compareDates.dateFormat() == null)
            return false;

        return dateValidation(date, compareDates.fromDate(), compareDates.toDate());
    }

    private boolean dateValidation(final Date date, final String field1, final String field2) {
        final Date fromDate = getValue(date, field1);
        final Date toDate = getValue(date, field2);
        if (fromDate == null || toDate == null)
            return false;

        return fromDate.before(toDate);
    }

    private Date getValue(final Date target, final String field) {
        try {
            final BeanInfo info = java.beans.Introspector.getBeanInfo(target.getClass());
            final PropertyDescriptor[] props = info.getPropertyDescriptors();
            for (final PropertyDescriptor propertyDescriptor : props)
                if (propertyDescriptor.getName().equals(field))
                    return (Date) propertyDescriptor.getReadMethod().invoke(target);
            return null;
        } catch (final Exception e) {
            throw new EGOVRuntimeException(e.getMessage(), e);
        }
    }
}
