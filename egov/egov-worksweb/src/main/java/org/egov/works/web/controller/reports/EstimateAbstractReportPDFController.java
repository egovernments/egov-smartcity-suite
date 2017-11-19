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

import org.egov.commons.dao.EgwTypeOfWorkHibernateDAO;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.admin.master.service.DepartmentService;
import org.egov.infra.reporting.engine.ReportFormat;
import org.egov.infra.reporting.engine.ReportOutput;
import org.egov.infra.reporting.engine.ReportRequest;
import org.egov.infra.reporting.engine.ReportService;
import org.egov.services.masters.SchemeService;
import org.egov.services.masters.SubSchemeService;
import org.egov.works.master.service.NatureOfWorkService;
import org.egov.works.reports.entity.EstimateAbstractReport;
import org.egov.works.reports.service.WorkProgressRegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
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
import java.util.Set;

@Controller
@RequestMapping(value = "/reports/estimateabstractreport")
public class EstimateAbstractReportPDFController {

    @Autowired
    private ReportService reportService;

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

    @Autowired
    private EgwTypeOfWorkHibernateDAO egwTypeOfWorkHibernateDAO;

    @Autowired
    private MessageSource messageSource;

    @RequestMapping(value = "/departmentwise/pdf", method = RequestMethod.GET)
    public @ResponseBody ResponseEntity<byte[]> generatePDFDepartmentWise(final HttpServletRequest request,
            @RequestParam("adminSanctionFromDate") final Date adminSanctionFromDate,
            @RequestParam("adminSanctionToDate") final Date adminSanctionToDate,
            @RequestParam("department") final Long department, @RequestParam("scheme") final Integer scheme,
            @RequestParam("subScheme") final Integer subScheme, @RequestParam("workCategory") final String workCategory,
            @RequestParam("beneficiary") final String beneficiary,
            @RequestParam("natureOfWork") final Long natureOfWork,
            @RequestParam("spillOverFlag") final boolean spillOverFlag,
            @RequestParam("contentType") final String contentType, final HttpSession session) throws IOException {
        final EstimateAbstractReport searchRequest = new EstimateAbstractReport();
        final Map<String, Object> reportParams = new HashMap<String, Object>();
        final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        searchRequest.setAdminSanctionFromDate(adminSanctionFromDate);
        searchRequest.setAdminSanctionToDate(adminSanctionToDate);
        searchRequest.setDepartment(department);
        searchRequest.setScheme(scheme);
        searchRequest.setSubScheme(subScheme);
        searchRequest.setWorkCategory(workCategory);
        searchRequest.setBeneficiary(beneficiary);
        searchRequest.setNatureOfWork(natureOfWork);
        searchRequest.setSpillOverFlag(spillOverFlag);

        final List<EstimateAbstractReport> estimateAbstractReports = workProgressRegisterService
                .searchEstimateAbstractReportByDepartmentWise(searchRequest);

        String queryParameters = messageSource.getMessage("msg.estimateabstractreport.by.departmentwise", null, null);
        if (spillOverFlag)
            queryParameters = messageSource.getMessage("msg.estimateabstractreport.by.departmentwise.for.spillover",
                    null, null);
        if (adminSanctionFromDate != null || adminSanctionToDate != null || department != null)
            queryParameters += "for ";

        if (adminSanctionFromDate != null && adminSanctionToDate != null)
            queryParameters += messageSource.getMessage("msg.daterange", null, null) + sdf.format(adminSanctionFromDate)
                    + " - " + sdf.format(adminSanctionToDate) + ", ";
        if (adminSanctionFromDate != null && adminSanctionToDate == null)
            queryParameters += messageSource.getMessage("msg.adminsanctionfromdate", null, null) + adminSanctionFromDate
                    + ", ";
        if (adminSanctionToDate != null && adminSanctionFromDate == null)
            queryParameters += messageSource.getMessage("msg.adminsanctiontodate", null, null) + adminSanctionToDate
                    + ", ";
        if (department != null)
            queryParameters += messageSource.getMessage("msg.department", null, null)
                    + departmentService.getDepartmentById(department).getName() + ", ";

        if (scheme != null)
            queryParameters += messageSource.getMessage("msg.scheme", null, null)
                    + schemeService.findById(scheme, false).getName() + ", ";

        if (subScheme != null)
            queryParameters += messageSource.getMessage("msg.subscheme", null, null)
                    + subSchemeService.findById(subScheme, false).getName() + ", ";

        if (workCategory != null)
            queryParameters += "Work Category : " + workCategory.replace('_', ' ') + ", ";

        if (beneficiary != null)
            queryParameters += messageSource.getMessage("msg.beneficiary", null, null)
                    + beneficiary.replaceAll("_C", "/C").replace("_", " ") + ", ";

        if (natureOfWork != null)
            queryParameters += messageSource.getMessage("msg.natureofwork", null, null)
                    + natureOfWorkService.findById(natureOfWork).getName() + ", ";

        if (queryParameters.endsWith(", "))
            queryParameters = queryParameters.substring(0, queryParameters.length() - 2);

        reportParams.put("queryParameters", queryParameters);

        return generateReportDepartmentWise(estimateAbstractReports, request, session, contentType, reportParams);
    }

    private ResponseEntity<byte[]> generateReportDepartmentWise(
            final List<EstimateAbstractReport> estimateAbstractReports, final HttpServletRequest request,
            final HttpSession session, final String contentType, final Map<String, Object> reportParams) {
        final List<EstimateAbstractReport> estimateAbstractReportPdfList = new ArrayList<EstimateAbstractReport>();
        final SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm a");
        ReportRequest reportInput = null;
        ReportOutput reportOutput = null;

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

        reportParams.put("heading",
                messageSource.getMessage("msg.estimateabstractreport.by.departmentwise", null, null));
        reportParams.put("reportRunDate", formatter.format(new Date()));
        reportParams.put("dataRunDate", dataRunDate);

        reportInput = new ReportRequest(
                messageSource.getMessage("msg.estimateabstractreportbydepartmentwisepdf", null, null),
                estimateAbstractReportPdfList, reportParams);

        final HttpHeaders headers = new HttpHeaders();
        if (contentType.equalsIgnoreCase("pdf")) {
            reportInput.setReportFormat(ReportFormat.PDF);
            headers.setContentType(MediaType.parseMediaType("application/pdf"));
            headers.add("content-disposition", "inline;filename=EstimateAbstractReportByDepartmentWise.pdf");
        } else {
            reportInput.setReportFormat(ReportFormat.XLS);
            headers.setContentType(MediaType.parseMediaType("application/vnd.ms-excel"));
            headers.add("content-disposition", "inline;filename=EstimateAbstractReportByDepartmentWise.xls");
        }
        reportOutput = reportService.createReport(reportInput);
        return new ResponseEntity<byte[]>(reportOutput.getReportOutputData(), headers, HttpStatus.CREATED);

    }

    @RequestMapping(value = "/typeofworkwise/pdf", method = RequestMethod.GET)
    public @ResponseBody ResponseEntity<byte[]> generatePDFTypeOfWorkWise(final HttpServletRequest request,
            @RequestParam("adminSanctionFromDate") final Date adminSanctionFromDate,
            @RequestParam("adminSanctionToDate") final Date adminSanctionToDate,
            @RequestParam("typeOfWork") final Long typeOfWork, @RequestParam("subTypeOfWork") final Long subTypeOfWork,
            @RequestParam("departments") final Set<Department> departments,
            @RequestParam("scheme") final Integer scheme, @RequestParam("subScheme") final Integer subScheme,
            @RequestParam("workCategory") final String workCategory,
            @RequestParam("beneficiary") final String beneficiary,
            @RequestParam("natureOfWork") final Long natureOfWork,
            @RequestParam("spillOverFlag") final boolean spillOverFlag,
            @RequestParam("contentType") final String contentType, final HttpSession session) throws IOException {
        final EstimateAbstractReport searchRequest = new EstimateAbstractReport();
        final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        searchRequest.setAdminSanctionFromDate(adminSanctionFromDate);
        searchRequest.setAdminSanctionToDate(adminSanctionToDate);
        searchRequest.setTypeOfWork(typeOfWork);
        searchRequest.setSubTypeOfWork(subTypeOfWork);
        searchRequest.setDepartments(departments);
        searchRequest.setScheme(scheme);
        searchRequest.setSubScheme(subScheme);
        searchRequest.setWorkCategory(workCategory);
        searchRequest.setBeneficiary(beneficiary);
        searchRequest.setNatureOfWork(natureOfWork);
        searchRequest.setSpillOverFlag(spillOverFlag);

        final List<EstimateAbstractReport> estimateAbstractReports = workProgressRegisterService
                .searchEstimateAbstractReportByTypeOfWorkWise(searchRequest);
        final Map<String, Object> reportParams = new HashMap<String, Object>();

        String queryParameters = messageSource.getMessage("msg.estimateabstractreport.by.typeofworkwise", null, null);
        if (spillOverFlag)
            queryParameters = messageSource.getMessage("msg.estimateabstractreport.by.typeofworkwise.for.spillover",
                    null, null);
        if (adminSanctionFromDate != null || adminSanctionToDate != null)
            queryParameters += "for ";

        if (adminSanctionFromDate != null && adminSanctionToDate != null)
            queryParameters += messageSource.getMessage("msg.daterange", null, null) + sdf.format(adminSanctionFromDate)
                    + " - " + sdf.format(adminSanctionToDate) + ", ";
        if (adminSanctionFromDate != null && adminSanctionToDate == null)
            queryParameters += messageSource.getMessage("msg.adminsanctionfromdate", null, null) + adminSanctionFromDate
                    + ", ";
        if (adminSanctionToDate != null && adminSanctionFromDate == null)
            queryParameters += messageSource.getMessage("msg.adminsanctiontodate", null, null) + adminSanctionToDate
                    + ", ";

        if (typeOfWork != null)
            queryParameters += messageSource.getMessage("msg.typeofwork", null, null)
                    + egwTypeOfWorkHibernateDAO.getTypeOfWorkById(typeOfWork).getDescription() + ", ";

        if (subTypeOfWork != null)
            queryParameters += messageSource.getMessage("msg.subtypeofwork", null, null)
                    + egwTypeOfWorkHibernateDAO.getTypeOfWorkById(subTypeOfWork).getDescription() + ", ";

        if (departments != null && !departments.toString().equalsIgnoreCase("[null]")) {
            String departmentNames = "";
            for (final Department dept : departments)
                departmentNames = departmentNames + dept.getName() + ",";
            departmentNames = departmentNames.substring(0, departmentNames.length() - 1);
            queryParameters += messageSource.getMessage("msg.departments", null, null) + departmentNames + ", ";
        }

        if (scheme != null)
            queryParameters += messageSource.getMessage("msg.scheme", null, null)
                    + schemeService.findById(scheme, false).getName() + ", ";

        if (subScheme != null)
            queryParameters += messageSource.getMessage("msg.subscheme", null, null)
                    + subSchemeService.findById(subScheme, false).getName() + ", ";

        if (workCategory != null)
            queryParameters += messageSource.getMessage("msg.workcategory", null, null) + workCategory.replace('_', ' ')
                    + ", ";

        if (beneficiary != null)
            queryParameters += messageSource.getMessage("msg.beneficiary", null, null)
                    + beneficiary.replaceAll("_C", "/C").replace("_", " ") + ", ";

        if (natureOfWork != null)
            queryParameters += messageSource.getMessage("msg.natureofwork", null, null)
                    + natureOfWorkService.findById(natureOfWork).getName() + ", ";

        if (queryParameters.endsWith(", "))
            queryParameters = queryParameters.substring(0, queryParameters.length() - 2);

        reportParams.put("queryParameters", queryParameters);

        return generateReportTypeOfWorkWise(estimateAbstractReports, request, session, contentType, departments,
                reportParams);
    }

    private ResponseEntity<byte[]> generateReportTypeOfWorkWise(
            final List<EstimateAbstractReport> estimateAbstractReports, final HttpServletRequest request,
            final HttpSession session, final String contentType, final Set<Department> departments,
            final Map<String, Object> reportParams) {
        final List<EstimateAbstractReport> estimateAbstractReportPdfList = new ArrayList<EstimateAbstractReport>();
        final SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm a");

        String dataRunDate = "";
        ReportRequest reportInput = null;
        ReportOutput reportOutput = null;

        if (estimateAbstractReports != null && !estimateAbstractReports.isEmpty())
            for (final EstimateAbstractReport eadwr : estimateAbstractReports) {
                final EstimateAbstractReport pdf = new EstimateAbstractReport();
                if (eadwr.getDepartmentName() != null)
                    pdf.setDepartmentName(eadwr.getDepartmentName());
                else
                    pdf.setDepartmentName("");

                if (eadwr.getTypeOfWorkName() != null)
                    pdf.setTypeOfWorkName(eadwr.getTypeOfWorkName());
                else
                    pdf.setTypeOfWorkName("");

                if (eadwr.getSubTypeOfWorkName() != null)
                    pdf.setSubTypeOfWorkName(eadwr.getSubTypeOfWorkName());
                else
                    pdf.setSubTypeOfWorkName("");

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

        reportParams.put("heading",
                messageSource.getMessage("msg.estimateabstractreport.by.typeofworkwise", null, null));
        reportParams.put("reportRunDate", formatter.format(new Date()));
        reportParams.put("dataRunDate", dataRunDate);
        if (departments != null && !departments.toString().equalsIgnoreCase("[null]"))
            reportInput = new ReportRequest(
                    messageSource.getMessage("msg.estimateabstractreportbytypeofworkwisewithdeptpdf", null, null),
                    estimateAbstractReportPdfList, reportParams);
        else
            reportInput = new ReportRequest(
                    messageSource.getMessage("msg.estimateabstractreportbytypeofworkwisepdf", null, null),
                    estimateAbstractReportPdfList, reportParams);

        final HttpHeaders headers = new HttpHeaders();
        if (contentType.equalsIgnoreCase("pdf")) {
            reportInput.setReportFormat(ReportFormat.PDF);
            headers.setContentType(MediaType.parseMediaType("application/pdf"));
            headers.add("content-disposition", "inline;filename=EstimateAbstractReportByTypeOfWorkWise.pdf");
        } else {
            reportInput.setReportFormat(ReportFormat.XLS);
            headers.setContentType(MediaType.parseMediaType("application/vnd.ms-excel"));
            headers.add("content-disposition", "inline;filename=EstimateAbstractReportByTypeOfWorkWise.xls");
        }
        reportOutput = reportService.createReport(reportInput);
        return new ResponseEntity<byte[]>(reportOutput.getReportOutputData(), headers, HttpStatus.CREATED);

    }

}