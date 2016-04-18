package org.egov.works.web.controller.reports;

import java.io.IOException;
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
import org.egov.infra.reporting.engine.ReportConstants;
import org.egov.infra.reporting.engine.ReportOutput;
import org.egov.infra.reporting.engine.ReportRequest;
import org.egov.infra.reporting.engine.ReportService;
import org.egov.infra.web.utils.WebUtils;
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
    public @ResponseBody ResponseEntity<byte[]> generateLineEstimatePDF(final HttpServletRequest request,
            @RequestParam("adminSanctionFromDate") final Date adminSanctionFromDate,
            @RequestParam("adminSanctionToDate") final Date adminSanctionToDate,
            @RequestParam("workIdentificationNumber") final String workIdentificationNumber,
            @RequestParam("contractor") final String contractor,
            @RequestParam("department") final Long department,
            @RequestParam("spillOverFlag") final boolean spillOverFlag,
            @RequestParam("contentType") final String contentType,
            final HttpSession session) throws IOException {
        final WorkProgressRegisterSearchRequest searchRequest = new WorkProgressRegisterSearchRequest();
        searchRequest.setAdminSanctionFromDate(adminSanctionFromDate);
        searchRequest.setAdminSanctionToDate(adminSanctionToDate);
        searchRequest.setContractor(contractor);
        searchRequest.setWorkIdentificationNumber(workIdentificationNumber);
        searchRequest.setDepartment(department);
        searchRequest.setSpillOverFlag(spillOverFlag);
        final List<WorkProgressRegister> workProgressRegisters = workProgressRegisterService
                .searchWorkProgressRegister(searchRequest);
        
        String queryParameters = "";
        if(adminSanctionFromDate != null) {
            queryParameters += "Admin Sanction From Date : " + adminSanctionFromDate + ", ";
        }
        if(adminSanctionToDate != null) {
            queryParameters += "Admin Sanction To Date : " + adminSanctionToDate + ", ";
        }
        if(workIdentificationNumber != null) {
            queryParameters += "Work Identification Number : " + workIdentificationNumber + ", ";
        }
        if(contractor != null) {
            queryParameters += "Contractor : " + contractor + ", ";
        }
        if(department != null) {
            queryParameters += "Department : " + departmentService.getDepartmentById(department).getName() + ", ";
        }
        queryParameters += "Spill Over Flag : " + spillOverFlag + ", ";
        
        if (queryParameters.endsWith(", "))
            queryParameters = queryParameters.substring(0, queryParameters.length() - 2);
        
        return generateReport(workProgressRegisters, request, session, contentType, queryParameters);
    }

    private ResponseEntity<byte[]> generateReport(final List<WorkProgressRegister> workProgressRegisters,
            final HttpServletRequest request,
            final HttpSession session, final String contentType, final String parameters) {
        final List<WorkProgressRegisterPdf> workProgressRegisterPdfList = new ArrayList<WorkProgressRegisterPdf>();
        final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        final SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm");
        
        String dataRunDate = "";
        
        if (workProgressRegisters != null && !workProgressRegisters.isEmpty()) {
            final WorkProgressRegisterPdf pdf = new WorkProgressRegisterPdf();

            for (final WorkProgressRegister wpr : workProgressRegisters) {
                if (wpr.getWard() != null)
                    pdf.setWard(wpr.getWard().getName());
                if (wpr.getLocation() != null)
                    pdf.setLocation(wpr.getLocation().getName());
                if (wpr.getWorkCategory() != null
                        && wpr.getWorkCategory().toString().equals(WorkCategory.SLUM_WORK.toString())) {
                    if (wpr.getTypeOfSlum() != null
                            && wpr.getTypeOfSlum().toString().equals(TypeOfSlum.NOTIFIED.toString()))
                        pdf.setTypeOfSlum("Notified Slum");
                    else
                        pdf.setTypeOfSlum("Non Notified Slum");
                }
                else
                    pdf.setTypeOfSlum("Non slum work");
                if (wpr.getBeneficiary() != null)
                    pdf.setBeneficiary(wpr.getBeneficiary().toString());
                else
                    pdf.setBeneficiary("NA");
                if (wpr.getWinCode() != null)
                    pdf.setWinCode(wpr.getWinCode());
                if (wpr.getFund() != null)
                    pdf.setFund(wpr.getFund().getName() + " - " + wpr.getFund().getCode());
                if (wpr.getFunction() != null)
                    pdf.setFunction(wpr.getFunction().getName() + " - " + wpr.getFunction().getCode());
                if (wpr.getBudgetHead() != null)
                    pdf.setBudgetHead(wpr.getBudgetHead().getName() + " - " + wpr.getBudgetHead().getDescription());
                if (wpr.getTypeOfWork() != null)
                    pdf.setTypeOfWork(wpr.getTypeOfWork().getCode());
                if (wpr.getSubTypeOfWork() != null)
                    pdf.setSubTypeOfWork(wpr.getSubTypeOfWork().getCode());
                if (wpr.getAdminSanctionBy() != null)
                    pdf.setAdminSanctionAuthorityDate(worksUtils.getUserDesignation(wpr.getAdminSanctionBy()) + " - "
                            + wpr.getAdminSanctionBy().getName() + ", "
                            + sdf.format(wpr.getAdminSanctionDate()));
                if (wpr.getAdminSanctionAmount() != null)
                    pdf.setAdminSanctionAmount(wpr.getAdminSanctionAmount().toString());
                if (wpr.getTechnicalSanctionBy() != null)
                    pdf.setTechnicalSanctionAuthorityDate(worksUtils.getUserDesignation(wpr.getTechnicalSanctionBy()) + " - "
                            + wpr.getTechnicalSanctionBy().getName() + ", "
                            + sdf.format(wpr.getTechnicalSanctionDate()));
                if (wpr.getEstimateAmount() != null)
                    pdf.setEstimateAmount(wpr.getEstimateAmount().toString());
                if (wpr.getModeOfAllotment() != null)
                    pdf.setModeOfAllotment(wpr.getModeOfAllotment().toString());
                if (wpr.getAgreementNumber() != null)
                    pdf.setAgreementNumberDate(wpr.getAgreementNumber() + " - " + sdf.format(wpr.getAgreementDate()));
                if (wpr.getContractor() != null)
                    pdf.setContractorCodeName(wpr.getContractor().getCode() + " - "
                            + wpr.getContractor().getName());
                if (wpr.getAgreementAmount() != null)
                    pdf.setAgreementAmount(wpr.getAgreementAmount().toString());
                // if (wpr.getLatestMbNumber() != null)
                // pdf.setLatestMbNumberDate(wpr.getLatestMbNumber() + ", " + sdf.format(wpr.getLatestMbDate()));
                if (wpr.getLatestBillNumber() != null)
                    pdf.setLatestBillNumberDate(wpr.getLatestBillNumber() + " - " + sdf.format(wpr.getLatestBillDate()));
                if (wpr.getBilltype() != null)
                    pdf.setBilltype(wpr.getBilltype());
                if (wpr.getBillamount() != null)
                    pdf.setBillamount(wpr.getBillamount().toString());
                if (wpr.getTotalBillPaidSoFar() != null)
                    pdf.setTotalBillPaidSoFar(wpr.getTotalBillPaidSoFar().toString());
                if (wpr.getBalanceValueOfWorkToBill() != null)
                    pdf.setBalanceValueOfWorkToBill(wpr.getBalanceValueOfWorkToBill().toString());
                
                dataRunDate = formatter.format(wpr.getCreatedDate());
                
                workProgressRegisterPdfList.add(pdf);
            }

            reportParams.put("heading", WorksConstants.HEADING_WORK_PROGRESS_REGISTER_REPORT);
            reportParams.put("reportRunDate", formatter.format(new Date()));
            
            String queryParameters = parameters + " as on Date : " + dataRunDate;
            
            reportParams.put("queryParameters", queryParameters);

            reportInput = new ReportRequest(WORKPROGRESSREGISTERPDF, workProgressRegisterPdfList, reportParams);

        }

        final HttpHeaders headers = new HttpHeaders();
        if(contentType.equalsIgnoreCase("pdf")) {
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