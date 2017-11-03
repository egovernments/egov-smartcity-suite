/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2017>  eGovernments Foundation
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
 */
package org.egov.model.budget;

import org.egov.commons.EgwStatus;
import org.egov.infra.workflow.entity.StateAware;
import org.egov.pims.commons.Position;

import java.math.BigDecimal;
import java.util.Date;

public class BudgetReAppropriation extends StateAware<Position> {
    private static final long serialVersionUID = 2343135780753283100L;
    private Long id = null;
    private BudgetDetail budgetDetail;
    private BigDecimal additionAmount = new BigDecimal("0.0");
    private BigDecimal deductionAmount = new BigDecimal("0.0");
    private BigDecimal originalAdditionAmount = new BigDecimal("0.0");
    private BigDecimal originalDeductionAmount = new BigDecimal("0.0");
    private BigDecimal anticipatoryAmount = new BigDecimal("0.0");
    private EgwStatus status;
    private Date asOnDate;
    private BudgetReAppropriationMisc reAppropriationMisc;

    public EgwStatus getStatus() {
        return status;
    }

    public void setStatus(final EgwStatus status) {
        this.status = status;
    }

    public BudgetReAppropriationMisc getReAppropriationMisc() {
        return reAppropriationMisc;
    }

    public void setReAppropriationMisc(final BudgetReAppropriationMisc reAppropriationMisc) {
        this.reAppropriationMisc = reAppropriationMisc;
    }

    public BigDecimal getAnticipatoryAmount() {
        return anticipatoryAmount;
    }

    public void setAnticipatoryAmount(final BigDecimal anticipatoryAmount) {
        this.anticipatoryAmount = anticipatoryAmount;
    }

    public BudgetDetail getBudgetDetail() {
        return budgetDetail;
    }

    public void setBudgetDetail(final BudgetDetail budgetDetail) {
        this.budgetDetail = budgetDetail;
    }

    public BigDecimal getAdditionAmount() {
        return additionAmount;
    }

    public void setAdditionAmount(final BigDecimal additionAmount) {
        this.additionAmount = additionAmount;
    }

    public BigDecimal getDeductionAmount() {
        return deductionAmount;
    }

    public void setDeductionAmount(final BigDecimal deductionAmount) {
        this.deductionAmount = deductionAmount;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(final Long id) {
        this.id = id;
    }

    @Override
    public String getStateDetails() {
        return null;
    }

    public BigDecimal getOriginalAdditionAmount() {
        return originalAdditionAmount;
    }

    public void setOriginalAdditionAmount(final BigDecimal originalAdditionAmount) {
        this.originalAdditionAmount = originalAdditionAmount;
    }

    public BigDecimal getOriginalDeductionAmount() {
        return originalDeductionAmount;
    }

    public void setOriginalDeductionAmount(final BigDecimal originalDeductionAmount) {
        this.originalDeductionAmount = originalDeductionAmount;
    }

    public Date getAsOnDate() {
        return asOnDate;
    }

    public void setAsOnDate(final Date asOnDate) {
        this.asOnDate = asOnDate;
    }

}
