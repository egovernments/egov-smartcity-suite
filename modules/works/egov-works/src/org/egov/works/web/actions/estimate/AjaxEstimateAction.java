package org.egov.works.web.actions.estimate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.egov.exceptions.EGOVException;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.exceptions.NoSuchObjectException;
import org.egov.infstr.config.AppConfigValues;
import org.egov.infstr.models.Money;
import org.egov.infstr.models.Script;
import org.egov.infstr.services.ScriptService;
import org.egov.lib.admbndry.Boundary;
import org.egov.lib.rjbac.dept.Department;
import org.egov.pims.commons.DesignationMaster;
import org.egov.pims.commons.dao.DesignationMasterDAO;
import org.egov.pims.model.Assignment;
import org.egov.pims.model.EmployeeView;
import org.egov.pims.service.EmployeeService;
import org.egov.pims.service.EisUtilService;
import org.egov.pims.service.PersonalInformationService;
import org.egov.web.actions.BaseFormAction;
import org.egov.works.models.estimate.AbstractEstimate;
import org.egov.works.models.estimate.WorkType;
import org.egov.works.models.masters.DocumnetTemplate;
import org.egov.works.models.masters.Overhead;
import org.egov.works.models.tender.TenderFile;
import org.egov.works.models.workorder.WorkOrder;
import org.egov.works.services.AbstractEstimateService;
import org.egov.works.services.WorksService;

public class AjaxEstimateAction extends BaseFormAction {
	private static final Logger logger = Logger.getLogger(AjaxEstimateAction.class);

	private static final String USERS_IN_DEPT = "usersInDept";
	private static final String DESIGN_FOR_EMP = "designForEmp";
	private static final String SUBCATEGORIES = "subcategories";
	private static final String OVERHEADS = "overheads";
	private static final String WORKFLOW_USER_LIST = "workflowUsers";
	private static final String WORKFLOW_DESIG_LIST = "workflowDesignations";
	private Integer executingDepartment;
	private Integer empID;
	private EmployeeService employeeService;
	private List usersInExecutingDepartment;
	private Assignment assignment;
	private List subCategories;
	private Long category;
	
	private Date estDate;
	private List<Overhead> overheads;
	private List<Overhead> overheadsnew;
	private List<Overhead> validOverheads;
	private Overhead overhead;
	private String departmentName;
	private DocumnetTemplate docTemplate;
	private String uomVal;
	private Double rate;
	private List workflowUsers;
	private Integer departmentId;
	private Integer designationId;
	private Integer wardId;
    private Long typeId;
    private String workflowAdditionalRule;
	private List workflowKDesigList;
	private String scriptName;
	private String stateName;
	private Long estimateId;
	private WorksService worksService;
	private EisUtilService eisService;
	private AbstractEstimateService abstractEstimateService;
	private Money worktotalValue;
	private PersonalInformationService personalInformationService;
	private List<String>  checklistValues= new LinkedList<String>();
	private List<AppConfigValues>  estimateChecklist= new LinkedList<AppConfigValues>();
	private static final String CHECK_LIST = "checkList";
	private static final String WORKS="Works";
	private static final String ADDITIONAL_RULE_RESULT="additionalWorkflowrule";
	private static final String SKIP_BUDGET_CHECK = "SKIP_BUDGET_CHECK";
	private static final String PUBLIC_WORKS_DEPARTMENT="Public Work";
	private TenderFile tenderFile;
	private WorkOrder workOrder;
	private String type;
	private String number;
	private transient ScriptService scriptExecutionService;
	
	public String execute(){
		return SUCCESS;
	}

	public Object getModel() {
		return null;
	}
	
	public String designationForUser(){
		try {
			assignment = employeeService.getLatestAssignmentForEmployee(
					empID);
		} catch (Exception e) {
			throw new EGOVRuntimeException("user.find.error", e);
		}
		return DESIGN_FOR_EMP;
	}

		public String usersInExecutingDepartment() {
		try {
			HashMap<String,Object> criteriaParams =new HashMap<String,Object>();
			criteriaParams.put("departmentId", executingDepartment);
			//criteriaParams.put("isPrimary", "Y"); 	// Commented to show primary and secondary designations in Prepared By list
			if(executingDepartment==null || executingDepartment==-1)
				usersInExecutingDepartment=Collections.EMPTY_LIST;
			else{
				putDesgnInCriteria(criteriaParams);
				setEmployeeViewList(criteriaParams);
			}
		} catch (Exception e) {
			throw new EGOVRuntimeException("user.find.error", e);
		} 
		return USERS_IN_DEPT;
	}
		
	public void userWithEmpCodeInDeptOnDate(String empCode, Date date) {
		try {
			HashMap<String,Object> criteriaParams =new HashMap<String,Object>();
			criteriaParams.put("departmentId", executingDepartment);
			if(empCode!=null)
			{
				List <String> empCodeList =  new ArrayList<String>();
				empCodeList.add(empCode);
				criteriaParams.put("employeeCode",empCodeList);
			}
			criteriaParams.put("asOnDate", date);
			//criteriaParams.put("isPrimary", "Y"); 	// Commented to show primary and secondary designations in Prepared By list
			if(executingDepartment==null || executingDepartment==-1)
				usersInExecutingDepartment=Collections.EMPTY_LIST;
			else{
				putDesgnInCriteria(criteriaParams);
				setEmployeeViewList(criteriaParams);
			}
		} catch (Exception e) {
			throw new EGOVRuntimeException("user.find.error", e);
		} 
	}

	private void setEmployeeViewList(HashMap<String, Object> criteriaParams) {
		usersInExecutingDepartment=personalInformationService.getListOfEmployeeViewBasedOnListOfDesignationAndOtherCriteria(criteriaParams, -1, -1);
		List<EmployeeView> finalList= new ArrayList<EmployeeView>();
		EmployeeView mainEmpViewObj,prevEmpView = new EmployeeView();
		Iterator iterator = usersInExecutingDepartment.iterator(); 
		while(iterator.hasNext())
		{
			mainEmpViewObj=(EmployeeView)iterator.next();
			if(!((mainEmpViewObj.getId().equals(prevEmpView.getId())) && (mainEmpViewObj.getDesigId().equals(prevEmpView.getDesigId())))){
				finalList.add(mainEmpViewObj); 
			}
			prevEmpView=mainEmpViewObj;
		}
		usersInExecutingDepartment=Collections.EMPTY_LIST; 
		usersInExecutingDepartment=finalList;
	}			

		private void putDesgnInCriteria(HashMap<String, Object> criteriaParams) {
			String actions = worksService.getWorksConfigValue("DESIGNATIONS_TOLOAD_PREPAREDBY");
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
						criteriaParams.put("designationId", designationIds);
					}
				}
			}
		}
	
	
	public String estimateCheckListDetails() throws NumberFormatException, EGOVException{		
		checklistValues.add("N/A");
		checklistValues.add("Yes");
		checklistValues.add("No");
			
		try {
			estimateChecklist= worksService.getAppConfigValue(WORKS,"Estimate-CheckList");
		} catch (Exception e) {
			logger.error("--------------Error in check bill List-------------");
		}
		return CHECK_LIST;
	}
	
	
	public String subcategories() {
		subCategories = getPersistenceService().findAllBy("from EgwTypeOfWork where parentid.id=?", category);
		return SUBCATEGORIES;
	}
	
	public String overheads() {
		overheads = (List)getPersistenceService().findAllByNamedQuery(Overhead.OVERHEADS_BY_DATE, estDate, estDate); 		
		return OVERHEADS; 
	}
	
	public String revisionOverheads() {
		overheadsnew = (List)getPersistenceService().findAllByNamedQuery(Overhead.OVERHEADS_BY_DATE, new Date(), new Date()); 		
		return OVERHEADS; 
	}
	public String getTemplateByDepName(){
		docTemplate = (DocumnetTemplate) getPersistenceService().findByNamedQuery("getTemplateByDepName", departmentName);
		
		return "documentTemplate";
	}
	public String getFactor(){
		Integer result = 1;
		
		Map<String,Integer> exceptionaSorMap = getExceptionSOR();
		if(exceptionaSorMap.containsKey(uomVal))
			result = exceptionaSorMap.get(uomVal);

    	Double finalRate	= rate/result;

		String outStr = finalRate.toString();
		getServletResponse().setContentType("text/xml");
        getServletResponse().setHeader("Cache-Control", "no-cache");
        try {
        	getServletResponse().getWriter().write(outStr);
        }catch (IOException ioex){
        	logger.info("Error while writing to response --from getByResponseAware()");
        }
		return null;
	}
	
	public Map<String,Integer> getExceptionSOR() {
		List<AppConfigValues> appConfigList = worksService.getAppConfigValue("Works","EXCEPTIONALSOR");
		Map<String,Integer> resultMap = new HashMap<String, Integer>();
		for(AppConfigValues configValue:appConfigList){
			String value[] = configValue.getValue().split(",");
			resultMap.put(value[0], Integer.valueOf(value[1]));
		}
		return resultMap;
	}
	
	public String getWorkFlowAdditionalRule(){
	    WorkType workType= (WorkType) getPersistenceService().find("from WorkType where id=?", typeId);
	    Department department =(Department) getPersistenceService().find("from DepartmentImpl where id=?", departmentId);
	    Boundary ward=(Boundary) getPersistenceService().find("from BoundaryImpl where id=?", wardId);
	    Boolean skipBudget=false;

	    List<String> depositTypeList=worksService.getNatureOfWorkAppConfigValues(WORKS, SKIP_BUDGET_CHECK);;
        if(workType!=null && workType.getName()!=null){
            for(String type:depositTypeList){
                if(type.equals(workType.getName())){
                    skipBudget=true;
                }
            }
        }   
        
        if( department!=null && PUBLIC_WORKS_DEPARTMENT.equalsIgnoreCase(department.getDeptName())){
            if(ward.getBoundaryType().getName().equalsIgnoreCase("City")){
                if(skipBudget){
                    workflowAdditionalRule="HQDepositCodeApp";
                }
                else{
                    workflowAdditionalRule="HQBudgetApp";
                }
            }
            else{
                if(skipBudget){
                    workflowAdditionalRule="zonalDepositCodeApp";
                }
                else{
                    workflowAdditionalRule="zonalBudgetApp";
                }
            }
        }
        else{
            if(skipBudget){
                workflowAdditionalRule="depositCodeApp";
            }
            else{
                workflowAdditionalRule="budgetApp";
            }
        }
        
	    return ADDITIONAL_RULE_RESULT;
	}
	
	public String getWorkFlowUsers() {
		if(designationId!=-1){
			HashMap<String,Object> paramMap = new HashMap<String, Object>();
			if(departmentId!=null && departmentId!=-1)
				paramMap.put("departmentId",departmentId.toString());
			if(wardId!=null && wardId!=-1)
				paramMap.put("boundaryId",wardId.toString());
			
			paramMap.put("designationId", designationId.toString());
			List roleList=worksService.getWorksRoles();	
			if(roleList!=null)
				paramMap.put("roleList", roleList);
			workflowUsers = eisService.getEmployeeInfoList(paramMap); 
		}
		return WORKFLOW_USER_LIST;
	}
	
	public  String getDesgByDeptAndType() {
		workflowKDesigList=new ArrayList<DesignationMaster>();
		String departmentName="";
		Department department=null;
		if(departmentId!=-1) {
			department =(Department)getPersistenceService().find("from DepartmentImpl where id=?", departmentId);
			departmentName=department.getDeptName();
		}		
		DesignationMaster designation=null;
		AbstractEstimate abstractEstimate=null;
		if(estimateId!=null) {
			abstractEstimate=abstractEstimateService.findById(estimateId, false);
		}
		Script validScript = (Script) persistenceService.findAllByNamedQuery(Script.BY_NAME,scriptName).get(0);
		List<String> list = (List<String>) scriptExecutionService.executeScript(validScript, ScriptService.createContext("state",stateName,"department",departmentName,"wfItem",abstractEstimate));
		
		for (String desgName : list) {
			if(desgName.trim().length()!=0){
				try {
					designation =new DesignationMasterDAO().getDesignationByDesignationName(desgName);
					workflowKDesigList.add(designation);
				}
				catch (NoSuchObjectException e) {
					logger.error(e);
				}
			}
		}
		return WORKFLOW_DESIG_LIST;
	}
	
	public String getApprovedTenderFileorWorkOrder(){
		HashMap<String,String> details=new HashMap<String,String>();
		tenderFile = (TenderFile) getPersistenceService().find(" from TenderFile tf where tf.id in (select tfd.tenderFile.id from TenderFileDetail tfd where tfd.abstractEstimate.id=?) and tf.egwStatus.code<>'CANCELLED'", estimateId);
		if(tenderFile!=null) {
			details.put("type", "TenderFile");
			details.put("number",tenderFile.getFileNumber());
		}
		else {
			workOrder=(WorkOrder) getPersistenceService().find("from WorkOrder wo where wo.id in (select woe.workOrder.id from WorkOrderEstimate woe where woe.estimate.id=?) and wo.egwStatus.code<>'CANCELLED'", estimateId);
			if(workOrder!=null) {
				details.put("type","WorkOrder");
				details.put("number",workOrder.getWorkOrderNumber());
			}
		}
		if(details.isEmpty()) {
			type="";
			number="";
		}
		else {
			type=details.get("type");
			number=details.get("number");
		}
	return "approvedTForWO";
}
	
	 /**
     * Convenience method to get the response
     *
     * @return current response
     */

	public HttpServletResponse getServletResponse() {
        return ServletActionContext.getResponse();
	}

	public void setWorksService(WorksService worksService) {
		this.worksService = worksService;
	}

	public List getUsersInExecutingDepartment() {
		return usersInExecutingDepartment;
	}
	
	public void setEmployeeService(EmployeeService employeeService) {
		this.employeeService = employeeService;
	}

	public void setExecutingDepartment(Integer executingDepartment) {
		this.executingDepartment = executingDepartment;
	}

	public void setCategory(Long category) {
		this.category = category;
	}

	public List getSubCategories() {
		return subCategories;
	}
	
	public List<Overhead> getOverheads() {
		return overheads;
	}
	
	public void setOverheads(List<Overhead> overheads) {
		this.overheads=overheads;
	}

	

	public Date getEstDate() {
		return estDate;
	}

	public void setEstDate(Date estDate) {
		this.estDate = estDate;
	}

	public Overhead getOverhead() {
		return overhead;
	}

	public void setOverhead(Overhead overhead) {
		this.overhead = overhead;
	}

	public List<Overhead> getValidOverheads() {
		return validOverheads;
	}

	public void setValidOverheads(List<Overhead> validOverheads) {
		this.validOverheads = validOverheads;
	}

	public Integer getEmpID() {
		return empID;
	}

	public void setEmpID(Integer empID) {
		this.empID = empID;
	}

	public Assignment getAssignment() {
		return assignment;
	}

	public void setAssignment(Assignment assignment) {
		this.assignment = assignment;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	public DocumnetTemplate getDocTemplate() {
		return docTemplate;
	}

	public String getUomVal() {
		return uomVal;
	}

	public void setUomVal(String uomVal) {
		this.uomVal = uomVal;
	}

	public Double getRate() {
		return rate;
	}

	public void setRate(Double rate) {
		this.rate = rate;
	}

	public List getWorkflowUsers() {
		return workflowUsers;
	}

	public Integer getDepartmentId() {
		return departmentId;
	}

	public EisUtilService getEisService() {
		return eisService;
	}
	public void setEisService(EisUtilService eisService) {
		this.eisService = eisService;
	}

	public Integer getDesignationId() {
		return designationId;
	}
	
	public List getWorkflowKDesigList() {
		return workflowKDesigList;
	}

	public String getScriptName() {
		return scriptName;
	}

	public void setScriptName(String scriptName) {
		this.scriptName = scriptName;
	}

	public void setDepartmentId(Integer departmentId) {
		this.departmentId = departmentId;
	}

	public void setDesignationId(Integer designationId) {
		this.designationId = designationId;
	}

	public String getStateName() {
		return stateName;
	}

	public void setStateName(String stateName) {
		this.stateName = stateName;
	}

	public void setAbstractEstimateService(
			AbstractEstimateService abstractEstimateService) {
		this.abstractEstimateService = abstractEstimateService;
	}

	public void setEstimateId(Long estimateId) {
		this.estimateId = estimateId;
	}

	public Integer getWardId() {
		return wardId;
	}

	public void setWardId(Integer wardId) {
		this.wardId = wardId;
	}

	public Money getWorktotalValue() {
		return worktotalValue;
	}

	public void setWorktotalValue(Money worktotalValue) {
		this.worktotalValue = worktotalValue;
	}
	
	public void setPersonalInformationService(
			PersonalInformationService personalInformationService) {
		this.personalInformationService = personalInformationService;
	}

	public List<String> getChecklistValues() {
		return checklistValues;
	}

	public void setChecklistValues(List<String> checklistValues) {
		this.checklistValues = checklistValues;
	}

	public List<AppConfigValues> getEstimateChecklist() {
		return estimateChecklist;
	}

	public void setEstimateChecklist(List<AppConfigValues> estimateChecklist) {
		this.estimateChecklist = estimateChecklist;
	}

    public void setTypeId(Long typeId) {
        this.typeId = typeId;
}

    public String getWorkflowAdditionalRule() {
        return workflowAdditionalRule;
    }

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public List<Overhead> getOverheadsnew() {
		return overheadsnew;
	}

	public void setOverheadsnew(List<Overhead> overheadsnew) {
		this.overheadsnew = overheadsnew;
	}
    
}
