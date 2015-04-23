package org.egov.model.deduction;

public class AutoRemittanceBean {
  private  int functionId;
  private	int fundId;
  private double   gldtlAmount;
  private  int detailtypeId;
  private int detailkeyId;
  private  int remittanceGldtlId;
  private int deptId;
  private double pendingAmount;
  private int generalledgerId;
  private int bankAccountId;

 public int getFundId() {
	return fundId;
}
public void setFundId(int fundId) {
	this.fundId = fundId;
}
public double getGldtlAmount() {
	return gldtlAmount;
}
public void setGldtlAmount(double gldtlAmount) {
	this.gldtlAmount = gldtlAmount;
}
public int getDetailtypeId() {
	return detailtypeId;
}
public void setDetailtypeId(int detailtypeId) {
	this.detailtypeId = detailtypeId;
}
public int getDetailkeyId() {
	return detailkeyId; 
}
public void setDetailkeyId(int detailkeyId) {
	this.detailkeyId = detailkeyId;
}
public int getRemittanceGldtlId() {
	return remittanceGldtlId;
}
public void setRemittanceGldtlId(int remittanceGldtlId) {
	this.remittanceGldtlId = remittanceGldtlId;
}
public int getFunctionId() {
	return functionId;
}
public void setFunctionId(int functionId) {
	this.functionId = functionId;
}
public int getDeptId() {
	return deptId;
}
public void setDeptId(int deptId) {
	this.deptId = deptId;
}
public double getPendingAmount() {
	return pendingAmount;
}
public void setPendingAmount(double pendingAmount) {
	this.pendingAmount = pendingAmount;
}
@Override
public String toString() {
	return "AutoRemittanceBean [functionId=" + functionId + ", fundId="
			+ fundId + ", gldtlAmount=" + gldtlAmount + ", detailtypeId="
			+ detailtypeId + ", detailkeyId=" + detailkeyId
			+ ", remittanceGldtlId=" + remittanceGldtlId + ", deptId=" + deptId
			+ ", pendingAmount=" + pendingAmount + "]";
}
public int getGeneralledgerId() {
	return generalledgerId;
}
public void setGeneralledgerId(int generalledgerId) {
	this.generalledgerId = generalledgerId;
}
public int getBankAccountId() {
	return bankAccountId;
}
public void setBankAccountId(int bankAccountId) {
	this.bankAccountId = bankAccountId;
}

}
