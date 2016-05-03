<%@ page language="java"
	import="org.egov.infstr.security.utils.SecurityUtils,org.egov.infstr.utils.database.utils.EgovDatabaseManager,java.sql.Connection,java.sql.PreparedStatement"%>
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
	String tablename = SecurityUtils.checkSQLInjection(request.getParameter("tablename"));
	String columnnameMod1 = SecurityUtils.checkSQLInjection(request.getParameter("columnnamemod1"));
	String columnnameMod2 = SecurityUtils.checkSQLInjection(request.getParameter("columnnamemod2"));
	String columnnameMod3 = SecurityUtils.checkSQLInjection(request.getParameter("columnnamemod3"));
	String columnnameMod4 = SecurityUtils.checkSQLInjection(request.getParameter("columnnamemod4"));
	String columnnameMod5 = SecurityUtils.checkSQLInjection(request.getParameter("columnnamemod5"));
	String whereclause1 = SecurityUtils.checkSQLInjection(request.getParameter("whereclause1"));

	String columnnameAct1 = SecurityUtils.checkSQLInjection(request.getParameter("columnnameact1"));
	String columnnameAct2 = SecurityUtils.checkSQLInjection(request.getParameter("columnnameact2"));
	String columnnameAct3 = SecurityUtils.checkSQLInjection(request.getParameter("columnnameact3"));
	String columnnameAct4 = SecurityUtils.checkSQLInjection(request.getParameter("columnnameact4"));
	String columnnameAct5 = SecurityUtils.checkSQLInjection(request.getParameter("columnnameact5"));
	String whereclause2 = SecurityUtils.checkSQLInjection(request.getParameter("whereclause2"));

	StringBuilder query = new StringBuilder();
	if (whereclause2 == null || whereclause2.trim().length() == 0) {
		query.append("SELECT ").append(columnnameMod1).append(",")
				.append(columnnameMod2).append(",").append(
						columnnameMod3).append(",").append(
						columnnameMod4).append(",").append(
						columnnameMod5).append(" FROM ").append(
						tablename).append(" where ").append(
						whereclause1);
	} else {
		query.append("SELECT ").append(columnnameMod1).append(",")
				.append(columnnameMod2).append(",").append(
						columnnameMod3).append(",").append(
						columnnameMod4).append(",").append(
						columnnameMod5).append(" FROM ").append(
						tablename).append(" where ").append(
						whereclause1).append(" UNION ");
		query.append("SELECT ").append(columnnameAct1).append(",")
				.append(columnnameAct2).append(",").append(
						columnnameAct3).append(",").append(
						columnnameAct4).append(",").append(
						columnnameAct5).append(" FROM ").append(
						tablename).append(" where ").append(
						whereclause2);;
	}

	int i = 0;
	Connection con = null;
	ResultSet rs = null;
	PreparedStatement stmt = null;
	StringBuilder result = new StringBuilder();
	StringBuilder id = new StringBuilder();
	StringBuilder name = new StringBuilder();
	StringBuilder url = new StringBuilder();
	StringBuilder ordernumber = new StringBuilder();
	try {
		con = EgovDatabaseManager.openConnection();
		stmt = con.prepareStatement(query.toString());
		rs = stmt.executeQuery();

		while (rs.next()) {

			if (i > 0) {
				id.append("+");
				id.append(rs.getString(1));
				name.append("+");
				name.append(rs.getString(2));
				url.append("+");
				url.append(rs.getString(3));
				ordernumber.append("+");
				ordernumber.append(rs.getString(4));
			} else {
				id.append(rs.getString(1));
				name.append(rs.getString(2));
				url.append(rs.getString(3));
				ordernumber.append(rs.getString(4));
				i++;
			}
		}

		result.append(id);
		result.append("^");
		result.append(name);
		result.append("^");
		result.append(url);
		result.append("^");
		result.append(ordernumber);
		result.append("^");

	} catch(Exception e){
		throw new RuntimeException("Error occurred while loading menu");
	} finally {
		if(rs != null)
			rs.close();
		EgovDatabaseManager.releaseConnection(stmt);
	}
	response.setContentType("text/xml;charset=utf-8");
	response.setHeader("Cache-Control", "no-cache");
	response.getWriter().write(result.toString());
%>

