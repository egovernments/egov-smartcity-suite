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

	String fieldName = request.getParameter("fieldName");
	String fieldValue = request.getParameter("fieldValue");
	String query="";
	
	if(fieldName.equalsIgnoreCase("pfAccountName"))
	{
		query="SELECT coa.id as id,coa.name as name FROM chartOfAccounts coa,chartofaccountdetail cod 	WHERE coa.glCode='"+fieldValue+"' and coa.isactiveforposting = 1 and coa.type='L' and coa.id=cod.glcodeid ";	
	}
	else if(fieldName.equalsIgnoreCase("pfIntExpAccountName"))
	{
		query="SELECT id as id,name as name  FROM chartOfAccounts WHERE glCode='"+fieldValue+"' and isactiveforposting = 1 and type='E'";	
	}
	else if(fieldName.equalsIgnoreCase("empName"))
	{
		query="SELECT id as id, name as name  FROM eg_employee WHERE code='"+fieldValue+"' and isactive = 1 ";	
	}
	
	try
	{
		rs=stmt.executeQuery(query);
		if(rs.next())
		{
			result.append(rs.getString(1)+"-"+rs.getString(2));
		}
		else 
		{
			result.append("false");
		}
	}
	catch(Exception e)
	{
		System.out.println(e.getMessage());
	}
	response.setContentType("text/xml");
	response.setHeader("Cache-Control", "no-cache");
	response.getWriter().write(result.toString());
%>


