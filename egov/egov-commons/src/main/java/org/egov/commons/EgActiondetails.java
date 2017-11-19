/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2017  eGovernments Foundation
 *
 *     The updated version of eGov suite of products as by eGovernments Foundation
 *     is available at http://www.egovernments.org
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program. If not, see http://www.gnu.org/licenses/ or
 *     http://www.gnu.org/licenses/gpl.html .
 *
 *     In addition to the terms of the GPL license to be adhered to in using this
 *     program, the following additional terms are to be complied with:
 *
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
 *            Further, all user interfaces, including but not limited to citizen facing interfaces,
 *            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
 *            derived works should carry eGovernments Foundation logo on the top right corner.
 *
 *            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
 *            For any further queries on attribution, including queries on brand guidelines,
 *            please contact contact@egovernments.org
 *
 *         2) Any misrepresentation of the origin of the material is prohibited. It
 *            is required that all modified versions of this material be marked in
 *            reasonable ways as different from the original version.
 *
 *         3) This license does not grant any rights to any user of the program
 *            with regards to rights under trademark law for use of the trade names
 *            or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 *
 */
package org.egov.commons;

import org.egov.infra.admin.master.entity.User;

import java.util.Date;

public class EgActiondetails implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	private Integer id;

	private String moduletype;

	private Integer moduleid;

	private User actionDoneBy;

	private Date actionDoneOn;

	private String comments;

	private Integer createdby;

	private Date lastmodifieddate;

	private String actiontype;

	/**
	 * @return Returns the actionDoneBy.
	 */
	public User getActionDoneBy() {
		return actionDoneBy;
	}

	/**
	 * @param actionDoneBy The actionDoneBy to set.
	 */
	public void setActionDoneBy(User actionDoneBy) {
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
