package org.egov.eb.web.action.master;

import static org.egov.utils.FinancialConstants.REGEXP_ALPHANUMERIC_DOT_COLON_SLASH;
import static org.egov.utils.FinancialConstants.REGEXP_ALPHANUMERIC_DOT_SLASH;
import static org.egov.utils.FinancialConstants.REQUIRED;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.commons.Accountdetailkey;
import org.egov.commons.Accountdetailtype;
import org.egov.eb.domain.master.entity.EBConsumer;
import org.egov.eb.domain.master.entity.TargetArea;
import org.egov.eb.service.master.EBConsumerService;
import org.egov.eb.service.master.EBDetailsService;
import org.egov.eb.service.master.TargetAreaService;
import org.egov.eb.utils.EBUtils;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.User;
import org.egov.infstr.ValidationError;
import org.egov.infstr.ValidationException;
import org.egov.infstr.client.filter.EGOVThreadLocals;
import org.egov.infstr.search.SearchQuery;
import org.egov.infstr.search.SearchQueryHQL;
import org.egov.services.masters.AccountdetailkeyService;
import org.egov.utils.FinancialConstants;
import org.egov.utils.VoucherHelper;
import org.egov.web.actions.SearchFormAction;
import org.egov.web.annotation.ValidationErrorPage;
import org.egov.web.utils.EgovPaginatedList;
import org.hibernate.FlushMode;

import com.opensymphony.xwork2.validator.annotations.RegexFieldValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredFieldValidator;
import com.opensymphony.xwork2.validator.annotations.Validations;

            
@ParentPackage("egov")   
public class EBConsumerAction extends SearchFormAction{

	private static final Logger	LOGGER = Logger.getLogger(EBConsumerAction.class);

	private EBConsumer consumer=new EBConsumer();

	private String mode;
	private String hasValidEBDetails;
	private boolean isDuplicate;
  
	private List<EBConsumer> eBConsumerList;
	private EgovPaginatedList paginatedList;
	
	private EBConsumerService eBConsumerService;
	private EBDetailsService ebDetailsService;
	private AccountdetailkeyService accountdetailkeyService;
	private TargetAreaService targetAreaService;

	
	public EBConsumerAction() {
		super();
		addRelatedEntity("ward", Boundary.class);
	}
	
	@Override
	public Object getModel() {
		return consumer;
	}
		
	@Override
	public SearchQuery prepareQuery(String sortField, String sortOrder) {
		
		if(LOGGER.isDebugEnabled())     LOGGER.debug("EBConsumerAction | prepare | start");
		String query = getQuery();
		StringBuffer srchQry = new StringBuffer("select distinct consumer "+query+"");
		StringBuffer countQry = new StringBuffer("select count(distinct consumer) "+query+"");
		if(LOGGER.isDebugEnabled())     LOGGER.debug("EBConsumerAction | prepare | query >> "+ query);
		
		return new SearchQueryHQL(srchQry.toString(),countQry.toString(),null);
	}
	public void prepareNewForm() {
		super.prepare();
	
		if(LOGGER.isDebugEnabled())     LOGGER.debug("Inside  Prepare ........");
		
		addDropdownData("targetAreaDropDownList", targetAreaService.getAllTargetAreas());
		addDropdownData("regionsList", VoucherHelper.TNEB_REGIONS);
		addDropdownData("billingList", EBUtils.TNEB_BILLING_TYPES);
		addDropdownData("wardsList", persistenceService.findAllBy("from Boundary where boundaryType.id in(select id from BoundaryType where name='Ward')"));
		
		if(LOGGER.isDebugEnabled())     LOGGER.debug(parameters);
		
	}
	          

	@SkipValidation
@Action(value="/master/eBConsumer-newForm")
	public String newForm() {
		if(LOGGER.isDebugEnabled())     LOGGER.debug("..Inside NewForm method..");
		this.consumer.setIsActive(true);
		return FinancialConstants.STRUTS_RESULT_PAGE_NEW;
	}
	
	@SkipValidation
@Action(value="/master/eBConsumer-beforeSearch")
	public String beforeSearch() {
		prepareNewForm();
		if(LOGGER.isInfoEnabled())     LOGGER.info("EBConsumer Mode="+mode);
		return FinancialConstants.STRUTS_RESULT_PAGE_SEARCH;
	}
	
	/**
	 * 
	 * @return
	 * if the consumer is used in EB bill then dont allow to modify account number and consumer number.
	 */
	@SkipValidation
@Action(value="/master/eBConsumer-beforeEdit")
	public String beforeEdit(){
		prepareNewForm();
	/*	this.consumer = (EBConsumer) eBConsumerService.find("from EBConsumer where id=?", this.consumer.getId());
		if(consumer.getWard()!=null){
		TargetArea ta = targetAreaService.find("select ta from TargetArea ta ,TargetAreaMappings tam where ta.id = tam.area.id and tam.boundary.id=?",consumer.getWard().getId());
		if(ta!=null){
		if(ta.getName()!=null){
			consumer.setTargetArea(ta.getName());
		       }
		   }
		}*///This fix is for Phoenix Migration.
		 if(ebDetailsService.hasValidEBDetails(consumer.getId())){
			hasValidEBDetails = "true";
		 }
		
		if(LOGGER.isDebugEnabled())     LOGGER.debug("..Inside Before Edit Method..");
		
		this.mode=FinancialConstants.STRUTS_RESULT_PAGE_EDIT;
		
		return FinancialConstants.STRUTS_RESULT_PAGE_EDIT;
	}
	

	@SkipValidation
@Action(value="/master/eBConsumer-beforeView")
	public String beforeView(){
		if(LOGGER.isDebugEnabled())     LOGGER.debug("..Inside Before View Method..");
		/*
		this.consumer = (EBConsumer) eBConsumerService.find("from EBConsumer where id=?", this.consumer.getId());
		if(consumer.getWard()!=null){
		TargetArea ta = targetAreaService.find("select ta from TargetArea ta ,TargetAreaMappings tam where ta.id = tam.area.id and tam.boundary.id=?",consumer.getWard().getId());
		if(ta!=null){
		if(ta.getName()!=null){
			consumer.setTargetArea(ta.getName());
		      }
		   }
		}*///This fix is for Phoenix Migration.
		this.mode=FinancialConstants.STRUTS_RESULT_PAGE_VIEW;
		return FinancialConstants.STRUTS_RESULT_PAGE_VIEW ;
	}	
	
	@SuppressWarnings("unchecked")
	public String edit() {
		if (consumer.getIsActive() == null) {
			consumer.setIsActive(false);
		}
		consumer.setModifiedBy(getLoggedInUser());
		consumer.setModifiedDate(new Date());
		try {
			//eBConsumerService.persist(consumer);
		} catch (ValidationException e) {
			LOGGER.error("ValidationException in creating EBConsumer" + e.getMessage());
			throw e;
		} catch (Exception e) {
			LOGGER.error("Exception while creating EBConsumer" + e.getMessage());
			throw new ValidationException(Arrays.asList(new ValidationError("An error occured contact Administrator",
					"An error occured contact Administrator")));
		}
		if (LOGGER.isDebugEnabled())
			LOGGER.debug(".................................EBConsumer Modified Successfully......................");
		addActionMessage(getText("TNEB Account Modified Successfully"));
		prepareNewForm();
		mode = "modify";
		return EDIT;
	}

	@Validations(
			requiredFields = { 
					@RequiredFieldValidator(fieldName = "code", message = "", key = REQUIRED),
					@RequiredFieldValidator(fieldName = "name", message = "", key = REQUIRED)
					
					/*@RequiredFieldValidator(fieldName = "region", message = "", key = REQUIRED),
					@RequiredFieldValidator(fieldName = "targetArea", message = "", key = REQUIRED),
					@RequiredFieldValidator(fieldName = "ward", message = "", key = REQUIRED),
					@RequiredFieldValidator(fieldName = "oddOrEvenBilling", message = "", key = REQUIRED)*/
					},
			regexFields = {
					/*@RegexFieldValidator(fieldName = "code", expression = REGEXP_ALPHANUMERIC_DOT_SLASH, message = "", key = "message.validation.validChar"),
					@RegexFieldValidator(fieldName = "name", expression = REGEXP_ALPHANUMERIC_DOT_COLON_SLASH, message = "", key = "message.validation.validChar")*/
			})//This fix is for Phoenix Migration.

	@ValidationErrorPage(value=NEW)
	public String create() {    
		if(LOGGER.isDebugEnabled())     LOGGER.debug("............................Creating New EBConsumer .......................");
		List resultSet;
		
		if (consumer.getIsActive() == null) {
			consumer.setIsActive(false);
		}
		
		consumer.setCreatedBy(getLoggedInUser());
		consumer.setCreatedDate(new Date());
		try {
			/*resultSet =  eBConsumerService.findAllBy("from EBConsumer where upper(code)=? or upper(name) = ?", consumer.getCode().toUpperCase(),consumer.getName().toUpperCase());
			if(resultSet.size()==0){
				eBConsumerService.persist(consumer);
			createAccountdetailkey();
			}else{
				addActionMessage(getText("TNEB Account Already exists"));
			}*/
		} catch (ValidationException e) {
			LOGGER.error("ValidationException in create EBConsumer" + e.getMessage());
			throw e;
		} catch (Exception e) {
			LOGGER.error("Exception while creating EBConsumer" + e.getMessage());
			throw new ValidationException(Arrays.asList(new ValidationError("An error occured contact Administrator",
					"An error occured contact Administrator")));
		}
/*		
		if(resultSet.size()==0){
		addActionMessage(getText("TNEB Account Created Successfully"));
		}
		*/
		if (LOGGER.isDebugEnabled()) 			LOGGER.debug("TNEB Account Created successfully");
		prepareNewForm();
		mode = "create";
		return NEW;
	}
	
	private void createAccountdetailkey() {   
		
		Accountdetailkey accountdetailkey = new Accountdetailkey();
		Accountdetailtype accountdetailtype = new Accountdetailtype(); 
		try {
			
		accountdetailtype = (Accountdetailtype) persistenceService.find("from Accountdetailtype where  name = ?","Electricity");
		accountdetailkey.setAccountdetailtype(accountdetailtype);
		accountdetailkey.setDetailname("Electricity_id");
		accountdetailkey.setGroupid(1);
		accountdetailkey.setDetailkey(consumer.getId().intValue());
		//accountdetailkeyServiceHibernateUtil.getCurrentSession().save(accountdetailkey);
		
		}catch (Exception e) {
			LOGGER.error("Exception while creating Accountdetailkey" + e.getMessage());
			throw new ValidationException(Arrays.asList(new ValidationError("Unable to Add AccountDetail",
					"Unable to Add AccountDetail")));
		}
		
	}
	
	     
	@SkipValidation
@Action(value="/master/eBConsumer-isCodeUnique")
	public String isCodeUnique(){
		if(LOGGER.isInfoEnabled())     LOGGER.info("......Consumer Unique check for code......");
		
		if(!this.consumer.getCode().equalsIgnoreCase("")){
			if(eBConsumerService.isCodeUnique(this.consumer.getCode(),this.consumer.getId())){
			
			isDuplicate = false;
			
			}else{		
				
			isDuplicate = true;
			}
			
	   }else{
		   
		   	isDuplicate = false;
		   
	   }
		
		return FinancialConstants.STRUTS_RESULT_PAGE_UNIQUECHECK;
	}
	
	
	@SkipValidation
@Action(value="/master/eBConsumer-isNameUnique")
	public String isNameUnique(){
		
		if(LOGGER.isInfoEnabled())     LOGGER.info("...... Consumer Unique check for name......");
		
		if(!consumer.getName().equalsIgnoreCase("")){
				if(eBConsumerService.isNameUnique(consumer.getName(),consumer.getId())){
			
						isDuplicate = false;
			
					}else{
			
						isDuplicate = true;
			
					}
		}else{
			   
				isDuplicate = false;
			   
		   }
		
		return FinancialConstants.STRUTS_RESULT_PAGE_UNIQUECHECK;
	}
	
	@SkipValidation
	public String Search() throws Exception{
		//HibernateUtil.getCurrentSession().setDefaultReadOnly(true);
	//HibernateUtil.getCurrentSession().setFlushMode(FlushMode.MANUAL);*/
		if(LOGGER.isDebugEnabled())     LOGGER.debug("EBConsumerAction | Search | start");
		super.search();
		paginatedList = (EgovPaginatedList) searchResult;
		if(LOGGER.isDebugEnabled())     LOGGER.debug("EBConsumerAction | list | End");
		prepareNewForm();	
			
		return FinancialConstants.STRUTS_RESULT_PAGE_SEARCH;
		
	}
	@SkipValidation
	public String getQuery(){
		
		if(LOGGER.isDebugEnabled())     LOGGER.debug("Inside getData |Search EBConsumer Action Starts");
		
		StringBuffer query = new StringBuffer();
		query.append("From EBConsumer consumer where consumer.id is not null ");
		if((consumer.getCode()!=null && !consumer.getCode().equals(""))){
			query.append(" and consumer.code= '"+consumer.getCode() + "'");
		}   
		
		if((consumer.getName()!=null && !consumer.getName().equals(""))){
			query.append(" and consumer.name= '"+consumer.getName()+ "'");
		}   
		
		if((consumer.getRegion()!=null && !consumer.getRegion().equals(""))){
			query.append(" and consumer.region='"+consumer.getRegion()+ "'");
		}   
		
		if((consumer.getOddOrEvenBilling()!=null && !consumer.getOddOrEvenBilling().equals(""))){
			query.append(" and consumer.oddOrEvenBilling='"+consumer.getOddOrEvenBilling()+ "'");
		}   
		if((consumer.getWard()!=null && !consumer.getWard().equals(""))){
			query.append(" and consumer.ward.name  = '"+consumer.getWard()+"'");
		}   
		
		if((consumer.getTargetArea()!=null && !consumer.getTargetArea().equals(""))){
			query.append(" and consumer.ward.id in (select tam.boundary.id from TargetAreaMappings tam, TargetArea ta where tam.area.id = ta.id and ta .name = '"+consumer.getTargetArea()+"')");
		}   
		
		query.append(" order by consumer.code");
		
		return query.toString();
	}
	
	private User getLoggedInUser() {
		 return null;////This fix is for Phoenix Migration.(User)persistenceService.load(User.class, Integer.valueOf(EGOVThreadLocals.getUserId()));
	}
	
	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}
	public EBConsumer getConsumer() {
		return consumer;
	}
	public void setConsumer(EBConsumer consumer) {
		this.consumer = consumer;
	}
	public List<EBConsumer> getEBConsumerList() {
		return eBConsumerList;
	}
	public void setEBConsumerList(List<EBConsumer> eBConsumerList) {
		this.eBConsumerList = eBConsumerList;
	}
	public EBConsumerService geteBConsumerService() {
		return eBConsumerService;
	}

	public void seteBConsumerService(EBConsumerService eBConsumerService) {
		this.eBConsumerService = eBConsumerService;
	}

	public AccountdetailkeyService getAccountdetailkeyService() {
		return accountdetailkeyService;
	}
	public void setAccountdetailkeyService(
			AccountdetailkeyService accountdetailkeyService) {
		this.accountdetailkeyService = accountdetailkeyService;
	}
	public EBDetailsService getEbDetailsService() {
		return ebDetailsService;
	}
	public void setEbDetailsService(EBDetailsService ebDetailsService) {
		this.ebDetailsService = ebDetailsService;
	}
	public void setTargetAreaService(TargetAreaService targetAreaService) {
		this.targetAreaService = targetAreaService;
	}
	
	public boolean getIsDuplicate() {
		return isDuplicate;
	}

	public String getHasValidEBDetails() {
		return hasValidEBDetails;
	}

	public void setHasValidEBDetails(String hasValidEBDetails) {
		this.hasValidEBDetails = hasValidEBDetails;
	}

	public TargetAreaService getTargetAreaService() {
		return targetAreaService;
	}
	public void setDuplicate(boolean isDuplicate) {
		this.isDuplicate = isDuplicate;
	}

	public EgovPaginatedList getPaginatedList() {
		return paginatedList;
	}

	public void setPaginatedList(EgovPaginatedList paginatedList) {
		this.paginatedList = paginatedList;
	}

	

	
}
