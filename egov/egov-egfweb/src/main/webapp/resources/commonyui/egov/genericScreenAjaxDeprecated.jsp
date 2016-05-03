<%@ page language="java" import="org.egov.infstr.utils.HibernateUtil,java.sql.Connection" %>
<%@ page import="java.sql.ResultSet" %>
<%@ page import="java.sql.Statement" %>

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
