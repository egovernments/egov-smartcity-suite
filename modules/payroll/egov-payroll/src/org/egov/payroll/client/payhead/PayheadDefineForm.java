package org.egov.payroll.client.payhead;


import org.apache.struts.action.ActionForm;


public class PayheadDefineForm extends ActionForm{
	
	String name;
	String description;
	String type;
	String calType;
	String categoryE;
	String categoryD;
	String glcode;
	String isTaxable;
	String pctBasis;
	String action;
	String payheadId;
	String orderNo;
	String recoveryAC;
	String modifyRemarks;
	String localLangDesc;
	String slabAC;
	String isInterest;
	String interestGlcode;
	String IsAttendanceBased;
	String IsRecomputed;
	String IsRecurring;
	String captureRate;
	//String isValidateByRule;
	String ruleScript;
	
	
	public String getRuleScript() {
		return ruleScript;
	}
	public void setRuleScript(String ruleScript) {
		this.ruleScript = ruleScript;
	}	
	public String getIsAttendanceBased() {
		return IsAttendanceBased;
	}
	public void setIsAttendanceBased(String isAttendanceBased) {
		IsAttendanceBased = isAttendanceBased;
	}
	public String getIsRecomputed() {
		return IsRecomputed;
	}
	public void setIsRecomputed(String isRecomputed) {
		IsRecomputed = isRecomputed;
	}
	public String getIsRecurring() {
		return IsRecurring;
	}
	public void setIsRecurring(String isRecurring) {
		IsRecurring = isRecurring;
	}
	public String getPctBasis() {
		return pctBasis;
	}
	public void setPctBasis(String pctBasis) {
		this.pctBasis = pctBasis;
	}
	public String getIsTaxable() {
		return isTaxable;
	}
	public void setIsTaxable(String isTaxable) {
		this.isTaxable = isTaxable;
	}
	public String getCalType() {
		return calType;
	}
	public void setCalType(String calType) {
		this.calType = calType;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	public String getGlcode() {
		return glcode;
	}
	public void setGlcode(String glcode) {		
		this.glcode = glcode;
	}
	public String getCategoryD() {
		return categoryD;
	}
	public void setCategoryD(String categoryD) {
		this.categoryD = categoryD;
	}
	public String getCategoryE() {
		return categoryE;
	}
	public void setCategoryE(String categoryE) {
		this.categoryE = categoryE;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public String getPayheadId() {
		return payheadId;
	}
	public void setPayheadId(String payheadId) {
		this.payheadId = payheadId;
	}
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public String getRecoveryAC() {
		return recoveryAC;
	}
	public void setRecoveryAC(String recoveryAC) {
		this.recoveryAC = recoveryAC;
	}
	public String getModifyRemarks() {
		return modifyRemarks;
	}
	public void setModifyRemarks(String modifyRemarks) {
		this.modifyRemarks = modifyRemarks;
	}
	public String getLocalLangDesc() {
		return localLangDesc;
	}
	public void setLocalLangDesc(String localLangDesc) {
		this.localLangDesc = localLangDesc;
	}
	public String getSlabAC() {
		return slabAC;
	}
	public void setSlabAC(String slabAC) {
		this.slabAC = slabAC;
	}
	public String getIsInterest() {
		return isInterest;
	}
	public void setIsInterest(String isInterest) {
		this.isInterest = isInterest;
	}
	public String getInterestGlcode() {
		return interestGlcode;
	}
	public void setInterestGlcode(String interestGlcode) {
		this.interestGlcode = interestGlcode;
	}
	public String getCaptureRate() {
		return captureRate;
	}
	
	public void setCaptureRate(String captureRate) {
		this.captureRate = captureRate;
	}
	
	

	
}
