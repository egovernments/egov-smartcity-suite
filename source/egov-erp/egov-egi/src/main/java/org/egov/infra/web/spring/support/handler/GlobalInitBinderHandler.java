package org.egov.infra.web.spring.support.handler;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.egov.infra.web.spring.support.propertyeditor.JodaDateTimeEditor;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.InitBinder;

@ControllerAdvice
public class GlobalInitBinderHandler {
    
    @Value("#{egovErpProperties.datePattern}")
    private String datePattern;
    
    @InitBinder
    public void initBinder(final WebDataBinder binder) {
        binder.registerCustomEditor(Date.class, new CustomDateEditor(new SimpleDateFormat(datePattern), true));
        binder.registerCustomEditor(DateTime.class, new JodaDateTimeEditor(datePattern, true));
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
        binder.setDisallowedFields("id");
    }

}
