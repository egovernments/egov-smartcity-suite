package org.egov.erpcollection.models;

import java.math.BigDecimal;

import org.egov.commons.Accountdetailtype;
import org.egov.commons.CChartOfAccounts;

/**
 * Used By Miscellaneous Receipts and Challan to create account details,
 * Rebate details, Subledger Details tables  
 *
 */
public class ReceiptDetailInfo {
	private Long functionIdDetail;
	private String functionDetail;
	private Long glcodeIdDetail;
	private String glcodeDetail;
	private String accounthead;
	private BigDecimal debitAmountDetail= BigDecimal.ZERO;
	private BigDecimal creditAmountDetail= BigDecimal.ZERO;
	private CChartOfAccounts glcode;
	private Accountdetailtype detailType;
	private String detailTypeName;
	private Integer detailKeyId;
	private String detailKey;
	private String detailCode;
	private BigDecimal amount =BigDecimal.ZERO;
	private String subledgerCode;
	private Long financialYearId;
	private String financialYearRange;
	
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
	public String getAccounthead() {
		return accounthead;
	}
	public void setAccounthead(String accounthead) {
		this.accounthead = accounthead;
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
	public String getDetailTypeName() {
		return detailTypeName;
	}
	public void setDetailTypeName(String detailTypeName) {
		this.detailTypeName = detailTypeName;
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
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public String getSubledgerCode() {
		return subledgerCode;
	}
	public void setSubledgerCode(String subledgerCode) {
		this.subledgerCode = subledgerCode;
	}
	public Long getFinancialYearId() {
		return financialYearId;
	}
	public void setFinancialYearId(Long financialYearId) {
		this.financialYearId = financialYearId;
	}
	public String getFinancialYearRange() {
		return financialYearRange;
	}
	public void setFinancialYearRange(String financialYearRange) {
		this.financialYearRange = financialYearRange;
	}
}
