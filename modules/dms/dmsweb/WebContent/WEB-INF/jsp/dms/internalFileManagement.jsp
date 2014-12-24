<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ include file="/includes/taglibs.jsp" %>
<jsp:include page="fileManagementHeader.jsp"/>
<s:form action="internalFileManagement" method="POST" enctype="multipart/form-data" theme="simple"  id="fileForm">
<s:token name="%{tokenName()}"/>
<s:push value="model">
	<jsp:include page="fileManagementDetail.jsp"/>
	<s:if test="%{id ==null}">
	<fieldset id="internal">
	<legend align="left"><b><s:text name="lbl.filemovdtl"/></b></legend>	
	<fieldset>
	<legend align="left"><b><s:text name="lbl.senderdtl"/></b></legend>	
	<table border="0" cellpadding="4" cellspacing="0" width="100%">
		<tbody>
			<tr class="graybox">
				<td>
					<s:text name="lbl.senderdept"/><span class="mandatory">*</span>
				</td>
				<td>
					<s:select headerValue="%{getText('sel.dept')}" headerKey="" name="sender.department" id="sender.department"  list="dropdownData.departmentList" 
					listValue="deptName" listKey="id"  theme="simple" onchange="loadSenderDesignation(this)" tabindex="1" value="sender.department.id"/>
					<egovtags:ajaxdropdown id="senderdesignationDropdown"  fields="['Text','Value']" dropdownId='senderdesignation' url='dms/fileManagement!populateDesignation.action' selectedValue="%{receiver.designation.designationId}"/>
				</td>
				<td>
					<s:text name="lbl.senderdesig"/><span class="mandatory">*</span>
				</td>
				<td>
					<s:select name="sender.designation" tabindex="1" id="senderdesignation" list="senderDesignationList" listKey="designationId" listValue='designationName' headerKey="-1" headerValue="%{getText('sel.desig')}"  value="sender.designation.designationId" onchange="loadSenderUsers(this,'sender.department')" />
					<egovtags:ajaxdropdown id="senderpositionDropdown"  fields="['Text','Value']" dropdownId='senderposition' url='dms/fileManagement!populateUsersByDeptAndDesig.action' selectedValue="%{sender.position.id}"/>

				</td>
			</tr>
	
			<tr class="whitebox">
				<td>
					<s:text name="lbl.sender"/><span class="mandatory">*</span>
				</td>
				<td>
					<s:select name="sender.position" tabindex="1" id="senderposition" list="senderEmpInfoList" listKey="position.id" listValue='employeeName' headerKey="-1" headerValue="%{getText('sel.sender')}"  value="sender.position.id" />
				
				</td>
				<td>
					<s:text name="lbl.date"/>
				</td>
				<td>
					<input name="sysDate" size="20" readonly="readonly" type="text" value="<s:property value='currentDate' />" tabindex="1">
				</td>
			</tr>
		</tbody>
	</table>
	</fieldset>
	<fieldset>
	<legend align="left"><b><s:text name="lbl.recvrdtl"/></b></legend>	
	<table border="0" cellpadding="4" cellspacing="0" width="100%">
		<tbody>
			<tr class="graybox">
				<td>
					<s:text name="lbl.recvrdept"/><span class="mandatory">*</span> 
				</td>
				<td>
					<s:select headerValue="%{getText('sel.dept')}" headerKey="" name="receiver.department" id="receiver.department"  list="dropdownData.departmentList" listValue="deptName" listKey="id"  theme="simple" onchange="loadDesignation(this)" tabindex="1" value="receiver.department.id"/>
					<egovtags:ajaxdropdown id="receiverdesignationDropdown"  fields="['Text','Value']" dropdownId='receiverdesignation' url='dms/fileManagement!populateDesignation.action' selectedValue="%{receiver.designation.designationId}"/>

				</td>
				<td>
					<s:text name="lbl.recvrdesig"/><span class="mandatory">*</span>
				</td>
				<td >
					<s:select name="receiver.designation" tabindex="1" id="receiverdesignation" list="designationList" listKey="designationId" listValue='designationName' headerKey="-1" headerValue="%{getText('sel.desig')}"  value="receiver.designation.designationId" onchange="loadUsers(this,'receiver.department')" />
					<egovtags:ajaxdropdown id="receiverpositionDropdown"  fields="['Text','Value']" dropdownId='receiverposition' url='dms/fileManagement!populateUsersByDeptAndDesig.action' selectedValue="%{receiver.position.id}"/>

				</td>
			</tr>

			<tr class="whitebox">
				<td>
					<s:text name="lbl.recvr"/><span class="mandatory">*</span>
				</td>
				<td>
					<s:select name="receiver.position" tabindex="1" id="receiverposition" list="empInfoList" listKey="position.id" listValue='employeeName' headerKey="-1" headerValue="%{getText('sel.receiver')}"  value="receiver.position.id" />

				</td>
				<td>
					<s:text name="lbl.date"/>
				</td>
				<td >
					<input name="sysDate" size="20" readonly="readonly" type="text" value="<s:property value='currentDate' />" tabindex="1"/>
				</td>
			</tr>
		</tbody>
	</table>
	</fieldset>
	</fieldset>
	</s:if>
	<s:else>
	<jsp:include page="internal-senderReceiver.jsp"/>
	<jsp:include page="fileManagementHistory.jsp"/>
	<jsp:include page="fileForwardDetails.jsp"/>
	</s:else>
	<br/>
	<table border="0" width="100%">
		<tr align="center" class="graybox">
			<td>
				<s:hidden name="isDraftEdit"/>
				<s:submit method="saveAsDraft"  value="%{getText('lbl.saveasdrft')}" id="drftbtn" tabindex="1" onclick="showWaiting();return checkBeforeSubmitFMS(true);"/>&nbsp;&nbsp;&nbsp;
				<s:if test="%{id ==null}">
				<s:submit method="createFile"  value="%{getText('lbl.submit')}" id="savebtn" tabindex="1" onclick="showWaiting();return checkBeforeSubmitFMS(false);"/>&nbsp;&nbsp;&nbsp;
				</s:if>
				<s:else>
				<s:submit method="saveFile"  value="%{getText('lbl.submit')}" id="savebtn" tabindex="1" onclick="showWaiting();return checkBeforeSubmitFMS(false);"/>&nbsp;&nbsp;&nbsp;
				</s:else>				
				<input type="reset" value="<s:text name='lbl.reset'/>" tabindex="1">&nbsp;&nbsp;&nbsp;
				<input type="button" value="<s:text name='lbl.close'/>" tabindex="1" onclick="window.close()">
			</td>
		</tr>
	</table>
</s:push>
</s:form>
<jsp:include page="fileManagementFooter.jsp"/>
<script>
document.getElementById('fileType').innerHTML = "Internal";
</script>
				
