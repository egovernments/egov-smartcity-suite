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
package org.egov.works.web.controller.letterofacceptance;

import org.egov.infra.admin.master.service.CityService;
import org.egov.infra.reporting.engine.ReportOutput;
import org.egov.infra.reporting.engine.ReportRequest;
import org.egov.infra.reporting.engine.ReportService;
import org.egov.works.letterofacceptance.service.LetterOfAcceptanceService;
import org.egov.works.lineestimate.entity.LineEstimateDetails;
import org.egov.works.lineestimate.service.LineEstimateService;
import org.egov.works.models.workorder.WorkOrder;
import org.egov.works.utils.WorksUtils;
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
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping(value = "/letterofacceptance")
public class LetterOfAcceptancePDFController {

    public static final String LETTEROFACCEPTANCEPDF = "letterOfAcceptancePDF";
    @Autowired
    private ReportService reportService;
    @Autowired
    private WorksUtils worksUtils;
    @Autowired
    private LineEstimateService lineEstimateService;
    @Autowired
    private LetterOfAcceptanceService letterOfAcceptanceService;
    @Autowired
    private CityService cityService;

    @RequestMapping(value = "/letterOfAcceptancePDF/{letterOfAcceptanceId}", method = RequestMethod.GET)
    public @ResponseBody
    ResponseEntity<byte[]> generateLineEstimatePDF(final HttpServletRequest request,
                                                   @PathVariable("letterOfAcceptanceId") final Long id) {
        final WorkOrder workOrder = letterOfAcceptanceService.getWorkOrderById(id);
        return generateReport(workOrder, request);
    }

    private ResponseEntity<byte[]> generateReport(final WorkOrder workOrder, final HttpServletRequest request) {
        final Map<String, Object> reportParams = new HashMap<>();
        ReportRequest reportInput = null;
        ReportOutput reportOutput;
        if (workOrder != null) {

            final SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            final DecimalFormat df = new DecimalFormat("0.00");
            final LineEstimateDetails lineEstimateDetails = lineEstimateService
                    .findByEstimateNumber(workOrder.getEstimateNumber());

            reportParams.put("cityLogo", cityService.getCityLogoURL());

            final String cityName = (String) request.getSession().getAttribute("citymunicipalityname");
            reportParams.put("cityName", cityName);
            reportParams.put("workOrderNumber",
                    workOrder.getWorkOrderNumber() != null ? workOrder.getWorkOrderNumber() : "");
            reportParams.put("workOrderDate",
                    workOrder.getWorkOrderDate() != null ? formatter.format(workOrder.getWorkOrderDate()) : "");
            reportParams.put("contractorName",
                    workOrder.getContractor().getName() != null ? workOrder.getContractor().getName() : "");
            reportParams.put("contractorAddress", workOrder.getContractor().getBankaccount() != null
                    ? workOrder.getContractor().getCorrespondenceAddress() : "");
            reportParams.put("panNo",
                    workOrder.getContractor().getPanNumber() != null ? workOrder.getContractor().getPanNumber() : "");
            reportParams.put("bank",
                    workOrder.getContractor().getBank() != null ? workOrder.getContractor().getBank().getName() : "");
            reportParams.put("accountNo", workOrder.getContractor().getBankaccount() != null
                    ? workOrder.getContractor().getBankaccount() : "");
            reportParams.put("subject", lineEstimateDetails.getNameOfWork());
            reportParams.put("modeOfAllotment", lineEstimateDetails.getLineEstimate().getModeOfAllotment());
            reportParams.put("agreementAmount", df.format(workOrder.getWorkOrderAmount()));
            reportParams.put("emd", df.format(workOrder.getEmdAmountDeposited()));
            reportParams.put("asd", df.format(workOrder.getSecurityDeposit()));
            reportParams.put("WINCode", lineEstimateDetails.getProjectCode().getCode());
            reportParams.put("amountOfEstimate",
                    lineEstimateDetails.getActualEstimateAmount().setScale(2, BigDecimal.ROUND_HALF_EVEN));
            reportParams.put("headOfAccount", lineEstimateDetails.getLineEstimate().getBudgetHead().getName());
            reportParams.put("ward", lineEstimateDetails.getLineEstimate().getWard().getName());
            String technicalSanctionByDesignation;
            if (lineEstimateDetails.getLineEstimate().getTechnicalSanctionBy() != null)
                technicalSanctionByDesignation = worksUtils
                        .getUserDesignation(lineEstimateDetails.getLineEstimate().getTechnicalSanctionBy());
            else
                technicalSanctionByDesignation = worksUtils
                        .getUserDesignation(lineEstimateDetails.getLineEstimate().getCreatedBy());
            reportParams.put("technicalSanctionByDesignation", technicalSanctionByDesignation);

            reportInput = new ReportRequest(LETTEROFACCEPTANCEPDF, workOrder.getContractor(), reportParams);

        }

        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/pdf"));
        headers.add("content-disposition", "inline;filename=LetterOfAcceptance.pdf");
        reportOutput = reportService.createReport(reportInput);
        return new ResponseEntity<>(reportOutput.getReportOutputData(), headers, HttpStatus.CREATED);

    }

}
