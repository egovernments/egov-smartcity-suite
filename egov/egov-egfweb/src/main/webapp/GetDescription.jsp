<!--
  ~ eGov suite of products aim to improve the internal efficiency,transparency,
  ~    accountability and the service delivery of the government  organizations.
  ~
  ~     Copyright (C) <2015>  eGovernments Foundation
  ~
  ~     The updated version of eGov suite of products as by eGovernments Foundation
  ~     is available at http://www.egovernments.org
  ~
  ~     This program is free software: you can redistribute it and/or modify
  ~     it under the terms of the GNU General Public License as published by
  ~     the Free Software Foundation, either version 3 of the License, or
  ~     any later version.
  ~
  ~     This program is distributed in the hope that it will be useful,
  ~     but WITHOUT ANY WARRANTY; without even the implied warranty of
  ~     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  ~     GNU General Public License for more details.
  ~
  ~     You should have received a copy of the GNU General Public License
  ~     along with this program. If not, see http://www.gnu.org/licenses/ or
  ~     http://www.gnu.org/licenses/gpl.html .
  ~
  ~     In addition to the terms of the GPL license to be adhered to in using this
  ~     program, the following additional terms are to be complied with:
  ~
  ~         1) All versions of this program, verbatim or modified must carry this
  ~            Legal Notice.
  ~
  ~         2) Any misrepresentation of the origin of the material is prohibited. It
  ~            is required that all modified versions of this material be marked in
  ~            reasonable ways as different from the original version.
  ~
  ~         3) This license does not grant any rights to any user of the program
  ~            with regards to rights under trademark law for use of the trade names
  ~            or trademarks of eGovernments Foundation.
  ~
  ~   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
  -->
<%@page language = "java"
   import = "com.exilant.exility.common.DataCollection"
   import = "com.exilant.exility.common.Messages"
%>
<%@ page import="com.exilant.exility.common.SafeJSString" %>
<%@ page import="com.exilant.exility.pagemanager.SecurityGuard" %>
<%@ page import="com.exilant.exility.pagemanager.ServiceAgent" %>
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
				out.print("\n bootbox.alert('Description service not invoked properly. serviceID and keyValue pairs are to be sent as paramaters');");		
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
				out.print("\nbootbox.alert('Exility Error: Description Service did not succeed.');");		
				out.print("\nbootbox.alert('" + SafeJSString.escape(dc.getMessageText()) +"');");
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
	
