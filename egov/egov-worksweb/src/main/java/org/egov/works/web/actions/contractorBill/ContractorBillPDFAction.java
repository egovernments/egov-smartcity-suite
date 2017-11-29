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
package org.egov.works.web.actions.contractorBill;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.exception.ApplicationException;
import org.egov.infra.web.struts.actions.BaseFormAction;
import org.egov.pims.service.EmployeeServiceOld;
import org.egov.works.abstractestimate.entity.AbstractEstimate;
import org.egov.works.contractorbill.entity.ContractorBillRegister;
import org.egov.works.models.measurementbook.MBForCancelledBill;
import org.egov.works.models.measurementbook.MBHeader;
import org.egov.works.services.ContractorBillService;
import org.egov.works.services.WorksService;
import org.egov.works.services.contractoradvance.ContractorAdvanceService;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@ParentPackage("egov")
@Result(name = BaseFormAction.SUCCESS, type = "stream", location = "egBillRegisterPDF", params = { "inputName",
        "egBillRegisterPDF", "contentType", "application/pdf", "contentDisposition", "no-cache" })
public class ContractorBillPDFAction extends BaseFormAction {

    private static final long serialVersionUID = -4416572537527288800L;
    private static final Logger logger = Logger.getLogger(ContractorBillPDFAction.class);
    private Long egbillRegisterId;
    private InputStream egBillRegisterPDF;
    @Autowired
    private EmployeeServiceOld employeeService;
    private ContractorBillService contractorBillService;
    private WorksService worksService;
    private Boundary boundary = null;
    private ContractorAdvanceService contractorAdvanceService;

    @Override
    public String execute() {
        if (egbillRegisterId != null) {
            final ContractorBillRegister egBillregister = getEgBillregister();
            MBHeader mBHeader = new MBHeader();
            MBForCancelledBill mbCancelBillObj = new MBForCancelledBill();
            if (egBillregister.getBillstatus().equals("CANCELLED")) {
                mbCancelBillObj = (MBForCancelledBill) persistenceService
                        .find("from MBForCancelledBill mbHeader where mbHeader.egBillregister.id = ?", egbillRegisterId);
                mBHeader = mbCancelBillObj.getMbHeader();
            } else
                mBHeader = (MBHeader) getPersistenceService().find("from MBHeader mbHeader where mbHeader.egBillregister.id = ?",
                        egbillRegisterId);
            final AbstractEstimate estimate = mBHeader.getWorkOrderEstimate().getEstimate();
            boundary = getTopLevelBoundary(estimate.getWard());
            final Map<String, String> pdfLabel = getPdfReportLabel();
            final ByteArrayOutputStream out = new ByteArrayOutputStream(1024 * 100);
            final ContractorBillPDFGenerator pdfGenerator = new ContractorBillPDFGenerator(egBillregister, mBHeader, out,
                    pdfLabel,
                    contractorBillService);
            pdfGenerator.setPersistenceService(getPersistenceService());
            pdfGenerator.setEmployeeService(employeeService);
            pdfGenerator.setWorksService(worksService);
            pdfGenerator.setContractorAdvanceService(contractorAdvanceService);
            try {
                pdfGenerator.generatePDF();
            } catch (final ApplicationException e) {

                logger.debug("exception " + e);
            }
            egBillRegisterPDF = new ByteArrayInputStream(out.toByteArray());
        }
        return SUCCESS;
    }

    private ContractorBillRegister getEgBillregister() {
        return (ContractorBillRegister) getPersistenceService().find("from ContractorBillRegister egBillregister where id = ?",
                egbillRegisterId);
    }

    protected Boundary getTopLevelBoundary(final Boundary boundary) {
        Boundary b = boundary;
        while (b != null && b.getParent() != null)
            b = b.getParent();
        return b;
    }

    public void setEgbillRegisterId(final Long egbillRegisterId) {
        this.egbillRegisterId = egbillRegisterId;
    }

    public InputStream getEgBillRegisterPDF() {
        return egBillRegisterPDF;
    }

    public void setEmployeeService(final EmployeeServiceOld employeeService) {
        this.employeeService = employeeService;
    }

    public void setContractorBillService(final ContractorBillService contractorBillService) {
        this.contractorBillService = contractorBillService;
    }

    public Map<String, String> getPdfReportLabel() {
        final Map<String, String> pdfLabel = new HashMap<String, String>();
        pdfLabel.put("contractorbill.pdf.leftheader", "Form No.CON 51");
        pdfLabel.put("contractorbill.pdf.mainheader", boundary == null ? "" : boundary.getName() + "\n   Contractor Bill");
        pdfLabel.put("contractorbill.pdf.rightheader", "User Department: ");

        pdfLabel.put("contractorbill.pdf.contractoraddress", "Contractor Name and Address:- ");
        pdfLabel.put("contractorbill.pdf.billno", "Bill number: ");
        pdfLabel.put("contractorbill.pdf.dateofbill", "Date of Bill Creation: ");
        pdfLabel.put("contractorbill.pdf.typeofbill", "Part / Final Bill: ");

        pdfLabel.put("contractorbill.pdf.workdescription", "Work Description: ");
        pdfLabel.put("contractorbill.pdf.workcommencedon", "Work Commenced on: ");
        pdfLabel.put("contractorbill.pdf.workcompleteon", "Work Completed on: ");

        pdfLabel.put("contractorbill.pdf.projectcode", "Project Code");
        pdfLabel.put("contractorbill.pdf.assetcode", "Asset Code and Description");
        pdfLabel.put("contractorbill.pdf.Mbno", "M Book \n No");
        pdfLabel.put("contractorbill.pdf.pages", "Pages");
        pdfLabel.put("contractorbill.pdf.from", "From");
        pdfLabel.put("contractorbill.pdf.to", "To");
        pdfLabel.put("contractorbill.pdf.estimateno", "Estimate Number: ");
        pdfLabel.put("contractorbill.pdf.estimateamt", "Estimate Amount: ");
        pdfLabel.put("contractorbill.pdf.todate", "To Date ");
        pdfLabel.put("contractorbill.pdf.lastbill", "Since Last Bill ");

        pdfLabel.put("contractorbill.pdf.valueofworkdone", "Value of Work Done ");
        pdfLabel.put("contractorbill.pdf.netamount", "Net amount payable (Rupees (in words)) ");
        // approval details
        pdfLabel.put("contractorbill.pdf.preparedby", "Approved By:");
        pdfLabel.put("contractorbill.pdf.checkedby", "Checked By:");
        pdfLabel.put("contractorbill.pdf.approvaldetails", "Approval Details");
        pdfLabel.put("contractorbill.pdf.aprvalstep", "Approval Step");
        pdfLabel.put("contractorbill.pdf.name", "Name");
        pdfLabel.put("contractorbill.pdf.designation", "Designation");
        pdfLabel.put("contractorbill.pdf.aprvdon", "Approved On");
        pdfLabel.put("contractorbill.pdf.remarks", "Remarks");
        pdfLabel.put("contractorbill.pdf.deductions", "Deductions");

        pdfLabel.put("contractorbill.pdf.contractorbill", "CONTRACTOR BILL");
        pdfLabel.put("contractorbill.pdf.certificate", "CERTIFICATE");
        pdfLabel.put("contractorbill.pdf.certificatecontent1",
                "1. Certified that the claim is correct, that necessary measurments have been made by me on \n" +
                        "_________________________________ and the work has been satisfactorily performed vide pages \n" +
                        "__________________________________ Measurement Book No.  __________________________________");

        pdfLabel.put("contractorbill.pdf.juniorengineer", "Junior Engineer");
        pdfLabel.put("contractorbill.pdf.date", "Date");

        pdfLabel.put("contractorbill.pdf.certificatecontent2",
                "2. Certified that the work was / materials were duly check measured by me \n" +
                        "on _______________________________\n");

        pdfLabel.put("contractorbill.pdf.certificatecontent3",
                "3. Certified that the work has been completed, in accordance with the plan and estimate in a \n" +
                        "substantial and satisfactory manner");

        pdfLabel.put("contractorbill.pdf.certificatecontent4",
                "4. Certified that the contractor has employed Technical Assistant as required in the Agreement");
        pdfLabel.put("contractorbill.pdf.certificatecontent5",
                "5. Certified that the debris has been removed and that the carriage way work and water table has " +
                        "completed");
        pdfLabel.put("contractorbill.pdf.certificatecontent6",
                "The Certificate mentioned against Sl.No. _________________________________________ are \n" +
                        "applicable to this bill and the certificates mentioned against Sl.No._________________________________________________ \n"
                        +
                        "_______________________________  are deleted");
        pdfLabel.put("contractorbill.pdf.exeasstengineer", "Exe. Engineer /Asst. Exe. Engineer");
        return pdfLabel;
    }

    public void setWorksService(final WorksService worksService) {
        this.worksService = worksService;
    }

    @Override
    public Object getModel() {
        return null;
    }

    public void setContractorAdvanceService(
            final ContractorAdvanceService contractorAdvanceService) {
        this.contractorAdvanceService = contractorAdvanceService;
    }

}