
<%@ page language="java" import="java.util.*,
		 org.egov.infstr.utils.*,
		 		 org.egov.pims.empLeave.service.*,
		 		 java.sql.Date ,
		 		 org.egov.pims.utils.*,
		 		 org.egov.pims.client.*,
		 		 org.egov.pims.model.*,
		 		 org.egov.pims.service.*,
		 		 org.egov.commons.CFinancialYear,
		 		 java.text.SimpleDateFormat"
		 
		  	
		  				  		
%>
	<%
	
	
		String gradeId = request.getParameter("gradeId");
		String retireDate="";
		
		 		StringBuffer result = new StringBuffer(100);		
		try
		{
		EmployeeServiceImpl employeeServiceImpl=new EmployeeServiceImpl();
		GenericMaster genericMaster = (GenericMaster)employeeServiceImpl.getGenericMaster(new Integer(gradeId),"GradeMaster");
		GradeMaster gradeMaster =(GradeMaster)genericMaster;

		String age = "";
			
			System.out.println("sdfdfsdfsdfsdf"+gradeMaster.getAge().toString());
			String dob = request.getParameter("dob");
			java.util.Date dte=null;
				retireDate=request.getParameter("retireDte");
				System.out.println("dob"+dob);
					SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

					java.util.Date dteob = sdf.parse(dob);
					
					
					if(retireDate!=null && !retireDate.equals(""))
						{
						   
						   java.util.Date retDte = sdf.parse(retireDate);
							Calendar calendar = Calendar.getInstance();
							calendar.setTime(retDte);
							dte=calendar.getTime();
							age=new Integer(DateUtils.getNumberOfYearPassesed(dteob,retDte)).toString();
					}
					else
					{
						
					Calendar calendar = Calendar.getInstance();
					calendar.setTime(dteob);
					age=gradeMaster.getAge().toString();
					calendar.add(Calendar.YEAR ,new Integer(age).intValue());
					dte = calendar.getTime() ;
					
					}


						
								System.out.println("dte"+dte);
								System.out.println("sdf.format(dte)"+sdf.format(dte));
								result.append(age);
								result.append("^");
								result.append(sdf.format(dte));
		
		
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
		}
		result.append("^");
		response.setContentType("text/xml");
		response.setHeader("Cache-Control", "no-cache");
		response.getWriter().write(result.toString());
	%>



