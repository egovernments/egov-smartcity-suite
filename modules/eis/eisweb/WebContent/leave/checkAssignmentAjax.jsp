<%@ page language="java" %>
<%@ page import="java.util.*,
				 org.egov.infstr.utils.*,
		 		 org.egov.pims.empLeave.model.*,
		 		 org.egov.pims.empLeave.service.*,
				 java.text.SimpleDateFormat,
				 org.egov.pims.model.*,
				 org.egov.pims.utils.*"
		 		
%>

<%
	
		StringBuffer result = new StringBuffer();
		java.util.Date fromdat=null;
		try
		{
		String fromDate=request.getParameter("fromDate");
		String empId=request.getParameter("empId");
		
		if(fromDate!=null && !fromDate.equals("") && empId!=null && !empId.equals(""))
		{
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			fromdat=sdf.parse(fromDate);
			Assignment assignmentObj =EisManagersUtill.getEmployeeService().getAssignmentByEmpAndDate(fromdat,new Integer(empId));
			
			if(assignmentObj!=null)

				result.append("true");
			else
				result.append("false");
		
		}
		}
		catch(Exception e)
		{
			throw e;
		}
		result.append("^");
		response.setContentType("text/xml");
		response.setHeader("Cache-Control", "no-cache");
		response.getWriter().write(result.toString());
%>