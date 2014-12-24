/*
 * PropertyReference.java
 * Created on May 12, 2007
 *
 * Copyright 2006 eGovernments Foundation. All rights reserved.
 * EGOVERNMENTS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.ptis.domain.entity.property;

import java.util.Date;

import org.egov.exceptions.InvalidPropertyException;

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
