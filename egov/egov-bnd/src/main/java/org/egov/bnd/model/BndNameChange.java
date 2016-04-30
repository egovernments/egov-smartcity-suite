/*******************************************************************************
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2015>  eGovernments Foundation
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
 * 	1) All versions of this program, verbatim or modified must carry this
 * 	   Legal Notice.
 *
 * 	2) Any misrepresentation of the origin of the material is prohibited. It
 * 	   is required that all modified versions of this material be marked in
 * 	   reasonable ways as different from the original version.
 *
 * 	3) This license does not grant any rights to any user of the program
 * 	   with regards to rights under trademark law for use of the trade names
 * 	   or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 ******************************************************************************/
package org.egov.bnd.model;

import org.apache.log4j.Logger;
import org.egov.infra.admin.master.entity.User;
import org.egov.portal.entity.Citizen;

import java.util.Date;

public class BndNameChange {

	private Integer id;
	private String firstName;
	private String middleName;
	private String lastName;
	private String oldfirstname;
	private String oldmiddlename;
	private String oldlastname;
	private String remarks;
	private User lastModifiedBy;
	private Date lastUpatedTimestamp;	
	// private BnDCitizen citizen;
	private Citizen citizen;
	private String receiptNo;

	protected final Logger logger = Logger.getLogger(getClass().getName());

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(final String firstName) {
		this.firstName = firstName;
	}

	public Integer getId() {
		return id;
	}

	public void setId(final Integer id) {
		this.id = id;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(final String lastName) {
		this.lastName = lastName;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(final String middleName) {
		this.middleName = middleName;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(final String remarks) {
		this.remarks = remarks;
	}

	public Citizen getCitizen() {
		return citizen;
	}

	public void setCitizen(final Citizen citizen) {
		this.citizen = citizen;
	}

	public String getReceiptNo() {
		return receiptNo;
	}

	public void setReceiptNo(final String receiptNo) {
		this.receiptNo = receiptNo;
	}

	public String getOldfirstname() {
		return oldfirstname;
	}

	public void setOldfirstname(final String oldfirstname) {
		this.oldfirstname = oldfirstname;
	}

	public String getOldlastname() {
		return oldlastname;
	}

	public void setOldlastname(final String oldlastname) {
		this.oldlastname = oldlastname;
	}

	public String getOldmiddlename() {
		return oldmiddlename;
	}

	public Date getLastUpatedTimestamp() {
		return lastUpatedTimestamp;
	}

	public void setLastUpatedTimestamp(final Date lastUpatedTimestamp) {
		this.lastUpatedTimestamp = lastUpatedTimestamp;
	}

	public void setOldmiddlename(final String oldmiddlename) {
		this.oldmiddlename = oldmiddlename;
	}

	public User getLastModifiedBy() {
		return lastModifiedBy;
	}

	public void setLastModifiedBy(final User lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}

}
