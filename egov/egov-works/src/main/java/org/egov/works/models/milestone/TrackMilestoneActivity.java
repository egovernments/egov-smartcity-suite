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
import org.egov.infra.persistence.validator.annotation.DateFormat;
import org.egov.infra.persistence.validator.annotation.Required;
import org.egov.infra.utils.StringUtils;
import org.egov.infstr.models.BaseModel;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.math.BigDecimal;
import java.util.Date;

public class TrackMilestoneActivity extends BaseModel {

    private static final long serialVersionUID = -6037373912017416277L;
    private TrackMilestone trackMilestone;
    private MilestoneActivity milestoneActivity;

    @Required(message = "trackmilestoneActivity.status.null")
    private String status;

    @Min(value = 0, message = "trackmilestoneActivity.percentage.min.valid")
    @Max(value = 100, message = "trackmilestoneActivity.percentage.max.valid")
    private BigDecimal complPercentage;

    @Length(max = 1024, message = "trackmilestoneActivity.remarks.length")
    private String remarks;

    @DateFormat(message = "trackmilestoneActivity.completionDate.valid")
    private Date completionDate;

    public TrackMilestone getTrackMilestone() {
        return trackMilestone;
    }

    public void setTrackMilestone(final TrackMilestone trackMilestone) {
        this.trackMilestone = trackMilestone;
    }

    public MilestoneActivity getMilestoneActivity() {
        return milestoneActivity;
    }

    public void setMilestoneActivity(final MilestoneActivity milestoneActivity) {
        this.milestoneActivity = milestoneActivity;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(final String status) {
        this.status = status;
    }

    public BigDecimal getComplPercentage() {
        return complPercentage;
    }

    public void setComplPercentage(final BigDecimal complPercentage) {
        this.complPercentage = complPercentage;
    }

    public String getRemarks() {
        return remarks;
    }

    public Date getCompletionDate() {
        return completionDate;
    }

    public void setCompletionDate(final Date completionDate) {
        this.completionDate = completionDate;
    }

    public String getRemarksJS() {
        return StringUtils.escapeJavaScript(remarks);
    }

    public void setRemarks(final String remarks) {
        this.remarks = StringEscapeUtils.unescapeHtml(remarks);
    }
}
