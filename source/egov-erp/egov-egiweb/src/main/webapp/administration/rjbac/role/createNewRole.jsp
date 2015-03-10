<%@page import="java.util.*"%>
<%@ include file="/includes/taglibs.jsp" %>
<html>
	<head>
		<title>Create Role</title>
		<script>
			function bodyonload() {
				document.getElementById('roleName').value="";
				document.getElementById('roleDesc').value="";
				document.getElementById('roleNameLocal').value="";
				document.getElementById('roleDescLocal').value="";
			}

			function validation() {
				if(document.getElementById('roleName').value=="") {
					alert("Please enter a Role Name");
					return false;
				}
				if(!isNaN(document.getElementById('roleName').value)) {
					alert("Role Name can not be a numeric value.");
					document.getElementById('roleName').value="";
					document.getElementById('roleName').focus();
					return false;
				}
   				if(!uniqueCheckingBoolean('/egi/commonyui/egov/uniqueCheckAjax.jsp' , 'EG_ROLE', 'name', 'roleName', 'no', 'no')) {
   					alert("Role Name already exists..!");
   					document.getElementById('roleName').value="";
   					document.getElementById('roleName').focus();
   					return false;
				}
				return true;
			}
		</script>
		
	</head>
	<body onload="bodyonload()" >
		<html:form action="CreateRole.do?bool=CREATE" onsubmit="return validation()">
			<table align="center" id="getAllDetails" name="getAllDetails">
				<table align="center" class="tableStyle">
					<tr>
						<td class="tableheader" align="middle" width="728" height="6">Create Role</td>
					</tr>
					<tr>
						<td colspan="4">&nbsp;</td>
					</tr>
			</table>
			<br/><br/>
			<table align="center"  class="tableStyle">
				<tr>
					<td  class="labelcell" width="40%" height="23" >Role Name<font class="ErrorText">*</font></td>
					<td  class="labelcell" align="left" width="40%" height="23" >
						<html:text property="roleName" styleId="roleName" styleClass="ControlText" />
					</td>
				</tr>
				<tr>
					<td class="labelcell"  width="40%" height="23" >Role Description</td>
					<td class="labelcell"  align="left" width="40%" height="23" >
						<html:text property="roleDesc" styleId="roleDesc" styleClass="ControlText"  />
					</td>
				</tr>
				<tr>
					<td  class="labelcell"  width="40%" height="23" >Role Name Local</td>
					<td class="labelcell"  align="left" width="40%" height="23" >
						<html:text property="roleNameLocal" styleId="roleNameLocal" styleClass="ControlText" />
					</td>
				</tr>
				<tr>
					<td  class="labelcell" width="40%" height="23" >Role Description Local</td>
					<td class="labelcell"  align="left" width="40%" height="23" >
						<html:text property="roleDescLocal" styleId="roleDescLocal" styleClass="ControlText"  />
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