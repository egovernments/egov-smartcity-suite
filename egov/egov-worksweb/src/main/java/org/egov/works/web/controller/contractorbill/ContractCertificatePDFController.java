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
package org.egov.works.web.controller.contractorbill;

import java.io.IOException;
import java.util.Date;
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
import org.egov.infra.web.utils.WebUtils;
import org.egov.works.contractorbill.entity.ContractorBillCertificateInfo;
import org.egov.works.contractorbill.entity.ContractorBillRegister;
import org.egov.works.contractorbill.service.ContractorBillRegisterService;
import org.joda.time.DateTime;
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
@RequestMapping(value = "/contractcertificate")
public class ContractCertificatePDFController {

    @Autowired
    private ReportService reportService;

    @Autowired
    private ContractorBillRegisterService contractorBillRegisterService;

    public static final String CONTRACTCERTIFICATEPDF = "ContractCertificatePDF";

    @RequestMapping(value = "/contractcertificatePDF/{contractorBillId}", method = RequestMethod.GET)
    public @ResponseBody ResponseEntity<byte[]> generateContractorBillPDF(final HttpServletRequest request,
            @PathVariable("contractorBillId") final Long id, final HttpSession session) throws IOException {
        final ContractorBillRegister contractorBillRegister = contractorBillRegisterService.getContractorBillById(id);
        return generateReport(contractorBillRegister, request, session);
    }

    private ResponseEntity<byte[]> generateReport(final ContractorBillRegister contractorBillRegister,
            final HttpServletRequest request, final HttpSession session) {
        final Map<String, Object> reportParams = new HashMap<String, Object>();
        ReportRequest reportInput = null;
        ReportOutput reportOutput = null;

        if (contractorBillRegister != null) {
            final String url = WebUtils.extractRequestDomainURL(request, false);
            reportParams.put("cityLogo", url.concat(ReportConstants.IMAGE_CONTEXT_PATH)
                    .concat((String) request.getSession().getAttribute("citylogo")));

            final String cityName = ApplicationThreadLocals.getMunicipalityName();
            reportParams.put("cityName", cityName);
            reportParams.put("contractorBillRegister", contractorBillRegister);
            final Date lastPartBillDate = contractorBillRegisterService.getLastPartBillDateForContractorBill(
                    contractorBillRegister.getCreatedDate(), contractorBillRegister.getWorkOrderEstimate().getId());
            reportParams.put("lastPartBillDate",
                    lastPartBillDate != null ? DateUtils.getDefaultFormattedDate(lastPartBillDate) : "NA");
            reportParams.put("billDate", DateUtils.getDefaultFormattedDate(contractorBillRegister.getBilldate()));
            reportParams.put("reportRunDate", DateUtils.getFormattedDateWithTimeStamp(new DateTime()));
            final List<ContractorBillCertificateInfo> contractCertificateInfoList = contractorBillRegisterService
                    .getContractCertificateDetails(
                            contractorBillRegister, reportParams);
            reportInput = new ReportRequest(CONTRACTCERTIFICATEPDF, contractCertificateInfoList, reportParams);
        }

        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/pdf"));
        headers.add("content-disposition",
                "inline;filename=ContractCertificate_" + contractorBillRegister.getBillnumber() + ".pdf");
        reportOutput = reportService.createReport(reportInput);
        return new ResponseEntity<byte[]>(reportOutput.getReportOutputData(), headers, HttpStatus.CREATED);
    }

}
