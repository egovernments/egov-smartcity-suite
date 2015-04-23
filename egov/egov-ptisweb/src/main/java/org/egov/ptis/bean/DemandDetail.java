package org.egov.ptis.bean;

import java.math.BigDecimal;

import org.egov.commons.Installment;


/**
 * 
 * @author nayeem
 *
 */
public class DemandDetail {

	private Installment installment;
	private String reasonMaster;
	private BigDecimal actualAmount;
	private BigDecimal revisedAmount;
	private BigDecimal actualCollection;
	private BigDecimal revisedCollection;	
	private Boolean isCollectionEditable;
	private Boolean isNew;

	public DemandDetail() {
	}

	public DemandDetail(String reasonMaster, BigDecimal actualAmount, BigDecimal revisedAmount,
			BigDecimal actualCollection, BigDecimal revisedCollection, Installment installment,
			Boolean isCollectionEditable, Boolean isNew) {
		this.reasonMaster = reasonMaster;
		this.actualAmount = actualAmount;
		this.revisedAmount = revisedAmount;
		this.actualCollection = actualCollection;
		this.revisedCollection = revisedCollection;
		this.installment = installment;
		this.isCollectionEditable = isCollectionEditable;
		this.isNew = isNew;
	}

	

	public String getReasonMaster() {
		return reasonMaster;
	}

	public void setReasonMaster(String reasonMaster) {
		this.reasonMaster = reasonMaster;
	}

	public BigDecimal getActualAmount() {
		return actualAmount;
	}

	public void setActualAmount(BigDecimal actualAmount) {
		this.actualAmount = actualAmount;
	}

	public BigDecimal getRevisedAmount() {
		return revisedAmount;
	}

	public void setRevisedAmount(BigDecimal revisedAmount) {
		this.revisedAmount = revisedAmount;
	}

	public BigDecimal getActualCollection() {
		return actualCollection;
	}

	public void setActualCollection(BigDecimal actualCollection) {
		this.actualCollection = actualCollection;
	}

	public BigDecimal getRevisedCollection() {
		return revisedCollection;
	}

	public void setRevisedCollection(BigDecimal revisedCollection) {
		this.revisedCollection = revisedCollection;
	}

	public Installment getInstallment() {
		return installment;
	}

	public void setInstallment(Installment installment) {
		this.installment = installment;
	}
	
	public Boolean getIsCollectionEditable() {
		return isCollectionEditable;
	}

	public void setIsCollectionEditable(Boolean isCollectionEditable) {
		this.isCollectionEditable = isCollectionEditable;
	}
	
	public Boolean getIsNew() {
		return isNew;
	}

	public void setIsNew(Boolean isNew) {
		this.isNew = isNew;
	}

	@Override
	public String toString() {
		return new StringBuilder(200).append("DemandDetail ")
				.append("[")
				.append("installment=").append(installment)
				.append(", reasonMaster=").append(reasonMaster)
				.append(", actualAmount=").append(actualAmount)
				.append(", revisedAmount=").append(revisedAmount)
				.append(", actualCollection=").append(actualCollection)
				.append(", revisedCollection=").append(revisedCollection)
				.append(", isCollectionEditable=").append(isCollectionEditable)
				.append(", isNew=").append(isNew)
				.append("]").toString();
	}
}
