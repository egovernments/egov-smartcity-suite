
<%@ page language="java" %>
<%@ page import="java.util.*,
		org.egov.infstr.utils.*,
		org.apache.log4j.Logger,
		org.egov.pims.*,
		org.egov.pims.dao.*,
		org.egov.pims.service.*,
		org.egov.pims.utils.*,
		org.egov.pims.model.*,
		org.egov.commons.*,
		org.egov.infstr.commons.dao.*,
		org.egov.infstr.utils.*,
		org.egov.lib.rjbac.dept.*,
		org.egov.lib.address.model.*,
		java.math.BigDecimal,
		org.egov.lib.address.dao.*,
		java.text.SimpleDateFormat,
		java.sql.*,
		org.egov.infstr.utils.HibernateUtil,
		org.egov.pims.client.*"
		%>
	<%

			Connection con=null;
			ResultSet rs=null;
			Statement stmt=null;
			PreparedStatement pst=null;
		
		StringBuffer accCode=new StringBuffer();
		String codeValues=null;	

		String effDate=null;
		SimpleDateFormat sdf = null;
		java.util.Date dat=null;

		String gradeIdSel="0";
		
		java.sql.Date effDte=null;
		try
		{

			con = HibernateUtil.getCurrentSession().connection();
			stmt=con.createStatement();
		}catch(Exception e){

			System.out.println(e.getMessage());
			e.printStackTrace();
			throw e;
		}
			
			 if(request.getParameter("type").equalsIgnoreCase("getAllGradeBasedDesignation"))
			{
				    String gradeId=request.getParameter("gradeId");
					System.out.println("check for grade Id--->>>"+gradeId);
					if(gradeId!=null && !gradeId.equals(""))
					{
						String query="SELECT eg.DESIGNATION_NAME||'`-`'||eg.DESIGNATIONID as \"Id\" FROM eg_designation eg WHERE eg.GRADE_ID="+gradeId+" ORDER BY eg.DESIGNATIONID";
						System.out.println("Query"+query);
						rs=stmt.executeQuery(query);

					}
					/*
					DesignationMasterDAO designationMasterDAO = new DesignationMasterDAO();
						Map desMap = designationMasterDAO.getAllDesignationMaster();
						for (Iterator it = desMap.entrySet().iterator(); it.hasNext(); )
						{
							Map.Entry entry = (Map.Entry) it.next();
							System.out.println("Desg Id"+entry.getKey()+"Designation name"+entry.getValue());
							
							accCode.append(entry.getValue());
							accCode.append("-");
							accCode.append(entry.getKey());
							
							

							accCode.append("+");
						}
				
				accCode.append("^");
				codeValues=accCode.toString();*/

				
			}
				
			else if(request.getParameter("type").equalsIgnoreCase("getAllDesgBasedPosition"))
			{
				
				String desgId=request.getParameter("desgId");
				System.out.println("Selected ID-->>"+desgId);
				if(desgId!=null && !desgId.equals(""))
				{
					String query="SELECT ep.POSITION_NAME||'`-`'||ep.ID as \"Id\" FROM EG_POSITION ep WHERE ep.DESIG_ID="+desgId+" ORDER BY ep.ID";
					System.out.println("Query"+query);
					rs=stmt.executeQuery(query);

				}
				

			
			}
			
		
	int i = 0;
	if(rs!=null)
	{
	   while(rs.next())
		{

				if(i > 0)
					{
					accCode.append("+");
					accCode.append(rs.getString("Id"));
					}
				else 
					{
					   accCode.append(rs.getString("Id"));
					}
			 i++;

	    }
		accCode.append("^");
	}
	    codeValues=accCode.toString();
		System.out.println("String"+codeValues);
		response.setContentType("text/xml");
	    response.setHeader("Cache-Control", "no-cache");
	    response.getWriter().write(codeValues);
	%>