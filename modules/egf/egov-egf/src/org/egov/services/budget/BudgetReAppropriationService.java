package org.egov.services.budget;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.commons.CFinancialYear;
import org.egov.dao.budget.BudgetDetailsHibernateDAO;
import org.egov.infstr.ValidationError;
import org.egov.infstr.ValidationException;
import org.egov.infstr.commons.dao.GenericHibernateDaoFactory;
import org.egov.infstr.config.AppConfigValues;
import org.egov.infstr.models.Script;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.services.ScriptService;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.infstr.utils.SequenceGenerator;
import org.egov.infstr.workflow.WorkflowService;
import org.egov.model.budget.BudgetDetail;
import org.egov.model.budget.BudgetReAppropriation;
import org.egov.model.budget.BudgetReAppropriationMisc;
import org.egov.pims.commons.Position;
import org.egov.utils.BudgetDetailConfig;
import org.egov.utils.Constants;
import org.egov.web.actions.budget.BudgetReAppropriationView;


public class BudgetReAppropriationService extends PersistenceService<BudgetReAppropriation, Long>{
	WorkflowService<BudgetReAppropriation> budgetReAppropriationWorkflowService;
	WorkflowService<BudgetReAppropriationMisc> miscWorkflowService;
	BudgetDetailService budgetDetailService;
	BudgetDetailConfig budgetDetailConfig;
	PersistenceService persistenceService;
	SequenceGenerator sequenceGenerator;
	private GenericHibernateDaoFactory genericDao;
	private BudgetDetailsHibernateDAO budgetDetailsDAO;
	public ScriptService getScriptService() {
		return scriptService;
	}

	public void setScriptService(ScriptService scriptService) {
		this.scriptService = scriptService;
	}

	private  ScriptService scriptService;

	public SequenceGenerator getSequenceGenerator() {
		return sequenceGenerator;
	}
	
	public void setGenericDao(GenericHibernateDaoFactory genericDao) {
		this.genericDao = genericDao;
	}

	public void setSequenceGenerator(SequenceGenerator sequenceGenerator) {
		this.sequenceGenerator = sequenceGenerator;
	}

	public void setBudgetDetailService(BudgetDetailService budgetDetailService) {
		this.budgetDetailService = budgetDetailService;
	}

	public void setPersistenceService(PersistenceService persistenceService) {
		this.persistenceService = persistenceService;
	}

	public void setBudgetReAppropriationWorkflowService(WorkflowService<BudgetReAppropriation> budgetReAppropriationWorkflowService) {
		this.budgetReAppropriationWorkflowService = budgetReAppropriationWorkflowService;
	}
	
	public void setMiscWorkflowService(WorkflowService<BudgetReAppropriationMisc> miscWorkflowService) {
		this.miscWorkflowService = miscWorkflowService;
	}

	public BudgetReAppropriationService(BudgetDetailConfig budgetDetailConfig){
		setType(BudgetReAppropriation.class);
		this.budgetDetailConfig = budgetDetailConfig;
	}
	
	
	public boolean createReAppropriationForNewBudgetDetail(String actionName, List<BudgetReAppropriationView> newBudgetReAppropriationList,Position position, BudgetReAppropriationMisc misc) {
		BudgetDetail detail = null;
		if(newBudgetReAppropriationList.isEmpty() || (!newBudgetReAppropriationList.isEmpty() && !rowsToAddExists(newBudgetReAppropriationList)))
			return false;
		List<BudgetReAppropriationView> addedList = new ArrayList<BudgetReAppropriationView>();
		for (BudgetReAppropriationView appropriation : newBudgetReAppropriationList) {
			detail = createApprovedBudgetDetail(appropriation,position);
			if(!checkRowEmpty(appropriation)){
				validateMandatoryFields(newBudgetReAppropriationList);
				validateDuplicates(addedList,appropriation);
				saveAndStartWorkFlowForNewDetail(actionName,detail,appropriation,position,misc);
				addedList.add(appropriation);
			}
		}
		return true;
	}
	
	protected boolean checkRowEmpty(BudgetReAppropriationView appropriation) {
		if((appropriation.getBudget()==null || appropriation.getBudget().getId() == 0) && (appropriation.getBudgetDetail().getBudgetGroup()==null || appropriation.getBudgetDetail().getBudgetGroup().getId()==0) 
				&& isMandatoryGridFieldEmpty(appropriation)){
			return true;
		}
		return false;
	}
	private BudgetDetail createApprovedBudgetDetail(BudgetReAppropriationView appropriation,Position position) {
		BudgetDetail detail = new BudgetDetail();
		BudgetDetail budgetDetail = appropriation.getBudgetDetail();
		detail.copyFrom(budgetDetail);
		BudgetDetail savedBudgetDetail = budgetDetailService.createBudgetDetail(detail, position,persistenceService);
		budgetDetailService.transitionToEnd(detail, position);
		return savedBudgetDetail;
	}
	
	public void validateMandatoryFields(List<BudgetReAppropriationView> reAppropriationList) {
		for (BudgetReAppropriationView entry : reAppropriationList) {
			entry.setBudgetDetail(setRelatedValues(entry.getBudgetDetail()));
			if(entry.getBudgetDetail().getBudgetGroup() == null || entry.getBudgetDetail().getBudgetGroup().getId()==0L)
				throw new ValidationException(Arrays.asList(new ValidationError("budgetDetail.budgetGroup.mandatory","budgetDetail.budgetGroup.mandatory")));
			Map<String, Object> valueMap = constructValueMap(entry.getBudgetDetail());
			budgetDetailConfig.checkHeaderMandatoryField(valueMap);
			budgetDetailConfig.checkGridMandatoryField(valueMap);
		}
	}
	
	public BudgetDetail setRelatedValues(BudgetDetail detail) {
		if(detail.getExecutingDepartment() != null && detail.getExecutingDepartment().getId()==0)
			detail.setExecutingDepartment(null);
		if(detail.getFunction() != null && detail.getFunction().getId()==0)
			detail.setFunction(null);
		if(detail.getScheme() != null && detail.getScheme().getId()==0)
			detail.setScheme(null);
		if(detail.getSubScheme() != null && detail.getSubScheme().getId()==0)
			detail.setSubScheme(null);
		if(detail.getFunctionary() !=null && detail.getFunctionary().getId()==0)
			detail.setFunctionary(null);
		if(detail.getBoundary() != null && detail.getBoundary().getId()==0)
			detail.setBoundary(null);
		if(detail.getFund() != null && detail.getFund().getId()==0)
			detail.setFund(null);
		return detail;
	}
	private Map<String,Object> constructValueMap(BudgetDetail budgetDetail) {
		Map<String,Object> valueMap = new HashMap<String, Object>();
		valueMap.put(Constants.EXECUTING_DEPARTMENT, budgetDetail.getExecutingDepartment());
		valueMap.put(Constants.FUNCTION, budgetDetail.getFunction());
		valueMap.put(Constants.FUNCTIONARY, budgetDetail.getFunctionary());
		valueMap.put(Constants.SCHEME, budgetDetail.getScheme());
		valueMap.put(Constants.SUB_SCHEME, budgetDetail.getSubScheme());
		valueMap.put(Constants.BOUNDARY, budgetDetail.getBoundary());
		valueMap.put(Constants.FUND, budgetDetail.getFund());
		return valueMap;
	}
	
	private boolean isMandatoryGridFieldEmpty(BudgetReAppropriationView appropriation) {
		for (String entry : budgetDetailConfig.getGridFields()) {
			if(Constants.FUNCTION.equalsIgnoreCase(entry) && budgetDetailConfig.getMandatoryFields().contains(Constants.FUNCTION) && (appropriation.getBudgetDetail().getFunction() == null || appropriation.getBudgetDetail().getFunction().getId() ==0) )
				return true;
			if(Constants.EXECUTING_DEPARTMENT.equalsIgnoreCase(entry) && budgetDetailConfig.getMandatoryFields().contains(Constants.EXECUTING_DEPARTMENT) && (appropriation.getBudgetDetail().getExecutingDepartment() == null || appropriation.getBudgetDetail().getExecutingDepartment().getId()==0))
				return true;
			if(Constants.FUND.equalsIgnoreCase(entry) && budgetDetailConfig.getMandatoryFields().contains(Constants.FUND) && (appropriation.getBudgetDetail().getExecutingDepartment() == null || appropriation.getBudgetDetail().getExecutingDepartment().getId()==0))
				return true;
			if(Constants.SCHEME.equalsIgnoreCase(entry) && budgetDetailConfig.getMandatoryFields().contains(Constants.SCHEME) && (appropriation.getBudgetDetail().getScheme() == null || appropriation.getBudgetDetail().getScheme().getId()==0))
				return true;
			if(Constants.SUB_SCHEME.equalsIgnoreCase(entry) && budgetDetailConfig.getMandatoryFields().contains(Constants.SUB_SCHEME) && (appropriation.getBudgetDetail().getSubScheme() == null || appropriation.getBudgetDetail().getSubScheme().getId()==0))
				return true;
			if(Constants.BOUNDARY.equalsIgnoreCase(entry) && budgetDetailConfig.getMandatoryFields().contains(Constants.BOUNDARY) && (appropriation.getBudgetDetail().getBoundary() == null || appropriation.getBudgetDetail().getBoundary().getBndryId()==0))
				return true;
			if(Constants.FUNCTIONARY.equalsIgnoreCase(entry) && budgetDetailConfig.getMandatoryFields().contains(Constants.FUNCTIONARY) && (appropriation.getBudgetDetail().getFunctionary() == null || appropriation.getBudgetDetail().getFunctionary().getId()==0))
				return true;
			if(Constants.FUND.equalsIgnoreCase(entry) && budgetDetailConfig.getMandatoryFields().contains(Constants.FUND) && (appropriation.getBudgetDetail().getFund() == null || appropriation.getBudgetDetail().getFund().getId()==0))
				return true;
		}
		return false;
	}
	
	public boolean rowsToAddExists(List<BudgetReAppropriationView> reAppropriationList) {
		for (BudgetReAppropriationView budgetReAppropriationView : reAppropriationList) {
			if(checkRowEmpty(budgetReAppropriationView))
				return false;
			BudgetDetail budgetDetail = budgetReAppropriationView.getBudgetDetail();
			setRelatedValues(budgetDetail);
			if(budgetDetail.getBudgetGroup() != null && budgetDetail.getBudgetGroup().getId()==0)
				budgetDetail.setBudgetGroup(null);
			if(!checkRowEmpty(budgetReAppropriationView))
				return true;
		}
		return false;
	}
	
	private void saveAndStartWorkFlowForNewDetail(String actionName, BudgetDetail detail,BudgetReAppropriationView appropriation,Position position, BudgetReAppropriationMisc misc) {
		BudgetReAppropriation reAppropriation = new BudgetReAppropriation();
		reAppropriation.setBudgetDetail(detail);
		reAppropriation.setReAppropriationMisc(misc);
		reAppropriation.setAnticipatoryAmount(appropriation.getAnticipatoryAmount());
		//Since it is a new budget detail, the amount will always be addition amount
		reAppropriation.setOriginalAdditionAmount(appropriation.getDeltaAmount());
		reAppropriation = budgetReAppropriationWorkflowService.start(reAppropriation, position);
		budgetReAppropriationWorkflowService.transition(actionName, reAppropriation, "");
	}
	
	public boolean createReAppropriation(String actionName,List<BudgetReAppropriationView> budgetReAppropriationList,Position position,CFinancialYear financialYear,String beRe, BudgetReAppropriationMisc misc) {
		if(budgetReAppropriationList.isEmpty() || !rowsToAddForExistingDetails(budgetReAppropriationList))
			return false;
		validateMandatoryFields(budgetReAppropriationList);
		List<BudgetReAppropriationView> addedList = new ArrayList<BudgetReAppropriationView>();
		for (BudgetReAppropriationView appropriation : budgetReAppropriationList) {
			validateDuplicates(addedList,appropriation);
			saveAndStartWorkFlowForExistingdetails(actionName,appropriation,position,financialYear,beRe,misc);
			addedList.add(appropriation);
		}
		return true;
	}
	
	private void validateDuplicates(List<BudgetReAppropriationView> budgetReAppropriationList,BudgetReAppropriationView appropriation) {
		for (BudgetReAppropriationView budgetReAppropriationView : budgetReAppropriationList) {
			if(appropriation.getBudgetDetail().compareTo(budgetReAppropriationView.getBudgetDetail()))
				throw new ValidationException(Arrays.asList(new ValidationError("reApp.duplicate.entry","reApp.duplicate.entry")));
		}
	}

	public boolean rowsToAddForExistingDetails(List<BudgetReAppropriationView> reAppropriationList) {
		for (BudgetReAppropriationView budgetReAppropriationView : reAppropriationList) {
			BudgetDetail budgetDetail = budgetReAppropriationView.getBudgetDetail();
			setRelatedValues(budgetDetail);
			if(budgetDetail.getBudgetGroup() != null && budgetDetail.getBudgetGroup().getId()==0)
				budgetDetail.setBudgetGroup(null);
			if(!(budgetReAppropriationView.getBudgetDetail().getBudgetGroup()==null && isMandatoryGridFieldEmpty(budgetReAppropriationView)))
				return true;
		}
		return false;
	}

	protected void saveAndStartWorkFlowForExistingdetails(String actionName, BudgetReAppropriationView reAppView,Position position,CFinancialYear financialYear,String beRe, BudgetReAppropriationMisc misc) {
		BudgetReAppropriation appropriation = new BudgetReAppropriation();
		List<BudgetDetail> searchBy = budgetDetailService.searchByCriteriaWithTypeAndFY(financialYear.getId(),beRe,reAppView.getBudgetDetail());
		if(searchBy.size()!=1)
			throw new ValidationException(Arrays.asList(new ValidationError("budget.reappropriation.invalid.combination","budget.reappropriation.invalid.combination")));
		appropriation.setBudgetDetail(searchBy.get(0));
		appropriation.setReAppropriationMisc(misc);
		appropriation.setAnticipatoryAmount(reAppView.getAnticipatoryAmount());
		if("Addition".equalsIgnoreCase(reAppView.getChangeRequestType()))
			appropriation.setOriginalAdditionAmount(reAppView.getDeltaAmount());
		else
			appropriation.setOriginalDeductionAmount(reAppView.getDeltaAmount());
		validateDeductionAmount(appropriation);
		appropriation = budgetReAppropriationWorkflowService.start(appropriation, position);
		budgetReAppropriationWorkflowService.transition(actionName, appropriation, "");
	}

	/**
	 * This api checks whether the amount being deducted is greater than the budget available. If it is greater, a validation exception
	 * is thrown.
	 * @param reAppropriation - The budget reappropriation being created.(This could be the addition or the deduction reappropriation)
	 * @return
	 */
	private void validateDeductionAmount(BudgetReAppropriation appropriation) {
		BigDecimal multiplicationFactor = new BigDecimal(Double.parseDouble(getAppConfigFor("EGF","planning_budget_multiplication_factor")));
		BigDecimal deductionAmount = appropriation.getOriginalDeductionAmount();
		if(deductionAmount!=null && BigDecimal.ZERO.compareTo(deductionAmount)==-1){
			if(deductionAmount.compareTo(appropriation.getBudgetDetail().getBudgetAvailable().divide(multiplicationFactor))==1 || !canDeduct(appropriation)){
				throw new ValidationException(Arrays.asList(new ValidationError("budget.deduction.greater.than.available","budget.deduction.greater.than.available"))); 
			}
		}
	}

	//checks if the deduction amount is greater than the available amount(i.e, approved-actuals)
	 private boolean canDeduct(BudgetReAppropriation appropriation){
		if(appropriation==null || appropriation.getOriginalDeductionAmount()==null || BigDecimal.ZERO.equals(appropriation.getOriginalDeductionAmount()))
			return true;
		Map<String,Object> paramMap = new HashMap<String,Object>();
		BudgetDetail budgetDetail = appropriation.getBudgetDetail();
		if(budgetDetail.getFund()!=null && budgetDetail.getFund().getId()!=null)
			paramMap.put("fundid", budgetDetail.getFund().getId());
		if(budgetDetail.getExecutingDepartment()!=null && budgetDetail.getExecutingDepartment().getId()!=null)
			paramMap.put("deptid", budgetDetail.getExecutingDepartment().getId());
		if(budgetDetail.getFunction()!=null && budgetDetail.getFunction().getId()!=null)
			paramMap.put("functionid", budgetDetail.getFunction().getId());
		if(budgetDetail.getFunctionary()!=null && budgetDetail.getFunctionary().getId()!=null)
			paramMap.put("functionaryid",budgetDetail.getFunctionary().getId());
		if(budgetDetail.getScheme()!=null && budgetDetail.getScheme().getId()!=null)
			paramMap.put("schemeid",budgetDetail.getScheme().getId());
		if(budgetDetail.getSubScheme()!=null && budgetDetail.getSubScheme().getId()!=null)
			paramMap.put("subschemeid",budgetDetail.getSubScheme().getId());
		if(budgetDetail.getBoundary()!=null && budgetDetail.getBoundary().getId()!=null)
			paramMap.put("boundaryid", budgetDetail.getBoundary().getId());
		paramMap.put("budgetheadid", budgetDetail.getBudgetGroup().getId());
		paramMap.put("glcodeid", budgetDetail.getBudgetGroup().getMinCode().getId());
		paramMap.put(Constants.ASONDATE, new Date());
		BigDecimal actualBudgetUtilized = budgetDetailsDAO.getActualBudgetUtilized(paramMap);
		BigDecimal billAmount = budgetDetailsDAO.getBillAmountForBudgetCheck(paramMap);
		BigDecimal approvedAmount = appropriation.getBudgetDetail().getApprovedAmount();
		approvedAmount = approvedAmount==null?BigDecimal.ZERO:approvedAmount;
		if(appropriation.getOriginalDeductionAmount().compareTo(
				approvedAmount.subtract(actualBudgetUtilized==null?BigDecimal.ZERO:actualBudgetUtilized).subtract(
						billAmount==null?BigDecimal.ZERO:billAmount))>0)
			return false;
		return true;
	 }

	 public BudgetReAppropriationMisc createReAppropriationMisc(String actionName,BudgetReAppropriationMisc appropriationMisc,BudgetDetail detail,Position position){
		BudgetReAppropriationMisc misc = new BudgetReAppropriationMisc();
		misc.setReAppropriationDate(appropriationMisc.getReAppropriationDate());
		misc.setRemarks(appropriationMisc.getRemarks());
		misc.setSequenceNumber(getSequenceNumber(detail));
		misc = miscWorkflowService.start(misc, position);
		miscWorkflowService.transition(actionName, misc, misc.getRemarks());
		return misc;
	}
	
	protected String getSequenceNumber(BudgetDetail detail){
		Script sequenceNumberScript = scriptService.findAllByNamedQuery(Script.BY_NAME, "egf.budget.reappropriation.sequence.generator").get(0);
		return (String) scriptService.executeScript(sequenceNumberScript,scriptService.createContext("wfItem",detail,"sequenceGenerator",sequenceGenerator));
	}
	
	public BudgetReAppropriation findBySequenceNumberAndBudgetDetail(String sequenceNumber,Long budgetDetailId){
		return (BudgetReAppropriation) persistenceService.find("from BudgetReAppropriation b where b.reAppropriationMisc.sequenceNumber=? and b.budgetDetail.id=?", sequenceNumber,budgetDetailId);
	}

	public BudgetReAppropriationMisc performActionOnMisc(String action, BudgetReAppropriationMisc reApp,String comment) {
		BudgetReAppropriationMisc misc = miscWorkflowService.transition(action, reApp,comment);
		HibernateUtil.getCurrentSession().flush();
		return misc;
	}
	
	/**
	 * This api updates the budget available amount for which the budget reappropriation is being done. The budget available is 
	 * calculated as,
	 * budget available = budget available + (additional amount * multiplication factor)    for addition and
	 * budget available = budget available - (deduction amount * multiplication factor)    for deduction
	 * @param reAppropriation - The budget reappropriation being created.(This could be the addition or the deduction reappropriation)
	 * @return
	 */
	public void updatePlanningBudget(BudgetReAppropriation reAppropriation){
		BigDecimal multiplicationFactor = new BigDecimal(Double.parseDouble(getAppConfigFor("EGF","planning_budget_multiplication_factor")));
		BudgetDetail budgetDetail = (BudgetDetail) budgetDetailService.find("from BudgetDetail where id=?",reAppropriation.getBudgetDetail().getId());
		BigDecimal budgetAvailable = budgetDetail.getBudgetAvailable()==null?BigDecimal.ZERO:budgetDetail.getBudgetAvailable();
		budgetAvailable = budgetAvailable.add(zeroOrValue(reAppropriation.getAdditionAmount()).multiply(multiplicationFactor));
		budgetAvailable = budgetAvailable.subtract(zeroOrValue(reAppropriation.getDeductionAmount()).multiply(multiplicationFactor));
		budgetDetail.setBudgetAvailable(budgetAvailable);
		budgetDetailService.update(budgetDetail);
		HibernateUtil.getCurrentSession().flush();
	}
	
	protected BigDecimal zeroOrValue(BigDecimal value) {
		return value==null?BigDecimal.ZERO:value;
	}

	private String getAppConfigFor(String module,String key) {
		try {
			List<AppConfigValues> list = genericDao.getAppConfigValuesDAO().getConfigValuesByModuleAndKey(module,key);
			return list.get(0).getValue().toString();
		} catch (Exception e) {
			throw new ValidationException(Arrays.asList(new ValidationError(key+" not defined in appconfig",key+" not defined in appconfig")));
		}
	}

	public void transition(String actionName, BudgetReAppropriation detail,String comment) {
		budgetReAppropriationWorkflowService.transition(actionName, detail, comment);
	}
	
	public List<BudgetReAppropriation> getNonApprovedReAppByUser(Integer userId, BudgetDetail budgetDetail, CFinancialYear financialYear){
		StringBuffer query = new StringBuffer();
		query.append("from BudgetReAppropriation where state.value='NEW' and createdBy.id="+userId+" and budgetDetail.budget.financialYear.id="+financialYear.getId());
		if(budgetDetail.getExecutingDepartment()!=null && budgetDetail.getExecutingDepartment().getId()!=null && budgetDetail.getExecutingDepartment().getId()!=0)
			query.append(" and budgetDetail.executingDepartment.id="+budgetDetail.getExecutingDepartment().getId());
		if(budgetDetail.getFund()!=null && budgetDetail.getFund().getId()!=null && budgetDetail.getFund().getId()!=0)
			query.append(" and budgetDetail.fund.id="+budgetDetail.getFund().getId());
		if(budgetDetail.getFunction()!=null && budgetDetail.getFunction().getId()!=null && budgetDetail.getFunction().getId()!=0)
			query.append(" and budgetDetail.function.id="+budgetDetail.getFunction().getId());
		if(budgetDetail.getFunctionary()!=null && budgetDetail.getFunctionary().getId()!=null && budgetDetail.getFunctionary().getId()!=0)
			query.append(" and budgetDetail.functionary.id="+budgetDetail.getFunctionary().getId());
		if(budgetDetail.getScheme()!=null && budgetDetail.getScheme().getId()!=null && budgetDetail.getScheme().getId()!=0)
			query.append(" and budgetDetail.scheme.id="+budgetDetail.getScheme().getId());
		if(budgetDetail.getSubScheme()!=null && budgetDetail.getSubScheme().getId()!=null && budgetDetail.getSubScheme().getId()!=0)
			query.append(" and budgetDetail.subScheme.id="+budgetDetail.getSubScheme().getId());
		if(budgetDetail.getBoundary()!=null && budgetDetail.getBoundary().getId()!=null && budgetDetail.getBoundary().getId()!=0)
			query.append(" and budgetDetail.boundary.id="+budgetDetail.getBoundary().getId());
		query = query.append(" order by budgetDetail.budgetGroup ");
		return findAllBy(query.toString());
	}

	public void setBudgetDetailsDAO(BudgetDetailsHibernateDAO budgetDetailsDAO) {
		this.budgetDetailsDAO = budgetDetailsDAO;
	}

}

