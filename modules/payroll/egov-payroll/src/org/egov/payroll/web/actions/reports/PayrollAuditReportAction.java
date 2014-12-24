package org.egov.payroll.web.actions.reports;

import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.egov.web.utils.ServletActionRedirectResult;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.web.actions.BaseFormAction;

@ParentPackage("egov")
@Result(name="auditReport",type="redirect",location="auditReport",params={"moduleName","PAYROLL","namespace","/egi/auditing","method","searchForm","actionName","auditReport","prependServletContext","false"})
public class PayrollAuditReportAction extends BaseFormAction{

	
	@SkipValidation
	public String auditReport() {
	 return "auditReport";
	}

	
	@Override
	public Object getModel() {
		
		return null;
	}

}
