package org.egov.works.models.estimate;

import org.apache.commons.lang.StringEscapeUtils;
import org.egov.infstr.commonMasters.EgUom;
import org.egov.infstr.models.BaseModel;
import org.egov.infstr.utils.StringUtils;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

public class NonSor extends BaseModel {

	
	private String description; 
	private EgUom uom;
	
	@NotEmpty(message="nonsor.desc.empty") 
	@Length(max=4000,message="masters.description.length")
	public String getDescription() {
		return description;
	}
	
	public String getDescriptionJS() {
		return StringUtils.escapeJavaScript(description);
	}
	
	public void setDescription(String description) {
		this.description = StringEscapeUtils.unescapeHtml(description);
	}
	
	public EgUom getUom() {
		return uom;
	}
	public void setUom(EgUom uom) {
		this.uom = uom;
	}	
}
