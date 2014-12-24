
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
			
			StringTokenizer stringTokenizer = new StringTokenizer(att);
			int i = 0;
			while(stringTokenizer.hasMoreElements())
			{
				String s = (String)stringTokenizer.nextElement();
				i++;
			}
			if(type.equals("P")||type.equals("A")||type.equals("HP")||type.equals("CE"))
			{
				if(i==2)
				{		
					Map mapCreate = (Map)session.getAttribute("AttendenceMapCompOff");
					if(type!=null&&!type.equals(""))
						mapCreate.put(att,type);
				}
				else{
					Map mapCreate = (Map)session.getAttribute("AttendenceMapCompOffModify");
					if(type!=null&&!type.equals(""))
						mapCreate.put(new Integer(att),type);
				}
				
			}
			
			
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
		}
		
		response.setContentType("text/xml");
		response.setHeader("Cache-Control", "no-cache");
		
	%>


