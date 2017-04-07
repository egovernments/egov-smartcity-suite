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
package org.egov.works.web.controller.masters;

import org.egov.common.entity.UOM;
import org.egov.infra.validation.regex.Constants;
import org.egov.works.masters.entity.EstimateTemplate;
import org.egov.works.masters.entity.EstimateTemplateActivity;
import org.egov.works.masters.service.EstimateTemplateService;
import org.egov.works.utils.WorksConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;

@Controller
public abstract class BaseEstimateTemplateController {

    protected static final String ESTIMATETEMPLATE = "estimateTemplate";
    private static final String TEMPESTIMATETEMPLATENONSORACTIVITIES = "tempEstimateTemplateNonSorActivities[";

    @Autowired
    private EstimateTemplateService estimateTemplateService;

    protected void validateEstimateTemplate(final EstimateTemplate estimateTemplate, final BindingResult resultBinder) {
        final EstimateTemplate existingEstimateTemplate = estimateTemplateService
                .getEstimateTemplateByCode(estimateTemplate.getCode());
        if (existingEstimateTemplate != null && !existingEstimateTemplate.getId().equals(estimateTemplate.getId()))
            resultBinder.reject("error.estimatetemplate.exists", new String[] { estimateTemplate.getCode() },
                    "error.estimatetemplate.exists");

        if (estimateTemplate.getCode() == null)
            resultBinder.reject("error.estimatetemplate.code", "error.estimatetemplate.code");

        if (estimateTemplate.getCode() != null
                && !estimateTemplate.getCode().matches(WorksConstants.ALPHANUMERICWITHHYPHENSLASH))
            resultBinder.reject("error.estimatetemplate.code.invalid", "error.estimatetemplate.code.invalid");

        checkValidationForNameAndDescription(estimateTemplate, resultBinder);

        if (estimateTemplate.getTypeOfWork() == null)
            resultBinder.reject("error.estimatetemplate.typeofwork", "error.estimatetemplate.typeofwork");

    }

    private void checkValidationForNameAndDescription(final EstimateTemplate estimateTemplate,
            final BindingResult resultBinder) {
        if (estimateTemplate.getName() == null)
            resultBinder.reject("error.estimatetemplate.name", "error.estimatetemplate.name");

        if (estimateTemplate.getName() != null
                && !estimateTemplate.getName().matches(WorksConstants.ALPHANUMERICWITHSPECIALCHAR))
            resultBinder.reject("error.estimatetemplate.name.invalid", "error.estimatetemplate.name.invalid");

        if (estimateTemplate.getDescription() == null)
            resultBinder.reject("error.estimatetemplate.description", "error.estimatetemplate.description");

        if (estimateTemplate.getDescription() != null
                && !estimateTemplate.getDescription().matches(Constants.ALPHANUMERICWITHSPECIALCHAR))
            resultBinder.reject("error.estimatetemplate.description.invalid",
                    "error.estimatetemplate.description.invalid");
    }

    protected void validateEstimateTemplateActivities(final EstimateTemplate estimateTemplate,
            final BindingResult resultBinder) {
        int index = 0;
        for (final EstimateTemplateActivity estimateTemplateActivity : estimateTemplate
                .getTempEstimateTemplateNonSorActivities()) {
            checkNonSorActivitiesValidation(estimateTemplate, resultBinder, index, estimateTemplateActivity);
            index++;
        }
    }

    private void checkNonSorActivitiesValidation(final EstimateTemplate estimateTemplate,
            final BindingResult resultBinder, final int index,
            final EstimateTemplateActivity estimateTemplateActivity) {
        final UOM uom = estimateTemplate.getTempEstimateTemplateNonSorActivities().get(index).getNonSor().getUom();
        if (uom != null && uom.getId() != null && estimateTemplateActivity.getNonSor().getDescription() == null)
            resultBinder.rejectValue(TEMPESTIMATETEMPLATENONSORACTIVITIES + index + "].nonSor.description",
                    "error.estimatetemplate.nonsor.description");
        if (uom != null && uom.getId() != null && estimateTemplateActivity.getNonSor().getUom() == null)
            resultBinder.rejectValue(TEMPESTIMATETEMPLATENONSORACTIVITIES + index + "].nonSor.uom",
                    "error.estimatetemplate.nonsor.uom");
        if (uom != null && uom.getId() != null && estimateTemplateActivity.getValue() == 0.0)
            resultBinder.rejectValue(TEMPESTIMATETEMPLATENONSORACTIVITIES + index + "].nonSor.value",
                    "error.estimatetemplate.nonsor.value");
    }

}
