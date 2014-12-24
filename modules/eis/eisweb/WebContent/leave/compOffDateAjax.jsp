

<%
		String result=(String)request.getAttribute("isLEaveHolidayExists");    
		response.setContentType("text/xml");
		response.setHeader("Cache-Control", "no-cache");
		response.getWriter().write(result);
		
%>		