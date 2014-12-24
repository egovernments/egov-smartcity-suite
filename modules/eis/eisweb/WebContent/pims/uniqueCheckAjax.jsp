<%@ page language="java" import="java.sql.*,org.egov.infstr.utils.HibernateUtil" %>



	<%

	Connection con=null;
	ResultSet rs=null;
	Statement stmt=null;
	StringBuffer result = new StringBuffer();
	try
	{
	con = HibernateUtil.getCurrentSession().connection();
	stmt=con.createStatement();
	}
	catch(Exception e)
	{
	throw new Exception("Not able to get a connection");
	}
	

	String tablename = request.getParameter("tablename");
	String columnname = request.getParameter("columnname");
	String fieldvalue = request.getParameter("fieldvalue");
	String uppercase = request.getParameter("uppercase");
	String lowercase = request.getParameter("lowercase");
	String query;
	
	if(uppercase.equalsIgnoreCase("yes"))
	{
	 query="SELECT "+columnname+" FROM "+tablename+" where "+columnname+" = '"+fieldvalue.toUpperCase()+"' ";	
	}
	else if(lowercase.equalsIgnoreCase("yes"))
	{
	 query="SELECT "+columnname+" FROM "+tablename+" where "+columnname+" = '"+fieldvalue.toLowerCase()+"' ";	
	}	
	else 
	{
	  query="SELECT "+columnname+" FROM "+tablename+" where "+columnname+" = '"+fieldvalue+"' ";
	}
	System.out.println("Unique Checking Query ---> "+query);
	
	try
	{
	rs=stmt.executeQuery(query);
	
		if(rs.next())
		{
			result.append("false");
		}
		else 
		{
			result.append("true");
		}
	result.append("^");	
	}
	
	
	catch(Exception e)
	{
	System.out.println(e.getMessage());
	}


	response.setContentType("text/xml");
	response.setHeader("Cache-Control", "no-cache");
	response.getWriter().write(result.toString());
	%>


