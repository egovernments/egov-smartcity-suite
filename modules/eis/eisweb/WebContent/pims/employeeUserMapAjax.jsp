
<%@ page language="java" %>
<%@ page import="java.util.*,
		org.apache.log4j.Logger,
		org.egov.infstr.security.utils.SecurityUtils,
		org.hibernate.LockMode,
		java.math.BigDecimal,
		org.egov.lib.address.dao.*,
		java.text.SimpleDateFormat,
		java.sql.*,
		org.egov.infstr.utils.HibernateUtil"
		%>
	<%

	try
	{
	   	
		//Logger LOGGER = Logger.getLogger("employeeUserMapAjax.jsp");
		StringBuffer result=new StringBuffer();
		String userNames=null;	
		List<String> resultList = null;
		StringBuffer accCode = new StringBuffer();

		
		if(request.getParameter("type").equalsIgnoreCase("getAllUserName"))
		{
			String query="SELECT eu.user_name||'-'||ee.id_user as \"UserName\" FROM eg_employee ee,eg_user eu where eu.id_user=ee.id_user "+
			" ORDER BY ee.id_user ";
			resultList = (List<String>) HibernateUtil
					.getCurrentSession().createSQLQuery(query).list();
		}
		int i = 0;
		if (resultList != null) {
			for (String rs : resultList) {
				if (i == 0) {
					
					result.append(rs);
				}
				else 
				{
					result.append("+");
					result.append(rs);
				}
				i++; 
			}		
			result.append("^");
		}	
		
		
		
	    
	    userNames=result.toString();
		response.setContentType("text/xml");
	    response.setHeader("Cache-Control", "no-cache");
	    response.getWriter().write(userNames);
	    
	    }catch(Exception e){
			//LOGGER.error(e.getMessage());			
			throw e;
	    }
	%>
