/**
 * 
 */
package org.egov.works.web.actions.revisionEstimate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.egov.commons.service.CommonsService;
import org.egov.infstr.ValidationError;
import org.egov.infstr.ValidationException;
import org.egov.infstr.config.AppConfigValues;
import org.egov.infstr.models.Money;
import org.egov.infstr.models.StateAware;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.workflow.WorkflowService;
import org.egov.lib.rjbac.dept.DepartmentImpl;
import org.egov.pims.commons.Position;
import org.egov.pims.service.EmployeeService;
import org.egov.web.actions.workflow.GenericWorkFlowAction;
import org.egov.works.models.estimate.AbstractEstimate;
import org.egov.works.models.estimate.Activity;
import org.egov.works.models.estimate.MeasurementSheet;
import org.egov.works.models.estimate.NonSor;
import org.egov.works.models.estimate.OverheadValue;
import org.egov.works.models.masters.Overhead;
import org.egov.works.models.masters.ScheduleOfRate;
import org.egov.works.models.revisionEstimate.RevisionAbstractEstimate;
import org.egov.works.models.revisionEstimate.RevisionType;
import org.egov.works.models.revisionEstimate.RevisionWorkOrder;
import org.egov.works.models.workorder.WorkOrder;
import org.egov.works.models.workorder.WorkOrderActivity;
import org.egov.works.models.workorder.WorkOrderEstimate;
import org.egov.works.models.workorder.WorkOrderMeasurementSheet;
import org.egov.works.services.AbstractEstimateService;
import org.egov.works.services.MeasurementBookService;
import org.egov.works.services.WorkOrderService;
import org.egov.works.services.WorksService;
import org.egov.works.services.impl.WorkOrderServiceImpl;
import org.egov.works.web.actions.estimate.AjaxEstimateAction;

/**
 * @author manoranjan
 *
 */
@ParentPackage("egov")
public class RevisionEstimateAction extends GenericWorkFlowAction {

	private static final Logger LOGGER = Logger.getLogger(RevisionEstimateAction.class);
	private static final long serialVersionUID = 1L;
	private RevisionAbstractEstimate revisionEstimate = new RevisionAbstractEstimate();
	private RevisionWorkOrder revisionWorkOrder = new RevisionWorkOrder();

	private WorkflowService<RevisionAbstractEstimate> workflowService;
	private PersistenceService<RevisionAbstractEstimate,Long> revisionAbstractEstimateService;
	private PersistenceService<RevisionWorkOrder,Long> revisionWorkOrderService;
	
		
	private AbstractEstimateService abstractEstimateService;
	private String messageKey;
	private WorksService worksService;
	private String employeeName;
	private String designation;
	private String additionalRuleValue;
	private String departmentName;
	private Long revEstimateId;
	private EmployeeService employeeService;
	private Integer approverPositionId;
	private CommonsService commonsService;
	private WorkOrderService workOrderService;
	private MeasurementBookService measurementBookService;
	private List<Activity> originalRevisedActivityList=new LinkedList<Activity>();
	private double originalTotalAmount=0;
	private double originalTotalTax=0;
	private double originalWorkValueIncludingTax=0;
	private boolean isSpillOverWorks;
	
	private String sourcepage="";
	private Long originalEstimateId; 
	private Long originalWOId;
	private double revisionEstimatesValue=0;
	private double revisionWOValue=0;
	List<RevisionAbstractEstimate> reList=new LinkedList<RevisionAbstractEstimate>() ;
	private AbstractEstimate abstractEstimate = new AbstractEstimate();
	private WorkOrder workOrder = new WorkOrder();

	private List<OverheadValue> actionOverheadValues = new LinkedList<OverheadValue>();
	
	private List<Activity> sorActivities = new LinkedList<Activity>();
	private List<Activity> nonSorActivities = new LinkedList<Activity>(); 
	private static final String CANCEL_ACTION = "cancel";                                                                                                                                              
	private static final String SAVE_ACTION = "save";                                                                                                                                                  
	private static final Object REJECT_ACTION = "reject"; 
	private static final String SOURCE_SEARCH = "search"; 
	private static final String SOURCE_INBOX = "inbox";
	private static final String ACTION_NAME="actionName";
	private PersistenceService<NonSor,Long> nonSorService;
	
	private static final String ACTIVITY_SEARCH = "activitySearch";	
	private String 	workorderNo;
	private String 	activityCode;
	private String  activityDesc;
	private List<WorkOrderActivity> activityList; // for search page
	private String estimateValue;
	private int reCount=1;  
	
	private List<WorkOrderActivity> changeQuantityActivities = new LinkedList<WorkOrderActivity>();
	
	public List<MeasurementSheet> measurementSheetList = new LinkedList<MeasurementSheet>();
	private String estimateUOM; 
	
	public Integer getApproverPositionId() {
		return approverPositionId;
	}

	public void setApproverPositionId(Integer approverPositionId) { 
		this.approverPositionId = approverPositionId;
	}
	
	
	public RevisionEstimateAction(){		
		addRelatedEntity("executingDepartment", DepartmentImpl.class);
	}

	@Override
	public StateAware getModel() {
		return revisionEstimate;
	}
	
	public String view(){
		LOGGER.debug("RevisionEstimateAction | view | Start"); 
		return NEW;
	}
	
	public String newform(){
		LOGGER.debug("RevisionEstimateAction | view | Start"); 
		return NEW;
	}
	
	public void prepare(){

		AjaxEstimateAction ajaxEstimateAction = new AjaxEstimateAction();
		ajaxEstimateAction.setPersistenceService(getPersistenceService());
		
		if (revEstimateId != null) {                                                                                                                                                                   
			 revisionEstimate= revisionAbstractEstimateService.findById(revEstimateId, false);    
			 revisionEstimate = revisionAbstractEstimateService.merge(revisionEstimate);
			 originalEstimateId=revisionEstimate.getParent().getId(); 
			 
			 abstractEstimate =(AbstractEstimate) persistenceService.find(" from AbstractEstimate where id=?",originalEstimateId);
				reList=revisionAbstractEstimateService.findAllByNamedQuery("HAS_REVISION_ESTIMATES", abstractEstimate.getId());
			 
			 WorkOrderEstimate revWorkOrderEstimate = null;
			 if("CANCELLED".equals(revisionEstimate.getEgwStatus().getCode()))
				 revWorkOrderEstimate=(WorkOrderEstimate) persistenceService.find(" from WorkOrderEstimate where estimate.id=? and workOrder.egwStatus.code='CANCELLED'",revEstimateId);
			 else
				 revWorkOrderEstimate=(WorkOrderEstimate) persistenceService.find(" from WorkOrderEstimate where estimate.id=? and workOrder.egwStatus.code!='CANCELLED'",revEstimateId); 
			 originalWOId=revWorkOrderEstimate.getWorkOrder().getParent().getId();  
			 revisionWorkOrder= revisionWorkOrderService.findById(revWorkOrderEstimate.getWorkOrder().getId(), false); 
			 
			 workOrderService.calculateCumulativeDetailsForRE(revWorkOrderEstimate);
			 revisionWorkOrder.getWorkOrderEstimates().get(0).setWorkOrderActivities(revWorkOrderEstimate.getWorkOrderActivities());			 
			 
		} 
		else {
			abstractEstimate =(AbstractEstimate) persistenceService.find(" from AbstractEstimate where id=?",originalEstimateId);
			reList=revisionAbstractEstimateService.findAllByNamedQuery("HAS_REVISION_ESTIMATES", abstractEstimate.getId());
		}
		                                                                          
		workOrder=(WorkOrder) persistenceService.find(" from WorkOrder where id=?",originalWOId);   
		revisionWOValue=workOrderService.getRevisionEstimateWOAmount(workOrder).doubleValue();
		
		double amnt=0;
		if(reList!=null && !reList.isEmpty()){ 
			for(RevisionAbstractEstimate re : reList){
				if(re.getId()!=revEstimateId)
					amnt+=re.getTotalAmount().getValue();
			}
			amnt+=abstractEstimate.getTotalAmount().getValue();
		}
		else{
			amnt+=abstractEstimate.getTotalAmount().getValue();
		}
		revisionEstimatesValue=amnt; 
		
		if(abstractEstimate!=null){ 
			List<AbstractEstimate> originalRevisedEstimateList=new LinkedList<AbstractEstimate>();
			originalRevisedEstimateList.add(abstractEstimate);
			originalRevisedEstimateList.addAll(reList);
			for(AbstractEstimate ae:originalRevisedEstimateList){
				if(ae.getId()!=revEstimateId){
				originalRevisedActivityList.addAll(ae.getActivities());
				originalTotalAmount=originalTotalAmount+ae.getWorkValue().getValue();
				originalTotalTax=originalTotalTax+ae.getTotalTax().getValue();
				originalWorkValueIncludingTax=originalWorkValueIncludingTax+ae.getWorkValueIncludingTaxes().getValue();
				}
			}
		} 
		 
		departmentName=abstractEstimate.getExecutingDepartment().getDeptName();
		super.prepare();
		setupDropdownDataExcluding("");
		addDropdownData("uomList", getPersistenceService().findAllBy("from EgUom order by upper(uom)"));
		addDropdownData("scheduleCategoryList", getPersistenceService().findAllBy("from ScheduleCategory order by upper(code)"));
		
		if(abstractEstimate!=null){
			if(abstractEstimate.getIsSpillOverWorks()){
				isSpillOverWorks=true;
			}else{
				isSpillOverWorks=false;
			}
		}
		
		if((SOURCE_INBOX.equals(getSourcepage()) || SOURCE_SEARCH.equals(getSourcepage()) || revEstimateId!=null) && measurementSheetList.isEmpty()){ 
			List<Activity> activityList =revisionEstimate.getActivities();
			Iterator iterator=activityList.iterator();
			Activity activity;
			int count=1;
            while(iterator.hasNext()){
                    activity=(Activity) iterator.next();
                    if(activity.getNonSor()!=null)
                            activity.setSrlNo(count++);
                    measurementSheetList.addAll(activity.getMeasurementSheetList());
            }
		}

		populateOverheadsList(ajaxEstimateAction);
	}
	
	public String save() { 
		revisionEstimate.setIsBudgetCheckRequired(budgetCheckRequired());                                                                                                                              
		String actionName = parameters.get("actionName")[0];                                                                                                                                    
		revisionEstimate.setApproverPositionId(approverPositionId);                                                                                                                                    
		                                                                                                                                                                                               
		String deptName=getWorkFlowDepartment();                                                                                                                                                       
		String curStatus;                                                                                                                                                                              
		if(revisionEstimate.getCurrentState()!=null){                                                                                                                                                  
			curStatus=revisionEstimate.getCurrentState().getValue();                                                                                                                                   
		}                                                                                                                                                                                              
		else                                                                                                                                                                                           
			curStatus="NEW";                                                                                                                                                                           
	    if(revisionWorkOrder!=null)
	    	revisionWorkOrderService.delete(revisionWorkOrder);
		
		saveREstimate(actionName);
		revisionEstimate.setAdditionalWfRule(getAdditionalRule());  
		
		if((actionName.equalsIgnoreCase("Forward")||actionName.equalsIgnoreCase("Approve")) && customizedWorkFlowService.getWfMatrix(getModel().getStateType(), deptName, getAmountRule(), getAdditionalRule(),curStatus, getPendingActions())==null
				&& !revisionEstimate.getParent().getIsSpillOverWorks()){                                                                                                                               
			throw new ValidationException(Arrays.asList(new ValidationError("revisionEstimate.workflow.notdefined",getText("revisionEstimate.workflow.notdefined",new String[]{deptName}))));          
		}                                                                                                                                                                                              
				
		if(!(SAVE_ACTION.equalsIgnoreCase(actionName)||CANCEL_ACTION.equalsIgnoreCase(actionName)||REJECT_ACTION.equals(actionName)) && revisionEstimate.getActivities().isEmpty()){                   
			throw new ValidationException(Arrays.asList(new ValidationError("revisionEstimate.activities.empty","revisionEstimate.activities.empty")));		                                           
		}       
		
		revisionEstimate=revisionAbstractEstimateService.persist(revisionEstimate);		
				                                                                                                                                                                                               
		if(revisionEstimate.getParent().getIsSpillOverWorks()){                                                                                                                                        
			revisionEstimate.setAdditionalWfRule("spillOverWorks");                                                                                                                                    
		}                                                                                                                                                                                              
		                                                                                                                                                                                               
		if(actionName.equalsIgnoreCase("save")){                                                                                                                                                       
			revisionEstimate.setEgwStatus(commonsService.getStatusByModuleAndCode("AbstractEstimate","NEW")); 
			if(revEstimateId ==null){                                                                                                                                                                  
				Position pos = employeeService.getPositionforEmp(employeeService.getEmpForUserId(abstractEstimateService.getCurrentUserId()).getIdPersonalInformation());                                            
				revisionEstimate = (RevisionAbstractEstimate) workflowService.start(revisionEstimate, pos, "Revision Estimate created.");                                                                  
			}                                                                                                                                     
			addActionMessage(getText(messageKey,"The Revision Estimate was saved successfully"));                                                                                                      
			revisionEstimate = revisionAbstractEstimateService.persist(revisionEstimate);                                                                                                              
		}                                                                                                                                                                                              
		else{                                                                                                                                                                                          
			if(revEstimateId ==null){                                                                                                                                                                  
				revisionEstimate.setEgwStatus(commonsService.getStatusByModuleAndCode("AbstractEstimate","NEW"));                                                                                          
				Position pos = employeeService.getPositionforEmp(employeeService.getEmpForUserId(abstractEstimateService.getCurrentUserId()).getIdPersonalInformation());                                        
				revisionEstimate = (RevisionAbstractEstimate) workflowService.start(revisionEstimate, pos, "Revision Estimate created.");                                                              
				revisionEstimate = revisionAbstractEstimateService.persist(revisionEstimate);                                                                                                          
			}     
			workflowService.transition(actionName, revisionEstimate, approverComments);                                                                                                                 
			revisionEstimate = revisionAbstractEstimateService.persist(revisionEstimate);                                                                                                              
		}     
		
		revisionWorkOrder = createRevisionWO();
		getPersistenceService().getSession().flush();
		getPersistenceService().getSession().refresh(revisionEstimate);
		revisionWorkOrder=revisionWorkOrderService.persist(revisionWorkOrder);
		
		messageKey="revisionEstimate."+actionName;                                                                                                                                                      
		getDesignation(revisionEstimate);                                                                                                                                                              
		workOrderService.calculateCumulativeDetails(revisionWorkOrder.getWorkOrderEstimates().get(0));                                                                                                                                                           
		return SAVE_ACTION.equalsIgnoreCase(actionName)?EDIT:SUCCESS;  
	}
	
	private void saveREstimate(String actionName) {
		revisionEstimate.getOverheadValues().clear(); 
		revisionEstimate.getActivities().clear();
		createRevisionEstimate();                                                                                                                                                                                                                                                                                                          
	} 
	
	protected void createRevisionEstimate() {
		List<AbstractEstimate> revisionEstimates=abstractEstimateService.findAllBy("from AbstractEstimate where parent.id=?", originalEstimateId);
		//int reCount=1;
		reCount=reCount+revisionEstimates.size();
		if(revisionEstimate.getId()!=null) {
			reCount=reCount-1;
		}
				
		revisionEstimate.setParent(abstractEstimate);
		revisionEstimate.setEstimateDate(new Date());
		revisionEstimate.setEstimateNumber(abstractEstimate.getEstimateNumber()+"/RE".concat(Integer.toString(reCount)));
		revisionEstimate.setName("Revision Estimate for: "+abstractEstimate.getName());
		revisionEstimate.setDescription("Revision Estimate for: "+abstractEstimate.getDescription());
		revisionEstimate.setNecessity(abstractEstimate.getNecessity()); 
		revisionEstimate.setScopeOfWork(abstractEstimate.getScopeOfWork()); 
		revisionEstimate.setType(abstractEstimate.getType());
		revisionEstimate.setExecutingDepartment(abstractEstimate.getExecutingDepartment());
		revisionEstimate.setEstimatePreparedBy(abstractEstimate.getEstimatePreparedBy());
		revisionEstimate.setWard(abstractEstimate.getWard());
		revisionEstimate.setFundSource(abstractEstimate.getFundSource());
		  
		populateSorActivities(revisionEstimate);
		populateNonSorActivities(revisionEstimate);
		populateActivities(revisionEstimate); 
		populateChangeQuantityItems(); 
		populateOverheads();
		AjaxEstimateAction ajaxEstimateAction = new AjaxEstimateAction();
		ajaxEstimateAction.setPersistenceService(getPersistenceService());
		populateOverheadsList(ajaxEstimateAction);
	}
	
	protected RevisionWorkOrder createRevisionWO() {		
		RevisionWorkOrder revisionWO=new RevisionWorkOrder();
		revisionWO.setParent(workOrder);
		revisionWO.setWorkOrderDate(new Date());
		revisionWO.setWorkOrderNumber(workOrder.getWorkOrderNumber()+"/RW".concat(Integer.toString(reCount)));
		revisionWO.setContractor(workOrder.getContractor());
		revisionWO.setWorkOrderPreparedBy(workOrder.getWorkOrderPreparedBy());
		revisionWO.setEngineerIncharge(workOrder.getEngineerIncharge());

		revisionWO.setEgwStatus(commonsService.getStatusByModuleAndCode("WorkOrder","NEW"));
		
		if(parameters.get(ACTION_NAME)[0].equalsIgnoreCase("Approve")) {
			Double tempREValue= ((10*revisionEstimatesValue)/100);
			Double tempRWOValue= ((10*revisionWOValue)/100);
			if((revisionEstimate.getTotalAmount().getValue() > tempREValue) || (revisionEstimate.getTotalAmount().getValue() > tempRWOValue))
				revisionWO.setEgwStatus(commonsService.getStatusByModuleAndCode("WorkOrder","NEW"));
			else
				revisionWO.setEgwStatus(commonsService.getStatusByModuleAndCode("WorkOrder","APPROVED"));
		}	
		
		if(parameters.get(ACTION_NAME)[0].equalsIgnoreCase("Cancel"))	
			revisionWO.setEgwStatus(commonsService.getStatusByModuleAndCode("WorkOrder","CANCELLED"));
		 
		
		populateWorkOrderActivities(revisionWO);
		return revisionWO;
	}
		
	protected void populateWorkOrderActivities(RevisionWorkOrder revisionWO) {
		WorkOrderEstimate workOrderEstimate = new WorkOrderEstimate();
		workOrderEstimate.setEstimate(revisionEstimate);
		workOrderEstimate.setWorkOrder(revisionWO);
		addWorkOrderEstimateActivities(workOrderEstimate,revisionWO);
		revisionWO.addWorkOrderEstimate(workOrderEstimate);
	}
 
	 private void addWorkOrderEstimateActivities(WorkOrderEstimate workOrderEstimate,RevisionWorkOrder revisionWO) {
		double woTotalAmount=0;
		for (Activity activity : revisionEstimate.getActivities()) {
			WorkOrderActivity workOrderActivity=new WorkOrderActivity();
			workOrderActivity.setActivity(activity); 
			double approvedAmount=0;
			if(workOrderActivity.getActivity().getRevisionType().equals(RevisionType.EXTRA_ITEM)) {
			workOrderActivity.setApprovedRate(activity.getRate().getValue()*activity.getConversionFactor());
			workOrderActivity.setApprovedQuantity(activity.getQuantity());			
				approvedAmount=new Money(workOrderActivity.getApprovedRate() * workOrderActivity.getApprovedQuantity() * activity.getConversionFactor()).getValue();
			}
			else{
				workOrderActivity.setApprovedRate(activity.getRate().getValue());
				workOrderActivity.setApprovedQuantity(activity.getQuantity());			
				approvedAmount=new Money(workOrderActivity.getApprovedRate() * workOrderActivity.getApprovedQuantity()).getValue();	
			}
			if(workOrderActivity.getActivity().getRevisionType().equals(RevisionType.REDUCED_QUANTITY))
				woTotalAmount=woTotalAmount-approvedAmount;
			else
			woTotalAmount=woTotalAmount+approvedAmount;
			workOrderActivity.setApprovedAmount(approvedAmount);
			workOrderActivity.setWorkOrderEstimate(workOrderEstimate);
			for(MeasurementSheet measurementSheet: activity.getMeasurementSheetList()){
				WorkOrderMeasurementSheet woMsheet=new WorkOrderMeasurementSheet();
				woMsheet.setNo(measurementSheet.getNo());
				woMsheet.setLength(measurementSheet.getUomLength());
				woMsheet.setWidth(measurementSheet.getWidth());
				woMsheet.setDepthOrHeight(measurementSheet.getDepthOrHeight());
				woMsheet.setQuantity(measurementSheet.getQuantity()); 
				woMsheet.setMeasurementSheet(measurementSheet);
				woMsheet.setWoActivity(workOrderActivity);
				workOrderActivity.addWoMeasurementSheet(woMsheet);
			}			
			workOrderEstimate.addWorkOrderActivity(workOrderActivity);
		}
		workOrderEstimate.getWorkOrder().setWorkOrderAmount(woTotalAmount);
	}
	
	protected void populateChangeQuantityItems() {
		for(WorkOrderActivity woa: changeQuantityActivities) {
			 if(woa!=null) {
				 WorkOrderActivity parentWOA=(WorkOrderActivity)getPersistenceService().find("from WorkOrderActivity where id=?", woa.getId());
				 woa.getActivity().setAbstractEstimate(revisionEstimate); 
				 if("-".equals(woa.getActivity().getSignValue()))
					 woa.getActivity().setRevisionType(RevisionType.REDUCED_QUANTITY);
				 else
					 woa.getActivity().setRevisionType(RevisionType.ADDITITONAL_QUANTITY);
				 woa.getActivity().setParent(parentWOA.getActivity());
				 woa.getActivity().setUom(parentWOA.getActivity().getUom());
				 woa.getActivity().setRate(woa.getActivity().getRate());
				 if(parentWOA.getActivity().getNonSor()==null)
					 woa.getActivity().setSchedule(parentWOA.getActivity().getSchedule());
				 else
					 woa.getActivity().setNonSor(parentWOA.getActivity().getNonSor());
				 
				 
				/* if(!changeQuantityMSheetList.isEmpty()){
					for (WorkOrderMeasurementSheet woms: changeQuantityMSheetList) { 
					   	 if(woms!=null) {
					   	
					   		 if(woms.getWoActivity()!=null && woms.getWoActivity().getId()!=null &&  woms.getWoActivity().getId().equals(woa.getId()) && woms.getMeasurementSheet().getId()==null){
					   			woms.getWoActivity().setParent(parentWOA);
					   			 woms.getMeasurementSheet().setActivity(woa.getActivity());
					   			 woa.getActivity().addMeasurementSheet(woms.getMeasurementSheet());
					   		 }
						}
				 }
				 }*/
			 revisionEstimate.addActivity(woa.getActivity());
			 }
		 }
		 
	 }
	
	protected void populateSorActivities(AbstractEstimate abstractEstimate) {
		 for(Activity activity: sorActivities) {
			 if (validSorActivity(activity)) {
				 activity.setSchedule((ScheduleOfRate) getPersistenceService().find("from ScheduleOfRate where id = ?", activity.getSchedule().getId()));
				 activity.setUom(activity.getSchedule().getUom());
				 abstractEstimate.addActivity(activity);
			 }
		 }
	 }
	
	 protected boolean validSorActivity(Activity activity) {
		 if (activity != null && activity.getSchedule() != null && activity.getSchedule().getId() != null) {
			 return true;
		 }
		 
		 return false;
	 }
	
	protected void populateNonSorActivities(AbstractEstimate abstractEstimate) {
		 for (Activity activity: nonSorActivities) {
			 if (activity!=null) {
				 activity.setUom(activity.getNonSor().getUom());
				 if(activity.getNonSor().getId()!=null && activity.getNonSor().getId()!=0 && activity.getNonSor().getId()!=1) {
					NonSor nonsor=(NonSor)getPersistenceService().find("from NonSor where id = ?", activity.getNonSor().getId());
					if(nonsor==null) {//In case of error on save of estimate, the nonsor is not yet persisted .
						activity.getNonSor().setId(null);//To clear the id which got created through previous persist
						 nonSorService.persist(activity.getNonSor()); 
                    }
					else{
						 activity.setNonSor(nonsor);
					}
					
				 }
				 else{
					 if(activity.getNonSor()!=null) {
						 nonSorService.persist(activity.getNonSor()); 
					 }
				 }			 
				 
				 abstractEstimate.addActivity(activity);
			 }
		 }
	 }
		
	 private void populateActivities(AbstractEstimate abstractEstimate) { 		       
		 int count=1;
		 for(Activity activity: abstractEstimate.getActivities()) { 
			 activity.setAbstractEstimate(abstractEstimate);
			 activity.setRevisionType(RevisionType.EXTRA_ITEM);
			if(!measurementSheetList.isEmpty()){
				 for (MeasurementSheet ms: measurementSheetList) { 
					   	 if (ms!=null) {
							if(((ms.getActivity().getSchedule()!=null && ms.getActivity().getSchedule().getId() !=null 
									 && activity.getSchedule()!=null && activity.getSchedule().getId()!=null && 
									 activity.getSchedule().getId().equals(ms.getActivity().getSchedule().getId())))) {
										 ms.setActivity(activity);
										 activity.addMeasurementSheet(ms);
							 }
							 else if( ms.getActivity()!=null  && ms.getSlNo()!=null && activity!=null && activity.getSchedule()==null){
										 if(activity.getSrlNo().equals(ms.getSlNo())) {
											 ms.setActivity(activity);
											 activity.addMeasurementSheet(ms);
										 }								 
								 } 
							}
				 } 
			 }

			 if(activity.getSrlNo()!=null) {				 
                activity.setSrlNo(count++);
			 }
		 }
		 
	 }

	
	public boolean budgetCheckRequired(){
		boolean budgetCheck=false;
		List<AppConfigValues> list =worksService.getAppConfigValue("EGF","budgetCheckRequired");
		if(!list.isEmpty())
		{
			String value = list.get(0).getValue();
			if(value.equalsIgnoreCase("Y"))
			{
				budgetCheck=true;      
			}
		}
		return budgetCheck;
	}
		
	public String getWorkFlowDepartment() {                                                                           
		//AbstractEstimate originalEst=(AbstractEstimate)persistenceService.find(" from AbstractEstimate where id="+abstractEstimate.getId());                                                         
		//revisionEstimate.setExecutingDepartment(abstractEstimate.getExecutingDepartment());		                                                                                                   
		return abstractEstimate==null?"":abstractEstimate.getIsSpillOverWorks()?"":abstractEstimate.getExecutingDepartment()==null?"":abstractEstimate.getExecutingDepartment().getDeptName(); 
	}
	
	public void getDesignation(RevisionAbstractEstimate revisionEstimate){
		/* start for customizing workflow message display */
		if(revisionEstimate.getCurrentState()!= null 
				&& !"NEW".equalsIgnoreCase(revisionEstimate.getCurrentState().getValue())) {
			String result = worksService.getEmpNameDesignation(revisionEstimate.getState().getOwner(),revisionEstimate.getState().getCreatedDate());
			if(result != null && !"@".equalsIgnoreCase(result)) {
				String empName = result.substring(0,result.lastIndexOf('@'));
				String designation =result.substring(result.lastIndexOf('@')+1,result.length());
				setEmployeeName(empName);
				setDesignation(designation);
			}
		}
		/* end */	
	}

	protected String getAdditionalRule() { 
		if(revisionEstimate!=null) {                                                                   
			if(abstractEstimate!=null && abstractEstimate.getIsSpillOverWorks()){                                                                                                                      
				additionalRuleValue="spillOverWorks";                                                                                                                                                  
			}else{                                                                                                                                                                                     
				int extraItemCount=0,additionItemCount=0;                                                                                                                                              
				String revisionType="";                                                                                                                                                                
				if(!revisionEstimate.getActivities().isEmpty()){                                                                                                                                       
					for(Activity act:revisionEstimate.getActivities()){                                                                                                                                
						if(act.getRevisionType().equals(RevisionType.EXTRA_ITEM))                                                                                                                      
							extraItemCount++;                                                                                                                                                          
						else                                                                                                                                                                           
							additionItemCount++;                                                                                                                                                       
					}                                                                                                                                                                                  
					if(extraItemCount!=0 && revisionEstimate.getActivities().size()==extraItemCount)                                                                                                   
						revisionType="ExtraItem";                                                                                                                                                      
					else if(additionItemCount!=0 && revisionEstimate.getActivities().size()==additionItemCount)                                                                                        
						revisionType="AdditionalItem";                                                                                                                                                 
					else if(revisionEstimate.getActivities().size()<=(extraItemCount+additionItemCount))                                                                                               
						revisionType="ExtraItem";                                                                                                                                                      
				}                                                                                                                                                                                      
				                                                                                                                                                                                       
				                                                                                                                                                                                       
				AbstractEstimate originalEstimate=revisionEstimate.getParent();                                                                                                                        
				//BigDecimal mbAmount = measurementBookService.getRevisionEstimateMBAmount(originalEstimate);                                                                                          
				//MBHeader mbHeader= measurementBookService.getRevisionEstimateMB(revisionEstimate);                                                                                                   
				//BigDecimal woAmount=workOrderService.getRevisionEstimateWOAmount(mbHeader.getWorkOrder());                                                                                           
				BigDecimal reAmount = new BigDecimal(revisionEstimate.getTotalAmount().getValue());                                                                                                    
				BigDecimal woAmount=workOrderService.getRevisionEstimateWOAmount(workOrder);                                                                                                           
				if(revisionType.equals("ExtraItem")){                                                                                                                                                  
					if((reAmount.compareTo(woAmount))==1){   // Total cost exceeding WO amount                                                                                                         
			additionalRuleValue="moreExtraItemCost";                                                                                                                                                   
					}                                                                                                                                                                                  
					else if((reAmount.compareTo(woAmount)) == -1){   // Total cost not exceeding WO amount                                                                                             
						additionalRuleValue="lessExtraItemCost";                                                                                                                                       
					}                                                                                                                                                                                  
				}                                                                                                                                                                                      
				else if(revisionType.equals("AdditionalItem")){                                                                                                                                        
					                                                                                                                                                                                   
					List<AbstractEstimate> revisionEstimates=new LinkedList<AbstractEstimate>();                                                                                                       
					revisionEstimates= abstractEstimateService.findAllByNamedQuery("HAS_REVISION_ESTIMATES", originalEstimate.getId());                                                                
					revisionEstimates.add(originalEstimate);                                                                                                                                           
					                                                                                                                                                                                   
					if(!revisionEstimate.getActivities().isEmpty()){                                                                                                                                   
						for(Activity act:revisionEstimate.getActivities()){                                                                                                                            
							if(act.getParent()!=null && act.getRevisionType().equals(RevisionType.ADDITITONAL_QUANTITY)) {                                                                             
								if(isRevisionEstimateQtyGreater(act,revisionEstimates)){                                                                                                               
									if((reAmount.compareTo(woAmount))==1){                                                                                                                             
										additionalRuleValue="moreAdditionalItemCost";                                                                                                                  
										break;                                                                                                                                                         
									}                                                                                                                                                                  
									else if((reAmount.compareTo(woAmount)) == -1){                                                                                                                     
										additionalRuleValue="lessAdditionalItemCost";                                                                                                                  
										break;                                                                                                                                                         
									}                                                                                                                                                                  
								}                                                                                                                                                                      
							}                                                                                                                                                                          
									}                                                                                                                                                                  
									}                                                                                                                                                                  
								}                                                                                                                                                                      
			}                                                                                                                                                                                          
		}                                                                                                                                                                                              
		return revisionEstimate==null?"":additionalRuleValue;  
	}
	public boolean isRevisionEstimateQtyGreater(Activity activity,List<AbstractEstimate> revisionEstimates){
		Double sumOfQty=0.0;
		for(AbstractEstimate ae:revisionEstimates){
			if(!ae.getActivities().isEmpty()){
				for(Activity act:ae.getActivities()){
					if(act.getId().equals(activity.getId()) || (activity.getParent()!=null && act.getId().equals(activity.getParent().getId()))){
						
						if(activity.getParent()==null || (activity.getParent()!=null && !activity.getRevisionType().equals(RevisionType.REDUCED_QUANTITY)))
							sumOfQty+=act.getQuantity();
						else
							sumOfQty-=act.getQuantity();
						}
					
					}
						}
					}
		Double tempSumOfQty= ((25*sumOfQty)/100);
		if(activity.getQuantity()>tempSumOfQty)
			return true;
		else
			return false;
	}

protected void populateOverheads() {
		 for(OverheadValue overheadValue: actionOverheadValues) {
			 if (validOverhead(overheadValue)) {
				 overheadValue.setOverhead((Overhead) getPersistenceService().find("from Overhead where id = ?", overheadValue.getOverhead().getId()));
				 overheadValue.setAbstractEstimate(revisionEstimate);
				 revisionEstimate.addOverheadValue(overheadValue);
			}
		 }
	 }
	 
	 protected boolean validOverhead(OverheadValue overheadValue) {
		 if (overheadValue != null && overheadValue.getOverhead() != null && overheadValue.getOverhead().getId() != null && overheadValue.getOverhead().getId()!=-1 && overheadValue.getOverhead().getId()!=0) {
			 return true;
		 }
		 return false;
	 }
	protected void populateOverheadsList(AjaxEstimateAction ajaxEstimateAction) {
		ajaxEstimateAction.revisionOverheads();
		addDropdownData("overheadsList", ajaxEstimateAction.getOverheadsnew());
	}
	
	public String loadSearchForActivity(){ 
		 return ACTIVITY_SEARCH;
	 }
	 
	 public String searchActivitiesForRE(){
		Map<String, Object> criteriaMap = new HashMap<String, Object>();
		if(originalWOId != null)
			criteriaMap.put(WorkOrderServiceImpl.WORKORDER_ID, originalWOId);
		if(originalEstimateId != null)
			criteriaMap.put(WorkOrderServiceImpl.WORKORDER_ESTIMATE_ID, originalEstimateId);
		if(activityCode != null && !"".equalsIgnoreCase(activityCode))
			criteriaMap.put(WorkOrderServiceImpl.ACTIVITY_CODE, activityCode);
		if(activityDesc != null && !"".equalsIgnoreCase(activityDesc))
			criteriaMap.put(WorkOrderServiceImpl.ACTIVITY_DESC, activityDesc);
		
		List<WorkOrderActivity> tempActivityList = workOrderService.searchWOActivities(criteriaMap);
		activityList=new ArrayList<WorkOrderActivity>();
		 for(WorkOrderActivity woa: tempActivityList) {
			 if(woa.getActivity().getParent()==null)
				 activityList.add(woa);
		 }
		
		return ACTIVITY_SEARCH;
	 }
	
	public AbstractEstimate getAbstractEstimate() {
		return abstractEstimate;
	}

	public RevisionAbstractEstimate getRevisionEstimate() {
		return revisionEstimate;
	}

	public void setRevisionEstimate(RevisionAbstractEstimate revisionEstimate) {
		this.revisionEstimate = revisionEstimate;
	}
	@Override
	protected String getPendingActions() {
		
		return getModel().getState()!=null?getModel().getState().getNextAction():"";
	}
	
	public void setRevisionEstimateWorkflowService(WorkflowService<RevisionAbstractEstimate> workflowService) {
		this.workflowService = workflowService;
	}

	public String getMessageKey() {
		return messageKey;
	}

	public void setWorksService(WorksService worksService) {
		this.worksService = worksService;
	}

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}

	public String getAdditionalRuleValue() {
		additionalRuleValue=getAdditionalRule();
		return additionalRuleValue;
	}

	public void setAdditionalRuleValue(String additionalRuleValue) {
		this.additionalRuleValue = additionalRuleValue;
	}
	
	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	public Long getRevEstimateId() {
		return revEstimateId;
	}

	public void setRevEstimateId(Long revEstimateId) {
		this.revEstimateId = revEstimateId;
	}

	public void setEmployeeService(EmployeeService employeeService) {
		this.employeeService = employeeService;
	}

	public AbstractEstimateService getAbstractEstimateService() {
		return abstractEstimateService;
	}

	public void setAbstractEstimateService(
			AbstractEstimateService abstractEstimateService) {
		this.abstractEstimateService = abstractEstimateService;
	}

	public void setRevisionAbstractEstimateService(
			PersistenceService<RevisionAbstractEstimate, Long> revisionAbstractEstimateService) {
		this.revisionAbstractEstimateService = revisionAbstractEstimateService;
	}

	public void setCommonsService(CommonsService commonsService) {
		this.commonsService = commonsService;
	}

	public void setWorkOrderService(WorkOrderService workOrderService) {
		this.workOrderService = workOrderService;
	}	

	public WorkOrderService getWorkOrderService() {
		return workOrderService;
	}

	public MeasurementBookService getMeasurementBookService() {
		return measurementBookService;
	}

	public void setMeasurementBookService(
			MeasurementBookService measurementBookService) {
		this.measurementBookService = measurementBookService;
	}

	public List<Activity> getOriginalRevisedActivityList() {
		return originalRevisedActivityList;
	}

	public void setOriginalRevisedActivityList(
			List<Activity> originalRevisedActivityList) {
		this.originalRevisedActivityList = originalRevisedActivityList;
	}

	public double getOriginalTotalAmount() {
		return originalTotalAmount;
	}

	public void setOriginalTotalAmount(double originalTotalAmount) {
		this.originalTotalAmount = originalTotalAmount;
	}

	public double getOriginalTotalTax() {
		return originalTotalTax;
	}

	public void setOriginalTotalTax(double originalTotalTax) {
		this.originalTotalTax = originalTotalTax;
	}

	public double getOriginalWorkValueIncludingTax() {
		return originalWorkValueIncludingTax;
	}

	public void setOriginalWorkValueIncludingTax(
			double originalWorkValueIncludingTax) {
		this.originalWorkValueIncludingTax = originalWorkValueIncludingTax;
	}

	public boolean isSpillOverWorks() {
		return isSpillOverWorks;
	}

	public void setSpillOverWorks(boolean isSpillOverWorks) {
		this.isSpillOverWorks = isSpillOverWorks;
	}

	public String getSourcepage() {
		return sourcepage;
	}

	public void setSourcepage(String sourcepage) {
		this.sourcepage = sourcepage;
	}

	public Long getOriginalEstimateId() {
		return originalEstimateId;
	}

	public void setOriginalEstimateId(Long originalEstimateId) {
		this.originalEstimateId = originalEstimateId;
	}

	public double getRevisionEstimatesValue() {
		return revisionEstimatesValue;
	}

	public void setRevisionEstimatesValue(double revisionEstimatesValue) {
		this.revisionEstimatesValue = revisionEstimatesValue;
	}

	public double getRevisionWOValue() {
		return revisionWOValue;
	}

	public void setRevisionWOValue(double revisionWOValue) {
		this.revisionWOValue = revisionWOValue;
	}

	public List<RevisionAbstractEstimate> getReList() {
		return reList;
	}

	public void setReList(List<RevisionAbstractEstimate> reList) {
		this.reList = reList;
	}

	public void setAbstractEstimate(AbstractEstimate abstractEstimate) {
		this.abstractEstimate = abstractEstimate;
	}

	public WorkOrder getWorkOrder() {
		return workOrder;
	}

	public void setWorkOrder(WorkOrder workOrder) {
		this.workOrder = workOrder;
	}

	public Long getOriginalWOId() {
		return originalWOId;
	}

	public void setOriginalWOId(Long originalWOId) {
		this.originalWOId = originalWOId;
	}

	public List<OverheadValue> getActionOverheadValues() {
		return actionOverheadValues;
	}

	public void setActionOverheadValues(List<OverheadValue> actionOverheadValues) {
		this.actionOverheadValues = actionOverheadValues;
	}	

	public List<Activity> getSorActivities() {
		return sorActivities;
	}

	public void setSorActivities(List<Activity> sorActivities) {
		this.sorActivities = sorActivities;
	}

	public List<Activity> getNonSorActivities() {
		return nonSorActivities;
	}

	public void setNonSorActivities(List<Activity> nonSorActivities) {
		this.nonSorActivities = nonSorActivities;
	}

	public PersistenceService<NonSor, Long> getNonSorService() {
		return nonSorService;
	}

	public void setNonSorService(PersistenceService<NonSor, Long> nonSorService) {
		this.nonSorService = nonSorService;
	}
	
	public String getWorkorderNo() {
		return workorderNo;
	}

	public void setWorkorderNo(String workorderNo) {
		this.workorderNo = workorderNo;
	}

	public String getActivityCode() {
		return activityCode;
	}

	public void setActivityCode(String activityCode) {
		this.activityCode = activityCode;
	}

	public String getActivityDesc() {
		return activityDesc;
	}

	public void setActivityDesc(String activityDesc) {
		this.activityDesc = activityDesc;
	}

	public List<WorkOrderActivity> getActivityList() {
		return activityList;
	}

	public void setActivityList(List<WorkOrderActivity> activityList) {
		this.activityList = activityList;
	}

	public List<WorkOrderActivity> getChangeQuantityActivities() {
		return changeQuantityActivities;
	}

	public void setChangeQuantityActivities(
			List<WorkOrderActivity> changeQuantityActivities) {
		this.changeQuantityActivities = changeQuantityActivities;
	}

	public String getEstimateValue() {
		return estimateValue;
	}

	public void setEstimateValue(String estimateValue) {
		this.estimateValue = estimateValue;
	}

	public List<MeasurementSheet> getMeasurementSheetList() {
		return measurementSheetList;
	}

	public void setMeasurementSheetList(List<MeasurementSheet> measurementSheetList) {
		this.measurementSheetList = measurementSheetList;
	}

	public String getEstimateUOM() {
		return estimateUOM;
	}

	public void setEstimateUOM(String estimateUOM) {
		this.estimateUOM = estimateUOM;
	}

	public RevisionWorkOrder getRevisionWorkOrder() {
		return revisionWorkOrder;
	}

	public void setRevisionWorkOrder(RevisionWorkOrder revisionWorkOrder) {
		this.revisionWorkOrder = revisionWorkOrder;
	}

	public void setRevisionWorkOrderService(
			PersistenceService<RevisionWorkOrder, Long> revisionWorkOrderService) {
		this.revisionWorkOrderService = revisionWorkOrderService;
	}

}