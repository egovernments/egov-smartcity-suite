package org.egov.works.web.controller.reports;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.egov.commons.dao.FinancialYearHibernateDAO;
import org.egov.commons.dao.FunctionHibernateDAO;
import org.egov.commons.dao.FundHibernateDAO;
import org.egov.dao.budget.BudgetDetailsDAO;
import org.egov.dao.budget.BudgetGroupDAO;
import org.egov.infra.admin.master.service.DepartmentService;
import org.egov.infra.filestore.service.FileStoreService;
import org.egov.infra.reporting.engine.ReportOutput;
import org.egov.infra.reporting.engine.ReportRequest;
import org.egov.infra.reporting.engine.ReportService;
import org.egov.works.models.estimate.BudgetFolioDetail;
import org.egov.works.reports.entity.EstimateAppropriationRegisterPdf;
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

    public static final String BUDGETFOLIOPDF = "BudgetFolio";
    private final Map<String, Object> reportParams = new HashMap<String, Object>();
    private ReportRequest reportInput = null;
    private ReportOutput reportOutput = null;

    @Autowired
    @Qualifier("fileStoreService")
    protected FileStoreService fileStoreService;

    @RequestMapping(value = "/pdf", method = RequestMethod.GET)
    public @ResponseBody ResponseEntity<byte[]> generateAppropriationRegisterPDF(final HttpServletRequest request,
            @RequestParam("departments") final Long department,
            @RequestParam("financialYear") final Long financialYear,
            @RequestParam("asOnDate") final Date asOnDate,
            @RequestParam("fund") final Long fund,
            @RequestParam("functionPDF") final Long function,
            @RequestParam("budgetHead") final Long budgetHead,
            @RequestParam("contentType") final String contentType,
            final HttpSession session) throws IOException {
        final EstimateAppropriationRegisterSearchRequest searchRequest = new EstimateAppropriationRegisterSearchRequest();
        searchRequest.setAsOnDate(asOnDate);
        searchRequest.setBudgetHead(budgetHead);
        searchRequest.setDepartment(department);
        searchRequest.setFinancialYear(financialYear);
        searchRequest.setFunction(function);
        searchRequest.setFund(fund);
        
        reportParams.put("department", departmentService.getDepartmentById(department).getName());
        reportParams.put("function", functionHibernateDAO.getFunctionById(function).getName());
        reportParams.put("budgetHead", budgetGroupDAO.getBudgetHeadById(budgetHead).getName());
        reportParams.put("fund", fundHibernateDAO.fundById(fund.intValue(),true).getName());
        
        final Map<String, List> approvedBudgetFolioDetailsMap = estimateAppropriationRegisterService
                .searchEstimateAppropriationRegister(searchRequest);
        
        List<BudgetFolioDetail> approvedBudgetFolioDetails = approvedBudgetFolioDetailsMap.get("budgetFolioList");

        List calculatedValuesList = approvedBudgetFolioDetailsMap.get("calculatedValues");
        Double latestCumulative = (Double) calculatedValuesList.get(0);
        BigDecimal latestBalance = (BigDecimal) calculatedValuesList.get(1);
        
        for(BudgetFolioDetail bfd : approvedBudgetFolioDetails) {
            bfd.setCumulativeExpensesIncurred(latestCumulative);
            bfd.setActualBalanceAvailable(latestBalance.doubleValue());
        }
        return generateReport(approvedBudgetFolioDetails, request, session, contentType);

    }

    private ResponseEntity<byte[]> generateReport(final List<BudgetFolioDetail> budgetFolioDetails,
            final HttpServletRequest request, final HttpSession session, final String contentType) {

        final SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm a");
     
        reportParams.put("heading", WorksConstants.HEADING_ESTIMATE_APPROPRIATION_REGISTER_REPORT);
        reportParams.put("reportRunDate", formatter.format(new Date()));

        reportInput = new ReportRequest(BUDGETFOLIOPDF, budgetFolioDetails, reportParams);
        reportParams.put("latestBalanceAvailable", budgetFolioDetails.get(budgetFolioDetails.size()-1).getBalanceAvailable());
        final HttpHeaders headers = new HttpHeaders();
        if (contentType.equalsIgnoreCase("pdf")) {
            headers.setContentType(MediaType.parseMediaType("application/pdf"));
            headers.add("content-disposition", "inline;filename=EstimateAppropriationRegister.pdf");
        } else {
            headers.setContentType(MediaType.parseMediaType("application/vnd.ms-excel"));
            headers.add("content-disposition", "inline;filename=EstimateAppropriationRegister.xls");
        }
        reportOutput = reportService.createReport(reportInput);
        return new ResponseEntity<byte[]>(reportOutput.getReportOutputData(), headers, HttpStatus.CREATED);
    }
}
