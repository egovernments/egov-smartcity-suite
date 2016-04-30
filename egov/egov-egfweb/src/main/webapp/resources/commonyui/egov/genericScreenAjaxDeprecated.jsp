<%@ page language="java" import="org.egov.infstr.utils.HibernateUtil,java.sql.Connection" %>
<%@ page import="java.sql.ResultSet" %>
<%@ page import="java.sql.Statement" %>

<%
	Connection con=null;
	ResultSet rs=null;
	Statement stmt=null;
	String values = "";
	try
	{
	con = HibernateUtil.getCurrentSession().connection();
	stmt=con.createStatement();
	}catch(Exception e){
		throw new Exception("Not able to get a connection");
	}

	/* This jsp returns the query output  */
	

	String query=request.getParameter("query");
	rs=stmt.executeQuery(query);

	StringBuffer result=new StringBuffer();
	StringBuffer id=new StringBuffer();
	StringBuffer name=new StringBuffer();
	StringBuffer narration=new StringBuffer();
	int i = 0;
	try{
	while(rs.next()){

	if(i > 0){
		id.append("+");
		id.append(rs.getString(1));
		name.append("+");
		name.append(rs.getString(2));
		narration.append("+");
		narration.append(rs.getString(3));
	}
	else 
	{
		id.append(rs.getString(1));
		name.append(rs.getString(2));
		narration.append(rs.getString(3));
	}
	i++;

	}
	result.append(id);
	result.append("^");
	result.append(name);
	result.append("^");
	result.append(narration);
	result.append("^");
	values=result.toString();

	}
	catch(Exception e){
		throw new Exception("Exception occurred in GenericScreenAjaxDeprecated.jsp");
	}	
	response.setContentType("text/xml;charset=utf-8");
	response.setHeader("Cache-Control", "no-cache");
	response.getWriter().write(values);
	%>
