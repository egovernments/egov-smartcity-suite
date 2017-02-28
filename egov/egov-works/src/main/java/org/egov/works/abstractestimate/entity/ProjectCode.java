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
package org.egov.works.abstractestimate.entity;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.egov.commons.EgwStatus;
import org.egov.commons.utils.EntityType;
import org.egov.infra.persistence.entity.AbstractAuditable;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.SafeHtml;

@Entity
@Table(name = "EGW_PROJECTCODE")
@NamedQueries({
        @NamedQuery(name = ProjectCode.ALLACTIVEPROJECTCODES, query = " select distinct pc from ProjectCode as pc left join pc.estimates as e where e.egwStatus.code not in('CANCELLED') order by pc.code "),
        @NamedQuery(name = ProjectCode.GETPROJECTCODEBYCODE, query = " select distinct pc from ProjectCode as pc left join pc.estimates as e where e.egwStatus.code not in('CANCELLED') and pc.code = ? ")
})
@SequenceGenerator(name = ProjectCode.SEQ_EGW_PROJECTCODE, sequenceName = ProjectCode.SEQ_EGW_PROJECTCODE, allocationSize = 1)
public class ProjectCode extends AbstractAuditable implements EntityType {

    private static final long serialVersionUID = -1569796745047275070L;

    public static final String SEQ_EGW_PROJECTCODE = "SEQ_EGW_PROJECTCODE";
    public static final String ALLACTIVEPROJECTCODES = "ALL_ACTIVEPROJECTCODES";
    public static final String GETPROJECTCODEBYCODE = "GET_ACTIVEPROJECTCODE_BY_CODE";

    @Id
    @GeneratedValue(generator = SEQ_EGW_PROJECTCODE, strategy = GenerationType.SEQUENCE)
    private Long id;

    private String code;

    @OneToMany(mappedBy = "projectCode", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, targetEntity = AbstractEstimate.class)
    private Set<AbstractEstimate> estimates = new HashSet<AbstractEstimate>();

    @Column(name = "ISACTIVE")
    private boolean active;

    @SafeHtml
    @Length(max = 1024, message = "projectCode.description.length")
    private String description;

    @SafeHtml
    @Length(max = 1024, message = "projectCode.name.length")
    @Column(name = "NAME")
    private String codeName;

    @JoinColumn(name = "STATUS_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private EgwStatus egwStatus;

    @Column(name = "PROJECT_VALUE")
    private Double projectValue;

    @Column(name = "COMPLETION_DATE")
    private Date completionDate;

    public ProjectCode() {
    }

    public ProjectCode(final AbstractEstimate abstractEstimate, final String code) {
        estimates.add(abstractEstimate);
        this.code = code;
    }

    @Override
    public String getCode() {
        return code;
    }

    public void setCode(final String code) {
        this.code = code;
    }

    public void addEstimate(final AbstractEstimate estimate) {
        estimates.add(estimate);
    }

    public Set<AbstractEstimate> getEstimates() {
        return estimates;
    }

    public void setEstimates(final Set<AbstractEstimate> estimates) {
        this.estimates = estimates;
    }

    @Override
    public String getBankaccount() {

        return null;
    }

    @Override
    public String getBankname() {

        return null;
    }

    @Override
    public String getIfsccode() {

        return null;
    }

    @Override
    public String getModeofpay() {

        return null;
    }

    @Override
    public String getName() {

        return codeName;
    }

    @Override
    public String getPanno() {

        return null;
    }

    @Override
    public String getTinno() {

        return null;
    }

    @Override
    public Integer getEntityId() {
        return Integer.valueOf(id.intValue());
    }

    @Override
    public String getEntityDescription() {
        return description;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(final boolean active) {
        this.active = active;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public String getCodeName() {
        return codeName;
    }

    public void setCodeName(final String codeName) {
        this.codeName = codeName;
    }

    @Override
    public EgwStatus getEgwStatus() {
        return egwStatus;
    }

    public void setEgwStatus(final EgwStatus egwStatus) {
        this.egwStatus = egwStatus;
    }

    public Double getProjectValue() {
        return projectValue;
    }

    public void setProjectValue(final Double projectValue) {
        this.projectValue = projectValue;
    }

    public Date getCompletionDate() {
        return completionDate;
    }

    public void setCompletionDate(final Date completionDate) {
        this.completionDate = completionDate;
    }

    @Override
    public void setId(final Long id) {
        this.id = id;
    }

    @Override
    public Long getId() {
        return id;
    }

}
