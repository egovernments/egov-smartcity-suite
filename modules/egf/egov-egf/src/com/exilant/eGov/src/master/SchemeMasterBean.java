/*
 * Created on Jul 19, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.exilant.eGov.src.master;
/**
 * @author Administrator
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class SchemeMasterBean 
{
	private String schemecode;
	private String schemename;
	private String description;
	private String startDate;
	private String endDate;
	private String isActive;
	
	/**
	 * @return Returns the description.
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description The description to set.
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * @return Returns the endDate.
	 */
	public String getEndDate() {
		return endDate;
	}
	/**
	 * @param endDate The endDate to set.
	 */
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	/**
	 * @return Returns the isActive.
	 */
	public String getIsActive() {
		return isActive;
	}
	/**
	 * @param isActive The isActive to set.
	 */
	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}
	/**
	 * @return Returns the schemecode.
	 */
	public String getSchemecode() {
		return schemecode;
	}
	/**
	 * @param schemecode The schemecode to set.
	 */
	public void setSchemecode(String schemecode) {
		this.schemecode = schemecode;
	}
	/**
	 * @return Returns the schemename.
	 */
	public String getSchemename() {
		return schemename;
	}
	/**
	 * @param schemename The schemename to set.
	 */
	public void setSchemename(String schemename) {
		this.schemename = schemename;
	}
	/**
	 * @return Returns the startDate.
	 */
	public String getStartDate() {
		return startDate;
	}
	/**
	 * @param startDate The startDate to set.
	 */
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
}