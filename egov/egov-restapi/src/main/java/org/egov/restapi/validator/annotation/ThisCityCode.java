package org.egov.restapi.validator.annotation;

import org.egov.restapi.validator.CityCodeValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ METHOD, FIELD })
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = CityCodeValidator.class)
public @interface ThisCityCode {
    String message() default "Invalid ULB Code";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
