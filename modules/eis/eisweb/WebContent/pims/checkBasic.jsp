
<%@ page import="java.util.*,
		 org.egov.infstr.utils.*,
		 java.math.BigDecimal,
		 org.egov.payroll.model.*,
		 org.egov.payroll.services.payslip.*,
		 org.egov.payroll.utils.*,
		 org.egov.pims.client.*"
		  	
		  				  		
%>


	<%
		
		String payHead = request.getParameter("payHead");
		String basic = request.getParameter("basic");
		if(!payHead.equals("0") )
		{
			
			
			StringBuffer result = new StringBuffer(100);
			try
			{
			PayRollManager payRollManager =  PayrollManagersUtill.getPayRollManager();
			PayScaleHeader payScaleHeader =payRollManager.getPayScaleHeaderById(new Integer(payHead));
			BigDecimal from = payScaleHeader.getAmountFrom();
			BigDecimal to = payScaleHeader.getAmountTo();
			BigDecimal basicBig  =new BigDecimal(basic);
			System.out.println("from"+from);
			System.out.println("to"+to);
			System.out.println("basicBig"+basicBig);
			
			if(basicBig.intValue() >= from.intValue()  &&   basicBig.intValue() <= to.intValue())
			{
				result.append("true");
			}
			else
			{
				result.append("false");
				result.append("^");
				result.append("the basic should be between " + from + " to " + to);
				
			}
			}
			catch(Exception e)
			{
				System.out.println(e.getMessage());
			}
			System.out.println("sdgfsdgf"+result.toString());
			result.append("^");
			System.out.println("sdgfsdgf"+result.toString());
			response.setContentType("text/xml");
			response.setHeader("Cache-Control", "no-cache");
			response.getWriter().write(result.toString());
		}
	%>


