
<%@ page language="java" import="java.util.*,
		 org.egov.infstr.utils.*,
		 		 org.egov.pims.empLeave.service.*,
		 		 java.sql.Date ,
		 		 org.egov.pims.utils.*,
		 		 org.egov.pims.client.*,
		 		 org.egov.pims.model.*,
		 		 org.egov.pims.service.*,
		 		 org.egov.commons.CFinancialYear,
		 		 java.text.SimpleDateFormat"
		 
		  	
		  				  		
%>
	<%
	
		StringBuffer result = new StringBuffer();
			
		try
		{
				
			String retireDate=request.getParameter("retireDate");
			String dob=request.getParameter("dob");
			int age=0;
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			java.util.Date dteob = sdf.parse(dob);
			java.util.Date retDteob = sdf.parse(retireDate);
			age=DateUtils.getNumberOfYearPassesed(dteob,retDteob);
			result.append(new Integer(age).toString());
			System.out.println("sdf.format(dte)"+sdf.format(retDteob));
			result.append("^");
			result.append(sdf.format(retDteob));
			System.out.println("age==="+age);
			
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



