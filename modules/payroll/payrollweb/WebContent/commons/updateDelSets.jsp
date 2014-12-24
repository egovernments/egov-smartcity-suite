<%@ page language="java" import="java.util.*" %>
	<%
		String delId = request.getParameter("id");
		String setName = request.getParameter("type");
		System.out.println("set name = "+setName +"  ==del id =="+delId);
		try
		{
			Set delSet = (Set)session.getAttribute(setName);
			if(delSet!=null && delSet.size()>0)
			{
				System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>del set not null =="+delSet);
				delSet.add(new Integer(delId));
			}
			else
			{
				delSet= new HashSet();
				System.out.println("delSet with null ==>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+delSet+">>>>>>>>"+setName);
				delSet.add(new Integer(delId));
			
			}
			session.setAttribute(setName,delSet);
			System.out.println("delSet after added into session ==>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+delSet);
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


 