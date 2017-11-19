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
import org.egov.works.models.contractorBill.AssetForBill;
import org.egov.works.models.contractorBill.DeductionTypeForBill;
import org.egov.works.models.contractorBill.StatutoryDeductionsForBill;
import org.egov.works.models.measurementbook.MBHeader;
import org.egov.works.models.workorder.WorkOrder;
import org.egov.works.models.workorder.WorkOrderEstimate;

@Entity
@Table(name = "EGW_CONTRACTORBILL")
public class ContractorBillRegister extends EgBillregister {

    private static final long serialVersionUID = -6056638534067396998L;

    public enum BillStatus {
        CREATED, APPROVED, REJECTED, CANCELLED
    }

    private Integer billSequenceNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workOrder", nullable = false)
    private WorkOrder workOrder;

    @Temporal(TemporalType.DATE)
    private Date approvedDate;

    @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "egbill", targetEntity = AssetForBill.class)
    private List<AssetForBill> assetDetailsList = new LinkedList<AssetForBill>();

    @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "egbill", targetEntity = DeductionTypeForBill.class)
    private List<DeductionTypeForBill> deductionTypeList = new LinkedList<DeductionTypeForBill>();

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
    private Long approvalDepartment;

    @Transient
    private String approvalComent;

    @Transient
    private MBHeader mbHeader;

    private String cancellationReason;

    private String cancellationRemarks;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workOrderEstimate", nullable = false)
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

    public WorkOrder getWorkOrder() {
        return workOrder;
    }

    public void setWorkOrder(final WorkOrder workOrder) {
        this.workOrder = workOrder;
    }

    public List<StatutoryDeductionsForBill> getStatutoryDeductionsList() {
        return statutoryDeductionsList;
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

    public List<EgBilldetails> getRefundBillDetails() {
        return refundBillDetails;
    }

    public void setRefundBillDetails(final List<EgBilldetails> refundBillDetails) {
        this.refundBillDetails = refundBillDetails;
    }
    
}
