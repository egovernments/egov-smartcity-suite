<%@page contentType="text/html"  import="java.util.*"  %>
<%@ include file="/includes/taglibs.jsp" %>
<%@ taglib uri="/deptRole" prefix="deptRole" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
		<title>Delete Role</title>
		<LINK rel="stylesheet" type="text/css" href="../../../css/egov.css">
	</head>
	<body bgcolor="#FFFFFF">
	<script>
		function validation()
		{
			if(document.roleForm.departmentId.value=="")
			{
					alert("Please choose a Department");
					document.roleForm.departmentId.focus();
					return false;
			}
			if(document.roleForm.roleId.value=="")
			{
					alert("Please choose a Role");
					document.roleForm.roleId.focus();
					return false;
			}
			else
			{
				return true;
			}

		}

	</script>
	
	<html:form action="Role" onsubmit="return validation();" >
	<center>

		<table class="tableStyle" width="734" border="1">
			<tr>
				<td class="tableheader" align="middle" width="728" height="6"><bean:message key="deleteRole"/>
					<bean:message key="deleteRole.ll"/>
				</td>
			</tr>
			<tr>
				<td width="728" height="200">
				<table border="1" width="730">
					<tr>
						<td class="labelcell" vAlign="center" align="middle" width="100%" colSpan="2" height="26">
						<p align="center"><bean:message key="RoleDetailLabel"/><bean:message key="RoleDetailLabel.ll"/></td>
					</tr>
					<%
						List labels = new ArrayList();
						labels.add("Choose a Department");
						labels.add("Choose a Role");
						labels.add("departmentId");
						labels.add("roleId");

						List departmentList = (ArrayList)session.getAttribute("departmentList");
					%>

					<deptRole:selection name="<%=departmentList %>" labels="<%=labels%>"/>

					<tr bgColor="#dddddd">
						<td class="labelcellforsingletd" vAlign="bottom" align="left" width="100%" colSpan="2" height="20">
							<p align="center"><font class="ErrorText"><bean:message key="note"/><bean:message key="note.ll"/></font>
						</td>
					</tr>
					<tr bgColor="#dddddd">
						<td class="button2" vAlign="bottom" align="left" width="100%" colSpan="2" height="23">
						<p align="center">
						<input type=submit name="bool" value="DELETE">&nbsp;&nbsp;&nbsp;&nbsp;<input type=submit name="bool" value="EDIT"></td>
					</tr>
				</table>
				</td>
			</tr>
		</table>
		</center>
		</html:form>
	


	</body>
</html>