<%@page import="org.egov.exceptions.EGOVRuntimeException"%>
<%@page import="org.hibernate.jdbc.Work"%>
<%@ page language="java" import="org.egov.infstr.security.utils.SecurityUtils,java.sql.*,org.egov.infstr.utils.HibernateUtil" %>

<%
	final StringBuilder rsltQ = new StringBuilder();
	final String username = SecurityUtils.checkSQLInjection(request.getParameter("username"));
	final String query = "SELECT r.role_name, ucm.counterid, l.LOCATIONID  FROM eg_roles r, eg_user u, eg_userrole ur, eg_usercounter_map ucm, eg_location l "+
	" WHERE u.id_user = ur.id_user and r.id_role = ur.id_role and ucm.userid = u.id_user and ucm.counterid = l.id and u.user_name = ? ";
		
		if(username != null && !username.trim().equalsIgnoreCase("")) 	{
			HibernateUtil.getCurrentSession().doWork(new Work() {
				public void execute(Connection con) {
					try {
						PreparedStatement stmt = con.prepareStatement(query);
						stmt.setString(1,username);	
						ResultSet rs =stmt.executeQuery();	
						if(rs.next()) {
							rsltQ.append(rs.getString(1));
							rsltQ.append("^");
							if (rs.getString(3) != null) { //parent location present so return that					
								rsltQ.append(rs.getString(3));
							} else {
								rsltQ.append(rs.getString(2));
							}
						}
						rsltQ.append("^");
					} catch (Exception e) {
						throw new EGOVRuntimeException("Error occurred while getting data for Terminal",e);
					}
				}
			});
			
		}
		
	
	response.setContentType("text/plain;charset=utf-8");
	response.setHeader("Cache-Control", "no-cache");
	response.getWriter().write(rsltQ.toString());
	%>