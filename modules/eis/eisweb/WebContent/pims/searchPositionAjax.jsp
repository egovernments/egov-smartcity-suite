
<%@ page language="java" %>
<%@ page import="java.util.*,
		org.egov.infstr.utils.*,
		org.apache.log4j.Logger,
		org.egov.pims.*,
		org.egov.pims.dao.*,
		org.egov.pims.service.*,
		org.egov.pims.utils.*,
		org.hibernate.LockMode,
		org.egov.pims.model.*,
		org.egov.commons.*,
		org.egov.infstr.commons.client.*,
		org.egov.infstr.commons.dao.*,
		org.egov.infstr.utils.*,
		org.egov.lib.rjbac.dept.*,
		org.egov.lib.address.model.*,
		java.math.BigDecimal,
		org.egov.lib.address.dao.*,
		java.text.SimpleDateFormat,
		java.sql.*,
		org.egov.infstr.utils.HibernateUtil,
		org.egov.pims.client.*,
		org.egov.exceptions.EGOVRuntimeException"
		%>
	<%
		try
			{
		
		List<String> resultList = null;
		StringBuffer accCode=new StringBuffer();
		String codeValues=null;	
			
		if (request.getParameter("type").equalsIgnoreCase(
				"getAllPosition")) {

			String query = "SELECT eg.POSITION_NAME||'`-`'||eg.ID as \"Id\" FROM eg_position eg  ORDER BY eg.ID";
			System.out.println("Query" + query);
			resultList = (List<String>) HibernateUtil
					.getCurrentSession().createSQLQuery(query).list();

		}

			int i = 0;
			if (resultList != null) {
				for (String rs : resultList) {
					if (i == 0) {
						accCode.append(rs);
					} else {
						accCode.append("+");
						accCode.append(rs);
					}
					i++;
				}
				accCode.append("^");
			}
			codeValues = accCode.toString();
			response.setContentType("text/xml");
			response.setHeader("Cache-Control", "no-cache");
			response.getWriter().write(codeValues);
		} catch (Exception e) {
			throw new EGOVRuntimeException(
					"Error in SearchPositionAjax.jsp:::::::", e);
		}
	%>