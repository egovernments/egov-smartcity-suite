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
import org.egov.services.masters.SchemeService;
import org.egov.services.masters.SubSchemeService;
import org.egov.works.master.service.NatureOfWorkService;
import org.egov.works.reports.entity.EstimateAbstractReport;
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
@RequestMapping(value = "/reports/estimateabstractreportbydepartmentwise")
public class EstimateAbstractReportByDepartmentWisePDFController {

    @Autowired
    private ReportService reportService;

    @Autowired
    private WorksUtils worksUtils;

    @Autowired
    private WorkProgressRegisterService workProgressRegisterService;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    @Qualifier("schemeService")
    private SchemeService schemeService;

    @Autowired
    @Qualifier("subSchemeService")
    private SubSchemeService subSchemeService;

    @Autowired
    private NatureOfWorkService natureOfWorkService;

    public static final String ESTIMATEABSTRACTREPORTBYDEPARTMENTWISEPDF = "estimateAbstractReportByDepartmentWise";
    private final Map<String, Object> reportParams = new HashMap<String, Object>();
    private ReportRequest reportInput = null;
    private ReportOutput reportOutput = null;

    @Autowired
    @Qualifier("fileStoreService")
    protected FileStoreService fileStoreService;

    @RequestMapping(value = "/pdf", method = RequestMethod.GET)
    public @ResponseBody ResponseEntity<byte[]> generatePDF(final HttpServletRequest request,
            @RequestParam("adminSanctionFromDate") final Date adminSanctionFromDate,
            @RequestParam("adminSanctionToDate") final Date adminSanctionToDate,
            @RequestParam("department") final Long department,
            @RequestParam("scheme") final Integer scheme,
            @RequestParam("subScheme") final Integer subScheme,
            @RequestParam("workCategory") final String workCategory,
            @RequestParam("typeOfSlum") final String typeOfSlum,
            @RequestParam("beneficiary") final String beneficiary,
            @RequestParam("natureOfWork") final Long natureOfWork,
            @RequestParam("spillOverFlag") final boolean spillOverFlag,
            @RequestParam("contentType") final String contentType,
            final HttpSession session) throws IOException {
        final EstimateAbstractReport searchRequest = new EstimateAbstractReport();
        final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        searchRequest.setAdminSanctionFromDate(adminSanctionFromDate);
        searchRequest.setAdminSanctionToDate(adminSanctionToDate);
        searchRequest.setDepartment(department);
        searchRequest.setScheme(scheme);
        searchRequest.setSubScheme(subScheme);
        searchRequest.setWorkCategory(workCategory);
        searchRequest.setTypeOfSlum(typeOfSlum);
        searchRequest.setBeneficiary(beneficiary);
        searchRequest.setNatureOfWork(natureOfWork);
        searchRequest.setSpillOverFlag(spillOverFlag);

        final List<EstimateAbstractReport> estimateAbstractReports = workProgressRegisterService
                .searchEstimateAbstractReportByDepartmentWise(searchRequest);

        String queryParameters = "Estimate Abstract Report By Department Wise ";
        if (spillOverFlag)
            queryParameters = "Estimate Abstract Report By Department Wise for Spill Over Line Estimates ";
        if (adminSanctionFromDate != null
                || adminSanctionToDate != null
                || department != null)
            queryParameters += "for ";

        if (adminSanctionFromDate != null && adminSanctionToDate != null)
            queryParameters += "Date Range : " + sdf.format(adminSanctionFromDate) + " - " + sdf.format(adminSanctionToDate)
                    + ", ";
        if (adminSanctionFromDate != null && adminSanctionToDate == null)
            queryParameters += "Admin Sanction From Date : " + adminSanctionFromDate + ", ";
        if (adminSanctionToDate != null && adminSanctionFromDate == null)
            queryParameters += "Admin Sanction To Date : " + adminSanctionToDate + ", ";
        if (department != null)
            queryParameters += "Department : " + departmentService.getDepartmentById(department).getName() + ", ";

        if (scheme != null)
            queryParameters += "Scheme : " + schemeService.findById(scheme, false).getName() + ", ";

        if (subScheme != null)
            queryParameters += "SubScheme : " + subSchemeService.findById(subScheme, false).getName() + ", ";

        if (workCategory != null && !workCategory.equalsIgnoreCase("undefined")) {
            queryParameters += "WorkCategory : " + workCategory + ", ";
        }

        if (typeOfSlum != null) {
            queryParameters += "TypeOfSlum : " + typeOfSlum + ", ";
        }

        if (beneficiary != null) {
            queryParameters += "Beneficiary : " + beneficiary + ", ";
        }

        if (natureOfWork != null) {
            queryParameters += "NatureOfWork : " + natureOfWorkService.findById(natureOfWork).getName() + ", ";
        }

        if (queryParameters.endsWith(", "))
            queryParameters = queryParameters.substring(0, queryParameters.length() - 2);

        reportParams.put("queryParameters", queryParameters);

        return generateReport(estimateAbstractReports, request, session, contentType);
    }

    private ResponseEntity<byte[]> generateReport(final List<EstimateAbstractReport> estimateAbstractReports,
            final HttpServletRequest request,
            final HttpSession session, final String contentType) {
        final List<EstimateAbstractReport> estimateAbstractReportPdfList = new ArrayList<EstimateAbstractReport>();
        final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        final SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm a");

        String dataRunDate = "";

        if (estimateAbstractReports != null && !estimateAbstractReports.isEmpty())
            for (final EstimateAbstractReport eadwr : estimateAbstractReports) {
                final EstimateAbstractReport pdf = new EstimateAbstractReport();
                if (eadwr.getDepartmentName() != null)
                    pdf.setDepartmentName(eadwr.getDepartmentName());
                else
                    pdf.setDepartmentName("");

                if (eadwr.getLineEstimates() != null)
                    pdf.setLineEstimates(eadwr.getLineEstimates());
                else
                    pdf.setLineEstimates(null);

                if (eadwr.getAdminSanctionedEstimates() != null)
                    pdf.setAdminSanctionedEstimates(eadwr.getAdminSanctionedEstimates());
                else
                    pdf.setAdminSanctionedEstimates(null);

                if (eadwr.getAdminSanctionedAmountInCrores() != null)
                    pdf.setAdminSanctionedAmountInCrores(new BigDecimal(eadwr.getAdminSanctionedAmountInCrores())
                            .setScale(2, BigDecimal.ROUND_HALF_EVEN).toString());
                else
                    pdf.setAdminSanctionedAmountInCrores("NA");

                if (eadwr.getTechnicalSanctionedEstimates() != null)
                    pdf.setTechnicalSanctionedEstimates(eadwr.getTechnicalSanctionedEstimates());
                else
                    pdf.setTechnicalSanctionedEstimates(null);

                if (eadwr.getLoaCreated() != null)
                    pdf.setLoaCreated(eadwr.getLoaCreated());
                else
                    pdf.setLoaCreated(null);

                if (eadwr.getAgreementValueInCrores() != null)
                    pdf.setAgreementValueInCrores(new BigDecimal(eadwr.getAgreementValueInCrores())
                            .setScale(2, BigDecimal.ROUND_HALF_EVEN).toString());
                else
                    pdf.setAgreementValueInCrores("NA");

                if (eadwr.getWorkInProgress() != null)
                    pdf.setWorkInProgress(eadwr.getWorkInProgress());
                else
                    pdf.setWorkInProgress(null);

                if (eadwr.getWorkCompleted() != null)
                    pdf.setWorkCompleted(eadwr.getWorkCompleted());
                else
                    pdf.setWorkCompleted(null);

                if (eadwr.getBillsCreated() != null)
                    pdf.setBillsCreated(eadwr.getBillsCreated());
                else
                    pdf.setBillsCreated(null);

                if (eadwr.getBillValueInCrores() != null)
                    pdf.setBillValueInCrores(new BigDecimal(eadwr.getBillValueInCrores())
                            .setScale(2, BigDecimal.ROUND_HALF_EVEN).toString());
                else
                    pdf.setBillValueInCrores("NA");

                dataRunDate = formatter.format(workProgressRegisterService.getReportSchedulerRunDate());

                estimateAbstractReportPdfList.add(pdf);
            }

        reportParams.put("heading", WorksConstants.HEADING_ESTIMATE_ABSTRACT_REPORT_BY_DEPARTMENT_WISE);
        reportParams.put("reportRunDate", formatter.format(new Date()));
        reportParams.put("dataRunDate", dataRunDate);

        reportInput = new ReportRequest(ESTIMATEABSTRACTREPORTBYDEPARTMENTWISEPDF, estimateAbstractReportPdfList, reportParams);

        final HttpHeaders headers = new HttpHeaders();
        if (contentType.equalsIgnoreCase("pdf")) {
            headers.setContentType(MediaType.parseMediaType("application/pdf"));
            headers.add("content-disposition", "inline;filename=EstimateAbstractReportByDepartmentWise.pdf");
        } else {
            headers.setContentType(MediaType.parseMediaType("application/vnd.ms-excel"));
            headers.add("content-disposition", "inline;filename=EstimateAbstractReportByDepartmentWise.xls");
        }
        reportOutput = reportService.createReport(reportInput);
        return new ResponseEntity<byte[]>(reportOutput.getReportOutputData(), headers, HttpStatus.CREATED);

    }

}