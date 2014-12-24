package org.egov.works.models.retentionMoney;

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

public class RetentionMoneyRefund extends StateAware{
	private WorkOrder workOrder;
	private Contractor contractor;
	private EgBillregister egBillregister;
	private CChartOfAccounts glcode;
	private double retentionMoneyBeingRefunded;
	private double retentionMoneyOutstanding;
	private String owner;
	private EgwStatus egwStatus;
	private List<String> refundRetentionMoneyActions = new ArrayList<String>();
	private transient String additionalWfRule;
	private transient BigDecimal amountWfRule;
	private String remarks;
	
	
	public List<ValidationError> validate()	{
		List<ValidationError> validationErrors = new ArrayList<ValidationError>();
		if(retentionMoneyBeingRefunded<=0){
			validationErrors.add(new ValidationError("returnSecurityDepositAmount", "refund.retention.amount.notgreaterthanzero"));
		}
		if(retentionMoneyBeingRefunded>retentionMoneyOutstanding){
			validationErrors.add(new ValidationError("returnSecurityDepositAmount", "refund.retention.money.incorrect.amount"));
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

	public CChartOfAccounts getGlcode() {
		return glcode;
	}

	public void setGlcode(CChartOfAccounts glcode) {
		this.glcode = glcode;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public EgwStatus getEgwStatus() {
		return egwStatus;
	}

	public void setEgwStatus(EgwStatus egwStatus) {
		this.egwStatus = egwStatus;
	}

	public List<String> getRefundRetentionMoneyActions() {
		return refundRetentionMoneyActions;
	}

	public void setRefundRetentionMoneyActions(
			List<String> refundRetentionMoneyActions) {
		this.refundRetentionMoneyActions = refundRetentionMoneyActions;
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

	public EgBillregister getEgBillregister() {
		return egBillregister;
	}

	public void setEgBillregister(EgBillregister egBillregister) {
		this.egBillregister = egBillregister;
	}

	@Override
	public String getStateDetails() {
		return "Retention Money Refund for WO: "+workOrder.getWorkOrderNumber();
	}

	public double getRetentionMoneyBeingRefunded() {
		return retentionMoneyBeingRefunded;
	}

	public void setRetentionMoneyBeingRefunded(
			double retentionMoneyBeingRefunded) {
		this.retentionMoneyBeingRefunded = retentionMoneyBeingRefunded;
	}

	public double getRetentionMoneyOutstanding() {
		return retentionMoneyOutstanding;
	}

	public void setRetentionMoneyOutstanding(double retentionMoneyOutstanding) {
		this.retentionMoneyOutstanding = retentionMoneyOutstanding;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

}

