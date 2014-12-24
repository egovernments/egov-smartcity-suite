package org.egov.model.voucher;

import java.math.BigDecimal;

import org.egov.commons.Accountdetailtype;
import org.egov.commons.CChartOfAccounts;

public class PreApprovedVoucher {
	
	private Long functionIdDetail;
	private String functionDetail;
	private Long glcodeIdDetail;
	private String glcodeDetail;
	private String accounthead;
	private BigDecimal debitAmountDetail=BigDecimal.ZERO;
	private BigDecimal creditAmountDetail=BigDecimal.ZERO;
	
	private CChartOfAccounts glcode;
	private Accountdetailtype detailType;
	private Integer detailKeyId;
	private String detailKey;
	private String detailCode;
	private BigDecimal debitAmount = BigDecimal.ZERO;
	private BigDecimal creditAmount= BigDecimal.ZERO;
	private BigDecimal amount = BigDecimal.ZERO;
	public Long getFunctionIdDetail() {
		return functionIdDetail;
	}
	public void setFunctionIdDetail(Long functionIdDetail) {
		this.functionIdDetail = functionIdDetail;
	}
	public String getFunctionDetail() {
		return functionDetail;
	}
	public void setFunctionDetail(String functionDetail) {
		this.functionDetail = functionDetail;
	}
	public Long getGlcodeIdDetail() {
		return glcodeIdDetail;
	}
	public void setGlcodeIdDetail(Long glcodeIdDetail) {
		this.glcodeIdDetail = glcodeIdDetail;
	}
	public String getGlcodeDetail() {
		return glcodeDetail;
	}
	public void setGlcodeDetail(String glcodeDetail) {
		this.glcodeDetail = glcodeDetail;
	}
	public BigDecimal getDebitAmountDetail() {
		return debitAmountDetail;
	}
	public void setDebitAmountDetail(BigDecimal debitAmountDetail) {
		this.debitAmountDetail = debitAmountDetail;
	}
	public BigDecimal getCreditAmountDetail() {
		return creditAmountDetail;
	}
	public void setCreditAmountDetail(BigDecimal creditAmountDetail) {
		this.creditAmountDetail = creditAmountDetail;
	}
	public CChartOfAccounts getGlcode() {
		return glcode;
	}
	public void setGlcode(CChartOfAccounts glcode) {
		this.glcode = glcode;
	}
	public Accountdetailtype getDetailType() {
		return detailType;
	}
	public void setDetailType(Accountdetailtype detailType) {
		this.detailType = detailType;
	}
	
	public Integer getDetailKeyId() {
		return detailKeyId;
	}
	public void setDetailKeyId(Integer detailKeyId) {
		this.detailKeyId = detailKeyId;
	}
	public String getDetailKey() {
		return detailKey;
	}
	public void setDetailKey(String detailKey) {
		this.detailKey = detailKey;
	}
	public String getDetailCode() {
		return detailCode;
	}
	public void setDetailCode(String detailCode) {
		this.detailCode = detailCode;
	}
	public BigDecimal getDebitAmount() {
		return debitAmount;
	}
	public void setDebitAmount(BigDecimal debitAmount) {
		this.debitAmount = debitAmount;
	}
	public BigDecimal getCreditAmount() {
		return creditAmount;
	}
	public void setCreditAmount(BigDecimal creditAmount) {
		this.creditAmount = creditAmount;
	}
	public String getAccounthead() {
		return accounthead;
	}
	public void setAccounthead(String accounthead) {
		this.accounthead = accounthead;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

}
