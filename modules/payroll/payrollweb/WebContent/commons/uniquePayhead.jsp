<%@ page language="java" import="java.sql.*,org.egov.infstr.utils.HibernateUtil,org.egov.infstr.utils.*" %>

	<%

	Connection con=null;
	ResultSet rs=null;
	Statement stmt=null;
	StringBuffer result = new StringBuffer();
	String name = request.getParameter("name");
	System.out.println("QJNIQUE-------"+name);
	try	{
		con = HibernateUtil.getCurrentSession().connection();
		stmt=con.createStatement();
	}catch(Exception e){
		System.out.println(e.getMessage());
		e.printStackTrace();
		throw e;
	}

	if(request.getParameter("action").equalsIgnoreCase("getPayheadByName")){
		System.out.println("PAYHEADNAME");
		String query="select head from EGPAY_SALARYCODES where HEAD ='"+name+"'";
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
	else if(request.getParameter("action").equalsIgnoreCase("getAllGlcodesByCode")){
		System.out.println("code-----"+request.getParameter("code"));
		String code = request.getParameter("code");
		String query="select glcode||'`-`'||name||'`-`'||ID as \"code\" from chartofaccounts where classification=4  and ISACTIVEFORPOSTING=1 and glcode='"+code+"' order by glcode ";
		rs=stmt.executeQuery(query);
		try	{
			if(rs != null){ 
				if(rs.next())
					result.append(rs.getString("code"));			
				else 
					result.append("false");								
				result.append("^");
				rs.close();
			}
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


