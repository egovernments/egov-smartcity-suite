
<%@ page import="java.util.*,
		 org.egov.infstr.utils.*,
		 		 org.egov.payroll.services.*,
				 org.egov.payroll.model.*,
				 org.egov.payroll.utils.*,
				 java.text.*,
		 		 java.text.SimpleDateFormat"
		  	
		  				  		
%>


	<%
		String month = (String)request.getParameter("month");
		String financialYr = (String)request.getParameter("financialYr");
		Integer monthId=null;
		Integer finYrId=null;
		if(month != null && !month.equals("") && !month.equals("null") && financialYr != null && !financialYr.equals("") && !financialYr.equals("null"))
		{
			
			monthId=new Integer(month);
			finYrId=new Integer(financialYr);
			
		}
		
					StringBuffer result = new StringBuffer();
					try
					{
						java.util.Date DateVal=null;
						Date currDate = new Date();
						SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
						Date date=PayrollManagersUtill.getPayRollService().getEndDateOfMonthByMonthAndFinYear(monthId.intValue(),finYrId.intValue());
						System.out.println("Check Date"+date+"Current DAte"+currDate);
						if(date.after(currDate))
						{
							
							result.append("true");
						}
						else
						{
							
							result.append("false");
						}
						
					}
					catch(Exception e)
					{
						System.out.println(e.getMessage());
						e.printStackTrace();

					}
					result.append("^");
					response.setContentType("text/xml");
					response.setHeader("Cache-Control", "no-cache");
					response.getWriter().write(result.toString());
			
	%>


