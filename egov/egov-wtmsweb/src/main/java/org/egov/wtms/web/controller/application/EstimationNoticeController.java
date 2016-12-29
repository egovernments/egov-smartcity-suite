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

import org.apache.commons.lang.WordUtils;
import org.egov.infra.reporting.engine.ReportOutput;
import org.egov.infra.reporting.engine.ReportRequest;
import org.egov.infra.reporting.engine.ReportService;
import org.egov.infra.utils.NumberToWord;
import org.egov.infra.utils.autonumber.AutonumberServiceBeanResolver;
import org.egov.ptis.domain.model.AssessmentDetails;
import org.egov.ptis.domain.model.OwnerName;
import org.egov.ptis.domain.model.enums.BasicPropertyStatus;
import org.egov.ptis.domain.service.property.PropertyExternalService;
import org.egov.wtms.application.entity.WaterConnectionDetails;
import org.egov.wtms.application.service.WaterConnectionDetailsService;
import org.egov.wtms.autonumber.EstimationNumberGenerator;
import org.egov.wtms.utils.PropertyExtnUtils;
import org.egov.wtms.utils.constants.WaterTaxConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping(value = "/application")
public class EstimationNoticeController {

    @Autowired
    private ReportService reportService;
    
    @Autowired
    private AutonumberServiceBeanResolver beanResolver;

    public static final String ESTIMATION_NOTICE = "estimationNotice";
    @Autowired
    private PropertyExtnUtils propertyExtnUtils;
    private final Map<String, Object> reportParams = new HashMap<String, Object>();
    private ReportRequest reportInput = null;
    private ReportOutput reportOutput = null;

    @Autowired
    private WaterConnectionDetailsService waterConnectionDetailsService;

    @RequestMapping(value = "/estimationNotice", method = RequestMethod.GET)
    public @ResponseBody ResponseEntity<byte[]> generateEstimationNotice(final HttpServletRequest request,
            final HttpSession session) {
        EstimationNumberGenerator estimationNoGen = beanResolver.getAutoNumberServiceFor(EstimationNumberGenerator.class);
        
        final WaterConnectionDetails waterConnectionDetails = waterConnectionDetailsService
                .findByApplicationNumber(request.getParameter("pathVar"));
        waterConnectionDetails.setEstimationNumber(estimationNoGen.generateEstimationNumber());
        waterConnectionDetailsService.saveAndFlushWaterConnectionDetail(waterConnectionDetails);
        return generateReport(waterConnectionDetails, session);
    }

    private ResponseEntity<byte[]> generateReport(final WaterConnectionDetails waterConnectionDetails,
            final HttpSession session) {
        if (waterConnectionDetails != null) {
            final SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            final AssessmentDetails assessmentDetails = propertyExtnUtils.getAssessmentDetailsForFlag(
                    waterConnectionDetails.getConnection().getPropertyIdentifier(),
                    PropertyExternalService.FLAG_FULL_DETAILS,BasicPropertyStatus.ACTIVE);
            final String doorNo[] = assessmentDetails.getPropertyAddress().split(",");
            String ownerName = "";
            double totalCharges = 0;
            for (final OwnerName names : assessmentDetails.getOwnerNames()) {
                ownerName = names.getOwnerName();
                break;
            }
                reportParams.put("applicationType",
                        WordUtils.capitalize(waterConnectionDetails.getApplicationType().getName()).toString());
            reportParams.put("cityName", session.getAttribute("citymunicipalityname"));
            reportParams.put("district", session.getAttribute("districtName"));
            reportParams.put("estimationDate",
                    formatter.format(waterConnectionDetails.getFieldInspectionDetails().getCreatedDate()));
            reportParams.put("estimationNumber", waterConnectionDetails.getEstimationNumber());
            reportParams.put("donationCharges", waterConnectionDetails.getDonationCharges());
            totalCharges = waterConnectionDetails.getDonationCharges()+waterConnectionDetails.getFieldInspectionDetails().getSupervisionCharges()+
                    waterConnectionDetails.getFieldInspectionDetails().getRoadCuttingCharges()+waterConnectionDetails.getFieldInspectionDetails().getSecurityDeposit();
            reportParams.put("totalCharges",totalCharges);
            reportParams.put("applicationDate", formatter.format(waterConnectionDetails.getApplicationDate()));
            reportParams.put("applicantName", ownerName);
            reportParams.put("address", assessmentDetails.getPropertyAddress());
            reportParams.put("houseNo", doorNo[0]);
            reportParams.put("propertyID", waterConnectionDetails.getConnection().getPropertyIdentifier());
            reportParams.put("amountInWords", getTotalAmntInWords(totalCharges));
            reportParams.put("securityDeposit", waterConnectionDetails.getFieldInspectionDetails().getSecurityDeposit());
            reportParams.put("roadCuttingCharges", waterConnectionDetails.getFieldInspectionDetails().getRoadCuttingCharges());
            reportParams.put("superVisionCharges", waterConnectionDetails.getFieldInspectionDetails().getSupervisionCharges());
            reportInput = new ReportRequest(ESTIMATION_NOTICE, waterConnectionDetails, reportParams);
        }
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/pdf"));
        headers.add("content-disposition", "inline;filename=EstimationNotice.pdf");
        reportOutput = reportService.createReport(reportInput);
        return new ResponseEntity<byte[]>(reportOutput.getReportOutputData(), headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/estimationNotice/view/{applicationNumber}", method = RequestMethod.GET)
    public @ResponseBody ResponseEntity<byte[]> viewEstimationNotice(@PathVariable final String applicationNumber,
            final HttpSession session) {
        final WaterConnectionDetails waterConnectionDetails = waterConnectionDetailsService
                .findByApplicationNumber(applicationNumber);
        return generateReport(waterConnectionDetails, session);
    }
    
    public String getTotalAmntInWords(Double totalCharges) {
        return NumberToWord.amountInWords(totalCharges);
    }
}