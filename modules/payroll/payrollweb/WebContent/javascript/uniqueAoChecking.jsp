/**
 * Created on May 02, 2007
 * @author suhasini.CH
 */

<%@ page language="java" import="java.sql.*,org.egov.infstr.utils.HibernateUtil" %>
	<%
	Connection con=null;
	ResultSet rs=null;
	ResultSet rs1=null;
	Statement stmt=null;
	String result = "";
	try
	{
	con = HibernateUtil.getCurrentSession().connection();
	stmt=con.createStatement();
	}
	catch(Exception e)
	{
	System.out.println("Error Occured::::::"+e.getMessage());
	}
	
	//Based on the type we will execute the query	

	if(request.getParameter("type").equalsIgnoreCase("checkUniqueness"))
	{ 
	String idProperty = request.getParameter("idProperty");
	String fieldvalue = request.getParameter("fieldvalue");
	String fieldconst = request.getParameter("fieldconst");
	System.out.println("fieldconst-->"+fieldconst);
	String query = "";
	String query1 = "";
	query="SELECT AONUMBER FROM EGPT_PTDEMAND_ARV where upper(AONUMBER) = '"+fieldvalue.toUpperCase()+"' and ID_PROPERTY = '"+idProperty+"' ";	
	query1="SELECT AONUMBER FROM EGPT_PTDEMAND_ARV where upper(AONUMBER) = '"+fieldvalue.toUpperCase()+"' and ARV_TYPE = '"+fieldconst+"' ";

	System.out.println("query"+query);
	try
	{
	rs=stmt.executeQuery(query);	
	
		if(rs.next())
		{ 
		  rs1 = stmt.executeQuery(query1);
		  if(rs1.next())
		  {
			result = "true";
		  }
		  else 
		  {
		      result = "false";
		  }
		}
		else 
		{
			result = "true";
		}
		System.out.println("result-->"+result);
	}
	
	catch(Exception e)
	{
	System.out.println(e.getMessage());
	}

	}
	
	response.setContentType("text/xml");
	response.setHeader("Cache-Control", "no-cache");
	response.getWriter().write(result);
	%>
<html>
<body>
</body>
</html>

