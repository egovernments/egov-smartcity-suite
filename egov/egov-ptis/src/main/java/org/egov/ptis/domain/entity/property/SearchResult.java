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

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * SearchResult is a Data Transfer Object for Property Search functionality.
 *
 * @author Suhasini
 * @version 2.00
 * @see
 * @see
 * @since   2.00
 */

/**
 * @author ramki
 * 
 */
public class SearchResult implements Serializable {
	private String receiptNumber;
	private String folioNumber;
	private String assesseeFullName;
	private String address;
	private String propertyId;
	private String upicNumber;
	private String basicPropertyId;
	private String currYearArv;
	private BigDecimal currDemand;
	private BigDecimal currDemandDue;
	private BigDecimal arrearDue;

	/**
	 * @return Returns the basicPropertyId.
	 */
	public String getBasicPropertyId() {
		return basicPropertyId;
	}

	/**
	 * @param basicPropertyId
	 *            The basicPropertyId to set.
	 */
	public void setBasicPropertyId(String basicPropertyId) {
		this.basicPropertyId = basicPropertyId;
	}

	/**
	 * @return Returns the address.
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * @param address
	 *            The address to set.
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * @return Returns the assesseeFullName.
	 */
	public String getAssesseeFullName() {
		return assesseeFullName;
	}

	/**
	 * @param assesseeFullName
	 *            The assesseeFullName to set.
	 */
	public void setAssesseeFullName(String assesseeFullName) {
		this.assesseeFullName = assesseeFullName;
	}

	/**
	 * @return Returns the folioNumber.
	 */
	public String getFolioNumber() {
		return folioNumber;
	}

	/**
	 * @param folioNumber
	 *            The folioNumber to set.
	 */
	public void setFolioNumber(String folioNumber) {
		this.folioNumber = folioNumber;
	}

	/**
	 * @return Returns the upicNumber.
	 */

	public String getUpicNumber() {
		return upicNumber;
	}

	/**
	 * @param upicNumber
	 *            The upicNumber to set.
	 */
	public void setUpicNumber(String upicNumber) {
		this.upicNumber = upicNumber;
	}

	/**
	 * @param propertyId
	 *            The propertyId to set.
	 */
	public void setPropertyId(String propertyId) {
		this.propertyId = propertyId;
	}

	/**
	 * @return Returns the propertyId.
	 */
	public String getPropertyId() {
		return propertyId;
	}

	/**
	 * @param receiptNumber
	 *            The receiptNumber to set.
	 */
	public void setReceiptNumber(String receiptNumber) {
		this.receiptNumber = receiptNumber;
	}

	/**
	 * @return Returns the currYearArv.
	 */
	public String getCurrYearArv() {
		return currYearArv;
	}

	/**
	 * @param currYearArv
	 *            The currYearArv to set.
	 */
	public void setCurrYearArv(String currYearArv) {
		this.currYearArv = currYearArv;
	}

	/**
	 * @return Returns the receiptNumber.
	 */
	public String getReceiptNumber() {
		return receiptNumber;
	}

	/**
	 * @return Returns the currDemand.
	 */
	public BigDecimal getCurrDemand() {
		return currDemand;
	}

	/**
	 * @param currDemand
	 *            The currDemand to set.
	 */
	public void setCurrDemand(BigDecimal currDemand) {
		this.currDemand = currDemand;
	}

	/**
	 * @return the currDemandDue
	 */
	public BigDecimal getCurrDemandDue() {
		return currDemandDue;
	}

	/**
	 * @param currDemandDue
	 *            the currDemandDue to set
	 */
	public void setCurrDemandDue(BigDecimal currDemandDue) {
		this.currDemandDue = currDemandDue;
	}

	/**
	 * @return the arrearDue
	 */
	public BigDecimal getArrearDue() {
		return arrearDue;
	}

	/**
	 * @param arrearDue
	 *            the arrearDue to set
	 */
	public void setArrearDue(BigDecimal arrearDue) {
		this.arrearDue = arrearDue;
	}

	@Override
	public String toString() {

		StringBuffer objStr = new StringBuffer();

		objStr.append("FolioNo: ").append(getFolioNumber()).append("|AssesseeFullName: ").append(getAssesseeFullName()).append(
				"|Address: ").append(getAddress()).append("|PropertyId: ").append(getPropertyId()).append(
				"|UpicNo: ").append(getUpicNumber()).append("|BasicPropertyId: ").append(getBasicPropertyId()).append(
				"|CurrYearrArv: ").append(getCurrYearArv()).append("|CurrDemand: ").append(getCurrDemand()).append(
				"|CurrDemandDue: ").append(getCurrDemandDue()).append("|ArrearDue: ").append(getArrearDue());

		return objStr.toString();
	}
}
