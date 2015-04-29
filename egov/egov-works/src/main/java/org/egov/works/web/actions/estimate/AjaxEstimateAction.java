/**
 * eGov suite of products aim to improve the internal efficiency,transparency,
   accountability and the service delivery of the government  organizations.

    Copyright (C) <2015>  eGovernments Foundation

    The updated version of eGov suite of products as by eGovernments Foundation
    is available at http://www.egovernments.org

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program. If not, see http://www.gnu.org/licenses/ or
    http://www.gnu.org/licenses/gpl.html .

    In addition to the terms of the GPL license to be adhered to in using this
    program, the following additional terms are to be complied with:

	1) All versions of this program, verbatim or modified must carry this
	   Legal Notice.

	2) Any misrepresentation of the origin of the material is prohibited. It
	   is required that all modified versions of this material be marked in
	   reasonable ways as different from the original version.

	3) This license does not grant any rights to any user of the program
	   with regards to rights under trademark law for use of the trade names
	   or trademarks of eGovernments Foundation.

  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.works.web.actions.estimate;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.script.ScriptContext;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.egov.commons.CFinancialYear;
import org.egov.commons.service.CommonsService;
import org.egov.egf.commons.EgovCommon;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.exceptions.NoSuchObjectException;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infstr.models.Money;
import org.egov.infstr.services.ScriptService;
import org.egov.pims.commons.DesignationMaster;
import org.egov.pims.commons.dao.DesignationMasterDAO;
import org.egov.pims.model.Assignment;
import org.egov.pims.model.PersonalInformation;
import org.egov.pims.service.EisUtilService;
import org.egov.pims.service.EmployeeService;
import org.egov.pims.service.PersonalInformationService;
import org.egov.web.actions.BaseFormAction;
import org.egov.works.models.estimate.AbstractEstimate;
import org.egov.works.models.estimate.EstimateTemplateActivity;
import org.egov.works.models.masters.DocumnetTemplate;
import org.egov.works.models.masters.Overhead;
import org.egov.works.models.tender.WorksPackage;
import org.egov.works.services.AbstractEstimateService;
import org.egov.works.services.WorksService;
import org.egov.works.utils.WorksConstants;
import org.springframework.beans.factory.annotation.Autowired;

public class AjaxEstimateAction extends BaseFormAction {
	private static final Logger logger = Logger.getLogger(AjaxEstimateAction.class);

	private static final String USERS_IN_DEPT = "usersInDept";
	private static final String DESIGN_FOR_EMP = "designForEmp";
	private static final String SUBCATEGORIES = "subcategories";
	private static final String OVERHEADS = "overheads";
	private static final String WORKFLOW_USER_LIST = "workflowUsers";
	private static final String WORKFLOW_DESIG_LIST = "workflowDesignations";
	private static final String CHANGE_DEPARTMENT="changeDepartment";
	private static final String ESTIMATE_NUMBER_SEARCH_RESULTS = "estimateNoSearchResults";
	private static final String PROJECT_CODE_SEARCH_RESULTS = "projectCodeSearchResults";
	private static final String DRAFT_ESTIMATE_NUMBER_SEARCH_RESULTS = "draftEstimateNoSearchResults";
	private Long executingDepartment;
	private Integer empID;
	@Autowired
        private EmployeeService employeeService;
	private List usersInExecutingDepartment;
	private Assignment assignment;
	private List subCategories;
	private Long category;
	private boolean isSkipDeptChange;

	private Date estDate;
	private List<Overhead> overheads;
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
	private List workflowKDesigList;
	private String scriptName;
	private String stateName;
	private Long estimateId;
	private WorksService worksService;
	private EisUtilService eisService;
	private AbstractEstimateService abstractEstimateService;
	private Money worktotalValue;
	private PersonalInformationService personalInformationService;
	private String query;
	private String wpNumber="";
	private boolean isVoucherExists=false;
	private EgovCommon egovCommon;
	private List<String> estimateNumberSearchList = new LinkedList<String>();
	private List<String> projectCodeList=new LinkedList<String>();
	private List<String> draftsEstimateNumberSearchList = new LinkedList<String>();
	private String estimateNum="";
	private boolean isCancelEstCopyExists=false;
	private Integer approverDepartmentId;
	private List<Object> approverList;
	private String employeeCode;
	private String dwSORCheck;
	private Long estimateTemplateId;
	private String estimateIds;
	private String sorCodes="";
	private Long woId;
	private String woNumber="";
	@Autowired
        private CommonsService commonsService;
	private List<String> estimateNoList = new LinkedList<String>();
	private BigDecimal estimateAmount;
	@Autowired
	private ScriptService scriptService;

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
	
	/**
	 * This auto-complete method is used to fetch estimate numbers for which
	 * final bill is not created, and
	 * Year end appropriation is not done in the current financial year and for which
	 * Financial sanction is  not done in the current financial year . 
	 * @return
	 */
	public String searchEstimateNumberForYearendAppr(){
		CFinancialYear currentFinYear;	
		currentFinYear=commonsService.getFinancialYearByDate(new Date());
		String strquery="";
		if(!StringUtils.isEmpty(query)) {
			strquery="select ae.estimateNumber from AbstractEstimate ae where ae.projectCode.egwStatus.code!='CLOSED' and ae.depositCode is null and ae.egwStatus.code='ADMIN_SANCTIONED' and ae.estimateNumber like  '%'||?||'%'  " +
					"and NOT EXISTS (select 'true' from  AbstractEstimateAppropriation aea where aea.abstractEstimate.id=ae.id and aea.budgetUsage.releasedAmount=0 and aea.budgetUsage.financialYearId=?) " +
					"and NOT EXISTS (select 'true' from MBHeader as mbh left outer join mbh.egBillregister egbr where mbh.workOrderEstimate.estimate.id=ae.id" +
					" and (egbr.billtype='Final Bill' and egbr.billstatus<>'CANCELLED'))" +
					"  and NOT EXISTS (select 'true' from MultiyearEstimateApprDetail myea where myea.estimate.id=ae.id " +
					"and myea.multiyearEstimateAppr.status.code<>'CANCELLED' and myea.multiyearEstimateAppr.financialYear.id=?)  ";
			
			estimateNoList = getPersistenceService().findAllBy(strquery,query.toUpperCase(),Integer.valueOf(currentFinYear.getId().toString()),currentFinYear.getId());
					
		}
		return "estimateNumSearchResults";
	}
	
	public String depositWorksSOREstTemplateCheck(){
		List<EstimateTemplateActivity> activityList = (List<EstimateTemplateActivity>) getPersistenceService().findAllBy("from EstimateTemplateActivity eta where eta.estimateTemplate.id = ? ", estimateTemplateId);

		dwSORCheck = WorksConstants.VALID;
		for(EstimateTemplateActivity act : activityList){
			if(act.getSchedule()!=null){
				if(!act.getSchedule().getIsDepositWorksSOR()){
					dwSORCheck = WorksConstants.INVALID;
					if(StringUtils.isBlank(sorCodes)){
						sorCodes = sorCodes.concat(act.getSchedule().getCode());
					}
					else{
						sorCodes = sorCodes.concat(", ").concat(act.getSchedule().getCode());
					}
				}
			}
		}
		return "depWorksSORCheck";
	}
	
	public String usersInExecutingDepartment() {
		try {
			HashMap<String,Object> criteriaParams =new HashMap<String,Object>();
			if(executingDepartment!=null && executingDepartment!=-1)
				criteriaParams.put("departmentId", executingDepartment.toString());
			//criteriaParams.put("isPrimary", "Y"); // Commented to show primary and secondary designations in Prepared By list
			if(StringUtils.isNotBlank(employeeCode)) {
				criteriaParams.put("code",employeeCode);
			}
			if(executingDepartment==null || executingDepartment==-1)
				usersInExecutingDepartment=Collections.EMPTY_LIST;
			else
				usersInExecutingDepartment=eisService.getEmployeeInfoList(criteriaParams); 
		} catch (Exception e) {
			throw new EGOVRuntimeException("user.find.error", e);
		}
		return USERS_IN_DEPT;
	}
	
	public String subcategories() {
		subCategories = getPersistenceService().findAllBy("from EgwTypeOfWork where parentid.id=?", category);
		return SUBCATEGORIES;
	}
	
	public String isSkipDepartmentChange(){
		Department department=null;
		if(departmentId !=null && departmentId != -1){
			department=(Department)getPersistenceService().find("from DepartmentImpl where id=?", departmentId);
		}
		String departmentCodes=worksService.getWorksConfigValue("REAPPROPRIATION_DEPARTMENTS");
		isSkipDeptChange=true;
		if(department!=null && departmentCodes!=null){
			for(String dept:departmentCodes.split(",")){
				if(dept.equals(department.getCode())){
					isSkipDeptChange=false;
					break;
				}
			}
		}
		return CHANGE_DEPARTMENT;
	}
	
	public String overheads() {
		overheads = (List)getPersistenceService().findAllByNamedQuery(Overhead.OVERHEADS_BY_DATE, estDate, estDate); 		
		return OVERHEADS;
	}
	
	public String getTemplateByDepName(){
		docTemplate = (DocumnetTemplate) getPersistenceService().findByNamedQuery("getTemplateByDepName", departmentName);
		
		return "documentTemplate";
	}
	public String getFactor(){
		Integer result = 1;
		
		Map<String,Integer> exceptionaSorMap = worksService.getExceptionSOR();
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
	
	public String getPositionByPassingDesigId() { 
		if (this.designationId != null && this.designationId != -1) {

			final HashMap<String, Object> paramMap = new HashMap<String, Object>();
			if (this.approverDepartmentId != null && this.approverDepartmentId != -1) {
				paramMap.put("departmentId", this.approverDepartmentId.toString());
			}
			paramMap.put("designationId", this.designationId.toString());
			List roleList=worksService.getWorksRoles();	
			if(roleList!=null)
				paramMap.put("roleList", roleList);
			this.approverList = new ArrayList<Object>();
			final List<? extends Object> empList = this.eisService.getEmployeeInfoList(paramMap);
			for (final Object emp : empList) {
				this.approverList.add(emp);
			}
		}
		return "workFlowApprovers";
	}
	
	public  String getDesgByDeptAndType() {
		workflowKDesigList=new ArrayList<DesignationMaster>();
		String departmentName="";
		Department department=null;
		if(departmentId!=-1) {
			department =(Department)getPersistenceService().find("from DepartmentImpl where id=?", departmentId);
			departmentName=department.getName();
		}		
		DesignationMaster designation=null;
		AbstractEstimate abstractEstimate=null;
		if(estimateId!=null) {
			abstractEstimate=abstractEstimateService.findById(estimateId, false);
		}
		//Script validScript = (Script) persistenceService.findAllByNamedQuery(Script.BY_NAME,scriptName).get(0);
		//List<String> list = (List<String>) validScript.eval(scriptService.createContext("state",stateName,"department",departmentName,"wfItem",abstractEstimate));
		
		ScriptContext scriptContext = ScriptService.createContext("state",stateName,"department",departmentName,"wfItem",abstractEstimate);
		List<String> list = (List<String>) scriptService.executeScript(scriptName, scriptContext);
		
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
	
	public String validateEstimateForCancel(){
		PersonalInformation emp = null;
		woNumber = (String) getPersistenceService().find("select woe.workOrder.workOrderNumber from WorkOrderEstimate woe where woe.workOrder.rateContract is not null " +
				"and woe.estimate.rateContract is not null and  woe.estimate.id=? and woe.estimate.egwStatus.code=? and woe.workOrder.egwStatus.code<>?"
				,estimateId, AbstractEstimate.EstimateStatus.ADMIN_SANCTIONED.toString(),WorksConstants.CANCELLED_STATUS.toString());
		if(woNumber == null) 
			woNumber = "";
		if(woNumber.equals("")) {
			wpNumber = (String) getPersistenceService().find("select wpd.worksPackage.wpNumber from WorksPackageDetails wpd where wpd.estimate.id=? " +
					"and wpd.estimate.egwStatus.code=? and wpd.worksPackage.egwStatus.code<>?",estimateId, AbstractEstimate.EstimateStatus.ADMIN_SANCTIONED.toString(),
					WorksPackage.WorkPacakgeStatus.CANCELLED.toString());
			if(wpNumber==null)
				wpNumber="";
			Long projectCodeId = (Long) getPersistenceService().find("select ae.projectCode.id from AbstractEstimate ae where ae.id=?",estimateId);
			List<Map<String,String>> expenditureDetails = egovCommon.getExpenditureDetailsforProject(projectCodeId,new Date());
			if(expenditureDetails!=null && !expenditureDetails.isEmpty())
				isVoucherExists = true;			
		}
		
		return "cancelEstimate";
	}
	
	/*
	 * Autocomplete of Admin sanctioned Estimate nos for Cancel Estimate screen 
	 */
	public String searchEstimateNumber(){
		String strquery="";
		ArrayList<Object> params=new ArrayList<Object>();
		if(!StringUtils.isEmpty(query)) {
			strquery="select distinct(ae.estimateNumber) from AbstractEstimate ae where ae.parent is null and UPPER(ae.estimateNumber) like '%'||?||'%' " +
			" and ae.egwStatus.code = ? )";
			params.add(query.toUpperCase());
			params.add(AbstractEstimate.EstimateStatus.ADMIN_SANCTIONED.toString());
			estimateNumberSearchList = getPersistenceService().findAllBy(strquery,params.toArray());
		}
		return ESTIMATE_NUMBER_SEARCH_RESULTS;
	}
	
	/*
	 * Autocomplete for estimates in Drafts - Planend Estimate Report 
	 */
	public String searchEstimateNumberForDraftEstimates(){
		String strquery="";
		ArrayList<Object> params=new ArrayList<Object>();
		if(!StringUtils.isEmpty(query)) {
			strquery="select distinct(ae.estimateNumber) from AbstractEstimate ae where ae.parent is null and UPPER(ae.estimateNumber) like '%'||?||'%' " +
			" and ae.egwStatus.code = 'NEW' )";
			params.add(query.toUpperCase());
			
			draftsEstimateNumberSearchList = getPersistenceService().findAllBy(strquery,params.toArray());
		}
		return DRAFT_ESTIMATE_NUMBER_SEARCH_RESULTS;
	}
	
	/*
	 * Autocomplete of Admin sanctioned Project codes for Cancel Estimate screen 
	 */
	public String searchProjectCodes(){
		if(!StringUtils.isEmpty(query))
		{
			String strquery="";
			ArrayList<Object> params=new ArrayList<Object>();
			strquery="select distinct(ae.projectCode.code) from AbstractEstimate ae where ae.parent is null and upper(ae.projectCode.code) like '%'||?||'%'"+
					" and ae.egwStatus.code=? and ae.projectCode.isActive=1";
			params.add(query.toUpperCase());
			params.add(AbstractEstimate.EstimateStatus.ADMIN_SANCTIONED.toString());
			projectCodeList=getPersistenceService().findAllBy(strquery,params.toArray());
		}	
		return PROJECT_CODE_SEARCH_RESULTS;
	}
	
	
	/** This method is to validate if the estimate number of the cancelled estimate has been already copied or not
	 * @return
	 */
	public String validateCancelledEstForCopy() {
		String estNo = estimateNum.substring(0, estimateNum.length()-2);
		String cancelledEst = (String) getPersistenceService().find("select est.estimateNumber from AbstractEstimate est where est.estimateNumber= ?",estNo);
		if(cancelledEst!=null) {
			isCancelEstCopyExists = true;
		}
		return "copyCancelledEst";
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

	public void setExecutingDepartment(Long executingDepartment) {
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

	public boolean getIsSkipDeptChange() {
		return isSkipDeptChange;
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public String getWpNumber() {
		return wpNumber;
	}

	public void setEgovCommon(EgovCommon egovCommon) {
		this.egovCommon = egovCommon;
	}

	public boolean getIsVoucherExists() {
		return isVoucherExists;
	}
	
	public List<String> getEstimateNumberSearchList() {
		return estimateNumberSearchList;
	}

	public List<String> getProjectCodeList() {
		return projectCodeList;
	}

	public List<String> getDraftsEstimateNumberSearchList() {
		return draftsEstimateNumberSearchList;
	}

	public void setDraftsEstimateNumberSearchList(
			List<String> draftsEstimateNumberSearchList) {
		this.draftsEstimateNumberSearchList = draftsEstimateNumberSearchList;
	}

	public String getEstimateNum() {
		return estimateNum;
	}

	public void setEstimateNum(String estimateNum) {
		this.estimateNum = estimateNum;
	}

	public boolean getIsCancelEstCopyExists() {
		return isCancelEstCopyExists;
	}

	public Integer getApproverDepartmentId() {
		return approverDepartmentId;
	}

	public void setApproverDepartmentId(Integer approverDepartmentId) {
		this.approverDepartmentId = approverDepartmentId;
	}

	public List<? extends Object> getApproverList() {
		return this.approverList;
	}

	public String getEmployeeCode() {
		return employeeCode;
	}

	public void setEmployeeCode(String employeeCode) {
		this.employeeCode = employeeCode;
	}

	public String getSorCodes() {
		return sorCodes;
	}

	public void setSorCodes(String sorCodes) {
		this.sorCodes = sorCodes;
	}
	public String getDwSORCheck() {
		return dwSORCheck;
	}

	public void setDwSORCheck(String dwSORCheck) {
		this.dwSORCheck = dwSORCheck;
	}
	
	public Long getEstimateTemplateId() {
		return estimateTemplateId;
	}

	public void setEstimateTemplateId(Long estimateTemplateId) {
		this.estimateTemplateId = estimateTemplateId;
	}
	
	public Long getWoId() {
		return woId;
	}

	public void setWoId(Long woId) {
		this.woId = woId;
	}

	public Long getEstimateId() {
		return estimateId;
	}
	
	public String getEstimateIds() {
		return estimateIds;
	}

	public void setEstimateIds(String estimateIds) {
		this.estimateIds = estimateIds;
	}

	public String getWoNumber() {
		return woNumber;
	}

	public void setWoNumber(String woNumber) {
		this.woNumber = woNumber;
	}

	public List<String> getEstimateNoList() {
		return estimateNoList;
	}

	public void setCommonsService(CommonsService commonsService) {
		this.commonsService = commonsService;
	}

	public BigDecimal getEstimateAmount() {
		return estimateAmount;
	}

	public void setEstimateAmount(BigDecimal estimateAmount) {
		this.estimateAmount = estimateAmount;
	}
	
}
