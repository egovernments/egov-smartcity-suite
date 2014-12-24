package org.egov.payroll.client.payslip;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.egov.infstr.ValidationError;
import org.egov.infstr.ValidationException;
import org.egov.infstr.client.filter.EGOVThreadLocals;
import org.egov.infstr.commons.dao.GenericDaoFactory;
import org.egov.infstr.config.AppConfigValues;
import org.egov.infstr.models.State;
import org.egov.infstr.models.StateAware;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.infstr.workflow.SimpleWorkflowService;
import org.egov.infstr.workflow.WorkFlowMatrix;
import org.egov.lib.rjbac.user.User;
import org.egov.model.bills.EgBillregister;
import org.egov.payroll.model.Deductions;
import org.egov.payroll.model.EmpPayroll;
import org.egov.payroll.services.payslip.IPayslipProcess;
import org.egov.payroll.services.payslip.PayslipFailureException;
import org.egov.payroll.services.payslipApprove.SalaryPaybill;
import org.egov.payroll.utils.PayrollManagersUtill;
import org.egov.payroll.workflow.payslip.PayslipService;
import org.egov.pims.commons.Position;
import org.egov.pims.model.EmployeeView;
import org.egov.pims.model.PersonalInformation;
import org.egov.web.actions.workflow.GenericWorkFlowAction;
import org.hibernate.Query;


@ParentPackage("egov")
public class PayslipWorkflowAction extends GenericWorkFlowAction {
	private static final Logger LOGGER=Logger.getLogger(PayslipWorkflowAction.class);
	private List<Hashtable<String, Object>> payslipSet=null;
	private List<EmpPayroll> payslipList=null;
	private IPayslipProcess payslipProcessImpl;
	private String comments="";
	private String payslipId=null;
	public List<ValidationError> errors=new ArrayList<ValidationError>();
	//private static final String acApprove="AccountsApproved";
	private PersistenceService actionService;
	private PayslipService payslipService;
	private String payslipWfNextAction;
	private SalaryPaybill salaryPaybill;
	private SimpleWorkflowService workflowService;
	private final static String FORWARD = "Forward";
	private String mode="";
	private Integer approverDepartment;
	//to set the approver position id for the workflow  
	private Integer approverPositionId;
	//to load the designations of wf matrix from loadDesignationFromMatrix(), need to set the current state of the payslip object
	private String paySlipCurrentState;
	String nextAction="";
	private String[] selectedPaySlips=new String[0];
	
	private BigDecimal grossPayTotal=BigDecimal.valueOf(0);

	private BigDecimal netPayTotal=BigDecimal.valueOf(0);
	
	private BigDecimal deductionsTotal=BigDecimal.valueOf(0);
	private String groupedDeptName;
	private  String groupedFunctionName;
	private  String groupedFundName;
	private Long stateValue;
	private String billNumber;

	public String[] getSelectedPaySlips() {
		return selectedPaySlips;
	}
	public void setSelectedPaySlips(String[] selectedPaySlips) {
		this.selectedPaySlips = selectedPaySlips;
	}
	public String getPaySlipCurrentState() {
		return paySlipCurrentState;
	}
	public void setPaySlipCurrentState(String paySlipCurrentState) {
		this.paySlipCurrentState = paySlipCurrentState;
	}
	
	public Long getStateValue() {
		return stateValue;
	}
	public void setStateValue(Long stateValue) {
		this.stateValue = stateValue;
	}
	
	public String getBillNumber() {
		return billNumber;
	}
	public void setBillNumber(String billNumber) {
		this.billNumber = billNumber;
	}
	public Integer getApproverPositionId() {
		return approverPositionId;
	}
	public void setApproverPositionId(Integer approverPositionId) {
		this.approverPositionId = approverPositionId;
	}
	public Integer getApproverDepartment() {
		return approverDepartment;
	}
	public void setApproverDepartment(Integer approverDepartment) {
		this.approverDepartment = approverDepartment;
	}
	public String getMode() {
		return mode;
	}
	public void setMode(String mode) {
		this.mode = mode;
	}
	public SimpleWorkflowService getWorkflowService() {
		return workflowService;
	}
	public void setWorkflowService(SimpleWorkflowService workflowService) {
		this.workflowService = workflowService;
	}
	public SalaryPaybill getSalaryPaybill() {
		return salaryPaybill;
	}
	public void setSalaryPaybill(SalaryPaybill salaryPaybill) {
		this.salaryPaybill = salaryPaybill;
	}
	public String getPayslipWfNextAction() {
		return payslipWfNextAction;
	}
	public void setPayslipWfNextAction(String payslipWfNextAction) {
		this.payslipWfNextAction = payslipWfNextAction;
	}
	public void setPayslipService(PayslipService payslipService) {
		this.payslipService = payslipService;
	}

	public void setActionService(PersistenceService actionService) {
		this.actionService = actionService;
	}

	public void setComments(String comments) {
		this.comments= comments;
	}

	public void setPayslipProcessImpl(IPayslipProcess payslipProcessImpl) {
		this.payslipProcessImpl = payslipProcessImpl;
	}
	public void setPayslipId(String payslipId) {
		this.payslipId = payslipId;
	}

	

	public BigDecimal getGrossPayTotal() {
		return grossPayTotal;
	}
	public void setGrossPayTotal(BigDecimal grossPayTotal) {
		this.grossPayTotal = grossPayTotal;
	}
	public BigDecimal getNetPayTotal() {
		return netPayTotal;
	}
	public void setNetPayTotal(BigDecimal netPayTotal) {
		this.netPayTotal = netPayTotal;
	}
	public BigDecimal getDeductionsTotal() {
		return deductionsTotal;
	}
	public void setDeductionsTotal(BigDecimal deductionsTotal) {
		this.deductionsTotal = deductionsTotal;
	}
	public String getGroupedDeptName() {
		return groupedDeptName;
	}
	public void setGroupedDeptName(String groupedDeptName) {
		this.groupedDeptName = groupedDeptName;
	}
	public String getGroupedFunctionName() {
		return groupedFunctionName;
	}
	public void setGroupedFunctionName(String groupedFunctionName) {
		this.groupedFunctionName = groupedFunctionName;
	}
	public String getGroupedFundName() {
		return groupedFundName;
	}
	public void setGroupedFundName(String groupedFundName) {
		this.groupedFundName = groupedFundName;
	}
	public StateAware getModel() {
		return null;
	}

	public String execute() {
		return EDIT;
	}

	public void prepare(){
		super.prepare();
		addDropdownData("departmentList", getPersistenceService().findAllBy("from DepartmentImpl order by deptName"));
		addDropdownData("designationList", getPersistenceService().findAllBy("from DesignationMaster order by designationName"));
		addDropdownData("approverList", new ArrayList<EmployeeView>());
		setApproverDepartment(payslipService.getLoggedUserDepartmentId());
	}


	public String loadpayslip(){
		//request.put("billList", "ddasdsdda");
		//Getting all parameters value for group by attributes
		LOGGER.info("--------"+parameters.get("month"));
		Integer ownerPosId =  Integer.parseInt(parameters.get("ownerPosId")[0]);
		Integer userId = Integer.parseInt(parameters.get("userId")[0]);
		BigDecimal month = new BigDecimal(parameters.get("month")[0]);
		Long yearId = Long.parseLong(parameters.get("yearId")[0]);
		Integer departmentId = Integer.parseInt(parameters.get("departmentId")[0]);
		Integer functionaryId = parameters.get("functionaryId")==null ? null:Integer.parseInt(parameters.get("functionaryId")[0]);
		Integer fundId = parameters.get("fundId")==null ? null:Integer.parseInt(parameters.get("fundId")[0]);
		Long functionId = parameters.get("functionId")==null ? null:Long.parseLong(parameters.get("functionId")[0]);
		Integer billNumberId=parameters.get("billNumberId")==null ? null:Integer.parseInt(parameters.get("billNumberId")[0]);
		String state = parameters.get("state")==null ? null:parameters.get("state")[0];


		String query = "from EmpPayroll pay where pay.state.owner =:owner and pay.state.next is null and pay.state.value !=:end " +
						"and pay.empAssignment.deptId.id =:departmentId and " +
						"pay.month =:month and pay.financialyear.id =:yearId ";
		if(functionaryId != null){
			query += "and pay.empAssignment.functionary.id =:functionaryId ";
		}
		if(fundId != null){
			query += "and pay.empAssignment.fundId.id =:fundId ";
		}
		if(functionId != null){
			query += "and pay.empAssignment.functionId.id =:functionId ";
		}
		if(billNumberId != null){
			query += "and pay.billNumber.id =:billNumberId ";
		}
		if(state != null){
			query += "and pay.state.value =:state ";
		}
		Query qry = this.persistenceService.getSession().createQuery(query);
		qry.setInteger("owner", ownerPosId);
		qry.setString("end", State.END);
		//qry.setString("new", State.NEW);
		//qry.setInteger("userId", userId);
		qry.setInteger("departmentId", departmentId);
		qry.setBigDecimal("month", month);
		qry.setLong("yearId", yearId);
		if(functionaryId != null){
			qry.setInteger("functionaryId", functionaryId);
		}
		if(fundId != null){
			qry.setInteger("fundId", fundId);
		}
		if(functionId != null){
			qry.setLong("functionId", functionId);
		}
		if(billNumberId != null){
			qry.setInteger("billNumberId",billNumberId);	
		}
		if(state != null){
			qry.setString("state", state);
		}
		List<EmpPayroll>  listPayslip = qry.list();
		
		if(!listPayslip.isEmpty())
			populateAggregateValues(listPayslip.get(0));
		
		//New Design of payslipRenderService Hashtable<String, Object> hashTablePayslips=new Hashtable<String, Object>();

		payslipSet = new ArrayList<Hashtable<String, Object>>();
		for(EmpPayroll payslipObj : listPayslip){
			//to show these as grand total in the jsp
			grossPayTotal=grossPayTotal.add(payslipObj.getGrossPay());
			deductionsTotal=deductionsTotal.add(payslipObj.getTotalDeductions());
			netPayTotal=netPayTotal.add(payslipObj.getNetPay());
			
			Hashtable<String, Object> hashTablePayslips=new Hashtable<String, Object>();
			payslipWfNextAction = payslipObj.getState().getNextAction();
			hashTablePayslips.put("paySlip", payslipObj);
			payslipSet.add(hashTablePayslips);
			this.paySlipCurrentState =payslipObj.getState().getValue(); 
			
		}
		LOGGER.info("getPayslipSet size at last "+getPayslipSet()==null?"nothing":getPayslipSet().size());
		return EDIT;
	}
	//to populate aggregate dept ,function and fund names
	private void populateAggregateValues(EmpPayroll payslip) {
		List<AppConfigValues> appConfigList = GenericDaoFactory.getDAOFactory().getAppConfigValuesDAO().getConfigValuesByModuleAndKey("Payroll", "PAYROLLFINTRNATTRIBUTES");
		List<String> mandatoryFields = new ArrayList<String>(); 
		for (AppConfigValues appConfig : appConfigList) {
			
				String value = appConfig.getValue();
				String header=value.substring(0, value.indexOf('|'));
				String mandate = value.substring(value.indexOf('|')+1);
				if(mandate.equalsIgnoreCase("M")){
					mandatoryFields.add(header);
				}
			
		}
		if(mandatoryFields.contains("fund")){
		setGroupedFundName(payslip.getEmpAssignment().getFundId().getName());
		}
		if(mandatoryFields.contains("function")){
			setGroupedFunctionName(payslip.getEmpAssignment().getFunctionId().getName());	
		}
		if(mandatoryFields.contains("department")){
			setGroupedDeptName(payslip.getEmpAssignment().getDeptId().getDeptName());
		}
		//to show history state id required
		setStateValue(payslip.getState()==null?null:payslip.getState().getId());
		setBillNumber(payslip.getBillNumber()==null?null:payslip.getBillNumber().getBillNumber());
	}

	public String approveOrRejectPayslip() throws Exception{
		//SalaryPaybill  salaryPaybill=new SalaryPaybill();
			
		Position empPos=null;
		PersonalInformation nextApprover=null;
		PersonalInformation loggedInEmp=null;
		User user = PayrollManagersUtill.getUserService().getUserByID(Integer.valueOf(EGOVThreadLocals.getUserId()));
		if(null!=approverPositionId && approverPositionId!=-1)
		{
			empPos = (Position) persistenceService.getSession().load(Position.class, approverPositionId);
			nextApprover = PayrollManagersUtill.getEmployeeService().getEmployeeforPosition(empPos);
		}	
		if(null!=user.getId())
		{
			loggedInEmp = PayrollManagersUtill.getEmployeeService().getEmpForUserId(user.getId());
		}
		try{
			
			if(selectedPaySlips.length>0)
			{
				createPayslipList(selectedPaySlips);
				if(null != payslipList && payslipList.size()>0)
				{
					for(EmpPayroll monthlyPayslip:payslipList)
					{
						payslipProcessImpl.fireWorkFlow(monthlyPayslip, workFlowAction, approverComments);
					}
				}	
			}
			
			if(null!=workFlowAction && "approve".equalsIgnoreCase(workFlowAction)){
				List<EgBillregister> billList = salaryPaybill.createSalaryBill(payslipList,user );
				HibernateUtil.getCurrentSession().flush();
				for(EgBillregister bill : billList){
					HibernateUtil.getCurrentSession().refresh(bill);
					bill.getEgBillregistermis().setSourcePath("/EGF/bill/billView!view.action?billId="+bill.getId().toString());
				}
				if(null!=loggedInEmp)
				{
					addActionMessage("Payslip(s) have been approved by "+loggedInEmp.getName());
				}
				request.put("billList", billList);
			}
			if(null!=workFlowAction && "reject".equalsIgnoreCase(workFlowAction)){
				salaryPaybill.populateRejectedPayslips(payslipList, user);
				updateRejectedPayslips(payslipList); 
				if(null!=payslipList.get(0).getEmpAssignment().getPosition())
				{
					empPos = payslipService.getApproverPositionByBillId(payslipList.get(0).getBillNumber().getId());
					nextApprover = PayrollManagersUtill.getEmployeeService().getEmployeeforPosition(empPos);
				}	
				if(!(payslipList.get(0).getCurrentState().getValue().equalsIgnoreCase("end")))
				{
					addActionMessage("Payslip(s) have been rejected and sent back to bill clerk: " +nextApprover.getName());
				}
				else
				{
					addActionMessage("Payslip(s) have been cancelled");
				}
			}
			if(workFlowAction.equalsIgnoreCase("forward") && null!=nextApprover)
			{
				addActionMessage("Payslip(s) have been forwarded to "+nextApprover.getName());
			}
			
		}catch(PayslipFailureException pfexception){
			addActionError(pfexception.getMessage());
		}
		catch(ValidationException e){
			String errorMsg = "";
			for(ValidationError vr : e.getErrors()){
				  errorMsg += vr.getMessage();
				  errorMsg += " ";
			}
			addActionError(errorMsg);
		}
		this.mode = "view";
		LOGGER.info("EDIT---"+EDIT);
		return EDIT;
	}


	public List<Hashtable<String, Object>> getPayslipSet() {
		return payslipSet;
	}


	private void createPayslipList(String[] paysliparray)
	{
		LOGGER.info("TEST---------");
		payslipList = new ArrayList<EmpPayroll>();
		EmpPayroll empPayroll=null;
		for(int i=0;i<paysliparray.length;i++){
			empPayroll=	PayrollManagersUtill.getPayRollService().getPayslipById(paysliparray[i]);
			if(null!=approverPositionId){
				empPayroll.setApproverPositionId(Integer.valueOf(approverPositionId));
			}	
			payslipList.add(empPayroll);
		}
	}


	/**
	 * This method sets the advance and advance scheduler reference to null for rejected and cancelled payslips 
	 * @param payslipList
	 */
	private void updateRejectedPayslips( List<EmpPayroll> payslipList)
	{
		LOGGER.info("inside updateRejectedPayslips..");
		for(EmpPayroll empPayroll:payslipList)
		{
			String status=GenericDaoFactory.getDAOFactory().getAppConfigValuesDAO().getAppConfigValueByDate("Payslip","PayslipCancelledStatus", new Date()).getValue();			
			LOGGER.info("empPayroll.getStatus().getCode()="+empPayroll.getStatus().getCode());
			if(empPayroll.getStatus().getCode().equalsIgnoreCase(status))
			{
				LOGGER.info("Setting advance and scheduler reference to null for empPayroll id ="+empPayroll.getId());
				Set<Deductions> deductions=empPayroll.getDeductionses();
				for(Deductions ded:deductions)
				{
					if(ded.getSalAdvances()!=null)
						ded.setSalAdvances(null);
					if(ded.getAdvanceScheduler() != null)
						ded.setAdvanceScheduler(null);					
				}
			}
			
		}
	}

	/**
	 * Used to get valid actions that needs to be performed Based on these value workflow buttons will be rendered
	 */
	@Override
	public List<String> getValidActions() {
		List<String> validActions = Collections.emptyList();		
		if (payslipSet != null && payslipSet.size() > 0) 
		{
			//the action list is based on "NEXT_ACTION" value of the paySlip
			//All paySLip in the inbox will have the same next action.
			EmpPayroll paySlip = ((EmpPayroll)payslipSet.get(0).get("paySlip"));		
			if (paySlip!=null && paySlip.getId() == null) {
				validActions = Arrays.asList(FORWARD);
	
			} else {
				if (paySlip.getCurrentState() != null) {
					validActions = this.customizedWorkFlowService.getNextValidActions(paySlip.getStateType(),
							this.getWorkFlowDepartment(), this.getAmountRule(), this.getAdditionalRule(), 
							paySlip.getCurrentState().getValue(), this.getPendingActions());
				} else {
					validActions = this.customizedWorkFlowService.getNextValidActions(paySlip.getStateType(),
							this.getWorkFlowDepartment(), this.getAmountRule(), this.getAdditionalRule(), State.NEW,
							this.getPendingActions());
				}
			}
		}
		return validActions;
	}
	
	/**
	 * Used to get next action If the nextAction value is END then approval Information won't be shown on the UI.
	 */
	@Override
	public String getNextAction() {
		WorkFlowMatrix wfMatrix = null;
		if (payslipSet != null && payslipSet.size() > 0)
		{
			EmpPayroll paySlip = ((EmpPayroll)payslipSet.get(0).get("paySlip"));
			if (paySlip!=null && paySlip.getId() != null) {
				if (paySlip.getCurrentState() != null) {
					wfMatrix = this.customizedWorkFlowService.getWfMatrix(paySlip.getStateType(),
							this.getWorkFlowDepartment(), this.getAmountRule(), this.getAdditionalRule(), 
							paySlip.getCurrentState().getValue(), this.getPendingActions());
				} else {
					wfMatrix = this.customizedWorkFlowService.getWfMatrix(paySlip.getStateType(),
							this.getWorkFlowDepartment(), this.getAmountRule(), this.getAdditionalRule(), State.NEW,
							this.getPendingActions());
				}
			}
		}
		return wfMatrix == null ? "" : wfMatrix.getNextAction();
	}
	
	
	
}
