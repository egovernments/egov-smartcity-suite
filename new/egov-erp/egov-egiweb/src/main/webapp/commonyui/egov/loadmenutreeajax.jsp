<%@page import="org.egov.exceptions.EGOVRuntimeException"%>
<%@page import="org.hibernate.jdbc.ReturningWork"%>
<%@ page language="java" import="org.egov.infstr.security.utils.SecurityUtils,java.sql.*,org.egov.infstr.utils.HibernateUtil" %>

	<%
	final String tablename = SecurityUtils.checkSQLInjection(request.getParameter("tablename"));
	final String columnnameMod1 = SecurityUtils.checkSQLInjection(request.getParameter("columnnamemod1"));
	final String columnnameMod2 = SecurityUtils.checkSQLInjection(request.getParameter("columnnamemod2"));
	final String columnnameMod3 = SecurityUtils.checkSQLInjection(request.getParameter("columnnamemod3"));
	final String columnnameMod4 = SecurityUtils.checkSQLInjection(request.getParameter("columnnamemod4"));
	final String columnnameMod5 = SecurityUtils.checkSQLInjection(request.getParameter("columnnamemod5"));
	final String whereclause1 = SecurityUtils.checkSQLInjection(request.getParameter("whereclause1"));

	final String columnnameAct1 = SecurityUtils.checkSQLInjection(request.getParameter("columnnameact1"));
	final String columnnameAct2 = SecurityUtils.checkSQLInjection(request.getParameter("columnnameact2"));
	final String columnnameAct3 = SecurityUtils.checkSQLInjection(request.getParameter("columnnameact3"));
	final String columnnameAct4 = SecurityUtils.checkSQLInjection(request.getParameter("columnnameact4"));
	final String columnnameAct5 = SecurityUtils.checkSQLInjection(request.getParameter("columnnameact5"));
	final String whereclause2 = SecurityUtils.checkSQLInjection(request.getParameter("whereclause2"));

	final StringBuilder query = new StringBuilder();
	if (whereclause2 == null || whereclause2.trim().length() == 0){
		query.append("SELECT ").append(columnnameMod1).append(",").append(columnnameMod2).append(",").append(columnnameMod3).append(",").append(columnnameMod4).append(",").append(columnnameMod5).append(" FROM ").append(tablename).append(" where ").append(whereclause1);
	} else {
		query.append("SELECT ").append(columnnameMod1).append(",").append(columnnameMod2).append(",").append(columnnameMod3).append(",").append(columnnameMod4).append(",").append(columnnameMod5).append(" FROM ").append(tablename).append(" where ").append(whereclause1).append(" UNION ");
		query.append("SELECT ").append(columnnameAct1).append(",").append(columnnameAct2).append(",").append(columnnameAct3).append(",").append(columnnameAct4).append(",").append(columnnameAct5).append(" FROM ").append(tablename).append(" where ").append(whereclause2);;
	} 	

	int i = 0;
	Connection con=null;
	ResultSet rs=null;
	PreparedStatement stmt=null;
	
	
	final String result = HibernateUtil.getCurrentSession().doReturningWork(new ReturningWork<String>(){
		public String execute(Connection con) {
			try {
				PreparedStatement stmt = con.prepareStatement(query.toString());				
				ResultSet rs = stmt.executeQuery();
				StringBuilder result = new StringBuilder();
				StringBuilder id=new StringBuilder();
				StringBuilder name=new StringBuilder();
				StringBuilder url=new StringBuilder();
				StringBuilder ordernumber = new StringBuilder();
				int i=0;
				while(rs.next()){
					if(i > 0) {
						id.append("+");
						id.append(rs.getString(1));
						name.append("+");
						name.append(rs.getString(2));
						url.append("+");
						url.append(rs.getString(3));
						ordernumber.append("+");
						ordernumber.append(rs.getString(4));
					} else {
						id.append(rs.getString(1));
						name.append(rs.getString(2));
						url.append(rs.getString(3));
						ordernumber.append(rs.getString(4));
						i++;
					}
				}
		
				result.append(id);
				result.append("^");
				result.append(name);
				result.append("^");
				result.append(url);
				result.append("^");
				result.append(ordernumber);
				result.append("^");
				return result.toString();
			} catch (Exception e) {
				throw new EGOVRuntimeException("Error occurred while loading Menutree");
			}
		}
	});
	
	response.setContentType("text/xml;charset=utf-8");
	response.setHeader("Cache-Control", "no-cache");
	response.getWriter().write(result);
	%>

