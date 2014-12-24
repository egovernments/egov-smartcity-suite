package org.egov.works.models.workorder;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringEscapeUtils;
import org.egov.commons.EgwStatus;
import org.egov.infstr.ValidationError;
import org.egov.infstr.models.Money;
import org.egov.infstr.models.StateAware;
import org.egov.infstr.models.validator.CheckDateFormat;
import org.egov.infstr.models.validator.Required;
import org.egov.infstr.models.validator.ValidateDate;
import org.egov.pims.model.PersonalInformation;
import org.egov.works.models.estimate.AbstractEstimate;
import org.egov.works.models.masters.Contractor;
import org.egov.works.models.measurementbook.MBHeader;
import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.Min;


public class WorkOrder extends StateAware{	
	private Contractor contractor;
	@Required(message="workOrder.workOrderDate.null")
	@CheckDateFormat(message="invalid.fieldvalue.workOrderDate") 
	@ValidateDate(allowPast=true, dateFormat="dd/MM/yyyy",message="invalid.workOrderDate") 
	private Date workOrderDate;	
	private Date workCompletionDate;
	private String workOrderNumber;
	private String packageNumber;
	private String tenderNumber;
	private String negotiationNumber;
	@Length(max=1024,message="workOrderDetails.length")   
	private String workOrderDetails;
	@Length(max=128,message="contractPeriod.length")   
	private String contractPeriod;
	@Length(max=1024,message="agreementDetails.length")   
	private String agreementDetails;
	@Length(max=1024,message="paymentTerms.length")   
	private String paymentTerms;
	private Long documentNumber;
	private Date siteHandOverDate;
	private Date workCommencedDate;
	private double emdAmountDeposited;
	@Min(value=0,message="workorder.non.negative")
	private double securityDeposit;
	@Min(value=0,message="workorder.non.negative")
	private double labourWelfareFund;
	@Required(message = "workorder.engineerIncharge.null")
	private PersonalInformation engineerIncharge;
	
	private PersonalInformation engineerIncharge2;
	
	@Required(message = "workorder.preparedBy.null")
	private PersonalInformation workOrderPreparedBy;
	
	private List<WorkOrderEstimate> workOrderEstimates = new LinkedList<WorkOrderEstimate>();
	
	private double workOrderAmount;
	private String owner;
	private String status;
	
	private double sdDeducted;
	private double sdRefunded;
	
	private double retentionMoneyFromBill;
	private double retentionMoneyRefunded;
	
	private WorkOrder parent;
	private EgwStatus egwStatus;
	private transient String additionalWfRule;
	
	private BigDecimal totalWorkOrderAmount;
	
	private List<String> workOrderActions = new ArrayList<String>();

	public double getEmdAmountDeposited() {
		return emdAmountDeposited;
	}

	public void setEmdAmountDeposited(double emdAmountDeposited) {
		this.emdAmountDeposited = emdAmountDeposited;
	}
	
	public double getSecurityDeposit() {
		return securityDeposit;
	}

	public void setSecurityDeposit(double securityDeposit) {
		this.securityDeposit = securityDeposit;
	}
	
	public double getLabourWelfareFund() {
		return labourWelfareFund;
	}

	public void setLabourWelfareFund(double labourWelfareFund) {
		this.labourWelfareFund = labourWelfareFund;
	}
	
	
	private Set<MBHeader> mbHeaders = new HashSet<MBHeader>();
	
	public Contractor getContractor() {
		return contractor;
	}

	public void setContractor(Contractor contractor) {
		this.contractor = contractor;
	}

	public void addWorkOrderEstimate(WorkOrderEstimate workOrderEstimate) {
		this.workOrderEstimates.add(workOrderEstimate);
	}
	
	public Date getWorkOrderDate() {
		return workOrderDate;
	}

	public void setWorkOrderDate(Date workOrderDate) {
		this.workOrderDate = workOrderDate;
	}

	public String getWorkOrderNumber() {
		return workOrderNumber;
	}

	public void setWorkOrderNumber(String workOrderNumber) {		
		this.workOrderNumber = workOrderNumber;
	}	
	
	public Set<MBHeader> getMbHeaders() {
		return mbHeaders;
	}

	public void setMbHeaders(Set<MBHeader> mbHeaders) {
		this.mbHeaders = mbHeaders;
	}
	
	public String getFormattedString(double value){
		double rounded=Math.round(value*100)/100.0;
		DecimalFormat formatter = new DecimalFormat("0.00");
		formatter.setDecimalSeparatorAlwaysShown(true);
		return formatter.format(rounded);
	}
	
	public List<ValidationError> validate()	{
		List<ValidationError> validationErrors = new ArrayList<ValidationError>();
		if(contractor!=null && (contractor.getId()==null || contractor.getId()==0 || contractor.getId()==-1)){
			validationErrors.add(new ValidationError("contractor", "workOrder.contractor.null"));
		}
		else if(contractor==null) {
			validationErrors.add(new ValidationError("contractor", "workOrder.contractor.null"));
		}
		return validationErrors;
	}
	
	public PersonalInformation getEngineerIncharge() {
		return engineerIncharge;
	}

	public void setEngineerIncharge(PersonalInformation engineerIncharge) {
		this.engineerIncharge = engineerIncharge;
	}

	public Date getWorkCompletionDate() {
		return workCompletionDate;
	}

	public void setWorkCompletionDate(Date workCompletionDate) {
		this.workCompletionDate = workCompletionDate;
	}

	public String getPackageNumber() {
		return packageNumber;
	}

	public void setPackageNumber(String packageNumber) {
		this.packageNumber = packageNumber;
	}

	public String getTenderNumber() {
		return tenderNumber;
	}

	public void setTenderNumber(String tenderNumber) {
		this.tenderNumber = tenderNumber;
	}

	public String getNegotiationNumber() {
		return negotiationNumber;
	}

	public void setNegotiationNumber(String negotiationNumber) {
		this.negotiationNumber = negotiationNumber;
	}

	public List<WorkOrderEstimate> getWorkOrderEstimates() {
		return workOrderEstimates;
	}

	public void setWorkOrderEstimates(List<WorkOrderEstimate> workOrderEstimates) {
		this.workOrderEstimates = workOrderEstimates;
	}

	public String getWorkOrderDetails() {
		return workOrderDetails;
	}

	public void setWorkOrderDetails(String workOrderDetails) {
		this.workOrderDetails = StringEscapeUtils.unescapeHtml(workOrderDetails);
	}

	public String getContractPeriod() {
		return StringEscapeUtils.unescapeHtml(contractPeriod);
	}

	public void setContractPeriod(String contractPeriod) {
		this.contractPeriod = contractPeriod;
	}

	public String getAgreementDetails() {
		return agreementDetails;
	}

	public void setAgreementDetails(String agreementDetails) {
		this.agreementDetails = StringEscapeUtils.unescapeHtml(agreementDetails);
	}

	public String getPaymentTerms() {
		return paymentTerms;
	}

	public void setPaymentTerms(String paymentTerms) {
		this.paymentTerms = StringEscapeUtils.unescapeHtml(paymentTerms);
	}

	public PersonalInformation getEngineerIncharge2() {
		return engineerIncharge2;
	}

	public void setEngineerIncharge2(PersonalInformation engineerIncharge2) {
		this.engineerIncharge2 = engineerIncharge2;
	}

	public PersonalInformation getWorkOrderPreparedBy() {
		return workOrderPreparedBy;
	}

	public void setWorkOrderPreparedBy(PersonalInformation workOrderPreparedBy) {
		this.workOrderPreparedBy = workOrderPreparedBy;
	}


	public double getWorkOrderAmount() {
		return workOrderAmount;
	}

	public void setWorkOrderAmount(double workOrderAmount) {
		this.workOrderAmount = workOrderAmount;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public Long getDocumentNumber() {
		return documentNumber;
	}

	public void setDocumentNumber(Long documentNumber) {
		this.documentNumber = documentNumber;
	}

	@Override
	public String getStateDetails() {
		return "Work Order: "+this.getWorkOrderNumber();
	}

	public Date getSiteHandOverDate() {
		return siteHandOverDate;
	}

	public void setSiteHandOverDate(Date siteHandOverDate) {
		this.siteHandOverDate = siteHandOverDate;
	}

	public Date getWorkCommencedDate() {
		return workCommencedDate;
	}

	public void setWorkCommencedDate(Date workCommencedDate) {
		this.workCommencedDate = workCommencedDate;
	}

	public List<String> getWorkOrderActions() {
		return workOrderActions;
	}

	public void setWorkOrderActions(List<String> workOrderActions) {
		this.workOrderActions = workOrderActions;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	public Money getTotalWorkOrderQuantity() {
		Money totalWorkOrderQuantity;
		double qty=0;
		for (WorkOrderEstimate workOrderEstimate : workOrderEstimates)
		for (WorkOrderActivity woa : workOrderEstimate.getWorkOrderActivities()) {
			qty+=woa.getApprovedQuantity();
		}
		totalWorkOrderQuantity = new Money(qty);
		return totalWorkOrderQuantity;
	}

	public double getSdDeducted() {
		return sdDeducted;
}

	public void setSdDeducted(double sdDeducted) {
		this.sdDeducted = sdDeducted;
	}

	public double getSdRefunded() {
		return sdRefunded;
	}

	public void setSdRefunded(double sdRefunded) {
		this.sdRefunded = sdRefunded;
	}
	
	public double getDefectLiabilityPeriod() {
		return defectLiabilityPeriod;
	}

	public void setDefectLiabilityPeriod(double defectLiabilityPeriod) { 
		this.defectLiabilityPeriod = defectLiabilityPeriod;
	}
	
	private double defectLiabilityPeriod;

	public WorkOrder getParent() {
		return parent;
	}

	public void setParent(WorkOrder parent) {
		this.parent = parent;
	}

	public EgwStatus getEgwStatus() {
		return egwStatus;
	}

	public void setEgwStatus(EgwStatus egwStatus) {
		this.egwStatus = egwStatus;
	}

	public String getAdditionalWfRule() {
		return additionalWfRule;
	}

	public void setAdditionalWfRule(String additionalWfRule) {
		this.additionalWfRule = additionalWfRule;
	}

	public BigDecimal getTotalWorkOrderAmount() {
		return totalWorkOrderAmount;
	}

	public void setTotalWorkOrderAmount(BigDecimal totalWorkOrderAmount) {
		this.totalWorkOrderAmount = totalWorkOrderAmount;
	}

	public double getRetentionMoneyFromBill() {
		return retentionMoneyFromBill;
	}

	public void setRetentionMoneyFromBill(double retentionMoneyFromBill) {
		this.retentionMoneyFromBill = retentionMoneyFromBill;
	}

	public double getRetentionMoneyRefunded() {
		return retentionMoneyRefunded;
	}

	public void setRetentionMoneyRefunded(double retentionMoneyRefunded) {
		this.retentionMoneyRefunded = retentionMoneyRefunded;
	}

}
