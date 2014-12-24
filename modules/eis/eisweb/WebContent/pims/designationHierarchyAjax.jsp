<%@ page language="java" %>
<%@ page import="java.util.*,
		java.sql.*,
		org.egov.infstr.utils.HibernateUtil,org.hibernate.jdbc.Work"
		%>
		<%
		
final String reqType=request.getParameter("type");
final String departmentGroup=request.getParameter("departmentGroup")==null?"0":request.getParameter("departmentGroup").toString();
final String objType=request.getParameter("objType")==null?"":request.getParameter("objType").toString();
final java.io.PrintWriter printWriter=response.getWriter();
final javax.servlet.http.HttpServletResponse servletResponse=response;
try{
HibernateUtil.getCurrentSession().doWork(new Work() {
		
		
		public void execute(Connection con) throws SQLException {
			ResultSet rs=null;
			Statement stmt=null;
			PreparedStatement pst=null;
		
			String codeValues=null;	
			StringBuffer returnValues=new StringBuffer();

			
				stmt=con.createStatement();
			
			
			if(reqType.equalsIgnoreCase("getAllDesignation"))
			{
				String query="SELECT eg.DESIGNATION_NAME||'`-`'||eg.DESIGNATIONID as \"desigDtls\" FROM eg_designation eg ORDER BY eg.DESIGNATIONID";
				System.out.println("Query"+query);
				rs=stmt.executeQuery(query);
				
				int i = 0;
				
				if(rs!=null)
				{
				   	while(rs.next())
					{
						if(i > 0)
						{
							returnValues.append("+");
							returnValues.append(rs.getString("desigDtls"));
						}
						else 
						{
							returnValues.append(rs.getString("desigDtls"));
						}
						 
						i++;
			
				    }
					returnValues.append("^");
				}
				
				codeValues=returnValues.toString();
			}
			else if(reqType.equalsIgnoreCase("isDesigHirAlreadyCreatedForDeptAndObjType"))
			{
				String deptGrp="";
				String query ="select  distinct dh.department_id, dp.dept_name from egeis_desig_hierarchy dh, eg_department dp where dh.object_type_id="+objType+" and dh.department_id = dp.id_dept and dh.department_id in ("+departmentGroup+") order by dp.dept_name ";
				rs=stmt.executeQuery(query);
				 
				while(rs.next())
				{
					if(!deptGrp.equals(""))
						deptGrp = deptGrp+ ","+rs.getString(1)+"#"+rs.getString(2);
					else
						deptGrp = rs.getString(1)+"#"+rs.getString(2);
				}
				codeValues = deptGrp;
			}
			
			
			
	    	servletResponse.setContentType("text/xml");
	    	servletResponse.setHeader("Cache-Control", "no-cache");
	    	printWriter.write(codeValues);
	    	}
	    	});
	    }
	    catch(Exception e){

				System.out.println(e.getMessage());
				e.printStackTrace();
				throw e;
			}
	%>