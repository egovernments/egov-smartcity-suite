<%@ page language="java" import="org.egov.infstr.security.utils.SecurityUtils,java.sql.*,org.egov.infstr.utils.HibernateUtil,org.egov.infstr.utils.database.utils.EgovDatabaseManager" %>

	<%

	Connection con=null;
	ResultSet rs=null;
	Statement stmt=null;
	StringBuffer result = new StringBuffer();
	try	{
		con = HibernateUtil.getCurrentSession().connection();
		stmt=con.createStatement();
		String tablename = SecurityUtils.checkSQLInjection(request.getParameter("tableName"));
		String whereclause = SecurityUtils.checkSQLInjection(request.getParameter("whereClause"));
		rs=stmt.executeQuery("select id from "+tablename+" where "+whereclause);
	
		if(rs.next()) {
			result.append("false");
		}
		else {
			result.append("true");
		}
		result.append("^");
	} catch(Exception e) {
		throw new RuntimeException("Error occurred while checking the record");
	}
	finally	{
		if(rs!=null)
			rs.close();
		if(stmt != null)
			EgovDatabaseManager.releaseConnection(stmt);
	}

	response.setContentType("text/plain;charset=utf-8");
	response.setHeader("Cache-Control", "no-cache");
	response.getWriter().write(result.toString());
%>
