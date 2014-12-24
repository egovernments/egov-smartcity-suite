package org.egov.works.models.serviceOrder;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringEscapeUtils;
import org.egov.infstr.models.BaseModel;
import org.egov.infstr.models.validator.OptionalPattern;
import org.egov.infstr.models.validator.Required;
import org.egov.infstr.models.validator.Unique;
import org.egov.infstr.models.validator.constants.ValidatorConstants;
import org.hibernate.validator.constraints.Length;
import javax.validation.Valid;

/**
 * EgwServiceTemplate entity. @author msahoo
 */
@Unique(fields={"templateCode","templateName"},id="id",tableName="EGW_SERVICE_TEMPLATE",columnName={"TEMPLATE_CODE","TEMPLATE_NAME"},message="template.code.name.isunique")
public class ServiceTemplate extends BaseModel {


	private static final long serialVersionUID = 1L;
	
	@OptionalPattern(regex=ValidatorConstants.alphaNumericwithSpace,message="template.code.alphaNumeric")
	private String templateCode;
	
	
	private String templateName;
	
	
	private String templateDesc;
	
	
	private Boolean isActive;
	@Valid
	private List<SoTemplateActivities> soTemplateActivities = new LinkedList<SoTemplateActivities>();
	
	private Set serviceOrderObjectDetails = new HashSet(0);
	
	@Required(message="template.code.null")
	@Length(max=256,message="template.code.length")
	public String getTemplateCode() {
		return templateCode;
	}
	
	@Required(message="template.name.null")
	@Length(max=256,message="template.name.length")
	public String getTemplateName() {
		return templateName;
	}
	
	@Required(message="template.desc.null")
	@Length(max=256,message="template.desc.length")
	public String getTemplateDesc() {
		return templateDesc;
	}
	public Boolean getIsActive() {
		return isActive;
	}
	public void setTemplateCode(String templateCode) {
		this.templateCode = templateCode;
	}
	public void setTemplateName(String templateName) {
		this.templateName = StringEscapeUtils.unescapeHtml(templateName);
	}
	public void setTemplateDesc(String templateDesc) {
		this.templateDesc = StringEscapeUtils.unescapeHtml(templateDesc);
	}
	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}
	
	public Set getServiceOrderObjectDetails() {
		return serviceOrderObjectDetails;
	}
	
	public void setServiceOrderObjectDetails(Set serviceOrderObjectDetails) {
		this.serviceOrderObjectDetails = serviceOrderObjectDetails;
	}
	public List<SoTemplateActivities> getSoTemplateActivities() {
		return soTemplateActivities;
	}
	public void setSoTemplateActivities(
			List<SoTemplateActivities> soTemplateActivities) {
		this.soTemplateActivities = soTemplateActivities;
	}
	

}
