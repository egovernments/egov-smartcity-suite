package org.egov.works.web.actions.qualityControl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.egov.exceptions.EGOVRuntimeException;
import org.egov.pims.model.EmployeeView;
import org.egov.pims.service.PersonalInformationService;
import org.egov.web.actions.BaseFormAction;
import org.egov.works.models.qualityControl.SampleLetterHeader;
import org.egov.works.models.qualityControl.TestSheetHeader;
import org.egov.works.models.workorder.WorkOrder;
import org.egov.works.services.WorkOrderService;
import org.egov.works.services.qualityControl.SampleLetterService;
import org.egov.works.services.qualityControl.TestSheetHeaderService;

public class AjaxSampleLetterAction  extends BaseFormAction{
	
	private static final String USER_LIST = "sampleCollectedBy";
	private Integer executingDepartment;
	private Long desgId; 
	private List userList;
	private PersonalInformationService personalInformationService;
	private static final String TESTSHEET_LIST = "testSheet";
	private static final String WORKORDER_LIST = "workOrder";
	private static final String SAMPLELETTER_LIST = "sampleLetter";
	private List<TestSheetHeader> testSheetList= new ArrayList<TestSheetHeader>();
	private List<WorkOrder> workOrderList= new ArrayList<WorkOrder>();
	private List<SampleLetterHeader> sampleLetterList= new ArrayList<SampleLetterHeader>();
	private TestSheetHeaderService testSheetHeaderService;
	private String query;
	private WorkOrderService workOrderService;
	private SampleLetterService sampleLetterService;
	private String mode;
	private String type;
	
	@Override
	public Object getModel() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	public String getUsersForDesg() {
		try {
			HashMap<String,Object> criteriaParams =new HashMap<String,Object>();
			criteriaParams.put("designationId", desgId.intValue());
			if(executingDepartment != null)
				criteriaParams.put("departmentId", executingDepartment); 
			
			userList=personalInformationService.getListOfEmployeeViewBasedOnCriteria(criteriaParams, -1, -1);
			List<EmployeeView> finalList= new ArrayList<EmployeeView>();
			EmployeeView mainEmpViewObj,prevEmpView = new EmployeeView(); 
    		Iterator iterator = userList.iterator(); 
    		while(iterator.hasNext())
    		{
    			mainEmpViewObj=(EmployeeView)iterator.next();
    			if(!((mainEmpViewObj.getId().equals(prevEmpView.getId())) && (mainEmpViewObj.getDesigId().equals(prevEmpView.getDesigId())))){
    				finalList.add(mainEmpViewObj); 
    			}
    			prevEmpView=mainEmpViewObj;
    		}
    		userList=Collections.EMPTY_LIST;
    		userList=finalList;
		} catch (Exception e) {
			throw new EGOVRuntimeException("user.find.error", e);
		}
		return USER_LIST;
	}
	
	public String searchTestSheetNumberAjax(){
		if(type!=null && type.equalsIgnoreCase("searchSLForCreateJob")) 
			testSheetList= testSheetHeaderService.findAllBy(" select distinct(slh.testSheetHeader) from  SampleLetterHeader slh where slh.egwStatus.code='APPROVED' and slh.id not in (select sampleLetterHeader.id from  JobHeader where egwStatus.code!='CANCELLED') and upper(slh.testSheetHeader.testSheetNumber) like '%' || ? || '%'", query.toUpperCase());
		else
			testSheetList= testSheetHeaderService.findAllBy(" select distinct(slh.testSheetHeader) from  SampleLetterHeader slh where slh.egwStatus.code!='NEW' and  upper(slh.testSheetHeader.testSheetNumber) like '%' || ? || '%'", query.toUpperCase());
		return TESTSHEET_LIST;
	}
	
	
	public String searchWONumberAjax(){
		if(type!=null && type.equalsIgnoreCase("searchSLForCreateJob")) 
			workOrderList= workOrderService.findAllBy(" select distinct(slh.testSheetHeader.workOrder) from  SampleLetterHeader slh where slh.egwStatus.code='APPROVED' and slh.id not in (select sampleLetterHeader.id from  JobHeader where egwStatus.code!='CANCELLED') and  upper(slh.testSheetHeader.workOrder.workOrderNumber) like '%' || ? || '%'", query.toUpperCase());
		else
			workOrderList= workOrderService.findAllBy(" select distinct(slh.testSheetHeader.workOrder) from  SampleLetterHeader slh where slh.egwStatus.code!='NEW' and  upper(slh.testSheetHeader.workOrder.workOrderNumber) like '%' || ? || '%'", query.toUpperCase());
		return WORKORDER_LIST;
	}
	
	public String searchSampleLetterNumberAjax(){ 
		mode="sampleLetter";
		if(type!=null && type.equalsIgnoreCase("searchSLForCreateJob"))
			sampleLetterList= sampleLetterService.findAllBy("select slh from  SampleLetterHeader slh where slh.egwStatus.code='APPROVED' and slh.id not in (select sampleLetterHeader.id from  JobHeader where egwStatus.code!='CANCELLED') and  upper(slh.sampleLetterNumber) like '%' || ? || '%'", query.toUpperCase());
		else
			sampleLetterList= sampleLetterService.findAllBy(" from  SampleLetterHeader where egwStatus.code!='NEW' and  upper(sampleLetterNumber) like '%' || ? || '%'", query.toUpperCase());
		return SAMPLELETTER_LIST;
	} 
	
	public String searchCoveringLetterNumberAjax(){
		mode="coveringLetter";
		if(type!=null && type.equalsIgnoreCase("searchSLForCreateJob"))
			sampleLetterList= sampleLetterService.findAllBy("select slh from  SampleLetterHeader slh where slh.egwStatus.code='APPROVED' and slh.id not in (select sampleLetterHeader.id from  JobHeader where egwStatus.code!='CANCELLED') and  upper(slh.coveringLetterNumber) like '%' || ? || '%'", query.toUpperCase());
		else
			sampleLetterList= sampleLetterService.findAllBy(" from  SampleLetterHeader where egwStatus.code!='NEW' and  upper(coveringLetterNumber) like '%' || ? || '%'", query.toUpperCase());
		return SAMPLELETTER_LIST;
	}
	

	public Integer getExecutingDepartment() {
		return executingDepartment;
	}


	public void setExecutingDepartment(Integer executingDepartment) {
		this.executingDepartment = executingDepartment;
	}


	public Long getDesgId() {
		return desgId;
	}


	public void setDesgId(Long desgId) {
		this.desgId = desgId;
	}


	public List getUserList() {
		return userList;
	}


	public void setUserList(List userList) {
		this.userList = userList;
	}


	public void setPersonalInformationService(
			PersonalInformationService personalInformationService) {
		this.personalInformationService = personalInformationService;
	}


	public List<TestSheetHeader> getTestSheetList() {
		return testSheetList;
	}


	public void setTestSheetList(List<TestSheetHeader> testSheetList) {
		this.testSheetList = testSheetList;
	}


	public void setTestSheetHeaderService(
			TestSheetHeaderService testSheetHeaderService) {
		this.testSheetHeaderService = testSheetHeaderService;
	}


	public String getQuery() {
		return query;
	}


	public void setQuery(String query) {
		this.query = query;
	}


	public List<WorkOrder> getWorkOrderList() {
		return workOrderList;
	}


	public void setWorkOrderList(List<WorkOrder> workOrderList) {
		this.workOrderList = workOrderList;
	}


	public void setWorkOrderService(WorkOrderService workOrderService) {
		this.workOrderService = workOrderService;
	}


	public void setSampleLetterService(SampleLetterService sampleLetterService) {
		this.sampleLetterService = sampleLetterService;
	}


	public String getMode() {
		return mode;
	}


	public void setMode(String mode) {
		this.mode = mode;
	}


	public List<SampleLetterHeader> getSampleLetterList() {
		return sampleLetterList;
	}


	public void setSampleLetterList(List<SampleLetterHeader> sampleLetterList) {
		this.sampleLetterList = sampleLetterList;
	}


	public String getType() {
		return type;
	}


	public void setType(String type) {
		this.type = type;
	}



}