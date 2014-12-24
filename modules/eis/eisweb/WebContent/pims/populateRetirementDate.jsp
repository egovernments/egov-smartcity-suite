
<%@ page language="java" import="java.util.*,
		 org.egov.infstr.utils.*,
		 		 org.egov.pims.empLeave.service.*,
		 		 java.sql.Date ,
		 		 org.egov.pims.utils.*,
		 		 org.egov.commons.CFinancialYear,
		 		 java.text.SimpleDateFormat"
		 
		  	
		  				  		
%>
	<%
	
	
		String dob = request.getParameter("dob");
		String age = request.getParameter("age");
		System.out.println("dob"+dob);
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		java.util.Date dteob = sdf.parse(dob);
		StringBuffer result = new StringBuffer(1000);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(dteob);
		calendar.add(Calendar.YEAR ,new Integer(age).intValue());
		
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


