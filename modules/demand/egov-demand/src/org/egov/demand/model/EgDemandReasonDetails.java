package org.egov.demand.model;

import java.math.BigDecimal;
import java.util.Date;

/**
 * EgDemandReasonDetails entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class EgDemandReasonDetails implements java.io.Serializable {

    // Fields

    private Long id;
    private EgDemandReason egDemandReason;
    // ToDo: Make it Float
    private BigDecimal percentage;
    private Date fromDate;
    private Date toDate;
    private BigDecimal lowLimit;
    private BigDecimal highLimit;
    private Date createTimestamp;
    private Date lastUpdatedTimeStamp;
    private BigDecimal flatAmount;
    private Integer isFlatAmntMax;
    // Property accessors

    @Override
    public Object clone() {
        EgDemandReasonDetails clone = null;
        try {
            clone = (EgDemandReasonDetails) super.clone();
        } catch (CloneNotSupportedException e) {
            // this should never happen
            throw new InternalError(e.toString());
        }
        clone.setId(null);
        return clone;
    }

    public BigDecimal getFlatAmount() {
        return flatAmount;
    }

    public void setFlatAmount(BigDecimal flatAmount) {
        this.flatAmount = flatAmount;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EgDemandReason getEgDemandReason() {
        return this.egDemandReason;
    }

    public void setEgDemandReason(EgDemandReason egDemandReason) {
        this.egDemandReason = egDemandReason;
    }

    public BigDecimal getPercentage() {
        return this.percentage;
    }

    public void setPercentage(BigDecimal percentage) {
        this.percentage = percentage;
    }

    public Date getFromDate() {
        return this.fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getToDate() {
        return this.toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    public BigDecimal getLowLimit() {
        return this.lowLimit;
    }

    public void setLowLimit(BigDecimal lowLimit) {
        this.lowLimit = lowLimit;
    }

    public BigDecimal getHighLimit() {
        return this.highLimit;
    }

    public void setHighLimit(BigDecimal highLimit) {
        this.highLimit = highLimit;
    }

    public Date getCreateTimestamp() {
        return this.createTimestamp;
    }

    public void setCreateTimestamp(Date createTimestamp) {
        this.createTimestamp = createTimestamp;
    }

    public Date getLastUpdatedTimeStamp() {
        return lastUpdatedTimeStamp;
    }

    public void setLastUpdatedTimeStamp(Date lastUpdatedTimeStamp) {
        this.lastUpdatedTimeStamp = lastUpdatedTimeStamp;
    }
    
    public Integer getIsFlatAmntMax() {
    	return isFlatAmntMax;
    }
    
    public void setIsFlatAmntMax(Integer isFlatAmntMax) {
    	this.isFlatAmntMax = isFlatAmntMax;
    }
}