<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld" %>

<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td colspan="5">
			<div class="headingsmallbg">
				<s:text name="recordObjection.title"></s:text>
			</div>
		</td>
	</tr>
	<tr>
		<td colspan="5">
			 <table width="100%" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td class="bluebox">&nbsp;</td>
				<td class="bluebox" width="25%"><s:text name="objection.received.date" /><span class="mandatory1">*</span></td>
			    <td class="bluebox" width="25%"><s:date name="recievedOn" id="recievedOnId" format="dd/MM/yyyy"  />
				<s:textfield name="recievedOn" id="recievedOn" value="%{recievedOnId}"  maxlength="10" onkeyup="DateFormat(this,this.value,event,false,'3')" size="10"/>
				<a href="javascript:show_calendar('objectionViewForm.recievedOn',null,null,'DD/MM/YYYY');" style="text-decoration:none">&nbsp;<img src="${pageContext.request.contextPath}/image/calendaricon.gif" border="0"/></a>(dd/mm/yyyy)
				</td>
			
				<td class="bluebox" width="25%"><s:text name="objection.received.by"/><span class="mandatory1">*</span></td>
				<td class="bluebox" width="25%"><s:textfield name="recievedBy" id="recievedBy" onblur="chkReceivedByLen(this);"/></td>
			</tr>
			<tr>
				<td class="greybox">&nbsp;</td>
				<td class="greybox" width="25%"><s:text name="objection.details"/><span class="mandatory1">*</span></td>
				<td class="greybox" width="25%"><s:textarea name="details" id="details" cols="40" rows="2"  onblur="checkLength(this)" ></s:textarea></td>
			
				<td class="greybox" width="25%"><s:text name="objection.upload.document"/></td>
				<td class="greybox" width="25%"><input type="button" class="button" value="Upload Document" id="docUploadButton" onclick="showDocumentManager();" /></td>
				<s:hidden name="docNumberObjection" id="docNumber" />

			</tr>
		</table>
		</td>
	</tr>

</table>
<script>
function chkReceivedByLen(obj){
	if(obj.value.length>256)
	{
		alert('Max 256 characters are allowed for received by text. Remaining characters are truncated.')
		obj.value = obj.value.substring(1,256);
	}
}

</script>