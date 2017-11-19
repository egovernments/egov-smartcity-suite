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
package org.egov.works.web.actions.tender;

import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.reporting.engine.ReportService;
import org.egov.infra.web.struts.actions.BaseFormAction;
import org.egov.pims.service.EmployeeServiceOld;
import org.egov.works.abstractestimate.entity.AbstractEstimate;
import org.egov.works.models.tender.TenderResponse;
import org.egov.works.services.TenderResponseService;
import org.egov.works.services.WorksService;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@Results({
        @Result(name = BaseFormAction.SUCCESS, type = "stream", location = "tenderResponsePDF", params = {
                "inputName", "tenderResponsePDF", "contentType", "application/pdf", "contentDisposition", "no-cache" }),
        @Result(name = "reportView", type = "stream", location = "tenderScrtAbsrtPDF", params = {
                "contentType", "application/pdf", "contentDisposition", "attachment; filename=${fileName}" }) })
@ParentPackage("egov")
public class TenderNegotiationPDFAction extends BaseFormAction {

    private static final long serialVersionUID = 3411944565347208419L;
    private Long tenderResponseId;
    private InputStream tenderResponsePDF;
    private InputStream tenderScrtAbsrtPDF;
    private TenderResponseService tenderResponseService;
    private ReportService reportService;
    @Autowired
    private EmployeeServiceOld employeeService;
    private WorksService worksService;
    private String fileName;

    @Override
    public Object getModel() {
        return null;
    }

    @Override
    public String execute() {
        if (tenderResponseId != null) {
            final Map<String, String> pdfLabel = getPdfReportLabel();
            final TenderResponse tenderResponse = getTenderResponse();
            Boundary boundary = null;
            if (tenderResponse != null && tenderResponse.getTenderEstimate() != null
                    && tenderResponse.getTenderEstimate().getWorksPackage() == null) {
                final AbstractEstimate estimate = tenderResponse.getTenderEstimate().getAbstractEstimate();
                boundary = getTopLevelBoundary(estimate.getWard());
            }
            final ByteArrayOutputStream out = new ByteArrayOutputStream(1024 * 100);
            final TenderNegotiationPDFGenerator pdfGenerator = new TenderNegotiationPDFGenerator(tenderResponse,
                    boundary == null ? "" : boundary.getName(), out, pdfLabel);
            pdfGenerator.setPersistenceService(getPersistenceService());
            pdfGenerator.setEmployeeService(employeeService);
            pdfGenerator.setWorksService(worksService);
            pdfGenerator.generatePDF();
            tenderResponsePDF = new ByteArrayInputStream(out.toByteArray());
        }
        return SUCCESS;
    }

    private TenderResponse getTenderResponse() {
        return tenderResponseService.findById(tenderResponseId, false);
    }

    protected Boundary getTopLevelBoundary(final Boundary boundary) {
        Boundary b = boundary;
        while (b != null && b.getParent() != null)
            b = b.getParent();
        return b;
    }

    public void setTenderResponseId(final Long tenderResponseId) {
        this.tenderResponseId = tenderResponseId;
    }

    public InputStream getTenderResponsePDF() {
        return tenderResponsePDF;
    }

    public void setEmployeeService(final EmployeeServiceOld employeeService) {
        this.employeeService = employeeService;
    }

    public void setTenderResponseService(final TenderResponseService tenderResponseService) {
        this.tenderResponseService = tenderResponseService;
    }

    /*
     * Generating label
     */
    public Map<String, String> getPdfReportLabel() {
        final Map<String, String> pdfLabel = new HashMap<String, String>();
        pdfLabel.put("tenderNegotiationpdf.header", "AFTER NEGOTIATION COMPARATIVE STATEMENT");
        pdfLabel.put("tenderNegotiationpdf.zone", "Zone: ");
        pdfLabel.put("tenderNegotiationpdf.ward", "Ward ");
        pdfLabel.put("tenderNegotiationpdf.nameofwork", "Name of Work: ");
        pdfLabel.put("tenderNumber", "Tender Number: ");
        pdfLabel.put("tenderFileNo", "Tender File No: ");
        pdfLabel.put("tenderNegotiationpdf.tenderdate", "Tender Due On: ");
        pdfLabel.put("tenderNegotiationpdf.slno", "Sl \n No");
        pdfLabel.put("tenderNegotiationpdf.scheduleno", "SCH\nNO");
        pdfLabel.put("tenderNegotiationpdf.descofwork", "Description \n of \n Work");
        pdfLabel.put("tenderNegotiationpdf.quantity", "Quantity");
        pdfLabel.put("tenderNegotiationpdf.asPerEstimate", "As Per Estimate");
        pdfLabel.put("tenderNegotiationpdf.rate", "Rate");
        pdfLabel.put("tenderNegotiationpdf.Per", "Per");
        pdfLabel.put("tenderNegotiationpdf.amount", "Amount \n Rs.P.");
        pdfLabel.put("tenderNegotiationpdf.asPerTender", "As Per Tender");
        pdfLabel.put("tenderNegotiationpdf.rate", "Rate \n Rs.P.");
        pdfLabel.put("tenderNegotiationpdf.aftneg", "After Negotiation Vide \n Letter dated");
        pdfLabel.put("tenderNegotiationpdf.marketratedate", "Market rate date : \n");
        pdfLabel.put("tenderNegotiationpdf.tendertotal", "Total");
        pdfLabel.put("tenderNegotiationpdf.percentage", "Tender Percentage");
        pdfLabel.put("tenderNegotiationpdf.quoted.total", "Total Value [Quoted Amount]");
        pdfLabel.put("tenderNegotiationpdf.preparedby", "Prepared By:");
        pdfLabel.put("tenderNegotiationpdf.checkedby", "Checked By:");
        pdfLabel.put("tenderNegotiationpdf.approvaldetails", "Approval Details");
        pdfLabel.put("tenderNegotiationpdf.aprvalstep", "Approval Step");
        pdfLabel.put("tenderNegotiationpdf.name", "Name");
        pdfLabel.put("tenderNegotiationpdf.designation", "Designation");
        pdfLabel.put("tenderNegotiationpdf.aprvdon", "Approved on");
        pdfLabel.put("tenderNegotiationpdf.remarks", "Remarks");
        pdfLabel.put("tenderNegotiationpdf.contractorcode", "Contractor Code");
        pdfLabel.put("tenderNegotiationpdf.contractorname", "Contractor Name");
        pdfLabel.put("tenderNegotiationpdf.contractoraddress", "Contractor Address");
        return pdfLabel;
    }

    public WorksService getWorksService() {
        return worksService;
    }

    public void setWorksService(final WorksService worksService) {
        this.worksService = worksService;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(final String fileName) {
        this.fileName = fileName;
    }

    public ReportService getReportService() {
        return reportService;
    }

    public void setReportService(final ReportService reportService) {
        this.reportService = reportService;
    }

    public InputStream getTenderScrtAbsrtPDF() {
        return tenderScrtAbsrtPDF;
    }

    public void setTenderScrtAbsrtPDF(final InputStream tenderScrtAbsrtPDF) {
        this.tenderScrtAbsrtPDF = tenderScrtAbsrtPDF;
    }

}
