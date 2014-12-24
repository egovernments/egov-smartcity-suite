<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="egovtags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<title> Access Permissions</title>
		<script type="text/css">
		
		</script>
	

  </head>
  
  
  <body  onload="disableFields();">
  <center>
		
  			<s:if test="hasActionMessages() && !hasFieldErrors() && !hasActionErrors()">
				<table border="0">
					<tr>
						
						<td valign="bottom" style="padding-top:14px;">
							<font color="navy" size="2" ><s:actionmessage/></font>
						</td>
					</tr>
				</table>		
			</s:if>
			<s:if test="hasFieldErrors() || hasActionErrors()">
				<table border="0">
					<tr>						
						<td valign="bottom" style="padding-top:14px;">
							<font color="red" size="2"><s:actionerror/><s:fielderror /></font>
						</td>
					</tr>
				</table>		
			</s:if>
  
		
  <s:form theme="simple">
		<div class="wfadmin" >
			<div class="wfadminheading" > 
				Access Permission Configuration
			</div>
			<div id="search">
		<table class="searchtbl" border="0" cellpadding="0" cellspacing="0" width="100%">


						<tr class="whitebox">
						<td  style="width:10%;text-align: left" >
							<s:checkbox id="userChk" name="userChk" onclick="disableUserInfo();"/>&nbsp;&nbsp;&nbsp;&nbsp;User</td>
							
						<td  style="width:30%" class="whitebox">	
				
						<div id="userName_autocomplete" class="autoComContainer" style="width:45%" >
							<div><s:textfield id="userName" name="userName" /><s:hidden name="userId" id="userId"/>
							 </div>
							<span id="userNameResults"></span>
						</div>
						<egovtags:autocomplete name="userName" width="20" maxResults="8"
						field="userName"
						url="${pageContext.request.contextPath}/acl/accessPermissions!ajaxUserNames.action"
						queryQuestionMark="true" results="userNameResults" handler="userNameSelectionHandler" 
						forceSelectionHandler="userNameSelectionEnforceHandler"/>
						<span class='warning' id="improperuserNameSelectionWarning"></span>						
						
					</td>	
					
					<td  style="width:10%" >
					<button id="addUser"  name="addUser" type="button" onclick="addToUserList()" style="float:left"><img src="../images/add.png" title="Add Users"  align="absmiddle"> </button>
					</td>
				
					
					<td  style="width:10%" >
					<s:select id="user" name="userIdList" list="userList" listKey="id" listValue="userName" multiple="true" size="5" tabindex="1" style="min-width:130px;height:50px" />
					</td>	
					<td  style="width:10%">
					<button type="button" id="removeUser" name="removeUser" onclick="removeFromUserList()" style="float:left"><img src="../images/delete.png" title="Remove Users"  align="absmiddle"> </button>
					</td> 				
											 
						
						</tr>
						
						<tr class="graybox">
						<td  style="width:10%;text-align: left">
							<s:checkbox name="roleChk" id="roleChk"  onclick="disableRoleInfo();"/>&nbsp;&nbsp;&nbsp;&nbsp;Role	</td>
							
						<td style="width:30%">	
				
						<div id="roleName_autocomplete" style="width:70%" >
							<div><s:textfield id="roleName" name="roleName" /><s:hidden name="roleId" id="roleId"/>
							 </div>
							<span id="roleNameResults"></span>
						</div>
						<egovtags:autocomplete name="roleName" width="20" maxResults="8"
						field="roleName"
						url="${pageContext.request.contextPath}/acl/accessPermissions!ajaxRoleNames.action"
						queryQuestionMark="true" results="roleNameResults" handler="roleNameSelectionHandler" 
						forceSelectionHandler="roleNameSelectionEnforceHandler"/>
						<span class='warning' id="improperroleNameSelectionWarning"></span>						
						
					</td>	
					
					<td style="width:10%" >
					<button type="button" id="addRole" name="addRole" onclick="addToRoleList()" style="float:left"><img src="../images/add.png" title="Add Roless"  align="absmiddle"> </button>
					</td>
				
					
					<td  style="width:10%" >
					<s:select id="role" name="roleIdList" list="roleList" listKey="id" listValue="roleName"  multiple="true" size="5" tabindex="1" style="min-width:130px;height:50px" />
					</td>	
					<td  style="width:10%">
					<button type="button" id="removeRole" name="removeRole" id="removeRole" onclick="removeFromRoleList()" style="float:left"><img src="../images/delete.png" title="Delete Roless"  align="absmiddle"> </button>
					</td> 				
											 
						
						</tr>
						<tr class="whitebox">
						<td  style="width:10%;text-align: left">
							<s:checkbox name="empChk" id="empChk"  onclick="disableEmpInfo();"/>&nbsp;&nbsp;&nbsp;&nbsp;Employee</td>
							
						<td  style="width:30%">	
				
						<div id="empName_autocomplete" style="width:70%" >
							<div><s:textfield id="empName" name="empName" /><s:hidden name="empId" id="empId"/>
							 </div>
							<span id="empNameResults"></span>
						</div>
						<egovtags:autocomplete name="empName" width="20" maxResults="8"
						field="empName"
						url="${pageContext.request.contextPath}/acl/accessPermissions!ajaxEmpNames.action"
						queryQuestionMark="true" results="empNameResults" handler="empNameSelectionHandler" 
						forceSelectionHandler="empNameSelectionEnforceHandler"/>
						<span class='warning' id="improperempNameSelectionWarning"></span>						
						
					</td>	
					
					<td  style="width:10%" >
					<button type="button" onclick="addToEmpList()" id="addEmp" name="addEmp" style="float:left"><img src="../images/add.png" title="Add Employees"  align="absmiddle"> </button>
					</td>
				
					
					<td  style="width:10%" >
					<s:select id="emp" name="empIdList" list="empList" listKey="id" listValue="employeeName"  multiple="true" size="5" tabindex="1" style="min-width:130px;height:50px" />
					</td>	
					<td  style="width:10%">
					<button type="button" id="removeEmp" name="removeEmp" onclick="removeFromEmpList()" style="float:left"><img src="../images/delete.png" title="Delete Employees"  align="absmiddle"> </button>
					</td> 				
											 
						
						</tr>
						
						<tr class="graybox">
						<td  style="width:10%;text-align: left">
							<s:checkbox id="groupChk" name="groupChk" onclick="disableGroupInfo();"/>&nbsp;&nbsp;&nbsp;&nbsp;Group</td>
							
						<td  style="width:30%">	
				
						<s:select name="groupSelect" id="groupSelect" list="dropdownData.groupList" listKey="id" listValue="groupName" multiple="true"
						 headerKey="-1" headerValue="--Select Group--" style="min-width:130px;height:50px" />					
						
					</td>	
					
					<td style="width:10%" >
					<button type="button" id="addGroup" name="addGroup" onclick="addToGroupList()" style="float:left"><img src="../images/add.png" title="Add Groups"  align="absmiddle"> </button>
					</td>
									
					<td  style="width:10%" >
					<s:select id="group" name="groupIdList" list="groupList"  listKey="id" listValue="groupName" multiple="true" size="5" tabindex="1" style="min-width:130px;height:50px" />
					</td>	
					<td style="width:10%">
					<button type="button" id="removeGroup" name="removeGroup" onclick="removeFromGroupList()" style="float:left"><img src="../images/delete.png" title="Delete Groups"  align="absmiddle"> </button>
					</td> 				
											 
						
						</tr>
						<tr class="whitebox">
						
						<td   style="width:10%;text-align: left" >
							<s:checkbox name="workflowChk" id="workflowChk"  />&nbsp;&nbsp;&nbsp;&nbsp;WorkflowUsers</td>
						<td  style="width:10%" colspan="4" />
						</tr>
		</table>	
		<table border="0" width="100%" class="graybox">
		<tr align="center" >
			<td>
				<s:hidden name="objectId" />
				<s:hidden name="objectClass" />
				<s:if test="!hasActionMessages()">
						<s:submit method="saveOrUpdate" cssClass="buttonfinal1" value="Save"  tabindex="1" onclick="selectAll();return  validateFields();" />&nbsp;&nbsp;&nbsp;
					
				<input type="reset" value="Reset" class="buttonfinal1" tabindex="1">&nbsp;&nbsp;&nbsp;
				<!--<s:submit method="save"  value="Save & Close"  tabindex="1" onclick="selectAll();" />&nbsp;&nbsp;&nbsp;-->
				</s:if>
				<input type="button" value="Close" class="buttonfinal1" tabindex="1" onclick="window.close();">
			</td>
		</tr>
	</table>			
  </s:form>
  </body>
</html>




