/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2017  eGovernments Foundation
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
package org.egov.ptis.web.controller.vacancyremission;

import org.egov.infra.admin.master.service.CityService;
import org.egov.infra.admin.master.service.UserService;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.infra.reporting.engine.ReportOutput;
import org.egov.infra.reporting.engine.ReportRequest;
import org.egov.infra.reporting.engine.ReportService;
import org.egov.ptis.domain.entity.property.VacancyRemission;
import org.egov.ptis.domain.service.property.VacancyRemissionService;
import org.egov.ptis.report.bean.PropertyAckNoticeInfo;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping(value = "/vacancyremission")
public class RejectionAcknowledgementController {

    public static final String REJECTION_ACK_TEMPLATE = "vacancyRemission_rejectionAck";
    @Autowired
    private ReportService reportService;
    @Autowired
    private VacancyRemissionService vacancyRemissionService;

    @Autowired
    private UserService userService;

    @Autowired
    private CityService cityService;

    @RequestMapping(value = "/rejectionacknowledgement", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<byte[]> generateRejectionAckNotice(final HttpServletRequest request) {
        String[] pathvars = request.getParameter("pathVar").split(",");
        final VacancyRemission vacancyRemission = vacancyRemissionService.getLatestRejectAckGeneratedVacancyRemissionForProperty(pathvars[0]);
        return generateReport(vacancyRemission, pathvars[1]);
    }

    private ResponseEntity<byte[]> generateReport(final VacancyRemission vacancyRemission, String rejectingUser) {
        final Map<String, Object> reportParams = new HashMap<>();
        ReportRequest reportInput = null;
        if (vacancyRemission != null) {
            final DateTimeFormatter formatter = DateTimeFormat.forPattern("dd/MM/yyyy");
            PropertyAckNoticeInfo ackBean = new PropertyAckNoticeInfo();
            DateTime dt = new DateTime(vacancyRemission.getState().getCreatedDate());
            reportParams.put("logoPath", cityService.getCityLogoURL());
            reportParams.put("cityName", cityService.getMunicipalityName());
            reportParams.put("loggedInUsername", userService.getUserById(ApplicationThreadLocals.getUserId()).getName());
            reportParams.put("rejectionDate", dt.toString(formatter));
            reportParams.put("rejectingUser", rejectingUser);
            ackBean.setAssessmentNo(vacancyRemission.getBasicProperty().getUpicNo());
            ackBean.setOwnerAddress(vacancyRemission.getBasicProperty().getAddress().toString());

            reportInput = new ReportRequest(REJECTION_ACK_TEMPLATE, ackBean, reportParams);
        }

        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/pdf"));
        headers.add("content-disposition", "inline;filename=RejectionNotice.pdf");
        ReportOutput reportOutput = reportService.createReport(reportInput);
        return new ResponseEntity<>(reportOutput.getReportOutputData(), headers, HttpStatus.CREATED);
    }
}
