/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2018  eGovernments Foundation
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
 *            Further, all user interfaces, including but not limited to citizen facing interfaces,
 *            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
 *            derived works should carry eGovernments Foundation logo on the top right corner.
 *
 *            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
 *            For any further queries on attribution, including queries on brand guidelines,
 *            please contact contact@egovernments.org
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
 *
 */
package org.egov.lcms.transactions.service;

import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.egov.infra.notification.entity.CalendarInviteInfo;
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
public class LegalCaseMailService {

    @Autowired
    @Qualifier("parentMessageSource")
    private MessageSource messageSource;

    @Autowired
    private LegalCaseUtil legalCaseUtil;

    public void sendCalendarInviteOnHearing(final Hearings hearings) {
        String mailId = LcmsConstants.BLANK;
        final String subject = messageSource.getMessage("calendar.invite.mail.subject",
                new String[] { hearings.getLegalCase().getCaseNumber(),
                        hearings.getLegalCase().getStatus().getDescription() },
                null);
        if (hearings.getLegalCase().getLegalCaseAdvocates() != null
                && !hearings.getLegalCase().getLegalCaseAdvocates().isEmpty())
            mailId = hearings.getLegalCase().getLegalCaseAdvocates().get(0).getAdvocateMaster()
                    .getEmail();
        CalendarInviteInfo calendarInfo = buildCalendarInfo(hearings.getLegalCase(), hearings.getHearingDate());
        if (hearings.getTempEmplyeeHearing() != null && !hearings.getTempEmplyeeHearing().isEmpty())
            for (final EmployeeHearing hearingEmp : hearings.getTempEmplyeeHearing())
                if (StringUtils.isNotBlank(hearingEmp.getEmployee().getEmailId()))
                    calendarInfo.addMailList(hearingEmp.getEmployee().getEmailId());
        calendarInfo.addMailList(mailId);
        if (StringUtils.isNotBlank(mailId) && StringUtils.isNotBlank(subject))
            legalCaseUtil.sendCalendarInviteOnLegalCase(mailId, subject, calendarInfo);

    }

    private CalendarInviteInfo buildCalendarInfo(LegalCase legalcase, Date hearingDate) {
        CalendarInviteInfo calendarInfo = new CalendarInviteInfo();
        // setting hearing court time
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(hearingDate);
        Calendar hearingCalendar = Calendar.getInstance();
        hearingCalendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE),
                Integer.valueOf(LcmsConstants.COURT_STARTTIME_HOUR), Integer.valueOf(LcmsConstants.COURT_STARTTIME_MINUTE));
        calendarInfo.setStartDateTime(LcmsConstants.DATEFORMATTER_YYYYMMDDTHHMMSSZ.format(hearingCalendar.getTime()));
        hearingCalendar.add(Calendar.HOUR, Integer.valueOf(LcmsConstants.COURT_WORKING_HOUR));
        calendarInfo.setEndDateTime(LcmsConstants.DATEFORMATTER_YYYYMMDDTHHMMSSZ.format(hearingCalendar.getTime()));
        StringBuilder location = new StringBuilder(legalcase.getCourtMaster().getName()).append(LcmsConstants.SEPARATOR)
                .append(legalcase.getCourtMaster().getAddress());
        calendarInfo.setLocation(location.toString());
        calendarInfo.setSummary(legalcase.getCaseTitle());
        calendarInfo.setDescription(legalcase.getCaseTitle());
        calendarInfo.setMailBodyMessage(messageSource.getMessage("calendar.invite.mailbody.casedetails",
                new String[] { legalcase.getCaseNumber(),
                        legalcase.getCaseDate().toString(),
                        legalcase.getCaseTypeMaster().getCaseType(),
                        legalcase.getCourtMaster().getName(),
                        legalcase.getCaseTitle(),
                        legalcase.getPetitionersNames(),
                        legalcase.getRespondantNames() },
                null));

        // setting calendar invite mail list including in charge officer and temporary employees
        if (legalcase.getOfficerIncharge() != null
                && legalcase.getOfficerIncharge().getName() != null)
            calendarInfo.addMailList(legalCaseUtil.getOfficerInchargeMailId(legalcase));

        return calendarInfo;
    }

}