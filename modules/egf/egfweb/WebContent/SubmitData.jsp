<%@page language = "java"
   import = "com.exilant.exility.common.DataCollection"
   import = "com.exilant.exility.pagemanager.*"
   import = "java.security.*"
%>
<%
	PageMap pm ;
	DataCollection dc;
	String serviceID = request.getParameter("serviceID");
	String js;
	if (serviceID == null){
		dc = new DataCollection();
		dc.addMessage("exilNoServiceID");
		pm = new PageMap();
	}else{
		pm = PageMaps.getPageMap(serviceID + "In");
		dc = pm.createDataCollection(request);
		dc.addValue("serviceID",serviceID);
		SecurityGuard guard =  new SecurityGuard();
		guard.clearedSecurity(dc, request);
		/* egf for security  
			Principal userPrincipal=request.getUserPrincipal();
			dc.addValue("rolesToValidate","1");
		  egf for security */	
		ServiceAgent agent = ServiceAgent.getAgent();
		agent.deliverService("UpdateService", dc);
		pm = PageMaps.getPageMap(serviceID + "Out");
	}
	js = pm.toJavaScript(dc);
%>
<HTML>
<head>
</head>
<body>
<script laguage="javascript">
<%= js %>
var win=window;
while (true){
	if(win.PageManager) break;
	if(win.opener) win = win.opener;
	else if(win.parent && win.parent != win) win = win.parent;
	else break;
}
if (!win.PageManager) alert ('View source to see what the server returned');
else win.PageManager.UpdateService.returnedFromServer(dc);
</script>
</body>
</html>
