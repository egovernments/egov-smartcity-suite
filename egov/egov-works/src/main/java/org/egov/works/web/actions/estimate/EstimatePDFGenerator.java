/**
 * eGov suite of products aim to improve the internal efficiency,transparency,
   accountability and the service delivery of the government  organizations.

    Copyright (C) <2015>  eGovernments Foundation

    The updated version of eGov suite of products as by eGovernments Foundation
    is available at http://www.egovernments.org

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program. If not, see http://www.gnu.org/licenses/ or
    http://www.gnu.org/licenses/gpl.html .

    In addition to the terms of the GPL license to be adhered to in using this
    program, the following additional terms are to be complied with:

	1) All versions of this program, verbatim or modified must carry this
	   Legal Notice.

	2) Any misrepresentation of the origin of the material is prohibited. It
	   is required that all modified versions of this material be marked in
	   reasonable ways as different from the original version.

	3) This license does not grant any rights to any user of the program
	   with regards to rights under trademark law for use of the trade names
	   or trademarks of eGovernments Foundation.

  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.works.web.actions.estimate;

import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.egov.commons.Accountdetailtype;
import org.egov.commons.CFinancialYear;
import org.egov.dao.budget.BudgetDetailsDAO;
import org.egov.exceptions.EGOVException;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.workflow.entity.StateHistory;
import org.egov.infstr.ValidationException;
import org.egov.infstr.config.AppConfigValues;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.utils.DateUtils;
import org.egov.model.budget.BudgetUsage;
import org.egov.pims.commons.DeptDesig;
import org.egov.pims.model.PersonalInformation;
import org.egov.pims.service.EmployeeService;
import org.egov.works.models.estimate.AbstractEstimate;
import org.egov.works.models.estimate.AbstractEstimateAppropriation;
import org.egov.works.models.estimate.Activity;
import org.egov.works.models.estimate.FinancialDetail;
import org.egov.works.models.estimate.MultiYearEstimate;
import org.egov.works.models.estimate.OverheadValue;
import org.egov.works.services.AbstractEstimateService;
import org.egov.works.services.DepositWorksUsageService;
import org.egov.works.services.WorksService;
import org.egov.works.utils.AbstractPDFGenerator;
import org.egov.works.utils.DateConversionUtil;
import org.springframework.beans.factory.annotation.Autowired;

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

public class EstimatePDFGenerator extends AbstractPDFGenerator {
    private static final Logger logger = Logger.getLogger(EstimatePDFGenerator.class);
    private PersistenceService persistenceService = new PersistenceService();
    private PersistenceService<AbstractEstimateAppropriation, Long> estimateAppropriationService;
    private final AbstractEstimate estimate;
    private final CFinancialYear financialYear;
    private String headerText;
    @Autowired
    private EmployeeService employeeService;
    private BudgetDetailsDAO budgetDetailsDAO;
    private AbstractEstimateService abstractEstimateService;
    private static final String space1 = "\t  \t  \t  \t \t \t  \t  \t  \t \t \t \t"
            + "\t  \t  \t  \t \t \t  \t  \t  \t \t \t \t";

    private WorksService worksService;
    private String appValue = "";
    private DepositWorksUsageService depositWorksUsageService;
    private static final String MODULE_NAME = "Works";
    private static final String KEY_NAME = "SKIP_BUDGET_CHECK";
    private boolean skipBudget = false;
    private static final String ELECTRICAL_DEPARTMENT = "L-Electrical";
    private List<StateHistory> history = null;
    private boolean shouldShowApprovalNumber;
    List<AbstractEstimateAppropriation> abstractEstimateAppropriationList = new LinkedList<AbstractEstimateAppropriation>();

    public void setBudgetDetailsDAO(final BudgetDetailsDAO budgetDetailsDAO) {
        this.budgetDetailsDAO = budgetDetailsDAO;
    }

    public EstimatePDFGenerator(final AbstractEstimate estimate, final String headerText,
            final CFinancialYear financialYear, final OutputStream out) {
        super(out, "portrait");
        this.estimate = estimate;
        this.headerText = headerText;
        this.financialYear = financialYear;
    }

    public void generatePDF() throws ValidationException {
        try {
            final Paragraph headerTextPara = new Paragraph(new Chunk(headerText, new Font(Font.UNDEFINED, LARGE_FONT,
                    Font.BOLD)));
            String projectCode = null;
            final String oldEstNo = "";
            HeaderFooter hf = null;
            headerTextPara.setAlignment(Element.ALIGN_CENTER);
            document.add(headerTextPara);
            document.add(makePara("Executing Department:" + estimate.getExecutingDepartment().getName(),
                    Element.ALIGN_LEFT));
            document.add(makePara("User Department:" + estimate.getUserDepartment().getName(), Element.ALIGN_LEFT));

            final CFinancialYear estimateFinancialYear = estimate.getMultiYearEstimates().isEmpty() ? financialYear
                    : estimate.getMultiYearEstimates().get(0).getFinancialYear();
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
                                + "\t  \t  \t  \t \t\t  \t  \t  \t \t ABSTRACT ESTIMATE").concat("\n\n")
                                .concat("Name of Work: " + estimate.getName()).concat("\n")
                                .concat("Description: " + estimate.getDescription()).concat("\n")
                                .concat("Estimate Number: " + estimate.getEstimateNumber()).concat(oldEstNo)
                                .concat("\n").concat(projectCode)), false);
            } else
                hf = new HeaderFooter(new Phrase("\t  \t  \t  \t \t \t  \t  \t  \t \t \t  \t  \t  \t \t"
                        + "\t  \t  \t  \t \t\t  \t  \t  \t \t\t  \t  \t  \t \t\t  \t  \t  "
                        + "\t  \t  \t  \t \t\t  \t  \t  \t \t"
                        + headerText
                        .concat("\n")
                        .concat("\t  \t  \t  \t \t \t  \t  \t  \t \t"
                                + "\t  \t  \t  \t \t\t  \t  \t  \t \t\t  \t  \t  \t \t\t  \t  \t  "
                                + "\t  \t  \t  \t \t\t  \t  \t  \t \t ABSTRACT ESTIMATE").concat("\n\n")
                                .concat("Name of Work: " + estimate.getName()).concat("\n")
                                .concat("Description: " + estimate.getDescription()).concat("\n")
                                .concat("Estimate Number: " + estimate.getEstimateNumber()).concat(oldEstNo)), false);

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
            document.add(makePara("Prepared By: " + estimate.getEstimatePreparedBy().getEmployeeName()));
            document.add(spacer());
            document.add(spacer());
            document.add(makePara("Checked By: "));
            document.newPage();
            addZoneYearHeaderWithOutEstimateNo(estimate, estimateFinancialYear);
            document.add(createActivitiesTable(estimate));
            document.add(spacer());
            checkIfShouldShowApprovalNumber();
            final PdfPTable approvaldetailsTable = createApprovalDetailsTable(estimate);

            if (approvaldetailsTable.getRows().size() != 1) {
                document.add(makePara("Approval Details"));
                document.add(spacer());
                document.add(approvaldetailsTable);
            }

            if (isSkipBudgetCheck()) {
                final PdfPTable depositWorksAppropriationTable = createDepositAppropriationTable(estimate);
                if (depositWorksAppropriationTable.getRows().size() != 1)
                    if (estimate.getBudgetApprNo() != null) {
                        document.newPage();
                        document.add(spacer());
                        document.add(makePara("Deposit Code Appropriation Details"));
                        document.add(spacer());
                        document.add(depositWorksAppropriationTable);
                    }
            } else {
                final PdfPTable BudgetaryAppropriationTable = createBudgetaryAppropriationTable(estimate);
                final String estimateNumber = estimate.getEstimateNumber();
                if (BudgetaryAppropriationTable.getRows().size() != 1)
                    if (getBudgetDetailUsage(estimateNumber).size() != 0 && estimate.getBudgetApprNo() != null) {
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
                                    + "\t  \t  \t  \t \t\t  \t  \t  \t \t ABSTRACT ESTIMATE").concat("\n\n")));
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
            throw new EGOVRuntimeException("estimate.pdf.error", e);
        } catch (final EGOVException ex) {
            throw new EGOVRuntimeException("estimate.pdf.error", ex);
        }
    }

    private void checkIfShouldShowApprovalNumber() {
        final List<AppConfigValues> configList = abstractEstimateService.getAppConfigValue("Works",
                "ESTIMATE_APPROVAL_NUMBER_DATE");
        Date dateToCheck = null;
        if (!configList.isEmpty())
            try {
                dateToCheck = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH).parse(configList.get(0).getValue());
            } catch (final ParseException e) {
                throw new EGOVRuntimeException("estimate.pdf.error", e);
            }
        shouldShowApprovalNumber = estimate.getCreatedDate().toDate().after(dateToCheck);
    }

    private PdfPTable createMultiYearTable(final AbstractEstimate estimate) throws DocumentException {
        final PdfPTable multiyearTable = new PdfPTable(3);
        multiyearTable.setWidthPercentage(100);
        multiyearTable.setWidths(new float[] { 1f, 2f, 2f });
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
        overheadsTable.setWidths(new float[] { 1f, 4f, 2f });
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
     * 1 Deposit Code Appropriation number,2 Deposit Code,3 Account Code,4
     * Function Center,5 Department,6 Total Deposit Amount, 7 Amount
     * Appropriated so far= totalUtilizedAmt- estimate amt 8 Balance on hand =
     * Amount Appropriated so far - estimat amt 9 Amount of the Estimate =
     * estimate amt 10 Balance after Appropriation of this estimate =balOnHand -
     * ESTIMATE AMT
     */
    private PdfPTable createDepositAppropriationTable(final AbstractEstimate estimate) throws DocumentException,
    EGOVException, ValidationException {
        /*
         * List<FinancialDetail>
         * financialdetails=estimate.getFinancialDetails(); String
         * appropriationNumber=estimate.getBudgetApprNo(); DepositWorksUsage
         * depositWorksUsage
         * =depositWorksUsageService.getDepositWorksUsage(estimate,
         * appropriationNumber);
         */int isReject = -1;
         depositWorksUsageService = abstractEstimateService.getDepositWorksUsageService();
         BigDecimal totalUtilizedAmt = BigDecimal.ZERO;
         BigDecimal amtAppropriatedsofar = BigDecimal.ZERO;
         BigDecimal totalDepositAmt = BigDecimal.ZERO;
         BigDecimal balOnHand = BigDecimal.ZERO;
         BigDecimal balanceAvailable = BigDecimal.ZERO;
         BigDecimal amtAppropriated = BigDecimal.ZERO;
         final Accountdetailtype accountdetailtype = worksService.getAccountdetailtypeByName("DEPOSITCODE");
         estimateAppropriationService = abstractEstimateService.getEstimateAppropriationService();
         abstractEstimateAppropriationList = estimateAppropriationService
                 .findAllBy(
                         "from AbstractEstimateAppropriation aea where aea.abstractEstimate.id=? and aea.depositWorksUsage.id is not null order by id, aea.depositWorksUsage.financialYearId asc",
                         estimate.getId());
         final PdfPTable depositWorksAppropriationTable = new PdfPTable(2);
         depositWorksAppropriationTable.setWidthPercentage(100);
         depositWorksAppropriationTable.setWidths(new float[] { 2f, 8f });

         if (estimate.getBudgetRejectionNo() != null) {
             final String budgetRejectionNo = estimate.getBudgetRejectionNo();
             isReject = budgetRejectionNo.indexOf(estimate.getBudgetApprNo());
         }

         if (estimate.getBudgetApprNo() != null && estimate.getTotalAmount() != null && isReject == -1) {
             /*
              * if(estimate.getBudgetApprNo()!=null &&
              * estimate.getTotalAmount()!=null){ for(FinancialDetail
              * financialDetail:financialdetails){
              * if(financialDetail.getCoa()!=null) {
              * System.out.println("depositWorksUsage.getAppropriationDate()>>>>"
              * +depositWorksUsage.getAppropriationDate());
              * totalUtilizedAmt=depositWorksUsageService
              * .getTotalUtilizedAmountForDepositWorks
              * (financialDetail,depositWorksUsage.getCreatedDate());
              * System.out.println
              * ("totalUtilizedAmt.doubleValue()>>>"+totalUtilizedAmt
              * .doubleValue()); if(totalUtilizedAmt == null){
              * totalUtilizedAmt=BigDecimal.ZERO; } amtAppropriated=new
              * BigDecimal
              * (totalUtilizedAmt.doubleValue()-estimate.getTotalAmount(
              * ).getValue());
              * totalDepositAmt=depositWorksUsageService.getTotalDepositWorksAmount
              * (estimate.getDepositCode().getFund(), financialDetail.getCoa(),
              * accountdetailtype, estimate.getDepositCode().getId(),
              * depositWorksUsage.getAppropriationDate()); if(totalDepositAmt ==
              * null){ totalDepositAmt=BigDecimal.ZERO; } balOnHand=new
              * BigDecimal
              * (totalDepositAmt.doubleValue()-amtAppropriated.doubleValue());
              * balAftApropriation=new
              * BigDecimal(balOnHand.doubleValue()-estimate
              * .getTotalAmount().getValue());
              * addRow(depositWorksAppropriationTable,
              * true,makePara("Deposit Code Appropriation Number"
              * ),makePara(estimate.getBudgetApprNo()));
              */addRow(depositWorksAppropriationTable, true, centerPara("Deposit Code"), centerPara(estimate
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
              /*
               * addRow(depositWorksAppropriationTable,
               * true,makePara("Total Deposit Amount"
               * ),rightPara(toCurrency(totalDepositAmt.doubleValue())));
               * addRow(depositWorksAppropriationTable,
               * true,makePara("Amount Appropriated so far"
               * ),rightPara(toCurrency(amtAppropriated.doubleValue())));
               * addRow(depositWorksAppropriationTable,
               * true,makePara("Balance on Hand"
               * ),rightPara(toCurrency(balOnHand.doubleValue())));
               * addRow(depositWorksAppropriationTable,
               * true,makePara("Balance After Appropriation of this Estimate"
               * ),rightPara(toCurrency(balAftApropriation.doubleValue())));
               */// }
              // }
              final PdfPTable appropriationDetailTable = new PdfPTable(6);
              addRow(appropriationDetailTable, true, makePara(7f, "Appropriation Number"),
                      makePara(7f, "Total Deposit Amount"), makePara(7f, "Amount Appropriated so far"),
                      makePara(7f, "Amount Appropriated"), makePara(7f, "Balance on Hand"),
                      makePara(7f, "Balance After Appropriation"));
              for (final AbstractEstimateAppropriation abstractEstimateAppropriation : abstractEstimateAppropriationList)
                  if (abstractEstimateAppropriation.getDepositWorksUsage().getConsumedAmount().doubleValue() != 0) {
                      totalDepositAmt = depositWorksUsageService.getTotalDepositWorksAmount(estimate.getDepositCode()
                              .getFund(), abstractEstimateAppropriation.getAbstractEstimate().getFinancialDetails()
                              .get(0).getCoa(), accountdetailtype, estimate.getDepositCode().getId(),
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
                      balanceAvailable = new BigDecimal(totalDepositAmt.doubleValue() - totalUtilizedAmt.doubleValue());
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
     * 1 Budget head,2 Function Center,3 Department,4 Total grant, 6 Balance on
     * hand = Budget Available - estimat amt 5 Amount Appropriated=
     * oneFifthTimesTotGrant-balOnHand 7 Amount of the Estimate = estimate amt 8
     * Balance after Appropriation =balOnHand - ESTIMATE AMT
     */
    private PdfPTable createBudgetaryAppropriationTable(final AbstractEstimate estimate) throws DocumentException,
    EGOVException, ValidationException {
        int isReject = -1;
        final List<FinancialDetail> financialdetails = estimate.getFinancialDetails();
        BigDecimal totalGrant = BigDecimal.ZERO;
        BigDecimal budgetAvailable = BigDecimal.ZERO;
        BigDecimal balOnHand = BigDecimal.ZERO;
        BigDecimal amtAppropriated = BigDecimal.ZERO;
        BigDecimal totGrantafterMultiFactor = BigDecimal.ZERO;

        estimateAppropriationService = abstractEstimateService.getEstimateAppropriationService();
        abstractEstimateAppropriationList = estimateAppropriationService
                .findAllBy(
                        "from AbstractEstimateAppropriation aea where aea.abstractEstimate.id=? and aea.budgetUsage.id is not null order by aea.budgetUsage.id,aea.budgetUsage.financialYearId asc",
                        estimate.getId());

        final PdfPTable BudgetaryAppropriationTable = new PdfPTable(1);
        BudgetaryAppropriationTable.setWidthPercentage(100);
        BudgetaryAppropriationTable.setWidths(new float[] { 8f });

        if (estimate.getBudgetRejectionNo() != null) {
            final String budgetRejectionNo = estimate.getBudgetRejectionNo();
            isReject = budgetRejectionNo.indexOf(estimate.getBudgetApprNo());
        }

        if (estimate.getBudgetApprNo() != null && estimate.getTotalAmount() != null && isReject == -1)
            for (final FinancialDetail financialDetail : financialdetails)
                if (financialDetail.getBudgetGroup() != null) {
                    addRow(BudgetaryAppropriationTable, true, centerPara("Budget Head"), centerPara(financialDetail
                            .getBudgetGroup().getName()));
                    addRow(BudgetaryAppropriationTable, true, makePara("Function Center"), centerPara(financialDetail
                            .getFunction().getName()));
                    addRow(BudgetaryAppropriationTable, true, makePara("Amount of the Estimate "),
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
                        .parseInt(dept.getId().toString()), abstractEstimateAppropriation.getBudgetUsage()
                        .getUpdatedTime());
                final BigDecimal planningBudgetPerc = abstractEstimateService.getPlanningBudgetPercentage(
                        financialdetails.get(0), abstractEstimateAppropriation.getBudgetUsage().getFinancialYearId()
                        .longValue(), Integer.parseInt(dept.getId().toString()));
                if (planningBudgetPerc != null && !planningBudgetPerc.equals(0)) {
                    totGrantafterMultiFactor = totalGrant.multiply(planningBudgetPerc.divide(new BigDecimal(100)));
                    appValue = planningBudgetPerc.divide(new BigDecimal(100)).toString();
                }
                budgetAvailable = abstractEstimateAppropriation.getBalanceAvailable();
                balOnHand = budgetAvailable.add(new BigDecimal(abstractEstimateAppropriation.getBudgetUsage()
                        .getConsumedAmount()));
                amtAppropriated = totGrantafterMultiFactor.subtract(balOnHand);
                // Print only for the first time
                if (count == 0) {
                    addRow(BudgetaryAppropriationTable, false, makePara(""),
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
        BudgetaryAppropriationTable.addCell(appDetailpdfCell);
        return BudgetaryAppropriationTable;
    }

    private Department getDeptFromBudgtAppropriationNo(final String number) {
        if (StringUtils.isBlank(number)) {
            logger.error("Empty Ban Number");
            throw new EGOVRuntimeException("Exception in getDeptFromBudgtAppropriationNo ");
        } else {
            final String[] strArr = number.split("/");
            if (strArr == null || strArr.length == 0) {
                logger.error("Department prefix not present in ban no--" + number);
                throw new EGOVRuntimeException("Exception in getDeptFromBudgtAppropriationNo ");
            } else {
                final String deptCode = strArr[0];
                final Department dept = (Department) persistenceService.find(" from Department where deptCode=?",
                        deptCode);
                if (dept == null) {
                    logger.error("No department found with prefix--" + deptCode);
                    throw new EGOVRuntimeException("Exception in getDeptFromBudgtAppropriationNo ");
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
                approvaldetailsTable.setWidths(new float[] { 2f, 1f, 1f, 1f, 1.5f, 2f });
            } else {
                approvaldetailsTable = new PdfPTable(5);
                approvaldetailsTable.setWidths(new float[] { 2f, 1.5f, 1.5f, 1.5f, 2f });
            }
            approvaldetailsTable.setWidthPercentage(100);
            if (shouldShowApprovalNumber)
                addRow(approvaldetailsTable, true, makePara("Approval Step"), centerPara("Name"),
                        centerPara("Designation"), centerPara("Approved on"), centerPara("Approval Number"),
                        centerPara("Remarks"));
            else
                addRow(approvaldetailsTable, true, makePara("Approval Step"), centerPara("Name"),
                        centerPara("Designation"), centerPara("Approved on"), centerPara("Remarks"));

            if (estimate != null && estimate.getCurrentState() != null
                    && estimate.getCurrentState().getHistory() != null)
                history = estimate.getCurrentState().getHistory();

            if (history != null) {
                Collections.reverse(history);
                for (final StateHistory ad : history)
                    displayHistory(ad, approvaldetailsTable);
            }
            return approvaldetailsTable;
        } catch (final Exception e) {
            throw new EGOVRuntimeException("Exception occured while getting approval details " + e);
        }
    }

    private PdfPTable createEstimateDetailsTable1(final AbstractEstimate estimate) throws DocumentException {
        try {
            final PdfPTable estimateDetailsTable1 = new PdfPTable(2);
            estimateDetailsTable1.setWidthPercentage(75);
            estimateDetailsTable1.setWidths(new float[] { 0.6f, 1f });

            addRow(estimateDetailsTable1, true, centerPara("Department"), centerPara(estimate.getExecutingDepartment()
                    .getName()));
            addRow(estimateDetailsTable1, true, centerPara("Name of the Work"), centerPara(estimate.getName()));
            addRow(estimateDetailsTable1, true, centerPara("Estimate Number"), centerPara(estimate.getEstimateNumber()));

            return estimateDetailsTable1;
        } catch (final Exception e) {
            throw new EGOVRuntimeException("Exception occured while estimate details method1 " + e);
        }
    }

    private PdfPTable createBudgetDetailsForEstimateTable(final AbstractEstimate estimate) throws DocumentException {
        try {

            final PdfPTable estBudgetDetailsTable = new PdfPTable(4);
            estBudgetDetailsTable.setWidthPercentage(75);
            estBudgetDetailsTable.setWidths(new float[] { 0.6f, 1f, 0.5f, 1f });

            BigDecimal totalGrant = BigDecimal.ZERO;
            BigDecimal budgetAvailable = BigDecimal.ZERO;
            BigDecimal balOnHand = BigDecimal.ZERO;
            BigDecimal amtAppropriated = BigDecimal.ZERO;
            BigDecimal totGrantafterMultiFactor = BigDecimal.ZERO;

            BigDecimal totalUtilizedAmt = BigDecimal.ZERO;
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
                                    .getFinancialYearId().longValue(), Integer.parseInt(dept.getId().toString()),
                                    latestAbstractEstimateAppropriation.getBudgetUsage().getUpdatedTime());
                            final BigDecimal planningBudgetPerc = abstractEstimateService.getPlanningBudgetPercentage(
                                    estimate.getFinancialDetails().get(0), latestAbstractEstimateAppropriation
                                    .getBudgetUsage().getFinancialYearId().longValue(),
                                    Integer.parseInt(dept.getId().toString()));
                            if (planningBudgetPerc != null && !planningBudgetPerc.equals(0)) {
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
            throw new EGOVRuntimeException("Exception occured while estimate details method 2 " + e);
        }
    }

    private PdfPTable createBalanceAmtCalculationTable(final AbstractEstimate estimate) throws DocumentException {
        try {

            final PdfPTable estimateDetailsTable2 = new PdfPTable(2);
            estimateDetailsTable2.setWidthPercentage(75);
            estimateDetailsTable2.setWidths(new float[] { 0.6f, 1f });

            BigDecimal budgetAvailable = BigDecimal.ZERO;
            BigDecimal totalUtilizedAmt = BigDecimal.ZERO;
            BigDecimal totalDepositAmt = BigDecimal.ZERO;
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
                                .getFinancialDetails().get(0).getCoa(), accountdetailtype, estimate.getDepositCode()
                                .getId(), latestAbstractEstimateAppropriation.getDepositWorksUsage()
                                .getAppropriationDate());
                        totalUtilizedAmt = depositWorksUsageService.getTotalUtilizedAmountForDepositWorks(
                                latestAbstractEstimateAppropriation.getAbstractEstimate().getFinancialDetails().get(0),
                                latestAbstractEstimateAppropriation.getDepositWorksUsage().getCreatedDate());
                        if (totalUtilizedAmt == null)
                            totalUtilizedAmt = BigDecimal.ZERO;
                        balanceAvailable = new BigDecimal(totalDepositAmt.doubleValue()
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
            throw new EGOVRuntimeException("Exception occured while estimate and budget details method 3" + e);
        }
    }

    public void displayHistory(final StateHistory ad, final PdfPTable approvaldetailsTable) throws Exception {
        if (!ad.getValue().equals("NEW") && !ad.getValue().equals("END")) {
            String nextAction = "";
            if (ad.getNextAction() != null)
                nextAction = ad.getNextAction();
            // EgwStatus status =(EgwStatus)
            // getPersistenceService().find("from EgwStatus where code=?",ad.getValue());
            String state = ad.getValue();
            if (!nextAction.equalsIgnoreCase(""))
                state = ad.getValue() + " - " + nextAction;
            Long positionId = null;
            String desgName = null;
            DeptDesig deptdesig = null;
            // if(ad.getPrevious()==null){
            positionId = ad.getOwnerPosition().getId();
            deptdesig = ad.getOwnerPosition().getDeptDesigId();
            desgName = deptdesig.getDesigId().getDesignationName();
            /*
             * } else{ positionId =ad.getPrevious().getOwner().getId();
             * deptdesig= ad.getPrevious().getOwner().getDeptDesigId(); desgName
             * = deptdesig.getDesigId().getDesignationName(); }
             */
            final PersonalInformation emp = employeeService.getEmpForPositionAndDate(ad.getCreatedDate(),
                    Integer.parseInt(positionId.toString()));
            if (shouldShowApprovalNumber)
                addRow(approvaldetailsTable, true, makePara(state), makePara(emp.getEmployeeName()),
                        makePara(desgName), makePara(DateUtils.getFormattedDate(ad.getCreatedDate(), "dd/MM/yyyy")),
                        makePara(ad.getExtraInfo()), rightPara(ad.getComments()));
            else
                addRow(approvaldetailsTable, true, makePara(state), makePara(emp.getEmployeeName()),
                        makePara(desgName), makePara(DateUtils.getFormattedDate(ad.getCreatedDate(), "dd/MM/yyyy")),
                        rightPara(ad.getComments()));
        }
    }

    private PdfPTable createActivitiesTable(final AbstractEstimate estimate) throws DocumentException {
        final PdfPTable activitiesTable = new PdfPTable(7);
        activitiesTable.setWidthPercentage(100);
        activitiesTable.setWidths(new float[] { 0.5f, 1f, 3.1f, 1.2f, 0.8f, 1.1f, 1.5f });
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
        headerTable.setWidths(new float[] { 1f, 1f });
        Paragraph financialYearPara = new Paragraph();
        Paragraph rateYearPara = new Paragraph();
        if (financialYear != null) {
            financialYearPara = makePara("Budget Year: " + financialYear.getFinYearRange(), Element.ALIGN_RIGHT);
            rateYearPara = makePara("Schedule Rate Year: " + getSORYear(), Element.ALIGN_RIGHT);
        }
        addRow(headerTable, false, makePara("Estimate Number: " + estimate.getEstimateNumber(), Element.ALIGN_LEFT),
                financialYearPara);
        addRow(headerTable, false, new Paragraph(), rateYearPara);

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
        headerTable.setWidths(new float[] { 1f, 1f });
        Paragraph financialYearPara = new Paragraph();
        Paragraph rateYearPara = new Paragraph();
        if (financialYear != null) {
            financialYearPara = makePara("Budget Year: " + financialYear.getFinYearRange(), Element.ALIGN_RIGHT);
            rateYearPara = makePara("Schedule Rate Year: " + getSORYear(), Element.ALIGN_RIGHT);
        }
        addRow(headerTable, false, new Paragraph(), financialYearPara);
        addRow(headerTable, false, new Paragraph(), rateYearPara);
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

    public EmployeeService getEmployeeService() {
        return employeeService;
    }

    public void setEmployeeService(final EmployeeService employeeService) {
        this.employeeService = employeeService;
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

    public void setAbstractEstimateService(final AbstractEstimateService abstractEstimateService) {
        this.abstractEstimateService = abstractEstimateService;
    }

    public void setWorksService(final WorksService worksService) {
        this.worksService = worksService;
    }

    private String getSORYear() {
        String year = "";
        final List<AppConfigValues> appConfigList = worksService.getAppConfigValue("Works", "SCHEDULE_RATE_YEAR");
        final String estimateDept = estimate.getExecutingDepartment().getName();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy", new Locale("en", "IN"));
        for (final AppConfigValues configValue : appConfigList) {
            final String value[] = configValue.getValue().split(",");
            if (ELECTRICAL_DEPARTMENT.equalsIgnoreCase(estimateDept)
                    && value[1].equalsIgnoreCase(ELECTRICAL_DEPARTMENT)) {
                final String date[] = value[0].split("-");
                df = new SimpleDateFormat("dd/MM/yyyy", new Locale("en", "IN"));
                try {
                    final Date startDate = df.parse(date[0]);
                    final Date endDate = df.parse(date[1]);
                    final Date estDate = df.parse(df.format(estimate.getEstimateDate()));
                    if (DateConversionUtil.isWithinDateRange(estDate, startDate, endDate))
                        year = value[2];
                } catch (final ParseException pe) {
                    logger.error("Error in parsing date" + pe.getMessage());
                }
            } else if (!ELECTRICAL_DEPARTMENT.equalsIgnoreCase(estimateDept)
                    && !value[1].equalsIgnoreCase(ELECTRICAL_DEPARTMENT)) {
                final String date[] = value[0].split("-");
                df = new SimpleDateFormat("dd/MM/yyyy", new Locale("en", "IN"));
                try {
                    final Date startDate = df.parse(date[0]);
                    final Date endDate = df.parse(date[1]);
                    final Date estDate = df.parse(df.format(estimate.getEstimateDate()));
                    if (DateConversionUtil.isWithinDateRange(estDate, startDate, endDate))
                        year = value[2];
                } catch (final ParseException pe) {
                    logger.error("Error in parsing date" + pe.getMessage());
                }
            }
        }
        return year;
    }

    public List<String> getAppConfigValuesToSkipBudget() {
        return worksService.getNatureOfWorkAppConfigValues(MODULE_NAME, KEY_NAME);
    }

    public Boolean isSkipBudgetCheck() {
        final List<String> depositTypeList = getAppConfigValuesToSkipBudget();
        if (estimate != null && estimate.getId() != null)
            for (final String type : depositTypeList)
                if (type.equals(estimate.getType().getName()))
                    skipBudget = true;
        return skipBudget;
    }

    public List<BudgetUsage> getBudgetDetailUsage(final String estimateNumber) {
        final List<BudgetUsage> budgetUsageList = persistenceService.findAllBy(
                "from BudgetUsage bu where bu.referenceNumber=?", estimateNumber);
        return budgetUsageList;
    }

    public List<AbstractEstimateAppropriation> getAbstractEstimateAppropriationList() {
        return abstractEstimateAppropriationList;
    }

    public void setAbstractEstimateAppropriationList(
            final List<AbstractEstimateAppropriation> abstractEstimateAppropriationList) {
        this.abstractEstimateAppropriationList = abstractEstimateAppropriationList;
    }

}
