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


<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/WEB-INF/tags/struts-tags.tld"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<%@ page language="java"%>


<html>
<head>
<style>
table.its th {
	text-align: left;
}
</style>
<title><s:text name="bank.advice.report" /></title>

</head>
<script>
	function doAfterSubmit() {
		document.getElementById('loading').style.display = 'block';
	}
	function populateBankBranch(bank) {
		var bankId = bank.options[bank.selectedIndex].value;
		populatebankbranch({
			bankId : bankId
		})
	}
	function populateBankAccount(branch) {
		var branchId = branch.options[branch.selectedIndex].value;
		populatebankaccount({
			branchId : branchId
		})
	}
	function populateInstrumentNumber(instrument) {
		var bankaccountId = instrument.options[instrument.selectedIndex].value;
		populateinstrumentnumber({
			bankaccountId : bankaccountId
		})
	}
</script>
<body>
	<s:form action="bankAdviceReport" name="bankAdviceReport"
		theme="simple" method="post" onsubmit="javascript:doAfterSubmit()">
		<span class="mandatory1"> <s:actionerror /> <s:fielderror /> <s:actionmessage />
		</span>
		<font style='color: red; font-weight: bold'>
			<p class="error-block" id="lblError"></p>
		</font>
		<div class="formmainbox">
			<div class="subheadnew">
				<s:text name="bank.advice.report" />
			</div>

			<table align="center" width="100%" cellpadding="0" cellspacing="0">
				<tr>
					<td class="bluebox" width="10%">Bank Name:<span
						class="bluebox"><span class="mandatory"></span></span></td>
					<td class="bluebox"><s:select name="bank.id" id="bank.id"
							list="dropdownData.bankList" listKey="id" listValue="name"
							headerKey="-1" headerValue="----Choose----"
							onChange="populateBankBranch(this);" /></td>
					<egov:ajaxdropdown id="bankbranch" fields="['Text','Value']"
						dropdownId="bankbranch"
						url="voucher/common-ajaxLoadBankBranchFromBank.action" />
					<td class="bluebox" width="10%">Bank Branch:<span
						class="bluebox"><span class="mandatory"></span></span></td>
					<td class="bluebox"><s:select name="bankbranch.id"
							id="bankbranch" list="dropdownData.bankBranchList" listKey="id"
							listValue="branchname" headerKey="-1"
							headerValue="----Choose----"
							onChange="populateBankAccount(this);" /></td>
					<egov:ajaxdropdown id="bankaccount" fields="['Text','Value']"
						dropdownId="bankaccount"
						url="voucher/common-ajaxLoadBankAccFromBranch.action" />
				</tr>
				<tr>
					<td class="bluebox" width="10%">Account Number:<span
						class="bluebox"><span class="mandatory"></span></span></td>
					<td class="bluebox"><s:select name="bankaccount.id"
							id="bankaccount" list="dropdownData.bankAccountList" listKey="id"
							listValue="accountnumber" headerKey="-1"
							headerValue="----Choose----"
							onChange="populateInstrumentNumber(this);" /></td>
					<egov:ajaxdropdown id="instrumentnumber" fields="['Text','Value']"
						dropdownId="instrumentnumber"
						url="voucher/common-ajaxLoadRTGSChequeFromBankAcc.action" />
					<td class="bluebox" width="10%">RTGS Number:<span
						class="bluebox"><span class="mandatory"></span></span></td>
					<td class="bluebox"><s:select name="instrumentnumber.id"
							id="instrumentnumber" list="dropdownData.chequeNumberList"
							listKey="id" listValue="transactionNumber" headerKey="-1"
							headerValue="----Choose----" /></td>
				</tr>
				<tr>

				</tr>
			</table>
			<div class="buttonbottom">
				<s:submit method="search" value="Search" cssClass="buttonsubmit"
					onclick="return validate();" />
				<input type="button" value="Close"
					onclick="javascript:window.close()" class="button" />

			</div>
			<br>
			<table width="100%" border="0" cellspacing="0" cellpadding="0">

				<s:if test="%{bankAdviseResultList.size>0}">
					<tr align="center">

						<th class="bluebgheadtd" width="30%">Party Name</th>
						<th class="bluebgheadtd" width="20%">Bank Name</th>
						<th class="bluebgheadtd" width="10%">Account Number</th>
						<th class="bluebgheadtd" width="10%">IFSC Code</th>
						<th class="bluebgheadtd" width="10%">Amount(Rs.)</th>
					</tr>
					<s:iterator value="bankAdviseResultList" status="stat" var="p">
						<tr>
							<td class="blueborderfortd" style="text-align: center"><s:property value="partyName" /></td>
							<td class="blueborderfortd" style="text-align: center"><s:property value="bank" /></td>
							<td class="blueborderfortd" style="text-align: center"><s:property
									value="accountNumber" /></td>
							<td class="blueborderfortd" style="text-align: center"><s:property value="ifscCode" /></td>
							<td class="blueborderfortd" style="text-align: right"><s:property
									value="amount" /></td>
						</tr>
					</s:iterator>
					<tr>
						<td class="blueborderfortd" colspan="5">
					</tr>
					<tr>
						<td class="blueborderfortd" width="10%" colspan="4"
							style="text-align: right">Total
						</th>
						<td class="blueborderfortd" style="text-align: right"><s:property
								value="totalAmount" /></td>
					</tr>



					<div>
						<table width="100%" border="0" cellpadding="0" cellspacing="0">
							<tr>
								<td>&nbsp;</td>
							</tr>
							<tr>
								<td align="center"><input type="button"
									class="buttonsubmit" value="EXPORT PDF" id="exportpdf"
									name="exportpdf" onclick="exportPDF();" /> <input
									type="button" class="buttonsubmit" value="EXPORT EXCEL"
									id="exportpdf" name="exportpdf" onclick="exportExcel();" /> <!-- 	<input type="button" class="buttonsubmit" value="EXPORT HTM" id="exporthtml" name="exportpdf" onclick="exportHtml();"/>-->
								</td>
							</tr>
						</table>
					</div>
					</td>
					</tr>

				</s:if>
				<s:else>
					<!-- <tr>
						<td colspan="7" align="center"><font color="red">No
								record Found.</font></td>

					</tr> -->
				</s:else>
			</table>
		</div>
	</s:form>

	<script>
		function validate() {
			document.bankAdviceReport.action = '/EGF/report/bankAdviceReport-search.action';
			document.bankAdviceReport.submit();
			return true;
		}

		function exportPDF() {
			var bank = document.getElementById("bank.id").value;
			var bankbranch = document.getElementById("bankbranch").value;
			var bankaccount = document.getElementById("bankaccount").value;
			var instrumentnumber = document.getElementById("instrumentnumber").value;
			var url = "${pageContext.request.contextPath}/report/bankAdviceReport-exportPDF.action?bank.id="
					+ bank
					+ "&bankbranch.id="
					+ bankbranch
					+ "&bankaccount.id="
					+ bankaccount
					+ "&instrumentnumber.id=" + instrumentnumber;
			window
					.open(url, '',
							'height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');
		}
		function exportExcel() {
			var bank = document.getElementById("bank.id").value;
			var bankbranch = document.getElementById("bankbranch").value;
			var bankaccount = document.getElementById("bankaccount").value;
			var instrumentnumber = document.getElementById("instrumentnumber").value;
			var url = "${pageContext.request.contextPath}/report/bankAdviceReport-exportExcel.action?bank.id="
					+ bank
					+ "&bankbranch.id="
					+ bankbranch
					+ "&bankaccount.id="
					+ bankaccount
					+ "&instrumentnumber.id=" + instrumentnumber;
			window
					.open(url, '',
							'height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');
		}
		function exportHtml() {
			var bank = document.getElementById("bank.id").value;
			var bankbranch = document.getElementById("bankbranch").value;
			var bankaccount = document.getElementById("bankaccount").value;
			var instrumentnumber = document.getElementById("instrumentnumber").value;
			var url = "${pageContext.request.contextPath}/report/bankAdviceReport-exportHtml.action?bank.id="
					+ bank
					+ "&bankbranch.id="
					+ bankbranch
					+ "&bankaccount.id="
					+ bankaccount
					+ "&instrumentnumber.id=" + instrumentnumber;
			window
					.open(url, '',
							'height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');
		}
	</script>
</body>
</html>
