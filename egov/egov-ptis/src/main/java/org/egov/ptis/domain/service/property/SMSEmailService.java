package org.egov.ptis.domain.service.property;

import org.egov.infra.admin.master.service.CityService;
import org.egov.infra.messaging.sms.SMSService;
import org.egov.infra.utils.EgovThreadLocals;
import org.egov.ptis.domain.entity.property.Property;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ResourceBundleMessageSource;

public class SMSEmailService {

    @Autowired
    private CityService cityService;

    @Autowired
    private SMSService smsService;

    public String getCityName() {
        return cityService.getCityByURL(EgovThreadLocals.getDomainName()).getName();
    }

    public void sendSMSOnNewAssessment(final String mobileNumber, final String smsBody) {
        smsService.sendSMS(smsBody, "91" + mobileNumber);
    }
}
