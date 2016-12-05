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
package org.egov.works.workorder.entity;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
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
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.egov.commons.EgwStatus;
import org.egov.eis.entity.Employee;
import org.egov.infra.persistence.entity.Auditable;
import org.egov.infra.persistence.entity.component.Money;
import org.egov.infra.persistence.validator.annotation.DateFormat;
import org.egov.infra.persistence.validator.annotation.OptionalPattern;
import org.egov.infra.persistence.validator.annotation.Required;
import org.egov.infra.persistence.validator.annotation.ValidateDate;
import org.egov.infra.utils.DateUtils;
import org.egov.infra.validation.exception.ValidationError;
import org.egov.infra.workflow.entity.StateAware;
import org.egov.works.lineestimate.entity.DocumentDetails;
import org.egov.works.masters.entity.Contractor;
import org.egov.works.mb.entity.MBHeader;
import org.egov.works.models.tender.OfflineStatus;
import org.egov.works.revisionestimate.entity.enums.RevisionType;
import org.egov.works.utils.WorksConstants;
import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "EGW_WORKORDER")
@Inheritance(strategy = InheritanceType.JOINED)
@NamedQueries({
        @NamedQuery(name = WorkOrder.GETCONTRACTORSWITHWO, query = " select distinct wo.contractor from WorkOrder wo where wo.parent is null "),
        @NamedQuery(name = WorkOrder.GETUNIQUEWO, query = " select distinct wo from WorkOrder wo where wo.parent is null "),
        @NamedQuery(name = WorkOrder.GETAPPROVEDCONTRACTORSWITHWO, query = " select distinct wo.contractor from WorkOrder wo where wo.parent is null and wo.egwStatus.code='APPROVED' "),
        @NamedQuery(name = WorkOrder.GET_All_CONTRACTORS, query = " select distinct wo.contractor from WorkOrder wo join wo.contractor.contractorDetails as detail where wo.parent is null and detail.status.description = ? and current_date >= detail.validity.startDate and (detail.validity.endDate is null or detail.validity.endDate >= current_date )") })
@SequenceGenerator(name = WorkOrder.SEQ_EGW_WORKORDER, sequenceName = WorkOrder.SEQ_EGW_WORKORDER, allocationSize = 1)
public class WorkOrder extends StateAware implements Auditable {

    private static final long serialVersionUID = -3955155765490287178L;

    public static final String SEQ_EGW_WORKORDER = "SEQ_EGW_WORKORDER";
    public static final String GETCONTRACTORSWITHWO = "getContractorsWithWO";
    public static final String GETUNIQUEWO = "getUniqueWO";
    public static final String GET_All_CONTRACTORS = "GET_All_CONTRACTORS";
    public static final String GETAPPROVEDCONTRACTORSWITHWO = "getApprovedContractorsWithWO";

    public enum OfflineStatuses {

        ACCEPTANCE_LETTER_ISSUED, ACCEPTANCE_LETTER_ACKNOWLEDGED, AGREEMENT_ORDER_SIGNED, WORK_ORDER_ACKNOWLEDGED, SITE_HANDED_OVER, WORK_COMMENCED;

        @Override
        public String toString() {
            return name().toLowerCase();
        }
    }

    @Id
    @GeneratedValue(generator = SEQ_EGW_WORKORDER, strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CONTRACTOR_ID", nullable = false)
    private Contractor contractor;

    @Required(message = "workOrder.workOrderDate.null")
    @DateFormat(message = "invalid.fieldvalue.workOrderDate")
    @ValidateDate(allowPast = true, dateFormat = "dd/MM/yyyy", message = "invalid.workOrderDate")
    @Temporal(value = TemporalType.DATE)
    @Column(name = "WORKORDER_DATE")
    private Date workOrderDate;

    @NotNull
    @Length(max = 256)
    @Column(name = "WORKORDER_NUMBER")
    private String workOrderNumber;

    @Column(name = "WP_NUMBER")
    private String packageNumber;

    @Column(name = "TENDER_NUMBER")
    private String tenderNumber;

    @Column(name = "NEGOTIATION_NUMBER")
    private String negotiationNumber;

    @Column(name = "WORK_ORDER_DETAILS")
    @Length(max = 1024, message = "workOrderDetails.length")
    private String workOrderDetails;

    @Column(name = "CONTRACT_PERIOD")
    private Integer contractPeriod;

    @Column(name = "AGREEMENT_DETAILS")
    @Length(max = 1024, message = "agreementDetails.length")
    private String agreementDetails;

    @Column(name = "PAYMENT_TERMS")
    @Length(max = 1024, message = "paymentTerms.length")
    private String paymentTerms;

    @Column(name = "EMD_AMOUNT_DEPOSITED")
    @Min(value = 0, message = "workorder.emdAmountDeposited.non.negative")
    private double emdAmountDeposited;

    @Column(name = "SECURITY_DEPOSIT")
    @Min(value = 0, message = "workorder.non.negative")
    private double securityDeposit;

    @Column(name = "LABOUR_WELFARE_FUND")
    @Min(value = 0, message = "workorder.non.negative")
    private double labourWelfareFund;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ENGINEERINCHARGE", nullable = false)
    @Required(message = "workorder.engineerIncharge.null")
    private Employee engineerIncharge;

    @Column(name = "DEFECT_LIABILITY_PERIOD")
    @Min(value = 0, message = "workorder.non.negative")
    private double defectLiabilityPeriod;

    @NotNull
    @Min(value = 1)
    @Column(name = "WORKORDER_AMOUNT")
    private double workOrderAmount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "STATUS_ID", nullable = false)
    private EgwStatus egwStatus;

    private double tenderFinalizedPercentage;

    @Column(name = "APPROVED_DATE")
    @DateFormat(message = "invalid.fieldvalue.workOrderDate")
    @Temporal(value = TemporalType.DATE)
    private Date approvedDate;

    @Length(max = 100)
    private String fileNumber;

    @DateFormat(message = "invalid.fieldvalue.fileDate")
    @Temporal(value = TemporalType.DATE)
    private Date fileDate;

    @Length(max = 1024, message = "bankguarantee.length")
    @OptionalPattern(regex = WorksConstants.ALPHANUMERICWITHALLSPECIALCHAR, message = "workorder.bankguarantee.alphaNumeric")
    private String bankGuarantee;

    @Length(max = 100)
    private String estimateNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PARENTID")
    private WorkOrder parent;

    @Transient
    private Date expectedCompletionDate;

    @Transient
    private String tenderType;

    @Transient
    private String owner;

    @Transient
    private String status;

    @Transient
    private String percentageSign;

    @JsonIgnore
    @OrderBy("id")
    @OneToMany(mappedBy = "workOrder", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, targetEntity = WorkOrderEstimate.class)
    private List<WorkOrderEstimate> workOrderEstimates = new ArrayList<WorkOrderEstimate>(0);

    @JsonIgnore
    @Transient
    private List<OfflineStatus> offlineStatuses = new ArrayList<OfflineStatus>();

    @Transient
    private List<String> workOrderActions = new ArrayList<String>();

    @JsonIgnore
    @OrderBy("id")
    @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, targetEntity = WorkOrder.class)
    private List<WorkOrder> revisionWOs = new ArrayList<WorkOrder>();

    @JsonIgnore
    @OrderBy("id")
    @OneToMany(mappedBy = "workOrder", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, targetEntity = MBHeader.class)
    private List<MBHeader> mbHeaders = new ArrayList<MBHeader>();

    private final transient List<DocumentDetails> documentDetails = new ArrayList<DocumentDetails>(0);

    @Length(max = 50)
    private String cancellationReason;

    @Length(max = 256)
    private String cancellationRemarks;

    @Transient
    private Long approvalDepartment;

    @Transient
    private String approvalComent;

    @Column(name = "TOTAL_INCLUDING_RE")
    private Double totalIncludingRE;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(final Long id) {
        this.id = id;
    }

    public Contractor getContractor() {
        return contractor;
    }

    public void setContractor(final Contractor contractor) {
        this.contractor = contractor;
    }

    public Date getWorkOrderDate() {
        return workOrderDate;
    }

    public void setWorkOrderDate(final Date workOrderDate) {
        this.workOrderDate = workOrderDate;
    }

    public String getWorkOrderNumber() {
        return workOrderNumber;
    }

    public void setWorkOrderNumber(final String workOrderNumber) {
        this.workOrderNumber = workOrderNumber;
    }

    public String getPackageNumber() {
        return packageNumber;
    }

    public void setPackageNumber(final String packageNumber) {
        this.packageNumber = packageNumber;
    }

    public String getTenderNumber() {
        return tenderNumber;
    }

    public void setTenderNumber(final String tenderNumber) {
        this.tenderNumber = tenderNumber;
    }

    public String getNegotiationNumber() {
        return negotiationNumber;
    }

    public void setNegotiationNumber(final String negotiationNumber) {
        this.negotiationNumber = negotiationNumber;
    }

    public String getWorkOrderDetails() {
        return workOrderDetails;
    }

    public void setWorkOrderDetails(final String workOrderDetails) {
        this.workOrderDetails = workOrderDetails;
    }

    public Integer getContractPeriod() {
        return contractPeriod;
    }

    public void setContractPeriod(final Integer contractPeriod) {
        this.contractPeriod = contractPeriod;
    }

    public String getAgreementDetails() {
        return agreementDetails;
    }

    public void setAgreementDetails(final String agreementDetails) {
        this.agreementDetails = agreementDetails;
    }

    public String getPaymentTerms() {
        return paymentTerms;
    }

    public void setPaymentTerms(final String paymentTerms) {
        this.paymentTerms = paymentTerms;
    }

    public double getEmdAmountDeposited() {
        return emdAmountDeposited;
    }

    public void setEmdAmountDeposited(final double emdAmountDeposited) {
        this.emdAmountDeposited = emdAmountDeposited;
    }

    public double getSecurityDeposit() {
        return securityDeposit;
    }

    public void setSecurityDeposit(final double securityDeposit) {
        this.securityDeposit = securityDeposit;
    }

    public double getLabourWelfareFund() {
        return labourWelfareFund;
    }

    public void setLabourWelfareFund(final double labourWelfareFund) {
        this.labourWelfareFund = labourWelfareFund;
    }

    public Employee getEngineerIncharge() {
        return engineerIncharge;
    }

    public void setEngineerIncharge(final Employee engineerIncharge) {
        this.engineerIncharge = engineerIncharge;
    }

    public double getDefectLiabilityPeriod() {
        return defectLiabilityPeriod;
    }

    public void setDefectLiabilityPeriod(final double defectLiabilityPeriod) {
        this.defectLiabilityPeriod = defectLiabilityPeriod;
    }

    public double getWorkOrderAmount() {
        return workOrderAmount;
    }

    public void setWorkOrderAmount(final double workOrderAmount) {
        this.workOrderAmount = workOrderAmount;
    }

    public EgwStatus getEgwStatus() {
        return egwStatus;
    }

    public void setEgwStatus(final EgwStatus egwStatus) {
        this.egwStatus = egwStatus;
    }

    public WorkOrder getParent() {
        return parent;
    }

    public void setParent(final WorkOrder parent) {
        this.parent = parent;
    }

    public String getEstimateNumber() {
        return estimateNumber;
    }

    public void setEstimateNumber(final String estimateNumber) {
        this.estimateNumber = estimateNumber;
    }

    public double getTenderFinalizedPercentage() {
        return tenderFinalizedPercentage;
    }

    public void setTenderFinalizedPercentage(final double tenderFinalizedPercentage) {
        this.tenderFinalizedPercentage = tenderFinalizedPercentage;
    }

    public Date getApprovedDate() {
        return approvedDate;
    }

    public void setApprovedDate(final Date approvedDate) {
        this.approvedDate = approvedDate;
    }

    public String getTenderType() {
        return tenderType;
    }

    public void setTenderType(final String tenderType) {
        this.tenderType = tenderType;
    }

    public String getFileNumber() {
        return fileNumber;
    }

    public void setFileNumber(final String fileNumber) {
        this.fileNumber = fileNumber;
    }

    public Date getFileDate() {
        return fileDate;
    }

    public void setFileDate(final Date fileDate) {
        this.fileDate = fileDate;
    }

    public String getBankGuarantee() {
        return bankGuarantee;
    }

    public void setBankGuarantee(final String bankGuarantee) {
        this.bankGuarantee = bankGuarantee;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(final String owner) {
        this.owner = owner;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(final String status) {
        this.status = status;
    }

    public List<WorkOrderEstimate> getWorkOrderEstimates() {
        return workOrderEstimates;
    }

    public void setWorkOrderEstimates(final List<WorkOrderEstimate> workOrderEstimates) {
        this.workOrderEstimates = workOrderEstimates;
    }

    public List<OfflineStatus> getOfflineStatuses() {
        return offlineStatuses;
    }

    public void setOfflineStatuses(final List<OfflineStatus> offlineStatuses) {
        this.offlineStatuses = offlineStatuses;
    }

    public List<String> getWorkOrderActions() {
        return workOrderActions;
    }

    public void setWorkOrderActions(final List<String> workOrderActions) {
        this.workOrderActions = workOrderActions;
    }

    public List<WorkOrder> getRevisionWOs() {
        return revisionWOs;
    }

    public void setRevisionWOs(final List<WorkOrder> revisionWOs) {
        this.revisionWOs = revisionWOs;
    }

    public void setExpectedCompletionDate(final Date expectedCompletionDate) {
        this.expectedCompletionDate = expectedCompletionDate;
    }

    public void addWorkOrderEstimate(final WorkOrderEstimate workOrderEstimate) {
        workOrderEstimates.add(workOrderEstimate);
    }

    public List<MBHeader> getMbHeaders() {
        return mbHeaders;
    }

    public void setMbHeaders(final List<MBHeader> mbHeaders) {
        this.mbHeaders = mbHeaders;
    }

    public String getFormattedString(final double value) {
        final double rounded = Math.round(value * 100) / 100.0;
        final DecimalFormat formatter = new DecimalFormat("0.00");
        formatter.setDecimalSeparatorAlwaysShown(true);
        return formatter.format(rounded);
    }

    public List<ValidationError> validate() {
        final List<ValidationError> validationErrors = new ArrayList<ValidationError>();
        if (contractor != null && (contractor.getId() == null || contractor.getId() == 0 || contractor.getId() == -1))
            validationErrors.add(new ValidationError("contractor", "workOrder.contractor.null"));
        else if (contractor == null)
            validationErrors.add(new ValidationError("contractor", "workOrder.contractor.null"));
        return validationErrors;
    }

    @Override
    public String getStateDetails() {
        return "Work Order: " + getWorkOrderNumber();
    }

    public Money getTotalWorkOrderQuantity() {
        Money totalWorkOrderQuantity;
        double qty = 0;
        for (final WorkOrderEstimate workOrderEstimate : workOrderEstimates)
            for (final WorkOrderActivity woa : workOrderEstimate.getWorkOrderActivities())
                if (woa.getActivity() != null && woa.getActivity().getRevisionType() != null
                        && woa.getActivity().getRevisionType().equals(RevisionType.REDUCED_QUANTITY))
                    qty -= woa.getApprovedQuantity();
                else
                    qty += woa.getApprovedQuantity();
        totalWorkOrderQuantity = new Money(qty);
        return totalWorkOrderQuantity;
    }

    public Date getExpectedCompletionDate() {
        if (getContractPeriod() > 0) {
            final Date date = DateUtils.add(getWorkOrderDate(), Calendar.DAY_OF_MONTH, getContractPeriod());
            expectedCompletionDate = date;
        }

        return expectedCompletionDate;
    }

    @Override
    public String toString() {
        return "WorkOrder ( Id : " + getId() + "Work Order No: " + workOrderNumber + ")";
    }

    public String getPercentageSign() {
        return percentageSign;
    }

    public void setPercentageSign(final String percentageSign) {
        this.percentageSign = percentageSign;
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

    public Double getTotalIncludingRE() {
        return totalIncludingRE;
    }

    public void setTotalIncludingRE(final Double totalIncludingRE) {
        this.totalIncludingRE = totalIncludingRE;
    }

}
