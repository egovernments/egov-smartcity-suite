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
var typeOfAcc="RECEIPTS_PAYMENTS,PAYMENTS";
var callback = {
		success: function(o){
			document.getElementById('results').innerHTML=o.responseText;
			document.getElementById('loading').style.display ='none';
			},
			failure: function(o) {
			document.getElementById('loading').style.display ='none';
			bootbox.alert("Search failed! Please try again");
			}
		}
function getData(){
   	var bankAccount = document.getElementById('accountNumber').value;
	var dateNm=document.getElementsByName('dateType');
	var chequeOrRTGS = "";
	if (document.getElementsByName('chequeOrRTGS')[0].checked){
		chequeOrRTGS = document.getElementsByName('chequeOrRTGS')[0].value;
	} else if (document.getElementsByName('chequeOrRTGS')[1].checked){
		chequeOrRTGS = document.getElementsByName('chequeOrRTGS')[1].value;
	}
	var chequeCondition="&chequeOrRTGS="+chequeOrRTGS;	
	for(var i=0 ; i < dateNm.length; i++){
		//bootbox.alert(dateNm[i].checked);
		if(dateNm[i].checked)
		var dtVal=dateNm[i].value;
	}
	isValid = validateData(dateNm);
	if(isValid == false)
	   return false;
	if(dtVal==1 && bankAccount!=-1){
	  var asOnDate = document.getElementById('asOnDate').value;
	  var url = '/EGF/payment/concurrenceReport!ajaxLoadPaymentHeader.action?skipPrepare=true&bankAccount.id='+bankAccount+'&asOnDate='+asOnDate+chequeCondition;
	  YAHOO.util.Connect.asyncRequest('POST', url, callback, null);
	}
	if(dtVal==1 && bankAccount==-1){
	  var asOnDate = document.getElementById('asOnDate').value;
	  var url = '/EGF/payment/concurrenceReport!ajaxLoadPaymentHeader.action?skipPrepare=true&asOnDate='+asOnDate+chequeCondition;
	  YAHOO.util.Connect.asyncRequest('POST', url, callback, null);
	}
	if(dtVal==0 && bankAccount!=-1){
	   var fromDate = document.getElementById('fromDate').value;
	   var toDate =document.getElementById('toDate').value;
	   var url = '/EGF/payment/concurrenceReport!ajaxLoadPaymentHeader.action?skipPrepare=true&bankAccount.id='+bankAccount+'&fromDate='+fromDate+'&toDate='+toDate+chequeCondition;
	   YAHOO.util.Connect.asyncRequest('POST', url, callback, null);
	}
	if(dtVal==0 && bankAccount==-1){
	   var fromDate = document.getElementById('fromDate').value;
	   var toDate =document.getElementById('toDate').value;
	   var url = '/EGF/payment/concurrenceReport!ajaxLoadPaymentHeader.action?skipPrepare=true&fromDate='+fromDate+'&toDate='+toDate+
	   chequeCondition;
	   YAHOO.util.Connect.asyncRequest('POST', url, callback, null);
	}
	doAfterSubmit();
	   
}
function loadBank(fund){
	document.getElementById('accountNumber').value="-1";
	if(fund.value!=-1){
		populatebank({fundId:fund.options[fund.selectedIndex].value, typeOfAccount:typeOfAcc})   
	}else{       
		populatebank({typeOfAccount:typeOfAcc})       
	} 
}

function validateData(dateNm1){
	var bank = document.getElementById('bank').value;
	var bankAccount = document.getElementById('accountNumber').value;
	 if(bank != -1 && bankAccount == -1){
		 bootbox.alert("Please select a Bank Account");
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

function populateAccNumbers(bankBranch){
	id = bankBranch.options[bankBranch.selectedIndex].value.split("-")[1];
	bankid=bankBranch.options[bankBranch.selectedIndex].value.split("-")[0];
	var fund = document.getElementById('fundId');
	if(fund.value!=-1){
		populateaccountNumber({branchId:id,bankId:bankid,fundId:fund.options[fund.selectedIndex].value,typeOfAccount:typeOfAcc})
	}else{
		populateaccountNumber({branchId:id,bankId:bankid,typeOfAccount:typeOfAcc})
	}	
}  
function onLoadTask(){
	document.getElementById('asdat1').style.display='none';
	document.getElementById('dateran').style.display='none';
	//bootbox.alert("---"+typeOfAcc);
    populatebank({typeOfAccount:typeOfAcc})
}
function loaddate(dateTypeObj){
  	if(dateTypeObj.value=='1'){
  		document.getElementById('asdat1').style.display='table-row';
  		document.getElementById('dateran').style.display='none';
  		document.getElementById('fromDate').value="";
  		document.getElementById('toDate').value="";
  	}
	else if(dateTypeObj.value=='0'){ 
	    document.getElementById('asdat1').style.display='none';
		document.getElementById('dateran').style.display='table-row';
		document.getElementById('asOnDate').value="";
	}
	else{
		document.getElementById('asdat1').style.display='none';
   		document.getElementById('dateran').style.display='none';
   		document.getElementById('fromDate').value="";
   		document.getElementById('toDate').value="";
   		document.getElementById('asOnDate').value="";
	}
}
function validateCancel(){
	document.getElementById('asdat1').style.display='none';
   	document.getElementById('dateran').style.display='none';
   	document.getElementById('loading').style.display ='none';
}
function exportXls(){
    var bankAccount = document.getElementById('accountNumber').value;
    var fromDate = document.getElementById('fromDate').value;
	var toDate =document.getElementById('toDate').value;
	var asOnDate = document.getElementById('asOnDate').value;
	var dateNm1=document.getElementsByName('dateType');
	var chequeOrRTGS = document.getElementById('chequeOrRTGS');
	var chequeOrRTGS = "";
	if (document.getElementsByName('chequeOrRTGS')[0].checked){
		chequeOrRTGS = document.getElementsByName('chequeOrRTGS')[0].value;
	} else if (document.getElementsByName('chequeOrRTGS')[1].checked){
		chequeOrRTGS = document.getElementsByName('chequeOrRTGS')[1].value;
	}
	for(var i=0 ; i < dateNm1.length; i++){
		if(dateNm1[i].checked)
		var dtVal=dateNm1[i].value;
	}
    if(bankAccount!=-1){
	if(dtVal==0){
	window.open('/EGF/payment/concurrenceReport!exportXls.action?skipPrepare=true&bankAccount.id='+bankAccount+'&fromDate='+fromDate+'&toDate='+toDate+"&chequeOrRTGS="+chequeOrRTGS,'','resizable=yes,height=650,width=900,scrollbars=yes,left=30,top=30,status=no');
	}
	if(dtVal==1){
	window.open('/EGF/payment/concurrenceReport!exportXls.action?skipPrepare=true&bankAccount.id='+bankAccount+'&asOnDate='+asOnDate+"&chequeOrRTGS="+chequeOrRTGS,'','resizable=yes,height=650,width=900,scrollbars=yes,left=30,top=30,status=no');
	}
  }
  else{
  if(dtVal==0){
	window.open('/EGF/payment/concurrenceReport!exportXls.action?skipPrepare=true&fromDate='+fromDate+'&toDate='+toDate+"&chequeOrRTGS="+chequeOrRTGS,'','resizable=yes,height=650,width=900,scrollbars=yes,left=30,top=30,status=no');
	}
	if(dtVal==1){
	window.open('/EGF/payment/concurrenceReport!exportXls.action?skipPrepare=true&asOnDate='+asOnDate+"&chequeOrRTGS="+chequeOrRTGS,'','resizable=yes,height=650,width=900,scrollbars=yes,left=30,top=30,status=no');
	}
  }
    doAfterSubmit();
}

function exportPdf(){
    var bankAccount = document.getElementById('accountNumber').value;
    var fromDate = document.getElementById('fromDate').value;
	var toDate =document.getElementById('toDate').value;
	var asOnDate = document.getElementById('asOnDate').value;
	var dateNm1=document.getElementsByName('dateType');
	var chequeOrRTGS = "";
	if (document.getElementsByName('chequeOrRTGS')[0].checked){
		chequeOrRTGS = document.getElementsByName('chequeOrRTGS')[0].value;
	} else if (document.getElementsByName('chequeOrRTGS')[1].checked){
		chequeOrRTGS = document.getElementsByName('chequeOrRTGS')[1].value;
	}
	for(var i=0 ; i < dateNm1.length; i++){
		if(dateNm1[i].checked)
		var dtVal=dateNm1[i].value;
	}
    if(bankAccount!=-1){
	if(dtVal==0){
	
	window.open('/EGF/payment/concurrenceReport!exportPdf.action?skipPrepare=true&bankAccount.id='+bankAccount+'&fromDate='+fromDate+'&toDate='+toDate+"&chequeOrRTGS="+chequeOrRTGS,'','resizable=yes,height=650,width=900,scrollbars=yes,left=30,top=30,status=no');
	}
	if(dtVal==1){
	window.open('/EGF/payment/concurrenceReport!exportPdf.action?skipPrepare=true&bankAccount.id='+bankAccount+'&asOnDate='+asOnDate+"&chequeOrRTGS="+chequeOrRTGS,'','resizable=yes,height=650,width=900,scrollbars=yes,left=30,top=30,status=no');
	}
  }
  else{
  if(dtVal==0){
	window.open('/EGF/payment/concurrenceReport!exportPdf.action?skipPrepare=true&fromDate='+fromDate+'&toDate='+toDate+"&chequeOrRTGS="+chequeOrRTGS,'','resizable=yes,height=650,width=900,scrollbars=yes,left=30,top=30,status=no');
	}
	if(dtVal==1){
	window.open('/EGF/payment/concurrenceReport!exportPdf.action?skipPrepare=true&asOnDate='+asOnDate+"&chequeOrRTGS="+chequeOrRTGS,'','resizable=yes,height=650,width=900,scrollbars=yes,left=30,top=30,status=no');
	}
  }
    doAfterSubmit();
}
function doAfterSubmit(){
		document.getElementById('loading').style.display ='block';
	}


</script>
<body onload="onLoadTask();">
	<div class="formmainbox">
		<div class="formheading"></div>
		<div class="subheadnew">Concurrence Report</div>


		<s:form action="concurrenceReport" theme="simple"
			name="concurrenceReport" theme="simple" method="post"
			onsubmit="javascript:doAfterSubmit()">
			<table width="100%" cellpadding="0" cellspacing="0" border="0">
				<tr>
					<td class="bluebox">&nbsp;</td>
					<td class="bluebox">Fund</td>
					<td class="bluebox" colspan="3"><s:select name="fundId"
							id="fundId" list="dropdownData.fundList" listKey="id"
							listValue="name" headerKey="-1" headerValue="----Choose----"
							onChange="loadBank(this);" value="%{fundId.id}" /></td>
				</tr>
				<tr>
					<td class="greybox">&nbsp;</td>
					<egov:ajaxdropdown id="bank" fields="['Text','Value']"
						dropdownId="bank"
						url="/voucher/common!ajaxLoadBanksByFundAndType.action" />
					<td class="greybox">Bank Name:<span class="bluebox"></span></td>
					<td class="greybox"><s:select name="bank" id="bank"
							list="dropdownData.bankList" listKey="bankBranchId"
							listValue="bankBranchName" headerKey="-1"
							headerValue="----Choose----" onChange="populateAccNumbers(this);" />
					</td>
					<egov:ajaxdropdown id="accountNumber" fields="['Text','Value']"
						dropdownId="accountNumber"
						url="voucher/common!ajaxLoadAccNumAndType.action" />
					<td class="greybox">Account Number:<span class="bluebox"></span></td>
					<td class="greybox"><s:select name="bankAccount"
							id="accountNumber" list="dropdownData.accNumList" listKey="id"
							listValue="accountnumber" headerKey="-1"
							headerValue="----Choose----" onclick="validateBank()" /></td>
				</tr>
				<tr>
					<td class="bluebox">&nbsp;</td>
					<td class="bluebox"><s:text name="Date Type" /><span
						class="mandatory">*</span></td>
					<td class="bluebox" colspan="3"><s:radio id="dateType"
							name="dateType" list="#{'1':'AsOnDate','0':'Date Range'}"
							value="%{dateType}" onclick="loaddate(this)" /></td>
				</tr>
				<tr id="asdat1">
					<td class="bluebox">&nbsp;</td>
					<td class="bluebox">As On Date:<span class="mandatory">*</span></td>
					<td class="bluebox" colspan="3"><s:textfield name="asOnDate"
							id="asOnDate" cssStyle="width:100px"
							value='%{getFormattedAsOnDate()}'
							onkeyup="DateFormat(this,this.value,event,false,'3')" /><a
						href="javascript:show_calendar('concurrenceReport.asOnDate');"
						style="text-decoration: none">&nbsp;<img
							src="/egi/resources/erp2/images/calendaricon.gif" border="0" /></a>(dd/mm/yyyy)<br />
					</td>
				</tr>
				<tr id="dateran">
					<td class="bluebox">&nbsp;</td>
					<td class="bluebox">From Date:<span class="mandatory">*</span></td>
					<td class="bluebox"><s:textfield name="fromDate" id="fromDate"
							cssStyle="width:100px" value='%{getFormattedDate(this.value)}'
							onkeyup="DateFormat(this,this.value,event,false,'3')" /><a
						href="javascript:show_calendar('concurrenceReport.fromDate');"
						style="text-decoration: none">&nbsp;<img
							src="/egi/resources/erp2/images/calendaricon.gif" border="0" /></a>(dd/mm/yyyy)<br />
					</td>
					<td class="bluebox">To Date:<span class="mandatory">*</span></td>
					<td class="bluebox"><s:textfield name="toDate" id="toDate"
							cssStyle="width:100px" value='%{getFormattedDate()}'
							onkeyup="DateFormat(this,this.value,event,false,'3')" /><a
						href="javascript:show_calendar('concurrenceReport.toDate');"
						style="text-decoration: none">&nbsp;<img
							src="/egi/resources/erp2/images/calendaricon.gif" border="0" /></a>(dd/mm/yyyy)<br />
					</td>
				</tr>
				<tr>
					<td class="greybox">&nbsp;</td>
					<td class="greybox"><s:text name="payment.mode" /></td>
					<td class="greybox" colspan="3"><s:radio id="chequeOrRTGS"
							name="chequeOrRTGS" list="#{'cheque':'Cheque','rtgs':'RTGS'}"
							value="%{chequeOrRTGS}" /></td>
				</tr>
			</table>
			<br />
			<br />
			<div class="buttonbottom" id="non-printable">
				<input type="button" value="Submit" class="buttonsubmit"
					onclick="return getData()" /> &nbsp;
				<s:reset name="button" type="submit" cssClass="button" id="button"
					value="Reset" onclick="validateCancel();" />
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

