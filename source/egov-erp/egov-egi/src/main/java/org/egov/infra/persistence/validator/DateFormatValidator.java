package org.egov.infra.persistence.validator;

import java.util.Date;
import java.util.regex.Pattern;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.egov.infra.persistence.validator.annotation.DateFormat;
import org.egov.infra.validation.regex.Constants;
import org.egov.infstr.utils.DateUtils;

public class DateFormatValidator implements ConstraintValidator<DateFormat, Date> {

    @Override
    public void initialize(final DateFormat dateFormat) {
        // Unused

    }

    @Override
    public boolean isValid(final Date date, final ConstraintValidatorContext context) {
        return date == null ? true : Pattern.compile(Constants.DATEFORMAT)
                .matcher(DateUtils.getFormattedDate(date, "dd/MM/yyyy")).matches();
    }

}
