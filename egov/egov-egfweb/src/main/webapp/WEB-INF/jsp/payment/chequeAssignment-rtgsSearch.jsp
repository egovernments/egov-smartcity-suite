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
<html>
<head>
<link rel="stylesheet" type="text/css"
	href="/EGF/resources/css/ccMenu.css?rnd=${app_release_no}" />
<title>RTGS Ref. No Assignment Search</title>
</head>
<body onload="onload()">
	<s:form action="chequeAssignment" theme="simple" id="chequeAssignment"
		name="chequeAssignment">
		<jsp:include page="../budget/budgetHeader.jsp">
			<jsp:param name="heading" value="RTGS Ref. No. Assignment Search" />
		</jsp:include>
		<span id="errorSpan"> <s:actionerror /> <s:fielderror /> <s:actionmessage />
		</span>
		<div class="formmainbox">
			<div class="subheadnew">
				<s:text name="chq.rtgs.assignment.search.heading" />
			</div>
			<table align="center" width="100%" cellpadding="0" cellspacing="0">
				<tr>
					<td class="bluebox" width="30%"><s:text
							name="chq.assignment.paymentvoucherdatefrom" /></td>
					<td class="bluebox"><s:textfield id="fromDate" name="fromDate"
							value="%{fromDate}" data-date-end-date="0d"
							onkeyup="DateFormat(this,this.value,event,false,'3')"
							placeholder="DD/MM/YYYY" class="form-control datepicker"
							data-inputmask="'mask': 'd/m/y'" /></td>
					<td class="bluebox" width="30%"><s:text
							name="chq.assignment.paymentvoucherdateto" /></td>
					<td class="bluebox"><s:textfield id="toDate" name="toDate"
							value="%{toDate}" data-date-end-date="0d"
							onkeyup="DateFormat(this,this.value,event,false,'3')"
							placeholder="DD/MM/YYYY" class="form-control datepicker"
							data-inputmask="'mask': 'd/m/y'" /></td>
				</tr>
				<tr>
					<td class="greybox"><s:text name="payment.mode" /><span
						class="mandatory"></span></td>
					<td class="greybox"><s:radio id="paymentMode"
							name="paymentMode" list="#{'rtgs':'RTGS'}"
							onchange="enableOrDisableBillType(this)" value="%{paymentMode}" /></td>
					<td class="greybox"><s:text
							name="chq.assignment.paymentvoucherno" /></td>
					<td class="greybox"><s:textfield name="voucherNumber"
							id="voucherNumber" value="%{voucherNumber}" /></td>
				</tr>
				<tr>
					<td class="bluebox"><s:text name="chq.assignment.billtype" />
					</td>
					<td class="bluebox"><s:select name="billType" id="billType"
							list="billTypeMap" headerKey="-1" headerValue="----Choose----"
							value="%{billType}" /></td>
				</tr>
				<tr>
					<td class="greybox"><s:text name="voucher.fund" /></td>
					<td class="greybox"><s:select name="fundId" id="fundId"
							list="dropdownData.fundList" listKey="id" listValue="name"
							headerKey="-1" headerValue="----Choose----"
							onChange="loadBank(this);" value="%{fundId.id}" /></td>

					<td class="greybox" id="deptLabel"><s:text
							name="voucher.department" /></td>
					<td class="greybox"><s:select name="vouchermis.departmentid"
							id="vouchermis.departmentid" list="dropdownData.departmentList"
							listKey="id" listValue="name" headerKey="-1"
							headerValue="----Choose----"
							value="voucherHeader.vouchermis.departmentid.id" /></td>
				</tr>
				<tr>
					<egov:ajaxdropdown id="bank_branch" fields="['Text','Value']"
						dropdownId="bank_branch"
						url="voucher/common-ajaxLoadAllBanks.action" />
					<td class="greybox"><s:text name="chq.assignment.bank" /></td>
					<td class="greybox"><s:select name="bank_branch"
							id="bank_branch" list="bankBranchMap" headerKey="-1"
							headerValue="----Choose----" onchange="loadBankAccount(this)"
							value="%{bank_branch}" /></td>
					<egov:ajaxdropdown id="bankaccount" fields="['Text','Value']"
						dropdownId="bankaccount"
						url="voucher/common-ajaxLoadBankAccountsWithApprovedPayments.action" />
					<td class="greybox"><s:text name="chq.assignment.bankaccount" /></td>
					<td class="greybox" colspan="2"><s:select name="bankaccount"
							id="bankaccount" list="dropdownData.bankaccountList" listKey="id"
							listValue="chartofaccounts.glcode+'--'+accountnumber+'---'+accounttype"
							headerKey="-1" headerValue="----Choose----"
							value="%{bankaccount}" /></td>
				</tr>


				<!-- <tr> 	<td class="bluebox">
				<s:text name="chq.assignment.re-assignsurrendercheque"/>
				</td class="bluebox">
				<td class="bluebox">
				<s:checkbox id="reassignSurrenderChq" name="reassignSurrenderChq" />
				</td class="bluebox">
				</tr> -->
			</table>
			<div class="buttonbottom">
				<s:submit method="searchRTGS" value="Search" id="searchBtn"
					cssClass="buttonsubmit" onclick="submitForm();" />
				<input type="button" value="Close"
					onclick="javascript:window.close()" class="button" />
			</div>
		</div>
		<s:hidden name="bankbranch" id="bankbranch" />
		<s:hidden name="rtgsContractorAssignment"
			id="rtgsContractorAssignment" />
		<s:hidden name="billSubType" id="billSubType" value="%{billSubType}" />
		<s:hidden name="region" id="region" value="%{region}" />
	</s:form>
	<script>
		var date = '<s:date name="currentDate" format="dd/MM/yyyy"/>';
		function onload() {
			populatebank_branch();
		}

		function loadBank(obj) {
			var vTypeOfAccount = '<s:property value="%{typeOfAccount}"/>';

			if (obj.options[obj.selectedIndex].value != -1) {
				populatebank_branch({
					fundId : obj.options[obj.selectedIndex].value
							+ '&asOnDate=' + date
				});
			} else {
				populatebank_branch();
			}
		}
		function loadBankAccount(obj) {
			var vTypeOfAccount = '<s:property value="%{typeOfAccount}"/>';
			var fund = document.getElementById('fundId');
			if (obj.options[obj.selectedIndex].value != -1) {
				var x = obj.options[obj.selectedIndex].value.split("-");
				//bootbox.alert("heelo"+x);                            
				document.getElementById("bankbranch").value = x[1];
				populatebankaccount({
					branchId : x[1] + '&asOnDate=' + date,
					fundId : fund.options[fund.selectedIndex].value
				});
			}

		}
		function submitForm() {
			document.chequeAssignment.action = '/EGF/payment/chequeAssignment-searchRTGS.action';
			document.chequeAssignment.submit();
		}
	</script>
	<s:if test="%{!validateUser('chequeassignment')}">
		<script>
			document.getElementById('searchBtn').disabled = true;
			document.getElementById('errorSpan').innerHTML = '<s:text name="chq.assignment.invalid.user"/>'
		</script>
	</s:if>
</body>
</html>
