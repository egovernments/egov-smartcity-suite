package org.egov.pims.empLeave.client;



import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.actions.DispatchAction;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.commons.CFinancialYear;
import org.egov.commons.dao.CommonsDaoFactory;
import org.egov.commons.dao.FinancialYearDAO;
import org.egov.commons.service.CommonsService;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.lib.rjbac.user.User;
import org.egov.pims.commons.Position;
import org.egov.pims.commons.client.FinancialYearFailureException;
import org.egov.pims.dao.StatusMasterDAO;
import org.egov.pims.empLeave.model.Attendence;
import org.egov.pims.empLeave.model.AttendenceType;
import org.egov.pims.empLeave.model.CalendarYear;
import org.egov.pims.empLeave.model.LeaveApplication;
import org.egov.pims.empLeave.model.LeaveApproval;
import org.egov.pims.empLeave.model.TypeOfLeaveMaster;
import org.egov.pims.empLeave.service.EmpLeaveService;
import org.egov.pims.model.Assignment;
import org.egov.pims.model.PersonalInformation;
import org.egov.pims.model.StatusMaster;
import org.egov.pims.service.EmployeeService;
import org.egov.pims.utils.EisConstants;
import org.egov.pims.utils.EisManagersUtill;
import org.egov.pims.workflow.leaveApplication.LeaveApplicationService;

/**
 * 
 * @author Deepak,DivyaShree
 *
 */
public class AfterLeaveAction extends DispatchAction
{
	public final static Logger LOGGER = Logger.getLogger(AfterLeaveAction.class.getClass());
	private EmployeeService employeeService;
	private CommonsService commonsService; 
	private EmpLeaveService eisLeaveService;
	LeaveApplicationService leaveService;
	
	public void setLeaveService(LeaveApplicationService leaveService)
	{
		
		this.leaveService =  leaveService;
	}
	
	protected boolean returnToken(HttpServletRequest req)
	{
		
		if(!isTokenValid(req, true))
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	protected boolean returnIsSelfApproval()
	{
		if(EisManagersUtill.getEmpLeaveService().isSelfApproval())
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	protected boolean returnIsCalendarBased()
	{
		if(EisManagersUtill.getEmpLeaveService().isLeaveCalendarBased())
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	public ActionForward saveLeaveApplication(ActionMapping mapping,ActionForm form,HttpServletRequest req,HttpServletResponse res)
																									throws IOException,ServletException{
		String target =null;
		String alertMessage=null;
		ActionMessages messages = new ActionMessages();
		LeaveForm leaveForm = (LeaveForm)form;
		int iEncashment=leaveForm.getEncashment();
		String isEncashment=String.valueOf(iEncashment);
		LeaveApplication leaveApplication = new LeaveApplication();		
		SimpleDateFormat sqlUtil = new SimpleDateFormat("dd-MMM-yyyy",Locale.getDefault());
		Integer empId = Integer.valueOf(leaveForm.getEmpId());//Integer.valueOf(req.getParameter("empId"));
		Integer userId =(Integer) req.getSession().getAttribute("com.egov.user.LoginUserId");	
		if (returnToken(req))
		{
		    target = STR_ERROR;
		}
		else
		{
			String appNo = "";
			try
			{				
				PersonalInformation personalInformation =null;
				User createdBy =null;
				if( empId!=null)
				{
					personalInformation = employeeService.getEmloyeeById(empId);
					createdBy = EisManagersUtill.getUserService().getUserByID(userId);					
				}
				if(personalInformation!=null)
				{
						String typeOfLeaveMstr = null;
						String fromDate = null;
						String toDate = null;
						String workingDays = null;
						String reason = null;
						String hLeave = null;
						//For Manual WorkFlow
						String deptId = null;
						String desgId=null;
						String posId = null;
						deptId = leaveForm.getApproverDept();
						desgId = leaveForm.getApproverDesig();
						posId = leaveForm.getApproverEmpAssignmentId();
						String availableLeaves = leaveForm.getAvailableLeaves();						
						typeOfLeaveMstr = leaveForm.getTypeOfLeaveMstr();						
						hLeave = leaveForm.getTwoHdLeaves();
						fromDate = leaveForm.getFromDate();
						toDate = leaveForm.getToDate();
						workingDays = leaveForm.getWorkingDays();
						reason = leaveForm.getReason();
						User user = EisManagersUtill.getUserService().getUserByID(userId);
						LOGGER.info(user);
						if(!"0".equals(typeOfLeaveMstr))
						{
							TypeOfLeaveMaster typeOfLeaveMaster =EisManagersUtill.getEmpLeaveService().getTypeOfLeaveMasterById(Integer.valueOf(typeOfLeaveMstr));
							leaveApplication.setTypeOfLeaveMstr(typeOfLeaveMaster);
						}
						if(fromDate!=null&&!fromDate.equals(""))
						{
							leaveApplication.setFromDate(getDateString(fromDate));							
							if(returnIsCalendarBased())
							{
								SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy",Locale.getDefault());
								Date fromDte=sdf.parse(fromDate);
								String calId=EisManagersUtill.getEmpLeaveService().getYearIdByGivenDate(sqlUtil.format(fromDte));
								CalendarYear calendarYear=EisManagersUtill.getEmpLeaveService().getCalendarYearById(Long.valueOf(calId));
								//set calendar id
								leaveApplication.setCalYear(calendarYear);
								//set financial id null
								leaveApplication.setFinancialY(null);
							}
							else
							{
								CFinancialYear cFinancialYear =EisManagersUtill.getFinYearForGivenDate(getDateString(fromDate));
								//set financial id
								leaveApplication.setFinancialY(cFinancialYear);
								//set calendar id as null
								leaveApplication.setCalYear(null);
							}
						}
						/*
						 *  change 
						 *  set Finacial year for current date  in case of encashment
						 */
						else
						{							
							Calendar cal = Calendar.getInstance();
					        int day = cal.get(Calendar.DATE);
					        int month = cal.get(Calendar.MONTH) + 1;
					        int year = cal.get(Calendar.YEAR);
							String strDate=day+"/"+month+"/"+year;
							SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy",Locale.getDefault());
							sqlUtil = new SimpleDateFormat("dd-MMM-yyyy",Locale.getDefault());
							Date dat = sdf.parse(strDate);							
							if(returnIsCalendarBased())
							{
								String calId=EisManagersUtill.getEmpLeaveService().getYearIdByGivenDate(sqlUtil.format(dat));
								CalendarYear calendarYear=EisManagersUtill.getEmpLeaveService().getCalendarYearById(Long.valueOf(calId));
								leaveApplication.setCalYear(calendarYear);
							}
							else
							{
								CFinancialYear cFinancialYear =EisManagersUtill.getFinYearForGivenDate(dat);
								leaveApplication.setFinancialY(cFinancialYear);
							}
						}
						if(toDate!=null&&!toDate.equals(""))
						{
							leaveApplication.setToDate(getDateString(toDate));
							
						}	
						if(workingDays!=null&&!workingDays.equals(""))
						{
							if(hLeave != null && hLeave.equals("1"))
							{
								//leaveApplication.setWorkingDays(new Integer(2));
								leaveApplication.setWorkingDays(Integer.valueOf(workingDays));
							}
							else
							{
								leaveApplication.setWorkingDays(Integer.valueOf(workingDays));
								
							}
						}
						/*
						 * Setting for isEncashment 
						 * set 1 for encashment 
						 * set 0 for other leave type
						 * in case of Encashment
						 * set Reason to Encashment
						 */
						if(isEncashment!=null && isEncashment!="")
						{
							
							leaveApplication.setIsEncashment(Integer.parseInt(isEncashment));
							
							leaveApplication.setReason("Encashment");
						}
						else
						{
							leaveApplication.setIsEncashment(Integer.valueOf(0));
						}
						
						
						/*if(strWorkingdays!=null && strWorkingdays!="")
						{
							leaveApplication.setWorkingDays(Integer.parseInt(strWorkingdays));
						}*/
						
						if(reason!=null&&!reason.equals(""))
						{
							leaveApplication.setReason(reason.toString().trim());
						}
						 
						if(availableLeaves!=null&&!availableLeaves.equals(""))
						{
							leaveApplication.setNoOfLeavesAvai(new Float(availableLeaves));
							//leaveApplication.setNoOfLeavesAvai(new Float(leaveForm.getAvailableLeaves())-new Float(leaveForm.getWorkingDays()));
						}
						if(hLeave != null && hLeave.equals("1"))
						{
							leaveApplication.setTwoHdLeaves(Character.valueOf('1'));
						}
						else
						{
							leaveApplication.setTwoHdLeaves(Character.valueOf('0'));
						}

						leaveApplication.setDesigId(employeeService.getPresentDesignation(personalInformation.getIdPersonalInformation()));
						StatusMasterDAO statusMasterDAO =new StatusMasterDAO();
						/*
						 * New Change for Single step approve
						 * set STATUS_APPLIED to STATUS_APPROVED 
						 * approve for one shot
						 */
						
						
						StatusMaster statusMaster = null;
						if(returnIsSelfApproval())
						{
						  statusMaster =statusMasterDAO.getStatusMaster(EisConstants.STATUS_APPROVED);
						  leaveApplication.setStatusId(statusMaster);
						    /*
							 * set sanction for single step approval
							 */						
							leaveApplication.setSanctionNo(getSanctionNo(employeeService.getEmloyeeById(empId)));
							
						}
						else
						{
							  statusMaster =statusMasterDAO.getStatusMaster(EisConstants.STATUS_APPLIED);
							  leaveApplication.setStatusId(statusMaster);
						}
						
						
						appNo = leaveForm.getApplicationNumber();
						
						leaveApplication.setApplicationNumber(appNo);
						leaveApplication.setEmployeeId(personalInformation);
						//leaveApplication.setCreatedBy(createdBy);
						
						/*
						 * will be save from workflow if config is Y
						 */
						if(returnIsSelfApproval())
						{
							eisLeaveService.createLeaveApplication(leaveApplication);
						}
						else
						{
							if(leaveService!=null)
							{
								boolean isManual = EisManagersUtill.getEmpLeaveService().isLeaveWfAutoOrManaul();
								if(isManual)
								{
									Position approverPosition = null;
									if(posId!=null && !"".equals(posId))
									{
										approverPosition = EisManagersUtill.getEisCommonsService().getPositionById(Integer.valueOf(posId));
									}
									if(approverPosition!=null)
									{
									  leaveApplication.setApproverPos(approverPosition);
									}
								}
								leaveService.createLeaveApplicationWorkFlow(leaveApplication);
							}
						}
						
						EisManagersUtill.getEmpLeaveService().addLeaveApplication(personalInformation,leaveApplication);
						

						/*
						 * New change
						 * set leave Approval only if config is Y
						 */	
						if(returnIsSelfApproval())
						{
							LeaveApproval leaveApproval = new LeaveApproval();
							leaveApproval.setApprovedBy(user);
							leaveApproval.setAppId(leaveApplication);
												
							// Set pay eligible to 1 (Yes) by default
							leaveApproval.setPayElegible(Character.valueOf('1'));
							EisManagersUtill.getEmpLeaveService().addLeaveApproval(leaveApproval);	
						}
						
						// TODO : If Leave Type is EL and isEncashment is true then dont populate attendance										
						if(!isEncashment.equals(EisConstants.LEAVE_ENCASHMENT_VALUE))
						{
							LOGGER.info("isEncashment--->>>>"+isEncashment);
							populateAttendence(leaveApplication);
						}
												
				}
				req.setAttribute("approval", leaveApplication.getSanctionNo());
				target = ("createScreenLeaveApplication"+(leaveForm.getEss()==null || leaveForm.getEss().trim().isEmpty()?"":"Ess")); 
				if(returnIsSelfApproval())
				{
				 alertMessage="LeaveApproval Executed successfully ";
				}
				else
				{
					alertMessage="Applied Leave successfully";
				}
				
		}
			catch(FinancialYearFailureException e)
		    {	  
				target = "userMsg";
				LOGGER.info("FinancialYearFailureException!!!"+e.getMessage());
				
					       
		    }
			catch(EGOVRuntimeException e){
				ActionMessage message = new ActionMessage("errors.message",e.getMessage()+" There is no superior position");			
	            messages.add( ActionMessages.GLOBAL_MESSAGE, message );
	            saveMessages( req, messages );
		        LOGGER.error("Exception $ now-------> "+e.getMessage());
				throw new EGOVRuntimeException(e.getMessage(),e);
			
			}
		catch(Exception ex)
		{
		   target = STR_ERROR;
		   LOGGER.error("Exception Encountered!!!"+ex.getMessage());
		   throw new EGOVRuntimeException(STR_EXCEPTION + ex.getMessage(),ex);
		}	   
	}
	req.setAttribute("alertMessage", alertMessage);
	req.setAttribute("leaveApplication",leaveApplication);
	return mapping.findForward(target);
}

	public ActionForward modifyLeaveApplication(ActionMapping mapping,ActionForm form,HttpServletRequest req,HttpServletResponse res)
	throws IOException,ServletException
	{
		
	String target =null;
	
	String alertMessage=null;
	
	if (returnToken(req))
	{
	    target = STR_ERROR;
	}
	else
	{
	try
	{	
	LeaveApplication leaveApplication = null;
	String id = "";
	if(req.getParameter("LeaveapplicationId") != null)
	{
		id = req.getParameter("LeaveapplicationId").trim();
	}
	if( id!=null&&!id.equals(""))
	{
		leaveApplication =EisManagersUtill.getEmpLeaveService().getLeaveApplicationById(Integer.valueOf(id));
	}
	if(leaveApplication!=null)
	{			
				
				LeaveForm leaveForm = (LeaveForm)form;
				String typeOfLeaveMstr = null;
				String fromDate = null;
				String toDate = null;
				String workingDays = null;
				String reason = null;
				String hLeave = null;
				hLeave = leaveForm.getTwoHdLeaves();
				String availableLeaves = leaveForm.getAvailableLeaves();
				
				
				typeOfLeaveMstr = leaveForm.getTypeOfLeaveMstr();
				fromDate = leaveForm.getFromDate();
				toDate = leaveForm.getToDate();
				workingDays = leaveForm.getWorkingDays();
				reason = leaveForm.getReason();
				
				int iEncashment=leaveForm.getEncashment();
				String isEncashment=String.valueOf(iEncashment);
				
				if(!"0".equals(typeOfLeaveMstr))
				{
					TypeOfLeaveMaster typeOfLeaveMaster =EisManagersUtill.getEmpLeaveService().getTypeOfLeaveMasterById(Integer.valueOf(typeOfLeaveMstr));
					leaveApplication.setTypeOfLeaveMstr(typeOfLeaveMaster);
					
				}
				
				
				if(fromDate!=null && !"".equals(fromDate))
				{
					
					leaveApplication.setFromDate(getDateString(fromDate));
				}
				/*
				 * Modify from other leave type to encashment
				 * set null for from date 
				 */
				else
				{
					
					leaveApplication.setFromDate(null);
				}
				if(toDate!=null && !"".equals(toDate))
				{
					
					leaveApplication.setToDate(getDateString(toDate));
					
				}
				/*
				 * Modify from other leave type to encashment
				 * Set null for toDate
				 */
				else
				{
					
					leaveApplication.setToDate(null);
				}
				
				
				
				
				
				/*
				 * Setting for isEncashment
				 * in case of encashment
				 * set reason as Encashment 
				 * 
				 */
				if(isEncashment!=null && isEncashment!="")
				{
					
					leaveApplication.setIsEncashment(Integer.parseInt(isEncashment));
					
					leaveApplication.setReason("Encashment");
					
					
				}
				
				/*
				 * New Change for Modify 
				 * set STATUS to STATUS_CANCELLED 
				 * 
				 */
				StatusMasterDAO statusMasterDAO =new StatusMasterDAO();
				StatusMaster statusMaster=statusMasterDAO.getStatusMaster(EisConstants.STATUS_CANCELLED);
				if(statusMaster!=null)
				{
					leaveApplication.setStatusId(statusMaster);
					
				}
				populateAttendence(leaveApplication);
				
				
				
				if(workingDays!=null&&!workingDays.equals(""))
				{
					if(hLeave != null && hLeave.equals("1"))
					{
						leaveApplication.setWorkingDays(Integer.valueOf(2));
					}
					else
					{
						leaveApplication.setWorkingDays(Integer.valueOf(workingDays));
						
					}
				}
				if(reason!=null&&!reason.equals(""))
				{
					leaveApplication.setReason(reason.toString().trim());
				}
				if(availableLeaves!=null&&!availableLeaves.equals(""))
				{
					leaveApplication.setNoOfLeavesAvai(new Float(availableLeaves));
					
				}
				if(hLeave != null && hLeave.equals("1"))
				{
					leaveApplication.setTwoHdLeaves(new Character('1'));
				}
				else
				{
					leaveApplication.setTwoHdLeaves(new Character('0'));
				}

				
				EisManagersUtill.getEmpLeaveService().updateLeaveApplication(leaveApplication);
				
				
	}
			//req.setAttribute("application", sanctionNo);
			
			target = "successModify";
			alertMessage="Cancelled LeaveApplication successfully";

	}
	catch(Exception ex)
		{
		   target = STR_ERROR;
		   LOGGER.info("Exception Encountered!!!"+ex.getMessage());
		   throw new EGOVRuntimeException(STR_EXCEPTION + ex.getMessage(),ex);
		}
	}
		req.setAttribute("alertMessage", alertMessage);
		return mapping.findForward(target);
	}

	public ActionForward rejectLeaveApplication(ActionMapping mapping,ActionForm form,HttpServletRequest req,HttpServletResponse res)
	throws IOException,ServletException
	{
		
	String target =null;
	
	String alertMessage=null;
	
	if (returnToken(req))
	{
	    target = STR_ERROR;
	}
	else
	{
	try
	{	
	LeaveApplication leaveApplication = null;
	String id = "";
	if(req.getParameter("LeaveapplicationId") != null)
	{
		id = req.getParameter("LeaveapplicationId").trim();
	}
	if( id!=null&&!id.equals(""))
	{
		leaveApplication =EisManagersUtill.getEmpLeaveService().getLeaveApplicationById(Integer.valueOf(id));
	}
	if(leaveApplication!=null)
	{			
				
				
				
				
				/*
				 * New Change for Modify 
				 * set STATUS to STATUS_CANCELLED 
				 * 
				 */
				StatusMasterDAO statusMasterDAO =new StatusMasterDAO();
				
				StatusMaster statusMaster=statusMasterDAO.getStatusMaster(EisConstants.STATUS_CANCELLED);
				leaveApplication.setStatusId(statusMaster);
				//populating Attendance from L to P 
				if(leaveApplication.getIsEncashment().intValue()!=1)
				{
					populateAttendence(leaveApplication);
				}
				
				leaveService.updateLeaveApplication(leaveApplication, "user_reject", leaveApplication.getCreatedBy().getFirstName());
				
					
	}
			//req.setAttribute("application", sanctionNo);
			
			target = "successReject";
			alertMessage="Cancelled LeaveApplication successfully";

	}
	catch(Exception ex)
		{
		   target = STR_ERROR;
		   LOGGER.info("Exception Encountered!!!"+ex.getMessage());
		   throw new EGOVRuntimeException(STR_EXCEPTION + ex.getMessage(),ex);
		}
	}
		req.setAttribute("alertMessage", alertMessage);
		return mapping.findForward(target);
	}
	
	
	public ActionForward saveLeaveApproval(ActionMapping mapping,ActionForm form,HttpServletRequest req,HttpServletResponse res)
	throws IOException,ServletException
	{
		String target =null;
		String alertMessage=null;
		String workingDays = null;
		LeaveForm leaveForm = (LeaveForm)form;
		int iEncashment=leaveForm.getEncashment();
		String isEncashment=String.valueOf(iEncashment);
		workingDays = leaveForm.getWorkingDays();
		
		String payElegible = leaveForm.getPayElegible();
		String applicationId = leaveForm.getLeaveAppId();
		Integer userId =(Integer) req.getSession().getAttribute("com.egov.user.LoginUserId");
		User user = EisManagersUtill.getUserService().getUserByID(userId);
		LeaveApplication leaveApplication = EisManagersUtill.getEmpLeaveService().getLeaveApplicationById(Integer.valueOf(applicationId));
		
		String rson = leaveForm.getReason();
		if(rson!=null && !rson.equals("")){
			leaveApplication.setReason(rson.toString().trim());
		}
		
		if (returnToken(req))
		{
		    target = "success";
		}
		else
		{
		try
		{
			LeaveApproval leaveApproval ;
			
			if(EisManagersUtill.getEmpLeaveService().getLeaveApprovalByApplicationID(Integer.valueOf((int)leaveApplication.getId().longValue()))!=null)
			{
		
				leaveApproval =EisManagersUtill.getEmpLeaveService().getLeaveApprovalByApplicationID(Integer.valueOf((int)leaveApplication.getId().longValue()));
			}
			else
			{
				leaveApproval = new LeaveApproval();
				
			}
			leaveApproval.setApprovedBy(user);
			leaveApproval.setAppId(leaveApplication);
			if(payElegible!=null && !payElegible.equals(""))
			{
				
			if("Y".equals(payElegible))
			{
				leaveApproval.setPayElegible(Character.valueOf('1'));
			}
			else
			{
				leaveApproval.setPayElegible(Character.valueOf('0'));
			}
			}
			
			
				
			/*
			 * set for working days
			 */
			if(workingDays!=null&&!workingDays.equals(""))
			{
				
					leaveApplication.setWorkingDays(Integer.valueOf(workingDays));
					
				
			}
			
			
			
			StatusMaster statusMaster =null;
			StatusMasterDAO statusMasterDAO = new StatusMasterDAO();
			LOGGER.info("statusMaster>>"+req.getParameter("status").trim());
			
			statusMaster = statusMasterDAO.getStatusMaster(req.getParameter("status").trim());
			
			if(statusMaster!=null)
			{
				leaveApplication.setStatusId(statusMaster);
			}
			
			leaveApplication.setSanctionNo(getSanctionNo(leaveApplication.getEmployeeId()));
			
			if(EisManagersUtill.getEmpLeaveService().getLeaveApprovalByApplicationID(Integer.valueOf((int)leaveApplication.getId().longValue()))!=null)
			{
				EisManagersUtill.getEmpLeaveService().updateLeaveApproval(leaveApproval);	
				
				if(returnIsSelfApproval())
				{
					
					EisManagersUtill.getEmpLeaveService().updateLeaveApplication(leaveApplication);
				}
				else
				{
					if(leaveService!=null)
					{
						leaveService.updateLeaveApplication(leaveApplication, "user_approve", leaveApplication.getCreatedBy().getFirstName());
					}
				}
			}
			else
			{
				
				EisManagersUtill.getEmpLeaveService().addLeaveApproval(leaveApproval);			
				
				if(returnIsSelfApproval())
				{
					EisManagersUtill.getEmpLeaveService().updateLeaveApplication(leaveApplication);
				}
				else
				{
					if(leaveService!=null)
					{
						leaveService.updateLeaveApplication(leaveApplication, "user_approve", leaveApplication.getCreatedBy().getFirstName());
					}
				}
				
			}			
			HibernateUtil.getCurrentSession().flush();
			// TODO : If Leave Type is EL and isEncashment is true then dont populate attendance
			
			LOGGER.info("isEncashment--->>>>"+isEncashment);
			
				if(leaveApplication.getIsEncashment().intValue()!=1)
				{
					populateAttendence(leaveApplication);
				}
				
			
			req.setAttribute("approval", leaveApplication.getSanctionNo());
			target = "successApproval";
			alertMessage="Executed saving LeaveApproval successfully";
			
	}catch(Exception ex)
	{
	   target = STR_ERROR;
	   LOGGER.info("Exception Encountered*********>>>>>"+ex.getMessage());
	   throw new EGOVRuntimeException(STR_EXCEPTION + ex.getMessage(),ex);
	}
	}
    
	req.setAttribute("alertMessage", alertMessage);
	return mapping.findForward(target);
	}
	
	
		private java.util.Date getDateString(String dateString)
	{

		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy",Locale.getDefault());
		java.util.Date d =null;
		try
		{
			d = dateFormat.parse(dateString);

		} catch (Exception e)
		{
			
			throw new EGOVRuntimeException(STR_EXCEPTION + e.getMessage(),e);
		}
		return d;
}
	
	private String getSanctionNo(PersonalInformation personalInformation)
	{
		String sNo=null;
		try {
			FinancialYearDAO finDAO= CommonsDaoFactory.getDAOFactory().getFinancialYearDAO();
			Assignment assignment = employeeService.getLatestAssignmentForEmployee(personalInformation.getIdPersonalInformation());
			String dept  = assignment.getDeptId().getDeptName();
			String finId = finDAO.getCurrYearFiscalId();
			CFinancialYear financialYear = commonsService.findFinancialYearById(Long.valueOf(finId));
			String finYear = financialYear.getFinYearRange();
			Integer seqNum = EisManagersUtill.getEmpLeaveService().getNextVal();
			sNo= finYear+"/"+dept+"/Leave/"+seqNum;
		} catch (Exception e) {
			
			
			throw new EGOVRuntimeException(STR_EXCEPTION + e.getMessage(),e);
		}
		return sNo;

	}
	
	private void  populateAttendence(LeaveApplication leaveApplication ) throws Exception
	{
		
		try 
		{			
			/**
			 *  Set the attendance type as Paid or Unpaid leaves based on whether Leave status = approved, payEligible=1
			 *  and available leaves >= No. of days leave applied.
			 */
			LOGGER.info("Inside populateAttendence \n Status = "+leaveApplication.getStatusId().getName());
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd",Locale.getDefault());
			List wDays = EisManagersUtill.getEmpLeaveService().getNoOfWorkingDaysBweenTwoDatesForEmployee(leaveApplication.getFromDate(),
																			leaveApplication.getToDate(),leaveApplication.getEmployeeId());
			Attendence attCrt  = null;
			String leaveType = "";
			int daysPaid=0;
			if(wDays!=null)
			{
				daysPaid = wDays.size();
			}
			int daysUnpaid = 0;
			if(leaveApplication.getStatusId().getName().equals(EisConstants.STATUS_APPROVED))
			{
				LOGGER.info("===========leaveApplication.getTypeOfLeaveMstr().getPayElegible()="+leaveApplication.getTypeOfLeaveMstr().getPayElegible());				
				if(leaveApplication.getNoOfLeavesAvai() != null && leaveApplication.getNoOfLeavesAvai().intValue() < wDays.size())
				{
					daysPaid = leaveApplication.getNoOfLeavesAvai().intValue();	
					daysUnpaid = wDays.size()-daysPaid;
				}									
				LOGGER.info("===========daysPaid="+daysPaid);
				LOGGER.info("===========daysUnpaid="+daysUnpaid);
				LOGGER.info("=========== wDays.size()="+ wDays.size());				
				
				int i=1;				
				for(Iterator iter = wDays.iterator();iter.hasNext();)
				{
					String dteStr = (String)iter.next();
					boolean attAlreadyExists = true;
					attCrt = EisManagersUtill.getEmpLeaveService().checkAttendenceByEmpAndDte(leaveApplication.getEmployeeId().getIdPersonalInformation(),sdf.parse(dteStr.trim()));
					if(attCrt == null)
					{
						attCrt  = new Attendence();
						attAlreadyExists=false;
					}
					LOGGER.info("=========== attAlreadyExists="+ attAlreadyExists);
					AttendenceType attype = null;
					if(daysUnpaid == 0 )
					{
						leaveType = getLeaveTypeString(leaveApplication, leaveApplication.getTypeOfLeaveMstr().getPayElegible().equals('1')?true:false);						
					}
					else if(i <= daysPaid)
					{
						leaveType = getLeaveTypeString(leaveApplication, leaveApplication.getTypeOfLeaveMstr().getPayElegible().equals('1')?true:false);						
					}
					else
					{
						leaveType = getLeaveTypeString(leaveApplication, leaveApplication.getTypeOfLeaveMstr().getPayElegible().equals('1')?false:false);
					}
					LOGGER.info("=========== leaveType="+ leaveType);		
					attype = EisManagersUtill.getEmpLeaveService().getAttendenceTypeByName(leaveType);
					attCrt.setAttendenceType(attype);
					attCrt.setEmployee(leaveApplication.getEmployeeId());
					attCrt.setAttDate(sdf.parse(dteStr.trim()));
					attCrt.setMonth(Integer.valueOf(dteStr.substring(5, 7).trim()));
					attCrt.setFinancialId(EisManagersUtill.getFinYearForGivenDate(sdf.parse(dteStr.trim())));
					if(attAlreadyExists)
					{
						EisManagersUtill.getEmpLeaveService().updateAttendence(attCrt);
					}
					else
					{
						EisManagersUtill.getEmpLeaveService().createAttendence(attCrt);
					}
					i++;
				}
			}
			else if(leaveApplication.getStatusId().getName().equals(EisConstants.STATUS_CANCELLED))
			{
				//Check for wDays not null									
						
				if(wDays!=null)
				{
					int i=1;				
					for(Iterator iter = wDays.iterator();iter.hasNext();)
					{
						String dteStr = (String)iter.next();					
						attCrt = EisManagersUtill.getEmpLeaveService().checkAttendenceByEmpAndDte(leaveApplication.getEmployeeId().getIdPersonalInformation(),sdf.parse(dteStr.trim()));
						
						//unused code
						/*AttendenceType attype = null;
						LOGGER.info("=========== leaveType="+ leaveType);		
						attype = EisManagersUtill.getEmpLeaveService().getAttendenceTypeByName(EisConstants.PRESENT);*/
						if(attCrt!=null)
						{
							//Commenting by Divya based on new requirements once cancelling leave,attendance should become blank instead of P.
							//deleting the entry fro the record.
							/*attCrt.setAttendenceType(attype);
						    EisManagersUtill.getEmpLeaveService().updateAttendence(attCrt);*/
							 EisManagersUtill.getEmpLeaveService().deleteAttendence(attCrt);
							
						}
						i++;
					}
				}
			
			}
				
			
		} catch (ParseException e)
		{
			
			throw new EGOVRuntimeException((new StringBuilder(STR_EXCEPTION)).append(e.getMessage()).toString(), e);
		}
				
		
	}
	
	private String getLeaveTypeString(LeaveApplication leaveApplication, boolean isPayEligible)
	{
		String leaveType=null;
		try {
			LOGGER.info("=========== Inside getLeaveTypeString : isPayEligible="+isPayEligible);	
			if(isPayEligible)
			{
				if(leaveApplication.getTypeOfLeaveMstr().getIsHalfDay().equals('1') && leaveApplication.getTwoHdLeaves().equals('1'))
				{
					leaveType = EisConstants.TWOHALFLEAVE_PAID;
				}
				else if(leaveApplication.getTypeOfLeaveMstr().getIsHalfDay().equals('1'))
				{
					leaveType = EisConstants.HALFLEAVE_PAID;
				}
				else 
				{
					leaveType = EisConstants.LEAVE_PAID;
				}
			}
			else
			{
				if(leaveApplication.getTypeOfLeaveMstr().getIsHalfDay().equals('1') && leaveApplication.getTwoHdLeaves().equals('1'))
				{
					leaveType = EisConstants.TWOHALFLEAVE_UNPAID;
				}
				else if(leaveApplication.getTypeOfLeaveMstr().getIsHalfDay().equals('1'))
				{
					leaveType = EisConstants.HALFLEAVE_UNPAID;
				}
				else 
				{
					leaveType = EisConstants.LEAVE_UNPAID;
				}
			}
		} catch (Exception e) {
			
			
			throw new EGOVRuntimeException(STR_EXCEPTION + e.getMessage(),e);
		}
		return leaveType;
	}

	public CommonsService getCommonsService() {
		return commonsService;
	}

	public void setCommonsService(CommonsService commonsService) {
		this.commonsService = commonsService;
	}

	public EmpLeaveService getEisLeaveService() {
		return eisLeaveService;
	}

	public void setEisLeaveService(EmpLeaveService eisLeaveService) {
		this.eisLeaveService = eisLeaveService;
	}

	public EmployeeService getEmployeeService() {
		return employeeService;
	}

	public void setEmployeeService(EmployeeService employeeService) {
		this.employeeService = employeeService;
	}
	private final static String STR_ERROR="error";
	private final static String STR_EXCEPTION="Exception:";
}
