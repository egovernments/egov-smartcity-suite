<%@ page language="java" import="java.util.*,org.egov.pims.utils.*" %>



	<%
	
	StringBuffer result = new StringBuffer();
	String month = request.getParameter("month");
	System.out.println("resultresult"+month);
	String fYear = request.getParameter("fYear");
	System.out.println("fYear"+fYear);
	
	try
	{
		Map mp = EisManagersUtill.getFYMap();
		System.out.println("mp"+mp);
		Map mpFY = (Map)mp.get(new Long(fYear.trim()));
		System.out.println("mpFY"+mpFY);
		int yer = new Integer((String)mpFY.get(new Integer(month.trim()))).intValue();
		System.out.println("yer"+yer);
		
		if(yer<Calendar.getInstance().get(Calendar.YEAR))
		{
			result.append("true");
			System.out.println("yer");
		}
		else if(yer>Calendar.getInstance().get(Calendar.YEAR))
		{
			result.append("false");
			System.out.println("yer1");
		}
		else if(yer==Calendar.getInstance().get(Calendar.YEAR) && Integer.parseInt(month.trim())>(Calendar.getInstance().get(Calendar.MONTH)+1))
		{
			result.append("false");
			System.out.println("yer3");
		}
		else if(yer==Calendar.getInstance().get(Calendar.YEAR) && Integer.parseInt(month.trim())<=(Calendar.getInstance().get(Calendar.MONTH)+1))
		{
			result.append("true");
			System.out.println("yer4");
		}
	result.append("^");	
	}
	catch(Exception e)
	{
	System.out.println(e.getMessage());
	}
	response.setContentType("text/xml");
	response.setHeader("Cache-Control", "no-cache");
	response.getWriter().write(result.toString());
	%>


