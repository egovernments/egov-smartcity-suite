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

import org.egov.infra.validation.regex.Constants;
import org.egov.works.masters.entity.MilestoneTemplate;
import org.egov.works.masters.entity.MilestoneTemplateActivity;
import org.egov.works.masters.service.MilestoneTemplateService;
import org.egov.works.utils.WorksConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;

@Controller
public abstract class BaseMilestoneTemplateController {

    protected static final String MILESTONETEMPLATE = "milestoneTemplate";
    private static final String TEMPMILESTONETEMPLATEACTIVITY = "tempMilestoneTemplateActivities[";

    @Autowired
    private MilestoneTemplateService milestoneTemplateService;

    protected void validateMilestoneTemplate(final MilestoneTemplate milestoneTemplate,
            final BindingResult resultBinder) {
        final MilestoneTemplate existingMilestoneTemplate = milestoneTemplateService
                .getMilestoneTemplateByCode(milestoneTemplate.getCode());
        if (existingMilestoneTemplate != null && !existingMilestoneTemplate.getId().equals(milestoneTemplate.getId()))
            resultBinder.reject("error.milestonetemplate.exists", "error.milestonetemplate.exists");

        if (milestoneTemplate.getCode() == null)
            resultBinder.reject("error.milestonetemplate.code", "error.milestonetemplate.code");

        if (milestoneTemplate.getCode() != null
                && !milestoneTemplate.getCode().matches(WorksConstants.ALPHANUMERICWITHHYPHENSLASH))
            resultBinder.reject("error.milestoneTemplate.code.invalid", "error.milestoneTemplate.code.invalid");

        if (milestoneTemplate.getTypeOfWork() == null)
            resultBinder.reject("error.milestonetemplate.typeofwork", "error.milestonetemplate.typeofwork");

        checkMilestoneTemplateDescriptionAndName(milestoneTemplate, resultBinder);

    }

    private void checkMilestoneTemplateDescriptionAndName(final MilestoneTemplate milestoneTemplate,
            final BindingResult resultBinder) {
        if (milestoneTemplate.getDescription() == null)
            resultBinder.reject("error.milestonetemplate.description", "error.milestonetemplate.description");
        if (milestoneTemplate.getDescription() != null
                && !milestoneTemplate.getDescription().matches(Constants.ALPHANUMERICWITHSPECIALCHAR))
            resultBinder.reject("error.milestoneTemplate.description.invalid",
                    "error.milestoneTemplate.description.invalid");
        if (milestoneTemplate.getName() == null)
            resultBinder.reject("error.milestonetemplate.name", "error.milestonetemplate.name");
        if (milestoneTemplate.getName() != null
                && !milestoneTemplate.getName().matches(WorksConstants.ALPHANUMERICWITHSPECIALCHAR))
            resultBinder.reject("error.name.invalid", "error.name.invalid");
    }

    protected void validateMilestoneTemplateActivities(final MilestoneTemplate milestoneTemplate,
            final BindingResult resultBinder) {
        Double totalPercentageValue = 0.0;
        int index = 0;
        for (final MilestoneTemplateActivity milestoneTemplateActivity : milestoneTemplate
                .getTempMilestoneTemplateActivities()) {
            totalPercentageValue += milestoneTemplateActivity.getPercentage();
            if (milestoneTemplateActivity.getStageOrderNo() == 0.0)
                resultBinder.rejectValue(TEMPMILESTONETEMPLATEACTIVITY + index + "].stageOrderNo",
                        "error.milestonetemplate.stageordernumber");
            if (milestoneTemplateActivity.getDescription() == null)
                resultBinder.rejectValue(TEMPMILESTONETEMPLATEACTIVITY + index + "].description",
                        "error.milestonetemplate.stagedescription");
            if (milestoneTemplateActivity.getPercentage() == 0)
                resultBinder.rejectValue(TEMPMILESTONETEMPLATEACTIVITY + index + "].percentage",
                        "error.milestonetemplate.stagepercentage");
            index++;
        }
        if (!totalPercentageValue.equals(100.0))
            resultBinder.reject("error.milestonetemplate.totalvalue", "error.milestonetemplate.totalvalue");
    }

}
