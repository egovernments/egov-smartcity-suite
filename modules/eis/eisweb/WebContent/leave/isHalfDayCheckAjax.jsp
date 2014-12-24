<%@ page language="java" %>
<%@ page import="java.util.*,
				 org.egov.infstr.utils.*,
		 		 org.egov.pims.empLeave.model.*,
		 		 org.egov.pims.empLeave.service.*"
		 		
%>

<%
		
		EmpLeaveServiceImpl empLeaveServiceImpl=new EmpLeaveServiceImpl();
		String result=null;
		String leaveType=request.getParameter("leaveid");
		
		TypeOfLeaveMaster typeOfLeaveMaster=empLeaveServiceImpl.getTypeOfLeaveMasterById(new Integer(leaveType));
		Character isHalfDay=typeOfLeaveMaster.getIsHalfDay();
		
		if(isHalfDay!=null)
		{
			result=isHalfDay.toString();
		}
		response.setContentType("text/xml");
		response.setHeader("Cache-Control", "no-cache");
		response.getWriter().write(result);
%>