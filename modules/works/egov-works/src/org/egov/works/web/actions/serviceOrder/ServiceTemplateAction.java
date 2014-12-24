/**
 * 
 */
package org.egov.works.web.actions.serviceOrder;

import java.math.BigDecimal;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.infstr.search.SearchQuery;
import org.egov.infstr.search.SearchQueryHQL;
import org.egov.web.actions.SearchFormAction;
import org.egov.works.models.serviceOrder.ServiceTemplate;
import org.egov.works.models.serviceOrder.SoTemplateActivities;
import org.egov.works.services.serviceOrder.ServiceTemplateService;

/**
 * @author manoranjan
 *
 */
@ParentPackage("egov")
public class ServiceTemplateAction extends SearchFormAction {


	private static final long serialVersionUID = 1L;
	private ServiceTemplate template = new ServiceTemplate();
	private ServiceTemplateService serviceTemplateSer;
	private String type;
	private String mode="";
	@Override
	public Object getModel() {
	
		return template;
	}
	@SkipValidation
	public String newform(){
		
		return "new";
	}
	
	public String create(){
		template.setIsActive(null==template.getIsActive()?Boolean.FALSE:template.getIsActive());
		serviceTemplateSer.persist(template);
		addActionMessage(" Template created sucessfully , Template Code :"+template.getTemplateCode());
		return "message";
	}
	
	
	public void validate() {
		
		if(null == template.getSoTemplateActivities() || template.getSoTemplateActivities().size() ==0){
			 
			addFieldError("activity.missing", "Error : Template activity is missing");
		}
		BigDecimal rate = BigDecimal.ZERO;
		for (SoTemplateActivities templateActivities : template.getSoTemplateActivities()) {
			if(templateActivities.getRateValue()!=null){
			rate = rate.add(templateActivities.getRateValue());
		}
		}
		if(rate.compareTo(BigDecimal.valueOf(100)) !=0){
			addFieldError("activity.total.rate", "Error : Total activity rate should be 100 percentage");
			 
		}
	}
	@SkipValidation
	public String beforeSearch(){
		type = parameters.get("type")!=null ? parameters.get("type")[0]:"";
		return "search";
	}
	
	@SkipValidation
	public String list(){
		search();
		return "search";
	}
	
	@SkipValidation
	public String beforeEdit(){
		
		template = serviceTemplateSer.findById(template.getId(), false);
		return "edit";
	}
	@SkipValidation
	public String edit(){
		
		ServiceTemplate oldTemplate = serviceTemplateSer.findById(Long.valueOf(parameters.get("templateId")[0]), false);
		oldTemplate.setIsActive(null==template.getIsActive()?Boolean.FALSE:Boolean.TRUE);
		serviceTemplateSer.update(oldTemplate);
		addActionMessage("Template modified sucessfully , Template Code :"+oldTemplate.getTemplateCode());
		return "message";
	}
	
	@SkipValidation
	public String view(){
		mode="view";
		template = serviceTemplateSer.findById(template.getId(), false);
		return "view";
	}
	public void setServiceTemplateSer(ServiceTemplateService serviceTemplateSer) {
		this.serviceTemplateSer = serviceTemplateSer;
	}
	public void setTemplate(ServiceTemplate template) {
		this.template = template;
	}
	@Override
	public SearchQuery prepareQuery(String sortField, String sortOrder) {
		
		StringBuffer query = new StringBuffer(100);
		query.append(" from ServiceTemplate st where st.isActive="+Boolean.TRUE);
	
		if(null != template.getTemplateCode() & StringUtils.isNotEmpty(template.getTemplateCode())){
			query.append(" and  st.templateCode like '%"+template.getTemplateCode()+"%'");
		}
		if(null != template.getTemplateName() & StringUtils.isNotEmpty(template.getTemplateName())){
			query.append(" and  st.templateCode like '%"+template.getTemplateName()+"%'");
		}
		
		return new SearchQueryHQL(query.toString(),"select count(*) "+query.toString(),null);
		
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getMode() {
		return mode;
	}
}
