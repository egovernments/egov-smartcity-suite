
<%@ page language="java" import="java.util.*,
		 org.egov.infstr.utils.*,
		 		 org.egov.pims.empLeave.service.*,
		 		 java.sql.Date ,
		 		  java.util.StringTokenizer,
		 		 java.text.SimpleDateFormat"



%>
	<%
		String att = request.getParameter("att");
		String type = request.getParameter("type");
		
		try
		{
			EmpLeaveServiceImpl empLeaveServiceImpl=new EmpLeaveServiceImpl();
			StringTokenizer stringTokenizer = new StringTokenizer(att);
			Map mapCreate;
			int i = 0;
			while(stringTokenizer.hasMoreElements())
			{
				String s = (String)stringTokenizer.nextElement();
				i++;
			}
			if(type.equals("P")||type.equals("A")||type.equals("HP") || type.equals("OT") || type.equals("H") || type.equals("CE"))
			{
				if(i==2)
				{
					   mapCreate = (Map)session.getAttribute("AttendenceMapCreate");
					   if(type!=null&&!type.equals(""))
							mapCreate.put(att,type);
				}
				else
				{					
						Map map = (Map)session.getAttribute("AttendenceMapModify");
						if(type!=null&&!type.equals(""))
							map.put(new Integer(att),type);
				}
			}
			else if(type.equals(""))
			{
					if (i==2)
					{ //not yet entered in DB. remove from mapCreate
					  mapCreate = (Map)session.getAttribute("AttendenceMapCreate");
					  mapCreate.remove(att);
					}
					else 
					{
						Set set = (Set)session.getAttribute("AttendencesetDelete");
						if(empLeaveServiceImpl.getAttendenceById(new Integer(att))!=null)
						{
							set.add(new Integer(att));
						}
					}
			}

		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.out.println(e.getMessage());
		}

		response.setContentType("text/xml");
		response.setHeader("Cache-Control", "no-cache");

	%>


