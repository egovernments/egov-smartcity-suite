package org.egov.web.actions.budget;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.struts2.convention.annotation.ParentPackage;
import org.egov.commons.CFinancialYear;
import org.egov.commons.CFunction;
import org.egov.commons.Functionary;
import org.egov.commons.Fund;
import org.egov.commons.Scheme;
import org.egov.commons.SubScheme;
import org.egov.infstr.client.filter.EGOVThreadLocals;
import org.egov.infstr.commons.dao.GenericHibernateDaoFactory;
import org.egov.infstr.models.Script;
import org.egov.infstr.services.ScriptService;
import org.egov.infstr.utils.EgovMasterDataCaching;
import org.egov.infstr.workflow.Action;
import org.egov.infstr.workflow.WorkflowService;
import org.egov.lib.admbndry.BoundaryImpl;
import org.egov.lib.rjbac.dept.DepartmentImpl;
import org.egov.model.budget.Budget;
import org.egov.model.budget.BudgetDetail;
import org.egov.model.budget.BudgetGroup;
import org.egov.model.budget.BudgetReAppropriation;
import org.egov.model.budget.BudgetReAppropriationMisc;
import org.egov.pims.commons.Position;
import org.egov.pims.commons.service.EisCommonsService;
import org.egov.services.budget.BudgetDetailService;
import org.egov.services.budget.BudgetReAppropriationService;
import org.egov.services.budget.BudgetService;
import org.egov.utils.BudgetDetailConfig;
import org.egov.utils.BudgetDetailHelper;
import org.egov.utils.Constants;
import org.egov.web.actions.BaseFormAction;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.util.ValueStack;

@ParentPackage("egov")
public class BudgetReAppropriationModifyAction extends BaseFormAction{
	private static final long serialVersionUID = 1L;
	protected BudgetDetailConfig budgetDetailConfig;
	BudgetDetail budgetDetail;
	protected Budget budget;
	protected List<String> headerFields = new ArrayList<String>();
	protected List<String> gridFields = new ArrayList<String>();
	protected List<String> mandatoryFields = new ArrayList<String>();
	BudgetDetailHelper budgetDetailHelper;
	BudgetDetailService budgetDetailService;
	BudgetReAppropriationService budgetReAppropriationService;
	CFinancialYear financialYear;
	BudgetService budgetService;
	String isBeRe = Constants.BE;
	List<BudgetReAppropriationView> savedBudgetReAppropriationList = new ArrayList<BudgetReAppropriationView>();
	GenericHibernateDaoFactory genericDao;
	String message = "";
	boolean deleted = false;
	BudgetReAppropriation budgetReAppropriation;
	EisCommonsService eisCommonsService;
	Long miscId;
	WorkflowService<BudgetReAppropriationMisc> miscWorkflowService;
	private List<Action> validActions = new ArrayList<Action>();
	private String comment = "";
	private BudgetReAppropriationMisc workFlowItem;
	private String actionName = "";
	private boolean enableApprovedAmount = false;
	private boolean enableOriginalAmount = false;
	private  ScriptService scriptService;

	
	public void setMiscWorkflowService(WorkflowService<BudgetReAppropriationMisc> miscWorkflowService) {
		this.miscWorkflowService = miscWorkflowService;
	}

	public Long getMiscId() {
		return miscId;
	}

	public void setMiscId(Long miscId) {
		this.miscId = miscId;
	}


	public BudgetReAppropriation getBudgetReAppropriation() {
		return budgetReAppropriation;
	}

	public void setBudgetReAppropriation(BudgetReAppropriation budgetReAppropriation) {
		this.budgetReAppropriation = budgetReAppropriation;
	}

	public String getMessage() {
		return message;
	}

	public void setBudgetDetailService(BudgetDetailService budgetDetailService) {
		this.budgetDetailService = budgetDetailService;
	}

	public List<BudgetReAppropriationView> getSavedBudgetReAppropriationList() {
		return savedBudgetReAppropriationList;
	}
	
	public void setGenericDao(final GenericHibernateDaoFactory genericDao) {
		this.genericDao = genericDao;
	}
	
	public String getIsBeRe() {
		return isBeRe;
	}
	public void setBudgetReAppropriationService(BudgetReAppropriationService budgetReAppropriationService) {
		this.budgetReAppropriationService = budgetReAppropriationService;
	}
	public void setIsBeRe(String beRe) {
		this.isBeRe = beRe;
	}

	public void setBudgetService(BudgetService budgetService) {
		this.budgetService = budgetService;
	}
	
	public void setBudgetDetailHelper(BudgetDetailHelper budgetDetailHelper) {
		this.budgetDetailHelper = budgetDetailHelper;
	}

	public void setFinancialYear(CFinancialYear financialYear) {
		this.financialYear = financialYear;
	}

	public BudgetDetail getBudgetDetail() {
		return budgetDetail;
	}

	public void setBudgetDetail(BudgetDetail budgetDetail) {
		this.budgetDetail = budgetDetail;
	}

	
	public CFinancialYear getFinancialYear() {
		return financialYear;
	}

	public Budget getBudget() {
		return budget;
	}

	public List<String> getHeaderFields() {
		return headerFields;
	}

	public List<String> getGridFields() {
		return gridFields;
	}

	public List<String> getMandatoryFields() {
		return mandatoryFields;
	}
	public BudgetReAppropriationModifyAction(BudgetDetailConfig budgetDetailConfig){
		this.budgetDetailConfig = budgetDetailConfig;
		headerFields = budgetDetailConfig.getHeaderFields();
		gridFields = budgetDetailConfig.getGridFields();
		mandatoryFields = budgetDetailConfig.getMandatoryFields();
		addRelatedEntity("budgetGroup", BudgetGroup.class);
		if(shouldShowField(Constants.FUNCTIONARY))
			addRelatedEntity(Constants.FUNCTIONARY, Functionary.class);
		if(shouldShowField(Constants.FUNCTION))
			addRelatedEntity(Constants.FUNCTION, CFunction.class);
		if(shouldShowField(Constants.SCHEME))
			addRelatedEntity(Constants.SCHEME, Scheme.class);
		if(shouldShowField(Constants.SUB_SCHEME))
			addRelatedEntity(Constants.SUB_SCHEME, SubScheme.class);
		if(shouldShowField(Constants.FUND))
			addRelatedEntity(Constants.FUND, Fund.class);
		if(shouldShowField(Constants.EXECUTING_DEPARTMENT))
			addRelatedEntity(Constants.EXECUTING_DEPARTMENT, DepartmentImpl.class);
		if(shouldShowField(Constants.BOUNDARY))
			addRelatedEntity(Constants.BOUNDARY, BoundaryImpl.class);
	}
	
	protected void setupDropdownsInHeader() {
		EgovMasterDataCaching masterCache = EgovMasterDataCaching.getInstance();
		setupDropdownDataExcluding(Constants.SUB_SCHEME);
		dropdownData.put("finYearList", getPersistenceService().findAllBy("from CFinancialYear where isActive=1 order by finYearRange desc "));
		dropdownData.put("budgetGroupList", masterCache.get("egf-budgetGroup"));
		if(shouldShowField(Constants.SUB_SCHEME))
			dropdownData.put("subSchemeList", Collections.EMPTY_LIST);
		if(shouldShowField(Constants.FUNCTIONARY))
			dropdownData.put("functionaryList", masterCache.get("egi-functionary"));
		if(shouldShowField(Constants.FUNCTION))
			dropdownData.put("functionList",  masterCache.get("egi-function"));
		if(shouldShowField(Constants.SCHEME))
			dropdownData.put("schemeList", persistenceService.findAllBy("from Scheme where isActive=1 order by name"));
		if(shouldShowField(Constants.EXECUTING_DEPARTMENT)) 
			dropdownData.put("executingDepartmentList", masterCache.get("egi-department"));
		if(shouldShowField(Constants.FUND))
			dropdownData.put("fundList", persistenceService.findAllBy("from Fund where isNotLeaf='0' and isActive='1' order by name"));
		if(shouldShowField(Constants.BOUNDARY))
			dropdownData.put("boundaryList", persistenceService.findAllBy("from BoundaryImpl order by name"));
	}
	
	public final boolean shouldShowField(String fieldName) {
		if(headerFields.isEmpty() && gridFields.isEmpty())
			return true;
		return budgetDetailConfig.shouldShowField(headerFields,fieldName) || budgetDetailConfig.shouldShowField(gridFields,fieldName);
	}
	
	public boolean shouldShowHeaderField(String fieldName) {
		return budgetDetailConfig.shouldShowField(headerFields,fieldName);
	}

	public boolean shouldShowGridField(String fieldName) {
		return budgetDetailConfig.shouldShowField(gridFields,fieldName);
	}

	public String execute() throws Exception {
		return "modify";
	}

	@Override
	public void prepare() {
		super.prepare();
		headerFields = budgetDetailConfig.getHeaderFields();
		gridFields = budgetDetailConfig.getGridFields();
		mandatoryFields = budgetDetailConfig.getMandatoryFields();
		setupDropdownsInHeader();
		EgovMasterDataCaching masterCache = EgovMasterDataCaching.getInstance();
		addDropdownData("departmentList", masterCache.get("egi-department"));
		addDropdownData("designationList", Collections.EMPTY_LIST);
		addDropdownData("userList", Collections.EMPTY_LIST);
	}
	public Object getModel() {
		return budgetDetail;
	}

	public String update(){
		for (BudgetReAppropriationView entry : savedBudgetReAppropriationList) {
			BudgetReAppropriation reApp = budgetReAppropriationService.findBySequenceNumberAndBudgetDetail(entry.getSequenceNumber(), entry.getBudgetDetail().getId());
			if("Addition".equalsIgnoreCase(entry.changeRequestType)){
				reApp.setOriginalAdditionAmount(entry.getDeltaAmount());
				reApp.setAdditionAmount(entry.getApprovedDeltaAmount());
			}
			else{
				reApp.setOriginalDeductionAmount(entry.getDeltaAmount());
				reApp.setDeductionAmount(entry.getApprovedDeltaAmount());
			}
			budgetReAppropriationService.persist(reApp);
		}
		if(!savedBudgetReAppropriationList.isEmpty())
			addActionMessage(getText("reapp.modified.successfully"));
		savedBudgetReAppropriationList = Collections.EMPTY_LIST;
		return "modify";
	}
	
	public String list(){
		savedBudgetReAppropriationList.clear();
		budgetDetail = budgetReAppropriationService.setRelatedValues(budgetDetail);
		List<BudgetReAppropriation> results = budgetReAppropriationService.getNonApprovedReAppByUser(Integer.valueOf(EGOVThreadLocals.getUserId().trim()),budgetDetail,financialYear);
		for (BudgetReAppropriation row : results) {
			BudgetReAppropriationView budgetReAppropriationView = new BudgetReAppropriationView();
			budgetReAppropriationView.setBudgetDetail(row.getBudgetDetail());
			BigDecimal approvedReAppropriationsTotal = row.getBudgetDetail().getApprovedReAppropriationsTotal();
			budgetReAppropriationView.setAppropriatedAmount(approvedReAppropriationsTotal==null?BigDecimal.ZERO.setScale(2):approvedReAppropriationsTotal.setScale(2));
			BigDecimal actuals = budgetDetailHelper.getTotalActualsFor(budgetDetailHelper.constructParamMap(getValueStack(),budgetReAppropriationView.getBudgetDetail()), new Date());
			budgetReAppropriationView.setActuals(actuals.setScale(2));
			budgetReAppropriationView.setApprovedAmount(budgetDetail.getApprovedAmount().setScale(2));
			budgetReAppropriationView.setAvailableAmount(budgetReAppropriationView.getApprovedAmount()
					.add(budgetReAppropriationView.getAppropriatedAmount()).subtract(budgetReAppropriationView.getActuals()).setScale(2));
			budgetReAppropriationView.setSequenceNumber(row.getReAppropriationMisc().getSequenceNumber());
			if(row.getOriginalAdditionAmount() == null || BigDecimal.ZERO.equals(row.getOriginalAdditionAmount())){
				budgetReAppropriationView.setChangeRequestType("Deduction");
				budgetReAppropriationView.setDeltaAmount(row.getOriginalDeductionAmount());
				if(row.getDeductionAmount() == null || BigDecimal.ZERO.equals(row.getDeductionAmount()))
					budgetReAppropriationView.setApprovedDeltaAmount(row.getOriginalDeductionAmount()==null?BigDecimal.ZERO:row.getOriginalDeductionAmount());
				else
					budgetReAppropriationView.setApprovedDeltaAmount(row.getDeductionAmount());
			}
			else{
				budgetReAppropriationView.setChangeRequestType("Addition");
				budgetReAppropriationView.setDeltaAmount(row.getOriginalAdditionAmount()==null?BigDecimal.ZERO.setScale(2):row.getOriginalAdditionAmount().setScale(2));
				if(row.getAdditionAmount() == null || BigDecimal.ZERO.equals(row.getAdditionAmount()))
					budgetReAppropriationView.setApprovedDeltaAmount(row.getOriginalAdditionAmount()==null?BigDecimal.ZERO.setScale(2):row.getOriginalAdditionAmount().setScale(2));
				else
					budgetReAppropriationView.setApprovedDeltaAmount(row.getAdditionAmount().setScale(2));
			}
			savedBudgetReAppropriationList.add(budgetReAppropriationView);
		}
		if(savedBudgetReAppropriationList.isEmpty())
			message = getText("no.data.found");
		return "modify";
	}
	
	protected ValueStack getValueStack() {
		return ActionContext.getContext().getValueStack();
	}
	
	public String ajaxDeleteBudgetReAppropriation(){
		Long budgetDetailId = Long.valueOf(parameters.get("id")[0]);
		String sequenceNumber = String.valueOf(parameters.get("sequenceNumber")[0]);
		BudgetReAppropriation reApp = budgetReAppropriationService.findBySequenceNumberAndBudgetDetail(sequenceNumber, budgetDetailId);
		if(reApp != null){
			budgetReAppropriationService.delete(reApp);
			deleted = true;
		}
		return "deleted";
	}

	public boolean isDeleted() {
		return deleted;
	}
	
	public String approvalList(){
		if(budgetReAppropriation == null)
			return "approvalList";
		miscId = budgetReAppropriation.getReAppropriationMisc().getId();
		BudgetReAppropriationMisc  misc = (BudgetReAppropriationMisc) persistenceService.find("from BudgetReAppropriationMisc where id=?", budgetReAppropriation.getReAppropriationMisc().getId());
		workFlowItem = misc;
		setEnablingAmounts(misc);
		comment = misc.getCurrentState().getText1();
		setValidActions(miscWorkflowService.getValidActions(misc));
		List<BudgetReAppropriation> nonApprovedReAppropriations = misc.getNonApprovedReAppropriations();
		for (BudgetReAppropriation row : nonApprovedReAppropriations) {
			BudgetReAppropriationView budgetReAppropriationView = new BudgetReAppropriationView();
			budgetReAppropriationView.setId(row.getId());
			budgetReAppropriationView.setBudgetDetail(row.getBudgetDetail());
			String actuals = budgetDetailHelper.getActualsFor(budgetDetailHelper.constructParamMap(getValueStack(),budgetReAppropriationView.getBudgetDetail()), new Date());
			budgetReAppropriationView.setActuals(new BigDecimal(actuals));
			budgetReAppropriationView.setApprovedAmount(row.getBudgetDetail().getApprovedAmount());
			budgetReAppropriationView.setAddedReleased(row.getBudgetDetail().getApprovedReAppropriationsTotal()==null?BigDecimal.ZERO:row.getBudgetDetail().getApprovedReAppropriationsTotal());
			budgetReAppropriationView.setAvailableAmount(budgetReAppropriationView.getApprovedAmount().add(budgetReAppropriationView.getAddedReleased())
					.subtract(budgetReAppropriationView.getActuals()));
			budgetReAppropriationView.setSequenceNumber(row.getReAppropriationMisc().getSequenceNumber());
			if(row.getOriginalAdditionAmount() == null || BigDecimal.ZERO.equals(row.getOriginalAdditionAmount())){
				budgetReAppropriationView.setChangeRequestType("Deduction");
				budgetReAppropriationView.setDeltaAmount(row.getOriginalDeductionAmount());
				if(row.getDeductionAmount() == null || BigDecimal.ZERO.equals(row.getDeductionAmount()))
					budgetReAppropriationView.setApprovedDeltaAmount(row.getOriginalDeductionAmount()==null?BigDecimal.ZERO:row.getOriginalDeductionAmount());
				else
					budgetReAppropriationView.setApprovedDeltaAmount(row.getDeductionAmount());
			}
			else{
				budgetReAppropriationView.setChangeRequestType("Addition");
				budgetReAppropriationView.setDeltaAmount(row.getOriginalAdditionAmount()==null?BigDecimal.ZERO.setScale(2):row.getOriginalAdditionAmount().setScale(2));
				if(row.getAdditionAmount() == null || BigDecimal.ZERO.equals(row.getAdditionAmount()))
					budgetReAppropriationView.setApprovedDeltaAmount(row.getOriginalAdditionAmount()==null?BigDecimal.ZERO.setScale(2):row.getOriginalAdditionAmount().setScale(2));
				else
					budgetReAppropriationView.setApprovedDeltaAmount(row.getAdditionAmount().setScale(2));
			}
			savedBudgetReAppropriationList.add(budgetReAppropriationView);
			budgetDetail = row.getBudgetDetail();
			financialYear = row.getBudgetDetail().getBudget().getFinancialYear();
		}
		return "approvalList";
	}
	public boolean enableApprovedAmount(){
		return enableApprovedAmount;
	}
	public boolean enableOriginalAmount(){
		return enableOriginalAmount;
	}

	public String forward() {
		actionName = actionName.replace(",", "").replace(" ", "").trim();
		BudgetReAppropriationMisc misc = null;
		for (BudgetReAppropriationView detail : savedBudgetReAppropriationList) {
			BudgetReAppropriation reApp = budgetReAppropriationService.findBySequenceNumberAndBudgetDetail(detail.getSequenceNumber(), detail.getBudgetDetail().getId());
			misc = approveReApp(reApp.getReAppropriationMisc(),reApp);
		}
		setEnablingAmounts(misc);
		update();
		return "success";
	}
	
	public String performAction() {
		if(miscId != null){
			BudgetReAppropriationMisc  misc = (BudgetReAppropriationMisc) persistenceService.find("from BudgetReAppropriationMisc where id=?", miscId);	
			if(misc != null){
				List<BudgetReAppropriation> reApps = budgetReAppropriationService.findAllBy("from BudgetReAppropriation where reAppropriationMisc.id=?", misc.getId());
				actionName = actionName.replace(",", "").replace(" ", "").trim();
				setEnablingAmounts(misc);
				if(actionName!=null && "forward".equalsIgnoreCase(actionName.trim()) || actionName.contains("approv")){  //if mode is approve move the object
					for (BudgetReAppropriation detail : reApps) {
						setAmounts(detail, getReAppById(detail.getId(),savedBudgetReAppropriationList));
					}
					misc = approve(misc,reApps);
				}else{ 
					for (BudgetReAppropriation detail : reApps) {
						setAmounts(detail, getReAppById(detail.getId(),savedBudgetReAppropriationList));
						budgetReAppropriationService.persist(detail);
					}
					addActionMessage(getText("budget.reapp.saved"));
				}
			}
			if("END".equalsIgnoreCase(misc.getCurrentState().getValue())){
				for (BudgetReAppropriation entry : misc.getBudgetReAppropriations()) {
					budgetReAppropriationService.updatePlanningBudget(entry);
				}
			}
		}
		savedBudgetReAppropriationList = Collections.EMPTY_LIST;
		return "success";
	}

	private void setAmounts(BudgetReAppropriation detail,BudgetReAppropriationView reAppById) {
		if(reAppById!=null){
			if(enableOriginalAmount){
				if("Addition".equalsIgnoreCase(reAppById.getChangeRequestType()))
					detail.setOriginalAdditionAmount(reAppById.getDeltaAmount());
				else
					detail.setOriginalDeductionAmount(reAppById.getDeltaAmount());
			}
			if(enableApprovedAmount){
				if("Addition".equalsIgnoreCase(reAppById.getChangeRequestType()))
					detail.setAdditionAmount(reAppById.getApprovedDeltaAmount());
				else
					detail.setDeductionAmount(reAppById.getApprovedDeltaAmount());
			}
		}
	}
	
	private BudgetReAppropriationView getReAppById(Long id,List<BudgetReAppropriationView> savedBudgetReAppropriationList2) {
		for (BudgetReAppropriationView budgetReAppropriationView : savedBudgetReAppropriationList2) {
			if(id!=null && id.equals(budgetReAppropriationView.getId()))
				return budgetReAppropriationView;
		}
		return null;
	}

	private void setEnablingAmounts(BudgetReAppropriationMisc misc){
		Script script = scriptService.findAllByNamedQuery(Script.BY_NAME, "BudgetDetail.enable.amounts").get(0);
		String value = (String) scriptService.executeScript(script,scriptService.createContext("wfItem",misc,"persistenceService",budgetService));
		if("approved".equalsIgnoreCase(value))
			enableApprovedAmount = true;
		else if("original".equalsIgnoreCase(value))
			enableOriginalAmount = true;
	}

	private BudgetReAppropriationMisc approve(BudgetReAppropriationMisc misc, List<BudgetReAppropriation> reApps) {
		Integer userId = fetchUserId();
		for (BudgetReAppropriation detail : reApps) {
			budgetReAppropriationService.transition(actionName+"|"+userId, detail, comment);
		}
		misc = transformAndSetActionMessage(misc, userId);  
		return misc;
	}

	private BudgetReAppropriationMisc approveReApp(BudgetReAppropriationMisc misc, BudgetReAppropriation reApp) {
		Integer userId = fetchUserId();
		budgetReAppropriationService.transition(actionName+"|"+userId, reApp, comment);
		misc = transformAndSetActionMessage(misc, userId);  
		return misc;
	}

	private BudgetReAppropriationMisc transformAndSetActionMessage(BudgetReAppropriationMisc misc, Integer userId) {
		misc = budgetReAppropriationService.performActionOnMisc(actionName+"|"+userId,misc,comment);
		Position owner = misc.getState().getOwner();
		if(actionName.contains("approv")){
			if("END".equalsIgnoreCase(misc.getCurrentState().getValue())){
				addActionMessage(getText("budget.reapp.approved.end"));
			}else{
				addActionMessage(getText("budget.reapp.approved")+budgetService.getEmployeeNameAndDesignationForPosition(owner));
			}
		}else{
			addActionMessage(getText("budget.reapp.approved")+budgetService.getEmployeeNameAndDesignationForPosition(owner));
		}
		return misc;
	}

	private Integer fetchUserId() {
		Integer userId = null;
		if (null != parameters.get("approverUserId") && Integer.valueOf(parameters.get("approverUserId")[0])!=-1 ) {
			userId = Integer.valueOf(parameters.get("approverUserId")[0]);
		}else {
			userId = Integer.valueOf(EGOVThreadLocals.getUserId().trim());
		}
		return userId;
	}
	public void setValidActions(List<Action> validActions) {
		this.validActions = validActions;
	}

	public List<Action> getValidActions() {
		return validActions;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getComment() {
		return comment;
	}

	public void setWorkFlowItem(BudgetReAppropriationMisc workFlowItem) {
		this.workFlowItem = workFlowItem;
	}

	public BudgetReAppropriationMisc getWorkFlowItem() {
		return workFlowItem;
	}

	public void setActionName(String actionName) {
		this.actionName = actionName;
	}

	public String getActionName() {
		return actionName;
	}

	public void setEnableApprovedAmount(boolean enableApprovedAmount) {
		this.enableApprovedAmount = enableApprovedAmount;
	}

	public void setEnableOriginalAmount(boolean enableOriginalAmount) {
		this.enableOriginalAmount = enableOriginalAmount;
	}

	public String capitalize(String value){
		if (value == null || value.length() == 0) return value;
		return value.substring(0, 1).toUpperCase() + value.substring(1).toLowerCase();
	}

	public void setEisCommonsService(EisCommonsService eisCommonsService) {
		this.eisCommonsService = eisCommonsService;
	}

}
	
