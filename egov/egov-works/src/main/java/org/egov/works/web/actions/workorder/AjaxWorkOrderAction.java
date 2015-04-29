package org.egov.works.web.actions.workorder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.script.ScriptContext;

import org.apache.commons.lang.StringUtils;
import org.egov.commons.CFinancialYear;
import org.egov.commons.service.CommonsService;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infstr.ValidationError;
import org.egov.infstr.ValidationException;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.services.ScriptService;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.pims.commons.DesignationMaster;
import org.egov.pims.model.PersonalInformation;
import org.egov.pims.service.EmployeeService;
import org.egov.pims.service.PersonalInformationService;
import org.egov.web.actions.BaseFormAction;
import org.egov.works.models.contractoradvance.ContractorAdvanceRequisition;
import org.egov.works.models.estimate.AbstractEstimate;
import org.egov.works.models.estimate.AbstractEstimateAppropriation;
import org.egov.works.models.measurementbook.MBHeader;
import org.egov.works.models.milestone.TrackMilestone;
import org.egov.works.models.tender.TenderResponseActivity;
import org.egov.works.models.workorder.WorkOrder;
import org.egov.works.models.workorder.WorkOrderEstimate;
import org.egov.works.services.MeasurementBookService;
import org.egov.works.services.contractoradvance.ContractorAdvanceService;
import org.springframework.beans.factory.annotation.Autowired;

public class AjaxWorkOrderAction extends BaseFormAction{

	private String departmentName;
	private Long desgId;
	private static final String WORKORDER_DESIG_LIST = "workOrderDesignations";
	private static final String WORKORDER_USER_LIST = "workOrderUsers";
	private static final String WORKORDER_ASSIGNED_LIST="workOrderAssignedUsers";
	private List<DesignationMaster> workOrderDesigList=new ArrayList<DesignationMaster>();
	private String traIds;
	private List<TenderResponseActivity> tenderResponseActivitylist=new ArrayList<TenderResponseActivity>();
	private PersistenceService<TenderResponseActivity, Long> tenderResponseActivityService;
	private Long executingDepartment;
	private PersonalInformationService personalInformationService;
	private List userList;
	private MeasurementBookService measurementBookService;
	List <MBHeader> approvedMBList = new ArrayList<MBHeader>();; 
	private Long workOrderId;
	private String query = "";
	private List<AbstractEstimate> estimateList = new LinkedList<AbstractEstimate>();
	private List<WorkOrder> workOrderList = new LinkedList<WorkOrder>();
	private String trackMlsCheck;
	private String yearEndApprCheck;
	@Autowired
        private CommonsService commonsService;
	private String estimateNo;
	private static final String VALID = "valid";
	private static final String INVALID = "invalid";
	private String advanceRequisitionNo;
	private String owner = "";
	private String arfInWorkFlowCheck;
	private ContractorAdvanceService contractorAdvanceService;
	@Autowired
        private EmployeeService employeeService;
	private static final String ARF_IN_WORKFLOW_CHECK="arfInWorkflowCheck"; 
	 @Autowired
         private ScriptService scriptService;
	
	
	public Object getModel() {
		return null;
	}
 
	public  String getDesignationByDeptId() {
		//List<Script> scriptList = persistenceService.findAllByNamedQuery(Script.BY_NAME,"workOrder.Designation.ByDepartment");
		if(StringUtils.isNotBlank(departmentName)){
			//List<String> list = (List<String>) scriptList.get(0).eval(Script.createContext("department",departmentName));	
			 ScriptContext scriptContext = ScriptService.createContext("department",departmentName);
	                 List<String> desglist = (List<String>)scriptService.executeScript("workOrder.Designation.ByDepartment", scriptContext);
	                 workOrderDesigList.addAll(getPersistenceService().findAllByNamedQuery("getDesignationForListOfDesgNames", desglist));
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
			else
				userList=personalInformationService.getListOfEmployeeViewBasedOnCriteria(criteriaParams, -1, -1);
		} catch (Exception e) {
			throw new EGOVRuntimeException("user.find.error", e);
		}
		return WORKORDER_USER_LIST;
	}
	
	public String getWOAssignedTo1ForDepartment() {
		try {
			if(executingDepartment==null || executingDepartment==-1)
				userList=Collections.EMPTY_LIST;
			else {
				userList=persistenceService.findAllBy("select distinct woe.workOrder.engineerIncharge from  WorkOrderEstimate woe where woe.estimate.executingDepartment.id=?",executingDepartment);
			}
		} catch (Exception e) {
			throw new EGOVRuntimeException("user.find.error", e);
		}
		return WORKORDER_ASSIGNED_LIST;
	}

	public String getWOAssignedTo2ForDepartment() {
		try {
			if(executingDepartment==null || executingDepartment==-1)
				userList=Collections.EMPTY_LIST;
			else {
				userList=persistenceService.findAllBy("select distinct woe.workOrder.engineerIncharge2 from  WorkOrderEstimate woe where woe.estimate.executingDepartment.id=?",executingDepartment);
			}
		} catch (Exception e) {
			throw new EGOVRuntimeException("user.find.error", e);
		}
		return WORKORDER_ASSIGNED_LIST;
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
					traIdLong[j] = Long.valueOf(values[i].split("~")[0].trim());//selected TenderResponseActivity Id
					traMap.put(traIdLong[j], Double.valueOf(values[i].split("~")[1]));//selected TenderResponseActivity 's UnAssigned Qty
					j++;
				}
			}
			traIdentifierSet.addAll(Arrays.asList(traIdLong));
			List<TenderResponseActivity> tempList=new ArrayList<TenderResponseActivity>();
			tempList=(List<TenderResponseActivity>)tenderResponseActivityService.findAllByNamedQuery("getTenderResponseActivityByIds", traIdentifierSet);
			for(TenderResponseActivity tenderResponseActivity:tempList){
				Double unAssignedQuantity=traMap.get(tenderResponseActivity.getId());
				tenderResponseActivity.setNegotiatedQuantity(unAssignedQuantity);//Temperorily setting UnAssigned Qty instead of Negotiated Qty
				HibernateUtil.getCurrentSession().evict(tenderResponseActivity);//To evict the changes done on tenderResponseActivity not to be committed.
				tenderResponseActivitylist.add(tenderResponseActivity);
			}
		}
		return "tenderResponseActivities";
	}
	
	public String getApprovedMBsForWorkOrder() {		
		approvedMBList = measurementBookService.findAllBy(" from MBHeader where workOrder.id=? and egwStatus.code<>'CANCELLED'", workOrderId);
		return "approvedMBs";
	}
	
	
	public String advanceRequisitionInWorkflowCheck() {
		arfInWorkFlowCheck = VALID;
		List<WorkOrderEstimate> woeList = (List<WorkOrderEstimate>) persistenceService.findAllBy(" from WorkOrderEstimate woe where woe.workOrder.id = ? ",workOrderId);
		if(woeList.size()==1){
			ContractorAdvanceRequisition arf =  contractorAdvanceService.getContractorARFInWorkflowByWOEId(woeList.get(0).getId());				
			if(arf != null) {
				arfInWorkFlowCheck = INVALID;
				advanceRequisitionNo = arf.getAdvanceRequisitionNumber();
				estimateNo = arf.getWorkOrderEstimate().getEstimate().getEstimateNumber();
				PersonalInformation emp = employeeService.getEmployeeforPosition(arf.getCurrentState().getOwnerPosition());
				if(emp!=null){
					owner = emp.getUserMaster().getName();
				}
			}
		} 
		return ARF_IN_WORKFLOW_CHECK;
	}
	
	public String trackMilestoneForBillCreationCheck() {
		List<TrackMilestone> tm = (List<TrackMilestone>) persistenceService.findAllBy(" select trmls from WorkOrderEstimate as woe left join woe.milestone mls left join mls.trackMilestone trmls where trmls.egwStatus.code='APPROVED' and woe.workOrder.id = ? and trmls.total>0 ",workOrderId);
		trackMlsCheck = "invalid";
		if(tm != null && !tm.isEmpty() && tm.get(0)!=null)
			trackMlsCheck = "valid";
		return "trackMlsForBillCreationCheck";
	}
	
	public String yearEndApprForBillCreationCheck() {
		List<WorkOrderEstimate> woeList = (List<WorkOrderEstimate>) persistenceService.findAllBy(" from WorkOrderEstimate woe where woe.workOrder.id = ? ",workOrderId);
		yearEndApprCheck = VALID;
		Long currentFinYearId=0l;
		if(woeList.size()==1){
			estimateNo = woeList.get(0).getEstimate().getEstimateNumber();
			CFinancialYear currFinancialYear;
			try{
				currFinancialYear = commonsService.getFinancialYearByDate(new Date());
			}
			catch (Exception e) { 
				throw new ValidationException(Arrays.asList(new ValidationError("yrEnd.appr.verification.for.bill.financialyear.invalid","yrEnd.appr.verification.for.bill.financialyear.invalid")));
			}
			
			if(currFinancialYear!=null) {
				currentFinYearId=currFinancialYear.getId();
			}
			
			if(woeList.get(0).getEstimate().getDepositCode()==null){
				AbstractEstimateAppropriation aeaObj = (AbstractEstimateAppropriation) persistenceService.findByNamedQuery("getLatestBudgetUsageForEstimate", woeList.get(0).getEstimate().getId());
				if(aeaObj!=null && aeaObj.getBudgetUsage().getConsumedAmount()>0){
					if(aeaObj.getBudgetUsage().getFinancialYearId().intValue()!=currentFinYearId.intValue()){
						yearEndApprCheck = INVALID;
					}
				}
				else{
					yearEndApprCheck = INVALID;
				}
			}
		}
		return "yearEndApprForBillCreationCheck";
	}
	
	public String searchEstimateNumber(){
		String strquery="";
		ArrayList<Object> params=new ArrayList<Object>();
		if(!StringUtils.isEmpty(query)) {
			strquery="select woe.estimate from WorkOrderEstimate woe where woe.workOrder.parent is null and UPPER(woe.estimate.estimateNumber) like '%'||?||'%' " +
			" and woe.workOrder.egwStatus.code = ? )";
			params.add(query.toUpperCase());
			params.add("APPROVED");
			estimateList = getPersistenceService().findAllBy(strquery,params.toArray());
		}
		return "estimateNoSearchResults";
	}
	
	public String searchWorkOrderNumber(){
		String strquery="";
		ArrayList<Object> params=new ArrayList<Object>();
		if(!StringUtils.isEmpty(query)) {
			strquery=" from WorkOrder wo where wo.parent is null and UPPER(wo.workOrderNumber) like '%'||?||'%' " +
			"and wo.egwStatus.code = ? ";
			params.add(query.toUpperCase());
			params.add("APPROVED");		
			workOrderList = getPersistenceService().findAllBy(strquery,params.toArray());
		}
		return "workOrderNoSearchResults";
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

	public String getTraIds() {
		return traIds;
	}

	public void setTraIds(String traIds) {
		this.traIds = traIds;
	}

	public List<TenderResponseActivity> getTenderResponseActivitylist() {
		return tenderResponseActivitylist;
	}

	public void setTenderResponseActivitylist(
			List<TenderResponseActivity> tenderResponseActivitylist) {
		this.tenderResponseActivitylist = tenderResponseActivitylist;
	}

	public void setTenderResponseActivityService(
			PersistenceService<TenderResponseActivity, Long> tenderResponseActivityService) {
		this.tenderResponseActivityService = tenderResponseActivityService;
	}

	public Long getExecutingDepartment() {
		return executingDepartment;
	}

	public void setExecutingDepartment(Long executingDepartment) {
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

	public Long getWorkOrderId() {
		return workOrderId;
	}

	public void setWorkOrderId(Long workOrderId) {
		this.workOrderId = workOrderId;
	}

	public List<AbstractEstimate> getEstimateList() {
		return estimateList;
	}

	public void setEstimateList(List<AbstractEstimate> estimateList) {
		this.estimateList = estimateList;
	}

	public List<WorkOrder> getWorkOrderList() {
		return workOrderList;
	}

	public void setWorkOrderList(List<WorkOrder> workOrderList) {
		this.workOrderList = workOrderList;
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public String getTrackMlsCheck() {
		return trackMlsCheck;
	}

	public String getYearEndApprCheck() {
		return yearEndApprCheck;
	}

	public void setYearEndApprCheck(String yearEndApprCheck) {
		this.yearEndApprCheck = yearEndApprCheck;
	}

	public void setCommonsService(CommonsService commonsService) {
		this.commonsService = commonsService;
	}

	public String getEstimateNo() {
		return estimateNo;
	}

	public void setEstimateNo(String estimateNo) {
		this.estimateNo = estimateNo;
	}

	public String getAdvanceRequisitionNo() {
		return advanceRequisitionNo;
	}

	public void setAdvanceRequisitionNo(String advanceRequisitionNo) {
		this.advanceRequisitionNo = advanceRequisitionNo;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getArfInWorkFlowCheck() {
		return arfInWorkFlowCheck;
	}

	public void setArfInWorkFlowCheck(String arfInWorkFlowCheck) {
		this.arfInWorkFlowCheck = arfInWorkFlowCheck;
	}

	public void setContractorAdvanceService(
			ContractorAdvanceService contractorAdvanceService) {
		this.contractorAdvanceService = contractorAdvanceService;
	}

	public void setEmployeeService(EmployeeService employeeService) {
		this.employeeService = employeeService;
	}
	

}
