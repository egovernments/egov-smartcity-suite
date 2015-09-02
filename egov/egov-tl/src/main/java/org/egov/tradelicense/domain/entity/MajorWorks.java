package org.egov.tradelicense.domain.entity;

import org.egov.infstr.models.validator.Required;

/**
 *  Electrical License Major Works  entity.
 * 
 * @author Iffath
 */
public class MajorWorks {
	private Long id;
	@Required(message = "required")
	private License license;
	private String fireFighterSystem; 
	private String detection;
	private String passiveProtection;
	
	public License getLicense() {
		return license;
	}

	public void setLicense(License license) {
		this.license = license;
	}

	public MajorWorks() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFireFighterSystem() {
		return fireFighterSystem;
	}

	public void setFireFighterSystem(String fireFighterSystem) {
		this.fireFighterSystem = fireFighterSystem;
	}

	public String getDetection() {
		return detection;
	}

	public void setDetection(String detection) {
		this.detection = detection;
	}

	public String getPassiveProtection() {
		return passiveProtection;
	}

	public void setPassiveProtection(String passiveProtection) {
		this.passiveProtection = passiveProtection;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("MajorWorks [id=").append(id).append(", license=")
				.append(license).append(", fireFighterSystem=")
				.append(fireFighterSystem).append(", detection=")
				.append(detection).append(", passiveProtection=")
				.append(passiveProtection).append("]");
		return builder.toString();
	}
}