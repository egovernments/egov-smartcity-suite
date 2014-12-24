package org.egov.web.actions.budget;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.struts2.convention.annotation.ParentPackage;
import org.egov.commons.CFinancialYear;
import org.egov.infstr.ValidationError;
import org.egov.infstr.ValidationException;
import org.egov.infstr.client.filter.EGOVThreadLocals;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.model.budget.Budget;
import org.egov.model.budget.BudgetDetail;
import org.egov.utils.BudgetDetailConfig;
import org.egov.utils.Constants;
@ParentPackage("egov")
public class BudgetDetailAction extends BaseBudgetDetailAction{
	private static final long serialVersionUID = 1L;
	private static final String ACTIONNAME="actionName";
	private Budget topBudget;
	private Map<Long,BigDecimal> beNextYearAmounts = new HashMap<Long,BigDecimal>();
	
	public BudgetDetailAction(BudgetDetailConfig budgetDetailConfig) {
		super(budgetDetailConfig);
	}
	
	protected void saveAndStartWorkFlow(BudgetDetail detail) {
		try {
			if(budgetDocumentNumber!=null && budgetDetail.getBudget()!=null){
				Budget b = budgetService.findById(budgetDetail.getBudget().getId(), false);
				b.setDocumentNumber(budgetDocumentNumber);
				budgetService.persist(b);
				HibernateUtil.getCurrentSession().flush();
			}
			BudgetDetail persist = budgetDetailService.createBudgetDetail(detail, getPosition(), getPersistenceService());
			populateSavedbudgetDetailListFor(persist.getBudget());
			headerDisabled = true;
		} catch (ValidationException e) {
			handleDuplicateBudgetDetailError(e);
			populateSavedbudgetDetailListFor(budgetDetail.getBudget());
		}
	}
	
	protected void handleDuplicateBudgetDetailError(ValidationException e) {
		for (ValidationError error : e.getErrors()) {
			if("budgetDetail.duplicate".equals(error.getKey())){
				headerDisabled = true;
				break;
			}
		}
		throw e;
	}
	
	public void populateSavedbudgetDetailListFor(Budget budget){
		if(budget != null)
			savedbudgetDetailList = budgetDetailService.findAllBy("from BudgetDetail where budget=?", budget);
	}

	public String ajaxLoadBudgetDetailList() {
		Long id = (Long)request.get("id");
		if(!Long.valueOf(0).equals(id)){
			savedbudgetDetailList = budgetDetailService.findAllBy("from BudgetDetail where budget.id=?", id);
			Budget budget = budgetService.findById(id, false);
			re = budgetService.hasReForYear(budget.getFinancialYear().getId());
			budgetDetail.setBudget(budget);
			setReferenceBudget(budgetService.getReferenceBudgetFor(budget));
			budgetDocumentNumber = budget.getDocumentNumber();
		}
		populateBeNextYearAmounts();
		populateFinancialYear();
		return Constants.SAVED_DATA;
	}
	
	public String ajaxLoadBudgets() {
		String bere = (String)parameters.get("bere")[0];
		loadBudgets(bere);
		return "budgets";
	}

	public String saveAndNew() {
		return create();
	}
	public String saveAndNewRe() {
		return createRe();
	}

	@Override
	public void prepare() {
		super.prepare();
		populateSavedbudgetDetailListFor(budgetDetail.getBudget());
		if(parameters.containsKey("re")){
			dropdownData.put("budgetList",Collections.EMPTY_LIST);
		}
		
	}
	
	@Override
	public boolean isShowMessage() {
		return super.isShowMessage();
	}
	
	public String getActionMessage(){
		if(getActionMessages() !=null && getActionMessages().iterator()!=null && getActionMessages().iterator().next() !=null)
			return getActionMessages().iterator().next().toString();
		else
			return "";
	}
	
	private void populateBeNextYearAmounts(){
		if(savedbudgetDetailList==null || savedbudgetDetailList.size()==0) return;
		Budget referenceBudgetFor = budgetService.getReferenceBudgetFor(savedbudgetDetailList.get(0).getBudget());
		if(referenceBudgetFor!=null){
			List<BudgetDetail> result = budgetDetailService.findAllBy("from BudgetDetail where budget.id=?",referenceBudgetFor.getId());
			for (BudgetDetail budgetDetail : savedbudgetDetailList) {
				for (BudgetDetail row : result) {
					if(compareDetails(row, budgetDetail)){
						beNextYearAmounts.put(budgetDetail.getId(), row.getOriginalAmount().setScale(2));
					}
				}
			}
		}
	}
	
	private boolean compareDetails(BudgetDetail nextYear,BudgetDetail current) {
		if(nextYear.getExecutingDepartment()!=null && current.getExecutingDepartment()!=null && current.getExecutingDepartment().getId()!=nextYear.getExecutingDepartment().getId())
			return false;
		if(nextYear.getFunction()!=null && current.getFunction()!=null && current.getFunction().getId()!=nextYear.getFunction().getId())
			return false;
		if(nextYear.getFund()!=null && current.getFund()!=null && current.getFund().getId()!=nextYear.getFund().getId())
			return false;
		if(nextYear.getFunctionary()!=null && current.getFunctionary()!=null && current.getFunctionary().getId()!=nextYear.getFunctionary().getId())
			return false;
		if(nextYear.getScheme()!=null && current.getScheme()!=null && current.getScheme().getId()!=nextYear.getScheme().getId())
			return false;
		if(nextYear.getSubScheme()!=null && current.getSubScheme()!=null && current.getSubScheme().getId()!=nextYear.getSubScheme().getId())
			return false;
		if(nextYear.getBoundary()!=null && current.getBoundary()!=null && current.getBoundary().getId()!=nextYear.getBoundary().getId())
			return false;
		if(nextYear.getBudgetGroup()!=null && current.getBudgetGroup()!=null && current.getBudgetGroup().getId()!=nextYear.getBudgetGroup().getId())
			return false;
		if(nextYear.getBudget()!=null && current.getBudget()!=null && current.getBudget().getId()==nextYear.getBudget().getId())
			return false;
		return true;
	}

	
	protected void saveAndStartWorkFlowForRe(BudgetDetail detail,int index,CFinancialYear finYear) {
		try {
			if(budgetDocumentNumber!=null && budgetDetail.getBudget()!=null){
				Budget b = budgetService.findById(budgetDetail.getBudget().getId(), false);
				b.setDocumentNumber(budgetDocumentNumber);
				budgetService.persist(b);
				HibernateUtil.getCurrentSession().flush();
			}
			detail.getBudget().setFinancialYear((CFinancialYear) persistenceService.find("from CFinancialYear where id=?",finYear.getId()));
			BudgetDetail reCurrentYear = budgetDetailService.createBudgetDetail(detail, getPosition(), getPersistenceService());
			populateSavedbudgetDetailListFor(reCurrentYear.getBudget());
			headerDisabled = true;
			BudgetDetail beNextYear = new BudgetDetail();
			beNextYear.copyFrom(detail);
			beNextYear.setBudget(null);
			beNextYear.setOriginalAmount(beAmounts.get(index));
			beNextYear.setDocumentNumber(detail.getDocumentNumber());
			beNextYear.setAnticipatoryAmount(reCurrentYear.getAnticipatoryAmount());
			Budget refBudget = budgetService.getReferenceBudgetFor(reCurrentYear.getBudget());
			if(refBudget==null)
				throw new ValidationException(Arrays.asList(new ValidationError("no.reference.budget","no.reference.budget")));
			beNextYear.setBudget(refBudget);
			beNextYear = budgetDetailService.createBudgetDetail(beNextYear, getPosition(), getPersistenceService());
		} catch (ValidationException e) {
			populateBeNextYearAmounts();
			handleDuplicateBudgetDetailError(e);
			populateSavedbudgetDetailListFor(budgetDetail.getBudget());
			throw e;
		}
	}
	
	public void approve(){
		String budgetComment="";
		topBudget=savedbudgetDetailList.get(0).getBudget();
		setTopBudget(topBudget);
		if(parameters.get("budget.comments")!=null)
			budgetComment=parameters.get("budget.comments")[0];
		Integer userId = null;
		if( parameters.get(ACTIONNAME)[0] != null && parameters.get(ACTIONNAME)[0].contains("reject"))
			userId = Integer.valueOf(parameters.get("approverUserId")[0]);
		else if (null != parameters.get("approverUserId") &&  Integer.valueOf(parameters.get("approverUserId")[0])!=-1 ) 
			userId = Integer.valueOf(parameters.get("approverUserId")[0]);
		else 
			userId = Integer.valueOf(EGOVThreadLocals.getUserId().trim());
		
		for (BudgetDetail detail : savedbudgetDetailList) {
			budgetDetailWorkflowService.transition(parameters.get(ACTIONNAME)[0]+"|"+userId, detail, detail.getComment());
		}
		forwardBudget(budgetComment, userId); //for RE
		setTopBudget(budgetService.getReferenceBudgetFor(topBudget));
		forwardBudget(budgetComment, userId); //for BE
		
		if((parameters.get("actionName")[0]).contains("approv")){
			if(topBudget.getState().getValue().equals("END"))
				addActionMessage(getMessage("budgetdetail.approved.end"));
			else
				addActionMessage(getMessage("budgetdetail.approved")+budgetService.getEmployeeNameAndDesignationForPosition(topBudget.getState().getOwner()));
		}
		else
			addActionMessage(getMessage("budgetdetail.approved")+budgetService.getEmployeeNameAndDesignationForPosition(topBudget.getState().getOwner()));
	}

	private void forwardBudget(String budgetComment, Integer userId) {
		budgetWorkflowService.transition(parameters.get(ACTIONNAME)[0]+"|"+userId, getTopBudget(),budgetComment);
	}   
	public String newRe() {
		showRe = true;
		return "new-re";
	}

	public void setShowRe(boolean showRe) {
		this.showRe = showRe;
	}

	public boolean isShowRe() {
		return showRe;
	}
	protected String getMessage(String key) {
		return getText(key);
	}
	/**
	 * @return the topBudget
	 */
	public Budget getTopBudget()
	{
		return topBudget;
	}
	/**
	 * @param topBudget the topBudget to set
	 */
	public void setTopBudget(Budget topBudget)
	{
		this.topBudget = topBudget;
	}

	public void setBeNextYearAmounts(Map<Long,BigDecimal> beNextYearAmounts) {
		this.beNextYearAmounts = beNextYearAmounts;
	}

	public Map<Long,BigDecimal> getBeNextYearAmounts() {
		return beNextYearAmounts;
	}

}
