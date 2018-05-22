package org.egov.eventnotification.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.egov.eventnotification.constants.Constants;
import org.springframework.stereotype.Service;

@Service
public class DateFormatUtil {

    public Date getDateInDDMMYYYY(Date date) throws ParseException {
        DateFormat formatter = new SimpleDateFormat(Constants.DDMMYYYY);
        return formatter.parse(formatter.format(date));
    }
}
