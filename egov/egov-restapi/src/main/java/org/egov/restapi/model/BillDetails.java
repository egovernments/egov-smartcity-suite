package org.egov.restapi.model;

import java.io.Serializable;
import java.math.BigDecimal;

public class BillDetails implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -5127775461515959368L;

    private String glcode;

    private BigDecimal debitAmount;

    private BigDecimal creditAmount;

    public String getGlcode() {
        return glcode;
    }

    public void setGlcode(final String glcode) {
        this.glcode = glcode;
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
}
