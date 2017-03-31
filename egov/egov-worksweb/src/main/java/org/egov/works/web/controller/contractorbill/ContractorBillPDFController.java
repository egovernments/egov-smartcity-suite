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
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.egov.commons.CChartOfAccounts;
import org.egov.commons.dao.ChartOfAccountsHibernateDAO;
import org.egov.infra.filestore.service.FileStoreService;
import org.egov.infra.reporting.engine.ReportConstants;
import org.egov.infra.reporting.engine.ReportOutput;
import org.egov.infra.reporting.engine.ReportRequest;
import org.egov.infra.reporting.engine.ReportService;
import org.egov.infra.utils.NumberUtil;
import org.egov.infra.web.utils.WebUtils;
import org.egov.model.bills.EgBilldetails;
import org.egov.works.contractorbill.entity.ContractorBillRegister;
import org.egov.works.contractorbill.service.ContractorBillRegisterService;
import org.egov.works.masters.entity.Contractor;
import org.egov.works.mb.service.MBHeaderService;
import org.egov.works.utils.WorksConstants;
import org.egov.works.utils.WorksUtils;
import org.egov.works.workorder.entity.WorkOrderEstimate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
@RequestMapping(value = "/contractorbill")
public class ContractorBillPDFController {

    @Autowired
    private ReportService reportService;

    @Autowired
    private WorksUtils worksUtils;

    @Autowired
    private ContractorBillRegisterService contractorBillRegisterService;

    @Autowired
    private ChartOfAccountsHibernateDAO chartOfAccountsHibernateDAO;

    @Autowired
    private MBHeaderService mbHeaderService;

    public static final String CONTRACTORBILLPDF = "ContractorBillPDF";

    @Autowired
    @Qualifier("fileStoreService")
    protected FileStoreService fileStoreService;

    @RequestMapping(value = "/contractorbillPDF/{contractorBillId}", method = RequestMethod.GET)
    @ResponseBody public ResponseEntity<byte[]> generateContractorBillPDF(final HttpServletRequest request,
            @PathVariable("contractorBillId") final Long id, final HttpSession session) throws IOException {
        final ContractorBillRegister contractorBillRegister = contractorBillRegisterService.getContractorBillById(id);
        return generateReport(contractorBillRegister, request);
    }

    private ResponseEntity<byte[]> generateReport(final ContractorBillRegister contractorBillRegister,
            final HttpServletRequest request) {
        final Map<String, Object> reportParams = new HashMap<String, Object>();
        ReportRequest reportInput = null;
        ReportOutput reportOutput;
        if (contractorBillRegister != null) {

            final SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm a");
            new DecimalFormat("#.##");
            final WorkOrderEstimate workOrderEstimate = contractorBillRegister.getWorkOrderEstimate();

            final String url = WebUtils.extractRequestDomainURL(request, false);

            reportParams.put("cityLogo", url.concat(ReportConstants.IMAGE_CONTEXT_PATH)
                    .concat((String) request.getSession().getAttribute("citylogo")));

            final String cityName = (String) request.getSession().getAttribute("citymunicipalityname");
            reportParams.put("cityName", cityName);
            if(workOrderEstimate != null){
                setContractorJsonValues(contractorBillRegister, reportParams, workOrderEstimate);
                if(workOrderEstimate.getWorkCompletionDate() != null)
                    reportParams.put("workCommencedDate", formatter.format(workOrderEstimate.getWorkCompletionDate()));
                else
                    reportParams.put("workCommencedDate", StringUtils.EMPTY);
                reportParams.put("win", workOrderEstimate.getEstimate().getProjectCode().getCode());
                reportParams.put("nameOfTheWork", workOrderEstimate.getEstimate().getName());
                reportParams.put("ward", workOrderEstimate.getEstimate().getWard().getName());
            }
            reportParams.put("billType", contractorBillRegister.getBilltype());
            reportParams.put("billNumber", contractorBillRegister.getBillnumber());
            reportParams.put("billDate", formatter.format(contractorBillRegister.getBilldate()));
            reportParams.put("billAmount", contractorBillRegister.getBillamount());
            reportParams.put("department", contractorBillRegister.getEgBillregistermis().getEgDepartment().getName());
            reportParams.put("reportRunDate", sdf.format(new Date()));
            reportParams.put("creatorName", contractorBillRegister.getCreatedBy().getName());
            reportParams.put("creatorDesignation",
                    worksUtils.getUserDesignation(contractorBillRegister.getCreatedBy()));
            reportParams.put("approverDesignation",
                    worksUtils.getUserDesignation(contractorBillRegister.getApprovedBy()));
            reportParams.put("approverName", contractorBillRegister.getApprovedBy() != null
                    ? contractorBillRegister.getApprovedBy().getName() : "N/A");
            reportParams.put("mbAmountExists",
                    contractorBillRegister.getWorkOrderEstimate().getWorkOrderActivities() != null
                            && !contractorBillRegister.getWorkOrderEstimate().getWorkOrderActivities().isEmpty() ? "Yes"
                                    : "No");
            reportParams.put("mbDetails", mbHeaderService.getApprovedMBHeadersByContractorBill(contractorBillRegister));
            reportInput = new ReportRequest(CONTRACTORBILLPDF, getBillDetailsMap(contractorBillRegister, reportParams),
                    reportParams);

        }

        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/pdf"));
        headers.add("content-disposition", "inline;filename=ContractorBill.pdf");
        reportOutput = reportService.createReport(reportInput);
        return new ResponseEntity<byte[]>(reportOutput.getReportOutputData(), headers, HttpStatus.CREATED);

    }

    private void setContractorJsonValues(final ContractorBillRegister contractorBillRegister,
            final Map<String, Object> reportParams, final WorkOrderEstimate workOrderEstimate) {
        final Contractor contractor = workOrderEstimate.getWorkOrder().getContractor();
        
        if (contractor != null) {
            if (contractor.getName() != null)
                reportParams.put("contractorName", contractor.getName());
            else
                reportParams.put("contractorName", org.egov.infra.utils.StringUtils.EMPTY);
            if (contractor.getCode() != null)
                reportParams.put("contractorCode", contractor.getCode());
            else
                reportParams.put("contractorCode", org.egov.infra.utils.StringUtils.EMPTY);
            if (contractor.getBank() != null)
                reportParams.put("bankAcc", contractor.getBankAccount());
            else
                reportParams.put("bankAcc", "N/A");
            if (StringUtils.isNotBlank(contractorBillRegister.getWorkOrderEstimate().getWorkOrder()
                    .getContractor().getPanNumber()))
                reportParams.put("panNo", contractorBillRegister.getWorkOrderEstimate().getWorkOrder()
                        .getContractor().getPanNumber());
            else
                reportParams.put("panNo", "N/A");
        }
    }

    public List<Map<String, Object>> getBillDetailsMap(final ContractorBillRegister contractorBillRegister,
            final Map<String, Object> reportParams) {
        final List<Map<String, Object>> billDetailsList = new ArrayList<Map<String, Object>>();
        Map<String, Object> billDetails = new HashMap<String, Object>();
        BigDecimal creditSum = BigDecimal.ZERO;
        BigDecimal debitSum = BigDecimal.ZERO;
        final List<CChartOfAccounts> contractorPayableAccountList = chartOfAccountsHibernateDAO
                .getAccountCodeByPurposeName(WorksConstants.CONTRACTOR_NETPAYABLE_PURPOSE);
        for (final EgBilldetails egBilldetails : contractorBillRegister.getEgBilldetailes())
            if (egBilldetails.getDebitamount() != null) {
                billDetails = new HashMap<String, Object>();
                final CChartOfAccounts coa = chartOfAccountsHibernateDAO.findById(egBilldetails.getGlcodeid().longValue(), false);
                billDetails.put("glcodeId", coa.getId());
                billDetails.put("glcode", coa.getGlcode());
                billDetails.put("accountHead", coa.getName());
                billDetails.put("amount", egBilldetails.getDebitamount());
                debitSum = debitSum.add(egBilldetails.getDebitamount());
                billDetails.put("isDebit", true);
                billDetails.put("isNetPayable", false);
                billDetailsList.add(billDetails);
            }
        for (final EgBilldetails egBilldetails : contractorBillRegister.getEgBilldetailes())
            if (egBilldetails.getCreditamount() != null) {
                billDetails = new HashMap<String, Object>();
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
                billDetailsList.add(billDetails);
            }
        reportParams.put("debitSum", debitSum);
        reportParams.put("creditSum", creditSum);
        BigDecimal netpayable = BigDecimal.ZERO;
        netpayable = debitSum.subtract(creditSum);
        reportParams.put("netPayable", netpayable);
        reportParams.put("netpayable", netpayable.setScale(2, BigDecimal.ROUND_HALF_EVEN));
        reportParams.put("totalAmountWords", NumberUtil.amountInWords(netpayable));

        return billDetailsList;
    }

}
