package org.egov.works.web.actions.qualityControl;

import org.apache.log4j.Logger;
import org.egov.infstr.ValidationException;
import org.egov.infstr.services.PersistenceService;
import org.egov.web.actions.BaseFormAction;
import org.egov.web.annotation.ValidationErrorPage;
import org.egov.works.models.qualityControl.MaterialType;

public class MaterialTypeSearchAction extends BaseFormAction{
	
	private static final Logger LOGGER = Logger.getLogger(MaterialTypeSearchAction.class);
	private String mode; 

	@Override
	public Object getModel() {
		// TODO Auto-generated method stub
		return null;  
	}
	
	public void prepare()
	{
		if(mode.equalsIgnoreCase("createTestMaster"))
			addDropdownData("materialTypeList", persistenceService.findAllBy(" from MaterialType where isActive=1 order by name "));
		else
			addDropdownData("materialTypeList", persistenceService.findAllBy(" from MaterialType order by id "));
		super.prepare();

	} 
	
	public String view(){  
		return "view";  
	} 
	
	public String edit(){  
		return "view";  
	} 

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}
	
}