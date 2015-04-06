package org.egov.infra.persistence.validator.annotation;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import org.egov.infra.persistence.validator.CompareDatesValidator;

@Target({ TYPE })
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = CompareDatesValidator.class)
public @interface CompareDates {
    String fromDate();

    String toDate();

    String dateFormat();

    String message() default "{validator.compareDates}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}