<%@ page language="java" import="java.sql.*,org.egov.infstr.utils.HibernateUtil,
				org.egov.infstr.client.filter.EGOVThreadLocals,org.egov.infstr.utils.EGovConfig,org.egov.payroll.utils.PayrollManagersUtill,org.egov.payroll.utils.PayrollExternalInterface,org.egov.payroll.utils.PayrollExternalImpl,java.text.SimpleDateFormat,
				org.egov.commons.EgwStatus,org.egov.payroll.utils.PayrollConstants,org.egov.infstr.commons.dao.GenericDaoFactory,org.egov.infstr.utils.*" %>

	<%

	Connection con=null;
	ResultSet rs=null;
	Statement stmt=null;
	String empId = request.getParameter("empId");
	String year = request.getParameter("year");
	String month = request.getParameter("month");
	EgwStatus expCreateStatus = null;
	EgwStatus expAppStatus = null;
	EgwStatus expclosedStatus = null;
		PayrollExternalInterface payrollExternalInterface=new PayrollExternalImpl();
	try	{
		con = HibernateUtil.getCurrentSession().connection();
		stmt=con.createStatement();
		expCreateStatus = (EgwStatus) payrollExternalInterface.getStatusByModuleAndDescription(
		PayrollConstants.EMP_EXCEPTION_MODILE, GenericDaoFactory.getDAOFactory().getAppConfigValuesDAO().getAppConfigValueByDate("EmpException","ExceptionCreatedStatus",new java.util.Date()).getValue());

		expAppStatus = (EgwStatus) payrollExternalInterface.getStatusByModuleAndDescription(
		PayrollConstants.EMP_EXCEPTION_MODILE, GenericDaoFactory.getDAOFactory().getAppConfigValuesDAO().getAppConfigValueByDate("EmpException","ExceptionApprovedStatus",new java.util.Date()).getValue());
		
		expclosedStatus = (EgwStatus) payrollExternalInterface.getStatusByModuleAndDescription(
		PayrollConstants.EMP_EXCEPTION_MODILE, GenericDaoFactory.getDAOFactory().getAppConfigValuesDAO().getAppConfigValueByDate("EmpException","ExceptionClosedStatus",new java.util.Date()).getValue());

	}catch(Exception e){
		System.out.println(e.getMessage());
		e.printStackTrace();
		throw e;
	}

	if(request.getParameter("action").equalsIgnoreCase("getExceptionForEmp")){ 
	
		
		System.out.println("EXCEPTION FOR EMPLOYEE---------"+empId);
		String query="select * from egpay_exception where id_employee='"+empId+"' and FINANCILAYEAR_ID='"+year+"' and month='"+month+"' and ( status="+expCreateStatus.getId()+" or (status="+expAppStatus.getId()+" and todate is null))";		
		rs=stmt.executeQuery(query);
		System.out.println("query=="+query);
	}
	//employee shouldn't have any future payslips while comparing with fromdate and they shouldn't have any opened exceptions and exception fromdate date should be greater than already existing exceptions todate
	if(request.getParameter("action").equalsIgnoreCase("checkExceptionFormDate")){ 	
		
		String date=(String)request.getParameter("fromdate");
		String expid = (String)request.getParameter("expid");
		String month1=date.split("/")[1];
		if(Integer.parseInt(month1)<4) 
		{
		month1=Integer.parseInt(month1)+12+"";
		}
		Integer status=(payrollExternalInterface.getStatusByModuleAndDescription("PaySlip",
				GenericDaoFactory.getDAOFactory().getAppConfigValuesDAO().getAppConfigValueByDate("Payslip","PayslipCancelledStatus",new java.util.Date()).getValue())).getId();
		String query="select * from egpay_emppayroll p1 where  p1.id_employee="+empId+" and ("
		+"p1.financialyearid >= (select id from financialyear where startingdate<=to_date('"+date+"', 'dd/mm/yyyy') and endingdate>=to_date('"+date+"', 'dd/mm/yyyy') ) and "
		+month1+"<=(select max(case p.month when 1 then 13 when 2 then 14 when 3 then 15 else p.month end) from egpay_emppayroll p where p.id_employee="+empId
		+" and p.financialyearid >=(select id from financialyear where startingdate<=to_date('"+date+"', 'dd/mm/yyyy') and endingdate>=to_date('"+date+"', 'dd/mm/yyyy') )) )"
		+" and id_employee not in ( select id_employee from egpay_exception where id_employee="+empId+" and status!="+expclosedStatus.getId()+" and todate>=to_date('"+date+"', 'dd/mm/yyyy')";
		if(expid!=null && !expid.equals("null"))
		{
		  query = query+" and id!= "+expid;
		}
		query = query + " ) and status !="+status;
		System.out.println("query=="+query);
		rs=stmt.executeQuery(query);
		
	}
	StringBuffer result = new StringBuffer();
	
	
	try
	{
		if(rs.next()){
			result.append("false");
		}
		else {
			result.append("true");
		}
		System.out.println("result=="+result);
	result.append("^");	
	}	
	catch(Exception e)
	{
	System.out.println(e.getMessage());
	e.printStackTrace();
	throw e;
	}
finally{
            if(rs != null)
                rs.close();
            if(stmt != null)
               EgovDatabaseManager.releaseConnection(stmt);     
    }

	response.setContentType("text/xml");
	response.setHeader("Cache-Control", "no-cache");
	response.getWriter().write(result.toString());
	%>
