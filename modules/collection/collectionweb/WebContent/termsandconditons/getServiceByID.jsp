<%@ page import="java.sql.*" %>
<%@ page import="java.util.*" %>
<%@ page import="org.egov.infstr.utils.HibernateUtil,org.hibernate.jdbc.Work"" %>
<%

final StringBuffer strbufQuery = new StringBuffer();
final String serviceid=request.getParameter("serviceid");

final java.io.PrintWriter printWriter=response.getWriter();
final javax.servlet.http.HttpServletResponse servletResponse=response;

try
	{
		HibernateUtil.getCurrentSession().doWork(new Work() {
			public void execute(Connection con) throws SQLException {
				ResultSet rs =null;
				Connection conn = null;
				Statement stmt=null;
				String messagekey = null;
				String messageContent = null;
					ResourceBundle resource=ResourceBundle.getBundle("custom");
					stmt = conn.createStatement();
					strbufQuery.append("select code from EG_SERVICEDETAILS where id ='"+ serviceid+"'");
							rs = stmt.executeQuery(strbufQuery.toString());
								while (rs.next()){
									 String result = rs.getString("CODE");
									 messagekey = result.concat(".transactionmessage");
									 messageContent=resource.getString(messagekey);
								}
				 servletResponse.setContentType("text/xml");
				 servletResponse.setHeader("Cache-Control", "no-cache");
				 printWriter.write(messageContent);
				}
		});	}catch(Exception e){

			System.out.println(e.getMessage());
			e.printStackTrace();
			throw e;
		}
		
		  
		
%>
