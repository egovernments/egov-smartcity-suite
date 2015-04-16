<%@page contentType="text/html" import="java.util.*,org.egov.lib.rjbac.dept.*" %>
<%@ include file="/includes/taglibs.jsp" %>
<%@ taglib uri="/deptUser" prefix="deptUser" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
	<head>
	<meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
	<title>Delete User</title>
	<LINK rel="stylesheet" type="text/css" href="../../../css/egov.css">
	<html:javascript formName="setUpDeleteEditUserForm" />
	</head>

	<body bgcolor="#FFFFFF">
	

	<html:form action="DeleteUpdateUser" onsubmit="return validateSetUpDeleteEditUserForm(this);" >

	<center>
		<table class="tableStyle" width="734" border="1">
			<tr>
				<td class="tableheader" align="middle" width="728" height="6"><bean:message key="deleteUser"/>
					<bean:message key="deleteUser.ll"/>
				</td>
			</tr>
			<tr>
				<td width="728" height="100">
				<table border="1" width="730">


					<%
						List labels = new ArrayList();
						labels.add(" Choose a Department");
						labels.add("Choose a User");
						labels.add("departmentId");
						labels.add("userid");
						List departmentList = (ArrayList)session.getAttribute("departmentList");
						System.out.println("In deleteUser Jsp :: departmentList::: " + departmentList);
				%>

					<deptUser:selection deptCollection="<%=departmentList%>" labels="<%=labels%>" />

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