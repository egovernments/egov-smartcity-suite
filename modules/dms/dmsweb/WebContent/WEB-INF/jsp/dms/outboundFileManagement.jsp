<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ include file="/includes/taglibs.jsp" %>
<jsp:include page="fileManagementHeader.jsp"/> 
<s:form action="outboundFileManagement" method="POST" enctype="multipart/form-data" theme="simple"  id="fileForm">
	<s:token name="%{tokenName()}"/>
	<s:push value="model">
		<jsp:include page="fileManagementDetail.jsp"/>
		<s:if test="%{id ==null}">
		<fieldset id="outbound">
		<legend align="left"><b><s:text name="lbl.filemovdtl"/></b></legend>
		<fieldset>
		<legend align="left"><b><s:text name="lbl.recvrdtl"/></b></legend>
		<table border="0" cellpadding="4" cellspacing="0" width="100%">
			<tbody>
				<tr class="graybox">
					<td>
						<s:text name="lbl.sentto"/><span class="mandatory">*</span>
					</td>
					<td>
						<s:select headerValue="%{getText('sel.src'}" headerKey="" name="receiver.userSource" id="sentTo"  list="dropdownData.fileSourceList" listValue="name" listKey="id"  theme="simple"  tabindex="1" value="receiver.userSource.id"/>
					</td>
					<td>
						<s:text name="lbl.nameofrcvr"/><span class="mandatory">*</span>
					</td>
					<td>
						<s:textfield name="receiver.userName" tabindex="1" id="receiverName" maxlength="100"/>
					</td>							
				</tr>

				<tr class="whitebox">
					<td>
						<s:text name="lbl.addr"/>
					</td>
					<td>
						<s:textarea name="receiver.userAddress" cols="20" rows="2" tabindex="1"></s:textarea>
					</td>
					<td >
						<s:text name="lbl.phno"/>
					</td>
					<td>
						<s:textfield name="receiver.userPhNumber" tabindex="1"/>
					</td>
				</tr>

				<tr class="graybox">							
					<td>
						<s:text name="lbl.email"/>
					</td>
					<td >
						<s:textfield name="receiver.userEmailId" tabindex="1"/>
					</td>
					<td>
						<s:text name="lbl.outboundfileno"/>
					</td>
					<td >
						<s:textfield name="receiver.outboundFileNumber" tabindex="1"/>
					</td>
				</tr>
			</tbody>
		</table>
		</fieldset>
		</fieldset>
		</s:if>
		<s:else>
		<jsp:include page="fileManagementHistory.jsp"/>
		<jsp:include page="outbound-senderReceiver.jsp"/>
		<jsp:include page="fileForwardDetails.jsp"/>		
		<br/>
		</s:else>
		<table border="0" width="100%">
			<tr align="center" class="graybox">
				<td>
				<s:hidden name="isDraftEdit" />
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
document.getElementById('fileType').innerHTML = "Outbound";
</script>
