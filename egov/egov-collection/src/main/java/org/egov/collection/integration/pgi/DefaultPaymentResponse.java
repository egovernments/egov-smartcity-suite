/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2017  eGovernments Foundation
 *
 *     The updated version of eGov suite of products as by eGovernments Foundation
 *     is available at http://www.egovernments.org
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program. If not, see http://www.gnu.org/licenses/ or
 *     http://www.gnu.org/licenses/gpl.html .
 *
 *     In addition to the terms of the GPL license to be adhered to in using this
 *     program, the following additional terms are to be complied with:
 *
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
 *            Further, all user interfaces, including but not limited to citizen facing interfaces,
 *            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
 *            derived works should carry eGovernments Foundation logo on the top right corner.
 *
 *            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
 *            For any further queries on attribution, including queries on brand guidelines,
 *            please contact contact@egovernments.org
 *
 *         2) Any misrepresentation of the origin of the material is prohibited. It
 *            is required that all modified versions of this material be marked in
 *            reasonable ways as different from the original version.
 *
 *         3) This license does not grant any rights to any user of the program
 *            with regards to rights under trademark law for use of the trade names
 *            or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 *
 */
package org.egov.collection.integration.pgi;

import java.math.BigDecimal;
import java.util.Date;

public class DefaultPaymentResponse implements PaymentResponse {

    private String merchantId;
    private String customerId;
    private String txnReferenceNo;
    private String bankReferenceNo;
    private BigDecimal txnAmount;
    private Integer bankId;
    private Integer bankMerchantId;
    private String txnType;
    private String currencyName;
    private String itemCode;
    private String securityType;
    private Integer securityId;
    private String securityPassword;
    private Date txnDate;
    private String authStatus;
    private String settlementType;
    private String receiptId;
    private String additionalInfo2;
    private String additionalInfo3;
    // private String additionalInfo4;
    private String paytGatewayServiceCode;
    // private String additionalInfo5;
    private String billingServiceCode;
    private String additionalInfo6;
    private String additionalInfo7;
    private String errorStatus;
    private String errorDescription;
    private String checksum;

    public DefaultPaymentResponse() {

    }

    @Override
    public String getMerchantId() {
        return merchantId;
    }

    @Override
    public void setMerchantId(final String merchantId) {
        this.merchantId = merchantId;
    }

    @Override
    public String getCustomerId() {
        return customerId;
    }

    @Override
    public void setCustomerId(final String customerId) {
        this.customerId = customerId;
    }

    @Override
    public String getTxnReferenceNo() {
        return txnReferenceNo;
    }

    @Override
    public void setTxnReferenceNo(final String txnReferenceNo) {
        this.txnReferenceNo = txnReferenceNo;
    }

    @Override
    public String getBankReferenceNo() {
        return bankReferenceNo;
    }

    @Override
    public void setBankReferenceNo(final String bankReferenceNo) {
        this.bankReferenceNo = bankReferenceNo;
    }

    @Override
    public BigDecimal getTxnAmount() {
        return txnAmount;
    }

    @Override
    public void setTxnAmount(final BigDecimal txnAmount) {
        this.txnAmount = txnAmount;
    }

    @Override
    public Integer getBankId() {
        return bankId;
    }

    @Override
    public void setBankId(final Integer bankId) {
        this.bankId = bankId;
    }

    @Override
    public Integer getBankMerchantId() {
        return bankMerchantId;
    }

    @Override
    public void setBankMerchantId(final Integer bankMerchantId) {
        this.bankMerchantId = bankMerchantId;
    }

    @Override
    public String getTxnType() {
        return txnType;
    }

    @Override
    public void setTxnType(final String txnType) {
        this.txnType = txnType;
    }

    @Override
    public String getCurrencyName() {
        return currencyName;
    }

    @Override
    public void setCurrencyName(final String currencyName) {
        this.currencyName = currencyName;
    }

    @Override
    public String getItemCode() {
        return itemCode;
    }

    @Override
    public void setItemCode(final String itemCode) {
        this.itemCode = itemCode;
    }

    @Override
    public String getSecurityType() {
        return securityType;
    }

    @Override
    public void setSecurityType(final String securityType) {
        this.securityType = securityType;
    }

    @Override
    public Integer getSecurityId() {
        return securityId;
    }

    @Override
    public void setSecurityId(final Integer securityId) {
        this.securityId = securityId;
    }

    @Override
    public String getSecurityPassword() {
        return securityPassword;
    }

    @Override
    public void setSecurityPassword(final String securityPassword) {
        this.securityPassword = securityPassword;
    }

    @Override
    public Date getTxnDate() {
        return txnDate;
    }

    @Override
    public void setTxnDate(final Date txnDate) {
        this.txnDate = txnDate;
    }

    @Override
    public String getAuthStatus() {
        return authStatus;
    }

    @Override
    public void setAuthStatus(final String authStatus) {
        this.authStatus = authStatus;
    }

    @Override
    public String getSettlementType() {
        return settlementType;
    }

    @Override
    public void setSettlementType(final String settlementType) {
        this.settlementType = settlementType;
    }

    @Override
    public String getReceiptId() {
        return receiptId;
    }

    @Override
    public void setReceiptId(final String receiptId) {
        this.receiptId = receiptId;
    }

    @Override
    public String getAdditionalInfo2() {
        return additionalInfo2;
    }

    @Override
    public void setAdditionalInfo2(final String additionalInfo2) {
        this.additionalInfo2 = additionalInfo2;
    }

    @Override
    public String getAdditionalInfo3() {
        return additionalInfo3;
    }

    @Override
    public void setAdditionalInfo3(final String additionalInfo3) {
        this.additionalInfo3 = additionalInfo3;
    }

    /*
     * public String getAdditionalInfo4() { return additionalInfo4; } public
     * void setAdditionalInfo4(String additionalInfo4) { this.additionalInfo4 =
     * additionalInfo4; } public String getAdditionalInfo5() { return
     * additionalInfo5; } public void setAdditionalInfo5(String additionalInfo5)
     * { this.additionalInfo5 = additionalInfo5; }
     */

    @Override
    public String getAdditionalInfo6() {
        return additionalInfo6;
    }

    @Override
    public void setAdditionalInfo6(final String additionalInfo6) {
        this.additionalInfo6 = additionalInfo6;
    }

    @Override
    public String getAdditionalInfo7() {
        return additionalInfo7;
    }

    @Override
    public void setAdditionalInfo7(final String additionalInfo7) {
        this.additionalInfo7 = additionalInfo7;
    }

    @Override
    public String getErrorStatus() {
        return errorStatus;
    }

    @Override
    public void setErrorStatus(final String errorStatus) {
        this.errorStatus = errorStatus;
    }

    @Override
    public String getErrorDescription() {
        return errorDescription;
    }

    @Override
    public void setErrorDescription(final String errorDescription) {
        this.errorDescription = errorDescription;
    }

    @Override
    public String getChecksum() {
        return checksum;
    }

    @Override
    public void setChecksum(final String checksum) {
        this.checksum = checksum;
    }

    @Override
    public String getPaytGatewayServiceCode() {
        return paytGatewayServiceCode;
    }

    @Override
    public void setPaytGatewayServiceCode(final String paytGatewayServiceCode) {
        this.paytGatewayServiceCode = paytGatewayServiceCode;
    }

    @Override
    public String getBillingServiceCode() {
        return billingServiceCode;
    }

    @Override
    public void setBillingServiceCode(final String billingServiceCode) {
        this.billingServiceCode = billingServiceCode;
    }

}
