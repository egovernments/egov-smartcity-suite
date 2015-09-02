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


