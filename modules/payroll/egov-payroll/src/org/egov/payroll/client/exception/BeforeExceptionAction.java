package org.egov.payroll.client.exception;


import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.actions.DispatchAction;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.commons.CFinancialYear;
import org.egov.commons.EgwStatus;
import org.egov.commons.ObjectHistory;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.utils.EgovMasterDataCaching;
import org.egov.lib.rjbac.user.User;
import org.egov.payroll.model.EmpException;
import org.egov.payroll.model.ExceptionMstr;
import org.egov.payroll.services.exception.ExceptionService;
import org.egov.payroll.utils.PayrollConstants;
import org.egov.payroll.utils.PayrollExternalInterface;
import org.egov.payroll.utils.PayrollManagersUtill;
import org.egov.payroll.workflow.exception.ExceptionWorkflowService;
import org.egov.pims.commons.Position;
import org.egov.pims.model.Assignment;
import org.egov.pims.model.DisciplinaryPunishment;
import org.egov.pims.model.PersonalInformation;
import org.egov.pims.service.EisUtilService;
import org.egov.pims.workflow.disciplinaryPunishment.DisciplinaryPunishmentService;



/**
 	FOR HANDLING EMPLOYEE EXCEPTION
 */

public class BeforeExceptionAction extends DispatchAction{
	
	private static final Logger LOGGER = Logger.getLogger(BeforeExceptionAction.class);
	private static final String LOGINUSER="com.egov.user.LoginUserName";
	private static final String ERROR="error";
	private PayrollExternalInterface payrollExternalInterface;
	ExceptionWorkflowService exceptionWorkflowService; 
	private EisUtilService eisService;
	private PersistenceService persistenceService;
	private DisciplinaryPunishmentService disciplinaryPunishmentService;
	
	
	public void setPersistenceService(PersistenceService persistenceService) {
		this.persistenceService = persistenceService;
	}

	public void setExceptionWorkflowService(ExceptionWorkflowService exceptionWorkflowService) {
		this.exceptionWorkflowService = exceptionWorkflowService;
	}

	public ActionForward getExceptionInfo(ActionMapping actionMapping,ActionForm form,HttpServletRequest request,
			HttpServletResponse response)throws Exception
	{
		ActionMessages messages = new ActionMessages();
		try{		
		HttpSession session = request.getSession();
		ExceptionService exceptionService = PayrollManagersUtill.getExceptionService();		
		ExceptionForm exceptionForm = (ExceptionForm)form;
		//To generate exception from disciplinary punishment. So, need to check for disciplinary id.
		if(request.getParameter("disciplinaryId")!=null && request.getParameter("disciplinaryEmpId")!=null)
		{
			Map<String,String> disEmpDetails = new HashMap<String,String>();
			PersonalInformation ps = payrollExternalInterface.getEmloyeeById(Integer.parseInt(request.getParameter("disciplinaryEmpId").toString()));
			disEmpDetails.put("disEmpId", ps.getIdPersonalInformation().toString());
			disEmpDetails.put("disEmpCode",ps.getCode().toString());
			disEmpDetails.put("disEmpName", ps.getEmployeeName());
			request.setAttribute("disEmpDetails", disEmpDetails);
			request.setAttribute("disciplinaryId",request.getParameter("disciplinaryId").toString());
		}
		String userName =(String) session.getAttribute(LOGINUSER);
		User user = payrollExternalInterface.getUserByUserName(userName);		
		List exceptionTypes = exceptionService.getAllDistinctTypeExceptionMstr(); 
		List<ExceptionMstr> exceptionMstrs = exceptionService.getAllExceptionMstr(); 
		ArrayList financialYears=(ArrayList)EgovMasterDataCaching.getInstance().get("egi-activeFinYr");
		request.setAttribute("exceptionWf", exceptionWorkflowService.typeOfExceptionWF());
		session.setAttribute("exceptionTypes", exceptionTypes);
		session.setAttribute("exceptionMstrs", exceptionMstrs);
		session.setAttribute("financialYears", financialYears);
		ArrayList deptMasterList=(ArrayList)EgovMasterDataCaching.getInstance().get("egi-department");
		request.getSession().setAttribute("departmentList",deptMasterList);
		ArrayList desgList=(ArrayList)EgovMasterDataCaching.getInstance().get("egEmp-DesignationMaster");
		request.getSession().setAttribute("designationList",desgList);
		
		session.setAttribute("user", user);
		
		return actionMapping.findForward("showExceptionJsp");
		}catch(EGOVRuntimeException egovExp)
		{
			/*LOGGER.error(egovExp.getMessage());
			HibernateUtil.rollbackTransaction();
			return actionMapping.findForward(ERROR);	*/
			ActionMessage message = new ActionMessage("errors.message",egovExp.getMessage());			
            messages.add( ActionMessages.GLOBAL_MESSAGE, message );
            saveMessages( request, messages );
	        LOGGER.error("Exception $ -------> "+egovExp.getMessage());
			throw new EGOVRuntimeException(egovExp.getMessage(),egovExp);
		}catch(Exception e)
		{
			/*LOGGER.error(e.getMessage());
			HibernateUtil.rollbackTransaction();
			return actionMapping.findForward(ERROR);*/
			ActionMessage message = new ActionMessage("errors.message",e.getMessage());			
            messages.add( ActionMessages.GLOBAL_MESSAGE, message );
            saveMessages( request, messages );
	        LOGGER.error("Exception $ -------> "+e.getMessage());
			throw new EGOVRuntimeException(e.getMessage(),e);
		}
	}
	
/*
 *	 CREATING EMPLOYEE EXCEPTION
 */	
	
	public ActionForward createException(ActionMapping actionMapping,ActionForm form,HttpServletRequest request,
			HttpServletResponse response)throws Exception
	{
		ActionMessages messages = new ActionMessages();
		Position pos=null; 
		try{
			ExceptionService exceptionService = PayrollManagersUtill.getExceptionService();
			HttpSession session = request.getSession();
			String userName =(String) session.getAttribute(LOGINUSER);
			User user = payrollExternalInterface.getUserByUserName(userName);
			ExceptionForm exceptionForm = (ExceptionForm)form;
			String empId[] = exceptionForm.getEmpCode();
			String reason[] = exceptionForm.getReason();
			String comments[] = exceptionForm.getComments();
			String srEntry[] = exceptionForm.getSrEntry();
			List<EmpException> exceptions = new ArrayList<EmpException>();
			EgwStatus status = payrollExternalInterface.getStatusByModuleAndDescription(PayrollConstants.EMP_EXCEPTION_MODILE,PayrollConstants.EMP_EXCEPTION_CREATED_STATUS);
			for(int i=0; i<exceptionForm.getEmpCode().length; i++){
				EmpException empException = new EmpException();
				empException.setEmployee(payrollExternalInterface.getEmloyeeById(Integer.parseInt(empId[i])));
				empException.setExceptionMstr(exceptionService.getExceptionMstrById(Integer.parseInt(reason[i])));
				empException.setComments(comments[i]);
				java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("dd/MM/yyyy");
				if(exceptionForm.getCreatedOn()==null || "".equals(exceptionForm.getCreatedOn()))
				{
					empException.setExpDate(new Date());
				}
				else
					{
					empException.setExpDate(formatter.parse(exceptionForm.getCreatedOn()));
					}
				if(srEntry != null){
					for(int j =0; j<srEntry.length; j++){
						if(empId[i].equals(srEntry[j]))
							{
							empException.setSrEntry(BigDecimal.ONE);
							}
					}			
				}
				empException.setFinancialyear(payrollExternalInterface.findFinancialYearById(Long.parseLong((exceptionForm.getFinancialYear()))));
				empException.setMonth(new BigDecimal(exceptionForm.getMonth()));			
				empException.setStatus(status);			
				empException.setUserid(user);
				
				if(exceptionWorkflowService.typeOfExceptionWF().equalsIgnoreCase(PayrollConstants.WORKFLOW_MANUAL)) 
				{ 
					Assignment assignment=PayrollManagersUtill.getEmployeeService().getAssignmentById(Integer.valueOf(exceptionForm.getApproverEmpAssignmentId()));
					pos=assignment.getPosition();
					empException.setApproverPos(pos);
				}
				else if(exceptionWorkflowService.typeOfExceptionWF().equalsIgnoreCase(PayrollConstants.WORKFLOW_AUTO))
				{
					
					pos=PayrollManagersUtill.getEisCommonsService().getPositionByUserId(empException.getEmployee().getUserMaster().getId());
				}
				
				LOGGER.info("inside create exception-----");
				//EmpException temp = exceptionManager.createException(empException);
				EmpException temp = exceptionWorkflowService.createExceptionWorkFlow(empException,pos);
				
				if(exceptionForm.getDisciplinaryId()!=null && !exceptionForm.getDisciplinaryId().equals(""))
				{
					DisciplinaryPunishment  disciplinaryPunishment =(DisciplinaryPunishment) PayrollManagersUtill.getEmployeeService().getDisciplinaryPunishmentById(Integer.valueOf(exceptionForm.getDisciplinaryId()));
					empException.setDisciplinary(disciplinaryPunishment);  
					//To end the disciplinary workflow when exception gets created.
					//DisciplinaryPunishmentService disciplinaryPunishmentService = (DisciplinaryPunishmentService)beanProvider.getBean("disciplinaryPunishmentService", org.egov.web.utils.ERPWebApplicationContext.ContextName.eis, false);
					//disciplinaryPunishmentService.setType(DisciplinaryPunishment.class);
					getDisciplinaryPunishmentService().updateDisciplinaryPunishmentWorkFlow(disciplinaryPunishment, "approve", "Workflow ended after Exception has been generated");
				}
				exceptions.add(temp);	
				ObjectHistory obj = new ObjectHistory();   
				obj.setModifiedBy(user);
				obj.setModifiedDate(new Date());
				obj.setObjectId(temp.getId().intValue());
				obj.setObjectType(payrollExternalInterface.getObjectTypeByType("Exception"));
				obj.setRemarks(temp.getComments());
				payrollExternalInterface.createObjectHistory(obj);
			}		
			for(EmpException ex : exceptions){
				LOGGER.info(ex.getExceptionMstr().getType());
			}
			session.setAttribute("exceptions", exceptions);
			return actionMapping.findForward("createExceptionJsp");			
		}catch (EGOVRuntimeException e) {			
			ActionMessage message = new ActionMessage("errors.message",e.getMessage());			
            messages.add( ActionMessages.GLOBAL_MESSAGE, message );
            saveMessages( request, messages );
	        LOGGER.error("Exception $ -------> "+e.getMessage());
			throw new EGOVRuntimeException(e.getMessage(),e);
		}
		catch(Exception e){
			ActionMessage message = new ActionMessage("errors.message",e.getMessage());
            messages.add( ActionMessages.GLOBAL_MESSAGE, message );
            saveMessages( request, messages );			 
	        LOGGER.error("Exception $ -----> "+e.getMessage());
			throw new EGOVRuntimeException(e.getMessage(),e);
		}
	}	
	
/*
 * 		SHOW EXCEPTIONS
 */	
	
	public ActionForward showExceptions(ActionMapping actionMapping,ActionForm form,HttpServletRequest request,
			HttpServletResponse response)throws Exception
	{	
		ActionMessages messages = new ActionMessages();
		try{
			ExceptionService exceptionService = PayrollManagersUtill.getExceptionService();
			HttpSession session = request.getSession();
			String userName =(String) session.getAttribute(LOGINUSER);
			User user = payrollExternalInterface.getUserByUserName(userName);
			ExceptionForm exceptionForm = (ExceptionForm)form;
			List<EmpException> exceptions = null;
			CFinancialYear finYear = null;
			String month = null;
			if(exceptionForm.getFinancialYear() == null)
				{
				finYear = payrollExternalInterface.findFinancialYearById(Long.parseLong(request.getParameter("yearId")));
				}
			else
				{
				finYear = payrollExternalInterface.findFinancialYearById(Long.parseLong(exceptionForm.getFinancialYear()));
				}
			if(exceptionForm.getMonth() == null)
				{
				month = request.getParameter("month");
				}
			else
				{
				month = exceptionForm.getMonth();
				}
			if(exceptionForm.getExceptionStatus() == null || "".equals(exceptionForm.getExceptionStatus()))
			{
				//exceptions = exceptionManager.getAllExceptionsForUserByFinYrAndMonth(user.getId(),finYear.getId().intValue(),Integer.parseInt( month));
				exceptions = exceptionService.getAllEmpException(finYear.getId(), new BigDecimal(month), null);
			}
			else{
				EgwStatus status = payrollExternalInterface.findEgwStatusById(Integer.parseInt(exceptionForm.getExceptionStatus()));
				exceptions = exceptionService.getAllEmpException(finYear.getId(), new BigDecimal(month), status.getDescription());
			}
			
			session.setAttribute("exceptions", exceptions);
			return actionMapping.findForward("showExceptions");
		}catch(EGOVRuntimeException egovExp)
		{
			/*LOGGER.error(egovExp.getMessage());
			HibernateUtil.rollbackTransaction();
			return actionMapping.findForward(ERROR);*/
			ActionMessage message = new ActionMessage("errors.message",egovExp.getMessage());
            messages.add( ActionMessages.GLOBAL_MESSAGE, message );
            saveMessages( request, messages );			 
	        LOGGER.error("Exception $ -----> "+egovExp.getMessage());
			throw new EGOVRuntimeException(egovExp.getMessage(),egovExp);
		}catch(Exception e)
		{
			/*LOGGER.error(e.getMessage());
			HibernateUtil.rollbackTransaction();
			return actionMapping.findForward(ERROR);*/
			ActionMessage message = new ActionMessage("errors.message",e.getMessage());
            messages.add( ActionMessages.GLOBAL_MESSAGE, message );
            saveMessages( request, messages );			 
	        LOGGER.error("Exception $ -----> "+e.getMessage());
			throw new EGOVRuntimeException(e.getMessage(),e);
		}
	}	
	
	
/*
 * 		EDITING EXCEPTION
 */
	
	public ActionForward editException(ActionMapping actionMapping,ActionForm form,HttpServletRequest request,
			HttpServletResponse response)throws Exception
	{		
		ActionMessages messages = new ActionMessages();
		try{
			String target ="";
			Long exceptionId = Long.parseLong(request.getParameter("exceptionId"));
			String mode = request.getParameter("mode");
			if("modify".equals(mode))
			{
				target ="editException";
			}
			else if("view".equals(mode))
			{
				target ="viewException";
			}
			else
			{
				target ="editException";
			}
			request.setAttribute("mode", mode);
			ExceptionService exceptionService = PayrollManagersUtill.getExceptionService();
			HttpSession session = request.getSession();		
			EmpException empException = exceptionService.getExceptionById(exceptionId);		
			LOGGER.info(empException.getFinancialyear().getFinYearRange());
			List exceptionTypes = exceptionService.getAllDistinctTypeExceptionMstr();
			List<ExceptionMstr> exceptionMstrs = exceptionService.getAllExceptionMstr(); 
			List<CFinancialYear> financialYears = payrollExternalInterface.getAllActiveFinancialYearList();
			String userName =(String) session.getAttribute(LOGINUSER);
			User user = payrollExternalInterface.getUserByUserName(userName);	
			session.setAttribute("exceptionTypes", exceptionTypes);
			session.setAttribute("exceptionMstrs", exceptionMstrs);
			session.setAttribute("financialYears", financialYears);
			session.setAttribute("exception", empException);
			session.setAttribute("user", user);
			return actionMapping.findForward(target);
		}catch(EGOVRuntimeException egovExp){
			/*LOGGER.error(egovExp.getMessage());
			HibernateUtil.rollbackTransaction();
			return actionMapping.findForward(ERROR);*/
			ActionMessage message = new ActionMessage("errors.message",egovExp.getMessage());
            messages.add( ActionMessages.GLOBAL_MESSAGE, message );
            saveMessages( request, messages );			 
	        LOGGER.error("Exception $ -----> "+egovExp.getMessage());
			throw new EGOVRuntimeException(egovExp.getMessage(),egovExp);
		}catch(Exception e){
			/*LOGGER.error(e.getMessage());
			HibernateUtil.rollbackTransaction();
			return actionMapping.findForward(ERROR);*/
			ActionMessage message = new ActionMessage("errors.message",e.getMessage());
            messages.add( ActionMessages.GLOBAL_MESSAGE, message );
            saveMessages( request, messages );			 
	        LOGGER.error("Exception $ -----> "+e.getMessage());
			throw new EGOVRuntimeException(e.getMessage(),e);
		}
	}	
	
	
/*
 * 		AFTER EDITING EXCEPTION 
 */	
	
	public ActionForward afterEditException(ActionMapping actionMapping,ActionForm form,HttpServletRequest request,
			HttpServletResponse response)throws Exception
	{				
		ActionMessages messages = new ActionMessages();
		ExceptionService exceptionService = PayrollManagersUtill.getExceptionService();
		EmpException temp = null;
		HttpSession session = request.getSession();
		String workFlowMessage = "";
		try{
			String userName =(String) session.getAttribute(LOGINUSER);
			User user = payrollExternalInterface.getUserByUserName(userName);
			ExceptionForm exceptionForm = (ExceptionForm)form;
			EmpException empException = exceptionService.getExceptionById(Long.parseLong(exceptionForm.getExceptionId()));
			empException.setComments(exceptionForm.getExComments());
			empException.setExceptionMstr(exceptionService.getExceptionMstrById(Integer.parseInt(exceptionForm.getExReason())));
			empException.setFinancialyear(payrollExternalInterface.findFinancialYearById(Long.parseLong((exceptionForm.getFinancialYear()))));
			empException.setMonth(new BigDecimal(exceptionForm.getMonth()));
			java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("dd/MM/yyyy",Locale.getDefault());
			if(exceptionForm.getCreatedOn()==null || "".equals(exceptionForm.getCreatedOn())){
				empException.setExpDate(new Date());
			}
			else{
				empException.setExpDate(formatter.parse(exceptionForm.getCreatedOn()));
			}
			if(("").equals(exceptionForm.getToDate())){
				empException.setToDate(null);
			}else{
				GregorianCalendar date = new GregorianCalendar(Integer.parseInt(exceptionForm.getToDate().split("/")[2])
						,Integer.parseInt(exceptionForm.getToDate().split("/")[1])-1,Integer.parseInt(exceptionForm.getToDate().split("/")[0]));
				empException.setToDate(date.getTime());
			}
			if(("").equals(exceptionForm.getFromDate())){
				empException.setFromDate(null);
				}
			else{
				GregorianCalendar date = new GregorianCalendar(Integer.parseInt(exceptionForm.getFromDate().split("/")[2])
						,Integer.parseInt(exceptionForm.getFromDate().split("/")[1])-1,Integer.parseInt(exceptionForm.getFromDate().split("/")[0]));
				empException.setFromDate(date.getTime());
			}
			LOGGER.info("aaaaaaaa"+exceptionForm.getActionType());
			EgwStatus toStatus = null;
		    String status="";
			if("Modify".equals(exceptionForm.getActionType())){
				LOGGER.info(exceptionForm.getActionType());
				exceptionForm.setIsSaved("Successfully Modified");
			}
			else{			
				if("Approve".equals(exceptionForm.getActionType())){
					SimpleDateFormat sim = new SimpleDateFormat("dd/MM/yyyy");
					eisService = new EisUtilService();
					Date fromDate = null;
					Date toDate = null;
					 //persistenceService = new PersistenceService();
					 eisService.setPersistenceService(persistenceService);
					 //persistenceService.setSessionFactory(new SessionFactory());
					 persistenceService.setType(BeforeExceptionAction.class);
					 if(!("").equals(exceptionForm.getFromDate()))
					 {
						 fromDate=sim.parse(exceptionForm.getFromDate());
					 }
					 if(!("").equals(exceptionForm.getToDate()))
					 {
						 toDate = sim.parse(exceptionForm.getToDate());
					 }
					
					List<Assignment> AssignmentObj=eisService.getPrimartAssignmentForGivenDateRange(empException.getEmployee().getId(),fromDate,toDate);
					if(AssignmentObj.isEmpty())
					{
						throw new EGOVRuntimeException("Employee Does not have Assignment for the given Dates Range!!");
					}
					else
					{
						//status=PayrollConstants.EMP_EXCEPTION_APPROVED_STATUS;
						temp = exceptionWorkflowService.updateExceptionWorkFlow(empException, "user_approve", empException.getComments());
					 
						workFlowMessage += "Successfully Approved";
						exceptionForm.setIsSaved("Successfully Approved");
					}
				}
				if("Reject".equals(exceptionForm.getActionType())){
					status=PayrollConstants.EMP_EXCEPTION_CLOSED_STATUS;
					temp = exceptionWorkflowService.updateExceptionWorkFlow(empException, "user_reject", empException.getComments());
					workFlowMessage += "Successfully Rejected";
					exceptionForm.setIsSaved("Successfully Rejected");
				}
				//toStatus = payrollExternalInterface.getStatusByModuleAndDescription(PayrollConstants.EMP_EXCEPTION_MODILE,status);
				/*EgwStatus fromStatus = empException.getStatus();
				empException.setStatus(toStatus);
				EgwSatuschange statusChanges = new EgwSatuschange();
				statusChanges.setCreatedby(user.getId());
				statusChanges.setFromstatus(fromStatus.getId());
				statusChanges.setModuleid(3);
				statusChanges.setModuletype(PayrollConstants.EMP_EXCEPTION_MODILE);
				statusChanges.setTostatus(toStatus.getId());			
				payrollExternalInterface.createEgwSatuschange(statusChanges);	
				empException.setStatus(toStatus);*/
			}
			
			//To backupdate the todate in disciplinary punishment
			if("Approve".equals(exceptionForm.getActionType()) || "Modify".equals(exceptionForm.getActionType())){
				if(empException.getDisciplinary()!=null &&  !("").equals(exceptionForm.getToDate())){
					DisciplinaryPunishment  disciplinaryPunishment =(DisciplinaryPunishment) PayrollManagersUtill.getEmployeeService().getDisciplinaryPunishmentById(Integer.valueOf(empException.getDisciplinary().getId().toString()));
					if(disciplinaryPunishment.getWhetherSuspended()=='1'){
						disciplinaryPunishment.setDateOfReinstatement(empException.getToDate());
						//PersistenceService<DisciplinaryPunishment, Long> disciplinaryPunishmentPersistenceService = (PersistenceService<DisciplinaryPunishment, Long>) beanProvider.getBean("disciplinaryPunishmentPersistentService", org.egov.web.utils.ERPWebApplicationContext.ContextName.eis, false);
						 persistenceService.setType(DisciplinaryPunishment.class);
						 persistenceService.update(disciplinaryPunishment);
					}
				}
			}
			
			//EmpException temp = exceptionManager.createException(empException);
			ObjectHistory obj = new ObjectHistory();
			obj.setModifiedBy(user);
			obj.setModifiedDate(new Date());
			obj.setObjectId(empException.getId().intValue());
			obj.setObjectType(payrollExternalInterface.getObjectTypeByType("Exception"));
			obj.setRemarks(empException.getComments());
			payrollExternalInterface.createObjectHistory(obj);
			//exceptionForm.setIsSaved("yes");
			request.getSession().setAttribute("workFlowMessage", workFlowMessage);
			session.setAttribute("exception", empException);
			return actionMapping.findForward("afterEditException");
		}catch(EGOVRuntimeException egovExp){
			/*LOGGER.error(egovExp.getMessage());
			//HibernateUtil.rollbackTransaction();
			//return actionMapping.findForward(ERROR);	
			throw egovExp;*/
			ActionMessage message = new ActionMessage("errors.message",egovExp.getMessage());
            messages.add( ActionMessages.GLOBAL_MESSAGE, message );
            saveMessages( request, messages );			 
	        LOGGER.error("Exception $ -----> "+egovExp.getMessage());
			throw new EGOVRuntimeException(egovExp.getMessage(),egovExp);
		}catch(Exception e){
			/*LOGGER.error(e.getMessage());
			//HibernateUtil.rollbackTransaction();
			throw e;*/
			ActionMessage message = new ActionMessage("errors.message",e.getMessage());
            messages.add( ActionMessages.GLOBAL_MESSAGE, message );
            saveMessages( request, messages );			 
	        LOGGER.error("Exception $ -----> "+e.getMessage());
			throw new EGOVRuntimeException(e.getMessage(),e);
		}
		
	}

	public void setPayrollExternalInterface(
			PayrollExternalInterface payrollExternalInterface) {
		this.payrollExternalInterface= payrollExternalInterface;
	}

	public DisciplinaryPunishmentService getDisciplinaryPunishmentService() {
		return disciplinaryPunishmentService;
	}

	public void setDisciplinaryPunishmentService(
			DisciplinaryPunishmentService disciplinaryPunishmentService) {
		this.disciplinaryPunishmentService = disciplinaryPunishmentService;
	}
	
}
