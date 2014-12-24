/*
 * @(#)FileProperty.java 3.0, 15 Jul, 2013 9:36:20 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.dms.models;

import java.io.Serializable;

import org.hibernate.validator.constraints.Length;


/**
 * The Class FileProperty.
 */
public class FileProperty implements Serializable {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	/** The id. */
	protected Long id;
	
	/** The code. */
	@Length(min=1,max=100,message="err.code")
	protected String code;
	
	/** The name. */
	@Length(min=1,max=100,message="err.namelength")
	protected String name;
	
	/** The description. */
	@Length(max=200,message="err.desc")
	protected String description;
	
	/**
	 * Gets the id.
	 * @return the id
	 */
	public Long getId() {
		return id;
	}
	
	/**
	 * Sets the id.
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}
	
	/**
	 * Gets the code.
	 * @return the code
	 */
	public String getCode() {
		return code;
	}
	
	/**
	 * Sets the code.
	 * @param code the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}
	
	/**
	 * Gets the name.
	 * @return the name
	 */
	public String getName() {
		return name.toUpperCase();
	}
	
	/**
	 * Sets the name.
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Gets the description.
	 * @return the description
	 */
	public String getDescription() {
		return description.toUpperCase();
	}
	
	/**
	 * Sets the description.
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
}
