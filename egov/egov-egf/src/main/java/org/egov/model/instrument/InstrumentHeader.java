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
package org.egov.model.instrument;

import org.egov.commons.Accountdetailtype;
import org.egov.commons.Bank;
import org.egov.commons.Bankaccount;
import org.egov.commons.CFinancialYear;
import org.egov.commons.EgwStatus;
import org.egov.infstr.models.BaseModel;
import org.egov.infstr.models.ECSType;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * EgfInstrumenHeader entity.
 *
 * @author Mani
 */

public class InstrumentHeader extends BaseModel {

    private static final long serialVersionUID = 3412036578900689029L;

    private Accountdetailtype detailTypeId;
    private Bankaccount bankAccountId;
    private EgwStatus statusId;
    private Bank bankId;
    private String instrumentNumber;
    private Date instrumentDate;
    private BigDecimal instrumentAmount;
    private String payTo;
    private String isPayCheque;
    private InstrumentType instrumentType;
    private Long detailKeyId;
    private String transactionNumber;
    private Date transactionDate;
    private String payee;
    private String bankBranchName;
    private String surrendarReason;
    private CFinancialYear serialNo;
    private ECSType ECSType;

    private Set<InstrumentVoucher> instrumentVouchers = new HashSet<InstrumentVoucher>(0);

    // Property accessors

    public CFinancialYear getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(CFinancialYear serialNo) {
        this.serialNo = serialNo;
    }

    /**
     * @return the instrumentVouchers
     */
    public Set<InstrumentVoucher> getInstrumentVouchers() {
        return instrumentVouchers;
    }

    /**
     * @param instrumentVouchers the instrumentVouchers to set
     */
    public void setInstrumentVouchers(final Set<InstrumentVoucher> instrumentVouchers) {
        this.instrumentVouchers = instrumentVouchers;
    }

    /**
     * @return the statusId
     */
    public EgwStatus getStatusId() {
        return statusId;
    }

    public String getBankBranchName() {
        return bankBranchName;
    }

    public void setBankBranchName(final String bankBranchName) {
        this.bankBranchName = bankBranchName;
    }

    /**
     * @param statusId the statusId to set
     */
    public void setStatusId(final EgwStatus statusId) {
        this.statusId = statusId;
    }

    /**
     * @return the bankId
     */
    public Bank getBankId() {
        return bankId;
    }

    /**
     * @param bankId the bankId to set
     */
    public void setBankId(final Bank bankId) {
        this.bankId = bankId;
    }

    /**
     * @return the instrumentNumber
     */
    public String getInstrumentNumber() {
        return instrumentNumber;
    }

    /**
     * @param instrumentNumber the instrumentNumber to set
     */
    public void setInstrumentNumber(final String instrumentNumber) {
        this.instrumentNumber = instrumentNumber;
    }

    /**
     * @return the instrumentDate
     */
    public Date getInstrumentDate() {
        return instrumentDate;
    }

    /**
     * @param instrumentDate the instrumentDate to set
     */
    public void setInstrumentDate(final Date instrumentDate) {
        this.instrumentDate = instrumentDate;
    }

    /**
     * @return the payTo
     */
    public String getPayTo() {
        return payTo;
    }

    /**
     * @param payTo the payTo to set
     */
    public void setPayTo(final String payTo) {
        this.payTo = payTo;
    }

    /**
     * @return the isPayCheque
     */
    public String getIsPayCheque() {
        return isPayCheque;
    }

    /**
     * @param isPayCheque the isPayCheque to set
     */
    public void setIsPayCheque(final String isPayCheque) {
        this.isPayCheque = isPayCheque;
    }

    /**
     * @return the instrumentType
     */
    public InstrumentType getInstrumentType() {
        return instrumentType;
    }

    /**
     * @param instrumentType the instrumentType to set
     */
    public void setInstrumentType(final InstrumentType instrumentType) {
        this.instrumentType = instrumentType;
    }

    /**
     * @return the detailKeyId
     */
    public Long getDetailKeyId() {
        return detailKeyId;
    }

    /**
     * @param detailKeyId the detailKeyId to set
     */
    public void setDetailKeyId(final Long detailKeyId) {
        this.detailKeyId = detailKeyId;
    }

    /**
     * @return the transactionNumber
     */
    public String getTransactionNumber() {
        return transactionNumber;
    }

    /**
     * @param transactionNumber the transactionNumber to set
     */
    public void setTransactionNumber(final String transactionNumber) {
        this.transactionNumber = transactionNumber;
    }

    /**
     * @return the transactionDate
     */
    public Date getTransactionDate() {
        return transactionDate;
    }

    /**
     * @param transactionDate the transactionDate to set
     */
    public void setTransactionDate(final Date transactionDate) {
        this.transactionDate = transactionDate;
    }

    /**
     * @return the bankAccountId
     */
    public Bankaccount getBankAccountId() {
        return bankAccountId;
    }

    /**
     * @param bankAccountId the bankAccountId to set
     */
    public void setBankAccountId(final Bankaccount bankAccountId) {
        this.bankAccountId = bankAccountId;
    }

    /**
     * @return the detailTypeId
     */
    public Accountdetailtype getDetailTypeId() {
        return detailTypeId;
    }

    /**
     * @param detailTypeId the detailTypeId to set
     */
    public void setDetailTypeId(final Accountdetailtype detailTypeId) {
        this.detailTypeId = detailTypeId;
    }

    /**
     * @return the payee
     */
    public String getPayee()
    {
        return payee;
    }

    /**
     * @param payee the payee to set
     */
    public void setPayee(final String payee)
    {
        this.payee = payee;
    }

    public BigDecimal getInstrumentAmount() {
        return instrumentAmount;
    }

    public void setInstrumentAmount(final BigDecimal instrumentAmount) {
        this.instrumentAmount = instrumentAmount;
    }

    /**
     * @return the ECSType
     */
    public ECSType getECSType() {
        return ECSType;
    }

    /**
     * @param ECSType the ECSType to set
     */
    public void setECSType(final ECSType ECSType) {
        this.ECSType = ECSType;
    }

    @Override
    public InstrumentHeader clone() {

        final InstrumentHeader newInstrumentHeader = new InstrumentHeader();
        newInstrumentHeader.setBankAccountId(bankAccountId);
        newInstrumentHeader.setBankBranchName(bankBranchName);
        newInstrumentHeader.setBankId(bankId);
        newInstrumentHeader.setDetailKeyId(detailKeyId);
        newInstrumentHeader.setDetailTypeId(detailTypeId);
        newInstrumentHeader.setInstrumentAmount(instrumentAmount);
        newInstrumentHeader.setInstrumentDate(instrumentDate);
        newInstrumentHeader.setInstrumentNumber(instrumentNumber);
        newInstrumentHeader.setInstrumentType(instrumentType);
        newInstrumentHeader.setIsPayCheque(isPayCheque);
        newInstrumentHeader.setPayee(payee);
        newInstrumentHeader.setPayTo(payTo);
        newInstrumentHeader.setTransactionDate(transactionDate);
        newInstrumentHeader.setTransactionNumber(transactionNumber);
        newInstrumentHeader.setSurrendarReason(surrendarReason);
        return newInstrumentHeader;

    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final InstrumentHeader other = (InstrumentHeader) obj;
        if (bankAccountId == null) {
            if (other.bankAccountId != null)
                return false;
        } else if (!bankAccountId.equals(other.bankAccountId))
            return false;
        if (bankId == null) {
            if (other.bankId != null)
                return false;
        } else if (!bankId.equals(other.bankId))
            return false;
        if (instrumentAmount == null) {
            if (other.instrumentAmount != null)
                return false;
        } else if (!instrumentAmount.equals(other.instrumentAmount))
            return false;
        if (instrumentDate == null) {
            if (other.instrumentDate != null)
                return false;
        } else if (!instrumentDate.equals(other.instrumentDate))
            return false;
        if (instrumentNumber == null) {
            if (other.instrumentNumber != null)
                return false;
        } else if (!instrumentNumber.equals(other.instrumentNumber))
            return false;
        if (instrumentType == null) {
            if (other.instrumentType != null)
                return false;
        } else if (!instrumentType.equals(other.instrumentType))
            return false;
        if (isPayCheque == null) {
            if (other.isPayCheque != null)
                return false;
        } else if (!isPayCheque.equals(other.isPayCheque))
            return false;
        if (transactionDate == null) {
            if (other.transactionDate != null)
                return false;
        } else if (!transactionDate.equals(other.transactionDate))
            return false;
        if (transactionNumber == null) {
            if (other.transactionNumber != null)
                return false;
        } else if (!transactionNumber.equals(other.transactionNumber))
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (bankAccountId == null ? 0 : bankAccountId.hashCode());
        result = prime * result + (bankId == null ? 0 : bankId.hashCode());
        result = prime * result + (instrumentAmount == null ? 0 : instrumentAmount.hashCode());
        result = prime * result + (instrumentDate == null ? 0 : instrumentDate.hashCode());
        result = prime * result + (instrumentNumber == null ? 0 : instrumentNumber.hashCode());
        result = prime * result + (instrumentType == null ? 0 : instrumentType.hashCode());
        result = prime * result + (isPayCheque == null ? 0 : isPayCheque.hashCode());
        result = prime * result + (transactionDate == null ? 0 : transactionDate.hashCode());
        result = prime * result + (transactionNumber == null ? 0 : transactionNumber.hashCode());
        return result;
    }

    @Override
    public String toString() {

        final StringBuffer str = new StringBuffer(1024);
        str.append("id:").append(id)
                .append("Number:").append(instrumentNumber)
                .append("Date:").append(instrumentDate)
                .append("Amount:").append(instrumentAmount)
                .append("Type:").append(instrumentType);
        return str.toString();
    }

    public String getSurrendarReason() {
        return surrendarReason;
    }

    public void setSurrendarReason(final String surrendarReason) {
        this.surrendarReason = surrendarReason;
    }

}
