#-------------------------------------------------------------------------------
# eGov suite of products aim to improve the internal efficiency,transparency, 
#     accountability and the service delivery of the government  organizations.
#  
#      Copyright (C) <2015>  eGovernments Foundation
#  
#      The updated version of eGov suite of products as by eGovernments Foundation 
#      is available at http://www.egovernments.org
#  
#      This program is free software: you can redistribute it and/or modify
#      it under the terms of the GNU General Public License as published by
#      the Free Software Foundation, either version 3 of the License, or
#      any later version.
#  
#      This program is distributed in the hope that it will be useful,
#      but WITHOUT ANY WARRANTY; without even the implied warranty of
#      MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
#      GNU General Public License for more details.
#  
#      You should have received a copy of the GNU General Public License
#      along with this program. If not, see http://www.gnu.org/licenses/ or 
#      http://www.gnu.org/licenses/gpl.html .
#  
#      In addition to the terms of the GPL license to be adhered to in using this
#      program, the following additional terms are to be complied with:
#  
#  	1) All versions of this program, verbatim or modified must carry this 
#  	   Legal Notice.
#  
#  	2) Any misrepresentation of the origin of the material is prohibited. It 
#  	   is required that all modified versions of this material be marked in 
#  	   reasonable ways as different from the original version.
#  
#  	3) This license does not grant any rights to any user of the program 
#  	   with regards to rights under trademark law for use of the trade names 
#  	   or trademarks of eGovernments Foundation.
#  
#    In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
#-------------------------------------------------------------------------------
<%@ page language="java" import="java.sql.*,org.egov.infstr.utils.HibernateUtil,java.util.*" %>



	<%

	Connection con=null;
	ResultSet rs=null;
	PreparedStatement stmt=null;
	StringBuffer result = new StringBuffer();
	try
	{
		con = HibernateUtil.getCurrentSession().connection();
	}
	catch(Exception e)
	{
		throw new Exception("Not able to get a connection");
	}


	String username = request.getParameter("username");
	String rolesQuery = "SELECT r.role_name FROM "+
		       "eg_roles r, eg_user u, eg_userrole ur "+
		       "WHERE u.id_user = ur.id_user and r.id_role = ur.id_role and u.user_name = ? ";

	String locQuery = "SELECT ucm.counterid, l.LOCATIONID  FROM "+
		       "eg_user u, eg_usercounter_map ucm, eg_location l "+
		       "WHERE ucm.userid = u.id_user and ucm.counterid = l.id "+
		       "and u.user_name = ? ";


	try
	{
		if(username != null && !username.equalsIgnoreCase(""))
		{
			stmt = con.prepareStatement(rolesQuery);
			stmt.setString(1,username);
			rs=stmt.executeQuery();
			while (rs.next())
			{
				result.append(rs.getString(1));
				result.append("^");

			}
			result.append("|");
			rs.close();
			stmt = con.prepareStatement(locQuery);
			stmt.setString(1,username);
			rs=stmt.executeQuery();
			if (rs.next())
			{

				if (rs.getString(2) != null) { //parent location present so return that

					result.append(rs.getString(2));
					result.append("^");
					result.append(rs.getString(1));
				}
				else
				{
					result.append(rs.getString(1));
					result.append("^");
					result.append("0");
				}

			}
		}
		//result.append("^");
	}


	catch(Exception e)
	{
		System.out.println(e.getMessage());
	}


	response.setContentType("text/xml;charset=utf-8");
	response.setHeader("Cache-Control", "no-cache");
	response.getWriter().write(result.toString());
	%>


