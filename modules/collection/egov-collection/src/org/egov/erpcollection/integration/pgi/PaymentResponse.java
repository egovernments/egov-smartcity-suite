package org.egov.erpcollection.integration.pgi;

import java.math.BigDecimal;
import java.util.Date;

/**
 * The payment response interface. This is used by the system to retrieve
 * details of payment response from the payment gateway.
 */
public interface PaymentResponse {

	public String getMerchantId();

	public void setMerchantId(String merchantId);

	public String getCustomerId();

	public void setCustomerId(String customerId);

	public String getTxnReferenceNo();

	public void setTxnReferenceNo(String txnReferenceNo);

	public String getBankReferenceNo();

	public void setBankReferenceNo(String bankReferenceNo);

	public BigDecimal getTxnAmount();

	public void setTxnAmount(BigDecimal txnAmount);

	public Integer getBankId();

	public void setBankId(Integer bankId);

	public Integer getBankMerchantId();

	public void setBankMerchantId(Integer bankMerchantId);

	public String getTxnType();

	public void setTxnType(String txnType);

	public String getCurrencyName();

	public void setCurrencyName(String currencyName);

	public String getItemCode();

	public void setItemCode(String itemCode);

	public String getSecurityType();

	public void setSecurityType(String securityType);

	public Integer getSecurityId();

	public void setSecurityId(Integer securityId);

	public String getSecurityPassword();

	public void setSecurityPassword(String securityPassword);

	public Date getTxnDate();

	public void setTxnDate(Date txnDate);

	public String getAuthStatus();

	public void setAuthStatus(String authStatus);

	public String getSettlementType();

	public void setSettlementType(String settlementType);

	public String getReceiptId();

	public void setReceiptId(String receiptId);

	public String getAdditionalInfo2();

	public void setAdditionalInfo2(String additionalInfo2);

	public String getAdditionalInfo3();

	public void setAdditionalInfo3(String additionalInfo3);

	/*public String getAdditionalInfo4();

	public void setAdditionalInfo4(String additionalInfo4);

	public String getAdditionalInfo5();

	public void setAdditionalInfo5(String additionalInfo5);
*/
	public String getPaytGatewayServiceCode();

	public void setPaytGatewayServiceCode(String paytGatewayServiceCode);

	public String getBillingServiceCode();

	public void setBillingServiceCode(String billingServiceCode);
	
	public String getAdditionalInfo6();

	public void setAdditionalInfo6(String additionalInfo6);

	public String getAdditionalInfo7();

	public void setAdditionalInfo7(String additionalInfo7);

	public String getErrorStatus();

	public void setErrorStatus(String errorStatus);

	public String getErrorDescription();

	public void setErrorDescription(String errorDescription);

	public String getChecksum();

	public void setChecksum(String checksum);
	
	
}
