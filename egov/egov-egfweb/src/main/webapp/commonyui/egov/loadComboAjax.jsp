<%@ page language="java" import="org.egov.infstr.security.utils.SecurityUtils,java.sql.*,org.egov.infstr.utils.HibernateUtil,org.egov.infstr.utils.database.utils.EgovDatabaseManager" %>

	<%

	Connection con=null;
	ResultSet rs=null;
	Statement stmt=null;
	String values = "";
	StringBuffer result = new StringBuffer();
	StringBuffer id=new StringBuffer();
	StringBuffer name=new StringBuffer();
	String query="";
	boolean isThirdColExists=false;

	try
	{
	con = HibernateUtil.getCurrentSession().connection();
	stmt=con.createStatement();
	}
	catch(Exception e)
	{
	throw new Exception("Not able to get a connection");
	}


	String tablename =  SecurityUtils.checkSQLInjection(request.getParameter("tablename"));
	String columnname1 = SecurityUtils.checkSQLInjection(request.getParameter("columnname1"));
	String columnname2 = SecurityUtils.checkSQLInjection(request.getParameter("columnname2"));
	String columnname3 = SecurityUtils.checkSQLInjection(request.getParameter("columnname3"));
	String whereclause = SecurityUtils.checkSQLInjection(request.getParameter("whereclause"));


	 if(!(columnname3!=null && !columnname3.trim().equals("")))
	 {
	  query="SELECT "+columnname1+","+columnname2+" FROM "+tablename+" where "+whereclause+" ";
	 }
	 else
	 {
	 	isThirdColExists=true;
	 	query="SELECT "+columnname1+","+columnname2+","+columnname3+" FROM "+tablename+" where "+whereclause+" ";
	 }


	int i = 0;
	try
	{
	if(query != "")
	{
	rs=stmt.executeQuery(query);

		while(rs.next()){

		if(i > 0)
		{
		id.append("+");
		id.append(rs.getString(1));
		name.append("+");
		name.append(rs.getString(2));
		if(isThirdColExists)
			name.append((" - "+rs.getString(3)));

		}
		else
		{
		id.append(rs.getString(1));
		name.append(rs.getString(2));
		if(isThirdColExists)
			name.append((" - "+rs.getString(3)));
		}
		i++;

		}

	result.append(id);
	result.append("^");
	result.append(name);
	result.append("^");
	values=result.toString();
	}
	}


	catch(Exception e)
	{
		throw new Exception("Not able to get a connection");
	}
	finally
	{
		if(rs!=null)
			rs.close();
		if(stmt != null)
			EgovDatabaseManager.releaseConnection(stmt);
	}

	response.setContentType("text/xml;charset=utf-8");
	response.setHeader("Cache-Control", "no-cache");
	response.getWriter().write(values);
	%>
