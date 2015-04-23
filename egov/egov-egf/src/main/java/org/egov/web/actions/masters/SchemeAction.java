package org.egov.web.actions.masters;

import org.apache.struts2.convention.annotation.Action;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.commons.Fund;
import org.egov.commons.Scheme;
import org.egov.infstr.ValidationError;
import org.egov.infstr.ValidationException;
import org.egov.infstr.client.filter.EGOVThreadLocals;
import org.egov.infstr.utils.EgovMasterDataCaching;
import org.egov.infstr.utils.ServiceLocator;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.UserService;
import org.egov.lib.rjbac.user.ejb.api.UserServiceHome;
import org.egov.services.masters.SchemeService;
import org.egov.utils.Constants;
import org.egov.web.actions.BaseFormAction;
import org.egov.web.annotation.ValidationErrorPage;

import com.opensymphony.xwork2.validator.annotations.RequiredFieldValidator;
import com.opensymphony.xwork2.validator.annotations.Validations;
            
@ParentPackage("egov")   
public class SchemeAction extends BaseFormAction{

	private Scheme scheme=new Scheme();
	private String mode;
	private static final String		REQUIRED	= "required";
	private static final String		UNIQUECHECKFIELD	= "fieldUniqueCheck";
	private boolean uniqueName=false;
	private boolean uniqueCode=false;
	public static final String SEARCH = "search";
	public static final String VIEW = "view";
	private static final Logger		LOGGER		= Logger.getLogger(SchemeAction.class);
	List<Scheme> schemeList;
	private SchemeService schemeService;
	
	@Override
	public Object getModel() {
		return scheme;
	}
	public SchemeAction(){
		if(LOGGER.isDebugEnabled())     LOGGER.debug("Inside Scheme Action Constructor");
		EgovMasterDataCaching masterCache = EgovMasterDataCaching.getInstance();
		addDropdownData("fundDropDownList",masterCache.get("egi-fund"));   
		addRelatedEntity("fund", Fund.class);
		addRelatedEntity("createdBy",User.class);
	}
	          
	@SkipValidation
@Action(value="/masters/scheme-newForm")
	public String newForm() {
		if(LOGGER.isDebugEnabled())     LOGGER.debug("..Inside NewForm method..");
		this.mode=NEW; 
		scheme.reset();
		return NEW;
	}
	
	@SkipValidation
@Action(value="/masters/scheme-beforeSearch")
	public String beforeSearch() {
		if(LOGGER.isInfoEnabled())     LOGGER.info("Scheme Mode="+mode);
		return SEARCH;
	}
	
	@SkipValidation
@Action(value="/masters/scheme-beforeEdit")
	public String beforeEdit(){
		this.scheme = (Scheme) persistenceService.find("from Scheme where id=?", this.scheme.getId());
		if(LOGGER.isDebugEnabled())     LOGGER.debug("..Inside Before Edit Method..");
		this.mode=EDIT;
		return EDIT;
	}
	
	@SkipValidation
@Action(value="/masters/scheme-beforeView")
	public String beforeView(){
		if(LOGGER.isDebugEnabled())     LOGGER.debug("..Inside Before View Method..");
		this.scheme = (Scheme) persistenceService.find("from Scheme where id=?", this.scheme.getId());
		this.mode=VIEW;
		return VIEW;
	}	
	@SkipValidation
	public String search() {
		if(LOGGER.isDebugEnabled())     LOGGER.debug("Inside Search |Search scheme Action Starts");
		StringBuffer query = new StringBuffer();
		schemeList=new ArrayList<Scheme>();
		query.append("From Scheme scheme");
		
		if(scheme.getFund().getId()!=null){
			query.append(" where scheme.fund="+scheme.getFund().getId());
		}         
		if(scheme.getValidfrom()!=null && scheme.getValidto()!=null){
			query.append(" and scheme.validfrom>='"+ Constants.DDMMYYYYFORMAT1.format(scheme.getValidfrom())+"'")
			.append("and scheme.validto<='"+ Constants.DDMMYYYYFORMAT1.format(scheme.getValidto())+"'");
		}else if(scheme.getValidfrom()!=null){
			query.append(" and scheme.validfrom>='"+ Constants.DDMMYYYYFORMAT1.format(scheme.getValidfrom())+"'");
		}else if(scheme.getValidto()!=null){
			query.append("and scheme.validto<='"+ Constants.DDMMYYYYFORMAT1.format(scheme.getValidto())+"'");
		}	
		query.append("order by scheme.name");
		this.schemeList = persistenceService.findAllBy(query.toString());
		if(LOGGER.isDebugEnabled())     LOGGER.debug("Scheme List Size is"+schemeList.size());
		return SEARCH;
	}
	
	@SuppressWarnings("unchecked")
	public String edit() {
		if (scheme.getIsactive() == null) {
			scheme.setIsactive(false);
		}
		scheme.setLastModifiedBy(getLoggedInUser());
		scheme.setLastModifiedDate(new Date());
		try {
			schemeService.persist(scheme);
		} catch (ValidationException e) {
			LOGGER.error("ValidationException in creating Scheme" + e.getMessage());
			throw e;
		} catch (Exception e) {
			LOGGER.error("Exception while creating Scheme" + e.getMessage());
			throw new ValidationException(Arrays.asList(new ValidationError("An error occured contact Administrator",
					"An error occured contact Administrator")));
		}
		if (LOGGER.isDebugEnabled())
			LOGGER.debug(".................................Scheme Modified Successfully......................");
		addActionMessage(getText("Scheme Modified Successfully"));
		return EDIT;
	}

	@Validations(requiredFields = { @RequiredFieldValidator(fieldName = "fund", message = "", key = REQUIRED),
			@RequiredFieldValidator(fieldName = "code", message = "", key = REQUIRED),
			@RequiredFieldValidator(fieldName = "name", message = "", key = REQUIRED),
			@RequiredFieldValidator(fieldName = "validfrom", message = "", key = REQUIRED),
			@RequiredFieldValidator(fieldName = "validto", message = "", key = REQUIRED) })
	
	@ValidationErrorPage(value=NEW)
	public String create() {    
		if(LOGGER.isDebugEnabled())     LOGGER.debug("............................Creating New Scheme method.......................");
		if (scheme.getIsactive() == null) {
			scheme.setIsactive(false);
		}
		scheme.setCreatedBy(getLoggedInUser());
		scheme.setCreatedDate(new Date());
		try {
			schemeService.persist(scheme);
		} catch (ValidationException e) {
			LOGGER.error("ValidationException in create Scheme" + e.getMessage());
			throw e;
		} catch (Exception e) {
			LOGGER.error("Exception while creating Scheme" + e.getMessage());
			throw new ValidationException(Arrays.asList(new ValidationError("An error occured contact Administrator",
					"An error occured contact Administrator")));
		}

		addActionMessage(getText("Scheme Created Successfully"));
		if (LOGGER.isDebugEnabled())
			LOGGER.debug("SchemeAction  | Scheme Created successfully");
		return NEW;
	}
	
	@SkipValidation
	public boolean getCheckField(){
		Scheme scheme_validate = null;
		boolean isDuplicate = false;
		if(LOGGER.isDebugEnabled())     LOGGER.debug("......Scheme Unique check Begins......");
		if(uniqueCode){
			if (!this.scheme.getCode().equals("") && this.scheme.getId()!=null)
				scheme_validate = (Scheme)persistenceService.find("from Scheme where code=? and id!=?", this.scheme.getCode(), this.scheme.getId());
			else if(!this.scheme.getCode().equals(""))
				scheme_validate = (Scheme)persistenceService.find("from Scheme where code=?", this.scheme.getCode());
			uniqueCode=false;		
		}else{
			if (!this.scheme.getName().equals("") && this.scheme.getId()!=null)
				scheme_validate = (Scheme)persistenceService.find("from Scheme where name=? and id!=?", this.scheme.getName(), this.scheme.getId());
			else if(!this.scheme.getName().equals(""))
				scheme_validate = (Scheme)persistenceService.find("from Scheme where name=?", this.scheme.getName());
			uniqueName=false;
		}
		if(scheme_validate!=null){
			isDuplicate=true;
		}
		if(LOGGER.isDebugEnabled())     LOGGER.debug("......Scheme Unique check processed......");
		return isDuplicate;
	}

	@SkipValidation
@Action(value="/masters/scheme-codeUniqueCheck")
	public String codeUniqueCheck(){
		if(LOGGER.isInfoEnabled())     LOGGER.info("......Scheme Unique check for code......");
		uniqueCode=true;
		return UNIQUECHECKFIELD;
	}
	
	@SkipValidation
@Action(value="/masters/scheme-nameUniqueCheck")
	public String nameUniqueCheck(){
		if(LOGGER.isInfoEnabled())     LOGGER.info("......Scheme Unique check for Name......");
		uniqueName=true;
		return UNIQUECHECKFIELD;
	}
	
	private User getLoggedInUser() {
		 return (User)persistenceService.load(User.class, Integer.valueOf(EGOVThreadLocals.getUserId()));
	}
	
	public Scheme getScheme() {
		return scheme;
	}

	public void setScheme(Scheme scheme) {
		this.scheme = scheme;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}
	public List<Scheme> getSchemeList() {
		return schemeList;
	}
	public void setSchemeList(List<Scheme> schemeList) {
		this.schemeList = schemeList;
	}
	public SchemeService getSchemeService() {
		return schemeService;
	}
	public void setSchemeService(SchemeService schemeService) {
		this.schemeService = schemeService;
	}
	
}
