<%@page contentType="text/html" %>
<%@page import="org.egov.lib.rjbac.dept.*" %>

<%@ include file="/includes/taglibs.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html>

	<head>
	<meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
	<title>Update Department</title>
	<LINK rel="stylesheet" type="text/css" href="../../../css/egov.css">

	<html:javascript formName="setUpDeleteEditDeptForm"/>

	</head>

	<body bgcolor="#FFFFFF">

		<html:form action="UpdateDepartment" onsubmit="return validateSetUpDeleteEditDeptForm(this);" >

		<center>
		<table class="tableStyle" width="734" border="1">
			<tr>
					<td class="tableheader" align="middle" width="728" height="6">
					<bean:message key="UpdateDeptlabel"/><bean:message key="UpdateDeptlabel.ll"/>
					</td>
			</tr>
			<tr>
				<td width="728" height="200">
				<table border="1" width="730">
					<tr>
						<td class="tablesubcaption" vAlign="center" align="middle" width="100%" colSpan="2" height="26">
						<p align="center"><bean:message key="DeptDetailLabel"/><bean:message key="DeptDetailLabel.ll"/></td>
					</tr>
					<tr>
						<td class="labelcell" width="41%" height="23"><bean:message key="Deptname"/><bean:message key="Deptname.ll"/><font class="ErrorText">*</font></td>
						<td class="labelcell" align="left" width="57%" height="23" class="eGovTblContent">
						<html:text size="36" property="deptName" styleClass="ControlText" /></td>
					</tr>
					<tr>
						<td class="labelcell" width="41%" height="23"><bean:message key="DeptDesc"/><bean:message key="DeptDesc.ll"/></td>
						<td  class="labelcell" align="left" width="57%" height="23" class="eGovTblContent">
						<html:text size="36" property="deptDetails" styleClass="ControlText" />
						<html:hidden  property="deptid"  />
					</td>
					<%
						//Gets the department from the session
						Department dept =(Department)session.getAttribute("DEPARTMENT");
						String deptID = dept.getId().toString();
					%>
					</tr>

				   	<tr bgColor="#dddddd">
						<td class="labelcellforsingletd" vAlign="bottom" align="left" width="100%" colSpan="2" height="20">
							<p align="center"><font class="ErrorText"><bean:message key="note"/><bean:message key="note.ll"/></font>
						</td>
					</tr>
					<tr bgColor="#dddddd">
						<td class="button2" vAlign="bottom" align="left" width="100%" colSpan="2" height="23">
						<p align="center">
						<input type=submit name="bool" value="UPDATE"></td>
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