<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld" %>

<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td colspan="5">
			<div class="headingsmallbg">
				<s:text name="notice156.title"></s:text>
			</div>
		</td>
	</tr>
	<s:hidden name="warrantNotice.recovery.id" value="%{id}"></s:hidden>
	<tr>
		<td colspan="5">
			 <table width="100%" border="0" cellspacing="0" cellpadding="0">
			<tr><td class="bluebox" width="25%"><s:text name="warrant.notice.return.date" /><span class="mandatory1">*</span></td>
			    <td class="bluebox" width="25%"><s:date name="warrantNotice.warrantReturnByDate" id="warrantReturnByDateId" format="dd/MM/yyyy"  />
				<s:textfield name="warrantNotice.warrantReturnByDate" id="warrantReturnByDate" value="%{warrantReturnByDateId}"  maxlength="10" onkeyup="DateFormat(this,this.value,event,false,'3')" onblur="validateDateFormat(this);" size="10"/>
				<a href="javascript:show_calendar('recoveryForm.warrantReturnByDate',null,null,'DD/MM/YYYY');" style="text-decoration:none">&nbsp;<img src="${pageContext.request.contextPath}/image/calendaricon.gif" border="0"/></a>
			</td>
			
			<td class="bluebox" width="25%"><s:text name="remarks"/></td>
			<td class="bluebox" width="25%"><s:textarea name="warrantNotice.remarks" id="remarks" cols="40" rows="2"  onblur="checkLength1024(this)" ></s:textarea></td>
			
			</tr>
			<tr>
			
			<td class="bluebox" width="25%"><s:text name="warrant.ccList"/></td>
			<td class="bluebox" width="25%"><s:textfield name="warrantNotice.cclist" id="cclist"  onblur="checkLength1024(this)" /></td>
		     <td class="bluebox" width="25%">
		      	&nbsp;
		      </td>
		       <td class="bluebox" width="25%" >
		      	&nbsp;
		      </td>
			</tr>
			
		</table>
		</td>
	</tr>

</table>
<script>

</script>