package org.egov.works.web.actions.masters;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.egov.exceptions.EGOVException;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.commons.CFinancialYear;
import org.egov.commons.CFunction;
import org.egov.commons.EgwTypeOfWork;
import org.egov.commons.Fund;
import org.egov.commons.Fundsource;
import org.egov.commons.Scheme;
import org.egov.commons.SubScheme;
import org.egov.commons.service.CommonsService;
import org.egov.infstr.models.Script;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.services.ScriptService;
import org.egov.lib.admbndry.Boundary;
import org.egov.lib.admbndry.BoundaryDAO;
import org.egov.lib.admbndry.BoundaryImpl;
import org.egov.lib.admbndry.BoundaryType;
import org.egov.lib.admbndry.BoundaryTypeDAO;
import org.egov.lib.admbndry.HeirarchyType;
import org.egov.lib.admbndry.HeirarchyTypeDAO;
import org.egov.lib.rjbac.dept.DepartmentImpl;
import org.egov.pims.service.EmployeeService;
import org.egov.web.actions.BaseFormAction;
import org.egov.works.models.estimate.ProjectCode;
import org.egov.works.models.estimate.ProjectCodeGenerator;
import org.egov.works.models.estimate.WorkType;
import org.egov.works.models.masters.DepositCode;
import org.egov.works.models.masters.DepositCodeGenerator;
import org.egov.works.services.WorksService;
import org.egov.works.web.actions.estimate.AjaxEstimateAction;
import org.egov.works.web.actions.estimate.AjaxFinancialDetailAction;

//@Result(name=Action.SUCCESS, type=ServletRedirectResult.class, value = "subledgerCode.action")  
@ParentPackage("egov")
public class SubledgerCodeAction extends BaseFormAction{
	private DepositCode depositCode = new DepositCode(); 
	private static final Logger logger = Logger.getLogger(SubledgerCodeAction.class);
	private EmployeeService employeeService;
   	private CommonsService commonsService;
	private static final String ADMIN_HIERARCHY_TYPE = "ADMINISTRATION";
	private WorksService worksService; 
	private String currentFinancialYearId;	 
	private static final String SCRIPT_NAME="works.subledgerCode.showFields";
	private List<String> list;
	private boolean depCode;
	private boolean prjctCode;
	private ProjectCode projectCode =new ProjectCode();
	private PersistenceService<DepositCode,Integer> depositCodeService; 
	private PersistenceService<ProjectCode,Integer> projectCodeService; 
	private DepositCodeGenerator depositCodeGenerator;
	private ProjectCodeGenerator projectcodeGenerator;
	private static final Boolean ISACTIVE=true;
	private String codeName;
	private transient ScriptService scriptExecutionService;

	@Override
	public Object getModel() {
		// TODO Auto-generated method stub
		return depositCode;
	}
	
	public void prepare(){
			logger.debug("Inside Prepare Method..........");
		    // To Read list of Fields to be shown in UI from the script.
		 	Script validScript = (Script) persistenceService.findAllByNamedQuery(Script.BY_NAME,SCRIPT_NAME).get(0);
		 	if(prjctCode){
		 		list = (List<String>)  scriptExecutionService.executeScript(validScript, ScriptService.createContext("depCode",null,"projCode",projectCode));
		 	}
		 	else{
				depCode=true;  // Onload to set DepositCode Radio Button 
				list = (List<String>)  scriptExecutionService.executeScript(validScript, ScriptService.createContext("depCode",depositCode,"projCode",null));
		 	}
			AjaxEstimateAction ajaxEstimateAction = new AjaxEstimateAction();
			ajaxEstimateAction.setPersistenceService(getPersistenceService());
			ajaxEstimateAction.setEmployeeService(employeeService);
			AjaxFinancialDetailAction ajaxFinancialDetailAction = new AjaxFinancialDetailAction();
			ajaxFinancialDetailAction.setPersistenceService(getPersistenceService());
			super.prepare();
			setupDropdownDataExcluding("typeOfWork","subTypeOfWork","fundSource","function","ward","zone","fund","scheme","subScheme");
			addDropdownData("financialYearList", getPersistenceService().findAllBy("from CFinancialYear where isActive=1"));
			addDropdownData("typeOfWorkList", getPersistenceService().findAllBy("from EgwTypeOfWork etw1 where etw1.parentid is null"));
			addDropdownData("functionList", commonsService.getAllFunction());   
			
			// TO load Fund and FundSource dropdown with appconfig defined values in case of SubLedgerDepositCode
			if((list.isEmpty() || !list.isEmpty()) && depCode){
				String config = worksService.getWorksConfigValue("SLDEPOSITCODE_SHOW_FUNDS"); 
				List<String> code = new ArrayList<String>();
				if(config==null) {
					addDropdownData("fundList",code);	 
				} 
				else {
					for(int i=0;i<config.split(",").length;i++)
						code.add(config.split(",")[i]);
					addDropdownData("fundList",getPersistenceService().findAllByNamedQuery("getListOfFundsForCodes", code));
				}
					
				
				config = worksService.getWorksConfigValue("SLDEPOSITCODE_SHOW_FUNDSOURCE");
				code = new ArrayList<String>();
				if(config==null) {
					addDropdownData("fundSourceList",code);	
				}
				else {
					for(int i=0;i<config.split(",").length;i++)
						code.add(config.split(",")[i]);
					addDropdownData("fundSourceList",getPersistenceService().findAllByNamedQuery("getListOfFundSourceForCodes", code));
				}
					
			}
			else{
				addDropdownData("fundList", commonsService.getAllActiveIsLeafFunds());
				try {
					addDropdownData("fundSourceList",commonsService.getAllActiveIsLeafFundSources());
				} catch (EGOVException e) {
					addFieldError("fundsourceunavailable", "Unable to load fund source information");
				}
			}
			
			
			/**
			 *  Fetch Zone Dropdown List
			 */
			addDropdownData("zoneList", getAllZone());
			addDropdownData("wardList",Collections.emptyList());
			addDropdownData("subTypeOfWorkList", Collections.emptyList());
			addDropdownData("schemeList", Collections.emptyList());
			addDropdownData("subSchemeList", Collections.emptyList());
			 CFinancialYear financialYear = getCurrentFinancialYear();
			 if(financialYear!=null) {
				 currentFinancialYearId=financialYear.getId().toString();
			 }
	}
	
	/* To Populate Zone DropDown */
	public List<Boundary> getAllZone() {
	    HeirarchyType hType = null;
		try{	
			hType = new HeirarchyTypeDAO().getHierarchyTypeByName(ADMIN_HIERARCHY_TYPE);
		}catch(EGOVException e){
			logger.error("Error while loading HeirarchyType - HeirarchyType."+ e.getMessage());
			throw new EGOVRuntimeException("Unable To Load Heirarchy Information",e);
		}
		List<Boundary> zoneList = null;
		BoundaryType bType = new BoundaryTypeDAO().getBoundaryType("zone",hType);
		zoneList = new BoundaryDAO().getAllBoundariesByBndryTypeId(bType.getId());
		return zoneList;
	}
	
	/* To Get Current Financial Year Range */
	protected CFinancialYear getCurrentFinancialYear() {
		String finyearRange = worksService.getWorksConfigValue("FINANCIAL_YEAR_RANGE");
		if(StringUtils.isNotBlank(finyearRange))
			return commonsService.getFinancialYearByFinYearRange(finyearRange);
		else
			return commonsService.getFinancialYearById(Long.valueOf(commonsService.getCurrYearFiscalId()));
	} 
	
	public String execute(){  
		return SUCCESS;
	}
	
	public SubledgerCodeAction() {
		addRelatedEntity("fundSource",Fundsource.class);
		addRelatedEntity("department", DepartmentImpl.class);
		addRelatedEntity("fund", Fund.class);
		addRelatedEntity("worksType", WorkType.class); 
		addRelatedEntity("typeOfWork", EgwTypeOfWork.class);
		addRelatedEntity("subTypeOfWork", EgwTypeOfWork.class);
		addRelatedEntity("function", CFunction.class);
		addRelatedEntity("ward", BoundaryImpl.class);
		addRelatedEntity("zone", BoundaryImpl.class);		
		addRelatedEntity("scheme", Scheme.class);
		addRelatedEntity("subScheme", SubScheme.class);
		addRelatedEntity("financialYear", CFinancialYear.class);
	}
	
	public String save(){
		logger.debug("Inside save method().....Account Entity Type DepositCode : "+depCode+".....ProjectCode : "+prjctCode); 
		if(depCode){
			depositCode.setCode(depositCodeGenerator.getAutoGeneratedDepositCode(depositCode));
			depositCode.setIsActive(ISACTIVE);
			depositCodeService.persist(depositCode);  // Persists an Entry in EGW_DEPOSITCODE Table
			worksService.createAccountDetailKey(depositCode.getId(), "DEPOSITCODE"); // Persists an Entry in ACCOUNTDETAILKEY Table
		}
		else if(prjctCode){
			projectCode.setCode(projectcodeGenerator.getAutoGeneratedProjectCode(depositCode));
			projectCode.setCodeName(codeName);
			projectCode.setIsActive(ISACTIVE);
			projectCodeService.persist(projectCode); // Persists an Entry in EGW_PROJECTCODE Table
			worksService.createAccountDetailKey(projectCode.getId(), "PROJECTCODE"); // Persists an Entry in ACCOUNTDETAILKEY Table
		}
		return SUCCESS; 
	}
	
	public String newform(){  
		return NEW; 
	}

	public EmployeeService getEmployeeService() {
		return employeeService;
	}

	public void setEmployeeService(EmployeeService employeeService) {
		this.employeeService = employeeService;
	}

	public CommonsService getCommonsService() {
		return commonsService;
	}

	public void setCommonsService(CommonsService commonsService) {
		this.commonsService = commonsService;
	}

	public WorksService getWorksService() {
		return worksService;
	}

	public void setWorksService(WorksService worksService) {
		this.worksService = worksService;
	}

	public String getCurrentFinancialYearId() {
		return currentFinancialYearId;
	}

	public void setCurrentFinancialYearId(String currentFinancialYearId) {
		this.currentFinancialYearId = currentFinancialYearId;
	}

	public List<String> getList() {
		return list;
	}

	public void setList(List<String> list) {
		this.list = list;
	}

	public DepositCode getDepositCode() {
		return depositCode;
	}

	public void setDepositCode(DepositCode depositCode) {
		this.depositCode = depositCode;
	}

	public boolean isPrjctCode() {
		return prjctCode;
	}

	public void setPrjctCode(boolean prjctCode) {
		this.prjctCode = prjctCode;
	}

	public void setProjectCode(ProjectCode projectCode) {
		this.projectCode = projectCode;
	}

	public ProjectCode getProjectCode() {
		return projectCode;
	}

	public void setDepositCodeService(
			PersistenceService<DepositCode, Integer> depositCodeService) {
		this.depositCodeService = depositCodeService;
	}

	public void setDepositCodeGenerator(DepositCodeGenerator depositCodeGenerator) {
		this.depositCodeGenerator = depositCodeGenerator;
	}

	public boolean isDepCode() {
		return depCode;
	}

	public void setDepCode(boolean depCode) {
		this.depCode = depCode;
	}

	public void setProjectcodeGenerator(ProjectCodeGenerator projectcodeGenerator) {
		this.projectcodeGenerator = projectcodeGenerator;
	}

	public void setProjectCodeService(
			PersistenceService<ProjectCode, Integer> projectCodeService) {
		this.projectCodeService = projectCodeService;
	}

	public String getCodeName() {
		return codeName;
	}

	public void setCodeName(String codeName) {
		this.codeName = codeName;
	}

	public void setScriptExecutionService(ScriptService scriptExecutionService) {
		this.scriptExecutionService = scriptExecutionService;
	}
	
}
