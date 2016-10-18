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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
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
import javax.validation.Valid;
import javax.validation.constraints.Min;

import org.apache.commons.collections.CollectionUtils;
import org.egov.commons.EgwStatus;
import org.egov.infra.persistence.entity.AbstractAuditable;
import org.egov.infra.persistence.validator.annotation.DateFormat;
import org.egov.infra.persistence.validator.annotation.GreaterThan;
import org.egov.infra.persistence.validator.annotation.OptionalPattern;
import org.egov.infra.persistence.validator.annotation.Required;
import org.egov.infra.persistence.validator.annotation.ValidateDate;
import org.egov.infra.validation.exception.ValidationError;
import org.egov.infra.workflow.entity.StateAware;
import org.egov.works.contractorbill.entity.ContractorBillRegister;
import org.egov.works.lineestimate.entity.DocumentDetails;
import org.egov.works.revisionestimate.entity.enums.RevisionType;
import org.egov.works.utils.WorksConstants;
import org.egov.works.workorder.entity.WorkOrder;
import org.egov.works.workorder.entity.WorkOrderEstimate;
import org.hibernate.envers.AuditJoinTable;
import org.hibernate.envers.AuditOverride;
import org.hibernate.envers.AuditOverrides;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.hibernate.envers.RelationTargetAuditMode;
import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "EGW_MB_HEADER")
@NamedQueries({
        @NamedQuery(name = MBHeader.GETAPPROVEDMBLIST, query = " select distinct mbh from MBHeader mbh where mbh.egwStatus.code = ? and trunc(mbh.state.createdDate) <= ? and mbh.isLegacyMB!=1 and mbh.workOrder.id = ? and mbh.workOrderEstimate.id = ? "),
        @NamedQuery(name = MBHeader.GETPARTBILLLIST, query = " select distinct mbh from MBHeader mbh left join mbh.egBillregister as reg where mbh.egwStatus.code = ? and mbh.workOrder.id = ? and (reg is not null or reg.status.code != ?) and reg.billtype=? "),
        @NamedQuery(name = MBHeader.GETALLBILLEDMBS, query = " select eb1.billdate from EgBillregister eb1 where exists(select mbh.egBillregister.id from MBHeader mbh left join mbh.egBillregister as reg where mbh.egwStatus.code = ? and mbh.workOrder.id = ? and reg is not null  and  upper(reg.status.code)!='CANCELLED' and eb1.id=mbh.egBillregister.id order by reg.billdate desc) and rownum=1 "),
        @NamedQuery(name = MBHeader.GETALLBILLEDMBSFORWOESTIMATE, query = " select eb1.billdate from EgBillregister eb1 where exists(select mbh.egBillregister.id from MBHeader mbh left join mbh.egBillregister as reg where mbh.egwStatus.code = ? and mbh.workOrderEstimate.id = ? and reg is not null and  upper(reg.status.code)!='CANCELLED' and eb1.id=mbh.egBillregister.id order by reg.billdate desc) and rownum=1 "),
        @NamedQuery(name = MBHeader.GETALLMBSFORBILLID, query = " from MBHeader mbh where mbh.egwStatus.code = ? and mbh.egBillregister is not null and mbh.egBillregister.id = ? "),
        @NamedQuery(name = MBHeader.GETMBBYWORKORDERESTID, query = " from MBHeader mbh where mbh.workOrderEstimate.id=? and mbh.egwStatus.code!=? and mbh.workOrderEstimate.id not in (select mb.workOrderEstimate.id from MBHeader mb where mb.egBillregister.billtype=? and mb.egBillregister.status.code!=? ) "),
        @NamedQuery(name = MBHeader.GETMBWITHOUTLEGACYBYWOESTID, query = " from MBHeader mbh where mbh.workOrderEstimate.id=? and mbh.egwStatus.code!=? and mbh.isLegacyMB!=1 and mbh.workOrderEstimate.id not in (select mb.workOrderEstimate.id from MBHeader mb where mb.egBillregister.billtype=? and mb.egBillregister.status.code!=?)  "),
        @NamedQuery(name = MBHeader.GETALLMBHEADERSBYBILLID, query = " Select distinct mbHeader from MBHeader mbHeader where mbHeader.egBillregister.id=?  "),
        @NamedQuery(name = MBHeader.GETALLMBNOSBYWORKESTIMATE, query = " Select distinct mbHeader.mbRefNo from MBHeader mbHeader where mbHeader.egwStatus.code = ? and mbHeader.workOrderEstimate.id=? "),
        @NamedQuery(name = MBHeader.TOTALMBAMOUNTOFMBS, query = " select sum(mbAmount) from MBHeader where egwStatus.code != 'CANCELLED' and  workOrderEstimate.workOrder.id=? and  workOrderEstimate.estimate.id= ? "),
        @NamedQuery(name = MBHeader.TOTALMBAMOUNTOFMBSFORREVISIONWO, query = " select sum(mbAmount) from MBHeader where egwStatus.code != 'CANCELLED' and  workOrderEstimate.workOrder.parent.id=? and  workOrderEstimate.estimate.parent.id= ?  "),
        @NamedQuery(name = MBHeader.GETAMOUNTFORAPPROVEDREVISIONWO, query = " select sum(wo.workOrderAmount) from WorkOrder wo where wo.parent is not null and wo.egwStatus.code='APPROVED' and wo.parent.id=? "),
        @NamedQuery(name = MBHeader.GETALLAPPROVEDMBHEADERS, query = " select distinct mbh from MBHeader mbh where mbh.egwStatus.code = ? and mbh.workOrder.id = ? and mbh.workOrderEstimate.estimate.id = ? ") })
@SequenceGenerator(name = MBHeader.SEQ_EGW_MB_HEADER, sequenceName = MBHeader.SEQ_EGW_MB_HEADER, allocationSize = 1)
@AuditOverrides({
        @AuditOverride(forClass = AbstractAuditable.class, name = "lastModifiedBy"),
        @AuditOverride(forClass = AbstractAuditable.class, name = "lastModifiedDate")
})
@Audited
public class MBHeader extends StateAware {

    private static final long serialVersionUID = 121631467636260459L;

    public enum MeasurementBookStatus {
        NEW, CREATED, CHECKED, REJECTED, RESUBMITTED, CANCELLED, APPROVED, WORK_COMMENCED
    }

    public enum Actions {
        SAVE, SUBMIT_FOR_APPROVAL, REJECT, CANCEL, APPROVAL;

        @Override
        public String toString() {
            return name().toLowerCase();
        }
    }

    public static final String SEQ_EGW_MB_HEADER = "SEQ_EGW_MB_HEADER";

    public static final String GETAPPROVEDMBLIST = "getApprovedMBList";
    public static final String GETPARTBILLLIST = "getPartBillList";
    public static final String GETALLBILLEDMBS = "getAllBilledMBs";
    public static final String GETALLBILLEDMBSFORWOESTIMATE = "getAllBilledMBsForWOEstimate";
    public static final String GETALLMBSFORBILLID = "getAllMBsForBillId";
    public static final String GETMBBYWORKORDERESTID = "getMBbyWorkOrderEstID";
    public static final String GETMBWITHOUTLEGACYBYWOESTID = "getMBWithoutLegacyByWOEstID";
    public static final String GETALLMBHEADERSBYBILLID = "getAllMBHeadersbyBillId";
    public static final String GETALLMBNOSBYWORKESTIMATE = "getAllMBNosbyWorkEstimate";
    public static final String TOTALMBAMOUNTOFMBS = "totalMBAmountOfMBs";
    public static final String TOTALMBAMOUNTOFMBSFORREVISIONWO = "totalMBAmountOfMBsForRevisionWO";
    public static final String GETAMOUNTFORAPPROVEDREVISIONWO = "getAmountForApprovedRevisionWO";
    public static final String GETALLAPPROVEDMBHEADERS = "getAllApprovedMBHeaders";

    @Id
    @GeneratedValue(generator = SEQ_EGW_MB_HEADER, strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "WORKORDER_ID")
    @Required(message = "mbheader.workorder.null")
    @NotAudited
    private WorkOrder workOrder;

    @Required(message = "mbheader.mbrefno.null")
    @Length(max = 50, message = "mbheader.mbrefno.length")
    @Column(name = "MB_REFNO")
    private String mbRefNo;

    @Length(max = 400, message = "mbheader.contractorComments.length")
    @OptionalPattern(regex = WorksConstants.ALPHANUMERICWITHALLSPECIALCHAR, message = "mb.contractorComments.alphaNumeric")
    @Column(name = "CONTRACTOR_COMMENTS")
    private String contractorComments;

    @Required(message = "mbheader.mbdate.null")
    @ValidateDate(allowPast = true, dateFormat = "dd/MM/yyyy", message = "mbheader.mbDate.futuredate")
    @DateFormat(message = "invalid.fieldvalue.mbDate")
    @Temporal(value = TemporalType.DATE)
    @Column(name = "MB_DATE")
    private Date mbDate;

    @Column(name = "MB_ISSUED_DATE")
    @Temporal(value = TemporalType.DATE)
    private Date mbIssuedDate;

    // @Required(message = "mbheader.mbabstract.null")
    @Length(max = 400, message = "mbheader.mbabstract.length")
    @OptionalPattern(regex = WorksConstants.ALPHANUMERICWITHALLSPECIALCHAR, message = "mb.mbabstract.alphaNumeric")
    @Column(name = "ABSTRACT")
    private String mbAbstract;

    @Required(message = "mbheader.fromPageNo.null")
    @GreaterThan(value = 0, message = "mbheader.fromPageNo.non.negative")
    @Column(name = "FROM_PAGE_NO")
    private Integer fromPageNo;

    @Min(value = 0, message = "mbheader.toPageNo.non.negative")
    @Column(name = "TO_PAGE_NO")
    private Integer toPageNo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "WORKORDER_ESTIMATE_ID")
    @NotAudited
    private WorkOrderEstimate workOrderEstimate;

    @Transient
    private Integer approverUserId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BILLREGISTER_ID")
    @NotAudited
    private ContractorBillRegister egBillregister;

    @Valid
    @JsonIgnore
    @OrderBy("id")
    @OneToMany(mappedBy = "mbHeader", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, targetEntity = MBDetails.class)
    @AuditJoinTable
    private final List<MBDetails> mbDetails = new ArrayList<MBDetails>(0);

    private transient List<MBDetails> sorMbDetails = new ArrayList<MBDetails>(0);

    private transient List<MBDetails> nonSorMbDetails = new ArrayList<MBDetails>(0);

    private transient List<MBDetails> nonTenderedMbDetails = new ArrayList<MBDetails>(0);

    private transient List<MBDetails> lumpSumMbDetails = new ArrayList<MBDetails>(0);

    private final transient List<DocumentDetails> documentDetails = new ArrayList<DocumentDetails>(0);

    @Transient
    private String owner;

    @Transient
    private List<String> mbActions = new ArrayList<String>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "STATUS_ID")
    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    private EgwStatus egwStatus;

    @Column(name = "IS_LEGACY_MB")
    private boolean isLegacyMB;

    @Column(name = "MB_AMOUNT")
    private BigDecimal mbAmount;

    @Column(name = "APPROVED_DATE")
    @Temporal(value = TemporalType.DATE)
    private Date approvedDate;

    @Transient
    private Long approvalDepartment;

    @Transient
    private String approvalComent;

    private String cancellationReason;

    private String cancellationRemarks;

    public List<ValidationError> validate() {
        final List<ValidationError> validationErrors = new ArrayList<ValidationError>();
        if (workOrder != null && (workOrder.getId() == null || workOrder.getId() == 0 || workOrder.getId() == -1))
            validationErrors.add(new ValidationError("workOrder", "mbheader.workorder.null"));

        if (fromPageNo != null && toPageNo != null && fromPageNo > toPageNo)
            validationErrors.add(new ValidationError("toPageNo", "mbheader.toPageNo.invalid"));
        if (mbDate != null && workOrder != null && workOrder.getWorkOrderDate() != null
                && mbDate.before(workOrder.getWorkOrderDate()))
            validationErrors.add(new ValidationError("mbDate", "mbheader.mbDate.invalid"));

        return validationErrors;
    }

    public void setWorkOrder(final WorkOrder workOrder) {
        this.workOrder = workOrder;
    }

    public WorkOrder getWorkOrder() {
        return workOrder;
    }

    public void setMbRefNo(final String mbRefNo) {
        this.mbRefNo = mbRefNo;
    }

    public String getMbRefNo() {
        return mbRefNo;
    }

    public void setMbDate(final Date mbDate) {
        this.mbDate = mbDate;
    }

    public Date getMbDate() {
        return mbDate;
    }

    public void setMbAbstract(final String mbAbstract) {
        this.mbAbstract = mbAbstract;
    }

    public String getMbAbstract() {
        return mbAbstract;
    }

    public Integer getFromPageNo() {
        return fromPageNo;
    }

    public void setFromPageNo(final Integer fromPageNo) {
        this.fromPageNo = fromPageNo;
    }

    public Integer getToPageNo() {
        return toPageNo;
    }

    public void setToPageNo(final Integer toPageNo) {
        this.toPageNo = toPageNo;
    }

    public String getContractorComments() {
        return contractorComments;
    }

    public void setContractorComments(final String contractorComments) {
        this.contractorComments = contractorComments;
    }

    public List<MBDetails> getMbDetails() {
        return mbDetails;
    }

    public void setMbDetails(final List<MBDetails> mbDetails) {
        this.mbDetails.clear();
        if (mbDetails != null)
            this.mbDetails.addAll(mbDetails);
    }

    public void addMbDetails(final MBDetails mbDetails) {
        this.mbDetails.add(mbDetails);
    }

    // to show in inbox
    @Override
    public String getStateDetails() {
        return "MB Ref Number : " + getMbRefNo();
    }

    public ContractorBillRegister getEgBillregister() {
        return egBillregister;
    }

    public void setEgBillregister(final ContractorBillRegister egBillregister) {
        this.egBillregister = egBillregister;
    }

    public Integer getApproverUserId() {
        return approverUserId;
    }

    public void setApproverUserId(final Integer approverUserId) {
        this.approverUserId = approverUserId;
    }

    public WorkOrderEstimate getWorkOrderEstimate() {
        return workOrderEstimate;
    }

    public void setWorkOrderEstimate(final WorkOrderEstimate workOrderEstimate) {
        this.workOrderEstimate = workOrderEstimate;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(final String owner) {
        this.owner = owner;
    }

    public List<String> getMbActions() {
        return mbActions;
    }

    public void setMbActions(final List<String> mbActions) {
        this.mbActions = mbActions;
    }

    public EgwStatus getEgwStatus() {
        return egwStatus;
    }

    public void setEgwStatus(final EgwStatus egwStatus) {
        this.egwStatus = egwStatus;
    }

    public boolean getIsLegacyMB() {
        return isLegacyMB;
    }

    public void setIsLegacyMB(final boolean isLegacyMB) {
        this.isLegacyMB = isLegacyMB;
    }

    public BigDecimal getMbAmount() {
        return mbAmount;
    }

    public void setMbAmount(final BigDecimal mbAmount) {
        this.mbAmount = mbAmount;
    }

    @Override
    public String toString() {
        return "MBHeader ( Id : " + getId() + "MB Ref No: " + mbRefNo + ")";
    }

    public BigDecimal getTotalMBAmount() {
        double amount = 0.0;
        BigDecimal resultAmount = BigDecimal.ZERO;
        for (final MBDetails mbd : mbDetails) {
            if (mbd.getWorkOrderActivity().getActivity().getNonSor() == null)
                amount = mbd.getWorkOrderActivity().getApprovedRate() * mbd.getQuantity()
                        * mbd.getWorkOrderActivity().getConversionFactor();
            else
                amount = mbd.getWorkOrderActivity().getApprovedRate() * mbd.getQuantity();
            resultAmount = resultAmount.add(BigDecimal.valueOf(amount));
        }
        return resultAmount;
    }

    public Date getApprovedDate() {
        return approvedDate;
    }

    public void setApprovedDate(final Date approvedDate) {
        this.approvedDate = approvedDate;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(final Long id) {
        this.id = id;
    }

    public List<MBDetails> getSorMbDetails() {
        return sorMbDetails;
    }

    public void setSorMbDetails(final List<MBDetails> sorMbDetails) {
        this.sorMbDetails = sorMbDetails;
    }

    public List<MBDetails> getNonSorMbDetails() {
        return nonSorMbDetails;
    }

    public void setNonSorMbDetails(final List<MBDetails> nonSorMbDetails) {
        this.nonSorMbDetails = nonSorMbDetails;
    }

    @SuppressWarnings("unchecked")
    public Collection<MBDetails> getSORMBDetails() {
        return CollectionUtils.select(mbDetails,
                mbDetail -> ((MBDetails) mbDetail).getWorkOrderActivity().getActivity().getSchedule() != null
                        && (((MBDetails) mbDetail).getWorkOrderActivity().getActivity().getRevisionType() == null ||
                                ((MBDetails) mbDetail).getWorkOrderActivity().getActivity().getRevisionType()
                                        .compareTo(RevisionType.ADDITIONAL_QUANTITY) == 0
                                ||
                                ((MBDetails) mbDetail).getWorkOrderActivity().getActivity().getRevisionType()
                                        .compareTo(RevisionType.REDUCED_QUANTITY) == 0));
    }

    @SuppressWarnings("unchecked")
    public Collection<MBDetails> getNonSORMBDetails() {
        return CollectionUtils.select(mbDetails,
                mbDetail -> ((MBDetails) mbDetail).getWorkOrderActivity().getActivity().getNonSor() != null
                        && (((MBDetails) mbDetail).getWorkOrderActivity().getActivity().getRevisionType() == null ||
                                ((MBDetails) mbDetail).getWorkOrderActivity().getActivity().getRevisionType()
                                        .compareTo(RevisionType.ADDITIONAL_QUANTITY) == 0
                                ||
                                ((MBDetails) mbDetail).getWorkOrderActivity().getActivity().getRevisionType()
                                        .compareTo(RevisionType.REDUCED_QUANTITY) == 0));
    }

    @SuppressWarnings("unchecked")
    public Collection<MBDetails> getNonTenderedMBDetails() {
        return CollectionUtils.select(mbDetails,
                mbDetail -> (((MBDetails) mbDetail).getWorkOrderActivity().getActivity().getRevisionType() != null
                        && ((MBDetails) mbDetail).getWorkOrderActivity().getActivity().getRevisionType()
                                .compareTo(RevisionType.NON_TENDERED_ITEM) == 0));
    }

    @SuppressWarnings("unchecked")
    public Collection<MBDetails> getLumpSumMBDetails() {
        return CollectionUtils.select(mbDetails,
                mbDetail -> (((MBDetails) mbDetail).getWorkOrderActivity().getActivity().getRevisionType() != null
                        && ((MBDetails) mbDetail).getWorkOrderActivity().getActivity().getRevisionType()
                                .compareTo(RevisionType.LUMP_SUM_ITEM) == 0));
    }

    public Long getApprovalDepartment() {
        return approvalDepartment;
    }

    public void setApprovalDepartment(final Long approvalDepartment) {
        this.approvalDepartment = approvalDepartment;
    }

    public String getApprovalComent() {
        return approvalComent;
    }

    public void setApprovalComent(final String approvalComent) {
        this.approvalComent = approvalComent;
    }

    public Date getMbIssuedDate() {
        return mbIssuedDate;
    }

    public void setMbIssuedDate(final Date mbIssuedDate) {
        this.mbIssuedDate = mbIssuedDate;
    }

    public List<DocumentDetails> getDocumentDetails() {
        return documentDetails;
    }

    public void setDocumentDetails(final List<DocumentDetails> documentDetails) {
        this.documentDetails.clear();
        if (documentDetails != null)
            this.documentDetails.addAll(documentDetails);
    }

    public String getCancellationReason() {
        return cancellationReason;
    }

    public void setCancellationReason(final String cancellationReason) {
        this.cancellationReason = cancellationReason;
    }

    public String getCancellationRemarks() {
        return cancellationRemarks;
    }

    public void setCancellationRemarks(final String cancellationRemarks) {
        this.cancellationRemarks = cancellationRemarks;
    }

    public List<MBDetails> getNonTenderedMbDetails() {
        return nonTenderedMbDetails;
    }

    public void setNonTenderedMbDetails(final List<MBDetails> nonTenderedMbDetails) {
        this.nonTenderedMbDetails = nonTenderedMbDetails;
    }

    public List<MBDetails> getLumpSumMbDetails() {
        return lumpSumMbDetails;
    }

    public void setLumpSumMbDetails(final List<MBDetails> lumpSumMbDetails) {
        this.lumpSumMbDetails = lumpSumMbDetails;
    }

}