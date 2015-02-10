/*
 * @(#)Position.java 3.0, 7 Jun, 2013 8:34:48 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.pims.commons;

import java.util.Date;

public class Position {
	
	private Integer id;
	private String name;
	private Integer sanctionedPosts;//move todept desig newobj
	private Integer outsourcedPosts;
	private DesignationMaster desigId;
	private Date efferctiveDate;//remove this
	private DrawingOfficer drawingOfficer;
	private DeptDesig deptDesigId;
	private Integer isPostOutsourced;
	

	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public Integer getOutsourcedPosts() {
		return outsourcedPosts;
	}
	
	public void setOutsourcedPosts(Integer outsourcedPosts) {
		this.outsourcedPosts = outsourcedPosts;
	}
	
	public Integer getSanctionedPosts() {
		return sanctionedPosts;
	}
	
	public void setSanctionedPosts(Integer sanctionedPosts) {
		this.sanctionedPosts = sanctionedPosts;
	}
	
	public DesignationMaster getDesigId() {
		return desigId;
	}
	
	public void setDesigId(DesignationMaster desigId) {
		this.desigId = desigId;
	}
	
	public Date getEfferctiveDate() {
		return efferctiveDate;
	}
	
	public void setEfferctiveDate(Date efferctiveDate) {
		this.efferctiveDate = efferctiveDate;
	}

	public DrawingOfficer getDrawingOfficer() {	 	
		return drawingOfficer;
	}

	public void setDrawingOfficer(DrawingOfficer drawingOfficer) {
		this.drawingOfficer = drawingOfficer;
	}

	public DeptDesig getDeptDesigId() {
		return deptDesigId;
	}

	public void setDeptDesigId(DeptDesig deptDesigId) {
		this.deptDesigId = deptDesigId;
	}

	public Integer getIsPostOutsourced() {
		return isPostOutsourced;
	}

	public void setIsPostOutsourced(Integer isPostOutsourced) {
		this.isPostOutsourced = (isPostOutsourced==null?0:isPostOutsourced);
	}

}
