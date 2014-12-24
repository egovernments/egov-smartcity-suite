<%@page language = "java"  
   import = "org.egov.infstr.utils.EgovMasterDataCaching"
%>
<%
	System.out.println("<<< inside MasterDataCacheRemove.jsp >>>");
	if(request.getParameter("type").equalsIgnoreCase("removeFunction"))
	{ 
		System.out.println("inside remove egi-function from Cache");
		EgovMasterDataCaching.getInstance().removeFromCache("egi-function");
	}
	else if(request.getParameter("type").equalsIgnoreCase("removeFund"))
	{ 
		System.out.println("inside remove egi-fund from Cache");
		EgovMasterDataCaching.getInstance().removeFromCache("egi-fund");
	}
	else if(request.getParameter("type").equalsIgnoreCase("removeFundSource"))
	{ 
		System.out.println("inside remove egi-fundSource from Cache");
		EgovMasterDataCaching.getInstance().removeFromCache("egi-fundSource");
	}
	else if(request.getParameter("type").equalsIgnoreCase("removeBank"))
	{ 
		System.out.println("inside remove egf-bank from Cache");
		EgovMasterDataCaching.getInstance().removeFromCache("egf-bank");
	}
	
%>
<html>
<body>
</body>
</html>