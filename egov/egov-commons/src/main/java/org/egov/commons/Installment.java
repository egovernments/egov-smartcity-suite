/**
 * eGov suite of products aim to improve the internal efficiency,transparency, 
   accountability and the service delivery of the government  organizations.

    Copyright (C) <2015>  eGovernments Foundation

    The updated version of eGov suite of products as by eGovernments Foundation 
    is available at http://www.egovernments.org

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program. If not, see http://www.gnu.org/licenses/ or 
    http://www.gnu.org/licenses/gpl.html .

    In addition to the terms of the GPL license to be adhered to in using this
    program, the following additional terms are to be complied with:

	1) All versions of this program, verbatim or modified must carry this 
	   Legal Notice.

	2) Any misrepresentation of the origin of the material is prohibited. It 
	   is required that all modified versions of this material be marked in 
	   reasonable ways as different from the original version.

	3) This license does not grant any rights to any user of the program 
	   with regards to rights under trademark law for use of the trade names 
	   or trademarks of eGovernments Foundation.

  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.commons;

import java.util.Date;

import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infra.admin.master.entity.Module;

/**
 * This class represents an Installment. Every installment has an year, 
 * it should start from the current financial year, and it is assumed that it spans 
 * untill the end of the current financial year,which falls in the next year. 
 * There can be one or more installments in a year, each representing a particular period in that year.
 */
public class Installment implements Comparable<Installment> {

	private Integer id;
	private java.util.Date fromDate;

	private java.util.Date toDate;
	private Module module;

	private Integer installmentNumber;

	private String description;

	private Date installmentYear;

	private Date lastUpdatedTimeStamp;

	private String installmentType;

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
	 * @return Returns the installmentNumber.
	 */
	public Integer getInstallmentNumber() {
		return installmentNumber;
	}

	/**
	 * @param installmentNumber The installmentNumber to set.
	 */
	public void setInstallmentNumber(Integer installmentNumber) {
		this.installmentNumber = installmentNumber;
	}

	/**
	 * @return Returns the installmentYear.
	 */
	public Date getInstallmentYear() {
		return installmentYear;
	}

	/**
	 * @param installmentYear The installmentYear to set.
	 */
	public void setInstallmentYear(Date installmentYear) {
		this.installmentYear = installmentYear;
	}

	/**
	 * @return Returns the fromDate.
	 */
	public java.util.Date getFromDate() {
		return fromDate;
	}

	/**
	 * @param fromDate The fromDate to set.
	 */
	public void setFromDate(java.util.Date fromDate) {
		this.fromDate = fromDate;
	}

	/**
	 * @return Returns the module.
	 */
	public Module getModule() {
		return module;
	}

	/**
	 * @param module The module to set.
	 */
	public void setModule(Module module) {
		this.module = module;
	}

	/**
	 * @return Returns the toDate.
	 */
	public java.util.Date getToDate() {
		return toDate;
	}

	/**
	 * @param toDate The toDate to set.
	 */
	public void setToDate(java.util.Date toDate) {
		this.toDate = toDate;
	}

	/**
	 * @return Returns the lastUpdatedTimeStamp.
	 */
	public Date getLastUpdatedTimeStamp() {
		return lastUpdatedTimeStamp;
	}

	/**
	 * @param lastUpdatedTimeStamp The lastUpdatedTimeStamp to set.
	 */
	public void setLastUpdatedTimeStamp(Date lastUpdatedTimeStamp) {
		this.lastUpdatedTimeStamp = lastUpdatedTimeStamp;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	public boolean validate() {
		if (fromDate == null || toDate == null)
			throw new EGOVRuntimeException("From Date or To Date is null in installemnt.");

		if (fromDate.compareTo(toDate) >= 0)
			throw new EGOVRuntimeException("From Date greater than or equal to 'ToDate' in installemnt.");

		if (module == null)
			throw new EGOVRuntimeException("Module not specified in installemnt.");

		if (installmentYear == null)
			throw new EGOVRuntimeException("Installment year not specified in installemnt.");

		if (installmentNumber == 0)
			throw new EGOVRuntimeException("Installment Number cannot be zero in a installemnt.");

		return true;
	}

	public boolean equals(Object obj) {
		if (obj == null)
			return false;

		if (!(obj instanceof Installment))
			return false;

		Installment inst = (Installment) obj;

		if (this == inst)
			return true;

		if (this.getId() != null && inst.getId() != null) {
			if (this.getId().equals(inst.getId()))
				return true;
			else
				return false;

		}

		if (this.fromDate == null || this.toDate == null || this.installmentNumber == null || this.installmentYear == null || this.module == null)
			return false;

		if (inst.fromDate == null || inst.toDate == null || inst.installmentNumber == null || inst.installmentYear == null || inst.module == null)
			return false;

		if (this.fromDate.equals(inst.fromDate) && this.toDate.equals(inst.toDate) && this.installmentNumber.equals(inst.installmentNumber) && this.installmentYear.equals(inst.installmentYear) && this.module.equals(inst.module))
			return true;
		else
			return false;
	}

	public int hashCode() {
		int hashcode = 0;
		if (this.fromDate != null)
			hashcode = this.fromDate.hashCode();

		if (this.toDate != null)
			hashcode = this.toDate.hashCode();

		if (this.installmentNumber != null)
			hashcode = this.installmentNumber.hashCode();

		if (this.installmentYear != null)
			hashcode = this.installmentYear.hashCode();

		if (this.module != null)
			hashcode = this.module.hashCode();

		return hashcode;
	}

	@Override
	public int compareTo(Installment inst) {
		Date date;
		if (inst == null) {
			return 1;
		} else {
			date = inst.getFromDate();
			if (this.fromDate.compareTo(date) > 0) // greater
			{
				return 1;
			} else if (this.fromDate.compareTo(date) < 0) // less
			{
				return -1;
			} else {
				return 0; // same
			}
		}
	}

	@Override
	public String toString() {
		return description;
	}

	public String getInstallmentType() {
		return installmentType;
	}

	public void setInstallmentType(String installmentType) {
		this.installmentType = installmentType;
	}
}
