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
package org.egov.works.masters.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.egov.commons.CChartOfAccounts;
import org.egov.infra.persistence.entity.AbstractAuditable;
import org.egov.infra.persistence.validator.annotation.Unique;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "EGW_OVERHEAD")
@Unique(id = "id", tableName = "EGW_OVERHEAD", columnName = { "name" }, fields = { "name" }, enableDfltMsg = true)
@NamedQueries({
        @NamedQuery(name = Overhead.OVERHEADS_BY_DATE, query = "from Overhead o inner join fetch o.overheadRates as rates where ((? between rates.validity.startDate and rates.validity.endDate ) or (rates.validity.startDate<=? and rates.validity.endDate is null))"),
        @NamedQuery(name = Overhead.BY_DATE_AND_TYPE, query = "from Overhead o inner join fetch o.overheadRates as rates where ((? between rates.validity.startDate and rates.validity.endDate ) or (rates.validity.startDate<=? and rates.validity.endDate is null))") })
@SequenceGenerator(name = Overhead.SEQ_EGW_OVERHEAD, sequenceName = Overhead.SEQ_EGW_OVERHEAD, allocationSize = 1)
public class Overhead extends AbstractAuditable {

    private static final long serialVersionUID = 474905206086516812L;

    public static final String SEQ_EGW_OVERHEAD = "SEQ_EGW_OVERHEAD";
    public static final String BY_DATE_AND_TYPE = "BY_DATE_AND_TYPE";
    public static final String OVERHEADS_BY_DATE = "OVERHEADS_BY_DATE";

    @Id
    @GeneratedValue(generator = SEQ_EGW_OVERHEAD, strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotNull
    private String name;

    @NotNull
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "accountcode", nullable = false)
    private CChartOfAccounts accountCode;

    @JsonIgnore
    @OrderBy("id")
    @OneToMany(mappedBy = "overhead", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, targetEntity = OverheadRate.class)
    private final List<OverheadRate> overheadRates = new ArrayList<OverheadRate>(0);

    @Transient
    private List<OverheadRate> tempOverheadRateValues = new ArrayList<OverheadRate>(0);

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(final Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public CChartOfAccounts getAccountCode() {
        return accountCode;
    }

    public void setAccountCode(final CChartOfAccounts accountCode) {
        this.accountCode = accountCode;
    }

    public List<OverheadRate> getOverheadRates() {
        return overheadRates;
    }

    public List<OverheadRate> getTempOverheadRateValues() {
        return tempOverheadRateValues;
    }

    public void setTempOverheadRateValues(final List<OverheadRate> tempOverheadRateValues) {
        this.tempOverheadRateValues = tempOverheadRateValues;
    }

}