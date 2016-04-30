package org.egov.works.web.controller.reports;

import org.egov.infra.admin.master.service.DepartmentService;
import org.egov.infra.filestore.service.FileStoreService;
import org.egov.infra.reporting.engine.ReportOutput;
import org.egov.infra.reporting.engine.ReportRequest;
import org.egov.infra.reporting.engine.ReportService;
import org.egov.works.lineestimate.entity.enums.TypeOfSlum;
import org.egov.works.lineestimate.entity.enums.WorkCategory;
import org.egov.works.reports.entity.WorkProgressRegister;
import org.egov.works.reports.entity.WorkProgressRegisterPdf;
import org.egov.works.reports.entity.WorkProgressRegisterSearchRequest;
import org.egov.works.reports.service.WorkProgressRegisterService;
import org.egov.works.utils.WorksConstants;
import org.egov.works.utils.WorksUtils;
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
@RequestMapping(value = "/reports/workprogressregister")
public class WorkProgressRegisterPDFController {

    @Autowired
    private ReportService reportService;

    @Autowired
    private WorksUtils worksUtils;

    @Autowired
    private WorkProgressRegisterService workProgressRegisterService;

    @Autowired
    private DepartmentService departmentService;

    public static final String WORKPROGRESSREGISTERPDF = "workProgressRegisterPdf";
    private final Map<String, Object> reportParams = new HashMap<String, Object>();
    private ReportRequest reportInput = null;
    private ReportOutput reportOutput = null;

    @Autowired
    @Qualifier("fileStoreService")
    protected FileStoreService fileStoreService;

    @RequestMapping(value = "/pdf", method = RequestMethod.GET)
    public @ResponseBody ResponseEntity<byte[]> generateWorkProgressRegisterPDF(final HttpServletRequest request,
            @RequestParam("adminSanctionFromDate") final Date adminSanctionFromDate,
            @RequestParam("adminSanctionToDate") final Date adminSanctionToDate,
            @RequestParam("workIdentificationNumber") final String workIdentificationNumber,
            @RequestParam("contractor") final String contractor,
            @RequestParam("department") final Long department,
            @RequestParam("spillOverFlag") final boolean spillOverFlag,
            @RequestParam("contentType") final String contentType,
            final HttpSession session) throws IOException {
        final WorkProgressRegisterSearchRequest searchRequest = new WorkProgressRegisterSearchRequest();
        final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        searchRequest.setAdminSanctionFromDate(adminSanctionFromDate);
        searchRequest.setAdminSanctionToDate(adminSanctionToDate);
        searchRequest.setContractor(contractor);
        searchRequest.setWorkIdentificationNumber(workIdentificationNumber);
        searchRequest.setDepartment(department);
        searchRequest.setSpillOverFlag(spillOverFlag);
        final List<WorkProgressRegister> workProgressRegisters = workProgressRegisterService
                .searchWorkProgressRegister(searchRequest);

        String queryParameters = "Work Progress Register Report ";
        if (spillOverFlag)
            queryParameters = "Work Progress Register for Spill Over Line Estimates ";
        if (adminSanctionFromDate != null
                || adminSanctionToDate != null
                || workIdentificationNumber != null
                || contractor != null
                || department != null)
            queryParameters += "for ";

        if (adminSanctionFromDate != null && adminSanctionToDate != null)
            queryParameters += "Date Range : " + sdf.format(adminSanctionFromDate) + " - " + sdf.format(adminSanctionToDate)
                    + ", ";
        if (adminSanctionFromDate != null && adminSanctionToDate == null)
            queryParameters += "Admin Sanction From Date : " + adminSanctionFromDate + ", ";
        if (adminSanctionToDate != null && adminSanctionFromDate == null)
            queryParameters += "Admin Sanction To Date : " + adminSanctionToDate + ", ";
        if (workIdentificationNumber != null)
            queryParameters += "Work Identification Number : " + workIdentificationNumber + ", ";
        if (contractor != null)
            queryParameters += "Contractor : " + contractor + ", ";
        if (department != null)
            queryParameters += "Department : " + departmentService.getDepartmentById(department).getName() + ", ";

        if (queryParameters.endsWith(", "))
            queryParameters = queryParameters.substring(0, queryParameters.length() - 2);

        reportParams.put("queryParameters", queryParameters);

        return generateReport(workProgressRegisters, request, session, contentType);
    }

    private ResponseEntity<byte[]> generateReport(final List<WorkProgressRegister> workProgressRegisters,
            final HttpServletRequest request,
            final HttpSession session, final String contentType) {
        final List<WorkProgressRegisterPdf> workProgressRegisterPdfList = new ArrayList<WorkProgressRegisterPdf>();
        final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        final SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm a");

        String dataRunDate = "";

        if (workProgressRegisters != null && !workProgressRegisters.isEmpty())
            for (final WorkProgressRegister wpr : workProgressRegisters) {
                final WorkProgressRegisterPdf pdf = new WorkProgressRegisterPdf();
                if (wpr.getWard() != null)
                    pdf.setWard(wpr.getWard().getName());
                else
                    pdf.setWard("");
                if (wpr.getLocation() != null)
                    pdf.setLocation(wpr.getLocation().getName());
                else
                    pdf.setLocation("");
                if (wpr.getWorkCategory() != null
                        && wpr.getWorkCategory().toString().equals(WorkCategory.SLUM_WORK.toString())) {
                    if (wpr.getTypeOfSlum() != null
                            && wpr.getTypeOfSlum().toString().equals(TypeOfSlum.NOTIFIED.toString()))
                        pdf.setTypeOfSlum("Notified Slum");
                    else
                        pdf.setTypeOfSlum("Non Notified Slum");
                } else
                    pdf.setTypeOfSlum("Non slum work");
                if (wpr.getBeneficiary() != null)
                    pdf.setBeneficiary(wpr.getBeneficiary().toString());
                else
                    pdf.setBeneficiary("NA");
                if (wpr.getWinCode() != null)
                    pdf.setWinCode(wpr.getWinCode());
                else
                    pdf.setWinCode("");
                if (wpr.getFund() != null)
                    pdf.setFund(wpr.getFund().getCode() + " - " + wpr.getFund().getName());
                else
                    pdf.setFund("");
                if (wpr.getFunction() != null)
                    pdf.setFunction(wpr.getFunction().getCode() + " - " + wpr.getFunction().getName());
                else
                    pdf.setFunction("");
                if (wpr.getBudgetHead() != null)
                    pdf.setBudgetHead(wpr.getBudgetHead().getName());
                else
                    pdf.setBudgetHead("");
                if (wpr.getTypeOfWork() != null)
                    pdf.setTypeOfWork(wpr.getTypeOfWork().getCode());
                else
                    pdf.setTypeOfWork("");
                if (wpr.getSubTypeOfWork() != null)
                    pdf.setSubTypeOfWork(wpr.getSubTypeOfWork().getCode());
                else
                    pdf.setSubTypeOfWork("");
                if (wpr.getAdminSanctionBy() != null)
                    pdf.setAdminSanctionAuthorityDate(worksUtils.getUserDesignation(wpr.getAdminSanctionBy()) + " - "
                            + wpr.getAdminSanctionBy().getName() + ", "
                            + sdf.format(wpr.getAdminSanctionDate()));
                else
                    pdf.setAdminSanctionAuthorityDate("");
                if (wpr.getAdminSanctionAmount() != null)
                    pdf.setAdminSanctionAmount(wpr.getAdminSanctionAmount().setScale(2, BigDecimal.ROUND_HALF_EVEN).toString());
                else
                    pdf.setAdminSanctionAmount("NA");
                if (wpr.getTechnicalSanctionBy() != null)
                    pdf.setTechnicalSanctionAuthorityDate(worksUtils.getUserDesignation(wpr.getTechnicalSanctionBy()) + " - "
                            + wpr.getTechnicalSanctionBy().getName() + ", "
                            + sdf.format(wpr.getTechnicalSanctionDate()));
                else
                    pdf.setTechnicalSanctionAuthorityDate("");
                if (wpr.getEstimateAmount() != null)
                    pdf.setEstimateAmount(wpr.getEstimateAmount().setScale(2, BigDecimal.ROUND_HALF_EVEN).toString());
                else
                    pdf.setEstimateAmount("NA");
                if (wpr.getModeOfAllotment() != null)
                    pdf.setModeOfAllotment(wpr.getModeOfAllotment().toString());
                else
                    pdf.setModeOfAllotment("");
                if (wpr.getAgreementNumber() != null)
                    pdf.setAgreementNumberDate(wpr.getAgreementNumber() + " - " + sdf.format(wpr.getAgreementDate()));
                else
                    pdf.setAgreementNumberDate("");
                if (wpr.getContractor() != null)
                    pdf.setContractorCodeName(wpr.getContractor().getCode() + " - "
                            + wpr.getContractor().getName());
                else
                    pdf.setContractorCodeName("");
                if (wpr.getAgreementAmount() != null)
                    pdf.setAgreementAmount(wpr.getAgreementAmount().setScale(2, BigDecimal.ROUND_HALF_EVEN).toString());
                else
                    pdf.setAgreementAmount("NA");
                if (wpr.getLatestMbNumber() != null && wpr.getLatestMbDate() != null)
                    pdf.setLatestMbNumberDate(wpr.getLatestMbNumber() + " - " + sdf.format(wpr.getLatestMbDate()));
                else
                    pdf.setLatestMbNumberDate("");
                if (wpr.getLatestBillNumber() != null)
                    pdf.setLatestBillNumberDate(wpr.getLatestBillNumber() + " - " + sdf.format(wpr.getLatestBillDate()));
                else
                    pdf.setLatestBillNumberDate("");
                if (wpr.getBilltype() != null)
                    pdf.setBilltype(wpr.getBilltype());
                else
                    pdf.setBilltype("");
                if (wpr.getBillamount() != null)
                    pdf.setBillamount(wpr.getBillamount().setScale(2, BigDecimal.ROUND_HALF_EVEN).toString());
                else
                    pdf.setBillamount("NA");
                if (wpr.getTotalBillAmount() != null)
                    pdf.setTotalBillAmount(wpr.getTotalBillAmount().setScale(2, BigDecimal.ROUND_HALF_EVEN).toString());
                else
                    pdf.setTotalBillAmount("NA");
                if (wpr.getTotalBillPaidSoFar() != null)
                    pdf.setTotalBillPaidSoFar(wpr.getTotalBillPaidSoFar().setScale(2, BigDecimal.ROUND_HALF_EVEN).toString());
                else
                    pdf.setTotalBillPaidSoFar("NA");
                if (wpr.getBalanceValueOfWorkToBill() != null)
                    pdf.setBalanceValueOfWorkToBill(
                            wpr.getBalanceValueOfWorkToBill().setScale(2, BigDecimal.ROUND_HALF_EVEN).toString());
                else
                    pdf.setBalanceValueOfWorkToBill("NA");

                dataRunDate = formatter.format(wpr.getCreatedDate());

                workProgressRegisterPdfList.add(pdf);
            }

        reportParams.put("heading", WorksConstants.HEADING_WORK_PROGRESS_REGISTER_REPORT);
        reportParams.put("reportRunDate", formatter.format(new Date()));
        reportParams.put("dataRunDate", dataRunDate);

        reportInput = new ReportRequest(WORKPROGRESSREGISTERPDF, workProgressRegisterPdfList, reportParams);

        final HttpHeaders headers = new HttpHeaders();
        if (contentType.equalsIgnoreCase("pdf")) {
            headers.setContentType(MediaType.parseMediaType("application/pdf"));
            headers.add("content-disposition", "inline;filename=WorkProgressRegister.pdf");
        } else {
            headers.setContentType(MediaType.parseMediaType("application/vnd.ms-excel"));
            headers.add("content-disposition", "inline;filename=WorkProgressRegister.xls");
        }
        reportOutput = reportService.createReport(reportInput);
        return new ResponseEntity<byte[]>(reportOutput.getReportOutputData(), headers, HttpStatus.CREATED);

    }

}