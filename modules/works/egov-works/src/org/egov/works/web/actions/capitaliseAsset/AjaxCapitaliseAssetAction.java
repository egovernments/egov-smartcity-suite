package org.egov.works.web.actions.capitaliseAsset;


import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.commons.EgwStatus;
import org.egov.lib.admbndry.Boundary;
import org.egov.lib.admbndry.BoundaryDAO;
import org.egov.pims.model.Assignment;
import org.egov.pims.service.EmployeeService;
import org.egov.pims.service.PersonalInformationService;
import org.egov.web.actions.BaseFormAction;

public class AjaxCapitaliseAssetAction extends BaseFormAction{
	
	private static final Logger LOGGER = Logger.getLogger(AjaxCapitaliseAssetAction.class);
	private Long zoneId;	    // Set by Ajax call
	private List<Boundary> locationList = new LinkedList<Boundary>();
	private static final String USERS_IN_DEPT = "usersInDept";
	private static final String DESIGN_FOR_EMP = "designForEmp";
	public static final String WARDS = "wards";
	private EmployeeService employeeService;
	private Integer departmentId;
	private List usersInDepartment;
	private Assignment assignment;
	private Integer empID;
	private PersonalInformationService personalInformationService;
	private Integer projectTypeId;
    private List<EgwStatus> assetStatusList;
	public Object getModel() {
		return null;
	}
	
	public String execute(){
		return SUCCESS;
	}
	
	/**
	 * Populate the ward list by  zone
	 */
	public String populateWard(){
		try{	
			locationList = new BoundaryDAO().getChildBoundaries(String.valueOf(zoneId));
		}catch(Exception e){
			LOGGER.error("Error while loading warda - wards." + e.getMessage());
			addFieldError("location", "Unable to load ward information");
			throw new EGOVRuntimeException("Unable to load ward information",e);
		}
		return WARDS;
	}
	
	public String usersInDepartment() {
		try {
			HashMap<String,Object> criteriaParams =new HashMap<String,Object>();
			criteriaParams.put("departmentId", departmentId);
			criteriaParams.put("isPrimary", 'Y');
			if(departmentId==null || departmentId==-1)
				usersInDepartment=Collections.EMPTY_LIST;
			else
				usersInDepartment=personalInformationService.getListOfEmployeeViewBasedOnCriteria(criteriaParams, -1, -1);
			
		} catch (Exception e) {
			throw new EGOVRuntimeException("user.find.error", e);
		}
		return USERS_IN_DEPT;
	}
	
	public String designationForUser(){
		try {
			assignment = employeeService.getLatestAssignmentForEmployee(empID);
		} catch (Exception e) {
			throw new EGOVRuntimeException("user.find.error", e);
		}
		return DESIGN_FOR_EMP;
	}
	public String populateStatus(){
		
		if(Integer.valueOf(-1).equals(projectTypeId)){
			assetStatusList = (List<EgwStatus>)persistenceService.findAllBy("from EgwStatus st where st.moduletype='ASSET'order by description ");
		}else{
			assetStatusList = getStatusListByDescs(getStatus(projectTypeId));
		}
		
		return "status";
	}
	private String[] getStatus(Integer projectTypeId){
		
		String[] status ;
		status = projectTypeId.equals(Integer.valueOf(0))?new String[]{"Created","CWIP"}:new String[]{"Capitalized"} ;
		return status;
	}
	private List<EgwStatus> getStatusListByDescs(String[] statusDesc){
		
		StringBuffer sql = new StringBuffer(150);
		sql.append("from EgwStatus st where st.moduletype='ASSET'  and UPPER(st.description) in (");
		for (int i = 0; i < statusDesc.length; i++) {
			 sql.append("'" + statusDesc[i].trim().toUpperCase() + "'");
			 if(i<statusDesc.length-1 )sql.append(",");
		}
		
		sql.append(") order by description");
		String query = sql.toString();
		return  (List<EgwStatus>)persistenceService.findAllBy(query);
			 
		
	}
	public Long getZoneId() {
		return zoneId;
	}

	public void setZoneId(Long zoneId) {
		this.zoneId = zoneId;
	}

	public List<Boundary> getLocationList() {
		return locationList;
	}

	public void setEmployeeService(EmployeeService employeeService) {
		this.employeeService = employeeService;
	}

	public Integer getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(Integer departmentId) {
		this.departmentId = departmentId;
	}

	public List getUsersInDepartment() {
		return usersInDepartment;
	}

	public void setUsersInDepartment(List usersInDepartment) {
		this.usersInDepartment = usersInDepartment;
	}

	public Assignment getAssignment() {
		return assignment;
	}

	public void setAssignment(Assignment assignment) {
		this.assignment = assignment;
	}

	public Integer getEmpID() {
		return empID;
	}

	public void setEmpID(Integer empID) {
		this.empID = empID;
	}
	
	public void setPersonalInformationService(
			PersonalInformationService personalInformationService) {
		this.personalInformationService = personalInformationService;
	}

	public Integer getProjectTypeId() {
		return projectTypeId;
	}

	public void setProjectTypeId(Integer projectTypeId) {
		this.projectTypeId = projectTypeId;
	}

	public List<EgwStatus> getAssetStatusList() {
		return assetStatusList;
	}

	public void setAssetStatusList(List<EgwStatus> assetStatusList) {
		this.assetStatusList = assetStatusList;
	}
	
}
