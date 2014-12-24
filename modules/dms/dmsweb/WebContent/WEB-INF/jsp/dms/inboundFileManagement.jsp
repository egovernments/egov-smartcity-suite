<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ include file="/includes/taglibs.jsp" %>
<jsp:include page="fileManagementHeader.jsp"/>
<s:form action="inboundFileManagement" method="POST" enctype="multipart/form-data" theme="simple" id="fileForm">
<s:token name="%{tokenName()}"/>
<s:push value="model">
	<jsp:include page="fileManagementDetail.jsp"/>
	<s:if test="%{id ==null}">
	<fieldset id="inbound">
	<legend align="left" ><b><s:text name="lbl.filemovdtl"/></b></legend>	
	<fieldset>
	<legend align="left"><b><s:text name="lbl.senderdtl"/></b></legend>	
	<table border="0" cellpadding="4" cellspacing="0" width="100%">
		<tbody>
			<tr class="graybox">
				<td>
					<s:text name="lbl.sentfrm"/><span class="mandatory">*</span>
				</td>
				<td>
					<s:select headerValue="%{getText('sel.src')}" headerKey="" name="sender.userSource" id="sentFrom"  list="dropdownData.fileSourceList" listValue="name" listKey="id"  theme="simple"  tabindex="1" value="sender.userSource.id"/>
				</td>
				<td>
					<s:text name="lbl.nameofsndr"/><span class="mandatory">*</span>
				</td>
				<td>
					<s:textfield name="sender.userName" id="sender.userName" tabindex="1" maxlength="100"/>
				</td>
			</tr>

			<tr class="whitebox">
				<td>
					<s:text name="lbl.fileaddrto"/>
				</td>
				<td >
					<s:textfield name="sender.addressedTo" tabindex="1" />
				</td>							
			<td>
				<s:text name="lbl.addr"/>
			</td>
			<td>
				<s:textarea name="sender.userAddress" cols="20" rows="2"  tabindex="1"></s:textarea>
				</td> 
			</tr>

			<tr class="graybox">
				<td >
					<s:text name="lbl.phno"/>
				</td>
				<td>
					<s:textfield name="sender.userPhNumber" tabindex="1" />
				</td>
				<td>
					<s:text name="lbl.email"/>
				</td>
				<td>
					<s:textfield name="sender.userEmailId" tabindex="1" />
				</td>
			</tr>
		</tbody>
	</table>
	</fieldset>
	<fieldset>
	<legend align="left"><b><s:text name="lbl.frwrdto"/></b></legend>	
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
					<s:select name="receiver.designation" tabindex="1"  id="receiverdesignation" list="designationList" listKey="designationId" listValue='designationName' headerKey="-1" headerValue="%{getText('sel.desig')}"  value="receiver.designation.designationId" onchange="loadUsers(this,'receiver.department')" />
					<egovtags:ajaxdropdown id="receiverpositionDropdown"  fields="['Text','Value']" dropdownId='receiverposition' url='dms/fileManagement!populateUsersByDeptAndDesig.action' selectedValue="%{receiver.position.id}"/>

				</td>
			</tr>

			<tr class="whitebox">
				<td>
					<s:text name="lbl.recvr"/><span class="mandatory">*</span>
				</td>
				<td>
					<s:select name="receiver.position" tabindex="1"  id="receiverposition" list="empInfoList" listKey="position.id" listValue='employeeName' headerKey="-1" headerValue="%{getText('sel.receiver')}"  value="receiver.position.id" />
				
				</td>
				<td>
					<s:text name="lbl.date"/>
				</td>
				<td >
					<input name="sysDate" size="20" readonly="readonly" type="text" value="<s:property value='currentDate' />" tabindex="1">
				</td>
			</tr>
		</tbody>
	</table>
	</fieldset>
	</fieldset>	
	<br/>
	</s:if>
	<s:else>
	<jsp:include page="inbound-senderReceiver.jsp"/>
	<jsp:include page="fileManagementHistory.jsp"/>
	<jsp:include page="fileForwardDetails.jsp"/>	
	</s:else>
	<table border="0" width="100%">
		<tr align="center" class="graybox">
			<td>
				<s:hidden name="isDraftEdit"/>
				<s:submit method="saveAsDraft"  value="%{getText('lbl.saveasdrft')}" id="drftbtn" tabindex="1" onclick="showWaiting();;return checkBeforeSubmitFMS(true)"/>&nbsp;&nbsp;&nbsp;
				<s:if test="%{id ==null}">
				<s:submit method="createFile"  value="%{getText('lbl.submit')}" id="savebtn" tabindex="1" onclick="showWaiting();return checkBeforeSubmitFMS(false)"/>&nbsp;&nbsp;&nbsp;
				</s:if>
				<s:else>
				<s:submit method="saveFile"  value="%{getText('lbl.submit')}" id="savebtn" tabindex="1" onclick="showWaiting();return checkBeforeSubmitFMS(false)"/>&nbsp;&nbsp;&nbsp;
				</s:else>
				
				<input type="reset" value="<s:text name="lbl.reset"/>" tabindex="1">&nbsp;&nbsp;&nbsp;
				<input type="button" value="<s:text name="lbl.close"/>" tabindex="1" onclick="window.close()">
			</td>
		</tr>
	</table>
</s:push>
</s:form>
<jsp:include page="fileManagementFooter.jsp"/>
<script>
document.getElementById('fileType').innerHTML = "Inbound";
</script>
