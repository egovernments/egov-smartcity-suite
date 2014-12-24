package org.egov.ptis.domain.entity.property;

import java.math.BigDecimal;

public class PropertyArrear {
	private Long id;
	private BasicProperty basicProperty;
	private Integer fromDate;
	private Integer toDate;
	private BigDecimal generalTax = BigDecimal.ZERO;
	private BigDecimal sewerageTax = BigDecimal.ZERO;
	private BigDecimal fireServiceTax = BigDecimal.ZERO;
	private BigDecimal lightingTax = BigDecimal.ZERO;
	private BigDecimal generalWaterTax = BigDecimal.ZERO;
	private BigDecimal educationCess = BigDecimal.ZERO;
	private BigDecimal egCess = BigDecimal.ZERO;
	private BigDecimal bigResidentailTax = BigDecimal.ZERO;
	private BigDecimal penalty = BigDecimal.ZERO;
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public BasicProperty getBasicProperty() {
		return basicProperty;
	}
	
	public void setBasicProperty(BasicProperty basicProperty) {
		this.basicProperty = basicProperty;
	}
	
	public Integer getFromDate() {
		return fromDate;
	}
	
	public void setFromDate(Integer fromDate) {
		this.fromDate = fromDate;
	}
	
	public Integer getToDate() {
		return toDate;
	}
	
	public void setToDate(Integer toDate) {
		this.toDate = toDate;
	}
	
	public BigDecimal getGeneralTax() {
		return generalTax;
	}
	
	public void setGeneralTax(BigDecimal generalTax) {
		this.generalTax = generalTax;
	}
	
	public BigDecimal getSewerageTax() {
		return sewerageTax;
	}
	
	public void setSewerageTax(BigDecimal sewerageTax) {
		this.sewerageTax = sewerageTax;
	}
	
	public BigDecimal getFireServiceTax() {
		return fireServiceTax;
	}
	
	public void setFireServiceTax(BigDecimal fireServiceTax) {
		this.fireServiceTax = fireServiceTax;
	}
	
	public BigDecimal getLightingTax() {
		return lightingTax;
	}
	
	public void setLightingTax(BigDecimal lightingTax) {
		this.lightingTax = lightingTax;
	}
	
	public BigDecimal getGeneralWaterTax() {
		return generalWaterTax;
	}
	
	public void setGeneralWaterTax(BigDecimal generalWaterTax) {
		this.generalWaterTax = generalWaterTax;
	}
	
	public BigDecimal getEducationCess() {
		return educationCess;
	}
	
	public void setEducationCess(BigDecimal educationCess) {
		this.educationCess = educationCess;
	}
	
	public BigDecimal getEgCess() {
		return egCess;
	}
	
	public void setEgCess(BigDecimal egCess) {
		this.egCess = egCess;
	}
	
	public BigDecimal getBigResidentailTax() {
		return bigResidentailTax;
	}
	
	public void setBigResidentailTax(BigDecimal bigResidentailTax) {
		this.bigResidentailTax = bigResidentailTax;
	}
	
	public BigDecimal getPenalty() {
		return penalty;
	}

	public void setPenalty(BigDecimal penalty) {
		this.penalty = penalty;
	}

	@Override
	public String toString() {
		return new StringBuilder(150)
				.append("PropertyArrear")
				.append(" [")
				.append("id=").append(getId())
				.append(", idBasicProperty=").append((getBasicProperty() != null) ? getBasicProperty().getId() : " ")
				.append(", fromDate=").append(getFromDate())
				.append(", toDate=").append(getToDate())
				.append(", generalTax=").append(getGeneralTax())
				.append(", sewerageTax=").append(getSewerageTax())
				.append(", fireServiceTax=").append(getFireServiceTax())
				.append(", lightingTax=").append(getLightingTax())
				.append(", generalWaterTax=").append(getGeneralWaterTax())
				.append(", educationCess=").append(getEducationCess())
				.append(", egCess=").append(getEgCess())
				.append(", bigResidentailTax=").append(getBigResidentailTax())
				.append(", penalty=").append(getPenalty())
				.append("]").toString();
	}
}
