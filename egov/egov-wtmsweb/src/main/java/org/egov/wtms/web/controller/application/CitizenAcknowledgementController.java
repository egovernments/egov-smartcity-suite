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
package org.egov.wtms.web.controller.application;

import static org.egov.wtms.utils.constants.WaterTaxConstants.PERMENENTCLOSE;
import static org.egov.wtms.utils.constants.WaterTaxConstants.TEMPERARYCLOSE;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.WordUtils;
import org.egov.infra.admin.master.service.CityService;
import org.egov.infra.reporting.engine.ReportOutput;
import org.egov.infra.reporting.engine.ReportRequest;
import org.egov.infra.reporting.engine.ReportService;
import org.egov.ptis.domain.model.AssessmentDetails;
import org.egov.ptis.domain.model.OwnerName;
import org.egov.ptis.domain.model.enums.BasicPropertyStatus;
import org.egov.ptis.domain.service.property.PropertyExternalService;
import org.egov.wtms.application.entity.WaterConnectionDetails;
import org.egov.wtms.application.service.WaterConnectionDetailsService;
import org.egov.wtms.masters.service.ApplicationProcessTimeService;
import org.egov.wtms.utils.PropertyExtnUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/application")
public class CitizenAcknowledgementController {

    @Autowired
    private ReportService reportService;

    @Autowired
    private ApplicationProcessTimeService applicationProcessTimeService;
    public static final String CITIZEN_ACKNOWLDGEMENT = "citizenAcknowledgement";

    @Autowired
    private PropertyExtnUtils propertyExtnUtils;

    @Autowired
    private CityService cityService;

    @Autowired
    private WaterConnectionDetailsService waterConnectionDetailsService;

    @RequestMapping(value = "/citizeenAcknowledgement", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<byte[]> generateEstimationNotice(final HttpServletRequest request,
            final HttpSession session) {

        final WaterConnectionDetails waterConnectionDetails = waterConnectionDetailsService
                .findByApplicationNumber(request.getParameter("pathVars"));
        return generateReport(waterConnectionDetails, session);
    }

    private ResponseEntity<byte[]> generateReport(final WaterConnectionDetails waterConnectionDetails,
            final HttpSession session) {
        ReportRequest reportInput = null;
        ReportOutput reportOutput;
        final Map<String, Object> reportParams = new HashMap<>();
        if (waterConnectionDetails != null) {
            final SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            final AssessmentDetails assessmentDetails = propertyExtnUtils.getAssessmentDetailsForFlag(
                    waterConnectionDetails.getConnection().getPropertyIdentifier(),
                    PropertyExternalService.FLAG_FULL_DETAILS, BasicPropertyStatus.ACTIVE);
            final String doorNo[] = assessmentDetails.getPropertyAddress().split(",");
            String ownerName = "";
            for (final OwnerName names : assessmentDetails.getOwnerNames()) {
                ownerName = names.getOwnerName();
                break;
            }
            final Integer appProcessTime = applicationProcessTimeService.getApplicationProcessTime(
                    waterConnectionDetails.getApplicationType(), waterConnectionDetails.getCategory());
            if (appProcessTime != null)
                reportParams.put("applicationDueDate",
                        formatter.format(waterConnectionDetailsService.getDisposalDate(waterConnectionDetails, appProcessTime)));

            else
                reportParams.put("applicationDueDate", null);

            final String districtName = (String) session.getAttribute("districtName");
            reportParams.put("cityUrl", (!cityService.findAll().isEmpty() ? cityService.findAll().get(0).getName().toLowerCase()
                    : districtName.toLowerCase()) + ".cdma.ap.gov.in");
            reportParams.put("applicationType",
                    WordUtils.capitalize(waterConnectionDetails.getApplicationType().getName()).toString());
            reportParams.put("cityName", session.getAttribute("citymunicipalityname"));
            reportParams.put("district", session.getAttribute("districtName"));
            reportParams.put("applicationNumber", waterConnectionDetails.getApplicationNumber());
            reportParams.put("applicationDate", formatter.format(waterConnectionDetails.getApplicationDate()));
            reportParams.put("applicantName", ownerName);
            reportParams.put("address", assessmentDetails.getPropertyAddress());
            reportParams.put("houseNo", doorNo[0]);
            reportParams.put("propertyID", waterConnectionDetails.getConnection().getPropertyIdentifier());
            reportParams.put("electionWard", assessmentDetails.getBoundaryDetails().getAdminWardName());
            if (waterConnectionDetails.getCloseConnectionType() != null)
                if ("T".equals(waterConnectionDetails.getCloseConnectionType().toString()))
                    waterConnectionDetails.setCloseConnectionType(TEMPERARYCLOSE);
                else if ("P".equals(waterConnectionDetails.getCloseConnectionType().toString()))
                    waterConnectionDetails.setCloseConnectionType(PERMENENTCLOSE);
            reportParams.put("closeconnectiontype", waterConnectionDetails.getCloseConnectionType());
            if (waterConnectionDetails.getCloseconnectionreason() != null)
                reportParams.put("closeconnectionreason", waterConnectionDetails.getCloseconnectionreason().toString());
            if (waterConnectionDetails.getReConnectionReason() != null)
                reportParams.put("reconnectionreason", waterConnectionDetails.getReConnectionReason().toString());
            reportInput = new ReportRequest(CITIZEN_ACKNOWLDGEMENT, waterConnectionDetails, reportParams);
        }
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/pdf"));
        headers.add("content-disposition", "inline;filename=EstimationNotice.pdf");
        reportOutput = reportService.createReport(reportInput);
        return new ResponseEntity<>(reportOutput.getReportOutputData(), headers, HttpStatus.CREATED);
    }

}