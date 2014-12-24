
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
		
		StringBuffer result=new StringBuffer();
			
		try
		{
				
			String dateOfFAstr=request.getParameter("dateOfFA");
			String fromDatestr=request.getParameter("fromDate");
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			java.util.Date dteOfFA = null;
			java.util.Date dateFrom = null;
			if(dateOfFAstr!=null & !dateOfFAstr.equals("") && fromDatestr!=null && !fromDatestr.equals(""))
			{
					 dteOfFA = sdf.parse(dateOfFAstr);
					 dateFrom=sdf.parse(fromDatestr);
					 long difference = Math.abs( dateFrom.getTime() - dteOfFA.getTime() );
					 int dayDiff = (int)Math.floor(difference/1000/60/60/24); 
					 Integer IDaysDifference = new Integer(dayDiff);
					 result.append(IDaysDifference.toString());
					 
			}
		
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
		}

		result.append("^");
		response.setContentType("text/xml");
		response.setHeader("Cache-Control", "no-cache");
		response.getWriter().write(result.toString());
	%>