package org.egov.restapi.model;

import java.io.Serializable;
import java.math.BigDecimal;

public class BillPayeeDetails implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -3195672358231170995L;

    private String glcode;

    private String accountDetailType;

    private String accountDetailKey;

    public String getGlcode() {
        return glcode;
    }

    public void setGlcode(final String glcode) {
        this.glcode = glcode;
    }

    public String getAccountDetailType() {
        return accountDetailType;
    }

    public void setAccountDetailType(final String accountDetailType) {
        this.accountDetailType = accountDetailType;
    }

    public String getAccountDetailKey() {
        return accountDetailKey;
    }

    public void setAccountDetailKey(final String accountDetailKey) {
        this.accountDetailKey = accountDetailKey;
    }

    public BigDecimal getDebitAmount() {
        return debitAmount;
    }

    public void setDebitAmount(final BigDecimal debitAmount) {
        this.debitAmount = debitAmount;
    }

    public BigDecimal getCreditAmount() {
        return creditAmount;
    }

    public void setCreditAmount(final BigDecimal creditAmount) {
        this.creditAmount = creditAmount;
    }

    private BigDecimal debitAmount;

    private BigDecimal creditAmount;

}
