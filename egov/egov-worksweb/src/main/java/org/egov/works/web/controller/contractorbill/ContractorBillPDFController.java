/**
 * eGov suite of products aim to improve the internal efficiency,transparency,
   accountability and the service delivery of the government  organizations.

    Copyright (C) <2015>  eGovernments Foundation

    The updated version of eGov suite of products as by eGovernments Foundation
    is available at http://www.egovernments.org

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program. If not, see http://www.gnu.org/licenses/ or
    http://www.gnu.org/licenses/gpl.html .

    In addition to the terms of the GPL license to be adhered to in using this
    program, the following additional terms are to be complied with:

        1) All versions of this program, verbatim or modified must carry this
           Legal Notice.

        2) Any misrepresentation of the origin of the material is prohibited. It
           is required that all modified versions of this material be marked in
           reasonable ways as different from the original version.

        3) This license does not grant any rights to any user of the program
           with regards to rights under trademark law for use of the trade names
           or trademarks of eGovernments Foundation.

  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.works.web.controller.contractorbill;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.egov.commons.CChartOfAccounts;
import org.egov.commons.dao.ChartOfAccountsHibernateDAO;
import org.egov.infra.filestore.service.FileStoreService;
import org.egov.infra.reporting.engine.ReportConstants;
import org.egov.infra.reporting.engine.ReportOutput;
import org.egov.infra.reporting.engine.ReportRequest;
import org.egov.infra.reporting.engine.ReportService;
import org.egov.infra.web.utils.WebUtils;
import org.egov.infstr.utils.NumberUtil;
import org.egov.model.bills.EgBilldetails;
import org.egov.works.contractorbill.entity.ContractorBillRegister;
import org.egov.works.contractorbill.service.ContractorBillRegisterService;
import org.egov.works.letterofacceptance.service.LetterOfAcceptanceService;
import org.egov.works.lineestimate.entity.LineEstimateDetails;
import org.egov.works.lineestimate.service.LineEstimateService;
import org.egov.works.utils.WorksConstants;
import org.egov.works.utils.WorksUtils;
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
    private LineEstimateService lineEstimateService;

    @Autowired
    private LetterOfAcceptanceService letterOfAcceptanceService;
    
    @Autowired
    private ContractorBillRegisterService contractorBillRegisterService;
    
    @Autowired
    private ChartOfAccountsHibernateDAO chartOfAccountsHibernateDAO;

    public static final String CONTRACTORBILLPDF = "ContractorBillPDF";
    private final Map<String, Object> reportParams = new HashMap<String, Object>();
    private ReportRequest reportInput = null;
    private ReportOutput reportOutput = null;

    @Autowired
    @Qualifier("fileStoreService")
    protected FileStoreService fileStoreService;

    @RequestMapping(value = "/contractorbillPDF/{contractorBillId}", method = RequestMethod.GET)
    public @ResponseBody ResponseEntity<byte[]> generateContractorBillPDF(final HttpServletRequest request,
            @PathVariable("contractorBillId") final Long id,
            final HttpSession session) throws IOException {
        final ContractorBillRegister contractorBillRegister = contractorBillRegisterService.getContractorBillById(id);
        return generateReport(contractorBillRegister, request, session);
    }

    private ResponseEntity<byte[]> generateReport(final ContractorBillRegister contractorBillRegister, final HttpServletRequest request,
            final HttpSession session) {
        if (contractorBillRegister != null) {

            final SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            final DecimalFormat df = new DecimalFormat("#.##");
            final LineEstimateDetails lineEstimateDetails = lineEstimateService.findByEstimateNumber(contractorBillRegister.getWorkOrder()
                    .getEstimateNumber());

            final String url = WebUtils.extractRequestDomainURL(request, false);
            reportParams.put("cityLogo",url.concat(ReportConstants.IMAGE_CONTEXT_PATH).concat((String) request.getSession().getAttribute("citylogo")));

            final String cityName = (String) request.getSession().getAttribute("citymunicipalityname");
            reportParams.put("cityName", cityName);
            //reportParams.put("departmentName", contractorBillRegister.getWorkOrder());
            reportParams.put("contractorName", contractorBillRegister.getWorkOrder().getContractor().getName() != null ? contractorBillRegister.getWorkOrder().getContractor().getName(): "");
            reportParams.put("contractorCode", contractorBillRegister.getWorkOrder().getContractor().getCode() != null ? contractorBillRegister.getWorkOrder().getContractor().getCode(): "");
            reportParams.put("panNo", contractorBillRegister.getWorkOrder().getContractor().getPanNumber() != null ? contractorBillRegister.getWorkOrder().getContractor().getPanNumber(): "N/A");
            reportParams.put("bankAcc", contractorBillRegister.getWorkOrder().getContractor().getBank() != null ? contractorBillRegister.getWorkOrder().getContractor().getBankaccount(): "N/A");
            reportParams.put("billType", contractorBillRegister.getBilltype());
            reportParams.put("win", lineEstimateDetails.getProjectCode().getCode());
            reportParams.put("billNumber", contractorBillRegister.getBillnumber());
            reportParams.put("billDate", contractorBillRegister.getBilldate());
            reportParams.put("billAmount", contractorBillRegister.getBillamount());
            reportParams.put("nameOfTheWork", lineEstimateDetails.getLineEstimate().getNatureOfWork().getName());
            reportParams.put("ward", lineEstimateDetails.getLineEstimate().getWard().getName());
            
            BigDecimal totalAmount = BigDecimal.ZERO;
            reportParams.put("totalAmountWords", NumberUtil.amountInWords(totalAmount));
            
            final String preparedBy = worksUtils.getUserDesignation(contractorBillRegister.getCreatedBy());
            reportParams.put("preparedBy", preparedBy);

            reportInput = new ReportRequest(CONTRACTORBILLPDF, getAmountDetails(contractorBillRegister.getBillDetailes()), reportParams);

        }

        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/pdf"));
        headers.add("content-disposition", "inline;filename=ContractorBill.pdf");
        reportOutput = reportService.createReport(reportInput);
        return new ResponseEntity<byte[]>(reportOutput.getReportOutputData(), headers, HttpStatus.CREATED);

    }
    
    public List<Map<String, Object>> getAmountDetails(List<EgBilldetails> details) {
        List<Map<String, Object>> amountDetailsList = new ArrayList<Map<String, Object>>();
        Map<String, Object> amountDetails = new HashMap<String, Object>();
        for (final EgBilldetails egBilldetails : details) {
            egBilldetails.getGlcodeid();
            if(egBilldetails.getDebitamount() != null){
                amountDetails.put("debitAmount",egBilldetails.getDebitamount());
                CChartOfAccounts cChartOfAccounts = chartOfAccountsHibernateDAO.findById(egBilldetails.getGlcodeid(), true);
                amountDetails.put("glcode",cChartOfAccounts.getGlcode());
                amountDetails.put("glname",cChartOfAccounts.getName());
            }
            if(egBilldetails.getCreditamount() != null){
                
/*               final List<CChartOfAccounts> contractorPayableAccountList = chartOfAccountsHibernateDAO
                        .getAccountCodeByPurposeName(WorksConstants.CONTRACTOR_NETPAYABLE_PURPOSE);*/
            }
            amountDetailsList.add(amountDetails);
            }
        return amountDetailsList;
    }

}
