/**
 * eGov suite of products aim to improve the internal efficiency,transparency,
   accountability and the service delivery of the government  organizations.

    Copyright (C) <2015>  eGovernments Foundation

    The updated version of eGov suite of products as by eGovernments Foundation
    is available at http://www.egovernments.org

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program. If not, see http://www.gnu.org/licenses/ or
    http://www.gnu.org/licenses/gpl.html .

    In addition to the terms of the GPL license to be adhered to in using this
    program, the following additional terms are to be complied with:

	1) All versions of this program, verbatim or modified must carry this
	   Legal Notice.

	2) Any misrepresentation of the origin of the material is prohibited. It
	   is required that all modified versions of this material be marked in
	   reasonable ways as different from the original version.

	3) This license does not grant any rights to any user of the program
	   with regards to rights under trademark law for use of the trade names
	   or trademarks of eGovernments Foundation.

  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.wtms.utils;

import org.egov.infra.admin.master.entity.AppConfigValues;
import org.egov.infra.admin.master.service.AppConfigValueService;
import org.egov.infra.admin.master.service.CityWebsiteService;
import org.egov.infra.messaging.email.EmailService;
import org.egov.infra.messaging.sms.SMSService;
import org.egov.infra.utils.EgovThreadLocals;
import org.egov.wtms.application.entity.WaterConnectionDetails;
import org.egov.wtms.utils.constants.WaterTaxConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Service;

@Service
public class WaterTaxUtils {

    @Autowired
    private AppConfigValueService appConfigValuesService;

    @Autowired
    private CityWebsiteService cityWebsiteService;

    @Autowired
    private SMSService smsService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private ResourceBundleMessageSource messageSource;

    public Boolean isSmsEnabled() {
        final AppConfigValues appConfigValue = appConfigValuesService.getConfigValuesByModuleAndKey(
                WaterTaxConstants.MODULE_NAME, "SENDSMSFORWATERTAX").get(0);
        return "YES".equalsIgnoreCase(appConfigValue.getValue());
    }

    public Boolean isEmailEnabled() {
        final AppConfigValues appConfigValue = appConfigValuesService.getConfigValuesByModuleAndKey(
                WaterTaxConstants.MODULE_NAME, "SENDEMAILFORWATERTAX").get(0);
        return "YES".equalsIgnoreCase(appConfigValue.getValue());
    }

    public String getCityName() {
        return cityWebsiteService.getCityWebSiteByURL(EgovThreadLocals.getDomainName()).getCityName();
    }

    public String getCityCode() {
        return cityWebsiteService.getCityWebSiteByURL(EgovThreadLocals.getDomainName()).getCode();
    }

    public String smsAndEmailBodyByCodeAndArgs(final String code, final WaterConnectionDetails waterConnectionDetails,
            final String applicantName) {
        final String smsMsg = messageSource.getMessage(code,
                new String[] { applicantName, waterConnectionDetails.getApplicationNumber(), getCityName() }, null);
        return smsMsg;
    }

    public String emailBodyforApprovalEmailByCodeAndArgs(final String code,
            final WaterConnectionDetails waterConnectionDetails, final String applicantName) {
        final String smsMsg = messageSource.getMessage(code,
                new String[] { applicantName, waterConnectionDetails.getApplicationNumber(),
                        waterConnectionDetails.getConnection().getConsumerCode(), getCityName() }, null);
        return smsMsg;
    }

    public String emailSubjectforEmailByCodeAndArgs(final String code, final String applicationNumber) {
        final String emailSubject = messageSource.getMessage(code, new String[] { applicationNumber }, null);
        return emailSubject;
    }

    public void sendSMSOnWaterConnection(final WaterConnectionDetails waterConnectionDetails, final String smsBody) {
        smsService.sendSMS(smsBody, "91" + waterConnectionDetails.getConnection().getMobileNumber());
    }

    public void sendEmailOnWaterConnection(final WaterConnectionDetails waterConnectionDetails, final String emailBody,
            final String emailSubject) {
        emailService.sendMail(waterConnectionDetails.getConnection().getEmail(), emailBody, emailSubject);
    }

}
