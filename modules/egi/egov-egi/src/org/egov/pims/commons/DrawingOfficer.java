/*
 * @(#)DrawingOfficer.java 3.0, 7 Jun, 2013 8:36:07 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.pims.commons;

import org.egov.commons.utils.EntityType;

public class DrawingOfficer implements  java.io.Serializable,EntityType {
	private static final long serialVersionUID = 1L;
	public static final String QRY_DO_STARTSWITH="DRAWINGOFFICER_STARTSWITH"; 
	private Integer id ;
	private String code;
	private String name;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public String getBankaccount() {
		return null;
	}
	@Override
	public String getBankname() {
		return null;
	}
	
	public String getEntityDescription() {
		return getName();
	}
	@Override	
	public Integer getEntityId() {
		return this.id;		
	}
	@Override
	public String getIfsccode() {
		return null;
	}
	@Override
	public String getModeofpay() {
		return null;
	}
	@Override
	public String getPanno() {
		return null;
	}
	@Override
	public String getTinno() {
		return null;
	}
	
	

}
