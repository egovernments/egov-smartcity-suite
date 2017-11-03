/*
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

import org.egov.commons.CFinancialYear;
import org.egov.commons.EgwStatus;
import org.egov.infra.persistence.validator.annotation.Required;
import org.egov.infra.persistence.validator.annotation.Unique;
import org.egov.infra.workflow.entity.State;
import org.egov.infra.workflow.entity.StateAware;
import org.egov.pims.commons.Position;
import org.hibernate.validator.constraints.Length;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;

import static org.egov.model.budget.Budget.SEQ_BUDGET;

@Entity
@Table(name = "EGF_BUDGET")
@SequenceGenerator(name = SEQ_BUDGET, sequenceName = SEQ_BUDGET, allocationSize = 1)
@Unique(fields = "name", enableDfltMsg = true)
public class Budget extends StateAware<Position> {

    public static final String SEQ_BUDGET = "SEQ_EGF_BUDGET";
    private static final long serialVersionUID = 3592259793739732756L;
    @Id
    @GeneratedValue(generator = SEQ_BUDGET, strategy = GenerationType.SEQUENCE)
    private Long id;

    @Required(message = "Name should not be empty")
    @Length(max = 250, message = "Max 250 characters are allowed for description")
    private String name;

    private String isbere;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FINANCIALYEARID")
    private CFinancialYear financialYear;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "parent")
    private Budget parent;

    @Length(max = 250, message = "Max 250 characters are allowed for description")
    private String description;

    @Column(name = "AS_ON_DATE")
    private Date asOnDate;

    private boolean isActiveBudget;

    private boolean isPrimaryBudget;

    @Length(max = 10, message = "Max 10 characters are allowed for description")
    private String materializedPath;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "reference_budget")
    private Budget referenceBudget;

    @Column(name = "DOCUMENT_NUMBER")
    private Long documentNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "STATUS")
    private EgwStatus status;

    @Transient
    private String searchBere;


    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(final Long id) {
        this.id = id;
    }

    public Budget getParent() {
        return parent;
    }

    public void setParent(final Budget parent) {
        this.parent = parent;
    }

    public Date getAsOnDate() {
        return asOnDate;
    }

    public void setAsOnDate(final Date asOnDate) {
        this.asOnDate = asOnDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String desc) {
        description = desc;
    }

    @Required(message = "Financial Year is required")
    public CFinancialYear getFinancialYear() {
        return financialYear;
    }

    public void setFinancialYear(final CFinancialYear finYear) {
        financialYear = finYear;
    }

    @Required(message = "Name should not be empty")
    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    /**
     * @return isbere
     */
    @Required(message = "BE/RE is required")
    public String getIsbere() {
        if (isbere == null)
            isbere = "BE";
        return isbere;
    }

    /**
     * @param isbere the isbere to set
     */
    public void setIsbere(final String isbere) {
        this.isbere = isbere;
    }

    /**
     * @return isActiveBudget
     */
    public boolean getIsActiveBudget() {
        return isActiveBudget;
    }

    /**
     * @param isActiveBudget the isActiveBudget to set
     */
    public void setIsActiveBudget(final boolean isActiveBudget) {
        this.isActiveBudget = isActiveBudget;
    }

    /**
     * @return isPrimaryBudget
     */
    public boolean getIsPrimaryBudget() {
        return isPrimaryBudget;
    }

    /**
     * @param isPrimaryBudget the isPrimaryBudget to set
     */
    public void setIsPrimaryBudget(final boolean isPrimaryBudget) {
        this.isPrimaryBudget = isPrimaryBudget;
    }

    @Override
    public String getStateDetails() {
        return name;
    }

    /**
     * @return the materialized_path
     */
    public String getMaterializedPath() {
        return materializedPath;
    }

    /**
     * @param materialized_path the materialized_path to set
     */
    public void setMaterializedPath(final String materializedPath) {
        this.materializedPath = materializedPath;
    }

    public Budget getReferenceBudget() {
        return referenceBudget;
    }

    public void setReferenceBudget(final Budget reference) {
        referenceBudget = reference;
    }

    public Long getDocumentNumber() {
        return documentNumber;
    }

    public void setDocumentNumber(final Long documentNumber) {
        this.documentNumber = documentNumber;
    }

    @Override
    public String myLinkId() {
        return getId().toString();
    }

    public EgwStatus getStatus() {
        return status;
    }

    public void setStatus(EgwStatus status) {
        this.status = status;
    }

    public void setWfState(State state) {
        //Won't work
    }

    public String getSearchBere() {
        return searchBere;
    }

    public void setSearchBere(String searchBere) {
        this.searchBere = searchBere;
    }

}
