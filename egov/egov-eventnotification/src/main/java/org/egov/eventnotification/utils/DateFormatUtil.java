package org.egov.eventnotification.utils;

import static org.egov.eventnotification.constants.Constants.DDMMYYYY;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.stereotype.Service;

@Service
public class DateFormatUtil {

    public Date getDateInDDMMYYYY(Date date) throws ParseException {
        DateFormat formatter = new SimpleDateFormat(DDMMYYYY);
        return formatter.parse(formatter.format(date));
    }
}
