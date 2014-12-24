<%@ page language="java"%>
<%@ page
	import="java.util.*,org.apache.log4j.Logger,java.math.BigDecimal,org.egov.lib.address.dao.*,
			java.text.SimpleDateFormat,java.sql.*,org.egov.infstr.security.utils.SecurityUtils,org.egov.infstr.utils.HibernateUtil"%>
<%
	Connection con = null;
	ResultSet rs = null;
	PreparedStatement pst = null;
	StringBuffer positions = new StringBuffer();
	String positionsvalues = null;
	String deptId = "";
	String desigId = "";
	String isAssignmentprimary = "";
	String strFromDate="";
	String strToDate="";
	String empId="";
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

	java.util.Date JfromDate=null;
	java.util.Date JtoDate=null;

	java.sql.Date fromDate=null;
	java.sql.Date toDate=null;
	//
	try {
		con = HibernateUtil.getCurrentSession().connection();
		if (!(null == request.getParameter("isPrimary") && null == request.getParameter("deptSelected")  &&  null == request.getParameter("designationSelected"))) 
		{
				isAssignmentprimary =SecurityUtils.checkSQLInjection(SecurityUtils.checkXSSAttack(request.getParameter("isPrimary")));
				deptId =SecurityUtils.checkSQLInjection(SecurityUtils.checkXSSAttack(request.getParameter("deptSelected"))); 				
				desigId=SecurityUtils.checkSQLInjection(SecurityUtils.checkXSSAttack(request.getParameter("designationSelected")));
				strFromDate=SecurityUtils.checkSQLInjection(SecurityUtils.checkXSSAttack(request.getParameter("fromDate")));
				strToDate=SecurityUtils.checkSQLInjection(SecurityUtils.checkXSSAttack(request.getParameter("toDate")));
				empId=SecurityUtils.checkSQLInjection(SecurityUtils.checkXSSAttack(request.getParameter("empId")));
				
				JfromDate = sdf.parse(strFromDate);
				fromDate = new java.sql.Date(JfromDate.getTime());
				JtoDate = sdf.parse(strToDate);
				toDate = new java.sql.Date(JtoDate.getTime());
				int paramIndex=0;
				
				String query = "";
				String notSameEmp=" ";
				System.out.println("empId "+empId);
				if(null!=empId && !"".equals(empId) && !"null".equals(empId) )//empid will be null while create
					notSameEmp=" and id_employee != ? ";
				
				if("Y".equalsIgnoreCase(isAssignmentprimary)){
				if (!(("").equalsIgnoreCase(deptId) && ("")
						.equalsIgnoreCase(desigId)  )) {
					query = "select egpos.position_name || '`-`' ||  egpos.id as  \"Id\" from eg_position egpos where egpos.id not in "+
							"(select position_id from eg_emp_assignment av where av.is_primary=? and av.ID_EMP_ASSIGN_PRD "+ 
							"in (select id from eg_emp_assignment_prd where ((from_date <= ? and to_date >=?) or (to_date <= ? and to_date >=?) or (from_date >= ? "+
							" and from_date <=?) or (from_date=?) or (to_date = ?)) "+notSameEmp+" ) and "+
							" position_id in (select id from eg_position where id_deptdesig in (select id from egeis_deptdesig where dept_id = ? and desig_id = ?) )  )  "
							+ " and id_deptdesig in (select id from egeis_deptdesig where dept_id = ? and desig_id = ?) order by egpos.position_name";
					pst = con.prepareStatement(query);
					
					pst.setString(++paramIndex,isAssignmentprimary);
					
					pst.setDate(++paramIndex,fromDate);
					pst.setDate(++paramIndex,toDate);

					pst.setDate(++paramIndex,toDate);
					pst.setDate(++paramIndex,fromDate);

					pst.setDate(++paramIndex,fromDate);
					pst.setDate(++paramIndex,toDate);

					pst.setDate(++paramIndex,fromDate);
					pst.setDate(++paramIndex,toDate);
					
					if(null!=empId && !"".equals(empId) && !"null".equals(empId) )
						pst.setInt(++paramIndex,Integer.valueOf(empId).intValue());
					
					pst.setInt(++paramIndex,Integer.valueOf(deptId).intValue());
					pst.setInt(++paramIndex,Integer.valueOf(desigId).intValue());
					pst.setInt(++paramIndex,Integer.valueOf(deptId).intValue());
					pst.setInt(++paramIndex,Integer.valueOf(desigId).intValue());
				}
				}
				else if (!("").equalsIgnoreCase(desigId))
				{
					query = "select egpos.position_name || '`-`' ||  egpos.id as  \"Id\" from eg_position egpos where egpos.id_deptdesig in (select id from egeis_deptdesig where desig_id = ?) ";
					pst = con.prepareStatement(query);
					pst.setInt(1, Integer.parseInt(desigId));
				}
				rs = pst.executeQuery();

				int i = 0;
				if (rs != null) {
					while (rs.next()) {
						if (i > 0) {
							positions.append("+");
							positions.append(rs.getString("Id"));
						} else {
							positions.append(rs.getString("Id"));
						}
						i++;
					}
					positions.append("^");
				}

				positionsvalues = positions.toString();
				response.setContentType("text/xml");
				response.setHeader("Cache-Control", "no-cache");
				response.getWriter().write(positionsvalues);
			
		}
	} catch (Exception e) {
		System.out.println(e.getMessage());
		e.printStackTrace();
		throw e;
	}
%>

