package org.egov.payroll.web.actions.common;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.ParentPackage ;
import org.egov.infstr.client.filter.EGOVThreadLocals;
import org.egov.infstr.services.PersistenceService;
import org.egov.lib.rjbac.user.ejb.api.UserService;
import org.egov.pims.commons.DesignationMaster;
import org.egov.pims.model.EmployeeView;
import org.egov.pims.model.PersonalInformation;
import org.egov.pims.service.EisUtilService;
import org.egov.pims.service.PersonalInformationService;
import org.egov.web.actions.BaseFormAction;

@ParentPackage("egov")  
public class EmployeeSearchAction extends BaseFormAction{
	private static final Logger LOGGER = Logger.getLogger(EmployeeSearchAction.class);
	private static final String SEARCH_RESULTS = "searchResults";
	private static final String APPROVEREMPLIST="approverEmpList";
	private static final String DESIGLISTFORHOD = "desigListForHod";
	private static final String APPROVERLISTFORHOD = "approverListForHod";
	private String code;
	private String accountId;
	private String vhDate;
	private String codeValues;
	private PersonalInformationService personalInformationService;
	private List<PersonalInformation> employeeList = new ArrayList<PersonalInformation>();
	private String query;
	private List<EmployeeView> approverEmployeeList = new ArrayList<EmployeeView>(); 
	private Integer approverDeptId;
	private Integer approverDesigId;
	private PersistenceService<EmployeeView, Integer> employeeVewService;
	private UserService userService;
	private String type;
	private List<DesignationMaster> desgnationList = new ArrayList<DesignationMaster>();
	private EisUtilService eisUtilService;
	private List<EmployeeView> approverList = new ArrayList<EmployeeView>();

	
	
	public EisUtilService getEisUtilService() {
		return eisUtilService;
	}
	public void setEisUtilService(EisUtilService eisUtilService) {
		this.eisUtilService = eisUtilService;
	}
	public List<DesignationMaster> getDesgnationList() {
		return desgnationList;
	}
	public void setDesgnationList(List<DesignationMaster> desgnationList) {
		this.desgnationList = desgnationList;
	}
	
	public UserService getUserService() {
		return userService;
	}
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	public Integer getApproverDeptId() {
		return approverDeptId;
	}
	public void setApproverDeptId(Integer approverDeptId) {
		this.approverDeptId = approverDeptId;
	}
	public Integer getApproverDesigId() {
		return approverDesigId;
	}
	public void setApproverDesigId(Integer approverDesigId) {
		this.approverDesigId = approverDesigId;
	}
	public String getCodeValues() {
		return codeValues;
	}
	public void setCodeValues(String codeValues) {
		this.codeValues = codeValues;
	}
	public String getVhDate() {
		return vhDate;
	}
	public void setVhDate(String vhDate) {
		this.vhDate = vhDate;
	}
	public String getAccountId() {
		return accountId;
	}
	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}

	public Object getModel() {
		return null;
	}
	
	public String getQuery() {
		return query;
	}
	public void setQuery(String query) {
		this.query = query;
	}
	

	/**
	 * this method gives employees based on current/last assignment
	 * @return
	 */
	public String getEmpListByEmpCodeLike() {


		if (StringUtils.isNotBlank(query)) {
			employeeList=getPersonalInformationService().getEmpListByUserLogin(Integer.valueOf(EGOVThreadLocals.getUserId()), query, 30);
		}
		
		return SEARCH_RESULTS;
	}
	/**
	 * this api gives employees based on current assignment
	 * @return
	 */
	public String getActiveEmpListByEmpCodeLike() {


		if (StringUtils.isNotBlank(query)) {
			employeeList=getPersonalInformationService().getActiveEmpListByUserLogin(Integer.valueOf(EGOVThreadLocals.getUserId()), query, 30);
		}
		
		return SEARCH_RESULTS;
	}
	
	 
	
	
	
	//Used for showing list of approver employee
	public String approverEmpList(){
		return APPROVEREMPLIST;
	}
	
	public List<EmployeeView> getApproverEmployeeList() {
		LOGGER.info("deptId------"+approverDeptId);
		LOGGER.info("desigId------"+approverDesigId);
		if(approverDeptId != null && approverDesigId != null){
			approverEmployeeList = employeeVewService.findAllBy("from EmployeeView where deptId.id=? and desigId.designationId=? and fromDate<=? and toDate>=? and isActive=1 and isPrimary='Y'",approverDeptId,approverDesigId,new Date(),new Date());
		}
		
		return approverEmployeeList;
	}
	
	public String getApproverDesignationListForHOD()
	{
		if(approverDeptId != null)
		{
 			desgnationList = getDesigListForHODByDeptId(approverDeptId);
		}
		return DESIGLISTFORHOD;
	}
	
	private List<DesignationMaster> getDesigListForHODByDeptId(Integer deptId)
	{
		
		return persistenceService.findAllBy("select distinct empHodDept.assignment.desigId from EmployeeDepartment empHodDept where empHodDept.hodept.id=?" , deptId);
	}
	
	public String getApproverListForHOD()
	{
		LOGGER.info("deptId------"+approverDeptId);
		LOGGER.info("desigId------"+approverDesigId);
		if((approverDeptId != null && approverDeptId!=-1) && (approverDesigId != null && approverDesigId!=-1)){
			approverList= eisUtilService.getHODListByDeptAndDesig(approverDeptId, approverDesigId);
					
		}
		
		return APPROVERLISTFORHOD;
	}
	
	public PersonalInformationService getPersonalInformationService() {  
		return personalInformationService;
	}
	public void setPersonalInformationService(
			PersonalInformationService personalInformationService) {
		this.personalInformationService = personalInformationService;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	//called by the intermediate Ajax[employeeSearch-searchResult.jsp] jsp to form ajax results
	public List<PersonalInformation> getEmployeeList() {
		return employeeList;
	}
	public void setEmployeeList(List<PersonalInformation> employeeList) {
		this.employeeList = employeeList;
	}

	
	public void setEmployeeVewService(
			PersistenceService<EmployeeView, Integer> employeeVewService) {
		this.employeeVewService = employeeVewService;
	}
	public List<EmployeeView> getApproverList() {
		return approverList;
	}
	public void setApproverList(List<EmployeeView> approverList) {
		this.approverList = approverList;
	}

	

}