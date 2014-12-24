
<%@ page language="java" %>
<%@ page import="java.util.*,org.egov.infstr.utils.*,org.apache.log4j.Logger,org.egov.pims.*,org.egov.pims.dao.*,org.egov.pims.service.*,org.egov.pims.utils.*,org.hibernate.LockMode,org.egov.pims.model.*,org.egov.commons.*,org.egov.infstr.commons.client.*,org.egov.infstr.commons.dao.*,
		org.egov.infstr.utils.*,org.egov.lib.rjbac.dept.*,org.egov.lib.address.model.*,java.math.BigDecimal,org.egov.lib.address.dao.*,java.sql.*,org.egov.infstr.utils.HibernateUtil,org.egov.pims.client.*,org.egov.infstr.security.utils.SecurityUtils"
		%>
		  	


	<%
		  			Connection con = null;
		  			ResultSet rs = null;
		  			Statement stmt = null;
		  			PreparedStatement pst = null;
		  			StringBuffer result = new StringBuffer();
		  			String gradeValues = null;

		  			String gradeId = (String) request.getParameter("gradeId");
		  			String empId = (String) request.getParameter("empId");
		  			
		  			try {

		  				con = HibernateUtil.getCurrentSession().connection();
		  				stmt = con.createStatement();
		  			} catch (Exception e) {

		  				System.out.println(e.getMessage());
		  				e.printStackTrace();
		  				throw e;
		  			}

		  			if (gradeId != null && empId != null) {
		  				empId = SecurityUtils.checkSQLInjection(SecurityUtils
		  						.checkXSSAttack(request.getParameter("empId")));
		  				gradeId = SecurityUtils.checkSQLInjection(SecurityUtils
		  						.checkXSSAttack(request.getParameter("gradeId")));
		  				String query = "select grade.grade_value,grade.age as \"age\" from egeis_grade_mstr grade where "
		  						+ " grade.grade_id=?  ";
		  				System.out.print("query" + query);
		  				pst = con.prepareStatement(query);

		  				pst.setInt(1, Integer.parseInt(gradeId));
		  				//pst.setString(2, empId);

		  				rs = pst.executeQuery();
		  			}
		  			int i = 0; 
		  			if (rs != null) {
		  				rs.next();
		  				result.append(rs.getString("age"));
		  				result.append("+");
		  				}

		  			
		  			gradeValues=result.toString();
		  			System.out.println("String" + gradeValues);
		  			response.setContentType("text/xml");
		  			response.setHeader("Cache-Control", "no-cache");
		  			response.getWriter().write(gradeValues);
		  		%>


