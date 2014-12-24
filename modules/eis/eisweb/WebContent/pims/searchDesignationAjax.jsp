<%@ page language="java"%>
<%@ page
	import="java.util.*,
		org.egov.infstr.utils.*,
		org.apache.log4j.Logger,
		org.egov.pims.*,
		java.sql.*,
		org.egov.infstr.utils.HibernateUtil,
		org.egov.pims.client.*,
		org.egov.exceptions.EGOVRuntimeException"%>
<%

	try
	{
		StringBuffer accCode=new StringBuffer();
		String codeValues=null;	
		List<String> resultList = null;

		 if(request.getParameter("type").equalsIgnoreCase("getDesignation"))
		{
			
                    String query="SELECT eg.DESIGNATION_NAME||'`-`'||eg.DESIGNATIONID as \"Id\" FROM eg_DESIGNATION eg  ORDER BY eg.DESIGNATIONID";
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
					"Error in searchDesignationAjax.jsp:::::::", e);
		}
				   
	%>

