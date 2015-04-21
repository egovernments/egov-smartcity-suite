<%---------------------------------------------------------------------------------
# eGov suite of products aim to improve the internal efficiency,transparency, 
#    accountability and the service delivery of the government  organizations.
# 
#     Copyright (C) <2015>  eGovernments Foundation
# 
#     The updated version of eGov suite of products as by eGovernments Foundation 
#     is available at http://www.egovernments.org
# 
#     This program is free software: you can redistribute it and/or modify
#     it under the terms of the GNU General Public License as published by
#     the Free Software Foundation, either version 3 of the License, or
#     any later version.
# 
#     This program is distributed in the hope that it will be useful,
#     but WITHOUT ANY WARRANTY; without even the implied warranty of
#     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
#     GNU General Public License for more details.
# 
#     You should have received a copy of the GNU General Public License
#     along with this program. If not, see http://www.gnu.org/licenses/ or 
#     http://www.gnu.org/licenses/gpl.html .
# 
#     In addition to the terms of the GPL license to be adhered to in using this
#     program, the following additional terms are to be complied with:
# 
# 	1) All versions of this program, verbatim or modified must carry this 
# 	   Legal Notice.
# 
# 	2) Any misrepresentation of the origin of the material is prohibited. It 
# 	   is required that all modified versions of this material be marked in 
# 	   reasonable ways as different from the original version.
# 
# 	3) This license does not grant any rights to any user of the program 
# 	   with regards to rights under trademark law for use of the trade names 
# 	   or trademarks of eGovernments Foundation.
# 
#   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
#-------------------------------------------------------------------------------
--%>
<%@ include file="/includes/taglibs.jsp" %>

<%
String msg = (String)request.getAttribute("MESSAGE");
%>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
		<title>Acknowledgement</title>
		<LINK rel="stylesheet" type="text/css" href="<c:url value="/css/egov.css" />">
	</head>
	<body bgcolor="#FFFFFF">

		<form name="ackForm">
			<table align="center" >
				<tr>
					<td colspan=4>&nbsp;</td>
				</tr>
				<tr>
					<td class="tableheader" align="center" width="728" height="23"><bean:message key="acknowledgementlabel"/>
						<bean:message key="acknowledgementlabel.ll"/>
					</td>
				</tr>
				<tr>
					<td  align="center" width="728" style="font-size:12px" height="50px">
						<p align="center"><b><font color="navy"><%= msg %></font></b></p>
					</td>
				</tr>
				<tr height="50px">
					<td>&nbsp;</td>
				</tr>
				<tr>
					<td class="button2" align="center" width="728" height="23">
						<input type="button" value ="Close" onclick="javascript:window.close();">
					</td>
				</tr>
			</table>
		</form>
	</body>
</html>
