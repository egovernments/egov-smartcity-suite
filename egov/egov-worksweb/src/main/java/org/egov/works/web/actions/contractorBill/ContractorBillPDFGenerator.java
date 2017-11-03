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
package org.egov.works.web.actions.contractorBill;

import com.lowagie.text.Chunk;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.egov.commons.CChartOfAccounts;
import org.egov.commons.dao.ChartOfAccountsHibernateDAO;
import org.egov.infra.exception.ApplicationException;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.utils.NumberToWordConverter;
import org.egov.infra.workflow.entity.StateHistory;
import org.egov.infstr.services.PersistenceService;
import org.egov.model.bills.EgBilldetails;
import org.egov.pims.commons.DeptDesig;
import org.egov.pims.commons.Position;
import org.egov.pims.model.PersonalInformation;
import org.egov.pims.service.EmployeeServiceOld;
import org.egov.works.contractorbill.entity.ContractorBillRegister;
import org.egov.works.models.contractorBill.AssetForBill;
import org.egov.works.models.contractorBill.DeductionTypeForBill;
import org.egov.works.models.contractorBill.StatutoryDeductionsForBill;
import org.egov.works.models.measurementbook.MBForCancelledBill;
import org.egov.works.models.measurementbook.MBHeader;
import org.egov.works.services.ContractorBillService;
import org.egov.works.services.WorksService;
import org.egov.works.services.contractoradvance.ContractorAdvanceService;
import org.egov.works.utils.AbstractPDFGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ContractorBillPDFGenerator extends AbstractPDFGenerator {
    public static final String newLine = "\n";
    public static final String CONTRACTOR_PDF_ERROR = "egBillRegister.pdf.error";
    public static final String blankSpace = "   ";
    public static final String blankSpace8 = "        ";
    public static final String blankSpace15 = "               ";
    public static final String blankSpace20 = "                    ";
    public static final String tab2 = "\t \t \t \t \t \t \t \t \t \t \t \t \t \t \t \t \t \t \t \t \t \t \t \t\t \t \t \t \t \t \t \t \t \t \t \t \t \t \t \t \t \t \t \t \t \t \t \t";
    public static final String dateLabel = "contractorbill.pdf.date";
    private static final Logger logger = Logger.getLogger(ContractorBillPDFGenerator.class);
    private static final String WORKS_NETPAYABLE_CODE = "WORKS_NETPAYABLE_CODE";
    private final Map<String, String> pdfLabel;
    private final ContractorBillRegister egBillRegister;
    private final MBHeader mbHeader;
    private final List<MBHeader> mbHeaderList = new ArrayList<>();
    private final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
    private final ContractorBillService contractorBillService;
    @Autowired
    @Qualifier("persistenceService")
    private PersistenceService persistenceService;
    @Autowired
    private EmployeeServiceOld employeeService;
    private String deptName = "";
    private String contactorName = "";
    private String contractorAddress = "";
    private String billNumber = "";
    private String billGenNumber = "";
    private String billDate = "";
    private String billType = "";
    private String workDescription = "";
    private String workcommencedOn = "";
    private String workCompletedOn = "";
    private String estimateNumber = "";
    private String projectCode = "";
    private Long workOrderId;
    @Autowired
    private ChartOfAccountsHibernateDAO chartOfAccountsHibernateDAO;
    private boolean flag = false;
    private List<StatutoryDeductionsForBill> sortedStatutorySortedList;
    private List<DeductionTypeForBill> sortedStandardDeductionList;
    private List<EgBilldetails> customDeductionList;
    private List<AssetForBill> assetForBillList;
    private WorksService worksService;
    private BigDecimal advanceAdjustment = new BigDecimal(0);
    private List<BigDecimal> glcodeIdList;
    private BigDecimal netPayableAmount = BigDecimal.ZERO;
    private ContractorAdvanceService contractorAdvanceService;

    public ContractorBillPDFGenerator(final ContractorBillRegister egBillRegister, final MBHeader mbHeader,
                                      final OutputStream out,
                                      final Map<String, String> pdfLabel, final ContractorBillService contractorBillService) {
        super(out, "landscape");
        this.pdfLabel = pdfLabel;
        this.egBillRegister = egBillRegister;
        this.mbHeader = mbHeader;
        this.contractorBillService = contractorBillService;

    }

    public void generatePDF() throws ApplicationException {
        logger.debug("FA1---inside generate pdf ");
        generateDisplayData(mbHeader, egBillRegister);
        try {
            // start header Part
            final PdfPTable contractorBillMainTable = new PdfPTable(11);
            contractorBillMainTable.setWidthPercentage(100);
            contractorBillMainTable.setWidths(new float[]{1.5f, 1.5f, 1.5f, 1.5f, 1.5f, 1.5f, 1.5f, 1.5f, 1.5f, 1.5f, 1.5f});
            contractorBillMainTable.getDefaultCell().setPadding(4);
            contractorBillMainTable.getDefaultCell().setBorderWidth(1);
            createHeaderRow(contractorBillMainTable);
            createDetailsRows(contractorBillMainTable);
            document.add(contractorBillMainTable);
            document.add(spacer());

            // ---approval details for workflow
            final PdfPTable approvaldetailsTable = createApprovalDetailsTable(egBillRegister);

            if (approvaldetailsTable.getRows().size() != 1) {
                document.add(makePara("Approval Details"));
                document.add(spacer());
                document.add(approvaldetailsTable);
                document.add(spacer());
            }
            if (contractorBillMainTable.getRows().size() > 11)
                document.newPage();
            createFooter();
            // create certificate page
            document.newPage();
            createCertificate();
            document.close();
        } catch (final DocumentException e) {
            throw new ApplicationRuntimeException(CONTRACTOR_PDF_ERROR, e);
        }
    }

    protected void createCertificate() throws DocumentException {
        final Paragraph headerTextPara = new Paragraph(new Chunk(pdfLabel.get("contractorbill.pdf.contractorbill"),
                new Font(Font.TIMES_ROMAN, Font.DEFAULTSIZE, Font.BOLD)));
        headerTextPara.setAlignment(Element.ALIGN_CENTER);
        document.add(headerTextPara);
        final Paragraph certificateheaderTextPara = new Paragraph(
                new Chunk(pdfLabel.get("contractorbill.pdf.certificate"), new Font(Font.COURIER, LARGE_FONT, Font.BOLD)));
        certificateheaderTextPara.setAlignment(Element.ALIGN_CENTER);
        document.add(certificateheaderTextPara);
        document.add(spacer());
        document.add(spacer());

        document.add(makePara(pdfLabel.get("contractorbill.pdf.certificatecontent1")));
        document.add(spacer());
        document.add(spacer());

        document.add(rightPara(pdfLabel.get("contractorbill.pdf.juniorengineer") + "\t \t\t \t \t \t\t \t\t \t \t \t \t"));
        document.add(spacer());
        document.add(rightPara(pdfLabel.get(dateLabel) + "\t \t \t \t\t \t\t \t \t \t \t\t \t\t \t \t \t \t\t \t\t" +
                "\t \t\t \t\t \t \t \t \t\t \t\t\t\t \t\t \t\t"));
        document.add(spacer());

        document.add(makePara(pdfLabel.get("contractorbill.pdf.certificatecontent2")));
        document.add(spacer());
        document.add(spacer());

        document.add(rightPara(pdfLabel.get("contractorbill.pdf.exeasstengineer") + "\t \t"));
        document.add(spacer());
        document.add(rightPara(pdfLabel.get(dateLabel) + "\t \t \t \t\t \t\t \t \t \t \t\t \t\t \t \t \t \t\t \t\t" +
                "\t \t \t \t\t \t\t \t \t \t \t\t \t\t \t \t \t \t\t \t\t\t \t \t\t\t \t\t \t \t \t \t\t \t\t \t \t \t \t\t \t\t \t \t \t \t\t \t\t"));

        document.add(spacer());

        document.add(makePara(pdfLabel.get("contractorbill.pdf.certificatecontent3")));
        document.add(spacer());
        document.add(makePara(pdfLabel.get("contractorbill.pdf.certificatecontent4")));
        document.add(spacer());

        document.add(makePara(pdfLabel.get("contractorbill.pdf.certificatecontent5")));
        document.add(spacer());
        document.add(spacer());

        document.add(makePara(pdfLabel.get("contractorbill.pdf.certificatecontent6")));

        document.add(spacer());
        document.add(spacer());

        document.add(makePara(pdfLabel.get("contractorbill.pdf.juniorengineer")
                + "\t \t \t \t \t \t \t \t \t \t \t \t \t \t \t \t \t \t \t \t \t \t \t \t " +
                tab2 +
                tab2 +
                "\t \t \t \t \t \t \t \t \t \t \t \t \t \t \t \t \t \t \t \t \t \t \t \t\t \t \t \t \t \t \t \t \t \t \t" +
                pdfLabel.get("contractorbill.pdf.exeasstengineer")));
        document.add(spacer());
        document.add(
                makePara(pdfLabel.get(dateLabel) + "\t \t \t \t \t \t \t \t \t \t \t \t \t \t \t \t \t \t \t \t \t \t \t \t" +
                        tab2 +
                        tab2 +
                        "\t \t \t \t \t \t \t \t \t \t \t \t \t \t \t \t \t \t \t \t \t \t \t \t\t \t \t \t \t \t \t \t \t \t \t \t \t \t \t \t \t \t \t \t \t \t \t"
                        +
                        "\t \t \t \t \t \t \t \t " +
                        pdfLabel.get(dateLabel)));
    }

    protected void createFooter() throws DocumentException {
        document.add(makePara(
                "Received( Rs---------------) Rupees-------------------------------------------------------------------------------------------\n"
                        +
                        "only as a final payment in settlement of all demands in( Vernacular)---------------------------------------------------\n"
                        +
                        "Witness:-   1.\n" +
                        "                      2."));
        logger.debug("FC---inside generate pdf add document");
    }

    // 1---header part of code
    protected void createHeaderRow(final PdfPTable contractorBillMainTable) {
        final PdfPTable contractorBillLeftHeader = createContractorBillHeader(pdfLabel.get("contractorbill.pdf.leftheader"), 0);
        contractorBillLeftHeader.getDefaultCell().setBorderWidth(0);
        final PdfPCell contractorBillLeftHeaderCell = new PdfPCell(contractorBillLeftHeader);
        contractorBillLeftHeaderCell.setBorderWidth(0);
        contractorBillLeftHeaderCell.setColspan(4);
        contractorBillMainTable.addCell(contractorBillLeftHeaderCell);
        final PdfPTable contractorBillMainHeader = createContractorBillHeader(pdfLabel.get("contractorbill.pdf.mainheader"), 1);
        contractorBillMainHeader.getDefaultCell().setBorderWidth(0);
        final PdfPCell contractorBillMainHeaderCell = new PdfPCell(contractorBillMainHeader);
        contractorBillMainHeaderCell.setBorderWidth(0);
        contractorBillMainHeaderCell.setColspan(3);
        contractorBillMainTable.addCell(contractorBillMainHeaderCell);
        final PdfPTable contractorBillRightHeader = createContractorBillHeader(pdfLabel.get("contractorbill.pdf.rightheader"), 2);
        contractorBillMainHeader.getDefaultCell().setBorderWidth(0);
        final PdfPCell contractorBillRightHeaderCell = new PdfPCell(contractorBillRightHeader);
        contractorBillRightHeaderCell.setBorderWidth(0);
        contractorBillRightHeaderCell.setColspan(4);
        contractorBillMainTable.addCell(contractorBillRightHeaderCell);
    }

    protected PdfPTable createContractorBillHeader(final String title, final int i) {
        final PdfPTable contractorBillHeaderTable = new PdfPTable(3);
        contractorBillHeaderTable.getDefaultCell().setBorderWidth(0);
        if (i == 0) {
            contractorBillHeaderTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
            contractorBillHeaderTable.getDefaultCell().setColspan(4);
            contractorBillHeaderTable.addCell(title);
        } else if (i == 1) {
            contractorBillHeaderTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
            final Phrase headerTextPara = new Phrase(title, new Font(Font.UNDEFINED, LARGE_FONT, Font.BOLD));
            contractorBillHeaderTable.getDefaultCell().setColspan(3);
            contractorBillHeaderTable.addCell(headerTextPara);
        } else if (i == 2) {
            contractorBillHeaderTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
            contractorBillHeaderTable.getDefaultCell().setColspan(4);
            contractorBillHeaderTable.addCell(title + " " + deptName);
        }
        return contractorBillHeaderTable;
    }

    // def creatreDetailsRow(contractorBillMainTable)
    protected void createDetailsRows(final PdfPTable contractorBillMainTable) {
        createContractorRow(contractorBillMainTable);
        createWorkDescRow(contractorBillMainTable);
        createDetailsForWorkOrder(contractorBillMainTable);         // project code row
        createWorkValueLabel(contractorBillMainTable);    // value of work done row
        createWorkValueData(contractorBillMainTable);

        createDeductionTypeLabel(contractorBillMainTable);    // deductions label row
        createDeductionTypeData(contractorBillMainTable);   // deductions data row

        createNetPayable(contractorBillMainTable);
    }

    // row7 createDeductionTypeLabel
    protected void createDeductionTypeLabel(final PdfPTable contractorBillMainTable) {
        contractorBillMainTable.getDefaultCell().setBorderWidth(1);
        final PdfPTable deductionTypeTable = createDeductionTypeLabelTable();
        deductionTypeTable.getDefaultCell().setBorderWidth(1);
        final PdfPCell deductionTypeCell = new PdfPCell(deductionTypeTable);
        deductionTypeCell.setColspan(11);
        contractorBillMainTable.addCell(deductionTypeCell);
    }

    protected PdfPTable createDeductionTypeLabelTable() {
        final PdfPTable deductionTypeLabel = new PdfPTable(11);
        deductionTypeLabel.getDefaultCell().setBorderWidth(1);
        deductionTypeLabel.getDefaultCell().setColspan(7);
        deductionTypeLabel.addCell(makePara(pdfLabel.get("contractorbill.pdf.deductions")));
        deductionTypeLabel.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        deductionTypeLabel.getDefaultCell().setColspan(1);
        deductionTypeLabel.addCell("");
        deductionTypeLabel.addCell("");
        deductionTypeLabel.addCell("");
        deductionTypeLabel.addCell("");
        return deductionTypeLabel;
    }

    // row8 createDeductionTypeData
    protected void createDeductionTypeData(final PdfPTable contractorBillMainTable) {
        contractorBillMainTable.getDefaultCell().setBorderWidth(1);
        final PdfPTable createDeductionTypeDataTable = createDeductionTypeDataTable();
        createDeductionTypeDataTable.getDefaultCell().setBorderWidth(1);
        final PdfPCell createWorkValueDataCell = new PdfPCell(createDeductionTypeDataTable);
        createWorkValueDataCell.setColspan(11);
        contractorBillMainTable.addCell(createWorkValueDataCell);
    }

    protected PdfPTable createDeductionTypeDataTable() {
        final PdfPTable createcreateDeductionTypeDataTable = new PdfPTable(11);
        createcreateDeductionTypeDataTable.getDefaultCell().setBorderWidth(1);

        // statutory
        if (!sortedStatutorySortedList.isEmpty())
            for (final StatutoryDeductionsForBill egBillPayeedetail : sortedStatutorySortedList) {
                // get tot amt for dedcution for all bill for workorder till bill date
                final BigDecimal totStatutoryAmt = getTotStatoryAmountForDeduction(egBillPayeedetail);
                final String resultTotStatuAmt = getIntDecimalParts(totStatutoryAmt);
                final String[] resultTotStatuAry = resultTotStatuAmt.split(":");

                createcreateDeductionTypeDataTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
                final String resultAmt = getIntDecimalParts(egBillPayeedetail.getEgBillPayeeDtls().getCreditAmount());
                final String[] resultAry = resultAmt.split(":");
                createcreateDeductionTypeDataTable.getDefaultCell().setColspan(7);
                createcreateDeductionTypeDataTable.addCell(egBillPayeedetail.getEgBillPayeeDtls().getRecovery().getType());
                createcreateDeductionTypeDataTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
                createcreateDeductionTypeDataTable.getDefaultCell().setColspan(1);
                createcreateDeductionTypeDataTable.addCell(resultTotStatuAry[0]);// Rs. amt all bill for workorder till billdate
                createcreateDeductionTypeDataTable.addCell(resultTotStatuAry[1]);// Pa, amt all bill for workorder till billdate
                createcreateDeductionTypeDataTable.addCell(resultAry[0]); // Rs. amt for this deduction specific to bill
                createcreateDeductionTypeDataTable.addCell(resultAry[1]);// pa. amt for this deduction specific to bill
            }
        final String type = "advanceAjustment";
        if ("advanceAjustment".equalsIgnoreCase(type)) {
            final BigDecimal totAmt = getTotAmountForAdvanceAdjustment();
            final String resultTotAmt = getIntDecimalParts(totAmt);
            final String[] resultTotAry = resultTotAmt.split(":");
            createcreateDeductionTypeDataTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);

            final String resultAmt = getIntDecimalParts(advanceAdjustment);
            final String[] resultAry = resultAmt.split(":");

            createcreateDeductionTypeDataTable.getDefaultCell().setColspan(7);
            createcreateDeductionTypeDataTable.addCell("Advance adjustment ");
            createcreateDeductionTypeDataTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
            createcreateDeductionTypeDataTable.getDefaultCell().setColspan(1);

            createcreateDeductionTypeDataTable.addCell(resultTotAry[0]);
            createcreateDeductionTypeDataTable.addCell(resultTotAry[1]);

            createcreateDeductionTypeDataTable.addCell(resultAry[0]); // Rs. amt for this deduction specific to bill
            createcreateDeductionTypeDataTable.addCell(resultAry[1]);// pa. amt for this deduction specific to bill
        }

        // standard deduction
        if (!sortedStandardDeductionList.isEmpty())
            for (final DeductionTypeForBill deductionTypeForBill : sortedStandardDeductionList) {
                final BigDecimal totStandardAmt = getTotStandardAmountForDeduction(deductionTypeForBill);
                final String resultTotStandardAmt = getIntDecimalParts(totStandardAmt);
                final String[] resultTotStandardAry = resultTotStandardAmt.split(":");
                createcreateDeductionTypeDataTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
                final String resultAmt = getIntDecimalParts(deductionTypeForBill.getCreditamount());
                final String[] resultAry = resultAmt.split(":");
                createcreateDeductionTypeDataTable.getDefaultCell().setColspan(7);
                createcreateDeductionTypeDataTable.addCell(deductionTypeForBill.getDeductionType());
                createcreateDeductionTypeDataTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
                createcreateDeductionTypeDataTable.getDefaultCell().setColspan(1);
                createcreateDeductionTypeDataTable.addCell(resultTotStandardAry[0]);// Rs. amt all bill for workorder till
                // billdate
                createcreateDeductionTypeDataTable.addCell(resultTotStandardAry[1]);// Pa, amt all bill for workorder till
                // billdate
                createcreateDeductionTypeDataTable.addCell(resultAry[0]); // Rs. amt for this deduction for this bill
                createcreateDeductionTypeDataTable.addCell(resultAry[1]);// Pa. amt for this deduction for this bill
            }

        if (!customDeductionList.isEmpty())
            for (final EgBilldetails egBilldetails : customDeductionList) {
                final BigDecimal totCustomAmt = getTotStandardAmountForDeduction(egBilldetails);
                final String resultTotCustomAmt = getIntDecimalParts(totCustomAmt);
                final String[] resultTotCustomAry = resultTotCustomAmt.split(":");
                createcreateDeductionTypeDataTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
                final String resultAmt = getIntDecimalParts(egBilldetails.getCreditamount());
                final String[] resultAry = resultAmt.split(":");
                createcreateDeductionTypeDataTable.getDefaultCell().setColspan(7);
                createcreateDeductionTypeDataTable.addCell(
                        chartOfAccountsHibernateDAO.findById(Long.valueOf(egBilldetails.getGlcodeid().toString()), false)
                                .getName());
                createcreateDeductionTypeDataTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
                createcreateDeductionTypeDataTable.getDefaultCell().setColspan(1);
                createcreateDeductionTypeDataTable.addCell(resultTotCustomAry[0]);// Rs. amt all bill for workorder till billdate
                createcreateDeductionTypeDataTable.addCell(resultTotCustomAry[1]);// Pa, amt all bill for workorder till billdate
                createcreateDeductionTypeDataTable.addCell(resultAry[0]); // amt for this deduction specific to bill
                createcreateDeductionTypeDataTable.addCell(resultAry[1]);
            }

        return createcreateDeductionTypeDataTable;
    }

    // row 9th
    protected void createNetPayable(final PdfPTable contractorBillMainTable) {
        contractorBillMainTable.getDefaultCell().setBorderWidth(1);
        final PdfPTable createNetPayableTable = createNetPayableTable();
        createNetPayableTable.getDefaultCell().setBorderWidth(1);
        final PdfPCell createNetPayableCell = new PdfPCell(createNetPayableTable);
        createNetPayableCell.setColspan(11);
        contractorBillMainTable.addCell(createNetPayableCell);
    }

    protected PdfPTable createNetPayableTable() {
        final String resultAmt = getIntDecimalParts(netPayableAmount);
        final String[] resultAry = resultAmt.split(":");
        final PdfPTable createNetPayableData = new PdfPTable(11);
        createNetPayableData.getDefaultCell().setBorderWidth(1);
        createNetPayableData.getDefaultCell().setColspan(9);
        createNetPayableData.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
        createNetPayableData
                .addCell(makePara(pdfLabel.get("contractorbill.pdf.netamount") + ":\t" + getNetPayAmtInWords(), Font.UNDERLINE));
        createNetPayableData.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
        createNetPayableData.getDefaultCell().setColspan(1);
        createNetPayableData.addCell(resultAry[0]);
        createNetPayableData.addCell(resultAry[1]);
        return createNetPayableData;
    }

    // row6 createWorkValueData
    protected void createWorkValueData(final PdfPTable contractorBillMainTable) {
        contractorBillMainTable.getDefaultCell().setBorderWidth(1);
        final PdfPTable createWorkValueDataTable = createWorkValueDataTable();
        createWorkValueDataTable.getDefaultCell().setBorderWidth(1);
        final PdfPCell createWorkValueDataCell = new PdfPCell(createWorkValueDataTable);
        createWorkValueDataCell.setColspan(11);

        contractorBillMainTable.addCell(createWorkValueDataCell);
    }

    protected PdfPTable createWorkValueDataTable() {
        final PdfPTable createWorkValueData = new PdfPTable(11);
        createWorkValueData.getDefaultCell().setBorderWidth(1);
        createWorkValueData.getDefaultCell().setColspan(7);
        createWorkValueData.addCell(makePara(pdfLabel.get("contractorbill.pdf.valueofworkdone")));

        createWorkValueData.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
        createWorkValueData.getDefaultCell().setColspan(1);
        final BigDecimal totalBillAmtUptBill = contractorBillService.getTotalValueWoForUptoBillDate(egBillRegister.getBilldate(),
                workOrderId, mbHeader.getWorkOrderEstimate().getId());

        if (totalBillAmtUptBill.compareTo(BigDecimal.ZERO) > 0) {
            final String totalBillAmt = toCurrency(totalBillAmtUptBill.doubleValue());
            try {
                createWorkValueData.addCell(rightPara(blankSpace + totalBillAmt.substring(0, totalBillAmt.indexOf('.'))));
                createWorkValueData.addCell(
                        centerPara(blankSpace + totalBillAmt.substring(totalBillAmt.indexOf('.') + 1, totalBillAmt.length())));
            } catch (final StringIndexOutOfBoundsException e) {
                logger.error("----totalBillAmt has no fractional part----" + e.getMessage());
                createWorkValueData.addCell(centerPara(blankSpace + totalBillAmt));
                createWorkValueData.addCell("00");
            }

        } else {
            createWorkValueData.addCell(" ");
            createWorkValueData.addCell(" ");
        }

        BigDecimal billAmount = BigDecimal.ZERO;
        if (egBillRegister.getBillamount() != null)
            billAmount = egBillRegister.getBillamount();

        if (billAmount.compareTo(BigDecimal.ZERO) > 0) {
            final String billAmt = toCurrency(billAmount.doubleValue());
            try {
                createWorkValueData.addCell(centerPara(blankSpace + billAmt.substring(0, billAmt.indexOf('.'))));
                createWorkValueData
                        .addCell(centerPara(blankSpace + billAmt.substring(billAmt.indexOf('.') + 1, billAmt.length())));
            } catch (final StringIndexOutOfBoundsException e) {
                logger.error("---billAmt has no fractional part---" + e.getMessage());
                createWorkValueData.addCell(centerPara(blankSpace + billAmt));
                createWorkValueData.addCell("");
            }
        } else {
            createWorkValueData.addCell(" ");
            createWorkValueData.addCell(" ");
        }
        return createWorkValueData;
    }

    // row5 createWorkValueLabe
    protected void createWorkValueLabel(final PdfPTable contractorBillMainTable) {
        contractorBillMainTable.getDefaultCell().setBorderWidth(1);
        final PdfPTable WorkValueLabelTable = createWorkValueLabelTable();
        WorkValueLabelTable.getDefaultCell().setBorderWidth(1);
        final PdfPCell WorkValueLabelCell = new PdfPCell(WorkValueLabelTable);
        WorkValueLabelCell.setColspan(11);
        contractorBillMainTable.addCell(WorkValueLabelCell);

    }

    protected PdfPTable createWorkValueLabelTable() {
        final PdfPTable createWorkValueLabel = new PdfPTable(11);
        createWorkValueLabel.getDefaultCell().setBorderWidth(1);
        createWorkValueLabel.getDefaultCell().setColspan(7);
        createWorkValueLabel.addCell(" ");
        createWorkValueLabel.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        createWorkValueLabel.getDefaultCell().setColspan(2);
        createWorkValueLabel.addCell(centerPara(pdfLabel.get("contractorbill.pdf.todate") + "\n" + " Rs.       P."));
        createWorkValueLabel.addCell(centerPara(pdfLabel.get("contractorbill.pdf.lastbill") + "\n" + " Rs.       P."));
        return createWorkValueLabel;
    }

    // row3 and row4 ---createDetailForWorkOrder
    protected void createDetailsForWorkOrder(final PdfPTable contractorBillMainTable) {
        createDetailsForWorkOrderLabel(contractorBillMainTable);
        createDetailsForWorkOrderData(contractorBillMainTable);
    }

    // row3
    protected void createDetailsForWorkOrderLabel(final PdfPTable contractorBillMainTable) {
        contractorBillMainTable.getDefaultCell().setBorderWidth(1);
        final PdfPTable detailsForWorkOrderTable = createDetailsForWorkOrderLabelTable();
        detailsForWorkOrderTable.getDefaultCell().setBorderWidth(1);
        final PdfPCell detailsForWorkOrderCell = new PdfPCell(detailsForWorkOrderTable);
        detailsForWorkOrderCell.setColspan(11);
        contractorBillMainTable.addCell(detailsForWorkOrderCell);
    }

    // row4 ---createDetailsForWorkOrderData
    protected void createDetailsForWorkOrderData(final PdfPTable contractorBillMainTable) {
        contractorBillMainTable.getDefaultCell().setBorderWidth(1);
        contractorBillMainTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
        if (!mbHeaderList.isEmpty()) {
            int listLen = 0;
            final int mbLen = mbHeaderList.size();
            int assetLen = 0;

            if (flag && assetForBillList.size() > mbHeaderList.size())
                listLen = assetForBillList.size();
            else
                listLen = mbHeaderList.size();

            if (flag)
                assetLen = assetForBillList.size();

            for (int i = 0; i < listLen; i++) {
                contractorBillMainTable.getDefaultCell().setColspan(2);
                if (i == 0)
                    contractorBillMainTable.addCell(centerPara(projectCode));
                else
                    contractorBillMainTable.addCell("");

                if (flag && i < assetLen)
                    contractorBillMainTable.addCell(
                            assetForBillList.get(i).getAsset().getCode() + "-" + assetForBillList.get(i).getAsset().getName());
                else
                    contractorBillMainTable.addCell("");
                contractorBillMainTable.getDefaultCell().setColspan(1);
                if (i < mbLen) {
                    String mbRefNo = "";
                    String mbFrmPgNo = "";
                    String mbToPgNo = "";

                    if (mbHeaderList.get(i).getMbRefNo() != null)
                        mbRefNo = mbHeaderList.get(i).getMbRefNo();

                    if (mbHeaderList.get(i).getFromPageNo() != null)
                        mbFrmPgNo = mbHeaderList.get(i).getFromPageNo().toString();

                    if (mbHeaderList.get(i).getToPageNo() != null)
                        mbToPgNo = mbHeaderList.get(i).getToPageNo().toString();

                    contractorBillMainTable.addCell(centerPara("  " + mbRefNo));
                    contractorBillMainTable.addCell(centerPara(blankSpace + mbFrmPgNo));
                    contractorBillMainTable.addCell(centerPara(blankSpace + mbToPgNo));
                } else {
                    contractorBillMainTable.addCell(centerPara(blankSpace));
                    contractorBillMainTable.addCell(centerPara(blankSpace));
                    contractorBillMainTable.addCell(centerPara(blankSpace));
                }
                contractorBillMainTable.getDefaultCell().setColspan(4);

                if (i == 0)
                    contractorBillMainTable.addCell(makePara(
                            pdfLabel.get("contractorbill.pdf.estimateamt") + toCurrency(
                                    mbHeader.getWorkOrderEstimate().getEstimate().getWorkValueIncludingTaxes().getValue()),
                            Element.ALIGN_LEFT));
                else
                    contractorBillMainTable.addCell("");
            }
        }
    }

    // row3 def---createDetailForWorkOrder
    protected PdfPTable createDetailsForWorkOrderLabelTable() {
        final PdfPTable detailsForWorkOrderLabel = new PdfPTable(11);
        detailsForWorkOrderLabel.getDefaultCell().setBorderWidth(1);
        detailsForWorkOrderLabel.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        detailsForWorkOrderLabel.getDefaultCell().setColspan(2);
        detailsForWorkOrderLabel.addCell(pdfLabel.get("contractorbill.pdf.projectcode"));
        detailsForWorkOrderLabel.addCell(pdfLabel.get("contractorbill.pdf.assetcode"));
        detailsForWorkOrderLabel.getDefaultCell().setColspan(1);
        detailsForWorkOrderLabel.addCell(pdfLabel.get("contractorbill.pdf.Mbno"));
        detailsForWorkOrderLabel.getDefaultCell().setColspan(2);
        detailsForWorkOrderLabel.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);

        detailsForWorkOrderLabel.addCell(blankSpace15 + pdfLabel.get("contractorbill.pdf.pages") +
                "\n" + blankSpace8
                + pdfLabel.get("contractorbill.pdf.from") + blankSpace8 + pdfLabel.get("contractorbill.pdf.to"));
        detailsForWorkOrderLabel.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
        detailsForWorkOrderLabel.getDefaultCell().setColspan(4);
        detailsForWorkOrderLabel.addCell(pdfLabel.get("contractorbill.pdf.estimateno") + " " + estimateNumber);
        return detailsForWorkOrderLabel;
    }

    // row2 --- workorder row
    protected void createWorkDescRow(final PdfPTable contractorBillMainTable) {
        contractorBillMainTable.getDefaultCell().setBorderWidth(1);
        final PdfPTable workDescTable = createWorkDescTable();
        workDescTable.getDefaultCell().setBorderWidth(1);
        final PdfPCell workDescCell = new PdfPCell(workDescTable);
        workDescCell.setColspan(11);
        contractorBillMainTable.addCell(workDescCell);
    }

    protected PdfPTable createWorkDescTable() {
        final PdfPTable workDescTable = new PdfPTable(11);
        workDescTable.getDefaultCell().setBorderWidth(1);
        workDescTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
        workDescTable.getDefaultCell().setColspan(7);
        workDescTable.addCell(pdfLabel.get("contractorbill.pdf.workdescription") + newLine + workDescription + newLine);
        workDescTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
        workDescTable.getDefaultCell().setColspan(4);
        workDescTable.addCell(pdfLabel.get("contractorbill.pdf.workcommencedon") + workcommencedOn + newLine
                + pdfLabel.get("contractorbill.pdf.workcompleteon") + workCompletedOn + newLine);
        return workDescTable;
    }

    // row1 --- createContractorRow

    protected void createContractorRow(final PdfPTable contractorBillMainTable) {
        contractorBillMainTable.getDefaultCell().setBorderWidth(1);
        final PdfPTable contractorTable = createContractorTable();
        contractorTable.getDefaultCell().setBorderWidth(1);
        final PdfPCell contractorCell = new PdfPCell(contractorTable);
        contractorCell.setColspan(11);
        contractorBillMainTable.addCell(contractorCell);
    }

    protected PdfPTable createContractorTable() {
        final PdfPTable contractorTable = new PdfPTable(11);
        contractorTable.getDefaultCell().setBorderWidth(1);
        contractorTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
        contractorTable.getDefaultCell().setColspan(7);
        contractorTable.addCell(pdfLabel.get("contractorbill.pdf.contractoraddress") + newLine + contactorName + newLine
                + contractorAddress + newLine);
        contractorTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
        contractorTable.getDefaultCell().setColspan(4);
        contractorTable.addCell(pdfLabel.get("contractorbill.pdf.billno") + billGenNumber + newLine
                + pdfLabel.get("contractorbill.pdf.dateofbill") + billDate + newLine
                + pdfLabel.get("contractorbill.pdf.typeofbill") + billType + newLine);
        return contractorTable;
    }

    // display data generation block
    protected void generateDisplayData(final MBHeader mbHeader, final ContractorBillRegister egBillRegister)
            throws ApplicationException {
        assetForBillList = contractorBillService.getAssetForBill(egBillRegister.getId());
        if (!assetForBillList.isEmpty())
            flag = true;
        List<String> requiredStatutoryList = null;
        requiredStatutoryList = contractorBillService.getSortedDeductionsFromConfig("StatutoryDeductionKey");
        final List<StatutoryDeductionsForBill> currentStatutoryList = contractorBillService
                .getStatutoryListForBill(egBillRegister.getId());
        sortedStatutorySortedList = contractorBillService.getStatutoryDeductionSortedOrder(requiredStatutoryList,
                currentStatutoryList);
        advanceAdjustment = contractorBillService.getAdvanceAdjustmentAmountForBill(egBillRegister.getId(),
                mbHeader.getWorkOrderEstimate().getId());

        // standard deduction
        final List<String> requiredStandardList = contractorBillService.getSortedDeductionsFromConfig("StandardDeductionKey");
        getStandardDeductionList(egBillRegister.getId(), requiredStandardList);
        getCustomDeductionList(egBillRegister);

        if (mbHeader != null) {
            deptName = mbHeader.getWorkOrderEstimate().getEstimate().getUserDepartment().getName();
            contactorName = mbHeader.getWorkOrder().getContractor().getName();
            contractorAddress = mbHeader.getWorkOrder().getContractor().getCorrespondenceAddress() == null ? ""
                    : mbHeader.getWorkOrder().getContractor().getCorrespondenceAddress();
            workDescription = mbHeader.getWorkOrderEstimate().getEstimate().getDescription();
            workcommencedOn = sdf.format(mbHeader.getWorkOrder().getCreatedDate());
            workOrderId = mbHeader.getWorkOrder().getId();
            if (egBillRegister.getBillstatus().equals("CANCELLED")) {
                for (final MBForCancelledBill mbCancelBillObj : contractorBillService
                        .getMbListForCancelBill(egBillRegister.getId()))
                    if (!mbHeaderList.contains(mbCancelBillObj.getMbHeader()))
                        mbHeaderList.add(mbCancelBillObj.getMbHeader());
            } else
                for (final MBHeader mbObj : contractorBillService.getMbListForBillAndWorkordrId(workOrderId,
                        egBillRegister.getId()))
                    if (!mbHeaderList.contains(mbObj))
                        mbHeaderList.add(mbObj);

            estimateNumber = mbHeader.getWorkOrderEstimate().getEstimate().getEstimateNumber();

            if (mbHeader.getWorkOrder() != null && mbHeader.getWorkOrderEstimate().getEstimate() != null
                    && mbHeader.getWorkOrderEstimate().getEstimate().getProjectCode() != null
                    && mbHeader.getWorkOrderEstimate().getEstimate().getProjectCode().getCode() != null)
                projectCode = mbHeader.getWorkOrderEstimate().getEstimate().getProjectCode().getCode();

        }

        if (egBillRegister.getBillnumber() != null)
            billGenNumber = egBillRegister.getBillnumber();

        // partbillNo
        if (egBillRegister.getBillnumber() != null && egBillRegister.getBillSequenceNumber() != null)
            billNumber = egBillRegister.getBillSequenceNumber().toString();

        if (egBillRegister.getBilldate() != null)
            billDate = sdf.format(egBillRegister.getBilldate());

        if (egBillRegister.getBilltype() != null)
            if ("Running".equalsIgnoreCase(billType))
                billType = billNumber + "-" + egBillRegister.getBilltype();
            else
                billType = egBillRegister.getBilltype();

        if (egBillRegister.getBilldate() != null)
            billDate = sdf.format(egBillRegister.getBilldate());

        if ("Final Bill".equalsIgnoreCase(egBillRegister.getBilltype())) {
            if (mbHeader != null && mbHeader.getWorkOrderEstimate() != null
                    && mbHeader.getWorkOrderEstimate().getWorkCompletionDate() != null)
                workCompletedOn = sdf.format(mbHeader.getWorkOrderEstimate().getWorkCompletionDate());
        } else
            workCompletedOn = "in progress";
    }

    // for statutory deduction
    public void getStandardDeductionList(final Long billId, final List<String> requiredStandardList) {
        final List<DeductionTypeForBill> currentStandardDeductionList = contractorBillService.getStandardDeductionForBill(billId);
        sortedStandardDeductionList = contractorBillService.getStandardDeductionSortedOrder(requiredStandardList,
                currentStandardDeductionList);

    }

    public void getCustomDeductionList(final ContractorBillRegister egBillRegister) throws ApplicationException {
        customDeductionList = new ArrayList<>();
        glcodeIdList = new ArrayList<>();
        getStatutoryDeductionGlcode();
        getStandardDeductionGlcode();
        String advanceAdjstglCodeId = "";
        final CChartOfAccounts advanceCOA = contractorAdvanceService
                .getContractorAdvanceAccountcodeForWOE(mbHeader.getWorkOrderEstimate().getId());
        if (advanceCOA != null)
            advanceAdjstglCodeId = advanceCOA.getId().toString();
        getGlCodeForNetPayable();
        if (StringUtils.isNotBlank(advanceAdjstglCodeId))
            glcodeIdList.add(new BigDecimal(advanceAdjstglCodeId));
        customDeductionList = contractorBillService.getCustomDeductionListforglcodes(glcodeIdList, egBillRegister.getId());
    }

    public void getStatutoryDeductionGlcode() {
        if (!sortedStatutorySortedList.isEmpty())
            for (final StatutoryDeductionsForBill bpd : sortedStatutorySortedList)
                if (bpd != null && bpd.getEgBillPayeeDtls().getRecovery() != null
                        && bpd.getEgBillPayeeDtls().getRecovery().getId() != null &&
                        bpd.getEgBillPayeeDtls().getRecovery().getChartofaccounts() != null
                        && bpd.getEgBillPayeeDtls().getRecovery().getChartofaccounts().getId() != null)
                    glcodeIdList.add(new BigDecimal(bpd.getEgBillPayeeDtls().getRecovery().getChartofaccounts().getId()));
    }

    public void getStandardDeductionGlcode() {
        if (!sortedStandardDeductionList.isEmpty())
            for (final DeductionTypeForBill deductionTypeForBill : sortedStandardDeductionList)
                if (deductionTypeForBill.getCoa() != null && deductionTypeForBill.getCoa().getId() != null)
                    glcodeIdList.add(new BigDecimal(deductionTypeForBill.getCoa().getId()));
    }

    public void getGlCodeForNetPayable() throws ApplicationException {
        final List<CChartOfAccounts> coaPayableList = chartOfAccountsHibernateDAO
                .getAccountCodeByPurpose(Integer.valueOf(worksService.getWorksConfigValue(WORKS_NETPAYABLE_CODE)));
        if (coaPayableList != null)
            for (final CChartOfAccounts coa : coaPayableList)
                if (coa.getId() != null) {
                    netPayableAmount = contractorBillService.getNetPayableAmountForGlCodeId(egBillRegister.getId());
                    glcodeIdList.add(new BigDecimal(coa.getId()));
                }
    }

    protected String getIntDecimalParts(final BigDecimal totalAmount) {
        String result = "";
        if (totalAmount != null) {
            String totalAmt = toCurrency(totalAmount.doubleValue());
            try {
                String intPart = totalAmt.substring(0, totalAmt.indexOf('.'));
                String decimalPart = totalAmt.substring(totalAmt.indexOf('.') + 1, totalAmt.length());
                result = intPart + ":" + decimalPart;
            } catch (final StringIndexOutOfBoundsException e) {
                logger.error("---totalAmt has no fractional part---" + e.getMessage());
                result = totalAmt + ":" + "00";
            }

        }
        return result;
    }

    public String getNetPayAmtInWords() {
        String netPayAmtStr = "";
        try {
            netPayAmtStr = NumberToWordConverter.amountInWordsWithCircumfix(netPayableAmount);
        } catch (final Exception e) {
            logger.debug("error -----" + e);
            netPayAmtStr = "";
        }
        return netPayAmtStr;
    }

    private PdfPTable createApprovalDetailsTable(final ContractorBillRegister egBillRegister) throws DocumentException {
        try {
            final PdfPTable approvaldetailsTable = new PdfPTable(5);
            approvaldetailsTable.setWidthPercentage(100);
            approvaldetailsTable.setWidths(new float[]{2f, 1f, 1f, 1.5f, 2f});
            addRow(approvaldetailsTable, true, makePara("Approval Step"), centerPara("Name"), centerPara("Designation"),
                    centerPara("Approved on"), centerPara("Remarks"));

            List<StateHistory<Position>> history = null;
            if (egBillRegister != null && egBillRegister.getCurrentState() != null
                    && egBillRegister.getCurrentState().getHistory() != null)
                history = egBillRegister.getStateHistory();

            if (history != null) {
                Collections.reverse(history);
                for (final StateHistory ad : history)
                    displayHistory(ad, approvaldetailsTable);
            }
            return approvaldetailsTable;
        } catch (final Exception e) {
            throw new ApplicationRuntimeException("Exception occured while getting approval details " + e);
        }
    }

    public void displayHistory(final StateHistory<Position> ad, final PdfPTable approvaldetailsTable) throws Exception {
        if (!ad.getValue().equals("NEW") && !ad.getValue().equals("END")) {
            String nextAction = "";
            if (ad.getNextAction() != null)
                nextAction = ad.getNextAction();
            String state = ad.getValue();
            if (!nextAction.equalsIgnoreCase(""))
                state = ad.getValue() + " - " + nextAction;
            Long positionId = null;
            String desgName = null;
            DeptDesig deptdesig = null;
            positionId = ad.getOwnerPosition().getId();
            deptdesig = ad.getOwnerPosition().getDeptDesig();
            desgName = deptdesig.getDesignation().getName();
            final PersonalInformation emp = employeeService.getEmpForPositionAndDate(ad.getCreatedDate(),
                    Integer.parseInt(positionId.toString()));
            addRow(approvaldetailsTable, true, makePara(state), makePara(emp.getEmployeeName()), makePara(desgName),
                    makePara(getDateInFormat(ad.getCreatedDate().toString())), rightPara(ad.getComments()));
        }
    }

    private String getDateInFormat(final String date) {
        String dateInFormat = null;
        try {
            dateInFormat = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH)
                    .format(new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).parse(date));
        } catch (final Exception e) {
            throw new ApplicationRuntimeException("Exception occured while parsing date := " + date + e);
        }
        return dateInFormat;
    }

    // calculate total amt for each deduction for a workorder upto bill date
    public BigDecimal getTotAmountForAdvanceAdjustment() {
        return contractorBillService.getTotAmtForAdvanceAdjustment(egBillRegister.getBilldate(), workOrderId,
                mbHeader.getWorkOrderEstimate().getId());
    }

    public BigDecimal getTotStatoryAmountForDeduction(final StatutoryDeductionsForBill egBillPayeedetail) {
        return contractorBillService.getTotAmtForStatutory(egBillRegister.getBilldate(), workOrderId,
                egBillPayeedetail, mbHeader.getWorkOrderEstimate().getId());
    }

    public BigDecimal getTotStandardAmountForDeduction(final DeductionTypeForBill deductionTypeForBill) {
        return contractorBillService.getTotAmtForStandard(egBillRegister.getBilldate(), workOrderId,
                deductionTypeForBill, mbHeader.getWorkOrderEstimate().getId());
    }

    public BigDecimal getTotStandardAmountForDeduction(final EgBilldetails egBilldetails) {
        return contractorBillService.getTotAmtForCustom(egBillRegister.getBilldate(), workOrderId, egBilldetails,
                mbHeader.getWorkOrderEstimate().getId());
    }

    // setter and getter
    public PersistenceService getPersistenceService() {
        return persistenceService;
    }

    public void setPersistenceService(final PersistenceService persistenceService) {
        this.persistenceService = persistenceService;
    }

    public EmployeeServiceOld getEmployeeService() {
        return employeeService;
    }

    public void setEmployeeService(final EmployeeServiceOld employeeService) {
        this.employeeService = employeeService;
    }

    public void setWorksService(final WorksService worksService) {
        this.worksService = worksService;
    }

    public void setContractorAdvanceService(
            final ContractorAdvanceService contractorAdvanceService) {
        this.contractorAdvanceService = contractorAdvanceService;
    }

}
