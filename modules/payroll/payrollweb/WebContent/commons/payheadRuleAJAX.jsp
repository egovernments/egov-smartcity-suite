<%@ page language="java" import="java.util.*,org.egov.infstr.utils.HibernateUtil,org.egov.payroll.model.*,org.egov.payroll.utils.*,org.egov.payroll.rules.*" %>

	<%

	
	StringBuffer result = new StringBuffer();
	String salarycodeHead = request.getParameter("salarycode");
	String effectiveFrom = request.getParameter("effectiveFrom");
	System.out.println("salarycode-------"+salarycodeHead);
	System.out.println("effectiveForm-------"+effectiveFrom);
	
	if(request.getParameter("action").equalsIgnoreCase("getPayheadRuleBySalarycodeEffectiveForm")){
		System.out.println("inside---------------");
		SalaryCodes salarycode = PayrollManagersUtill.getPayheadService().getSalaryCodeByHead(salarycodeHead);
		PayheadRule payheadRule = PayheadRuleUtil.getPayheadRule(salarycode, new Date(effectiveFrom));
		if(payheadRule == null)
			result.append("false");
		else
			result.append("true");
		result.append("^");
	/*	String query="select head from EGPAY_SALARYCODES where HEAD ='"+name+"'";
		rs = stmt.executeQuery(query);
		try	{
			if(rs.next()){
				result.append("false");
			}
			else {
				result.append("true");
			}
			result.append("^");
			rs.close();
		}catch(Exception e){
			System.out.println(e.getMessage());
			e.printStackTrace();
			throw e;
		}*/
	}


	System.out.println("result--------"+result.toString());
	response.setContentType("text/xml");
	response.setHeader("Cache-Control", "no-cache");
	response.getWriter().write(result.toString());
	%>


