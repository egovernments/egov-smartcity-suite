<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-nested.tld" prefix="nested"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<html>
<head>
	<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/voucherHelper.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/contra.js"></script>
	<script type="text/javascript" src="/EGF/commonjs/ajaxCommonFunctions.js"></script>
	<meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
	<title>Contra - Cash Withdrawal</title>
</head>

	
<body onload="onloadtask();">
<s:form action="contraBTC" theme="simple" name="cashWithDrawalForm" >
	<jsp:include page="../budget/budgetHeader.jsp">
      		<jsp:param name="heading" value="Cash Withdrawal" />
	</jsp:include>
	<div class="formmainbox">
		<div class="formheading"/>
		<div class="subheadnew">Cash Withdrawal</div>
		<div id="listid" style="display:block"><br/></div>
		<div align="center">
			<font  style='color: red ;'> 
				<p class="error-block" id="lblError" ></p>
			</font>
			<span class="mandatory">
				<s:actionerror/>  
				<s:fielderror />
				<s:actionmessage />
			</span>
		</div>
			<table border="0" width="100%">
		<tr>
		<s:if test="%{shouldShowHeaderField('vouchernumber')}">
			<td class="bluebox" width="22%"><s:text name="voucher.number"/><span class="mandatory">*</span></td>
			<td class="bluebox" width="22%">
			<table width="100%">
			<tr>
			<td style="width:25%"><input type="text" name="voucherNumberPrefix" id="voucherNumberPrefix" readonly="true"  style="width:100%"/></td> 
			<td style="width:75%"><s:textfield name="voucherNumber" id="voucherNumber" /></td>
			</tr>
			</table>
			</td>
			
			</s:if>
		<s:else>
			<td class="bluebox"><s:text name="payin.number"/><span class="mandatory">*</span></td>
			<td class="bluebox"><s:textfield name="voucherNumber" id="voucherNumber" readonly="true" /></td>
		</s:else>	
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
	
		<div class="buttonbottom" style="padding-bottom:10px;"> 
			<s:submit type="submit" cssClass="buttonsubmit" value="Save" method="edit" onclick="return validateInput()"/>
			<s:reset name="button" type="submit" cssClass="button" id="button" value="Cancel"/>
			<s:submit value="Close" onclick="javascript: self.close()" cssClass="button"/>
		</div>

	<input type="hidden" id="voucherHeader.id" name="voucherHeader.id" value='<s:property value="voucherHeader.id"/>'/>
	<s:hidden id="bankBalanceMandatory" name="bankBalanceMandatory" value="%{isBankBalanceMandatory()}"/>
	</div>
</s:form>
<div id="resultGrid"></div>
<script>

String.prototype.trim = function () {
    return this.replace(/^\s*/, "").replace(/\s*$/, "");
}
function onloadtask(){
	document.getElementById('fundId').disabled =true;
			   <s:if test="%{shouldShowHeaderField('vouchernumber')}">
			   var tempVoucherNumber='<s:property value="voucherHeader.voucherNumber"/>';
			   var prefixLength='<s:property value="voucherNumberPrefixLength"/>';
			   document.getElementById('voucherNumberPrefix').value=tempVoucherNumber.substring(0,prefixLength);
			   document.getElementById('voucherNumber').value=tempVoucherNumber.substring(prefixLength,tempVoucherNumber.length);
			</s:if>
	var currentTime = new Date()
	var month = currentTime.getMonth() + 1
	var day = currentTime.getDate()
	var year = currentTime.getFullYear()
	if(document.getElementById('voucherDate').value == ''){
		document.getElementById('voucherDate').value = day + "/" + month + "/" + year ;
	}
	if(document.getElementById('chequeDate').value == ''){
		document.getElementById('chequeDate').value = day + "/" + month + "/" + year ;
	}
	var accnum =  document.getElementById('accountNumber');
	if(accnum.value != -1){
		populateAvailableBalance(accnum)
		populateNarration(accnum)
	}
}
function validateInput(){
		document.getElementById('fundId').enabled =true;
		document.getElementById('lblError').innerHTML = "";
		
		if(document.getElementById('bankId').value == -1){
		document.getElementById('lblError').innerHTML = "Please select a bank ";
		return false;
		}
		if(document.getElementById('voucherDate').value.trim().length == 0){
		document.getElementById('lblError').innerHTML = "Please enter  voucher date ";
		return false;
		}
		if(document.getElementById('accountNumber').value == -1 ){
		document.getElementById('lblError').innerHTML = "Please select an account number ";
		return false;
		}
		if(document.getElementById('amount').value.trim().length == 0 || document.getElementById('amount').value.trim() == 0){
		document.getElementById('lblError').innerHTML = "Please enter an amount greater than zero";
		return false;
		}
		if(document.getElementById('chequeNumber') && document.getElementById('chequeNumber').value == ''){
		document.getElementById('lblError').innerHTML = "Please enter cheque number";
		return false;
		}
		if(document.getElementById('chequeDate').value == ''){
		document.getElementById('lblError').innerHTML = "Please enter cheque date";
		return false;
		}
		var validMis = validateMIS();
		if(validMis == true){
			document.getElementById('fundId').disabled =false;
			//return true;
		}
		else{
			return false;
		}
		if(parseFloat(document.getElementById('amount').value)>parseFloat(document.getElementById('availableBalance').value))
		{
			if(document.getElementById('bankBalanceMandatory').value=='true')
			{
				alert('There is no sufficient bank balance');
				return false;
			}
			else
			{
				if(confirm('There is no sufficient bank balance. Do you want to continue?'))
				{
					return true;
				}
				else
				{
					return false;
				}
			}
		}
		return true;
		
}
function validateAmountFormat(){
	var amount = document.getElementById('amount').value.trim();
}
if('<s:text name="%{isBankBalanceMandatory()}"/>'=='')
		document.getElementById('lblError').innerHTML = "bank_balance_mandatory parameter is not defined";
</script>

</body>

</html>