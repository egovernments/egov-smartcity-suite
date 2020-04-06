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

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.lang.StringUtils;
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

public class AjaxRevisionEstimateAction extends BaseFormAction {

    private static final long serialVersionUID = -6192212773360994495L;
    private static final String ACTIVITY_DETAILS = "activityDetails";
    private String query = "";
    private List<AbstractEstimate> estimateList = new LinkedList<>();
    private List<AbstractEstimate> estimateNoList = new LinkedList<>();
    private List<WorkOrder> workOrderList = new LinkedList<>();
    private WorksService worksService;
    private Long reWOEstId;
    private Long revEstId;
    private static final String CANCEL_REVISIONESTIMATE = "cancelRE";
    private static final String REV_ESTIMATE_LIST = "revisionEstList";
    private List<MBHeader> approvedMBList = new ArrayList<>();
    private MeasurementBookService measurementBookService;
    private Double prevCulmEntry;
    private WorkOrderActivity workOrderActivity;
    private Long woActivityId;
    private Double totalEstQuantity;
    private Long estimateId;
    private String errorMessage;
    private static final String VALIDATE_CANCEL = "validateCancel";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Object getModel() {
        return null;
    }

    public String searchEstimateNumber() {
        final StringBuffer strquery = new StringBuffer();
        if (!StringUtils.isEmpty(query)) {
            strquery.append("select woe.estimate")
                    .append(" from WorkOrderEstimate woe")
                    .append(" where woe.workOrder.parent is null and woe.workOrder.egwStatus.code <> :woStatus ")
                    .append("and woe.workOrder.egwStatus.code = :woeStatus and woe.estimate.parent is null")
                    .append(" and woe.estimate.estimateNumber like '%'||:estimateNumber||'%' ")
                    .append(" and woe.id not in (select distinct mbh.workOrderEstimate.id from MBHeader mbh where")
                    .append(" mbh.egwStatus.code = :mbhStatus and (mbh.egBillregister.billstatus <> :billStatus")
                    .append(" and mbh.egBillregister.billtype = :billtype) and mbh.workOrderEstimate.workOrder.egwStatus.code='APPROVED'")
                    .append(" and mbh.workOrderEstimate.estimate.egwStatus.code = :estimateStatus)");

            estimateList = entityManager.createQuery(strquery.toString(), AbstractEstimate.class)
                    .setParameter("woStatus", "NEW")
                    .setParameter("woeStatus", "APPROVED")
                    .setParameter("estimateNumber", query.toUpperCase())
                    .setParameter("mbhStatus", MBHeader.MeasurementBookStatus.APPROVED.toString())
                    .setParameter("billStatus", MBHeader.MeasurementBookStatus.CANCELLED.toString())
                    .setParameter("billtype", getFinalBillTypeConfigValue())
                    .setParameter("estimateStatus", AbstractEstimate.EstimateStatus.ADMIN_SANCTIONED.toString())
                    .getResultList();

        }
        return "estimateNoSearchResults";
    }

    public String activityDetails() {
        prevCulmEntry = null;
        try {
            workOrderActivity = entityManager.find(WorkOrderActivity.class, woActivityId);
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
        final StringBuffer strquery = new StringBuffer();
        if (!StringUtils.isEmpty(query)) {
            strquery.append("select distinct woe.workOrder")
                    .append(" from WorkOrderEstimate woe")
                    .append(" where woe.workOrder.parent is null and woe.workOrder.egwStatus.code <> :woStatus ")
                    .append(" and woe.workOrder.egwStatus.code = :workOrderStatus and woe.estimate.parent is null")
                    .append(" and woe.workOrder.workOrderNumber like '%'||:workOrderNumber||'%' ")
                    .append(" and woe.id not in (select distinct mbh.workOrderEstimate.id from MBHeader mbh where")
                    .append(" mbh.egwStatus.code = :mbhStatus and (mbh.egBillregister.billstatus <> :billStatus")
                    .append(" and mbh.egBillregister.billtype = :billtype) and mbh.workOrderEstimate.workOrder.egwStatus.code='APPROVED'")
                    .append(" and mbh.workOrderEstimate.estimate.egwStatus.code = :estimateStatus)");

            workOrderList = entityManager.createQuery(strquery.toString(), WorkOrder.class)
                    .setParameter("woStatus", "NEW")
                    .setParameter("workOrderStatus", "APPROVED")
                    .setParameter("workOrderNumber", query.toUpperCase())
                    .setParameter("mbhStatus", MBHeader.MeasurementBookStatus.APPROVED.toString())
                    .setParameter("billStatus", MBHeader.MeasurementBookStatus.CANCELLED.toString())
                    .setParameter("billtype", getFinalBillTypeConfigValue())
                    .setParameter("estimateStatus", AbstractEstimate.EstimateStatus.ADMIN_SANCTIONED.toString())
                    .getResultList();

        }
        return "workOrderNoSearchResults";
    }

    public String searchRevisionEstimateNumber() {
        final StringBuffer strquery = new StringBuffer();
        if (!StringUtils.isEmpty(query)) {
            strquery.append(" from AbstractEstimate ae")
                    .append(" where ae.parent is not null and ae.egwStatus.code <> :status")
                    .append(" and ae.estimateNumber like '%'||:estimateNumber||'%'");

            estimateList = entityManager.createQuery(strquery.toString(), AbstractEstimate.class)
                    .setParameter("status", "NEW")
                    .setParameter("estimateNumber", query.toUpperCase())
                    .getResultList();
        }
        return "estimateNoSearchResults";
    }

    public String searchREWorkOrderNumber() {
        final StringBuffer strquery = new StringBuffer();
        if (!StringUtils.isEmpty(query)) {
            strquery.append("select distinct woe.workOrder.parent")
                    .append(" from WorkOrderEstimate woe")
                    .append(" where woe.workOrder.parent is not null and woe.estimate.egwStatus.code <> :status ")
                    .append("and woe.workOrder.parent.workOrderNumber like '%'||:workOrderNumber||'%' ");

            workOrderList = entityManager.createQuery(strquery.toString(), WorkOrder.class)
                    .setParameter("status", "NEW")
                    .setParameter("workOrderNumber", query.toUpperCase())
                    .getResultList();

        }
        return "workOrderNoSearchResults";
    }

    public String searchApprovedWONumberForRE() {
        final StringBuffer strquery = new StringBuffer();
        if (!StringUtils.isEmpty(query)) {
            strquery.append("select distinct woe.workOrder.parent")
                    .append(" from WorkOrderEstimate woe")
                    .append(" where woe.workOrder.parent is not null and woe.estimate.egwStatus.code = :status ")
                    .append(" and woe.workOrder.parent.workOrderNumber like '%'||:workOrderNumber||'%' ");

            workOrderList = entityManager.createQuery(strquery.toString(), WorkOrder.class)
                    .setParameter("status", "APPROVED")
                    .setParameter("workOrderNumber", query.toUpperCase())
                    .getResultList();

        }
        return "workOrderNoSearchResults";
    }

    public String getMBDetailsForRE() throws Exception {
        List<MBHeader> mbheaderlist = entityManager.createQuery(new StringBuffer("select distinct mbd.mbHeader")
                .append(" from MBDetails mbd")
                .append(" where mbd.workOrderActivity.workOrderEstimate.id = :woeId ")
                .append(" and mbd.mbHeader.egwStatus.code <> 'CANCELLED'").toString(), MBHeader.class)
                .setParameter("woeId", reWOEstId)
                .getResultList();

        if (mbheaderlist != null && !mbheaderlist.isEmpty())
            approvedMBList.addAll(mbheaderlist);

        return CANCEL_REVISIONESTIMATE;
    }

    @SuppressWarnings("unchecked")
    public String validateCancellation() {
        errorMessage = "";
        if (reWOEstId != null) {
            final WorkOrderEstimate revWOEst = entityManager.find(WorkOrderEstimate.class, reWOEstId);
            final WorkOrder revWorkOrder = revWOEst.getWorkOrder();
            final AbstractEstimate revEstimate = revWOEst.getEstimate();

            final List<WorkOrderActivity> revWoaList = entityManager.createQuery(
                    "from WorkOrderActivity where workOrderEstimate.workOrder.id = :woId", WorkOrderActivity.class)
                    .setParameter("woId", revWorkOrder.getId())
                    .getResultList();

            final List<Long> activtityIdList = new ArrayList<>();
            List<MBHeader> mbheaderlist = new ArrayList<>();
            // First check if any non tendered or lumpsum items are present, if
            // yes then dont allow to cancel
            for (final WorkOrderActivity revWoa : revWoaList)
                if (revWoa.getActivity().getRevisionType() != null
                        && (revWoa.getActivity().getRevisionType().equals(RevisionType.LUMP_SUM_ITEM) || revWoa
                                .getActivity().getRevisionType().equals(RevisionType.NON_TENDERED_ITEM))) {
                    mbheaderlist = entityManager.createQuery(new StringBuffer("select distinct mbd.mbHeader")
                            .append(" from MBDetails mbd")
                            .append(" where mbd.workOrderActivity.workOrderEstimate.estimate.id = :estimateId")
                            .append(" and mbd.workOrderActivity.workOrderEstimate.workOrder.id = :woId")
                            .append(" and mbd.workOrderActivity.activity.id = :woaId and mbd.mbHeader.egwStatus.code<>'CANCELLED'")
                            .toString(),
                            MBHeader.class)
                            .setParameter("estimateId", revEstimate.getId())
                            .setParameter("woId", revWorkOrder.getId())
                            .setParameter("woaId", revWoa.getActivity().getId())
                            .getResultList();

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
                    final List<Object[]> activityIdQuantityList = entityManager
                            .createQuery(new StringBuffer("select workOrderActivity.activity.id, nvl(sum(quantity),0)")
                                    .append(" from MBDetails")
                                    .append(" where mbHeader.egwStatus.code != 'CANCELLED'")
                                    .append(" and workOrderActivity.activity.id in :activtityIdList")
                                    .append(" group by workOrderActivity.activity.id ").toString())
                            .setParameter("activtityIdList", activtityIdList)
                            .getResultList();

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

                                    final List<Double> originalQuantities = entityManager
                                            .createQuery(new StringBuffer("select sum(woa.approvedQuantity)")
                                                    .append(" from WorkOrderActivity woa")
                                                    .append(" group by woa,woa.activity having activity.id = :id").toString(),
                                                    Double.class)
                                            .setParameter("id", activityId)
                                            .getResultList();

                                    final double originalQuantity = originalQuantities.isEmpty() ? 0.0
                                            : originalQuantities.get(0);

                                    final List<Double> revEstQuantityObjs = entityManager
                                            .createQuery(new StringBuffer(
                                                    " select sum(woa.approvedQuantity*nvl(decode(woa.activity.revisionType,'REDUCED_QUANTITY',")
                                                            .append("-1,'ADDITIONAL_QUANTITY',1,'NON_TENDERED_ITEM',1,'LUMP_SUM_ITEM',1),1))")
                                                            .append(" from WorkOrderActivity woa")
                                                            .append(" where woa.activity.abstractEstimate.egwStatus.code = 'APPROVED'")
                                                            .append(" and woa.activity.abstractEstimate.id != :estimateId")
                                                            .append(" group by woa.activity.parent having (woa.activity.parent is not null")
                                                            .append(" and woa.activity.parent.id = :apId )  ").toString(),
                                                    Double.class)
                                            .setParameter("estimateId", revEstimate.getId())
                                            .setParameter("apId", revWoa.getActivity().getParent().getId())
                                            .getResultList();

                                    final Double revEstQuantityObj = revEstQuantityObjs.isEmpty() ? null
                                            : revEstQuantityObjs.get(0);

                                    final double revEstQuantity = revEstQuantityObj == null ? 0.0
                                            : (Double) revEstQuantityObj;
                                    if (originalQuantity + revEstQuantity >= Double.parseDouble(activityIdQuantity[1]
                                            .toString()))
                                        continue;
                                    else {
                                        final List<MBDetails> results = entityManager.createQuery(
                                                new StringBuffer(" from MBDetails mbd")
                                                        .append(" where mbd.mbHeader.egwStatus.code != 'CANCELLED'")
                                                        .append(" and mbd.workOrderActivity.activity.id = :activityId")
                                                        .append(" and (mbdetailsDate is not null or OrderNumber is not null) ")
                                                        .toString(),
                                                MBDetails.class)
                                                .setParameter("activityId", revWoa.getActivity().getParent().getId())
                                                .getResultList();
                                        final MBDetails mbDetails = results.isEmpty() ? null : results.get(0);

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
        estimateNoList = entityManager.createQuery(new StringBuffer("select distinct abs.estimateNumber")
                .append(" from AbstractEstimate abs")
                .append(" where abs.parent.id = :id ")
                .append(" and abs.egwStatus.code<>'CANCELLED' order by abs.estimateNumber ").toString(), AbstractEstimate.class)
                .setParameter("id", estimateId).getResultList();

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