<%@ include file="/includes/taglibs.jsp" %>

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title><s:text name="cancelreceipt.title"/></title>
<script>
function warningInfo()
{
	dom.get("cancellationreasonerror").style.display="none";
	if(trimAll(document.getElementById("reasonForCancellation").value).length==0 || trimAll(document.getElementById("reasonForCancellation").value)=="")
	{
		dom.get("cancellationreasonerror").style.display="block";
		return false;
	}
	else
	{
		document.cancelChallanReceiptForm.action="challan!saveOnCancel.action";
		document.cancelChallanReceiptForm.submit();
	}
}

</script>
</head>
<span align="center" style="display:none" id="cancellationreasonerror">
  <li>
     <font size="2" color="red"><b><s:text name="cancellationreason.error"/></b></font>
  </li>
</span>
<body >
<s:form theme="simple" name="cancelChallanReceiptForm" action="challan">
<s:push value="model">
<div class="formmainbox">
	<div class="subheadnew"><s:text name="cancelreceipt.title"/></div>
	
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
			<s:hidden label="receiptId" id="receiptId" name="receiptId" value="%{model.id}"/>
			<tr>
				<td width="4%" class="bluebox2">&nbsp;</td>
				<td width="21%" class="bluebox2"><s:text name="viewReceipt.receiptno"/></td>
				<td width="24%" class="bluebox2"><b><s:property value="receiptnumber" /></b></td>
				<td width="21%" class="bluebox2"><s:text name="viewReceipt.receiptdate"/></td>
				<td width="30%" class="bluebox2"><b><s:date name="receiptDate" format="dd/MM/yyyy"/></b></td>
			</tr>
			<tr><td colspan="5">
			<%@ include file='challandetails.jsp'%>
			</td></tr>
			<tr>
					<table cellspacing="0" cellpadding="0" align="center" width="100%" class="tablebottom">
						<tr>
							<td width="4%" class="bluebox2">&nbsp;</td>
							<td width="21%" class="bluebox2"><b><s:text name="viewReceipt.amtinwords"/></b></td>
							<td width="75%" class="bluebox2" colspan="7"><u><b><s:property value="amountInWords(totalAmount)" /> </b></u></td>
						</tr>
						<tr>
							<td width="4%" class="bluebox">&nbsp;</td>
							<td width="21%" class="bluebox"><s:text name="viewReceipt.payee.name"/></td>
							<td width="75%" class="bluebox" colspan="7"><s:property value="receiptPayeeDetails.payeename" /></td>
						</tr>

						<s:iterator value='%{getInstruments("cash")}' >

						<tr>
							<td width="4%" class="bluebox2">&nbsp;</td>
							<td width="21%" class="bluebox2"><s:text name="viewReceipt.cashReceived"/></td>
							<fmt:formatNumber var="totalRecievedAmount" value='${amount}' pattern='#0.00' />
							<td width="75%" class="bluebox2" colspan="7">${totalRecievedAmount}</td>
						</tr>

						</s:iterator>

						<s:iterator value='%{getInstruments("cheque")}' >
						<tr>
							<td width="4%" class="bluebox2">&nbsp;</td>
							<td width="15%" class="bluebox2"><s:text name="viewReceipt.chequedate"/></td>
							<td width="21%" class="bluebox2"><s:property value="%{instrumentNumber}"/>&nbsp;-&nbsp;<s:date name="%{instrumentDate}" format="dd/MM/yyyy" /></td>
							<td width="10%" class="bluebox2"><s:text name="viewReceipt.chequeamount"/></td>
							<fmt:formatNumber var="chequeAmount" value='${instrumentAmount}' pattern='#0.00' />
							<td width="15%" class="bluebox2">${chequeAmount}</td>
							<td width="10%" class="bluebox2"><s:text name="viewReceipt.bankbranch"/></td>
							<td width="25%" class="bluebox2"><s:property value="%{bankId.name}"/>&nbsp;-&nbsp;<s:property value="%{bankBranchName}" /></td>
						</tr>
						</s:iterator>

						<s:iterator value='%{getInstruments("dd")}' >
						<tr>
							<td width="4%" class="bluebox2">&nbsp;</td>
							<td width="21%" class="bluebox2"><s:text name="viewReceipt.dddate"/></td>
							<td width="15%" class="bluebox2"><s:property value="%{instrumentNumber}"/>&nbsp;-&nbsp;<s:date name="%{instrumentDate}" format="dd/MM/yyyy" /></td>
							<td width="10%" class="bluebox2"><s:text name="viewReceipt.ddamount"/></td>
							<fmt:formatNumber var="chequeAmount" value='${instrumentAmount}' pattern='#0.00' />
							<td width="15%" class="bluebox2">${chequeAmount}</td>
							<td width="10%" class="bluebox2"><s:text name="viewReceipt.bankbranch"/></td>
							<td width="25%" class="bluebox2"><s:property value="%{bankId.name}"/>
							<s:if test="bankBranchName!=null">
							&nbsp;-&nbsp;<s:property value="%{bankBranchName}" />
							</s:if>
							</td>
						</tr>
						
						</s:iterator>

						<s:iterator value='%{getInstruments("card")}' >
						<tr>
							<td width="4%" class="bluebox2">&nbsp;</td>
							<td width="21%" class="bluebox2"><s:text name="viewReceipt.creditcardno"/></td>
							<td width="24%" class="bluebox2" colspan="3"><s:property value="%{instrumentNumber}" /></td>
						</tr>
						</s:iterator>

						
						<tr>
							<td width="4%" class="bluebox2">&nbsp;</td>
							<td width="21%" class="bluebox2"><s:text name="billreceipt.reasonforcancellation"/><span class="mandatory">*</span></td>
							<td width="24%" class="bluebox2" colspan="7"><s:textarea id="reasonForCancellation" label="reasonForCancellation" cols="90" rows="8" name="reasonForCancellation" value="%{reasonForCancellation}" /></td>
						</tr>
					</table>
			</tr>	
			
			
		</table>

		<br/>
		<div align="left" class="mandatorycoll"><s:text name="common.mandatoryfields"/></div>
		<div class="buttonbottom">
		<input name="button32" type="button" class="buttonsubmit" id="button32"  value="Cancel Receipt" onclick="return warningInfo()"/>
		<input name="button2" type="button" class="button" id="button" onclick="window.close()" value="Close"/>
		<input name="buttonBack" type="button" class="button" id="buttonBack" value="Back" onclick="history.back()" /> 
		</div>
</div>
</s:push>
</s:form>
</body>
</html>
