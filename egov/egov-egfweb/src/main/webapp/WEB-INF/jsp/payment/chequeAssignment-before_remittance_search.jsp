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
<title>Cheque Assignment Search</title>
</head>
<body>
	<s:form action="chequeAssignment" theme="simple"
		name="chequeAssignment" id="chequeAssignment">
		<jsp:include page="../budget/budgetHeader.jsp">
			<jsp:param name="heading" value="Cheque Assignment Search" />
		</jsp:include>
		<span class="error-msg" id="errorSpan"> <s:actionerror /> <s:fielderror />
			<s:actionmessage />
		</span>
		<div class="formmainbox">
			<div class="subheadnew">
				<s:text name="chq.assignment.heading.search" />
			</div>
			<table align="center" width="100%" cellpadding="0" cellspacing="0">
				<tr>
					<td class="greybox"></td>
					<td class="greybox"><s:text
							name="chq.assignment.paymentvoucherdatefrom" /></td>
					<td class="greybox"><s:textfield name="fromDate" id="fromDate"
							maxlength="20" value="%{fromDate}"
							onkeyup="DateFormat(this,this.value,event,false,'3')" /><a
						href="javascript:show_calendar('forms[0].fromDate');"
						style="text-decoration: none">&nbsp;<img
							src="/egi/resources/erp2/images/calendaricon.gif" border="0" /></a><br />(dd/mm/yyyy)</td>
					<td class="greybox"><s:text
							name="chq.assignment.paymentvoucherdateto" /></td>
					<td class="greybox"><s:textfield name="toDate" id="toDate"
							maxlength="20" value="%{toDate}"
							onkeyup="DateFormat(this,this.value,event,false,'3')" /><a
						href="javascript:show_calendar('forms[0].toDate');"
						style="text-decoration: none">&nbsp;<img
							src="/egi/resources/erp2/images/calendaricon.gif" border="0" /></a>(dd/mm/yyyy)</td>
				</tr>
				<tr>
					<td class="greybox"></td>
					<td class="bluebox"><s:text name="payment.mode" /><span
						class="mandatory"></span></td>
					<td class="bluebox"><s:radio id="paymentMode"
							name="paymentMode" list="%{modeOfPaymentMap}"
							value="%{paymentMode}" /></td>
					<td class="bluebox"><s:text
							name="chq.assignment.paymentvoucherno" /></td>
					<td class="bluebox"><s:textfield name="voucherNumber"
							id="voucherNumber" value="%{voucherNumber}" /></td>
				</tr>
				<tr>
					<td class="greybox"></td>
					<td class="greybox"><s:text name="recovery.code" /></td>
					<td class="greybox"><s:select name="recoveryId"
							id="recoveryId" list="dropdownData.recoveryList" listKey="id"
							listValue="type+'-'+recoveryName" headerKey="" headerValue="----Choose----" /></td>

				</tr>
				<jsp:include page="../voucher/vouchertrans-filter.jsp" />
				<tr>
					<td class="greybox"></td>
					<egov:ajaxdropdown id="bank_branch" fields="['Text','Value']"
						dropdownId="bank_branch"
						url="voucher/common-ajaxLoadBanksWithApprovedRemittances.action" />
					<td class="greybox"><s:text name="chq.assignment.bank" /><span
						class="mandatory"></span></td>
					<td class="greybox"><s:select name="bank_branch"
							id="bank_branch" list="bankBranchMap" headerKey="-1"
							headerValue="----Choose----" onchange="loadBankAccount(this)"
							value="%{bank_branch}" /></td>
					<egov:ajaxdropdown id="bankaccount" fields="['Text','Value']"
						dropdownId="bankaccount"
						url="voucher/common-ajaxLoadBankAccountsWithApprovedRemittances.action" />
					<td class="greybox"><s:text name="chq.assignment.bankaccount" /><span
						class="mandatory"></span></td>
					<td class="greybox" colspan="2"><s:select name="bankaccount"
							id="bankaccount" list="dropdownData.bankaccountList" listKey="id"
							listValue="chartofaccounts.glcode+'--'+accountnumber+'---'+accounttype"
							headerKey="-1" headerValue="----Choose----"
							value="%{bankaccount}" /></td>
				</tr>
				<tr>
					<td class="greybox"></td>
					<td class="bluebox"><s:text
							name="chq.assignment.re-assignsurrendercheque" /></td class="bluebox">
					<td class="bluebox"><s:checkbox id="reassignSurrenderChq"
							name="reassignSurrenderChq" /></td class="bluebox">
				</tr>
			</table>
			<div class="buttonbottom">
				<s:submit method="searchChequesOfRemittance" value="Search"
					id="searchBtn" cssClass="buttonsubmit" onclick="submitForm()" />
				<input type="button" value="Close"
					onclick="javascript:window.close()" class="button" />
			</div>
		</div>
		<s:hidden name="bankbranch" id="bankbranch" />
	</s:form>
	<script>
		function submitForm() {

			document.chequeAssignment.action = "/EGF/payment/chequeAssignment-searchChequesOfRemittance.action";
			document.chequeAssignment.submit();
		}
		var date = '<s:date name="currentDate" format="dd/MM/yyyy"/>';
		function loadBank(obj) {
			if (document.getElementById("recoveryId").value == "") {
				var revocery = 0;
			}else{
				var revocery = document.getElementById("recoveryId").value;
			}
			var vTypeOfAccount = '<s:property value="%{typeOfAccount}"/>';
			
			if (obj.options[obj.selectedIndex].value != -1)
				populatebank_branch({
					fundId : obj.options[obj.selectedIndex].value
							+ '&asOnDate=' + date + '&recoveryId=' + revocery
				});
		}
		function loadBankAccount(obj) {
			var vTypeOfAccount = '<s:property value="%{typeOfAccount}"/>';
			var fund = document.getElementById('fundId');
			if (obj.options[obj.selectedIndex].value != -1) {
				var x = obj.options[obj.selectedIndex].value.split("-");
				document.getElementById("bankbranch").value = x[1];
				if (document.getElementById("recoveryId").value == "") {
					var revocery = 0;
				}else{
					var revocery = document.getElementById("recoveryId").value;
				}
				populatebankaccount({
					branchId : x[1] + '&asOnDate=' + date,
					fundId : fund.options[fund.selectedIndex].value
							+ '&recoveryId=' + revocery
				});
			}

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