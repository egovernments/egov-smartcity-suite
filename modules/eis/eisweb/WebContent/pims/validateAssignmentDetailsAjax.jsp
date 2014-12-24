<%-- this jsp is to validate , if the position has objects attached while modifying the assignment --%>
<%@ page language="java" %>
<%@ page import="java.util.*, java.math.BigDecimal,	java.text.SimpleDateFormat,	java.sql.*,	org.egov.infstr.utils.HibernateUtil"%>
	<%
		Connection con=null;
		ResultSet rs=null;
		Statement stmt=null;
		PreparedStatement pst=null;		
		
		String resultStr="";		
		
		String posId=request.getParameter("positionId");
		String assignmentId=request.getParameter("assignmentId");
		String strFromDate=request.getParameter("fromDateDB");
		String strToDate=request.getParameter("toDateDB");

		String strFromDateEnter=request.getParameter("fromDateEnter");
		String strToDateEnter=request.getParameter("toDateEnter");
		try
		{
			con = HibernateUtil.getCurrentSession().connection();
			stmt=con.createStatement();
		}catch(Exception e){
			System.out.println(e.getMessage());
			e.printStackTrace();
			throw e;
		}
			
		
		
		
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

			java.util.Date JfromDate=null;
			java.util.Date JtoDate=null;

			java.sql.Date fromDate=null;
			java.sql.Date toDate=null;
			String query = "";
			if(posId!=null)
			{
				query="select distinct owner from eg_wf_states  where owner=?";
				pst=con.prepareStatement(query);

				pst.setInt(1,Integer.valueOf(posId).intValue());
				rs=pst.executeQuery();
				if( rs!=null && rs.next() )
				{
					//check any other assignment holding the position for the period(db),check that position is in workflow
					if(resultStr.isEmpty() && assignmentId!=null && !assignmentId.equals("") && posId!=null && !posId.equals("") && 
					strFromDate!=null && !strFromDate.equals("") &&  strToDate!=null && !strToDate.equals(""))
					{
						JfromDate = sdf.parse(strFromDate);
						fromDate = new java.sql.Date(JfromDate.getTime());
						JtoDate = sdf.parse(strToDate);
						toDate = new java.sql.Date(JtoDate.getTime());
						
							query=  "select distinct assmnt.id  from eg_emp_assignment assmnt , eg_emp_assignment_prd prd, eg_wf_states state "+
									"where  assmnt.id not in(?)  and assmnt.position_id=? and assmnt.id_emp_assign_prd= prd.id "+
									" and prd.from_date>=? and prd.to_date<=? "+
									" and state.owner=? "+
									" and to_date (to_char( state.created_date,'dd/MM/yyyy') ) between ? and ? ";
							pst=con.prepareStatement(query);

							pst.setInt(1,Integer.valueOf(assignmentId).intValue());
							pst.setInt(2,Integer.valueOf(posId).intValue());
							pst.setDate(3,fromDate);
							pst.setDate(4,toDate);				
							pst.setInt(5,Integer.valueOf(posId).intValue());
							pst.setDate(6,fromDate);
							pst.setDate(7,toDate);
							rs=pst.executeQuery();
							
							if( rs!=null && rs.next() )
							{
								resultStr="anotherAssignmentExists^";
							}
					}
				}
				else
				{
					resultStr="noWorkFlowItem^";
				}
			}

		
			if(resultStr.isEmpty() && assignmentId!=null && !assignmentId.equals("") && posId!=null && !posId.equals("") && 
					strFromDateEnter!=null && !strFromDateEnter.equals("") &&  strToDateEnter!=null && !strToDateEnter.equals(""))
			{
				JfromDate = sdf.parse(strFromDateEnter);
				fromDate = new java.sql.Date(JfromDate.getTime());
				JtoDate = sdf.parse(strToDateEnter);
				toDate = new java.sql.Date(JtoDate.getTime());
							
					query="select min(state.created_date)  from eg_emp_assignment assmnt , eg_wf_states state "+
							"where  assmnt.id  in(?)  and assmnt.position_id=?  and  state.owner=? "+
							"having min(state.created_date) >= ? and max(to_date (to_char( state.created_date,'dd/MM/yyyy') ) )<= ? ";
							
							pst=con.prepareStatement(query);

							pst.setInt(1,Integer.valueOf(assignmentId).intValue());
							pst.setInt(2,Integer.valueOf(posId).intValue());
							pst.setInt(3,Integer.valueOf(posId).intValue());
							pst.setDate(4,fromDate);
							pst.setDate(5,toDate);	
							rs=pst.executeQuery();
							
							if( rs!=null && rs.next() )
							{
								resultStr="modifyCurrentAssignment^";
							}
							else
							{
								resultStr="canNotModify^";
							}
			}
		System.out.println("resultStr "+resultStr);	
		response.setContentType("text/xml");
	    response.setHeader("Cache-Control", "no-cache");
	    response.getWriter().write(resultStr);
	    //if resultStr is modifyCurrentAssignment, then user can modify current assignment's all the values except position and designation
	    //if resultStr is anotherAssignmentExists/noWorkFlowItem, then user can  modify anything
	    //if resultStr is canNotModify, don't allow user to modify anything
	%>
