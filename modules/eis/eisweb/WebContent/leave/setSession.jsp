<%@ page language="java" import="java.sql.*"%>

	<%
		
		System.out.println("QQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQ"+(Boolean)session.getAttribute("compOff"));
		Boolean compOff = (Boolean)session.getAttribute("compOff");
		compOff = new Boolean(true);	
		session.setAttribute("compOff",compOff);
		System.out.println("QQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQ"+(Boolean)session.getAttribute("compOff"));
		response.setContentType("text/xml");
		response.setHeader("Cache-Control", "no-cache");
	%>


