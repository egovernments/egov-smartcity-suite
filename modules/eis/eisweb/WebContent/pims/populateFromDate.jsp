
<%@ page language="java" import="java.util.*,
		 org.egov.infstr.utils.*,
		 		 org.egov.pims.empLeave.service.*,
		 		 java.sql.Date ,
		 		 org.egov.pims.utils.*,
		 		 org.egov.commons.CFinancialYear,
		 		 java.text.SimpleDateFormat"
		 
		  	
		  				  		
%>
	<%
	
	
		String fromDateStr = request.getParameter("fromDate");
		System.out.println("fromDateStr"+fromDateStr);
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		java.util.Date frodat = sdf.parse(fromDateStr);
		StringBuffer result = new StringBuffer(1000);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(frodat);
		calendar.add(Calendar.DATE ,1);
		
		try
		{
		
			java.util.Date dte = calendar.getTime() ;
			System.out.println("dte"+dte);
			System.out.println("sdf.format(dte)"+sdf.format(dte));
			result.append(sdf.format(dte));
		
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


