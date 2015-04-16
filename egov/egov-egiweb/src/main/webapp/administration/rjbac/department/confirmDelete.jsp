<%@page contentType="text/html" %>
<%@page import="org.egov.lib.rjbac.dept.*,org.egov.lib.rjbac.role.*" %>

<%@ include file="/includes/taglibs.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
	<head>
		<title>Delete Confirm Page</title>
		<LINK rel="stylesheet" type="text/css" href="../../../css/egov.css">
	</head>
	<body bgcolor="#FFFFFF">

	<html:form action="DeleteUpdateDepartment" >

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
					<tr>
						<td class="tablesubcaption" vAlign="center" align="middle" width="100%" height="30">
						<p align="center">Are You Sure Want to Delete the Department </td>
					</tr>
					<%
						//Gets the Department session
						String roleIdStr	=	"";
						String deptID		=	"";
						if((Department)session.getAttribute("DEPARTMENT")!= null)
						{
							Department dept =(Department)session.getAttribute("DEPARTMENT");
							deptID = dept.getId().toString();
						}
					%>
					<tr bgColor="#dddddd">
						<td class="button2" vAlign="bottom" align="left" width="100%" height="23">
						<p align="center">
							<input type=button  value="DELETE" onclick="window.location = '/DeleteUpdateDepartment.do?bool=ConfirmDelete&deptid=<%=deptID%>';">
						</td>
					</tr>
				</table>
			</tr>
		</table>
		</center>
	</html:form>
	

	</body>
</html>