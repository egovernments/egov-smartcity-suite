/*
 * @(#)MajorWorks.java 3.0, 29 Jul, 2013 1:24:27 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.license.domain.entity;

import org.egov.infstr.models.BaseModel;
import org.egov.infstr.models.validator.Required;

public class MajorWorks extends BaseModel {

	private static final long serialVersionUID = 1L;
	@Required(message = "required")
	private License license;
	private String fireFighterSystem;
	private String detection;
	private String passiveProtection;

	public License getLicense() {
		return this.license;
	}

	public void setLicense(final License license) {
		this.license = license;
	}

	public String getFireFighterSystem() {
		return this.fireFighterSystem;
	}

	public void setFireFighterSystem(final String fireFighterSystem) {
		this.fireFighterSystem = fireFighterSystem;
	}

	public String getDetection() {
		return this.detection;
	}

	public void setDetection(final String detection) {
		this.detection = detection;
	}

	public String getPassiveProtection() {
		return this.passiveProtection;
	}

	public void setPassiveProtection(final String passiveProtection) {
		this.passiveProtection = passiveProtection;
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("MajorWorks [id=").append(this.id).append(", license=").append(this.license).append(", fireFighterSystem=").append(this.fireFighterSystem).append(", detection=").append(this.detection).append(", passiveProtection=")
				.append(this.passiveProtection).append("]");
		return builder.toString();
	}
}