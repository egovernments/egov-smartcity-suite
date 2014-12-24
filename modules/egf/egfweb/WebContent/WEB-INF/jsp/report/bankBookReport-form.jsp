<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld" %>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<head>
	<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/contra.js"></script>
	<script type="text/javascript" src="/EGF/commonjs/ajaxCommonFunctions.js"></script>

<script>
var callback = {
		success: function(o){
			document.getElementById('results').innerHTML=o.responseText;
			},
			failure: function(o) {
		    }
		}
function getData(){
	var startDate =  document.getElementById('startDate').value;
	var endDate =  document.getElementById('endDate').value;
	var bankAccount = document.getElementById('accountNumber').value;
	isValid = validateData();
	if(isValid == false)
		return false;
	var url = '/EGF/report/bankBookReport!ajaxLoadBankBook.action?skipPrepare=true&bankAccount.id='+bankAccount+'&startDate='+startDate+'&endDate='+endDate+getMiscData();
	YAHOO.util.Connect.asyncRequest('POST', url, callback, null);
}

function getMiscData(){
	var fund,department,functionary,field,scheme,subscheme,data="";
	fund = document.getElementById('fundId').value;
	department = document.getElementById('vouchermis.departmentid').value;
	if(fund != undefined)
		data = data+"&fundId.id="+fund;
	if(department != undefined)
		data = data+"&vouchermis.departmentid.id="+department;
	if(functionary != undefined)
		data = data+"&vouchermis.functionary.id="+functionary;
	if(field != undefined)
		data = data+"&vouchermis.division.id="+field;
	if(scheme != undefined)
		data = data+"&vouchermis.schemeid.id="+scheme;
	if(subscheme != undefined)
		data = data+"&vouchermis.subschemeid.id="+subscheme;
	return data;
}
function loadBankForFund(fund){
	populatebank({fundId:fund.options[fund.selectedIndex].value});	
}
function loadBank(fund){}
function populateAccNumbers(bankBranch){
	var fund = document.getElementById('fund');
	id = bankBranch.options[bankBranch.selectedIndex].value.split("-")[1]
	populateaccountNumber({branchId:id,fundId:fund.options[fund.selectedIndex].value})	
}

var exportCallback = {
		success: function(o){
			},
			failure: function(o) {
		    }
		}
function exportXls(){
	var bankAccount = document.getElementById('accountNumber').value;
	var startDate = document.getElementById('startDate').value;
	var endDate = document.getElementById('endDate').value;
	window.open('/EGF/report/bankBookReport!exportXls.action?skipPrepare=true&bankAccount.id='+bankAccount+'&startDate='+startDate+'&endDate='+endDate,'','resizable=yes,height=650,width=900,scrollbars=yes,left=30,top=30,status=no');
}

function exportPdf(){
	var bankAccount = document.getElementById('accountNumber').value;
	var startDate = document.getElementById('startDate').value;
	var endDate = document.getElementById('endDate').value;
	window.open('/EGF/report/bankBookReport!exportPdf.action?skipPrepare=true&bankAccount.id='+bankAccount+'&startDate='+startDate+'&endDate='+endDate,'','resizable=yes,height=650,width=900,scrollbars=yes,left=30,top=30,status=no');
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
	var startDate =  Date.parse(document.getElementById('startDate').value);
	if(isNaN(startDate)){
		alert("Please enter a valid start date")
		return false;
	}
	var endDate =  Date.parse(document.getElementById('endDate').value);
	if(isNaN(endDate)){
		alert("Please enter a valid end date")
		return false;
	}
	return true;	
}

function validateFund(){
	var fund = document.getElementById('fund').value;
	var bank = document.getElementById('bank');
	if(fund == -1 && bank.options.length==1){
		alert("Please select a Fund")
		return false;
	}
	return true;
}

function validateBank(){
	var bank = document.getElementById('bank').value;
	if(bank == -1){
		alert("Please select a Bank")
		return false;
	}
	return true;
}

function viewVoucher(vid){
	var url = '../voucher/preApprovedVoucher!loadvoucherview.action?vhid='+ vid;
	window.open(url,'Search','resizable=yes,scrollbars=yes,left=300,top=40, width=900, height=700');
}

function showChequeDetails(voucherId){
	var url = '../report/bankBookReport!showChequeDetails.action?skipPrepare=true&voucherId='+ voucherId;
	window.open(url,'Search','resizable=yes,scrollbars=yes,left=300,top=40, width=900, height=700');
}
</script>
</head>  
<body>
<div class="formmainbox">
<div class="formheading"></div>
<div class="subheadnew">Bank Book Report</div>


<s:form action="bankBookReport" theme="simple" name="bankBookReport">
<table width="100%" cellpadding="0" cellspacing="0" border="0">
	<tr>
		<td class="greybox" width="10%">Fund:</td>
		<td class="greybox">
			<s:select name="fund" id="fund" list="dropdownData.fundList" listKey="id" listValue="name" headerKey="-1" headerValue="----Choose----" onChange="loadBankForFund(this);" />
		</td>
		<td class="greybox" width="10%">&nbsp;</td>
		<td class="greybox">&nbsp;</td>
	</tr>
	<tr>
	    <egov:ajaxdropdown id="bank" fields="['Text','Value']" dropdownId="bank" url="voucher/common!ajaxLoadBanks.action" />
	    <td class="bluebox" width="10%">Bank Name:<span class="bluebox"><span class="mandatory">*</span></span></td>
	    <td class="bluebox">
	    	<s:select name="bank" id="bank" list="dropdownData.bankList" listKey="bankBranchId" listValue="bankBranchName" headerKey="-1" headerValue="----Choose----" onChange="validateFund(); populateAccNumbers(this)"  />
	    </td>
	    <egov:ajaxdropdown id="accountNumber" fields="['Text','Value']" dropdownId="accountNumber" url="voucher/common!ajaxLoadAccountNumbers.action" />
		<td class="bluebox" width="10%">Account Number:<span class="bluebox"><span class="mandatory">*</span></span></td>
		<td class="bluebox">
			<s:select  name="bankAccount" id="accountNumber" list="dropdownData.accNumList" listKey="id" listValue="accountnumber" headerKey="-1" headerValue="----Choose----" onclick="return validateBank()"/>
		</td>
	</tr>
	<tr>
		<td class="greybox" width="10%">Start Date:<span class="mandatory">*</span></td>
		<td class="greybox">
			<s:textfield name="startDate" id="startDate" cssStyle="width:100px" value='%{getFormattedDate(startDate)}' onkeyup="DateFormat(this,this.value,event,false,'3')"/><a href="javascript:show_calendar('bankBookReport.startDate');" style="text-decoration:none">&nbsp;<img src="${pageContext.request.contextPath}/image/calendaricon.gif" border="0"/></a>(dd/mm/yyyy)<br/>
		</td>
		<td class="greybox" width="10%">End Date:<span class="mandatory">*</span></td>
		<td class="greybox">
			<s:textfield name="endDate" id="endDate" cssStyle="width:100px" value='%{getFormattedDate(endDate)}' onkeyup="DateFormat(this,this.value,event,false,'3')"/><a href="javascript:show_calendar('bankBookReport.endDate');" style="text-decoration:none">&nbsp;<img src="${pageContext.request.contextPath}/image/calendaricon.gif" border="0"/></a>(dd/mm/yyyy)<br/>
		</td>
	</tr>
	<tr>
		<jsp:include page="../voucher/vouchertrans-filter.jsp"/>
	</tr>
</table>
<br/><br/>
<div class="buttonbottom">
  <input type="button" value="Search" class="buttonsubmit" onclick="return getData()"/>
  &nbsp;
	<s:reset name="button" type="submit" cssClass="button" id="button" value="Cancel"/>
	<s:submit value="Close" onclick="javascript: self.close()" cssClass="button"/>
</div>
</div>
</s:form>

<div id="results">
</div>
</body>
</html>
