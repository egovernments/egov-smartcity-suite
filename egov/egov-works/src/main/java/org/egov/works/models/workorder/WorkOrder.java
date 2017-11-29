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
package org.egov.works.models.workorder;

import org.egov.commons.EgwStatus;
import org.egov.eis.entity.Employee;
import org.egov.infra.persistence.entity.Auditable;
import org.egov.infra.persistence.entity.component.Money;
import org.egov.infra.persistence.validator.annotation.DateFormat;
import org.egov.infra.persistence.validator.annotation.Required;
import org.egov.infra.persistence.validator.annotation.ValidateDate;
import org.egov.infra.utils.DateUtils;
import org.egov.infra.validation.exception.ValidationError;
import org.egov.infra.workflow.entity.StateAware;
import org.egov.pims.commons.Position;
import org.egov.works.lineestimate.entity.DocumentDetails;
import org.egov.works.models.masters.Contractor;
import org.egov.works.models.measurementbook.MBHeader;
import org.egov.works.models.tender.OfflineStatus;
import org.egov.works.revisionestimate.entity.enums.RevisionType;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class WorkOrder extends StateAware<Position> implements Auditable {

    private static final long serialVersionUID = -3955155765490287178L;

    private Long id;

    @NotNull
    private Contractor contractor;

    @Required(message = "workOrder.workOrderDate.null")
    @DateFormat(message = "invalid.fieldvalue.workOrderDate")
    @ValidateDate(allowPast = true, dateFormat = "dd/MM/yyyy", message = "invalid.workOrderDate")
    private Date workOrderDate;

    @NotNull
    private String workOrderNumber;

    private String packageNumber;
    private String tenderNumber;
    private String negotiationNumber;

    @Length(max = 1024, message = "workOrderDetails.length")
    private String workOrderDetails;

    private Integer contractPeriod;

    @Length(max = 1024, message = "agreementDetails.length")
    private String agreementDetails;

    @Length(max = 1024, message = "paymentTerms.length")
    private String paymentTerms;

    @Min(value = 0, message = "workorder.emdAmountDeposited.non.negative")
    private double emdAmountDeposited;

    @Min(value = 0, message = "workorder.non.negative")
    private double securityDeposit;

    @Min(value = 0, message = "workorder.non.negative")
    private double labourWelfareFund;

    @Required(message = "workorder.engineerIncharge.null")
    private Employee engineerIncharge;

    @Min(value = 0, message = "workorder.non.negative")
    private double defectLiabilityPeriod;

    @NotNull
    @Min(value = 1)
    private double workOrderAmount;

    @NotNull
    private EgwStatus egwStatus;

    private double tenderFinalizedPercentage;

    @DateFormat(message = "invalid.fieldvalue.workOrderDate")
    private Date approvedDate;

    private String fileNumber;

    @DateFormat(message = "invalid.fieldvalue.fileDate")
    private Date fileDate;

    @Length(max = 1024, message = "bankguarantee.length")
    private String bankGuarantee;

    private String estimateNumber;

    private WorkOrder parent;

    private Date expectedCompletionDate;
    private String tenderType;
    private String owner;
    private String status;
    private transient String percentageSign;

    private List<WorkOrderEstimate> workOrderEstimates = new LinkedList<>();
    private Set<OfflineStatus> offlineStatuses = new HashSet<>();
    private List<String> workOrderActions = new ArrayList<>();
    private Set<WorkOrder> revisionWOs = new HashSet<>();
    private Set<MBHeader> mbHeaders = new HashSet<>();

    private transient List<DocumentDetails> documentDetails = new ArrayList<>();

    private String cancellationReason;

    private String cancellationRemarks;

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

    public Set<OfflineStatus> getOfflineStatuses() {
        return offlineStatuses;
    }

    public void setOfflineStatuses(final Set<OfflineStatus> offlineStatuses) {
        this.offlineStatuses = offlineStatuses;
    }

    public List<String> getWorkOrderActions() {
        return workOrderActions;
    }

    public void setWorkOrderActions(final List<String> workOrderActions) {
        this.workOrderActions = workOrderActions;
    }

    public Set<WorkOrder> getRevisionWOs() {
        return revisionWOs;
    }

    public void setRevisionWOs(final Set<WorkOrder> revisionWOs) {
        this.revisionWOs = revisionWOs;
    }

    public void addWorkOrderEstimate(final WorkOrderEstimate workOrderEstimate) {
        workOrderEstimates.add(workOrderEstimate);
    }

    public Set<MBHeader> getMbHeaders() {
        return mbHeaders;
    }

    public void setMbHeaders(final Set<MBHeader> mbHeaders) {
        this.mbHeaders = mbHeaders;
    }

    public String getFormattedString(final double value) {
        final double rounded = Math.round(value * 100) / 100.0;
        final DecimalFormat formatter = new DecimalFormat("0.00");
        formatter.setDecimalSeparatorAlwaysShown(true);
        return formatter.format(rounded);
    }

    public List<ValidationError> validate() {
        final List<ValidationError> validationErrors = new ArrayList<>();
        if (contractor == null || contractor.getId() == null || contractor.getId() == 0 || contractor.getId() == -1)
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
            final Date date = DateUtils.add(getWorkOrderDate(), Calendar.DAY_OF_MONTH,
                    getContractPeriod());
            expectedCompletionDate = date;
        }

        return expectedCompletionDate;
    }

    public void setExpectedCompletionDate(final Date expectedCompletionDate) {
        this.expectedCompletionDate = expectedCompletionDate;
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
        this.documentDetails = documentDetails;
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

}
