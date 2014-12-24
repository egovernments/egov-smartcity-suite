<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page
	import="java.util.*,
		org.egov.lib.admbndry.*,
		org.egov.lib.rjbac.dept.*,
		org.egov.lib.rjbac.role.*,
		org.egov.lib.rjbac.user.*,
		org.egov.lib.rjbac.role.Role,
		org.egov.lib.rjbac.user.UserRole,
		java.text.SimpleDateFormat"%>
<%@ include file="/includes/taglibs.jsp"%>
<%@ taglib uri="/deptRole" prefix="deptRole"%> 

<html>
	<head>
		<title>Update User</title>
		<html:javascript formName="userForm" />
		<%

				UserImpl user =(UserImpl)session.getAttribute("userDetail");
				session.removeAttribute("userDetail");
				String username= user.getUserName();
				SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
				Set delUserRoles = new HashSet();
				session.setAttribute("delUserRoles", delUserRoles);
		%>
		<%
		 Integer isactive1=(Integer)session.getAttribute("isactive");
		%>
		<script>		
			var name="<%= username %>";
		</script>
		<script type="text/javascript" src="/egi/administration/rjbac/user/updateUser.js"></script>
		<script type="text/javascript" src="/egi/commonjs/calendar.js"></script>
		<script type="text/javascript" src="/egi/javascript/dateValidation.js"></script>

	</head>

	<body>
		
		<html:form action="SubmitUser?bool=UPDATE" 	onsubmit="return (validation() && validateUserForm(this));" method="POST" enctype="multipart/form-data">
			<table align="center" class="tableStyle" width="100%">
				<tr>
					<td colspan='4'>
						&nbsp;
					</td>
				</tr>
				<tr>
					<td class="tableheader" valign="middle" align="center" colspan="2" height="26">
						Update User
					</td>
				</tr>
			</table>
			<br/>
			<fieldset style="border:1px solid gray;padding:5px;margin:5px">
				<legend><bean:message key="UserDetailLabel" /><bean:message key="UserDetailLabel.ll" /></legend>
				<table align="center" class="tableStyle" width="100%" >
					<tr>
						<td class="labelcell" height="23" style="padding-left:100px">
							<bean:message key="firstName" />
							<bean:message key="firstName.ll" />
							<font class="ErrorText">*</font>
						</td>
						<td class="labelcell" height="23">
							<html:text property="firstName" styleId="firstName" styleClass="ControlText" />
						</td>
					</tr>
					<tr>
						<td class="labelcell" height="23" style="padding-left:100px">
							<bean:message key="middleName" />
							<bean:message key="middleName.ll" />
						</td>
						<td class="labelcell" height="23">
							<html:text property="middleName" styleId="middleName" styleClass="ControlText" onchange="return trimText1(this,this.value);" />
						</td>
					</tr>
					<tr>
						<td class="labelcell" height="23" style="padding-left:100px">
							<bean:message key="lastName" />
							<bean:message key="lastName.ll" />
						</td>
						<td class="labelcell" height="23">
							<html:text property="lastName" styleId="lastName" styleClass="ControlText" onchange="return trimText1(this,this.value);" />
						</td>
					</tr>
					<tr>
						<td class="labelcell" height="23" style="padding-left:100px">
							From Date
							<font class="ErrorText">*</font>
						</td>
						<td class="labelcellforsingletd" height="23">
							<html:text property="fromDate" styleId="fromDate" styleClass="ControlText" onkeyup="DateFormat(this,this.value,event,false,'3')" onblur="validateDateFormat(this);"/>
							<a href="javascript:show_calendar('userForm.fromDate');" >
								<img src="<c:url value='images/calendaricon.gif'/>" border="0" align="absmiddle"/> 
							</a>
						</td>
					</tr>
					<tr>
						<td class="labelcell" height="23" style="padding-left:100px">
							To Date
						</td>
						<td class="labelcellforsingletd" height="23">
							<html:text property="toDate" styleId="toDate" styleClass="ControlText" onkeyup="DateFormat(this,this.value,event,false,'3')" onblur="validateDateFormat(this);"/>
							<a href="javascript:show_calendar('userForm.toDate');" >
								<img src="<c:url value='images/calendaricon.gif'/>" border="0" align="absmiddle"/>
							</a>
						</td>
					</tr>
					<tr>
						<td class="labelcell" height="23" style="padding-left:100px">
							<bean:message key="salute" />
							<bean:message key="salute.ll" />[Mr/Mrs/Miss]
						</td>
						<td class="labelcell" height="23">
							<html:text property="salutation" styleId="salutation" styleClass="ControlText" onchange="return trimText1(this,this.value);" />
						</td>
					</tr>
					<tr>
						<td class="labelcell" height="23" style="padding-left:100px">
							<bean:message key="dob" />
							<bean:message key="dob.ll" />
						</td>
						<td class="labelcellforsingletd" height="23">
							<html:text property="dob" styleId="dob" styleClass="ControlText" onkeyup="DateFormat(this,this.value,event,false,'3')" onblur="validateDateFormat(this);"/>
							<a href="javascript:show_calendar('userForm.dob');" >
								<img src="<c:url value='images/calendaricon.gif'/>" border="0" align="absmiddle"/>
							</a>
						</td>	
					</tr>
					<tr>
						<td class="labelcell" height="23" style="padding-left:100px">
							<bean:message key="userName" />
							<bean:message key="userName.ll" />
							<font class="ErrorText">*</font>
						</td>
						<td class="labelcell" height="23">
							<html:text styleId="userName" property="userName" styleClass="ControlText" onchange="return callAction();" />
						</td>
					</tr>
					<tr>
						<td class="labelcell" height="23" style="padding-left:100px">
							<bean:message key="pwd" />
							<bean:message key="pwd.ll" />
							<font class="ErrorText">*</font>
						</td>
						<td class="labelcell" height="23">
							<html:password property="pwd" styleId="pwd" styleClass="ControlText" onchange="return trimText1(this,this.value);" />
						</td>
					</tr>
					<tr>
						<td class="labelcellforsingletd" height="23" style="padding-left:100px">
							<bean:message key="rPwd" />
							<bean:message key="rPwd.ll" />
							<font class="ErrorText">*</font>
						</td>
						<td class="labelcell" height="23">
							<input type="password" id="retypePwd" class="ControlText" onblur="return validatePwdReminder();" value="" />
						</td>
					</tr>
					<tr>
						<td class="labelcellforsingletd" height="23" style="padding-left:100px">
							Password Reminder
							<font class="ErrorText">*</font>
						</td>
						<td class="labelcell" align="left" width="40%" height="23">
							<html:password property="pwdReminder" styleId="pwdReminder" styleClass="ControlText" />
						</td>
					</tr>
					<tr>
						<td class="labelcell" height="23" style="padding-left:100px">
							<bean:message key="isActive" />
							<bean:message key="isActive.ll" />
						</td>
						<td class="labelcell" height="23">
							<input type="checkbox" name="isActiveValue" id="isActiveValue" value="ON" <%= (isactive1==1) ? "checked='checked'" : "" %>/>
							<input type="hidden" name="isActive" id="isActive" />
						</td>
					</tr>
					<tr>
						<td class="labelcell"  height="23" style="padding-left:100px">
							Signature
						</td>
						<td class="labelcell" oncontextmenu="return false" onmousedown="return false;">
							<img src="/egi/common/imageRenderer!getUserSignature.action?id=<%=user.getId()%>" height="50" width="150" alt="No User Signature" onerror="this.parentNode.removeChild(this);"/> <br/>
							Upload new signature
							<html:file property="file" size="50" styleId="file" />
						</td>
					</tr>
				</table>
			</fieldset>
			<br/>
			<br/>
			<script>
				document.getElementById('retypePwd').value = document.getElementById('pwd').value
			</script>
			<%
			Set roleset = new HashSet();
			roleset= user.getUserRoles();
			if(request.getAttribute("roleObj1")!=null) {
				roleset = (Set)request.getAttribute("roleObj1");
			}
			%>
			<fieldset style="border:1px solid gray;padding:5px;margin:5px">
				<legend>User Roles</legend>					
				<table border="1" width="100%" align="center" class="tableStyle">
					<tr>
						<td class="tablesubcaption" valign="bottom" height="20">
							Modify
						</td>
						<td class="tablesubcaption" valign="bottom" align="left" width="23%" height="20">
							Select User Role
						</td>
						<td valign="bottom" class="tablesubcaption" align="left" width="23%" height="20">
							&nbsp;From Date
						</td>
						<td valign="bottom" class="tablesubcaption" align="left" width="23%" height="20">
							&nbsp;To Date
						</td>
						<td class="button2" valign="bottom" class="tablesubcaption" align="center" width="23%" height="20">
							<input type="button" name="addRow" value="Add Role" onclick="addRole();" />	
						</td>
					</tr>
				</table>
				<table border="1" width="100%" id="reason_table" align="center" class="tableStyle" style='font-size:12px'>
					<% if(roleset.size()!=0){
				 		for (Iterator iter = roleset.iterator(); iter.hasNext();){
						UserRole userrole = (UserRole)iter.next();
						Role selectedrole = userrole.getRole();
						String id = selectedrole.getId().toString();
						%>
					<tr id="reasonrow">
						<td  valign="middle" height="20" width="52px">
							<input type="checkbox" name="modifyRole" id="modifyRole" value="ON" onclick="validate(this)"/>
							<input type="hidden" name="selCheck" id="selCheck"/>
						</td>
						<td  valign="bottom" align="left" width="160px" height="20">
							<html:hidden property="userRoleId" value="<%= userrole.getId().toString()%>" />
							<html:hidden property="selRoleID" value="<%= id%>" />
							<html:hidden property="exisFromDate" value="<%= formatter.format(userrole.getFromDate())%>" />
							<%
							String date = "";
							if(userrole.getToDate()!=null)
								date=formatter.format(userrole.getToDate());
							%>
							<html:hidden property="exisToDate" value="<%=date%>" />
							<html:select property="roleId" styleId="roleId" disabled="true" styleClass="ControlText" onchange="setSelRoleID(this)">
								<html:option value="<%= id %>"><%=selectedrole.getRoleName()%></html:option>
								<html:option value="0">--Choose--</html:option>
								<%
									List roleList = (ArrayList) request.getSession().getAttribute("RoleList");
									for(Iterator itr =roleList.iterator();itr.hasNext();)
									{
										Role role = (Role)itr.next();
										String roleId = role.getId().toString();
								%>
								<html:option value="<%=roleId %>"><%=role.getRoleName()%></html:option>
								<%
									}
								%>

							</html:select>
						</td>
						<td  valign="bottom" align="left" width="23%" height="20">
							<html:text property="fromDate1" styleId="fromDate1" disabled="true" value="<%=formatter.format(userrole.getFromDate())%>" size="19" onkeyup="DateFormat(this,this.value,event,false,'3')" onblur="validateDateFormat(this);"/>
						</td>

						
						<td  valign="bottom" align="left" width="23%" height="20">
							<html:text property="toDate1" value="<%=date%>" disabled="true" styleId="toDate1" size="19" onkeyup="DateFormat(this,this.value,event,false,'3')" onblur="validateDateFormat(this);"/>
						</td>

						<td class="button2" valign="bottom" align="center" width="23%" height="20">
							&nbsp;<input type=button name="delRow" value="Delete Role" onclick="deleteRole(this)" />
						</td>

					</tr>
					<% }
					} else if(roleset.size()==0) {

					%>
					<tr id="reasonrow">
						<html:hidden property="userRoleId" value="0" />
						<td class="labelcell" valign="middle" align="left" width="52px" height="20">
							<input type="checkbox" name="modifyRole" id="modifyRole" value="ON" onclick="validate(this)" />
							<input type="hidden" name="selCheck" id="selCheck" />
							<html:hidden property="selRoleID" />
						</td>						
						<td valign="bottom" align="left" width="23%" height="20">
							<select name="roleId" id="roleId" class="ControlText" onchange="setSelRoleID(this)">
								<option value="0">--Choose--</option>
								<%
									List roleList = (ArrayList) request.getSession().getAttribute("RoleList");
									for(Iterator itr =roleList.iterator();itr.hasNext();) {
										Role role = (Role)itr.next();
								%>
								
								<option value="<%=role.getId()%>"><%=role.getRoleName()%></option>
								<%
									}
								%>
							</select>
						</td>

						<td valign="bottom" align="left" width="160px" height="20">
							<b><input type="text" name="fromDate1" id="fromDate1" size="19" onkeyup="DateFormat(this,this.value,event,false,'3')" onblur="validateDateFormat(this);"/></b>
						</td>
						<td valign="bottom" align="left" width="23%" height="20">
							<b><input type="text" name="toDate1" id="toDate1" size="19" onkeyup="DateFormat(this,this.value,event,false,'3')" onblur="validateDateFormat(this);"/></b>
						</td>
						<td valign="bottom" class="button2" align="left" width="23%" height="20">
							&nbsp;<input type="button" name="delRow" value="Delete Row" onclick="deleteRole(this)" />
						</td>
					</tr>
					<% } %>
				</table>							
				</fieldset>
				<br/>
				<br/>
				<table align="center" class="tableStyle" width="100%">
					<tr>
						<td  valign="bottom" align="center" height="20">
							<br/>
							<html:submit value="  Save  " />
						</td>
					</tr>
					<tr>
						<td valign="bottom" align="left" 	height="20">
							<font class="ErrorText"><bean:message key="note" />
								<bean:message key="note.ll" /><br/>
								All date values should be in dd/MM/yyyy format eg: 31/12/2100
							</font>
						</td>
					</tr>
				</table>
		</html:form>
	</body>
	</html>
