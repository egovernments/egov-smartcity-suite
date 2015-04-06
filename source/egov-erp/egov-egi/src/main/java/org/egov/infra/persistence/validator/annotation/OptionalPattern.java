package org.egov.infra.persistence.validator.annotation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import org.egov.infra.persistence.validator.OptionalPatternValidator;

@Documented
@Target({ METHOD, FIELD })
@Retention(RUNTIME)
@Constraint(validatedBy = OptionalPatternValidator.class)
public @interface OptionalPattern {
    String regex();

    int flags() default 0;

    String message() default "{validator.pattern}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
