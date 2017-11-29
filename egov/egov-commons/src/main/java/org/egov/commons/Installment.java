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

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.egov.infra.admin.master.entity.Module;
import org.egov.infra.exception.ApplicationRuntimeException;

import java.io.Serializable;
import java.util.Date;

/**
 * This class represents an Installment. Every installment has an year, it should start from the current financial year, and it is
 * assumed that it spans untill the end of the current financial year,which falls in the next year. There can be one or more
 * installments in a year, each representing a particular period in that year.
 */
public class Installment implements Serializable, Comparable<Installment> {

    /**
    *
    */
    private static final long serialVersionUID = 4862908812359340638L;
    private Integer id;
    private java.util.Date fromDate;

    private java.util.Date toDate;
    private Module module;

    private Integer installmentNumber;

    private String description;

    private Date installmentYear;

    private Date lastUpdatedTimeStamp;

    private String installmentType;

    private String finYearRange;
    
	/**
     * @return Returns the id.
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id The id to set.
     */
    public void setId(final Integer id) {
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
    public void setInstallmentNumber(final Integer installmentNumber) {
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
    public void setInstallmentYear(final Date installmentYear) {
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
    public void setFromDate(final java.util.Date fromDate) {
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
    public void setModule(final Module module) {
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
    public void setToDate(final java.util.Date toDate) {
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
    public void setLastUpdatedTimeStamp(final Date lastUpdatedTimeStamp) {
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
    public void setDescription(final String description) {
        this.description = description;
    }

    public boolean validate() {
        if (fromDate == null || toDate == null)
            throw new ApplicationRuntimeException("From Date or To Date is null in installemnt.");

        if (fromDate.compareTo(toDate) >= 0)
            throw new ApplicationRuntimeException("From Date greater than or equal to 'ToDate' in installemnt.");

        if (module == null)
            throw new ApplicationRuntimeException("Module not specified in installemnt.");

        if (installmentYear == null)
            throw new ApplicationRuntimeException("Installment year not specified in installemnt.");

        if (installmentNumber == 0)
            throw new ApplicationRuntimeException("Installment Number cannot be zero in a installemnt.");

        return true;
    }

    @Override
    public String toString() {
        return description;
    }

    public String getInstallmentType() {
        return installmentType;
    }

    public void setInstallmentType(final String installmentType) {
        this.installmentType = installmentType;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (fromDate == null ? 0 : fromDate.hashCode());
        result = prime * result + (id == null ? 0 : id.hashCode());
        result = prime * result + (installmentNumber == null ? 0 : installmentNumber.hashCode());
        result = prime * result + (installmentYear == null ? 0 : installmentYear.hashCode());
        result = prime * result + (installmentType == null ? 0 : installmentType.hashCode());
        result = prime * result + (module == null ? 0 : module.hashCode());
        result = prime * result + (toDate == null ? 0 : toDate.hashCode());
        return result;
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

		if (this.fromDate == null || this.toDate == null
				|| this.installmentNumber == null
				|| this.installmentYear == null || this.module == null)
			return false;

		if (inst.fromDate == null || inst.toDate == null
				|| inst.installmentNumber == null
				|| inst.installmentYear == null || inst.module == null)
			return false;

		if (this.fromDate.equals(inst.fromDate)
				&& this.toDate.equals(inst.toDate)
				&& this.installmentNumber.equals(inst.installmentNumber)
				&& this.installmentYear.equals(inst.installmentYear)
				&& this.module.equals(inst.module)
				&& ((this.installmentType != null && inst.installmentType != null) && this.installmentType
						.equals(inst.installmentType)))
			return true;
		else
			return false;
	}

    @Override
	public int compareTo(final Installment inst) {
		return new CompareToBuilder().append(fromDate, inst.getFromDate())
				.append(id, inst.getId())
				.append(installmentYear, inst.getInstallmentYear())
				.append(installmentNumber, inst.getInstallmentNumber())
				.append(installmentType, inst.getInstallmentType())
				.append(module.getName(), inst.getModule().getName())
				.append(toDate, inst.getToDate()).build();
	}
    
    public String getFinYearRange() {
		return finYearRange;
	}

	public void setFinYearRange(String finYearRange) {
		this.finYearRange = finYearRange;
	}
}
