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
package org.egov.works.web.actions.estimate;

import com.lowagie.text.Chunk;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.HeaderFooter;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.egov.commons.Accountdetailtype;
import org.egov.commons.CFinancialYear;
import org.egov.dao.budget.BudgetDetailsDAO;
import org.egov.eis.entity.Employee;
import org.egov.eis.service.AssignmentService;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.utils.DateUtils;
import org.egov.infra.workflow.entity.StateHistory;
import org.egov.infstr.services.PersistenceService;
import org.egov.model.budget.BudgetUsage;
import org.egov.pims.commons.DeptDesig;
import org.egov.pims.commons.Position;
import org.egov.works.abstractestimate.entity.AbstractEstimate;
import org.egov.works.abstractestimate.entity.AbstractEstimateAppropriation;
import org.egov.works.abstractestimate.entity.Activity;
import org.egov.works.abstractestimate.entity.FinancialDetail;
import org.egov.works.abstractestimate.entity.MultiYearEstimate;
import org.egov.works.abstractestimate.entity.OverheadValue;
import org.egov.works.services.AbstractEstimateService;
import org.egov.works.services.DepositWorksUsageService;
import org.egov.works.services.WorksService;
import org.egov.works.utils.AbstractPDFGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class EstimatePDFGenerator extends AbstractPDFGenerator {
    private static final Logger logger = Logger.getLogger(EstimatePDFGenerator.class);
    private static final String space1 = "\t  \t  \t  \t \t \t  \t  \t  \t \t \t \t"
            + "\t  \t  \t  \t \t \t  \t  \t  \t \t \t \t";
    private static final String MODULE_NAME = "Works";
    private static final String KEY_NAME = "SKIP_BUDGET_CHECK";
    private final AbstractEstimate estimate;
    private List<AbstractEstimateAppropriation> abstractEstimateAppropriationList = new LinkedList<>();
    @Autowired
    @Qualifier("persistenceService")
    private PersistenceService persistenceService;
    private PersistenceService<AbstractEstimateAppropriation, Long> estimateAppropriationService;
    private String headerText;
    @Autowired
    private AssignmentService assignmentService;
    private BudgetDetailsDAO budgetDetailsDAO;
    private AbstractEstimateService abstractEstimateService;
    private WorksService worksService;
    private String appValue = "";
    private DepositWorksUsageService depositWorksUsageService;
    private boolean skipBudget = false;
    private List<StateHistory> history = null;
    private boolean shouldShowApprovalNumber;

    public EstimatePDFGenerator(final AbstractEstimate estimate, final String headerText, final OutputStream out) {
        super(out, "portrait");
        this.estimate = estimate;
        this.headerText = headerText;
    }

    public void generatePDF() {
        try {
            final Paragraph headerTextPara = new Paragraph(new Chunk(headerText, new Font(Font.UNDEFINED, LARGE_FONT,
                    Font.BOLD)));
            String projectCode;
            final String oldEstNo = "";
            HeaderFooter hf;
            headerTextPara.setAlignment(Element.ALIGN_CENTER);
            document.add(headerTextPara);
            document.add(makePara("Executing Department:" + estimate.getExecutingDepartment().getName(),
                    Element.ALIGN_LEFT));
            if (estimate.getUserDepartment() != null)
                document.add(makePara("User Department:" + estimate.getUserDepartment().getName(), Element.ALIGN_LEFT));

            final CFinancialYear estimateFinancialYear = estimate.getMultiYearEstimates().get(0).getFinancialYear();
            addZoneYearHeader(estimate, estimateFinancialYear);

            document.add(makePara("Name of Work: " + estimate.getName(), Element.ALIGN_LEFT));
            document.add(makePara("Description: " + estimate.getDescription(), Element.ALIGN_LEFT));

            if (estimate.getProjectCode() != null) {
                projectCode = "Project Code : " + estimate.getProjectCode().getCode();
                document.add(makePara(projectCode, Element.ALIGN_LEFT));
                hf = new HeaderFooter(new Phrase("\t  \t  \t  \t \t \t  \t  \t  \t \t \t  \t  \t  \t \t"
                        + "\t  \t  \t  \t \t\t  \t  \t  \t \t\t  \t  \t  \t \t\t  \t  \t  "
                        + "\t  \t  \t  \t \t\t  \t  \t  \t \t"
                        + headerText
                        .concat("\n")
                        .concat("\t  \t  \t  \t \t \t  \t  \t  \t \t"
                                + "\t  \t  \t  \t \t\t  \t  \t  \t \t\t  \t  \t  \t \t\t  \t  \t  "
                                + "\t  \t  \t  \t \t\t  \t  \t  \t \t ABSTRACT ESTIMATE")
                        .concat("\n\n")
                        .concat("Name of Work: " + estimate.getName()).concat("\n")
                        .concat("Description: " + estimate.getDescription()).concat("\n")
                        .concat("Estimate Number: " + estimate.getEstimateNumber()).concat(oldEstNo)
                        .concat("\n").concat(projectCode)),
                        false);
            } else
                hf = new HeaderFooter(new Phrase("\t  \t  \t  \t \t \t  \t  \t  \t \t \t  \t  \t  \t \t"
                        + "\t  \t  \t  \t \t\t  \t  \t  \t \t\t  \t  \t  \t \t\t  \t  \t  "
                        + "\t  \t  \t  \t \t\t  \t  \t  \t \t"
                        + headerText
                        .concat("\n")
                        .concat("\t  \t  \t  \t \t \t  \t  \t  \t \t"
                                + "\t  \t  \t  \t \t\t  \t  \t  \t \t\t  \t  \t  \t \t\t  \t  \t  "
                                + "\t  \t  \t  \t \t\t  \t  \t  \t \t ABSTRACT ESTIMATE")
                        .concat("\n\n")
                        .concat("Name of Work: " + estimate.getName()).concat("\n")
                        .concat("Description: " + estimate.getDescription()).concat("\n")
                        .concat("Estimate Number: " + estimate.getEstimateNumber()).concat(oldEstNo)),
                        false);

            hf.disableBorderSide(Rectangle.TOP);
            hf.disableBorderSide(Rectangle.BOTTOM);
            hf.setLeft(Element.ALIGN_LEFT);
            document.setHeader(hf);
            final PdfPTable overheadsTable = createOverheadsTable(estimate);
            document.add(spacer());
            document.add(overheadsTable);
            document.add(spacer());
            final PdfPTable multiyearTable = createMultiYearTable(estimate);
            document.add(makePara("Year-wise Estimate"));
            document.add(spacer());
            document.add(multiyearTable);
            document.add(spacer());
            document.add(makePara("Estimate Created By: " + estimate.getCreatedBy().getName()));
            document.add(spacer());
            document.add(spacer());
            document.add(makePara("Checked By: "));
            document.newPage();
            addZoneYearHeaderWithOutEstimateNo(estimate, estimateFinancialYear);
            document.add(createActivitiesTable(estimate));
            document.add(spacer());

            final PdfPTable approvaldetailsTable = createApprovalDetailsTable(estimate);
            // TODO:Fixme - commented final out workflow history final details since ordering final of approval is final not
            // getting final listed properly
            /*
             * if (approvaldetailsTable.getRows().size() != 1) { document.add(makePara("Approval Details"));
             * document.add(spacer()); document.add(approvaldetailsTable); }
             */

            final String appropriationNumber = abstractEstimateService.getLatestEstimateAppropriationNumber(estimate);

            if (isSkipBudgetCheck()) {
                final PdfPTable depositWorksAppropriationTable = createDepositAppropriationTable(estimate, appropriationNumber);
                if (depositWorksAppropriationTable.getRows().size() != 1)
                    if (appropriationNumber != null) {
                        document.newPage();
                        document.add(spacer());
                        document.add(makePara("Deposit Code Appropriation Details"));
                        document.add(spacer());
                        document.add(depositWorksAppropriationTable);
                    }
            } else {
                final PdfPTable BudgetaryAppropriationTable = createBudgetaryAppropriationTable(estimate, appropriationNumber);
                final String estimateNumber = estimate.getEstimateNumber();
                if (BudgetaryAppropriationTable.getRows().size() != 1)
                    if (!getBudgetDetailUsage(estimateNumber).isEmpty() && appropriationNumber != null) {
                        document.newPage();
                        document.add(spacer());
                        document.add(makePara("Budgetary Appropriation"));
                        document.add(spacer());
                        document.add(BudgetaryAppropriationTable);
                    }
            }

            document.newPage();
            document.add(spacer());
            document.add(makePara(
                    "EXECUTIVE ENGINEER'S OFFICE,  ZONE.......................................................................",
                    Element.ALIGN_LEFT));
            document.add(spacer());
            document.add(makePara(
                    "Est No.                                                Unit:                                                 Dept.",
                    Element.ALIGN_LEFT));
            document.add(spacer());
            final Paragraph budgetheadTextPara = new Paragraph(new Chunk("BUDGET HEAD", new Font(Font.UNDEFINED,
                    LARGE_FONT, Font.BOLD)));
            budgetheadTextPara.setAlignment(Element.ALIGN_CENTER);
            document.add(budgetheadTextPara);
            document.add(spacer());
            document.add(makePara("____________________________________________________________________________",
                    Element.ALIGN_LEFT));
            document.add(makePara("Rs.                                            ", Element.ALIGN_LEFT));
            document.add(makePara("____________________________________________________________________________",
                    Element.ALIGN_LEFT));
            document.add(makePara("Works:                                          ", Element.ALIGN_LEFT));
            document.add(spacer());
            document.add(spacer());
            final Paragraph memoTextPara = new Paragraph(new Chunk("MEMO", new Font(Font.UNDEFINED, LARGE_FONT,
                    Font.BOLD)));
            memoTextPara.setAlignment(Element.ALIGN_CENTER);
            document.add(memoTextPara);
            document.add(makePara("Budget Grant                               ", Element.ALIGN_LEFT));
            document.add(makePara("Amount Appropriated:__________________________________________________________",
                    Element.ALIGN_LEFT));
            document.add(makePara("Balance on Hand:                                ", Element.ALIGN_LEFT));
            document.add(makePara("Amount of this estimate_________________________________________________________",
                    Element.ALIGN_LEFT));
            document.add(makePara("Balance forward_______________________________________________________________",
                    Element.ALIGN_LEFT));
            document.add(makePara("Submitted for favour of sanction                           ", Element.ALIGN_LEFT));
            document.add(spacer());
            document.add(spacer());
            document.add(makePara("A.E.E.Unit " + space1
                    + "\t  \t  \t  \t \t \t  \t  \t  \t \t \t \t \t \t \t \t \t \t"
                    + "Exe.Eng.Zone.....................", Element.ALIGN_LEFT));
            document.add(spacer());
            document.add(makePara("Sanctioned", Element.ALIGN_CENTER));
            document.add(spacer());
            document.add(spacer());
            document.add(makePara("DATE:" + space1
                    + "\t  \t  \t  \t \t \t  \t  \t  \t \t \t \t\t  \t  \t  \t \t \t  \t  \t  \t \t \t \t"
                    + "Asst.Commissioner Zone...............", Element.ALIGN_LEFT));
            document.add(spacer());
            document.add(makePara(space1
                    + "\t  \t  \t  \t \t \t  \t  \t  \t \t \t \t\t  \t  \t  \t \t \t  \t  \t  \t \t \t \t"
                    + "APPROPRIATION No.", Element.ALIGN_LEFT));
            document.add(makePara(space1 + "\t  \t  \t  \t \t \t  \t  \t  \t \t \t \t"
                    + "\t  \t  \t  \t \t \t  \t  \t  \t \t \t \t \t \t \t" + "Date:", Element.ALIGN_LEFT));

            // WF for signature -----
            if (approvaldetailsTable.getRows().size() != 1)
                if (shouldShowApprovalNumber) {
                    document.resetHeader();
                    document.newPage();
                    document.add(makePara("\t  \t  \t  \t \t \t  \t  \t  \t \t \t  \t  \t  \t \t"
                            + "\t  \t  \t  \t \t\t  \t  \t  \t \t\t  \t  \t  \t \t\t  \t  \t  "
                            + "\t  \t  \t  \t \t\t  \t  \t  \t \t"
                            + headerText
                            .concat("\n")
                            .concat("\t  \t  \t  \t \t \t  \t  \t  \t \t"
                                    + "\t  \t  \t  \t \t\t  \t  \t  \t \t\t  \t  \t  \t \t\t  \t  \t  "
                                    + "\t  \t  \t  \t \t\t  \t  \t  \t \t ABSTRACT ESTIMATE")
                            .concat("\n\n")));
                    document.add(makePara("File Current Number :" + space1
                            + "\t  \t  \t  \t \t \t  \t  \t  \t \t \t \t \t   " + "Date: \t \t", Element.ALIGN_LEFT));
                    document.add(makePara(space1
                            + "\t  \t  \t  \t \t \t  \t  \t  \t \t \t \t\t  \t  \t  \t \t \t \t \t  \t  \t  \t "
                            + "Department : ", Element.ALIGN_LEFT));
                    document.add(spacer());
                    final Paragraph headingPara1 = new Paragraph(new Chunk(
                            "NOTE FOR ADMINISTRATIVE SANCTION AS PER RULE 78 OF ", new Font(Font.UNDEFINED, LARGE_FONT,
                            Font.BOLD)));
                    headingPara1.setAlignment(Element.ALIGN_CENTER);
                    document.add(headingPara1);
                    final Paragraph headingPara2 = new Paragraph(new Chunk("MCMC ACT 1919 ", new Font(Font.UNDEFINED,
                            LARGE_FONT, Font.BOLD)));
                    headingPara2.setAlignment(Element.ALIGN_CENTER);
                    document.add(headingPara2);

                    document.add(spacer());
                    final PdfPTable estimateDetailsTable1 = createEstimateDetailsTable1(estimate);
                    document.add(estimateDetailsTable1);
                    final PdfPTable budgetDetailsTableFourCols = createBudgetDetailsForEstimateTable(estimate);
                    document.add(budgetDetailsTableFourCols);
                    final PdfPTable estimateDetailsTable2 = createBalanceAmtCalculationTable(estimate);
                    document.add(estimateDetailsTable2);
                    document.add(spacer());
                    final Paragraph endTextPara = new Paragraph(new Chunk("** END **", new Font(Font.UNDEFINED,
                            LARGE_FONT, Font.BOLD)));
                    endTextPara.setAlignment(Element.ALIGN_CENTER);
                    document.add(endTextPara);
                }

            document.close();
        } catch (final DocumentException e) {
            throw new ApplicationRuntimeException("estimate.pdf.error", e);
        }
    }

    private PdfPTable createMultiYearTable(final AbstractEstimate estimate) throws DocumentException {
        final PdfPTable multiyearTable = new PdfPTable(3);
        multiyearTable.setWidthPercentage(100);
        multiyearTable.setWidths(new float[]{1f, 2f, 2f});
        addRow(multiyearTable, true, makePara("Sl No"), centerPara("Year"), centerPara("Percentage"));
        int i = 0;
        for (final MultiYearEstimate year : estimate.getMultiYearEstimates())
            addRow(multiyearTable, true, makePara(++i),
                    makePara(year.getFinancialYear().getFinYearRange(), Element.ALIGN_CENTER),
                    rightPara(year.getPercentage()));
        return multiyearTable;
    }

    private PdfPTable createOverheadsTable(final AbstractEstimate estimate) throws DocumentException {
        final PdfPTable overheadsTable = new PdfPTable(3);
        overheadsTable.setWidthPercentage(100);
        overheadsTable.setWidths(new float[]{1f, 4f, 2f});
        addRow(overheadsTable, true, makePara("Sl No"), centerPara("Description"), centerPara("Amount"));
        addRow(overheadsTable, true, makePara("1"), makePara("Work Value"),
                rightPara(toCurrency(estimate.getWorkValue())));
        int i = 1;
        for (final OverheadValue oh : estimate.getOverheadValues())
            addRow(overheadsTable, true, makePara(++i), makePara(getOverheadDescription(oh)),
                    rightPara(toCurrency(oh.getAmount())));
        addRow(overheadsTable, true, centerPara(""), makePara("TOTAL"),
                rightPara(toCurrency(estimate.getTotalAmount())));
        return overheadsTable;
    }

    /*
     * 1 Deposit Code Appropriation number,2 Deposit Code,3 Account Code,4 Function Center,5 Department,6 Total Deposit Amount, 7
     * Amount Appropriated so far= totalUtilizedAmt- estimate amt 8 Balance on hand = Amount Appropriated so far - estimat amt 9
     * Amount of the Estimate = estimate amt 10 Balance after Appropriation of this estimate =balOnHand - ESTIMATE AMT
     */
    private PdfPTable createDepositAppropriationTable(final AbstractEstimate estimate, final String appropriationNumber)
            throws DocumentException {
        int isReject = -1;
        depositWorksUsageService = abstractEstimateService.getDepositWorksUsageService();
        BigDecimal totalUtilizedAmt;
        BigDecimal amtAppropriatedsofar;
        BigDecimal totalDepositAmt;
        BigDecimal balOnHand;
        BigDecimal balanceAvailable;
        BigDecimal amtAppropriated;
        final Accountdetailtype accountdetailtype = worksService.getAccountdetailtypeByName("DEPOSITCODE");
        estimateAppropriationService = abstractEstimateService.getEstimateAppropriationService();
        abstractEstimateAppropriationList = estimateAppropriationService
                .findAllBy(
                        "from AbstractEstimateAppropriation aea where aea.abstractEstimate.id=? and aea.depositWorksUsage.id is not null order by id, aea.depositWorksUsage.financialYearId asc",
                        estimate.getId());
        final PdfPTable depositWorksAppropriationTable = new PdfPTable(2);
        depositWorksAppropriationTable.setWidthPercentage(100);
        depositWorksAppropriationTable.setWidths(new float[]{2f, 8f});

        if (appropriationNumber != null && appropriationNumber.toUpperCase().contains("BC"))
            isReject = 1;

        if (appropriationNumber != null && estimate.getTotalAmount() != null && isReject == -1) {
            addRow(depositWorksAppropriationTable, true, centerPara("Deposit Code"), centerPara(estimate
                    .getDepositCode().getCode()));
            addRow(depositWorksAppropriationTable, true, centerPara("Account Code"), centerPara(estimate
                    .getFinancialDetails().get(0).getCoa().getGlcode()
                    + "-" + estimate.getFinancialDetails().get(0).getCoa().getName()));
            addRow(depositWorksAppropriationTable, true, makePara("Function Center"), centerPara(estimate
                    .getFinancialDetails().get(0).getFunction().getName()));
            addRow(depositWorksAppropriationTable, true, makePara("Department"), centerPara(estimate
                    .getUserDepartment().getName()));
            addRow(depositWorksAppropriationTable, true, makePara("Amount of the Estimate "),
                    rightPara(toCurrency(estimate.getTotalAmount())));
            final PdfPTable appropriationDetailTable = new PdfPTable(6);
            addRow(appropriationDetailTable, true, makePara(7f, "Appropriation Number"),
                    makePara(7f, "Total Deposit Amount"), makePara(7f, "Amount Appropriated so far"),
                    makePara(7f, "Amount Appropriated"), makePara(7f, "Balance on Hand"),
                    makePara(7f, "Balance After Appropriation"));
            for (final AbstractEstimateAppropriation abstractEstimateAppropriation : abstractEstimateAppropriationList)
                if (abstractEstimateAppropriation.getDepositWorksUsage().getConsumedAmount().doubleValue() != 0) {
                    totalDepositAmt = depositWorksUsageService.getTotalDepositWorksAmount(estimate.getDepositCode()
                                    .getFund(), abstractEstimateAppropriation.getAbstractEstimate().getFinancialDetails()
                                    .get(0).getCoa(),
                            accountdetailtype, estimate.getDepositCode().getId(),
                            abstractEstimateAppropriation.getDepositWorksUsage().getAppropriationDate());
                    totalUtilizedAmt = depositWorksUsageService.getTotalUtilizedAmountForDepositWorks(
                            abstractEstimateAppropriation.getAbstractEstimate().getFinancialDetails().get(0),
                            abstractEstimateAppropriation.getDepositWorksUsage().getCreatedDate());
                    if (totalUtilizedAmt == null)
                        totalUtilizedAmt = BigDecimal.ZERO;
                    amtAppropriatedsofar = totalUtilizedAmt.subtract(abstractEstimateAppropriation
                            .getDepositWorksUsage().getConsumedAmount());
                    balOnHand = totalDepositAmt.subtract(amtAppropriatedsofar);
                    amtAppropriated = abstractEstimateAppropriation.getDepositWorksUsage().getConsumedAmount();
                    balanceAvailable = BigDecimal.valueOf(totalDepositAmt.doubleValue() - totalUtilizedAmt.doubleValue());
                    addRow(appropriationDetailTable,
                            true,
                            makePara(7f, abstractEstimateAppropriation.getDepositWorksUsage().getAppropriationNumber()),
                            rightPara(7f, toCurrency(totalDepositAmt.doubleValue())),
                            rightPara(7f, toCurrency(amtAppropriatedsofar.doubleValue())),
                            rightPara(7f, toCurrency(amtAppropriated.doubleValue())),
                            rightPara(7f, toCurrency(balOnHand.doubleValue())),
                            rightPara(7f, toCurrency(balanceAvailable.doubleValue())));
                }

            final PdfPCell appDetailpdfCell = new PdfPCell(appropriationDetailTable);
            appDetailpdfCell.setBorderWidth(0);

            final PdfPCell appDetailRightHeader = new PdfPCell(makePara("Financail Year Wise Appropriation Details"));
            appDetailRightHeader.setVerticalAlignment(Element.ALIGN_MIDDLE);

            depositWorksAppropriationTable.addCell(appDetailRightHeader);
            appropriationDetailTable.setWidthPercentage(100);
            depositWorksAppropriationTable.addCell(appDetailpdfCell);
        }
        return depositWorksAppropriationTable;
    }

    /*
     * 1 Budget head,2 Function Center,3 Department,4 Total grant, 6 Balance on hand = Budget Available - estimat amt 5 Amount
     * Appropriated= oneFifthTimesTotGrant-balOnHand 7 Amount of the Estimate = estimate amt 8 Balance after Appropriation
     * =balOnHand - ESTIMATE AMT
     */
    private PdfPTable createBudgetaryAppropriationTable(final AbstractEstimate estimate, final String appropriationNumber)
            throws DocumentException {
        int isReject = -1;
        final List<FinancialDetail> financialdetails = estimate.getFinancialDetails();
        BigDecimal totalGrant;
        BigDecimal budgetAvailable;
        BigDecimal balOnHand;
        BigDecimal amtAppropriated;
        BigDecimal totGrantafterMultiFactor = BigDecimal.ZERO;

        estimateAppropriationService = abstractEstimateService.getEstimateAppropriationService();
        abstractEstimateAppropriationList = estimateAppropriationService
                .findAllBy(
                        "from AbstractEstimateAppropriation aea where aea.abstractEstimate.id=? and aea.budgetUsage.id is not null order by aea.budgetUsage.id,aea.budgetUsage.financialYearId asc",
                        estimate.getId());

        final PdfPTable budgetaryAppropriationTable = new PdfPTable(1);
        budgetaryAppropriationTable.setWidthPercentage(100);
        budgetaryAppropriationTable.setWidths(new float[]{8f});
        if (appropriationNumber != null && appropriationNumber.toUpperCase().contains("BC"))
            isReject = 1;

        if (appropriationNumber != null && estimate.getTotalAmount() != null && isReject == -1)
            for (final FinancialDetail financialDetail : financialdetails)
                if (financialDetail.getBudgetGroup() != null) {
                    addRow(budgetaryAppropriationTable, true, centerPara("Budget Head"), centerPara(financialDetail
                            .getBudgetGroup().getName()));
                    addRow(budgetaryAppropriationTable, true, makePara("Function Center"), centerPara(financialDetail
                            .getFunction().getName()));
                    addRow(budgetaryAppropriationTable, true, makePara("Amount of the Estimate "),
                            rightPara(toCurrency(estimate.getTotalAmount())));
                }
        final PdfPTable appropriationDetailTable = new PdfPTable(8);
        int count = 0;
        for (final AbstractEstimateAppropriation abstractEstimateAppropriation : abstractEstimateAppropriationList)
            if (abstractEstimateAppropriation.getBudgetUsage().getConsumedAmount() != 0) {
                final Department dept = getDeptFromBudgtAppropriationNo(abstractEstimateAppropriation.getBudgetUsage()
                        .getAppropriationnumber());
                totalGrant = abstractEstimateService.getTotalGrantForYearAsOnDate(financialdetails.get(0),
                        abstractEstimateAppropriation.getBudgetUsage().getFinancialYearId().longValue(), Integer
                                .parseInt(dept.getId().toString()),
                        abstractEstimateAppropriation.getBudgetUsage()
                                .getUpdatedTime());
                final BigDecimal planningBudgetPerc = abstractEstimateService.getPlanningBudgetPercentage(
                        financialdetails.get(0), abstractEstimateAppropriation.getBudgetUsage().getFinancialYearId()
                                .longValue(),
                        Integer.parseInt(dept.getId().toString()));
                if (planningBudgetPerc != null && planningBudgetPerc.compareTo(BigDecimal.ZERO) != 0) {
                    totGrantafterMultiFactor = totalGrant.multiply(planningBudgetPerc.divide(new BigDecimal(100)));
                    appValue = planningBudgetPerc.divide(new BigDecimal(100)).toString();
                }
                budgetAvailable = abstractEstimateAppropriation.getBalanceAvailable();
                balOnHand = budgetAvailable.add(new BigDecimal(abstractEstimateAppropriation.getBudgetUsage()
                        .getConsumedAmount()));
                amtAppropriated = totGrantafterMultiFactor.subtract(balOnHand);
                // Print only for the first time
                if (count == 0) {
                    addRow(budgetaryAppropriationTable, false, makePara(""),
                            centerPara("Financial Year Wise Appropriation Details "));
                    addRow(appropriationDetailTable, true, makePara(8f, "Department"),
                            makePara(8f, "Appropriation Number"), makePara(8f, "Total Grant"),
                            makePara(8f, appValue + " Times Total Grant"), makePara(8f, "Amount Appropriated so far"),
                            makePara(8f, "Amount Appropriated"), makePara(8f, "Balance on Hand"),
                            makePara(8f, "Balance After Appropriation"));
                }
                addRow(appropriationDetailTable, true, rightPara(8f, dept.getName()),
                        makePara(8f, abstractEstimateAppropriation.getBudgetUsage().getAppropriationnumber()),
                        rightPara(8f, toCurrency(totalGrant.doubleValue())),
                        rightPara(8f, toCurrency(totGrantafterMultiFactor.doubleValue())),
                        rightPara(8f, toCurrency(amtAppropriated.doubleValue())),
                        rightPara(8f, toCurrency(abstractEstimateAppropriation.getBudgetUsage().getConsumedAmount())),
                        rightPara(8f, toCurrency(balOnHand.doubleValue())),
                        rightPara(8f, toCurrency(budgetAvailable.doubleValue())));
                count++;
            }

        final PdfPCell appDetailpdfCell = new PdfPCell(appropriationDetailTable);
        appropriationDetailTable.setWidthPercentage(100);
        budgetaryAppropriationTable.addCell(appDetailpdfCell);
        return budgetaryAppropriationTable;
    }

    private Department getDeptFromBudgtAppropriationNo(final String number) {
        if (StringUtils.isBlank(number)) {
            logger.error("Empty Ban Number");
            throw new ApplicationRuntimeException("Exception in getDeptFromBudgtAppropriationNo ");
        } else {
            final String[] strArr = number.split("/");
            if (strArr == null || strArr.length == 0) {
                logger.error("Department prefix not present in ban no--" + number);
                throw new ApplicationRuntimeException("Exception in getDeptFromBudgtAppropriationNo ");
            } else {
                final String deptCode = strArr[0];
                final Department dept = (Department) persistenceService.find(" from Department where code=?",
                        deptCode);
                if (dept == null) {
                    logger.error("No department found with prefix--" + deptCode);
                    throw new ApplicationRuntimeException("Exception in getDeptFromBudgtAppropriationNo ");
                } else
                    return dept;
            }
        }
    }

    private PdfPTable createApprovalDetailsTable(final AbstractEstimate estimate) throws DocumentException {
        try {
            PdfPTable approvaldetailsTable = null;
            if (shouldShowApprovalNumber) {
                approvaldetailsTable = new PdfPTable(6);
                approvaldetailsTable.setWidths(new float[]{2f, 1f, 1f, 1f, 1.5f, 2f});
            } else {
                approvaldetailsTable = new PdfPTable(5);
                approvaldetailsTable.setWidths(new float[]{2f, 1.5f, 1.5f, 1.5f, 2f});
            }
            approvaldetailsTable.setWidthPercentage(100);
            addRow(approvaldetailsTable, true, makePara("Approval Step"), centerPara("Name"),
                    centerPara("Designation"), centerPara("Approved on"), centerPara("Remarks"));

            if (estimate != null && estimate.getCurrentState() != null) {
                history = new LinkedList<>();
                if (estimate.getCurrentState().getHistory() != null)
                    history.addAll(estimate.getCurrentState().getHistory());
                history.add(new StateHistory(estimate.getCurrentState()));
            }

            if (history != null)
                for (final StateHistory stateHistory : history)
                    displayHistory(stateHistory, approvaldetailsTable);
            return approvaldetailsTable;
        } catch (final Exception e) {
            throw new ApplicationRuntimeException("Exception occured while getting approval details " + e);
        }
    }

    private PdfPTable createEstimateDetailsTable1(final AbstractEstimate estimate) throws DocumentException {
        try {
            final PdfPTable estimateDetailsTable1 = new PdfPTable(2);
            estimateDetailsTable1.setWidthPercentage(75);
            estimateDetailsTable1.setWidths(new float[]{0.6f, 1f});

            addRow(estimateDetailsTable1, true, centerPara("Department"), centerPara(estimate.getExecutingDepartment()
                    .getName()));
            addRow(estimateDetailsTable1, true, centerPara("Name of the Work"), centerPara(estimate.getName()));
            addRow(estimateDetailsTable1, true, centerPara("Estimate Number"), centerPara(estimate.getEstimateNumber()));

            return estimateDetailsTable1;
        } catch (final Exception e) {
            throw new ApplicationRuntimeException("Exception occured while estimate details method1 " + e);
        }
    }

    private PdfPTable createBudgetDetailsForEstimateTable(final AbstractEstimate estimate) throws DocumentException {
        try {

            final PdfPTable estBudgetDetailsTable = new PdfPTable(4);
            estBudgetDetailsTable.setWidthPercentage(75);
            estBudgetDetailsTable.setWidths(new float[]{0.6f, 1f, 0.5f, 1f});

            BigDecimal totalGrant;
            BigDecimal budgetAvailable;
            BigDecimal balOnHand;
            BigDecimal amtAppropriated = BigDecimal.ZERO;
            BigDecimal totGrantafterMultiFactor = BigDecimal.ZERO;

            BigDecimal totalUtilizedAmt;
            BigDecimal amtAppropriatedsofar = BigDecimal.ZERO;
            worksService.getAccountdetailtypeByName("DEPOSITCODE");
            AbstractEstimateAppropriation latestAbstractEstimateAppropriation;

            if (abstractEstimateAppropriationList != null && !abstractEstimateAppropriationList.isEmpty()) {
                latestAbstractEstimateAppropriation = abstractEstimateAppropriationList
                        .get(abstractEstimateAppropriationList.size() - 1);

                if (latestAbstractEstimateAppropriation != null)
                    if (estimate.getDepositCode() == null) {
                        if (latestAbstractEstimateAppropriation.getBudgetUsage().getConsumedAmount() != 0) {
                            final Department dept = getDeptFromBudgtAppropriationNo(latestAbstractEstimateAppropriation
                                    .getBudgetUsage().getAppropriationnumber());
                            totalGrant = abstractEstimateService.getTotalGrantForYearAsOnDate(estimate
                                            .getFinancialDetails().get(0), latestAbstractEstimateAppropriation.getBudgetUsage()
                                            .getFinancialYearId().longValue(),
                                    Integer.parseInt(dept.getId().toString()),
                                    latestAbstractEstimateAppropriation.getBudgetUsage().getUpdatedTime());
                            final BigDecimal planningBudgetPerc = abstractEstimateService.getPlanningBudgetPercentage(
                                    estimate.getFinancialDetails().get(0), latestAbstractEstimateAppropriation
                                            .getBudgetUsage().getFinancialYearId().longValue(),
                                    Integer.parseInt(dept.getId().toString()));
                            if (planningBudgetPerc != null && planningBudgetPerc.compareTo(BigDecimal.ZERO) != 0) {
                                totGrantafterMultiFactor = totalGrant.multiply(planningBudgetPerc
                                        .divide(new BigDecimal(100)));
                                appValue = planningBudgetPerc.divide(new BigDecimal(100)).toString();
                            }

                            budgetAvailable = latestAbstractEstimateAppropriation.getBalanceAvailable();
                            balOnHand = budgetAvailable.add(new BigDecimal(latestAbstractEstimateAppropriation
                                    .getBudgetUsage().getConsumedAmount()));
                            amtAppropriated = totGrantafterMultiFactor.subtract(balOnHand);
                        }
                    } else if (latestAbstractEstimateAppropriation.getDepositWorksUsage().getConsumedAmount()
                            .doubleValue() != 0) {
                        totalUtilizedAmt = depositWorksUsageService.getTotalUtilizedAmountForDepositWorks(
                                latestAbstractEstimateAppropriation.getAbstractEstimate().getFinancialDetails().get(0),
                                latestAbstractEstimateAppropriation.getDepositWorksUsage().getCreatedDate());
                        if (totalUtilizedAmt == null)
                            totalUtilizedAmt = BigDecimal.ZERO;
                        amtAppropriatedsofar = totalUtilizedAmt.subtract(latestAbstractEstimateAppropriation
                                .getDepositWorksUsage().getConsumedAmount());
                    }
            }
            addRow(estBudgetDetailsTable, true, centerPara("Estimate Date"),
                    centerPara(DateUtils.getFormattedDate(estimate.getEstimateDate(), "dd/MM/yyyy")),
                    centerPara("Fund"), centerPara(estimate.getFinancialDetails().isEmpty() ? "" : estimate
                            .getFinancialDetails().get(0).getFund().getName()));
            if (estimate.getDepositCode() == null) {
                addRow(estBudgetDetailsTable, true, centerPara("Function "), centerPara(estimate.getFinancialDetails()
                                .isEmpty() ? "" : estimate.getFinancialDetails().get(0).getFunction().getName()),
                        centerPara("Budget Head"), centerPara(estimate.getFinancialDetails().isEmpty() ? "" : estimate
                                .getFinancialDetails().get(0).getBudgetGroup().getName()));
                addRow(estBudgetDetailsTable, true, centerPara("Amount Appropriated so far"),
                        centerPara(amtAppropriated == null ? "" : toCurrency(amtAppropriated.doubleValue())),
                        centerPara("Estimate Amount"), centerPara(toCurrency(estimate.getTotalAmount().getValue())));
            } else {
                addRow(estBudgetDetailsTable,
                        true,
                        centerPara("Function "),
                        centerPara(estimate.getFinancialDetails().isEmpty() ? "" : estimate.getFinancialDetails()
                                .get(0).getFunction().getName()),
                        centerPara("Deposit COA/Deposit Code"),
                        centerPara(estimate.getFinancialDetails().isEmpty() ? "" : estimate.getFinancialDetails()
                                .get(0).getCoa().getGlcode().concat("-")
                                .concat(estimate.getFinancialDetails().get(0).getCoa().getName()).concat(" / ")
                                .concat(estimate.getDepositCode().getCode())));
                addRow(estBudgetDetailsTable, true, centerPara("Amount Appropriated so far"),
                        centerPara(amtAppropriatedsofar == null ? "" : toCurrency(amtAppropriatedsofar.doubleValue())),
                        centerPara("Estimate Amount"),
                        makePara(toCurrency(estimate.getTotalAmount().getValue()), Element.ALIGN_RIGHT));
            }

            return estBudgetDetailsTable;
        } catch (final Exception e) {
            throw new ApplicationRuntimeException("Exception occured while estimate details method 2 " + e);
        }
    }

    private PdfPTable createBalanceAmtCalculationTable(final AbstractEstimate estimate) throws DocumentException {
        try {

            final PdfPTable estimateDetailsTable2 = new PdfPTable(2);
            estimateDetailsTable2.setWidthPercentage(75);
            estimateDetailsTable2.setWidths(new float[]{0.6f, 1f});

            BigDecimal budgetAvailable = BigDecimal.ZERO;
            BigDecimal totalUtilizedAmt;
            BigDecimal totalDepositAmt;
            BigDecimal balanceAvailable = BigDecimal.ZERO;

            final Accountdetailtype accountdetailtype = worksService.getAccountdetailtypeByName("DEPOSITCODE");

            if (abstractEstimateAppropriationList != null && !abstractEstimateAppropriationList.isEmpty()) {
                final AbstractEstimateAppropriation latestAbstractEstimateAppropriation = abstractEstimateAppropriationList
                        .get(abstractEstimateAppropriationList.size() - 1);
                if (latestAbstractEstimateAppropriation != null)
                    if (estimate.getDepositCode() == null) {
                        if (latestAbstractEstimateAppropriation.getBudgetUsage().getConsumedAmount() != 0)
                            budgetAvailable = latestAbstractEstimateAppropriation.getBalanceAvailable();
                    } else if (latestAbstractEstimateAppropriation.getDepositWorksUsage().getConsumedAmount()
                            .doubleValue() != 0) {
                        totalDepositAmt = depositWorksUsageService.getTotalDepositWorksAmount(estimate.getDepositCode()
                                        .getFund(), latestAbstractEstimateAppropriation.getAbstractEstimate()
                                        .getFinancialDetails().get(0).getCoa(),
                                accountdetailtype, estimate.getDepositCode()
                                        .getId(),
                                latestAbstractEstimateAppropriation.getDepositWorksUsage()
                                        .getAppropriationDate());
                        totalUtilizedAmt = depositWorksUsageService.getTotalUtilizedAmountForDepositWorks(
                                latestAbstractEstimateAppropriation.getAbstractEstimate().getFinancialDetails().get(0),
                                latestAbstractEstimateAppropriation.getDepositWorksUsage().getCreatedDate());
                        if (totalUtilizedAmt == null)
                            totalUtilizedAmt = BigDecimal.ZERO;
                        balanceAvailable = BigDecimal.valueOf(totalDepositAmt.doubleValue()
                                - totalUtilizedAmt.doubleValue());
                    }
            }
            if (estimate.getDepositCode() == null)
                addRow(estimateDetailsTable2, true, centerPara("Balance Amount Available"),
                        centerPara(budgetAvailable == null ? "" : toCurrency(budgetAvailable.doubleValue())));
            else
                addRow(estimateDetailsTable2, true, centerPara("Balance Amount Available"),
                        centerPara(balanceAvailable == null ? "" : toCurrency(balanceAvailable.doubleValue())));
            addRow(estimateDetailsTable2, true, centerPara("Reference"), centerPara(space1));
            addRow(estimateDetailsTable2, true, centerPara("Any Other Remarks"), centerPara(space1 + "\n\n"));

            return estimateDetailsTable2;
        } catch (final Exception e) {
            throw new ApplicationRuntimeException("Exception occured while estimate and budget details method 3" + e);
        }
    }

    public void displayHistory(final StateHistory<Position> stateHistory, final PdfPTable approvaldetailsTable) {
        if (!stateHistory.getValue().equals("NEW") && !stateHistory.getValue().equals("END")) {
            String nextAction = "";
            if (stateHistory.getNextAction() != null && !stateHistory.getNextAction().equals("END"))
                nextAction = stateHistory.getNextAction();
            String state = stateHistory.getValue();
            if (!nextAction.equalsIgnoreCase(""))
                state = stateHistory.getValue() + " - " + nextAction;
            final Long positionId = stateHistory.getOwnerPosition().getId();
            final DeptDesig deptdesig = stateHistory.getOwnerPosition().getDeptDesig();
            final String desgName = deptdesig.getDesignation().getName();
            final Employee employee = assignmentService
                    .getPrimaryAssignmentForPositionAndDate(positionId, stateHistory.getCreatedDate())
                    .getEmployee();
            addRow(approvaldetailsTable, true, makePara(state), makePara(employee.getName()),
                    makePara(desgName), makePara(DateUtils.getFormattedDate(stateHistory.getCreatedDate(), "dd/MM/yyyy")),
                    rightPara(stateHistory.getComments()));
        }
    }

    private PdfPTable createActivitiesTable(final AbstractEstimate estimate) throws DocumentException {
        final PdfPTable activitiesTable = new PdfPTable(7);
        activitiesTable.setWidthPercentage(100);
        activitiesTable.setWidths(new float[]{0.5f, 1f, 3.1f, 1.2f, 0.8f, 1.1f, 1.5f});
        addRow(activitiesTable, true, makePara("Sl No"), centerPara("Quantity"), centerPara("Description"),
                centerPara("Sch. No"), centerPara("Unit"), centerPara("Rate"), centerPara("Amount"));
        Collection<Activity> activities = estimate.getSORActivities();
        int index = 1;
        for (final Activity activity : activities) {
            String estimateUom = "";
            if (activity.getUom() == null)
                estimateUom = activity.getSchedule().getUom().getUom();
            else
                estimateUom = activity.getUom().getUom();
            addRow(activitiesTable, true, makePara(index++), rightPara(activity.getQuantity()), makePara(activity
                            .getSchedule().getDescription()), centerPara(activity.getSchedule().getCode()),
                    centerPara(estimateUom), rightPara(toCurrency(activity.getSORCurrentRate())),
                    rightPara(toCurrency(activity.getAmount())));
        }
        activities = estimate.getNonSORActivities();
        for (final Activity activity : activities)
            addRow(activitiesTable, true, makePara(index++), rightPara(activity.getQuantity()), makePara(activity
                            .getNonSor().getDescription()), centerPara(""), centerPara(activity.getNonSor().getUom().getUom()),
                    rightPara(toCurrency(activity.getRate())), rightPara(toCurrency(activity.getAmount())));
        addRow(activitiesTable, true, centerPara(""), centerPara(""), makePara(""), rightPara(""), centerPara(""),
                centerPara("TOTAL"), rightPara(toCurrency(estimate.getWorkValue())));
        addRow(activitiesTable, true, centerPara(""), centerPara(""), makePara(""), rightPara(""), centerPara(""),
                centerPara("WORK VALUE"), rightPara(toCurrency(estimate.getWorkValueIncludingTaxes())));

        return activitiesTable;
    }

    private String getOverheadDescription(final OverheadValue oh) {
        if (oh.getOverhead().getOverheadRates().get(0).getPercentage() > 0)
            return oh.getOverhead().getDescription() + '-' + oh.getOverhead().getOverheadRates().get(0).getPercentage()
                    + '%';
        else
            return oh.getOverhead().getDescription();
    }

    private void addZoneYearHeader(final AbstractEstimate estimate, final CFinancialYear financialYear)
            throws DocumentException {
        document.add(spacer());
        final PdfPTable headerTable = new PdfPTable(2);
        headerTable.setWidthPercentage(100);
        headerTable.setWidths(new float[]{1f, 1f});
        Paragraph financialYearPara = new Paragraph();
        if (financialYear != null)
            financialYearPara = makePara("Budget Year: " + financialYear.getFinYearRange(), Element.ALIGN_RIGHT);
        addRow(headerTable, false, makePara("Estimate Number: " + estimate.getEstimateNumber(), Element.ALIGN_LEFT),
                financialYearPara);

        document.add(headerTable);
        if (estimate.getWard() != null && "Ward".equalsIgnoreCase(estimate.getWard().getBoundaryType().getName())
                && estimate.getWard().getParent() != null && estimate.getWard().getParent().getName() != null)
            document.add(makePara("Ward: " + estimate.getWard().getName() + " / Zone: "
                    + estimate.getWard().getParent().getName(), Element.ALIGN_RIGHT));
        else if (estimate.getWard() != null && "Zone".equalsIgnoreCase(estimate.getWard().getBoundaryType().getName())
                && estimate.getWard().getParent() != null && estimate.getWard().getParent().getName() != null)
            document.add(makePara("Zone: " + estimate.getWard().getName() + " / Region: "
                    + estimate.getWard().getParent().getName(), Element.ALIGN_RIGHT));
        else if (estimate.getWard() != null)
            document.add(makePara("Jurisdiction: " + estimate.getWard().getName() + "("
                    + estimate.getWard().getBoundaryType().getName() + ")", Element.ALIGN_RIGHT));
        document.add(spacer());
    }

    private void addZoneYearHeaderWithOutEstimateNo(final AbstractEstimate estimate, final CFinancialYear financialYear)
            throws DocumentException {
        final PdfPTable headerTable = new PdfPTable(2);
        headerTable.setWidthPercentage(100);
        headerTable.setWidths(new float[]{1f, 1f});
        Paragraph financialYearPara = new Paragraph();
        if (financialYear != null)
            financialYearPara = makePara("Budget Year: " + financialYear.getFinYearRange(), Element.ALIGN_RIGHT);
        addRow(headerTable, false, new Paragraph(), financialYearPara);
        document.add(headerTable);
        if (estimate.getWard() != null && "Ward".equalsIgnoreCase(estimate.getWard().getBoundaryType().getName())
                && estimate.getWard().getParent() != null && estimate.getWard().getParent().getName() != null)
            document.add(makePara("Ward: " + estimate.getWard().getName() + " / Zone: "
                    + estimate.getWard().getParent().getName(), Element.ALIGN_RIGHT));
        else if (estimate.getWard() != null && "Zone".equalsIgnoreCase(estimate.getWard().getBoundaryType().getName())
                && estimate.getWard().getParent() != null && estimate.getWard().getParent().getName() != null)
            document.add(makePara("Zone: " + estimate.getWard().getName() + " / Region: "
                    + estimate.getWard().getParent().getName(), Element.ALIGN_RIGHT));
        else if (estimate.getWard() != null)
            document.add(makePara("Jurisdiction: " + estimate.getWard().getName() + "("
                    + estimate.getWard().getBoundaryType().getName() + ")", Element.ALIGN_RIGHT));
        document.add(spacer());
    }

    public PersistenceService getPersistenceService() {
        return persistenceService;
    }

    public void setPersistenceService(final PersistenceService persistenceService) {
        this.persistenceService = persistenceService;
    }

    public String getHeaderText() {
        return headerText;
    }

    public void setHeaderText(final String headerText) {
        this.headerText = headerText;
    }

    public BudgetDetailsDAO getBudgetDetailsDAO() {
        return budgetDetailsDAO;
    }

    public void setBudgetDetailsDAO(final BudgetDetailsDAO budgetDetailsDAO) {
        this.budgetDetailsDAO = budgetDetailsDAO;
    }

    public void setAbstractEstimateService(final AbstractEstimateService abstractEstimateService) {
        this.abstractEstimateService = abstractEstimateService;
    }

    public void setWorksService(final WorksService worksService) {
        this.worksService = worksService;
    }

    public List<String> getAppConfigValuesToSkipBudget() {
        return worksService.getNatureOfWorkAppConfigValues(MODULE_NAME, KEY_NAME);
    }

    public Boolean isSkipBudgetCheck() {
        final List<String> depositTypeList = getAppConfigValuesToSkipBudget();
        if (estimate != null && estimate.getId() != null)
            for (final String type : depositTypeList)
                if (type.equals(estimate.getNatureOfWork().getName()))
                    skipBudget = true;
        return skipBudget;
    }

    public List<BudgetUsage> getBudgetDetailUsage(final String estimateNumber) {
        return (List<BudgetUsage>) persistenceService.findAllBy(
                "from BudgetUsage bu where bu.referenceNumber=?", estimateNumber);
    }

    public List<AbstractEstimateAppropriation> getAbstractEstimateAppropriationList() {
        return abstractEstimateAppropriationList;
    }

    public void setAbstractEstimateAppropriationList(
            final List<AbstractEstimateAppropriation> abstractEstimateAppropriationList) {
        this.abstractEstimateAppropriationList = abstractEstimateAppropriationList;
    }

    public void setAssignmentService(final AssignmentService assignmentService) {
        this.assignmentService = assignmentService;
    }

}
