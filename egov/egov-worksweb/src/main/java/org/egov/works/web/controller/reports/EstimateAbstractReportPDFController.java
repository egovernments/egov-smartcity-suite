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
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.egov.commons.service.TypeOfWorkService;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.admin.master.service.DepartmentService;
import org.egov.infra.reporting.engine.ReportFormat;
import org.egov.infra.reporting.engine.ReportOutput;
import org.egov.infra.reporting.engine.ReportRequest;
import org.egov.infra.reporting.engine.ReportService;
import org.egov.infra.utils.StringUtils;
import org.egov.services.masters.SchemeService;
import org.egov.services.masters.SubSchemeService;
import org.egov.works.config.properties.WorksApplicationProperties;
import org.egov.works.masters.service.NatureOfWorkService;
import org.egov.works.reports.entity.EstimateAbstractReport;
import org.egov.works.reports.entity.enums.WorkStatus;
import org.egov.works.reports.service.WorkProgressRegisterService;
import org.egov.works.utils.WorksConstants;
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
    @Qualifier("messageSource")
    private MessageSource messageSource;

    @Autowired
    private TypeOfWorkService typeOfWorkService;

    @Autowired
    private WorksApplicationProperties worksApplicationProperties;

    @RequestMapping(value = "/departmentwise/pdf", method = RequestMethod.GET)
    @ResponseBody public  ResponseEntity<byte[]> generatePDFDepartmentWise(
            @RequestParam("adminSanctionFromDate") final Date adminSanctionFromDate,
            @RequestParam("adminSanctionToDate") final Date adminSanctionToDate,
            @RequestParam("department") final Long department, @RequestParam("scheme") final Integer scheme,
            @RequestParam("subScheme") final Integer subScheme, @RequestParam("workCategory") final String workCategory,
            @RequestParam("beneficiary") final String beneficiary, @RequestParam("workStatus") final String workStatus,
            @RequestParam("natureOfWork") final Long natureOfWork,
            @RequestParam("spillOverFlag") final boolean spillOverFlag,
            @RequestParam("contentType") final String contentType) throws IOException {
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
        searchRequest.setWorkStatus(workStatus);
        searchRequest.setNatureOfWork(natureOfWork);
        searchRequest.setSpillOverFlag(spillOverFlag);

        List<EstimateAbstractReport> estimateAbstractReports;
        if (worksApplicationProperties.lineEstimateRequired())
            estimateAbstractReports = workProgressRegisterService
                    .searchEstimateAbstractReportByDepartmentWise(searchRequest);
        else
            estimateAbstractReports = workProgressRegisterService
                    .searchEstimateAbstractReportByDepartmentWiseForAE(searchRequest);

        String queryParameters = setQueryParameterForDepartmentWise(spillOverFlag);
        queryParameters = setQueryParametersForAdminSanctionDates(adminSanctionFromDate, adminSanctionToDate,
                department, sdf, queryParameters);
        if (department != null)
            queryParameters += messageSource.getMessage("msg.department", null, null)
                    + departmentService.getDepartmentById(department).getName() + ", ";

        queryParameters = setQueryParametersForSchemesAndSubSchemes(scheme, subScheme, queryParameters);

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

        return generateReportDepartmentWise(estimateAbstractReports, contentType, searchRequest, reportParams);
    }

    private String setQueryParameterForDepartmentWise(final boolean spillOverFlag) {
        String queryParameters = messageSource.getMessage("msg.estimateabstractreport.by.departmentwise", null, null);
        if (spillOverFlag)
            queryParameters = messageSource.getMessage("msg.estimateabstractreport.by.departmentwise.for.spillover",
                    null, null);
        return queryParameters;
    }

    private String setQueryParametersForAdminSanctionDates(final Date adminSanctionFromDate,
            final Date adminSanctionToDate, final Long department, final SimpleDateFormat sdf, String queryParameters) {
        String queryParams = queryParameters;
        if (adminSanctionFromDate != null || adminSanctionToDate != null || department != null)
            queryParams += " for ";

        if (adminSanctionFromDate != null && adminSanctionToDate != null)
            queryParams += messageSource.getMessage("msg.daterange", null, null) + sdf.format(adminSanctionFromDate)
                    + " - " + sdf.format(adminSanctionToDate) + ", ";
        if (adminSanctionFromDate != null && adminSanctionToDate == null)
            queryParams += messageSource.getMessage("msg.adminsanctionfromdate", null, null) + adminSanctionFromDate
                    + ", ";
        if (adminSanctionToDate != null && adminSanctionFromDate == null)
            queryParams += messageSource.getMessage("msg.adminsanctiontodate", null, null) + adminSanctionToDate + ", ";
        return queryParams;
    }

    private String setQueryParametersForSchemesAndSubSchemes(final Integer scheme, final Integer subScheme,
            String queryParameters) {
        String queryParams = queryParameters;
        if (scheme != null)
            queryParams += messageSource.getMessage("msg.scheme", null, null)
                    + schemeService.findById(scheme, false).getName() + ", ";

        if (subScheme != null)
            queryParams += messageSource.getMessage("msg.subscheme", null, null)
                    + subSchemeService.findById(subScheme, false).getName() + ", ";
        return queryParams;
    }

    private ResponseEntity<byte[]> generateReportDepartmentWise(
            final List<EstimateAbstractReport> estimateAbstractReports, final String contentType,
            final EstimateAbstractReport searchRequest, final Map<String, Object> reportParams) {
        final List<EstimateAbstractReport> estimateAbstractReportPdfList = new ArrayList<EstimateAbstractReport>();
        final SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm a");
        ReportRequest reportInput;
        ReportOutput reportOutput;
        String dataRunDate = StringUtils.EMPTY;
        
        final EstimateAbstractReport pdf = new EstimateAbstractReport();
        if (estimateAbstractReports != null && !estimateAbstractReports.isEmpty())
            for (final EstimateAbstractReport eadwr : estimateAbstractReports) {
                setDepartmentNamePDFValue(pdf, eadwr);

                setEstimatesPDFValues(eadwr, pdf);

                setSanctionedEstimatesPDFValues(eadwr, pdf);

                setPDFLOAValues(eadwr, pdf);

                setPDFAgreementValue(eadwr, pdf);

                setWorkStatusPDFValues(eadwr, pdf);

                setBillsPDFValues(eadwr, pdf);

                setPDFValuesForWorkOrderStatus(searchRequest, pdf);

                dataRunDate = formatter.format(workProgressRegisterService.getReportSchedulerRunDate());

                estimateAbstractReportPdfList.add(pdf);
            }

        reportParams.put("heading",
                messageSource.getMessage("msg.estimateabstractreport.by.departmentwise", null, null));
        reportParams.put("reportRunDate", formatter.format(new Date()));
        reportParams.put("dataRunDate", dataRunDate);

        reportInput = setDocumentWiseReportInputValues(reportParams, estimateAbstractReportPdfList);

        final HttpHeaders headers = setDocumentWisePDFHeaders(contentType, reportInput);
        reportOutput = reportService.createReport(reportInput);
        return new ResponseEntity<byte[]>(reportOutput.getReportOutputData(), headers, HttpStatus.CREATED);

    }

    private void setDepartmentNamePDFValue(final EstimateAbstractReport pdf, final EstimateAbstractReport eadwr) {
        if (eadwr.getDepartmentName() != null)
            pdf.setDepartmentName(eadwr.getDepartmentName());
        else
            pdf.setDepartmentName(StringUtils.EMPTY);
    }

    private void setEstimatesPDFValues(final EstimateAbstractReport eadwr, final EstimateAbstractReport pdf) {
        if (eadwr.getLineEstimates() != null)
            pdf.setLineEstimates(eadwr.getLineEstimates());
        else
            pdf.setLineEstimates(null);

        if (eadwr.getAbstractEstimates() != null)
            pdf.setAbstractEstimates(eadwr.getAbstractEstimates());
        else
            pdf.setAbstractEstimates(null);
    }

    private void setPDFAgreementValue(final EstimateAbstractReport eadwr, final EstimateAbstractReport pdf) {
        if (eadwr.getAgreementValueInCrores() != null)
            pdf.setAgreementValueInCrores(new BigDecimal(eadwr.getAgreementValueInCrores())
                    .setScale(2, BigDecimal.ROUND_HALF_EVEN).toString());
        else
            pdf.setAgreementValueInCrores(WorksConstants.NA);
    }

    private void setSanctionedEstimatesPDFValues(final EstimateAbstractReport eadwr, final EstimateAbstractReport pdf) {
        if (eadwr.getAdminSanctionedEstimates() != null)
            pdf.setAdminSanctionedEstimates(eadwr.getAdminSanctionedEstimates());
        else
            pdf.setAdminSanctionedEstimates(null);

        setAdminSanctionedAmount(eadwr, pdf);

        if (eadwr.getTechnicalSanctionedEstimates() != null)
            pdf.setTechnicalSanctionedEstimates(eadwr.getTechnicalSanctionedEstimates());
        else
            pdf.setTechnicalSanctionedEstimates(null);
    }

    private void setPDFValuesForWorkOrderStatus(final EstimateAbstractReport searchRequest,
            final EstimateAbstractReport pdf) {
        // Making value NA based on work status
        if (searchRequest.getWorkStatus() != null && !searchRequest.getWorkStatus().isEmpty()) {

            setPDFValuesWhenLOANotCreated(searchRequest, pdf);

            setPDFValuesWhenWorkNotCommenced(searchRequest, pdf);

            setPDFValuesWhenWorkInProgressOrCompleted(searchRequest, pdf);

        }
    }

    private HttpHeaders setDocumentWisePDFHeaders(final String contentType, final ReportRequest reportInput) {
        final HttpHeaders headers = new HttpHeaders();

        if (WorksConstants.PDF.equalsIgnoreCase(contentType)) {
            reportInput.setReportFormat(ReportFormat.PDF);
            headers.setContentType(MediaType.parseMediaType("application/pdf"));
            if (worksApplicationProperties.lineEstimateRequired())
                headers.add(WorkProgressRegisterService.CONTENTDISPOSITION, "inline;filename=EstimateAbstractReportByDepartmentWise.pdf");
            else
                headers.add(WorkProgressRegisterService.CONTENTDISPOSITION, "inline;filename=EstimateAbstractReportForDepartmentWise.pdf");
        } else {
            reportInput.setReportFormat(ReportFormat.XLS);
            headers.setContentType(MediaType.parseMediaType("application/vnd.ms-excel"));
            headers.add(WorkProgressRegisterService.CONTENTDISPOSITION, "inline;filename=EstimateAbstractReportByDepartmentWise.xls");
        }
        return headers;
    }

    private ReportRequest setDocumentWiseReportInputValues(final Map<String, Object> reportParams,
            final List<EstimateAbstractReport> estimateAbstractReportPdfList) {
        ReportRequest reportInput;
        if (worksApplicationProperties.lineEstimateRequired())
            reportInput = new ReportRequest(
                    messageSource.getMessage("msg.estimateabstractreportbydepartmentwisepdf", null, null),
                    estimateAbstractReportPdfList, reportParams);
        else
            reportInput = new ReportRequest(
                    messageSource.getMessage("msg.estimateabstractreportfordepartmentwise", null, null),
                    estimateAbstractReportPdfList, reportParams);
        return reportInput;
    }

    private void setWorkStatusPDFValues(final EstimateAbstractReport eadwr, final EstimateAbstractReport pdf) {
        setWorkNotCommencedPDFValue(pdf, eadwr);

        if (eadwr.getWorkInProgress() != null)
            pdf.setWorkInProgress(eadwr.getWorkInProgress());
        else
            pdf.setWorkInProgress(null);

        if (eadwr.getWorkCompleted() != null)
            pdf.setWorkCompleted(eadwr.getWorkCompleted());
        else
            pdf.setWorkCompleted(null);
    }

    private void setBillsPDFValues(final EstimateAbstractReport eadwr, final EstimateAbstractReport pdf) {
        if (eadwr.getBillsCreated() != null)
            pdf.setBillsCreated(eadwr.getBillsCreated());
        else
            pdf.setBillsCreated(null);

        if (eadwr.getBillValueInCrores() != null)
            pdf.setBillValueInCrores(
                    new BigDecimal(eadwr.getBillValueInCrores()).setScale(2, BigDecimal.ROUND_HALF_EVEN).toString());
        else
            pdf.setBillValueInCrores(WorksConstants.NA);
    }

    private void setPDFLOAValues(final EstimateAbstractReport eadwr, final EstimateAbstractReport pdf) {
        if (eadwr.getLoaCreated() != null)
            pdf.setLoaCreated(eadwr.getLoaCreated());
        else
            pdf.setLoaCreated(null);

        if (eadwr.getLoaNotCreated() != null)
            pdf.setLoaNotCreated(eadwr.getLoaNotCreated());
        else
            pdf.setLoaNotCreated(null);
    }

    private void setAdminSanctionedAmount(final EstimateAbstractReport eadwr, final EstimateAbstractReport pdf) {
        if (eadwr.getLeAdminSanctionedAmountInCrores() != null)
            pdf.setLeAdminSanctionedAmountInCrores(new BigDecimal(eadwr.getLeAdminSanctionedAmountInCrores())
                    .setScale(2, BigDecimal.ROUND_HALF_EVEN).toString());
        else
            pdf.setLeAdminSanctionedAmountInCrores(WorksConstants.NA);

        if (eadwr.getAeAdminSanctionedAmountInCrores() != null)
            pdf.setAeAdminSanctionedAmountInCrores(new BigDecimal(eadwr.getAeAdminSanctionedAmountInCrores())
                    .setScale(2, BigDecimal.ROUND_HALF_EVEN).toString());
        else
            pdf.setAeAdminSanctionedAmountInCrores(WorksConstants.NA);

        if (eadwr.getWorkValueOfAdminSanctionedAEInCrores() != null)
            pdf.setWorkValueOfAdminSanctionedAEInCrores(new BigDecimal(eadwr.getWorkValueOfAdminSanctionedAEInCrores())
                    .setScale(2, BigDecimal.ROUND_HALF_EVEN).toString());
        else
            pdf.setWorkValueOfAdminSanctionedAEInCrores(WorksConstants.NA);
    }

    private void setPDFValuesWhenWorkInProgressOrCompleted(final EstimateAbstractReport searchRequest,
            final EstimateAbstractReport pdf) {
        if (searchRequest.getWorkStatus().equalsIgnoreCase(WorkStatus.In_Progress.toString())
                || searchRequest.getWorkStatus().equalsIgnoreCase(WorkStatus.Completed.toString())) {
            pdf.setLoaNotCreated(WorksConstants.NA);
            pdf.setWorkNotCommenced(WorksConstants.NA);
            pdf.setWorkInProgress(WorksConstants.NA);
            pdf.setWorkCompleted(WorksConstants.NA);
        }
    }

    private void setPDFValuesWhenWorkNotCommenced(final EstimateAbstractReport searchRequest,
            final EstimateAbstractReport pdf) {
        if (searchRequest.getWorkStatus().equalsIgnoreCase(WorkStatus.Not_Commenced.toString())) {
            pdf.setLoaNotCreated(WorksConstants.NA);
            pdf.setWorkNotCommenced(WorksConstants.NA);
            pdf.setWorkInProgress(WorksConstants.NA);
            pdf.setWorkCompleted(WorksConstants.NA);
            pdf.setBillsCreated(WorksConstants.NA);
            pdf.setBillValueInCrores(WorksConstants.NA);
        }
    }

    private void setPDFValuesWhenLOANotCreated(final EstimateAbstractReport searchRequest,
            final EstimateAbstractReport pdf) {
        if (searchRequest.getWorkStatus().equalsIgnoreCase(WorkStatus.LOA_Not_Created.toString())) {
            pdf.setLoaCreated(WorksConstants.NA);
            pdf.setAgreementValueInCrores(WorksConstants.NA);
            pdf.setLoaNotCreated(WorksConstants.NA);
            pdf.setWorkNotCommenced(WorksConstants.NA);
            pdf.setWorkInProgress(WorksConstants.NA);
            pdf.setWorkCompleted(WorksConstants.NA);
            pdf.setBillsCreated(WorksConstants.NA);
            pdf.setBillValueInCrores(WorksConstants.NA);
        }
    }

    @RequestMapping(value = "/typeofworkwise/pdf", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<byte[]> generatePDFTypeOfWorkWise(final HttpServletRequest request,
            @RequestParam("adminSanctionFromDate") final Date adminSanctionFromDate,
            @RequestParam("adminSanctionToDate") final Date adminSanctionToDate,
            @RequestParam("typeOfWork") final Long typeOfWork, @RequestParam("subTypeOfWork") final Long subTypeOfWork,
            @RequestParam("departments") final Set<Department> departments,
            @RequestParam("scheme") final Integer scheme, @RequestParam("subScheme") final Integer subScheme,
            @RequestParam("workCategory") final String workCategory,
            @RequestParam("beneficiary") final String beneficiary,
            @RequestParam("natureOfWork") final Long natureOfWork, @RequestParam("workStatus") final String workStatus,
            @RequestParam("spillOverFlag") final boolean spillOverFlag,
            @RequestParam("contentType") final String contentType, final HttpSession session) throws IOException {
        final EstimateAbstractReport searchRequest = new EstimateAbstractReport();
        final Map<String, Object> reportParams = new HashMap<String, Object>();
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
        searchRequest.setWorkStatus(workStatus);
        searchRequest.setNatureOfWork(natureOfWork);
        searchRequest.setSpillOverFlag(spillOverFlag);

        List<EstimateAbstractReport> estimateAbstractReports;
        if (worksApplicationProperties.lineEstimateRequired())
            estimateAbstractReports = workProgressRegisterService
                    .searchEstimateAbstractReportByTypeOfWorkWise(searchRequest);
        else
            estimateAbstractReports = workProgressRegisterService
                    .searchEstimateAbstractReportByTypeOfWorkWiseForAE(searchRequest);

        String queryParameters = setQueryParametersForTOWWise(spillOverFlag);
        queryParameters = setQueryParametersForAdminSanctionedDates(adminSanctionFromDate, adminSanctionToDate, sdf,
                queryParameters);

        queryParameters = setQueryParametersForTypeOfWork(typeOfWork, subTypeOfWork, queryParameters);

        queryParameters = setQueryParametersForDepartmentNames(departments, queryParameters);

        queryParameters = setQueryParametersForSchemesAndSubSchemes(scheme, subScheme, queryParameters);

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

        return generateReportTypeOfWorkWise(estimateAbstractReports, contentType, searchRequest, reportParams);
    }

    private String setQueryParametersForDepartmentNames(final Set<Department> departments, String queryParameters) {
        if (departments != null && !"[null]".equalsIgnoreCase(departments.toString())
                && !"[]".equalsIgnoreCase(departments.toString())) {
            StringBuilder deptNames = new StringBuilder();
            for (final Department dept : departments)
                deptNames.append(dept.getName() + ",");
            final String departmentNames = deptNames.toString().substring(0, deptNames.toString().length() - 1);
            queryParameters += messageSource.getMessage("msg.departments", null, null) + departmentNames + ", ";
        }
        return queryParameters;
    }

    private String setQueryParametersForTOWWise(final boolean spillOverFlag) {
        String queryParameters = messageSource.getMessage("msg.estimateabstractreport.by.typeofworkwise", null, null);
        if (spillOverFlag)
            queryParameters = messageSource.getMessage("msg.estimateabstractreport.by.typeofworkwise.for.spillover",
                    null, null);
        return queryParameters;
    }

    private String setQueryParametersForAdminSanctionedDates(final Date adminSanctionFromDate,
            final Date adminSanctionToDate, final SimpleDateFormat sdf, String queryParameters) {
        String queryParams = queryParameters;
        if (adminSanctionFromDate != null || adminSanctionToDate != null)
            queryParams += " for ";

        if (adminSanctionFromDate != null && adminSanctionToDate != null)
            queryParams += messageSource.getMessage("msg.daterange", null, null) + sdf.format(adminSanctionFromDate)
                    + " - " + sdf.format(adminSanctionToDate) + ", ";
        if (adminSanctionFromDate != null && adminSanctionToDate == null)
            queryParams += messageSource.getMessage("msg.adminsanctionfromdate", null, null) + adminSanctionFromDate
                    + ", ";
        if (adminSanctionToDate != null && adminSanctionFromDate == null)
            queryParams += messageSource.getMessage("msg.adminsanctiontodate", null, null) + adminSanctionToDate + ", ";
        return queryParams;
    }

    private String setQueryParametersForTypeOfWork(final Long typeOfWork, final Long subTypeOfWork,
            String queryParameters) {
        String queryParams = queryParameters;
        if (typeOfWork != null)
            queryParams += messageSource.getMessage("msg.typeofwork", null, null)
                    + typeOfWorkService.getTypeOfWorkById(typeOfWork).getName() + ", ";

        if (subTypeOfWork != null)
            queryParams += messageSource.getMessage("msg.subtypeofwork", null, null)
                    + typeOfWorkService.getTypeOfWorkById(subTypeOfWork).getName() + ", ";
        return queryParams;
    }

    private ResponseEntity<byte[]> generateReportTypeOfWorkWise(
            final List<EstimateAbstractReport> estimateAbstractReports,
            final String contentType, final EstimateAbstractReport searchRequest,
            final Map<String, Object> reportParams) {
        final List<EstimateAbstractReport> estimateAbstractReportPdfList = new ArrayList<EstimateAbstractReport>();
        final SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm a");

        String dataRunDate = StringUtils.EMPTY;
        ReportRequest reportInput;
        ReportOutput reportOutput;
        final EstimateAbstractReport pdf = new EstimateAbstractReport();
        if (estimateAbstractReports != null && !estimateAbstractReports.isEmpty())
            for (final EstimateAbstractReport eadwr : estimateAbstractReports) {
                
                setDepartmentNamePDFValue(pdf, eadwr);

                setTOWPDFValues(eadwr, pdf);

                setEstimatesPDFValues(eadwr, pdf);

                setSanctionedEstimatesPDFValues(eadwr, pdf);

                setPDFLOAValues(eadwr, pdf);

                setWorkNotCommencedPDFValue(pdf, eadwr);

                setPDFAgreementValue(eadwr, pdf);

                setWorkStatusPDFValues(eadwr, pdf);

                setBillsPDFValues(eadwr, pdf);

                setPDFValuesForWorkOrderStatus(searchRequest, pdf);

                dataRunDate = formatter.format(workProgressRegisterService.getReportSchedulerRunDate());

                estimateAbstractReportPdfList.add(pdf);
            }

        reportParams.put("heading",
                messageSource.getMessage("msg.estimateabstractreport.by.typeofworkwise", null, null));
        reportParams.put("reportRunDate", formatter.format(new Date()));
        reportParams.put("dataRunDate", dataRunDate);
        reportInput = setTOWWiseReportInputValue(searchRequest, reportParams, estimateAbstractReportPdfList);

        final HttpHeaders headers = setTOWWiseReportHeaderValues(contentType, reportInput);
        reportOutput = reportService.createReport(reportInput);
        return new ResponseEntity<byte[]>(reportOutput.getReportOutputData(), headers, HttpStatus.CREATED);

    }

    private void setWorkNotCommencedPDFValue(final EstimateAbstractReport pdf, final EstimateAbstractReport eadwr) {
        if (eadwr.getWorkNotCommenced() != null)
            pdf.setWorkNotCommenced(eadwr.getWorkNotCommenced());
        else
            pdf.setWorkNotCommenced(null);
    }

    private ReportRequest setTOWWiseReportInputValue(final EstimateAbstractReport searchRequest,
            final Map<String, Object> reportParams, final List<EstimateAbstractReport> estimateAbstractReportPdfList) {
        ReportRequest reportInput;
        if (searchRequest.getDepartments() != null
                && !"[null]".equalsIgnoreCase(searchRequest.getDepartments().toString())
                && !"[]".equalsIgnoreCase(searchRequest.getDepartments().toString()))
            if (worksApplicationProperties.lineEstimateRequired())
                reportInput = new ReportRequest(
                        messageSource.getMessage("msg.estimateabstractreportbytypeofworkwisewithdeptpdf", null, null),
                        estimateAbstractReportPdfList, reportParams);
            else
                reportInput = new ReportRequest(
                        messageSource.getMessage("msg.estimateabstractreportfortypeofworkwise", null, null),
                        estimateAbstractReportPdfList, reportParams);
        else
            reportInput = new ReportRequest(
                    messageSource.getMessage("msg.estimateabstractreportbytypeofworkwisepdf", null, null),
                    estimateAbstractReportPdfList, reportParams);
        return reportInput;
    }

    private HttpHeaders setTOWWiseReportHeaderValues(final String contentType, final ReportRequest reportInput) {
        final HttpHeaders headers = new HttpHeaders();

        if (WorksConstants.PDF.equalsIgnoreCase(contentType)) {
            reportInput.setReportFormat(ReportFormat.PDF);
            headers.setContentType(MediaType.parseMediaType("application/pdf"));
            if (worksApplicationProperties.lineEstimateRequired())
                headers.add(WorkProgressRegisterService.CONTENTDISPOSITION, "inline;filename=EstimateAbstractReportByTypeOfWorkWise.pdf");
            else
                headers.add(WorkProgressRegisterService.CONTENTDISPOSITION, "inline;filename=EstimateAbstractReportForTypeOfWorkWise.pdf");
        } else {
            reportInput.setReportFormat(ReportFormat.XLS);
            headers.setContentType(MediaType.parseMediaType("application/vnd.ms-excel"));
            headers.add(WorkProgressRegisterService.CONTENTDISPOSITION, "inline;filename=EstimateAbstractReportByTypeOfWorkWise.xls");
        }
        return headers;
    }

    private void setTOWPDFValues(final EstimateAbstractReport eadwr, final EstimateAbstractReport pdf) {
        if (eadwr.getTypeOfWorkName() != null)
            pdf.setTypeOfWorkName(eadwr.getTypeOfWorkName());
        else
            pdf.setTypeOfWorkName(StringUtils.EMPTY);

        if (eadwr.getSubTypeOfWorkName() != null)
            pdf.setSubTypeOfWorkName(eadwr.getSubTypeOfWorkName());
        else
            pdf.setSubTypeOfWorkName(StringUtils.EMPTY);
    }

}