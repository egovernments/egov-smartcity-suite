<%@page import="org.egov.exceptions.EGOVRuntimeException"%>
<%@page import="org.hibernate.jdbc.ReturningWork"%>
<%@ page language="java" import="org.egov.infstr.security.utils.SecurityUtils,
				java.sql.*,org.egov.infstr.utils.HibernateUtil" %>

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
					throw new EGOVRuntimeException("Error occurred while loading combobox",e);
				}
				
			}
		});
	response.setContentType("text/xml;charset=utf-8");
	response.setHeader("Cache-Control", "no-cache");
	response.getWriter().write(values);
	%>