package org.egov.works.models.serviceOrder;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.StringEscapeUtils;
import org.egov.infstr.models.BaseModel;
import org.egov.infstr.models.validator.Required;
import org.hibernate.validator.constraints.Length;

/**
 * SoTemplateActivities entity. @author msahoo
 */
public class SoTemplateActivities extends BaseModel{

	
	private static final long serialVersionUID = 1L;
	private BigDecimal stageNo;
	private ServiceTemplate serviceTemplate;
	private String description;
	private BigDecimal rateValue;
	
	private Set serviceOrderDetails = new HashSet(0);
	
	@Required(message="templateactv.desc.null")
	@Length(max=1024,message="templateactv.desc.length")
	public String getDescription() {
		return description;
	}
	
	@Required(message="templateactv.rateValue.null")
	public BigDecimal getRateValue() {
		return rateValue;
	}
	
	
	public void setDescription(String description) {
		this.description = StringEscapeUtils.unescapeHtml(description);
	}
	public void setRateValue(BigDecimal rateValue) {
		this.rateValue = rateValue;
	}
	public Set getServiceOrderDetails() {
		return serviceOrderDetails;
	}
	public void setServiceOrderDetails(Set serviceOrderDetails) {
		this.serviceOrderDetails = serviceOrderDetails;
	}
	public ServiceTemplate getServiceTemplate() {
		return serviceTemplate;
	}
	public void setServiceTemplate(ServiceTemplate serviceTemplate) {
		this.serviceTemplate = serviceTemplate;
	}
	
	@Required(message="templateactv.stageNo.null")
	public BigDecimal getStageNo() {
		return stageNo;
	}
	public void setStageNo(BigDecimal stageNo) {
		this.stageNo = stageNo;
	}
	
}
