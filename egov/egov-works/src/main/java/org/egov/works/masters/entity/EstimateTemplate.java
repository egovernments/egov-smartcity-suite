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
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang.StringEscapeUtils;
import org.egov.commons.EgwTypeOfWork;
import org.egov.infra.persistence.entity.AbstractAuditable;
import org.egov.infra.persistence.validator.annotation.OptionalPattern;
import org.egov.infra.persistence.validator.annotation.Required;
import org.egov.infra.persistence.validator.annotation.Unique;
import org.egov.works.utils.WorksConstants;
import org.hibernate.validator.constraints.SafeHtml;

@Entity
@Table(name = "EGW_ESTIMATE_TEMPLATE")
@Unique(fields = { "code" }, enableDfltMsg = true)
@SequenceGenerator(name = EstimateTemplate.SEQ_EGW_ESTIMATE_TEMPLATE, sequenceName = EstimateTemplate.SEQ_EGW_ESTIMATE_TEMPLATE, allocationSize = 1)
public class EstimateTemplate extends AbstractAuditable {

    private static final long serialVersionUID = -1150757466961896868L;

    public static final String SEQ_EGW_ESTIMATE_TEMPLATE = "SEQ_EGW_ESTIMATE_TEMPLATE";

    @Id
    @GeneratedValue(generator = SEQ_EGW_ESTIMATE_TEMPLATE, strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotNull
    @SafeHtml
    @Required(message = "estimatetemplate.code.not.null")
    private String code;

    @NotNull
    @SafeHtml
    @OptionalPattern(regex = WorksConstants.ALPHANUMERICWITHALLSPECIALCHAR, message = "estimatetemplate.name.alphaNumeric")
    @Required(message = "estimatetemplate.name.not.null")
    private String name;

    @NotNull
    @SafeHtml
    @OptionalPattern(regex = WorksConstants.ALPHANUMERICWITHALLSPECIALCHAR, message = "estimatetemplate.description.alphaNumeric")
    @Required(message = "estimatetemplate.description.not.null")
    private String description;

    private boolean status = true;

    @Required(message = "estimatetemplate.workType.not.null")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "WORKTYPE_ID", nullable = false)
    private EgwTypeOfWork typeOfWork;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "WORKSUBTYPE_ID")
    private EgwTypeOfWork subTypeOfWork;

    @OrderBy("id")
    @OneToMany(mappedBy = "estimateTemplate", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, targetEntity = EstimateTemplateActivity.class)
    private final List<EstimateTemplateActivity> estimateTemplateActivities = new ArrayList<EstimateTemplateActivity>(
            0);

    @Transient
    private List<EstimateTemplateActivity> tempEstimateTemplateSorActivities = new ArrayList<EstimateTemplateActivity>(
            0);

    @Transient
    private List<EstimateTemplateActivity> tempEstimateTemplateNonSorActivities = new ArrayList<EstimateTemplateActivity>(
            0);

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
        this.code = StringEscapeUtils.unescapeHtml(code);
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = StringEscapeUtils.unescapeHtml(name);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = StringEscapeUtils.unescapeHtml(description);
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

    public List<EstimateTemplateActivity> getEstimateTemplateActivities() {
        return estimateTemplateActivities;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(final boolean status) {
        this.status = status;
    }

    public List<EstimateTemplateActivity> getTempEstimateTemplateSorActivities() {
        return tempEstimateTemplateSorActivities;
    }

    public void setTempEstimateTemplateSorActivities(
            final List<EstimateTemplateActivity> tempEstimateTemplateSorActivities) {
        this.tempEstimateTemplateSorActivities = tempEstimateTemplateSorActivities;
    }

    public List<EstimateTemplateActivity> getTempEstimateTemplateNonSorActivities() {
        return tempEstimateTemplateNonSorActivities;
    }

    public void setTempEstimateTemplateNonSorActivities(
            final List<EstimateTemplateActivity> tempEstimateTemplateNonSorActivities) {
        this.tempEstimateTemplateNonSorActivities = tempEstimateTemplateNonSorActivities;
    }

    public void setEstimateTemplateActivities(final List<EstimateTemplateActivity> estimateTemplateActivities) {
        this.estimateTemplateActivities.clear();
        if (estimateTemplateActivities != null)
            this.estimateTemplateActivities.addAll(estimateTemplateActivities);
    }

}
