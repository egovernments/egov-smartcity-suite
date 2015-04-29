package org.egov.works.models.milestone;


import java.math.BigDecimal;
import java.util.Date;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import org.apache.commons.lang.StringEscapeUtils;
import org.egov.infra.persistence.validator.annotation.DateFormat;
import org.egov.infra.persistence.validator.annotation.Required;
import org.egov.infstr.models.BaseModel;
import org.egov.infstr.utils.StringUtils;
import org.hibernate.validator.constraints.Length;

/**
 * @author vikas
 */

public class TrackMilestoneActivity extends BaseModel{

	private TrackMilestone trackMilestone;
	private MilestoneActivity milestoneActivity;
	
	@Required(message="trackmilestoneActivity.status.null")
	private String status;

	@Min(value=0, message="trackmilestoneActivity.percentage.min.valid")
	@Max(value=100,message="trackmilestoneActivity.percentage.max.valid")
	private BigDecimal complPercentage;

	@Length(max=1024,message="trackmilestoneActivity.remarks.length")
	private String remarks;

	@DateFormat(message="trackmilestoneActivity.completionDate.valid") 
	private Date completionDate;

	public TrackMilestone getTrackMilestone() {
		return trackMilestone;
	}

	public void setTrackMilestone(TrackMilestone trackMilestone) {
		this.trackMilestone = trackMilestone;
	}

	public MilestoneActivity getMilestoneActivity() {
		return milestoneActivity;
	}

	public void setMilestoneActivity(MilestoneActivity milestoneActivity) {
		this.milestoneActivity = milestoneActivity;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public BigDecimal getComplPercentage() {
		return complPercentage;
	}

	public void setComplPercentage(BigDecimal complPercentage) {
		this.complPercentage = complPercentage;
	}

	public String getRemarks() {
		return remarks;
	}

	public Date getCompletionDate() {
		return completionDate;
	}

	public void setCompletionDate(Date completionDate) {
		this.completionDate = completionDate;
	}
	
	public String getRemarksJS() {
		return StringUtils.escapeJavaScript(remarks);
	}
	
	public void setRemarks(String remarks) {
		this.remarks = StringEscapeUtils.unescapeHtml(remarks);
	}
}

