package org.egov.ptis.bean.dashboard;

import java.math.BigDecimal;

import org.apache.commons.lang3.StringUtils;

public class TaxDefaulters {
    private String ulbName = StringUtils.EMPTY;
    private String propertyType = StringUtils.EMPTY;
    private String ownerName = StringUtils.EMPTY;
    private String period = StringUtils.EMPTY;
    private BigDecimal balance = BigDecimal.ZERO;

    public String getUlbName() {
        return ulbName;
    }

    public void setUlbName(String ulbName) {
        this.ulbName = ulbName;
    }

    public String getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(String propertyType) {
        this.propertyType = propertyType;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

}
