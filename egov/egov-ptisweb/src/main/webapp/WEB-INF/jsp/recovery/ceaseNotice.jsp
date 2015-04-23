<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld" %>

<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td colspan="5">
			<div class="headingsmallbg">
				<s:text name="notice159.title"></s:text>
			</div>
		</td>
	</tr>
	<s:hidden name="ceaseNotice.recovery.id" value="%{id}"></s:hidden>
	<tr>
		<td colspan="5">
			 <table width="100%" border="0" cellspacing="0" cellpadding="0">
			<tr><td class="bluebox" width="25%"><s:text name="execution.date" /><span class="mandatory1">*</span></td>
			    <td class="bluebox" width="25%"><s:date name="ceaseNotice.executionDate" id="executionDateId" format="dd/MM/yyyy"  />
				<s:textfield name="ceaseNotice.executionDate" id="executionDate" value="%{executionDateId}"  maxlength="10" onkeyup="DateFormat(this,this.value,event,false,'3')" onblur="validateDateFormat(this);" size="10"/>
				<a href="javascript:show_calendar('recoveryForm.executionDate',null,null,'DD/MM/YYYY');" style="text-decoration:none">&nbsp;<img src="${pageContext.request.contextPath}/image/calendaricon.gif" border="0"/></a>
			</td>
			
			<td class="bluebox" width="25%"><s:text name="remarks"/></td>
			<td class="bluebox" width="25%"><s:textarea name="ceaseNotice.remarks" id="remarks" cols="40" rows="2"  onblur="checkLength1024(this)" ></s:textarea></td>
			
			</tr>
			
		</table>
		</td>
	</tr>

</table>
<script>

</script>