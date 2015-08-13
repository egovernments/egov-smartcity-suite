package org.egov.ptis.domain.service.property;

import org.egov.infra.admin.master.service.CityService;
import org.egov.infra.messaging.sms.SMSService;
import org.egov.infra.utils.EgovThreadLocals;
import org.springframework.beans.factory.annotation.Autowired;

public class SMSEmailService {

    @Autowired
    private CityService cityService;

    @Autowired
    private SMSService smsService;

    public String getCityName() {
        return cityService.getCityByURL(EgovThreadLocals.getDomainName()).getName();
    }

    public void sendSMSOnNewAssessment(final String mobileNumber, final String smsBody) {
        smsService.sendSMS("91" + mobileNumber, smsBody);
    }
}
