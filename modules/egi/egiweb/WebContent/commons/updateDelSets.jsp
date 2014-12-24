<%@ page language="java" import="java.util.*, org.egov.infstr.utils.HibernateUtil,org.egov.exceptions.EGOVRuntimeException" %>


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
			e.printStackTrace();
			HibernateUtil.rollbackTransaction();
			throw new EGOVRuntimeException("Exception occured -----> "+e.getMessage());
		}

		response.setContentType("text/xml;charset=utf-8");
		response.setHeader("Cache-Control", "no-cache");

	%>


