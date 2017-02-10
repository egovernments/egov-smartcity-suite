
/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2015>  eGovernments Foundation
 *
 *     The updated version of eGov suite of products as by eGovernments Foundation
 *     is available at http://www.egovernments.org
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program. If not, see http://www.gnu.org/licenses/ or
 *     http://www.gnu.org/licenses/gpl.html .
 *
 *     In addition to the terms of the GPL license to be adhered to in using this
 *     program, the following additional terms are to be complied with:
 *
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
 *
 *         2) Any misrepresentation of the origin of the material is prohibited. It
 *            is required that all modified versions of this material be marked in
 *            reasonable ways as different from the original version.
 *
 *         3) This license does not grant any rights to any user of the program
 *            with regards to rights under trademark law for use of the trade names
 *            or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.lcms.transactions.service;

import java.text.SimpleDateFormat;

import org.apache.commons.lang.StringUtils;
import org.egov.eis.entity.Employee;
import org.egov.lcms.transactions.entity.EmployeeHearing;
import org.egov.lcms.transactions.entity.Hearings;
import org.egov.lcms.transactions.entity.LegalCase;
import org.egov.lcms.utils.LegalCaseUtil;
import org.egov.lcms.utils.constants.LcmsConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class LegalCaseSmsService {

    @Autowired
    @Qualifier("parentMessageSource")
    private MessageSource messageSource;

    @Autowired
    private LegalCaseUtil legalCaseUtil;

    public void sendSmsToOfficerIncharge(final LegalCase legalcase) {
        if (legalcase.getOfficerIncharge() != null && legalcase.getOfficerIncharge().getName() != null) {
            final String mobileNumber = legalCaseUtil.getOfficerInchargeMobileNumber(legalcase);
            // SMS for legalcase
            if (LcmsConstants.LEGALCASE_STATUS_CREATED.equalsIgnoreCase(legalcase.getStatus().getCode()))
                getSmsForLegalCase(legalcase, mobileNumber);

        }
    }

    public void getSmsForLegalCase(final LegalCase legalcase, final String mobileNo) {
        String smsMsg = null;
        smsMsg = SmsBodyByCodeAndArgsWithTypeForLegalcase("msg.createlegalcase.sms", legalcase);

        if (StringUtils.isNotBlank(mobileNo) && StringUtils.isNotBlank(smsMsg))
            legalCaseUtil.sendSMSOnLegalCase(mobileNo, smsMsg);

    }

    public String SmsBodyByCodeAndArgsWithTypeForLegalcase(final String code, final LegalCase legalcase) {
        String smsMsg = "";
        smsMsg = messageSource.getMessage(code, new String[] { legalCaseUtil.getOfficerInchargeName(legalcase),
                legalcase.getCaseNumber(), legalcase.getPetitionersNames(), legalcase.getRespondantNames() }, null);
        return smsMsg;
    }

    public void sendSmsToHearingEmployee(final Hearings hearings) {
        for (final EmployeeHearing hearingEmp : hearings.getTempEmplyeeHearing())
            getSmsForHearingsEmployee(hearings, hearingEmp.getEmployee());

    }

    public void getSmsForHearingsEmployee(final Hearings hearings, final Employee employee) {
        final String mobileNo = employee.getMobileNumber();
        String smsMsg = null;
        if (LcmsConstants.LEGALCASE_HEARING_STATUS.equalsIgnoreCase(hearings.getLegalCase().getStatus().getCode()))
            smsMsg = SmsBodyByCodeAndArgsWithTypeForHearings("msg.hearingemployee.sms", hearings, employee);

        if (StringUtils.isNotBlank(mobileNo) && StringUtils.isNotBlank(smsMsg))
            legalCaseUtil.sendSMSOnLegalCase(mobileNo, smsMsg);

    }

    public String SmsBodyByCodeAndArgsWithTypeForHearings(final String code, final Hearings hearings,
            final Employee employee) {
        String smsMsg = "";
        final SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        smsMsg = messageSource.getMessage(code,
                new String[] { employee.getName(), hearings.getLegalCase().getCaseNumber(),
                        formatter.format(hearings.getHearingDate()).toString(), hearings.getPurposeofHearings() },
                null);
        return smsMsg;
    }

}