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
package org.egov.eis.web.controller.reports;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.egov.eis.entity.Assignment;
import org.egov.eis.entity.Employee;
import org.egov.eis.reports.entity.EmployeeAssignmentSearch;
import org.egov.eis.service.AssignmentService;
import org.egov.eis.service.DesignationService;
import org.egov.eis.service.PositionMasterService;
import org.egov.eis.utils.EisUtils;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.admin.master.service.DepartmentService;
import org.egov.infra.reporting.engine.ReportFormat;
import org.egov.infra.reporting.engine.ReportOutput;
import org.egov.infra.utils.DateUtils;
import org.egov.pims.commons.Designation;
import org.egov.pims.commons.Position;
import org.jfree.util.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lowagie.text.DocumentException;

import ar.com.fdvs.dj.core.DynamicJasperHelper;
import ar.com.fdvs.dj.core.layout.ClassicLayoutManager;
import ar.com.fdvs.dj.domain.DynamicReport;
import ar.com.fdvs.dj.domain.Style;
import ar.com.fdvs.dj.domain.builders.ColumnBuilderException;
import ar.com.fdvs.dj.domain.builders.FastReportBuilder;
import ar.com.fdvs.dj.domain.constants.Border;
import ar.com.fdvs.dj.domain.constants.HorizontalAlign;
import ar.com.fdvs.dj.domain.constants.Page;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Controller
public class EmployeeAssignmentReportPDFController {

    public static final String EMPLOYEEASSIGNMENTPDF = "employeeAssignemntPdf";
    private static final int MB = 1024 * 1024;
    private static final String REPORT_NO_DATA = "No Data";

    @Autowired
    private AssignmentService assignmentService;

    @Autowired
    private EisUtils eisUtils;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private DesignationService designationService;

    @Autowired
    private PositionMasterService positionMasterService;

    @RequestMapping(value = "/reports/employeeassignments/pdf", method = RequestMethod.GET)
    public @ResponseBody ResponseEntity<byte[]> generateEmployeeAssignmentsPDF(final HttpServletRequest request,
            @RequestParam("code") final String code, @RequestParam("name") final String name,
            @RequestParam("departmentId") final Long departmentId,
            @RequestParam("designationId") final Long designationId, @RequestParam("positionId") final Long positionId,
            @RequestParam("contentType") final String contentType, @RequestParam("date") final Date date,
            final HttpSession session, final Model model) throws DocumentException {
        final EmployeeAssignmentSearch employeeAssignmentSearch = new EmployeeAssignmentSearch();
        employeeAssignmentSearch.setEmployeeCode(code);
        employeeAssignmentSearch.setEmployeeName(name);
        employeeAssignmentSearch.setDepartment(departmentId);
        employeeAssignmentSearch.setDesignation(designationId);
        employeeAssignmentSearch.setPosition(positionId);
        employeeAssignmentSearch.setAssignmentDate(date);

        final List<Employee> employeeList = assignmentService.searchEmployeeAssignments(employeeAssignmentSearch);
        final StringBuilder searchCriteria = new StringBuilder();
        searchCriteria.append("Employee Assignment Report as on ");
        if (employeeAssignmentSearch.getAssignmentDate() != null)
            searchCriteria.append(DateUtils.getDefaultFormattedDate(employeeAssignmentSearch.getAssignmentDate()));
        if (StringUtils.isNotBlank(employeeAssignmentSearch.getEmployeeName()))
            searchCriteria.append(", Employee Name : ").append(employeeAssignmentSearch.getEmployeeName()).append("");
        if (StringUtils.isNotBlank(employeeAssignmentSearch.getEmployeeCode()))
            searchCriteria.append(", Employee Code : ").append(employeeAssignmentSearch.getEmployeeCode()).append(" ");
        if (employeeAssignmentSearch.getDepartment() != null) {
            final Department department = departmentService.getDepartmentById(employeeAssignmentSearch.getDepartment());
            searchCriteria.append(" for Department : ").append(department.getName()).append(" ");
        }
        if (employeeAssignmentSearch.getDesignation() != null) {
            final Designation designation = designationService
                    .getDesignationById(employeeAssignmentSearch.getDesignation());
            searchCriteria.append(" and Designation : ").append(designation.getName()).append(" ");
        }
        if (employeeAssignmentSearch.getPosition() != null) {
            final Position position = positionMasterService.getPositionById(employeeAssignmentSearch.getPosition());
            searchCriteria.append(" and Position : ").append(position.getName()).append(" ");
        }

        String searchString = StringUtils.EMPTY;
        if (searchCriteria.toString().endsWith(" "))
            searchString = searchCriteria.substring(0, searchCriteria.length() - 1);

        final List<EmployeeAssignmentSearch> searchResult = new ArrayList<EmployeeAssignmentSearch>();
        Map<String, String> tempAssignments = null;
        EmployeeAssignmentSearch empAssignmentSearch = null;
        int maxTempAssignments = 0;
        for (final Employee employee : employeeList) {
            int index = 0;
            tempAssignments = new HashMap<String, String>();
            empAssignmentSearch = new EmployeeAssignmentSearch();
            empAssignmentSearch.setEmployeeCode(employee.getCode());
            empAssignmentSearch.setEmployeeName(employee.getName());
            for (final Assignment assignment : employee.getAssignments())
                if (assignment.getPrimary()) {
                    empAssignmentSearch.setDepartmentName(assignment.getDepartment().getName());
                    empAssignmentSearch.setDesignationName(assignment.getDesignation().getName());
                    empAssignmentSearch.setPositionName(assignment.getPosition().getName());
                    empAssignmentSearch.setDateRange(DateUtils.getDefaultFormattedDate(assignment.getFromDate()) + " - "
                            + DateUtils.getDefaultFormattedDate(assignment.getToDate()));
                } else {
                    tempAssignments.put("department_" + String.valueOf(index), assignment.getDepartment().getName());
                    tempAssignments.put("designation_" + String.valueOf(index), assignment.getDesignation().getName());
                    tempAssignments.put("position_" + String.valueOf(index), assignment.getPosition().getName());
                    tempAssignments.put("daterange_" + String.valueOf(index),
                            DateUtils.getDefaultFormattedDate(assignment.getFromDate()) + " - "
                                    + DateUtils.getDefaultFormattedDate(assignment.getToDate()));
                    index++;
                }
            empAssignmentSearch.setTempPositionDetails(tempAssignments);
            searchResult.add(empAssignmentSearch);
            if (employee.getAssignments().size() >= maxTempAssignments)
                maxTempAssignments = employee.getAssignments().size();

        }
        JasperPrint jasperPrint;
        ByteArrayOutputStream outputBytes = null;
        try {
            jasperPrint = generateEmployeeAssignmentReport(searchResult, maxTempAssignments, searchString);
            outputBytes = new ByteArrayOutputStream(MB);
            JasperExportManager.exportReportToPdfStream(jasperPrint, outputBytes);
        } catch (final Exception e) {
            Log.error("Error while generating employee assignment report ", e);
        }
        final ReportOutput reportOutput = new ReportOutput();
        reportOutput.setReportOutputData(outputBytes.toByteArray());
        final HttpHeaders headers = new HttpHeaders();
        if (contentType.equalsIgnoreCase("pdf")) {
            reportOutput.setReportFormat(ReportFormat.PDF);
            reportOutput.setReportFormat(ReportFormat.PDF);
            headers.setContentType(MediaType.parseMediaType("application/pdf"));
            headers.add("content-disposition", "inline;filename=EmployeeAssignment.pdf");
        } else {
            reportOutput.setReportFormat(ReportFormat.XLS);
            headers.setContentType(MediaType.parseMediaType("application/vnd.ms-excel"));
            headers.add("content-disposition", "inline;filename=EmployeeAssignment.xls");
        }
        return new ResponseEntity<byte[]>(reportOutput.getReportOutputData(), headers, HttpStatus.CREATED);
    }

    private JasperPrint generateEmployeeAssignmentReport(final List<EmployeeAssignmentSearch> searchResult,
            final int maxTempAssignments, final String searchString)
            throws ColumnBuilderException, ClassNotFoundException, JRException {
        final FastReportBuilder reportBuilder = new FastReportBuilder();
        final Style STYLE_BLANK = Style.createBlankStyle("oddRowNoStyle");
        reportBuilder.setPrintBackgroundOnOddRows(false).setWhenNoData(REPORT_NO_DATA, null)
                .setDefaultStyles(eisUtils.getTitleStyle(), eisUtils.getSubTitleStyle(), STYLE_BLANK, STYLE_BLANK)
                .setDetailHeight(70).setUseFullPageWidth(true).setTitleHeight(100)
                .setPageSizeAndOrientation(Page.Page_A4_Landscape()).setOddRowBackgroundStyle(STYLE_BLANK)
                .setWhenResourceMissingShowKey();
        reportBuilder
                .addColumn("Employee Code", "employeeCode", String.class.getName(), 3,
                        eisUtils.getTextStyleLeftBorder(), eisUtils.getHeaderStyleLeftAlign())
                .addColumn("Employee Name", "employeeName", String.class.getName(), 5,
                        eisUtils.getTextStyleLeftBorder(), eisUtils.getHeaderStyleLeftAlign())
                .addColumn("Department", "departmentName", String.class.getName(), 3, eisUtils.getTextStyleLeftBorder(),
                        eisUtils.getHeaderStyleLeftAlign())
                .addColumn("Designation", "designationName", String.class.getName(), 3,
                        eisUtils.getTextStyleLeftBorder(), eisUtils.getHeaderStyleLeftAlign())
                .addColumn("Position", "positionName", String.class.getName(), 3, eisUtils.getTextStyleLeftBorder(),
                        eisUtils.getHeaderStyleLeftAlign())
                .addColumn("Date Range", "dateRange", String.class.getName(), 3, eisUtils.getTextStyleLeftBorder(),
                        eisUtils.getHeaderStyleLeftAlign());

        reportBuilder.setTitle(searchString);
        for (int i = 0; i < maxTempAssignments - 1; i++) {
            reportBuilder.addColumn("Department", "tempPositionDetails.department_" + i, String.class.getName(), 3,
                    eisUtils.getTextStyleLeftBorder(), eisUtils.getHeaderStyleLeftAlign());
            reportBuilder.addColumn("Designation", "tempPositionDetails.designation_" + i, String.class.getName(), 3,
                    eisUtils.getTextStyleLeftBorder(), eisUtils.getHeaderStyleLeftAlign());
            reportBuilder.addColumn("Position", "tempPositionDetails.position_" + i, String.class.getName(), 3,
                    eisUtils.getTextStyleLeftBorder(), eisUtils.getHeaderStyleLeftAlign());
            reportBuilder.addColumn("Date Range", "tempPositionDetails.daterange_" + i, String.class.getName(), 3,
                    eisUtils.getTextStyleLeftBorder(), eisUtils.getHeaderStyleLeftAlign());
        }

        final Style colspanStyle = new Style();
        colspanStyle.setHorizontalAlign(HorizontalAlign.CENTER);
        reportBuilder.setColspan(2, 4, "Primary Position");
        int k = 1;
        for (int i = 6; i < reportBuilder.getColumns().size(); i = i + 4) {
            reportBuilder.setColspan(i, 4, "Temporary Position " + k);
            k++;
        }

        final DynamicReport dr = reportBuilder.build();
        dr.getOptions().getDefaultHeaderStyle().setBorder(Border.PEN_1_POINT());
        dr.setWhenNoDataShowColumnHeader(false);
        dr.setWhenNoDataShowTitle(false);
        final JRDataSource ds = new JRBeanCollectionDataSource(searchResult);
        return DynamicJasperHelper.generateJasperPrint(dr, new ClassicLayoutManager(), ds);
    }

}
