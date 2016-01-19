	<%@ page language="java" import="java.sql.*,org.egov.infstr.utils.HibernateUtil,org.egov.infstr.utils.EGovConfig,org.egov.infstr.utils.database.utils.EgovDatabaseManager" %>

	<%

	Connection con=null;
	ResultSet rs=null;
	Statement stmt=null;
	String values = "";
	StringBuffer result = new StringBuffer();
	StringBuffer data = new StringBuffer();

	try
	{
	con = HibernateUtil.getCurrentSession().connection();
	stmt=con.createStatement();
	}
	catch(Exception e)
	{
	throw new Exception("Not able to get a connection");
	}


	//Based on the xmlQueryName, we retrieve the query from appl_sqlconfig.xml

	String applXmlName = request.getParameter("applXmlName");
	String xmlTagName = request.getParameter("xmlTagName");
	String queryWhereClause = request.getParameter("queryWhereClause");
	String query = EGovConfig.getProperty(""+applXmlName+"","query","","sql."+xmlTagName+"");

	if(queryWhereClause != null)
	{
		query = query + " " + queryWhereClause;
	}


	try
	{
	if(!query.equals(""))
	{
		rs=stmt.executeQuery(query);
		int i = 0;

		while(rs.next())
		{

		if(i > 0)
		{
			data.append("+");
			data.append(rs.getString(1));
		}
		else
		{
			data.append(rs.getString(1));
		}
		i++;

		}

	result.append(data);
	result.append("^");
	values=result.toString();
	}
	}


	catch(Exception e)
	{
		throw new Exception("Exception while loading data for YUI");
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



