package org.egov.works.services;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.egov.commons.Accountdetailkey;
import org.egov.commons.Accountdetailtype;
import org.egov.commons.CFinancialYear;
import org.egov.commons.service.CommonsService;
import org.egov.dao.budget.BudgetDetailsDAO;
import org.egov.dao.budget.BudgetGroupDAO;
import org.egov.egf.commons.EgovCommon;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infstr.ValidationException;
import org.egov.infstr.commonMasters.EgUom;
import org.egov.infstr.commons.dao.GenericHibernateDaoFactory;
import org.egov.infstr.config.AppConfigValues;
import org.egov.infstr.models.Money;
import org.egov.infstr.services.PersistenceService;
import org.egov.model.budget.BudgetGroup;
import org.egov.model.budget.BudgetUsage;
import org.egov.pims.model.Assignment;
import org.egov.pims.model.PersonalInformation;
import org.egov.pims.service.EmployeeService;
import org.egov.works.models.estimate.AbstractEstimate;
import org.egov.works.models.estimate.AbstractEstimateAppropriation;
import org.egov.works.models.estimate.BudgetFolioDetail;
import org.egov.works.models.estimate.BudgetNumberGenerator;
import org.egov.works.models.estimate.DepositWorksUsage;
import org.egov.works.models.estimate.EstimateNumberGenerator;
import org.egov.works.models.estimate.FinancialDetail;
import org.egov.works.models.estimate.MultiYearEstimate;
import org.egov.works.models.estimate.ProjectCode;
import org.egov.works.models.estimate.ProjectCodeGenerator;
import org.egov.works.models.tender.TenderResponse;
import org.egov.works.utils.WorksConstants;
import org.springframework.beans.factory.annotation.Autowired;


public class AbstractEstimateService extends PersistenceService<AbstractEstimate,Long> {
	private static final Logger logger = Logger.getLogger(AbstractEstimateService.class);
	private EstimateNumberGenerator estimateNumberGenerator;
	private BudgetNumberGenerator budgetNumberGenerator;
	private ProjectCodeGenerator projectcodeGenerator;
	private PersistenceService<ProjectCode, Long> projectCodeService;
	private PersistenceService<AbstractEstimateAppropriation, Long> estimateAppropriationService;
	private DepositWorksUsageService depositWorksUsageService;
	
	private BudgetDetailsDAO budgetDetailsDAO;
	private GenericHibernateDaoFactory genericHibDao;
	@Autowired
	private EmployeeService employeeService;
	private PersistenceService<TenderResponse,Long> tenderResponseService;
	private BudgetGroupDAO budgetGroupDAO;
	@Autowired
	private CommonsService commonsService;
//	private MastersManager mastersMgr;
	private WorksService worksService;
	
	public static final String APPROVED="APPROVED";
	public static final String UNAPPROVED="UNAPPROVED";
	private final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy",Locale.US);	
	private EgovCommon egovCommon;
	private static final String FROM_DATE="fromDate";
	private static final String TO_DATE="toDate";
	private PersistenceService<BudgetUsage, Long> budgetUsageService;
	private WorkOrderService workOrderService;
	private WorksPackageService workspackageService;
	
	public void setGenericHibDao(GenericHibernateDaoFactory genericHibDao) {
		this.genericHibDao = genericHibDao;
	}

	public void setBudgetDetailsDAO(BudgetDetailsDAO budgetDetailsDAO) {
		this.budgetDetailsDAO = budgetDetailsDAO;
	}

	public void setBudgetNumberGenerator(BudgetNumberGenerator budgetNumberGenerator) {
		this.budgetNumberGenerator = budgetNumberGenerator;
	}


	public AbstractEstimateService(){
		setType(AbstractEstimate.class);
	}
	
	
	public AbstractEstimate create(AbstractEstimate entity) {
		AbstractEstimate saved = super.create(entity);
		setEstimateNumber(saved);
		return saved;
	}
	
	public AbstractEstimate persist(AbstractEstimate entity) {
		AbstractEstimate saved= super.persist(entity);
		//setEstimateNumber(saved);
		
		return saved;
	}
	
	
	/**
	 * This method invokes the script service to generate the budget appropriation number.
	 * This method is invoked from the work flow rules.
	 * 
	 * @param entity an instance of <code>AbstractEstimate</code>	
	 * 
	 * @return a <code>String<code> containing the generated the budget 
	 * appropriation number.
	 */
	public String getBudgetAppropriationNumber(AbstractEstimate entity){
		CFinancialYear finYear=null;
		try {
			if(isPreviousYearApprRequired(entity.getFinancialDetails().get(0))){
				finYear = getPreviousFinancialYear();
			}
			else {
				finYear = getCurrentFinancialYear(new Date());
			}
			
			return budgetNumberGenerator.getBudgetApprNo(entity, finYear);
		 }
		 catch (ValidationException sequenceException){
			throw sequenceException;
		 }
	}
	
	
	/**
	 * The method return true if the estimate number has to be re-generated
	 * 
	 * @param entity an instance of <code>AbstractEstimate</code> containing the 
	 * estimate date 
	 * 
	 * @param financialYear an instance of <code>CFinancialYear</code> representing the 
	 * financial year for the estimate date.
	 * 
	 * @return a boolean value indicating if the estimate number change is required.
	 */
	private boolean estimateNumberChangeRequired(AbstractEstimate entity, 
			CFinancialYear financialYear){
		String[] estNum = entity.getEstimateNumber().split("/");
		
		if(estNum[0].equals(entity.getExecutingDepartment().getCode()) && 
				estNum[1].equals(financialYear.getFinYearRange())){
			return false;
		}
		return true;
	}

	public void setEstimateNumber(AbstractEstimate entity) {
		CFinancialYear financialYear=getCurrentFinancialYear(entity.getEstimateDate());
		if((entity.getEstimateNumber()==null) ||
				(entity.getEstimateNumber()!=null && 
						estimateNumberChangeRequired(entity,financialYear))){
			entity.setEstimateNumber(estimateNumberGenerator.getEstimateNumber(
					entity, financialYear));
		}
	}
	
	
	/**
	 * This method generates and set the project code for the estimate
	 * 
	 * @param entity an instance of <code>AbstractEstimate</code> for the project 
	 * code is to be generated.
	 */
	public void setProjectCode(AbstractEstimate entity){
		CFinancialYear finYear=getCurrentFinancialYear(entity.getEstimateDate());
		ProjectCode projectCode = new ProjectCode(entity, null);
		try{
		   projectCode.setCode(projectcodeGenerator.generateProjectcode(entity, finYear));
		}
		catch (ValidationException sequenceException) {
		   throw sequenceException;
		}
		projectCode.setCodeName(entity.getName());
		projectCode.setDescription(entity.getName());
		projectCode.setIsActive(true);
		projectCode.setEgwStatus(commonsService.getStatusByModuleAndCode(ProjectCode.class.getSimpleName(),WorksConstants.DEFAULT_PROJECTCODE_STATUS));
		entity.setProjectCode(projectCode);
		projectCode.addEstimate(entity);
		projectCodeService.persist(projectCode);
		createAccountDetailKey(projectCode);		
	}
	
	protected void createAccountDetailKey(ProjectCode proj){
		Accountdetailtype accountdetailtype=worksService.getAccountdetailtypeByName("PROJECTCODE");
		Accountdetailkey adk=new Accountdetailkey();
		adk.setGroupid(1);
		adk.setDetailkey(proj.getId().intValue());
		adk.setDetailname(accountdetailtype.getAttributename());
		adk.setAccountdetailtype(accountdetailtype);
		commonsService.createAccountdetailkey(adk);
		
	}
	
	/**
	 * This method performs the required validations and persists the 
	 * <code>FinancialDetail</code> object
	 * 
	 * @param financialDetail the <code>FinancialDetail</code> object to be persisted.
	 * 
	 * @param estimate the parent <code>AbstractEstimate</code> object
	 * 
	 * @return the persisted <code>AbstractEstimate</code> object 
	 */
	public AbstractEstimate persistFinancialDetail(
			FinancialDetail financialDetail, AbstractEstimate estimate) {
		super.validate(estimate);
		
		estimate.getFinancialDetails().clear();
		estimate.addFinancialDetails(financialDetail);		
		
		return super.persist(estimate);
	}
		
	public List<AppConfigValues> getAppConfigValue(String moduleName,String key){
		return genericHibDao.getAppConfigValuesDAO().getConfigValuesByModuleAndKey(
				moduleName, key);
	}
	
	public boolean checkForBudgetaryAppropriation(FinancialDetail financialDetail) 
	throws ValidationException{
		Long finyrId;
		double budgApprAmnt;
		Date budgetAppDate=null;
		if(isPreviousYearApprRequired(financialDetail)) {
			budgetAppDate=getPreviousFinancialYear().getEndingDate();
		}
		else {
			budgetAppDate=new Date();
		}
	//	CFinancialYear estimateDate_finYear=commonsService.getFinancialYearByDate(financialDetail.getAbstractEstimate().getEstimateDate());
		CFinancialYear budgetApprDate_finYear=commonsService.getFinYearByDate(budgetAppDate);
		List<Long> budgetheadid=new ArrayList<Long>();
		budgetheadid.add(financialDetail.getBudgetGroup().getId());
		boolean flag=false;

		finyrId=budgetApprDate_finYear.getId();
		if(budgetAppDate.compareTo(financialDetail.getAbstractEstimate().getEstimateDate())>=0){
			for(MultiYearEstimate multiYearEstimate:financialDetail.getAbstractEstimate().getMultiYearEstimates()) {
				if(multiYearEstimate!=null && multiYearEstimate.getFinancialYear().getId().compareTo(finyrId)==0 && multiYearEstimate.getPercentage()>0){
					budgApprAmnt=financialDetail.getAbstractEstimate().getTotalAmount().getValue();
					double percAmt= (budgApprAmnt*multiYearEstimate.getPercentage())/100;
					flag=checkConsumeEncumbranceBudget(financialDetail,finyrId,percAmt,budgetheadid);
					if(flag!=true)
						return flag;
				}
			}
		}
		return flag; 
	}
	
	public boolean checkConsumeEncumbranceBudget(FinancialDetail financialDetail, Long finyrId, double budgApprAmnt, List<Long> budgetheadid){
		boolean flag=true;
		BudgetUsage budgetUsage=budgetDetailsDAO.consumeEncumbranceBudget(
				financialDetail.getAbstractEstimate().getBudgetApprNo()==null? null:financialDetail.getAbstractEstimate().getBudgetApprNo(),
				finyrId, Integer.valueOf(11), 
				financialDetail.getAbstractEstimate().getEstimateNumber(), 
				Integer.parseInt(financialDetail.getAbstractEstimate().getUserDepartment().getId().toString()), 
				(financialDetail.getFunction()==null? null:financialDetail.getFunction().getId()), 
				(financialDetail.getFunctionary()==null? null:financialDetail.getFunctionary().getId()), 
				(financialDetail.getScheme()==null? null:financialDetail.getScheme().getId()), 
				(financialDetail.getSubScheme()==null? null:financialDetail.getSubScheme().getId()), 
				(financialDetail.getAbstractEstimate().getWard()==null? null:Integer.parseInt(financialDetail.getAbstractEstimate().getWard().getId().toString())),
				(financialDetail.getBudgetGroup()==null? null:budgetheadid), 
				(financialDetail.getFund()==null? null:financialDetail.getFund().getId()),
				budgApprAmnt
				);
		
		if(budgetUsage!=null){
			persistBudgetAppropriationDetails(financialDetail.getAbstractEstimate(),budgetUsage);
		}else{
			return false;
		}
		
		return flag;
	}
	
	public boolean releaseBudgetOnReject(FinancialDetail financialDetail) 
	throws ValidationException{
		
		//CFinancialYear finYear =financialDetail.getAbstractEstimate().getLeastFinancialYearForEstimate();
		AbstractEstimateAppropriation estimateAppropriation=estimateAppropriationService.findByNamedQuery("getLatestBudgetUsageForEstimate",financialDetail.getAbstractEstimate().getId());
		List<Long> budgetheadid=new ArrayList<Long>();
		budgetheadid.add(financialDetail.getBudgetGroup().getId());
		BudgetUsage budgetUsage=null;
		boolean flag=false;
		
		budgetUsage=budgetDetailsDAO.releaseEncumbranceBudget(
			financialDetail.getAbstractEstimate().getBudgetRejectionNo()==null? null:financialDetail.getAbstractEstimate().getBudgetRejectionNo(),	
			estimateAppropriation.getBudgetUsage().getFinancialYearId().longValue(), Integer.valueOf(11), 
			financialDetail.getAbstractEstimate().getEstimateNumber(), 
			Integer.parseInt(financialDetail.getAbstractEstimate().getUserDepartment().getId().toString()), 
			(financialDetail.getFunction()==null? null:financialDetail.getFunction().getId()), 
			(financialDetail.getFunctionary()==null? null:financialDetail.getFunctionary().getId()), 
			(financialDetail.getScheme()==null? null:financialDetail.getScheme().getId()), 
			(financialDetail.getSubScheme()==null? null:financialDetail.getSubScheme().getId()), 
			(financialDetail.getAbstractEstimate().getWard()==null? null:Integer.parseInt(financialDetail.getAbstractEstimate().getWard().getId().toString())),
			(financialDetail.getBudgetGroup()==null? null:budgetheadid), 
			(financialDetail.getFund()==null? null:financialDetail.getFund().getId()), 
			estimateAppropriation.getBudgetUsage().getConsumedAmount()
			);
		
		if(financialDetail.getAbstractEstimate()!=null){
			persistBudgetReleaseDetails(financialDetail.getAbstractEstimate(),budgetUsage);
		}
		return flag;
	}
	
	/**
	 * returns the sanctioned budget for the year
	 * @param paramMap
	 * @return
	 * @throws ValidationException
	 */
		
	 public BigDecimal getTotalGrantForYear(FinancialDetail fd)	throws ValidationException {
			BigDecimal val = BigDecimal.ZERO;
			Map<String,Object> searchMap = new HashMap<String, Object>();
			CFinancialYear finYear =null;
			
			if(fd!=null){				
				finYear=fd.getAbstractEstimate().getLeastFinancialYearForEstimate();
				searchMap.put("financialyearid", Long.valueOf(finYear.getId()));
			}
			
			if(fd!=null ){
				List<BudgetGroup> budgetheadid=new ArrayList<BudgetGroup>();
				budgetheadid.add(fd.getBudgetGroup());				
				if(fd.getFunction()!=null && fd.getFunction().getId()!=null)
					searchMap.put("functionid", fd.getFunction().getId());
				if(fd.getFunctionary()!=null && fd.getFunctionary().getId()!=null)
					searchMap.put("functionaryid", fd.getFunctionary().getId());
				if(fd.getFund()!=null && fd.getFund().getId()!=null)
					searchMap.put("fundid", fd.getFund().getId());
				if(fd.getBudgetGroup()!=null && fd.getBudgetGroup().getId()!=null)
					searchMap.put("budgetheadid", budgetheadid);  
				if(fd.getScheme()!=null && fd.getScheme().getId()!=null)
					searchMap.put("schemeid", fd.getScheme().getId());
				if(fd.getSubScheme()!=null && fd.getSubScheme().getId()!=null)
					searchMap.put("subschemeid", fd.getSubScheme().getId());
				if(fd.getAbstractEstimate().getUserDepartment()!=null)
					searchMap.put("deptid", fd.getAbstractEstimate().getUserDepartment().getId());
				if(fd.getAbstractEstimate().getWard().getId()!=null)
				searchMap.put("boundaryid", fd.getAbstractEstimate().getWard().getId());
			}
			
			
			try{
				val = budgetDetailsDAO.getBudgetedAmtForYear(searchMap);
			}
			catch(ValidationException valEx){
				logger.error(valEx);
			}
			return val;
	  }
	 
		/**
		 * returns the sanctioned budget for the year
		 * @param paramMap
		 * @return
		 * @throws ValidationException
		 */
			
	 public BigDecimal getTotalGrantForYear(FinancialDetail fd,Long financialyearid)	throws ValidationException {
				BigDecimal val = BigDecimal.ZERO;
				Map<String,Object> searchMap = new HashMap<String, Object>();
				
				searchMap.put("financialyearid", financialyearid);
				
				if(fd!=null ){
					List<BudgetGroup> budgetheadid=new ArrayList<BudgetGroup>();
					budgetheadid.add(fd.getBudgetGroup());				
					if(fd.getFunction()!=null && fd.getFunction().getId()!=null)
						searchMap.put("functionid", fd.getFunction().getId());
					if(fd.getFunctionary()!=null && fd.getFunctionary().getId()!=null)
						searchMap.put("functionaryid", fd.getFunctionary().getId());
					if(fd.getFund()!=null && fd.getFund().getId()!=null)
						searchMap.put("fundid", fd.getFund().getId());
					if(fd.getBudgetGroup()!=null && fd.getBudgetGroup().getId()!=null)
						searchMap.put("budgetheadid", budgetheadid);  
					if(fd.getScheme()!=null && fd.getScheme().getId()!=null)
						searchMap.put("schemeid", fd.getScheme().getId());
					if(fd.getSubScheme()!=null && fd.getSubScheme().getId()!=null)
						searchMap.put("subschemeid", fd.getSubScheme().getId());
					if(fd.getAbstractEstimate().getUserDepartment()!=null)
						searchMap.put("deptid", fd.getAbstractEstimate().getUserDepartment().getId());
					if(fd.getAbstractEstimate().getWard().getId()!=null)
					searchMap.put("boundaryid", fd.getAbstractEstimate().getWard().getId());
				}
				
				
				try{
					val = budgetDetailsDAO.getBudgetedAmtForYear(searchMap);
				}
				catch(ValidationException valEx){
					logger.error(valEx);
				}
				return val;
		  }
	 
	 /**
		 * returns the sanctioned budget for the year
		 * @param paramMap
		 * @return
		 * @throws ValidationException
		 */
			
	 public BigDecimal getTotalGrantForYear(FinancialDetail fd,Long financialyearid,Integer deptid) throws ValidationException {
				BigDecimal val = BigDecimal.ZERO;
				Map<String,Object> searchMap = new HashMap<String, Object>();
				
				searchMap.put("financialyearid", financialyearid);
				searchMap.put("deptid", deptid);
				if(fd!=null ){
					List<BudgetGroup> budgetheadid=new ArrayList<BudgetGroup>();
					budgetheadid.add(fd.getBudgetGroup());				
					if(fd.getFunction()!=null && fd.getFunction().getId()!=null)
						searchMap.put("functionid", fd.getFunction().getId());
					if(fd.getFunctionary()!=null && fd.getFunctionary().getId()!=null)
						searchMap.put("functionaryid", fd.getFunctionary().getId());
					if(fd.getFund()!=null && fd.getFund().getId()!=null)
						searchMap.put("fundid", fd.getFund().getId());
					if(fd.getBudgetGroup()!=null && fd.getBudgetGroup().getId()!=null)
						searchMap.put("budgetheadid", budgetheadid);  
					if(fd.getScheme()!=null && fd.getScheme().getId()!=null)
						searchMap.put("schemeid", fd.getScheme().getId());
					if(fd.getSubScheme()!=null && fd.getSubScheme().getId()!=null)
						searchMap.put("subschemeid", fd.getSubScheme().getId());
					
					if(fd.getAbstractEstimate().getWard().getId()!=null)
					searchMap.put("boundaryid", fd.getAbstractEstimate().getWard().getId());
				}
				  
				
				try{
					val = budgetDetailsDAO.getBudgetedAmtForYear(searchMap);
				}
				catch(ValidationException valEx){
					logger.error(valEx);
				}
				return val;
		  }
	 
	 
	 /**
		 * returns the sanctioned budget for the year as on the date passed
		 * @param paramMap
		 * @return
		 * @throws ValidationException
		 */
			
	 public BigDecimal getTotalGrantForYearAsOnDate(FinancialDetail fd,Long financialyearid,Integer deptid, Date asOnDate) throws ValidationException {
				BigDecimal val = BigDecimal.ZERO;
				Map<String,Object> searchMap = new HashMap<String, Object>(); 
				
				searchMap.put("financialyearid", financialyearid);
				searchMap.put("deptid", deptid);
				if(fd!=null ){
					List<BudgetGroup> budgetheadid=new ArrayList<BudgetGroup>();
					budgetheadid.add(fd.getBudgetGroup());				
					if(fd.getFunction()!=null && fd.getFunction().getId()!=null)
						searchMap.put("functionid", fd.getFunction().getId());
					if(fd.getFunctionary()!=null && fd.getFunctionary().getId()!=null)
						searchMap.put("functionaryid", fd.getFunctionary().getId());
					if(fd.getFund()!=null && fd.getFund().getId()!=null)
						searchMap.put("fundid", fd.getFund().getId());
					if(fd.getBudgetGroup()!=null && fd.getBudgetGroup().getId()!=null)
						searchMap.put("budgetheadid", budgetheadid);  
					if(fd.getScheme()!=null && fd.getScheme().getId()!=null)
						searchMap.put("schemeid", fd.getScheme().getId());
					if(fd.getSubScheme()!=null && fd.getSubScheme().getId()!=null)
						searchMap.put("subschemeid", fd.getSubScheme().getId());
					
					if(fd.getAbstractEstimate().getWard().getId()!=null)
					searchMap.put("boundaryid", fd.getAbstractEstimate().getWard().getId());
				}
				  
				
				try{
					val = budgetDetailsDAO.getBudgetedAmtForYearAsOnDate(searchMap,asOnDate);
				}
				catch(ValidationException valEx){
					logger.error(valEx);
				}
				return val;
		  }
	 
	 /**
		 * returns the planning budget percentage
		 * @param paramMap
		 * @return
		 * @throws ValidationException
		 */			
	 public BigDecimal getPlanningBudgetPercentage(FinancialDetail fd,Long financialyearid,Integer deptid) throws ValidationException {
				BigDecimal val = BigDecimal.ZERO;
				Map<String,Object> searchMap = new HashMap<String, Object>();
				
				searchMap.put("financialyearid", financialyearid);
				searchMap.put("deptid", deptid);
				if(fd!=null ){
					List<BudgetGroup> budgetheadid=new ArrayList<BudgetGroup>();
					budgetheadid.add(fd.getBudgetGroup());				
					if(fd.getFunction()!=null && fd.getFunction().getId()!=null)
						searchMap.put("functionid", fd.getFunction().getId());
					if(fd.getFunctionary()!=null && fd.getFunctionary().getId()!=null)
						searchMap.put("functionaryid", fd.getFunctionary().getId());
					if(fd.getFund()!=null && fd.getFund().getId()!=null)
						searchMap.put("fundid", fd.getFund().getId());
					if(fd.getBudgetGroup()!=null && fd.getBudgetGroup().getId()!=null)
						searchMap.put("budgetheadid", budgetheadid);  
					if(fd.getScheme()!=null && fd.getScheme().getId()!=null)
						searchMap.put("schemeid", fd.getScheme().getId());
					if(fd.getSubScheme()!=null && fd.getSubScheme().getId()!=null)
						searchMap.put("subschemeid", fd.getSubScheme().getId());
					
					if(fd.getAbstractEstimate().getWard().getId()!=null)
					searchMap.put("boundaryid", fd.getAbstractEstimate().getWard().getId());
				}
				  				
				try{
					val = budgetDetailsDAO.getPlanningPercentForYear(searchMap);
				}
				catch(ValidationException valEx){
					logger.error(valEx);
				}
				return val;
		  }
		
		/**
		 * This method will return budgetary appropriation done estimate list  for budgetHead 
		 * @param  
		 */
		 public Map<String,List>  getApprovedAppropriationDetailsForBugetHead(AbstractEstimate viewEstimate,BigDecimal totalGrantPerc){
			logger.debug("1---inside getApprovedAppropriationDetailsForBugetHead------");
			 List<BudgetFolioDetail> approvedBudgetFolioResultList=new ArrayList<BudgetFolioDetail>();
			 Map<String, Object> queryParamMap=new HashMap<String,Object>();
			 FinancialDetail fd=null;
			 Long deptId=null;
			 Long functionId=null;
			 Integer fundId=null;
			 Long budgetHeadId=null;
			 Long financialYearId=null;	 			 
			 Map<String,List>  budgetFolioMap=null;			 
			
			 //fundId,ExecutionDepartmentId,functionId,moduleId,financialYearId,budgetgroupId,fromDate,toDate and Order By
			 if(viewEstimate!=null && viewEstimate.getFinancialDetails().get(0)!=null){
				 fd=viewEstimate.getFinancialDetails().get(0);
			 }
		
			 if(fd!=null){
				if(fd.getBudgetGroup()!=null && fd.getBudgetGroup().getId()!=null){
					budgetHeadId=viewEstimate.getFinancialDetails().get(0).getBudgetGroup().getId();
					queryParamMap.put("budgetgroupId", budgetHeadId);
				}
				if(fd.getAbstractEstimate().getUserDepartment()!=null){
					deptId=fd.getAbstractEstimate().getUserDepartment().getId();
					queryParamMap.put("ExecutionDepartmentId", deptId);
				}
				if(fd.getFunction()!=null && fd.getFunction().getId()!=null){
					functionId=fd.getFunction().getId();
					queryParamMap.put("functionId", functionId);
				}
				
				if(fd.getFund()!=null && fd.getFund()!=null && fd.getFund().getId()!=null){
					fundId=fd.getFund().getId();
					queryParamMap.put("fundId", fundId);
				}
				if(fd.getAbstractEstimate()!=null && fd.getAbstractEstimate().getLeastFinancialYearForEstimate()!=null
						&&fd.getAbstractEstimate().getLeastFinancialYearForEstimate().getId()!=null){
					financialYearId=fd.getAbstractEstimate().getLeastFinancialYearForEstimate().getId();
					queryParamMap.put("financialYearId", financialYearId);
					addFinancialYearToQuery(fd,queryParamMap);
				}
			 }						
			 Integer moduleId=11;
			 queryParamMap.put("moduleId", moduleId);
			 
			 List<BudgetUsage> budgetUsageList=egovCommon.getListBudgetUsage(queryParamMap);	
			
			 if(budgetUsageList != null && !budgetUsageList.isEmpty()){
				 budgetFolioMap = addApprovedEstimateResultList(approvedBudgetFolioResultList,budgetUsageList,totalGrantPerc);
			 }		
			 return budgetFolioMap;
		}
		 
		public Map<String,List>  getApprovedAppropriationDetailsForBugetHead(Map<String,Object> queryParamMap){
			List<BudgetFolioDetail> approvedBudgetFolioResultList=new ArrayList<BudgetFolioDetail>();
			Map<String, Object> paramMap=new HashMap<String,Object>();
			if(queryParamMap.get("budgetheadid")!=null) {
				List<BudgetGroup> budgetheadid=(List)queryParamMap.get("budgetheadid");
				BudgetGroup bg=budgetheadid.get(0);
				paramMap.put("budgetgroupId", bg.getId());
			}
			if(queryParamMap.get("deptid")!=null)
				paramMap.put("ExecutionDepartmentId", queryParamMap.get("deptid"));
			if(queryParamMap.get("functionid")!=null)
				paramMap.put("functionId", queryParamMap.get("functionid"));
			if(queryParamMap.get("fundid")!=null)
				paramMap.put("fundId", queryParamMap.get("fundid"));
			if(queryParamMap.get("financialyearid")!=null)
				paramMap.put("financialYearId", queryParamMap.get("financialyearid"));
			if(queryParamMap.get(FROM_DATE)!=null)
				paramMap.put(FROM_DATE, queryParamMap.get(FROM_DATE));
			if(queryParamMap.get(TO_DATE)!=null)
				paramMap.put(TO_DATE, queryParamMap.get(TO_DATE));
			Integer moduleId=11;
			paramMap.put("moduleId", moduleId);
			List<BudgetUsage> budgetUsageList=budgetDetailsDAO.getListBudgetUsage(paramMap);	
			if(budgetUsageList != null && !budgetUsageList.isEmpty()){
				return addApprovedEstimateResultList(approvedBudgetFolioResultList,budgetUsageList,
						new BigDecimal(queryParamMap.get("totalGrantPerc").toString()));
			}
			return new HashMap<String,List>();
		}
			
		public void addFinancialYearToQuery(FinancialDetail fd,Map<String, Object> queryParamMap){
			if(fd.getAbstractEstimate().getLeastFinancialYearForEstimate().getStartingDate()!=null)
				queryParamMap.put(FROM_DATE, fd.getAbstractEstimate().getLeastFinancialYearForEstimate().getStartingDate());
		//	if(fd.getAbstractEstimate().getLeastFinancialYearForEstimate().getEndingDate()!=null)
				queryParamMap.put(TO_DATE, new Date());	
		}		
	 
		public Map<String,List> addApprovedEstimateResultList(List<BudgetFolioDetail> budgetFolioResultList, List<BudgetUsage> budgetUsageList,
				BigDecimal totalGrantPerc){
			int srlNo=1;
			Double cumulativeTotal = 0.00D;
			BigDecimal balanceAvailable  = BigDecimal.ZERO;
			Map<String,List>  budgetFolioMap=new HashMap<String, List>();
			for(BudgetUsage budgetUsage : budgetUsageList){
				BudgetFolioDetail budgetFolioDetail=new BudgetFolioDetail();
				budgetFolioDetail.setSrlNo(srlNo++);
				
				AbstractEstimate estimate=find("from AbstractEstimate ae where ae.estimateNumber=?", budgetUsage.getReferenceNumber());
				if(estimate != null){	
					 budgetFolioDetail.setEstimateNo(estimate.getEstimateNumber());
					 budgetFolioDetail.setNameOfWork(estimate.getName());
					 budgetFolioDetail.setWorkValue(estimate.getTotalAmount().getValue());
					 budgetFolioDetail.setEstimateDate(sdf.format(estimate.getEstimateDate()));	
					 
				} 
				
				budgetFolioDetail.setBudgetApprNo(budgetUsage.getAppropriationnumber());
				budgetFolioDetail.setCumulativeTotal(cumulativeTotal);
				balanceAvailable = totalGrantPerc.subtract(new BigDecimal(cumulativeTotal));
				budgetFolioDetail.setBalanceAvailable(balanceAvailable);
				budgetFolioDetail.setAppDate(sdf.format(new Date(budgetUsage.getUpdatedTime().getTime())));
				budgetFolioDetail.setAppType(getApporpriationType(budgetUsage.getId()));
				budgetFolioResultList.add(budgetFolioDetail);
				
				if(budgetUsage.getReleasedAmount()>0){
					cumulativeTotal = cumulativeTotal - budgetUsage.getReleasedAmount();
					budgetFolioDetail.setAppropriatedValue(0.0-budgetUsage.getReleasedAmount());
				}
				else{
					cumulativeTotal = cumulativeTotal + budgetUsage.getConsumedAmount();
					budgetFolioDetail.setAppropriatedValue(budgetUsage.getConsumedAmount());
				}
			}
			List calculatedValuesList = new ArrayList();
			calculatedValuesList.add(cumulativeTotal);
			calculatedValuesList.add(totalGrantPerc.subtract(new BigDecimal(cumulativeTotal)));
			budgetFolioMap.put("budgetFolioList", budgetFolioResultList);
			budgetFolioMap.put("calculatedValues",calculatedValuesList);
			return budgetFolioMap;
		 }
		 
		 public Map<String, Object> createBudgetFolioHeaderJasperObject(AbstractEstimate ae,BigDecimal totalGrant,BigDecimal totalGrantPer){		 
			HashMap<String,Object> budgetFolioMapObject = new HashMap<String,Object>();
			String departmentName="";
			String functionCenter="";
			String budgetHead="";
			String fund="";	
			FinancialDetail fd=null;
				
			if(ae!=null && ae.getExecutingDepartment()!=null && ae.getExecutingDepartment().getName()!=null)
				departmentName=ae.getExecutingDepartment().getName();
				
			if(ae!=null && ae.getFinancialDetails()!=null &&  ae.getFinancialDetails().get(0)!=null)
				fd=ae.getFinancialDetails().get(0);
				
			if(fd!=null){
				if(fd.getFunction()!=null && fd.getFunction()!=null && fd.getFunction().getName()!=null)
					functionCenter=ae.getFinancialDetails().get(0).getFunction().getName();
				if(fd.getBudgetGroup()!=null && fd.getBudgetGroup().getName()!=null)
					budgetHead=fd.getBudgetGroup().getName();
				if(fd.getFund()!=null && fd.getFund().getName()!=null)
					fund=fd.getFund().getName();
			}
			budgetFolioMapObject.put("departmentName", departmentName);
			budgetFolioMapObject.put("functionCenter", functionCenter);
			budgetFolioMapObject.put("budgetHead", budgetHead);
			budgetFolioMapObject.put("fund", fund);
			budgetFolioMapObject.put("totalGrant", totalGrant);
			budgetFolioMapObject.put("totalGrantPer", totalGrantPer);
			return budgetFolioMapObject;
    }

	public String getApporpriationType(long budgetUsageId){
		String appType="Regular";
		if(estimateAppropriationService!=null){
			List allReadyAppropriatedBudgetUsageList=estimateAppropriationService.findAllBy("from AbstractEstimateAppropriation where abstractEstimate.id=(select max(abstractEstimate.id) from AbstractEstimateAppropriation where budgetUsage.id=?) and budgetUsage.id<?",budgetUsageId,budgetUsageId);
			if(allReadyAppropriatedBudgetUsageList.size()!=0){
				appType="Re-Appropriation";
			}
		}
		return appType;
	}
	public void setEstimateNumberGenerator(
			EstimateNumberGenerator estimateNumberGenerator) {
		this.estimateNumberGenerator = estimateNumberGenerator;
	}
	
	public void setProjectcodeGenerator(ProjectCodeGenerator projectcodeGenerator) {
		this.projectcodeGenerator = projectcodeGenerator;
	}
	public void setProjectCodeService(
			PersistenceService<ProjectCode, Long> projectCodeService) {
		this.projectCodeService = projectCodeService;
	}


	public PersistenceService<ProjectCode, Long> getProjectCodeService() {
		return this.projectCodeService;
	}


	public BudgetGroupDAO getBudgetGroupDAO() {
		return budgetGroupDAO;
	}


	public void setBudgetGroupDAO(BudgetGroupDAO budgetGroupDAO) {
		this.budgetGroupDAO = budgetGroupDAO;
	}


	public BudgetDetailsDAO getBudgetDetailsDAO() {
		return budgetDetailsDAO;
	}
	
	public boolean validateCancelEstimate(AbstractEstimate abstractEstimate) {
		boolean flag=false;
		List<TenderResponse> tenderResponse = tenderResponseService.findAllByNamedQuery("MY_NONCANCELLED_NEGOTIATIONSTATEMENTS", abstractEstimate.getId());
		if(tenderResponse.isEmpty())
			flag=true;
		return flag;
	}

	public void setTenderResponseService(
			PersistenceService<TenderResponse, Long> tenderResponseService) {
		this.tenderResponseService = tenderResponseService;
	}
		
	public void setWorksService(WorksService worksService) {
		this.worksService = worksService;
	}	
	
	public Assignment getLatestAssignmentForCurrentLoginUser() {
		PersonalInformation personalInformation=null;
		Long currentLoginUserId = worksService.getCurrentLoggedInUserId();
		if(currentLoginUserId != null && currentLoginUserId != 0) {
			personalInformation=employeeService.getEmpForUserId(currentLoginUserId);		    
		}
		Assignment assignment=null;
		if(personalInformation!=null){
			assignment=employeeService.getLatestAssignmentForEmployee(personalInformation.getIdPersonalInformation());			
		}
		return assignment; 
	}
	
	public CFinancialYear getCurrentFinancialYear(Date estimateDate) {
		return commonsService.getFinYearByDate(estimateDate);
	}
	
	public CFinancialYear getPreviousFinancialYear() {
		return commonsService.getFinancialYearById(Long.parseLong(commonsService.getPrevYearFiscalId()));
	}

	public Date getLatestApprYearEndDate(FinancialDetail financialDetail) {
		AbstractEstimateAppropriation estimateAppropriation=estimateAppropriationService.findByNamedQuery("getLatestBudgetUsageForEstimate",financialDetail.getAbstractEstimate().getId());
		if(estimateAppropriation!=null) {
			return commonsService.getFinancialYearById(estimateAppropriation.getBudgetUsage().getFinancialYearId().longValue()).getEndingDate();
		}
		else {
			return new Date();
		}

	}
	public BigDecimal getBudgetAvailable(AbstractEstimate estimate,Date date) throws ValidationException{
		   BigDecimal budgetAvailable=BigDecimal.ZERO;
		   Long finYearId=null;
		   List<Long> budgetheadid=new ArrayList<Long>();
		   if(date==null) {
			   finYearId=commonsService.getFinYearByDate(new Date()).getId();
			}
		   else {
			   finYearId=commonsService.getFinYearByDate(date).getId();
		   }
		   if(estimate.getFinancialDetails()!=null && estimate.getFinancialDetails().size()>0){
			   FinancialDetail financialDetail = estimate.getFinancialDetails().get(0);
				budgetheadid.add(financialDetail.getBudgetGroup().getId());
			   return  budgetDetailsDAO.getPlanningBudgetAvailable(
					     finYearId, 
					         Integer.parseInt(estimate.getUserDepartment().getId().toString()),
						 (financialDetail.getFunction()==null? null:financialDetail.getFunction().getId()), null,
						 (financialDetail.getScheme()==null? null:financialDetail.getScheme().getId()), 
						 (financialDetail.getSubScheme()==null? null:financialDetail.getSubScheme().getId()), 
						 Integer.parseInt(estimate.getWard().getId().toString()),
						 (financialDetail.getBudgetGroup()==null? null:budgetheadid),
						 (financialDetail.getFund()==null? null:financialDetail.getFund().getId()));
		   }else
			   return budgetAvailable;
	 }
	
	public Boolean isPreviousYearApprRequired(FinancialDetail financialDetail) {
		if("yes".equalsIgnoreCase(worksService.getWorksConfigValue("PREVIOUS_YEAR_APPROPRIATION_ALLOWED")) && financialDetail!=null && financialDetail.getApprYear()!=null && WorksConstants.PREVIOUS_APPROPRIATION_YEAR.equalsIgnoreCase(financialDetail.getApprYear())) {
			return Boolean.TRUE;
		}
		else {
			return Boolean.FALSE;
		}
	}
	
	public BigDecimal getBudgetAvailable(Integer departmentId,Long functionId,Integer fundId,Long budgetGroupId,Long finYearId) throws ValidationException{
		logger.info("Start of getBudgetAvailable(Long functionId,Integer fundId,Long budgetGroupId,Long finYearId) : functionId="+functionId+"fundId:"+fundId+"budgetGroupId="+budgetGroupId+"finYearId="+finYearId);
		List<Long> budgetheadid=new ArrayList<Long>();
		if(functionId==null||fundId==null ||budgetGroupId==null ||finYearId==null ||departmentId==null) {
			throw new EGOVRuntimeException("Error:Invalid Argument passed to getBudgetAvailable(Integer departmentId,Long functionId,Integer fundId,Long budgetGroupId,Long finYearId)");
		}
		budgetheadid.add(budgetGroupId);

		return  budgetDetailsDAO.getPlanningBudgetAvailable(finYearId,departmentId,functionId, null,null,null,null,budgetheadid,fundId);
	}

	public List<AbstractEstimate> getAbEstimateListById(String estId)
	{
		String[] estValues = estId.split("`~`");
		Long[] estIdLong=new Long[estValues.length];
		Set<Long> abIdentifierSet = new HashSet<Long>();
		int j=0;
		for(int i=0;i<estValues.length;i++){
			if(StringUtils.isNotBlank(estValues[i])){
				estIdLong[j] = Long.valueOf(estValues[i]);
				j++;
			}
		}
		abIdentifierSet.addAll(Arrays.asList(estIdLong));
		return findAllByNamedQuery("getAbEstimateListById", abIdentifierSet);
	}
	
	public Money getWorkValueIncludingTaxesForEstList(List<AbstractEstimate> abList)
	{
		double amt=0;
		if(!abList.isEmpty()){
			for(AbstractEstimate ab :abList)
				amt+=ab.getWorkValueIncludingTaxes().getValue();
		}
		return new Money(amt);
	}
	

	public void setEgovCommon(EgovCommon egovCommon) {
		this.egovCommon = egovCommon;
	}

	public EgovCommon getEgovCommon() {
		return egovCommon;
	}
	
	public boolean checkForBudgetaryAppropriationForDepositWorks(FinancialDetail financialDetail) 
	throws ValidationException{			
		boolean flag=false;
		Date appDate=new Date();
		double depApprAmnt=0.0;
		Accountdetailtype accountdetailtype=worksService.getAccountdetailtypeByName("DEPOSITCODE");
		
		//In case of deposit work whole amount will be appropriated at once.
		if(appDate.compareTo(financialDetail.getAbstractEstimate().getEstimateDate())>=0){
				depApprAmnt= financialDetail.getAbstractEstimate().getTotalAmount().getValue();
		}
		
		BigDecimal creditBalance=egovCommon.getDepositAmountForDepositCode(new Date(),financialDetail.getCoa().getGlcode(),financialDetail.getFund().getCode(), accountdetailtype.getId(), financialDetail.getAbstractEstimate().getDepositCode().getId().intValue());
		BigDecimal utilizedAmt=depositWorksUsageService.getTotalUtilizedAmountForDepositWorks(financialDetail,appDate);
		BigDecimal balance=BigDecimal.ZERO;
		if(utilizedAmt==null){
			balance=creditBalance;
			utilizedAmt=BigDecimal.ZERO;
		}
		else{
			balance=creditBalance.subtract(utilizedAmt);
		}
		
		if(balance.doubleValue()>=depApprAmnt) {
			DepositWorksUsage depositWorksUsage=new DepositWorksUsage();
			CFinancialYear budgetApprDate_finYear=commonsService.getFinYearByDate(appDate);
			depositWorksUsage.setTotalDepositAmount(creditBalance);
			depositWorksUsage.setConsumedAmount(new BigDecimal(depApprAmnt));
			depositWorksUsage.setReleasedAmount(BigDecimal.ZERO);
			depositWorksUsage.setAppropriationNumber(financialDetail.getAbstractEstimate().getBudgetApprNo());
			depositWorksUsage.setAbstractEstimate(financialDetail.getAbstractEstimate());
			depositWorksUsage.setAppropriationDate(appDate);
			depositWorksUsage.setFinancialYearId(budgetApprDate_finYear.getId().intValue());
			depositWorksUsage.setDepositCode(financialDetail.getAbstractEstimate().getDepositCode());
			depositWorksUsage.setCoa(financialDetail.getCoa());
			depositWorksUsage=depositWorksUsageService.persist(depositWorksUsage);
			persistDepositCodeAppDetails(depositWorksUsage);
			flag=true;
		}
		return flag;
	}
	
	public boolean releaseDepositWorksAmountOnReject(FinancialDetail financialDetail) 
	throws ValidationException{			
		boolean flag=false;
		Accountdetailtype accountdetailtype=worksService.getAccountdetailtypeByName("DEPOSITCODE");
		AbstractEstimateAppropriation estimateAppropriation=estimateAppropriationService.findByNamedQuery("getLatestDepositWorksUsageForEstimate",financialDetail.getAbstractEstimate().getId());
		//double estimateAmount=financialDetail.getAbstractEstimate().getTotalAmount().getValue();
		BigDecimal creditBalance=egovCommon.getDepositAmountForDepositCode(new Date(),financialDetail.getCoa().getGlcode(),financialDetail.getFund().getCode(), accountdetailtype.getId(), financialDetail.getAbstractEstimate().getDepositCode().getId().intValue());
		double releaseAmount=estimateAppropriation.getDepositWorksUsage().getConsumedAmount().doubleValue();
		DepositWorksUsage depositWorksUsage=new DepositWorksUsage();
		depositWorksUsage.setTotalDepositAmount(creditBalance);
		depositWorksUsage.setConsumedAmount(BigDecimal.ZERO);
		depositWorksUsage.setReleasedAmount(new BigDecimal(releaseAmount));
		depositWorksUsage.setAppropriationNumber(financialDetail.getAbstractEstimate().getBudgetRejectionNo());
		depositWorksUsage.setAbstractEstimate(financialDetail.getAbstractEstimate());
		depositWorksUsage.setAppropriationDate(new Date());
		depositWorksUsage.setFinancialYearId(estimateAppropriation.getDepositWorksUsage().getFinancialYearId());
		depositWorksUsage.setCoa(financialDetail.getCoa());
		depositWorksUsage.setDepositCode(financialDetail.getAbstractEstimate().getDepositCode());
		depositWorksUsage=depositWorksUsageService.persist(depositWorksUsage);
		persistReleaseDepositWorksAmountDetails(depositWorksUsage);
		flag=true;
		return flag;
	}

	public void setDepositWorksUsageService(
			DepositWorksUsageService depositWorksUsageService) {
		this.depositWorksUsageService = depositWorksUsageService;
	}

	public DepositWorksUsageService getDepositWorksUsageService() {
		return depositWorksUsageService;
	}

	private void persistBudgetAppropriationDetails(AbstractEstimate abstractEstimate,BudgetUsage budgetUsage){
		AbstractEstimateAppropriation estimateAppropriation=null;
		Integer finYearId=budgetUsage.getFinancialYearId();
		Date endingDate=commonsService.getFinancialYearById(finYearId.longValue()).getEndingDate();
		estimateAppropriation=estimateAppropriationService.findByNamedQuery("getBudgetUsageForEstimateByFinYear",abstractEstimate.getId(),finYearId.intValue());

		if(estimateAppropriation!=null){
			estimateAppropriation.setBalanceAvailable(getBudgetAvailable(abstractEstimate,endingDate));
			estimateAppropriation.setBudgetUsage(budgetUsage);
		}else{
			estimateAppropriation=new AbstractEstimateAppropriation();
			estimateAppropriation.setAbstractEstimate(abstractEstimate);
			estimateAppropriation.setBalanceAvailable(getBudgetAvailable(abstractEstimate,endingDate));
			estimateAppropriation.setBudgetUsage(budgetUsage);
		}
		estimateAppropriationService.persist(estimateAppropriation);
	}
	
	
	private void persistDepositCodeAppDetails(DepositWorksUsage depositWorksUsage){
		AbstractEstimateAppropriation estimateAppropriation=null;
		int finYearId=commonsService.getFinYearByDate(new Date()).getId().intValue();
		BigDecimal creditBalance=depositWorksUsage.getTotalDepositAmount();
		AbstractEstimate abstractEstimate=depositWorksUsage.getAbstractEstimate();
		BigDecimal utilizedAmt=depositWorksUsageService.getTotalUtilizedAmountForDepositWorks(abstractEstimate.getFinancialDetails().get(0),depositWorksUsage.getCreatedDate());
		BigDecimal balance=BigDecimal.ZERO;
		if(utilizedAmt==null){
			balance=creditBalance;
		    utilizedAmt=BigDecimal.ZERO;
		}
		else{
			balance=creditBalance.subtract(utilizedAmt);
		}

		estimateAppropriation=estimateAppropriationService.findByNamedQuery("getDepositWorksUsageForEstimateByFinYear",abstractEstimate.getId(),finYearId);
		if(estimateAppropriation!=null){
			estimateAppropriation.setBalanceAvailable(balance);
			estimateAppropriation.setDepositWorksUsage(depositWorksUsage);
		}else{
			estimateAppropriation=new AbstractEstimateAppropriation();
			estimateAppropriation.setAbstractEstimate(abstractEstimate);
			estimateAppropriation.setBalanceAvailable(balance);
			estimateAppropriation.setDepositWorksUsage(depositWorksUsage);
		}
		estimateAppropriationService.persist(estimateAppropriation);
	}

	private void persistBudgetReleaseDetails(AbstractEstimate abstractEstimate,BudgetUsage budgetUsage){
		AbstractEstimateAppropriation estimateAppropriation=null;
		estimateAppropriation=estimateAppropriationService.findByNamedQuery("getLatestBudgetUsageForEstimate",abstractEstimate.getId());
		Integer finYearId =estimateAppropriation.getBudgetUsage().getFinancialYearId();
		Date endingDate=commonsService.getFinancialYearById(finYearId.longValue()).getEndingDate();
		estimateAppropriation.setBalanceAvailable(getBudgetAvailable(abstractEstimate,endingDate));
		estimateAppropriation.setBudgetUsage(budgetUsage);
		estimateAppropriationService.persist(estimateAppropriation);
	}
	
	private void persistReleaseDepositWorksAmountDetails(DepositWorksUsage depositWorksUsage){
		AbstractEstimateAppropriation estimateAppropriation=null;
		BigDecimal creditBalance=depositWorksUsage.getTotalDepositAmount();
		AbstractEstimate abstractEstimate=depositWorksUsage.getAbstractEstimate();
		BigDecimal utilizedAmt=depositWorksUsageService.getTotalUtilizedAmountForDepositWorks(abstractEstimate.getFinancialDetails().get(0),new Date());
		BigDecimal balance=creditBalance.subtract(utilizedAmt);
		estimateAppropriation=estimateAppropriationService.findByNamedQuery("getLatestDepositWorksUsageForEstimate",abstractEstimate.getId());
		estimateAppropriation.setBalanceAvailable(balance);
		estimateAppropriation.setDepositWorksUsage(depositWorksUsage);
		estimateAppropriationService.persist(estimateAppropriation);
	}
	
/*
 * This API will return amount to be appropriated for estimate in the given financial year.
 * @param estimate,finYearId
 * @return 0 if no appropriation required otherwise appropriation amount
 * 
 */
	public Double getEstimateAppropriationAmountForFinyear(AbstractEstimate estimate,Integer finYearId){
		Double percentage=0.0;
		Double appropriationAmount=0.0;		
		logger.debug("start of getEstimateAppropriationAmountByFinyear() || estimate number="+estimate.getEstimateNumber());
		if(estimate==null || finYearId==null) {
			throw new EGOVRuntimeException("Invalid argument passed to getEstimateAppropriationAmountForFinyear()");
		}
		for(MultiYearEstimate multiYearEstimate:estimate.getMultiYearEstimates()) {
			if(multiYearEstimate.getFinancialYear().getId().intValue()==finYearId) {
				percentage=multiYearEstimate.getPercentage();
				break;
			}
		}
		if(percentage!=0.0) {
			appropriationAmount=(estimate.getTotalAmount().getValue())*(percentage/100);
		}
		logger.debug("end of getEstimateAppropriationAmountByFinyear() ||appropariation amount="+appropriationAmount+"||estimate number ||"+estimate.getEstimateNumber()+" Finyear ||"+finYearId);
		return appropriationAmount;
		
	}
	public void setEstimateAppropriationService(
			PersistenceService<AbstractEstimateAppropriation, Long> estimateAppropriationService) {
		this.estimateAppropriationService = estimateAppropriationService;
	}

	public PersistenceService<AbstractEstimateAppropriation, Long> getEstimateAppropriationService() {
		return estimateAppropriationService;
	}
		
	public List<EgUom> prepareUomListByExcludingSpecialUoms(List<EgUom> uomList) {
		Set<String> exceptionSor = worksService.getExceptionSOR().keySet();
		List<EgUom> newList = new ArrayList<EgUom>();
		for (EgUom uom : uomList) {
			if (!exceptionSor.contains(uom.getUom())) {
				newList.add(uom);
			}
		}
		return newList;
	}
	
	public List<BudgetUsage> getBudgetUsageListForEstNo(String estNumber){
		 List<BudgetUsage> buList = new ArrayList<BudgetUsage>();
		 	buList = budgetUsageService.findAllBy("from BudgetUsage  where referenceNumber = ?) ", estNumber);
		 return buList;
	 }
	
	/**
	 * This function returns List of objects containing workOder Id and WorkOrder number.
	 * This method in turn calls  getWorkOrderDetails() by passing estimateId as parameter
	 * which runs the query to get the list of objects.
	 * @param estimateId
	 * @return List<Object> containing workOder Id and WorkOrder number
	 */
	public List<Object> getWODetailsForEstimateId(Long estimateId) {
		List<Object> woDetails = new ArrayList<Object>();
		woDetails = workOrderService.getWorkOrderDetails(estimateId);
		return woDetails;
	}
	
	/**
	 * This function returns List of objects containing worksPackage Id and worksPackage number.
	 * This method in turn calls  getWorksPackageDetails() by passing estimateId as parameter
	 * which runs the query to get the list of objects.
	 * @param estimateId
	 * @return List<Object> containing worksPackage Id and worksPackage number
	 */
	public List<Object> getWPDetailsForEstimateId(Long estimateId) {
		List<Object> wpDetails = new ArrayList<Object>();
		wpDetails =  workspackageService.getWorksPackageDetails(estimateId);
		return wpDetails;
	}
	
	public PersistenceService<BudgetUsage, Long> getBudgetUsageService() {
		return budgetUsageService;
	}

	public void setBudgetUsageService(
			PersistenceService<BudgetUsage, Long> budgetUsageService) {
		this.budgetUsageService = budgetUsageService;
	}

	public WorkOrderService getWorkOrderService() {
		return workOrderService;
	}

	public void setWorkOrderService(WorkOrderService workOrderService) {
		this.workOrderService = workOrderService;
	}

	public WorksPackageService getWorkspackageService() {
		return workspackageService;
	}

	public void setWorkspackageService(WorksPackageService workspackageService) {
		this.workspackageService = workspackageService;
	}

}
