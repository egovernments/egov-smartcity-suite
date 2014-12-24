package org.egov.pims.empLeave.client;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import org.egov.commons.service.CommonsService;
import org.egov.infstr.commons.dao.GenericDaoFactory;
import org.egov.infstr.config.AppConfigValues;
import org.egov.infstr.services.EISServeable;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.utils.EgovMasterDataCaching;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.pims.commons.Position;
import org.egov.pims.commons.service.EisCommonsService;
import org.egov.pims.empLeave.dao.AttendenceDAO;
import org.egov.pims.empLeave.dao.AttendenceHibernateDAO;
import org.egov.pims.empLeave.model.Attendence;
import org.egov.pims.empLeave.model.CompOff;
import org.egov.pims.empLeave.service.EmpLeaveService;
import org.egov.pims.model.EmployeeView;
import org.egov.pims.model.PersonalInformation;
import org.egov.pims.model.StatusMaster;
import org.egov.pims.service.EmployeeService;
import org.egov.pims.service.EisUtilService;
import org.egov.pims.utils.EisConstants;
import org.egov.pims.utils.EisManagersUtill;
import org.egov.pims.utils.Helper;
import org.egov.pims.workflow.compOff.CompOffService;
public class BeforeCompOffAction extends DispatchAction 
{
	private static  final Logger LOGGER = Logger.getLogger(BeforeCompOffAction.class);
	String idCompOff;
	private final SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy");
	private EmployeeService employeeService;
	private EmpLeaveService eisLeaveService;
	private CommonsService commonsService;
	private EisCommonsService eisCommonsService;
	private PersistenceService<StatusMaster,Integer> persistenceService;
	private CompOffService compOffService;
	private EISServeable eisService;
	
	public void setCompOffService(CompOffService compOffService) {
		this.compOffService = compOffService;
	}	
	
	public void setPersistenceService(PersistenceService persistenceService) {
		this.persistenceService = persistenceService;
	}
	
	public ActionForward beforeCompOffApprove(ActionMapping mapping,ActionForm form,HttpServletRequest req,HttpServletResponse res)
	throws IOException,ServletException
	{
		String target = "fromInbox";
		idCompOff = req.getParameter("idCompOff");
		LeaveForm leaveForm=(LeaveForm)form;
		try
		{
			
			//when approver click on modify coming here ,putting mode to modify to show/hide buttons
			if(req.getParameter("mode")!=null && req.getParameter("mode").equals("modify"))
				req.setAttribute("mode","modify");
			else
				req.setAttribute("mode","fromInbox");//when approver cilck on inbox item
				
			LOGGER.info("idCompOff>>>>>"+idCompOff);
			CompOff compoff=(CompOff)compOffService.getSession().load(CompOff.class, Long.valueOf(idCompOff));
			leaveForm.setCompOffDate(sdf.format(compoff.getCompOffDate()));
			leaveForm.setEmployeeName(compoff.getAttObj().getEmployee().getEmployeeName());
			leaveForm.setEmpCode(compoff.getAttObj().getEmployee().getEmployeeCode().toString());
			leaveForm.setEmpId(compoff.getAttObj().getEmployee().getIdPersonalInformation().toString());
			leaveForm.setWorkedOnHolidayDate(sdf.format(compoff.getAttObj().getAttDate()));
			leaveForm.setCompOffId(compoff.getId().toString());
			target = "fromInbox";
		}
		catch(Exception e)
		{
			target = "error";
			LOGGER.debug("Exception Encountered!!!"+e.getMessage());
			throw new EGOVRuntimeException(e.getMessage(),e);
		}

		return mapping.findForward(target);

	}
	

	public ActionForward getAllPresentsOnHolidays(ActionMapping mapping,ActionForm form,HttpServletRequest req,HttpServletResponse res)
	throws IOException,ServletException,Exception
	{
		String target = "success"; 
		LeaveForm leaveForm=(LeaveForm)form;
		List<CompOff> compOffList=null;
		List<StatusMaster> statusMstrNameList=new ArrayList<StatusMaster>();  
		String mode=(String)req.getParameter("mode");
		Date workedOnFrom=null;
		Date workedOnTo=null;
		SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy");
		try{
			if(mode!=null && mode.equals("create"))//when it is coming from menu tree where all appoved can be viewd/cancelled
			{
				req.setAttribute("mode","create");
				statusMstrNameList.add(eisLeaveService.getStatusMasterByName(EisConstants.STATUS_REJECTED));
				statusMstrNameList.add(eisLeaveService.getStatusMasterByName(EisConstants.STATUS_CANCELLED));
			}
			else if(mode!=null && mode.equals("cancel"))
			{
				req.setAttribute("mode","cancel");
				statusMstrNameList.add(eisLeaveService.getStatusMasterByName(EisConstants.STATUS_APPROVED));
			}
			else if(mode!=null && mode.equals("view"))
			{
				if(leaveForm.getWorkedonFromDate()==null || leaveForm.getWorkedonFromDate().trim().equals(""))
					throw new EGOVRuntimeException("From Date missing");
				else
				{
				workedOnFrom=	sdf.parse(leaveForm.getWorkedonFromDate());
				if(leaveForm.getWorkedonToDate()!=null && !leaveForm.getWorkedonToDate().trim().equals(""))
					workedOnTo=sdf.parse(leaveForm.getWorkedonToDate());
				req.setAttribute("mode","view");
				persistenceService.setType(StatusMaster.class);
				statusMstrNameList.addAll(persistenceService.findAll());
				}
			}
		List<EmployeeView> empViewList=employeeService.searchEmployee(Integer.valueOf(req.getParameter("department")),Integer.valueOf(req.getParameter("designation")),
      		  Integer.valueOf(req.getParameter("functionary")),req.getParameter("code"),null,Integer.valueOf("0"));
			LOGGER.debug("empViewList"+empViewList);
			List<Integer> empList=new ArrayList<Integer>();
			if(empViewList!=null && !empViewList.isEmpty())
			{ 
				for(EmployeeView empview:empViewList)
				{
					empList.add(empview.getId());
				}
			}
		
		LOGGER.debug("emp "+empList);
		
		if(empList!=null && !empList.isEmpty())
		{
			if(mode!=null && mode.equals("view")  )
				compOffList=compOffService.getCompOffByEmpAndWorkedOndate(workedOnFrom, workedOnTo, empList);
			else
				
			compOffList=compOffService.findAllByNamedQuery("COMPOFF_BYEMP_STATUS", empList,statusMstrNameList);  
		}
		else
			req.setAttribute("alertMsg","Search Yielded No results");
		}
		catch(EGOVRuntimeException egovRuntimeException)
		{
			req.setAttribute("alertMsg",egovRuntimeException.getMessage());
		}
		catch(Exception e)
		{
			LOGGER.error(e.getMessage());
			target="error";
			throw new EGOVRuntimeException(e.getMessage(),e);
		}
		req.setAttribute("compOffList", compOffList); 
		return mapping.findForward(target); 
	}
	public ActionForward getEmpInfoForCompOff(ActionMapping mapping,ActionForm form,HttpServletRequest req,HttpServletResponse res)
	{
		LeaveForm leaveForm=(LeaveForm)form;
		String target="beforeCompOffCreate";
		String compOffId=req.getParameter("compOffId");
		ActionMessages messages = new ActionMessages();
		try
		{
		CompOff compoff=(CompOff)compOffService.getSession().get(CompOff.class, Long.valueOf(compOffId));
		leaveForm.setEmployeeName(compoff.getAttObj().getEmployee().getEmployeeName());
		leaveForm.setEmpCode(compoff.getAttObj().getEmployee().getEmployeeCode().toString());
		leaveForm.setEmpId(compoff.getAttObj().getEmployee().getIdPersonalInformation().toString());
		leaveForm.setWorkedOnHolidayDate(sdf.format(compoff.getAttObj().getAttDate())); 
		leaveForm.setCompOffId(compoff.getId().toString());
		leaveForm.setCompOffDate(compoff.getCompOffDate()==null?"":sdf.format(compoff.getCompOffDate()));
		req.setAttribute("compoffWf", compOffService.typeOfCompoffWF());
		 if(req.getParameter("mode")!=null && req.getParameter("mode").equals("create") )
		{
			req.setAttribute("mode", "cancelBeforeApply");
		}
		else if(req.getParameter("mode")!=null && req.getParameter("mode").equals("view"))
		{
			req.setAttribute("mode", "view");
		}
		 else if(req.getParameter("mode")!=null && req.getParameter("mode").equals("cancel") )
		{
			req.setAttribute("mode", "cancel");
		}
	}catch(EGOVRuntimeException e){
		target="error";
		ActionMessage message = new ActionMessage("errors.message",e.getMessage());			
        messages.add( ActionMessages.GLOBAL_MESSAGE, message );
        saveMessages( req, messages );
       // HibernateUtil.rollbackTransaction();
        LOGGER.error("Exception $ -------> "+e.getMessage());
        throw new EGOVRuntimeException(e.getMessage(),e);
	}
	catch(Exception e)
	{
		target="error";
		throw new EGOVRuntimeException(e.getMessage(),e);
	}
		
		return mapping.findForward(target);
	}
	public ActionForward createCompOff(ActionMapping mapping,ActionForm form,HttpServletRequest req,HttpServletResponse res)
	{
		String target="compOffCreate";
		LeaveForm leaveForm=(LeaveForm)form; 
		ActionMessages messages = new ActionMessages(); 
		Position pos=null;
		try{
			String compoffWfType = GenericDaoFactory.getDAOFactory().getAppConfigValuesDAO().getAppConfigValueByDate(EisConstants.MODULE_LEAVEAPP,EisConstants.MODULE_KEY_LEAVEWF,new Date()).getValue();
			if(compoffWfType==null || compoffWfType.trim().equals(""))
				throw new EGOVRuntimeException("Appconfig Value is not there for the module "+EisConstants.MODULE_LEAVEAPP+" and key "+ EisConstants.MODULE_KEY_LEAVEWF);
			else
			{
				CompOff compoff=(CompOff)compOffService.getSession().get(CompOff.class, Long.valueOf(leaveForm.getCompOffId()));
				if(compoffWfType.trim().equalsIgnoreCase(EisConstants.MANUALWF))
				{ 
					
					pos=eisCommonsService.getPositionById(Integer.valueOf(leaveForm.getApproverEmpAssignmentId()));
					compoff.setApproverPos(pos);
				}
				else if(compoffWfType.trim().equalsIgnoreCase(EisConstants.AUTOWF))
				{
					
					pos=eisCommonsService.getPositionByUserId(compoff.getAttObj().getEmployee().getUserMaster().getId());
				}
				
				if(leaveForm.getCompOffDate()==null)
					throw new EGOVRuntimeException("Compoff Date not Exists");
				compoff.setCompOffDate(sdf.parse(leaveForm.getCompOffDate()));
				req.setAttribute("alertMsg", "CompOff Created Successfully");
				compOffService.createcompOffWorkFlow(compoff,pos);
			}
		}catch(EGOVRuntimeException e){
			target="error";
			ActionMessage message = new ActionMessage("errors.message",e.getMessage());			
            messages.add( ActionMessages.GLOBAL_MESSAGE, message );
            saveMessages( req, messages );
           // HibernateUtil.rollbackTransaction();
	        LOGGER.error("Exception $ -------> "+e.getMessage());
	        throw new EGOVRuntimeException(e.getMessage(),e);
		}
		catch(Exception e)
		{
			target="error";
			throw new EGOVRuntimeException(e.getMessage(),e);
		}
		return mapping.findForward(target);
	}
	public ActionForward populateRequiredData(ActionMapping mapping,ActionForm form,HttpServletRequest req,HttpServletResponse res)
	{
		String target="success";
		String mode=(String)req.getParameter("mode");
		if(mode!=null && mode.equals("create"))//when it is coming from menu tree where all appoved can be viewd/cancelled
		{
			req.setAttribute("mode","create");
		}
		else if(mode!=null && mode.equals("view"))
		{
			req.setAttribute("mode","view");
		}
		else if(mode!=null && mode.equals("cancel"))
		{
			req.setAttribute("mode","cancel");
		}
		req.getSession().setAttribute("deptList",getEisService().getDeptsForUser());
		ArrayList deptMasterList=(ArrayList)EgovMasterDataCaching.getInstance().get("egi-department");
		req.getSession().setAttribute("departmentList",deptMasterList);
		ArrayList functionaryMasterList=(ArrayList)EgovMasterDataCaching.getInstance().get("egEmp-Functionary");
		req.getSession().setAttribute("functionaryList",functionaryMasterList);
		ArrayList desgList=(ArrayList)EgovMasterDataCaching.getInstance().get("egEmp-DesignationMaster");
		req.getSession().setAttribute("designationList",desgList);
		return mapping.findForward(target);
	} 
	
	public ActionForward cancelHolidayWorkedON(ActionMapping mapping,ActionForm form,HttpServletRequest req,HttpServletResponse res)
	{
		String target="cancelHolidayWorkedOn";
		LeaveForm leaveForm=(LeaveForm)form;
		try{
		CompOff compoff=(CompOff)compOffService.getSession().load(CompOff.class, Long.valueOf(leaveForm.getCompOffId()));
		Attendence attendence=compoff.getAttObj();
			HibernateUtil.getCurrentSession().delete(attendence);
			compOffService.delete(compoff);
		req.setAttribute("alertMsg", "HolidayWorkedOn Cancelled Successfully");
		}catch(Exception e){
			target="error";
			throw new EGOVRuntimeException(e.getMessage(),e);
		}
		return mapping.findForward(target);
	}
	public ActionForward checkCompOffDateAjax(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)throws ParseException
	{
		String target="checkCompOffDate";
		String compOffReqStr = (String)request.getParameter("compOffRequestDate");
		String workedOnStr = (String)request.getParameter("workedOnDate");
		String empIdStr = (String)request.getParameter("empId");
		String resultStr = "";
		Date compoffDate=null;
		Date  workedOnDate=null;
		Long empId=null;
		String compoffValidityPeriod="";
		ActionMessages messages = new ActionMessages();
		Calendar calendar=Calendar.getInstance();
		if(compOffReqStr != null && !compOffReqStr.trim().equals("") && workedOnStr!=null && !workedOnStr.trim().equals(""))
		{
			compoffDate=sdf.parse(compOffReqStr);
			calendar.setTime(compoffDate);
			workedOnDate=sdf.parse(workedOnStr);
			compOffReqStr=new SimpleDateFormat("yyyy-MM-dd").format(compoffDate);
			if(empIdStr!=null && !empIdStr.trim().equals(""))
			empId=Long.valueOf(empIdStr); 
			 
			try
			{					
				SimpleDateFormat formatter  =new SimpleDateFormat("dd-MMM-yyyy");
				String fid =formatter.format(calendar.getTime());
				//String fid =commonsService.getFinancialYearById(formatter.format(calendar.getTime()));
				CFinancialYear cFinancialYear =commonsService.findFinancialYearById(Long.valueOf(fid));
				int month = calendar.get(Calendar.MONTH)+1;
				Map<String, Date> startEndDateMap = EisManagersUtill.getStartingAndEndingDateForMonthAanFyer(month, cFinancialYear);
				Set<String> holidaySet = eisLeaveService.getHolidaySetForEmployeInDateRange((Date)startEndDateMap.get("startDate"),
														(Date)startEndDateMap.get("endDate"),employeeService.getEmloyeeById(Integer.parseInt(empIdStr)));
				if(holidaySet.isEmpty()){
					holidaySet = eisLeaveService.getHolidaySet(cFinancialYear);
				}				 
				List<AppConfigValues> compoffValidityList=GenericDaoFactory.getDAOFactory().getAppConfigValuesDAO().getConfigValuesByModuleAndKey(EisConstants.MODULE_LEAVEAPP, EisConstants.MODULE_KEY_COMPVAL);
				if(compoffValidityList!=null || !compoffValidityList.isEmpty())
					compoffValidityPeriod=compoffValidityList.get(0).getValue();
				if(!Helper.isCompOffValid(workedOnDate, compoffDate))
					resultStr="CompOff date should be any day within "+compoffValidityPeriod+" days from the worked on day";
				else if(holidaySet.contains(compOffReqStr))
					resultStr="Entered CompOff date is a Holiday. Please enter another date";
				else if(eisLeaveService.isLeaveAvailForDateEmpIdStatus(compoffDate,empId))
					resultStr="Entered CompOff date is a Leave. Please enter another date";
				else if(compOffService.isCompOffAvailForDateEmpId(compoffDate,empId))
					resultStr="Entered CompOff date is a CompOff. Please enter another date";
				else {
					AttendenceDAO attDAO=new AttendenceHibernateDAO(Attendence.class,compOffService.getSession());
					PersonalInformation employee=employeeService.getEmloyeeById(Integer.valueOf(empIdStr));
					List<Attendence> attPresentlist=attDAO.getListOfPresentDaysForEmployeebetweenDates(compoffDate,compoffDate,employee);
					if(attPresentlist!=null && !attPresentlist.isEmpty())
					{
						
						resultStr="Entered CompOff date is a Present. Please enter another date"; 
					}
					else
					{
						Set halfPresentSet=eisLeaveService.listHalfPresentForAnEmployeebetweenTwoDates(compoffDate,compoffDate,employee);
						if(halfPresentSet!=null && !halfPresentSet.isEmpty())
						{
							resultStr="Entered CompOff date is a HalfPresent. Please enter another date";
						}
					}
				}
					
				LOGGER.info(" result "+ resultStr);
			}
			catch(Exception e)
			{
				target="error";
				LOGGER.error(e.getMessage());
				ActionMessage message = new ActionMessage("errors.message",e.getMessage());			
	            messages.add( ActionMessages.GLOBAL_MESSAGE, message );
	            saveMessages( request, messages );
	            throw new EGOVRuntimeException(e.getMessage(),e);
 
			}
		}
		request.setAttribute("isLEaveHolidayExists", resultStr);
		return mapping.findForward(target);
	}
	
	public void setCommonsService(CommonsService commonsService) {
		this.commonsService = commonsService;
	}
	
	public void setEmployeeService(EmployeeService employeeService) { 
		this.employeeService = employeeService;
	}

	public void setEisLeaveService(EmpLeaveService eisLeaveService) {
		this.eisLeaveService = eisLeaveService;
	}

	public void setEisCommonsService(EisCommonsService eisCommonsService) {
		this.eisCommonsService = eisCommonsService;
	}

	public EISServeable getEisService() {
		return eisService;
	}

	public void setEisService(EISServeable eisService) {
		this.eisService = eisService;
	}
	
	
}