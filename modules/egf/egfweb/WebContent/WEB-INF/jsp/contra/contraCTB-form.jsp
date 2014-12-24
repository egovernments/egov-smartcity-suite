<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
	<table border="0" width="100%">
	<tr>
		  <s:if test="%{shouldShowHeaderField('vouchernumber')}">
			<td class="bluebox"><s:text name="voucher.number"/><span class="mandatory">*</span></td>
			<td class="bluebox"><s:textfield name="voucherNumber" id="voucherNumber" /></td>
		  </s:if>
		  <td class="bluebox"><s:text name="voucher.date"/><span class="mandatory">*</span></td>
		  <td class="bluebox"><s:date name="voucherDate" id="voucherDateId" format="dd/MM/yyyy"/>
		  <s:textfield name="voucherDate" id="voucherDate" value="%{voucherDateId}"  maxlength="10" onkeyup="DateFormat(this,this.value,event,false,'3')"/>
		  <a href="javascript:show_calendar('cashDepositForm.voucherDate',null,null,'DD/MM/YYYY');" style="text-decoration:none">&nbsp;<img tabIndex=-1 src="${pageContext.request.contextPath}/image/calendaricon.gif" border="0"/></a>(dd/mm/yyyy)
		 </td>
	</tr>
	<tr>
		  <td class="greybox"><s:text name="payin.bank"/>
		  <span class="greybox"><span class="mandatory">*</span></span></td>
		  <td class="greybox" colspan="3"><s:select name="contraBean.bankBranchId" id="bankId" list="dropdownData.bankList" listKey="bankBranchId" listValue="bankBranchName" headerKey="-1" headerValue="----Choose----" onChange="populateAccNum(this);"  /></td>
	</tr>
	<tr>
		 <egov:ajaxdropdown id="accountNumber" fields="['Text','Value']" dropdownId="accountNumber" url="voucher/common!ajaxLoadAccNum.action" />
		<td class="bluebox"><s:text name="payin.accountNum"/>
		<span class="bluebox"><span class="mandatory">*</span></span></td>
		<td class="bluebox"><s:select  name="contraBean.accountNumberId" id="accountNumber" list="dropdownData.accNumList" listKey="id" listValue="accountnumber" headerKey="-1" headerValue="----Choose----" onChange="populateNarration(this);" />
		<s:textfield name="contraBean.accnumnar" id="accnumnar" value="%{contraBean.accnumnar}" readonly="true" tabindex="-1"/>
		</td>
		<td class="bluebox"><s:text name="contra.amount"/>
		<span class="bluebox"><span class="mandatory">*</span></span></td>
		<td class="bluebox"><s:textfield name="contraBean.amount" id="amount"  onkeyup="amountFormat()" cssStyle="text-align:right" /></td>
	</tr>
	
		<jsp:include page="../voucher/vouchertrans-filter.jsp"/>
	<tr id="chequeGrid" >
	<td class="greybox"><span id="mdcNumber"><s:text name="contra.refNumber" /></span>
		<span class="greybox"><span class="mandatory">*</span></span></td>
		<td class="greybox" ><s:textfield  name="contraBean.chequeNumber" id="documentNum"  value="%{contraBean.chequeNumber}"/>	</td>
		<td class="greybox" ><span id="mdcDate"><s:text name="contra.refDate" /></span>
		<span class="greybox"><span class="mandatory">*</span></span></td>
			<td class="greybox"  ><s:textfield name="contraBean.chequeDate" id="documentDate" onkeyup="DateFormat(this,this.value,event,false,'3')"/>
					<a href="javascript:show_calendar('cashDepositForm.documentDate');" style="text-decoration:none">&nbsp;<img tabIndex="-1" src="${pageContext.request.contextPath}/image/calendaricon.gif" border="0"/></a>(dd/mm/yyyy)</td>
				
	</tr>		
		<tr>
			<td class="greybox"><s:text name="voucher.narration" /></td>
			<td class="greybox" colspan="3"><s:textarea  id="narration" name="description" style="width:580px" onblur="checkVoucherNarrationLen(this)"/></td>
		</tr>
	</table>