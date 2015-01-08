/*
 * @(#)WorkflowTypes.java 3.0, 17 Jun, 2013 2:57:39 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.infstr.models;

import org.egov.infstr.commons.Module;

public class WorkflowTypes extends BaseModel {

	private static final long serialVersionUID = 1L;
	public static final String MODULE_FOR_TYPE = "MODULE_FOR_TYPE";
	public static final String TYPE_FOR_NAME = "TYPE_FOR_NAME";
	public static final String TYPE_LIKE_NAME = "TYPE_LIKE_NAME";

	private Module module;
	private String type;
	private String fullyQualifiedName;
	private String link;
	private String displayName;
	private Character renderYN;
	private Character groupYN;

	/**
	 * Gets the module.
	 * @return the module
	 */
	public Module getModule() {
		return module;
	}

	/**
	 * Sets the module.
	 * @param module the new module
	 */
	public void setModule(final Module module) {
		this.module = module;
	}

	/**
	 * Gets the type.
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * Sets the type.
	 * @param type the new type
	 */
	public void setType(final String type) {
		this.type = type;
	}

	/**
	 * Gets the display name.
	 * @return the display name
	 */
	public String getDisplayName() {
		return displayName;
	}

	/**
	 * Sets the display name.
	 * @param displayName the new display name
	 */
	public void setDisplayName(final String displayName) {
		this.displayName = displayName;
	}

	/**
	 * Gets the link.
	 * @return the link
	 */
	public String getLink() {
		return link;
	}

	/**
	 * Sets the link.
	 * @param link the new link
	 */
	public void setLink(final String link) {
		this.link = link;
	}

	/**
	 * Gets the fully qualified name.
	 * @return the fully qualified name
	 */
	public String getFullyQualifiedName() {
		return fullyQualifiedName;
	}

	/**
	 * Sets the fully qualified name.
	 * @param fullyQualifiedName the new fully qualified name
	 */
	public void setFullyQualifiedName(final String fullyQualifiedName) {
		this.fullyQualifiedName = fullyQualifiedName;
	}

	/**
	 * Gets the render yn.
	 * @return the render yn
	 */
	public Character getRenderYN() {
		return renderYN;
	}

	/**
	 * Sets the render yn.
	 * @param renderYN the new render yn
	 */
	public void setRenderYN(final Character renderYN) {
		this.renderYN = renderYN;
	}

	/**
	 * Gets the group yn.
	 * @return the group yn
	 */
	public Character getGroupYN() {
		return groupYN;
	}

	/**
	 * Sets the group yn.
	 * @param groupYN the new group yn
	 */
	public void setGroupYN(final Character groupYN) {
		this.groupYN = groupYN;
	}
}
