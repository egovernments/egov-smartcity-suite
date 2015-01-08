/*
 * @(#)Favourites.java 3.0, 17 Jun, 2013 2:48:12 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.infstr.models;

public class Favourites extends BaseModel{
	private static final long serialVersionUID = 1L;
	private Integer userId;
	private Integer actionId;
	private String favouriteName;
	private String contextName;
	
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public Integer getActionId() {
		return actionId;
	}
	public void setActionId(Integer actionId) {
		this.actionId = actionId;
	}
	public String getFavouriteName() {
		return favouriteName;
	}
	public void setFavouriteName(String favouriteName) {
		this.favouriteName = favouriteName;
	}
	
	public String getContextName() {
	
		return contextName;
	}
	
	public void setContextName(String ctxName) {
	
		this.contextName = ctxName;
	}
	
	
}
