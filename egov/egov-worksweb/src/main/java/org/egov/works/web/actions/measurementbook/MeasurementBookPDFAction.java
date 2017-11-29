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

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.egov.commons.EgwStatus;
import org.egov.infra.reporting.engine.ReportOutput;
import org.egov.infra.reporting.engine.ReportRequest;
import org.egov.infra.reporting.engine.ReportService;
import org.egov.infra.web.struts.actions.BaseFormAction;
import org.egov.infra.workflow.entity.StateHistory;
import org.egov.pims.commons.DeptDesig;
import org.egov.pims.commons.Position;
import org.egov.pims.model.PersonalInformation;
import org.egov.pims.service.EmployeeServiceOld;
import org.egov.works.abstractestimate.entity.Activity;
import org.egov.works.models.measurementbook.ApprovalDetails;
import org.egov.works.models.measurementbook.MBDetails;
import org.egov.works.models.measurementbook.MBHeader;
import org.egov.works.models.measurementbook.MeasurementBookPDF;
import org.egov.works.models.workorder.WorkOrderActivity;
import org.egov.works.revisionestimate.entity.enums.RevisionType;
import org.egov.works.services.MeasurementBookService;
import org.egov.works.services.WorkOrderService;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.egov.infra.utils.DateUtils.toDefaultDateFormat;

@Result(name = BaseFormAction.SUCCESS, type = "stream", location = "measurementBookPDF", params = {
        "inputName", "measurementBookPDF", "contentType", "application/pdf", "contentDisposition",
        "no-cache;filename=MeasurementBook.pdf"})
@ParentPackage("egov")
public class MeasurementBookPDFAction extends BaseFormAction {

    private static final long serialVersionUID = 3881748750533287264L;

    private static final Logger logger = Logger.getLogger(MeasurementBookPDFAction.class);

    private Long measurementBookId;
    private transient InputStream measurementBookPDF;
    private transient MeasurementBookService measurementBookService;
    private transient WorkOrderService workOrderService;
    @Autowired
    private transient EmployeeServiceOld employeeService;
    private transient ReportService reportService;

    @Override
    public String execute() {
        if (measurementBookId != null) {
            final MBHeader mbHeader = getMBHeader();
            ReportRequest reportRequest;
            if (areNTOrLSItemsPresent(mbHeader))
                reportRequest = new ReportRequest("mbWithRevisionType", createMbData(mbHeader), getParamMap(mbHeader));
            else
                reportRequest = new ReportRequest("measurementBook", createMbData(mbHeader), getParamMap(mbHeader));
            final ReportOutput reportOutput = reportService.createReport(reportRequest);
            if (reportOutput != null && reportOutput.getReportOutputData() != null)
                measurementBookPDF = new ByteArrayInputStream(reportOutput.getReportOutputData());
        }
        return SUCCESS;
    }

    private Map<String, Object> getParamMap(final MBHeader mbHeader) {
        final Map<String, Object> reportParams = new HashMap<>();
        reportParams.put("mbNumber", mbHeader.getMbRefNo());
        reportParams.put("pageNumber", mbHeader.getFromPageNo()
                + (mbHeader.getToPageNo() == null ? "" : " to " + mbHeader.getToPageNo()));
        reportParams.put("mbDate", toDefaultDateFormat(mbHeader.getMbDate()));
        reportParams.put("reportTitle", getText("page.title.measurement.book"));
        reportParams.put("approvalDetails", createApprovalDetailsTable(mbHeader));
        return reportParams;
    }

    private List<Object> createMbData(final MBHeader mbHeader) {
        double uomFactor = 0.0;
        final List<Object> mbPDFList = new ArrayList<>();
        for (final MBDetails mbDetails : mbHeader.getMbDetails()) {
            final MeasurementBookPDF mbPDF = new MeasurementBookPDF();
            String description = "";
            String per = "";
            String schNo = "";
            double currentMeasurement = mbDetails.getQuantity();
            final WorkOrderActivity workOrderActivity = mbDetails.getWorkOrderActivity();
            final Activity activity = workOrderActivity.getActivity();
            if (activity != null) {
                if (activity.getSchedule() != null && activity.getSchedule().getCode() != null)
                    schNo = activity.getSchedule().getCode();
                mbPDF.setScheduleNo(schNo);

                // start sor/non sor description
                if (activity.getSchedule() != null && activity.getSchedule().getDescription() != null)
                    description = activity.getSchedule().getDescription();

                if (activity.getNonSor() != null && activity.getNonSor().getDescription() != null)
                    description = activity.getNonSor().getDescription();
                mbPDF.setWorkDescription(description);
                if (activity.getRevisionType() != null
                        && activity.getRevisionType().toString()
                        .equalsIgnoreCase(RevisionType.NON_TENDERED_ITEM.toString()))
                    mbPDF.setRevisionType("Non Tendered");
                if (activity.getRevisionType() != null
                        && activity.getRevisionType().toString()
                        .equalsIgnoreCase(RevisionType.LUMP_SUM_ITEM.toString()))
                    mbPDF.setRevisionType("Lump Sum");
            }

            // for completedMeasurement area --------------->Cumulative quantity
            // including current entry= Cumulative upto previous entry + Current
            // MB entry
            // ( cumulative MB measurement for line item) for selected MB
            // including MB entry

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
            mbPDF.setCompletedMeasurement(completedMeasurement);

            double approveRateWo;
            approveRateWo = workOrderActivity.getApprovedRate();
            mbPDF.setUnitRate(approveRateWo);

            // start unit
            if (activity != null) {
                // umofactor for conversion of rate and amount
                uomFactor = workOrderActivity.getConversionFactor();
                if (logger.isDebugEnabled())
                    logger.debug("----------uomFactor------------" + uomFactor);

                if (activity.getSchedule() != null && activity.getSchedule().getUom() != null
                        && activity.getSchedule().getUom().getUom() != null)
                    per = activity.getSchedule().getUom().getUom();
                if (activity.getNonSor() != null && activity.getNonSor().getUom() != null
                        && activity.getNonSor().getUom().getUom() != null)
                    per = activity.getNonSor().getUom().getUom();
                mbPDF.setUom(per);
                // end start unit
            }       // end of if activity

            final double workCompleted = completedMeasurement * approveRateWo * uomFactor;
            mbPDF.setCompletedCost(workCompleted);

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
            mbPDF.setPageNo(pageNoInfo);

            // b)Cumulative measurement recorded for the previous MB entry for
            // line item( Cumulative measurements-current MB entry)
            mbPDF.setPrevMeasurement(cumlPrevMb);

            // Current Finalised Measurements a)Current MB entry and b) Column6
            // Estimate Percentage
            // a)Current MB entry---->Measurements (Col5-8) i.e (area-previous
            // measurement)
            mbPDF.setCurrentMeasurement(currentMeasurement);

            // current cost
            double currentCost;
            currentCost = currentMeasurement * approveRateWo * uomFactor;
            mbPDF.setCurrentCost(currentCost);
            mbPDFList.add(mbPDF);
        }
        return mbPDFList;
    }

    // Are nontendered or lumpsum items present
    private boolean areNTOrLSItemsPresent(final MBHeader mbHeader) {
        if (mbHeader != null && mbHeader.getMbDetails() != null && !mbHeader.getMbDetails().isEmpty())
            for (final MBDetails mbdetails : mbHeader.getMbDetails())
                if (mbdetails.getWorkOrderActivity() != null && mbdetails.getWorkOrderActivity().getActivity() != null
                        && mbdetails.getWorkOrderActivity().getActivity().getRevisionType() != null)
                    return true;
        return false;
    }

    private List<ApprovalDetails> createApprovalDetailsTable(final MBHeader mbHeader) {
        try {
            List<StateHistory<Position>> history = new ArrayList<>();
            String code;
            final List<ApprovalDetails> approvalDetList = new ArrayList<>();
            if (mbHeader.getCurrentState() != null && mbHeader.getCurrentState().getHistory() != null)
                history = mbHeader.getStateHistory();
            if (history != null) {
                Collections.reverse(history);
                for (final StateHistory<Position> state : history)
                    if (!state.getValue().equals("NEW") && !state.getValue().equals("END")) {
                        final ApprovalDetails approvalDet = new ApprovalDetails();
                        String nextAction = "";
                        if (state.getNextAction() != null)
                            nextAction = state.getNextAction();
                        Long positionId = null;
                        String desgName = null;
                        DeptDesig deptdesig = null;
                        positionId = state.getOwnerPosition().getId();
                        deptdesig = state.getOwnerPosition().getDeptDesig();
                        desgName = deptdesig.getDesignation().getName();

                        final PersonalInformation emp = employeeService.getEmpForPositionAndDate(
                                state.getCreatedDate(), Integer.parseInt(positionId.toString()));

                        code = state.getValue();
                        final EgwStatus status = (EgwStatus) getPersistenceService().find(
                                "from EgwStatus where moduletype=? and code=?", "MBHeader", code);
                        String statusDesc = status.getDescription();
                        if (!nextAction.equalsIgnoreCase(""))
                            statusDesc = status.getDescription() + " - " + nextAction;
                        approvalDet.setStatusDesc(statusDesc);
                        approvalDet.setEmplName(emp.getEmployeeName());
                        approvalDet.setDesgName(desgName);
                        approvalDet.setDate(state.getCreatedDate());
                        approvalDet.setText(state.getComments());
                        approvalDetList.add(approvalDet);
                    }
            }
            return approvalDetList;
        } catch (final Exception e) {
            return Collections.emptyList();
        }
    }

    public MBHeader getMBHeader() {
        return measurementBookService.findById(measurementBookId, false);
    }

    public InputStream getMeasurementBookPDF() {
        return measurementBookPDF;
    }

    @Override
    public Object getModel() {
        return null;
    }

    public void setMeasurementBookId(final Long measurementBookId) {
        this.measurementBookId = measurementBookId;
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

    public void setReportService(final ReportService reportService) {
        this.reportService = reportService;
    }
}