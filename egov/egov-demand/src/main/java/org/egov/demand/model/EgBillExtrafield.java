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


/**
 * EgBillExtrafield entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class EgBillExtrafield implements java.io.Serializable {

	// Fields

	private Long id;
	private EgBill egBill;
	private String extrafield1;
	private String extrafield2;
	private String extrafield3;
	private String extrafield4;
	private String extrafield5;

	
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public EgBill getEgBill() {
		return this.egBill;
	}

	public void setEgBill(EgBill egBill) {
		this.egBill = egBill;
	}

	public String getExtrafield1() {
		return this.extrafield1;
	}

	public void setExtrafield1(String extrafield1) {
		this.extrafield1 = extrafield1;
	}

	public String getExtrafield2() {
		return this.extrafield2;
	}

	public void setExtrafield2(String extrafield2) {
		this.extrafield2 = extrafield2;
	}

	public String getExtrafield3() {
		return this.extrafield3;
	}

	public void setExtrafield3(String extrafield3) {
		this.extrafield3 = extrafield3;
	}

	public String getExtrafield4() {
		return this.extrafield4;
	}

	public void setExtrafield4(String extrafield4) {
		this.extrafield4 = extrafield4;
	}

	public String getExtrafield5() {
		return this.extrafield5;
	}

	public void setExtrafield5(String extrafield5) {
		this.extrafield5 = extrafield5;
	}

}