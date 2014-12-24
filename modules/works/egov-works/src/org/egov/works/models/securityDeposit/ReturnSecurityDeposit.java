package org.egov.works.models.securityDeposit;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.egov.commons.CChartOfAccounts;
import org.egov.commons.EgwStatus;
import org.egov.infstr.ValidationError;
import org.egov.infstr.models.StateAware;
import org.egov.model.bills.EgBillregister;
import org.egov.works.models.masters.Contractor;
import org.egov.works.models.workorder.WorkOrder;

public class ReturnSecurityDeposit extends StateAware{
	private WorkOrder workOrder;
	private Contractor contractor;
	private double returnSecurityDepositAmount;
	private double securityDepositAmountdeducted;
	private CChartOfAccounts glcode;
	private String remarks;
	private String owner;
	private String status;
	private EgwStatus egwStatus;
	private List<String> returnSecurityDepositActions = new ArrayList<String>();
	private transient String additionalWfRule;
	private transient BigDecimal amountWfRule;
	private double defectLiabilityPeriod;
	private EgBillregister egBillregister;
	
	public List<ValidationError> validate()	{
		List<ValidationError> validationErrors = new ArrayList<ValidationError>();
		if(returnSecurityDepositAmount<=0){
			validationErrors.add(new ValidationError("returnSecurityDepositAmount", "returnSecurityDeposit.amount.notgreaterthanzero"));
		}
		if(returnSecurityDepositAmount>securityDepositAmountdeducted){
			validationErrors.add(new ValidationError("returnSecurityDepositAmount", "rsd.incorrect.returnSecurityDepositAmount"));
		}
		return validationErrors;
	}
	
	public WorkOrder getWorkOrder() {
		return workOrder;
	}
	public void setWorkOrder(WorkOrder workOrder) {
		this.workOrder = workOrder;
	}
	public Contractor getContractor() {
		return contractor;
	}
	public void setContractor(Contractor contractor) {
		this.contractor = contractor;
	}
	public double getReturnSecurityDepositAmount() {
		return returnSecurityDepositAmount;
	}
	public void setReturnSecurityDepositAmount(double returnSecurityDepositAmount) {
		this.returnSecurityDepositAmount = returnSecurityDepositAmount;
	}
	public double getSecurityDepositAmountdeducted() {
		return securityDepositAmountdeducted;
	}
	public void setSecurityDepositAmountdeducted(double securityDepositAmountdeducted) {
		this.securityDepositAmountdeducted = securityDepositAmountdeducted;
	}
	public CChartOfAccounts getGlcode() {
		return glcode;
	}

	public void setGlcode(CChartOfAccounts glcode) {
		this.glcode = glcode;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public String getOwner() {
		return owner;
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public List<String> getReturnSecurityDepositActions() {
		return returnSecurityDepositActions;
	}
	public void setReturnSecurityDepositActions(
			List<String> returnSecurityDepositActions) {
		this.returnSecurityDepositActions = returnSecurityDepositActions;
	}

	public EgwStatus getEgwStatus() {
		return egwStatus;
	}

	public void setEgwStatus(EgwStatus egwStatus) {
		this.egwStatus = egwStatus;
	}

	@Override
	public String getStateDetails() {
		return "Return Security Deposit for WO: "+workOrder.getWorkOrderNumber();
	}

	public String getAdditionalWfRule() {
		return additionalWfRule;
	}

	public void setAdditionalWfRule(String additionalWfRule) {
		this.additionalWfRule = additionalWfRule;
	}

	public BigDecimal getAmountWfRule() {
		return amountWfRule;
	}

	public void setAmountWfRule(BigDecimal amountWfRule) {
		this.amountWfRule = amountWfRule;
	}
	
	public double getDefectLiabilityPeriod() {
		return defectLiabilityPeriod;
	}

	public void setDefectLiabilityPeriod(double defectLiabilityPeriod) { 
		this.defectLiabilityPeriod = defectLiabilityPeriod;
	}

	public EgBillregister getEgBillregister() {
		return egBillregister;
	}

	public void setEgBillregister(EgBillregister egBillregister) {
		this.egBillregister = egBillregister;
	}
}

