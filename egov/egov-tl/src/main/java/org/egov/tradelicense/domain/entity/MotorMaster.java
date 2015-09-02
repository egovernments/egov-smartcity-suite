package org.egov.tradelicense.domain.entity;

import java.math.BigDecimal;
import java.util.Date;

import org.egov.infstr.models.BaseModel;
import org.egov.infstr.models.validator.Required;

public class MotorMaster extends BaseModel {
	private static final long serialVersionUID = 1L;
	@Required(message = "masters.master.licensefee.effectivedate")
	private Date effectiveFrom;
	private Date effectiveTo;
	private Long id;
	@Required(message = "masters.erection.motorhpfrom")
	private BigDecimal motorHpFrom;
	@Required(message = "masters.erection.motorhpto")
	private BigDecimal motorHpTo;
	@Required(message = "masters.erection.usingfee")
	private BigDecimal usingFee;

	public Date getEffectiveFrom() {
		return effectiveFrom;
	}

	public Date getEffectiveTo() {
		return effectiveTo;
	}

	@Override
	public Long getId() {
		return id;
	}

	public BigDecimal getMotorHpFrom() {
		return motorHpFrom;
	}

	public BigDecimal getMotorHpTo() {
		return motorHpTo;
	}

	public BigDecimal getUsingFee() {
		return usingFee;
	}

	public void setEffectiveFrom(Date effectiveFrom) {
		this.effectiveFrom = effectiveFrom;
	}

	public void setEffectiveTo(Date effectiveTo) {
		this.effectiveTo = effectiveTo;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	public void setMotorHpFrom(BigDecimal motorHpFrom) {
		this.motorHpFrom = motorHpFrom;
	}

	public void setMotorHpTo(BigDecimal motorHpTo) {
		this.motorHpTo = motorHpTo;
	}

	public void setUsingFee(BigDecimal usingFee) {
		this.usingFee = usingFee;
	}

	
	@Override
	public String toString() {
		StringBuilder str = new StringBuilder();
		str.append("MotorMaster={ ");
		str.append("serialVersionUID=").append(serialVersionUID);
		str.append("Id=").append(id);
		str.append("effectiveFrom=").append(effectiveFrom == null ? "null" : effectiveFrom.toString());
		str.append("effectiveTo=").append(effectiveTo == null ? "null" : effectiveTo.toString());
		str.append("motorHpFrom=").append(motorHpFrom == null ? "null" : motorHpFrom.toString());
		str.append("motorHpTo=").append(motorHpTo == null ? "null" : motorHpTo.toString());
		str.append("usingFee=").append(usingFee == null ? "null" : usingFee.toString());
		str.append("}");
		return str.toString();
	}
}
