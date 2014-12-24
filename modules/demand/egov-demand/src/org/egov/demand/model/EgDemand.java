package org.egov.demand.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.egov.commons.Installment;

/**
 * EgDemand entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class EgDemand implements Cloneable {

    private Long id;
    private Installment egInstallmentMaster;
    private BigDecimal baseDemand = BigDecimal.ZERO;
    private String isHistory;
    private Date createTimestamp;
    private Date lastUpdatedTimestamp;
    private Set<EgDemandDetails> egDemandDetails = new HashSet<EgDemandDetails>(0);
    private Set<EgBill> egBills = new HashSet<EgBill>(0);
    private BigDecimal amtCollected = BigDecimal.ZERO;
    private Character status;
    private BigDecimal minAmtPayable = BigDecimal.ZERO;
    private BigDecimal amtRebate = BigDecimal.ZERO;

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        EgDemand other = (EgDemand) obj;
        if (id != null && other != null && id.equals(other.id)) {
            return true;
        }
        return false;
    }

    /**
     * Returns a copy that can be associated with another billing system entity.
     * The copy has the same amounts, installment, time stamps and (cloned)
     * demand-details if any. It will NOT have a copy of the EgBills of the
     * original demand. (Note: making it public instead of protected to allow
     * any class to use it.)
     */
    @Override
    public Object clone() {
        EgDemand clone = null;
        try {
            clone = (EgDemand) super.clone();
        } catch (CloneNotSupportedException e) {
            // this should never happen
            throw new InternalError(e.toString());
        }
        clone.setId(null);
        clone.setEgBills(new HashSet<EgBill>());
        clone.setEgDemandDetails(new HashSet<EgDemandDetails>());
        for (EgDemandDetails det : egDemandDetails) {
            clone.addEgDemandDetails((EgDemandDetails) det.clone());
        }
        return clone;
    }

    public Character getStatus() {
        return status;
    }

    public void setStatus(Character status) {
        this.status = status;
    }

    public BigDecimal getMinAmtPayable() {
        return minAmtPayable;
    }

    public void setMinAmtPayable(BigDecimal minAmtPayable) {
        this.minAmtPayable = minAmtPayable;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Installment getEgInstallmentMaster() {
        return this.egInstallmentMaster;
    }

    public void setEgInstallmentMaster(Installment egInstallmentMaster) {
        this.egInstallmentMaster = egInstallmentMaster;
    }

    public BigDecimal getBaseDemand() {
        return this.baseDemand;
    }

    public void setBaseDemand(BigDecimal baseDemand) {
        this.baseDemand = baseDemand;
    }

    public void addBaseDemand(BigDecimal amount) {
        setBaseDemand(getBaseDemand().add(amount));
    }

    public String getIsHistory() {
        return this.isHistory;
    }

    public void setIsHistory(String isHistory) {
        this.isHistory = isHistory;
    }

    public Date getCreateTimestamp() {
        return this.createTimestamp;
    }

    public void setCreateTimestamp(Date createTimestamp) {
        this.createTimestamp = createTimestamp;
    }

    public Date getLastUpdatedTimestamp() {
        return this.lastUpdatedTimestamp;
    }

    public void setLastUpdatedTimestamp(Date lastUpdatedTimestamp) {
        this.lastUpdatedTimestamp = lastUpdatedTimestamp;
    }

    public Set<EgBill> getEgBills() {
        return this.egBills;
    }

    public void setEgBills(Set<EgBill> egBills) {
        this.egBills = egBills;
    }

    public Set<EgDemandDetails> getEgDemandDetails() {
        return egDemandDetails;
    }

    public void setEgDemandDetails(Set<EgDemandDetails> egDemandDetails) {
        this.egDemandDetails = egDemandDetails;
    }

    public void addEgBill(EgBill egBill) {
        getEgBills().add(egBill);
    }

    public void removeEgBill(EgBill egBill) {
        getEgBills().remove(egBill);
    }

    public void addEgDemandDetails(EgDemandDetails egDemandDetails) {
        getEgDemandDetails().add(egDemandDetails);
    }

    public void removeEgDemandDetails(EgDemandDetails egDemandDetails) {
        getEgDemandDetails().remove(egDemandDetails);
    }

    public BigDecimal getAmtCollected() {
        return amtCollected;
    }

    public void setAmtCollected(BigDecimal amtCollected) {
        this.amtCollected = amtCollected;
    }

    /**
     * Adds an amount to the existing collected amount.
     */
    public void addCollected(BigDecimal amount) {
        if (getAmtCollected() != null) {
            setAmtCollected(getAmtCollected().add(amount != null ? amount : BigDecimal.ZERO));
        } else {
            setAmtCollected(amount);
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(id).append("|").append(egInstallmentMaster).append("|").append(baseDemand)
                .append("|").append(amtCollected).append("|").append(egDemandDetails).append("|")
                .append(amtRebate);
        return sb.toString();
    }

    public BigDecimal getAmtRebate() {
        return amtRebate;
    }

    public void setAmtRebate(BigDecimal amtRebate) {
        this.amtRebate = amtRebate;
    }

    public void addRebateAmt(BigDecimal rebateAmt) {
        if (getAmtRebate() != null) {
            setAmtRebate(getAmtRebate().add(rebateAmt != null ? rebateAmt : BigDecimal.ZERO));
        } else {
            setAmtRebate(rebateAmt);
        }
    }

}