<%--
  ~    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
  ~    accountability and the service delivery of the government  organizations.
  ~
  ~     Copyright (C) 2017  eGovernments Foundation
  ~
  ~     The updated version of eGov suite of products as by eGovernments Foundation
  ~     is available at http://www.egovernments.org
  ~
  ~     This program is free software: you can redistribute it and/or modify
  ~     it under the terms of the GNU General Public License as published by
  ~     the Free Software Foundation, either version 3 of the License, or
  ~     any later version.
  ~
  ~     This program is distributed in the hope that it will be useful,
  ~     but WITHOUT ANY WARRANTY; without even the implied warranty of
  ~     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  ~     GNU General Public License for more details.
  ~
  ~     You should have received a copy of the GNU General Public License
  ~     along with this program. If not, see http://www.gnu.org/licenses/ or
  ~     http://www.gnu.org/licenses/gpl.html .
  ~
  ~     In addition to the terms of the GPL license to be adhered to in using this
  ~     program, the following additional terms are to be complied with:
  ~
  ~         1) All versions of this program, verbatim or modified must carry this
  ~            Legal Notice.
  ~            Further, all user interfaces, including but not limited to citizen facing interfaces,
  ~            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
  ~            derived works should carry eGovernments Foundation logo on the top right corner.
  ~
  ~            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
  ~            For any further queries on attribution, including queries on brand guidelines,
  ~            please contact contact@egovernments.org
  ~
  ~         2) Any misrepresentation of the origin of the material is prohibited. It
  ~            is required that all modified versions of this material be marked in
  ~            reasonable ways as different from the original version.
  ~
  ~         3) This license does not grant any rights to any user of the program
  ~            with regards to rights under trademark law for use of the trade names
  ~            or trademarks of eGovernments Foundation.
  ~
  ~   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
  ~
  --%>


<%@ taglib prefix="s" uri="/WEB-INF/tags/struts-tags.tld"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<head>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/resources/javascript/contra.js?rnd=${app_release_no}"></script>
<script type="text/javascript"
	src="/EGF/resources/javascript/ajaxCommonFunctions.js?rnd=${app_release_no}"></script>
<STYLE type="text/css">
@media print {
	#non-printable {
		display: none;
	}
}
</STYLE>
</head>
<script>
var callback = {
		success: function(o){
			document.getElementById('results').innerHTML=o.responseText;
			document.getElementById('loading').style.display ='none';
			},
			failure: function(o) {
			document.getElementById('loading').style.display ='none';
			bootbox.alert("Please generate Fund Flow Report ");
		    }
		}
function getData(){
	var asOnDate =  document.getElementById('asOnDate').value;
	var bankAccount = document.getElementById('accountNumber').value;
	
	isValid = validateData();
	if(isValid == false)
		return false;
	var url = '/EGF/payment/outstandingPayment!ajaxLoadPaymentHeader.action?skipPrepare=true&bankAccount.id='+bankAccount+'&asOnDate='+asOnDate;
	YAHOO.util.Connect.asyncRequest('POST', url, callback, null);
	doAfterSubmit();
}
function loadBank(fund){
	var status = '<s:property value="voucherStatusKey" escape="false"/>';
	var asOnDate =  document.getElementById('asOnDate').value;
	populatebank({fundId:fund.options[fund.selectedIndex].value,asOnDate:asOnDate,voucherStatusKey:status})	
}
function populateAccNumbers(bankBranch){
	var status = '<s:property value="voucherStatusKey" escape="false"/>';
	var fund = document.getElementById('fundId');
	id = bankBranch.options[bankBranch.selectedIndex].value.split("-")[1]
	var asOnDate =  document.getElementById('asOnDate').value;
	populateaccountNumber({branchId:id,fundId:fund.options[fund.selectedIndex].value,asOnDate:asOnDate,voucherStatusKey:status})	
}
function doAfterSubmit(){
		document.getElementById('loading').style.display ='block';
}
function validateData(){
	var bankAccount = document.getElementById('accountNumber').value;
	var bank = document.getElementById('bank').value;
	var fund = document.getElementById('fundId').value;
	if(fund == -1){
		bootbox.alert("Please select a Fund")
		return false;
	}
	if(bank == -1){
		bootbox.alert("Please select a Bank")
		return false;
	}
	if(bankAccount == -1){
		bootbox.alert("Please select a Bank Account")
		return false;
	}
	var asOnDate =  Date.parse(document.getElementById('asOnDate').value);
	if(isNaN(asOnDate)){
		bootbox.alert("Please enter a valid date")
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

function validateFund(){
	var fund = document.getElementById('fundId').value;
	if(fund == -1){
		bootbox.alert("Please select a Fund")
		return false;
	}
	return true;
}

function validateBank(){
	var bank = document.getElementById('bank').value;
	if(bank == -1){
		bootbox.alert("Please select a Bank")
		return false;
	}
	return true;
}
function viewVoucher(vid){


	var url = '../voucher/preApprovedVoucher!loadvoucherview.action?vhid='+vid;
	window.open(url,'Search','resizable=yes,scrollbars=yes,left=300,top=40, width=900, height=700');
}
function exportXls(){
	var bankAccount = document.getElementById('accountNumber').value;
	var asOnDate = document.getElementById('asOnDate').value;
	var runningBalance = document.getElementById('rBalance').value;	
	var selectedVhs="";
	var selectdVhs="";
	var x=document.getElementsByName('selectVhs');
	for(i=0;i<x.length;i++){
		if(x[i].checked==true){
			selectedVhs=selectedVhs+x[i].value + ",";
			}
	}
	if(selectedVhs!=""){
	window.open('/EGF/payment/outstandingPayment!exportXls.action?skipPrepare=true&bankAccount.id='+bankAccount+'&asOnDate='+asOnDate+'&selectedVhs='+selectedVhs+'&runningBalance='+runningBalance,'','resizable=yes,height=650,width=900,scrollbars=yes,left=30,top=30,status=no');
	}else{
	window.open('/EGF/payment/outstandingPayment!exportXls.action?skipPrepare=true&bankAccount.id='+bankAccount+'&asOnDate='+asOnDate+'&runningBalance='+runningBalance,'','resizable=yes,height=650,width=900,scrollbars=yes,left=30,top=30,status=no');
	}
}


function exportPdf(){
	var bankAccount = document.getElementById('accountNumber').value;
	var asOnDate = document.getElementById('asOnDate').value;
	var runningBalance = document.getElementById('rBalance').value;	
	var selectedVhs="";
	var selectdVhs="";
	var x=document.getElementsByName('selectVhs');
	for(i=0;i<x.length;i++){
		if(x[i].checked==true){
			selectedVhs=selectedVhs+x[i].value + ",";
			}
	}
	if(selectedVhs!=""){
	window.open('/EGF/payment/outstandingPayment!exportPdf.action?skipPrepare=true&bankAccount.id='+bankAccount+'&asOnDate='+asOnDate+'&selectedVhs='+selectedVhs+'&runningBalance='+runningBalance,'','resizable=yes,height=650,width=900,scrollbars=yes,left=30,top=30,status=no');
	}else{
	window.open('/EGF/payment/outstandingPayment!exportPdf.action?skipPrepare=true&bankAccount.id='+bankAccount+'&asOnDate='+asOnDate+'&runningBalance='+runningBalance,'','resizable=yes,height=650,width=900,scrollbars=yes,left=30,top=30,status=no');
	}
}
</script>
<body>
	<div class="formmainbox">
		<div class="formheading"></div>
		<div class="subheadnew">Outstanding Bank Payments & Running
			Balance Report</div>

		<s:form action="outstandingPayment" theme="simple"
			name="outstandingPayment">
			<table width="100%" cellpadding="0" cellspacing="0" border="0">
				<tr>
					<td class="greybox" width="20%">&nbsp;</td>
					<td class="greybox" width="10%">Fund:<span class="mandatory">*</span></td>
					<td class="greybox"><s:select name="fundId" id="fundId"
							list="dropdownData.fundList" listKey="id" listValue="name"
							headerKey="-1" headerValue="----Choose----"
							onChange="loadBank(this);" /></td>
					<td class="greybox" width="10%">As On Date:<span
						class="mandatory">*</span></td>
					<td class="greybox"><s:textfield name="asOnDate" id="asOnDate"
							value='%{getFormattedDate(asOnDate)}' cssStyle="width:100px"
							onkeyup="DateFormat(this,this.value,event,false,'3')" /><a
						href="javascript:show_calendar('outstandingPayment.asOnDate');"
						style="text-decoration: none">&nbsp;<img
							src="/egi/resources/erp2/images/calendaricon.gif" border="0" /></a>(dd/mm/yyyy)<br />
					</td>
				</tr>
				<tr>
					<td width="20%">&nbsp;</td>
					<egov:ajaxdropdown id="bank" fields="['Text','Value']"
						dropdownId="bank"
						url="voucher/common!ajaxLoadBanksWithPaymentInWorkFlow.action" />
					<td class="bluebox" width="10%">Bank Name:<span
						class="bluebox"><span class="mandatory">*</span></span></td>
					<td class="bluebox"><s:select name="bank" id="bank"
							list="dropdownData.bankList" listKey="bankBranchId"
							listValue="bankBranchName" headerKey="-1"
							headerValue="----Choose----" onclick="validateFund()"
							onChange="populateAccNumbers(this);" /></td>
					<egov:ajaxdropdown id="accountNumber" fields="['Text','Value']"
						dropdownId="accountNumber"
						url="voucher/common!ajaxLoadBankAccountsWithPaymentInWorkFlow.action" />
					<td class="bluebox" width="10%">Account Number:<span
						class="bluebox"><span class="mandatory">*</span></span></td>
					<td class="bluebox"><s:select name="bankAccount"
							id="accountNumber" list="dropdownData.accNumList" listKey="id"
							listValue="accountnumber" headerKey="-1"
							headerValue="----Choose----" onclick="validateBank()" /></td>
				</tr>

			</table>
			<br />
			<br />
			<div class="buttonbottom" id="non-printable">
				<input type="button" value="Submit" class="buttonsubmit"
					onclick="return getData()" /> &nbsp;
				<s:reset name="button" type="submit" cssClass="button" id="button"
					value="Cancel" />
				<s:submit value="Close" onclick="javascript: self.close()"
					cssClass="button" />
			</div>
	</div>
	<div id="loading" class="loading"
		style="width: 700; height: 700; display: none" align="center">
		<blink style="color: red">Searching processing, Please wait...</blink>
	</div>
	</s:form>


	<div id="results">
		<script>
document.getElementById('loading').style.display ='none';
</script>
	</div>
	<div id="resultGrid"></div>
</body>
</html>
