<%@ page language="java"%>
<%@ page
	import="java.util.*,
		org.apache.log4j.Logger,
		org.hibernate.*,
		org.egov.exceptions.EGOVRuntimeException,
		java.math.BigDecimal,
		org.egov.lib.address.dao.*,
		java.text.SimpleDateFormat,
		java.sql.*,
		org.egov.infstr.utils.HibernateUtil,org.hibernate.jdbc.Work"%>
<%
try {
	List<String> resultList = null;
	StringBuffer accCode = new StringBuffer();
	String codeValues = null;

	String effDate = null;
	String gradeIdSel = "0";

	java.sql.Date effDte = null;
	if (request.getParameter("type").equalsIgnoreCase(
			"getAllGradeBasedDesignation")) {
		String gradeId = request.getParameter("gradeId");
		System.out.println("check for grade Id--->>>" + gradeId);
		if (gradeId != null && !gradeId.equals("")) {
			String query = "SELECT eg.DESIGNATION_NAME||'`-`'||eg.DESIGNATIONID as \"Id\" FROM eg_designation eg WHERE eg.GRADE_ID="
					+ gradeId + " ORDER BY eg.DESIGNATIONID";
			resultList = (List<String>) HibernateUtil
					.getCurrentSession().createSQLQuery(query).list();
		}
	} else if (request.getParameter("type").equalsIgnoreCase(
			"getAllDesignation")) {
		String query = "SELECT eg.DESIGNATION_NAME||'`-`'||eg.DESIGNATIONID as \"Id\" FROM eg_designation eg ORDER BY eg.DESIGNATIONID";
		resultList = (List<String>) HibernateUtil.getCurrentSession()
				.createSQLQuery(query).list();
	} else if (request.getParameter("type").equalsIgnoreCase(
			"getAllDesgBasedPosition")) {
		String desgId = request.getParameter("desgId");
		//from date and to date for vacant position
		String strFromDate = request.getParameter("fromDate");
		String strToDate = request.getParameter("toDate");
		String isPrimary = request.getParameter("isPrimary");
		String empId = request.getParameter("empId");
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

		java.util.Date JfromDate = null;
		java.util.Date JtoDate = null;

		java.sql.Date fromDate = null;
		java.sql.Date toDate = null;

		if (desgId != null && !desgId.equals("") && strFromDate != null
				&& !strFromDate.equals("") && strToDate != null
				&& !strToDate.equals("")) {
			JfromDate = sdf.parse(strFromDate);
			fromDate = new java.sql.Date(JfromDate.getTime());
			JtoDate = sdf.parse(strToDate);
			toDate = new java.sql.Date(JtoDate.getTime());
			String query = "";
			if (!"null".equals(empId) && empId != null) {
				query = "SELECT ep.POSITION_NAME||'`-`'||ep.ID as \"Id\" FROM EG_POSITION ep WHERE ep.DESIG_ID=? and ID not in (select position_id from eg_emp_assignment av where av.is_primary=? and av.ID_EMP_ASSIGN_PRD in (select id from eg_emp_assignment_prd where ((from_date <= ? and to_date >=?) or (to_date <= ? and to_date >=?) or (from_date >= ? and from_date <=?) or (from_date=?) or (to_date = ?)) and id_employee != ?) and position_id in (select id from eg_position where desig_id=?)) ORDER BY ep.ID";
			} else {
				query = "SELECT ep.POSITION_NAME||'`-`'||ep.ID as \"Id\" FROM EG_POSITION ep WHERE ep.DESIG_ID=? and ID not in (select position_id from eg_emp_assignment av where av.is_primary=? and av.ID_EMP_ASSIGN_PRD in (select id from eg_emp_assignment_prd where (from_date <= ? and to_date >=?) or (to_date <= ? and to_date >=?) or (from_date >= ? and from_date <=?) or (from_date=?) or (to_date = ?)) and position_id in (select id from eg_position where desig_id=?)) ORDER BY ep.ID";
			}

			Query qry = HibernateUtil.getCurrentSession().createSQLQuery(query.toString());

			qry.setInteger(0, Integer.valueOf(desgId).intValue());
			qry.setString(1, isPrimary);

			qry.setDate(2, fromDate);
			qry.setDate(3, toDate);

			qry.setDate(4, toDate);
			qry.setDate(5, fromDate);

			qry.setDate(6, fromDate);
			qry.setDate(7, toDate);

			qry.setDate(8, fromDate);
			qry.setDate(9, toDate);
			if (!"null".equals(empId) && empId != null) {
				qry.setInteger(10, Integer.valueOf(empId).intValue());
				qry.setInteger(11, Integer.valueOf(desgId).intValue());
			} else {
				qry.setInteger(10, Integer.valueOf(desgId).intValue());
			}
			resultList = (List<String>) qry.list();

		}
	}

	
		int i = 0;
		if (resultList != null) {
		  for (String rs : resultList ) {
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
				"Error in employeeGradeAjax.jsp:::::::", e);
	}
%>