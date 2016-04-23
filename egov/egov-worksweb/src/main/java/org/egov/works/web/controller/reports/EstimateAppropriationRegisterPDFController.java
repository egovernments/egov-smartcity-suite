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

import org.egov.infra.admin.master.service.DepartmentService;
import org.egov.infra.filestore.service.FileStoreService;
import org.egov.infra.reporting.engine.ReportOutput;
import org.egov.infra.reporting.engine.ReportRequest;
import org.egov.infra.reporting.engine.ReportService;
import org.egov.works.lineestimate.entity.LineEstimateAppropriation;
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

    public static final String ESTIMATEAPPROPRIATIONREGISTERPDF = "EstimateAppropriationRegisterPdf";
    private final Map<String, Object> reportParams = new HashMap<String, Object>();
    private ReportRequest reportInput = null;
    private ReportOutput reportOutput = null;

    @Autowired
    @Qualifier("fileStoreService")
    protected FileStoreService fileStoreService;

    @RequestMapping(value = "/pdf", method = RequestMethod.GET)
    public @ResponseBody ResponseEntity<byte[]> generateAppropriationRegisterPDF(final HttpServletRequest request,
            @RequestParam("asOnDate") final Date asOnDate,
            @RequestParam("budgetHead") final Long budgetHead,
            @RequestParam("department") final Long department,
            @RequestParam("financialYear") final Long financialYear,
            @RequestParam("function") final Long function,
            @RequestParam("fund") final Long fund,
            @RequestParam("contentType") final String contentType,
            final HttpSession session) throws IOException {
        final EstimateAppropriationRegisterSearchRequest searchRequest = new EstimateAppropriationRegisterSearchRequest();
        searchRequest.setAsOnDate(asOnDate);
        searchRequest.setBudgetHead(budgetHead);
        searchRequest.setDepartment(department);
        searchRequest.setFinancialYear(financialYear);
        searchRequest.setFunction(function);
        searchRequest.setFund(fund);

        String queryParameters = "Estimate Appropriation Register Report ";

        queryParameters += "for ";
        // queryParameters += "As On Date : " + asOnDate + ", ";
        queryParameters += "Budget Head : " + budgetHead + ", ";
        queryParameters += "Department : " + departmentService.getDepartmentById(department).getName() + ", ";
        // queryParameters += "Financial Year : " + financialYear + ", ";
        queryParameters += "Function : " + function + ", ";
        queryParameters += "Fund : " + fund;

        reportParams.put("queryParameters", queryParameters);
        
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
        return generateReport(new ArrayList<LineEstimateAppropriation>(), request, session, contentType);

    }

    private ResponseEntity<byte[]> generateReport(final List<LineEstimateAppropriation> lineEstimateAppropriations,
            final HttpServletRequest request, final HttpSession session, final String contentType) {

        final List<EstimateAppropriationRegisterPdf> estimateAppropriationRegisterPdfList = new ArrayList<EstimateAppropriationRegisterPdf>();
        final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        final SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm a");

        String dataRunDate = "";

        if (lineEstimateAppropriations != null && !lineEstimateAppropriations.isEmpty())
            for (final LineEstimateAppropriation lea : lineEstimateAppropriations) {
                final EstimateAppropriationRegisterPdf pdf = new EstimateAppropriationRegisterPdf();
                if (lea.getBudgetUsage().getAppropriationnumber() != null)
                    pdf.setAppropriationNumber(lea.getBudgetUsage().getAppropriationnumber());
                else
                    pdf.setAppropriationNumber("");
                if (lea.getCreatedDate() != null)
                    pdf.setAppropriationDate(lea.getCreatedDate() + " - " + sdf.format(lea.getCreatedDate()));
                else
                    pdf.setAppropriationDate("");
                if (lea.getLineEstimateDetails().getEstimateNumber() != null)
                    pdf.setEstimateNumber(lea.getLineEstimateDetails().getEstimateNumber());
                else
                    pdf.setEstimateNumber("");
                if (lea.getLineEstimateDetails().getProjectCode().getCode() != null)
                    pdf.setWinCode(lea.getLineEstimateDetails().getProjectCode().getCode());
                else
                    pdf.setWinCode("");
                if (lea.getLineEstimateDetails().getNameOfWork() != null)
                    pdf.setNameOfTheWork(lea.getLineEstimateDetails().getNameOfWork());
                else
                    pdf.setNameOfTheWork("");
                if (lea.getLineEstimateDetails().getCreatedDate() != null)
                    pdf.setEstimateDate(lea.getLineEstimateDetails().getCreatedDate() + " - "
                            + sdf.format(lea.getLineEstimateDetails().getCreatedDate()));
                else
                    pdf.setEstimateDate("");
                if (lea.getLineEstimateDetails().getEstimateAmount() != null)
                    pdf.setEstimateValue(
                            lea.getLineEstimateDetails().getEstimateAmount().setScale(2, BigDecimal.ROUND_HALF_EVEN).toString());
                else
                    pdf.setEstimateValue("");
                // for appripraite value checking realease and consume amount
                if (lea.getBudgetUsage().getConsumedAmount() != null && lea.getBudgetUsage().getConsumedAmount() > 0)
                    pdf.setAppropriatedValue(lea.getBudgetUsage().getConsumedAmount().toString());
                else if (lea.getBudgetUsage().getReleasedAmount() != null && lea.getBudgetUsage().getReleasedAmount() < 0)
                    pdf.setAppropriatedValue(lea.getBudgetUsage().getConsumedAmount().toString());
                else
                    pdf.setAppropriatedValue("");
                // for cummulative total and balence available
                if (lea.getLineEstimateDetails().getEstimateAmount() != null)
                    pdf.setEstimateValue(
                            lea.getLineEstimateDetails().getEstimateAmount().setScale(2, BigDecimal.ROUND_HALF_EVEN).toString());
                else
                    pdf.setEstimateValue("");

                if (lea.getLineEstimateDetails().getEstimateAmount() != null)
                    pdf.setEstimateValue(
                            lea.getLineEstimateDetails().getEstimateAmount().setScale(2, BigDecimal.ROUND_HALF_EVEN).toString());
                else
                    pdf.setEstimateValue("");

                dataRunDate = formatter.format(lea.getCreatedDate());

                estimateAppropriationRegisterPdfList.add(pdf);
            }
        reportParams.put("heading", WorksConstants.HEADING_ESTIMATE_APPROPRIATION_REGISTER_REPORT);
        reportParams.put("reportRunDate", formatter.format(new Date()));
        reportParams.put("dataRunDate", dataRunDate);

        reportInput = new ReportRequest(ESTIMATEAPPROPRIATIONREGISTERPDF, estimateAppropriationRegisterPdfList, reportParams);

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
