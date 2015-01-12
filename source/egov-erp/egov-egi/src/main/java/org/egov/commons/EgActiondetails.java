/*
 * @(#)EgActiondetails.java 3.0, 6 Jun, 2013 3:19:05 PM Copyright 2013 eGovernments Foundation. All rights reserved. eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.commons;

import java.util.Date;

import org.egov.lib.rjbac.user.UserImpl;

public class EgActiondetails implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	private Integer id;

	private String moduletype;

	private Integer moduleid;

	private UserImpl actionDoneBy;

	private Date actionDoneOn;

	private String comments;

	private Integer createdby;

	private Date lastmodifieddate;

	private String actiontype;

	/**
	 * @return Returns the actionDoneBy.
	 */
	public UserImpl getActionDoneBy() {
		return actionDoneBy;
	}

	/**
	 * @param actionDoneBy The actionDoneBy to set.
	 */
	public void setActionDoneBy(UserImpl actionDoneBy) {
		this.actionDoneBy = actionDoneBy;
	}

	/**
	 * @return Returns the actionDoneOn.
	 */
	public Date getActionDoneOn() {
		return actionDoneOn;
	}

	/**
	 * @param actionDoneOn The actionDoneOn to set.
	 */
	public void setActionDoneOn(Date actionDoneOn) {
		this.actionDoneOn = actionDoneOn;
	}

	/**
	 * @return Returns the actiontype.
	 */
	public String getActiontype() {
		return actiontype;
	}

	/**
	 * @param actiontype The actiontype to set.
	 */
	public void setActiontype(String actiontype) {
		this.actiontype = actiontype;
	}

	/**
	 * @return Returns the comments.
	 */
	public String getComments() {
		return comments;
	}

	/**
	 * @param comments The comments to set.
	 */
	public void setComments(String comments) {
		this.comments = comments;
	}

	/**
	 * @return Returns the createdby.
	 */
	public Integer getCreatedby() {
		return createdby;
	}

	/**
	 * @param createdby The createdby to set.
	 */
	public void setCreatedby(Integer createdby) {
		this.createdby = createdby;
	}

	/**
	 * @return Returns the id.
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @param id The id to set.
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * @return Returns the lastmodifieddate.
	 */
	public Date getLastmodifieddate() {
		return lastmodifieddate;
	}

	/**
	 * @param lastmodifieddate The lastmodifieddate to set.
	 */
	public void setLastmodifieddate(Date lastmodifieddate) {
		this.lastmodifieddate = lastmodifieddate;
	}

	/**
	 * @return Returns the moduleid.
	 */
	public Integer getModuleid() {
		return moduleid;
	}

	/**
	 * @param moduleid The moduleid to set.
	 */
	public void setModuleid(Integer moduleid) {
		this.moduleid = moduleid;
	}

	/**
	 * @return Returns the moduletype.
	 */
	public String getModuletype() {
		return moduletype;
	}

	/**
	 * @param moduletype The moduletype to set.
	 */
	public void setModuletype(String moduletype) {
		this.moduletype = moduletype;
	}
}
