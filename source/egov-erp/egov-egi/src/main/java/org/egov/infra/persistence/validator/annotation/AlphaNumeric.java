package org.egov.infra.persistence.validator.annotation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import org.egov.infra.persistence.validator.AlphaNumericValidator;

@Documented
@Target({ METHOD, FIELD })
@Retention(RUNTIME)
@Constraint(validatedBy = AlphaNumericValidator.class)
public @interface AlphaNumeric {
    String message() default "{validator.required}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}