/*
 * @(#)FileAssignment.java 3.0, 15 Jul, 2013 9:36:47 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.dms.models;

import javax.validation.Valid;

import org.egov.infstr.models.BaseModel;
import org.hibernate.validator.constraints.Length;

/**
 * The Class FileAssignment.
 */
public class FileAssignment extends BaseModel {
	
	private static final long serialVersionUID = 1L;

	/** The user type. */
	@Length(min=1,max=100,message="err.usrtypelen")
	private String userType;
	
	/** The internal user. */
	@Valid
	private InternalUser internalUser;
	
	/** The external user. */
	@Valid
	private ExternalUser externalUser;
	
	/** The level. */
	private Integer level;
	
	
	
	/**
	 * Gets the user type.
	 * @return the user type
	 */
	public String getUserType() {
		return userType;
	}
	
	/**
	 * Sets the user type.
	 * @param userType the new user type
	 */
	public void setUserType(String userType) {
		this.userType = userType;
	}
	
	/**
	 * Gets the internal user.
	 * @return the internal user
	 */
	public InternalUser getInternalUser() {
		return internalUser;
	}
	
	/**
	 * Sets the internal user.
	 * @param internalUser the new internal user
	 */
	public void setInternalUser(InternalUser internalUser) {
		this.internalUser = internalUser;
	}
	
	/**
	 * Gets the external user.
	 * @return the external user
	 */
	public ExternalUser getExternalUser() {
		return externalUser;
	}
	
	/**
	 * Sets the external user.
	 * @param externalUser the new external user
	 */
	public void setExternalUser(ExternalUser externalUser) {
		this.externalUser = externalUser;
	}
	
	/**
	 * Gets the level.
	 * @return the level
	 */
	public Integer getLevel() {
		return level;
	}
	
	/**
	 * Sets the level.
	 * @param level the new level
	 */
	public void setLevel(Integer level) {
		this.level = level;
	}

	
}
