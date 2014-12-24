<%@ page language="java" import="java.sql.*,org.egov.infstr.utils.HibernateUtil,java.text.SimpleDateFormat "%>

	<%
		Connection con=null;
		ResultSet rs=null;
		PreparedStatement stmt = null;
		StringBuffer result = new StringBuffer();
		java.util.Date dat=null;
		java.sql.Date fromDate=null;
		java.util.Date todat=null;
		java.sql.Date toDate=null;

		try
		{
			con = HibernateUtil.getCurrentSession().connection();
		}
		catch(Exception e)
		{
			throw new Exception("Not able to get a connection");
		}
		String fromDateStr = request.getParameter("fromDate");
		System.out.println("fromDateStr"+fromDateStr);
		String toDateStr = request.getParameter("toDate");
		String empId = request.getParameter("empId");
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		if(toDateStr!=null && fromDateStr!=null && !fromDateStr.equals("") && !toDateStr.equals(""))
		{
			
			 dat = sdf.parse(fromDateStr);
			 fromDate = new java.sql.Date(dat.getTime());
			todat = sdf.parse(toDateStr);
			 toDate = new java.sql.Date(todat.getTime());
			String query = "";
			if(session.getAttribute("viewMode").equals("create"))
				query="SELECT FROM_DATE,TO_DATE ,ELA.STATUS as STATUS ,ETLM.TYPE_OF_LEAVE_VALUE AS LEAVE_TYPE  FROM EGEIS_LEAVE_APPLICATION ELA,EGEIS_TYPE_OF_LEAVE_MSTR ETLM   where ETLM.TYPE_OF_LEAVE_ID =ELA.LEAVE_TYPE_ID  and  (((ELA.STATUS ='1' or ELA.STATUS = '2' ) and ((FROM_DATE <= ? AND TO_DATE > ? ) or (FROM_DATE < ? AND TO_DATE >= ?) or (FROM_DATE <= ? and TO_DATE >= ?) OR (TO_DATE=?) OR (FROM_DATE=?))) or (ELA.STATUS ='3' and FROM_DATE = ? and TO_DATE = ?)) and ELA.EMP_ID = ?";
			else
				query="SELECT FROM_DATE,TO_DATE ,ELA.STATUS as STATUS ,ETLM.TYPE_OF_LEAVE_VALUE AS LEAVE_TYPE  FROM EGEIS_LEAVE_APPLICATION ELA,EGEIS_TYPE_OF_LEAVE_MSTR ETLM   where ETLM.TYPE_OF_LEAVE_ID =ELA.LEAVE_TYPE_ID  and  ((( ELA.STATUS = '2' ) and ((FROM_DATE <= ? AND TO_DATE > ? ) or (FROM_DATE < ? AND TO_DATE >= ?) or (FROM_DATE <= ? and TO_DATE >= ?)OR (TO_DATE=?) OR (FROM_DATE=?) )) or (ELA.STATUS ='3' and FROM_DATE = ? and TO_DATE = ?)) and ELA.EMP_ID = ?";
			
			stmt=con.prepareStatement(query);
			stmt.setDate(1,fromDate);
			stmt.setDate(2,fromDate);
			stmt.setDate(3,toDate);
			stmt.setDate(4,toDate);
			stmt.setDate(5,fromDate);
			stmt.setDate(6,toDate);

			stmt.setDate(7,fromDate);
			stmt.setDate(8,toDate);

			stmt.setDate(9,fromDate);
			stmt.setDate(10,toDate);
			stmt.setInt(11,new Integer(empId).intValue());
				try
				{
				
					
					rs=stmt.executeQuery();
					if(rs.next())
					{
						String from = sdf.format(rs.getDate("FROM_DATE"));
					String to = sdf.format(rs.getDate("TO_DATE"));
						if(rs.getString("STATUS").equals("1"))
						{
							result.append("A leave of the Type " + rs.getString("LEAVE_TYPE")+ " has already been applied between " + from + " to " + to + " so please go to modify mode"  );
						}
						else if(rs.getString("STATUS").equals("2"))
						{
							result.append("A leave of the Type" + rs.getString("LEAVE_TYPE")+ " has already been approved between " + from + " to " + to );
						}
						else if(rs.getString("STATUS").equals("3"))
						{
							result.append("A leave of the Type" + rs.getString("LEAVE_TYPE")+ " has already been rejected on " + from + " to " + to );
						}

						System.out.println("true");
					}
					else 
					{
						result.append("false");
						
						System.out.println("false");
					}
					result.append("^");	
				}


				catch(Exception e)
				{
				System.out.println(e.getMessage());
				}
		}
		response.setContentType("text/xml");
		response.setHeader("Cache-Control", "no-cache");
		response.getWriter().write(result.toString());
	%>


