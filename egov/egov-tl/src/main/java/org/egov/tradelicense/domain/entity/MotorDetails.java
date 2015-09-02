package org.egov.tradelicense.domain.entity;

import java.math.BigDecimal;

import org.egov.infstr.models.validator.Required;

/**
 * TradelicInstalledMotor entity.
 * 
 * @author Rajesh
 */
public class MotorDetails {
	private Long id;
	@Required(message = "tradelicense.error.trader.tradedetails")
	private License license;
	@Required(message = "tradelicense.error.trader.motorhorsepower")
	private BigDecimal hp;
	private Long noOfMachines;
	private boolean history;

	public License getLicense() {
		return license;
	}

	public void setLicense(License license) {
		this.license = license;
	}

	public MotorDetails() {
	}

	public boolean isHistory() {
		return history;
	}

	public void setHistory(boolean history) {
		this.history = history;
	}

	public BigDecimal getHp() {
		return hp;
	}

	public void setHp(BigDecimal hp) {
		this.hp = hp;
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getNoOfMachines() {
		return noOfMachines;
	}

	public void setNoOfMachines(Long noOfMachines) {
		this.noOfMachines = noOfMachines;
	}

	public String toString() {
		StringBuilder str = new StringBuilder();
		str.append("MotorDetails={");
		str.append("  id=").append(id);
		str.append("  hp=").append(hp == null ? "null" : hp.toString());
		str.append("  noOfMachines=").append(noOfMachines == null ? "null" : noOfMachines.toString());
		str.append("  history=").append(history);
		str.append("}");
		return str.toString(); 
		
	}
}