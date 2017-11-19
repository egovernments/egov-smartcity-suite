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
package org.egov.collection.integration.models;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "VerifyOutput")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ResponseAtomReconcilation")
public class ResponseAtomReconcilation {
    @XmlAttribute(name = "MerchantID")
    protected String merchantID;
    @XmlAttribute(name = "MerchantTxnID")
    protected String merchantTxnID;
    @XmlAttribute(name = "AMT")
    protected String amt;
    @XmlAttribute(name = "VERIFIED")
    protected String verified;
    @XmlAttribute(name = "BID")
    protected String bid;
    @XmlAttribute
    protected String bankname;
    @XmlAttribute
    protected String atomtxnId;
    @XmlAttribute
    protected String discriminator;
    @XmlAttribute(name = "CardNumber")
    protected String cardNumber;
    @XmlAttribute(name = "surcharge")
    protected String surcharge;
    @XmlAttribute(name = "TxnDate")
    protected String txnDate;
    @XmlAttribute(name = "UDF9")
    protected String udf9;
    /**
     * @return the bankname
     */
    public String getBankname() {
            return bankname;
    }

    /**
     * @param bankname
     *            the bankname to set
     */
    public void setBankname(String bankname) {
            this.bankname = bankname;
    }

    /**
     * @return the atomtxnId
     */
    public String getAtomtxnId() {
            return atomtxnId;
    }

    /**
     * @param atomtxnId
     *            the atomtxnId to set
     */
    public void setAtomtxnId(String atomtxnId) {
            this.atomtxnId = atomtxnId;
    }

    /**
     * @return the surcharge
     */
    public String getSurcharge() {
            return surcharge;
    }

    /**
     * @param surcharge
     *            the surcharge to set
     */
    public void setSurcharge(String surcharge) {
            this.surcharge = surcharge;
    }

    /**
     * @return the txnDate
     */
    public String getTxnDate() {
            return txnDate;
    }

    /**
     * @param txnDate
     *            the txnDate to set
     */
    public void setTxnDate(String txnDate) {
            this.txnDate = txnDate;
    }

    /**
     * @return the amt
     */
    public String getAmt() {
            return amt;
    }

    /**
     * @param amt
     *            the amt to set
     */
    public void setAmt(String amt) {
            this.amt = amt;
    }

    /**
     * @return the merchantID
     */
    public String getMerchantID() {
            return merchantID;
    }

    /**
     * @param merchantID
     *            the merchantID to set
     */
    public void setMerchantID(String merchantID) {
            this.merchantID = merchantID;
    }

    /**
     * @return the merchantTxnID
     */
    public String getMerchantTxnID() {
            return merchantTxnID;
    }

    /**
     * @param merchantTxnID
     *            the merchantTxnID to set
     */
    public void setMerchantTxnID(String merchantTxnID) {
            this.merchantTxnID = merchantTxnID;
    }

    /**
     * @return the verified
     */
    public String getVerified() {
            return verified;
    }

    /**
     * @param verified
     *            the verified to set
     */
    public void setVerified(String verified) {
            this.verified = verified;
    }

    /**
     * @return the bid
     */
    public String getBid() {
            return bid;
    }

    /**
     * @param bid
     *            the bid to set
     */
    public void setBid(String bid) {
            this.bid = bid;
    }

    /**
     * @return the cardNumber
     */
    public String getCardNumber() {
            return cardNumber;
    }

    /**
     * @param cardNumber
     *            the cardNumber to set
     */
    public void setCardNumber(String cardNumber) {
            this.cardNumber = cardNumber;
    }

    public String getDiscriminator() {
        return discriminator;
    }

    public void setDiscriminator(String discriminator) {
        this.discriminator = discriminator;
    }

    public String getUdf9() {
        return udf9;
    }

    public void setUdf9(String udf9) {
        this.udf9 = udf9;
    }


}
