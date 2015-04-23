/**
 * 
 */
package org.egov.model.voucher;

import java.math.BigDecimal;

import org.egov.commons.Accountdetailtype;
import org.egov.commons.CChartOfAccounts;
import org.egov.infstr.utils.StringUtils;

/**
 * @author msahoo
 *
 */
public class VoucherDetails {
	
	
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
	private String detailName;
	private BigDecimal amount =BigDecimal.ZERO;
	private String subledgerCode;
	private String isSubledger;
	public String getIsSubledger() {
		return isSubledger;
	}
	public void setIsSubledger(String isSubledger) {
		this.isSubledger = isSubledger;
	}
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
	public String getDetailTypeName() {
		return detailTypeName;
	}
	public void setDetailTypeName(String detailTypeName) {
		this.detailTypeName = detailTypeName;
	}
	public String getSubledgerCode() {
		return subledgerCode;
	}
	public void setSubledgerCode(String subledgerCode) {
		this.subledgerCode = subledgerCode;
	}
	public String getDetailName() {
		return detailName;
	}
	public void setDetailName(String detailName) {
		this.detailName = detailName;
	}
	
	public String getDetailKeyEscSpecChar() {
		return StringUtils.escapeJavaScript(detailKey);
	}
	
	public String getDetailNameEscSpecChar() {
		return StringUtils.escapeJavaScript(detailName);
	}
	
	
	
}
