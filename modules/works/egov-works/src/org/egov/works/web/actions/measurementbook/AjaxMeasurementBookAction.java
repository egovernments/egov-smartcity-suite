package org.egov.works.web.actions.measurementbook;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.exceptions.NoSuchObjectException;
import org.egov.infstr.models.Script;
import org.egov.infstr.services.ScriptService;
import org.egov.lib.rjbac.dept.Department;
import org.egov.pims.commons.DesignationMaster;
import org.egov.pims.commons.dao.DesignationMasterDAO;
import org.egov.pims.model.Assignment;
import org.egov.pims.model.EmployeeView;
import org.egov.pims.service.EisUtilService;
import org.egov.pims.service.EmployeeService;
import org.egov.pims.service.PersonalInformationService;
import org.egov.web.actions.BaseFormAction;
import org.egov.works.models.estimate.AbstractEstimate;
import org.egov.works.models.measurementbook.MBDetails;
import org.egov.works.models.measurementbook.MBHeader;
import org.egov.works.models.measurementbook.MBMeasurementSheet;
import org.egov.works.models.workorder.WorkOrder;
import org.egov.works.models.workorder.WorkOrderActivity;
import org.egov.works.models.workorder.WorkOrderMeasurementSheet;
import org.egov.works.services.AbstractEstimateService;
import org.egov.works.services.MeasurementBookService;
import org.egov.works.services.WorksService;

public class AjaxMeasurementBookAction extends BaseFormAction{
	private static final Logger logger = Logger.getLogger(AjaxMeasurementBookAction.class);
	private static final String USERS_IN_DEPT = "usersInDept";
	private static final String DESIGN_FOR_EMP = "designForEmp";
	private static final String WORK_ORDER_DETAILS = "workOrderDetails";
	private static final String ACTIVITY_DETAILS = "activityDetails";
	private static final String WORKFLOW_USER_LIST = "workflowUsers";
	private static final String WORKFLOW_DESIG_LIST = "workflowDesignations";
	private static final String MB_MSHEET_LIST = "mbMSheetDetails";
	private static final String CHANGEQTY_MSHEET_LIST = "changeQtyMSheetDetails";
	
	private MeasurementBookService measurementBookService;
	private EmployeeService employeeService;
	private Assignment assignment;
	private Integer empID;
	private Integer executingDepartment;
	private List usersInExecutingDepartment;
	private WorkOrder workOrder;
	private String workOrderNumber;
	private Long woActivityId;
	private Long mbHeaderId;
	private WorkOrderActivity workOrderActivity;
	private Double prevCulmEntry;
	private Double totalEstQuantity;	
	
	//-----------------------Manual Workflow ----------------------------
	private EisUtilService eisService;
	private Integer departmentId; 
	private Integer designationId; 
	private String scriptName;
	private String stateName;
	private Long modelId;
	private String modelType;
	private Integer wardId;
	private List workflowKDesigList;
	private List workflowUsers;
	private PersonalInformationService personalInformationService;
	private AbstractEstimateService abstractEstimateService;
	private WorksService worksService;
	private List<MBMeasurementSheet> mbMSheetDtls = new LinkedList<MBMeasurementSheet>();
	private List<WorkOrderActivity> woactivityList =  new LinkedList<WorkOrderActivity>();
	private String mbPercentagelevel;
	private boolean isTenderPercentageType;
	//-------------------------------------------------------------------
	private transient ScriptService scriptExecutionService;
	
	public Object getModel() {
		return null;
	}
	
	public String designationForUser(){
		try {
			assignment = employeeService.getLatestAssignmentForEmployee(empID);
		} catch (Exception e) {
			throw new EGOVRuntimeException("user.find.error", e);
		}
		return DESIGN_FOR_EMP;
	}
	
	public String usersInExecutingDepartment() {
		try {
			HashMap<String,Object> criteriaParams =new HashMap<String,Object>();
			criteriaParams.put("userId",Integer.toString(abstractEstimateService.getCurrentUserId()));
			criteriaParams.put("departmentId", executingDepartment.toString());
			//criteriaParams.put("isPrimary", "Y");		// Commented to show primary and secondary designations in Prepared By list
			usersInExecutingDepartment=eisService.getEmployeeInfoList(criteriaParams);
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
		} catch (Exception e) {
			throw new EGOVRuntimeException("user.find.error", e);
		}
		logger.info("Success ajax call to 'usersInExecutingDepartment' ----------------------------------------------------------");
		return USERS_IN_DEPT;
	}
	
	public String workOrderDetails() {
		try {
			workOrder = (WorkOrder) persistenceService.find("from WorkOrder where workOrderNumber=?",workOrderNumber);
			if(workOrder!=null){
				HashMap<String,Object> criteriaParams =new HashMap<String,Object>();
				criteriaParams.put("userId",Integer.toString(abstractEstimateService.getCurrentUserId()));
				//criteriaParams.put("isPrimary", "Y");		// Commented to show primary and secondary designations in Prepared By list
				usersInExecutingDepartment=eisService.getEmployeeInfoList(criteriaParams);
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
		} catch (Exception e) {
			throw new EGOVRuntimeException("workorder.find.error", e);
		}
		logger.info("Success ajax call to 'workOrderDetails' ----------------------------------------------------------");
		return WORK_ORDER_DETAILS;
	}
	
	public String activityDetails() {
		prevCulmEntry = null;
		try{
			workOrderActivity = (WorkOrderActivity) persistenceService.find("from WorkOrderActivity where id=?",woActivityId);	
			if(workOrderActivity.getActivity().getParent()==null) {
				prevCulmEntry = measurementBookService.prevCumulativeQuantity(woActivityId,mbHeaderId,workOrderActivity.getActivity().getId());
				totalEstQuantity=measurementBookService.totalEstimatedQuantity(woActivityId,mbHeaderId,workOrderActivity.getActivity().getId());
			}
			else {				
				prevCulmEntry = measurementBookService.prevCumulativeQuantity(woActivityId,mbHeaderId,workOrderActivity.getActivity().getParent().getId());
				totalEstQuantity=measurementBookService.totalEstimatedQuantity(woActivityId,mbHeaderId,workOrderActivity.getActivity().getParent().getId());				
			}
			if(totalEstQuantity==0)
				totalEstQuantity=workOrderActivity.getApprovedQuantity();
		} 
		catch (Exception e) {
			throw new EGOVRuntimeException("activity.find.error", e);
		}
		
		return ACTIVITY_DETAILS;
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
		MBHeader mbHeader = null;
		if(modelId!=null) {
			mbHeader = (MBHeader)getPersistenceService().find("from MBHeader where id=?", modelId);
		}
		Script validScript = (Script) persistenceService.findAllByNamedQuery(Script.BY_NAME,scriptName).get(0);
		
		List<String> list = (List<String>) scriptExecutionService.executeScript(validScript,ScriptService.createContext("state",stateName,"department",departmentName,"wfItem",mbHeader));
		
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
	
	public String getMbMSheetDetails() throws Exception {    
		mbMSheetDtls=new LinkedList<MBMeasurementSheet>(); 
		if(mbHeaderId!=null){
			 MBDetails mbDetails = (MBDetails)getPersistenceService().find("from MBDetails where workOrderActivity.id=? and mbHeader.id=?", woActivityId,mbHeaderId);
			 boolean isZeroQty;
			 if(mbDetails.getTotalEstQuantity()==0)
				 isZeroQty=true;
			 else 
				 isZeroQty=false;
			 if(mbDetails!=null){     // Block of code is executed for MB's from Drafts 
					 for(MBMeasurementSheet mbMeasurementSheet:mbDetails.getMbMeasurementSheetList()){
                       double prevQuantity=0;
                       double tempQty=1;
                       double currentQty;
                       List<MBMeasurementSheet> prevMBMsheetCurEntryList;
                       if(!isZeroQty)
                    	   prevMBMsheetCurEntryList=prevMBMsheetDetailsIncludingCurEntry(mbMeasurementSheet.getWoMeasurementSheet().getId(), mbHeaderId);
                       else
                    	   prevMBMsheetCurEntryList=prevMBMsheetDetailsIncludingCurEntryForZeroQty(mbMeasurementSheet.getMbDetails().getWorkOrderActivity().getId(), mbHeaderId);
                       
                       if(prevMBMsheetCurEntryList!=null){
                               for(MBMeasurementSheet prevMBMeasurementSheet:prevMBMsheetCurEntryList){
                            	   	 tempQty=prevMBMeasurementSheet.getQuantity();
  	                                 prevQuantity=prevQuantity+tempQty;
                               }
                       }
                       currentQty=mbMeasurementSheet.getQuantity();
                       mbMeasurementSheet.setCumulativeQuantity(prevQuantity+currentQty);
                       mbMSheetDtls.add(mbMeasurementSheet);
		    	   }
					 // Block of Code for adding Measurement Sheet from Work Order In case if it is not added in MB. 
					 boolean flag;
					 for(WorkOrderMeasurementSheet woMSheet:mbDetails.getWorkOrderActivity().getWoMeasurementSheetList()){
						 flag=false;
						 for(MBMeasurementSheet mbMeasurementSheet:mbDetails.getMbMeasurementSheetList()){
							 if(mbMeasurementSheet.getWoMeasurementSheet().getId().equals(woMSheet.getId())){
								 flag=true;
							 }	
						 }
						 if(!flag){
		                       double prevQuantity=0;
		                       double tempQty=1;
		                       List<MBMeasurementSheet> prevMBMsheetCurEntryList=prevMBMsheetDetailsIncludingCurEntry(woMSheet.getId(), mbHeaderId);
		                       if(prevMBMsheetCurEntryList!=null){ 
		                               for(MBMeasurementSheet prevMBMeasurementSheet:prevMBMsheetCurEntryList){
		                            	   tempQty=prevMBMeasurementSheet.getQuantity();
			                               prevQuantity=prevQuantity+tempQty;
		                                       
		                               }
		                       }
		                      MBMeasurementSheet mbMSheet1=new MBMeasurementSheet();
		                      mbMSheet1.setWoMeasurementSheet(woMSheet); 
		                      mbMSheet1.setCumulativeQuantity(prevQuantity);
		                      mbMSheetDtls.add(mbMSheet1);
						}
					 }
					// Block of Code for adding Revision Measurement Sheet from Revision Work Order In case if it is not added in MB.
					 AbstractEstimate parentEstimate=null;
					 workOrderActivity = (WorkOrderActivity)getPersistenceService().find("from WorkOrderActivity where id=?", woActivityId);
					  if(workOrderActivity!=null){
						if(workOrderActivity.getActivity().getAbstractEstimate().getParent()==null){
							parentEstimate=workOrderActivity.getActivity().getAbstractEstimate();
		       }
						else{
							parentEstimate=workOrderActivity.getActivity().getAbstractEstimate().getParent();
						}
						
						List<AbstractEstimate> abstractEstimateList=new LinkedList<AbstractEstimate>() ;
						abstractEstimateList.addAll(abstractEstimateService.findAllByNamedQuery("HAS_REVISION_ESTIMATES", parentEstimate.getId()));
						List<Long> estimateIds=new LinkedList<Long>();
						for(AbstractEstimate ae:abstractEstimateList){
							estimateIds.add(ae.getId());
						}
						if(!estimateIds.isEmpty())
							woactivityList=(List<WorkOrderActivity>) getPersistenceService().findAllByNamedQuery("getallWorkOrderActivityForRE",mbHeaderId,mbHeaderId,workOrderActivity.getActivity().getId() ,workOrderActivity.getActivity().getId(),estimateIds);
						for(WorkOrderActivity woa:woactivityList){ 
							woa.setParent(workOrderActivity);
							 for(WorkOrderMeasurementSheet woMSheet:woa.getWoMeasurementSheetList()){
								 flag=false;
								 for(MBMeasurementSheet mbMeasurementSheet:mbDetails.getMbMeasurementSheetList()){
									 if(mbMeasurementSheet.getWoMeasurementSheet().getId().equals(woMSheet.getId())){
										 flag=true;
									 }	
								 }
								 if(!flag){
				                       double prevQuantity=0;
				                       double tempQty=1;
				                       List<MBMeasurementSheet> prevMBMsheetCurEntryList=prevMBMsheetDetailsIncludingCurEntry(woMSheet.getId(), mbHeaderId);
				                       if(prevMBMsheetCurEntryList!=null){ 
				                               for(MBMeasurementSheet prevMBMeasurementSheet:prevMBMsheetCurEntryList){
				                            	   tempQty=prevMBMeasurementSheet.getQuantity();
					                               prevQuantity=prevQuantity+tempQty;
				                                       
				                               }
				                       }
				                      MBMeasurementSheet mbMSheet1=new MBMeasurementSheet();
				                      mbMSheet1.setWoMeasurementSheet(woMSheet); 
				                      mbMSheet1.setCumulativeQuantity(prevQuantity);
				                      mbMSheetDtls.add(mbMSheet1);
								}
							 }
						}
					}
		       }
			 else{   // Block of code is executed for MB's from Drafts / Rejected MB's - while adding new activity
				 workOrderActivity = (WorkOrderActivity)getPersistenceService().find("from WorkOrderActivity where id=?", woActivityId);
				 List<WorkOrderMeasurementSheet> workOrderMSheetDtls=workOrderActivity.getWoMeasurementSheetList();
				 for(WorkOrderMeasurementSheet measurementSheet:workOrderMSheetDtls){ 
	                 double prevQuantity=0;
	                 double tempQty=1;
	                 List<MBMeasurementSheet> prevMBMsheetCurEntryList=prevMBMsheetDetailsIncludingCurEntry(measurementSheet.getId(), mbHeaderId);
	                 if(prevMBMsheetCurEntryList!=null){
	                         for(MBMeasurementSheet prevMBMeasurementSheet:prevMBMsheetCurEntryList){
	                        	 tempQty=prevMBMeasurementSheet.getQuantity();
	                             prevQuantity=prevQuantity+tempQty;
	                         }
	                 }
	                 MBMeasurementSheet mbMeasurementSheet1=new MBMeasurementSheet();
	                 mbMeasurementSheet1.setWoMeasurementSheet(measurementSheet);
	                 mbMeasurementSheet1.setCumulativeQuantity(prevQuantity);
	                 mbMSheetDtls.add(mbMeasurementSheet1);
	          	   }
				  getMBMSheetForRE();
     		}
		}
		else{  //Block of code for MB creation (either first MB or later MB's)
			 workOrderActivity = (WorkOrderActivity)getPersistenceService().find("from WorkOrderActivity where id=?", woActivityId);
			 List<WorkOrderMeasurementSheet> workOrderMSheetDtls=workOrderActivity.getWoMeasurementSheetList();
		     for(WorkOrderMeasurementSheet measurementSheet:workOrderMSheetDtls){
                 double prevQuantity=0;
                 double tempQty=1;
                 Long mbId=-1l;
     			 if(mbHeaderId!=null)
     				mbId=mbHeaderId;                 	
                 
                 List<MBMeasurementSheet> prevMBMsheetList=prevMBMsheetDetails(mbId,measurementSheet.getId());
                 if(prevMBMsheetList!=null){
                         for(MBMeasurementSheet prevMBMeasurementSheet:prevMBMsheetList){ 
                        	 tempQty=prevMBMeasurementSheet.getQuantity();
                            prevQuantity=prevQuantity+tempQty;
                         }
                 }
                 MBMeasurementSheet mbMeasurementSheet=new MBMeasurementSheet();
                 mbMeasurementSheet.setWoMeasurementSheet(measurementSheet); 
                 mbMeasurementSheet.setCumulativeQuantity(prevQuantity);
                 mbMSheetDtls.add(mbMeasurementSheet); 
         		}		     
		     getMBMSheetForRE();
		}
		return MB_MSHEET_LIST;
	}
	
	//For adding Approved ChangeQuantity Revision Measurement Sheet In MBDetails.
	public void getMBMSheetForRE() throws Exception {  
		 AbstractEstimate parentEstimate=null;
		 workOrderActivity = (WorkOrderActivity)getPersistenceService().find("from WorkOrderActivity where id=?", woActivityId);
		  if(workOrderActivity!=null){
			if(workOrderActivity.getActivity().getAbstractEstimate().getParent()==null){
				parentEstimate=workOrderActivity.getActivity().getAbstractEstimate();
			}
			else{
				parentEstimate=workOrderActivity.getActivity().getAbstractEstimate().getParent();
			}
			
			List<AbstractEstimate> abstractEstimateList=new LinkedList<AbstractEstimate>() ;
			abstractEstimateList.addAll(abstractEstimateService.findAllByNamedQuery("HAS_REVISION_ESTIMATES", parentEstimate.getId()));
			List<Long> estimateIds=new LinkedList<Long>();
			for(AbstractEstimate ae:abstractEstimateList){
				estimateIds.add(ae.getId());
			}
			Long mbId=-1l;
			if(mbHeaderId!=null)
				mbId=mbHeaderId;
			if(!estimateIds.isEmpty())
				woactivityList=(List<WorkOrderActivity>) getPersistenceService().findAllByNamedQuery("getallWorkOrderActivityForRE",mbId,mbId,workOrderActivity.getActivity().getId() ,workOrderActivity.getActivity().getId(),estimateIds);
			
			for(WorkOrderActivity woa:woactivityList){ 
				woa.setParent(workOrderActivity);
				for(WorkOrderMeasurementSheet woMeasurementSheet:woa.getWoMeasurementSheetList()){
	              	 if(woMeasurementSheet!=null) {
			                double prevQuantity=0;
			                double tempQty=1;
			                List<MBMeasurementSheet> prevMBMsheetList=prevMBMsheetDetails(mbId,woMeasurementSheet.getId());
			                if(prevMBMsheetList!=null){
			                        for(MBMeasurementSheet prevMBMeasurementSheet:prevMBMsheetList){
			                        	tempQty=prevMBMeasurementSheet.getQuantity();
			                           prevQuantity=prevQuantity+tempQty;
			                        }
			                }
			                woMeasurementSheet.setCumulativeQuantity(prevQuantity);
	              	 }
	              	MBMeasurementSheet mbMSheet1=null;
         		    mbMSheet1=new MBMeasurementSheet();
                   mbMSheet1.setWoMeasurementSheet(woMeasurementSheet); 
                   mbMSheet1.setCumulativeQuantity(woMeasurementSheet.getCumulativeQuantity());				                     
	              	if(mbMSheet1!=null)
             			 mbMSheetDtls.add(mbMSheet1);  
				}
			}
		}
	}
	
	
	public String getMbChangeQtyMSheetDetails() throws Exception { 
		Long mbId=-1l;
		if(mbHeaderId!=null)
			mbId=mbHeaderId;
		mbMSheetDtls=new LinkedList<MBMeasurementSheet>();
		workOrderActivity = (WorkOrderActivity)getPersistenceService().find("from WorkOrderActivity where id=?", woActivityId); 
		AbstractEstimate parentEstimate=null;
		if(workOrderActivity!=null){ 
			if(workOrderActivity.getActivity().getAbstractEstimate().getParent()==null)
				parentEstimate=workOrderActivity.getActivity().getAbstractEstimate();
			else 
				parentEstimate=workOrderActivity.getActivity().getAbstractEstimate().getParent(); 
			List<AbstractEstimate> abstractEstimateList=new LinkedList<AbstractEstimate>() ;
			abstractEstimateList.add(parentEstimate);
			abstractEstimateList.addAll(abstractEstimateService.findAllByNamedQuery("HAS_REVISION_ESTIMATES", parentEstimate.getId()));
			List<Long> estimateIds=new LinkedList<Long>();
			for(AbstractEstimate ae:abstractEstimateList){
				estimateIds.add(ae.getId());
			}
			
			if(!estimateIds.isEmpty())
				woactivityList=(List<WorkOrderActivity>) getPersistenceService().findAllByNamedQuery("getallWorkOrderActivityForRE",mbId,mbId,workOrderActivity.getActivity().getId() ,workOrderActivity.getActivity().getId(),estimateIds);
			
			if(!woactivityList.contains(workOrderActivity))
				woactivityList.add(workOrderActivity);
		
			for(WorkOrderActivity woa:woactivityList){ 			
				woa.setParent(workOrderActivity);
			    int mSheetcount=1;
				for(WorkOrderMeasurementSheet woMeasurementSheet:woa.getWoMeasurementSheetList()){
					if(woMeasurementSheet!=null) {
							double prevQuantity=0;
							double tempQty=1;
							List<MBMeasurementSheet> prevMBMsheetList=prevMBMsheetDetails(mbId, woMeasurementSheet.getId());
							if(prevMBMsheetList!=null){
								for(MBMeasurementSheet prevMBMeasurementSheet:prevMBMsheetList){
									tempQty=prevMBMeasurementSheet.getQuantity();
									prevQuantity=prevQuantity+tempQty;
								}
							}
							woMeasurementSheet.setCumulativeQuantity(prevQuantity);
							woMeasurementSheet.getMeasurementSheet().setMbExtraItemSlNo(mSheetcount++);
					}
               }
			}
		}
		return CHANGEQTY_MSHEET_LIST; 
	}	
	
	
	public List<MBMeasurementSheet> prevMBMsheetDetailsIncludingCurEntry(Long mbMsheetId,Long mbHeaderId){
        Object[] params = new Object[]{mbHeaderId,mbHeaderId,mbMsheetId};
        return (List<MBMeasurementSheet>) persistenceService.findAllByNamedQuery("prevMBMsheetsIncludingCurEntry",params);
	}
	
	public List<MBMeasurementSheet> prevMBMsheetDetailsIncludingCurEntryForZeroQty(Long mbMsheetId,Long mbHeaderId){
        Object[] params = new Object[]{mbHeaderId,mbHeaderId,mbMsheetId}; 
        return (List<MBMeasurementSheet>) persistenceService.findAllByNamedQuery("prevMBMsheetsIncludingCurEntryForZeroQty",params);
	}
	
	public List<MBMeasurementSheet> prevMBMsheetDetails(Long mbHeaderId, Long mSheetId){
        Object[] params = new Object[]{mbHeaderId,mbHeaderId,mSheetId};
        return (List<MBMeasurementSheet>) persistenceService.findAllByNamedQuery("prevMBMsheets",params);
	}
	
	
	public void setEmployeeService(EmployeeService employeeService) {
		this.employeeService = employeeService;
	}
	
	public void setEmpID(Integer empID) {
		this.empID = empID;
	}
	
	public List getUsersInExecutingDepartment() {
		return usersInExecutingDepartment;
	}
	
	public void setExecutingDepartment(Integer executingDepartment) {
		this.executingDepartment = executingDepartment;
	}

	public Assignment getAssignment() {
		return assignment;
	}

	public WorkOrder getWorkOrder() {
		return workOrder;
	}

	public void setWorkOrderNumber(String workOrderNumber) {
		this.workOrderNumber = workOrderNumber;
	}

	public void setWoActivityId(Long woActivityId) {
		this.woActivityId = woActivityId;
	}

	public WorkOrderActivity getWorkOrderActivity() {
		return workOrderActivity;
	}

	public Double getPrevCulmEntry() {
		return prevCulmEntry;
	}
	
	public void setMbHeaderId(Long mbHeaderId) {
		this.mbHeaderId = mbHeaderId;
	}

	public void setMeasurementBookService(
			MeasurementBookService measurementBookService) {
		this.measurementBookService = measurementBookService;
	}

	public Integer getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(Integer departmentId) {
		this.departmentId = departmentId;
	}

	public Integer getDesignationId() {
		return designationId;
	}

	public void setDesignationId(Integer designationId) {
		this.designationId = designationId;
	}

	public String getScriptName() {
		return scriptName;
	}

	public void setScriptName(String scriptName) {
		this.scriptName = scriptName;
	}

	public String getStateName() {
		return stateName;
	}

	public void setStateName(String stateName) {
		this.stateName = stateName;
	}

	public Long getModelId() {
		return modelId;
	}

	public void setModelId(Long modelId) {
		this.modelId = modelId;
	}

	public String getModelType() {
		return modelType;
	}

	public void setModelType(String modelType) {
		this.modelType = modelType;
	}

	public Integer getWardId() {
		return wardId;
	}

	public void setWardId(Integer wardId) {
		this.wardId = wardId;
	}

	public void setEisService(EisUtilService eisService) {
		this.eisService = eisService;
	}

	public List getWorkflowKDesigList() {
		return workflowKDesigList;
	}

	public List getWorkflowUsers() {
		return workflowUsers;
	}
	
	public void setPersonalInformationService(
			PersonalInformationService personalInformationService) {
		this.personalInformationService = personalInformationService;
	}

	public void setWorksService(WorksService worksService) {
		this.worksService = worksService;
	}

	public List<MBMeasurementSheet> getMbMSheetDtls() {
		return mbMSheetDtls;
	}

	public void setMbMSheetDtls(List<MBMeasurementSheet> mbMSheetDtls) {
		this.mbMSheetDtls = mbMSheetDtls;
	}

	public void setAbstractEstimateService(
			AbstractEstimateService abstractEstimateService) {
		this.abstractEstimateService = abstractEstimateService;
	}

	public Double getTotalEstQuantity() {
		return totalEstQuantity;
	}

	public void setTotalEstQuantity(Double totalEstQuantity) {
		this.totalEstQuantity = totalEstQuantity;
	}

	public List<WorkOrderActivity> getWoactivityList() {
		return woactivityList;
	}

	public void setWoactivityList(List<WorkOrderActivity> woactivityList) {
		this.woactivityList = woactivityList;
	}

	public String getMbPercentagelevel() {
		return mbPercentagelevel;
	}

	public void setMbPercentagelevel(String mbPercentagelevel) {
		this.mbPercentagelevel = mbPercentagelevel;
	}

	public boolean getIsTenderPercentageType() {
		return isTenderPercentageType;
	}

	public void setIsTenderPercentageType(boolean isTenderPercentageType) {
		this.isTenderPercentageType = isTenderPercentageType;
	}

	public void setScriptExecutionService(ScriptService scriptExecutionService) {
		this.scriptExecutionService = scriptExecutionService;
	}
	
}  