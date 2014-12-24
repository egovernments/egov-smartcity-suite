
<%@ page import="java.util.*,
		 org.egov.infstr.utils.*,
		 		java.util.Calendar,
		 		 org.apache.log4j.Logger,
		 		 org.egov.pims.*,
		 		 org.egov.pims.utils.*,
		 		 org.egov.pims.empLeave.dao.*,
		 		 org.egov.pims.empLeave.model.*,
		 		 org.egov.pims.empLeave.service.*,
		 		 org.egov.pims.dao.*,
		 		 org.egov.pims.model.*,
		 		 org.egov.pims.service.*,
		 		 org.egov.commons.CFinancialYear,
		 		 org.egov.commons.dao.CommonsDaoFactory,
		 		 org.egov.commons.dao.FinancialYearDAO,
		 		 org.egov.infstr.commons.*,
		 		 org.egov.pims.commons.client.*,
		 		 org.egov.infstr.commons.dao.*,
		 		 org.egov.infstr.commons.service.*,
				 org.egov.pims.empLeave.model.*,
		 		 org.egov.lib.address.dao.AddressDAO,
		 		 org.egov.lib.address.dao.AddressTypeDAO,
		 		 org.egov.lib.address.model.*,
				 org.egov.infstr.commons.dao.*,
		 		 org.egov.lib.address.dao.*,
		 		 java.text.SimpleDateFormat,
		 org.egov.pims.client.*"
		  	
		  				  		
%>


	<%
	
	    String enteredDate=null;
		enteredDate = request.getParameter("enteredDate");
		String empId = request.getParameter("empId");
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		StringBuffer result = new StringBuffer();		
		String checkIfHoliday="false";
		try
		{		
			//ServiceLocator 		serviceloc      = 	ServiceLocator.getInstance();
			//EmpLeaveManagerHome lManagerHome			=	(EmpLeaveManagerHome)serviceloc.getLocalHome("EmpLeaveManagerHome");
			EmpLeaveService 		lManager			=	EisManagersUtill.getEmpLeaveService();
			//if(lManager.isHolidayEnclosed())
			//{
				if(enteredDate!=null && empId!=null)
				{ 					
	   				Date entryDte = sdf.parse(enteredDate);
	  				CFinancialYear financialObj = EisManagersUtill.getFinYearForGivenDate(entryDte);
	  				//Set holidatSet = lManager.getHolidaySet(FinancialObj);
	  				PersonalInformation employee = EisManagersUtill.getEmployeeService().getEmloyeeById(Integer.parseInt(empId));
	  				Set holidatSet = lManager.getHolidaySetForEmployeInDateRange(financialObj.getStartingDate(),financialObj.getEndingDate(),employee);	   
		 			Calendar calendar = Calendar.getInstance();
		  			calendar.setTime(entryDte);
		  			if(holidatSet.contains(new java.sql.Date(calendar.getTime().getTime()).toString()))
		  			{
		     			checkIfHoliday = "true";
		     
		   			}
				}
			//}
		   result.append(checkIfHoliday);
		}
		catch(Exception e)
		{
			response.setStatus(response.SC_INTERNAL_SERVER_ERROR);
			throw e;
		}
		result.append("^");
		response.setContentType("text/xml");
		response.setHeader("Cache-Control", "no-cache");
		response.getWriter().write(result.toString());
	%>


