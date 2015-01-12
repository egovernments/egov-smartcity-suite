<%@page contentType="text/html" %>
<%@page import="java.util.*,org.egov.lib.rjbac.dept.*" %>
<%@ include file="/includes/taglibs.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
		<title>Delete Department</title>
		<LINK rel="stylesheet" type="text/css" href="../../../css/egov.css">
		<html:javascript formName="setUpDeleteEditDeptForm" />
	</head>
	<body bgcolor="#FFFFFF">
	

	<html:form action="DeleteUpdateDepartment"  onsubmit="return validateSetUpDeleteEditDeptForm(this);">

	<center>
		<table class="tableStyle" width="734" border="1">
			<tr>
				<td class="tableheader" align="middle" width="728" height="6"><bean:message key="deleteDept"/>
					<bean:message key="deleteDept.ll"/>
				</td>
			</tr>
			<tr>
				<td width="728" height="100">
				<table border="1" width="730">
				<%
						List departmentList = (ArrayList)session.getAttribute("departmentList");
						System.out.println("In deleteDepartment Jsp :: departmentList::: " + departmentList);
				%>
					<tr bgColor="#dddddd">
						<td class="labelcell" width="41%" height="23"><bean:message key="Deptname"/><bean:message key="Deptname.ll"/><font class="ErrorText">*</font></td>
						<td align="left" width="57%" height="23" class="labelcell">
						<html:select property="deptid" styleClass="ControlText" >
						<html:option value="">Choose</html:option>
						<%
							for(Iterator itr = departmentList.iterator();itr.hasNext();)
							{
								Department dept = (Department)itr.next();
								String deptName = dept.getDeptName();
								String depid = dept.getId().toString();
						%>
						<html:option value="<%=depid%>"><%=deptName%></html:option>
						<%
							}
						%>
						</html:select>
						</td>
					</tr>

					<tr bgColor="#dddddd">
					<td class="labelcellforsingletd" vAlign="bottom" align="left" width="100%" colSpan="2" height="20">
						<p align="center"><font class="ErrorText"><bean:message key="note"/><bean:message key="note.ll"/></font>
					</td>
					</tr>
					<tr bgColor="#dddddd">
						<td class="button2" vAlign="bottom" align="left" width="100%" colSpan="2" height="23">
						<p align="center">
						<input type=submit name="bool" value="DELETE">&nbsp;&nbsp;&nbsp;&nbsp;<input type=submit name="bool" value="EDIT"></td>
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