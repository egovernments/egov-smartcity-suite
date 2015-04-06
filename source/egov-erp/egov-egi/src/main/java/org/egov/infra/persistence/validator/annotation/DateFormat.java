package org.egov.infra.persistence.validator.annotation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import org.egov.infra.persistence.validator.DateFormatValidator;

@Documented
@Target({ METHOD, FIELD })
@Retention(RUNTIME)
@Constraint(validatedBy = DateFormatValidator.class)
public @interface DateFormat {
    String message() default "{validator.dateFormat}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
