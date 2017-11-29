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
package org.egov.model.budget;

import java.sql.Timestamp;

/**
 * @author eGov Model class for BudgetUsage
 */
public class BudgetUsage {
    public BudgetUsage()
    {
        super();
    }

    private Long id;
    private Integer financialYearId;
    private Integer moduleId;
    private String referenceNumber;
    private Double consumedAmount;
    private Double releasedAmount;
    private Timestamp updatedTime;
    private Integer createdby;
    private BudgetDetail budgetDetail;
    private String appropriationnumber;

    /**
     * @return id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id to set
     */
    public void setId(final Long id) {
        this.id = id;
    }

    /**
     * @return
     */
    public Integer getFinancialYearId() {
        return financialYearId;
    }

    /**
     * @param financialYearid
     */
    public void setFinancialYearId(final Integer financialYearId) {
        this.financialYearId = financialYearId;
    }

    /**
     * @return
     */
    public Integer getModuleId() {
        return moduleId;
    }

    /**
     * @param moduleId
     */
    public void setModuleId(final Integer moduleId) {
        this.moduleId = moduleId;
    }

    /**
     * @return
     */
    public String getReferenceNumber() {
        return referenceNumber;
    }

    /**
     * @param referenceNumber
     */
    public void setReferenceNumber(final String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    /**
     * @return
     */
    public Double getConsumedAmount() {
        return consumedAmount;
    }

    /**
     * @param consumedAmount
     */
    public void setConsumedAmount(final Double consumedAmount) {
        this.consumedAmount = consumedAmount;
    }

    /**
     * @return
     */
    public Double getReleasedAmount() {
        return releasedAmount;
    }

    /**
     * @param releasedAmount
     */
    public void setReleasedAmount(final Double releasedAmount) {
        this.releasedAmount = releasedAmount;
    }

    /**
     * @return
     */
    public Timestamp getUpdatedTime() {
        return updatedTime;
    }

    /**
     * @param updatedTime
     */
    public void setUpdatedTime(final Timestamp updatedTime) {
        this.updatedTime = updatedTime;
    }

    /**
     * @return
     */
    public Integer getCreatedby() {
        return createdby;
    }

    /**
     * @param createdby
     */
    public void setCreatedby(final Integer createdby) {
        this.createdby = createdby;
    }

    /**
     * @return budgetDetail
     */
    public BudgetDetail getBudgetDetail() {
        return budgetDetail;
    }

    /**
     * @param budgetDetail the budgetDetail to set
     */
    public void setBudgetDetail(final BudgetDetail budgetDetail) {
        this.budgetDetail = budgetDetail;
    }

    public String getAppropriationnumber() {
        return appropriationnumber;
    }

    public void setAppropriationnumber(final String appropriationnumber) {
        this.appropriationnumber = appropriationnumber;
    }

}
