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

import org.egov.infra.persistence.entity.AbstractAuditable;
import org.egov.infra.persistence.validator.annotation.Unique;
import org.hibernate.envers.NotAudited;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Entity
@Table(name = "financialyear")
@Unique(id = "id", tableName = "financialyear", fields = { "finYearRange" }, columnName = { "financialyear" }, enableDfltMsg = true)
@SequenceGenerator(name = CFinancialYear.SEQ_CFINANCIALYEAR, sequenceName = CFinancialYear.SEQ_CFINANCIALYEAR, allocationSize = 1)
public class CFinancialYear extends AbstractAuditable {

    private static final long serialVersionUID = -1563670460427134487L;
    public static final String SEQ_CFINANCIALYEAR = "SEQ_FINANCIALYEAR";

    @Id
    @GeneratedValue(generator = SEQ_CFINANCIALYEAR, strategy = GenerationType.SEQUENCE)
    private Long id;

    @Length(min = 1, max = 25)
    @NotBlank
    @Column(name="financialyear")
    private String finYearRange;

  
    @NotNull
    private Date startingDate;

    @NotNull
    private Date endingDate;
    
    private Boolean isActive;
  
    private Boolean isActiveForPosting;
    
    private Boolean isClosed;
 
    private Boolean transferClosingBalance;
    
    @OneToMany(mappedBy = "cFinancialYear", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("id DESC ")
    @NotAudited
    private final List<CFiscalPeriod> cFiscalPeriod = new ArrayList<CFiscalPeriod>(0);

    public Boolean getIsActive() {
        return isActive;
    }

    public Boolean getIsClosed() {
        return isClosed;
    }

    public Boolean getTransferClosingBalance() {
        return transferClosingBalance;
    }

    public void setIsActive(final Boolean isActive) {
        this.isActive = isActive;
    }

    public void setIsClosed(final Boolean isClosed) {
        this.isClosed = isClosed;
    }

    public void setTransferClosingBalance(final Boolean transferClosingBalance) {
        this.transferClosingBalance = transferClosingBalance;
    }

    public String getFinYearRange() {
        return finYearRange;
    }

    public Date getStartingDate() {
        return startingDate;
    }

    public Date getEndingDate() {
        return endingDate;
    }

    public Boolean getIsActiveForPosting() {
        return isActiveForPosting;
    }

    public void setFinYearRange(final String finYearRange) {
        this.finYearRange = finYearRange;
    }

    public void setStartingDate(final Date startingDate) {
        this.startingDate = startingDate;
    }

    public void setEndingDate(final Date endingDate) {
        this.endingDate = endingDate;
    }

    public void setIsActiveForPosting(final Boolean isActiveForPosting) {
        this.isActiveForPosting = isActiveForPosting;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(final Long id) {
        this.id = id;
    }
    

    public List<CFiscalPeriod> getcFiscalPeriod() {
        return cFiscalPeriod;
    }
    

    public void setcFiscalPeriod(final List<CFiscalPeriod> cFiscalPeriod) {
        this.cFiscalPeriod.clear();
        if (cFiscalPeriod != null)
            this.cFiscalPeriod.addAll(cFiscalPeriod);
    }
    
    public void addCFiscalPeriod(final CFiscalPeriod cFiscalPeriod) {
        getcFiscalPeriod().add(cFiscalPeriod);
    }

}
