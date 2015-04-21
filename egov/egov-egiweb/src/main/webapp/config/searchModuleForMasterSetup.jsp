<!-- #-------------------------------------------------------------------------------
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
#------------------------------------------------------------------------------- -->
<%@ include file="/includes/taglibs.jsp"%>
<%@page import="java.util.*"%>
<%
	String mode = request.getParameter("mode");
%>

<html>
	<head>
		<title>Search App Config Module</title>
		<script language="JavaScript" type="text/JavaScript">
			function checkOnSubmit() {
				if(document.getElementById('moduleName').value==""){
					alert('Select a module');
					document.getElementById('moduleName').focus();
					return false;
				}
				var mode = '<%= mode %>'
				document.forms[0].action ="${pageContext.request.contextPath}/config/MasterSetUpAction.do?submitType=populateExistingDetails&mode="+mode;
				document.forms[0].submit();
			}	  		
 		</script>
	</head>
	<body>
		<html:form action="/config/MasterSetUpAction">
			<table style="width: 700;" class="tableStyle" cellpadding="0" cellspacing="0" border="1" id="employee">
				<tr>
					<%if("view".equals(mode)){ %>
					<td colspan="6" height=30 align="center" class="tableheader">
						<p align="center">
							<b>View &nbsp;&nbsp;&nbsp;</b>
						</p>
					</td>
					<%} %>
					<%if("create".equals(mode)){ %>
					<td colspan="6" height=30 align="center" class="tableheader">
						<p align="center">
							<b>Create &nbsp;&nbsp;&nbsp;</b>
						</p>
					</td>
					<%} %>
				</tr>
				<tr>
					<td height="15" class="tablesubcaption" colspan="6" align="center"></td>
				</tr>

				<tr>
					<td class="labelcell">
						Select Module Name
						<font color="red">*</font>
					</td>
					<td class="labelcell">
						<html:select styleClass="fieldcell" property="moduleName" styleId="moduleName">
							<html:option value="">-----------Select-----------</html:option>
							<c:forEach var="moduleObj" items="${masterDataForm.moduleSet}">
								<html:option value="${moduleObj}">${moduleObj}</html:option>
							</c:forEach>
						</html:select>
					</td>
				</tr>
				<tr>
					<td class="labelcell" colspan="2">
						&nbsp;
					</td>
				</tr>
			</table>

			<table align='center' id="table2">
				<tr>
					<td class="labelcell"></td>
					<%if("view".equals(mode)){ %>
					<td>
						<input type="button" name="view" value="   View   " onclick="return checkOnSubmit();" />
					</td>
					<%} %>
					<%if("create".equals(mode)){ %>
					<td>
						<input type="button" name="create" value="  Update  " onclick="return checkOnSubmit();" />
					</td>
					<%} %>
				</tr>
			</table>
		</html:form>
	</body>
</html>
