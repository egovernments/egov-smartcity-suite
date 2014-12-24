
<%@ page import="java.util.*,
		 org.egov.infstr.utils.*,
		 		 
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
		 		 org.egov.lib.address.dao.AddressDAO,
		 		 org.egov.lib.address.dao.AddressTypeDAO,
		 		 org.egov.lib.address.model.*,
		 		 org.egov.lib.address.dao.*,
		 		 java.text.SimpleDateFormat,
		 org.egov.pims.client.*"
		  	
		  				  		
%>


	<%

		String calYear="";
		String dateStr = request.getParameter("date");
		String finId = request.getParameter("finId");
		SimpleDateFormat sqlUtil = new SimpleDateFormat("dd-MMM-yyyy");

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		java.util.Date dat = null;
		 java.sql.Date fromDate=null;
		StringBuffer result = new StringBuffer(100);
		try
		{
			Map mpFYMap = EisManagersUtill.getFYMap();
			Map mpcuFY = (Map)mpFYMap.get(new Long(finId.trim()));
			if(dateStr!=null && !dateStr.equals(""))
			{
				dat = sdf.parse(dateStr);
				fromDate = new java.sql.Date(dat.getTime());

				if(EisManagersUtill.getEmpLeaveService().isLeaveCalendarBased())
				{
					CalendarYear calendarYr = EisManagersUtill.getEmpLeaveService().getCalendarYearById(new Long(finId));
					if(calendarYr!=null)
					{
						calYear = calendarYr.getCalendarYear();
						if(calYear.equals(dateStr.substring(6)))
							result.append("true");
						else
							result.append("false");
					}

				}
				else
				{
						if(new Integer((String)mpcuFY.get(new Integer(dateStr.substring(3,5)))).toString().equals(dateStr.substring(6)))
						{
							result.append("true");
						}
						else
						{
							result.append("false");
						}
				}
			}
		}
		catch(Exception e)
		{
			throw e;
		}
		
		result.append("^");
		response.setContentType("text/xml");
		response.setHeader("Cache-Control", "no-cache");
		response.getWriter().write(result.toString());
	%>


