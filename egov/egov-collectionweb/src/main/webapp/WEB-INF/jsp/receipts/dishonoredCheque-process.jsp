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

<%@ include file="/includes/taglibs.jsp"%>

<%@ page language="java"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title><s:text name="dishonorcheque.title" /></title>
<sj:head jqueryui="true" jquerytheme="redmond" loadAtOnce="true" />
<script type="text/javascript"
	src="/EGF/resources/javascript/helper.js?rnd=${app_release_no}"></script>
<script type="text/javascript">
	function dishonorSelectedChq() {
		var transactionDate = dom.get("transactionDate").value;
		var dishonorReason = dom.get("dishonorReason").value;
		var totalAmount = dom.get("totalAmount").value;
		var reversalAmount = dom.get("reversalAmount").value;

		dom.get("errorDiv").innerHTML = '';
		if (transactionDate == "") {
			dom.get("errorDiv").style.display = '';
			dom.get("errorDiv").innerHTML = 'Please enter Transaction Date.';
			return false;
		}
		if (dishonorReason == -1) {
			dom.get("errorDiv").style.display = '';
			dom.get("errorDiv").innerHTML = 'Please select Dishonor Reason.';
			return false;
		}

		if (Number(totalAmount) != Number(reversalAmount)) {
			dom.get("errorDiv").style.display = '';
			dom.get("errorDiv").innerHTML = 'Total amount should be equal to Instrument amount';
			return false;
		}
		var showGlTotalAmount = 0;
		var showGlTable = dom.get("showGlEntry");
		//jQuery(showGlTable).removeClass("display-hide");
		var amount = document.getElementsByName("amount");
		var showGlDebitAmount = document.getElementsByName("showGlDebitAmount");
		var len = amount.length;
		var totalAmount = 0;
		for (i = 0; i < len; i++) {
			showGlTotalAmount = Number(showGlTotalAmount)
					+ Number(amount[i].value);
			showGlDebitAmount[i].innerHTML = amount[i].value;
		}
		dom.get("showGlDebitTotalAmount").innerHTML = showGlTotalAmount;
		dom.get("showGlCreditTotalAmount").innerHTML = dom
				.get("showRemittanceGlCreditAmount").innerHTML;
		var instHeaderIds = dom.get("instHeaderIds").value;
		var receiptGLDetails = "";
		var remittanceGLDetails = "";

		var showGlGlcode = document.getElementsByName("showGlGlcode");
		var showGlDebitAmount = document.getElementsByName("showGlDebitAmount");
		var showGlCreditAmount = document
				.getElementsByName("showGlCreditAmount");

		var showGlLen = showGlGlcode.length;
		for (i = 0; i < showGlLen; i++) {
			if (!(Number(showGlCreditAmount[i].innerText) <= 0 && Number(showGlDebitAmount[i].innerText) <= 0)) {
				if (receiptGLDetails != "") {

					receiptGLDetails = receiptGLDetails + ","
							+ showGlGlcode[i].innerText + "-"
							+ Number(showGlDebitAmount[i].innerText) + "-"
							+ Number(showGlCreditAmount[i].innerText);
				} else {
					receiptGLDetails = showGlGlcode[i].innerText + "-"
							+ Number(showGlDebitAmount[i].innerText) + "-"
							+ Number(showGlCreditAmount[i].innerText);
				}
			}
		}

		var showRemittanceGlGlcode = document
				.getElementsByName("showRemittanceGlGlcode");
		var showRemittanceGlDebitAmount = document
				.getElementsByName("showRemittanceGlDebitAmount");
		var showRemittanceGlCreditAmount = document
				.getElementsByName("showRemittanceGlCreditAmount");

		var showRemittanceGlLen = showRemittanceGlGlcode.length;
		for (i = 0; i < showRemittanceGlLen; i++) {
			if (!(Number(showRemittanceGlCreditAmount[i].innerText) <= 0 && Number(showRemittanceGlDebitAmount[i].innerText) <= 0)) {
				if (remittanceGLDetails != "") {

					remittanceGLDetails = remittanceGLDetails + ","
							+ showRemittanceGlGlcode[i].innerText + "-"
							+ Number(showRemittanceGlDebitAmount[i].innerText)
							+ "-"
							+ Number(showRemittanceGlCreditAmount[i].innerText);
				} else {
					remittanceGLDetails = showRemittanceGlGlcode[i].innerText
							+ "-"
							+ Number(showRemittanceGlDebitAmount[i].innerText)
							+ "-"
							+ Number(showRemittanceGlCreditAmount[i].innerText);
				}
			}
		}
		var receiptGLDetails1 = dom.get("receiptGLDetails");
		var remittanceGLDetails1 = dom.get("remittanceGLDetails");
		receiptGLDetails1.value = receiptGLDetails;
		remittanceGLDetails1.value = remittanceGLDetails;

		document.dishonorForm.action = '/collection/receipts/dishonoredCheque-create.action';
		document.dishonorForm.submit();
	}
	function updateTotal() {
		var amount = document.getElementsByName("amount");
		var len = amount.length;
		var totalAmount = 0;
		for (i = 0; i < len; i++) {
			totalAmount = Number(totalAmount) + Number(amount[i].value);
		}
		var total = dom.get("totalAmount");
		total.value = totalAmount;

	}

	function showGlEntry() {
		var totalAmount = dom.get("totalAmount").value;
		var reversalAmount = dom.get("reversalAmount").value;
		var showGlTotalAmount = 0;
		if (Number(totalAmount) != Number(reversalAmount)) {
			dom.get("errorDiv").style.display = '';
			dom.get("errorDiv").innerHTML = 'Total amount should be equal to Instrument amount';
			return false;
		} else {
			var showGlTable = dom.get("showGlEntry");
			jQuery(showGlTable).removeClass("display-hide");
			var amount = document.getElementsByName("amount");
			var showGlDebitAmount = document
					.getElementsByName("showGlDebitAmount");
			var len = amount.length;
			var totalAmount = 0;
			for (i = 0; i < len; i++) {
				showGlTotalAmount = Number(showGlTotalAmount)
						+ Number(amount[i].value);
				showGlDebitAmount[i].innerHTML = amount[i].value;
			}
			dom.get("showGlDebitTotalAmount").innerHTML = showGlTotalAmount;
			dom.get("showGlCreditTotalAmount").innerHTML = dom
					.get("showRemittanceGlCreditAmount").innerHTML;
			/* var gLTable = dom.get("gLEntry");
			jQuery(gLTable).addClass("display-hide"); */
		}
	}

	function copyAmountDetails() {

		var receiptDebitAmount = document
				.getElementsByName("receiptDebitAmount");
		var receiptCreditAmount = document
				.getElementsByName("receiptCreditAmount");
		var amount = document.getElementsByName("amount");
		var len = amount.length;
		var debitAmount;
		var creditAmount;
		var totalAmount = 0;
		for (i = 0; i < len; i++) {

			debitAmount = Number(receiptDebitAmount[i].innerHTML);
			creditAmount = Number(receiptCreditAmount[i].innerHTML);
			if (debitAmount > 0) {
				amount[i].value = debitAmount;
				totalAmount = totalAmount + debitAmount;
			} else {
				amount[i].value = creditAmount;
				totalAmount = totalAmount + creditAmount;
			}
		}
		var total = dom.get("totalAmount");
		total.value = totalAmount;
	}
</script>
</head>
<body>
	<s:if test="%{dishonoredChequeDisplayList.size != 0}">
		<table width="70%" border="1" align="center" cellpadding="0"
			cellspacing="0" class="tablebottom">
			<tr>
				<th class="bluebgheadtd">Voucher Number</th>
				<th class="bluebgheadtd">Receipt Number</th>
				<th class="bluebgheadtd">Receipt Date</th>
				<th class="bluebgheadtd">DD/Chq Number</th>
				<th class="bluebgheadtd">DD/Chq Date</th>
				<th class="bluebgheadtd">DD/Chq Amount</th>
				<th class="bluebgheadtd">Bank</th>
				<th class="bluebgheadtd">Bank Account Number</th>
				<th class="bluebgheadtd">Status</th>
				<th class="bluebgheadtd">Reference No</th>
			</tr>
			<s:iterator var="p" value="dishonoredChequeDisplayList" status="s">
				<tr>
					<td class="blueborderfortd" align="center"><s:property
							value="%{voucherNumber}" /></td>
					<td class="blueborderfortd" align="center"><s:property
							value="%{receiptNumber}" /></td>
					<td class="blueborderfortd text-center" align="center"
						id="debitAmount"><s:property value="%{receiptDate}" /></td>
					<td class="blueborderfortd text-right" align="center"><s:property
							value="%{instrumentNumber}" /></td>
					<td class="blueborderfortd" align="right"><s:property
							value="%{instrumentDate}" /></td>
					<td class="blueborderfortd" align="right"><s:property
							value="%{instrumentAmount}" /></td>
					<td class="blueborderfortd" align="right"><s:property
							value="%{bankName}" /></td>
					<td class="blueborderfortd" align="right"><s:property
							value="%{accountNumber}" /></td>
					<td class="blueborderfortd" align="right"><s:property
							value="%{description}" /></td>
					<td class="blueborderfortd" align="right"><s:property
							value="%{referenceNo}" /></td>
				</tr>
			</s:iterator>
		</table>
	</s:if>
	<s:form name="dishonorForm" action="dishonoredCheque" theme="simple"
		validate="true">
		<div style="color: green">
			<s:actionmessage />
		</div>
		<div style="color: red">
			<s:actionerror />
		</div>
		<div style="color: red">
			<div class="errorstyle" style="display: none" id="errorDiv"></div>
		</div>
		<div class="formmainbox">
			<div class="formheading">
				<div class="subheadnew">
					<s:text name="dishonorcheque.title" />
				</div>
			</div>
			<br />
			<s:if test="%{generalLedger.size != 0}">
				<div class="formmainbox">
					<table width="100%" border="0" cellspacing="0" cellpadding="0">
						<tr>
							<td class="greybox"></td>
							<td class="greybox"><s:text
									name="dishonorcheque.transactiondate" />:<span
								class="mandatory1">*</span></td>
							<td class="greybox"><s:textfield id="transactionDate"
									name="transactionDate" value="%{transactionDate}"
									data-date-end-date="0d"
									onkeyup="DateFormat(this,this.value,event,false,'3')"
									placeholder="DD/MM/YYYY" cssClass="form-control datepicker"
									data-inputmask="'mask': 'd/m/y'" />
							<td class="greybox"><s:text
									name="dishonorcheque.reversalamount" />:</td>
							<td class="greybox"><s:textfield name="reversalAmount"
									style="width: 200px;" id="reversalAmount"
									cssClass="patternvalidation text-right form-control"
									data-pattern="number" value="%{reversalAmount}" readonly="true" /></td>
						</tr>

						<tr>
							<td class="bluebox"></td>
							<td class="greybox"><s:text name="dishonorcheque.reason" />:<span
								class="mandatory1">*</span></td>
							<td class="greybox"><s:select name="dishonorReason"
									id="dishonorReason" list="dropdownData.dishonorReasonsList"
									headerKey="-1" headerValue="---Choose---" /></td>
							<td class="greybox"><s:text name="dishonorcheque.remarks" />:</td>
							<td class="greybox"><s:textarea name="remarks"
									style="width: 200px;" id="remarks" rows="4" /></td>
						</tr>

					</table>
				</div>
				</br>
				</br>
				<div id="gLEntry">
					<table width="70%" border="1" align="center" cellpadding="0"
						cellspacing="0" class="tablebottom">
						<tr>
							<th class="bluebgheadtd">Account Code</th>
							<th class="bluebgheadtd">Description</th>
							<th class="bluebgheadtd">Debit Amount</th>
							<th class="bluebgheadtd">Credit Amount</th>
							<th class="bluebgheadtd">Reversal Amount</th>
						</tr>
						<s:iterator var="p" value="generalLedger" status="s">
							<tr>
								<td class="blueborderfortd" align="center"><s:property
										value="%{glcode}" /></td>
								<td class="blueborderfortd" align="center"><s:property
										value="%{description}" /></td>
								<td class="blueborderfortd text-right" align="center"
									id="debitAmount"><div id="receiptDebitAmount"
										name="receiptDebitAmount">
										<s:property value="%{debitAmount}" />
									</div></td>
								<td class="blueborderfortd text-right" align="center"><div
										id="receiptCreditAmount" name="receiptCreditAmount">
										<s:property value="%{creditAmount}" />
									</div></td>
								<td class="blueborderfortd" align="right"><s:textfield
										name="amount" id="amount" value="0" onblur="updateTotal()"
										cssClass="patternvalidation text-right form-control"
										data-pattern="number" /></td>
							</tr>
						</s:iterator>
						<tr>
							<td class="blueborderfortd text-right" align="right" colspan="4">Total</td>
							<td class="blueborderfortd "><s:textfield name="totalAmount"
									id="totalAmount" value="0" readonly="true"
									cssClass="patternvalidation text-right form-control"
									data-pattern="number" /></td>
						</tr>
					</table>
					<div class="buttonbottom">
						<td><input type="button" class="button" value="Show GLEntry"
							id="showGlEntryButton" name="button"
							onclick="return showGlEntry();" />&nbsp;</td>
						<td><input type="button" class="button"
							value="Copy Amount Details" id="copyAmountDetailsButton"
							name="button" onclick="return copyAmountDetails();" />&nbsp;</td>
					</div>
				</div>
				<div id="showGlEntry" class="display-hide">
					<table width="70%" border="1" align="center" cellpadding="0"
						cellspacing="0" class="tablebottom">
						<tr>
							<th class="bluebgheadtd">Account Code</th>
							<th class="bluebgheadtd">Description</th>
							<th class="bluebgheadtd">Debit Amount</th>
							<th class="bluebgheadtd">Credit Amount</th>
						</tr>
						<s:iterator var="p" value="generalLedger" status="s">
							<tr>
								<td class="blueborderfortd" align="center"><div
										name="showGlGlcode" id="showGlGlcode">
										<s:property value="%{glcode}" />
									</div></td>
								<td class="blueborderfortd" align="center"><s:property
										value="%{description}" /></td>
								<td class="blueborderfortd" align="right"><div
										class="text-right" name="showGlDebitAmount"
										id="showGlDebitAmount">
										<%-- <s:property value="%{debitAmount}" /> --%>
									</div></td>
								<td class="blueborderfortd" align="right"><div
										class="text-right" id="showGlCreditAmount"
										name="showGlCreditAmount">
										<s:property value="%{creditAmount}" />
									</div></td>
							</tr>
						</s:iterator>
						<s:iterator var="p" value="remittanceGeneralLedger" status="s">
							<tr>
								<td class="blueborderfortd" align="center"><div
										name="showRemittanceGlGlcode" id="showRemittanceGlGlcode">
										<s:property value="%{glcode}" />
									</div></td>
								<td class="blueborderfortd" align="center"><s:property
										value="%{description}" /></td>
								<td class="blueborderfortd" align="right"><div
										name="showRemittanceGlDebitAmount" class="text-right"
										id="showRemittanceGlDebitAmount">
										<s:property value="%{debitAmount}" />
									</div></td>
								<td class="blueborderfortd" align="right">
									<div name="showRemittanceGlCreditAmount" class="text-right"
										id="showRemittanceGlCreditAmount">
										<s:property value="%{reversalAmount}" />
									</div>
								</td>
							</tr>
						</s:iterator>
						<tr>
							<td class="blueborderfortd text-right" align="right" colspan="2">Total</td>
							<td class="blueborderfortd" align="right"><div
									name="showGlDebitTotalAmount" id="showGlDebitTotalAmount"
									class="text-right"></div></td>
							<td class="blueborderfortd" align="right"><div
									name="showGlCreditTotalAmount" id="showGlCreditTotalAmount"
									class="text-right"></div></td>
						</tr>
					</table>

				</div>
				<div class="buttonbottom">
					<td><input type="submit" class="buttonsubmit" value="Dishonor"
						id="dishonorButton" name="button"
						onclick="return dishonorSelectedChq();" />&nbsp;</td>
					<!-- <td><input type="submit" class="button" value="Copy Amount Details"
						id="dishonorButton" name="button"
						onclick="return copyAmountDetails();" />&nbsp;</td> -->
				</div>
			</s:if>
			<s:hidden name="instHeaderIds" id="instHeaderIds"
				value="%{instHeaderIds}" />
			<s:hidden name="voucherHeaderIds" id="voucherHeaderIds"
				value="%{voucherHeaderIds}" />
			<s:hidden name="receiptHeaderIds" id="receiptHeaderIds"
				value="%{receiptHeaderIds}" />
			<s:hidden name="receiptGLDetails" id="receiptGLDetails"
				value="%{receiptGLDetails}" />
			<s:hidden name="remittanceGLDetails" id="remittanceGLDetails"
				value="%{remittanceGLDetails}" />
			<s:hidden name="referenceNo" id="referenceNo" value="%{referenceNo}" />
	</s:form>
</body>
</html>