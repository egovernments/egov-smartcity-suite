<%@page language = "java"
   import = "com.exilant.exility.common.DataCollection"
   import = "com.exilant.exility.pagemanager.*"
 %>
<%
	PageMap pm = new PageMap();
	DataCollection dc;
	String serviceID = request.getParameter("serviceID");
	String js;
	if (serviceID == null){
		dc = new DataCollection();
		dc.addMessage("exilNoServiceID");
	}else{
		dc = pm.createDataCollection(request);
		dc.addValue("serviceID",serviceID);
		ServiceAgent agent = ServiceAgent.getAgent();
		agent.deliverService("ListService", dc);
	}
	js = pm.toJavaScript(dc);
%>
<HTML>
<head>
</head>
<body>
<script laguage="javascript">
var win=window;
while (true){
	if(win.PageManager) break;
	if(win.opener) win = win.opener;
	else if(win.parent && win.parent != win) win = win.parent;
	else break;
}
if (!win.PageManager) alert ('View source to see what the server returned');

<%= js %>
	win.PageManager.DataService.refreshPage(dc, true);
</script>
</body>
</html>
