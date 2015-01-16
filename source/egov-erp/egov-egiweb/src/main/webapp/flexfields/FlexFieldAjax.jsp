<%@page import="org.egov.exceptions.EGOVRuntimeException"%>
<%@ page language="java" import="org.egov.infstr.security.utils.SecurityUtils,java.sql.*,org.egov.infstr.utils.HibernateUtil" %>

	<%
		String result = "";
		if(request.getParameter("type").equalsIgnoreCase("getAttributes")) { 
			final String domainId = SecurityUtils.checkSQLInjection(request.getParameter("domainid"));
			result = HibernateUtil.getCurrentSession().doReturningWork(new ReturningWork<String>() {
				public String execute(Connection  con) {
					try {
					String query="SELECT AT.ID,AT.ATT_NAME FROM EG_ATTRIBUTETYPE AT, EG_APPL_DOMAIN D WHERE D.ID = AT.APPL_DOMAINID AND AT.APPL_DOMAINID = ? AND "+
							" AT.ID NOT IN (SELECT DISTINCT ATT_TYPEID FROM EG_ATTRIBUTEVALUES) ORDER BY AT.ATT_NAME ";
					PreparedStatement stmt=con.prepareStatement(query);
					stmt.setInt(1,Integer.valueOf(domainId));
					ResultSet rs=stmt.executeQuery();
					StringBuffer id = new StringBuffer();
					StringBuffer name = new StringBuffer();
					StringBuffer result = new StringBuffer();
					int i = 0;	
					System.out.println("COMING OVER HERE");		
					while(rs.next()){
						if(i > 0) {
							id.append("+");
							id.append(rs.getString(1));
							name.append("+");
							name.append(rs.getString(2));
						} else {
							id.append(rs.getString(1));
							name.append(rs.getString(2));
							i++;
						}
					}
					
					result.append(id);
					result.append("^");
					result.append(name);
					result.append("^");
					result.append("!$");
					return result.toString();
					} catch (Exception e) {
						throw new EGOVRuntimeException("Error occurred while getting Flex Fields",e);
					}
				}
			});
			
		}
	
	response.setContentType("text/plain");
	response.setHeader("Cache-Control", "no-cache");
	response.getWriter().write(result);
	%>