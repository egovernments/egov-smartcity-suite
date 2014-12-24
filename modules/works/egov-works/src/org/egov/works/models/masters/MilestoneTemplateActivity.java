package org.egov.works.models.masters;

import java.math.BigDecimal;

import org.apache.commons.lang.StringEscapeUtils;
import org.egov.infstr.models.BaseModel;
import org.egov.infstr.models.validator.Required;
import org.hibernate.validator.constraints.Length;

/**
 * @author vikas
 */

public class MilestoneTemplateActivity extends BaseModel{

	
	private static final long serialVersionUID = 1L;
	private BigDecimal stageOrderNo;
	private String description;
	private BigDecimal percentage;
	private MilestoneTemplate milestoneTemplate;
	
	
	@Required(message="milestoneTemplateActivity.desc.null")
	@Length(max=1024,message="milestoneTemplateActivity.desc.length")
	public String getDescription() {
		return description;
	}
	
	@Required(message="milestoneTemplateActivity.percentage.null")
	public BigDecimal getPercentage() {
		return percentage;
	}
	
	
	public void setDescription(String description) {
		this.description = StringEscapeUtils.unescapeHtml(description);
	}

	public void setPercentage(BigDecimal percentage) {
		this.percentage = percentage;
	}

	public MilestoneTemplate getMilestoneTemplate() {
		return milestoneTemplate;
	}

	public void setMilestoneTemplate(MilestoneTemplate milestoneTemplate) {
		this.milestoneTemplate = milestoneTemplate;
	}
	
	@Required(message="milestoneTemplateActivity.stageOrderNo.null")
	public BigDecimal getStageOrderNo() {
		return stageOrderNo;
	}
	public void setStageOrderNo(BigDecimal stageOrderNo) {
		this.stageOrderNo = stageOrderNo;
	}

	
}
