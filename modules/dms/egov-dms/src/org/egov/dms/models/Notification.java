/*
 * @(#)Notification.java 3.0, 15 Jul, 2013 9:35:16 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.dms.models;

import javax.validation.Valid;

import org.egov.infstr.models.BaseModel;
import org.egov.pims.commons.Position;


public class Notification extends BaseModel {

	private static final long serialVersionUID = 1L;
	@Valid
	private GenericFile file;
	@Valid
	private Position position;
	
	/**
	 * @return the file
	 */
	public GenericFile getFile() {
		return file;
	}
	
	/**
	 * @param file the file to set
	 */
	public void setFile(GenericFile file) {
		this.file = file;
	}
	
	/**
	 * @return the position
	 */
	public Position getPosition() {
		return position;
	}
	
	/**
	 * @param position the position to set
	 */
	public void setPosition(Position position) {
		this.position = position;
	}
	
}
