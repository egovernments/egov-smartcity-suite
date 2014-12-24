
<%@ page language="java"%>
<%@ page
	import="java.util.*,
		org.egov.infstr.utils.*,
		org.egov.pims.*,
		org.egov.pims.dao.*,
		org.egov.pims.service.*,
		org.egov.pims.utils.*,
		org.hibernate.*,
		java.sql.*,
		org.egov.infstr.utils.HibernateUtil,
		org.egov.pims.client.*,
		org.egov.infstr.security.utils.SecurityUtils,
		org.egov.exceptions.EGOVRuntimeException,org.hibernate.jdbc.Work"%>
<%
	try {

		StringBuffer accCode = new StringBuffer();
		String codeValues = null;
		List<String> resultList = null;
		String deptId = "";
		String desigId = "";
		int paramIndex = 0;

		if (request.getParameter("type").equalsIgnoreCase(
				"getAllPosition")
				&& null != request.getParameter("deptSelected")
				&& null != request.getParameter("designationSelected")) {
			deptId = SecurityUtils.checkSQLInjection(SecurityUtils
					.checkXSSAttack(request
							.getParameter("deptSelected")));
			desigId = SecurityUtils.checkSQLInjection(SecurityUtils
					.checkXSSAttack(request
							.getParameter("designationSelected")));
			System.out.print("deptid=" + deptId);
			System.out.print("desigId=" + desigId);
			if (!(("").equalsIgnoreCase(deptId) && ("")
					.equalsIgnoreCase(desigId))) {
				String query = "select position_name || '`-`' ||  id as  \"Id\" from eg_position where id_deptdesig in "
						+ "(select id from egeis_deptdesig where dept_id=? and  desig_id=? )order by position_name";
				Query qry = HibernateUtil.getCurrentSession()
						.createSQLQuery(query.toString());
				qry.setInteger(0, Integer.parseInt(deptId));
				qry.setInteger(1, Integer.parseInt((desigId)));
				System.out.println("Query $$$$$$$$$$$$ " + query);
				System.out.print("deptid= " + deptId);
				System.out.print("desigId= " + desigId);
				resultList = (List<String>) qry.list();

			}
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
				"Error in SearchForPositionAjax.jsp:::::::", e);
	}
%>