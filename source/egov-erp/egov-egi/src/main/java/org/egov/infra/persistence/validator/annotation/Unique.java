package org.egov.infra.persistence.validator.annotation;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import org.egov.infra.persistence.validator.UniqueCheckValidator;

@Target({ TYPE })
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = UniqueCheckValidator.class)
public @interface Unique {
    String[] fields() default {};

    String id();

    String tableName();

    String[] columnName() default {};

    String message() default "{validator.unique}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}