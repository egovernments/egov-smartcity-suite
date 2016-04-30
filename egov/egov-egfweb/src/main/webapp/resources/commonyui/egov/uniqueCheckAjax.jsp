<%@ page language="java" import="org.egov.infstr.security.utils.SecurityUtils,org.egov.infstr.utils.HibernateUtil,org.egov.infstr.utils.database.utils.EgovDatabaseManager,java.sql.Connection" %>
<%@ page import="java.sql.PreparedStatement" %>
<%@ page import="java.sql.ResultSet" %>
<%

	Connection con=null;
	ResultSet rs=null;
	PreparedStatement stmt=null;
	StringBuilder qStr = new StringBuilder();
	StringBuilder resultStr = new StringBuilder();
	try	{
		con = HibernateUtil.getCurrentSession().connection();
		String type= request.getParameter("type");
		String uppercase = request.getParameter("uppercase");
		String lowercase = request.getParameter("lowercase");
		String tablename = SecurityUtils.checkSQLInjection(request.getParameter("tablename"));
		String columnname = SecurityUtils.checkSQLInjection(request.getParameter("columnname"));
		String fieldvalue = SecurityUtils.checkSQLInjection(SecurityUtils.checkXSSAttack(request.getParameter("fieldvalue")));
		String fieldvalue2 = SecurityUtils.checkSQLInjection(SecurityUtils.checkXSSAttack(request.getParameter("fieldvalue2")));
		String columnname2 = SecurityUtils.checkSQLInjection(request.getParameter("columnname2"));

		if(type==null|| type.equals("")) {
			qStr.append("SELECT ").append(columnname).append(" FROM ").append(tablename).append(" where ").append(columnname).append(" = ? ");
		} else if("compUniqueness".equals(type)) {
		   	qStr.append("SELECT ").append(columnname).append(" FROM ").append(tablename).append(" where ").append(columnname).append(" = ? and ").append(columnname2).append(" = ?");
		}
		
		stmt=con.prepareStatement(qStr.toString());
		
		if(type == null || type.equals("")) {
			if("yes".equalsIgnoreCase(uppercase)) {
		 		stmt.setString(1,fieldvalue.toUpperCase());
			} else if ("yes".equalsIgnoreCase(lowercase)) {
		 		stmt.setString(1,fieldvalue.toLowerCase());
			} else {
		 		stmt.setString(1,fieldvalue);
		 	}
		} else if("compUniqueness".equals(type)) {
		  	if ("yes".equalsIgnoreCase(uppercase)) {
		    	stmt.setString(1,fieldvalue.toUpperCase());
		    	stmt.setString(2,fieldvalue2.toUpperCase());
		    } else if ("yes".equalsIgnoreCase(lowercase)) {
		    	stmt.setString(1,fieldvalue.toLowerCase());
		    	stmt.setString(2,fieldvalue2.toLowerCase());
		    } else {
		     	stmt.setString(1,fieldvalue);
		    	stmt.setString(2,fieldvalue2);
		    }
		}
		rs=stmt.executeQuery();

		if(rs.next()) {
			resultStr.append("false");
		} else {
			resultStr.append("true");
		}
		resultStr.append("^");
	} catch(Exception e) {
		throw new Exception("Error occurred while checking for Uniqueness");
	} finally {
		if(rs!=null) {
			rs.close();
		}
		if(stmt != null) {
			EgovDatabaseManager.releaseConnection(stmt);
		}
	}
	response.setContentType("text/plain;charset=utf-8");
	response.setHeader("Cache-Control", "no-cache");
	response.getWriter().write(resultStr.toString());
	%>
