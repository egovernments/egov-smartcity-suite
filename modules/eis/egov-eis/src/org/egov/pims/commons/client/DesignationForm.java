/*
 *
 *
 * Copyright 2005 eGovernments Foundation. All rights reserved.
 * EGOVERNMENTS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.egov.pims.commons.client;
import org.apache.struts.action.ActionForm;
public class DesignationForm extends ActionForm
{

	private String designationId ="";
	private String deptId ="";
	private String designationName ="";
	private String designationLocal ="";
	private String reportsTo ="";
	private	String officerLevel ="";
	private	String designationDescription ="";
	private	String sanctionedPostsDesig ="";
	private	String outsourcedPostsDesig ="";
	private	String departmentId ="";
	
	private	String[] positionName;
	private String[] effectiveDate;
	private	String[] sanctionedPosts ;
	private	String[] outsourcedPosts;
	private String[] tpId;
	private String[] tpIdName;
	private	String[] drawingOfficer;
	private String[] billNumberId;
	
		
	public String[] getPositionName() {
		return positionName;
	}
	public void setPositionName(String[] positionName) {
		this.positionName = positionName;
	}
	
	public String[] getEffectiveDate() {
		return effectiveDate;
	}
	public void setEffectiveDate(String[] effectiveDate) {
		this.effectiveDate = effectiveDate;
	}
	
	public String[] getTpId() {
		return tpId;
	}
	public void setTpId(String[] tpId) {
		this.tpId = tpId;
	}
	public String[] getTpIdName() {
		return tpIdName;
	}
	public void setTpIdName(String[] tpIdName) {
		this.tpIdName = tpIdName;
	}

	public String getDepartmentId() {
		return departmentId;
	}
	public void setDepartmentId(String departmentId) {
		this.departmentId = departmentId;
	}
	/**
	 * @return Returns the annIncrement.
	 */
	
	/**
	 * @return Returns the deptId.
	 */
	public String getDeptId() {
		return deptId;
	}
	/**
	 * @param deptId The deptId to set.
	 */
	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}
	/**
	 * @return Returns the designationDescription.
	 */
	public String getDesignationDescription() {
		return designationDescription;
	}
	/**
	 * @param designationDescription The designationDescription to set.
	 */
	public void setDesignationDescription(String designationDescription) {
		this.designationDescription = designationDescription;
	}
	/**
	 * @return Returns the designationId.
	 */
	public String getDesignationId() {
		return designationId;
	}
	/**
	 * @param designationId The designationId to set.
	 */
	public void setDesignationId(String designationId) {
		this.designationId = designationId;
	}
	/**
	 * @return Returns the designationLocal.
	 */
	public String getDesignationLocal() {
		return designationLocal;
	}
	/**
	 * @param designationLocal The designationLocal to set.
	 */
	public void setDesignationLocal(String designationLocal) {
		this.designationLocal = designationLocal;
	}
	/**
	 * @return Returns the designationName.
	 */
	public String getDesignationName() {
		return designationName;
	}
	/**
	 * @param designationName The designationName to set.
	 */
	public void setDesignationName(String designationName) {
		this.designationName = designationName;
	}
	/**
	 * @return Returns the officerLevel.
	 */
	public String getOfficerLevel() {
		return officerLevel;
	}
	/**
	 * @param officerLevel The officerLevel to set.
	 */
	public void setOfficerLevel(String officerLevel) {
		this.officerLevel = officerLevel;
	}

	public String[] getOutsourcedPosts() {
		return outsourcedPosts;
	}
	public void setOutsourcedPosts(String[] outsourcedPosts) {
		this.outsourcedPosts = outsourcedPosts;
	}
	public String getReportsTo() {
		return reportsTo;
	}
	public void setReportsTo(String reportsTo) {
		this.reportsTo = reportsTo;
	}
	public String[] getSanctionedPosts() {
		return sanctionedPosts;
	}
	public void setSanctionedPosts(String[] sanctionedPosts) {
		this.sanctionedPosts = sanctionedPosts;
	}
	public String getOutsourcedPostsDesig() {
		return outsourcedPostsDesig;
	}
	public void setOutsourcedPostsDesig(String outsourcedPostsDesig) {
		this.outsourcedPostsDesig = outsourcedPostsDesig;
	}
	public String getSanctionedPostsDesig() {
		return sanctionedPostsDesig;
	}
	public void setSanctionedPostsDesig(String sanctionedPostsDesig) {
		this.sanctionedPostsDesig = sanctionedPostsDesig;
	}
	public String[] getDrawingOfficer() {
		return drawingOfficer;
	}
	public void setDrawingOfficer(String[] drawingOfficer) {
		this.drawingOfficer = drawingOfficer;
	}
	public String[] getBillNumberId() {
		return billNumberId;
	}
	public void setBillNumberId(String[] billNumberId) {
		this.billNumberId = billNumberId;
	}	
}