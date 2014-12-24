
<%@ page language="java" import="java.sql.*,org.egov.infstr.utils.HibernateUtil,org.egov.infstr.security.utils.SecurityUtils,org.apache.log4j.Logger" %>
<%
	Logger LOGGER = Logger.getLogger("uniqueEmpUserCheck.jsp");
	Connection con=null;
	ResultSet rs=null;
	PreparedStatement pst=null;
	StringBuffer result = new StringBuffer();
	try
	{
		con = HibernateUtil.getCurrentSession().connection();
		String tablename1=SecurityUtils.checkSQLInjection(SecurityUtils.checkXSSAttack(request.getParameter("tablename1")));
		String tablename2=SecurityUtils.checkSQLInjection(SecurityUtils.checkXSSAttack(request.getParameter("tablename2")));
		String columnname1 = SecurityUtils.checkSQLInjection(SecurityUtils.checkXSSAttack(request.getParameter("columnname1")));
		String columnname2 = SecurityUtils.checkSQLInjection(SecurityUtils.checkXSSAttack(request.getParameter("columnname2")));
		String fieldvalue = SecurityUtils.checkSQLInjection(SecurityUtils.checkXSSAttack(request.getParameter("fieldvalue")));
		String uppercase = SecurityUtils.checkSQLInjection(SecurityUtils.checkXSSAttack(request.getParameter("uppercase")));
		String lowercase = SecurityUtils.checkSQLInjection(SecurityUtils.checkXSSAttack(request.getParameter("lowercase")));
		String query;
	
		if(uppercase.equalsIgnoreCase("yes"))
		{
		fieldvalue=fieldvalue.toUpperCase();
		 query="SELECT * FROM "+tablename1+" WHERE "+columnname1+" IN(SELECT "+columnname1+" FROM "+tablename2+" WHERE UPPER("+columnname2+")= ?)";	
		}
		else if(lowercase.equalsIgnoreCase("yes"))
		{
		fieldvalue=fieldvalue.toLowerCase();
		 query="SELECT * FROM "+tablename1+" WHERE "+columnname1+" IN(SELECT "+columnname1+" FROM "+tablename2+" WHERE LOWER("+columnname2+")= ?)";	
		}	
		else 
		{
		  query="SELECT * FROM "+tablename1+" WHERE "+columnname1+" IN(SELECT "+columnname1+" FROM "+tablename2+" WHERE "+columnname2+"= ?)";	
		}
		LOGGER.debug("Unique userEmpMap checking  Query ---> "+query);
		pst=con.prepareStatement(query);
		pst.setString(1,fieldvalue);
	}
	catch(Exception e)
	{
		LOGGER.debug(e.getMessage());
		throw new Exception("Not able to get a connection");
	}
	try
	{
		rs=pst.executeQuery();
		result.append("^");
			if(rs.next())
			{
				result.append("true");
			}
			else 
			{
				result.append("false");
			}
		result.append("^");	
	
	}
	catch(Exception e)
	{
			LOGGER.debug(e.getMessage());
	}

	

	response.setContentType("text/xml");
	response.setHeader("Cache-Control", "no-cache");
	response.getWriter().write(result.toString());
	%>



