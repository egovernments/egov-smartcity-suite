package org.egov.pgr.web.formatter;

import java.text.ParseException;
import java.util.Locale;

import org.egov.pgr.entity.ComplaintStatus;
import org.egov.pgr.service.ComplaintStatusService;
import org.egov.pgr.service.ComplaintTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

@Component
public class ComplaintStatusFormatter implements Formatter<ComplaintStatus> {

    private final ComplaintStatusService complaintStatusService;

    @Autowired
    public ComplaintStatusFormatter(final ComplaintStatusService complaintStatusService) {
        this.complaintStatusService = complaintStatusService;
    }

    @Override
    public ComplaintStatus parse(final String status, final Locale locale) throws ParseException {
       
    	return complaintStatusService.load(Long.valueOf(status));
    }

    @Override
    public String print(final ComplaintStatus status, final Locale locale) {
        return status.getName();
    }

}
