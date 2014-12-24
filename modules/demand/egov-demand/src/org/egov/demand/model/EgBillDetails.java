package org.egov.demand.model;

import java.math.BigDecimal;
import java.util.Date;

import org.egov.commons.Installment;

/**
 * EgBillDetails entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class EgBillDetails implements java.io.Serializable, Comparable<EgBillDetails> {

	// Fields

	private Long id;
	private EgBill egBill;
	private Date createTimeStamp;
	private Date lastUpdatedTimestamp;
	private String glcode;  
	private BigDecimal collectedAmount;
	private Integer orderNo;
	private String functionCode;
	private BigDecimal crAmount;
	private BigDecimal drAmount;
	private String description;
	private Installment egInstallmentMaster;
	private Integer additionalFlag;
	private EgDemandReason egDemandReason;
	
    @Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("EgBillDetails [glcode=").append(glcode)
				.append(", collectedAmount=").append(collectedAmount)
				.append(", orderNo=").append(orderNo).append(", functionCode=")
				.append(functionCode).append(", crAmount=").append(crAmount)
				.append(", drAmount=").append(drAmount)
				.append(", description=").append(description)
				.append(", additionalFlag=").append(additionalFlag)
				.append(", egDemandReason=").append(egDemandReason).append("]");
		return builder.toString();
	}
    
    /**
     * The "orderNo" field is used as the key to sort bill details.
     */
    @Override
    public int compareTo(EgBillDetails other) {
        return this.orderNo.compareTo(other.orderNo);
    }

    /**
     * Returns the difference between the CR and DR amount. 
     */
    public BigDecimal balance() {
        return crAmount.subtract(drAmount);
    }
    
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(Integer orderNo) {
		this.orderNo = orderNo;
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public EgBill getEgBill() {
		return this.egBill;
	}

	public void setEgBill(EgBill egBill) {
		this.egBill = egBill;
	}

	public Date getCreateTimeStamp() {
		return this.createTimeStamp;
	}

	public void setCreateTimeStamp(Date createTimeStamp) {
		this.createTimeStamp = createTimeStamp;
	}

	public Date getLastUpdatedTimestamp() {
		return this.lastUpdatedTimestamp;
	}

	public void setLastUpdatedTimestamp(Date lastUpdatedTimestamp) {
		this.lastUpdatedTimestamp = lastUpdatedTimestamp;
	}

	public String getGlcode() {
		return glcode;
	}

	public void setGlcode(String glcode) {
		this.glcode = glcode;
	}

	public String getFunctionCode() {
		return functionCode;
	}

	public void setFunctionCode(String functionCode) {
		this.functionCode = functionCode;
	}

	public BigDecimal getCrAmount() {
		return crAmount;
	}

	public void setCrAmount(BigDecimal crAmount) {
		this.crAmount = crAmount;
	}

	public BigDecimal getDrAmount() {
		return drAmount;
	}

	public void setDrAmount(BigDecimal drAmount) {
		this.drAmount = drAmount;
	}

	public BigDecimal getCollectedAmount() {
		return collectedAmount;
	}

	public void setCollectedAmount(BigDecimal collectedAmount) {
		this.collectedAmount = collectedAmount;
	}

	public Installment getEgInstallmentMaster() {
		return egInstallmentMaster;
	}

	public void setEgInstallmentMaster(Installment egInstallmentMaster) {
		this.egInstallmentMaster = egInstallmentMaster;
	}

	public Integer getAdditionalFlag() {
		return additionalFlag;
	}

	public void setAdditionalFlag(Integer additionalFlag) {
		this.additionalFlag = additionalFlag;
	}

	public EgDemandReason getEgDemandReason() {
		return egDemandReason;
	}

	public void setEgDemandReason(EgDemandReason egDemandReason) {
		this.egDemandReason = egDemandReason;
	}
	
}