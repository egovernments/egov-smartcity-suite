
<%@ page import="java.util.*,
		 org.egov.infstr.utils.*,
		 		 org.egov.pims.empLeave.service.*,
		 		 java.text.SimpleDateFormat"
		  	
		  				  		
%>


	<%
		String attType = (String)request.getParameter("attType");
		Integer attIntType=null;
		
		
		if(attType != null && !attType.equals("") && !attType.equals("null"))
		{
			
			attIntType=Integer.valueOf(attType);
			System.out.println("ATTENDANCE TYPE--------------"+attIntType);
			StringBuffer result = new StringBuffer(100);
					try
					{
						
						EmpLeaveServiceImpl empLeaveServiceImpl=new EmpLeaveServiceImpl();
						
								if(empLeaveServiceImpl.checkForCompOffApprove(attIntType)==true)
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
			
		}
				
					
				
		
	%>


