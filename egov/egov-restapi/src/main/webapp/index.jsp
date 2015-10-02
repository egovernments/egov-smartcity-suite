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
<%@page session="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
	<head>
		<script type="text/javascript" src="./resources/javascript/jquery/jquery-1.7.2.min.js"></script>
		<script type="text/javascript">
			$(document).ready(function(){
				$("#createProeprtyBtn").click(function(){
					createProeprty();
				});
			});
			function createProeprty() {
				$.ajax({
					url : $("#urlField").val(),
					type : 'POST',
					dataType : 'JSON',
					data : $("#createPropertyField").val(),
					success : function(data) {
						console.log(JSON.stringify(data));
					},
					error : function(e) {
						alert(e.message);
					}
				});
			}
		</script>
	</head>
	<body>
		<div id="create_proeprty">
			<table>
				<tr>
					<td>Create Property Request JSON</td>
					<td><textarea rows="5" cols="50" id="createPrpertyField">
					</textarea> /></td>
				</tr>
				<tr>
					<td>URL</td>
					<td><input type="text" id="urlField" name="urlField" value=""></td>
				</tr>				
				<tr>
					<td>Request JSON</td>
					<td><textarea rows="5" cols="50" id="createPropertyField">
					</textarea> /></td>
				</tr>
				<tr>
					<td></td>
					<td><input id="createProeprtyBtn" type="button" value="Create"></td>
				</tr>
			</table>
		</div>
	</body>
</html>