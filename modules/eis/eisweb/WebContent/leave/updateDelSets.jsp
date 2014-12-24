<%@ page language="java" import="java.util.*" %>
		 
		  	
	<%
		String delId = request.getParameter("id");
		String setName = request.getParameter("type");
		System.out.println("55555555555"+setName);
		try
		{
			Set delSet = (Set)session.getAttribute(setName);
			delSet.add(new Integer(delId));
			System.out.println("delSet"+delSet);
			
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
		}
		
		response.setContentType("text/xml");
		response.setHeader("Cache-Control", "no-cache");
		
	%>


