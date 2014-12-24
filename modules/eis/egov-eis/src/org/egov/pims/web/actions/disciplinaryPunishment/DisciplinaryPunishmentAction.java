package org.egov.pims.web.actions.disciplinaryPunishment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletException;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.commons.EgwStatus;
import org.egov.infstr.models.State;
import org.egov.infstr.services.PersistenceService;
import org.egov.pims.commons.Position;
import org.egov.pims.model.DetOfEnquiryOfficer;
import org.egov.pims.model.DisciplinaryPunishment;
import org.egov.pims.model.EmployeeView;
import org.egov.pims.model.PersonalInformation;
import org.egov.pims.utils.EisConstants;
import org.egov.pims.utils.EisManagersUtill;
import org.egov.pims.workflow.disciplinaryPunishment.DisciplinaryPunishmentService;
import org.egov.web.actions.BaseFormAction;
import org.hibernate.Query;

import com.opensymphony.xwork2.validator.annotations.Validation;
 

/**
 * @author Jagadeesan *
 */
@ParentPackage("egov")
@Validation()
public class DisciplinaryPunishmentAction extends BaseFormAction {
	
	private static final long serialVersionUID = 1L;
	private DisciplinaryPunishment disciplinaryPunishment = new DisciplinaryPunishment();
	protected transient PersistenceService<DisciplinaryPunishment, Long> disciplinaryPunishmentPersistentService;
	private List<DisciplinaryPunishment> disciplinaryPunishmentList = new ArrayList<DisciplinaryPunishment>();
	private String mode="";
	private static final String VIEW="view";
	private static final String MODIFY ="modify";
	private static final String LIST ="list";
	private String comments;
	private String disEmpId;
	private String exceptionActionUrl; 
	private DisciplinaryPunishmentService disciplinaryPunishmentService;
	private String modifyType;
	private String typeOfDisciplinaryPunishmentWF;
	private String approverDept;
	private String approverDesig;
	private String approverEmpPositionId;
	private String approveOrReject="";
	private String wfStatus;
	private List<DetOfEnquiryOfficer> enqDetailsList = null;
	//private HttpServletResponse response;
	
	@Override
	public Object getModel() {
		return disciplinaryPunishment;
	}
	
	public DisciplinaryPunishmentAction() {
		addRelatedEntity("employeeId", PersonalInformation.class);
		addRelatedEntity("status", EgwStatus.class);
		addRelatedEntity("state", State.class); 
	}
	
	public String execute() 
	{
		return NEW;
	}
	
	public void prepare()
	{
		super.prepare(); 
		addDropdownData("departmentList", getPersistenceService().findAllBy("from DepartmentImpl order by deptName"));
		addDropdownData("designationList", getPersistenceService().findAllBy("from DesignationMaster order by designationName"));
		addDropdownData("approverEmpList", new ArrayList<EmployeeView>());
		
		if((getMode().equals("modify") && getModifyType().equals("menutree")) || (getMode().equals("view")) ||  (getMode().equals("create")) ){
			PersonalInformation pi = (PersonalInformation)getPersistenceService().getSession().load(PersonalInformation.class, Integer.valueOf(disEmpId));
			disciplinaryPunishment.setEmployeeId(pi);
		}/*else if(getMode().equals("modify") && getModifyType().equals("workflow") && getApproveOrReject().equals("")){
			DisciplinaryPunishment disciplinaryPunish =(DisciplinaryPunishment) getDisciplinaryPunishmentPersistentService().findById(Long.valueOf(disciplinaryPunishment.getId()), false);
			disciplinaryPunishment.setEmployeeId(disciplinaryPunish.getEmployeeId());
		}*/
		
		if(enqDetailsList!=null)
		{
			//To remove duplicates in list
			enqDetailsList.removeAll(Collections.singleton(null));
			for(DetOfEnquiryOfficer deo :enqDetailsList)
			{
				if(deo.getEmpId()!=null)
				{
					deo.setEmployeeId((PersonalInformation)getPersistenceService().getSession().load(PersonalInformation.class, deo.getEmpId()));
					deo.setDisciplinarypunishmentId(disciplinaryPunishment);
				}
			}
		}
		
		//To set the manual or auto workflow type
		String wftype = getDisciplinaryPunishmentService().typeOfDisciplinaryPunishmentWF();
		setTypeOfDisciplinaryPunishmentWF(wftype);
	}
	
	@SkipValidation
	public String loadToList()  //This called when loading the page before view or modify
	{
		disciplinaryPunishmentList = getDisciplinaryPunishmentPersistentService().findAllBy("from DisciplinaryPunishment where employeeId.idPersonalInformation=?", Integer.valueOf(disEmpId));
		return LIST; 
	}
	
	@SkipValidation
	public String loadToCreate()  //This will get called while the disciplinary punishment page gets loaded.
	{
		enqDetailsList = new ArrayList<DetOfEnquiryOfficer>();
		enqDetailsList.add(new DetOfEnquiryOfficer());
		return NEW;
	}
	
	@SkipValidation
	public String viewAfterSuccessful()  //This get called , once after disciplinary punishment created/modified.
	{
		enqDetailsList = new ArrayList<DetOfEnquiryOfficer>();
		Query qry = getPersistenceService().getSession().createQuery("from DetOfEnquiryOfficer where disciplinarypunishmentId=:punishmentId");
		qry.setEntity("punishmentId", disciplinaryPunishment);
		
		//dEnqOffList=disciplinaryPunishment.getEgpimsDetOfEnquiryOfficers();
		/*if(dEnqOffList.isEmpty())
		{
			enqDetailsList.add(new DetOfEnquiryOfficer()); 
		}*/
		enqDetailsList.addAll(qry.list());
		return VIEW;
	}
	
	@SkipValidation
	public String loadToViewOrModify()  //This get called when to load the page for view/modify.
	{
		String returnStr=VIEW;
		disciplinaryPunishment=(DisciplinaryPunishment) getDisciplinaryPunishmentPersistentService().findById(Long.valueOf(disciplinaryPunishment.getId()), false);
		enqDetailsList = new ArrayList<DetOfEnquiryOfficer>();
		Set<DetOfEnquiryOfficer> dEnqOffList = new HashSet<DetOfEnquiryOfficer>();
		Set<DetOfEnquiryOfficer> tmpDetEnqOffList = new HashSet<DetOfEnquiryOfficer>();
		dEnqOffList=disciplinaryPunishment.getEgpimsDetOfEnquiryOfficers();
		if(dEnqOffList.isEmpty())
		{
			enqDetailsList.add(new DetOfEnquiryOfficer()); 
		}
		else
		{
			//To remove duplicates in list
			enqDetailsList.removeAll(Collections.singleton(null));
			for(DetOfEnquiryOfficer dtl: dEnqOffList)
			{
				dtl.setEmpId(dtl.getEmployeeId().getIdPersonalInformation());
				dtl.setEnquiryOfficerCode(dtl.getEmployeeId().getCode());
				if(tmpDetEnqOffList.add(dtl)){
					enqDetailsList.add(dtl);
				}
			}
		}
		
		//To set the workflow status
		String status = disciplinaryPunishment.getState().getValue().toString();
		setWfStatus(status); 
		
		if(getMode().equals(MODIFY)){
			if(getModifyType().equals("workflow")){
				setExceptionActionUrl(getDisciplinaryPunishmentService().getActionUrlByPassingActionName(EisConstants.GENERATE_EXCEPTION_DISCIPLINARY,disciplinaryPunishment.getId().toString()));
			}
			returnStr=EDIT;
		}
			
		return returnStr;
	}
	
	/**
	 * To create the disciplinary punishment
	 * @return
	 * @throws IOException
	 * @throws ServletException
	 */
	public String create()  throws IOException,ServletException,Exception
	{
		//To set the enquiry details
		Set<DetOfEnquiryOfficer> dEnqOffList = new HashSet<DetOfEnquiryOfficer>();
		dEnqOffList.addAll(enqDetailsList);
		disciplinaryPunishment.setEgpimsDetOfEnquiryOfficers(dEnqOffList);
		//to generate application number.
		disciplinaryPunishment.setApplicationNumber(getDisciplinaryPunishmentService().generateApplicationNo());
		getDisciplinaryPunishmentPersistentService().create(disciplinaryPunishment);
		
		//To set the approved position
		setApproverPosition();
		
		//To create a workflow
		getDisciplinaryPunishmentService().createDisciplinaryPunishmentWorkFlow(disciplinaryPunishment);
		
		addActionMessage("Disciplinary Punishment Successfully Created.");
		
		setMode("view");
		return viewAfterSuccessful();
	}

	/**
	 * Purpose : Only to edit the disciplinary punishment
	 * @return
	 * @throws IOException
	 * @throws ServletException
	 * @throws Exception
	 */

	public void edit()  throws IOException,ServletException,Exception
	{
		disciplinaryPunishment.getEgpimsDetOfEnquiryOfficers().addAll(enqDetailsList);
		Query qry = getPersistenceService().getSession().createQuery(" delete from DetOfEnquiryOfficer where disciplinarypunishmentId=:punishmentId");
		qry.setEntity("punishmentId", disciplinaryPunishment);
		qry.executeUpdate();
		getDisciplinaryPunishmentPersistentService().update(disciplinaryPunishment);
	}
	
	/**
	 * Purpose : Only to edit the disciplinary punishment
	 * @return
	 * @throws IOException
	 * @throws ServletException
	 * @throws Exception
	 */
	public String editDisciplinaryPunishmentOnly()  throws IOException,ServletException,Exception
	{
		//To edit the disciplinary punishment
		edit();
		
		addActionMessage("Disciplinary Punishment Modified Successfully.");
		setMode("view");
		return viewAfterSuccessful();
	}
	
	
	/**
	 * Purpose : To create payslip exception and approve.
	 * @return
	 * @throws IOException
	 * @throws ServletException 
	 */
	public void createExceptionAndApprove()  throws IOException,ServletException,Exception
	{
		edit(); 
		String url = getExceptionActionUrl(); 
		ServletActionContext.getResponse().sendRedirect(url);
	}
	
	/**
	 * Purpose : To approve the disciplinary punishment
	 * @return
	 * @throws IOException
	 * @throws ServletException 
	 */
	public String modifyAndApproveWithoutException()  throws IOException,ServletException,Exception
	{
/*		User user = EisManagersUtill.getUserManager().getUserByID(Integer.valueOf(EGOVThreadLocals.getUserId()));
		Position position = EisManagersUtill.getEisCommonsManager().getPositionByUserId(user.getId());
		if(position==null) {
			throw new EGOVRuntimeException("Logged in user do not have position assigned. So this user cannot able to approve disciplinary");
		}
		else{
			
			//To update the current user position
			disciplinaryPunishment.setApproverPos(position);*/
			//To update a workflow
			
		//To edit the disciplinary punishment
		edit();
		
		//To set the approved position
		if(getMode().equals("modify") && getModifyType().equals("workflow") && getWfStatus().equals("APPROVER_REJECTED") ){
			setApproverPosition();
		}
		
		getDisciplinaryPunishmentService().updateDisciplinaryPunishmentWorkFlow(disciplinaryPunishment,getApproveOrReject(),comments);

		getActionMessages().clear();
		if(disciplinaryPunishment.getStatus().getCode().equals("Approved")){
			addActionMessage("Disciplinary Punishment Successfully Modified And Approved without Payslip Exception");
		}
		else{
			addActionMessage("Disciplinary Punishment Successfully Modified And Approved");
		}
		setMode("view");
		//}
		return viewAfterSuccessful();
	}
	
	@SkipValidation
	public String rejectDisciplinaryPunishment()
	{
		disciplinaryPunishment=(DisciplinaryPunishment) getDisciplinaryPunishmentPersistentService().findById(Long.valueOf(disciplinaryPunishment.getId()), false);
		
		if(!getApproveOrReject().equals("") && getApproveOrReject().equals("reject")){
			//If approver rejected, will go to creator inbox. If creator reject will end the workflow.
			getDisciplinaryPunishmentService().updateDisciplinaryPunishmentWorkFlow(disciplinaryPunishment,getApproveOrReject(),comments);
		}
		addActionMessage("Disciplinary Punishment Successfully Rejected.");
		setMode("view");
		return viewAfterSuccessful();
	}
	
	public void validate()
	{
		if(disciplinaryPunishment.getNatureOfAlligations().equals("") || disciplinaryPunishment.getNatureOfAlligations()==null)
			addFieldError("natureOfAlligations", "Nature of allegation is empty");
		if(disciplinaryPunishment.getChargeMemoNo().equals("") || disciplinaryPunishment.getChargeMemoNo()==null)
			addFieldError("chargeMemoNo", "Charge Memo Number is empty");
		if(disciplinaryPunishment.getChargeMemoDate()==null)
			addFieldError("chargeMemoDate", "Charge Memo Date is empty");
		if(disciplinaryPunishment.getChargeMemoServedDate()==null)
			addFieldError("chargeMemoServedDate", "Charge Memo Served Date is empty");
		if(disciplinaryPunishment.getChargeMemoDate()!=null && disciplinaryPunishment.getChargeMemoServedDate()!=null){
			if(disciplinaryPunishment.getChargeMemoDate().after(disciplinaryPunishment.getChargeMemoServedDate()))
			{
				addFieldError("chargeMemoDate", "Charge Memo Date should not be greater than Charge Memo Served Date");
			}
		}
		if(disciplinaryPunishment.getIsUnauthorisedAbsent()=='1')
		{
			if(disciplinaryPunishment.getAbsentFrom()==null)
			{
				addFieldError("absentFrom", "Absent From Date is empty");
			}
			if(disciplinaryPunishment.getAbsentTo()==null)
			{
				addFieldError("absentTo", "Absent To Date is empty");
			}
			
			if(disciplinaryPunishment.getAbsentFrom()!=null && disciplinaryPunishment.getAbsentTo()!=null){
				if(disciplinaryPunishment.getAbsentFrom().compareTo(disciplinaryPunishment.getAbsentTo())>0){
					addFieldError("absentFrom", "Period of Absent's From Date should not be greater than To Date");
				}
			}
		}
		
		if(disciplinaryPunishment.getWhetherSuspended()=='1')
		{
			if(disciplinaryPunishment.getDateOfSuspension()==null)
			{
				addFieldError("dateOfSuspension", "Suspended From Date is empty");
			}
			
			if(disciplinaryPunishment.getDateOfSuspension()!=null && disciplinaryPunishment.getDateOfReinstatement()!=null){
				if(disciplinaryPunishment.getDateOfSuspension().compareTo(disciplinaryPunishment.getDateOfReinstatement())>0){
					addFieldError("dateOfSuspension", "Period of Suspendend's From Date should not be greater than To Date");
				}
			}
		}
		
		if(enqDetailsList.size()>0)
		{
			boolean isEnquiryOffCodeEmpty=false;
			//To check enquiry details mandatory
			for(DetOfEnquiryOfficer deo : enqDetailsList)
			{
				if(deo!=null && (deo.getEmpId()==null || deo.getEnquiryOfficerCode()==null))
				{
					addFieldError("empId", "Enquiry Officer Code is empty");
					isEnquiryOffCodeEmpty=true;
					break;
				}
			}
			
			if(!isEnquiryOffCodeEmpty){
				//To check whether enquiry officer is same as for whom disciplinary action is created.
				for(DetOfEnquiryOfficer deo : enqDetailsList)
				{
					if(deo.getEnquiryOfficerCode().equals(disciplinaryPunishment.getEmployeeId().getCode()))
					{
						addFieldError("empId", "Enquiry Officer Code should not be same as to whom disciplinary punishment being created");
						break;
					}
				}
			}
		}
		
		//To check for approval mandatory
		if( getMode().equals("create") || (getMode().equals("modify") && getModifyType().equals("workflow") && getWfStatus().equals("APPROVER_REJECTED") && getApproveOrReject().equals("approve"))){ 
			if(getTypeOfDisciplinaryPunishmentWF().equals("Manual"))
			{
				if(getApproverDept().equals(""))
				{
					addFieldError("approverDept", "Please select Approver Department");
				}
				if(getApproverDesig().equals(""))
				{
					addFieldError("approverDesig", "Please select Approver Designation");
				}
				if(getApproverEmpPositionId().equals(""))
				{
					addFieldError("approverEmpPositionId", "Please select Approver Employee");
				}
			}
		}
		
		//To check the sanction no mandatory
		if(getMode().equals("modify") && getModifyType().equals("workflow") && getWfStatus().equals("CREATED")){
			if(disciplinaryPunishment.getSanctionNo().equals("")){
				addFieldError("sanctionNo", "Sanction No. is empty");
			}
		}
	}
	
	public void setApproverPosition()
	{
		//To set the approved position
		if(approverEmpPositionId != null){
			Position approverPosition = EisManagersUtill.getEisCommonsService().getPositionById(Integer.valueOf(approverEmpPositionId));;
			disciplinaryPunishment.setApproverPos(approverPosition);
		}
	}
	
	public DisciplinaryPunishmentService getDisciplinaryPunishmentService() {
		return disciplinaryPunishmentService;
	}
	
	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode.toLowerCase();
	}
	
	protected String getMessage(final String key) {
		return getText(key);
	}

	public DisciplinaryPunishment getDisciplinaryPunishment() {
		return disciplinaryPunishment;
	}

	public void setDisciplinaryPunishment(
			DisciplinaryPunishment disciplinaryPunishment) {
		this.disciplinaryPunishment = disciplinaryPunishment;
	}

	public String getDisEmpId() {
		return disEmpId;
	}

	public void setDisEmpId(String disEmpId) {
		this.disEmpId = disEmpId;
	}

	public List<DisciplinaryPunishment> getDisciplinaryPunishmentList() {
		return disciplinaryPunishmentList;
	}

	public void setDisciplinaryPunishmentList(
			List<DisciplinaryPunishment> disciplinaryPunishmentList) {
		this.disciplinaryPunishmentList = disciplinaryPunishmentList;
	}
	
	public String getModifyType() {
		return modifyType;
	}

	public void setModifyType(String modifyType) {
		this.modifyType = modifyType;
	}

	public String getTypeOfDisciplinaryPunishmentWF() {
		return typeOfDisciplinaryPunishmentWF;
	}

	public void setTypeOfDisciplinaryPunishmentWF(
			String typeOfDisciplinaryPunishmentWF) {
		this.typeOfDisciplinaryPunishmentWF = typeOfDisciplinaryPunishmentWF;
	}

	public String getApproverEmpPositionId() {
		return approverEmpPositionId;
	}

	public void setApproverEmpPositionId(String approverEmpPositionId) {
		this.approverEmpPositionId = approverEmpPositionId;
	}

	public String getApproverDept() {
		return approverDept;
	}

	public String getApproverDesig() {
		return approverDesig;
	}

	public void setApproverDept(String approverDept) {
		this.approverDept = approverDept;
	}

	public void setApproverDesig(String approverDesig) {
		this.approverDesig = approverDesig;
	}

	public String getApproveOrReject() {
		return approveOrReject;
	}

	public void setApproveOrReject(String approveOrReject) {
		this.approveOrReject = approveOrReject;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getWfStatus() {
		return wfStatus;
	}

	public void setWfStatus(String wfStatus) {
		this.wfStatus = wfStatus;
	}

	public String getExceptionActionUrl() {
		return exceptionActionUrl;
	}

	public void setExceptionActionUrl(String exceptionActionUrl) {
		this.exceptionActionUrl = exceptionActionUrl;
	}

	public List<DetOfEnquiryOfficer> getEnqDetailsList() {
		return enqDetailsList;
	}

	public void setEnqDetailsList(List<DetOfEnquiryOfficer> enqDetailsList) {
		this.enqDetailsList = enqDetailsList;
	}
	
	public void setDisciplinaryPunishmentService(
			DisciplinaryPunishmentService disciplinaryPunishmentService) {
		this.disciplinaryPunishmentService = disciplinaryPunishmentService;
	}
	
	public PersistenceService<DisciplinaryPunishment, Long> getDisciplinaryPunishmentPersistentService() {
		return disciplinaryPunishmentPersistentService;
	}

	public void setDisciplinaryPunishmentPersistentService(
			PersistenceService<DisciplinaryPunishment, Long> disciplinaryPunishmentPersistentService) {
		this.disciplinaryPunishmentPersistentService = disciplinaryPunishmentPersistentService;
	}

	/*public void setServletResponse(HttpServletResponse response){
	    this.response = response;
	}

	public HttpServletResponse getServletResponse(){
		return response;
	}*/
}