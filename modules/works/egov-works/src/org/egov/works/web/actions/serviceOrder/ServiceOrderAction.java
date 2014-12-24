/**
 * 
 */
package org.egov.works.web.actions.serviceOrder;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.script.ScriptContext;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.egov.commons.Accountdetailtype;
import org.egov.commons.CFinancialYear;
import org.egov.commons.CFiscalPeriod;
import org.egov.commons.EgwStatus;
import org.egov.infstr.ValidationError;
import org.egov.infstr.ValidationException;
import org.egov.infstr.search.SearchQuery;
import org.egov.infstr.search.SearchQueryHQL;
import org.egov.infstr.services.ScriptService;
import org.egov.infstr.utils.EgovMasterDataCaching;
import org.egov.infstr.utils.SequenceGenerator;
import org.egov.lib.rjbac.dept.DepartmentImpl;
import org.egov.lib.rjbac.user.UserImpl;
import org.egov.masters.model.AccountEntity;
import org.egov.model.masters.AccountCodePurpose;
import org.egov.pims.commons.DesignationMaster;
import org.egov.pims.model.EmployeeView;
import org.egov.pims.service.EisUtilService;
import org.egov.pims.service.PersonalInformationService;
import org.egov.web.actions.SearchFormAction;
import org.egov.web.annotation.ValidationErrorPage;
import org.egov.web.utils.EgovPaginatedList;
import org.egov.works.models.estimate.AbstractEstimate;
import org.egov.works.models.estimate.OverheadValue;
import org.egov.works.models.serviceOrder.ServiceOrder;
import org.egov.works.models.serviceOrder.ServiceOrderDetails;
import org.egov.works.models.serviceOrder.ServiceOrderObjectDetail;
import org.egov.works.models.serviceOrder.ServiceTemplate;
import org.egov.works.models.serviceOrder.SoTemplateActivities;
import org.egov.works.services.AbstractEstimateService;
import org.egov.works.services.WorksService;
import org.egov.works.services.serviceOrder.ServiceOrderService;
import org.egov.works.services.serviceOrder.ServiceTemplateService;

/**
 * @author manoranjan
 *
 */
@ParentPackage("egov")
public class ServiceOrderAction extends SearchFormAction {

	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(ServiceOrderAction.class);
	public static final Locale LOCALE = new Locale("en","IN");
	public static final SimpleDateFormat  DDMMYYYYFORMATS= new SimpleDateFormat("dd/MM/yyyy",LOCALE);
	private List<Map<String, Object>> tmpltActDtlsList = new ArrayList<Map<String, Object>>();
	private ServiceOrder serviceOrder = new ServiceOrder();
	private AbstractEstimate estimate;
	private ServiceTemplate template;
	private ServiceTemplateService serviceTemplateSer;
	private ServiceOrderService serviceOrderSer;
	private AbstractEstimateService abstractEstimateService;
	private List<AbstractEstimate> estimates;
	private String value;
	private Integer estmtTmpltIndex;
	private List<AccountEntity> archtectList; 
	private List<Map<String, Object>> soList;
	private EisUtilService eisService;
	private ScriptService scriptExecutionService;
	private SequenceGenerator sequenceGenerator;
	private Date fromDate;
	private Date toDate;
	private String estimateId;
	private PersonalInformationService personalInformationService;
	private WorksService worksService;
	
	public ServiceOrderAction(){
		
		addRelatedEntity("preparedby", UserImpl.class);
		addRelatedEntity("departmentId", DepartmentImpl.class);
	}
	
	@Override 
	public Object getModel() {
		
		return serviceOrder;
	}
	@SuppressWarnings("unchecked")
	@Override
	public void prepare() {
		super.prepare();
		archtectList = persistenceService.findAllBy(" from AccountEntity where accountdetailtype.name=?", "Architect");
		addDropdownData("archtectList",archtectList);
		EgovMasterDataCaching masterCache = EgovMasterDataCaching.getInstance();
		addDropdownData("prepareByList", Collections.EMPTY_LIST);
		addDropdownData("templateList",serviceTemplateSer.getAllActiveServiceTemplate());
	
		addDropdownData("departmentList", masterCache.get("egi-department"));
		
	}
	public String newform(){
		LOGGER.debug(" ServiceOrderAction |  newform | Start");
		estimates = abstractEstimateService.getAbEstimateListById(estimateId);
		serviceOrder.setDepartmentId((DepartmentImpl)estimates.get(0).getExecutingDepartment()); 
		serviceOrder.setServiceorderdate(new Date());
		loadPreparedByList(serviceOrder);
		LOGGER.debug(" ServiceOrderAction |  newform | end");
		return "new";
	}
	@SuppressWarnings("unchecked") 
	public String  ajaxTemplateDetails(){
		if(StringUtils.isEmpty(parameters.get("templateId")[0])){
			return "result";
		}
		Map<String, Object> temptActMap ;
		estmtTmpltIndex = Integer.valueOf(parameters.get("index")[0]);
		estimate= (AbstractEstimate) persistenceService.find(" from AbstractEstimate where id="+Long.valueOf(parameters.get("estimateId")[0]));
		template= (ServiceTemplate) persistenceService.find(" from ServiceTemplate where id="+Long.valueOf(parameters.get("templateId")[0]));
		BigDecimal totalOHVal =  getOverHeadValue(estimate.getId());
		List<SoTemplateActivities> templateActvList= (List<SoTemplateActivities>)persistenceService.findAllBy
		("from  SoTemplateActivities where serviceTemplate.id=? order by stageNo",Long.valueOf(parameters.get("templateId")[0]));
		for (SoTemplateActivities soTemplateActivities : templateActvList) {
			
			temptActMap = new HashMap<String, Object>();
			
			BigDecimal amount = (totalOHVal.multiply(soTemplateActivities.getRateValue())).divide(BigDecimal.valueOf(100));
			temptActMap.put("amount",amount.setScale(2, BigDecimal.ROUND_HALF_UP));
			temptActMap.put("desc",soTemplateActivities.getDescription());
			temptActMap.put("rate",soTemplateActivities.getRateValue());
			temptActMap.put("stageNo",soTemplateActivities.getStageNo());
			temptActMap.put("tmptActvid",soTemplateActivities.getId());
			tmpltActDtlsList.add(temptActMap);
		}
		
		return "result";
	}
	public String ajaxAmount (){
		BigDecimal estimateVal = getOverHeadValue(Long.valueOf(parameters.get("estimateId")[0]));
		
		BigDecimal rate = new BigDecimal(parameters.get("rate")[0]);
		BigDecimal amount = (estimateVal.multiply(rate)).divide(BigDecimal.valueOf(100));
		value =amount.setScale(2, BigDecimal.ROUND_HALF_UP).toString();
		return "value";
		
	}
	@ValidationErrorPage(value="new")
	public String save(){
	
		try {
			LOGGER.debug("ServiceOrderAction | save | start");
			serviceOrder.setServiceordernumber(getSoNum());
			Accountdetailtype accountdetailtype =(Accountdetailtype) persistenceService.find(" from Accountdetailtype where name='Architect'");
			EgwStatus status = (EgwStatus)persistenceService.find(" from EgwStatus where moduletype='"+"SERVICEORDER"+"' and description='"+"ServiceOrderCreated'");
			serviceOrder.setStatus(status);
			serviceOrder.setAccountdetailtype(accountdetailtype);
			LOGGER.debug("ServiceOrderAction | save | No of template seletced : "+ serviceOrder.getServiceOrderObjectDetails().size() );
			serviceOrderSer.persist(serviceOrder);
			addActionMessage("Service Order created sucessfully , Service order number : "+ serviceOrder.getServiceordernumber());
		} 
		catch (ValidationException e) {
			 newform();
			 clearMessages();
			 List<ValidationError> errors=new ArrayList<ValidationError>();
			 errors.add(new ValidationError("exp",e.getErrors().get(0).getMessage()));
			 throw new ValidationException(errors);
		} 
		catch (Exception e) {
			 newform();
			 clearMessages();
			 List<ValidationError> errors=new ArrayList<ValidationError>();
			 errors.add(new ValidationError("exp",e.getMessage()));
			 throw new ValidationException(errors);
		}
	
		return "message";
	}
	
	@ValidationErrorPage(value="search")
	public String beforeSearch(){
		LOGGER.debug("ServiceOrderAction | save | beforeSearch");
		value = parameters.get("type")!=null?parameters.get("type")[0]:"view";
	
		return "search";
	}
	
	@Override
	public SearchQuery prepareQuery(String sortField, String sortOrder) {
	
		StringBuffer query = new StringBuffer(100);
		query.append(" from ServiceOrder so where ");
		Accountdetailtype detailtype= (Accountdetailtype)persistenceService.find(" from Accountdetailtype where name='Architect'");
		query.append("so.accountdetailtype.id="+detailtype.getId());
		if(null !=serviceOrder.getDetailkeyid() && StringUtils.isNotEmpty(serviceOrder.getDetailkeyid().toString())){
			query.append(" and so.detailkeyid="+serviceOrder.getDetailkeyid());
			
		}
		if(StringUtils.isNotEmpty(serviceOrder.getServiceordernumber())){
			query.append(" and so.serviceordernumber='"+serviceOrder.getServiceordernumber()+"'");
		}
		if(null != fromDate){
			query.append(" and so.serviceorderdate >= to_date('"+DDMMYYYYFORMATS.format(fromDate)+"','dd/MM/yyyy')");
		}
		if(null != toDate){
			query.append(" and so.serviceorderdate <= to_date('"+DDMMYYYYFORMATS.format(toDate)+"','dd/MM/yyyy')");
		}
		return new SearchQueryHQL(query.toString(),"select count(*) "+query.toString(),null);
	}
 
	@ValidationErrorPage(value="search")
	public String list(){
		search();
		formatResult();
		return "search";
	}
	

	@ValidationErrorPage(value="view")
	public String view(){
	
		serviceOrder = (ServiceOrder) persistenceService.find(" from ServiceOrder where id="+serviceOrder.getId());
		return "view";
	}
	
	@SuppressWarnings("unchecked")
	private void  formatResult(){
		soList = new ArrayList<Map<String,Object>>();
		EgovPaginatedList egovPaginatedList = (EgovPaginatedList)searchResult;
		List<ServiceOrder> list = egovPaginatedList.getList();
		for(ServiceOrder serviceOrder : list) {
			
			Map<String, Object> map = new HashMap<String, Object>();
			
			map.put("serviceOrderId", serviceOrder.getId());
			map.put("serviceordernumber", serviceOrder.getServiceordernumber());
			map.put("serviceorderdate", DDMMYYYYFORMATS.format(serviceOrder.getServiceorderdate()));
			AccountEntity  accountEntity= (AccountEntity)persistenceService.find("from AccountEntity where id= "+ serviceOrder.getDetailkeyid());
			map.put("architect", accountEntity.getName());
			StringBuffer estimateNum = new StringBuffer();
			for (ServiceOrderObjectDetail soObjDetail : serviceOrder.getServiceOrderObjectDetails()) {
				estimateNum = estimateNum.length()>0?estimateNum.append(" | ").append(soObjDetail.getAbstractEstimate().getEstimateNumber()):
													estimateNum.append(soObjDetail.getAbstractEstimate().getEstimateNumber());
				
			}
			map.put("estimateNumber", estimateNum.toString());
			soList.add(map);
		}
		egovPaginatedList.setList(soList);
	}
	
	public String getArchitect(){
		AccountEntity  accountEntity= (AccountEntity)persistenceService.find("from AccountEntity where id= "+ serviceOrder.getDetailkeyid());
		return accountEntity.getName();
	}
	
	private String getSoNum(){
		
		CFiscalPeriod fiscalPeriod = (CFiscalPeriod)persistenceService.find("from CFiscalPeriod where  to_date('"+DDMMYYYYFORMATS.format(serviceOrder.getServiceorderdate())+"','dd/MM/yyyy')"
				+"  between startingDate and endingDate");
		CFinancialYear financialYear =(CFinancialYear) persistenceService.find(" from CFinancialYear where  to_date('"+DDMMYYYYFORMATS.format(serviceOrder.getServiceorderdate())+"','dd/MM/yyyy')"+
				" between  startingDate and endingDate");
		ScriptContext scriptContext = ScriptService.createContext("serviceOrder",serviceOrder,"sequenceGenerator",sequenceGenerator,
				"fiscalPeriodName",fiscalPeriod.getName(),"financialYearId",financialYear.getFinYearRange());  
		return  (String)scriptExecutionService.executeScript("serviceorder.number", scriptContext);
	}
	
	
	public BigDecimal getOverHeadValue(Long estmtId ){
		AccountCodePurpose purpose = (AccountCodePurpose)persistenceService.findAllBy(" from AccountCodePurpose where name=?", "Architect Fee Code").get(0);
		OverheadValue overheadValue =(OverheadValue) persistenceService.find(" from OverheadValue where abstractEstimate.id="+estmtId +" and overhead.account.purposeId="+purpose.getId());
		
		if(null != overheadValue){
			return BigDecimal.valueOf(overheadValue.getAmount().getValue()).setScale(2, BigDecimal.ROUND_HALF_UP);
		}else{
			return BigDecimal.valueOf(0);
		}
	}

	private void loadPreparedByList( ServiceOrder serviceOrder){
		HashMap<String,Object> paramMap = new HashMap<String, Object>();
		List<EmployeeView> empInfoList = new ArrayList<EmployeeView>();
		String actions = worksService.getWorksConfigValue("DESIGNATIONS_TOLOAD_PREPAREDBY");
		paramMap.put("departmentId",serviceOrder.getDepartmentId().getId());
		if(actions!=null && actions!=""){
			List<String> designationNames = new ArrayList<String>();
			List<Integer> designationIds = new ArrayList<Integer>();
			List<DesignationMaster> designationMaster=new ArrayList<DesignationMaster>();
			designationNames= Arrays.asList(actions.split(","));
			if(designationNames!=null && !designationNames.isEmpty()){
				List<String> desgListUpper = new ArrayList<String>();
				for(String desgNames:designationNames){
					desgListUpper.add(desgNames.toUpperCase());
				}
				designationMaster.addAll(persistenceService.findAllByNamedQuery("getDesignationForListOfDesgNames", desgListUpper));
				if(designationMaster!=null && !designationMaster.isEmpty()){
					for(DesignationMaster dm : designationMaster){
						designationIds.add(dm.getDesignationId());
					} 
					paramMap.put("designationId", designationIds);
				}
			}
		}
		empInfoList=personalInformationService.getListOfEmployeeViewBasedOnListOfDesignationAndOtherCriteria(paramMap, -1, -1);
		List<EmployeeView> userList = new ArrayList<EmployeeView>();
		List<EmployeeView> finalList= new ArrayList<EmployeeView>();
		EmployeeView mainEmpViewObj,prevEmpView = new EmployeeView();
		Iterator iterator = empInfoList.iterator(); 
		while(iterator.hasNext())
		{
			mainEmpViewObj=(EmployeeView)iterator.next();
			if(!((mainEmpViewObj.getId().equals(prevEmpView.getId())) && (mainEmpViewObj.getDesigId().equals(prevEmpView.getDesigId())))){
				finalList.add(mainEmpViewObj); 
		}
			prevEmpView=mainEmpViewObj;
		}
		userList=Collections.EMPTY_LIST;
		userList=finalList;
		addDropdownData("prepareByList", userList);
	}
	
	public String beforeEdit(){
		serviceOrder = (ServiceOrder) persistenceService.find(" from ServiceOrder where id="+serviceOrder.getId());
		loadPreparedByList(serviceOrder);
		Long count = (Long)persistenceService.find("select count(*) from  SoMeasurementHeader where  serviceOrderObjectDetail.serviceOrder.id="+serviceOrder.getId());
		value=count >0 ?"billed":"";
		return "edit";
	}
	
	public String edit(){
		ServiceOrder origionalServiceOrder = serviceOrderSer.findById(Long.valueOf(parameters.get("serviceOrderId")[0]), false);
		
		origionalServiceOrder.setDetailkeyid(serviceOrder.getDetailkeyid());
		origionalServiceOrder.setComments(serviceOrder.getComments());
		origionalServiceOrder.setPreparedby(serviceOrder.getPreparedby());
		origionalServiceOrder.setServiceorderdate(serviceOrder.getServiceorderdate());
		if(! origionalServiceOrder.getServiceorderdate().equals(serviceOrder.getServiceorderdate())){
			origionalServiceOrder.setServiceordernumber(getSoNum());
		}
		
		
		for (ServiceOrderObjectDetail soObjectDetail : origionalServiceOrder.getServiceOrderObjectDetails()) {
			List<ServiceOrderDetails> serviceOrderDetails = soObjectDetail.getServiceOrderDetails();
			serviceOrderDetails.clear();	
		} 
		origionalServiceOrder.getServiceOrderObjectDetails().clear();
		origionalServiceOrder.getServiceOrderObjectDetails().addAll(serviceOrder.getServiceOrderObjectDetails());
		
		//editSO(origionalServiceOrder);
		
		serviceOrderSer.merge(origionalServiceOrder);
		addActionMessage(" Service Order : "+origionalServiceOrder.getServiceordernumber() +" modified sucessfully");
		return "message";
	}
	
	private void editSO(ServiceOrder origionalServiceOrder){
		
		Long count = (Long)persistenceService.find("select count(*) from  SoMeasurementHeader where  serviceOrderObjectDetail.serviceOrder.id="+serviceOrder.getId());
		
		
		// edit service order for which few lines are measured.
		if(count > 0){
			Map<String,List<ServiceOrderDetails>> soObjDetailMap = new HashMap<String, List<ServiceOrderDetails>>();
			Map<String, ServiceOrderDetails> soDetailMap = new HashMap<String, ServiceOrderDetails>();
			List<ServiceOrderDetails> soDetailsList = new ArrayList<ServiceOrderDetails>();
			List<ServiceOrderObjectDetail> soObjDetailsList = new ArrayList<ServiceOrderObjectDetail>();
			
			for (ServiceOrderObjectDetail soObjectDetail : serviceOrder.getServiceOrderObjectDetails()) {
				for (ServiceOrderDetails soDetails : soObjectDetail.getServiceOrderDetails()) {
					soDetailMap.put(soDetails.getId().toString(), soDetails);
				}
				soObjDetailMap.put(soObjectDetail.getId().toString(), soObjectDetail.getServiceOrderDetails());
			}
			
			for (ServiceOrderObjectDetail soObjectDetail : origionalServiceOrder.getServiceOrderObjectDetails()) {
				Long count1 = (Long)persistenceService.find("select count(*) from  SoMeasurementHeader where  serviceOrderObjectDetail.id="+soObjectDetail.getId());
				if(count1 >0){
					List<ServiceOrderDetails> soDetailList = soObjectDetail.getServiceOrderDetails();
					for (ServiceOrderDetails soDetails: soDetailList) {
						ServiceOrderDetails soDetail = soDetailMap.get(soDetails.getId());
						soDetails.setRatePercentage(soDetail.getRatePercentage());
						soDetailsList.add(soDetails);
					}
				}else{
					
					soObjectDetail.getServiceOrderDetails().clear();
					soDetailsList.addAll(soObjDetailMap.get(soObjectDetail.getId().toString()));
				}
				soObjectDetail.setServiceOrderDetails(soDetailsList);
				soObjDetailsList.add(soObjectDetail);
			} 
			
			origionalServiceOrder.getServiceOrderObjectDetails().addAll(soObjDetailsList);
			
		}
		else{ //edit service order for which no line item  is  measured.
			// deleting the old service order details and service order object details 
			for (ServiceOrderObjectDetail soObjectDetail : origionalServiceOrder.getServiceOrderObjectDetails()) {
				List<ServiceOrderDetails> serviceOrderDetails = soObjectDetail.getServiceOrderDetails();
				serviceOrderDetails.clear();	
			} 
			origionalServiceOrder.getServiceOrderObjectDetails().clear();
			origionalServiceOrder.getServiceOrderObjectDetails().addAll(serviceOrder.getServiceOrderObjectDetails());
			
		}
		
		
	}
	
	public ServiceOrder getServiceOrder() {
		return serviceOrder;
	}

	public void setServiceOrder(ServiceOrder serviceOrder) {
		this.serviceOrder = serviceOrder;
	}

	public void setServiceTemplateSer(ServiceTemplateService serviceTemplateSer) {
		this.serviceTemplateSer = serviceTemplateSer;
	}

	
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public List<AbstractEstimate> getEstimates() {
		return estimates;
	}

	public void setEstimates(List<AbstractEstimate> estimates) {
		this.estimates = estimates;
	}

	

	public AbstractEstimate getEstimate() {
		return estimate;
	}

	public ServiceTemplate getTemplate() {
		return template;
	}

	public void setEstimate(AbstractEstimate estimate) {
		this.estimate = estimate;
	}

	public void setTemplate(ServiceTemplate template) {
		this.template = template;
	}

	public Integer getEstmtTmpltIndex() {
		return estmtTmpltIndex;
	}

	public void setEstmtTmpltIndex(Integer estmtTmpltIndex) {
		this.estmtTmpltIndex = estmtTmpltIndex;
	}

	public List<Map<String, Object>> getTmpltActDtlsList() {
		return tmpltActDtlsList;
	}

	public void setTmpltActDtlsList(List<Map<String, Object>> tmpltActDtlsList) {
		this.tmpltActDtlsList = tmpltActDtlsList;
	}

	
	public void setServiceOrderSer(ServiceOrderService serviceOrderSer) {
		this.serviceOrderSer = serviceOrderSer;
	}



	public void setAbstractEstimateService(
			AbstractEstimateService abstractEstimateService) {
		this.abstractEstimateService = abstractEstimateService;
	}

	public List<AccountEntity> getArchtectList() {
		return archtectList;
	}

	public void setArchtectList(List<AccountEntity> archtectList) {
		this.archtectList = archtectList;
	}

	public List<Map<String, Object>> getSoList() {
		return soList;
	}

	public void setSoList(List<Map<String, Object>> soList) {
		this.soList = soList;
	}

	public void setEisService(EisUtilService eisService) {
		this.eisService = eisService;
	}

	public void setScriptExecutionService(ScriptService scriptExecutionService) {
		this.scriptExecutionService = scriptExecutionService;
	}

	public void setSequenceGenerator(SequenceGenerator sequenceGenerator) {
		this.sequenceGenerator = sequenceGenerator;
	}

	public Date getFromDate() {
		return fromDate;
	}

	public Date getToDate() {
		return toDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

	public String getEstimateId() {
		return estimateId;
	}

	public void setEstimateId(String estimateId) {
		this.estimateId = estimateId;
	}

	public void setPersonalInformationService(
			PersonalInformationService personalInformationService) {
		this.personalInformationService = personalInformationService;
	}

	public void setWorksService(WorksService worksService) {
		this.worksService = worksService;
	}

	

}
