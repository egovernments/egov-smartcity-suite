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
package org.egov.works.web.controller.reports;

import org.egov.commons.dao.FinancialYearHibernateDAO;
import org.egov.commons.dao.FunctionHibernateDAO;
import org.egov.commons.dao.FundHibernateDAO;
import org.egov.dao.budget.BudgetDetailsDAO;
import org.egov.dao.budget.BudgetGroupDAO;
import org.egov.infra.admin.master.service.DepartmentService;
import org.egov.infra.filestore.service.FileStoreService;
import org.egov.infra.reporting.engine.ReportFormat;
import org.egov.infra.reporting.engine.ReportOutput;
import org.egov.infra.reporting.engine.ReportRequest;
import org.egov.infra.reporting.engine.ReportService;
import org.egov.model.budget.BudgetGroup;
import org.egov.services.budget.BudgetGroupService;
import org.egov.works.models.estimate.BudgetFolioDetail;
import org.egov.works.reports.entity.EstimateAppropriationRegisterSearchRequest;
import org.egov.works.reports.service.EstimateAppropriationRegisterService;
import org.egov.works.utils.WorksConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/reports/estimateappropriationregister")
public class EstimateAppropriationRegisterPDFController {

    @Autowired
    private ReportService reportService;

    @Autowired
    private EstimateAppropriationRegisterService estimateAppropriationRegisterService;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private FundHibernateDAO fundHibernateDAO;

    @Autowired
    private FunctionHibernateDAO functionHibernateDAO;

    @Autowired
    private BudgetDetailsDAO budgetDetailsDAO;

    @Autowired
    private BudgetGroupDAO budgetGroupDAO;

    @Autowired
    private FinancialYearHibernateDAO financialYearHibernateDAO;

    @Autowired
    private BudgetGroupService budgetGroupService;

    public static final String BUDGETFOLIOPDF = "BudgetFolio";

    @Autowired
    @Qualifier("fileStoreService")
    protected FileStoreService fileStoreService;

    @RequestMapping(value = "/pdf", method = RequestMethod.GET)
    public @ResponseBody ResponseEntity<byte[]> generateAppropriationRegisterPDF(final HttpServletRequest request,
            @RequestParam("departments") final Long department, @RequestParam("financialYear") final Long financialYear,
            @RequestParam("asOnDate") final Date asOnDate, @RequestParam("fund") final Long fund,
            @RequestParam("functionName") final Long function, @RequestParam("budgetHead") final Long budgetHead,
            @RequestParam("contentType") final String contentType, final HttpSession session) throws IOException {
        final EstimateAppropriationRegisterSearchRequest searchRequest = new EstimateAppropriationRegisterSearchRequest();
        searchRequest.setAsOnDate(asOnDate);
        searchRequest.setBudgetHead(budgetHead);
        searchRequest.setDepartment(department);
        searchRequest.setFinancialYear(financialYear);
        searchRequest.setFunction(function);
        searchRequest.setFund(fund);
        final Map<String, Object> reportParams = new HashMap<String, Object>();

        final Map<String, Object> queryParamMap = new HashMap<String, Object>();
        BigDecimal totalGrant = BigDecimal.ZERO;
        BigDecimal totalGrantPerc = BigDecimal.ZERO;
        BigDecimal planningBudgetPerc = new BigDecimal(0);

        if (searchRequest != null && searchRequest.getFund() != null)
            queryParamMap.put("fundid", searchRequest.getFund().intValue());
        if (searchRequest != null && searchRequest.getFunction() != null)
            queryParamMap.put("functionid", searchRequest.getFunction());
        if (searchRequest != null && searchRequest.getBudgetHead() != null) {
            final List<BudgetGroup> budgetheadid = new ArrayList<BudgetGroup>();
            final BudgetGroup budgetGroup = budgetGroupService.findById(searchRequest.getBudgetHead(), true);
            budgetheadid.add(budgetGroup);
            queryParamMap.put("budgetheadid", budgetheadid);
        }
        if (searchRequest != null && searchRequest.getDepartment() != null)
            queryParamMap.put("deptid", searchRequest.getDepartment());
        if (searchRequest != null && searchRequest.getFinancialYear() != null)
            queryParamMap.put("financialyearid", searchRequest.getFinancialYear());
        if (searchRequest != null && searchRequest.getAsOnDate() != null)
            queryParamMap.put("fromDate",
                    financialYearHibernateDAO.getFinancialYearById(searchRequest.getFinancialYear()).getStartingDate());

        totalGrant = budgetDetailsDAO.getBudgetedAmtForYear(queryParamMap);
        queryParamMap.put("deptid", searchRequest.getDepartment().intValue());
        planningBudgetPerc = budgetDetailsDAO.getPlanningPercentForYear(queryParamMap);

        if (planningBudgetPerc != null && !planningBudgetPerc.equals(0)) {
            totalGrantPerc = totalGrant.multiply(planningBudgetPerc.divide(new BigDecimal(100)));
            queryParamMap.put("totalGrantPerc", totalGrantPerc);
        }
        reportParams.put("totalGrant", totalGrant);
        reportParams.put("planningBudgetPerc", planningBudgetPerc);
        reportParams.put("totalGrantPerc", totalGrantPerc);

        reportParams.put("department", departmentService.getDepartmentById(department).getName());
        reportParams.put("function", functionHibernateDAO.getFunctionById(function).getName());
        reportParams.put("functionCode", functionHibernateDAO.getFunctionById(function).getCode());
        reportParams.put("budgetHead", budgetGroupDAO.getBudgetHeadById(budgetHead).getName());
        reportParams.put("fund", fundHibernateDAO.fundById(fund.intValue(), true).getName());

        final Map<String, List> approvedBudgetFolioDetailsMap = estimateAppropriationRegisterService
                .searchEstimateAppropriationRegister(searchRequest);

        final List<BudgetFolioDetail> approvedBudgetFolioDetails = approvedBudgetFolioDetailsMap.get("budgetFolioList");

        final List calculatedValuesList = approvedBudgetFolioDetailsMap.get("calculatedValues");
        final Double latestCumulative = (Double) calculatedValuesList.get(0);
        final BigDecimal latestBalance = (BigDecimal) calculatedValuesList.get(1);

        for (final BudgetFolioDetail bfd : approvedBudgetFolioDetails) {
            bfd.setCumulativeExpensesIncurred(latestCumulative);
            bfd.setActualBalanceAvailable(latestBalance.doubleValue());
        }
        return generateReport(approvedBudgetFolioDetails, request, session, contentType, reportParams);

    }

    private ResponseEntity<byte[]> generateReport(final List<BudgetFolioDetail> budgetFolioDetails,
            final HttpServletRequest request, final HttpSession session, final String contentType,
            final Map<String, Object> reportParams) {

        final SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm a");
        ReportRequest reportInput = null;
        ReportOutput reportOutput = null;
        reportParams.put("heading", WorksConstants.HEADING_ESTIMATE_APPROPRIATION_REGISTER_REPORT);
        reportParams.put("reportRunDate", formatter.format(new Date()));

        reportInput = new ReportRequest(BUDGETFOLIOPDF, budgetFolioDetails, reportParams);

        reportParams.put("latestBalanceAvailable", budgetFolioDetails.get(0).getActualBalanceAvailable());
        reportParams.put("cumulativeExpensesIncurred", budgetFolioDetails.get(0).getCumulativeExpensesIncurred());

        final HttpHeaders headers = new HttpHeaders();
        if (contentType.equalsIgnoreCase("pdf")) {
            reportInput.setReportFormat(ReportFormat.PDF);
            headers.setContentType(MediaType.parseMediaType("application/pdf"));
            headers.add("content-disposition", "inline;filename=EstimateAppropriationRegister.pdf");
        } else {
            reportInput.setReportFormat(ReportFormat.XLS);
            headers.setContentType(MediaType.parseMediaType("application/vnd.ms-excel"));
            headers.add("content-disposition", "inline;filename=EstimateAppropriationRegister.xls");
        }
        reportOutput = reportService.createReport(reportInput);
        return new ResponseEntity<byte[]>(reportOutput.getReportOutputData(), headers, HttpStatus.CREATED);
    }
}
