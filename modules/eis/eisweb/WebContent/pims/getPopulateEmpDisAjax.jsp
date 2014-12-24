
<%@ page language="java" import="java.util.*,
		 org.egov.infstr.utils.*,
		 		 org.egov.pims.empLeave.service.*,
		 		 java.sql.Date ,
		 		  java.util.StringTokenizer,
		 		 java.text.SimpleDateFormat"
		 
		  	
		  				  		
%>
	<%
		String type = request.getParameter("type");
		System.out.println("55555555555"+type);
		try
		{
			Set delSet = (Set)session.getAttribute("DelSet");
			delSet.add(new Integer(type));
			System.out.println("delSet"+delSet);
			
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
		}
		
		response.setContentType("text/xml");
		response.setHeader("Cache-Control", "no-cache");
		
	%>


