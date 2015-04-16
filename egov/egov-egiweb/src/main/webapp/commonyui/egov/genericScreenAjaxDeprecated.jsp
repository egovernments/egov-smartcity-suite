<%@page import="org.egov.infstr.security.utils.SecurityUtils"%>
<%@page import="org.egov.exceptions.EGOVRuntimeException"%>
<%@page import="org.hibernate.jdbc.ReturningWork"%>
<%@ page language="java" import="java.sql.*,org.egov.infstr.utils.HibernateUtil" %>

	<%
	final String query= SecurityUtils.checkSQLInjection(request.getParameter("query"));
	String values = HibernateUtil.getCurrentSession().doReturningWork(new ReturningWork<String>(){
		
		public String execute(Connection con) {
			try {
				PreparedStatement stmt = con.prepareStatement(query);
				ResultSet rs=stmt.executeQuery();

				StringBuffer result=new StringBuffer();
				StringBuffer id=new StringBuffer();
				StringBuffer name=new StringBuffer();
				StringBuffer narration=new StringBuffer();
				int i = 0;
				while(rs.next()){

				if(i > 0) {
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
				return result.toString();
			} catch (Exception e) {
				throw new EGOVRuntimeException("Error occurred in data retraival from generic Screen",e);
			}
		}
	});
	
	response.setContentType("text/xml;charset=utf-8");
	response.setHeader("Cache-Control", "no-cache");
	response.getWriter().write(values);
	%>