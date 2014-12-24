package org.egov.payroll.client.payslip;


import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;


/**
 * TODO Brief Description of the purpose of the class/interface
 *
 * @author Lokesh,Mamatha E
 * @version 1.00
 * @see
 * @see
 * @since   1.00
 */

public class SalaryPaySlipForm extends ActionForm 
{
	// properties realted to payslip
	private String payHead[];
	private String pct[];
	private String pctBasis[];
	private String calculationType[];
	private String salaryAdvances[];
	private String accountCodeId[];
	private String employeeCodeId=null;
	private String month=null;
	private String year=null;
	private String payHeadAmount[];
	private String yearToDateHead[];
	private String yearToDateTax[];
	private String yearToDateAdv[];
	private String yearToDateDed[];
	private String yearToDateOther[];
	private String salaryAdvancesAmount[];
	private String otherDeductionsAmount[];
	private String taxPayHeadId[];
	private String taxTypeAmount[];
	private String netPay=null;
	private String grossPay=null;
	private String basicPay=null;
	private String payType;
	private String userName=null;
	private String[] taxTypeName=null;
	private String [] dedOthrName=null;
	private String[] dedOthrAmount=null;
	private  String [] dedRefNo=null;
	private String department = null;
	
	// properties related to pay scale
	
	private String payScaleName;
	private String payCommision;
	private String effectiveFrom;
	private String amountFrom;
	private String amountTo;
	private String increment;
	private String effectiveTo;
	private String functionaryId;
	private String deptId;
	private String ruleScript;
	private String remarks;
	private String numDays;
	private String delPayhead[];
	private String workingDays;
	private String earningId[];
	private String advDedId[];
	private String otherDedId[];
	private String payslipId;
	private String modifyRemarks;
	private String supplComment;
	private String frwdType;
	private String leaveApplication; 
	private String otherDedTxDedId[];
	private String dedOthrTxId[];
	//Increment slabs for payscale
	private String incSlabFrmAmt[];
	private String incSlabToAmt[];
	private String incSlabAmt[];
	private ArrayList dedTaxlist;
	private String[] dedTaxamountlist;
	private ArrayList dedOtherlist;
	private String[] dedOtheramountlist;	
	private ArrayList deductionsAdvList;
	private ArrayList otherDedList;
	private String gradeId;
	private String type;
	private String[] referenceno;
	private String[] billNumberIds;
	private String[] advanceSchedule;
	
	//Added for manual workflow
	private String approverDept;
	private String approverDesig;
	private String approverEmpAssignmentId;
	
	private String functionId;	
	
	private String billNumberId;
	
	public String getBillNumberId() {
		return billNumberId;
	}

	public void setBillNumberId(String billNumberId) {
		this.billNumberId = billNumberId;
	}
	
	
	public String[] getBillNumberIds() {
		return billNumberIds;
	}

	public void setBillNumberIds(String[] billNumberIds) {
		this.billNumberIds = billNumberIds;
	}

	public String getApproverEmpAssignmentId() {
		return approverEmpAssignmentId;
	}
	public void setApproverEmpAssignmentId(String approverEmpAssignmentId) {
		this.approverEmpAssignmentId = approverEmpAssignmentId;
	}
	public String getApproverDesig() {
		return approverDesig;
	}
	public void setApproverDesig(String approverDesig) {
		this.approverDesig = approverDesig;
	}
	public String getApproverDept() {
		return approverDept;
	}
	public void setApproverDept(String approverDept) {
		this.approverDept = approverDept;
	}
	public String[] getReferenceno() {
		return referenceno;
	}
	public void setReferenceno(String[] referenceno) {
		this.referenceno = referenceno;
	}
	public String getGradeId() {
		return gradeId;
	}
	public void setGradeId(String gradeId) {
		this.gradeId = gradeId;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public void reset(ActionMapping mapping,HttpServletRequest request) {
	   this.payHead = null;
	   this.payHeadAmount = null;
	   this.pct = null;
	   this.pctBasis = null;
	   this.accountCodeId = null;
	   this.calculationType = null;
	   this.employeeCodeId = "";
	   this.grossPay = "";
	   this.month = "";
	   this.year = "";
	   this.netPay = "";
	   this.otherDeductionsAmount = null;
	   this.salaryAdvances = null;
	   this.salaryAdvancesAmount = null;
	   this.taxPayHeadId = null;
	   this.taxTypeAmount = null;
	   this.yearToDateAdv = null;
	   this.yearToDateDed = null;
	   this.yearToDateTax = null;
	   this.yearToDateHead = null;
	   this.payScaleName = "";
	   this.payCommision = "";
	   this.effectiveFrom = "";
	   this.amountFrom = "";
	   this.amountTo = "";
	   this.increment = "";
	   this.basicPay = "";
	   this.deptId = null ;
	   this.effectiveTo = "";
	   this.remarks = "" ;
	   this.numDays="";
	   this.workingDays= "" ;
	   this.incSlabFrmAmt=null;
  	   this.incSlabToAmt=null;
  	   this.incSlabAmt=null;
  	   this.dedTaxlist=null;
  	   this.dedTaxamountlist=null;
  	   this.dedOtherlist=null;
  	   this.dedOtheramountlist=null;	
  	   this.deductionsAdvList=null;
  	   this.otherDedList=null;
  	   this.userName=null;
	 }
	/**
	 * @return the accountCodeId
	 */
	public String[] getAccountCodeId() {
		return accountCodeId;
	}
	/**
	 * @param accountCodeId the accountCodeId to set
	 */
	public void setAccountCodeId(String[] accountCodeId) {
		this.accountCodeId = accountCodeId;
	}
	/**
	 * @return the calculationType
	 */
	public String[] getCalculationType() {
		return calculationType;
	}
	/**
	 * @param calculationType the calculationType to set
	 */
	public void setCalculationType(String[] calculationType) {
		this.calculationType = calculationType;
	}
	/**
	 * @return the employeeCodeId
	 */
	public String getEmployeeCodeId() {
		return employeeCodeId;
	}
	/**
	 * @param employeeCodeId the employeeCodeId to set
	 */
	public void setEmployeeCodeId(String employeeCodeId) {
		this.employeeCodeId = employeeCodeId;
	}
	/**
	 * @return the grossPay
	 */
	public String getGrossPay() {
		return grossPay;
	}
	/**
	 * @param grossPay the grossPay to set
	 */
	public void setGrossPay(String grossPay) {
		this.grossPay = grossPay;
	}
	
	/**
	 * @return the netPay
	 */
	public String getNetPay() {
		return netPay;
	}
	/**
	 * @param netPay the netPay to set
	 */
	public void setNetPay(String netPay) {
		this.netPay = netPay;
	}
	/**
	 * @return the otherDeductionsAmount
	 */
	public String[] getOtherDeductionsAmount() {
		return otherDeductionsAmount;
	}
	/**
	 * @param otherDeductionsAmount the otherDeductionsAmount to set
	 */
	public void setOtherDeductionsAmount(String[] otherDeductionsAmount) {
		this.otherDeductionsAmount = otherDeductionsAmount;
	}
	/**
	 * @return the payHead
	 */
	public String[] getPayHead() {
		return payHead;
	}
	/**
	 * @param payHead the payHead to set
	 */
	public void setPayHead(String[] payHead) {
		this.payHead = payHead;
	}
	/**
	 * @return the payHeadAmount
	 */
	public String[] getPayHeadAmount() {
		return payHeadAmount;
	}
	/**
	 * @param payHeadAmount the payHeadAmount to set
	 */
	public void setPayHeadAmount(String[] payHeadAmount) {
		this.payHeadAmount = payHeadAmount;
	}
	/**
	 * @return the pct
	 */
	public String[] getPct() {
		return pct;
	}
	/**
	 * @param pct the pct to set
	 */
	public void setPct(String[] pct) {
		this.pct = pct;
	}
	/**
	 * @return the pctBasis
	 */
	public String[] getPctBasis() {
		return pctBasis;
	}
	/**
	 * @param pctBasis the pctBasis to set
	 */
	public void setPctBasis(String[] pctBasis) {
		this.pctBasis = pctBasis;
	}
	/**
	 * @return the salaryAdvances
	 */
	public String[] getSalaryAdvances() {
		return salaryAdvances;
	}
	/**
	 * @param salaryAdvances the salaryAdvances to set
	 */
	public void setSalaryAdvances(String[] salaryAdvances) {
		this.salaryAdvances = salaryAdvances;
	}
	/**
	 * @return the salaryAdvancesAmount
	 */
	public String[] getSalaryAdvancesAmount() {
		return salaryAdvancesAmount;
	}
	/**
	 * @param salaryAdvancesAmount the salaryAdvancesAmount to set
	 */
	public void setSalaryAdvancesAmount(String[] salaryAdvancesAmount) {
		this.salaryAdvancesAmount = salaryAdvancesAmount;
	}
	
	public String[] getTaxPayHeadId() {
		return taxPayHeadId;
	}
	public void setTaxPayHeadId(String[] taxPayHeadId) {
		this.taxPayHeadId = taxPayHeadId;
	}
	public String[] getTaxTypeAmount() {
		return taxTypeAmount;
	}
	public void setTaxTypeAmount(String[] taxTypeAmount) {
		this.taxTypeAmount = taxTypeAmount;
	}
	/**
	 * @return the month
	 */
	public String getMonth() {
		return month;
	}
	/**
	 * @param month the month to set
	 */
	public void setMonth(String month) {
		this.month = month;
	}
	/**
	 * @return the year
	 */
	public String getYear() {
		return year;
	}
	/**
	 * @param year the year to set
	 */
	public void setYear(String year) {
		this.year = year;
	}
	public String[] getYearToDateAdv() {
		return yearToDateAdv;
	}
	public void setYearToDateAdv(String[] yearToDateAdv) {
		this.yearToDateAdv = yearToDateAdv;
	}
	public String[] getYearToDateDed() {
		return yearToDateDed;
	}
	public void setYearToDateDed(String[] yearToDateDed) {
		this.yearToDateDed = yearToDateDed;
	}
	public String[] getYearToDateHead() {
		return yearToDateHead;
	}
	public void setYearToDateHead(String[] yearToDateHead) {
		this.yearToDateHead = yearToDateHead;
	}
	public String[] getYearToDateTax() {
		return yearToDateTax;
	}
	public void setYearToDateTax(String[] yearToDateTax) {
		this.yearToDateTax = yearToDateTax;
	}
	/**
	 * @return the amountFrom
	 */
	public String getAmountFrom() {
		return amountFrom;
	}
	/**
	 * @param amountFrom the amountFrom to set
	 */
	public void setAmountFrom(String amountFrom) {
		this.amountFrom = amountFrom;
	}
	/**
	 * @return the amountTo
	 */
	public String getAmountTo() {
		return amountTo;
	}
	/**
	 * @param amountTo the amountTo to set
	 */
	public void setAmountTo(String amountTo) {
		this.amountTo = amountTo;
	}
	/**
	 * @return the effectiveFrom
	 */
	public String getEffectiveFrom() {
		return effectiveFrom;
	}
	/**
	 * @param effectiveFrom the effectiveFrom to set
	 */
	public void setEffectiveFrom(String effectiveFrom) {
		this.effectiveFrom = effectiveFrom;
	}
	/**
	 * @return the payCommision
	 */
	public String getPayCommision() {
		return payCommision;
	}
	/**
	 * @param payCommision the payCommision to set
	 */
	public void setPayCommision(String payCommision) {
		this.payCommision = payCommision;
	}
	/**
	 * @return the payScaleName
	 */
	public String getPayScaleName() {
		return payScaleName;
	}
	/**
	 * @param payScaleName the payScaleName to set
	 */
	public void setPayScaleName(String payScaleName) {
		this.payScaleName = payScaleName;
	}
	/**
	 * @return the increment
	 */
	public String getIncrement() {
		return increment;
	}
	/**
	 * @param increment the increment to set
	 */
	public void setIncrement(String increment) {
		this.increment = increment;
	}
	public String getBasicPay() {
		return basicPay;
	}
	public void setBasicPay(String basicPay) {
		this.basicPay = basicPay;
	}
	public String getDeptId() {
		return deptId;
	}
	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}
	public String getEffectiveTo() {
		return effectiveTo;
	}
	public void setEffectiveTo(String effectiveTo) {
		this.effectiveTo = effectiveTo;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public String getNumDays() {
		return numDays;
	}	
	public String[] getDelPayhead() {
		return delPayhead;
	}
	public void setNumDays(String numDays) {
		this.numDays = numDays;
	}	
	public void setDelPayhead(String[] delPayhead) {
		this.delPayhead = delPayhead;
	}
	public String getWorkingDays() {
		return workingDays;
	}
	public String[] getEarningId() {
		return earningId;
	}
	public void setWorkingDays(String workingDays) {
		this.workingDays = workingDays;
	}	
	public void setEarningId(String[] earningId) {
		this.earningId = earningId;
	}
	public String getPayType() {
		return payType;
	}	
	public String[] getAdvDedId() {
		return advDedId;
	}
	public void setPayType(String payType) {
		this.payType = payType;
	}
	public void setAdvDedId(String[] advDedId) {
		this.advDedId = advDedId;
	}
	public String[] getOtherDedId() {
		return otherDedId;
	}
	public void setOtherDedId(String[] otherDedId) {
		this.otherDedId = otherDedId;
	}
	public String getPayslipId() {
		return payslipId;
	}
	public void setPayslipId(String payslipId) {
		this.payslipId = payslipId;
	}
	public String getModifyRemarks() {
		return modifyRemarks;
	}
	public void setModifyRemarks(String modifyRemarks) {
		this.modifyRemarks = modifyRemarks;
	}
	public String getSupplComment() {
		return supplComment;
	}
	public void setSupplComment(String supplComment) {
		this.supplComment = supplComment;
	}
	public String getFrwdType() {
		return frwdType;
	}
	public void setFrwdType(String frwdType) {
		this.frwdType = frwdType;
	}	
	public String getLeaveApplication() {
		return leaveApplication;
	}
	public void setLeaveApplication(String leaveApplication) {
		this.leaveApplication = leaveApplication;
	}
	public String[] getIncSlabAmt() {
		return incSlabAmt;
	}
	public void setIncSlabAmt(String[] incSlabAmt) {
		this.incSlabAmt = incSlabAmt;
	}
	public String[] getIncSlabFrmAmt() {
		return incSlabFrmAmt;
	}
	public void setIncSlabFrmAmt(String[] incSlabFrmAmt) {
		this.incSlabFrmAmt = incSlabFrmAmt;
	}
	public String[] getIncSlabToAmt() {
		return incSlabToAmt;
	}
	public void setIncSlabToAmt(String[] incSlabToAmt) {
		this.incSlabToAmt = incSlabToAmt;
	}

	public ArrayList getDedOtherlist() {
		return dedOtherlist;
	}
	public void setDedOtherlist(ArrayList dedOtherlist) {
		this.dedOtherlist = dedOtherlist;
	}

	public ArrayList getDedTaxlist() {
		return dedTaxlist;
	}
	public void setDedTaxlist(ArrayList dedTaxlist) {
		this.dedTaxlist = dedTaxlist;
	}
	public ArrayList getDeductionsAdvList() {
		return deductionsAdvList;
	}
	public void setDeductionsAdvList(ArrayList deductionsAdvList) {
		this.deductionsAdvList = deductionsAdvList;
	}
	public ArrayList getOtherDedList() {
		return otherDedList;
	}
	public void setOtherDedList(ArrayList otherDedList) {
		this.otherDedList = otherDedList;
	}
	public String[] getDedOtheramountlist() {
		return dedOtheramountlist;
	}
	public void setDedOtheramountlist(String[] dedOtheramountlist) {
		this.dedOtheramountlist = dedOtheramountlist;
	}
	public String[] getDedTaxamountlist() {
		return dedTaxamountlist;
	}
	public void setDedTaxamountlist(String[] dedTaxamountlist) {
		this.dedTaxamountlist = dedTaxamountlist;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getFunctionaryId() {
		return functionaryId;
	}
	public void setFunctionaryId(String functionaryId) {
		this.functionaryId = functionaryId;
	}
	public String[] getTaxTypeName() {
		return taxTypeName;
	}
	public void setTaxTypeName(String[] taxTypeName) {
		this.taxTypeName = taxTypeName;
	}
	public String[] getYearToDateOther() {
		return yearToDateOther;
	}
	public void setYearToDateOther(String[] yearToDateOther) {
		this.yearToDateOther = yearToDateOther;
	}
	public String[] getOtherDedTxDedId() {
		return otherDedTxDedId;
	}
	public void setOtherDedTxDedId(String[] otherDedTxDedId) {
		this.otherDedTxDedId = otherDedTxDedId;
	}
	public String[] getDedOthrName() {
		return dedOthrName;
	}
	public void setDedOthrName(String[] dedOthrName) {
		this.dedOthrName = dedOthrName;
	}
	public String[] getDedOthrAmount() {
		return dedOthrAmount;
	}
	public void setDedOthrAmount(String[] dedOthrAmount) {
		this.dedOthrAmount = dedOthrAmount;
	}
	public String[] getDedRefNo() {
		return dedRefNo;
	}
	public void setDedRefNo(String[] dedRefNo) {
		this.dedRefNo = dedRefNo;
	}
	public String[] getDedOthrTxId() {
		return dedOthrTxId;
	}
	public void setDedOthrTxId(String[] dedOthrTxId) {
		this.dedOthrTxId = dedOthrTxId;
	}
	public String[] getAdvanceSchedule() {
		return advanceSchedule;
	}
	public void setAdvanceSchedule(String[] advanceSchedule) {
		this.advanceSchedule = advanceSchedule;
	}
	public String getRuleScript() {
		return ruleScript;
	}
	public void setRuleScript(String ruleScript) {
		this.ruleScript = ruleScript;
	}
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	public String getFunctionId() {
		return functionId;
	}
	public void setFunctionId(String functionId) {
		this.functionId = functionId;
	}
}