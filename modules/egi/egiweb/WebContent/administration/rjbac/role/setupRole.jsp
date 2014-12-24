<%@page contentType="text/html"%>
<%@page import="java.util.List,java.util.ArrayList,java.util.Iterator,org.egov.lib.rjbac.role.Role"%>
<%@include file="/includes/taglibs.jsp"%>
<html>
	<head>
		<title>Role Setup</title>
		<link rel="stylesheet" type="text/css" href="/egi/css/egov.css"/>
		<script type="text/javascript" src="/egi/script/jsCommonMethods.js"></script>
		<script type="text/javascript" src="/egi/commonjs/ajaxCommonFunctions.js"></script>
		<script>
		
			function submitForm(mode) {
				if(document.getElementById('roleId').value == "0" && mode != "CREATE") {
					alert("Please select a Role Name");
					return false;
				}
				var actionName = "";
				switch(mode) {
					case "CREATE":
								actionName = "/egi/administration/rjbac/role/createNewRole.jsp";
								break;
					case "MODIFY":
								actionName = "/egi/SetupRole.do?bool=UPDATE";
								break;
					case "VIEW":
								actionName = "/egi/SetupRole.do?bool=VIEW";
								break;
					case "DELETE":
								if (!uniqueCheckingBoolean('/egi/commonyui/egov/uniqueCheckAjax.jsp' , 'EG_USERROLE','ID_ROLE', 'roleId', 'no', 'no')) {
									alert("Can not delete this Role..! User are assigned to this Role.");
									return false;
								}
								if (!uniqueCheckingBoolean('/egi/commonyui/egov/uniqueCheckAjax.jsp' , 'EG_ROLEACTION_MAP','ROLEID', 'roleId', 'no', 'no')) {
									alert("Can not delete this Role..! Action mapping exist for this Role.");
									return false;
								}
								actionName = "/egi/CreateRole.do?bool=DELETE";
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

		<html:form action="SetupRole?">
			<table align="center" class="tableStyle">
				<tr>
					<td class="tableheader" align="middle" width="728" height="6">
						Roles
					</td>
				</tr>
				<tr>
					<td colspan=4>
						&nbsp;
					</td>
				</tr>
			</table>
			<br/><br/>
			<table align="center" class="tableStyle">
				<tr>
					<td class="labelcell" width="25%" height="23" colspan="2">
						Role Name
						<font class="ErrorText"></font>
					</td>
					<td class="labelcell" align="left" colspan="2">
						<html:select styleId="roleId"  property="roleId" styleClass="ControlText">
							<html:option value="0">--Choose--</html:option>
							<%
								if(request.getSession().getAttribute("roleList")!=null) {
									List roleList = (ArrayList) request.getSession().getAttribute("roleList");
									for(Iterator itr =roleList.iterator();itr.hasNext();){
										Role role = (Role)itr.next();
										String roleId = role.getId().toString();
							%>
							<html:option value="<%=roleId %>"><%=role.getRoleName()%></html:option>

							<%
									}
								}
							%>
						</html:select>						
					</td>
				</tr>
			</table>
			<br/><br/>
			<table align="center" class="tableStyle">
				<tr>
					<td align="middle" width="728" colspan="4" >
						<input type="button"  value="  Create" onclick="return submitForm('CREATE')">&nbsp;&nbsp;
						<input type="button"  value="View"  onclick="return submitForm('VIEW')">&nbsp;&nbsp;
						<input type="button"  value="Modify" onclick="return submitForm('MODIFY')">&nbsp;&nbsp;
						<input type="button"  value="Delete" onclick="return submitForm('DELETE')">
					</td>
				</tr>			
			</table>
		</html:form>
	</body>
</html>