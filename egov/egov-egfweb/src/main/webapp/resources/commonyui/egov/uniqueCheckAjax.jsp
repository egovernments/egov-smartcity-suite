<%@ page language="java" import="org.egov.infstr.security.utils.SecurityUtils,org.egov.infstr.utils.HibernateUtil,org.egov.infstr.utils.database.utils.EgovDatabaseManager,java.sql.Connection" %>
<%@ page import="java.sql.PreparedStatement" %>
<%@ page import="java.sql.ResultSet" %>
<%--
  ~ eGov suite of products aim to improve the internal efficiency,transparency,
  ~    accountability and the service delivery of the government  organizations.
  ~
  ~     Copyright (C) <2015>  eGovernments Foundation
  ~
  ~     The updated version of eGov suite of products as by eGovernments Foundation
  ~     is available at http://www.egovernments.org
  ~
  ~     This program is free software: you can redistribute it and/or modify
  ~     it under the terms of the GNU General Public License as published by
  ~     the Free Software Foundation, either version 3 of the License, or
  ~     any later version.
  ~
  ~     This program is distributed in the hope that it will be useful,
  ~     but WITHOUT ANY WARRANTY; without even the implied warranty of
  ~     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  ~     GNU General Public License for more details.
  ~
  ~     You should have received a copy of the GNU General Public License
  ~     along with this program. If not, see http://www.gnu.org/licenses/ or
  ~     http://www.gnu.org/licenses/gpl.html .
  ~
  ~     In addition to the terms of the GPL license to be adhered to in using this
  ~     program, the following additional terms are to be complied with:
  ~
  ~         1) All versions of this program, verbatim or modified must carry this
  ~            Legal Notice.
  ~
  ~         2) Any misrepresentation of the origin of the material is prohibited. It
  ~            is required that all modified versions of this material be marked in
  ~            reasonable ways as different from the original version.
  ~
  ~         3) This license does not grant any rights to any user of the program
  ~            with regards to rights under trademark law for use of the trade names
  ~            or trademarks of eGovernments Foundation.
  ~
  ~   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
  --%>

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
