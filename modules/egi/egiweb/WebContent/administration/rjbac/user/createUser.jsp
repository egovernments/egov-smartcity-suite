<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="java.util.*,
		org.egov.lib.admbndry.*,
		org.egov.lib.rjbac.dept.*,
		org.egov.lib.rjbac.role.*"%>
<%@ include file="/includes/taglibs.jsp"%>
<%
	//Sets the HierarchyTypeName in session
	String hrchyTypeName = "ADMINISTRATION" ;
	session.setAttribute("hrchyTypeName",hrchyTypeName);
%>
	
<html>
	<head>
		<title>Create User</title>
		<html:javascript formName="userForm" />
		<script type="text/javascript" src="/egi/administration/rjbac/user/createUser.js"></script>
		<script type="text/javascript" src="/egi/commonjs/calendar.js"></script>
		<script type="text/javascript" src="/egi/javascript/dateValidation.js"></script>
	</head>
	<body onload="bodyonload();">
		<html:form action="SubmitUser?bool=CREATE" 	onsubmit=" return (validate() && validateUserForm(this));" method="POST" enctype="multipart/form-data">
			<html:hidden property="bndryType" />
			
			<table align="center" class="tableStyle" width="100%">
				<tr>
					<td colspan=4>
						&nbsp;
					</td>
				</tr>
				<tr>
					<td class="tableheader" valign="middle" align="center" colspan="2" height="26">
						<bean:message key="CreateUserlabel" />
						<bean:message key="CreateUserlabel.ll" />
					</td>
				</tr>
			</table>
			<br/>
			<fieldset style="border:1px solid gray;padding:5px;margin:5px">
			<legend><bean:message key="UserDetailLabel" /><bean:message key="UserDetailLabel.ll" /></legend>
			<table align="center" class="tableStyle" width="100%" >
				<tr>
					<td class="labelcell"  height="23" style="padding-left:150px">
						<bean:message key="firstName" />
						<bean:message key="firstName.ll" />
						<font class="ErrorText">*</font>
					</td>
					<td class="labelcell" height="23">
						<html:text property="firstName" styleId="firstName" styleClass="ControlText" />
					</td>
				</tr>
				<tr>
					<td class="labelcell" height="23" style="padding-left:150px">
						<bean:message key="middleName" />
						<bean:message key="middleName.ll" />
					</td>
					<td class="labelcell" height="23">
						<html:text property="middleName" styleId="middleName" styleClass="ControlText" onchange="return trimText1(this,this.value);" />
					</td>
				</tr>
				<tr>
					<td class="labelcell" height="23" style="padding-left:150px">
						<bean:message key="lastName" />
						<bean:message key="lastName.ll" />
					</td>
					<td class="labelcell" height="23">
						<html:text property="lastName" styleId="lastName" styleClass="ControlText" onchange="return trimText1(this,this.value);" />
					</td>
				</tr>

				<tr>
					<td class="labelcell" height="23" style="padding-left:150px">
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
					<td class="labelcell" height="23" style="padding-left:150px">
						To Date
						<font class="ErrorText"></font>
					</td>
					<td class="labelcellforsingletd" height="23">
						<html:text property="toDate" styleId="toDate" styleClass="ControlText" onkeyup="DateFormat(this,this.value,event,false,'3')" onblur="validateDateFormat(this);"/>
						<a href="javascript:show_calendar('userForm.toDate');" >
							<img src="<c:url value='images/calendaricon.gif'/>" border="0" align="absmiddle"/>
						</a>
					</td>
				</tr>

				<tr>
					<td class="labelcell" height="23" style="padding-left:150px">
						<bean:message key="salute" />
						<bean:message key="salute.ll" />[Mr/Mrs/Miss]
					</td>
					<td class="labelcell" height="23">
						<html:text property="salutation" styleId="salutation" styleClass="ControlText" onchange="return trimText1(this,this.value);" />
					</td>
				</tr>


				<tr>
					<td class="labelcell" height="23" style="padding-left:150px">
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
					<td class="labelcell" height="23" style="padding-left:150px">
						<bean:message key="userName" />
						<bean:message key="userName.ll" />
						<font class="ErrorText">*</font>
					<td class="labelcell"  height="23">
						<html:text styleId="userName" property="userName" styleClass="ControlText" onblur="uniqueChecking('${pageContext.request.contextPath}/commonyui/egov/uniqueCheckAjax.jsp' , 'EG_USER', 'USER_NAME', 'userName', 'no', 'no');" />
					</td>
				</tr>
				<tr>
					<td class="labelcell" height="23" style="padding-left:150px">
						<bean:message key="pwd" />
						<bean:message key="pwd.ll" />
						<font class="ErrorText">*</font>
					<td class="labelcell" height="23">
						<html:password property="pwd" styleId="pwd" styleClass="ControlText" onchange="return trimText1(this,this.value);" />
					</td>
				</tr>
				<tr>
					<td class="labelcellforsingletd" height="23" style="padding-left:150px">
						<bean:message key="rPwd" />
						<bean:message key="rPwd.ll" />
						<font class="ErrorText">*</font>
					</td>
					<td class="labelcell"  height="23">
						<input type="password" id="retypePwd" class="ControlText" onblur="return validatePwd();" />
					</td>
				</tr>
				<tr>
					<td class="labelcellforsingletd" height="23" style="padding-left:150px">
						Password Reminder
						<font class="ErrorText">*</font>
					</td>
					<td class="labelcell" align="left" width="40%" height="23">
						<html:password property="pwdReminder" styleId="pwdReminder" styleClass="ControlText" />
					</td>
				</tr>
				<tr>
					<td class="labelcell" height="23" style="padding-left:150px">
						<bean:message key="isActive" />
						<bean:message key="isActive.ll" />
					</td>
					<td class="labelcell" align="left" width="40%" height="23">
						<input type="checkbox" name="isActiveValue" id="isActiveValue" value="ON" checked="checked"/>
						<input type="hidden" name="isActive" id="isActive" />
					</td>
				</tr>
				
				<tr>
					<td class="labelcell" height="23" style="padding-left:150px">
						Upload Signature
					</td>
					<td class="labelcell" align="left" width="40%" height="23">
						<html:file property="file" size="50" styleId="file"/>
					</td>
				</tr>
			</table>
			</fieldset>
				
			<fieldset style="border:1px solid gray;padding:5px;margin:5px">
				<legend>User Roles</legend>			
				<table border="1" width="100%" align="center" class="tableStyle">
					<tr>
						<td class="tablesubcaption" valign="bottom" align="left" width="25%" height="20">
							Select User Role
						</td>
						<td class="tablesubcaption" valign="bottom" align="left" width="25%" height="20">
							&nbsp;From Date(dd/mm/yyyy)
						</td>
						<td class="tablesubcaption" valign="bottom" align="left" width="25%" height="20">
							&nbsp;To Date(dd/mm/yyyy)
						</td>
						<td class="button2" colspan="2" align="center">
							<input type=button  name="addRow" value="Add Role" onclick="addRole();" />
						</td>
					</tr>
				</table>
				<table width="100%" id="reason_table" class="tableStyle" border="1">
					<tr id="reasonrow">
						<td class="labelcell" valign="bottom" align="left" width="25%" height="20">
							<html:select styleId="roleId" property="roleId" styleClass="ControlText">
								<html:option value="0">--Choose--</html:option>
								<%
									List roleList = (ArrayList) request.getSession().getAttribute("RoleList");
									for(Iterator itr =roleList.iterator();itr.hasNext();) {
										Role role = (Role)itr.next();
										String roleId = role.getId().toString();
								%>
								<html:option value="<%=roleId %>"><%=role.getRoleName()%></html:option>
								<%
									}
								%>

							</html:select>
						</td>
						<td class="labelcell" valign="bottom" align="left" width="25%" height="20">
							<b><html:text property="fromDate1" styleId="fromDate1" size="19" onkeyup="DateFormat(this,this.value,event,false,'3')" onblur="validateDateFormat(this);"/></b>
						</td>
						<td class="labelcell" valign="bottom" align="left" width="25%" height="20">
							<b><html:text property="toDate1" styleId="toDate1" size="19" onkeyup="DateFormat(this,this.value,event,false,'3')" onblur="validateDateFormat(this);"/></b>
						</td>

						<td class="button2" valign="bottom" align="center" width="25%" height="20">
							<input type="button" name="delRow" value="Delete Role" onclick="deleteRole(this)" />
						</td>
					</tr>
				</table>
				</fieldset>
				<br/>
				<table align="center" class="tableStyle" width="100%">
					<tr>
						<td align="center" valign="bottom" height="20">
							<br/>
							<html:submit value="Create" style="font-size:12px"/>
						</td>
					</tr>
					<tr>
						<td valign="bottom" height="20">
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
