package org.egov.pgr.web.formatter;

import java.text.ParseException;
import java.util.Locale;

import org.egov.pgr.entity.ComplaintType;
import org.egov.pgr.service.ComplaintTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

@Component
public class ComplaintTypeFormatter implements Formatter<ComplaintType> {

    private final ComplaintTypeService complaintTypeService;

    @Autowired
    public ComplaintTypeFormatter(final ComplaintTypeService complaintTypeService) {
        this.complaintTypeService = complaintTypeService;
    }

    @Override
    public ComplaintType parse(final String complaintTypeName, final Locale locale) throws ParseException {
        return complaintTypeService.findByName(complaintTypeName);
    }

    @Override
    public String print(final ComplaintType complaintType, final Locale locale) {
        return complaintType.getName();
    }

}
