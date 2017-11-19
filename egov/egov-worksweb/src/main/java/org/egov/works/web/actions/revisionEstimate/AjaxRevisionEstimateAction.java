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
package org.egov.works.web.actions.revisionEstimate;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.web.struts.actions.BaseFormAction;
import org.egov.works.abstractestimate.entity.AbstractEstimate;
import org.egov.works.models.measurementbook.MBDetails;
import org.egov.works.models.measurementbook.MBHeader;
import org.egov.works.models.workorder.WorkOrder;
import org.egov.works.models.workorder.WorkOrderActivity;
import org.egov.works.models.workorder.WorkOrderEstimate;
import org.egov.works.revisionestimate.entity.enums.RevisionType;
import org.egov.works.services.MeasurementBookService;
import org.egov.works.services.WorksService;
import org.hibernate.Query;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class AjaxRevisionEstimateAction extends BaseFormAction {

    private static final long serialVersionUID = -6192212773360994495L;
    private static final Logger logger = Logger.getLogger(AjaxRevisionEstimateAction.class);
    private static final String ACTIVITY_DETAILS = "activityDetails";
    private String query = "";
    private List<AbstractEstimate> estimateList = new LinkedList<AbstractEstimate>();
    private List<AbstractEstimate> estimateNoList = new LinkedList<AbstractEstimate>();
    private List<WorkOrder> workOrderList = new LinkedList<WorkOrder>();
    private WorksService worksService;
    private Long reWOEstId;
    private Long revEstId;
    private static final String CANCEL_REVISIONESTIMATE = "cancelRE";
    private static final String REV_ESTIMATE_LIST = "revisionEstList";
    private List<MBHeader> approvedMBList = new ArrayList<MBHeader>();
    private MeasurementBookService measurementBookService;
    private Double prevCulmEntry;
    private WorkOrderActivity workOrderActivity;
    private Long woActivityId;
    private Double totalEstQuantity;
    private Long estimateId;
    private String errorMessage;
    private static final String VALIDATE_CANCEL = "validateCancel";

    @Override
    public Object getModel() {
        return null;
    }

    public String searchEstimateNumber() {
        String strquery = "";
        final ArrayList<Object> params = new ArrayList<Object>();
        if (!StringUtils.isEmpty(query)) {
            strquery = "select woe.estimate from WorkOrderEstimate woe where woe.workOrder.parent is null and woe.workOrder.egwStatus.code<>? "
                    + "and woe.workOrder.egwStatus.code = ? and woe.estimate.parent is null and woe.estimate.estimateNumber like '%'||?||'%' "
                    + " and woe.id not in (select distinct mbh.workOrderEstimate.id from MBHeader mbh where"
                    + " mbh.egwStatus.code = ? and (mbh.egBillregister.billstatus <> ? and mbh.egBillregister.billtype = ?) and"
                    + " mbh.workOrderEstimate.workOrder.egwStatus.code='APPROVED' and mbh.workOrderEstimate.estimate.egwStatus.code=?)";
            params.add("NEW");
            params.add("APPROVED");
            params.add(query.toUpperCase());
            params.add(MBHeader.MeasurementBookStatus.APPROVED.toString());
            params.add(MBHeader.MeasurementBookStatus.CANCELLED.toString());
            params.add(getFinalBillTypeConfigValue());
            params.add(AbstractEstimate.EstimateStatus.ADMIN_SANCTIONED.toString());

            estimateList = getPersistenceService().findAllBy(strquery, params.toArray());
        }
        return "estimateNoSearchResults";
    }

    public String activityDetails() {
        prevCulmEntry = null;
        try {
            workOrderActivity = (WorkOrderActivity) persistenceService.find("from WorkOrderActivity where id=?",
                    woActivityId);
            prevCulmEntry = measurementBookService.prevCumulativeQuantityIncludingCQ(woActivityId, null,
                    workOrderActivity.getActivity().getId(), workOrderActivity.getWorkOrderEstimate().getWorkOrder());
            totalEstQuantity = measurementBookService.totalEstimatedQuantityForRE(woActivityId, null, workOrderActivity
                    .getActivity().getId(), workOrderActivity.getWorkOrderEstimate().getWorkOrder());
        } catch (final Exception e) {
            throw new ApplicationRuntimeException("activity.find.error", e);
        }

        return ACTIVITY_DETAILS;
    }

    public String searchWorkOrderNumber() {
        String strquery = "";
        final ArrayList<Object> params = new ArrayList<Object>();
        if (!StringUtils.isEmpty(query)) {
            strquery = "select distinct woe.workOrder from WorkOrderEstimate woe where woe.workOrder.parent is null and woe.workOrder.egwStatus.code<>? "
                    + "and woe.workOrder.egwStatus.code = ? and woe.estimate.parent is null and woe.workOrder.workOrderNumber like '%'||?||'%' "
                    + "and woe.id not in (select distinct mbh.workOrderEstimate.id from MBHeader mbh where"
                    + " mbh.egwStatus.code = ? and (mbh.egBillregister.billstatus <> ? and mbh.egBillregister.billtype = ?) and"
                    + " mbh.workOrderEstimate.workOrder.egwStatus.code='APPROVED' and mbh.workOrderEstimate.estimate.egwStatus.code=?)";
            params.add("NEW");
            params.add("APPROVED");
            params.add(query.toUpperCase());
            params.add(MBHeader.MeasurementBookStatus.APPROVED.toString());
            params.add(MBHeader.MeasurementBookStatus.CANCELLED.toString());
            params.add(getFinalBillTypeConfigValue());
            params.add(AbstractEstimate.EstimateStatus.ADMIN_SANCTIONED.toString());

            workOrderList = getPersistenceService().findAllBy(strquery, params.toArray());
        }
        return "workOrderNoSearchResults";
    }

    public String searchRevisionEstimateNumber() {
        String strquery = "";
        final ArrayList<Object> params = new ArrayList<Object>();
        if (!StringUtils.isEmpty(query)) {
            strquery = " from AbstractEstimate ae where ae.parent is not null and ae.egwStatus.code<>? and ae.estimateNumber like '%'||?||'%'";
            params.add("NEW");
            params.add(query.toUpperCase());
            estimateList = getPersistenceService().findAllBy(strquery, params.toArray());
        }
        return "estimateNoSearchResults";
    }

    public String searchREWorkOrderNumber() {
        String strquery = "";
        final ArrayList<Object> params = new ArrayList<Object>();
        if (!StringUtils.isEmpty(query)) {
            strquery = "select distinct woe.workOrder.parent from WorkOrderEstimate woe where woe.workOrder.parent is not null and woe.estimate.egwStatus.code<>? "
                    +
                    // " woe.workOrder.id in(select wo.parent.id from WorkOrder where woe.workOrder.parent is not null)"
                    // +
                    "and woe.workOrder.parent.workOrderNumber like '%'||?||'%' ";
            params.add("NEW");
            params.add(query.toUpperCase());

            workOrderList = getPersistenceService().findAllBy(strquery, params.toArray());
        }
        return "workOrderNoSearchResults";
    }

    public String searchApprovedWONumberForRE() {
        String strquery = "";
        final ArrayList<Object> params = new ArrayList<Object>();
        if (!StringUtils.isEmpty(query)) {
            strquery = "select distinct woe.workOrder.parent from WorkOrderEstimate woe where woe.workOrder.parent is not null and woe.estimate.egwStatus.code=? "
                    + "and woe.workOrder.parent.workOrderNumber like '%'||?||'%' ";
            params.add("APPROVED");
            params.add(query.toUpperCase());

            workOrderList = getPersistenceService().findAllBy(strquery, params.toArray());
        }
        return "workOrderNoSearchResults";
    }

    public String getMBDetailsForRE() throws Exception {
        List<MBHeader> mbheaderlist = new ArrayList<MBHeader>();
        mbheaderlist = measurementBookService.findAllBy(
                "select distinct mbd.mbHeader from MBDetails mbd where mbd.workOrderActivity.workOrderEstimate.id=? "
                        + "and mbd.mbHeader.egwStatus.code<>'CANCELLED'",
                reWOEstId);

        if (mbheaderlist != null && !mbheaderlist.isEmpty())
            approvedMBList.addAll(mbheaderlist);

        return CANCEL_REVISIONESTIMATE;
    }

    public String validateCancellation() {
        errorMessage = "";
        if (reWOEstId != null) {
            final WorkOrderEstimate revWOEst = (WorkOrderEstimate) persistenceService.find(
                    " from WorkOrderEstimate where id = ? ", reWOEstId);
            final WorkOrder revWorkOrder = revWOEst.getWorkOrder();
            final AbstractEstimate revEstimate = revWOEst.getEstimate();

            final List<WorkOrderActivity> revWoaList = persistenceService.findAllBy(
                    "from WorkOrderActivity where workOrderEstimate.workOrder.id=?", revWorkOrder.getId());
            final List<Long> activtityIdList = new ArrayList<Long>();
            List<MBHeader> mbheaderlist = new ArrayList<MBHeader>();
            // First check if any non tendered or lumpsum items are present, if
            // yes then dont allow to cancel
            for (final WorkOrderActivity revWoa : revWoaList)
                if (revWoa.getActivity().getRevisionType() != null
                        && (revWoa.getActivity().getRevisionType().equals(RevisionType.LUMP_SUM_ITEM) || revWoa
                                .getActivity().getRevisionType().equals(RevisionType.NON_TENDERED_ITEM))) {
                    mbheaderlist = measurementBookService
                            .findAllBy(
                                    "select distinct mbd.mbHeader from MBDetails mbd where mbd.workOrderActivity.workOrderEstimate.estimate.id=? and mbd.workOrderActivity.workOrderEstimate.workOrder.id=? and  mbd.workOrderActivity.activity.id=? "
                                            + "and mbd.mbHeader.egwStatus.code<>'CANCELLED'",
                                    revEstimate.getId(),
                                    revWorkOrder.getId(), revWoa.getActivity().getId());
                    if (mbheaderlist != null && !mbheaderlist.isEmpty()) {
                        final StringBuffer mbNos = new StringBuffer();
                        for (final MBHeader mbHdr : mbheaderlist)
                            mbNos.append(mbHdr.getMbRefNo() + ", ");
                        errorMessage = getText("cancelRE.MB.created.message")
                                + mbNos.toString().substring(0, mbNos.length() - 2) + ". "
                                + getText("cancelRE.MB.created.message.part2");
                        return VALIDATE_CANCEL;
                    }
                }
            if (revWoaList != null && revWoaList.size() > 0) {
                for (final WorkOrderActivity revWoa : revWoaList)
                    // Add only additional quantity items activities
                    if (revWoa.getActivity().getRevisionType() != null
                            && revWoa.getActivity().getRevisionType().equals(RevisionType.ADDITIONAL_QUANTITY))
                        activtityIdList.add(revWoa.getActivity().getParent().getId()); // Passing
                // parent
                // of
                // Rev
                // Work
                // order
                // activity,
                // as
                // only
                // these
                // can
                // have
                // MBs
                // created
                // for
                // them
                if (activtityIdList != null && activtityIdList.size() > 0) {
                    final Query qry = getPersistenceService()
                            .getSession()
                            .createQuery(
                                    " select workOrderActivity.activity.id, nvl(sum(quantity),0)  from MBDetails where mbHeader.egwStatus.code!='CANCELLED' and workOrderActivity.activity.id in (:activtityIdList) group by workOrderActivity.activity.id ");
                    qry.setParameterList("activtityIdList", activtityIdList);
                    final List<Object[]> activityIdQuantityList = qry.list();
                    if (activityIdQuantityList != null && activityIdQuantityList.size() > 0)
                        for (final WorkOrderActivity revWoa : revWoaList) {
                            if (revWoa.getActivity().getRevisionType() != null
                                    && !revWoa.getActivity().getRevisionType().equals(RevisionType.ADDITIONAL_QUANTITY))
                                continue;
                            for (final Object[] activityIdQuantity : activityIdQuantityList)
                                if (Long.parseLong(activityIdQuantity[0].toString()) == revWoa.getActivity()
                                        .getParent().getId().longValue()) {
                                    Long activityId = null;
                                    if (revWoa.getActivity().getParent() == null)
                                        activityId = revWoa.getActivity().getId();
                                    else
                                        activityId = revWoa.getActivity().getParent().getId();
                                    final double originalQuantity = (Double) persistenceService
                                            .find("select sum(woa.approvedQuantity) from WorkOrderActivity woa  group by woa,woa.activity having activity.id = ?",
                                                    activityId);
                                    final Object revEstQuantityObj = persistenceService
                                            .find(" select sum(woa.approvedQuantity*nvl(decode(woa.activity.revisionType,'REDUCED_QUANTITY',-1,'ADDITIONAL_QUANTITY',1,'NON_TENDERED_ITEM',1,'LUMP_SUM_ITEM',1),1)) from WorkOrderActivity woa where woa.activity.abstractEstimate.egwStatus.code = 'APPROVED'  and woa.activity.abstractEstimate.id != ? group by woa.activity.parent having (woa.activity.parent is not null and woa.activity.parent.id = ? )  ",
                                                    revEstimate.getId(), revWoa.getActivity().getParent().getId());
                                    final double revEstQuantity = revEstQuantityObj == null ? 0.0
                                            : (Double) revEstQuantityObj;
                                    if (originalQuantity + revEstQuantity >= Double.parseDouble(activityIdQuantity[1]
                                            .toString()))
                                        continue;
                                    else {
                                        final MBDetails mbDetails = (MBDetails) persistenceService
                                                .find(" from MBDetails mbd where mbd.mbHeader.egwStatus.code != 'CANCELLED' and mbd.workOrderActivity.activity.id = ? and (mbdetailsDate is not null or OrderNumber is not null) ",
                                                        revWoa.getActivity().getParent().getId());
                                        if (mbDetails != null) {
                                            Double maxPercent = worksService.getConfigval();
                                            if (maxPercent != null)
                                                maxPercent += 100;
                                            else
                                                maxPercent = 100d;
                                            final Double maxAllowedQuantity = maxPercent
                                                    * (originalQuantity + revEstQuantity) / 100;
                                            if (maxAllowedQuantity >= Double.parseDouble(activityIdQuantity[1]
                                                    .toString()))
                                                continue;
                                            else {
                                                errorMessage = getText("cancelRE.MBs.present.message");
                                                return VALIDATE_CANCEL;
                                            }
                                        } else {
                                            errorMessage = getText("cancelRE.MBs.present.message");
                                            return VALIDATE_CANCEL;
                                        }
                                    }
                                }
                        }
                }
            }
        }
        return VALIDATE_CANCEL;
    }

    public String getListOfREsForParent() throws Exception {
        estimateNoList = getPersistenceService().findAllBy(
                "select distinct abs.estimateNumber from AbstractEstimate abs where abs.parent.id=? "
                        + "and abs.egwStatus.code<>'CANCELLED' order by abs.estimateNumber ",
                estimateId);

        return REV_ESTIMATE_LIST;
    }

    public String getFinalBillTypeConfigValue() {
        return worksService.getWorksConfigValue("FinalBillType");
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(final String query) {
        this.query = query;
    }

    public void setWorksService(final WorksService worksService) {
        this.worksService = worksService;
    }

    public List<AbstractEstimate> getEstimateList() {
        return estimateList;
    }

    public void setEstimateList(final List<AbstractEstimate> estimateList) {
        this.estimateList = estimateList;
    }

    public List<WorkOrder> getWorkOrderList() {
        return workOrderList;
    }

    public void setWorkOrderList(final List<WorkOrder> workOrderList) {
        this.workOrderList = workOrderList;
    }

    public Long getReWOEstId() {
        return reWOEstId;
    }

    public void setReWOEstId(final Long reWOEstId) {
        this.reWOEstId = reWOEstId;
    }

    public List<MBHeader> getApprovedMBList() {
        return approvedMBList;
    }

    public void setApprovedMBList(final List<MBHeader> approvedMBList) {
        this.approvedMBList = approvedMBList;
    }

    public void setMeasurementBookService(final MeasurementBookService measurementBookService) {
        this.measurementBookService = measurementBookService;
    }

    public Double getPrevCulmEntry() {
        return prevCulmEntry;
    }

    public Long getWoActivityId() {
        return woActivityId;
    }

    public Double getTotalEstQuantity() {
        return totalEstQuantity;
    }

    public void setWoActivityId(final Long woActivityId) {
        this.woActivityId = woActivityId;
    }

    public WorkOrderActivity getWorkOrderActivity() {
        return workOrderActivity;
    }

    public void setWorkOrderActivity(final WorkOrderActivity workOrderActivity) {
        this.workOrderActivity = workOrderActivity;
    }

    public void setEstimateId(final Long estimateId) {
        this.estimateId = estimateId;
    }

    public List<AbstractEstimate> getEstimateNoList() {
        return estimateNoList;
    }

    public Long getRevEstId() {
        return revEstId;
    }

    public void setRevEstId(final Long revEstId) {
        this.revEstId = revEstId;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(final String errorMessage) {
        this.errorMessage = errorMessage;
    }

}