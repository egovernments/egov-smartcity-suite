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
package org.egov.works.mb.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.egov.infra.persistence.entity.AbstractAuditable;
import org.egov.infra.persistence.validator.annotation.GreaterThan;
import org.egov.infra.persistence.validator.annotation.OptionalPattern;
import org.egov.infra.persistence.validator.annotation.Required;
import org.egov.infra.validation.exception.ValidationError;
import org.egov.infra.validation.regex.Constants;
import org.egov.works.workorder.entity.WorkOrderActivity;
import org.hibernate.envers.AuditJoinTable;
import org.hibernate.envers.AuditOverride;
import org.hibernate.envers.AuditOverrides;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;
import org.hibernate.validator.constraints.Length;

@Entity
@Table(name = "EGW_MB_DETAILS")
@NamedQueries({
        @NamedQuery(name = MBDetails.PREVCUMULATIVEQUANTITY, query = " select sum(mbd.quantity) from MBDetails mbd where (mbd.mbHeader.createdDate < (select createdDate from MBHeader where id = ?) or (select count(*) from MBHeader where id = ?) = 0 ) and mbd.mbHeader.egwStatus.code != ? group by mbd.workOrderActivity having mbd.workOrderActivity.id = ?"),
        @NamedQuery(name = MBDetails.PREVCUMULATIVEQUANTITYFORCQ, query = " select sum(mbd.quantity) from MBDetails mbd where (mbd.mbHeader.createdDate < (select createdDate from MBHeader where id = ?) or (select count(*) from MBHeader where id = ?) = 0 ) and mbd.mbHeader.egwStatus.code != ? and (mbd.workOrderActivity.workOrderEstimate.workOrder = ? or mbd.workOrderActivity.workOrderEstimate.workOrder.parent = ?) group by mbd.workOrderActivity,mbd.workOrderActivity.activity having (mbd.workOrderActivity.id = ? or mbd.workOrderActivity.activity.id = ? ) "),
        @NamedQuery(name = MBDetails.PREVCUMULATIVEQUANTITYFORRES, query = " select sum(mbd.quantity) from MBDetails mbd where (mbd.mbHeader.createdDate < (select createdDate from MBHeader where id = ?) or (select count(*) from MBHeader where id = ?) = 0 ) and mbd.mbHeader.egwStatus.code != ? group by mbd.workOrderActivity.activity.parent having ((mbd.workOrderActivity.activity.parent is not null and mbd.workOrderActivity.activity.parent.id = ?)) "),
        @NamedQuery(name = MBDetails.TOTALAPPROVEDMBAMOUNT, query = " select sum(mbd.quantity*mbd.workOrderActivity.approvedRate) from MBDetails mbd where mbd.mbHeader.egwStatus.code = ? and mbd.mbHeader.workOrderEstimate.workOrder.id = ? and mbd.mbHeader.workOrderEstimate.id = ? and trunc(mbd.mbHeader.state.createdDate) <= ? and mbd.mbHeader.egBillregister is null "),
        @NamedQuery(name = MBDetails.TOTALAPPROVEDMBAMOUNTFORCANCELLEDBILL, query = "select sum(mbd.quantity*mbd.workOrderActivity.approvedRate) from MBDetails mbd where mbd.mbHeader.egwStatus.code = ? and mbd.mbHeader.workOrderEstimate.workOrder.id = ? and mbd.mbHeader.workOrderEstimate.id = ? and trunc(mbd.mbHeader.state.createdDate) <= ? and mbd.mbHeader.egBillregister is not null and mbd.mbHeader.egBillregister.status.code = ?"),
        @NamedQuery(name = MBDetails.GETUTLIZEDAMOUNTFORUNARROVEDBILL, query = " 	select sum(reg.passedamount) from MBHeader mbh join mbh.egBillregister as reg where  reg.status.code not in (?) and mbh.workOrder.id = ? and trunc(reg.billdate) <= ? and reg.egBillregistermis.voucherHeader is null"),
        @NamedQuery(name = MBDetails.GETTOTALAPPROVEDMBS, query = " 	select mbd.workOrderActivity.id,mbd.quantity  from MBDetails mbd where mbd.mbHeader.egwStatus.code = ? and mbd.mbHeader.isLegacyMB!=1 and mbd.mbHeader.workOrderEstimate.workOrder.id = ? and mbd.mbHeader.workOrderEstimate.id = ? and trunc(mbd.mbHeader.state.createdDate) <= ? and mbd.mbHeader.egBillregister is null group by mbd.workOrderActivity.id,mbd.quantity, mbd.mbHeader.id "),
        @NamedQuery(name = MBDetails.GETTOTALAPPROVEDMBSFORCANCELLEDBILL, query = " select mbd.workOrderActivity.id,mbd.quantity from MBDetails mbd where mbd.mbHeader.egwStatus.code = ? and mbd.mbHeader.isLegacyMB!=1 and mbd.mbHeader.workOrderEstimate.workOrder.id = ? and mbd.mbHeader.workOrderEstimate.id = ? and trunc(mbd.mbHeader.state.createdDate) <= ? and mbd.mbHeader.egBillregister is not null and mbd.mbHeader.egBillregister.status.code = ? group by mbd.workOrderActivity.id,mbd.quantity, mbd.mbHeader.id "),
        @NamedQuery(name = MBDetails.GETMBAMOUNTFORBILL, query = " select mbd.workOrderActivity.id,mbd.quantity  from MBDetails mbd where mbd.mbHeader.egwStatus.code = ?  and mbd.mbHeader.isLegacyMB!=1 and mbd.mbHeader.egBillregister.id = ? group by mbd.workOrderActivity.id,mbd.quantity, mbd.mbHeader.id "),
        @NamedQuery(name = MBDetails.TOTALESTIMATEDQUANTITY, query = "  select sum(woa.approvedQuantity) from WorkOrderActivity woa where woa.workOrderEstimate.workOrder.egwStatus.code<>'CANCELLED' and woa.workOrderEstimate.workOrder = ? group by woa,woa.activity having (id = ? or activity.id = ?) "),
        @NamedQuery(name = MBDetails.TOTALESTIMATEDQUANTITYFORRE, query = "select sum(woa.approvedQuantity*coalesce((CASE WHEN woa.activity.revisionType = 'REDUCED_QUANTITY' THEN -1 WHEN woa.activity.revisionType = 'ADDITIONAL_QUANTITY' THEN 1 WHEN woa.activity.revisionType = 'NON_TENDERED_ITEM' THEN 1 WHEN woa.activity.revisionType = 'LUMP_SUM_ITEM' THEN 1 END),1)) from WorkOrderActivity woa where woa.activity.abstractEstimate.egwStatus.code = 'APPROVED' and woa.activity.createdDate < ? and woa.workOrderEstimate.workOrder.egwStatus.code<>'CANCELLED' and ((woa.activity.parent is null and woa.workOrderEstimate.workOrder = ?) or (woa.workOrderEstimate.workOrder.parent is not null and woa.workOrderEstimate.workOrder.parent = ?)) group by woa.activity.parent having (woa.activity.parent is not null and woa.activity.parent.id = ? )"),
        @NamedQuery(name = MBDetails.TOTALESTIMATEDQUANTITYINRE, query = "  select sum(woa.approvedQuantity) from WorkOrderActivity woa where woa.activity.abstractEstimate.egwStatus.code != 'CANCELLED' and woa.activity.abstractEstimate.id != ? and woa.workOrderEstimate.workOrder.egwStatus.code<>'CANCELLED' and (woa.workOrderEstimate.workOrder = ? or (woa.activity.parent is null and woa.workOrderEstimate.workOrder.parent is not null and woa.workOrderEstimate.workOrder.parent = ?)) group by woa,woa.activity having (id = ? or activity.id = ?)"),
        @NamedQuery(name = MBDetails.TOTALESTIMATEDQUANTITYFORREINRE, query = " select sum(woa.approvedQuantity*coalesce((CASE WHEN woa.activity.revisionType = 'REDUCED_QUANTITY' THEN -1 WHEN woa.activity.revisionType = 'ADDITIONAL_QUANTITY' THEN 1 WHEN woa.activity.revisionType = 'NON_TENDERED_ITEM' THEN 1 WHEN woa.activity.revisionType = 'LUMP_SUM_ITEM' THEN 1 END),1)) from WorkOrderActivity woa where woa.activity.abstractEstimate.egwStatus.code != 'CANCELLED' and woa.activity.abstractEstimate.id != ? and woa.workOrderEstimate.workOrder.egwStatus.code<>'CANCELLED' and (woa.workOrderEstimate.workOrder = ? or (woa.workOrderEstimate.workOrder.parent is not null and woa.workOrderEstimate.workOrder.parent = ?)) group by woa.activity.parent having (woa.activity.parent is not null and woa.activity.parent.id = ? ) "),
        @NamedQuery(name = MBDetails.TOTALESTIMATEDQUANTITYFORPREVIOUSRES, query = " select sum(woa.approvedQuantity*coalesce((CASE WHEN woa.activity.revisionType = 'REDUCED_QUANTITY' THEN -1 WHEN woa.activity.revisionType = 'ADDITIONAL_QUANTITY' THEN 1 WHEN woa.activity.revisionType = 'NON_TENDERED_ITEM' THEN 1 WHEN woa.activity.revisionType = 'LUMP_SUM_ITEM' THEN 1 END),1)) from WorkOrderActivity woa where woa.activity.abstractEstimate.egwStatus.code != 'CANCELLED' and woa.activity.abstractEstimate.createdDate < (select est.createdDate from AbstractEstimate est where est.id = ?) and woa.workOrderEstimate.workOrder.egwStatus.code<>'CANCELLED' and (woa.workOrderEstimate.workOrder = ? or (woa.workOrderEstimate.workOrder.parent is not null and woa.workOrderEstimate.workOrder.parent = ?)) group by woa.activity.parent having (woa.activity.parent is not null and woa.activity.parent.id = ? )  ") })
@SequenceGenerator(name = MBDetails.SEQ_EGW_MB_DETAILS, sequenceName = MBDetails.SEQ_EGW_MB_DETAILS, allocationSize = 1)
@AuditOverrides({
        @AuditOverride(forClass = AbstractAuditable.class, name = "lastModifiedBy"),
        @AuditOverride(forClass = AbstractAuditable.class, name = "lastModifiedDate")
})
@Audited
public class MBDetails extends AbstractAuditable {

    private static final long serialVersionUID = -5088074625605584344L;

    public static final String SEQ_EGW_MB_DETAILS = "SEQ_EGW_MB_DETAILS";

    public static final String PREVCUMULATIVEQUANTITY = "prevCumulativeQuantity";
    public static final String PREVCUMULATIVEQUANTITYFORCQ = "prevCumulativeQuantityForCQ";
    public static final String PREVCUMULATIVEQUANTITYFORRES = "prevCumulativeQuantityForREs";
    public static final String TOTALAPPROVEDMBAMOUNT = "totalApprovedMBAmount";
    public static final String TOTALAPPROVEDMBAMOUNTFORCANCELLEDBILL = "totalApprovedMBAmountForCancelledBill";
    public static final String GETUTLIZEDAMOUNTFORUNARROVEDBILL = "getUtlizedAmountForUnArrovedBill";
    public static final String GETTOTALAPPROVEDMBS = "gettotalApprovedMBs";
    public static final String GETTOTALAPPROVEDMBSFORCANCELLEDBILL = "gettotalApprovedMBsForCancelledBill";
    public static final String GETMBAMOUNTFORBILL = "getMBAmountForBill";
    public static final String TOTALESTIMATEDQUANTITY = "totalEstimatedQuantity";
    public static final String TOTALESTIMATEDQUANTITYFORRE = "totalEstimatedQuantityForRE";
    public static final String TOTALESTIMATEDQUANTITYINRE = "totalEstimatedQuantityInRE";
    public static final String TOTALESTIMATEDQUANTITYFORREINRE = "totalEstimatedQuantityForREinRE";
    public static final String TOTALESTIMATEDQUANTITYFORPREVIOUSRES = "totalEstimatedQuantityForPreviousREs";

    @Id
    @GeneratedValue(generator = SEQ_EGW_MB_DETAILS, strategy = GenerationType.SEQUENCE)
    private Long id;

    @Required(message = "mbdetails.mbheader.null")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MBHEADER_ID", nullable = false)
    @AuditJoinTable
    private MBHeader mbHeader;

    @Required(message = "mbdetails.activity.null")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "WO_ACTIVITY_ID", nullable = false)
    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    private WorkOrderActivity workOrderActivity;

    @GreaterThan(value = 0, message = "mbdetails.quantity.non.negative")
    private double quantity;

    private double rate;

    @Length(max = 400, message = "mbdetails.remark.length")
    private String remarks;

    // ------------------------Fields for calculations---------------------
    @Transient
    private double prevCumlvQuantity;
    @Transient
    private double currCumlvQuantity;
    @Transient
    private double amtForCurrQuantity;
    @Transient
    private double cumlvAmtForCurrCumlvQuantity;

    @Column(name = "ORDER_DATE")
    @Temporal(value = TemporalType.DATE)
    private Date mbdetailsDate;

    @Column(name = "ORDER_NUMBER")
    @OptionalPattern(regex = Constants.ALPHANUMERIC_WITHSLASHES, message = "mbdetails.ordernumber")
    private String OrderNumber;

    // -------------------------------------------------------------------
    @Transient
    private double totalEstQuantity; // Added for RE

    private double amount = 0.0;

    @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "mbDetails", targetEntity = MBMeasurementSheet.class)
    @AuditJoinTable
    @OrderBy("id")
    private List<MBMeasurementSheet> measurementSheets = new LinkedList<MBMeasurementSheet>();

    public List<ValidationError> validate() {
        final List<ValidationError> validationErrors = new ArrayList<ValidationError>();
        if (mbHeader != null && (mbHeader.getId() == null || mbHeader.getId() == 0 || mbHeader.getId() == -1))
            validationErrors.add(new ValidationError("mbHeader", "mbdetails.mbheader.null"));
        if (workOrderActivity != null && (workOrderActivity.getId() == null || workOrderActivity.getId() == 0
                || workOrderActivity.getId() == -1))
            validationErrors.add(new ValidationError("workOrderActivity", "mbdetails.activity.null"));
        return validationErrors;
    }

    public void setMbHeader(final MBHeader mbHeader) {
        this.mbHeader = mbHeader;
    }

    public MBHeader getMbHeader() {
        return mbHeader;
    }

    public void setQuantity(final double quantity) {
        this.quantity = quantity;
    }

    public double getQuantity() {
        return quantity;
    }

    public WorkOrderActivity getWorkOrderActivity() {
        return workOrderActivity;
    }

    public void setWorkOrderActivity(final WorkOrderActivity workOrderActivity) {
        this.workOrderActivity = workOrderActivity;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(final String remarks) {
        this.remarks = remarks;
    }

    /**
     * Get Cumulative quantity upto pervious entry
     */
    public double getPrevCumlvQuantity() {
        return prevCumlvQuantity;
    }

    public void setPrevCumlvQuantity(final double prevCumlvQuantity) {
        this.prevCumlvQuantity = prevCumlvQuantity;
    }

    /**
     * Get Cumulative quantity including current entry
     */
    public double getCurrCumlvQuantity() {
        return currCumlvQuantity;
    }

    public void setCurrCumlvQuantity(final double currCumlvQuantity) {
        this.currCumlvQuantity = currCumlvQuantity;
    }

    /**
     * Get Amount for current entry
     */
    public double getAmtForCurrQuantity() {
        return amtForCurrQuantity;
    }

    public void setAmtForCurrQuantity(final double amtForCurrQuantity) {
        this.amtForCurrQuantity = amtForCurrQuantity;
    }

    /**
     * Get Cumulative amount including current entry
     */
    public double getCumlvAmtForCurrCumlvQuantity() {
        return cumlvAmtForCurrCumlvQuantity;
    }

    public void setCumlvAmtForCurrCumlvQuantity(final double cumlvAmtForCurrCumlvQuantity) {
        this.cumlvAmtForCurrCumlvQuantity = cumlvAmtForCurrCumlvQuantity;
    }

    public Date getMbdetailsDate() {
        return mbdetailsDate;
    }

    public void setMbdetailsDate(final Date mbdetailsDate) {
        this.mbdetailsDate = mbdetailsDate;
    }

    public String getOrderNumber() {
        return OrderNumber;
    }

    public void setOrderNumber(final String orderNumber) {
        OrderNumber = orderNumber;
    }

    public double getTotalEstQuantity() {
        return totalEstQuantity;
    }

    public void setTotalEstQuantity(final double totalEstQuantity) {
        this.totalEstQuantity = totalEstQuantity;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(final double amount) {
        this.amount = amount;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(final double rate) {
        this.rate = rate;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(final Long id) {
        this.id = id;
    }

    public List<MBMeasurementSheet> getMeasurementSheets() {
        return measurementSheets;
    }

    public void setMeasurementSheets(final List<MBMeasurementSheet> measurementSheets) {
        this.measurementSheets = measurementSheets;
    }

    public void addMBMeasurementSheet(final MBMeasurementSheet mbMeasurementSheet) {
        measurementSheets.add(mbMeasurementSheet);
    }
}
