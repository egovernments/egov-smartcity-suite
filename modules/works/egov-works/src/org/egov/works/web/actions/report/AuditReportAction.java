package org.egov.works.web.actions.report;

import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.web.actions.BaseFormAction;
import org.egov.web.utils.ServletActionRedirectResult;

/**
 * 
 * @author Sathish P 
 *
 */

@ParentPackage("egov")
@Result(name="auditReport",type="redirect",location="auditReport",params={"moduleName","WORKS","namespace","/egi/auditing","method","searchForm","actionName","auditReport","prependServletContext","false"})
public class AuditReportAction extends BaseFormAction{
	private static final long serialVersionUID = 1L;
	
	@SkipValidation
	public String search() { 
	 return "auditReport";
	}

	
	@Override
	public Object getModel() {
		
		return null;
	}

}