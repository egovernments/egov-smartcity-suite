/**
 * 
 */
package org.egov.erpcollection.web.actions.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.config.ParentPackage;
import org.egov.commons.Accountdetailtype;
import org.egov.commons.CChartOfAccountDetail;
import org.egov.commons.CChartOfAccounts;
import org.egov.commons.CFunction;
import org.egov.commons.Functionary;
import org.egov.commons.Fund;
import org.egov.commons.Fundsource;
import org.egov.commons.Scheme;
import org.egov.commons.SubScheme;
import org.egov.erpcollection.web.constants.CollectionConstants;
import org.egov.infstr.models.ServiceAccountDetails;
import org.egov.infstr.models.ServiceCategory;
import org.egov.infstr.models.ServiceDetails;
import org.egov.infstr.models.ServiceSubledgerInfo;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.utils.EgovMasterDataCaching;
import org.egov.lib.rjbac.dept.DepartmentImpl;
import org.egov.web.actions.BaseFormAction;
import org.egov.web.annotation.ValidationErrorPage;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;



/**
 * @author manoranjan
 *
 */
@ParentPackage("egov")
public class ServiceDetailsAction extends BaseFormAction {

	private static final long serialVersionUID = 1L;
	private PersistenceService<ServiceCategory, Long> serviceCategoryService;
	private PersistenceService<ServiceDetails, Long> serviceDetailsService;
	private ServiceDetails serviceDetails = new ServiceDetails();
	private static final String BEFORECREATE =  "beforeCreate" ; 
	private static final String BEFOREMODIFY =  "beforeModify" ; 
	private static final String MESSAGE =  "message" ; 
	private List<ServiceAccountDetails> accountDetails = new ArrayList<ServiceAccountDetails>();
	private List<ServiceSubledgerInfo> subledgerDetails = new ArrayList<ServiceSubledgerInfo> ();
	private List<Integer> departmentList = new ArrayList<Integer>(); 
	private List<ServiceDetails> serviceList;
	private Boolean isVoucherApproved = Boolean.FALSE;
	
	public ServiceDetailsAction(){
		
		addRelatedEntity("serviceCategory", ServiceCategory.class);
		addRelatedEntity("fund", Fund.class);
		addRelatedEntity("scheme", Scheme.class);
		addRelatedEntity("subscheme", SubScheme.class);
		addRelatedEntity("fundSource", Fundsource.class);
		addRelatedEntity("functionary", Functionary.class);
		addRelatedEntity("function", CFunction.class);
		
	}

	@Override
	public ServiceDetails getModel() {
	
		return serviceDetails;
	}

	
	public String newform(){
		addDropdownData("serviceCategoryList", serviceCategoryService.findAllByNamedQuery("SERVICE_CATEGORY_ALL"));
		return NEW;
	}
	
	@Override
	public void prepare() {
		super.prepare();
		if(null != parameters.get("serviceId") && StringUtils.isNotEmpty(parameters.get("serviceId")[0])){
			serviceDetails = serviceDetailsService.findById(Long.valueOf(parameters.get("serviceId")[0]),false);
			accountDetails.addAll(serviceDetails.getServiceAccountDtls());
			for (ServiceAccountDetails account : serviceDetails.getServiceAccountDtls()) {
				subledgerDetails.addAll(account.getSubledgerDetails());
				
			}
			for(DepartmentImpl department : serviceDetails.getServiceDept()){
				departmentList.add(department.getId());
			}
		}else if(null != serviceDetails.getServiceCategory() && null != serviceDetails.getServiceCategory().getId()){
			ServiceCategory category = serviceCategoryService.findById(serviceDetails.getServiceCategory().getId(),false);
			serviceDetails.setServiceCategory(category);
		} 
		EgovMasterDataCaching masterCache = EgovMasterDataCaching.getInstance();
		
		addDropdownData("departmentList", masterCache.get("egi-department"));
		addDropdownData("functionaryList", masterCache.get("egi-functionary"));
		addDropdownData("fundList",  masterCache.get("egi-fund"));
		addDropdownData("fundsourceList", masterCache.get("egi-fundSource"));
		addDropdownData("functionList", masterCache.get("egi-function"));
		if( null != serviceDetails.getFund() && serviceDetails.getFund().getId() != -1){
			addDropdownData("schemeList", getPersistenceService().findAllBy(" from Scheme where fund.id=?", serviceDetails.getFund().getId()));
		}else{
			addDropdownData("schemeList",  Collections.EMPTY_LIST );
		}
		if(null != serviceDetails.getScheme() && serviceDetails.getScheme().getId() != -1 ){
			addDropdownData("subschemeList", getPersistenceService().findAllBy("from SubScheme where scheme.id=? and isActive=1 order by name", serviceDetails.getScheme().getId()));
		}else{
			addDropdownData("subschemeList", Collections.EMPTY_LIST);
		}
		
		

	}
	
	public String beforeCreate(){
		
		accountDetails.add(new ServiceAccountDetails());
		subledgerDetails.add(new ServiceSubledgerInfo());
		return BEFORECREATE;
	}
	
	
	@ValidationErrorPage(value=BEFORECREATE)
	public String create(){
		insertOrUpdateService();
		if(hasActionErrors()){
			return BEFORECREATE;
		}
		return MESSAGE;
	}


	
	public String listServices(){
		return "list";
	}
	
	public String view(){
		
		return "view";
	}
	
	@ValidationErrorPage(value=BEFOREMODIFY)
	public String beforeModify(){
		
		if( null == this.accountDetails || this.accountDetails.isEmpty()) {
			this.accountDetails.add(new ServiceAccountDetails());
		}
		
		if( null == this.subledgerDetails || this.subledgerDetails.isEmpty()) {
			this.subledgerDetails.add(new ServiceSubledgerInfo());
		}
		return BEFOREMODIFY;
	}
	@SuppressWarnings("unchecked")
	@ValidationErrorPage(value=BEFOREMODIFY)
	public String modify(){
		
		
		List<ServiceAccountDetails> accountList =(List<ServiceAccountDetails>) getPersistenceService().getSession().
		createCriteria(ServiceAccountDetails.class).add(Restrictions.eq("serviceDetails.id", serviceDetails.getId())).list();
		
		for (ServiceAccountDetails serviceAccountDetails : accountList) {
			
			Query qry = getPersistenceService().getSession().createQuery("delete from ServiceSubledgerInfo where serviceAccountDetail.id=:accountId");
			qry.setLong("accountId", serviceAccountDetails.getId());
			qry.executeUpdate();
		}
		
		Query qry = getPersistenceService().getSession().createQuery("delete from ServiceAccountDetails where serviceDetails.id=:serviceId");
		qry.setLong("serviceId", serviceDetails.getId());
		qry.executeUpdate();
		insertOrUpdateService();
		if(hasActionErrors()){
			return BEFOREMODIFY;
		}
		return MESSAGE;
	}
	
	private void insertOrUpdateService() {
		removeEmptyRowsAccoutDetail(accountDetails);
		removeEmptyRowsSubledger(subledgerDetails);
		if(validateAccountDetails()){
			formatServiceDetails();
			if (serviceDetails.getVoucherCreation().equals(Boolean.TRUE))
			{
				isVoucherApproved=serviceDetails.getIsVoucherApproved();
				serviceDetails.setIsVoucherApproved(isVoucherApproved);
			}
			serviceDetailsService.persist(serviceDetails);
			addActionMessage(getText("service.create.success.msg",new String[]{getModel().getCode(),getModel().getServiceName()}));
		}
		if(subledgerDetails.isEmpty()){
			subledgerDetails.add(new ServiceSubledgerInfo());
		}
		if(accountDetails .isEmpty()){
			accountDetails.add(new ServiceAccountDetails());
		}
	}
	
	
	private void formatServiceDetails(){
	
		for(Integer deptId : departmentList){
			
			DepartmentImpl dept = (DepartmentImpl) getPersistenceService().find(" from DepartmentImpl where id= ?",deptId);
			serviceDetails.addServiceDept(dept);
			
		}
		
		for(ServiceAccountDetails account : accountDetails){
			
			ServiceAccountDetails serviceAccount = new  ServiceAccountDetails();
			serviceAccount.setAmount(account.getAmount());
			CChartOfAccounts glCodeId = (CChartOfAccounts)persistenceService.find(" from CChartOfAccounts where id =?",account.getGlCodeId().getId());
			serviceAccount.setGlCodeId(glCodeId);
			if(null != account.getFunction()  && null != account.getFunction().getId()){
				CFunction function = (CFunction)persistenceService.find("from CFunction where id=?",account.getFunction().getId());
				serviceAccount.setFunction(function);
			}
			
			serviceAccount.setServiceDetails(serviceDetails);
			for(ServiceSubledgerInfo subledger : subledgerDetails){
				
				if(subledger.getServiceAccountDetail().getGlCodeId().getId().equals( account.getGlCodeId().getId())){
					
					ServiceSubledgerInfo subledgerInfo = new  ServiceSubledgerInfo();
					Accountdetailtype accdetailtype = (Accountdetailtype) getPersistenceService().findByNamedQuery(
							CollectionConstants.QUERY_ACCOUNTDETAILTYPE_BY_ID, subledger.getDetailType().getId());
					subledgerInfo.setDetailType(accdetailtype);
					subledgerInfo.setDetailKeyId(subledger.getDetailKeyId());
					subledgerInfo.setAmount(subledger.getAmount());
					subledgerInfo.setServiceAccountDetail(serviceAccount);
					serviceAccount.addSubledgerDetails(subledgerInfo);
				}
				
			}
			serviceDetails.addServiceAccountDtls(serviceAccount);
		}
		
	    }
	                       
	
	
	
	private void removeEmptyRowsAccoutDetail(List<ServiceAccountDetails> list) {
		for (Iterator<ServiceAccountDetails> detail = list.iterator(); detail.hasNext();) {
			ServiceAccountDetails next = detail.next();
			if ( null != next  && (null == next.getGlCodeId() || null == next.getGlCodeId().getId() || next.getGlCodeId().getId().toString().trim().isEmpty()) 
					&& next.getAmount().compareTo(BigDecimal.ZERO) ==0) {
					detail.remove();
			}
			else if(null == next)
			{
				detail.remove();
			}
		}
	}
	
	protected void removeEmptyRowsSubledger(List<ServiceSubledgerInfo>  list) {
		for (Iterator<ServiceSubledgerInfo> detail = list.iterator(); detail.hasNext();) {
			ServiceSubledgerInfo next = detail.next();
			if( (null!= next) && (null == next.getServiceAccountDetail() || null ==  next.getServiceAccountDetail().getGlCodeId() 
					|| null == next.getServiceAccountDetail().getGlCodeId().getId() || next.getServiceAccountDetail().getGlCodeId().getId()
					== 0 || next.getServiceAccountDetail().getGlCodeId().getId() == -1)) {
				
					detail.remove();
				
			}else if(null == next)
			{
				detail.remove();
			}
				
		}
	
	}
	private boolean validateAccountDetails(){
		int index=0;
		for (ServiceAccountDetails account : accountDetails) {

			if(null != account.getGlCodeId() && null!= account.getGlCodeId().getGlcode() 
					&& account.getAmount().compareTo(BigDecimal.ZERO) == 0){
				addActionError(getText("service.accdetail.amountZero",new String[]{""+ ++index,account.getGlCodeId().getGlcode()}));
				return Boolean.FALSE;
			}
			else if(account.getAmount().compareTo(BigDecimal.ZERO) >0 && ( null == account.getGlCodeId() || null == account.getGlCodeId().getId())){
				addActionError(getText("service.accdetail.accmissing",new String[]{""+ ++index}));
				return Boolean.FALSE;
			}

		}
		return validateSubledger();
	}
	
	private boolean validateSubledger(){
		Map<String, BigDecimal> accountDetailAmount = new HashMap<String, BigDecimal>();
		for (ServiceAccountDetails account : accountDetails) {
			CChartOfAccountDetail  chartOfAccountDetail = (CChartOfAccountDetail) getPersistenceService().find(" from CChartOfAccountDetail" +
					" where glCodeId.id=?", account.getGlCodeId().getId());
			if(null != chartOfAccountDetail){
				accountDetailAmount.put(account.getGlCodeId().getGlcode(), account.getAmount());
			}
		}
		
		Map<String, BigDecimal> subledgerAmount = new HashMap<String, BigDecimal>();
	
		for (ServiceSubledgerInfo subledger : subledgerDetails) {
			
			if(null == subledger.getDetailType() || null == subledger.getDetailType().getId() || subledger.getDetailType().getId() == 0){
				
				addActionError(getText("service.accdetailType.entrymissing",new String[]{subledger.getServiceAccountDetail().getGlCodeId().getGlcode()}));
				return Boolean.FALSE;
			}/*else if(null == subledger.getDetailKeyId()){
				
				addActionError(getText("service.accdetailKey.entrymissing",new String[]{subledger.getServiceAccountDetail().getGlCodeId().getGlcode()}));
				
				return Boolean.FALSE;
			}*/
			
			else if(null  != subledgerAmount.get(subledger.getServiceAccountDetail().getGlCodeId().getGlcode())){
				
					BigDecimal amount =  subledgerAmount.get(subledger.getServiceAccountDetail().getGlCodeId().getGlcode());
					subledgerAmount.put(subledger.getServiceAccountDetail().getGlCodeId().getGlcode(), amount.add(subledger.getAmount()));
			}
			else {
				
				subledgerAmount.put(subledger.getServiceAccountDetail().getGlCodeId().getGlcode(),subledger.getAmount());
			}
			
		}
		
		for ( Map.Entry<String, BigDecimal> entry  : accountDetailAmount.entrySet()) {
			
			String key = entry.getKey();
			BigDecimal value = entry.getValue();
			if(null == subledgerAmount.get(key)){
				addActionError(getText("service.accdetail.entrymissing",new String[]{key}));
				return Boolean.FALSE;
			}else if(subledgerAmount.get(key).compareTo(value) != 0){
				addActionError(getText("service.subledger.amtnotmatchinng",new String[]{key}));
				return Boolean.FALSE;
			}
		}
		
		return Boolean.TRUE;
	}
	
	
	public String codeUniqueCheck(){
		
		return "codeUniqueCheck";
	}
	public boolean getCodeCheck(){
		
		boolean codeExistsOrNot = false;
		ServiceDetails service = (ServiceDetails)persistenceService.find("from ServiceDetails where code='"+serviceDetails.getCode()+"'");
		if(null != service ){
			codeExistsOrNot = true; 
		}
		return codeExistsOrNot;
	}
	
	
	/**
	 * @return the serviceDetails
	 */
	public ServiceDetails getServiceDetails() {
		return serviceDetails;
	}

	/**
	 * @param serviceDetails the serviceDetails to set
	 */
	public void setServiceDetails(ServiceDetails serviceDetails) {
		this.serviceDetails = serviceDetails;
	}

	/**
	 * @param serviceCategoryService the serviceCategoryService to set
	 */
	public void setServiceCategoryService(
			PersistenceService<ServiceCategory, Long> serviceCategoryService) {
		this.serviceCategoryService = serviceCategoryService;
	}

	public List<ServiceAccountDetails> getAccountDetails() {
		return accountDetails;
	}

	public void setAccountDetails(List<ServiceAccountDetails> accountDetails) {
		this.accountDetails = accountDetails;
	}

	public List<ServiceSubledgerInfo> getSubledgerDetails() {
		return subledgerDetails;
	}

	public void setSubledgerDetails(List<ServiceSubledgerInfo> subledgerDetails) {
		this.subledgerDetails = subledgerDetails;
	}

	public void setServiceDetailsService(
			PersistenceService<ServiceDetails, Long> serviceDetailsService) {
		this.serviceDetailsService = serviceDetailsService;
	}

	public List<Integer> getDepartmentList() {
		return departmentList;
	}

	public void setDepartmentList(List<Integer> departmentList) {
		this.departmentList = departmentList;
	}

	public List<ServiceDetails> getServiceList() {
		return serviceList;
	}

}
