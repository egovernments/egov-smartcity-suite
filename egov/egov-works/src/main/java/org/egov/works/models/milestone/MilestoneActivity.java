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
package org.egov.works.models.milestone;

import org.apache.commons.lang.StringEscapeUtils;
import org.egov.infra.persistence.validator.annotation.Required;
import org.egov.infra.utils.StringUtils;
import org.egov.infstr.models.BaseModel;
import org.hibernate.validator.constraints.Length;

import java.math.BigDecimal;

public class MilestoneActivity extends BaseModel {

    private static final long serialVersionUID = -622141478386284778L;
    private BigDecimal stageOrderNo;
    private String description;
    private BigDecimal percentage;
    private Milestone milestone;

    @Required(message = "milestoneTemplateActivity.desc.null")
    @Length(max = 1024, message = "milestoneTemplateActivity.desc.length")
    public String getDescription() {
        return description;
    }

    @Required(message = "milestoneTemplateActivity.percentage.null")
    public BigDecimal getPercentage() {
        return percentage;
    }

    public String getDescriptionJS() {
        return StringUtils.escapeJavaScript(description);
    }

    public void setDescription(final String description) {
        this.description = StringEscapeUtils.unescapeHtml(description);
    }

    public void setPercentage(final BigDecimal percentage) {
        this.percentage = percentage;
    }

    public Milestone getMilestone() {
        return milestone;
    }

    public void setMilestone(final Milestone milestone) {
        this.milestone = milestone;
    }

    @Required(message = "milestoneTemplateActivity.stageOrderNo.null")
    public BigDecimal getStageOrderNo() {
        return stageOrderNo;
    }

    public void setStageOrderNo(final BigDecimal stageOrderNo) {
        this.stageOrderNo = stageOrderNo;
    }

}
