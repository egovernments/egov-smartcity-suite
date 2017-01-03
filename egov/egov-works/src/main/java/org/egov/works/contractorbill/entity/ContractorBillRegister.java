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
package org.egov.works.contractorbill.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.egov.infra.admin.master.entity.User;
import org.egov.model.bills.EgBilldetails;
import org.egov.model.bills.EgBillregister;
import org.egov.works.lineestimate.entity.DocumentDetails;
import org.egov.works.mb.entity.MBHeader;
import org.egov.works.workorder.entity.WorkOrderEstimate;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

@Entity
@Table(name = "EGW_CONTRACTORBILL")
@Audited
public class ContractorBillRegister extends EgBillregister {

    private static final long serialVersionUID = -6056638534067396998L;

    public enum BillStatus {
        CREATED, APPROVED, REJECTED, CANCELLED, RESUBMITTED, CHECKED
    }

    private Integer billSequenceNumber;

    @Temporal(TemporalType.DATE)
    private Date approvedDate;

    @NotAudited
    @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "egbill", targetEntity = AssetForBill.class)
    private List<AssetForBill> assetDetailsList = new LinkedList<AssetForBill>();

    @NotAudited
    @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "egbill", targetEntity = DeductionTypeForBill.class)
    private List<DeductionTypeForBill> deductionTypeList = new LinkedList<DeductionTypeForBill>();

    @NotAudited
    @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "egBillReg", targetEntity = StatutoryDeductionsForBill.class)
    private List<StatutoryDeductionsForBill> statutoryDeductionsList = new LinkedList<StatutoryDeductionsForBill>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approvedBy")
    private User approvedBy;

    @Transient
    private String owner;

    @Transient
    private List<String> billActions = new ArrayList<String>();

    private final transient List<DocumentDetails> documentDetails = new ArrayList<DocumentDetails>(0);

    @Transient
    private List<EgBilldetails> billDetailes = new ArrayList<EgBilldetails>(0);

    @Transient
    private List<EgBilldetails> refundBillDetails = new ArrayList<EgBilldetails>(0);

    @Transient
    private List<EgBilldetails> statutoryDeductionDetailes = new ArrayList<EgBilldetails>(0);

    @Transient
    private List<EgBilldetails> otherDeductionDetailes = new ArrayList<EgBilldetails>(0);

    @Transient
    private List<EgBilldetails> retentionMoneyDeductionDetailes = new ArrayList<EgBilldetails>(0);

    @Transient
    private List<EgBilldetails> advanceAdjustmentDetails = new ArrayList<EgBilldetails>(0);

    @Transient
    private Long approvalDepartment;

    @Transient
    private String approvalComent;

    @Transient
    private MBHeader mbHeader;

    @Transient
    private Long[] mbHeaderIds;

    @NotAudited
    private String cancellationReason;

    @NotAudited
    private String cancellationRemarks;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workOrderEstimate", nullable = false)
    @NotAudited
    private WorkOrderEstimate workOrderEstimate;

    @Override
    public String getStateDetails() {
        return "Contractor Bill No: " + getBillnumber();
    }

    public List<AssetForBill> getAssetDetailsList() {
        return assetDetailsList;
    }

    public void setAssetDetailsList(final List<AssetForBill> assetDetailsList) {
        this.assetDetailsList = assetDetailsList;
    }

    public void addAssetDetails(final AssetForBill assetForBill) {
        assetDetailsList.add(assetForBill);
    }

    public void addDeductionType(final DeductionTypeForBill deductionTypeForBill) {
        deductionTypeList.add(deductionTypeForBill);
    }

    public List<DeductionTypeForBill> getDeductionTypeList() {
        return deductionTypeList;
    }

    public void setDeductionTypeList(final List<DeductionTypeForBill> deductionTypeList) {
        this.deductionTypeList = deductionTypeList;
    }

    public Integer getBillSequenceNumber() {
        return billSequenceNumber;
    }

    public void setBillSequenceNumber(final Integer billSequenceNumber) {
        this.billSequenceNumber = billSequenceNumber;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(final String owner) {
        this.owner = owner;
    }

    public List<String> getBillActions() {
        return billActions;
    }

    public void setBillActions(final List<String> billActions) {
        this.billActions = billActions;
    }

    public List<StatutoryDeductionsForBill> getStatutoryDeductionsList() {
        return statutoryDeductionsList;
    }

    public List<EgBilldetails> getAdvanceAdjustmentDetails() {
        return advanceAdjustmentDetails;
    }

    public void setAdvanceAdjustmentDetails(final List<EgBilldetails> advanceAdjustmentDetails) {
        this.advanceAdjustmentDetails = advanceAdjustmentDetails;
    }

    public void setStatutoryDeductionsList(final List<StatutoryDeductionsForBill> statutoryDeductionsList) {
        this.statutoryDeductionsList = statutoryDeductionsList;
    }

    public void addStatutoryDeductions(final StatutoryDeductionsForBill statutoryDeductionsForBill) {
        statutoryDeductionsList.add(statutoryDeductionsForBill);
    }

    public Date getApprovedDate() {
        return approvedDate;
    }

    public void setApprovedDate(final Date approvedDate) {
        this.approvedDate = approvedDate;
    }

    public List<DocumentDetails> getDocumentDetails() {
        return documentDetails;
    }

    public void setDocumentDetails(final List<DocumentDetails> documentDetails) {
        this.documentDetails.clear();
        if (documentDetails != null)
            this.documentDetails.addAll(documentDetails);
    }

    public List<EgBilldetails> getBillDetailes() {
        return billDetailes;
    }

    public void setBillDetailes(final List<EgBilldetails> billDetailes) {
        this.billDetailes = billDetailes;
    }

    @Override
    public Long getApprovalDepartment() {
        return approvalDepartment;
    }

    @Override
    public void setApprovalDepartment(final Long approvalDepartment) {
        this.approvalDepartment = approvalDepartment;
    }

    @Override
    public String getApprovalComent() {
        return approvalComent;
    }

    @Override
    public void setApprovalComent(final String approvalComent) {
        this.approvalComent = approvalComent;
    }

    public User getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(final User approvedBy) {
        this.approvedBy = approvedBy;
    }

    public MBHeader getMbHeader() {
        return mbHeader;
    }

    public void setMbHeader(final MBHeader mbHeader) {
        this.mbHeader = mbHeader;
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

    public WorkOrderEstimate getWorkOrderEstimate() {
        return workOrderEstimate;
    }

    public void setWorkOrderEstimate(final WorkOrderEstimate workOrderEstimate) {
        this.workOrderEstimate = workOrderEstimate;
    }

    public List<EgBilldetails> getStatutoryDeductionDetailes() {
        return statutoryDeductionDetailes;
    }

    public void setStatutoryDeductionDetailes(final List<EgBilldetails> statutoryDeductionDetailes) {
        this.statutoryDeductionDetailes = statutoryDeductionDetailes;
    }

    public List<EgBilldetails> getOtherDeductionDetailes() {
        return otherDeductionDetailes;
    }

    public void setOtherDeductionDetailes(final List<EgBilldetails> otherDeductionDetailes) {
        this.otherDeductionDetailes = otherDeductionDetailes;
    }

    public List<EgBilldetails> getRetentionMoneyDeductionDetailes() {
        return retentionMoneyDeductionDetailes;
    }

    public void setRetentionMoneyDeductionDetailes(final List<EgBilldetails> retentionMoneyDeductionDetailes) {
        this.retentionMoneyDeductionDetailes = retentionMoneyDeductionDetailes;
    }

    public Long[] getMbHeaderIds() {
        return mbHeaderIds;
    }

    public void setMbHeaderIds(final Long[] mbHeaderIds) {
        this.mbHeaderIds = mbHeaderIds;
    }

    public List<EgBilldetails> getRefundBillDetails() {
        return refundBillDetails;
    }

    public void setRefundBillDetails(final List<EgBilldetails> refundBillDetails) {
        this.refundBillDetails = refundBillDetails;
    }

}
