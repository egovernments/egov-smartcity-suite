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

package org.egov.works.web.controller.lineestimate;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.infra.reporting.engine.ReportConstants;
import org.egov.infra.reporting.engine.ReportOutput;
import org.egov.infra.reporting.engine.ReportRequest;
import org.egov.infra.reporting.engine.ReportService;
import org.egov.infra.utils.DateUtils;
import org.egov.infra.utils.NumberUtil;
import org.egov.infra.web.utils.WebUtils;
import org.egov.works.lineestimate.entity.LineEstimate;
import org.egov.works.lineestimate.entity.LineEstimateAppropriation;
import org.egov.works.lineestimate.entity.LineEstimateDetails;
import org.egov.works.lineestimate.service.LineEstimateService;
import org.egov.works.utils.WorksConstants;
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

@Controller
@RequestMapping(value = "/lineestimate")
public class LineEstimatePDFController {

    @Autowired
    private ReportService reportService;

    @Autowired
    private LineEstimateService lineEstimateService;

    public static final String LINEESTIMATEPDF = "lineEstimatePDF";

    @RequestMapping(value = "/lineEstimatePDF/{lineEstimateId}", method = RequestMethod.GET)
    public @ResponseBody ResponseEntity<byte[]> generateLineEstimatePDF(final HttpServletRequest request,
            @PathVariable("lineEstimateId") final Long id,
            final HttpSession session) throws IOException {
        final LineEstimate lineEstimate = lineEstimateService.getLineEstimateById(id);
        return generateReport(lineEstimate, request, session);
    }

    private ResponseEntity<byte[]> generateReport(final LineEstimate lineEstimate, final HttpServletRequest request,
            final HttpSession session) {
        final Map<String, Object> reportParams = new HashMap<String, Object>();
        ReportRequest reportInput = null;
        ReportOutput reportOutput = null;

        if (lineEstimate != null) {

            final String url = WebUtils.extractRequestDomainURL(request, false);
            reportParams.put("cityLogo", url.concat(ReportConstants.IMAGE_CONTEXT_PATH)
                    .concat((String) request.getSession().getAttribute("citylogo")));

            reportParams.put("cityName", ApplicationThreadLocals.getMunicipalityName());
            reportParams.put("proNo", lineEstimate.getAdminSanctionNumber() != null ? lineEstimate.getAdminSanctionNumber() : "");
            reportParams.put("sub", lineEstimate.getSubject());
            reportParams.put("ref", lineEstimate.getReference());
            reportParams.put("dated",
                    lineEstimate.getAdminSanctionDate() != null
                            ? DateUtils.getFormattedDate(lineEstimate.getAdminSanctionDate(), "dd/MM/yyyy") : "");
            reportParams.put("scheme", lineEstimate.getScheme() != null ? lineEstimate.getScheme().getName() : "");
            reportParams.put("function", lineEstimate.getFunction() != null ? lineEstimate.getFunction().getName() : "");
            reportParams.put("account", lineEstimate.getBudgetHead() != null ? lineEstimate.getBudgetHead().getName() : "");
            reportParams.put("modeOfAllotment", lineEstimate.getModeOfAllotment());
            reportParams.put("workCategory", lineEstimate.getWorkCategory().toString().replace("_", " ") + " - "
                    + lineEstimate.getBeneficiary().toString().replaceAll("_C", "/C").replace("_", " "));
            reportParams.put("present",
                    lineEstimate.getAdminSanctionBy() != null ? lineEstimate.getAdminSanctionBy() : "");
            reportParams.put("zonalCommissioner", WorksConstants.DESIGNATION_COMMISSIONER);
            reportParams.put("zonalCommissionerCapital", WorksConstants.DESIGNATION_COMMISSIONER.toUpperCase());
            reportParams.put("beneficiary",
                    lineEstimate.getBeneficiary() != null ? lineEstimate.getBeneficiary().toString() : "");

            String basNos = "";
            BigDecimal totalAmount = BigDecimal.ZERO;

            for (final LineEstimateDetails led : lineEstimate.getLineEstimateDetails()) {
                final List<LineEstimateAppropriation> leas = led.getLineEstimateAppropriations();
                for (final LineEstimateAppropriation lea : leas)
                    basNos += lea.getBudgetUsage().getAppropriationnumber() + ", ";
                totalAmount = totalAmount.add(led.getEstimateAmount());
            }
            if (basNos.endsWith(", "))
                basNos = basNos.substring(0, basNos.length() - 2);

            reportParams.put("basNos", basNos);
            reportParams.put("totalAmount", totalAmount.setScale(2, BigDecimal.ROUND_HALF_EVEN));
            reportParams.put("totalAmountWords", NumberUtil.amountInWords(totalAmount));

            reportInput = new ReportRequest(LINEESTIMATEPDF, lineEstimate.getLineEstimateDetails(), reportParams);

        }

        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/pdf"));
        headers.add("content-disposition", "inline;filename=LineEstimate.pdf");
        reportOutput = reportService.createReport(reportInput);
        return new ResponseEntity<byte[]>(reportOutput.getReportOutputData(), headers, HttpStatus.CREATED);

    }

}