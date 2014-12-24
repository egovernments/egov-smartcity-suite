package org.egov.web.actions.budget;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.commons.CFinancialYear;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infstr.ValidationError;
import org.egov.infstr.ValidationException;
import org.egov.infstr.client.filter.EGOVThreadLocals;
import org.egov.infstr.models.Script;
import org.egov.infstr.services.ScriptService;
import org.egov.infstr.utils.EgovMasterDataCaching;
import org.egov.infstr.workflow.Action;
import org.egov.infstr.workflow.SimpleWorkflowService;
import org.egov.infstr.workflow.WorkflowService;
import org.egov.lib.rjbac.dept.DepartmentImpl;
import org.egov.model.budget.Budget;
import org.egov.model.budget.BudgetDetail;
import org.egov.pims.commons.DesignationMaster;
import org.egov.services.voucher.VoucherService;
import org.egov.utils.BudgetDetailConfig;
import org.egov.utils.Constants;

public class BudgetSearchAndModify extends BudgetSearchAction {
	private static final String ACTIONNAME="actionName";
	boolean enableApprovedAmount = false;
	boolean enableOriginalAmount = false;
	private String comments = "";
	private  ScriptService scriptService;
	
	public ScriptService getScriptService() {
		return scriptService;
	}

	public void setScriptService(ScriptService scriptService) {
		this.scriptService = scriptService;
	}

	public BudgetSearchAndModify(BudgetDetailConfig budgetDetailConfig) {
		super(budgetDetailConfig);
	}

	private static final long serialVersionUID = 1L;
	private static final Logger	LOGGER	= Logger.getLogger(BudgetSearchAndModify.class);
	protected WorkflowService<Budget> budgetWorkflowService;
	
	public String modifyList(){
		if(getMode()!=null && Constants.APPROVE.equals(getMode()) )
			budgetDetailApproveList();
		else
			budgetDetailList();
		setEnablingAmounts();
		loadApproverUser(savedbudgetDetailList);
		return Constants.DETAILLIST;
	}
	
	@Override
	public void prepare() {
		super.prepare();
		if (parameters.containsKey("action") && Constants.MODIFY.equals(parameters.get("action")[0])) {
			for (int i = 0; i < savedbudgetDetailList.size(); i++) {
				savedbudgetDetailList.set(i, budgetDetailService.findById(savedbudgetDetailList.get(i).getId(), false));
			} 
		}
		if(isApproveMode())
			dropdownData.put("budgetList", budgetDetailService.findBudgetsForFY(getFinancialYear()));
		else
			dropdownData.put("budgetList", budgetDetailService.findBudgetsForFYWithNewState(getFinancialYear()));
		EgovMasterDataCaching masterCache = EgovMasterDataCaching.getInstance();
		addDropdownData("departmentList", masterCache.get("egi-department"));
		addDropdownData("designationList", Collections.EMPTY_LIST);
		addDropdownData("userList", Collections.EMPTY_LIST);
	}
	
	public String update(){
		Budget budget=null;
		Budget b=null;
		if(parameters.get("budget.id")!=null){
			budget=budgetService.find(" from Budget where id=?",Long.valueOf(parameters.get("budget.id")[0]));
			setTopBudget(budget);
		}
		if("forward".equalsIgnoreCase(parameters.get("actionName")[0]) || parameters.get("actionName")[0].contains("approv")){  //if mode is approve move the object
			approve();
		}else{           //if not approve then only update 
			for (BudgetDetail detail : savedbudgetDetailList) {
				validateAmount(detail);
				budgetDetailService.persist(detail);
				b = detail.getBudget();
			}
			if(b!=null && b.getId()!=null){
				b = budgetService.find("from Budget where id=?",b.getId());
				if(b.getCurrentState()!=null)
					b.getCurrentState().setText1(comments);
				budgetService.persist(b);
			}
			addActionMessage(getMessage("budgetdetail.updated"));
		}
		setBudgetDetail((BudgetDetail) session().get(Constants.SEARCH_CRITERIA_KEY));
		return setUpDataForList();
	}

	private void validateAmount(BudgetDetail detail) {
		setEnablingAmounts();
		if(enableApprovedAmount && (detail.getApprovedAmount()==null || BigDecimal.ZERO.equals(detail.getApprovedAmount()))){
			loadApproverUser(savedbudgetDetailList);
			throw new ValidationException(Arrays.asList(new ValidationError("approved.amount.mandatory","approved.amount.mandatory")));
		}
		if(enableOriginalAmount && (detail.getOriginalAmount()==null || BigDecimal.ZERO.equals(detail.getOriginalAmount()))){
			loadApproverUser(savedbudgetDetailList);
			throw new ValidationException(Arrays.asList(new ValidationError("original.amount.mandatory","original.amount.mandatory")));
		}

	}
	
	public String setUpDataForList(){
		if(financialYear == null && getSession().get(Constants.FINANCIALYEARID)!= null)
			financialYear = (Long) getSession().get(Constants.FINANCIALYEARID);
		dropdownData.put("budgetList", budgetDetailService.findBudgetsForFYWithNewState(financialYear==null?getFinancialYear():financialYear));
		return Constants.LIST;
	}
	
	private boolean isApproveMode() {
		return parameters.containsKey(Constants.MODE) && Constants.APPROVE.equals(parameters.get(Constants.MODE)[0]);
	}
	/**
	 * move the budget detail and its parents depending on save or approve 
	 * @return
	 */
	public void approve(){
		Integer userId = null;
		if( parameters.get(ACTIONNAME)[0] != null && parameters.get(ACTIONNAME)[0].contains("reject")){
			userId = Integer.valueOf(parameters.get("approverUserId")[0]);
		}
		else if (null != parameters.get("approverUserId") &&  Integer.valueOf(parameters.get("approverUserId")[0])!=-1 ) {
			userId = Integer.valueOf(parameters.get("approverUserId")[0]);
		}else {
			userId = Integer.valueOf(EGOVThreadLocals.getUserId().trim());
		}
		
		for (BudgetDetail detail : savedbudgetDetailList) {
			validateAmount(detail);
			String comment = detail.getState()==null?"":detail.getState().getText1();
			budgetDetailWorkflowService.transition(parameters.get(ACTIONNAME)[0]+"|"+userId, detail, comment);
		}
		budgetWorkflowService.transition(parameters.get(ACTIONNAME)[0]+"|"+userId, getTopBudget(),comments);
		
		if((parameters.get("actionName")[0]).contains("approv")){
			if(topBudget.getState().getValue().equals("END")){
				addActionMessage(getMessage("budgetdetail.approved.end"));
			}else{
				addActionMessage(getMessage("budgetdetail.approved")+budgetService.getEmployeeNameAndDesignationForPosition(topBudget.getState().getOwner()));
			}
		}else{
			addActionMessage(getMessage("budgetdetail.approved")+budgetService.getEmployeeNameAndDesignationForPosition(topBudget.getState().getOwner()));
		}  
		showButton=false;
	}   
	
	public String ajaxDeleteBudgetDetail(){
		Long id = Long.valueOf(parameters.get("id")[0]);
		BudgetDetail detail = budgetDetailService.findById(id, false);
		budgetDetailService.delete(detail);
		BudgetDetail criteria = (BudgetDetail) getSession().get(Constants.SEARCH_CRITERIA_KEY);
		savedbudgetDetailList = budgetDetailService.searchBy(criteria);
		return Constants.MODIFYLIST;
	}
	public String budgetDetailApproveList(){
		isApproveAction=true;
		LOGGER.debug("Budget.id "+parameters.get("budget.id")[0]);
		if(parameters.get("budget.id")[0]!=null)
		{		
			topBudget=budgetService.findById(Long.valueOf(parameters.get("budget.id")[0]), false);
			setTopBudget(topBudget);
			comments = topBudget.getState().getText1();
		}
		BudgetDetail criteria = (BudgetDetail) getSession().get(Constants.SEARCH_CRITERIA_KEY);
		criteria.setBudget(null);
		savedbudgetDetailList = budgetDetailService.findAllBudgetDetailsWithReAppropriation(topBudget, criteria);
		re = checkRe(topBudget);
		if("BE".equalsIgnoreCase(topBudget.getIsbere())){
			CFinancialYear previousYearFor = budgetDetailHelper.getPreviousYearFor(topBudget.getFinancialYear());
			populateActualData(previousYearFor);
		}
		else
			populateActualData(topBudget.getFinancialYear());
		computeActualAmounts(savedbudgetDetailList);
		return	Constants.DETAILLIST;
	}
	
	private void computeActualAmounts(List<BudgetDetail> budgetDetails) {
		budgetAmountView = new ArrayList<BudgetAmountView>();
		for (BudgetDetail detail : budgetDetails) {
			BudgetAmountView view = new BudgetAmountView();
			view.setId(detail.getId());
			String previousYearAmount = getPreviousYearBudgetDetailIdsAndAmount().get(detail.getId().toString());
			view.setPreviousYearActuals(previousYearAmount==null?BigDecimal.ZERO.setScale(2):new BigDecimal(previousYearAmount).setScale(2));
			String currentYearAmount = getBudgetDetailIdsAndAmount().get(detail.getId().toString());
			view.setCurrentYearBeActuals(currentYearAmount==null?BigDecimal.ZERO.setScale(2):new BigDecimal(currentYearAmount).setScale(2));
			budgetAmountView.add(view);
			if(detail.getState()!=null)
				detail.setComment(detail.getState().getText1());
			BigDecimal approvedAmt = detail.getApprovedAmount()==null?BigDecimal.ZERO:detail.getApprovedAmount().setScale(2);
			if(re) {
				view.setCurrentYearReApproved(approvedAmt.setScale(2).toString());
			} else{
				view.setCurrentYearBeApproved(approvedAmt.setScale(2).toString());
			}
			detail.setAnticipatoryAmount(detail.getAnticipatoryAmount()==null?BigDecimal.ZERO:detail.getAnticipatoryAmount().setScale(2));
			detail.getOriginalAmount().setScale(2);
		}
	}

	void populateActualData(CFinancialYear financialYear){
		if(financialYear==null) return;
		String fromDate = Constants.DDMMYYYYFORMAT1.format(financialYear.getStartingDate());
		List<Object[]> result = budgetDetailService.fetchActualsForFY(fromDate,"'"+Constants.DDMMYYYYFORMAT2.format(financialYear.getEndingDate())+"'",mandatoryFields);
		for (Object[] row : result) {
			getBudgetDetailIdsAndAmount().put(row[0].toString(), row[1].toString());
		}
		fromDate = Constants.DDMMYYYYFORMAT1.format(subtractYear(financialYear.getStartingDate()));
		List<Object[]> previousYearResult = budgetDetailService.fetchActualsForFY(fromDate,"'"+Constants.DDMMYYYYFORMAT2.format(subtractYear(financialYear.getEndingDate()))+"'",mandatoryFields);
		for (Object[] row : previousYearResult) {
			getPreviousYearBudgetDetailIdsAndAmount().put(row[0].toString(), row[1].toString());
		}
	}
	public boolean enableApprovedAmount(){
		return enableApprovedAmount;
	}
	public boolean enableOriginalAmount(){
		return enableOriginalAmount;
	}
	public void setBudgetDetailWorkflowService(
			SimpleWorkflowService<BudgetDetail> workflowService) {
		this.budgetDetailWorkflowService = workflowService;
	}
	
	public List<Action> getValidActions(){
		List<Action> validButtons=null;
        if(isReferenceBudget(getTopBudget())){
        	LOGGER.debug("Budget is Reference budget hence cannot be saved to sent for approval");
        }else{
        	validButtons=budgetWorkflowService.getValidActions(getTopBudget());
        }
		return validButtons;
		 
	}
	
	private void setEnablingAmounts(){
		Script script = (Script) scriptService.findAllByNamedQuery(Script.BY_NAME, "BudgetDetail.enable.amounts").get(0);
		String value = (String) scriptService.executeScript(script,scriptService.createContext("wfItem",topBudget,"persistenceService",budgetService));
		if("approved".equalsIgnoreCase(value))
			enableApprovedAmount = true;
		else if("original".equalsIgnoreCase(value))
			enableOriginalAmount = true;
	}
	/** 
	 * reference Budget is one which  exists in  the system but wont be having active and primary budget as parent
	 * it is used for reference . These should be filtered for approval life cycle    
	 * @param budget
	 */

	public boolean isReferenceBudget(Budget budget) {
		boolean  isReference=false;	
		if(budget==null)
		{
			isReference=false;
			return isReference;
		}
		String mPath=budget.getMaterializedPath();
		LOGGER.debug("meterialized path for the Budget"+mPath);
		if(mPath==null || mPath.isEmpty())  //if null system is not expecting anything without materialized path
		{
			throw new EGOVRuntimeException("Materialized path is not set for the Budget "+budget.getName());
		}
		else if(budget.getIsPrimaryBudget() && budget.getIsActiveBudget()) //check for root budget if yes return
		{
			isReference=false; 
			return isReference;
		}
		else{ //it should be some child
		int start=mPath.indexOf('.');
		if(start!=-1)
		{
			String rootPath = mPath.substring(0, start);
			LOGGER.debug("meterialized path for root the Budget"+"   "+rootPath);
			Budget rootBudget = budgetService.find("from Budget where materializedPath=?",rootPath);
			if(rootBudget==null)
			{
				throw new EGOVRuntimeException("Materialized path is incorrect please verify for "+rootPath);
			}
			else
				if(rootBudget.getIsPrimaryBudget() && rootBudget.getIsActiveBudget())
				{
					isReference=false;
				}
				else
				{
					isReference=true;
				}
		}
		else
		{
			isReference=true;	//it is not root it is not child of any budget so it is reference
		}
		}
		return isReference;
	}

	public void setBudgetWorkflowService(WorkflowService<Budget> budgetWorkflowService) {
		this.budgetWorkflowService = budgetWorkflowService;
	}
	
	/*
	 * validates the comments for length 1024 
	 * 
	 */
	public void validate()  
	{
		for (BudgetDetail detail : savedbudgetDetailList) {
			if(detail.getComment()!=null && !(detail.getComment().trim()).isEmpty() && detail.getComment().length()>1024)
			{
				addFieldError("Comments Max Length  Exceeded BudgetDetail ",getText("budgetdetail.comments.lengthcheck"));}
		}
		if(parameters.get("budget.comments")!=null && parameters.get("budget.comments")[0]!=null && !parameters.get("budget.comments")[0].trim().isEmpty() &&  parameters.get("budget.comments")[0].length()>1024 )
		{
			addFieldError("Comments Max Length  Exceeded for  Budget",getText("budget.comments.lengthcheck"));}
	}
	
	@SkipValidation
	private void loadApproverUser(List<BudgetDetail> budgetDetailList)
	{
		EgovMasterDataCaching masterCache = EgovMasterDataCaching.getInstance();
		Map<String, Object>  map = voucherService.getDesgBYPassingWfItem("BudgetDetail.nextDesg",null,budgetDetailList.get(0).getExecutingDepartment().getId()); 
		addDropdownData("departmentList", masterCache.get("egi-department"));
		
		List<Map<String,Object>> desgList  =  (List<Map<String,Object>>) map.get("designationList");
		String  strDesgId = "", dName = "";
		boolean bDefaultDeptId = false;
		List< Map<String , Object>> designationList = new ArrayList<Map<String,Object>>();
		Map<String, Object> desgFuncryMap;
		for(Map<String,Object> desgIdAndName : desgList) {
			desgFuncryMap = new HashMap<String, Object>();
			
			if(desgIdAndName.get("designationName") != null ) {
				desgFuncryMap.put("designationName",(String) desgIdAndName.get("designationName"));
			}
			
			if(desgIdAndName.get("designationId") != null ) {
				strDesgId = (String) desgIdAndName.get("designationId");
				if(strDesgId.indexOf("~") != -1) {
					strDesgId = strDesgId.substring(0, strDesgId.indexOf('~'));
					dName = (String) desgIdAndName.get("designationId");
					dName = dName.substring(dName.indexOf('~')+1);
					bDefaultDeptId = true;
				}
				desgFuncryMap.put("designationId",strDesgId);
			}
			designationList.add(desgFuncryMap);
		}
		map.put("designationList", designationList);
		
		addDropdownData("designationList", (List<DesignationMaster>)map.get("designationList")); 
		if(bDefaultDeptId && !dName.equals("")) {
			DepartmentImpl dept = (DepartmentImpl) persistenceService.find("from DepartmentImpl where deptName like '%"+dName+"' ");
			defaultDept = dept.getId();
		}
		wfitemstate = map.get("wfitemstate")!=null?map.get("wfitemstate").toString():""; 
	}
	
	private String wfitemstate;
	private VoucherService voucherService;
	private Integer defaultDept;
	public Integer getDefaultDept() {
		return defaultDept;
	}

	public void setDefaultDept(Integer defaultDept) {
		this.defaultDept = defaultDept;
	}

	public void setVoucherService(VoucherService voucherService) {
		this.voucherService = voucherService;
	}

	public String getWfitemstate() {
		return wfitemstate;
	}

	public void setWfitemstate(String wfitemstate) {
		this.wfitemstate = wfitemstate;
	}
	
	public String capitalize(String value){
		if (value == null || value.length() == 0) return value;
		return value.substring(0, 1).toUpperCase() + value.substring(1).toLowerCase();
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getComments() {
		return comments;
	}

}
