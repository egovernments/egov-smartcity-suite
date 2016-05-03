<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ page language="java" import="org.egov.infstr.utils.EGovConfig"%>
<%--
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
  --%>

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
