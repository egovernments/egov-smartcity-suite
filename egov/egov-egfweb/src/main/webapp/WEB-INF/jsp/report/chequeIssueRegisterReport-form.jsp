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
</head>
<script>
	function populateSubSchemes(scheme){
		populatebudgetDetail_subScheme({schemeId:scheme.options[scheme.selectedIndex].value})
	}

var callback = {
		success: function(o){
			document.getElementById('results').innerHTML=o.responseText;
			undoLoadingMask();
			},
			failure: function(o) {
				undoLoadingMask();
		    }
		}
function generateReport(){
	var fromDate =  document.getElementById('fromDate').value;
	var toDate = document.getElementById('toDate').value;
	var bankAccount = document.getElementById('accountNumber').value;
	var bank = document.getElementById('bank').value;
	var department = document.getElementById('department').value;
	isValid = validateDates();
	if(isValid == false)
		return false;
	doLoadingMask();
	var url = '../report/chequeIssueRegisterReport-ajaxPrint.action?fromDate='+fromDate+'&toDate='+toDate+'&accountNumber.id='+bankAccount+'&department.id='+department+'&bank='+bank+'&showDropDown=false';
	YAHOO.util.Connect.asyncRequest('POST', url, callback, null);
}

function validateDates(){
	var bankAccount = document.getElementById('accountNumber').value;
	var bank = document.getElementById('bank').value;
	if(bank == -1){
		bootbox.alert("Please select a bank")
		return false;
	}
	if(bankAccount == -1){
		bootbox.alert("Please select a bank account")
		return false;
	}
	var fromDate = document.getElementById('fromDate').value;
	var toDate = document.getElementById('toDate').value;
	if(fromDate == '' || toDate == ''){
		bootbox.alert("Please select the dates")
		return false;
	}
	var startDate= fromDate.split('/');
	fromDate=new Date(startDate[2],startDate[1]-1,startDate[0]);
    var endDate = toDate.split('/');
    toDate=new Date(endDate[2],endDate[1]-1,endDate[0]);
	
	
	if(fromDate > toDate ){
		bootbox.alert("From date should not be greater than To date  ")
		return false;
	}
	
	document.getElementById('accountNumber.id').value=bankAccount;
	return true;	
}
function viewVoucher(vid){
	var url = '../voucher/preApprovedVoucher-loadvoucherview.action?vhid='+vid;
	window.open(url,'Search','resizable=yes,scrollbars=yes,left=300,top=40, width=900, height=700');
}

function printCheque(id)
{
	var chequeFormat=document.getElementById('chequeFormatId').value;
	if(chequeFormat == "" || chequeFormat == null){
		bootbox.alert("This bank account is not attached to any cheque formats");
		return false;
	} 
	window.open('/EGF/payment/chequeAssignmentPrint-generateChequeFormat.action?instrumentHeader='+id,'Search','resizable=yes,scrollbars=yes,left=300,top=40,width=900, height=700');
}

</script>
<body>
	<div class="formmainbox">
		<div class="formheading"></div>
		<div class="subheadnew">Cheque Issue Register Report</div>
		<br/>
		<br/>


		<s:form action="chequeIssueRegisterReport" theme="simple"
			name="chequeIssueRegister">
			<table width="100%" cellpadding="0" cellspacing="0" border="0">
				<tr>
					<td width="10%">&nbsp;</td>
					<td class="bluebox" width="10%">Bank Name:<span
						class="bluebox"><span class="mandatory1">*</span></span></td>
					<td class="bluebox"><s:select name="bank" id="bank"
							list="dropdownData.bankList" listKey="bankBranchId"
							listValue="bankBranchName" headerKey="-1"
							headerValue="----Choose----" onChange="populateAccNum(this);" />
					</td>
					<td width="10%">&nbsp;</td>
					<egov:ajaxdropdown id="accountNumber" fields="['Text','Value']"
						dropdownId="accountNumber"
						url="voucher/common-ajaxLoadAccNum.action" />
					<td class="bluebox" width="10%">Account Number:<span
						class="bluebox"><span class="mandatory1">*</span></span></td>
					<td class="bluebox"><s:select name="bankAccount"
							id="accountNumber" list="dropdownData.bankAccountList"
							listKey="id" listValue="accountnumber" headerKey="-1"
							headerValue="----Choose----" /></td>
				</tr>
				<tr>
					<td class="greybox" width="10%">&nbsp;</td>
					<td class="greybox" width="10%">Cheque From Date:<span
						class="mandatory1">*</span></td>

					<td class="greybox"><s:date name="fromDate" var="fromDate"
							format="dd/MM/yyyy" /> <s:textfield id="fromDate"
							name="fromDate" value="%{fromDate}"
							onkeyup="DateFormat(this,this.value,event,false,'3')"
							placeholder="DD/MM/YYYY" cssClass="form-control datepicker"
							data-inputmask="'mask': 'd/m/y'" /></td>


					<td width="10%">&nbsp;</td>
					<td class="greybox" width="10%">Cheque To Date:<span
						class="mandatory1">*</span></td>

					<td class="greybox"><s:date name="toDate" var="toDate"
							format="dd/MM/yyyy" /> <s:textfield id="toDate" name="toDate"
							value="%{toDate}"
							onkeyup="DateFormat(this,this.value,event,false,'3')"
							placeholder="DD/MM/YYYY" cssClass="form-control datepicker"
							data-inputmask="'mask': 'd/m/y'" /></td>

				</tr>
				<tr>
					<td width="10%" class="bluebox">&nbsp;</td>
					<td class="bluebox"><s:text name="report.department" /></td>
					<td width="22%" class="bluebox"><s:select
							list="dropdownData.executingDepartmentList" listKey="id"
							listValue="name" name="department.id" headerKey="0"
							headerValue="--- Select ---" value="department.id"
							id="department"></s:select></td>
				</tr>
			</table>
			<br />
			<br />
			<div class="subheadsmallnew"></div>
			<div align="left" class="mandatory1">* Mandatory Fields</div>

			<div class="buttonbottom">
				<input type="button" value="Submit" class="buttonsubmit"
					onclick="return generateReport()" /> &nbsp;
				<s:reset name="button" type="submit" cssClass="button" id="button"
					value="Cancel" />
				<input type="button" value="Close"
					onclick="javascript:window.close()" class="button" />
			</div>
			<input type="hidden" name="accountNumber.id" id="accountNumber.id" />
	</div>
	</s:form>

	<div id="results"></div>
</body>
</html>
