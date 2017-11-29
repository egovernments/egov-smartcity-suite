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
package org.egov.works.models.masters;

import org.egov.commons.EgwTypeOfWork;
import org.egov.infra.persistence.entity.AbstractAuditable;
import org.egov.infra.persistence.validator.annotation.Unique;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "EGW_MILESTONE_TEMPLATE")
@Unique(id = "id", tableName = "EGW_MILESTONE_TEMPLATE", columnName = { "code" }, fields = { "code" }, enableDfltMsg = true)
@SequenceGenerator(name = MilestoneTemplate.SEQ_EGW_MILESTONE_TEMPLATE, sequenceName = MilestoneTemplate.SEQ_EGW_MILESTONE_TEMPLATE, allocationSize = 1)
public class MilestoneTemplate extends AbstractAuditable {

    private static final long serialVersionUID = 474905206086516812L;

    public static final String SEQ_EGW_MILESTONE_TEMPLATE = "SEQ_EGW_MILESTONE_TEMPLATE";

    @Id
    @GeneratedValue(generator = SEQ_EGW_MILESTONE_TEMPLATE, strategy = GenerationType.SEQUENCE)
    private Long id;

    private String code;

    private String name;

    private String description;

    private Integer status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "typeOfWork", nullable = false)
    private EgwTypeOfWork typeOfWork;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subTypeOfWork")
    private EgwTypeOfWork subTypeOfWork;

    @OrderBy("id")
    @OneToMany(mappedBy = "milestoneTemplate", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, targetEntity = MilestoneTemplateActivity.class)
    private final List<MilestoneTemplateActivity> milestoneTemplateActivities = new ArrayList<MilestoneTemplateActivity>(0);

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(final Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(final String code) {
        this.code = code;
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(final Integer status) {
        this.status = status;
    }

    public EgwTypeOfWork getTypeOfWork() {
        return typeOfWork;
    }

    public void setTypeOfWork(final EgwTypeOfWork typeOfWork) {
        this.typeOfWork = typeOfWork;
    }

    public EgwTypeOfWork getSubTypeOfWork() {
        return subTypeOfWork;
    }

    public void setSubTypeOfWork(final EgwTypeOfWork subTypeOfWork) {
        this.subTypeOfWork = subTypeOfWork;
    }

    public List<MilestoneTemplateActivity> getMilestoneTemplateActivities() {
        return milestoneTemplateActivities;
    }

    public void addMilestoneTemplateActivity(final MilestoneTemplateActivity milestoneTemplateactivity) {
        milestoneTemplateActivities.add(milestoneTemplateactivity);
    }

}
