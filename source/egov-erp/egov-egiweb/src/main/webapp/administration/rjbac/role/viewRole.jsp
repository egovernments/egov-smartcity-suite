<%@page contentType="text/html" %>
<%@page import="java.util.*,org.egov.infstr.client.delegate.*,org.egov.lib.rjbac.role.*" %>

<%@ include file="/includes/taglibs.jsp" %>
<%@ taglib uri="/deptRole" prefix="deptRole" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html>

	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
		<title>View Role</title>
		<LINK rel="stylesheet" type="text/css" href="../../../css/egov.css">
	</head>
	<body bgcolor="#FFFFFF" >
	<%
		response.addHeader("Pragma" , "No-cache") ;
    	response.addHeader("Cache-Control", "no-cache") ;
   		response.addDateHeader("Expires", 1);
	%>
		<html:form action="Role" >
			<center>
			<table class="tableStyle" width="734" border="1">
				<tr>
					<td class="tableheader" align="middle" width="728" height="6">View Role Details
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
							//gets the Role from the session
							Role role = ((Role)session.getAttribute("ROLE")) ;
							String roleIdStr 	= role.getId().toString();
							String roleDesc 	= (role.getRoleDesc()== null)?"":role.getRoleDesc();
							String parentRole 	= (role.getParent()== null)? "" : role.getParent().getRoleName();

						%>
						<tr>
							<td class="labelcell" width="41%" height="23"><bean:message key="RoleName"/><bean:message key="RoleName.ll"/><font class="ErrorText">*</font></td>
							<td class="labelcell" align="left" width="57%" height="23" class="eGovTblContent">&nbsp;&nbsp;<%=role.getRoleName()%>
							</td>
						</tr>
						<tr>
							<td class="labelcell" width="41%" height="23"><bean:message key="RoleDesc"/><bean:message key="RoleDesc.ll"/></td>
							<td  class="labelcell" align="left" width="57%" height="23" class="eGovTblContent">&nbsp;&nbsp;<%=roleDesc%>
							</td>
						</tr>
						<tr>
							<td class="eGovTblContent" width="41%" height="23">Department<font class="ErrorText">*</font></td>
							<td  align="left" width="57%" height="23" class="eGovTblContent">&nbsp;&nbsp;<%=role.getRoleDescLocal()%>
							</td>
						</tr>
						<tr>
							<td class="labelcell" width="41%" height="23">Reports To</td>
							<td class="labelcell" align="left" width="57%" height="23" class="eGovTblContent">&nbsp;&nbsp;<%=parentRole%>
							</td>
						</tr>

						<tr bgColor="#dddddd">
							<td class="labelcellforsingletd" vAlign="bottom" align="left" width="100%" colSpan="2" height="20">
								<p align="center"><font class="ErrorText"><bean:message key="note"/><bean:message key="note.ll"/></font>
							</td>
						</tr>
						<tr bgColor="#dddddd">
							<td vAlign="bottom" class="button2" align="left" width="100%" colSpan="2" height="23">
								<p align="center">

								<input type=button  value="ADD USER" onclick="window.location = '/SetupUser.do';">&nbsp;&nbsp;
								<input type=button 	value="EDIT"	 onclick="window.location = '/Role.do?bool=EDIT&roleid=<%=roleIdStr%>';">&nbsp;&nbsp;
								<input type=button  value="DELETE" 	 onclick="window.location = '/Role.do?bool=DELETE&roleid=<%=roleIdStr%>';">
							</td>
						</tr>
					</table>
					</td>
				</tr>
			</table>
			</center>
		</html:form>
	
	</body>
</html>