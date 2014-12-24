package org.egov.payroll.client.advance;

import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;

import org.apache.struts2.convention.annotation.ParentPackage ;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.infstr.ValidationError;
import org.egov.infstr.client.filter.EGOVThreadLocals;
import org.egov.infstr.services.PersistenceService;
import org.egov.lib.rjbac.user.User;
import org.egov.lib.rrbac.model.Action;
import org.egov.lib.rrbac.services.RbacService;
import org.egov.payroll.model.Advance;
import org.egov.payroll.model.SalaryARF;
import org.egov.payroll.utils.PayrollConstants;
import org.egov.payroll.utils.PayrollExternalInterface;
import org.egov.payroll.utils.PayrollManagersUtill;
import org.egov.payroll.workflow.advance.AdvanceWorkflowService;
import org.egov.payroll.workflow.advance.SalaryARFWorkflowService;
import org.egov.pims.model.Assignment;
import org.egov.pims.model.EmployeeView;
import org.egov.web.actions.BaseFormAction;

@ParentPackage("egov")
public class AdvanceWorkflowAction extends BaseFormAction {
	
	
	private List<Hashtable<String, Object>> advanceList=null;
	private String comments=null;
	private String advanceId=null;
	private String salaryARFId=null;
	private String sanctionNum;
	private Date sanctionDate;
	private User sanctionOrRejectBy;
	private String bpvActionUrl=null;
	private PayrollExternalInterface payrollExternalInterface;
	private RbacService rbacService;
	public List<ValidationError> errors=new ArrayList<ValidationError>();
	private static final String approve="approve";
	private static final String reject="reject";
	private transient PersistenceService<Advance, Long> advancePersistenceService;
	private transient PersistenceService<SalaryARF, Long> salaryARFPersistenceService;
	private transient AdvanceWorkflowService advanceWorkflowService;
	private transient SalaryARFWorkflowService salaryARFWorkflowService;
	private String wfStatus;
	private String isSalaryARF="false";
	private String advanceWfType;

	//Added for manual workflow
	private String approverDept;
	private String approverDesig;
	private String approverEmpAssignmentId;

	public Object getModel() {
		return null;
	}

	public void prepare(){
		super.prepare();
		addDropdownData("departmentList", getPersistenceService().findAllBy("from DepartmentImpl order by deptName"));
		addDropdownData("designationList", getPersistenceService().findAllBy("from DesignationMaster order by designationName"));
		addDropdownData("approverEmpList", new ArrayList<EmployeeView>());
	}
	
	public String execute() {		
		return EDIT;
	}
	
	@SkipValidation
	public String loadAdvance()
	{
		Advance advance=null; 
		advanceList=new ArrayList<Hashtable<String, Object>>();
		
		if(advanceId!=null){ //For deduction-bankloan advance
			advance= advancePersistenceService.findById(Long.valueOf(advanceId), false);
			setWfStatus(advance.getState().getValue().toString());
			setAdvanceWfType(salaryARFWorkflowService.getTypeOfAdvanceWf());
			setIsSalaryARF("false");
			
		}
		else if(salaryARFId!=null){ //For deduction-advance
			SalaryARF salARF= salaryARFPersistenceService.findById(Long.valueOf(salaryARFId), false);
			advance = salARF.getAdvance();
			setWfStatus(salARF.getState().getValue().toString());
			
			String url=getActionUrlByPassingActionName(PayrollConstants.GENERATEADVANCEBPVURL);
			if(url!=null) 
			{
				setBpvActionUrl(url);
				
			}
			setAdvanceWfType(advanceWorkflowService.getTypeOfAdvanceWf());
		}
		
		setSanctionOrRejectBy(findLoggedInUser()); 
		Hashtable<String, Object> advanceHT=new Hashtable<String, Object>(); 
		advanceHT.put("advance",advance );
		advanceList.add(advanceHT);

		return EDIT;
	}
	
	@SkipValidation
	public String approveAdvance()
	{
		Advance advance=null;
		if("".equals(getSanctionNum()))
		{
			addFieldError("sanctionNum","Please enter sanction no");
		}
		else
		{
			if(isSanctionNoExists())
			{
				addFieldError("sanctionNum","Sanction No. Already exists");
			}
		}
		if(null==getSanctionDate())
		{
			addFieldError("sanctionDate","Please enter sanction/reject date");
		}
		
		if("Manual".equals(advanceWorkflowService.getTypeOfAdvanceWf()))
		{
			if("".equals(getApproverDept()))
			{
				addFieldError("approverDept","Select approver department");
			}
			if("".equals(getApproverDesig()))
			{
				addFieldError("approverDesig","Select approver employee");
			}
			if("".equals(getApproverEmpAssignmentId()))
			{
				addFieldError("approverEmpAssignmentId","Select approver employee");
			}
		}
		if(!getActionErrors().isEmpty() || !getFieldErrors().isEmpty())
		{
			return loadAdvance();
		}
		
		if((advanceId!=null && !("").equals(advanceId) )|| (salaryARFId!=null && !("").equals(salaryARFId) ) )
		{
			if(getIsSalaryARF().equals("true"))
			{
				SalaryARF salARF= salaryARFPersistenceService.findById(Long.valueOf(salaryARFId), false);
				advance = salARF.getAdvance();
			}
			else if(getIsSalaryARF().equals("false"))
			{
				advance= advancePersistenceService.findById(Long.valueOf(advanceId), false);
			}
			
			advance.setSanctionNum(sanctionNum);
			advance.setSanctionedDate(getSanctionDate());
			advance.setSanctionedBy(findLoggedInUser());

			//Setting Approver position to the payslip if manual payslip workflow
			if("Manual".equals(advanceWorkflowService.getTypeOfAdvanceWf())){
				if(approverEmpAssignmentId != null && !"".equals(approverEmpAssignmentId)){
					Assignment approverAssignment = PayrollManagersUtill.getEmployeeService().getAssignmentById(Integer.parseInt(approverEmpAssignmentId));
					advance.setApproverPos(approverAssignment.getPosition());
				}				
			}
			
			if(getIsSalaryARF().equals("true"))
			{
				getSalaryARFWorkflowService().getSalaryARFSimpleWorkflowService().transition(approve, advance.getSalaryARF(),comments );
			}
			else if(getIsSalaryARF().equals("false"))
			{
				getAdvanceWorkflowService().getAdvanceSimpleWorkflowService().transition(approve, advance,comments );
			}
			
			if(getActionErrors().isEmpty() && getFieldErrors().isEmpty())
			{
				addActionMessage("Advance Approved Successfully");
			}	
			
		}
		return EDIT;
	}

	@SkipValidation
	public String rejectAdvance() 
	{
		Advance advance=null;
		if(null==getSanctionDate())
		{
			addFieldError("sanctionDate","Please enter sanction/reject date");
		}
		if(!getActionErrors().isEmpty() || !getFieldErrors().isEmpty())
		{
			return loadAdvance(); 
		}
		if((advanceId!=null && !("").equals(advanceId) )|| (salaryARFId!=null && !("").equals(salaryARFId) ) )
		{
			if(getIsSalaryARF().equals("true"))
			{
				SalaryARF salARF= salaryARFPersistenceService.findById(Long.valueOf(salaryARFId), false);
				advance = salARF.getAdvance();
			}
			else if(getIsSalaryARF().equals("false"))
			{
				advance= advancePersistenceService.findById(Long.valueOf(advanceId), false);
			}
			
			advance.setSanctionedDate(getSanctionDate());
			advance.setSanctionedBy(findLoggedInUser());
			
			if(getIsSalaryARF().equals("true"))
			{
				getSalaryARFWorkflowService().getSalaryARFSimpleWorkflowService().transition(reject, advance.getSalaryARF(),comments );
			}
			else if(getIsSalaryARF().equals("false"))
			{
				getAdvanceWorkflowService().getAdvanceSimpleWorkflowService().transition(reject, advance,comments );
			}
			if(getActionErrors().isEmpty() && getFieldErrors().isEmpty())
			{
				addActionMessage("Advance Rejected Successfully");
			}
		}

		return EDIT;
	}
	
	public String getActionUrlByPassingActionName(String actionName) 
	{
		String url=null;
		if(actionName!=null && !"".equals(actionName)){
			Action action= rbacService.getActionByName(actionName);
			
			if(action!=null)
			{
				url="/"+action.getContextRoot().concat(action.getUrl().concat("?").concat(action.getQueryParams()));
			}
		}
		return url;
		
	}
	
	public User findLoggedInUser()
	{
		User usr =payrollExternalInterface.getUserByID(Integer.valueOf(EGOVThreadLocals.getUserId()));
		return usr;
	}
	
	public boolean isSanctionNoExists()
	{
		Advance  adv= (Advance)advancePersistenceService.find("from Advance where sanctionNum=?",sanctionNum);
		
		if(adv==null)
		{
			return false;
		}
		else
		{
			return true;
		}
	}

	public List<Hashtable<String, Object>> getAdvanceList() {
		return advanceList;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getAdvanceId() {
		return advanceId;
	}

	public void setAdvanceId(String advanceId) {
		this.advanceId = advanceId;
	}

	public void setAdvancePersistenceService(
			PersistenceService<Advance, Long> advancePersistenceService) {
		this.advancePersistenceService = advancePersistenceService;
	}

	public AdvanceWorkflowService getAdvanceWorkflowService() {
		return advanceWorkflowService;
	}

	public void setAdvanceWorkflowService(
			AdvanceWorkflowService advanceWorkflowService) {
		this.advanceWorkflowService = advanceWorkflowService;
	}

	public Date getSanctionDate() {
		return sanctionDate;
	}

	public void setSanctionDate(Date sanctionDate) {
		this.sanctionDate = sanctionDate;
	}

	public String getSanctionNum() {
		return sanctionNum;
	}

	public void setSanctionNum(String sanctionNum) {
		this.sanctionNum = sanctionNum;
	}

	public void setPayrollExternalInterface(
			PayrollExternalInterface payrollExternalInterface) {
		this.payrollExternalInterface = payrollExternalInterface;
	}

	public User getSanctionOrRejectBy() {
		return sanctionOrRejectBy;
	}

	public void setSanctionOrRejectBy(User sanctionOrRejectBy) {
		this.sanctionOrRejectBy = sanctionOrRejectBy;
	}
	
	public String getBpvActionUrl() {
		return bpvActionUrl;
	}

	public void setBpvActionUrl(String bpvActionUrl) {
		this.bpvActionUrl = bpvActionUrl;
	}
	
	public void setRbacService(RbacService rbacService) {
		this.rbacService = rbacService;
	}

	public String getWfStatus() {
		return wfStatus;
	}

	public void setWfStatus(String wfStatus) {
		this.wfStatus = wfStatus;
	}
	
	public String getAdvanceWfType() {
		return advanceWfType;
	}

	public void setAdvanceWfType(String advanceWfType) {
		this.advanceWfType = advanceWfType;
	}
	
	public String getApproverEmpAssignmentId() {
		return approverEmpAssignmentId;
	}
	public void setApproverEmpAssignmentId(String approverEmpAssignmentId) {
		this.approverEmpAssignmentId = approverEmpAssignmentId;
	}
	
	public String getApproverDept() {
		return approverDept;
	}

	public void setApproverDept(String approverDept) {
		this.approverDept = approverDept;
	}

	public String getApproverDesig() {
		return approverDesig;
	}

	public void setApproverDesig(String approverDesig) {
		this.approverDesig = approverDesig;
	}

	public String getSalaryARFId() {
		return salaryARFId;
	}

	public void setSalaryARFId(String salaryARFId) {
		this.salaryARFId = salaryARFId;
	}

	public void setSalaryARFPersistenceService(
			PersistenceService<SalaryARF, Long> salaryARFPersistenceService) {
		this.salaryARFPersistenceService = salaryARFPersistenceService;
	}

	public void setSalaryARFWorkflowService(
			SalaryARFWorkflowService salaryARFWorkflowService) {
		this.salaryARFWorkflowService = salaryARFWorkflowService;
	}

	public SalaryARFWorkflowService getSalaryARFWorkflowService() {
		return salaryARFWorkflowService;
	}

	public String getIsSalaryARF() {
		return isSalaryARF;
	}

	public void setIsSalaryARF(String isSalaryARF) {
		this.isSalaryARF = isSalaryARF;
	}

	

}
