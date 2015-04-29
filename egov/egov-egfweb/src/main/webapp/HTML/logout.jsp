#-------------------------------------------------------------------------------
# eGov suite of products aim to improve the internal efficiency,transparency, 
#      accountability and the service delivery of the government  organizations.
#   
#       Copyright (C) <2015>  eGovernments Foundation
#   
#       The updated version of eGov suite of products as by eGovernments Foundation 
#       is available at http://www.egovernments.org
#   
#       This program is free software: you can redistribute it and/or modify
#       it under the terms of the GNU General Public License as published by
#       the Free Software Foundation, either version 3 of the License, or
#       any later version.
#   
#       This program is distributed in the hope that it will be useful,
#       but WITHOUT ANY WARRANTY; without even the implied warranty of
#       MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
#       GNU General Public License for more details.
#   
#       You should have received a copy of the GNU General Public License
#       along with this program. If not, see http://www.gnu.org/licenses/ or 
#       http://www.gnu.org/licenses/gpl.html .
#   
#       In addition to the terms of the GPL license to be adhered to in using this
#       program, the following additional terms are to be complied with:
#   
#   	1) All versions of this program, verbatim or modified must carry this 
#   	   Legal Notice.
#   
#   	2) Any misrepresentation of the origin of the material is prohibited. It 
#   	   is required that all modified versions of this material be marked in 
#   	   reasonable ways as different from the original version.
#   
#   	3) This license does not grant any rights to any user of the program 
#   	   with regards to rights under trademark law for use of the trade names 
#   	   or trademarks of eGovernments Foundation.
#   
#     In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
#-------------------------------------------------------------------------------
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="bean" uri="/tags/struts-bean" %>


<%
	
	if(session!=null) { 
	session.invalidate(); }

%>


<html>

	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=x-user-defined">
		<META HTTP-EQUIV="Pragma" CONTENT="no-cache">
		<META HTTP-EQUIV="Expires" CONTENT="-1">
		<LINK rel="stylesheet" type="text/css" href="./css/egov.css">
		<title>eGov Birth</title>
	</head>
	<body bgcolor="#FFFFFF">
	
	
	<table align='center' id="table2" >
	 <tr>
	 <td>
	
	 <!-- Body Begins -->
	
	  <DIV id=paymain>
	        <DIV id=paym2>
	       <DIV id=m3>
	       
	<div align="center">

	




<table align="center" cellPadding="3" cellSpacing="0" width="100%"  summary>
			<tbody>
			<tr><td>
			<table align="center" width="100%">
				<tr align="center">
					<td colspan="2" align="center" valign="bottom" height="50">
					<span class="labelcellmedium"><bean:message key="youLogout"/>
						</span>
					</td>
				</tr>
				<tr align="center">
					
				</tr>
				<tr align="center">
					<td  colspan="2" align="center">
						<span class="labelcellmedium"><bean:message key="loginagain"/> <a href="./BnDSecureHome.jsp"><bean:message key="here"/></a>
						</span>
					</td>
				</tr>	
				<tr align="center">
					<td  colspan="2" align="center" valign="bottom" height="100">
					<span class="labelcellmedium">&nbsp;
						</span>
					</td>
				</tr>
				</table></td></tr>			
			</tbody>
		</table>
	<%@ include file = "./egovfooter.html" %>
	</td></tr></table>
</div></div></div></div>
	</body>
</html>
