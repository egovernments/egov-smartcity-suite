
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
	
	    Calendar cal = Calendar.getInstance();
        int day = cal.get(Calendar.DATE);
        int month = cal.get(Calendar.MONTH) + 1;
        int year = cal.get(Calendar.YEAR);
		String strDate=day+"/"+month+"/"+year;
		
		String fromDateStr=null;
		String toDateStr=null;
		String finId=null;
		  java.util.Date datFrom=null;
		  java.util.Date datTo=null;
		  java.util.Date dat=null;

		String type=null;
		type = request.getParameter("type");
		String empId = request.getParameter("empId");
		SimpleDateFormat sdf = null;
		SimpleDateFormat sqlUtil = null;

		
	if(request.getParameter("fromDate")!=null && request.getParameter("fromDate")!="")
	{
		
		fromDateStr = request.getParameter("fromDate");
		sdf = new SimpleDateFormat("dd/MM/yyyy");
		datFrom = sdf.parse(fromDateStr);
	}
	else
	{
		fromDateStr=strDate;
		sdf = new SimpleDateFormat("dd/MM/yyyy");
		dat = sdf.parse(fromDateStr);
	}
	if(request.getParameter("toDate")!=null && request.getParameter("toDate")!="")
	{
		
		toDateStr = request.getParameter("toDate");
		sdf = new SimpleDateFormat("dd/MM/yyyy");
		datTo = sdf.parse(toDateStr);
	}
	
		
		StringBuffer result = new StringBuffer();
		int avLeaves =0;
		try
		{
		
			EmpLeaveServiceImpl empLeaveServiceImpl=new EmpLeaveServiceImpl();
			String isEncashment=request.getParameter("isEncashed");
			if(datFrom!=null && datTo!=null)
			{ 
				 //For Given dates				
				if(type!=null && !type.equals("0") && (isEncashment != null || isEncashment.equals("0")))
						{							
							Float avialVal = empLeaveServiceImpl.getAvailableLeavs(new Integer(empId),new Integer(type),datFrom);
							Integer intAvailVal=new Integer(Math.round(avialVal.floatValue()));
							avLeaves=intAvailVal.intValue();												

						}
				
				
				
			}
			else
			 {
				//for current date: works for encashed enable
				if(isEncashment.equals("1"))
						{
							Float avialVal = empLeaveServiceImpl.getAvailableLeavs(new Integer(empId),new Integer(type),dat);
							Integer intAvailVal=new Integer(Math.round(avialVal.floatValue()));
							avLeaves=intAvailVal.intValue();
							
						}
			 }

		   result.append(new Integer(Math.round(avLeaves)));

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


