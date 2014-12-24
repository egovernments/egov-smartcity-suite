/**
 * Created on May 02, 2007
 * @author Lokesh
 */

<%@ page language="java" import="java.sql.*,org.egov.infstr.utils.HibernateUtil,org.egov.infstr.utils.*" %>
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
	e.printStackTrace();
	throw e;
	}

	//Based on the type we will execute the query

	if(request.getParameter("type").equalsIgnoreCase("checkingAmount"))
	{
	String tablename=request.getParameter("tablename");
	String fieldname=request.getParameter("fieldname");
	String fieldvalue=request.getParameter("fieldvalue");
	String fieldname1=request.getParameter("fieldname1");
	String fieldvalue1=request.getParameter("fieldvalue1");
	String fieldname2=request.getParameter("fieldname2");
	String fieldvalue2=request.getParameter("fieldvalue2");
	String query="SELECT "+fieldname+" FROM "+tablename+" where upper("+fieldname+") >= "+fieldvalue.toUpperCase()+" and upper("+fieldname1+") = '"+fieldvalue1.toUpperCase()+"' and upper("+fieldname2+") = '"+fieldvalue2.toUpperCase()+"' ";
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
	e.printStackTrace();
	throw e;
	}
		finally{
            if(rs != null)
                rs.close();
            if(stmt != null)
               EgovDatabaseManager.releaseConnection(stmt);     
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

