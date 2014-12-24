package org.egov.tender.web.actions.common;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import org.egov.exceptions.EGOVRuntimeException;
import org.egov.pims.commons.DesignationMaster;
import org.egov.pims.model.EmployeeView;
import org.egov.pims.service.EmployeeService;
import org.egov.pims.service.EisUtilService;
import org.egov.tender.model.GenericTenderResponse;
import org.egov.tender.model.TenderableEntityGroup;
import org.egov.tender.services.common.TenderCommonService;
import org.egov.tender.services.tenderresponse.TenderResponseService;
import org.egov.web.actions.BaseFormAction;

@SuppressWarnings("serial")
public class AjaxCommonAction extends BaseFormAction{

	private List<DesignationMaster> designationList;
	private List<EmployeeView> approverList;
	private Integer designationId;
	private String approverDepartmentId=null;
	private static final String WF_DESIGNATIONS = "designations";
	private static final String  WF_APPROVERS = "approvers";
	
	private String paramDate=null;
	private final SimpleDateFormat DATEFORMAT = new SimpleDateFormat("dd/MM/yyyy",Locale.getDefault());
	private EisUtilService eisService;
	private EmployeeService empService;
	private TenderCommonService tenderCommonService;
	private TenderResponseService tenderResponseService;
	
	public Object getModel()
	{
		return null;
	}
	
	public EisUtilService getEisService() {
		return eisService;
	}

	public void setEisService(EisUtilService eisService) {
		this.eisService = eisService;
	}

	public EmployeeService getEmpService() {
		return empService;
	}

	public void setEmpService(EmployeeService empService) {
		this.empService = empService;
	}

	public void setTenderCommonService(TenderCommonService tenderCommonService) {
		this.tenderCommonService = tenderCommonService;
	}
	
	public List<DesignationMaster> getDesignationList() {
		return designationList;
	}

	public String getParamDate() {
		return paramDate;
	}

	public void setParamDate(String paramDate) {
		this.paramDate = paramDate;
	}

	public String getApproverDepartmentId() {
		return approverDepartmentId;
	}

	public void setApproverDepartmentId(String approverDepartmentId) {
		this.approverDepartmentId = approverDepartmentId;
	}

	public void setDesignationList(List<DesignationMaster> designationList) {
		this.designationList = designationList;
	}

	public List<EmployeeView> getApproverList() {
		return approverList;
	}

	public void setApproverList(List<EmployeeView> approverList) {
		this.approverList = approverList;
	}

	public Integer getDesignationId() {
		return designationId;
	}

	public void setDesignationId(Integer designationId) {
		this.designationId = designationId;
	}

	public String getPositionByPassingDesigId()
	{
		if(designationId!=null && designationId!=-1){

			HashMap<String,String> paramMap = new HashMap<String, String>();
			if(approverDepartmentId!=null && !approverDepartmentId.equals(""))
				paramMap.put("departmentId",approverDepartmentId);
			if(designationId!=null && !designationId.equals(""))			
				paramMap.put("designationId", designationId.toString());

			approverList = new ArrayList<EmployeeView>();

			List<EmployeeView> empList = eisService.getEmployeeInfoList(paramMap);
			for (EmployeeView emp : empList) {
				approverList.add(emp);

			}
			return WF_APPROVERS;	
		}

		return null;
	}
	public String getDesignationByDeptId() throws NumberFormatException, Exception{
		designationList = new ArrayList<DesignationMaster>();

		String searchDept = getApproverDepartmentId();
		Date date = (paramDate == null || paramDate.equals("") ? null : DATEFORMAT.parse(paramDate)  );

		if (date == null || date.equals("")) {
			date = new Date();
		}

		if (searchDept == null || "".equals(searchDept) || "-1".equals(searchDept)) {
			throw new EGOVRuntimeException("Department id is required.");
		} 
		else
			designationList = eisService.getAllDesignationByDept(Integer.parseInt(searchDept), date);

		return WF_DESIGNATIONS;
	}
	
	
	
	// For Populating Group number...
	private Long fileType;
	private String groupNumber;

	private List<TenderableEntityGroup> groupList=new ArrayList<TenderableEntityGroup>();
	private List<GenericTenderResponse> responseList=new ArrayList<GenericTenderResponse>();
	
	
	public String populateGroups()
	{
		populateGroupList();
		return "group";
	}
	
	public String populateResponse() 
	{
		responseList=tenderResponseService.getResponseListByReferenceNumber(groupNumber);
		return "response";
	}
	
	private void populateGroupList()
	{
		groupList=tenderCommonService.getGroupListByFileType(fileType);
	}

	public void setGroupNumber(String groupNumber) {
		this.groupNumber = groupNumber;
	}
	
	public void setFileType(Long fileType) {
		this.fileType = fileType;
	}

	public List<TenderableEntityGroup> getGroupList() {
		return groupList;
	}
	
	public List<GenericTenderResponse> getResponseList() {
		return responseList;
	}
	
	public void setTenderResponseService(TenderResponseService tenderResponseService) {
		this.tenderResponseService = tenderResponseService;
	}
		
}