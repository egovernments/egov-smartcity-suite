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

import org.egov.infra.admin.master.service.DepartmentService;
import org.egov.infra.filestore.service.FileStoreService;
import org.egov.infra.reporting.engine.ReportFormat;
import org.egov.infra.reporting.engine.ReportOutput;
import org.egov.infra.reporting.engine.ReportRequest;
import org.egov.infra.reporting.engine.ReportService;
import org.egov.infra.utils.DateUtils;
import org.egov.works.contractorbill.entity.enums.BillTypes;
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

    @Autowired
    @Qualifier("fileStoreService")
    protected FileStoreService fileStoreService;

    @RequestMapping(value = "/pdf", method = RequestMethod.GET)
    public @ResponseBody ResponseEntity<byte[]> generateWorkProgressRegisterPDF(final HttpServletRequest request,
            @RequestParam("adminSanctionFromDate") final Date adminSanctionFromDate,
            @RequestParam("adminSanctionToDate") final Date adminSanctionToDate,
            @RequestParam("workIdentificationNumber") final String workIdentificationNumber,
            @RequestParam("contractor") final String contractor, @RequestParam("department") final Long department,
            @RequestParam("spillOverFlag") final boolean spillOverFlag,
            @RequestParam("contentType") final String contentType, final HttpSession session) throws IOException {
        final Map<String, Object> reportParams = new HashMap<String, Object>();
        final WorkProgressRegisterSearchRequest searchRequest = new WorkProgressRegisterSearchRequest();
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
        if (adminSanctionFromDate != null || adminSanctionToDate != null || workIdentificationNumber != null
                || contractor != null || department != null)
            queryParameters += "for ";

        if (adminSanctionFromDate != null && adminSanctionToDate != null)
            queryParameters += "Date Range : " + DateUtils.getFormattedDate(adminSanctionFromDate, "dd/MM/yyyy") + " - "
                    + DateUtils.getFormattedDate(adminSanctionToDate, "dd/MM/yyyy") + ", ";
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

        return generateReport(workProgressRegisters, request, session, contentType, reportParams);
    }

    private ResponseEntity<byte[]> generateReport(final List<WorkProgressRegister> workProgressRegisters,
            final HttpServletRequest request, final HttpSession session, final String contentType,
            final Map<String, Object> reportParams) {
        final List<WorkProgressRegisterPdf> workProgressRegisterPdfList = new ArrayList<WorkProgressRegisterPdf>();

        String dataRunDate = "";
        ReportRequest reportInput = null;
        ReportOutput reportOutput = null;

        if (workProgressRegisters != null && !workProgressRegisters.isEmpty())
            for (final WorkProgressRegister wpr : workProgressRegisters) {
                final WorkProgressRegisterPdf pdf = new WorkProgressRegisterPdf();
                if (wpr.getWard() != null) {
                    if (wpr.getWard().getBoundaryType().getName().equalsIgnoreCase(WorksConstants.BOUNDARY_TYPE_CITY))
                        pdf.setWard(wpr.getWard().getName());
                    else
                        pdf.setWard(wpr.getWard().getBoundaryNum().toString());
                } else
                    pdf.setWard("");
                if (wpr.getLocation() != null)
                    pdf.setLocation(wpr.getLocation().getName());
                else
                    pdf.setLocation("");
                if (wpr.getWorkCategory() != null)
                    pdf.setWorkCategory(wpr.getWorkCategory().toString().replace("_", " "));
                else
                    pdf.setWorkCategory("NA");
                if (wpr.getBeneficiary() != null)
                    pdf.setBeneficiary(wpr.getBeneficiary().toString().replaceAll("_C", "/C").replace("_", " "));
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
                    pdf.setTypeOfWork(wpr.getTypeOfWork().getDescription());
                else
                    pdf.setTypeOfWork("");
                if (wpr.getSubTypeOfWork() != null)
                    pdf.setSubTypeOfWork(wpr.getSubTypeOfWork().getDescription());
                else
                    pdf.setSubTypeOfWork("");
                if (wpr.getAdminSanctionBy() != null)
                    pdf.setAdminSanctionAuthorityDate(wpr.getAdminSanctionBy() + " , "
                            + DateUtils.getFormattedDate(wpr.getAdminSanctionDate(), "dd/MM/yyyy"));
                else
                    pdf.setAdminSanctionAuthorityDate("");
                if (wpr.getAdminSanctionAmount() != null)
                    pdf.setAdminSanctionAmount(
                            wpr.getAdminSanctionAmount().setScale(2, BigDecimal.ROUND_HALF_EVEN).toString());
                else
                    pdf.setAdminSanctionAmount("NA");
                if (wpr.getTechnicalSanctionBy() != null)
                    pdf.setTechnicalSanctionAuthorityDate(worksUtils.getUserDesignation(wpr.getTechnicalSanctionBy())
                            + " - " + wpr.getTechnicalSanctionBy().getName() + ", "
                            + DateUtils.getFormattedDate(wpr.getTechnicalSanctionDate(), "dd/MM/yyyy"));
                else
                    pdf.setTechnicalSanctionAuthorityDate("NA");
                if (wpr.getEstimateAmount() != null)
                    pdf.setEstimateAmount(wpr.getEstimateAmount().setScale(2, BigDecimal.ROUND_HALF_EVEN).toString());
                else
                    pdf.setEstimateAmount("NA");
                if (wpr.getModeOfAllotment() != null)
                    pdf.setModeOfAllotment(wpr.getModeOfAllotment());
                else
                    pdf.setModeOfAllotment("");
                if (wpr.getAgreementNumber() != null)
                    pdf.setAgreementNumberDate(wpr.getAgreementNumber() + " - "
                            + DateUtils.getFormattedDate(wpr.getAgreementDate(), "dd/MM/yyyy"));
                else
                    pdf.setAgreementNumberDate("");
                if (wpr.getContractor() != null)
                    pdf.setContractorCodeName(wpr.getContractor().getCode() + " - " + wpr.getContractor().getName());
                else
                    pdf.setContractorCodeName("");
                if (wpr.getAgreementAmount() != null)
                    pdf.setAgreementAmount(wpr.getAgreementAmount().setScale(2, BigDecimal.ROUND_HALF_EVEN).toString());
                else
                    pdf.setAgreementAmount("NA");
                if (wpr.getLatestMbNumber() != null && wpr.getLatestMbDate() != null)
                    pdf.setLatestMbNumberDate(wpr.getLatestMbNumber() + " - "
                            + DateUtils.getFormattedDate(wpr.getLatestMbDate(), "dd/MM/yyyy"));
                else
                    pdf.setLatestMbNumberDate("");
                if (wpr.getLatestBillNumber() != null)
                    pdf.setLatestBillNumberDate(wpr.getLatestBillNumber() + " - "
                            + DateUtils.getFormattedDate(wpr.getLatestBillDate(), "dd/MM/yyyy"));
                else
                    pdf.setLatestBillNumberDate("");
                if (wpr.getBilltype() != null)
                    pdf.setBilltype(wpr.getBilltype());
                else
                    pdf.setBilltype("NA");
                if (wpr.getBillamount() != null)
                    pdf.setBillamount(wpr.getBillamount().setScale(2, BigDecimal.ROUND_HALF_EVEN).toString());
                else
                    pdf.setBillamount("NA");
                if (wpr.getTotalBillAmount() != null)
                    pdf.setTotalBillAmount(wpr.getTotalBillAmount().setScale(2, BigDecimal.ROUND_HALF_EVEN).toString());
                else
                    pdf.setTotalBillAmount("NA");
                if (wpr.getMilestonePercentageCompleted() != null)
                    pdf.setMilestonePercentageCompleted(wpr.getMilestonePercentageCompleted().toString());
                else
                    pdf.setMilestonePercentageCompleted("NA");
                if (wpr.getTotalBillPaidSoFar() != null)
                    pdf.setTotalBillPaidSoFar(
                            wpr.getTotalBillPaidSoFar().setScale(2, BigDecimal.ROUND_HALF_EVEN).toString());
                else
                    pdf.setTotalBillPaidSoFar("NA");
                if (wpr.getBalanceValueOfWorkToBill() != null) {
                    if (wpr.getBilltype() != null
                            && wpr.getBilltype().equalsIgnoreCase(BillTypes.Final_Bill.toString()))
                        pdf.setBalanceValueOfWorkToBill("NA");
                    else
                        pdf.setBalanceValueOfWorkToBill(
                                wpr.getBalanceValueOfWorkToBill().setScale(2, BigDecimal.ROUND_HALF_EVEN).toString());
                } else
                    pdf.setBalanceValueOfWorkToBill("NA");

                dataRunDate = DateUtils.getFormattedDate(wpr.getCreatedDate(), "dd/MM/yyyy hh:mm a");

                workProgressRegisterPdfList.add(pdf);
            }

        reportParams.put("heading", WorksConstants.HEADING_WORK_PROGRESS_REGISTER_REPORT);
        reportParams.put("reportRunDate", DateUtils.getFormattedDate(new Date(), "dd/MM/yyyy hh:mm a"));
        reportParams.put("dataRunDate", dataRunDate);

        reportInput = new ReportRequest(WORKPROGRESSREGISTERPDF, workProgressRegisterPdfList, reportParams);

        final HttpHeaders headers = new HttpHeaders();
        if (contentType.equalsIgnoreCase("pdf")) {
            reportInput.setReportFormat(ReportFormat.PDF);
            headers.setContentType(MediaType.parseMediaType("application/pdf"));
            headers.add("content-disposition", "inline;filename=WorkProgressRegister.pdf");
        } else {
            reportInput.setReportFormat(ReportFormat.XLS);
            headers.setContentType(MediaType.parseMediaType("application/vnd.ms-excel"));
            headers.add("content-disposition", "inline;filename=WorkProgressRegister.xls");
        }
        reportOutput = reportService.createReport(reportInput);
        return new ResponseEntity<byte[]>(reportOutput.getReportOutputData(), headers, HttpStatus.CREATED);

    }

}
