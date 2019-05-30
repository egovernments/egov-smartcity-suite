package org.egov.restapi.validator.annotation;

import org.egov.restapi.validator.OverlappedDateRangeValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target(TYPE)
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = OverlappedDateRangeValidator.class)
public @interface OverlappedDateRange {
  String fromDate();

  String toDate();

  String dateFormat();

  String message() default "{validator.overlappedDates}";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}