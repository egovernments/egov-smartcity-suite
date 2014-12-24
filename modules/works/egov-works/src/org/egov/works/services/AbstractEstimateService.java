package org.egov.works.services;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
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
import org.egov.infstr.client.filter.EGOVThreadLocals;
import org.egov.infstr.commons.dao.GenericHibernateDaoFactory;
import org.egov.infstr.config.AppConfigValues;
import org.egov.infstr.models.Money;
import org.egov.infstr.services.PersistenceService;
import org.egov.lib.admbndry.Boundary;
import org.egov.lib.admbndry.BoundaryDAO;
import org.egov.model.budget.BudgetGroup;
import org.egov.model.budget.BudgetUsage;
import org.egov.pims.model.Assignment;
import org.egov.pims.model.PersonalInformation;
import org.egov.pims.service.EmployeeService;
import org.egov.works.models.estimate.AbstractEstimate;
import org.egov.works.models.estimate.AdminSanctionNumberGenerator;
import org.egov.works.models.estimate.BudgetFolioDetail;
import org.egov.works.models.estimate.BudgetNumberGenerator;
import org.egov.works.models.estimate.DepositWorksUsage;
import org.egov.works.models.estimate.EstimateNumberGenerator;
import org.egov.works.models.estimate.FinancialDetail;
import org.egov.works.models.estimate.ProjectCode;
import org.egov.works.models.estimate.ProjectCodeGenerator;
import org.egov.works.models.estimate.TechnicalSanction;
import org.egov.works.models.estimate.TechnicalSanctionNumberGenerator;
import org.egov.works.models.revisionEstimate.RevisionAbstractEstimate;
import org.egov.works.utils.WorksConstants;
import org.hibernate.Query;


public class AbstractEstimateService extends PersistenceService<AbstractEstimate,Long> {
	private static final Logger logger = Logger.getLogger(AbstractEstimateService.class);
	private EstimateNumberGenerator estimateNumberGenerator;
	private BudgetNumberGenerator budgetNumberGenerator;
	private ProjectCodeGenerator projectcodeGenerator;
	private PersistenceService<ProjectCode, Long> projectCodeService;
	
	private DepositWorksUsageService depositWorksUsageService;
	
	private BudgetDetailsDAO budgetDetailsDAO;
	private GenericHibernateDaoFactory genericHibDao;
	private EmployeeService employeeService;
	private BudgetGroupDAO budgetGroupDAO;
	private BoundaryDAO boundaryDao = new BoundaryDAO();
	private CommonsService commonsService;
//	private MastersManager mastersMgr;
	private WorksService worksService;
	
	public static final String APPROVED="APPROVED";
	public static final String UNAPPROVED="UNAPPROVED";
	private final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy",Locale.US);	
	private EgovCommon egovCommon;
	private static final String FROM_DATE="fromDate";
	private static final String TO_DATE="toDate";
	private PersistenceService<TechnicalSanction, Long> technicalSanctionService;
	
	private TechnicalSanctionNumberGenerator technicalSanctionNumberGenerator;
	private AdminSanctionNumberGenerator adminSanctionNumberGenerator;
	private PersistenceService<RevisionAbstractEstimate,Long> revisionAbstractEstimateService;
	
	public void setGenericHibDao(GenericHibernateDaoFactory genericHibDao) {
		this.genericHibDao = genericHibDao;
	}

	public void setBudgetDetailsDAO(BudgetDetailsDAO budgetDetailsDAO) {
		this.budgetDetailsDAO = budgetDetailsDAO;
	}

	public void setBudgetNumberGenerator(BudgetNumberGenerator budgetNumberGenerator) {
		this.budgetNumberGenerator = budgetNumberGenerator;
	}

	public void setAdminSanctionNumberGenerator(
			AdminSanctionNumberGenerator adminSanctionNumberGenerator) {
		this.adminSanctionNumberGenerator = adminSanctionNumberGenerator;
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
		//CFinancialYear finYear = getCurrentFinancialYear(entity.getEstimateDate());
		CFinancialYear finYear=commonsService.getFinancialYearByDate(entity.getEstimateDate());
		return budgetNumberGenerator.getBudgetApprNo(entity, finYear);
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
		
		if(estNum[0].equals(entity.getExecutingDepartment().getDeptCode()) && 
				estNum[1].equals(financialYear.getFinYearRange())){
			return false;
		}
		return true;
	}
	
	public void setEstimateNumber(AbstractEstimate entity) {
		//CFinancialYear financialYear=getCurrentFinancialYear(entity.getEstimateDate());
		CFinancialYear financialYear=commonsService.getFinancialYearByDate(entity.getEstimateDate());
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
		//CFinancialYear finYear=getCurrentFinancialYear(entity.getEstimateDate());
		CFinancialYear finYear=commonsService.getFinancialYearByDate(entity.getEstimateDate());
		ProjectCode projectCode = new ProjectCode(entity, null);
		projectCode.setCode(projectcodeGenerator.generateProjectcode(entity, finYear));
		projectCode.setCodeName(entity.getName());
		projectCode.setDescription(entity.getName());
		projectCode.setIsActive(true);
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
		
	public Double getConsumedBudgetForBoundary(Boundary boundary) {
		List<Integer> boundaryIds=new ArrayList<Integer>();
		List<Boundary> 	boundaries=null;
		Double consumedAmount=0.0;
		Query query=getSession().createQuery(
				"select sum(ae.workValue) from AbstractEstimate ae " +
				"where ae.ward.id in (:bndlist) and " +
				"ae.egwStatus.code=:code and " +
				"ae.estimateNumber in (select referenceNumber from org.egov.model.budget.BudgetUsage where financialYearId=:finyearId) "
			);
		try
		{
			boundaries=boundaryDao.getChildBoundaries(boundary.getId().toString());
			boundaries.add(boundaryDao.getBoundary(boundary.getId()));
		}
		catch(Exception exp){
			throw new EGOVRuntimeException("Error occurred in getConsumedBudgetForBoundary() for getChildBoundaries()", exp);
		}

		for(Boundary bndry:boundaries){
			boundaryIds.add(bndry.getId());
		}	

		query.setParameterList("bndlist", boundaryIds);
		query.setParameter("code", WorksConstants.BUDGET_CONSUMPTION_STATUS);
		query.setParameter("finyearId", Integer.parseInt(commonsService.getCurrYearFiscalId()));
		consumedAmount=(Double)query.uniqueResult();

		if(consumedAmount==null){
			return 0.0;
		}
		else
		{
			return consumedAmount;
		}
	}

	
	public List<AppConfigValues> getAppConfigValue(String moduleName,String key){
		return genericHibDao.getAppConfigValuesDAO().getConfigValuesByModuleAndKey(
				moduleName, key);
	}
	
	public boolean checkForBudgetaryAppropriation(FinancialDetail financialDetail) 
	throws ValidationException{
		
		CFinancialYear finYear = financialDetail.getAbstractEstimate().getLeastFinancialYearForEstimate();
		List<Long> budgetheadid=new ArrayList<Long>();
		budgetheadid.add(financialDetail.getBudgetGroup().getId());
		
		boolean flag=false;

		flag= budgetDetailsDAO.consumeEncumbranceBudget(
			finYear.getId(), 
			Integer.valueOf(11), 
			financialDetail.getAbstractEstimate().getEstimateNumber(), 
			financialDetail.getAbstractEstimate().getExecutingDepartment().getId(), 
			(financialDetail.getFunction()==null? null:financialDetail.getFunction().getId()), 
			(financialDetail.getFunctionary()==null? null:financialDetail.getFunctionary().getId()), 
			(financialDetail.getScheme()==null? null:financialDetail.getScheme().getId()), 
			(financialDetail.getSubScheme()==null? null:financialDetail.getSubScheme().getId()), 
			(financialDetail.getAbstractEstimate().getWard()==null? null:financialDetail.getAbstractEstimate().getWard().getId()),
			(financialDetail.getBudgetGroup()==null? null:budgetheadid), 
			(financialDetail.getFund()==null? null:financialDetail.getFund().getId()), 
			financialDetail.getAbstractEstimate().getTotalAmount().getValue(),
			financialDetail.getAbstractEstimate().getBudgetApprNo()==null? null:financialDetail.getAbstractEstimate().getBudgetApprNo());
		
		return flag;
	}
	
	public boolean releaseBudgetOnReject(FinancialDetail financialDetail) 
	throws ValidationException{
		
		CFinancialYear finYear =financialDetail.getAbstractEstimate().getLeastFinancialYearForEstimate();
		
		List<Long> budgetheadid=new ArrayList<Long>();
		budgetheadid.add(financialDetail.getBudgetGroup().getId());
		
		boolean flag=false;
		budgetDetailsDAO.releaseEncumbranceBudget(
			finYear.getId(), Integer.valueOf(11), 
			financialDetail.getAbstractEstimate().getEstimateNumber(), 
			financialDetail.getAbstractEstimate().getExecutingDepartment().getId(), 
			(financialDetail.getFunction()==null? null:financialDetail.getFunction().getId()), 
			(financialDetail.getFunctionary()==null? null:financialDetail.getFunctionary().getId()), 
			(financialDetail.getScheme()==null? null:financialDetail.getScheme().getId()), 
			(financialDetail.getSubScheme()==null? null:financialDetail.getSubScheme().getId()), 
			(financialDetail.getAbstractEstimate().getWard()==null? null:financialDetail.getAbstractEstimate().getWard().getId()),
			(financialDetail.getBudgetGroup()==null? null:budgetheadid), 
			(financialDetail.getFund()==null? null:financialDetail.getFund().getId()), 
			financialDetail.getAbstractEstimate().getTotalAmount().getValue(),
			financialDetail.getAbstractEstimate().getBudgetRejectionNo()==null? null:financialDetail.getAbstractEstimate().getBudgetRejectionNo());
		
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
				if(fd.getAbstractEstimate().getExecutingDepartment()!=null)
					searchMap.put("deptid", fd.getAbstractEstimate().getExecutingDepartment().getId());
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
		 * This method will return budgetary appropriation done estimate list  for budgetHead 
		 * @param  
		 */
		 public Map<String,List>  getApprovedAppropriationDetailsForBugetHead(AbstractEstimate viewEstimate,BigDecimal totalGrantPerc){
			logger.debug("1---inside getApprovedAppropriationDetailsForBugetHead------");
			 List<BudgetFolioDetail> approvedBudgetFolioResultList=new ArrayList<BudgetFolioDetail>();
			 Map<String, Object> queryParamMap=new HashMap<String,Object>();
			 FinancialDetail fd=null;
			 Integer deptId=null;
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
				if(fd.getAbstractEstimate().getExecutingDepartment()!=null){
					deptId=fd.getAbstractEstimate().getExecutingDepartment().getId();
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
				
				budgetFolioResultList.add(budgetFolioDetail);
				
				if(budgetUsage.getReleasedAmount()>0){
					cumulativeTotal = cumulativeTotal - budgetUsage.getReleasedAmount();
				}
				else{
					cumulativeTotal = cumulativeTotal + budgetUsage.getConsumedAmount();
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
				
			if(ae!=null && ae.getExecutingDepartment()!=null && ae.getExecutingDepartment().getDeptName()!=null)
				departmentName=ae.getExecutingDepartment().getDeptName();
				
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

	public void setEmployeeService(EmployeeService employeeService) {
		this.employeeService = employeeService;
	}

	public EmployeeService getEmployeeService() {
		return employeeService;
	}
	
	public CommonsService getCommonsService() {
		return commonsService;
	}


	public void setCommonsService(CommonsService commonsService) {
		this.commonsService = commonsService;
	}
	
	
	public void setWorksService(WorksService worksService) {
		this.worksService = worksService;
	}
	
	public int getCurrentUserId() {
		return Integer.parseInt(EGOVThreadLocals.getUserId());
	}
	
	public Assignment getLatestAssignmentForCurrentLoginUser() {
		PersonalInformation personalInformation=null;
		if(getCurrentUserId()!=0) {
			personalInformation=employeeService.getEmpForUserId(getCurrentUserId());		    
		}
		Assignment assignment=null;
		if(personalInformation!=null){
			assignment=employeeService.getLatestAssignmentForEmployee(personalInformation.getIdPersonalInformation());			
		}
		return assignment; 
	}
	
	public CFinancialYear getCurrentFinancialYear(Date estimateDate) {
		String finyearRange = worksService.getWorksConfigValue("FINANCIAL_YEAR_RANGE");
		if(StringUtils.isNotBlank(finyearRange))
			return commonsService.getFinancialYearByFinYearRange(finyearRange);
		else
			return commonsService.getFinancialYearByDate(estimateDate);
	}
	
	public BigDecimal getBudgetAvailable(AbstractEstimate estimate) throws ValidationException{
		   BigDecimal budgetAvailable=BigDecimal.ZERO;
		   List<Long> budgetheadid=new ArrayList<Long>();
		
		   
		   if(estimate.getFinancialDetails()!=null && estimate.getFinancialDetails().size()>0){
			   FinancialDetail financialDetail = estimate.getFinancialDetails().get(0);
				budgetheadid.add(financialDetail.getBudgetGroup().getId());
			   return  budgetDetailsDAO.getPlanningBudgetAvailable(
						 estimate.getLeastFinancialYearForEstimate().getId(), 
						 estimate.getExecutingDepartment().getId(),
						 (financialDetail.getFunction()==null? null:financialDetail.getFunction().getId()), null,
						 (financialDetail.getScheme()==null? null:financialDetail.getScheme().getId()), 
						 (financialDetail.getSubScheme()==null? null:financialDetail.getSubScheme().getId()), 
						  estimate.getWard().getId(),
						 (financialDetail.getBudgetGroup()==null? null:budgetheadid),
						 (financialDetail.getFund()==null? null:financialDetail.getFund().getId()));
		   }else
			   return budgetAvailable;
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
	
	public Money getWorkValueExcludingTaxesForEstList(List<AbstractEstimate> abList) {
		double amt=0;
		if(!abList.isEmpty()){
			for(AbstractEstimate ab :abList)
				amt+=ab.getWorkValue().getValue();
		}
		return new Money(amt);
	}
	
	/*public Map<String, String> appConfigMandataryFieldsForBudgetAppropriation(){
		Map<String, String> mandatoryFields = new HashMap<String, String>();
		List<AppConfig> appConfigList = (List<AppConfig>)findAllBy("from AppConfig where key_name = 'DEFAULTTXNMISATTRRIBUTES'");
		for (AppConfig appConfig : appConfigList) {
			for (AppConfigValues appConfigVal : appConfig.getAppDataValues()) {
				String value = appConfigVal.getValue();
				String header=value.substring(0, value.indexOf("|"));
				String mandate = value.substring(value.indexOf("|")+1);
				if(mandate.equalsIgnoreCase("M")){
					mandatoryFields.put(header, "M");
				}
			}
		}
		return mandatoryFields;
	}*/

	public void setEgovCommon(EgovCommon egovCommon) {
		this.egovCommon = egovCommon;
	}

	public EgovCommon getEgovCommon() {
		return egovCommon;
	}
	
	public BigDecimal getDepositWorksBalance(AbstractEstimate abstractEstimate) throws ValidationException{
		BigDecimal depositAmountAvailable=BigDecimal.ZERO;
		FinancialDetail financialDetail=null;
		if(abstractEstimate.getFinancialDetails()!=null && abstractEstimate.getFinancialDetails().size()>0)
			financialDetail = abstractEstimate.getFinancialDetails().get(0);
		Accountdetailtype accountdetailtype=worksService.getAccountdetailtypeByName("DEPOSITCODE");
		BigDecimal creditBalance=egovCommon.getCreditBalanceforDate(new Date(),financialDetail.getCoa().getGlcode(),financialDetail.getFund().getCode(), accountdetailtype.getId(), financialDetail.getAbstractEstimate().getDepositCode().getId().intValue());
		BigDecimal utilizedAmt=depositWorksUsageService.getTotalUtilizedAmountForDepositWorks(financialDetail);
		if(utilizedAmt==null)
			depositAmountAvailable=creditBalance;
		else
			depositAmountAvailable=creditBalance.subtract(utilizedAmt);
		return depositAmountAvailable;		
	}
	
	public boolean checkForBudgetaryAppropriationForDepositWorks(FinancialDetail financialDetail) 
	throws ValidationException{			
		boolean flag=false;
		Accountdetailtype accountdetailtype=worksService.getAccountdetailtypeByName("DEPOSITCODE");
		double estimateAmount=financialDetail.getAbstractEstimate().getTotalAmount().getValue();
		BigDecimal creditBalance=egovCommon.getCreditBalanceforDate(new Date(),financialDetail.getCoa().getGlcode(),financialDetail.getFund().getCode(), accountdetailtype.getId(), financialDetail.getAbstractEstimate().getDepositCode().getId().intValue());
			
		BigDecimal utilizedAmt=depositWorksUsageService.getTotalUtilizedAmountForDepositWorks(financialDetail);
		BigDecimal balance=BigDecimal.ZERO;
		if(utilizedAmt==null)
			balance=creditBalance;
		else
			balance=creditBalance.subtract(utilizedAmt);
		
		if(balance.doubleValue()>=estimateAmount) {
			DepositWorksUsage depositWorksUsage=new DepositWorksUsage();
			depositWorksUsage.setTotalDepositAmount(creditBalance);
			depositWorksUsage.setConsumedAmount(new BigDecimal(estimateAmount));
			depositWorksUsage.setReleasedAmount(BigDecimal.ZERO);
			depositWorksUsage.setAppropriationNumber(financialDetail.getAbstractEstimate().getBudgetApprNo());
			depositWorksUsage.setAbstractEstimate(financialDetail.getAbstractEstimate());
			depositWorksUsage.setAppropriationDate(new Date());
			depositWorksUsageService.persist(depositWorksUsage);
			flag=true;
		}
		return flag;
	}
	
	public boolean releaseDepositWorksAmountOnReject(FinancialDetail financialDetail) 
	throws ValidationException{			
		boolean flag=false;
		Accountdetailtype accountdetailtype=worksService.getAccountdetailtypeByName("DEPOSITCODE");
		double estimateAmount=financialDetail.getAbstractEstimate().getTotalAmount().getValue();
		BigDecimal creditBalance=egovCommon.getCreditBalanceforDate(new Date(),financialDetail.getCoa().getGlcode(),financialDetail.getFund().getCode(), accountdetailtype.getId(), financialDetail.getAbstractEstimate().getDepositCode().getId().intValue());
		
		DepositWorksUsage depositWorksUsage=new DepositWorksUsage();
		depositWorksUsage.setTotalDepositAmount(creditBalance);
		depositWorksUsage.setConsumedAmount(BigDecimal.ZERO);
		depositWorksUsage.setReleasedAmount(new BigDecimal(estimateAmount));
		depositWorksUsage.setAppropriationNumber(financialDetail.getAbstractEstimate().getBudgetRejectionNo());
		depositWorksUsage.setAbstractEstimate(financialDetail.getAbstractEstimate());
		depositWorksUsage.setAppropriationDate(new Date());
		depositWorksUsageService.persist(depositWorksUsage);
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

	public void setTechSanctionNumber(AbstractEstimate entity) {
		CFinancialYear financialYear=commonsService.getFinancialYearByDate(entity.getEstimateDate());
		TechnicalSanction techSanction=new TechnicalSanction();
		techSanction.setAbstractEstimate(entity);
		techSanction.setTechSanctionDate(new Date());
		String tsNo=technicalSanctionNumberGenerator.getTechnicalSanctionNumber(entity,financialYear);
		techSanction.setTechSanctionNumber(tsNo);
		entity.setTechSanctionNumber(tsNo); 
		technicalSanctionService.persist(techSanction); 
	}
	
	public void setTechSanctionNumberForRE(AbstractEstimate entity) {
		CFinancialYear financialYear=commonsService.getFinancialYearByDate(entity.getEstimateDate());
		TechnicalSanction techSanction=new TechnicalSanction();
		techSanction.setAbstractEstimate(entity);   
		techSanction.setTechSanctionDate(new Date());
		String tsNo=technicalSanctionNumberGenerator.getTechnicalSanctionNumberForRE(entity,financialYear);
		techSanction.setTechSanctionNumber(tsNo);
		entity.setTechSanctionNumber(tsNo); 
		technicalSanctionService.persist(techSanction);
	}
	
	public BigDecimal getTotalEstimateAmountInclusiveOfRE(AbstractEstimate abstractEstimate) { 
		BigDecimal estAmountInlusiveOfRE = BigDecimal.ZERO;
		List<RevisionAbstractEstimate> reList = new LinkedList<RevisionAbstractEstimate>() ;
		reList = revisionAbstractEstimateService.findAllByNamedQuery("HAS_REVISION_ESTIMATES", abstractEstimate.getId());
		if(reList!=null && !reList.isEmpty()){
			for(RevisionAbstractEstimate re : reList){
				estAmountInlusiveOfRE=estAmountInlusiveOfRE.add(new BigDecimal(re.getTotalAmount().getValue()));
			}
		}
		estAmountInlusiveOfRE=estAmountInlusiveOfRE.add(new BigDecimal(abstractEstimate.getTotalAmount().getValue()));
		return estAmountInlusiveOfRE;
	}

	public void setTechnicalSanctionService(
			PersistenceService<TechnicalSanction, Long> technicalSanctionService) {
		this.technicalSanctionService = technicalSanctionService;
	}

	public void setTechnicalSanctionNumberGenerator(
			TechnicalSanctionNumberGenerator technicalSanctionNumberGenerator) {
		this.technicalSanctionNumberGenerator = technicalSanctionNumberGenerator;
	}
	
	public AbstractEstimate getAbstractEstimateByNumber(String estimateNumber){
		return findByNamedQuery("getAbEstimateListByNumber", estimateNumber);
	}

	
	public String getAdminsanctionNumber(AbstractEstimate abstractEstimate) {
		return adminSanctionNumberGenerator.getAdminNumber(abstractEstimate);
	}
	
	public Date getAdminsanctDate(AbstractEstimate abstractEstimate) {
		return (new Date());
	}
	
	public AdminSanctionNumberGenerator getAdminSanctionNumberGenerator() {
		return adminSanctionNumberGenerator;
	}

	public void setRevisionAbstractEstimateService(
			PersistenceService<RevisionAbstractEstimate, Long> revisionAbstractEstimateService) {
		this.revisionAbstractEstimateService = revisionAbstractEstimateService;
	}
	
}
