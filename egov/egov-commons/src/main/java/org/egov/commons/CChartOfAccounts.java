/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 * accountability and the service delivery of the government  organizations.
 *
 *  Copyright (C) 2017  eGovernments Foundation
 *
 *  The updated version of eGov suite of products as by eGovernments Foundation
 *  is available at http://www.egovernments.org
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program. If not, see http://www.gnu.org/licenses/ or
 *  http://www.gnu.org/licenses/gpl.html .
 *
 *  In addition to the terms of the GPL license to be adhered to in using this
 *  program, the following additional terms are to be complied with:
 *
 *      1) All versions of this program, verbatim or modified must carry this
 *         Legal Notice.
 *
 *      2) Any misrepresentation of the origin of the material is prohibited. It
 *         is required that all modified versions of this material be marked in
 *         reasonable ways as different from the original version.
 *
 *      3) This license does not grant any rights to any user of the program
 *         with regards to rights under trademark law for use of the trade names
 *         or trademarks of eGovernments Foundation.
 *
 *  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */

package org.egov.commons;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.egov.infra.persistence.entity.AbstractAuditable;
import org.hibernate.envers.AuditJoinTable;
import org.hibernate.envers.AuditOverride;
import org.hibernate.envers.AuditOverrides;
import org.hibernate.envers.Audited;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.HashSet;
import java.util.Set;

import static org.egov.commons.CChartOfAccounts.SEQ_CHARTOFACCOUNTS;

@Entity
@Table(name = "CHARTOFACCOUNTS")
@SequenceGenerator(name = SEQ_CHARTOFACCOUNTS, sequenceName = SEQ_CHARTOFACCOUNTS, allocationSize = 1)
@AuditOverrides({
        @AuditOverride(forClass = AbstractAuditable.class, name = "lastModifiedBy"),
        @AuditOverride(forClass = AbstractAuditable.class, name = "lastModifiedDate")
})
@Audited
public class CChartOfAccounts extends AbstractAuditable {

    public static final String SEQ_CHARTOFACCOUNTS = "SEQ_CHARTOFACCOUNTS";
    private static final long serialVersionUID = 61219209022946300L;
    @Id
    @GeneratedValue(generator = SEQ_CHARTOFACCOUNTS, strategy = GenerationType.SEQUENCE)
    private Long id;

    private String glcode;

    private String name;

    private Long purposeId;

    @Column(name = "DESCRIPTION")
    private String desc;

    private Boolean isActiveForPosting;

    private Long parentId;

    @Column(name = "SCHEDULEID")
    private Long schedule;

    private Character operation;

    private Character type;

    private Long classification;

    private Boolean functionReqd;

    private Boolean budgetCheckReq;

    private String majorCode;

    @Column(name = "CLASS")
    private Long myClass;

    @Transient
    private Boolean isSubLedger;

    @AuditJoinTable
    @JsonIgnore
    @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "glCodeId", targetEntity = CChartOfAccountDetail.class)
    private Set<CChartOfAccountDetail> chartOfAccountDetails = new HashSet<>();

    public Set<CChartOfAccountDetail> getChartOfAccountDetails() {
        return chartOfAccountDetails;
    }

    public void setChartOfAccountDetails(final Set<CChartOfAccountDetail> chartOfAccountDetail) {
        chartOfAccountDetails = chartOfAccountDetail;
    }

    public String getMajorCode() {
        return majorCode;
    }

    public void setMajorCode(final String majorCode) {
        this.majorCode = majorCode;
    }

    public Long getMyClass() {
        return myClass;
    }

    public void setMyClass(final Long myClass) {
        this.myClass = myClass;
    }

    public String getGlcode() {
        return glcode;
    }

    public void setGlcode(final String glcode) {
        this.glcode = glcode;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public Long getPurposeId() {
        return purposeId;
    }

    public void setPurposeId(final Long purposeId) {
        this.purposeId = purposeId;
    }

    public Long getClassification() {
        return classification;
    }

    public void setClassification(final Long classification) {
        this.classification = classification;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(final String desc) {
        this.desc = desc;
    }

    public Boolean getFunctionReqd() {

        return functionReqd;

    }

    public void setFunctionReqd(final Boolean functionReqd) {
        this.functionReqd = functionReqd;
    }

    public Boolean getIsActiveForPosting() {
        return isActiveForPosting;
    }

    public void setIsActiveForPosting(final Boolean isActiveForPosting) {
        this.isActiveForPosting = isActiveForPosting;
    }

    public Character getOperation() {
        return operation;
    }

    public void setOperation(final Character operation) {
        this.operation = operation;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(final Long parentId) {
        this.parentId = parentId;
    }

    public Long getSchedule() {
        return schedule;
    }

    public void setSchedule(final Long schedule) {
        this.schedule = schedule;
    }

    public Character getType() {
        return type;
    }

    public void setType(final Character type) {
        this.type = type;
    }

    @Override
    public boolean equals(final Object o) {
        return o instanceof CChartOfAccounts && ((CChartOfAccounts) o).getId().equals(getId());
    }

    @Override
    public int hashCode() {
        return Integer.valueOf(glcode != null ? glcode : "0");
    }

    public Boolean getBudgetCheckReq() {
        return budgetCheckReq;
    }

    public void setBudgetCheckReq(final Boolean budgetCheckReq) {
        this.budgetCheckReq = budgetCheckReq;
    }

    public Boolean getIsSubLedger() {
        return isSubLedger;
    }

    public void setIsSubLedger(final Boolean isSubLedger) {
        this.isSubLedger = isSubLedger;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(final Long id) {
        this.id = id;
    }

}
