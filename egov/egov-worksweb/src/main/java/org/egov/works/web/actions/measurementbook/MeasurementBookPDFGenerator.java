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
package org.egov.works.web.actions.measurementbook;

import com.lowagie.text.Chunk;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import org.apache.log4j.Logger;
import org.egov.commons.EgwStatus;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.workflow.entity.StateHistory;
import org.egov.infstr.services.PersistenceService;
import org.egov.pims.commons.DeptDesig;
import org.egov.pims.commons.Position;
import org.egov.pims.model.PersonalInformation;
import org.egov.pims.service.EmployeeServiceOld;
import org.egov.works.abstractestimate.entity.Activity;
import org.egov.works.models.measurementbook.MBDetails;
import org.egov.works.models.measurementbook.MBHeader;
import org.egov.works.models.workorder.WorkOrderActivity;
import org.egov.works.revisionestimate.entity.enums.RevisionType;
import org.egov.works.services.MeasurementBookService;
import org.egov.works.services.WorkOrderService;
import org.egov.works.utils.AbstractPDFGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MeasurementBookPDFGenerator extends AbstractPDFGenerator {
    public static final String MEASUREMENTBOOK_PDF_ERROR = "measurementbook.pdf.error";
    private static final Logger logger = Logger.getLogger(MeasurementBookPDFGenerator.class);
    private final Map<String, String> pdfLabel;
    private final MBHeader mbHeader;
    private final NumberFormat formatter = new DecimalFormat("#0.00");
    private final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
    @Autowired
    @Qualifier("persistenceService")
    private PersistenceService persistenceService;
    private MeasurementBookService measurementBookService;
    private WorkOrderService workOrderService;
    @Autowired
    private EmployeeServiceOld employeeService;
    private boolean includeRevisionTypeColumn;

    public MeasurementBookPDFGenerator(final MBHeader mbHeader, final OutputStream out,
                                       final Map<String, String> pdfLabel) {
        super(out, "landscape");
        this.pdfLabel = pdfLabel;
        this.mbHeader = mbHeader;
    }

    public void generatePDF() {
        final String headerText = pdfLabel.get("mbpdf.header");
        try {
            // start header Part
            final Paragraph headerTextPara = new Paragraph(new Chunk(headerText, new Font(Font.UNDEFINED, LARGE_FONT,
                    Font.BOLD)));
            headerTextPara.setAlignment(Element.ALIGN_CENTER);
            document.add(headerTextPara);
            document.add(spacer());
            if (mbHeader != null) {
                String toPageno = "";
                if (mbHeader.getToPageNo() == null || mbHeader.getToPageNo().intValue() == 0)
                    toPageno = mbHeader.getFromPageNo().toString();
                else
                    toPageno = mbHeader.getToPageNo().toString();

                document.add(makeParaWithFont(
                        8,
                        " \t  \t  \t  \t \t "
                                + pdfLabel.get("mbpdf.refno")
                                + mbHeader.getMbRefNo()
                                + " \t  \t  \t  \t \t  \t  \t  \t \t  \t  \t  \t \t  \t  \t  \t \t  \t  \t  \t \t  \t  \t "
                                + pdfLabel.get("mbpdf.pageno")
                                + " : "
                                + mbHeader.getFromPageNo()
                                + " to "
                                + toPageno
                                + " \t  \t  \t  \t \t  \t  \t  \t \t  \t  \t  \t \t  \t  \t  \t \t  \t  \t  \t \t  \t  \t "
                                + pdfLabel.get("mbpdf.date") + sdf.format(mbHeader.getMbDate()),
                        Element.ALIGN_LEFT));

            }
            document.add(spacer());
            includeRevisionTypeColumn = false;
            // /Find if revision type Non tendered and Lump sum items are there
            areNTOrLSItemsPresent(mbHeader);
            // creating label row
            PdfPTable mbTable = createMbTable();
            if (mbHeader != null)
                mbTable = createMbData(mbTable, mbHeader);
            document.add(mbTable);
            document.add(spacer());
            document.newPage();
            // approval details table

            PdfPTable approvaldetailsTable = null;
            if (mbHeader != null)
                approvaldetailsTable = createApprovalDetailsTable(mbHeader);
            document.add(makePara(8, pdfLabel.get("mbpdf.approvaldetails")));
            document.add(spacer());
            document.add(approvaldetailsTable);

            document.close();
        } catch (final DocumentException e) {
            throw new ApplicationRuntimeException(MEASUREMENTBOOK_PDF_ERROR, e);
        }
    }

    private void areNTOrLSItemsPresent(final MBHeader mbHeader) {
        if (mbHeader != null && mbHeader.getMbDetails() != null && !mbHeader.getMbDetails().isEmpty())
            for (final MBDetails mbdetails : mbHeader.getMbDetails())
                if (mbdetails.getWorkOrderActivity() != null && mbdetails.getWorkOrderActivity().getActivity() != null
                        && mbdetails.getWorkOrderActivity().getActivity().getRevisionType() != null)
                    includeRevisionTypeColumn = true;
    }

    private PdfPTable createApprovalDetailsTable(final MBHeader mbHeader) throws DocumentException {
        try {
            final PdfPTable approvaldetailsTable = new PdfPTable(5);
            approvaldetailsTable.setWidthPercentage(100);
            approvaldetailsTable.setWidths(new float[]{2f, 1f, 1f, 1.5f, 2f});
            addRow(approvaldetailsTable, true, makePara(8, pdfLabel.get("mbpdf.aprvalstep")),
                    centerPara(8, pdfLabel.get("mbpdf.name")), centerPara(8, pdfLabel.get("mbpdf.designation")),
                    centerPara(8, pdfLabel.get("mbpdf.aprvdon")), centerPara(8, pdfLabel.get("mbpdf.remarks")));
            List<StateHistory<Position>> history = null;
            String code = "";
            if (mbHeader.getCurrentState() != null && mbHeader.getCurrentState().getHistory() != null)
                history = mbHeader.getStateHistory();
            if (history != null) {
                Collections.reverse(history);
                for (final StateHistory<Position> ad : history)
                    if (!ad.getValue().equals("NEW") && !ad.getValue().equals("END")) {
                        String nextAction = "";
                        if (ad.getNextAction() != null)
                            nextAction = ad.getNextAction();
                        Long positionId = null;
                        String desgName = null;
                        DeptDesig deptdesig = null;
                        positionId = ad.getOwnerPosition().getId();
                        deptdesig = ad.getOwnerPosition().getDeptDesig();
                        desgName = deptdesig.getDesignation().getName();
                        final PersonalInformation emp = employeeService.getEmpForPositionAndDate(ad.getCreatedDate(),
                                Integer.parseInt(positionId.toString()));
                        code = ad.getValue();
                        final EgwStatus status = (EgwStatus) getPersistenceService().find(
                                "from EgwStatus where moduletype=? and code=?", "MBHeader", code);
                        String state = status.getDescription();
                        if (!nextAction.equalsIgnoreCase(""))
                            state = status.getDescription() + " - " + nextAction;
                        addRow(approvaldetailsTable, true, makePara(8, state), makePara(8, emp.getEmployeeName()),
                                makePara(8, desgName), makePara(8, getDateInFormat(ad.getCreatedDate().toString())),
                                rightPara(8, ad.getComments()));
                    }
            }
            return approvaldetailsTable;
        } catch (final Exception e) {
            throw new DocumentException("Exception occured while getting approval details " + e);
        }
    }

    private String getDateInFormat(final String date) throws DocumentException {
        String dateInFormat = null;
        try {
            dateInFormat = new SimpleDateFormat("dd-MMM-yyyy", Locale.US).format(new SimpleDateFormat("yyyy-MM-dd",
                    Locale.US).parse(date));
        } catch (final Exception e) {
            throw new DocumentException("Exception occured while parsing date := " + e);
        }
        return dateInFormat;

    }

    // label row method definition
    private PdfPTable createMbTable() throws DocumentException {
        PdfPTable mbTable;
        if (includeRevisionTypeColumn) {
            mbTable = new PdfPTable(12);
            mbTable.setWidths(new float[]{1f, 1.5f, 4f, 1.4f, 1.9f, 1.6f, 1.4f, 1.8f, 1.9f, 1.9f, 1.9f, 1.6f});
        } else {
            mbTable = new PdfPTable(11);
            mbTable.setWidths(new float[]{1f, 1.5f, 4f, 1.9f, 1.6f, 1.4f, 1.8f, 1.9f, 1.9f, 1.9f, 1.6f});
        }
        // main table
        mbTable.setWidthPercentage(100);

        try {
            final Font font = new Font();
            font.setSize(8);
            mbTable.getDefaultCell().setPadding(3);
            mbTable.getDefaultCell().setBorderWidth(1);
            mbTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
            mbTable.addCell(new PdfPCell(new Phrase(pdfLabel.get("mbpdf.slno"), font)));
            mbTable.addCell(new PdfPCell(new Phrase(pdfLabel.get("mbpdf.schno"), font)));
            mbTable.addCell(new PdfPCell(new Phrase(pdfLabel.get("mbpdf.descofwork"), font)));
            if (includeRevisionTypeColumn)
                mbTable.addCell(new PdfPCell(new Phrase(pdfLabel.get("mbpdf.revisiontype"), font)));
            mbTable.addCell(new PdfPCell(new Phrase(pdfLabel.get("mbpdf.completedmeasurement"), font)));
            mbTable.addCell(new PdfPCell(new Phrase(pdfLabel.get("mbpdf.unitrate"), font)));
            mbTable.addCell(new PdfPCell(new Phrase(pdfLabel.get("mbpdf.unit"), font)));
            mbTable.addCell(new PdfPCell(new Phrase(pdfLabel.get("mbpdf.totalvalueofcomplwork"), font)));

            // start creating tables for previous measurements
            final PdfPTable previousMbTable = createPreviousMbTable();
            final PdfPCell previousMbCell = new PdfPCell(previousMbTable);
            previousMbCell.setColspan(2);
            mbTable.addCell(previousMbCell);

            mbTable.addCell(new PdfPCell(new Phrase(pdfLabel.get("mbpdf.currentmeasurement"), font)));

            // last column
            mbTable.addCell(new PdfPCell(new Phrase(pdfLabel.get("mbpdf.currentcost"), font)));
        } catch (final RuntimeException e) {
            throw new ApplicationRuntimeException(MEASUREMENTBOOK_PDF_ERROR, e);
        }
        return mbTable;
    }

    // creating table for previous mb
    public PdfPTable createPreviousMbTable() {
        final PdfPTable previousMbTable = new PdfPTable(2);
        final Font font = new Font();
        font.setSize(8);
        previousMbTable.getDefaultCell().setBorderWidth(1);
        previousMbTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        previousMbTable.getDefaultCell().setColspan(2);
        previousMbTable.addCell(new PdfPCell(new Phrase(pdfLabel.get("mbpdf.previousmeasurement"), font)));
        previousMbTable.getDefaultCell().setColspan(1);
        previousMbTable.addCell(new PdfPCell(new Phrase(pdfLabel.get("mbpdf.pageno"), font)));
        previousMbTable.addCell(new PdfPCell(new Phrase(pdfLabel.get("mbpdf.measurements"), font)));
        return previousMbTable;
    }

    // for creating mbheader data
    private PdfPTable createMbData(final PdfPTable mbTable, final MBHeader mbHeader) {
        Integer i = 0;
        double uomFactor = 0.0;

        // iterating mbdetails
        for (final MBDetails mbDetails : mbHeader.getMbDetails()) {
            String description = "";
            String per = "";
            String schNo = "";
            double currentMeasurement;
            currentMeasurement = mbDetails.getQuantity();
            ++i;
            mbTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
            mbTable.addCell(rightPara(8, i.toString()));
            final WorkOrderActivity workOrderActivity = mbDetails.getWorkOrderActivity();
            final Activity activity = workOrderActivity.getActivity();
            if (activity != null) {
                if (activity.getSchedule() != null && activity.getSchedule().getCode() != null)
                    schNo = activity.getSchedule().getCode();
                mbTable.addCell(rightPara(8, schNo));

                // start sor/non sor description
                if (activity.getSchedule() != null && activity.getSchedule().getDescription() != null)
                    description = activity.getSchedule().getDescription();

                if (activity.getNonSor() != null && activity.getNonSor().getDescription() != null)
                    description = activity.getNonSor().getDescription();

                mbTable.addCell(makeParaWithFont(8, description, Element.ALIGN_LEFT));
                // end sor/non sor description
            }
            // for completedMeasurement area --------------->Cumulative quantity
            // including current entry= Cumulative upto previous entry + Current
            // MB entry
            // ( cumulative MB measurement for line item) for selected MB
            // including MB entry

            if (includeRevisionTypeColumn) {
                if (activity.getRevisionType() == null)
                    mbTable.addCell(makePara(8, ""));
                if (activity.getRevisionType() != null
                        && activity.getRevisionType().toString()
                        .equalsIgnoreCase(RevisionType.NON_TENDERED_ITEM.toString()))
                    mbTable.addCell(makePara(8, "Non Tendered"));
                if (activity.getRevisionType() != null
                        && activity.getRevisionType().toString()
                        .equalsIgnoreCase(RevisionType.LUMP_SUM_ITEM.toString()))
                    mbTable.addCell(makePara(8, "Lump Sum"));
            }

            double completedMeasurement;
            double cumlPrevMb;
            try {

                long woaId = 0l;
                if (workOrderActivity.getId() != null)
                    woaId = workOrderActivity.getId();

                cumlPrevMb = measurementBookService.prevCumulativeQuantityIncludingCQ(woaId, mbHeader.getId(),
                        workOrderActivity.getActivity().getId(), mbHeader.getWorkOrder());

            } catch (final Exception e) {
                cumlPrevMb = 0.0;
            }
            completedMeasurement = cumlPrevMb + currentMeasurement;
            mbTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
            mbTable.addCell(rightPara(8, completedMeasurement));

            double approveRateWo = workOrderActivity.getApprovedRate();
            mbTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
            mbTable.addCell(rightPara(8, formatter.format(approveRateWo)));

            // start unit
            if (activity != null) {
                // umofactor for conversion of rate and amount
                uomFactor = activity.getConversionFactor();
                if (logger.isDebugEnabled())
                    logger.debug("----------uomFactor------------" + uomFactor);

                mbTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
                if (activity.getSchedule() != null && activity.getSchedule().getUom() != null
                        && activity.getSchedule().getUom().getUom() != null)
                    per = activity.getSchedule().getUom().getUom();
                if (activity.getNonSor() != null && activity.getNonSor().getUom() != null
                        && activity.getNonSor().getUom().getUom() != null)
                    per = activity.getNonSor().getUom().getUom();
                mbTable.addCell(centerPara(8, per));
                // end start unit
            }       // end of if activity

            /*
             * measurementBookService.prevCumulativeAmount(workOrderActivity.getId ()); total work completed------->(completed
             * mesurement(col 5) * rate) here rate is wo.getAprovedrate added uom factor on april4th 2010
             */
            final double workCompleted = completedMeasurement * approveRateWo * uomFactor;
            mbTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
            mbTable.addCell(rightPara(8, formatter.format(workCompleted)));

            // previous measurements a)pageno and b)measurements
            // a)Page no: for last MB entry for forline item---->page-no call
            // api
            Integer frompageNo = null;
            Integer topageNo = null;

            final MBHeader resultHeader = workOrderService.findLastMBPageNoForLineItem(workOrderActivity,
                    mbHeader.getId());
            if (resultHeader != null) {
                frompageNo = resultHeader.getFromPageNo();
                topageNo = resultHeader.getToPageNo();
            }

            String pageNoInfo = "";
            if (frompageNo != null)
                pageNoInfo = resultHeader.getMbRefNo() + "/" + frompageNo.toString();
            if (topageNo != null)
                pageNoInfo = pageNoInfo + "-" + topageNo;

            mbTable.addCell(rightPara(8, pageNoInfo));
            // b)Cumulative measurement recorded for the previous MB entry for
            // line item( Cumulative measurements-current MB entry)
            mbTable.addCell(rightPara(8, cumlPrevMb));

            // Current Finalised Measurements a)Current MB entry and b) Column6
            // Estimate Percentage
            // a)Current MB entry---->Measurements (Col5-8) i.e (area-previous
            // measurement)

            mbTable.addCell(rightPara(8, currentMeasurement));

            // current cost
            double currentCost;
            currentCost = currentMeasurement * approveRateWo * uomFactor;
            mbTable.addCell(rightPara(8, formatter.format(currentCost)));
            // } //end of if mbDetails
        }      // end of for loop
        return mbTable;
    }

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

    public void setMeasurementBookService(final MeasurementBookService measurementBookService) {
        this.measurementBookService = measurementBookService;
    }

    public void setWorkOrderService(final WorkOrderService workOrderService) {
        this.workOrderService = workOrderService;
    }
}
