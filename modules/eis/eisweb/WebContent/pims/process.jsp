<%@ page language="java" import="java.sql.*,java.text.SimpleDateFormat,org.egov.infstr.utils.HibernateUtil,java.text.*,java.util.Date" %>

<%

	Connection con=null;
	ResultSet rs=null;
	Statement stmt=null;
	StringBuffer accCode=new StringBuffer();
	String empModule = "Employee";
	try
	{

		con = HibernateUtil.getCurrentSession().connection();
		stmt=con.createStatement();
	}catch(Exception e){

		System.out.println(e.getMessage());
		e.printStackTrace();
		throw e;
	}
	
	 if(request.getParameter("type").equalsIgnoreCase("getAllEmployeeCodes")){
	String query="SELECT EV.CODE||'`-`'||EV.NAME||'`-`'||EV.ID||'`-`'||DG.DESIGNATION_NAME||'`-`'||D.DEPT_NAME||'`-`'||EV.DATE_OF_FA  || '`-`'|| ph.NAME "+
"AS \"code\" FROM EG_EIS_EMPLOYEEINFO EV ,EG_EMPLOYEE E,EG_DEPARTMENT D,EGPAY_PAYSCALE_EMPLOYEE pe,EGPAY_PAYSCALE_HEADER ph,EG_DESIGNATION DG "+
"WHERE D.ID_DEPT =   EV.DEPT_ID and ph.id = pe.ID_PAYHEADER and pe.ID in (select max(s.ID) from egpay_payscale_employee s where s.EFFECTIVEFROM<=SYSDATE and s.ID_EMPLOYEE=e.ID) "+
"AND pe.ID_EMPLOYEE = e.ID and DG.DESIGNATIONID = EV.DESIGNATIONID AND "+
 "((EV.TO_DATE IS NULL and EV.FROM_DATE <= SYSDATE ) OR (EV.FROM_DATE <= SYSDATE AND EV.TO_DATE >= SYSDATE)) AND EV.isActive=1 AND EV.status in(select s.id from egw_status s where s.moduletype='"+empModule+"' and s.DESCRIPTION='Employed') AND E.ID = EV.ID(+)  ORDER BY EV.CODE";
	rs=stmt.executeQuery(query);
}


	int i = 0;
	try{
	if(rs!=null)
	{
	while(rs.next()){

	if(i > 0) {
		accCode.append("+");
		accCode.append(rs.getString("code"));
	}
	else {
		accCode.append(rs.getString("code"));
	}
	i++;

	}

	accCode.append("^");
	}
	String codeValues=accCode.toString();

	//System.out.println(codeValues);

	response.setContentType("text/xml");
	response.setHeader("Cache-Control", "no-cache");
	response.getWriter().write(codeValues);
	}
	catch(Exception e){
	e.printStackTrace(System.out);
	System.out.println(e.getMessage());
    throw e;
	}
	%>
