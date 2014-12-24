
<%@ page language="java" %>
<%@ page import="java.util.*,
				 org.egov.infstr.utils.StringUtils,		 		
				 org.egov.payroll.services.*,
				 org.egov.payroll.model.*,
				 org.egov.payroll.utils.*,
				 org.apache.commons.lang.*,org.egov.pims.model.*,
				 org.egov.pims.utils.*"		 		
%>
	<%
		
		StringBuffer result = new StringBuffer();			
		try
		{				
			String payId=request.getParameter("payId");
			String month=request.getParameter("month");
			String finYear=request.getParameter("finYear");
			String empGrpMstrId = request.getParameter("empCatId");
			System.out.println("empcatid is "+empGrpMstrId);
			if(!StringUtils.trimToEmpty(payId).equals("") && !StringUtils.trimToEmpty(month).equals("") && !StringUtils.trimToEmpty(finYear).equals("") && !StringUtils.trimToEmpty(empGrpMstrId).equals(""))
			{
				PayGenUpdationRule payGenMstrRule=PayrollManagersUtill.getPayheadService().checkRuleBasedOnSalCodeMonFnYrEmpGrp(new Integer(payId),new Integer(month),new Integer(finYear), new Integer(empGrpMstrId));
				if(payGenMstrRule==null)
				{
					System.out.println("true ");
					result.append("true");
				}
				else
				{
					System.out.println("false ");
					result.append("false");
				}
			}
			
		
		}
		catch(Exception e){
			e.printStackTrace();
			response.setStatus(response.SC_INTERNAL_SERVER_ERROR);
			throw new Exception("Exception occured in checkUniqueAjax.jsp"+e);

		}
		result.append("^");
		response.setContentType("text/plain");
		response.setHeader("Cache-Control", "no-cache");
		response.getWriter().write(result.toString());
	%>