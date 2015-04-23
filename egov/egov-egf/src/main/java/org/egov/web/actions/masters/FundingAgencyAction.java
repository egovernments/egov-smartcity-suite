package org.egov.web.actions.masters;

import org.apache.struts2.convention.annotation.Action;
import java.util.Arrays;
import java.util.List;

import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.commons.Accountdetailkey;
import org.egov.commons.Accountdetailtype;
import org.egov.egf.masters.model.FundingAgency;
import org.egov.infstr.ValidationError;
import org.egov.infstr.ValidationException;
import org.egov.web.actions.BaseFormAction;
import org.egov.web.annotation.ValidationErrorPage;
import org.hibernate.exception.ConstraintViolationException;


public class FundingAgencyAction extends BaseFormAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3276832532518985316L;
	private FundingAgency fundingAgency = new FundingAgency();
	private boolean clearValues = false;
	List<FundingAgency> fundingAgencyList;
	
	
	@Override
	public Object getModel() {
		return fundingAgency;
	}
	@SkipValidation
@Action(value="/masters/fundingAgency-newForm")
	public String newForm() {
		return NEW;
	}
	
	@SkipValidation
@Action(value="/masters/fundingAgency-beforeSearch")
	public String beforeSearch() {
		return "search";
	}
	@ValidationErrorPage(NEW)
	@SuppressWarnings("unchecked")
	public String create() {
		try {
			//validateMandatory();
			persistenceService.setType(FundingAgency.class);
			persistenceService.persist(fundingAgency);
			//save to accountdetail key also
			Accountdetailkey ac=new Accountdetailkey();
			Accountdetailtype adt =(Accountdetailtype) persistenceService.find("From Accountdetailtype where name='FundingAgency'");
			ac.setGroupid(1);
			ac.setAccountdetailtype(adt);
			ac.setDetailkey(fundingAgency.getId().intValue());
			ac.setDetailname(fundingAgency.getName());
			persistenceService.setType(Accountdetailkey.class);
			persistenceService.persist(ac);
			persistenceService.setType(FundingAgency.class);
		}catch (ValidationException e) {
			throw e;
		}catch(ConstraintViolationException e){
			throw new ValidationException(Arrays.asList(new ValidationError("Duplicate FundingAgency","Duplicate FundingAgency")));
		}catch (Exception e){
			throw new ValidationException(Arrays.asList(new ValidationError("An error occured contact Administrator","An error occured contact Administrator")));
		}
		addActionMessage(getText("Funding Agency Created Successfully"));
		clearValues = true;
		return NEW;
	}
	
	@SuppressWarnings("unchecked")
	@SkipValidation
	public String search() {
		StringBuffer query = new StringBuffer();
		query.append("From FundingAgency");
		if(!fundingAgency.getCode().equals("") && !fundingAgency.getName().equals("")){
			query.append(" where code like '%"+fundingAgency.getCode()+"%' and name like '%"+fundingAgency.getName()+"%'");
		}
		else{
			if(!fundingAgency.getCode().isEmpty())
				query.append(" where code like '%"+fundingAgency.getCode()+"%'");
			if(!fundingAgency.getName().isEmpty())
				query.append(" where name like '%"+fundingAgency.getName()+"%'");
		}
		this.fundingAgencyList = persistenceService.findAllBy(query.toString());
		return "search";
	}
	@SkipValidation
@Action(value="/masters/fundingAgency-beforeEdit")
	public String beforeEdit(){
		this.fundingAgency = (FundingAgency) persistenceService.find("from FundingAgency where id=?", this.fundingAgency.getId());
		
		return EDIT;
	}
	//edit
	
	@SuppressWarnings("unchecked")
	public String edit(){
		persistenceService.setType(FundingAgency.class);
		persistenceService.persist(fundingAgency);
		//save to accountdetail key also
		Accountdetailtype adt =(Accountdetailtype) persistenceService.find("From Accountdetailtype where name='FundingAgency'");
		Accountdetailkey ac= (Accountdetailkey)persistenceService.find("from Accountdetailkey where accountdetailtype=? and detailkey=?",adt,fundingAgency.getId().intValue());
		ac.setDetailname(fundingAgency.getName());
		persistenceService.setType(Accountdetailkey.class);
		persistenceService.persist(ac);
		addActionMessage(getText("Funding Agency Modified Successfully"));
		return EDIT;
	}

	@SkipValidation
@Action(value="/masters/fundingAgency-codeUniqueCheckCode")
	public String codeUniqueCheckCode(){
		return "codeUniqueCheckCode";
	}
	
	public void validate(){
		if(this.fundingAgency.getCode().equals("")){
			addFieldError("code", getText("fundingagency.code.required"));
			//throw new ValidationException(Arrays.asList(new ValidationError("fundingagency.code.required","fundingagency.code.required")));
		}
		if(this.fundingAgency.getName().equals("")){
			addFieldError("name", getText("fundingagency.name.required"));
			//throw new ValidationException(Arrays.asList(new ValidationError("fundingagency.name.required","fundingagency.name.required")));
		}
	}

	public FundingAgency getFundingAgency() {
		return fundingAgency;
	}

	public void setFundingAgency(FundingAgency fundingAgency) {
		this.fundingAgency = fundingAgency;
	}

	public List<FundingAgency> getFundingAgencyList() {
		return fundingAgencyList;
	}

	public void setFundingAgencyList(List<FundingAgency> fundingAgencyList) {
		this.fundingAgencyList = fundingAgencyList;
	}
	
	@SkipValidation
	public boolean getCodeCheckCode(){
		FundingAgency fa = null;
		boolean isDuplicate = false;
		if (!this.fundingAgency.getCode().equals("") && this.fundingAgency.getId()!=null)
			fa = (FundingAgency)persistenceService.find("from FundingAgency where code=? and id!=?", this.fundingAgency.getCode(), this.fundingAgency.getId());
		else if(!this.fundingAgency.getCode().equals(""))
			fa = (FundingAgency)persistenceService.find("from FundingAgency where code=?", this.fundingAgency.getCode());
		if(fa!=null){
			isDuplicate=true;
		}
		return isDuplicate;
	}
	public boolean isClearValues() {
		return clearValues;
	}
	public void setClearValues(boolean clearValues) {
		this.clearValues = clearValues;
	}

}
