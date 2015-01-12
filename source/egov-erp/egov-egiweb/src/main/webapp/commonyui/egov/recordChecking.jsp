<%@page import="org.egov.exceptions.EGOVRuntimeException"%>
<%@page import="org.hibernate.jdbc.ReturningWork"%>
<%@ page language="java" import="org.egov.infstr.security.utils.SecurityUtils,java.sql.*,org.egov.infstr.utils.HibernateUtil" %>

	<%
	final String tablename = SecurityUtils.checkSQLInjection(request.getParameter("tableName"));
	final String whereclause = SecurityUtils.checkSQLInjection(request.getParameter("whereClause"));
	final String result = HibernateUtil.getCurrentSession().doReturningWork(new ReturningWork<String>(){
		public String execute(Connection con) {
			try {
				PreparedStatement stmt = con.prepareStatement("select id from "+tablename+" where "+whereclause);				
				ResultSet rs = stmt.executeQuery();
				StringBuffer result = new StringBuffer();
				if(rs.next()) {
					result.append("false");
				} else {
					result.append("true");
				}
				result.append("^");
				return result.toString();
			} catch (Exception e) {
				throw new EGOVRuntimeException("Error occurred while checking Uniquiness of Data");
			}
		}
	});

	response.setContentType("text/plain;charset=utf-8");
	response.setHeader("Cache-Control", "no-cache");
	response.getWriter().write(result);
	%>