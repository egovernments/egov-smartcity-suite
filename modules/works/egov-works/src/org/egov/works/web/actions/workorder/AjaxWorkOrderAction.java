package org.egov.works.web.actions.workorder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infstr.models.Script;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.services.ScriptService;
import org.egov.pims.commons.DesignationMaster;
import org.egov.pims.model.EmployeeView;
import org.egov.pims.service.EisUtilService;
import org.egov.pims.service.PersonalInformationService;
import org.egov.tender.model.TenderResponseLine;
import org.egov.web.actions.BaseFormAction;
import org.egov.works.models.estimate.AbstractEstimate;
import org.egov.works.models.estimate.Activity;
import org.egov.works.models.estimate.MeasurementSheet;
import org.egov.works.models.measurementbook.MBHeader;
import org.egov.works.models.tender.GenericTenderResponseActivity;
import org.egov.works.services.MeasurementBookService;
import org.egov.works.services.WorkOrderService;

public class AjaxWorkOrderAction extends BaseFormAction{

	private String departmentName;
	private Long desgId;
	private static final String WORKORDER_DESIG_LIST = "workOrderDesignations";
	private static final String WORKORDER_USER_LIST = "workOrderUsers";
	private List<DesignationMaster> workOrderDesigList=new ArrayList<DesignationMaster>();
	private EisUtilService eisService;
	private String traIds;
	private List<GenericTenderResponseActivity> tenderResponseActivitylist=new ArrayList<GenericTenderResponseActivity>();
	//private PersistenceService<TenderResponseActivity, Long> tenderResponseActivityService;
	private Integer executingDepartment;
	private PersonalInformationService personalInformationService;
	private List userList;
	private Long activityId;
	private static final String WORKORDER_ESTIMATE_MSHEETLIST="woEstimateMsheet";
	private List<MeasurementSheet> measurementSheet=new LinkedList<MeasurementSheet>();
	private PersistenceService<Activity, Long> activityService;
	private WorkOrderService workOrderService;
	private MeasurementBookService measurementBookService;
	List <MBHeader> approvedMBList = new ArrayList<MBHeader>();; 
	private Long workOrderId;
	private transient ScriptService scriptExecutionService;
	
	public Object getModel() {
		return null;
	}

	public  String getEstimateMSheetDetails(){
		Activity activity=activityService.findById(activityId, false);
		measurementSheet.addAll(activity.getMeasurementSheetList());
		return WORKORDER_ESTIMATE_MSHEETLIST;
	}

	public  String getDesignationByDeptId() {
		List<Script> scriptList = persistenceService.findAllByNamedQuery(Script.BY_NAME,"workOrder.Designation.ByDepartment");
		if(!scriptList.isEmpty() && StringUtils.isNotBlank(departmentName)){
			List<String> list = (List<String>) scriptExecutionService.executeScript(scriptList.get(0), ScriptService.createContext("department",departmentName));
			workOrderDesigList.addAll(getPersistenceService().findAllByNamedQuery("getDesignationForListOfDesgNames", list));
		}
		return WORKORDER_DESIG_LIST;
	}
	
	public String getUsersForDesg() {
		try {
			HashMap<String,Object> criteriaParams =new HashMap<String,Object>();
			criteriaParams.put("designationId", desgId.intValue());
			criteriaParams.put("departmentId", executingDepartment);
			if(executingDepartment==null || executingDepartment==-1)
				userList=Collections.EMPTY_LIST;
			else{  
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
			}
		} catch (Exception e) {
			throw new EGOVRuntimeException("user.find.error", e);
		}
		return WORKORDER_USER_LIST;
	}
	

	public String getTenderResponseActivityList(){
		if(StringUtils.isNotBlank(traIds)){
			Set<Long> traIdentifierSet = new HashSet<Long>();
			Map<Long,Double> traMap=new HashMap<Long,Double>();
			String[] values = traIds.split("\\^");//To split the data (For Eg:1^2) 
			Long[] traIdLong=new Long[values.length];
			int j=0;
			for(int i=0;i<values.length;i++){
				if(StringUtils.isNotBlank(values[i])){
					traIdLong[j] = Long.valueOf(values[i].split("~")[0].trim());//selected Activity Id
					traMap.put(traIdLong[j], Double.valueOf(values[i].split("~")[1]));//selected Activity 's UnAssigned Qty
					j++;
				}
			}
			traIdentifierSet.addAll(Arrays.asList(traIdLong));
			List<TenderResponseLine> tempList=new ArrayList<TenderResponseLine>();
			tempList=(List<TenderResponseLine>)persistenceService.findAllByNamedQuery("getTenderResponseLineByIds", traIdentifierSet);
			AbstractEstimate estimate=null;
			if(tempList.size()>0){
				estimate=(AbstractEstimate)persistenceService.find("from AbstractEstimate where estimateNumber=?",tempList.get(0).getTenderableEntity().getTenderableEntityGroup().getNumber());
			}
			for(TenderResponseLine trl:tempList){
				GenericTenderResponseActivity genericTRA=new GenericTenderResponseActivity();
				Activity activity=workOrderService.getActivityFromTenderResponseLineAndEstimate(trl, estimate);
				genericTRA.setActivity(activity);
				genericTRA.setEstimatedQty(activity.getQuantity().doubleValue());
				genericTRA.setGenericTenderResponse(trl.getTenderResponse());
				genericTRA.setNegotiatedQuantity(trl.getQuantityByUom().doubleValue());
				genericTRA.setNegotiatedRate(trl.getBidRateByUom().doubleValue());
				genericTRA.setTenderResponseLine(trl);
				Double unAssignedQuantity=traMap.get(trl.getId());
				genericTRA.setNegotiatedQuantity(unAssignedQuantity);//Temperorily setting UnAssigned Qty instead of Negotiated Qty
				tenderResponseActivitylist.add(genericTRA);
		}
		}
		return "tenderResponseActivities";
	}
		
	public String getApprovedMBsForWorkOrder() {		
		approvedMBList = measurementBookService.findAllBy(" from MBHeader where workOrder.id=? and state.previous.value<>'CANCELLED'", workOrderId);
		return "approvedMBs";
	}
	
	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	public List<DesignationMaster> getWorkOrderDesigList() {
		return workOrderDesigList;
	}

	public void setWorkOrderDesigList(List<DesignationMaster> workOrderDesigList) {
		this.workOrderDesigList = workOrderDesigList;
	}

	public void setDesgId(Long desgId) {
		this.desgId = desgId;
	}

	public void setEisService(EisUtilService eisService) {
		this.eisService = eisService;
	}

	public String getTraIds() {
		return traIds;
	}

	public void setTraIds(String traIds) {
		this.traIds = traIds;
	}

	public List<GenericTenderResponseActivity> getTenderResponseActivitylist() {
		return tenderResponseActivitylist;
	}

	public void setTenderResponseActivitylist(
			List<GenericTenderResponseActivity> tenderResponseActivitylist) {
		this.tenderResponseActivitylist = tenderResponseActivitylist;
	}

/*	public void setTenderResponseActivityService(
			PersistenceService<TenderResponseActivity, Long> tenderResponseActivityService) {
		this.tenderResponseActivityService = tenderResponseActivityService;
	}
*/
	public Integer getExecutingDepartment() {
		return executingDepartment;
	}

	public void setExecutingDepartment(Integer executingDepartment) {
		this.executingDepartment = executingDepartment;
	}

	public void setPersonalInformationService(
			PersonalInformationService personalInformationService) {
		this.personalInformationService = personalInformationService;
	}
	
	public List getUserList(){
		return userList;
	}
	public void setUserList(List userList) {
		this.userList = userList;
	}

	public Long getActivityId() {
		return activityId;
}

	public void setActivityId(Long activityId) {
		this.activityId = activityId;
	}

	public List<MeasurementSheet> getMeasurementSheet() {
		return measurementSheet;
	}

	public void setMeasurementSheet(List<MeasurementSheet> measurementSheet) {
		this.measurementSheet = measurementSheet;
	}

	public void setActivityService(
			PersistenceService<Activity, Long> activityService) {
		this.activityService = activityService;
	}

	public void setWorkOrderService(WorkOrderService workOrderService) {
		this.workOrderService = workOrderService;
	}
	
	public Long getWorkOrderId() {
		return workOrderId;
	}

	public void setWorkOrderId(Long workOrderId) {
		this.workOrderId = workOrderId;
	}

	public void setMeasurementBookService(
			MeasurementBookService measurementBookService) {
		this.measurementBookService = measurementBookService;
	}

	public List<MBHeader> getApprovedMBList() {
		return approvedMBList;
	}

	public void setApprovedMBList(List<MBHeader> approvedMBList) {
		this.approvedMBList = approvedMBList;
	}

	public void setScriptExecutionService(ScriptService scriptExecutionService) {
		this.scriptExecutionService = scriptExecutionService;
	}

}
