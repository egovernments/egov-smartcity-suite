/**
 * eGov suite of products aim to improve the internal efficiency,transparency,
   accountability and the service delivery of the government  organizations.

    Copyright (C) <2015>  eGovernments Foundation

    The updated version of eGov suite of products as by eGovernments Foundation
    is available at http://www.egovernments.org

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program. If not, see http://www.gnu.org/licenses/ or
    http://www.gnu.org/licenses/gpl.html .

    In addition to the terms of the GPL license to be adhered to in using this
    program, the following additional terms are to be complied with:

	1) All versions of this program, verbatim or modified must carry this
	   Legal Notice.

	2) Any misrepresentation of the origin of the material is prohibited. It
	   is required that all modified versions of this material be marked in
	   reasonable ways as different from the original version.

	3) This license does not grant any rights to any user of the program
	   with regards to rights under trademark law for use of the trade names
	   or trademarks of eGovernments Foundation.

  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.works.models.masters;

import org.egov.infra.persistence.validator.annotation.Required;
import org.egov.infstr.models.BaseModel;
import org.hibernate.validator.constraints.Length;

import java.math.BigDecimal;

public class MilestoneTemplateActivity extends BaseModel {

    private static final long serialVersionUID = 1L;
    private BigDecimal stageOrderNo;
    private String description;
    private BigDecimal percentage;
    private MilestoneTemplate milestoneTemplate;

    @Required(message = "milestoneTemplateActivity.desc.null")
    @Length(max = 1024, message = "milestoneTemplateActivity.desc.length")
    public String getDescription() {
        return description;
    }

    @Required(message = "milestoneTemplateActivity.percentage.null")
    public BigDecimal getPercentage() {
        return percentage;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public void setPercentage(final BigDecimal percentage) {
        this.percentage = percentage;
    }

    public MilestoneTemplate getMilestoneTemplate() {
        return milestoneTemplate;
    }

    public void setMilestoneTemplate(final MilestoneTemplate milestoneTemplate) {
        this.milestoneTemplate = milestoneTemplate;
    }

    @Required(message = "milestoneTemplateActivity.stageOrderNo.null")
    public BigDecimal getStageOrderNo() {
        return stageOrderNo;
    }

    public void setStageOrderNo(final BigDecimal stageOrderNo) {
        this.stageOrderNo = stageOrderNo;
    }

}
