<%@ page language="java" import="java.util.*" %>


	<%
		String delId = request.getParameter("id");
		String setName = request.getParameter("type");
		
		
		try
		{
			Set delSet = (Set)session.getAttribute(setName);
			System.out.println("DELETE SET>>>>>>>>"+delSet);
			if(delSet!=null)
			{
				
				delSet.add(new Integer(delId));
			}
			else
			{
				
				delSet = new HashSet();
				delSet.add(new Integer(delId));

			}

			System.out.println("delSet>>>>"+delSet);
			session.setAttribute(setName,delSet);

		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
			e.printStackTrace();
		    throw e;
		}

		response.setContentType("text/xml");
		response.setHeader("Cache-Control", "no-cache");

	%>


 