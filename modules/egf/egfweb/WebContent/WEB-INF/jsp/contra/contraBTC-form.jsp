	<table border="0" width="100%">
		<tr>
		 <s:if test="%{shouldShowHeaderField('vouchernumber') || showMode.equalsIgnoreCase('reverse')}">
			<td class="greybox" width="30%"><s:text name="voucher.number"/><span class="mandatory">*</span></td>
			<td class="greybox"><s:textfield name="voucherHeader.voucherNumber" id="voucherNumber" maxlength="25"/></td>
		</s:if>
		  <td class="greybox" width="30%"><s:text name="voucher.date"/><span class="mandatory">*</span></td>
		  <td class="greybox">
		  	<input type="text"  id="voucherDate" name="voucherHeader.voucherDate" style="width:100px" value='<s:date name="voucherDate" format="dd/MM/yyyy"/>'/>
		  	<a href="javascript:show_calendar('cashWithDrawalForm.voucherDate',null,null,'DD/MM/YYYY');" style="text-decoration:none">&nbsp;<img tabIndex=-1 src="${pageContext.request.contextPath}/image/calendaricon.gif" border="0"/></a>(dd/mm/yyyy)
		  </td>
	</tr>
	<tr>
		  <td class="bluebox" width="30%"><s:text name="payin.bank"/>
		  <span class="bluebox"><span class="mandatory">*</span></span></td>
		  <td class="bluebox"><s:select name="contraBean.bankBranchId" id="bankId" list="dropdownData.bankList" listKey="bankBranchId" listValue="bankBranchName" headerKey="-1" headerValue="----Choose----" onChange="populateAccNum(this);"  /></td>
	 	<td class="bluebox" width="30%"><s:text name="contra.amount"/><span class="mandatory">*</span></td>
		<td class="bluebox"><s:textfield  name="contraBean.amount" id="amount"  onkeyup="validateAmountFormat()" cssStyle="text-align:right"/></td>
	
	</tr>
	<tr>
		 <egov:ajaxdropdown id="accountNumber" fields="['Text','Value']" dropdownId="accountNumber" url="voucher/common!ajaxLoadAccNum.action" />
		<td class="greybox"><s:text name="payin.accountNum"/><span class="bluebox"><span class="mandatory">*</span></span></td>
		<td class="greybox"><s:select  name="contraBean.accountNumberId" id="accountNumber" list="dropdownData.accNumList" listKey="id" listValue="accountnumber" headerKey="-1" headerValue="----Choose----" onChange="populateNarration(this);populateAvailableBalance(this);" />
		<s:textfield name="contraBean.accnumnar" id="accnumnar" value="%{contraBean.accnumnar}"/></td>
		<td class="greybox"><s:text name="balance.available"/></td>
		<td class="greybox"><input type="text" id="availableBalance" readonly="readonly" style="text-align:right"/></td>
	</tr>
	<tr>
		  <td class="bluebox"><s:text name="cheque.date"/><span class="mandatory">*</span></td>
		  <td class="bluebox"><input type="text"  id="chequeDate" name="contraBean.chequeDate" style="width:100px" value='<s:property value="contraBean.chequeDate"/>'/>
		  <a href="javascript:show_calendar('cashWithDrawalForm.chequeDate',null,null,'DD/MM/YYYY');" style="text-decoration:none">&nbsp;<img tabIndex=-1 src="${pageContext.request.contextPath}/image/calendaricon.gif" border="0"/></a>(dd/mm/yyyy)
		 </td>
		<s:if test="%{showChequeNumber()}">
		  <td class="bluebox"><s:text name="cheque.number"/><span class="greybox"><span class="mandatory">*</span></span></td>
		  <td class="bluebox"><s:textfield name="contraBean.chequeNumber" id="chequeNumber" maxlength="25"/>
		</s:if>
	</tr>
		<jsp:include page="../voucher/vouchertrans-filter.jsp"/>
	<tr>
		<td class="greybox">Narration &nbsp;</td>
		<td class="greybox"><s:textarea rows="4" cols="60" name="narration" onkeydown="textCounter('narration',250)" onkeyup="textCounter('narration',250)" onblur="textCounter('narration',250)" id="narration"/></td>
      	<td class="greybox"><s:text name="contra.cashInHand"/></td>
		<td class="greybox"><s:textfield name="contraBean.cashInHand" id="cashInHand" readonly="true" /></td>
	</tr>
	</table>