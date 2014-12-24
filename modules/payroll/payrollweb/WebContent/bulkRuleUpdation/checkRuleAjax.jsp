<%@ page language="java" %>
<%@ page import="java.util.*, org.egov.infstr.utils.StringUtils,	
				 org.egov.payroll.services.*,
				 org.egov.payroll.model.*,
				 org.egov.payroll.utils.*,
				 org.apache.commons.lang.*,
				 org.egov.pims.utils.*,org.egov.pims.model.*,
				 java.io.*"

%>
	<% 
	    StringBuffer result = new StringBuffer();
		try
		{
			String payId=request.getParameter("payId");
			String month=request.getParameter("month");
			String finYear=request.getParameter("finYear");
			String ruleId=request.getParameter("ruleId");
			String empCatId= request.getParameter("empCatId");

			if(!StringUtils.trimToEmpty(payId).equals("") && !StringUtils.trimToEmpty(month).equals("") && !StringUtils.trimToEmpty(finYear).equals("") && !StringUtils.trimToEmpty(empCatId).equals("") && !StringUtils.trimToEmpty(ruleId).equals(""))
			{
				
					if(PayrollManagersUtill.getPayheadService().checkRuleExists(new Integer(ruleId),new Integer(payId),new Integer(month),new Integer(finYear),Integer.valueOf(empCatId))==true)
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
			throw new Exception("Exception occured in checkUniqueAjax.jsp"+e);

		}
		result.append("^");
		response.setContentType("text/plain");
		response.setHeader("Cache-Control", "no-cache");
		response.getWriter().write(result.toString());
	%>