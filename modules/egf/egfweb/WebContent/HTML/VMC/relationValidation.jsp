<%@ page language="java" import="java.sql.*,org.egov.infstr.utils.database.utils.*,org.egov.infstr.utils.HibernateUtil,org.hibernate.jdbc.Work"%>
<%

final String type=request.getParameter("type");
final String relationtype=request.getParameter("relationtype");
final String panno=request.getParameter("panno");
final String tinno=request.getParameter("tinno");
final String code=request.getParameter("code");

final java.io.PrintWriter printWriter=response.getWriter();
final javax.servlet.http.HttpServletResponse servletResponse=response;

HibernateUtil.getCurrentSession().doWork(new Work() {
			
	public void execute(Connection con) throws SQLException {

ResultSet rs=null;
Statement stmt=null;
StringBuffer accCode=new StringBuffer();
try     
{
	
	stmt=con.createStatement();
}catch(Exception e){
	System.out.println(e.getMessage());	  
}

String query="";
type = request.getParameter("type");
relationtype = request.getParameter("relationtype");
panno = request.getParameter("panno");
tinno = request.getParameter("tinno");
code = request.getParameter("code");
if(type.equalsIgnoreCase("validaUniquePanTinno"))
{
	if(relationtype.equals("2") && panno!=null && !panno.equals(""))
	{
		query="select code AS \"code\" from relation where panno='"+panno+"' and code!='"+code+"' ";
	}
	else if(relationtype.equals("1"))
	{
		if(tinno!=null && !tinno.equals(""))
			query="select code AS \"code\" from relation where tinno='"+tinno+"' and code!='"+code+"' ";
	}
	System.out.println(query);
	rs=stmt.executeQuery(query);
}

if(rs.next())
{
	if(relationtype.equals("1"))
		accCode.append("Please check for the TIN NO entered. This TIN NO is already registered in the system ");
	else
		accCode.append("Please check for the PAN NO entered. This PAN NO is already registered in the system ");
}
else
{
	if(relationtype.equals("1") && panno!=null && !panno.equals(""))
	{
		query="select code AS \"code\" from relation where panno='"+panno+"' and tinno='"+tinno+"' and code!='"+code+"' ";
		System.out.println(query);
		rs=stmt.executeQuery(query);
		if(rs.next())
		{
			accCode.append("Combination of Pan & Tin no is already used for code "+code);
		}
		else
			accCode.append("^");
	}
	else
		accCode.append("^");
}
String codeValues=accCode.toString();
servletResponse.setContentType("text/xml");
servletResponse.setHeader("Cache-Control", "no-cache");
printWriter.write(codeValues);
	}
		});
		
%>
