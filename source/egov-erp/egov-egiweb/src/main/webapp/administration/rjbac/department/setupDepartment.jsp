<%@page import="java.util.*,org.egov.lib.rjbac.dept.*"%>
<%@ include file="/includes/taglibs.jsp" %>
<html>
	<head>
		<title>Department Setup</title>
		<script>
			function submitForm(mode) {
				if(document.getElementById('deptid').value == "0" && mode != "CREATE") {
					alert("Please select a Department Name");
					return false;
				}
				var actionName = "";
				switch(mode) {
					case "CREATE":
								actionName = "/egi/administration/rjbac/department/createDept.jsp";
								break;
					case "MODIFY":
								actionName = "/egi/SetupDepartment.do?bool=UPDATE";
								break;
					case "VIEW":
								actionName = "/egi/SetupDepartment.do?bool=VIEW";
								break;
					case "DELETE":
								//can not be done due to DEPT ID is used as Foreign Key to many tables
								//if(!uniqueCheckingBoolean('/egi/commonyui/egov/uniqueCheckAjax.jsp' , 'EG_USER','ID_DEPARTMENT', 'deptid', 'no', 'no')) {
									//alert("Unable to delete, User exists for this Department.");
									//return false;
								//}
								//actionName = "/egi/Department.do?bool=DELETE";
								alert("Delete operation not supported, Please contact your Administrator!");
								break;
				}				
				if (actionName != "") {
					document.forms[0].action = actionName;
					document.forms[0].submit();
				}			
			}
		</script>
	</head>
	<body>
		<html:form action="SetupDepartment?" >
			<table align="center" class="tableStyle">
				<tr>
					<td class="tableheader" align="middle" width="728" height="6">Department</td>
				</tr>
			</table>
			<br/><br/>
			<table align="center"  class="tableStyle">
				<tr>
					<td class="labelcell" width="25%" height="23" >Department Name <font class="ErrorText"></font> </td>
					<td class="labelcell" align="center">
						<html:select styleId="deptid"  property="deptid" styleClass="ControlText">
							<html:option value="0">--Choose--</html:option>
							<%
							if(request.getSession().getAttribute("deptList")!=null) {
								List deptList = (ArrayList) request.getSession().getAttribute("deptList");
								for(Iterator itr =deptList.iterator();itr.hasNext();) {
									Department department = (Department)itr.next();
									String deptid = department.getId().toString();
							%>
							<html:option value="<%=deptid%>"><%=department.getDeptName()%></html:option>
							<%
								}
							}
							%>
						</html:select>
					</td>
				</tr>
			</table>
			<br/><br/>
			<table class="tableStyle"  width="728">
				<tr >
					<td align="center">
						<input type="button"  value="  Create " onclick="submitForm('CREATE')">&nbsp;&nbsp;
						<input type="button"  value="  View  " 	 onclick="submitForm('VIEW')">&nbsp;&nbsp;
						<input type="button"  value="  Modify  " onclick="submitForm('MODIFY')">&nbsp;&nbsp;
						<input type="button"  value="  Delete  " onclick="submitForm('DELETE')">
					</td>
				</tr>
			</table>
		</html:form>
	</body>
</html>