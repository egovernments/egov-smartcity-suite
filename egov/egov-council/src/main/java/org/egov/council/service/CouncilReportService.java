/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2016>  eGovernments Foundation
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
package org.egov.council.service;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.WordUtils;
import org.egov.council.entity.CouncilAgendaDetails;
import org.egov.council.entity.CouncilMeeting;
import org.egov.council.entity.MeetingMOM;
import org.egov.infra.reporting.engine.ReportConstants;
import org.egov.infra.reporting.engine.ReportOutput;
import org.egov.infra.reporting.engine.ReportRequest;
import org.egov.infra.reporting.engine.ReportService;
import org.egov.infra.reporting.util.ReportUtil;
import org.egov.infra.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class CouncilReportService {

    private final String AGENDA = "agenda";
    private final String MEETINGMOM = "meetingMom";
    private final String subReportPath = "agendaDetails.jasper";
    private final Map<String, Object> reportParams = new HashMap<String, Object>();
    private ReportRequest reportInput = null;

    @Autowired
    private ReportService reportService;

    public byte[] generatePDFForAgendaDetails(CouncilMeeting councilMeeting, String logoPath) {
        if (councilMeeting != null) {
            final Map<String, Object> agendaDetails = new HashMap<String, Object>();
            List<CouncilAgendaDetails> agendaDetailsList = councilMeeting.getMeetingMOMs().get(0).getAgenda()
                    .getAgendaDetails();

            Collections.sort(agendaDetailsList, new Comparator<CouncilAgendaDetails>() {
                @Override
                public int compare(CouncilAgendaDetails f1, CouncilAgendaDetails f2) {
                    return f1.getItemNumber().compareTo(f2.getItemNumber());
                }
            });

            agendaDetails.put("agendaList", agendaDetailsList);
            reportInput = new ReportRequest(AGENDA, agendaDetails, buildReportParameters(councilMeeting, logoPath));
        }
        reportInput.setReportFormat(ReportConstants.FileFormat.PDF);
        reportInput.setPrintDialogOnOpenReport(false);
      return createReport(reportInput).getReportOutputData();

    }
    public byte[] generatePDFForMom(CouncilMeeting councilMeeting, String logoPath) {
        if (councilMeeting != null) {
            final Map<String, Object> momDetails = new HashMap<String, Object>();
            List<MeetingMOM> meetingMomList = councilMeeting.getMeetingMOMs();

            Collections.sort(meetingMomList, new Comparator<MeetingMOM>() {
                @Override
                public int compare(MeetingMOM f1, MeetingMOM f2) {
                    return f1.getItemNumber().compareTo(f2.getItemNumber());
                }
            });

            momDetails.put("meetingMOMList", meetingMomList);
            momDetails.put("meetingAttendenceList", councilMeeting.getMeetingAttendence());
            reportInput = new ReportRequest(MEETINGMOM, momDetails, buildReportParameters(councilMeeting, logoPath));
        }
        reportInput.setReportFormat(ReportConstants.FileFormat.PDF);
        reportInput.setPrintDialogOnOpenReport(false);
      return createReport(reportInput).getReportOutputData();

    }


    private Map<String, Object> buildReportParameters(CouncilMeeting councilMeeting, String logoPath) {

        StringBuffer meetingDateTimeLocation = new StringBuffer();

        reportParams.put("logoPath", logoPath);
        reportParams.put("commiteeType", WordUtils.capitalize(councilMeeting.getCommitteeType().getName()).toString());
        reportParams.put("meetingNumber", WordUtils.capitalize(councilMeeting.getMeetingNumber()).toString());
        meetingDateTimeLocation.append(DateUtils.getDefaultFormattedDate(councilMeeting.getMeetingDate()));
        if (null != councilMeeting.getMeetingTime()) {
            meetingDateTimeLocation.append(' ');
            meetingDateTimeLocation.append(councilMeeting.getMeetingTime());
        }
        if (null != councilMeeting.getMeetingLocation()) {
            meetingDateTimeLocation.append(' ');
            meetingDateTimeLocation.append(councilMeeting.getMeetingLocation());
        }
        reportParams.put("meetingDateTimePlace", meetingDateTimeLocation.toString());
        reportParams.put("cityName", ReportUtil.getCityName());
        //reportParams.put("agendaSubReportPath", ReportUtil.getTemplateAsStream(subReportPath));
        return reportParams;
    }

    public ReportOutput createReport(final ReportRequest reportRequest) {
        return reportService.createReport(reportRequest);
    }

}
