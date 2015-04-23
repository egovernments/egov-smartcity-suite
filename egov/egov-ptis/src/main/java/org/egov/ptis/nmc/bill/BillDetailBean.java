package org.egov.ptis.nmc.bill;

import static org.egov.ptis.nmc.constants.NMCPTISConstants.DEMANDRSN_REBATE;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.DEMANDRSN_STR_ADVANCE;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.DEMANDRSN_STR_ADVANCE_REBATE;

import java.math.BigDecimal;

import org.egov.commons.Installment;

public class BillDetailBean {
	
	private Installment installment;
	private Integer orderNo;
	private BigDecimal amount;	
	private String key;
	private String glCode;
	private String reasonMaster;
	private String description;
	private Integer isActualDemand;
	
	
	public BillDetailBean() {
	}
	
	public BillDetailBean(Installment installment, Integer orderNo, String key, BigDecimal billDetailAmount,
			String glCode, String reasonMaster, Integer isActualDemand) {
		this.installment = installment;
		this.orderNo = orderNo;
		this.amount = billDetailAmount;
		this.key = key;
		this.glCode = glCode;
		this.reasonMaster = reasonMaster;
		this.isActualDemand = isActualDemand;
		
		if (reasonMaster.equalsIgnoreCase(DEMANDRSN_STR_ADVANCE_REBATE)
				|| reasonMaster.equalsIgnoreCase(DEMANDRSN_STR_ADVANCE)) {
			this.description = reasonMaster + "-" + key;
		} else {
			this.description = reasonMaster + "-" + installment.getDescription();
		}
	}
	
	public boolean isRebate() {
		return reasonMaster.equalsIgnoreCase(DEMANDRSN_STR_ADVANCE_REBATE)
				|| reasonMaster.equalsIgnoreCase(DEMANDRSN_REBATE) ? true : false;
	}
	
	public boolean invalidData() {
		return this.orderNo == null || this.amount == null || this.glCode == null ? true : false;
	}
	
	@Override
	public String toString() {
		return new StringBuilder(200).append("BillDetailBean [")
				.append("installment=").append(installment)
				.append("reasonMaster=").append(reasonMaster)
				.append("description=").append(description)
				.append("glCode=").append(glCode)
				.append("orderNo=").append(orderNo)
				.append("key=").append(key)
				.append("isActualDemand=").append(isActualDemand)
				.append("]").toString();
	}

	public Installment getInstallment() {
		return installment;
	}

	public void setInstallment(Installment installment) {
		this.installment = installment;
	}

	public Integer getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(Integer orderNo) {
		this.orderNo = orderNo;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getGlCode() {
		return glCode;
	}

	public void setGlCode(String glCode) {
		this.glCode = glCode;
	}

	public String getReasonMaster() {
		return reasonMaster;
	}

	public void setReasonMaster(String reasonMaster) {
		this.reasonMaster = reasonMaster;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getIsActualDemand() {
		return isActualDemand;
	}

	public void setIsActualDemand(Integer isActualDemand) {
		this.isActualDemand = isActualDemand;
	}
}
