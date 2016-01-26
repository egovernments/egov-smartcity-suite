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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.egov.infra.persistence.entity.AbstractAuditable;

@Entity
@Table(name="FINANCIALYEAR")
@SequenceGenerator(name = CFinancialYear.SEQ_CFINANCIALYEAR, sequenceName = CFinancialYear.SEQ_CFINANCIALYEAR, allocationSize = 1)
public class CFinancialYear extends AbstractAuditable{

	private static final long serialVersionUID = 4577944445258780977L;
	public static final String SEQ_CFINANCIALYEAR = "SEQ_CFINANCIALYEAR";
	
	@Id
	@GeneratedValue(generator=SEQ_CFINANCIALYEAR, strategy=GenerationType.SEQUENCE)
	private Long id = null;
	
	@Column(name="FINANCIALYEAR")
	private String finYearRange = "";
	
	private Date startingDate;
	private Date endingDate;
	private Integer isActive = 1;
	private Integer isActiveForPosting = 0;
	private Integer isClosed = 0;
	private Integer transferClosingBalance = 0;
	
	/**
	 * @return Returns the endingDate.
	 */
	public Date getEndingDate() {
		return endingDate;
	}

    /**
     * @param endingDate The endingDate to set.
     */
    public void setEndingDate(final Date endingDate) {
        this.endingDate = endingDate;
    }

    /**
     * @return Returns the finYearRange.
     */
    public String getFinYearRange() {
        return finYearRange;
    }

    /**
     * @param finYearRange The finYearRange to set.
     */
    public void setFinYearRange(final String finYearRange) {
        this.finYearRange = finYearRange;
    }

    /**
     * @return Returns the id.
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id The id to set.
     */
    public void setId(final Long id) {
        this.id = id;
    }

    /**
     * @return Returns the isActive.
     */
    public Integer getIsActive() {
        return isActive;
    }

    /**
     * @param isActive The isActive to set.
     */
    public void setIsActive(final Integer isActive) {
        this.isActive = isActive;
    }

    /**
     * @return Returns the isActiveForPosting.
     */
    public Integer getIsActiveForPosting() {
        return isActiveForPosting;
    }

    /**
     * @param isActiveForPosting The isActiveForPosting to set.
     */
    public void setIsActiveForPosting(final Integer isActiveForPosting) {
        this.isActiveForPosting = isActiveForPosting;
    }

    /**
     * @return Returns the isClosed.
     */
    public Integer getIsClosed() {
        return isClosed;
    }

	/**
	 * @param isClosed The isClosed to set.
	 */
	public void setIsClosed(Integer isClosed) {
		this.isClosed = isClosed;
	}

    /**
     * @return Returns the startingDate.
     */
    public Date getStartingDate() {
        return startingDate;
    }

    /**
     * @param startingDate The startingDate to set.
     */
    public void setStartingDate(final Date startingDate) {
        this.startingDate = startingDate;
    }

    /**
     * @return Returns the transferClosingBalance.
     */
    public Integer getTransferClosingBalance() {
        return transferClosingBalance;
    }

    /**
     * @param transferClosingBalance The transferClosingBalance to set.
     */
    public void setTransferClosingBalance(final Integer transferClosingBalance) {
        this.transferClosingBalance = transferClosingBalance;
    }
}
