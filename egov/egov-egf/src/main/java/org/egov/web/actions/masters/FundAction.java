package org.egov.web.actions.masters;

import org.apache.struts2.convention.annotation.Action;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.commons.EgfAccountcodePurpose;
import org.egov.commons.Fund;
import org.egov.infstr.ValidationError;
import org.egov.infstr.ValidationException;
import org.egov.infstr.utils.EgovMasterDataCaching;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.web.actions.BaseFormAction;
import org.egov.web.annotation.ValidationErrorPage;

import com.opensymphony.xwork2.validator.annotations.Validation;

@ParentPackage("egov")
@Validation()
public class FundAction extends BaseFormAction{

	private static final long serialVersionUID = -1076021355881784888L;
	private Fund fund = new Fund();
	private boolean clearValues = false;
	private boolean close = false;
	private String showMode = "view";
	private List<Fund> fundList;
	private List<Fund> fundSearchList = new ArrayList<Fund>();
	public static final String ACC_CODE_PURPOSE = "Bank Codes";
	protected static final Logger LOGGER = Logger.getLogger(FundAction.class);
	private String success = "";

	
	@SkipValidation
	public Object getModel() {
		return fund;
	}

	@Override
	public void prepare() {
		super.prepare();
		dropdownData.put("fundList", persistenceService
				.findAllBy("from Fund where isActive=1 order by name"));
	}

	@SkipValidation
@Action(value="/masters/fund-newform")
	public String newform() {
		return NEW;
	}
	
	private boolean getParentIsNotLeaf(Fund fund) {
		boolean isNotLeaf = false;
		
		if (fund.getFund() != null && fund.getFund().getId() != null) {
			
			List<Fund> fundList = new ArrayList<Fund>(persistenceService.findAllBy("from Fund where fund.id=?",
					fund.getFund().getId()));
			if (fundList.size() != 0) {
				isNotLeaf = true;
			}
		}
		
		return isNotLeaf;
	}
	
	@ValidationErrorPage(NEW)
	@SuppressWarnings("unchecked")
	public String create() {
		StringBuffer fundNameStr = new StringBuffer();
		BigDecimal parentLevel = BigDecimal.ZERO;
		Fund parentFund = null;
		try {
			validatemandatoryFields_create();
			
			EgovMasterDataCaching.getInstance().removeFromCache("egi-fund");
			fundNameStr.append(fund.getCode()).append("-").append(fund.getName());
			fund.setName(fundNameStr.toString());
			fund.setCode(fund.getCode());
			fund.setIsnotleaf(false);
			fund.setLlevel(parentLevel);
			fund.setChartofaccountsByPayglcodeid(null);
			
			if (fund.getFund() != null && fund.getFund().getId() != null) {
				parentFund = (Fund) persistenceService.find("from Fund where id=?", fund.getFund().getId());
				parentFund.setIsnotleaf(true);
				parentLevel = parentFund.getLlevel().add(BigDecimal.ONE);
			}
			fund.setLlevel(parentLevel);
			EgfAccountcodePurpose accCodePurpose = (EgfAccountcodePurpose) persistenceService.
					find("From EgfAccountcodePurpose where name = '" + ACC_CODE_PURPOSE + "'");
			fund.setEgfAccountcodePurpose(accCodePurpose);
			fund.setFund(parentFund);
				
			persistenceService.setType(Fund.class);
			persistenceService.persist(fund);
			setSuccess("yes");
		} catch (Exception e) {
			setSuccess("no");
			LOGGER.error("Exception occurred in FundAction-create ", e);
             
            throw new EGOVRuntimeException("Exception occurred in FundAction-create ", e);
		}
		
		clearValues = true;
		return NEW;
	}
	
	@ValidationErrorPage(value = "edit")
	@SuppressWarnings("unchecked")
	public String edit() {
		StringBuffer fundNameStr = new StringBuffer();
		BigDecimal parentLevel = BigDecimal.ZERO;
		Fund parentFund = null;
		validatemandatoryFields();
		
		try {
			EgovMasterDataCaching.getInstance().removeFromCache("egi-fund");
			Fund fundOld = (Fund) persistenceService.find("from Fund where id=?", fund.getId());
			if (fund.getFund() != null && fund.getFund().getId() != null) {
				parentFund = (Fund) persistenceService.find("from Fund where id=?",
						fund.getFund().getId());
				parentLevel = parentFund.getLlevel().add(BigDecimal.ONE);
			}
			
			//check if the old and the new parent fund are not the same. 
			if (fund.getFund() != null && fund.getFund().getId() != null && fundOld.getFund() != null
					&& fundOld.getFund().getId() != null) {
				if (!fundOld.getFund().getId().equals(fund.getFund().getId())) {
					Fund oldParentFund = (Fund) persistenceService.find("from Fund where id=?", fundOld.getFund().getId());
					// setting the existing(old) parent fund isNotLeaf value
					oldParentFund.setIsnotleaf(getParentIsNotLeaf(fundOld));
					persistenceService.setType(Fund.class);
					persistenceService.update(oldParentFund);
				}
			}
			

			fundNameStr.append(fund.getCode()).append("-").append(""/*fund.getFundNameActual()*/);//phoenix Migration 
			fundOld.setName(fundNameStr.toString());
			fundOld.setCode(fund.getCode());
			fundOld.setIdentifier(fund.getIdentifier());
			fundOld.setIsactive(fund.isIsactive());
			fundOld.setLlevel(parentLevel);
			fundOld.setLastmodified(new Date());
			fundOld.setModifiedby(getLoggedInUser());
		//	fundOld.setFundNameActual(fund.getFundNameActual());//phoenix Migration
			/*if (fund.getChartofaccountsByPayglcodeid() != null) {
				fundOld.setChartofaccountsByPayglcodeid(fund.getChartofaccountsByPayglcodeid());
			}*/
			if (fund.getFund() != null && fund.getFund().getId() != null) {
				parentFund.setIsnotleaf(true);
			}
			
			fundOld.setFund(parentFund);
			setFund(fundOld);
			persistenceService.setType(Fund.class);
			persistenceService.persist(fund);
			if(fund.getName().contains("-"))
				//phoenix Migration fund.setFundNameActual(fund.getName().split("-")[1]);
				;
			else
				//phoenix Migration fund.setFundNameActual(fund.getName());
			setSuccess("yes");
			
		} catch (Exception e) {
			setSuccess("no");
			LOGGER.error("Exception occurred in FundAction-edit ", e);
             
            throw new EGOVRuntimeException("Exception occurred in FundAction-edit ", e);
		}
		showMode = "edit";
		return EDIT;
	}
	
	@SkipValidation
@Action(value="/masters/fund-beforeSearch")
	public String beforeSearch() {
		return "search";
	}
	
	@SuppressWarnings("unchecked")
	@SkipValidation
	public String search() {
		StringBuffer query = new StringBuffer();

		query.append("From Fund");
		if (!fund.getCode().equals("") && !fund.getName().equals("")) {
			query.append(" where upper(code) like upper('%" + fund.getCode()
					+ "%') and upper(name) like upper('%" + fund.getName() + "%')");
		} else {
			if (!fund.getCode().isEmpty())
				query.append(" where upper(code) like upper('%" + fund.getCode() + "%')");
			if (!fund.getName().isEmpty())
				query.append(" where upper(name) like upper('%" + fund.getName() + "%')");
		}
		List<Fund> fList = persistenceService.findAllBy(query.toString());

		for (Fund f : fList) {
			fundSearchList.add(f);
		}
		return "search";
	}
	
	@SkipValidation
@Action(value="/masters/fund-beforeModify")
	public String beforeModify() {
		fund = (Fund) persistenceService.find("from Fund where id=?", fund.getId());
		
	 	  if (fund.getName().contains("-")) {
			String fundName[] = fund.getName().split("-");
			//phoenix Migration fund.setFundNameActual(fundName[1]);
	    }
		return EDIT;
	}
	
	
	@SkipValidation
	public boolean getCheckCode() {
		Fund f = null;
		boolean isDuplicate = false;
		if (!this.fund.getCode().equals("") && this.fund.getId() != null)
			f = (Fund) persistenceService.find("from Fund where code=? and id!=?",
					this.fund.getCode(), this.fund.getId());
		else if (!this.fund.getCode().equals(""))
			f = (Fund) persistenceService.find("from Fund where code=?",
							this.fund.getCode());
		if (f != null) {
			isDuplicate = true;
		}
		return isDuplicate;
	}
	
	@SkipValidation
	public boolean getCheckIdentifier() {
		Fund f = null;
		boolean isDuplicate = false;
		if (!this.fund.getIdentifier().equals("") && this.fund.getId() != null)
			f = (Fund) persistenceService.find("from Fund where identifier=? and id!=?",
					this.fund.getIdentifier(), this.fund.getId());
		else if (!this.fund.getIdentifier().equals(""))
			f = (Fund) persistenceService.find("from Fund where identifier=?",
							this.fund.getIdentifier());
		if (f != null) {
			isDuplicate = true;
		}
		return isDuplicate;
	}
	
	@SkipValidation
	private BigDecimal getLoggedInUser() {
		Integer uid = (Integer)getSession().get("com.egov.user.LoginUserId");
		BigDecimal userId = new BigDecimal(uid.toString());
		return userId;
	}
	
	private void validatemandatoryFields() {
		if (fund.getCode() == null || "".equals(fund.getCode())) {
			throw new ValidationException(Arrays.asList(new ValidationError(
					"fund.code.mandatory", getText("mandatory.fund.code"))));
		}
		/*if (fund.getFundNameActual() == null || "".equals(fund.getFundNameActual())) {
			throw new ValidationException(Arrays.asList(new ValidationError(
					"fund.name.mandatory", getText("mandatory.fund.actualname"))));
		}*///phoenix Migration 
		if (fund.getCode() != null) {
			if(getCheckCode()) {
				throw new ValidationException(Arrays.asList(new ValidationError(
						"Fund.code.unique", getText("Fund.code.unique"))));
			}
			if (fund.getCode().contains("-")) {
				throw new ValidationException(Arrays.asList(new ValidationError(
						"validation.fund.code", getText("validation.fund.code"))));
			}
		}
		if (fund.getIdentifier() != null) {
			if(getCheckIdentifier()) {
				throw new ValidationException(Arrays.asList(new ValidationError(
						"Fund.identifier.unique", getText("Fund.identifier.unique"))));
			}
		}
	}

	private void validatemandatoryFields_create() {
		if (fund.getCode() == null || "".equals(fund.getCode())) {
			throw new ValidationException(Arrays.asList(new ValidationError(
					"fund.code.mandatory", getText("mandatory.fund.code"))));
		}
		if (fund.getName() == null || "".equals(fund.getName())) {
			throw new ValidationException(Arrays.asList(new ValidationError(
					"fund.name.mandatory", getText("mandatory.fund.name"))));
		}
		if (fund.getCode() != null) {
			if(getCheckCode()) {
				throw new ValidationException(Arrays.asList(new ValidationError(
						"Fund.code.unique", getText("Fund.code.unique"))));
			}
			if (fund.getCode().contains("-")) {
				throw new ValidationException(Arrays.asList(new ValidationError(
						"validation.fund.code", getText("validation.fund.code"))));
			}
		}
		if (fund.getIdentifier() != null) {
			if(getCheckIdentifier()) {
				throw new ValidationException(Arrays.asList(new ValidationError(
						"Fund.identifier.unique", getText("Fund.identifier.unique"))));
			}
		}
	}

	public void setClearValues(boolean clearValues) {
		this.clearValues = clearValues;
	}

	public boolean isClearValues() {
		return clearValues;
	}

	public Fund getFund() {
		return fund;
	}

	public void setFund(Fund fund) {
		this.fund = fund;
	}
	
	public void setClose(boolean close) {
		this.close = close;
	}
	public boolean isClose() {
		return close;
	}

	public String getShowMode() {
		return showMode;
	}

	public void setShowMode(String showMode) {
		this.showMode = showMode;
	}

	public List<Fund> getFundList() {
		return fundList;
	}

	public void setFundList(List<Fund> fundList) {
		this.fundList = fundList;
	}

	public List<Fund> getFundSearchList() {
		return fundSearchList;
	}

	public void setFundSearchList(List<Fund> fundSearchList) {
		this.fundSearchList = fundSearchList;
	}

	public String getSuccess() {
		return success;
	}

	public void setSuccess(String success) {
		this.success = success;
	}
	
}
