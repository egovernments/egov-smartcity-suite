/**
 * eGov suite of products aim to improve the internal efficiency,transparency,
   accountability and the service delivery of the government  organizations.

    Copyright (C) <2015>  eGovernments Foundation

    The updated version of eGov suite of products as by eGovernments Foundation
    is available at http://www.egovernments.org

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program. If not, see http://www.gnu.org/licenses/ or
    http://www.gnu.org/licenses/gpl.html .

    In addition to the terms of the GPL license to be adhered to in using this
    program, the following additional terms are to be complied with:

	1) All versions of this program, verbatim or modified must carry this
	   Legal Notice.

	2) Any misrepresentation of the origin of the material is prohibited. It
	   is required that all modified versions of this material be marked in
	   reasonable ways as different from the original version.

	3) This license does not grant any rights to any user of the program
	   with regards to rights under trademark law for use of the trade names
	   or trademarks of eGovernments Foundation.

  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.works.models.workorder;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.validation.constraints.Min;

import org.apache.commons.lang.StringUtils;
import org.egov.commons.EgwStatus;
import org.egov.infra.persistence.validator.annotation.DateFormat;
import org.egov.infra.persistence.validator.annotation.Required;
import org.egov.infra.persistence.validator.annotation.ValidateDate;
import org.egov.infra.validation.exception.ValidationError;
import org.egov.infstr.models.Money;
import org.egov.infstr.utils.DateUtils;
import org.egov.pims.model.PersonalInformation;
import org.egov.works.models.masters.Contractor;
import org.egov.works.models.measurementbook.MBHeader;
import org.egov.works.models.revisionEstimate.RevisionType;
import org.egov.works.models.tender.OfflineStatus;
import org.egov.works.models.workflow.WorkFlow;
import org.hibernate.validator.constraints.Length;

public class WorkOrder extends WorkFlow {

    private static final long serialVersionUID = -8549101031204311679L;
    private Contractor contractor;
    @Required(message = "workOrder.workOrderDate.null")
    @DateFormat(message = "invalid.fieldvalue.workOrderDate")
    @ValidateDate(allowPast = true, dateFormat = "dd/MM/yyyy", message = "invalid.workOrderDate")
    private Date workOrderDate;
    private String workOrderNumber;
    private String packageNumber;
    private String tenderNumber;
    private String negotiationNumber;
    @Length(max = 1024, message = "workOrderDetails.length")
    private String workOrderDetails;
    @Length(max = 128, message = "contractPeriod.length")
    private String contractPeriod;
    @Length(max = 1024, message = "agreementDetails.length")
    private String agreementDetails;
    @Length(max = 1024, message = "paymentTerms.length")
    private String paymentTerms;
    private Long documentNumber;
    private Date siteHandOverDate;
    private Date workCommencedDate;
    @Min(value = 0, message = "workorder.emdAmountDeposited.non.negative")
    private double emdAmountDeposited;
    @Min(value = 0, message = "workorder.non.negative")
    private double securityDeposit;
    @Min(value = 0, message = "workorder.non.negative")
    private double labourWelfareFund;
    @Required(message = "workorder.engineerIncharge.null")
    private PersonalInformation engineerIncharge;

    private Double defectLiabilityPeriod;

    private PersonalInformation engineerIncharge2;

    @Required(message = "workorder.preparedBy.null")
    private PersonalInformation workOrderPreparedBy;

    private double workOrderAmount;
    private String owner;
    private String status;

    private EgwStatus egwStatus;
    private WorkOrder parent;
    private Date expectedCompletionDate;
    private BigDecimal negotiationPercentage;
    private Date approvedDate;
    private String tenderType;

    private List<WorkOrderEstimate> workOrderEstimates = new LinkedList<WorkOrderEstimate>();
    private Set<OfflineStatus> offlineStatuses = new HashSet<OfflineStatus>();
    private List<String> workOrderActions = new ArrayList<String>();
    private Set<WorkOrder> revisionWOs = new HashSet<WorkOrder>();

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

    private Set<MBHeader> mbHeaders = new HashSet<MBHeader>();

    public Contractor getContractor() {
        return contractor;
    }

    public void setContractor(final Contractor contractor) {
        this.contractor = contractor;
    }

    public void addWorkOrderEstimate(final WorkOrderEstimate workOrderEstimate) {
        workOrderEstimates.add(workOrderEstimate);
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
        final List<ValidationError> validationErrors = new ArrayList<ValidationError>();
        if (contractor != null && (contractor.getId() == null || contractor.getId() == 0 || contractor.getId() == -1))
            validationErrors.add(new ValidationError("contractor", "workOrder.contractor.null"));
        else if (contractor == null)
            validationErrors.add(new ValidationError("contractor", "workOrder.contractor.null"));
        return validationErrors;
    }

    public PersonalInformation getEngineerIncharge() {
        return engineerIncharge;
    }

    public void setEngineerIncharge(final PersonalInformation engineerIncharge) {
        this.engineerIncharge = engineerIncharge;
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

    public List<WorkOrderEstimate> getWorkOrderEstimates() {
        return workOrderEstimates;
    }

    public void setWorkOrderEstimates(final List<WorkOrderEstimate> workOrderEstimates) {
        this.workOrderEstimates = workOrderEstimates;
    }

    public String getWorkOrderDetails() {
        return workOrderDetails;
    }

    public void setWorkOrderDetails(final String workOrderDetails) {
        this.workOrderDetails = workOrderDetails;
    }

    public String getContractPeriod() {
        return contractPeriod;
    }

    public void setContractPeriod(final String contractPeriod) {
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

    public PersonalInformation getEngineerIncharge2() {
        return engineerIncharge2;
    }

    public void setEngineerIncharge2(final PersonalInformation engineerIncharge2) {
        this.engineerIncharge2 = engineerIncharge2;
    }

    public PersonalInformation getWorkOrderPreparedBy() {
        return workOrderPreparedBy;
    }

    public void setWorkOrderPreparedBy(final PersonalInformation workOrderPreparedBy) {
        this.workOrderPreparedBy = workOrderPreparedBy;
    }

    public double getWorkOrderAmount() {
        return workOrderAmount;
    }

    public void setWorkOrderAmount(final double workOrderAmount) {
        this.workOrderAmount = workOrderAmount;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(final String owner) {
        this.owner = owner;
    }

    public Long getDocumentNumber() {
        return documentNumber;
    }

    public void setDocumentNumber(final Long documentNumber) {
        this.documentNumber = documentNumber;
    }

    @Override
    public String getStateDetails() {
        return "Work Order: " + getWorkOrderNumber();
    }

    public Date getSiteHandOverDate() {
        return siteHandOverDate;
    }

    public void setSiteHandOverDate(final Date siteHandOverDate) {
        this.siteHandOverDate = siteHandOverDate;
    }

    public Date getWorkCommencedDate() {
        return workCommencedDate;
    }

    public void setWorkCommencedDate(final Date workCommencedDate) {
        this.workCommencedDate = workCommencedDate;
    }

    public List<String> getWorkOrderActions() {
        return workOrderActions;
    }

    public void setWorkOrderActions(final List<String> workOrderActions) {
        this.workOrderActions = workOrderActions;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(final String status) {
        this.status = status;
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

    public Set<OfflineStatus> getOfflineStatuses() {
        return offlineStatuses;
    }

    public void setOfflineStatuses(final Set<OfflineStatus> offlineStatuses) {
        this.offlineStatuses = offlineStatuses;
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

    public Set<WorkOrder> getRevisionWOs() {
        return revisionWOs;
    }

    public void setRevisionWOs(final Set<WorkOrder> revisionWOs) {
        this.revisionWOs = revisionWOs;
    }

    public Date getExpectedCompletionDate() {
        if (getContractPeriod() != null && StringUtils.isNotBlank(getContractPeriod())) {
            final Date date = DateUtils.add(getWorkOrderDate(), Calendar.DAY_OF_MONTH,
                    Integer.parseInt(getContractPeriod()));
            expectedCompletionDate = date;
        }

        return expectedCompletionDate;
    }

    public void setExpectedCompletionDate(final Date expectedCompletionDate) {
        this.expectedCompletionDate = expectedCompletionDate;
    }

    public BigDecimal getNegotiationPercentage() {
        return negotiationPercentage;
    }

    public void setNegotiationPercentage(final BigDecimal negotiationPercentage) {
        this.negotiationPercentage = negotiationPercentage;
    }

    @Override
    public String toString() {
        return "WorkOrder ( Id : " + getId() + "Work Order No: " + workOrderNumber + ")";
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

    public Double getDefectLiabilityPeriod() {
        return defectLiabilityPeriod;
    }

    public void setDefectLiabilityPeriod(final Double defectLiabilityPeriod) {
        this.defectLiabilityPeriod = defectLiabilityPeriod;
    }

}
