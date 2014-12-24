
<%@ page import="java.util.*,
		 org.egov.infstr.utils.*,
		 		
		 		 org.egov.pims.service.*,
		 		 java.text.SimpleDateFormat"
		  	
		  				  		
%>


	<%
		String posId = (String)request.getParameter("pos");
		System.out.println("position id----->>>>>>>"+posId);
		String dateFrom = (String)request.getParameter("dateFrom");
		String dateTo = (String)request.getParameter("dateTo");
		String empId=null;
		Integer employeeId=null;
		empId=(String)request.getParameter("empId");
		
		String isPrimary = request.getParameter("isPrimary");
		if(empId != null && !empId.equals("") && !empId.equals("null"))
		{
			
			employeeId=new Integer(empId);
			
		}
		if(posId!=null && !posId.equals(""))
		{
				if(!posId.equals("0") )
				{
					SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
					java.util.Date fromDate = null;
					java.util.Date toDate = null;
					if(dateFrom!=null && !dateFrom.equals(""))
						fromDate = sdf.parse(dateFrom.trim());
					if(dateTo!=null && !dateTo.equals(""))
						toDate = sdf.parse(dateTo.trim());

					StringBuffer result = new StringBuffer(100);
					try
					{
						
						EmployeeServiceImpl employeeServiceImpl=new EmployeeServiceImpl();
						if(posId!=null && !posId.equals("") && !posId.equals("undefined") && toDate!=null)
						{
								if(employeeServiceImpl.checkPos(new Integer(posId),fromDate,toDate,employeeId,isPrimary)==true)
								{
									
									result.append("true");
								}
								else
								{
									
									result.append("false");
								}
							
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
		}
	%>


