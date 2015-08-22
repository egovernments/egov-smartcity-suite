/**
 *
 */
package org.egov.collection.integration.models;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author rishi
 */
public class PaymentInfoChequeDD implements PaymentInfo {

    private Long bankId;
    private String branchName;
    private Date instrumentDate;
    private String instrumentNumber;
    private TYPE instrumentType;
    private BigDecimal instrumentAmount;

    /**
     * Default constructor
     */
    public PaymentInfoChequeDD() {
    }

    public PaymentInfoChequeDD(final Long bankId, final String branchName, final Date instrumentDate,
            final String instrumentNumber, final TYPE instrumentType, final BigDecimal instrumentAmount) {
        this.bankId = bankId;
        this.branchName = branchName;
        this.instrumentDate = instrumentDate;
        this.instrumentNumber = instrumentNumber;
        this.instrumentType = instrumentType;
        this.instrumentAmount = instrumentAmount;
    }

    public Long getBankId() {
        return bankId;
    }

    public String getBranchName() {
        return branchName;
    }

    @Override
    public BigDecimal getInstrumentAmount() {
        return instrumentAmount;
    }

    public Date getInstrumentDate() {
        return instrumentDate;
    }

    public String getInstrumentNumber() {
        return instrumentNumber;
    }

    @Override
    public TYPE getInstrumentType() {
        return instrumentType;
    }

    /**
     * @param bankId
     *            the bankId to set
     */
    public void setBankId(final Long bankId) {
        this.bankId = bankId;
    }

    /**
     * @param branchName
     *            the branchName to set
     */
    public void setBranchName(final String branchName) {
        this.branchName = branchName;
    }

    /**
     * @param instrumentDate
     *            the instrumentDate to set
     */
    public void setInstrumentDate(final Date instrumentDate) {
        this.instrumentDate = instrumentDate;
    }

    /**
     * @param instrumentNumber
     *            the instrumentNumber to set
     */
    public void setInstrumentNumber(final String instrumentNumber) {
        this.instrumentNumber = instrumentNumber;
    }

    /**
     * @param instrumentAmount
     *            the instrumentAmount to set
     */
    @Override
    public void setInstrumentAmount(final BigDecimal instrumentAmount) {
        this.instrumentAmount = instrumentAmount;
    }

    /**
     * @param instrumentType
     *            the instrumentType to set
     */
    public void setInstrumentType(final TYPE instrumentType) {
        this.instrumentType = instrumentType;
    }
}
