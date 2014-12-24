<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld" %>

<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td colspan="5">
			<div class="headingsmallbg">
				<s:text name="recoveryDet"></s:text>
			</div>
		</td>
	</tr>
	<tr>
		<td colspan="5">
			 <table width="100%" border="0" cellspacing="0" cellpadding="0">
			<tr><td class="bluebox" width="25%"><s:text name="payment.due.date" /><span class="mandatory1">*</span></td>
			    <td class="bluebox" width="25%"><s:date name="intimationNotice.paymentDueDate" id="paymentDueDateId" format="dd/MM/yyyy"  />
				<s:textfield name="intimationNotice.paymentDueDate" id="paymentDueDate" value="%{paymentDueDateId}"  maxlength="10" onkeyup="DateFormat(this,this.value,event,false,'3')" onblur="validateDateFormat(this);" size="10"/>
				<a href="javascript:show_calendar('recoveryForm.paymentDueDate',null,null,'DD/MM/YYYY');" style="text-decoration:none">&nbsp;<img src="${pageContext.request.contextPath}/image/calendaricon.gif" border="0"/></a>
			</td>
			
			<td class="bluebox" width="25%"><s:text name="remarks"/></td>
			<td class="bluebox" width="25%"><s:textarea name="intimationNotice.remarks" id="remarks" cols="40" rows="2"  onblur="checkLength1024(this)" ></s:textarea></td>
			
			</tr>
			
		</table>
		</td>
	</tr>

</table>
<script>

</script>