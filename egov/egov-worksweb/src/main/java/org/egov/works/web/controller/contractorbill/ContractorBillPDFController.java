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

import org.egov.commons.CChartOfAccounts;
import org.egov.commons.dao.ChartOfAccountsHibernateDAO;
import org.egov.infra.admin.master.service.CityService;
import org.egov.infra.reporting.engine.ReportOutput;
import org.egov.infra.reporting.engine.ReportRequest;
import org.egov.infra.reporting.engine.ReportService;
import org.egov.infra.utils.NumberUtil;
import org.egov.model.bills.EgBilldetails;
import org.egov.works.contractorbill.entity.ContractorBillRegister;
import org.egov.works.contractorbill.service.ContractorBillRegisterService;
import org.egov.works.lineestimate.entity.LineEstimateDetails;
import org.egov.works.lineestimate.service.LineEstimateService;
import org.egov.works.mb.service.MBHeaderService;
import org.egov.works.models.measurementbook.MBHeader;
import org.egov.works.utils.WorksConstants;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/contractorbill")
public class ContractorBillPDFController {
    public static final String CONTRACTORBILLPDF = "ContractorBillPDF";

    @Autowired
    private ReportService reportService;

    @Autowired
    private WorksUtils worksUtils;

    @Autowired
    private LineEstimateService lineEstimateService;

    @Autowired
    private ContractorBillRegisterService contractorBillRegisterService;

    @Autowired
    private ChartOfAccountsHibernateDAO chartOfAccountsHibernateDAO;

    @Autowired
    private MBHeaderService mbHeaderService;

    @Autowired
    private CityService cityService;

    @RequestMapping(value = "/contractorbillPDF/{contractorBillId}", method = RequestMethod.GET)
    public @ResponseBody
    ResponseEntity<byte[]> generateContractorBillPDF(final HttpServletRequest request,
                                                     @PathVariable("contractorBillId") final Long id) {
        final ContractorBillRegister contractorBillRegister = contractorBillRegisterService.getContractorBillById(id);
        return generateReport(contractorBillRegister, request);
    }

    private ResponseEntity<byte[]> generateReport(final ContractorBillRegister contractorBillRegister,
                                                  final HttpServletRequest request) {
        final Map<String, Object> reportParams = new HashMap<>();
        ReportRequest reportInput = null;
        ReportOutput reportOutput;
        if (contractorBillRegister != null) {

            final SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm a");
            new DecimalFormat("#.##");
            final LineEstimateDetails lineEstimateDetails = lineEstimateService
                    .findByEstimateNumber(contractorBillRegister.getWorkOrder().getEstimateNumber());

            reportParams.put("cityLogo", cityService.getCityLogoURL());

            final String cityName = (String) request.getSession().getAttribute("citymunicipalityname");
            reportParams.put("cityName", cityName);
            reportParams.put("contractorName", contractorBillRegister.getWorkOrder().getContractor().getName() != null
                    ? contractorBillRegister.getWorkOrder().getContractor().getName() : "");
            reportParams.put("contractorCode", contractorBillRegister.getWorkOrder().getContractor().getCode() != null
                    ? contractorBillRegister.getWorkOrder().getContractor().getCode() : "");
            reportParams.put("bankAcc", contractorBillRegister.getWorkOrder().getContractor().getBank() != null
                    ? contractorBillRegister.getWorkOrder().getContractor().getBankaccount() : "N/A");
            reportParams.put("panNo", !contractorBillRegister.getWorkOrder().getContractor().getPanNumber().isEmpty()
                    ? contractorBillRegister.getWorkOrder().getContractor().getPanNumber() : "N/A");
            reportParams.put("billType", contractorBillRegister.getBilltype());
            reportParams.put("win", lineEstimateDetails.getProjectCode().getCode());
            reportParams.put("billNumber", contractorBillRegister.getBillnumber());
            reportParams.put("billDate", formatter.format(contractorBillRegister.getBilldate()));
            reportParams.put("billAmount", contractorBillRegister.getBillamount());
            reportParams.put("nameOfTheWork", lineEstimateDetails.getNameOfWork());
            reportParams.put("ward", lineEstimateDetails.getLineEstimate().getWard().getName());
            reportParams.put("department", lineEstimateDetails.getLineEstimate().getExecutingDepartment().getName());
            reportParams.put("reportRunDate", sdf.format(new Date()));
            reportParams.put("creatorName", contractorBillRegister.getCreatedBy().getName());
            reportParams.put("creatorDesignation",
                    worksUtils.getUserDesignation(contractorBillRegister.getCreatedBy()));
            reportParams.put("approverDesignation",
                    worksUtils.getUserDesignation(contractorBillRegister.getApprovedBy()));
            reportParams.put("approverName", contractorBillRegister.getApprovedBy().getName());
            final List<MBHeader> mbHeaders = mbHeaderService
                    .getApprovedMBHeadersByContractorBill(contractorBillRegister);
            reportParams.put("mbRefNo", mbHeaders != null && !mbHeaders.isEmpty() ? mbHeaders.get(0).getMbRefNo() : "");

            reportInput = new ReportRequest(CONTRACTORBILLPDF, getBillDetailsMap(contractorBillRegister, reportParams),
                    reportParams);

        }

        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/pdf"));
        headers.add("content-disposition", "inline;filename=ContractorBill.pdf");
        reportOutput = reportService.createReport(reportInput);
        return new ResponseEntity<>(reportOutput.getReportOutputData(), headers, HttpStatus.CREATED);

    }

    public List<Map<String, Object>> getBillDetailsMap(final ContractorBillRegister contractorBillRegister,
                                                       final Map<String, Object> reportParams) {
        final List<Map<String, Object>> billDetailsList = new ArrayList<>();
        Map<String, Object> billDetails = new HashMap<>();
        BigDecimal creditSum = BigDecimal.ZERO;
        BigDecimal debitSum = BigDecimal.ZERO;
        final List<CChartOfAccounts> contractorPayableAccountList = chartOfAccountsHibernateDAO
                .getAccountCodeByPurposeName(WorksConstants.CONTRACTOR_NETPAYABLE_PURPOSE);
        for (final EgBilldetails egBilldetails : contractorBillRegister.getEgBilldetailes()) {
            if (egBilldetails.getDebitamount() != null) {
                billDetails = new HashMap<>();
                final CChartOfAccounts coa = chartOfAccountsHibernateDAO
                        .findById(egBilldetails.getGlcodeid().longValue(), false);
                billDetails.put("glcodeId", coa.getId());
                billDetails.put("glcode", coa.getGlcode());
                billDetails.put("accountHead", coa.getName());
                billDetails.put("amount", egBilldetails.getDebitamount());
                debitSum = debitSum.add(egBilldetails.getDebitamount());
                billDetails.put("isDebit", true);
                billDetails.put("isNetPayable", false);
            } else if (egBilldetails.getCreditamount() != null) {
                billDetails = new HashMap<>();
                final CChartOfAccounts coa = chartOfAccountsHibernateDAO
                        .findById(egBilldetails.getGlcodeid().longValue(), false);
                billDetails.put("glcodeId", coa.getId());
                billDetails.put("glcode", coa.getGlcode());
                billDetails.put("accountHead", coa.getName());
                billDetails.put("amount", egBilldetails.getCreditamount());
                billDetails.put("isDebit", false);
                if (contractorPayableAccountList != null && !contractorPayableAccountList.isEmpty()
                        && contractorPayableAccountList.contains(coa))
                    billDetails.put("isNetPayable", true);
                else {
                    billDetails.put("isNetPayable", false);
                    creditSum = creditSum.add(egBilldetails.getCreditamount());
                }

            }
            reportParams.put("debitSum", debitSum);
            reportParams.put("creditSum", creditSum);
            BigDecimal netpayable = debitSum.subtract(creditSum);
            reportParams.put("netPayable", netpayable);
            reportParams.put("netpayable", netpayable.setScale(2, BigDecimal.ROUND_HALF_EVEN));
            reportParams.put("totalAmountWords", NumberUtil.amountInWords(netpayable));
            billDetailsList.add(billDetails);
        }
        return billDetailsList;
    }

}
