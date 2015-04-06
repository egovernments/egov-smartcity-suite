package org.egov.pgr.formatter;

import java.text.ParseException;
import java.util.Locale;

import org.egov.pgr.entity.ComplaintType;
import org.egov.pgr.service.ComplaintTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

@Component
public class ComplaintTypeFormatter implements Formatter<ComplaintType> {

	private ComplaintTypeService complaintTypeService;

	@Autowired
	public ComplaintTypeFormatter(ComplaintTypeService complaintTypeService) {
		this.complaintTypeService = complaintTypeService;
	}

	@Override
	public ComplaintType parse(String name, Locale locale)
			throws ParseException {
		return (ComplaintType) complaintTypeService.findByName(name);
	}

	@Override
	public String print(ComplaintType complaintType, Locale locale) {
		return complaintType.getName();
	}

}
