<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld" %>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<head>
	<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/contra.js"></script>
	<script type="text/javascript" src="/EGF/commonjs/ajaxCommonFunctions.js"></script>
	<STYLE type="text/css">
	@media print
    {
        #non-printable { display: none; }
    }
	</STYLE>

<script>
var typeOfAcc="RECEIPTS_PAYMENTS,PAYMENTS";
function updateBalance(){
	if(document.getElementById('rBalance')!=null && document.getElementById('rBalance') != 'undefined'){
		document.getElementById('rBalance').value = document.getElementById('rBalance').value;
	}
}

var callback = {
		success: function(o){
			document.getElementById('results').innerHTML=o.responseText;
			updateBalance();
			},
			failure: function(o) {
		    }
		}
function getData(){
	var asOnDate =  document.getElementById('asOnDate').value;
	var bankAccount = document.getElementById('accountNumber').value;
	var currentReceiptsAmount = document.getElementById('currentReceiptsAmount').value;
	if(currentReceiptsAmount == '')
		currentReceiptsAmount = 0.0;
	isValid = validateData();
	if(isValid == false)
		return false;
	var url = '/EGF/payment/concurrenceReport!ajaxLoadPaymentHeader.action?skipPrepare=true&bankAccount.id='+bankAccount+'&asOnDate='+asOnDate+'&currentReceiptsAmount='+currentReceiptsAmount;
	YAHOO.util.Connect.asyncRequest('POST', url, callback, null);
}
function loadBank(fund){
	populatebank({fundId:fund.options[fund.selectedIndex].value})	
}

function validateData(){
	var bankAccount = document.getElementById('accountNumber').value;
	var bank = document.getElementById('bank').value;
	if(bank == -1){
		alert("Please select a Bank")
		return false;
	}
	if(bankAccount == -1){
		alert("Please select a Bank Account")
		return false;
	}
	var asOnDate =  Date.parse(document.getElementById('asOnDate').value);
	if(isNaN(asOnDate)){
		alert("Please enter a valid date")
		return false;
	}
	return true;	
}

function computeBalance(index){
	checkBox = document.getElementById('chbox_'+index);
	paymentAmount = document.getElementById('netPayable'+index).value;
	runningBalance = document.getElementById('rBalance').value;
	if(isNaN(paymentAmount)){
		paymentAmount = 0;
	}
	if(isNaN(runningBalance)){
		runningBalance = 0;
	}
	if(checkBox.checked){
		document.getElementById('rBalance').value = (parseInt(runningBalance) - parseInt(paymentAmount)).toFixed(2);
	}
	else{
		document.getElementById('rBalance').value = (parseInt(runningBalance) + parseInt(paymentAmount)).toFixed(2);
	}
}

function validateBank(){
	var bank = document.getElementById('bank').value;
	if(bank == -1){
		alert("Please select a Bank")
		return false;
	}
	return true;
}
function populateAccNumbers(bankBranch){
	id = bankBranch.options[bankBranch.selectedIndex].value.split("-")[1];
	bankid=bankBranch.options[bankBranch.selectedIndex].value.split("-")[0];
	//var asOnDate =  document.getElementById('asOnDate').value;
	populateaccountNumber({branchId:id,bankId:bankid,typeOfAccount:typeOfAcc})	
}
var balanceCallback = {
		success: function(o){
			document.getElementById('resultGrid').innerHTML=o.responseText;
			value = document.getElementById('balanceResult').value
			if(isNaN(value)){
				value = 0;
			}
			document.getElementById('availableBalance').value = parseInt(value).toFixed(2);
			},
			failure: function(o) {
		    }
		}
function onLoadTask(){
	var asOnDate = document.getElementById('asOnDate').value;
	
	populatebank({typeOfAccount:typeOfAcc})
}
</script>
</head>
<body onload="onLoadTask()">
<div class="formmainbox">
<div class="formheading"></div>
<div class="subheadnew">Concurrence Report</div>


<s:form action="concurrenceReport" theme="simple" name="concurrenceReport">
<table width="100%" cellpadding="0" cellspacing="0" border="0">
	<tr>
		<td width="20%">&nbsp;</td>
		<egov:ajaxdropdown id="bank" fields="['Text','Value']" dropdownId="bank" url="/voucher/common!ajaxLoadBanksByFundAndType.action" />
	    <td class="bluebox" width="10%">Bank Name:<span class="bluebox"><span class="mandatory">*</span></span></td>
	    <td class="bluebox">
	    	<s:select name="bank" id="bank" list="dropdownData.bankList" listKey="bankBranchId" listValue="bankBranchName" headerKey="-1" headerValue="----Choose----" onChange="populateAccNumbers(this);"  />
	    </td>
	    <egov:ajaxdropdown id="accountNumber" fields="['Text','Value']" dropdownId="accountNumber" url="voucher/common!ajaxLoadAccNumAndType.action" />
		<td class="bluebox" width="10%">Account Number:<span class="bluebox"><span class="mandatory">*</span></span></td>
		<td class="bluebox">
			<s:select  name="bankAccount" id="accountNumber" list="dropdownData.accNumList" listKey="id" listValue="accountnumber" headerKey="-1" headerValue="----Choose----" onclick="validateBank()"/>
		</td>
	</tr>
	<tr>
		<td class="greybox" width="20%">&nbsp;</td>
		<td class="greybox" width="10%">As On Date:<span class="mandatory">*</span></td>
		<td class="greybox">
			<s:textfield name="asOnDate" id="asOnDate" cssStyle="width:100px" value='%{getFormattedAsOnDate()}' onchange="fetchAvailableBalance(this);" onkeyup="DateFormat(this,this.value,event,false,'3')"/><a href="javascript:show_calendar('concurrenceReport.asOnDate');" style="text-decoration:none">&nbsp;<img src="${pageContext.request.contextPath}/image/calendaricon.gif" border="0"/></a>(dd/mm/yyyy)<br/>
		</td>
		<td class="greybox" width="10%">Current Receipts:</td>
		<td class="greybox">
			<input type="text" name="currentReceiptsAmount" style="text-align: right;" id="currentReceiptsAmount" value='<s:text name="payment.format.number">
					<s:param name="value" value="currentReceiptsAmount"/></s:text>'/>
		</td>
	</tr>
	
</table>
<br/><br/>
<div class="buttonbottom" id="non-printable">
  <input type="button" value="Submit" class="buttonsubmit" onclick="return getData()"/>
  &nbsp;
	<s:reset name="button" type="submit" cssClass="button" id="button" value="Cancel"/>
	<s:submit value="Close" onclick="javascript: self.close()" cssClass="button"/>
</div>
</div>
</s:form>

<div id="results">
</div>
<div id="resultGrid">
</div>

</body>
</html>
