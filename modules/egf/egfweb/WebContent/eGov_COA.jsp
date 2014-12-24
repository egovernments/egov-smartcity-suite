<%@ page import=" org.egov.lib.rrbac.services.RbacServiceImpl,
		org.egov.lib.rrbac.services.RbacService,
		org.egov.infstr.utils.HibernateUtil,
		 org.egov.lib.rrbac.model.Action;"
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Frameset//EN" "http://www.w3.org/TR/html4/frameset.dtd">
<html>
<head>
<title>eGovernance</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
</head>

<%
System.out.println("actionid|||||||||||||||||||||||||||||||||||||||||||||||||="+request.getParameter("actionid"));
	String actionId=request.getParameter("actionid");
	if(actionId==null)
	{
		try
		{
			RbacService rbacService=new RbacServiceImpl();
			Action actobj= rbacService.getActionByURL("/eGov_COA.jsp?window=left");
			actionId = actobj.getId().toString();
			System.out.println("actobj========="+actobj.getId());
		}catch(Exception e){actionId="553";}
	}
	if(actionId!=null)
		session.setAttribute("actionid",actionId);
		
		
%>

<frameset id="mainFrameset" rows="100%,*" cols="300,*" framespacing="0" border="0" frameborder="NO">
		<frame src="commonyui/egov/ChartOfaccountsMenuTree.jsp?eGovAppName=ChartOfAccounts&actionid=<%=actionId%>" name="leftFrame" id="leftFrame" scrolling="AUTO" >
		<frame src="index.jsp" name="mainFrame">
	</frameset>


<noframes>

<body >
</body></noframes>
</html>
