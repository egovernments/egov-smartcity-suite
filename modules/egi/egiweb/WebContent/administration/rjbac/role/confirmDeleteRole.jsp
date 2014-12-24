<%@page contentType="text/html" %>
<%@page import="org.egov.lib.rjbac.dept.*,org.egov.lib.rjbac.role.*" %>

<%@ include file="/includes/taglibs.jsp" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
	<head>
		<title>Delete Role Confirm Page</title>
		<LINK rel="stylesheet" type="text/css" href="../../../css/egov.css">
	</head>
	<body bgcolor="#FFFFFF">
		<html:form action="Role" >

			<center>
			<table class="tableStyle" width="734" border="1">
				<tr>
						<td class="tableheader" align="middle" width="728" height="6">
						Confirm Deletion
						</td>
				</tr>
				<tr>
					<td width="728" height="100">
					<table border="1" width="730">

						<%
							//Gets Role from the session
							String roleIdStr	=	"";
							Role role = null;
							if((Role)session.getAttribute("ROLE")!= null)
							{
								role = (Role)session.getAttribute("ROLE");
								roleIdStr 	= role.getId().toString();
							}
						%>
						<tr>
							<td class="labellcellforsingletd" vAlign="center" align="middle" width="100%" height="30">
							<p align="center">Are You Sure Want to Delete the Role <%= role.getRoleName()%></td>
						</tr>

						<tr bgColor="#dddddd">
							<td class="button2" vAlign="bottom" align="left" width="100%" height="23">
							<p align="center">
								<input type=button  value="DELETE" onclick="window.location = '/Role.do?bool=ConfirmDelete&roleid=<%=roleIdStr%>';">
							</td>
						</tr>
					</table>
				</tr>
			</table>
			</center>
		</html:form>
	

	</body>
</html>