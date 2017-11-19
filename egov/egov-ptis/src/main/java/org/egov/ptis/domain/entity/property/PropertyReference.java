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

import org.egov.exceptions.InvalidPropertyException;

import java.util.Date;

/**
 * @author Administrator TODO To change the template for this generated type
 *         comment go to Window - Preferences - Java - Code Style - Code
 *         Templates
 */

public class PropertyReference {

	private Integer idPropertyRef = null;
	private String fileNo = null;
	private Date fileDate = null;
	private String surveyNo = null;
	private BasicProperty basicProperty = null;
	private Character isHistory = null;

	/**
	 * @return Returns the basicProperty.
	 */
	public BasicProperty getBasicProperty() {
		return basicProperty;
	}

	/**
	 * @param basicProperty
	 *            The basicProperty to set.
	 */
	public void setBasicProperty(BasicProperty basicProperty) {
		this.basicProperty = basicProperty;
	}

	/**
	 * @return Returns the fileDate.
	 */
	public Date getFileDate() {
		return fileDate;
	}

	/**
	 * @param fileDate
	 *            The fileDate to set.
	 */
	public void setFileDate(Date fileDate) {
		this.fileDate = fileDate;
	}

	/**
	 * @return Returns the fileNo.
	 */
	public String getFileNo() {
		return fileNo;
	}

	/**
	 * @param fileNo
	 *            The fileNo to set.
	 */
	public void setFileNo(String fileNo) {
		this.fileNo = fileNo;
	}

	/**
	 * @return Returns the idPropertyRef.
	 */
	public Integer getIdPropertyRef() {
		return idPropertyRef;
	}

	/**
	 * @param idPropertyRef
	 *            The idPropertyRef to set.
	 */
	public void setIdPropertyRef(Integer idPropertyRef) {
		this.idPropertyRef = idPropertyRef;
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
	 * @return Returns the surveyNo.
	 */
	public String getSurveyNo() {
		return surveyNo;
	}

	/**
	 * @param surveyNo
	 *            The surveyNo to set.
	 */
	public void setSurveyNo(String surveyNo) {
		this.surveyNo = surveyNo;
	}

	/**
	 * @return Returns the boolean after validating the current object
	 */
	public boolean validate() throws InvalidPropertyException {
		if (getFileNo() == null && getFileDate() == null) {
			throw new InvalidPropertyException(
					"PropertyReference.validate : FileNo and FileDate  not Set, Please Check !!");
		}
		return true;
	}

	@Override
	public String toString() {
		StringBuilder objStr = new StringBuilder();

		objStr.append("Id: ").append(getIdPropertyRef()).append("|FileNo: ").append(getFileNo()).append("|FileDate: ")
				.append(getFileDate()).append("|SurveyNo: ").append(getSurveyNo()).append("|BasicProperty: ");
		objStr = (getBasicProperty() != null) ? objStr.append(getBasicProperty().getUpicNo()) : objStr.append("NULL");
		objStr.append("|isHistory: ").append(getIsHistory());

		return objStr.toString();
	}
}
