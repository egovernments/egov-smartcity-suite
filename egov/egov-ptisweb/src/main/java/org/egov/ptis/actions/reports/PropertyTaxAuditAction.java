package org.egov.ptis.actions.reports;

import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.web.actions.BaseFormAction;
import org.egov.web.utils.ServletActionRedirectResult;

@ParentPackage("egov")
@Result(name="auditReport",type=ServletActionRedirectResult.class,value="auditReport",params={"moduleName","PROPERTYTAX","namespace","/egi/auditing","method","searchForm","actionName","auditReport","prependServletContext","false"})
public class PropertyTaxAuditAction  extends BaseFormAction{

	@SkipValidation
	public String auditReport() {
		return "auditReport";
	}

	@Override
	public Object getModel() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
