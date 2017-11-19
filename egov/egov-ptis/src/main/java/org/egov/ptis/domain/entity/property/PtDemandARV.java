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



package org.egov.ptis.domain.entity.property;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author Suhasini.CH TODO To change the template for this generated type
 *         comment go to Window - Preferences - Java - Code Style - Code
 *         Templates
 */

public class PtDemandARV {
	private Integer id = null;
	private BigDecimal arv = null;
	private Date fromDate = null;
	private String type = null;
	private Date lastUpdatedTimeStamp = null;
	private Date createTimeStamp = null;
	private Property property = null;
	private Date toDate = null;
	private Integer reasonId = null;
	private String section72No = null;
	private String aoNumber = null;
	private Date aoDate = null;
	private String amalgamatedPid = null;
	private String netRateOfTax = null;
	private Character isHistory = null;
	private Integer userId = null;
	private Date notice72Date = null;

	/**
	 * @return Returns the userId.
	 */
	public Integer getUserId() {
		return userId;
	}

	/**
	 * @param userId
	 *            The userId to set.
	 */
	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	/**
	 * @return Returns the arv.
	 */
	public BigDecimal getArv() {
		return arv;
	}

	/**
	 * @param arv
	 *            The arv to set.
	 */
	public void setArv(BigDecimal arv) {
		this.arv = arv;
	}

	/**
	 * @return Returns the createTimeStamp.
	 */
	public Date getCreateTimeStamp() {
		return createTimeStamp;
	}

	/**
	 * @param createTimeStamp
	 *            The createTimeStamp to set.
	 */
	public void setCreateTimeStamp(Date createTimeStamp) {
		this.createTimeStamp = createTimeStamp;
	}

	/**
	 * @return Returns the fromDate.
	 */
	public Date getFromDate() {
		return fromDate;
	}

	/**
	 * @param fromDate
	 *            The fromDate to set.
	 */
	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	/**
	 * @return Returns the toDate.
	 */
	public Date getToDate() {
		return toDate;
	}

	/**
	 * @param toDate
	 *            The toDate to set.
	 */
	public void setToDate(Date toDate) {
		this.toDate = toDate;
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
	 * @return Returns the property.
	 */
	public Property getProperty() {
		return property;
	}

	/**
	 * @param property
	 *            The property to set.
	 */
	public void setProperty(Property property) {
		this.property = property;
	}

	/**
	 * @return Returns the type.
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type
	 *            The type to set.
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return Returns the amalgamatedPid.
	 */
	public String getAmalgamatedPid() {
		return amalgamatedPid;
	}

	/**
	 * @param amalgamatedPid
	 *            The amalgamatedPid to set.
	 */
	public void setAmalgamatedPid(String amalgamatedPid) {
		this.amalgamatedPid = amalgamatedPid;
	}

	/**
	 * @return Returns the aoDate.
	 */
	public Date getAoDate() {
		return aoDate;
	}

	/**
	 * @param aoDate
	 *            The aoDate to set.
	 */
	public void setAoDate(Date aoDate) {
		this.aoDate = aoDate;
	}

	/**
	 * @return Returns the aoNumber.
	 */
	public String getAoNumber() {
		return aoNumber;
	}

	/**
	 * @param aoNumber
	 *            The aoNumber to set.
	 */
	public void setAoNumber(String aoNumber) {
		this.aoNumber = aoNumber;
	}

	/**
	 * @return Returns the reasonId.
	 */
	public Integer getReasonId() {
		return reasonId;
	}

	/**
	 * @param reasonId
	 *            The reasonId to set.
	 */
	public void setReasonId(Integer reasonId) {
		this.reasonId = reasonId;
	}

	/**
	 * @return Returns the section72No.
	 */
	public String getSection72No() {
		return section72No;
	}

	/**
	 * @param section72No
	 *            The section72No to set.
	 */
	public void setSection72No(String section72No) {
		this.section72No = section72No;
	}

	/**
	 * @return Returns the netRateOfTax.
	 */
	public String getNetRateOfTax() {
		return netRateOfTax;
	}

	/**
	 * @param netRateOfTax
	 *            The netRateOfTax to set.
	 */
	public void setNetRateOfTax(String netRateOfTax) {
		this.netRateOfTax = netRateOfTax;
	}

	/**
	 * @return Returns the isHistory.
	 */
	public Character getIsHistory() {
		return isHistory;
	}

	/**
	 * @param isHistory
	 *            The isHistory to set.
	 */
	public void setIsHistory(Character isHistory) {
		this.isHistory = isHistory;
	}

	/**
	 * @return Returns the notice72Date.
	 */
	public Date getNotice72Date() {
		return notice72Date;
	}

	/**
	 * @param notice72Date
	 *            The notice72Date to set.
	 */
	public void setNotice72Date(Date notice72Date) {
		this.notice72Date = notice72Date;
	}

	@Override
	public String toString() {
		StringBuilder objStr = new StringBuilder();

		objStr.append("Id: ").append(getId()).append("|").append("ARV: ").append(getArv()).append("Type: ").append(
				getType());

		return objStr.toString();
	}
}
