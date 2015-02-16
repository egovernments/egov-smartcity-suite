package org.egov.pgr.web.formatter;

import java.text.ParseException;
import java.util.Locale;

import org.egov.pgr.entity.ReceivingCenter;
import org.egov.pgr.service.ReceivingCenterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

@Component
public class ReceivingCenterFormatter implements Formatter<ReceivingCenter> {

    private final ReceivingCenterService receivingCenterService;

    @Autowired
    public ReceivingCenterFormatter(final ReceivingCenterService receivingCenterService) {
        this.receivingCenterService = receivingCenterService;
    }

    @Override
    public ReceivingCenter parse(final String receivingCenterId, final Locale locale) throws ParseException {
        return receivingCenterService.load(Long.valueOf(receivingCenterId));
    }

    @Override
    public String print(final ReceivingCenter receivingCenter, final Locale locale) {
        return receivingCenter.getName();
    }

}
