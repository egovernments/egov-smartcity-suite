
/**
 * Created on May 02, 2007
 * @author suhasini.CH
 */

<%@ page language="java" import="java.sql.*,org.egov.infstr.utils.HibernateUtil" %>
	<%
	Connection con=null;
	ResultSet rs=null;
	Statement stmt=null;
	String result = "";
	try
	{
	con = HibernateUtil.getCurrentSession().connection();
	stmt=con.createStatement();
	}
	catch(Exception e)
	{
	System.out.println(e.getMessage());
	}
	
	//Based on the type we will execute the query	

	if(request.getParameter("type").equalsIgnoreCase("checkUniqueness"))
	{ 

	String tablename=request.getParameter("tablename");
	String fieldname=request.getParameter("fieldname");
	String fieldvalue=request.getParameter("fieldvalue");
	String query;
	if(!fieldname.equalsIgnoreCase("PROPERTYID"))
	{
	 query="SELECT "+fieldname+" FROM "+tablename+" where upper("+fieldname+") = '"+fieldvalue.toUpperCase()+"' ";
	
	}
	else
	{
	  query="SELECT "+fieldname+" FROM "+tablename+" where upper("+fieldname+") = '"+fieldvalue+"' ";
	  System.out.println("query"+query);
	}
	System.out.println("query"+query);
	try
	{
	rs=stmt.executeQuery(query);
	
		if(rs.next())
		{
			result = "true";
		}
		else 
		{
			result = "false";
		}
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

