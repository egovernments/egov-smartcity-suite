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
<%@page import="org.egov.infra.exception.ApplicationRuntimeException"%>
<%@page import="org.egov.infstr.security.utils.SecurityUtils"%>
<%@ page language="java" import="org.egov.infstr.utils.HibernateUtil,org.hibernate.jdbc.ReturningWork,java.sql.Connection" %>
<%@ page import="java.sql.PreparedStatement" %>
<%@ page import="java.sql.ResultSet" %>

<%
	    final String tablename =  SecurityUtils.checkSQLInjection(request.getParameter("tablename"));
		final String columnname1 = SecurityUtils.checkSQLInjection(request.getParameter("columnname1"));
		final String columnname2 = SecurityUtils.checkSQLInjection(request.getParameter("columnname2"));
		final String columnname3 = SecurityUtils.checkSQLInjection(request.getParameter("columnname3"));
		final String whereclause = SecurityUtils.checkSQLInjection(request.getParameter("whereclause"));
		final String values =	HibernateUtil.getCurrentSession().doReturningWork(new ReturningWork<String>() {
				public String execute(Connection con) {
					try {
						String query="";
						boolean isThirdColExists=false;
						 if(!(columnname3!=null && !columnname3.trim().equals(""))) {
						  query="SELECT "+columnname1+","+columnname2+" FROM "+tablename+" where "+whereclause+" ";
						 } else {
						 	isThirdColExists=true;
						 	query="SELECT "+columnname1+","+columnname2+","+columnname3+" FROM "+tablename+" where "+whereclause+" ";
						 }
						StringBuffer result = new StringBuffer();
						StringBuffer id=new StringBuffer();
						StringBuffer name=new StringBuffer();
						PreparedStatement stmt = con.prepareStatement(query);
						ResultSet rs=stmt.executeQuery();
						int i =0;
						while(rs.next()){

							if(i > 0) {
								id.append("+");
								id.append(rs.getString(1));
								name.append("+");
								name.append(rs.getString(2));
								if(isThirdColExists)
									name.append((" - "+rs.getString(3)));
							} else {
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
						return result.toString();
						
					} catch (Exception e) {
						throw new ApplicationRuntimeException("Error occurred while loading combobox",e);
					}
					
				}
			});
		response.setContentType("text/xml;charset=utf-8");
		response.setHeader("Cache-Control", "no-cache");
		response.getWriter().write(values);
	%>
