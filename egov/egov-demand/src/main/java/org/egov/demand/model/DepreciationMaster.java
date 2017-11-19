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

package org.egov.demand.model;

import org.egov.commons.Installment;
import org.egov.infra.admin.master.entity.Module;
import org.egov.infra.admin.master.entity.User;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * 
 * @author Administrator
 * @version 1.00
 * @since 1.00
 */
public class DepreciationMaster implements Serializable {
	private Integer id = null;
	private Integer year = null;
	private Float depreciationPct = null;
	private Date lastUpdatedTimeStamp;
	private Module module = null;
	private char isHistory;
	private User userId = null;
	private Installment startInstallment;
	private String depreciationName;
	private String depreciationType;
	private Date fromDate;
	private Date toDate;

	/**
	 * @return Returns the startInstallment.
	 */
	public Installment getStartInstallment() {
		return startInstallment;
	}

	/**
	 * @param startInstallment
	 *            The startInstallment to set.
	 */
	public void setStartInstallment(Installment startInstallment) {
		this.startInstallment = startInstallment;
	}

	/**
	 * @return Returns the depreciationPct.
	 */
	public Float getDepreciationPct() {
		return depreciationPct;
	}

	/**
	 * @param depreciationPct
	 *            The depreciationPct to set.
	 */
	public void setDepreciationPct(Float depreciationPct) {
		this.depreciationPct = depreciationPct;
	}

	/**
	 * @return Returns the id.
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @param id
	 *            The id to set.
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * @return Returns the lastUpdatedTimeStamp.
	 */
	public Date getLastUpdatedTimeStamp() {
		return lastUpdatedTimeStamp;
	}

	/**
	 * @param lastUpdatedTimeStamp
	 *            The lastUpdatedTimeStamp to set.
	 */
	public void setLastUpdatedTimeStamp(Date lastUpdatedTimeStamp) {
		this.lastUpdatedTimeStamp = lastUpdatedTimeStamp;
	}

	/**
	 * @return Returns the module.
	 */
	public Module getModule() {
		return module;
	}

	/**
	 * @param module
	 *            The module to set.
	 */
	public void setModule(Module module) {
		this.module = module;
	}

	/**
	 * @return Returns the year.
	 */
	public Integer getYear() {
		return year;
	}

	/**
	 * @param year
	 *            The year to set.
	 */
	public void setYear(Integer year) {
		this.year = year;
	}

	/**
	 * @return Returns the isHistory.
	 */
	public char getIsHistory() {
		return isHistory;
	}

	/**
	 * @param isHistory
	 *            The isHistory to set.
	 */
	public void setIsHistory(char isHistory) {
		this.isHistory = isHistory;
	}

	/**
	 * @return Returns the userId.
	 */
	public User getUserId() {
		return userId;
	}

	/**
	 * @param userId
	 *            The userId to set.
	 */
	public void setUserId(User userId) {
		this.userId = userId;
	}

	public String getDepreciationName() {
		return depreciationName;
	}

	public void setDepreciationName(String depreciationName) {
		this.depreciationName = depreciationName;
	}

	public String getDepreciationType() {
		return depreciationType;
	}

	public void setDepreciationType(String depreciationType) {
		this.depreciationType = depreciationType;
	}

	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public Date getToDate() {
		return toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

}
