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
	<script type="text/javascript"
	src="/EGF/resources/javascript/dateValidation.js?rnd=${app_release_no}"></script>
</head>
<script>
var callback = {
		
		success: function(o){
			document.getElementById('results').innerHTML=o.responseText;
			undoLoadingMask();
			},
			failure: function(o) {
				undoLoadingMask();
		    }
		    
			
		}
function getData(){
	var startDate =  document.getElementById('startDate').value;
	var endDate =  document.getElementById('endDate').value;
	var bankAccount = document.getElementById('accountNumber').value;
	
    

	//var isDateValid =validateFromAndToDate(startDate,endDate);
	isValid = validateDataa();
	if(isValid == false )
		{
		return false;
		}
	
	//doLoadingMask();
	var url = '/EGF/report/bankBookReport-ajaxLoadBankBook.action?skipPrepare=true&bankAccount.id='+bankAccount+'&startDate='+startDate+'&endDate='+endDate+getMiscData();
	YAHOO.util.Connect.asyncRequest('POST', url, callback, null);
}


function getMiscData(){
	var fund,department,functionary,field,scheme,subscheme,data,function1="";
	fund = document.getElementById('fundId').value;
	//fund1 = document.getElementById('fund').value;
	
	department = document.getElementById('vouchermis.departmentid').value;
	
	function1=document.getElementById('vouchermis.function').value;
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
	if(function1 != undefined)
		data = data+"&vouchermis.function.id="+function1;
	
	return data;
}

function loadBank(fund){
	if(fund.value!=-1){
		populatebank({fundId:fund.options[fund.selectedIndex].value})   
	}else{       
		populatebank()       
	} 
}
function populateAccNumbers(bankBranch){
	var fund = document.getElementById('fundId');
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
	//var fund2 = document.getElementById('fund').value;
	window.open('/EGF/report/bankBookReport-exportXls.action?skipPrepare=true&bankAccount.id='+bankAccount+'&startDate='+startDate+'&endDate='+endDate+getMiscData(),'','resizable=yes,height=650,width=900,scrollbars=yes,left=30,top=30,status=no');
}

function exportPdf(){
	var bankAccount = document.getElementById('accountNumber').value;
	var startDate = document.getElementById('startDate').value;
	var endDate = document.getElementById('endDate').value;
	//var fund2 = document.getElementById('fund').value;
	window.open('/EGF/report/bankBookReport-exportPdf.action?skipPrepare=true&bankAccount.id='+bankAccount+'&startDate='+startDate+'&endDate='+endDate+getMiscData(),'','resizable=yes,height=650,width=900,scrollbars=yes,left=30,top=30,status=no');
}

function validateDataa(){
	var bankAccount = document.getElementById('accountNumber').value;
	var bank = document.getElementById('bank').value;
	if(bank == -1){
		bootbox.alert("Please select a Bank")
		return false;
	}
	if(bankAccount == -1){
		bootbox.alert("Please select a Bank Account")
		return false;
	}
	
	var startDate = document.getElementById('startDate').value;
	if(startDate=='')
		{ 
		bootbox.alert("Please enter start date")
		return false;
		}
	
	var endDate = document.getElementById('endDate').value;
	
	if(endDate=='')
		{ 
		bootbox.alert("Please enter end date")
		return false;
		}

	var fromdate= startDate.split('/');
	startDate=new Date(fromdate[2],fromdate[1]-1,fromdate[0]);
    var todate = endDate.split('/');
    endDate=new Date(todate[2],todate[1]-1,todate[0]);
	

	if(startDate > endDate)
	{ 
		bootbox.alert("Start date should be less than end date.")
		return false;
		}
	return true;
}


function validateFund(){
	var fund = document.getElementById('fundId').value;
	var bank = document.getElementById('bank');
	if(fund == -1 && bank.options.length==1){
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
	var url = '../voucher/preApprovedVoucher-loadvoucherview.action?vhid='+ vid;
	window.open(url,'Search','resizable=yes,scrollbars=yes,left=300,top=40, width=900, height=700');
}

function showChequeDetails(voucherId){
	var url = '../report/bankBookReport-showChequeDetails.action?skipPrepare=true&voucherId='+ voucherId;
	window.open(url,'Search','resizable=yes,scrollbars=yes,left=300,top=40, width=900, height=700');
}
</script>
<body>

 
 
	<div class="formmainbox">
		<div class="formheading"></div>
		<div class="subheadnew">Bank Book Report</div>


		<s:form action="bankBookReport" theme="simple" name="bankBookReport">
			<table width="100%" cellpadding="0" cellspacing="0" border="0">
			
			<tr>
					<jsp:include page="../voucher/vouchertrans-filter.jsp" />
				</tr>
				
				<tr>
					<egov:ajaxdropdown id="bank" fields="['Text','Value']"
						dropdownId="bank" url="voucher/common-ajaxLoadAllBanks.action" />

                    <td style="width: 5%"></td>
					<td class="bluebox" width="10%">Bank Name:<span
						class="bluebox"><span class="mandatory1">*</span></span></td>
					<td class="bluebox"><s:select name="bank" id="bank"
							list="dropdownData.bankList" listKey="bankBranchId"
							listValue="bankBranchName" headerKey="-1"
							headerValue="----Choose----" onclick="validateFund()"
							onChange="populateAccNumbers(this);" /></td>
					<egov:ajaxdropdown id="accountNumber" fields="['Text','Value']"
						dropdownId="accountNumber"
						url="voucher/common-ajaxLoadAccountNumbers.action" />
					<td class="bluebox" width="10%">Account Number:<span
						class="bluebox"><span class="mandatory1">*</span></span></td>
					<td class="bluebox"><s:select name="bankAccount"
							id="accountNumber" list="dropdownData.accNumList" listKey="id"
							listValue="accountnumber" headerKey="-1"
							headerValue="----Choose----" onclick="validateBank()" /></td>
				</tr>
				<tr>
					<td style="width: 5%"></td>
					<td class="greybox" width="10%">Start Date:<span
						class="mandatory1">*</span></td>
					<s:date name="startDate" format="dd/MM/yyyy" var="tempFromDate" />
					<td class="greybox"><s:textfield id="startDate"
							name="startDate" value="%{tempFromDate}"
							onkeyup="DateFormat(this,this.value,event,false,'3')"
							placeholder="DD/MM/YYYY" cssClass="form-control datepicker"
							data-inputmask="'mask': 'd/m/y'" /></td>
					<td class="greybox" width="10%">End Date:<span
						class="mandatory1">*</span></td>
					<td class="greybox"><s:textfield id="endDate" name="endDate"
							value="%{endDate}"
							onkeyup="DateFormat(this,this.value,event,false,'3')"
							placeholder="DD/MM/YYYY" cssClass="form-control datepicker"
							data-inputmask="'mask': 'd/m/y'" /></td>
				</tr>
			</table>
			<br />
			<br />
			<div class="buttonbottom">
				<input type="button" value="Search" class="buttonsubmit"
					onclick="return getData()" /> &nbsp;
				<s:reset name="button" type="submit" cssClass="button" id="button"
					value="Cancel" />
				<input type="button" value="Close"
					onclick="javascript:window.close()" class="button" />
			</div>
	</div>
	</s:form>

	<div id="results"></div>
</body>
</html>
