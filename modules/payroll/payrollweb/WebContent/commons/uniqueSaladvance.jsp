<%@ page language="java"
	import="java.sql.*,org.egov.infstr.utils.HibernateUtil,org.egov.infstr.utils.*,
org.hibernate.jdbc.*,javax.http.*"%>

<%
	final HttpServletRequest req = request;
	final HttpServletResponse res = response;
	HibernateUtil.getCurrentSession().doWork(new Work() {
		Connection con = null;
		ResultSet rs = null;
		Statement stmt = null;
		StringBuffer result = new StringBuffer();
		String number = req.getParameter("number");
		String empId = req.getParameter("empId");
		String code = req.getParameter("code");

		public void execute(Connection con) {
			try {
				stmt = con.createStatement();
				if (req.getParameter("action").equalsIgnoreCase(
						"getAdvanceBySanctionNo")) {
					System.out.println("SANCTIONNO");
					String query = "select SANCTION_NUM from EGPAY_SALADVANCES where SANCTION_NUM ='"
							+ number + "'";
					rs = stmt.executeQuery(query);
					try {
						if (rs.next()) {
							result.append("false");
						} else {
							result.append("true");
						}
						result.append("^");
					} catch (Exception e) {
						System.out.println(e.getMessage());
						e.printStackTrace();
						throw e;
					} finally {
						if (rs != null)
							rs.close();
						if (stmt != null)
							stmt.close();
					}

				}

				if (req.getParameter("action").equalsIgnoreCase(
						"getBankAccountForEmp")) {
					System.out.println("BANK_FOR_EMPLOYEE-------------"
							+ empId);
					String query = "select id from EGEIS_BANK_DET where id ='"
							+ empId + "'";
					rs = stmt.executeQuery(query);
					try {
						if (rs.next()) {
							result.append("false");
						} else {
							result.append("true");
						}
						result.append("^");
					} catch (Exception e) {
						System.out.println(e.getMessage());
						e.printStackTrace();
						throw e;
					} finally {
						if (rs != null)
							rs.close();
						if (stmt != null)
							stmt.close();
					}
				}

				if (req.getParameter("action").equalsIgnoreCase(
						"getEmployeeByCode")) {
					System.out.println("code-------------" + code);
					String query = "SELECT EV.CODE||'`-`'||EV.NAME||'`-`'||EV.ID||'`-`'||DG.DESIGNATION_NAME||'`-`'||D.DEPT_NAME||'`-`'||EV.DATE_OF_FA  || '`-`'|| ph.NAME "
							+ "AS \"code\" FROM EG_EIS_EMPLOYEEINFO EV ,EG_EMPLOYEE E,EG_DEPARTMENT D,EGPAY_PAYSCALE_EMPLOYEE pe,EGPAY_PAYSCALE_HEADER ph,EG_DESIGNATION DG "
							+ "WHERE EV.code ='"
							+ code
							+ "' and D.ID_DEPT =   EV.DEPT_ID and ph.id = pe.ID_PAYHEADER and pe.EFFECTIVEFROM in (select max(s.effectivefrom) from egpay_payscale_employee s where s.EFFECTIVEFROM<=SYSDATE and s.ID_EMPLOYEE=e.ID) "
							+ "AND pe.ID_EMPLOYEE = e.ID and DG.DESIGNATIONID = EV.DESIGNATIONID AND "
							+ "((EV.TO_DATE IS NULL and EV.FROM_DATE <= SYSDATE ) OR (EV.FROM_DATE <= SYSDATE AND EV.TO_DATE >= SYSDATE)) AND EV.isActive=1 AND E.ID = EV.ID(+) ORDER BY EV.CODE";

					rs = stmt.executeQuery(query);
					try {
						if (rs.next()) {
							result.append(rs.getString("code"));
						} else {
							result.append("false");
						}
						result.append("^");
					} catch (Exception e) {
						System.out.println(e.getMessage());
						e.printStackTrace();
					} finally {
						if (rs != null)
							rs.close();
						if (stmt != null)
							stmt.close();
					}
				}
				res.setContentType("text/xml");
				res.setHeader("Cache-Control", "no-cache");
				res.getWriter().write(result.toString());
			} catch (Exception e) {
				System.out.println(e.getMessage());
				e.printStackTrace();
			}

		}
	});
%>


