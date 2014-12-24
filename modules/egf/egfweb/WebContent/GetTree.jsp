<%@page language = "java"
   import = "com.exilant.exility.common.DataCollection"
   import = "com.exilant.exility.pagemanager.*"
 %>
<%


	DataCollection dc = new DataCollection();
	SecurityGuard guard = new SecurityGuard();
	guard.clearedSecurity(dc, request);

	String treeName   = request.getParameter("treeName");
	String divName   = request.getParameter("divName");

	if (treeName == null)  
	{
		dc.addMessage("exilServerError", "Tree service not invoked with a treeName ");		
	} 
	else
	{
		dc.addValue("treeName",treeName);
		dc.addValue("divName",divName);
		ServiceAgent agent = ServiceAgent.getAgent();
		agent.deliverService("TreeService", dc);
	}
	PageMap map = new PageMap();
	String js = map.toJavaScript(dc);

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
else win.PageManager.TreeService.displayTree(dc);
</script>
</body>
</html>
