<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ include file="/includes/taglibs.jsp" %>
	<fieldset id="internal">
	<legend align="left"><b><s:text name="lbl.frwdngdtl"/></b></legend>	
	<s:radio  name="forwardUserType" id="forwardUserType" list="#{'INTERNAL':'Internal','EXTERNAL':'External'}" onclick="displayUserDetails(this.value)" />
	<div id="internalUserDetails">
	<table border="0" cellpadding="4" cellspacing="0" width="100%">
		<tbody>
			<tr class="graybox">
				<td>
					<s:text name="lbl.dept"/><span class="mandatory">*</span>
				</td>
				<td>
					<s:select headerValue="%{getText('sel.dept')}" headerKey="" name="internalUser.department" id="internalUser.department"  
					list="dropdownData.departmentList" listValue="deptName" listKey="id"  theme="simple" 
					onchange="loadInternalUserDesignation(this)" tabindex="1"/>
					<egovtags:ajaxdropdown id="internalUserdesignationDropdown"  fields="['Text','Value']" dropdownId='internalUserdesignation' 
					url='dms/fileManagement!populateDesignation.action' selectedValue="%{internalUser.designation.designationId}"/>

				</td>
				<td>
					<s:text name="lbl.recvrdesig"/><span class="mandatory">*</span>
				</td>
				<td >
					<s:select name="internalUser.designation" tabindex="1" id="internalUserdesignation" list="designationList" listKey="designationId" listValue='designationName' headerKey="-1" headerValue="%{getText('sel.desig')}"  value="internalUser.designation.designationId" onchange="loadInternalUsers(this,'internalUser.department')" />
					<egovtags:ajaxdropdown id="internalUserpositionDropdown"  fields="['Text','Value']" dropdownId='internalUserposition' url='dms/fileManagement!populateUsersByDeptAndDesig.action' selectedValue="%{internalUser.position.id}"/>

				</td>

			</tr>
	
			<tr class="whitebox">
				<td>
					<s:text name="lbl.recvr"/><span class="mandatory">*</span>
				</td>
				<td>
					<s:select name="internalUser.position" tabindex="1" id="internalUserposition" list="empInfoList" listKey="position.id" listValue='employeeName' headerKey="-1" headerValue="%{getText('sel.receiver')}"  value="internalUser.position.id" />
				</td>
				<td>
					<s:text name="lbl.date"/>
				</td>
				<td>
					<input name="sysDate" type="text"  size="20" readonly="readonly"  value="<s:property value='currentDate' />" tabindex="1" />
				</td>
			</tr>
			<tr class="graybox">
				<td><s:text name="lbl.cmmts"/></td>
				<td colspan="3"><s:textarea tabindex="1" name="workflowComment" cols="20" rows="2" /></td>
			</tr>
		</tbody>
	</table>
	</div>
	<div id="externalUserDetails">
	<table border="0" cellpadding="4" cellspacing="0" width="100%">
			<tbody>
				<tr class="graybox">
					<td>
						<s:text name="lbl.sentto"/><span class="mandatory">*</span>
					</td>
					<td>
						<s:select headerValue="%{getText('sel.src')}" headerKey="" name="externalUser.userSource" id="externalUser.userSource"  list="dropdownData.fileSourceList" listValue="name" listKey="id"  theme="simple"  tabindex="1" value="externalUser.userSource.id"/>
					</td>
					<td>
						<s:text name="lbl.nameofrcvr"/><span class="mandatory">*</span>
					</td>
					<td>
						<s:textfield name="externalUser.userName" id="externalUser.userName" type="text" tabindex="1"/>
					</td>							
				</tr>

				<tr class="whitebox">
					<td>
						<s:text name="lbl.addr"/>
					</td>
					<td>
						<s:textarea name="externalUser.userAddress" cols="20" rows="2" tabindex="1"></s:textarea>
					</td>
					<td >
						<s:text name="lbl.phno"/>
					</td>
					<td>
						<s:textfield name="externalUser.userPhNumber"  tabindex="1"/>
					</td>
				</tr>

				<tr class="graybox">							
					<td>
						<s:text name="lbl.email"/>
					</td>
					<td>
						<s:textfield name="externalUser.userEmailId" tabindex="1"/>
					</td>
					<td>
						<s:text name="lbl.outboundfileno"/>
					</td>
					<td>
						<s:textfield name="externalUser.outboundFileNumber" tabindex="1"/>
					</td>
				</tr>
				<tr class="whitebox">
					<td><s:text name="lbl.cmmts"/></td>
					<td colspan="3"><s:textarea tabindex="1" name="workflowComment" cols="20" rows="2" /></td>
				</tr>
			</tbody>
	</table>
	</div>
	</fieldset>
	
	<script>
		function displayUserDetails(userType) {
			document.getElementById('forwardUserType'+userType).checked = true;
			if (userType == "INTERNAL") {
				document.getElementById("internalUserDetails").style.display = "block";
				document.getElementById("externalUserDetails").style.display = "none";
			} else {
				document.getElementById("externalUserDetails").style.display = "block";
				document.getElementById("internalUserDetails").style.display = "none";
			}
		}
		displayUserDetails('${forwardUserType}');
	</script>
	
