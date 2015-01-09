	<%@page import="org.egov.exceptions.EGOVRuntimeException"%>
<%@page import="org.egov.exceptions.EGOVException"%>
<%@page import="org.hibernate.jdbc.ReturningWork"%>
<%@ page language="java" import="java.sql.*,org.egov.infstr.utils.HibernateUtil,org.egov.infstr.utils.EGovConfig" %>

	<%

	//Based on the xmlQueryName, we retrieve the query from appl_sqlconfig.xml
	String applXmlName = request.getParameter("applXmlName");
	String xmlTagName = request.getParameter("xmlTagName");
	String queryWhereClause = request.getParameter("queryWhereClause");
	String query = EGovConfig.getProperty(""+applXmlName+"","query","","sql."+xmlTagName+"");

	if(queryWhereClause != null) {
		query = query + " " + queryWhereClause;
	}


	String values = "";
	if(!query.equals(""))
	{
		final String sqlQuery = query;
		values = HibernateUtil.getCurrentSession().doReturningWork(new ReturningWork<String>(){
			public String execute(Connection con) {
				try {
					PreparedStatement stmt = con.prepareStatement(sqlQuery);
					ResultSet rs=stmt.executeQuery();
					int i = 0;
					StringBuffer result = new StringBuffer();
					StringBuffer data = new StringBuffer();
					while(rs.next()) {
					if(i > 0) {
						data.append("+");
						data.append(rs.getString(1));
					} else {
						data.append(rs.getString(1));
					}
					i++;
	
					}
	
					result.append(data);
					result.append("^");
					return result.toString();
				} catch (Exception e) {
					throw new EGOVRuntimeException("Error occurred while populating YUI Data");
				}
			}
		});
		
	}
	
	response.setContentType("text/xml;charset=utf-8");
	response.setHeader("Cache-Control", "no-cache");
	response.getWriter().write(values);
	%>