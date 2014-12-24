
<%@page language = "java"
   import = "com.exilant.exility.common.*"
   import = "com.exilant.exility.pagemanager.*"
%>
<HTML>
<head></head>
<body>
<script laguage="javascript">
var win=window;
while (true){
	if(win.PageManager) break;
	if(win.opener) win = win.opener;
	else if(win.parent && win.parent != win) win = win.parent;
	else break;
}
if (!win.PageManager)alert ('View source to see what the server returned');
var values; 

<%
			DataCollection dc = new DataCollection();
			SecurityGuard guard = new SecurityGuard();
			guard.clearedSecurity(dc, request);

			String[] serviceID   = request.getParameterValues("serviceID");
			String[] keyValue    = request.getParameterValues("keyValue");

			if (serviceID == null || keyValue == null || serviceID.length != keyValue.length) 
			{
				out.print("\n alert('Description service not invoked properly. serviceID and keyValue pairs are to be sent as paramaters');");		
				out.print("\n </script></body></html>");		
				return;
			} 
			int length = keyValue.length;
			dc.addValueList("serviceID", serviceID);
			dc.addValueList("keyValue", keyValue);

			ServiceAgent agent = ServiceAgent.getAgent();
			agent.deliverService("DescriptionService", dc);

			if (dc.getSevirity() > Messages.WARNING)
			{ // error
				out.print("\nalert('Exility Error: Description Service did not succeed.');");		
				out.print("\nalert('" + SafeJSString.escape(dc.getMessageText()) +"');");
				out.print("\n </script></body></html>");		
				return;
			}

			String[][] grid;
			for(int i = 0; i < keyValue.length; i++)
			{
				grid = dc.getGrid(serviceID[i]+"_"+keyValue[i]);
				if(null == grid || grid.length==0 || 
					grid[0] == null || grid[0].length == 0) {
					 out.print("\nvalues = null;");
				} else {
				
					out.print("\nvalues=['" +grid[0][0]);
					for(int j=1; j<grid[0].length; j++) out.print("','" +SafeJSString.escape(grid[0][j]));
					out.print("'];");
				}
				out.println("\nif(win.PageManager)win.PageManager.DescService.setDesc('" + serviceID[i] + "','" + keyValue[i] +"',values);");
			}
%>
</script>
</body>
</html>
	