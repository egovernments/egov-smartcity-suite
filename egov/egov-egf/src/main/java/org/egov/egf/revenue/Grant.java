package org.egov.egf.revenue;

import java.math.BigDecimal;
import java.util.Date;

import org.egov.commons.CFinancialYear;
import org.egov.commons.CVoucherHeader;
import org.egov.infstr.models.BaseModel;
import org.egov.infra.admin.master.entity.Department;
import org.egov.model.instrument.InstrumentHeader;

public class Grant extends BaseModel{
private static final long	serialVersionUID	= 5059477505404700650L;
 private  Department department;
 private  CFinancialYear financialYear;
 private  String period;//is a string with I half,II half,Quarter I,Quarter II etc
 private String proceedingsNo;
 private Date proceedingsDate;
 private CVoucherHeader accrualVoucher;
 private BigDecimal accrualAmount;
 private CVoucherHeader generalVoucher; //GJV
 private CVoucherHeader receiptVoucher;
 private BigDecimal grantAmount; //either Receipt or GJV amount
 private InstrumentHeader ihID;
 private String remarks;
 private String grantType;//like SFC,CFC,StampDuty etc
 private String commTaxOfficer;
public Department getDepartment() {
	return department;
}

public String getPeriod() {
	return period;
}
public String getProceedingsNo() {
	return proceedingsNo;
}
public Date getProceedingsDate() {
	return proceedingsDate;
}
public String getRemarks() {
	return remarks;
}
public String getGrantType() {
	return grantType;
}
public void setDepartment(Department department) {
	this.department = department;
}
public void setPeriod(String period) {
	this.period = period;
}
public void setProceedingsNo(String proceedingsNo) {
	this.proceedingsNo = proceedingsNo;
}
public void setProceedingsDate(Date proceedingsDate) {
	this.proceedingsDate = proceedingsDate;
}
public void setAccrualAmount(BigDecimal accrualAmount) {
	this.accrualAmount = accrualAmount;
}
public void setRemarks(String remarks) {
	this.remarks = remarks;
}
public void setGrantType(String grantType) {
	this.grantType = grantType;
}
public CVoucherHeader getAccrualVoucher() {
	return accrualVoucher;
}
public void setAccrualVoucher(CVoucherHeader accrualVoucher) {
	this.accrualVoucher = accrualVoucher;
}
public CVoucherHeader getGeneralVoucher() {
	return generalVoucher;
}
public void setGeneralVoucher(CVoucherHeader generalVoucher) {
	this.generalVoucher = generalVoucher;
}
public CVoucherHeader getReceiptVoucher() {
	return receiptVoucher;
}
public void setReceiptVoucher(CVoucherHeader receiptVoucher) {
	this.receiptVoucher = receiptVoucher;
}
public BigDecimal getGrantAmount() {
	return grantAmount;
}
public void setGrantAmount(BigDecimal grantAmount) {
	this.grantAmount = grantAmount;
}
public BigDecimal getAccrualAmount() {
	return accrualAmount;
}
  public String toString()
  {
	  StringBuilder sb=new StringBuilder(64);
	  sb.append("(Id=").append(id)
	  .append(",proceedingsNo=").append(proceedingsNo)
	  .append(",proceedingsDate").append(proceedingsDate)
	  .append(",GrantType=").append(grantType)
	  .append(",grantAmount=").append(grantAmount).append(")");
	  return sb.toString();
  }

public CFinancialYear getFinancialYear() {
	return financialYear;
}

public void setFinancialYear(CFinancialYear financialYear) {
	this.financialYear = financialYear;
}

public InstrumentHeader getIhID() {
	return ihID;
}

public void setIhID(InstrumentHeader ihID) {
	this.ihID = ihID;
}

public String getCommTaxOfficer() {
	return commTaxOfficer;
}

public void setCommTaxOfficer(String commTaxOfficer) {
	this.commTaxOfficer = commTaxOfficer;
}
 
  

}
