<%@page contentType="text/html" %>
<%@ include file="/includes/taglibs.jsp" %>
<html>
	<head>
		<title>Modify Role</title>
		<link rel="stylesheet" type="text/css" href="/egi/css/egov.css"/>
	</head>
	<body>
		<html:form action="CreateRole?bool=UPDATE" >
			<table align="center" class="tableStyle">
				<tr>
					<td class="tableheader" align="middle" width="728" height="6">Update Role</td>
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
						<html:text property="roleName" readonly="true" styleClass="ControlText" />
					</td>
				</tr>
				<tr>
					<td class="labelcell"  width="40%" height="23" >Role Desc</font></td>
					<td class="labelcell" align="left" width="40%" height="23" >
						<html:text property="roleDesc" styleClass="ControlText"  />
					</td>
				</tr>
				<tr>
					<td  class="labelcell" width="40%" height="23" >Role Name Local</td>
					<td class="labelcell" align="left" width="40%" height="23" >
						<html:text property="roleNameLocal" styleClass="ControlText" />
					</td>
				</tr>
				<tr>
					<td class="labelcell"  width="40%" height="23" >Role Desc Local</font></td>
					<td class="labelcell" align="left" width="40%" height="23" >
						<html:text property="roleDescLocal" styleClass="ControlText"  />
					</td>
				</tr>
			</table>
			<br/><br/>
			<table align="center" class="tableStyle">
				<tr>
					<td align="middle" width="728" colspan="4" >
						<html:submit value="  Save  " />
					</td>
				</tr>			
			</table>
		</html:form>
	</body>
</html>