/*
 * @(#)CFunction.java 3.0, 6 Jun, 2013 3:03:56 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.commons;

import java.util.Date;

public class CFunction {

	private Long id = null;
	private String name;
	private String code;
	private String type;
	private int level;
	private Long parentId;
	private int isActive;
	private Date created;
	private Date lastModified;
	private String modifiedBy;
	private int isNotLeaf;

	/**
	 * @return Returns the id.
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id The id to set.
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return Returns the name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name The name to set.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return Returns the code.
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @param code The code to set.
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * @return Returns the created.
	 */
	public Date getCreated() {
		return created;
	}

	/**
	 * @param created The created to set.
	 */
	public void setCreated(Date created) {
		this.created = created;
	}

	/**
	 * @return Returns the isActive.
	 */
	public int getIsActive() {
		return isActive;
	}

	/**
	 * @param isActive The isActive to set.
	 */
	public void setIsActive(int isActive) {
		this.isActive = isActive;
	}

	/**
	 * @return Returns the isNotLeaf.
	 */
	public int getIsNotLeaf() {
		return isNotLeaf;
	}

	/**
	 * @param isNotLeaf The isNotLeaf to set.
	 */
	public void setIsNotLeaf(int isNotLeaf) {
		this.isNotLeaf = isNotLeaf;
	}

	/**
	 * @return Returns the lastModified.
	 */
	public Date getLastModified() {
		return lastModified;
	}

	/**
	 * @param lastModified The lastModified to set.
	 */
	public void setLastModified(Date lastModified) {
		this.lastModified = lastModified;
	}

	/**
	 * @return Returns the lLevel.
	 */
	public int getLevel() {
		return level;
	}

	/**
	 * @param level The lLevel to set.
	 */
	public void setLevel(int level) {
		this.level = level;
	}

	/**
	 * @return Returns the modifiedBy.
	 */
	public String getModifiedBy() {
		return modifiedBy;
	}

	/**
	 * @param modifiedBy The modifiedBy to set.
	 */
	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	/**
	 * @return Returns the parentId.
	 */
	public Long getParentId() {
		return parentId;
	}

	/**
	 * @param parentId The parentId to set.
	 */
	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	/**
	 * @return Returns the type.
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type The type to set.
	 */
	public void setType(String type) {
		this.type = type;
	}
}
