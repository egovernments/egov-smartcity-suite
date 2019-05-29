package org.egov.restapi.validator;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.utils.StringUtils;
import org.egov.restapi.validator.annotation.OverlappedDateRange;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class OverlappedDateRangeValidator implements ConstraintValidator<OverlappedDateRange, Object> {

  OverlappedDateRange constraintAnnotation;

  @Override
  public void initialize(OverlappedDateRange constraintAnnotation) {
    this.constraintAnnotation = constraintAnnotation;
  }

  @Override
  public boolean isValid(Object target, ConstraintValidatorContext context) {
    try {
      String fromDateString = (String) FieldUtils.readField(target, constraintAnnotation.fromDate(), true);
      String toDateString = (String) FieldUtils.readField(target, constraintAnnotation.toDate(), true);
      if(StringUtils.isEmpty(fromDateString) || StringUtils.isEmpty(toDateString)) {
        return true;
      }
      SimpleDateFormat sdf = new SimpleDateFormat(constraintAnnotation.dateFormat());
      sdf.setLenient(false);

      Date fromDate = sdf.parse(fromDateString);
      Date toDate = sdf.parse(toDateString);

      boolean isValid = (fromDate.before(toDate) || fromDate.equals(toDate));
      if (!isValid)
        context.buildConstraintViolationWithTemplate(constraintAnnotation.message()).
            addPropertyNode(constraintAnnotation.toDate()).addConstraintViolation();
      return isValid;
    } catch (ParseException e) {
      return false;
    }catch (IllegalAccessException e) {
      throw new ApplicationRuntimeException("Could not compare dates", e);
    }
  }
}
