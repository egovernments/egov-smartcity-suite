<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ page language="java" import="org.egov.infstr.utils.EGovConfig"%>
<%
String xmlConfigName = request.getParameter("xmlconfigname"); 
String categoryName = request.getParameter("categoryname");
%>
<html>
	<head>
		<title>Search screen</title>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">	
		
		<link rel=stylesheet href="/egi/css/egov.css" type="text/css">
		<link href="/egi/commonyui/yui2.7/menu/assets/skins/sam/menu.css" rel="stylesheet" type="text/css"></link>
		<link rel=stylesheet href="/egi/commonyui/yui2.7/treeview/assets/skins/sam/treeview.css" type="text/css">
		
		<script type="text/javascript" src="/egi/commonyui/yui2.7/yahoo/yahoo-min.js"></script>
		<script type="text/javascript" src="/egi/commonyui/yui2.7/yahoo-dom-event/yahoo-dom-event.js"></script>
        <script type="text/javascript" src="/egi/commonyui/yui2.7/container/container_core-min.js"></script>
		<script type="text/javascript" src="/egi/commonyui/yui2.7/event/event-min.js"></script>
		<script type="text/javascript" src="/egi/commonyui/yui2.7/connection/connection-min.js"> </script>
		<script src="/egi/commonyui/yui2.7/menu/menu-min.js" type="text/javascript"></script>
		<script type="text/javascript" src="/egi/commonyui/yui2.7/treeview/treeview-min.js"></script>	
		
		<script>       
			var xmlConfigName = '<%=request.getParameter("xmlconfigname")%>'
			var categoryName = '<%=request.getParameter("categoryname")%>'	
		</script>
		
	</head>

	<body class="yui-skin-sam" oncontextmenu="return false;">
		<table align='center' id="table2">
			<tr>
				<td>
					<div id="topStyle">
						<div id="bottomStyle">
							<div id="tabletextStyle">
								<table align="center" id="mainTable" name="mainTable"
									class="smallTableStyle">
									<tr>
									<tr>
										<td colspan=4>
											&nbsp;
										</td>
									</tr>
									<td class="tableheader" colspan="4" align="center">
										<span id="screenName"> Search screen - <%=EGovConfig.getProperty(xmlConfigName, "headername", "", categoryName)%>
										</span>
									</td>
									</tr>
									<tr>
										<td colspan="4">
											<table align="center" width="100%">

												<tr>
													<td colspan=4>
														&nbsp;
													</td>
												</tr>
												<tr>
													<td colspan=4>
														<div id="menutree" align="left"></div>
													</td>
												</tr>
												<tr>
													<td colspan=4>
														&nbsp;
													</td>
												</tr>
											</table>
											<table width="100%">
												<tr>
													<td colspan=4>
														&nbsp;
													</td>
												</tr>
												<tr>
													<td colspan="4" align="center">
														<input type="button" class="button"
															onclick="window.close()" value="Close" />
													</td>
												</tr>
												<tr>
													<td colspan="4" align="center">
														<font size="2" color="red">Right click on item to select</font>
													</td>
												</tr>
											</table>

										</td>
									</tr>
								</table>
							</div>
						</div>
					</div>
				</td>
			</tr>
		</table>
	</body>
</html>
<script type="text/javascript" src="genericTreeView.js"></script>
