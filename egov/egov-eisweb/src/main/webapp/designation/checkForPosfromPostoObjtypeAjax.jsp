<%@ page language="java" %>
<%@ page import="java.util.*, org.egov.infstr.utils.*,	
				 org.egov.infstr.utils.StringUtils,
				 org.egov.pims.utils.*,
				 org.egov.pims.commons.service.*,
				 org.apache.commons.lang.*,
				  java.io.*"

%>

	<% 
	    StringBuffer result = new StringBuffer();
		try
		{
			String posFrom=request.getParameter("posFrom");
			String posTo=request.getParameter("posTo");
			String objType=request.getParameter("objType");
			String posHir=request.getParameter("posHir");
			

			if(!StringUtils.trimToEmpty(posFrom).equals("") && !StringUtils.trimToEmpty(posTo).equals("") && !StringUtils.trimToEmpty(objType).equals(""))
			{
				
				if(EisManagersUtill.getEisCommonsService().checkPositionHeirarchy(new Integer(posFrom),new Integer(posTo),new Integer(objType),posHir)==true)
				{
					result.append("true");
				}
				else
				{
					result.append("false");
				}
				
				


				
			}
		}
		catch(Exception e){
			
			response.setStatus(response.SC_INTERNAL_SERVER_ERROR);
			throw new Exception("Exception occured in checkFromPosfromPosToObjectType.jsp"+e);
			
		}
		result.append("^");
		response.setContentType("text/plain");
		response.setHeader("Cache-Control", "no-cache");
		response.getWriter().write(result.toString());
		System.out.println(result.toString());
	%>