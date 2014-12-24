<%@page contentType="text/html" %>
<%@page import="org.egov.lib.rjbac.dept.*" %>

<%@ include file="/includes/taglibs.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html>

	<head>
	<meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
	<title>View Department</title>
	<LINK rel="stylesheet" type="text/css" href="../../../css/egov.css">



	</head>

	<body bgcolor="#FFFFFF">

		<html:form action="DeleteUpdateDepartment" >
			<html:hidden  property="deptid"  />
		<center>
		<table class="tableStyle" width="734" border="1">
			<tr>
					<td class="tableheader" align="middle" width="728" height="6">
					View Department Details
					</td>
			</tr>
			<tr>
				<td width="728" height="200">
				<table border="1" width="730">
					<tr>
						<td class="tablesubcaption" vAlign="center" align="middle" width="100%" colSpan="2" height="26">
						<p align="center"><bean:message key="DeptDetailLabel"/><bean:message key="DeptDetailLabel.ll"/></td>
					</tr>
					<%
						//Gets the department from session
						Department dept =(Department)session.getAttribute("DEPARTMENT");
						String deptID = dept.getId().toString();
						String 	deptDetails = dept.getDeptDetails()== null ? "" :dept.getDeptDetails();
					%>
					<tr>
						<td class="labelcell" width="41%" height="23"><bean:message key="Deptname"/><bean:message key="Deptname.ll"/><font class="ErrorText">*</font></td>
						<td align="left" width="57%" height="23" class="labelcell">&nbsp;&nbsp;<%=dept.getDeptName()%>
						</td>

					</tr>
					<tr>
						<td class="labelcell" width="41%" height="23"><bean:message key="DeptDesc"/><bean:message key="DeptDesc.ll"/></td>
						<td  align="left" width="57%" height="23" class="labelcell">&nbsp;&nbsp;<%=deptDetails%>
						</td>

					</td>
					</tr>
					<tr bgColor="#dddddd">
						<td class="labelcellforsingletd" vAlign="bottom" align="left" width="100%" colSpan="2" height="20">
							<p align="center"><font class="ErrorText"><bean:message key="note"/><bean:message key="note.ll"/></font>
						</td>
					</tr>
					<tr bgColor="#dddddd">
						<td vAlign="bottom" align="left" width="100%" colSpan="2" height="23">
						<p align="center">
						<input type=button class="button2" value="ADD ROLE" onclick="window.location = '/administration/rjbac/role/crtRole.jsp?deptid=<%=deptID%>';">&nbsp;&nbsp;
						<input type=button  class="button2" value="EDIT" onclick="window.location = '/DeleteUpdateDepartment.do?bool=EDIT&deptid=<%=deptID%>';">&nbsp;
						<input type=button class="button2" value="DELETE" onclick="window.location = '/DeleteUpdateDepartment.do?bool=DELETE&deptid=<%=deptID%>';"></td>
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