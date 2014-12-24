<%@ page language="java" import="java.sql.*,org.egov.infstr.utils.HibernateUtil,org.egov.infstr.utils.*" %>

	<%

	Connection con=null;
	ResultSet rs=null;
	Statement stmt=null;
	StringBuffer result = new StringBuffer();
	String name = request.getParameter("name");
	System.out.println("QJNIQUE------->>>>>"+request.getParameter("action")+""+name);
	try	{
		con = HibernateUtil.getCurrentSession().connection();
		stmt=con.createStatement();
	}catch(Exception e){
		System.out.println(e.getMessage());
		e.printStackTrace();
		throw e;
	}

	
	if(("getPayScaleByName").equalsIgnoreCase(request.getParameter("action")))
        {
		System.out.println("PAYscaleNAME");
		String query="select name from EGPAY_PAYSCALE_HEADER where NAME ='"+name+"'";
		rs=stmt.executeQuery(query);
		try	{
			if(rs.next()){
				result.append("false");
			}
			else {
				result.append("true");
			}
			result.append("^");
			rs.close();
		}catch(Exception e){
			System.out.println(e.getMessage());
			e.printStackTrace();
			throw e;
		}
			finally{
            if(rs != null)
                rs.close();
            if(stmt != null)
               EgovDatabaseManager.releaseConnection(stmt);     
        }
	}
	


	System.out.println("result--------"+result.toString());
	response.setContentType("text/xml");
	response.setHeader("Cache-Control", "no-cache");
	response.getWriter().write(result.toString());
	%>


