package org.egov.ptis.nmc.model;

import java.math.BigDecimal;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("applicablefactor")
public class ApplicableFactor {
    @XStreamAsAttribute
    private String factorName;
    // %value or amount value
    private String factorIndex;
    private BigDecimal factorValue;

    public String getFactorName() {
        return factorName;
    }

    public void setFactorName(String factorName) {
        this.factorName = factorName;
    }

    public BigDecimal getFactorValue() {
        return factorValue;
    }

    public void setFactorValue(BigDecimal factorValue) {
        this.factorValue = factorValue;
    }

    public String getFactorIndex() {
        return factorIndex;
    }

    public void setFactorIndex(String factorIndex) {
        this.factorIndex = factorIndex;
    }
}
