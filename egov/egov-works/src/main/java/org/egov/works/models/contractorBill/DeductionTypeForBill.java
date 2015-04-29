package org.egov.works.models.contractorBill;

import java.math.BigDecimal;

import org.egov.commons.CChartOfAccounts;
import org.egov.model.bills.EgBillregister;
import org.egov.works.models.workorder.WorkOrder;

public class DeductionTypeForBill {
	
	private Long id;
	private String deductionType;
	private WorkOrder workOrder;
	private EgBillregister egbill;
	private BigDecimal glcodeid;
	private BigDecimal creditamount; 
	private String narration;
	private CChartOfAccounts coa;
	
	public Long getId() {
		return id;
	}
	public WorkOrder getWorkOrder() {
		return workOrder;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public void setWorkOrder(WorkOrder workOrder) {
		this.workOrder = workOrder;
	}
	public String getDeductionType() {
		return deductionType;
	}
	public void setDeductionType(String deductionType) {
		this.deductionType = deductionType;
	}
	public BigDecimal getGlcodeid() {
		return glcodeid;
	}
	public void setGlcodeid(BigDecimal glcodeid) {
		this.glcodeid = glcodeid;
	}
	public BigDecimal getCreditamount() {
		return creditamount;
	}
	public void setCreditamount(BigDecimal creditamount) {
		this.creditamount = creditamount;
	}
	public String getNarration() {
		return narration;
	}
	public void setNarration(String narration) {
		this.narration = narration;
	}
	public EgBillregister getEgbill() {
		return egbill;
	}
	public void setEgbill(EgBillregister egbill) {
		this.egbill = egbill;
	}
	public CChartOfAccounts getCoa() {
		return coa;
	}
	public void setCoa(CChartOfAccounts coa) {
		this.coa = coa;
	}
	
}
