<%@page contentType="text/html" %>
<%@page import="java.util.*,org.egov.infstr.client.delegate.*,org.egov.lib.rjbac.role.*" %>

<%@ include file="/includes/taglibs.jsp" %>
<%@ taglib uri="/deptRole" prefix="deptRole" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html>

	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
		<title>Create Role</title>
		<LINK rel="stylesheet" type="text/css" href="../../../css/egov.css">
		<html:javascript formName="roleForm"/>
	</head>
	<body bgcolor="#FFFFFF" onload="populateRoleSelect();">
	<%
		response.addHeader("Pragma" , "No-cache") ;
    	response.addHeader("Cache-Control", "no-cache") ;
   		response.addDateHeader("Expires", 1);
	%>
	<html:form action="Role" onsubmit="return validateRoleForm(this);">
		<center>

		<table class="tableStyle" width="734" border="1">
			<tr>
				<td class="tableheader" align="middle" width="728" height="6">
				<bean:message key="UpdateRolelabel"/><bean:message key="UpdateRolelabel.ll"/>
				</td>
			</tr>

			<tr>
				<td width="728" height="200">
				<table border="1" width="730">
					<tr>
						<td class="labelcell" vAlign="center" align="middle" width="100%" colSpan="2" height="26">
						<p align="center"><bean:message key="RoleDetailLabel"/><bean:message key="RoleDetailLabel.ll"/></td>
					</tr>
					<tr>
						<td class="labelcell" width="41%" height="23"><bean:message key="RoleName"/><bean:message key="RoleName.ll"/><font class="ErrorText">*</font></td>
						<td class="labelcell" align="left" width="57%" height="23" class="eGovTblContent">
						<html:text size="36" property="roleName" styleClass="ControlText" /></td>
					</tr>
					<tr>
						<td class="labelcell" width="41%" height="23"><bean:message key="RoleDesc"/><bean:message key="RoleDesc.ll"/></td>
						<td  class="labelcell"align="left" width="57%" height="23" class="eGovTblContent">
						<html:text size="36" property="roleDesc" styleClass="ControlText" /></td>
					</tr>
					<%
						UserRoleDelegate userRoleDelegate = UserRoleDelegate.getInstance();
						String editdeptid = (request.getAttribute("editdeptid")==null)? null :((Integer)request.getAttribute("editdeptid")).toString();
						String editroleid = (request.getAttribute("editroleid")==null)? null :((Integer)request.getAttribute("editroleid")).toString();
						//Populates the List <labels> to be sent as an parameter to the deptRole taglib
						List labels = new ArrayList();
						labels.add("Department");
						labels.add("Reports To");
						labels.add("departmentId");
						labels.add("roleId");
						labels.add(editdeptid );
						labels.add(editroleid);
						List departmentList = userRoleDelegate.getAlldepartments();
						//Gets Role from the session
						Role role = ((Role)session.getAttribute("ROLE")) ;
						String roleIdStr = role.getId().toString();
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

					<input type=submit name="bool" value="UPDATE"></td>

					</tr>
				</table>
				</td>
			</tr>
		</table>
		</center>
	</html:form>
	
	</body>
</html>