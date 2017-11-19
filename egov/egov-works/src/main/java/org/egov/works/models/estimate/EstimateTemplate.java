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
package org.egov.works.models.estimate;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.egov.commons.EgwTypeOfWork;
import org.egov.infra.persistence.validator.annotation.Required;
import org.egov.infra.persistence.validator.annotation.Unique;
import org.egov.infra.validation.exception.ValidationError;
import org.egov.infstr.models.BaseModel;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

@Unique(fields = { "code" }, id = "id", tableName = "EGW_ESTIMATE_TEMPLATE", columnName = {
        "CODE" }, message = "estimateTemplate.code.isunique")
public class EstimateTemplate extends BaseModel {

    private static final long serialVersionUID = -1150757466961896868L;
    @Required(message = "estimatetemplate.code.not.null")
    private String code;
    @Required(message = "estimatetemplate.name.not.null")
    private String name;
    @Required(message = "estimatetemplate.description.not.null")
    private String description;
    private int status;
    @Valid
    @Required(message = "estimatetemplate.workType.not.null")
    private EgwTypeOfWork workType;
    private EgwTypeOfWork subType;

    @Valid
    private List<EstimateTemplateActivity> estimateTemplateActivities = new LinkedList<EstimateTemplateActivity>();

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

    public int getStatus() {
        return status;
    }

    public void setStatus(final int status) {
        this.status = status;
    }

    public EgwTypeOfWork getWorkType() {
        return workType;
    }

    public void setWorkType(final EgwTypeOfWork workType) {
        this.workType = workType;
    }

    public EgwTypeOfWork getSubType() {
        return subType;
    }

    public void setSubType(final EgwTypeOfWork subType) {
        this.subType = subType;
    }

    public List<EstimateTemplateActivity> getEstimateTemplateActivities() {
        return estimateTemplateActivities;
    }

    public void setEstimateTemplateActivities(final List<EstimateTemplateActivity> estimateTemplateActivities) {
        this.estimateTemplateActivities = estimateTemplateActivities;
    }

    public void addActivity(final EstimateTemplateActivity estimateTemplateActivity) {
        estimateTemplateActivities.add(estimateTemplateActivity);
    }

    public Collection<EstimateTemplateActivity> getSORActivities() {
        return CollectionUtils.select(estimateTemplateActivities,
                activity -> ((EstimateTemplateActivity) activity).getSchedule() != null);
    }

    public Collection<EstimateTemplateActivity> getNonSORActivities() {
        return CollectionUtils.select(estimateTemplateActivities,
                activity -> ((EstimateTemplateActivity) activity).getNonSor() != null);
    }

    public List<ValidationError> validateActivities() {
        final List<ValidationError> validationErrors = new ArrayList<ValidationError>();
        for (final EstimateTemplateActivity estimateTemplateActivity : estimateTemplateActivities)
            validationErrors.addAll(estimateTemplateActivity.validate());
        return validationErrors;
    }

    @Override
    public List<ValidationError> validate() {
        final List<ValidationError> validationErrors = new ArrayList<ValidationError>();
        validationErrors.addAll(validateActivities());
        return validationErrors;
    }

}
