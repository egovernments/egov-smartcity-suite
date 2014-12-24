
<%@ page language="java" import="java.util.*,
		 org.egov.infstr.utils.*,
		 		 org.egov.pims.empLeave.service.*,
		 		 java.sql.Date ,
		 		 org.egov.pims.utils.*,
		 		 org.egov.commons.CFinancialYear,
		 		 java.text.SimpleDateFormat"
		 
		  	
		  				  		
%>
<%@page import="org.egov.pims.model.PersonalInformation"%>
	<%
	StringBuffer result = new StringBuffer();
	try
		{
		
			String fromDateStr = request.getParameter("fromDate");
			String toDateStr = request.getParameter("toDate");
			String empId = request.getParameter("empId");
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			java.util.Date frodat=null;
			java.util.Date todat=null;
			if(toDateStr!=null && fromDateStr!=null && !fromDateStr.equals("") && !toDateStr.equals(""))
			{			
				frodat = sdf.parse(fromDateStr);
				todat = sdf.parse(toDateStr);				
				SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");               
				String id = EisManagersUtill.getCommonsService().getFinancialYearId(formatter.format(frodat));
				CFinancialYear cinancialYear = EisManagersUtill.getCommonsService().findFinancialYearById(new Long(id));				
				//ServiceLocator 		serviceloc      = 	ServiceLocator.getInstance();
				//EmpLeaveManagerHome lManagerHome			=	(EmpLeaveManagerHome)serviceloc.getLocalHome("EmpLeaveManagerHome");
				//EmpLeaveManager 		lManager			=	lManagerHome.create();
				PersonalInformation employee = EisManagersUtill.getEmployeeService().getEmloyeeById(Integer.parseInt(empId));
				int workingDays = EisManagersUtill.getEmpLeaveService().getNoOfWorkingDaysBweenTwoDatesForEmployee(frodat,todat,employee).size();
				//int workingDays = lManager.calculateNoOfWorkingDaysBweenTwoDates(frodat,todat);
				result.append(new Integer(workingDays).toString());
				
			}
		}catch(Exception e)
		{
			System.out.println(e.getMessage());
		}
		result.append("^");
		response.setContentType("text/xml");
		response.setHeader("Cache-Control", "no-cache");
		response.getWriter().write(result.toString());
	%>


