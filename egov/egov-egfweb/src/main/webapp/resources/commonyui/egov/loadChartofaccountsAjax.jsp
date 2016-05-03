<%@ page language="java" import="org.egov.infstr.utils.HibernateUtil,java.sql.Connection,java.sql.ResultSet" %>
<%@ page import="java.sql.Statement" %>
<%@ page import="java.util.List" %>

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
	StringBuffer result = new StringBuffer();
	StringBuffer type=new StringBuffer();
	StringBuffer chartOfAccounts_ID=new StringBuffer();
	StringBuffer chartOfAccounts_name=new StringBuffer();
	StringBuffer chartOfAccounts_parentId = new StringBuffer();
	StringBuffer chartOfAccounts_glCode = new StringBuffer();
	

	String parentId = request.getParameter("parentId");
	String query;
	if(parentId==null)
	{
	 query=" SELECT '' AS \"type\", ID AS \"chartOfAccounts_ID\", name AS \"chartOfAccounts_name\", parentId AS \"chartOfAccounts_parentId\", glcode AS \"chartOfAccounts_glCode\" FROM  chartOfAccounts where parentId is null order by id asc";
	}
	else
	{
	 query=" SELECT '' AS \"type\", ID AS \"chartOfAccounts_ID\", name AS \"chartOfAccounts_name\", parentId AS \"chartOfAccounts_parentId\", glcode AS \"chartOfAccounts_glCode\" FROM  chartOfAccounts where parentId ="+parentId+" order by id asc";
	}

	int i = 0;
	try
	{
	if(query != "")
	{
		List list2=HibernateUtil.getCurrentSession().createSQLQuery(query).list();
    for(Object ob:list2)
    {
    System.out.println("66666666666--------------"+ob);
    }

		while(rs.next()){

		if(i > 0)
		{
		type.append("+");
		type.append(rs.getString(1));
		chartOfAccounts_ID.append("+");
		chartOfAccounts_ID.append(rs.getString(2));
		chartOfAccounts_name.append("+");
		chartOfAccounts_name.append(rs.getString(3));
		chartOfAccounts_parentId.append("+");
		chartOfAccounts_parentId.append(rs.getString(4));
		chartOfAccounts_glCode.append("+");
		chartOfAccounts_glCode.append(rs.getString(5));

		}
		else
		{
		type.append(rs.getString(1));
		chartOfAccounts_ID.append(rs.getString(2));
		chartOfAccounts_name.append(rs.getString(3));
		chartOfAccounts_parentId.append(rs.getString(4));
		chartOfAccounts_glCode.append(rs.getString(5));

		}
		i++;

		}

	result.append(type);
	result.append("^");
	result.append(chartOfAccounts_ID);
	result.append("^");
	result.append(chartOfAccounts_name);
	result.append("^");
	result.append(chartOfAccounts_parentId);
	result.append("^");
	result.append(chartOfAccounts_glCode);
	result.append("^");


	values=result.toString();
	}
	}


	catch(Exception e)
	{

	}
	finally
		{
			if(rs!=null)
				rs.close();
			
				
	}

	response.setContentType("text/xml;charset=utf-8");
	response.setHeader("Cache-Control", "no-cache");
	response.getWriter().write(values);
	%>
