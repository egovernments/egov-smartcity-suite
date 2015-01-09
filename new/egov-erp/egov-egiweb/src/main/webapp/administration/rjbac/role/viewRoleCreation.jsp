<%@ include file="/includes/taglibs.jsp" %>
<html>
	<head>
		<title>View Role</title>
		<link rel="stylesheet" type="text/css" href="/egi/css/egov.css" />
		<script>
			function goBack() {
				document.forms[0].action = "/egi/SetupRole.do?bool=VIEW";
				document.forms[0].submit();
			}
		</script>
	</head>
	<body>
		<html:form action="CreateRole?" >
			<table align="center" class="tableStyle">
				<tr>
					<td class="tableheader" align="middle" width="728" height="6">View Role</td>
				</tr>
				<tr>
					<td colspan=4>&nbsp;</td>
				</tr>
			</table>
			<br/><br/>
			<table align="center"  class="tableStyle">
				<tr>
					<td class="labelcell"  width="40%" height="23" >Role Name</td>
					<td class="labelcell" align="left" width="40%" height="23" >
						<html:text property="roleName" readonly= "true" styleClass="ControlText" />
					</td>
				</tr>
				<tr>
					<td class="labelcell"  width="40%" height="23" >Role Description</font></td>
					<td class="labelcell" align="left" width="40%" height="23" >
						<html:text property="roleDesc" readonly= "true" styleClass="ControlText"  />
					</td>
				</tr>
				<tr>
					<td class="labelcell"  width="40%" height="23" >Role Name Local</td>
					<td class="labelcell" align="left" width="40%" height="23" >
						<html:text property="roleNameLocal" readonly= "true" styleClass="ControlText" />
					</td>
				</tr>
				<tr>
					<td class="labelcell"  width="40%" height="23" >Role Description Local</font></td>
					<td  class="labelcell" align="left" width="40%" height="23" >
						<html:text property="roleDescLocal" readonly= "true" styleClass="ControlText"  />
					</td>
				</tr>
			</table>
			<br/><br/>
			<table align="center" class="tableStyle">
				<tr>
					<td align="middle" width="728" colspan="4" >
						<input type="button" value=" Back " onclick="goBack()">
					</td>
				</tr>			
			</table>
		</html:form>
	</body>
</html>