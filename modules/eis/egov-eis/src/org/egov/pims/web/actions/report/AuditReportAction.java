package org.egov.pims.web.actions.report;

import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.web.actions.BaseFormAction;
import org.egov.web.utils.ServletActionRedirectResult;

@ParentPackage("egov")
@Result(name="auditReport",type="redirect",location="auditReport",params={"moduleName","EIS","namespace","/egi/auditing","method","searchForm","actionName","auditReport","prependServletContext","false"})
public class AuditReportAction extends BaseFormAction {

	@SkipValidation
	public String auditReport() {
	 return "auditReport";
	}

	
	@Override
	public Object getModel() {
		
		return null;
	}

}
