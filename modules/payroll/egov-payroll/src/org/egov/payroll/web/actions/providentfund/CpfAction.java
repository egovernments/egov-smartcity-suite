package org.egov.payroll.web.actions.providentfund;

import org.apache.struts2.convention.annotation.ParentPackage ;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.dispatcher.ServletRedirectResult;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.infstr.services.PersistenceService;
import org.egov.model.recoveries.Recovery;
import org.egov.payroll.model.providentfund.PFHeader;
import org.egov.web.actions.BaseFormAction;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.validator.annotations.Validation;

@Result(name=Action.SUCCESS, type="redirect", location = "cpf.action")
@ParentPackage("egov")
@Validation()
public class CpfAction extends BaseFormAction{
	private static final long serialVersionUID = 1L;
	private PFHeader pfHeader = new PFHeader();
	private PersistenceService<PFHeader, Long> cpfService;
	private String target="";
	public static final  String CREATEORMODIFY="createOrModify";
	public static final  String NEW="new";
	
	@Override
	public Object getModel() {
		return pfHeader;
	}
	
	public CpfAction() {
		addRelatedEntity("tds", Recovery.class);
		addRelatedEntity("pfIntExpAccount",org.egov.commons.CChartOfAccounts.class);
		addRelatedEntity("ruleScript",org.egov.infstr.workflow.Action.class);
	}
	
	public String execute()
	{
		return INDEX;
	}
	
	public void prepare()
	{
		super.prepare();
		addDropdownData("wfActionList", getPersistenceService().findAllByNamedQuery("BY_TYPE", "CPFHeader"));
		addDropdownData("tdsList", getPersistenceService().findAllBy("from Recovery tds where upper(tds.egPartytype.code) ='EMPLOYEE' "));
	}
	
	@SkipValidation
	public String createOrModify()
	{
		pfHeader = (PFHeader) getPersistenceService().find(" from PFHeader where pfType=?", "CPF");
		if(pfHeader==null){
			pfHeader = new PFHeader();
		}
		return NEW;
	}
	
	public String create() //This for create and modify
	{
		pfHeader.setPfType("CPF");
		if(pfHeader.getId()==null)
		{
			try{
				cpfService.create(pfHeader);
			}catch (Exception e) {
				addActionError("Exception ="+e.getMessage());
				return NEW;
			}
			addActionMessage(getMessage("CPF.Create.Msg"));
		}
		else
		{	try{
				cpfService.update(pfHeader);
			}catch (Exception e) {
				addActionError("Exception ="+e.getMessage());
				return NEW;
			}
			addActionMessage(getMessage("CPF.Modify.Msg"));
		}
		target=Action.SUCCESS;
		return NEW;
	}

	protected String getMessage(final String key) {
		return getText(key);
	}

	public void validate()
	{
		if(pfHeader.getTds()==null){
			addFieldError("tds", getMessage("CPF.tds.errMsg"));
		}
		if(pfHeader.getPfIntExpAccount()==null){
			addFieldError("pfIntExpAccount", getMessage("CPF.expAccCode.errMsg"));
		}
		if(pfHeader.getRuleScript()==null){
			addFieldError("ruleScript", getMessage("CPF.RuleScript.errMsg"));
		}
	}

	public void setCpfService(PersistenceService<PFHeader, Long> cpfService) {
		this.cpfService = cpfService;
	}

	public PFHeader getPfHeader() {
		return pfHeader;
	}

	public void setPfHeader(PFHeader pfHeader) {
		this.pfHeader = pfHeader;
	}

	public void setTarget(final String target) {
		this.target = target;
	}

	public String getTarget() {
		return target;
	}
}
