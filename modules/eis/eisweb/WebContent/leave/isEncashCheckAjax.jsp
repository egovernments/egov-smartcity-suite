<%@ page language="java" %>
<%@ page import="java.util.*,
				 org.egov.infstr.utils.*,
		 		 org.egov.pims.empLeave.model.*,
		 		 org.egov.pims.empLeave.service.*"
		 		
%>

<%
		
		
		EmpLeaveServiceImpl empLeaveServiceImpl=new EmpLeaveServiceImpl();
		String result=null;
		String leaveType=request.getParameter("leaveTypeId");
		TypeOfLeaveMaster typeOfLeaveMaster=empLeaveServiceImpl.getTypeOfLeaveMasterById(new Integer(leaveType));
		Character isEncashable=typeOfLeaveMaster.getIsEncashable();
		
		if(isEncashable!=null)
		{
		result=isEncashable.toString().trim();
		}
		response.setContentType("text/xml");
		response.setHeader("Cache-Control", "no-cache");
		response.getWriter().write(result);
%>