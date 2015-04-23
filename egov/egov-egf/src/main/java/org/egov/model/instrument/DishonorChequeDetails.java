package org.egov.model.instrument;

import java.math.BigDecimal;

import org.egov.commons.Accountdetailkey;
import org.egov.commons.Accountdetailtype;
import org.egov.commons.CChartOfAccounts;
import org.egov.commons.CFunction;
import org.egov.infstr.models.BaseModel;

public class DishonorChequeDetails extends BaseModel {
	private DishonorCheque header;
	private CChartOfAccounts glcodeId;
	private BigDecimal debitAmt;
	private BigDecimal creditAmount;
	private Accountdetailkey detailKey;
	private Accountdetailtype detailType;
	private Integer functionId;  
	private CFunction function;
	
	public Integer getFunctionId() {
		return functionId;
	}
	public void setFunctionId(Integer functionId) {
		this.functionId = functionId;
	}
	public DishonorCheque getHeader() {
		return header;
	}
	public void setHeader(DishonorCheque header) {
		this.header = header;
	}
	public CChartOfAccounts getGlcodeId() {
		return glcodeId;
	}
	public void setGlcodeId(CChartOfAccounts glcodeId) {
		this.glcodeId = glcodeId;
	}
	public BigDecimal getDebitAmt() {
		return debitAmt;
	}
	public void setDebitAmt(BigDecimal debitAmt) {
		this.debitAmt = debitAmt;
	}
	public BigDecimal getCreditAmount() {
		return creditAmount;
	}
	public void setCreditAmount(BigDecimal creditAmount) {
		this.creditAmount = creditAmount;
	}
	public Accountdetailkey getDetailKey() {
		return detailKey;
	}
	public void setDetailKey(Accountdetailkey detailKey) {
		this.detailKey = detailKey;
	}
	public Accountdetailtype getDetailType() {
		return detailType;
	}
	public void setDetailType(Accountdetailtype detailType) {
		this.detailType = detailType;
	}
	public CFunction getFunction() {
		return function;
	}
	public void setFunction(CFunction function) {
		this.function = function;
	}


}
