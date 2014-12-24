<%@ page language="java" %>
<%@ page import="java.util.*,
				 org.egov.payroll.utils.*,
				 java.math.BigDecimal,
		 		 org.egov.payroll.services.pension.*,"
		 		
%>

<%

		String amount=null;
		String nomineeId=null;
		BigDecimal eligibleAmt=null;
		Integer AmountEntered = null; 
		StringBuffer result = new StringBuffer();
		try
		{
        if(request.getParameter("amount")!=null && !request.getParameter("amount").equals(""))
		{
			amount=(String)request.getParameter("amount");
			AmountEntered=new Integer(amount);
		}
		if(request.getParameter("nomineeId")!=null && !request.getParameter("nomineeId").equals(""))
		    nomineeId=(String)request.getParameter("nomineeId");
		
		if(nomineeId!=null && !nomineeId.equals(""))
		{
			
			Integer nomId = new Integer(nomineeId);
			eligibleAmt=PayrollManagersUtill.getPensionManager().getEligibleAmount(nomId);
			if(AmountEntered.intValue() > eligibleAmt.intValue())
			{
				result.append("false");
				result.append("^");
		        result.append(eligibleAmt.toString());
		
				
			}
			else
				result.append("true");
				
		}
		}
		catch(Exception e)
		{
			throw e;
		}
		
		response.setContentType("text/xml");
		response.setHeader("Cache-Control", "no-cache");
		response.getWriter().write(result.toString());
%>