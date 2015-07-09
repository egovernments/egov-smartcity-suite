package org.egov.ptis.actions.reports;

import static org.egov.infra.web.struts.actions.BaseFormAction.NEW;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.ResultPath;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.infra.web.struts.actions.BaseFormAction;
import org.springframework.transaction.annotation.Transactional;

import com.opensymphony.xwork2.validator.annotations.Validations;
@SuppressWarnings("serial")
@ParentPackage("egov")
@Validations
@Transactional(readOnly = true)
@Namespace("/search")
@ResultPath("/WEB-INF/jsp/")
@Results({ @Result(name = NEW, location = "reports/collectionSummary-new.jsp") ,
		@Result(name = "result", location = "reports/collectionSummary-new.jsp") })
public class CollectionSummaryReportAction extends BaseFormAction {
	@SkipValidation
	@Action(value = "/collectionSummary-newForm")
	public String newForm() {
		return NEW;
	}

	@Override
	public Object getModel() {
		// TODO Auto-generated method stub
		return null;
	}

}
