/*
 * RegisterAction.java	1.00 Jan 31 2005
 *
 * Copyright 2005 eGovernments Foundation. All rights reserved.
 * EGOVERNMENTS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.pims.client;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.displaytag.pagination.PaginatedList;
import org.displaytag.tags.TableTagParameters;
import org.displaytag.util.ParamEncoder;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.commons.CFinancialYear;
import org.egov.infstr.client.filter.EGOVThreadLocals;
import org.egov.infstr.services.Page;
import org.egov.pims.client.common.BaseSearchAction;
import org.egov.pims.empLeave.model.EmployeeAttendenceReport;
import org.egov.pims.empLeave.service.EmpLeaveService;
import org.egov.pims.model.EmployeeView;
import org.egov.pims.model.PersonalInformation;
import org.egov.pims.service.EmployeeService;
import org.egov.pims.service.PersonalInformationService;
import org.egov.pims.utils.EisConstants;
import org.egov.pims.utils.EisManagersUtill;
import org.egov.web.utils.EgovPaginatedList;

public class AfterSearchAction extends BaseSearchAction
{

	public final static Logger LOGGER = Logger.getLogger(AfterSearchAction.class.getClass());
	private EmployeeService employeeService;
	private EmpLeaveService eisLeaveService;
	private PersonalInformationService personalInformationService;
	private static final String STR_EXCEPTION= "Exception:";
	
	public ActionForward executeSearch(ActionMapping mapping,ActionForm form,HttpServletRequest req,HttpServletResponse res)
	 throws IOException,ServletException
	{
		String target =null;
		String alertMessage=null;
		try
		{
			//super.search(mapping, form, req, res);
			Collection employeeList=null;
			SearchForm searchForm = (SearchForm)form;
			EmployeeService eisManager = getEmployeeService();
			String functionaryId = searchForm.getFunctionaryId();
			
			Map finParamsMap=new LinkedHashMap<String , Integer>();
			Integer designationId=Integer.valueOf(searchForm.getDesignationId());
			Integer statusId=Integer.valueOf(searchForm.getStatus()); 
			Integer emptypeId=Integer.valueOf(searchForm.getEmpType());
			
			//to search using employee username
			if(searchForm.getUserNameId()!=null && !searchForm.getUserNameId().equalsIgnoreCase(""))
			{
				finParamsMap.put("userId",Integer.valueOf(searchForm.getUserNameId()));
			}	
			
			//checks whether UserActive checkbox is enabled or not
			if(searchForm.getUserActiveCheckbox())
			{
				int i=1;
				finParamsMap.put("isActive",i);
				searchForm.setUserActiveCheckbox(false);//set default value of UserActive value to false
			}
			
			if(searchForm.getDepartmentId()!=null && !searchForm.getDepartmentId().isEmpty())
			finParamsMap.put("departmentId", Integer.valueOf(searchForm.getDepartmentId()));
			
			if(searchForm.getFunctionaryId()!=null && !searchForm.getFunctionaryId().isEmpty())
				finParamsMap.put("functionaryId", Integer.valueOf(searchForm.getFunctionaryId()));
			
			if(searchForm.getFunctionId()!=null && !searchForm.getFunctionId().isEmpty())
				finParamsMap.put("functionId", Integer.valueOf(searchForm.getFunctionId()));
            if(req.getParameter(EisConstants.ESS)==null || req.getParameter(EisConstants.ESS).trim().isEmpty()){ 
           
              employeeList=eisManager.searchEmployee(Integer.valueOf(searchForm.getDesignationId()),
            		  searchForm.getCode().toUpperCase(),searchForm.getName(),statusId,emptypeId,finParamsMap);
            }
            else if(req.getParameter(EisConstants.ESS).equals("1")) 
            {
            	PersonalInformation employee=eisManager.getEmpForUserId(Integer.valueOf(EGOVThreadLocals.getUserId()));
            	req.setAttribute(EisConstants.ESS, "1");
            	if(employee!=null) 
            		{
            		employeeList=eisManager.searchEmployee(Integer.valueOf(0),Integer.valueOf(0),employee.getCode(),null,Integer.valueOf(0));
            		}  
            }
	 
             
             
            if( employeeList.isEmpty()) 
            {
            	alertMessage= "Search yielded no results";
            	
            }
            
	   	 	req.setAttribute("employeeList",employeeList);
	   	 	String module = null;
			if(req.getParameter(STR_MODULE)!=null)
			{
				module = req.getParameter(STR_MODULE);
			}
			if(req.getParameter("master")!=null)
			{
				req.setAttribute("masters",req.getParameter("master"));
			}
			if(module != null)
			{
			   	 if("Attendence".equals(module))
			   	 {
					target = "attendence";
			   	 }
			   	 else if("Employee".equals(module))
			   	 {
					target = "successEmp";
			   	 }
			   	 else if("Pension".equals(module))
			   	 {
			   		req.setAttribute(STR_MODULE,module);
			   		String mode = req.getParameter("mode"); 
			   		req.setAttribute("mode",mode);
			   	 	target="successPension";
			   	 }
				  else if ("Payslip".equals(module))
			   	 {
			   		req.setAttribute(STR_MODULE,module);
			   		String mode = req.getParameter("mode"); 
			   		req.setAttribute("mode",mode);
			   		target = "payrollSearch";
			   	 }
			   	 else
			   	 {
					target = "attendenceReport";
			   	 }
			}
		}
		catch(Exception ex)
		{
		   target = STR_ERROR;
		   LOGGER.error(ex.getMessage());
		   throw new EGOVRuntimeException(ex.getMessage(),ex);
		}
		req.setAttribute(STR_ALERT_MESSAGE, alertMessage);
		req.setAttribute(STR_MODULE,(String)req.getAttribute(STR_MODULE));
		
		return mapping.findForward(target);
	}

	public ActionForward executeSearchByStatus(ActionMapping mapping,ActionForm form,HttpServletRequest req,HttpServletResponse res)
	 throws IOException,ServletException
	{
		String target =null;
		String alertMessage=null;
		Integer pageNumber; 
		Integer pageSize=null; 
		PaginatedList pagedResults=null;
		try
		{
			SearchForm searchForm = (SearchForm)form;
			Date fromDateObj = getDateString(searchForm.getFromDate());
			Date toDateObj = getDateString(searchForm.getToDate());
			

			pageNumber=req.getParameter("page")==null||req.getParameter("page").isEmpty()? 
					Integer.valueOf(1):Integer.valueOf(req.getParameter("page"));
	
					ParamEncoder paramEncoder  = new ParamEncoder("currentRowObject");//currentRowObject
					  if(req.getParameter(paramEncoder.encodeParameterName(
					      TableTagParameters.PARAMETER_EXPORTTYPE)) != null){
						pageSize= null;
						pageNumber = 1;
					  }
					  else
					  {
						  pageSize= 20;  
					  }
					  

					
           if(!searchForm.getStatus().equals("0")){
        	   pagedResults=getPagedListForEmpByStatus(Integer.valueOf(searchForm.getStatus()),fromDateObj,toDateObj,pageNumber,pageSize);
           }
           if(pagedResults==null || pagedResults.getList().isEmpty())
           {
        	   alertMessage= "Search yielded no results";
           }
           
       
           target = "successEmployeeByStatus";
	   	 	req.setAttribute("employeeList",pagedResults);
	   	 req.setAttribute("toDate",new SimpleDateFormat("dd/MM/yyyy").parse(searchForm.getToDate()));
	   	 req.setAttribute("searchForm",searchForm);
		}
		catch(Exception ex)
		{
		   target = "error";
		   LOGGER.error(ex.getMessage());
		   throw new EGOVRuntimeException(ex.getMessage(),ex);
		}
		req.setAttribute("alertMessage", alertMessage);
		return mapping.findForward(target);
	}
	/**
	 * returns paginated list for the given parameters,considering pageSize is 20
	 * @param status
	 * @param fromdate
	 * @param todate
	 * @param pageNumber
	 * @return
	 */
	private PaginatedList getPagedListForEmpByStatus(Integer status,Date fromdate,Date todate,Integer pageNumber,Integer pageSize)
	{
		
		Page page=getPersonalInformationService().getEmployeesByStatus(status,fromdate,todate,pageNumber,pageSize);
		int totalCount=getPersonalInformationService().getTotalCountOfEmployeesByStatus(status,fromdate,todate);
		return new EgovPaginatedList(page,totalCount);
	}
	public String search() {

		  //check if export parameter is set
		  //currentRowObject is the id/uid of the display tag table
		/*Integer pageSize;
		Integer page;
		
		  ParamEncoder paramEncoder  = new ParamEncoder("currentRowObject");
		  if(parameters.get(paramEncoder.encodeParameterName(
		      TableTagParameters.PARAMETER_EXPORTTYPE)) != null){
			pageSize= null;
			page = 1;
		  }

		  Page page = getPersonalInformationService().findPageByNamedQuery(QRY_SOME_OBJECT, page, pageSize, param1, param2)
		  PaginatedList pagedResults = new EgovPaginatedList(page,page.getList().size());*/
		return "";
		}

	public ActionForward executeSearchForAttendence(ActionMapping mapping,ActionForm form,HttpServletRequest req,HttpServletResponse res)
	 throws IOException,ServletException
	{
		String target =null;
		String alertMessage=null;
		try
		{
			SearchForm searchForm = (SearchForm)form;
			String finYear = req.getParameter(STR_FINYEAR);
			String mon = req.getParameter(STR_MONTHID);
			String functionaryId = req.getParameter("functionaryId");
			String desgId = "0";
			String fromSearchName = req.getParameter("fromName");
			String toSearchName = req.getParameter("toName");
			String functionId = req.getParameter("functionId");
			String employeeTypeId = searchForm.getEmpType();
			String billId=searchForm.getBillId();
			
			Map employeeMap = null;
			if(!searchForm.getDesignationId().equals(""))
				{
					desgId = searchForm.getDesignationId();
				}
			employeeMap =eisLeaveService.searchEmployeeForAttendence(Integer.valueOf(searchForm.getDepartmentId()),Integer.valueOf(desgId),searchForm.getCode(),searchForm.getName(),searchForm.getSearchAll(),finYear,mon,Integer.valueOf(functionaryId),
						fromSearchName,toSearchName,Integer.valueOf(functionId),Integer.valueOf(employeeTypeId),
						Integer.valueOf(billId==null ||billId.isEmpty()?"0":billId));
			
			String module = null; 
			if(req.getParameter(STR_MODULE)!=null)
			{
				module = req.getParameter(STR_MODULE);
			}
			if(module != null)
			{
				if(module.equals("Attendence") && employeeMap!=null && employeeMap.size() > 200)
				{
					target="attExceeds100";
					if(Integer.valueOf(searchForm.getDepartmentId())!=0 && !"0".equals(functionaryId))
					{
						alertMessage = "List of Employees exceeds 200. Please refine your search by Designation";
					}
					else
					{
						alertMessage = "List of Employees exceeds 200. Please refine your search";
					}
				}
				else
				{
					req.setAttribute("employeeMap",employeeMap);
			   	 	if("Attendence".equals(module))
					{
				   	 	target = "attendence";
					}
			   	 	else if("Employee".equals(module))
			   	 	{
			   	 		target = "successEmp";
			   	 	}
			   	 	else
			   	 	{
			   	 		target = "attendenceReport";
			   	 	}
				}
			}			
		}
		catch(Exception ex)
		{
		   target = STR_ERROR;
		   LOGGER.error(ex.getMessage());
		   throw new EGOVRuntimeException(ex.getMessage(),ex);
		}
		req.setAttribute(STR_MODULE,(String)req.getAttribute(STR_MODULE));
		req.setAttribute(STR_ALERT_MESSAGE, alertMessage);
		return mapping.findForward(target);
	}

	
	public ActionForward executeSearchForRetirement(ActionMapping mapping,ActionForm form,HttpServletRequest req,HttpServletResponse res)
	 throws IOException,ServletException
	{
		String target =null;
		List<EmployeeView> employeeList = new ArrayList<EmployeeView>();
		try
		{
			SearchForm searchForm = (SearchForm)form;
			String finYear = req.getParameter(STR_FINYEAR);
			CFinancialYear cufinancial  =EisManagersUtill.getCommonsService().findFinancialYearById(Long.valueOf(finYear));
			String mon = req.getParameter(STR_MONTHID);
			List lOfemployeeList =employeeService.searchEmployee(Integer.valueOf(searchForm.getDepartmentId()),Integer.valueOf(searchForm.getDesignationId()),searchForm.getCode(),searchForm.getName(),searchForm.getStatus());
			Map mp = EisManagersUtill.getStartingAndEndingDateForMonthAanFyer(Integer.valueOf(mon),cufinancial);
			java.util.Date retDate = null;
			Calendar dobCat =null ;
			for(Iterator iter = lOfemployeeList.iterator();iter.hasNext();)
			{
				dobCat =Calendar.getInstance();
				EmployeeView employeeView = (EmployeeView)iter.next();
				Integer id  = employeeView.getId();
				PersonalInformation personalInformation = employeeService.getEmloyeeById(id);
				if(personalInformation.getDateOfBirth()!=null)
				{
					dobCat.setTime(personalInformation.getDateOfBirth());
				}
				if(personalInformation.getRetirementAge()!=null)
				{
					dobCat.add(Calendar.YEAR, personalInformation.getRetirementAge());
				}
				retDate = dobCat.getTime();
				if(personalInformation.getDateOfBirth()!=null && personalInformation.getRetirementAge()!=null && retDate.after((java.util.Date)mp.get("startDate"))&& retDate.before((java.util.Date)mp.get("endDate")))
				{
					
						employeeList.add(employeeView);
					
				}
				
			}
			req.setAttribute("employeeList",employeeList);
	   	 if(((String)req.getSession().getAttribute(STR_MODULE)).equals("EmployeeRetirement"))
			{
				target = "successretirementReport";
			}
	   	 
		}
		catch(Exception ex)
		{
		   target = STR_ERROR;
		   LOGGER.error(ex.getMessage());
		   throw new EGOVRuntimeException(ex.getMessage(),ex);
		}
		req.setAttribute(STR_ALERT_MESSAGE, null);
		return mapping.findForward(target);
	}

	
	public ActionForward executeSearchForMatuarity(ActionMapping mapping,ActionForm form,HttpServletRequest req,HttpServletResponse res)
	 throws IOException,ServletException
	{
		String target =null;
		
		List<EmployeeView> employeeList = new ArrayList<EmployeeView>();
		try
		{
			SearchForm searchForm = (SearchForm)form;
			String finYear = req.getParameter(STR_FINYEAR);
			String mon = req.getParameter(STR_MONTHID);
			CFinancialYear cufinancial  =EisManagersUtill.getCommonsService().findFinancialYearById(Long.valueOf(finYear));
			Map mp = EisManagersUtill.getStartingAndEndingDateForMonthAanFyer(Integer.valueOf(mon),cufinancial);
			List lOfemployeeList =employeeService.searchEmployee(Integer.valueOf(searchForm.getDepartmentId()),Integer.valueOf(searchForm.getDesignationId()),searchForm.getCode(),searchForm.getName(),searchForm.getSearchAll());
			for(Iterator iter = lOfemployeeList.iterator();iter.hasNext();)
			{
				LOGGER.info("employeeView");
				EmployeeView employeeView = (EmployeeView)iter.next();
				LOGGER.info("employeeView"+employeeView.getEmployeeCode());
				Integer id  = employeeView.getId();
				PersonalInformation personalInformation = employeeService.getEmloyeeById(id);
				LOGGER.info("personalInformation"+personalInformation.getEmployeeFirstName());
				if(personalInformation.getMaturityDate()!=null && personalInformation.getMaturityDate().after((java.util.Date)mp.get("startDate"))&&personalInformation.getMaturityDate().before((java.util.Date)mp.get("endDate")))
				{
					employeeList.add(employeeView);
					
				}
			}
	   	 	req.setAttribute("employeeList",employeeList);
	   	 if(((String)req.getSession().getAttribute(STR_MODULE)).equals("EmployeeMatuarity"))
			{
				target = "successmatuarityReport";
			}
	   	 

		}
		catch(Exception ex)
		{
		   target = STR_ERROR;
		   LOGGER.error(ex.getMessage());
		   throw new EGOVRuntimeException(ex.getMessage(),ex);
		}
		req.setAttribute(STR_ALERT_MESSAGE, null);
		return mapping.findForward(target);
	}
	public ActionForward executeDisiplinaryAction(ActionMapping mapping,ActionForm form,HttpServletRequest req,HttpServletResponse res)
	 throws IOException,ServletException
	{
		String target =null;
		try
		{
			SearchForm searchForm = (SearchForm)form;
			List disiplinaryList = employeeService.getListByAppNoAndMeMoNo(searchForm.getAppNo(),searchForm.getChargeMemoNo(),Integer.valueOf(req.getParameter("empId")));
			req.setAttribute("disiplinaryList",disiplinaryList);
			LOGGER.info("disiplinaryList"+disiplinaryList);
			target = "successDisiplinary";
			

		}
		catch(Exception ex)
		{
		   target = STR_ERROR;
		   LOGGER.error(ex.getMessage());
		   throw new EGOVRuntimeException(ex.getMessage(),ex);
		}
		req.setAttribute(STR_ALERT_MESSAGE,null);
		return mapping.findForward(target);
	}
	public ActionForward executeLeaveAction(ActionMapping mapping,ActionForm form,HttpServletRequest req,HttpServletResponse res)
	 throws IOException,ServletException
	{
		String target =null;
		try
		{
			SearchForm searchForm = (SearchForm)form;
			List leaveList = eisLeaveService.getListByAppNoAndLeaveType(searchForm.getAppNo(),Integer.valueOf(searchForm.getTypeId()),Integer.valueOf(req.getParameter("empId")));
			req.setAttribute("leaveList",leaveList);
			LOGGER.info("leaveList"+leaveList);
			target = "searchLeave";
			
		}
		catch(Exception ex)
		{
		   target = STR_ERROR;
		   LOGGER.error(ex.getMessage());
		   throw new EGOVRuntimeException(ex.getMessage(),ex);
		}
		req.setAttribute(STR_ALERT_MESSAGE,null);
		return mapping.findForward(target);
	}

	public ActionForward executeReportSearch(ActionMapping mapping,ActionForm form,HttpServletRequest req,HttpServletResponse res)
	 throws IOException,ServletException
	{
		String target =null;
		String alertMessage=null;
		try
		{
			String empId = req.getParameter("Id");
			EmployeeService eisManager = getEmployeeService();
			Collection employeeListReport =eisManager.searchEmployee(Integer.valueOf(empId));
			req.setAttribute("employeeListReport",employeeListReport);
			target = "successReport";
			alertMessage="Executed successfully";
		}
		catch(Exception ex)
		{
		   target = STR_ERROR;
		   LOGGER.error(ex.getMessage());
		   throw new EGOVRuntimeException(ex.getMessage(),ex);
		}
		req.setAttribute(STR_ALERT_MESSAGE, alertMessage);
		return mapping.findForward(target);
	}
	public ActionForward executeAttReport(ActionMapping mapping,ActionForm form,HttpServletRequest req,HttpServletResponse res)
	 throws IOException,ServletException
	{
		String target =null;
		String alertMessage=null;

		try
		{
			HttpSession session=req.getSession();
			List<EmployeeAttendenceReport> reportList = null;
			Map finParamsMap=new LinkedHashMap<String , Integer>();
			String month = req.getParameter(STR_MONTHID);
			SearchForm searchForm = (SearchForm)form;
			String finYear = req.getParameter(STR_FINYEAR);
			String functionaryId = searchForm.getFunctionaryId();
			EmpLeaveService lManager = getEisLeaveService();
			if(searchForm.getDepartmentId()!=null && !searchForm.getDepartmentId().isEmpty())
				finParamsMap.put("departmentId", Integer.valueOf(searchForm.getDepartmentId()));
				
				if(searchForm.getFunctionaryId()!=null && !searchForm.getFunctionaryId().isEmpty())
					finParamsMap.put("functionaryId", Integer.valueOf(searchForm.getFunctionaryId()));
				
				if(searchForm.getFunctionId()!=null && !searchForm.getFunctionId().isEmpty())
					finParamsMap.put("functionId", Integer.valueOf(searchForm.getFunctionId()));
			if(req.getParameter(EisConstants.ESS)==null)
			{
			reportList =lManager.searchEmployeeForAttRept(Integer.valueOf(searchForm.getDesignationId()),searchForm.getCode()
					,searchForm.getName(),searchForm.getSearchAll(),month,finYear,finParamsMap);
			}
			else if(req.getParameter(EisConstants.ESS).equals("1"))
			{
				List fYMasterList=EisManagersUtill.getCommonsService().getAllFinancialYearList();
				session.setAttribute("finMap",getFinMap(fYMasterList));
				PersonalInformation employee=employeeService.getEmpForUserId(Integer.valueOf(EGOVThreadLocals.getUserId()));
				searchForm.setCode(employee.getCode());
				searchForm.setName(employee.getEmployeeFirstName());
				if(req.getParameter(STR_MONTHID)==null || req.getParameter(STR_MONTHID).trim().isEmpty())
				{
					month=	Integer.valueOf(Calendar.getInstance().get(Calendar.MONTH) + 1).toString();
				}
				else
				{
					month=	req.getParameter(STR_MONTHID);
				}
				if(req.getParameter(STR_FINYEAR)==null || req.getParameter(STR_FINYEAR).trim().isEmpty())
				{
					finYear=EisManagersUtill.getCommonsService().getCurrYearFiscalId();
				}
				else
				{
					finYear=req.getParameter(STR_FINYEAR);
				}
				if(employee!=null)
				{
				reportList =lManager.searchEmployeeForAttRept(Integer.valueOf(0),employee.getCode(),searchForm.getName(),searchForm.getSearchAll(),month,finYear,finParamsMap);
				}
			}
			req.setAttribute("EmployeeAttendenceReportList",reportList);
			target = "attendenceReport";
			alertMessage="Executed successfully";
		}
		catch(Exception ex)
		{
		   target = STR_ERROR;
		   LOGGER.error(ex.getMessage());
		   throw new EGOVRuntimeException(ex.getMessage(),ex);
		}
		req.setAttribute(STR_ALERT_MESSAGE, alertMessage);
		return mapping.findForward(target);
	}
	
	public ActionForward executeSearchForLeaveCompoff(ActionMapping mapping,ActionForm form,HttpServletRequest req,HttpServletResponse res)
	 throws IOException,ServletException
	{
		String target ="successLeaveCompOff";
		String alertMessage=null;
		try
		{
			Collection employeeList=null;
			EmployeeService eisManager = getEmployeeService();
			SearchForm searchForm = (SearchForm)form;
			String finYear = req.getParameter(STR_FINYEAR);
			String mon = req.getParameter(STR_MONTHID);
			String functionaryId = req.getParameter("functionaryId");
			String desgId = "0";
			String fromSearchName = req.getParameter("fromName");
			String toSearchName = req.getParameter("toName");
			
			Map employeeMap = null;
			if(!searchForm.getDesignationId().equals(""))
				{
					desgId = searchForm.getDesignationId();
				}
			//searchForm.getDepartmentId()
			//searchForm.getDesignationId()//functionaryId//searchForm.getStatus()//searchForm.getEmpType()
			employeeList=eisManager.searchEmployee(Integer.valueOf(0),Integer.valueOf(0),
          		  Integer.valueOf(0),searchForm.getCode(),searchForm.getName(),Integer.valueOf(0),
          		  Integer.valueOf(0));
	 
           
           
          if(employeeList.isEmpty())
          {
          	alertMessage= "Search yielded no results";
          	
          }
          
	   	 	req.setAttribute("employeeList",employeeList);
			
			
					
		}
		catch(Exception ex)
		{
		   target = STR_ERROR;
		   LOGGER.error(ex.getMessage());
		   throw new EGOVRuntimeException(ex.getMessage(),ex);
		}
		req.setAttribute(STR_MODULE,(String)req.getAttribute(STR_MODULE));
		req.setAttribute(STR_ALERT_MESSAGE, alertMessage);
		return mapping.findForward(target);
	}
	/**
	 * @return the eisManagr
	 */
	public EmployeeService getEmployeeService() {
		return employeeService;
	}


	/**
	 * @param eisManagr the eisManagr to set
	 */
	public void setEmployeeService(EmployeeService employeeService) {
		this.employeeService = employeeService;
	}


	/**
	 * @return the eisLeaveManagr
	 */
	public EmpLeaveService getEisLeaveService() {
		return eisLeaveService;
	}
	
	private java.util.Date getDateString(String dateString)
	{

		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		java.util.Date d = null;
		try
		{
			d = dateFormat.parse(dateString);

		} catch (Exception e)
		{
			LOGGER.debug(e.getMessage());
			throw new EGOVRuntimeException(e.getMessage(),e);
		}
		return d;
	}
	
	public Map getFinMap(List list)
	{
		Map<Long,String> finMap = new TreeMap<Long,String>();
		try {
			for(Iterator iter = list.iterator();iter.hasNext();)
			{
				CFinancialYear cFinancialYear = (CFinancialYear)iter.next();
				finMap.put(cFinancialYear.getId(), cFinancialYear.getFinYearRange());
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			throw new EGOVRuntimeException(STR_EXCEPTION + e.getMessage(),e);
		}
		return finMap;
	}


	/**
	 * @param eisLeaveManagr the eisLeaveManagr to set
	 */
	public void setEisLeaveService(EmpLeaveService eisLeaveService) {
		this.eisLeaveService = eisLeaveService;
	}

	public PersonalInformationService getPersonalInformationService() {
		return personalInformationService;
	}

	public void setPersonalInformationService(
			PersonalInformationService personalInformationService) {
		this.personalInformationService = personalInformationService;
	}





	private final static String STR_MODULE ="module";
	private final static String STR_ERROR="error";	
	private final static String STR_ALERT_MESSAGE = "alertMessage";
	private final static String STR_FINYEAR="finYear";
	private final static String STR_MONTHID="monthId";
}
