/*
 * @(#)ExternalUserType.java 3.0, 15 Jul, 2013 9:38:17 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.dms.models;

import java.io.Serializable;

import org.hibernate.validator.constraints.Length;


/**
 * The Class ExternalUserType.
 */
public class ExternalUserType implements Serializable {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	/** The id. */
	private Long id;
	
	/** The type code. */
	@Length(min=1,max=10,message="err.typecode")
	private String typeCode;
	
	/** The type name. */
	@Length(min=1,max=100,message="err.typename")
	private String typeName;
	
	/** The type description. */
	@Length(max=200,message="err.typedesc")
	private String typeDescription;
	
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
	 * Gets the type code.
	 * @return the typeCode
	 */
	public String getTypeCode() {
		return typeCode;
	}
	
	/**
	 * Sets the type code.
	 * @param typeCode the typeCode to set
	 */
	public void setTypeCode(String typeCode) {
		this.typeCode = typeCode;
	}
	
	/**
	 * Gets the type name.
	 * @return the typeName
	 */
	public String getTypeName() {
		return typeName;
	}
	
	/**
	 * Sets the type name.
	 * @param typeName the typeName to set
	 */
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	
	/**
	 * Gets the type description.
	 * @return the typeDescription
	 */
	public String getTypeDescription() {
		return typeDescription;
	}
	
	/**
	 * Sets the type description.
	 * @param typeDescription the typeDescription to set
	 */
	public void setTypeDescription(String typeDescription) {
		this.typeDescription = typeDescription;
	}
}
