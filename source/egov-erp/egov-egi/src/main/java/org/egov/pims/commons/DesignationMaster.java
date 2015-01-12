/*
 * @(#)DesignationMaster.java 3.0, 7 Jun, 2013 8:37:31 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.pims.commons;

import java.util.HashSet;
import java.util.Set;

public class DesignationMaster {
	
	private Integer designationId;
	private Integer deptId;
	private String designationName;
	private String designationLocal;
	private Integer officerLevel;
	private String designationDescription;
	private Integer sanctionedPosts;
	private Integer outsourcedPosts;
	private Set<Position> positionSet = new HashSet<Position>(0);
	
	public Set<Position> getPositionSet() {
		return positionSet;
	}
	
	public void setPositionSet(Set<Position> positionSet) {
		this.positionSet = positionSet;
	}
	
	public void addPosition(Position Position) {
		
		if (getPositionSet() != null)
			getPositionSet().add(Position);
	}
	
	public void removePosition(Position Position) {
		
		if (getPositionSet() != null)
			getPositionSet().remove(Position);
	}
	
	public DesignationMaster() {
	}
	
	public Integer getDeptId() {
		return deptId;
	}
	
	public void setDeptId(Integer deptId) {
		this.deptId = deptId;
	}
	
	public String getDesignationDescription() {
		return designationDescription;
	}
	
	public void setDesignationDescription(String designationDescription) {
		this.designationDescription = designationDescription;
	}
	
	public Integer getDesignationId() {
		return designationId;
	}
	
	public void setDesignationId(Integer designationId) {
		this.designationId = designationId;
	}
	
	public String getDesignationLocal() {
		return designationLocal;
	}
	
	public void setDesignationLocal(String designationLocal) {
		this.designationLocal = designationLocal;
	}
	
	public String getDesignationName() {
		return designationName;
	}
	
	public void setDesignationName(String designationName) {
		this.designationName = designationName;
	}
	
	public Integer getOfficerLevel() {
		return officerLevel;
	}
	
	public void setOfficerLevel(Integer officerLevel) {
		this.officerLevel = officerLevel;
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
	
}
