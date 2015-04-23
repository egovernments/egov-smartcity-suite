package org.egov.web.actions.budget;

import org.apache.struts2.convention.annotation.Action;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.dispatcher.ServletRedirectResult;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.commons.CChartOfAccounts;
import org.egov.infstr.commons.dao.GenericHibernateDaoFactory;
import org.egov.infstr.config.AppConfigValues;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.utils.EgovMasterDataCaching;
import org.egov.model.budget.BudgetGroup;
import org.egov.web.actions.BaseFormAction;
import org.egov.utils.BudgetAccountType;
import org.egov.utils.BudgetingType;
import org.egov.utils.Constants;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.validator.annotations.Validation;

@Result(name=Action.SUCCESS, type=ServletRedirectResult.class, value = "budgetGroup.action")
@ParentPackage("egov")
@Validation
public class BudgetGroupAction extends BaseFormAction{
	private static final long serialVersionUID = 1L;
	private BudgetGroup budgetGroup = new BudgetGroup();
	private PersistenceService<BudgetGroup, Long> budgetGroupService;
	private GenericHibernateDaoFactory genericDao;	
	private List<BudgetGroup> budgetGroupList = new ArrayList<BudgetGroup>();
	private static final String SEARCH="search"; 
	private static final String VIEW="view"; 
	private static final String MAJORCODE="majorCode";
	private static final String LENGTHQUERY=" from CChartOfAccounts c where length(c.glcode)=? order by c.glcode";
	private static final String BGQUERYID=" from BudgetGroup where minCode.glcode<=? and maxCode.glcode>=? and id!=? ";
	private static final String BGQUERY="from BudgetGroup where majorCode.glcode<=? and majorCode.glcode>=?";
	private String target="";
	private String mode="";
	private int majorcodelength =0;
	public void setGenericDao(final GenericHibernateDaoFactory genericDao) {
		this.genericDao = genericDao;
	}
	
	@Override
	public Object getModel() {
		return budgetGroup;
	}
	
	public BudgetGroupAction() {
		addRelatedEntity(MAJORCODE, CChartOfAccounts.class);
		addRelatedEntity("minCode", CChartOfAccounts.class);
		addRelatedEntity("maxCode", CChartOfAccounts.class);
	}
	public String execute()
	{
		return INDEX;
	}
	public void prepare()
	{
		super.prepare();
		List<AppConfigValues> appList = genericDao.getAppConfigValuesDAO().getConfigValuesByModuleAndKey(Constants.EGF,"coa_majorcode_length");
		majorcodelength = Integer.valueOf(appList.get(0).getValue());
		
		appList = genericDao.getAppConfigValuesDAO().getConfigValuesByModuleAndKey(Constants.EGF,"budgetgroup_range_minor_or_detailed");
		String range = appList.get(0).getValue();
		
		if(range.equalsIgnoreCase("minor"))
			appList = genericDao.getAppConfigValuesDAO().getConfigValuesByModuleAndKey(Constants.EGF,"coa_minorcode_length");
		else
			appList = genericDao.getAppConfigValuesDAO().getConfigValuesByModuleAndKey(Constants.EGF,"coa_detailcode_length");
		int rangecodelength = Integer.valueOf(appList.get(0).getValue());
		
		addDropdownData("majorCodeList", getPersistenceService().findAllBy(LENGTHQUERY,majorcodelength));
		addDropdownData("minCodeList", getPersistenceService().findAllBy(LENGTHQUERY,rangecodelength));
		addDropdownData("maxCodeList", getPersistenceService().findAllBy(LENGTHQUERY,rangecodelength));
		addDropdownData("accountTypeList",Arrays.asList(BudgetAccountType.values()));
		addDropdownData("budgetingTypeList",Arrays.asList(BudgetingType.values())); 
	}
	@SkipValidation
	@Action(value="/budget/budgetGroup-newform")
	public String newform()
	{
		return NEW;
	}
	public String create()
	{
		if(budgetGroup.getMinCode()!=null && budgetGroup.getMaxCode()==null)
			budgetGroup.setMaxCode(budgetGroup.getMinCode());
		if(budgetGroup.getMaxCode()!=null && budgetGroup.getMinCode()==null)
			budgetGroup.setMinCode(budgetGroup.getMaxCode());
		
		budgetGroupService.create(budgetGroup);
		addActionMessage(getMessage("budgetgroup.create"));
		clearBudgetGroupCache();
		target="SUCCESS";
		return NEW;
	}

	protected String getMessage(final String key,String... value) {
		return getText(key,value);
	}
	@SkipValidation
	public String list()
	{
		if(budgetGroup.getName()==null || budgetGroup.getName().equals(""))
			budgetGroupList= budgetGroupService.findAllBy(" from BudgetGroup ");
		else
			budgetGroupList= budgetGroupService.findAllBy(" from BudgetGroup where name=?", budgetGroup.getName());
		if(budgetGroupList.isEmpty())
			target="EMPTY";
		return SEARCH;
	}
	public String save()
	{
		if(budgetGroup.getMinCode()!=null && budgetGroup.getMaxCode()==null)
			budgetGroup.setMaxCode(budgetGroup.getMinCode());
		if(budgetGroup.getMaxCode()!=null && budgetGroup.getMinCode()==null)
			budgetGroup.setMinCode(budgetGroup.getMaxCode());
		budgetGroupService.update(budgetGroup);
		clearBudgetGroupCache();
		addActionMessage(getMessage("budgetgroup.update"));
		target="SUCCESS";
		mode="edit";
		return NEW;
	}

	private void clearBudgetGroupCache() {
		EgovMasterDataCaching.getInstance().removeFromCache("egf-budgetGroup");
	}
	@SkipValidation
	@Action(value="/budget/budgetGroup-edit")
	public String edit()
	{
		budgetGroup=budgetGroupService.findById(budgetGroup.getId(), false);
		if(getMode().equals("edit"))
			return EDIT;
		else
			return VIEW;
	}
	@SkipValidation
	@Action(value="/budget/budgetGroup-search")
	public String search(){
		mode=parameters.get("mode")[0];
		target="NONE";
		return SEARCH;
	}
	public void validate()
	{
		if(budgetGroup.getMajorCode()==null && budgetGroup.getMinCode()==null && budgetGroup.getMaxCode()==null)
			addFieldError(MAJORCODE, getMessage("budgetgroup.select.accountcode"));
		if(budgetGroup.getMajorCode()!=null && (budgetGroup.getMinCode()!=null || budgetGroup.getMaxCode()!=null))
			addFieldError(MAJORCODE, getMessage("budgetgroup.invalid.mapping"));
		
		
		if(budgetGroup.getIsActive())
		{
			BudgetGroup bg=null;
			if(budgetGroup.getMajorCode()!=null && budgetGroup.getId()==null)  // check major code is mapped to any other budget group
				bg =(BudgetGroup) getPersistenceService().find(" from BudgetGroup where majorCode=? ", budgetGroup.getMajorCode());
			else if(budgetGroup.getMajorCode()!=null && budgetGroup.getId()!=null)
				bg =(BudgetGroup) getPersistenceService().find(" from BudgetGroup where majorCode=? and id!=? ", budgetGroup.getMajorCode(),budgetGroup.getId());
			if(bg!=null) 
				addFieldError(MAJORCODE, getMessage("budgetgroup.invalid.majorcode",new String[]{bg.getName()}));
			bg=null;
			
			if(budgetGroup.getMinCode()!=null && budgetGroup.getId()==null)  // check min code is mapped to any other budget group
				bg =(BudgetGroup) getPersistenceService().find(" from BudgetGroup where minCode.glcode<=? and maxCode.glcode>=? ", budgetGroup.getMinCode().getGlcode(),budgetGroup.getMinCode().getGlcode());
			else if(budgetGroup.getMinCode()!=null && budgetGroup.getId()!=null)
				bg =(BudgetGroup) getPersistenceService().find(BGQUERYID, budgetGroup.getMinCode().getGlcode(),budgetGroup.getMinCode().getGlcode(),budgetGroup.getId());
			if(bg!=null) 
				addFieldError("minCode", getMessage("budgetgroup.invalid.mincode",new String[]{bg.getName()}));
			bg=null;
			
			if(budgetGroup.getMaxCode()!=null && budgetGroup.getId()==null)  // check max code is mapped to any other budget group
				bg =(BudgetGroup) getPersistenceService().find(" from BudgetGroup where minCode.glcode<=? and maxCode.glcode>=? ", budgetGroup.getMaxCode().getGlcode(),budgetGroup.getMaxCode().getGlcode());
			else if(budgetGroup.getMaxCode()!=null && budgetGroup.getId()!=null)
				bg =(BudgetGroup) getPersistenceService().find(BGQUERYID, budgetGroup.getMaxCode().getGlcode(),budgetGroup.getMaxCode().getGlcode(),budgetGroup.getId());
			if(bg!=null) 
				addFieldError("maxCode", getMessage("budgetgroup.invalid.maxcode",new String[]{bg.getName()}));
			bg=null;
			
			if(budgetGroup.getMajorCode()!=null  && budgetGroup.getId()==null)  // check under this majorcode, any minor or detail codes are mapped 
				bg =(BudgetGroup) getPersistenceService().find(" from BudgetGroup where substr(minCode.glcode,1,"+budgetGroup.getMajorCode().getGlcode().length()+")<=? and substr(maxCode.glcode,1,"+budgetGroup.getMajorCode().getGlcode().length()+")>=? ", budgetGroup.getMajorCode().getGlcode(),budgetGroup.getMajorCode().getGlcode());
			else if(budgetGroup.getMajorCode()!=null  && budgetGroup.getId()!=null) 
				bg =(BudgetGroup) getPersistenceService().find(" from BudgetGroup where substr(minCode.glcode,1,"+budgetGroup.getMajorCode().getGlcode().length()+")<=? and substr(maxCode.glcode,1,"+budgetGroup.getMajorCode().getGlcode().length()+")>=? and id!=?", budgetGroup.getMajorCode().getGlcode(),budgetGroup.getMajorCode().getGlcode(),budgetGroup.getId());
			if(bg!=null) 
				addFieldError(MAJORCODE, getMessage("budgetgroup.invalid.majorcode1",new String[]{bg.getName()}));
			bg=null;
			
			if(budgetGroup.getMinCode()!=null && budgetGroup.getId()==null)  // check this minor/detail codes majorcode is mapped to any other budget group
				bg =(BudgetGroup) getPersistenceService().find(BGQUERY, budgetGroup.getMinCode().getGlcode().substring(0, majorcodelength),budgetGroup.getMinCode().getGlcode().substring(0, majorcodelength));
			else if(budgetGroup.getMinCode()!=null && budgetGroup.getId()!=null)
				bg =(BudgetGroup) getPersistenceService().find(BGQUERYID, budgetGroup.getMinCode().getGlcode().substring(0, majorcodelength),budgetGroup.getMinCode().getGlcode().substring(0, majorcodelength),budgetGroup.getId());
			if(bg!=null) 
				addFieldError("minCode", getMessage("budgetgroup.invalid.mincode1",new String[]{bg.getName()}));
			bg=null;
			
			if(budgetGroup.getMaxCode()!=null && budgetGroup.getId()==null)  // check this minor/detail codes majorcode is mapped to any other budget group
				bg =(BudgetGroup) getPersistenceService().find(BGQUERY, budgetGroup.getMaxCode().getGlcode().substring(0, majorcodelength),budgetGroup.getMaxCode().getGlcode().substring(0, majorcodelength));
			else if(budgetGroup.getMaxCode()!=null && budgetGroup.getId()!=null)
				bg =(BudgetGroup) getPersistenceService().find(BGQUERYID, budgetGroup.getMaxCode().getGlcode().substring(0, majorcodelength),budgetGroup.getMaxCode().getGlcode().substring(0, majorcodelength),budgetGroup.getId());
			if(bg!=null) 
				addFieldError("maxCode", getMessage("budgetgroup.invalid.maxcode1",new String[]{bg.getName()}));
			bg=null;
		}
	}

	public void setBudgetGroupService(final PersistenceService<BudgetGroup, Long> budgetGroupService) {
		this.budgetGroupService = budgetGroupService;
	}
	/**
	 * @return budget
	 */
	public BudgetGroup getBudgetGroup() {
		return budgetGroup;
	}
	/**
	 * @param budget the budget to set
	 */
	public void setBudgetGroup(final BudgetGroup budgetGroup) {
		this.budgetGroup = budgetGroup;
	}

	public void setTarget(final String target) {
		this.target = target;
	}

	public List<BudgetGroup> getBudgetGroupList() {
		return budgetGroupList;
	}

	public String getTarget() {
		return target;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(final String mode) {
		this.mode = mode;
	}
	
}
