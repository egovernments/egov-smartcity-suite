<%@ page language="java" %>

	<%

	    String values = (String)request.getAttribute("values");
		response.setContentType("text/xml");
		response.setHeader("Cache-Control", "no-cache");
		response.getWriter().write(values);
	%>

