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
package org.egov.wtms.web.controller.application;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.WordUtils;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.filestore.service.FileStoreService;
import org.egov.infra.reporting.engine.ReportFormat;
import org.egov.infra.reporting.engine.ReportOutput;
import org.egov.infra.reporting.engine.ReportRequest;
import org.egov.infra.reporting.engine.ReportService;
import org.egov.ptis.domain.model.AssessmentDetails;
import org.egov.ptis.domain.model.OwnerName;
import org.egov.ptis.domain.model.enums.BasicPropertyStatus;
import org.egov.ptis.domain.service.property.PropertyExternalService;
import org.egov.wtms.application.entity.FieldInspectionDetails;
import org.egov.wtms.application.entity.WaterConnectionDetails;
import org.egov.wtms.application.service.ReportGenerationService;
import org.egov.wtms.application.service.WaterConnectionDetailsService;
import org.egov.wtms.utils.PropertyExtnUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.egov.infra.reporting.util.ReportUtil.reportAsResponseEntity;
import static org.egov.infra.utils.DateUtils.toDefaultDateFormat;
import static org.egov.wtms.masters.entity.enums.ConnectionType.NON_METERED;
import static org.egov.wtms.utils.constants.WaterTaxConstants.FILESTORE_MODULECODE;
import static org.springframework.http.MediaType.APPLICATION_PDF_VALUE;

@Controller
@RequestMapping(value = "/application")
public class EstimationNoticeController {

    public static final String ESTIMATION_NOTICE = "estimationNotice";
    @Autowired
    private ReportService reportService;
    @Autowired
    private PropertyExtnUtils propertyExtnUtils;

    @Autowired
    private WaterConnectionDetailsService waterConnectionDetailsService;

    @Autowired
    private ReportGenerationService reportGenerationService;

    @Autowired
    private FileStoreService fileStoreService;

    @GetMapping(value = "/estimationNotice", produces = APPLICATION_PDF_VALUE)
    @ResponseBody
    public ResponseEntity<InputStreamResource> generateEstimationNotice(HttpServletRequest request,
                                                                        HttpSession session) {

        WaterConnectionDetails waterConnectionDetails = waterConnectionDetailsService
                .findByApplicationNumber(request.getParameter("pathVar"));
        return generateEstimationReport(waterConnectionDetails, session);
    }

    private ResponseEntity<InputStreamResource> generateEstimationReport(WaterConnectionDetails waterConnectionDetails,
                                                                         HttpSession session) {
        ReportOutput reportOutput = new ReportOutput();
        if (waterConnectionDetails != null)
            if (waterConnectionDetails.getEstimationNoticeFileStoreId() != null) {
                File file = fileStoreService.fetch(waterConnectionDetails.getEstimationNoticeFileStoreId(), FILESTORE_MODULECODE);
                reportOutput = new ReportOutput();
                try {
                    reportOutput.setReportName(waterConnectionDetails.getEstimationNumber());
                    reportOutput.setReportOutputData(FileUtils.readFileToByteArray(file));
                    reportOutput.setReportFormat(ReportFormat.PDF);
                } catch (IOException e) {
                    throw new ApplicationRuntimeException("Exception in generating work order notice" + e);
                }
            } else {

                Map<String, Object> reportParams = new HashMap<>();
                AssessmentDetails assessmentDetails = propertyExtnUtils.getAssessmentDetailsForFlag(
                        waterConnectionDetails.getConnection().getPropertyIdentifier(),
                        PropertyExternalService.FLAG_FULL_DETAILS, BasicPropertyStatus.ACTIVE);
                String[] doorNo = assessmentDetails.getPropertyAddress().split(",");
                StringBuilder ownerName = new StringBuilder();

                for (OwnerName names : assessmentDetails.getOwnerNames()) {
                    if (assessmentDetails.getOwnerNames().size() > 1)
                        ownerName.append(", ");
                    ownerName.append(names.getOwnerName());
                }

                reportParams.put("applicationType", WordUtils.capitalize(waterConnectionDetails.getApplicationType().getName()));
                reportParams.put("cityName", session.getAttribute("citymunicipalityname"));
                reportParams.put("district", session.getAttribute("districtName"));
                reportParams.put("estimationNumber", waterConnectionDetails.getEstimationNumber());
                reportParams.put("donationCharges", waterConnectionDetails.getDonationCharges());
                FieldInspectionDetails inspectionDetails = waterConnectionDetails.getFieldInspectionDetails();
                reportParams.put("estimationDate", toDefaultDateFormat(inspectionDetails.getCreatedDate()));
                double totalCharges = waterConnectionDetails.getDonationCharges()
                        + inspectionDetails.getSupervisionCharges()
                        + inspectionDetails.getRoadCuttingCharges()
                        + inspectionDetails.getSecurityDeposit();
                reportParams.put("totalCharges", totalCharges);
                reportParams.put("applicationDate", toDefaultDateFormat(waterConnectionDetails.getApplicationDate()));
                reportParams.put("applicantName", ownerName.toString());
                reportParams.put("address", assessmentDetails.getPropertyAddress());
                reportParams.put("houseNo", doorNo[0]);
                reportParams.put("propertyID", waterConnectionDetails.getConnection().getPropertyIdentifier());
                reportParams.put("amountInWords", reportGenerationService.getTotalAmntInWords(totalCharges));
                reportParams.put("securityDeposit", inspectionDetails.getSecurityDeposit());
                reportParams.put("roadCuttingCharges", inspectionDetails.getRoadCuttingCharges());
                reportParams.put("superVisionCharges", inspectionDetails.getSupervisionCharges());
                if (waterConnectionDetails.getConnectionType().equals(NON_METERED)) {
                    reportParams.put("estimationDetails", waterConnectionDetails.getEstimationDetails());
                    reportParams.put("designation", waterConnectionDetails.getState().getOwnerPosition().getDeptDesig().getDesignation().getName());
                    reportOutput = reportService.createReport(new ReportRequest("wtr_estimation_notice_for_non_metered",
                            waterConnectionDetails.getEstimationDetails(), reportParams));
                } else {
                    reportOutput = reportService.createReport(new ReportRequest(ESTIMATION_NOTICE,
                            waterConnectionDetails.getEstimationDetails(), reportParams));
                }

                reportOutput.setReportFormat(ReportFormat.PDF);
                reportOutput.setReportName(waterConnectionDetails.getEstimationNumber());
            }
        return reportAsResponseEntity(reportOutput);
    }

    @GetMapping(value = "/estimationNotice/view/{applicationNumber}", produces = APPLICATION_PDF_VALUE)
    @ResponseBody
    public ResponseEntity<InputStreamResource> viewEstimationNotice(@PathVariable String applicationNumber,
                                                                    HttpSession session) {
        WaterConnectionDetails waterConnectionDetails = waterConnectionDetailsService.findByApplicationNumber(applicationNumber);
        return generateEstimationReport(waterConnectionDetails, session);
    }

}