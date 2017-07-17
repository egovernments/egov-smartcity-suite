<%--
  ~ eGov suite of products aim to improve the internal efficiency,transparency,
  ~    accountability and the service delivery of the government  organizations.
  ~
  ~     Copyright (C) <2015>  eGovernments Foundation
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
  --%>


<%@ include file="/includes/taglibs.jsp"%>
<%@ page language="java"%>
<html>
<head>
<link rel="stylesheet"
	href="/EGF/resources/css/tabber.css?rnd=${app_release_no}"
	TYPE="text/css">
	<script type="text/javascript"
	src="${pageContext.request.contextPath}/resources/javascript/voucherHelper.js?rnd=${app_release_no}"></script>
<script type="text/javascript"
	src="/EGF/resources/javascript/tabber.js?rnd=${app_release_no}"></script>
<script type="text/javascript"
	src="/EGF/resources/javascript/tabber2.js?rnd=${app_release_no}"></script>
</head>
<script language="javascript">

</script>
</head>
<body onload="onLoad();">
	<br>
	<s:form action="payment" theme="simple">
		<s:token />
		<div class="formmainbox">
			<jsp:include page="../budget/budgetHeader.jsp">
				<jsp:param name="heading" value="Bill Payment" />
			</jsp:include>

			<span class="mandatory1"> <s:actionerror /> <s:fielderror />
				<s:actionmessage />
			</span>
			<div class="subheadnew">Bill Payment</div>
			<div id="budgetSearchGrid" style="display: block; width: 100%;">
				<table width="100%" cellpadding="0" cellspacing="0" border="0">
					<tr>
						<td>
							<div align="left">
								<br />
								<table border="0" cellspacing="0" cellpadding="0" width="100%">
									<tr>
										<td>
											<div class="tabber">
												<div class="tabbertab">
													<h2>Payment Details</h2>
													<span>
														<table width="100%" border="0" cellspacing="0"
															cellpadding="0">
															<tr>
																<td colspan="6"><div class="subheadsmallnew"
																		style="border: 0;">Payment Details</div></td>
															</tr>
															<tr>
																<td width="9%" class="bluebox"><s:hidden
																		name="billregister.id" /></td>
																<s:if test="%{shouldShowHeaderField('fund')}">
																	<td width="12%" class="bluebox"><strong><s:text
																				name="voucher.fund" /></strong> <s:if
																			test="%{isFieldMandatory('fund')}">
																			<span class="bluebox"><span class="mandatory1">*</span></span>
																		</s:if></td>
																	<td width="20%" class="bluebox"><s:property
																			value="%{billregister.egBillregistermis.fund.name}" /></td>
																</s:if>
																<s:else>
																	<td class="greybox">
																	<td class="greybox">
																</s:else>
																<s:if test="%{shouldShowHeaderField('fundsource')}">
																	<td width="17%" class="bluebox"><strong><s:text
																				name="voucher.fundsource" /></strong> <s:if
																			test="%{isFieldMandatory('fundsource')}">
																			<span class="bluebox"><span class="mandatory1">*</span></span>
																		</s:if></td>
																	<td width="33%" class="bluebox"><s:property
																			value="%{billregister.egBillregistermis.fundsource.name}" /></td>
																</s:if>
																<s:else>
																	<td class="greybox">
																	<td class="greybox">
																</s:else>
															</tr>
															<tr>
																<td class="greybox">&nbsp;</td>
																<s:if test="%{shouldShowHeaderField('department')}">
																	<td class="greybox"><strong><s:text
																				name="voucher.department" /></strong> <s:if
																			test="%{isFieldMandatory('department')}">
																			<span class="bluebox"><span class="mandatory1">*</span></span>
																		</s:if></td>
																	<td class="greybox"><s:select name="department"
																			id="department" list="dropdownData.departmentList"
																			listKey="id" listValue="name" headerKey="-1"
																			headerValue="----Choose----"
																			value="%{billregister.egBillregistermis.egDepartment.id}" /></td>
																	<%-- <s:property
																	value="%{billregister.egBillregistermis.egDepartment.name}" /> --%>
																</s:if>
																<s:else>
																	<td class="greybox">
																	<td class="greybox">
																</s:else>
																<s:if test="%{shouldShowHeaderField('functionary')}">
																	<td class="greybox"><strong><s:text
																				name="voucher.functionary" /></strong> <s:if
																			test="%{isFieldMandatory('functionary')}">
																			<span class="bluebox"><span class="mandatory1">*</span></span>
																		</s:if></td>
																	<td class="greybox" colspan="4"><s:property
																			value="%{billregister.egBillregistermis.functionaryid.name}" /></td>
																</s:if>
																<s:else>
																	<td class="greybox">
																	<td class="greybox">
																</s:else>
															</tr>
															<tr>
																<td class="bluebox">&nbsp;</td>
																<s:if test="%{shouldShowHeaderField('scheme')}">
																	<td class="bluebox"><strong><s:text
																				name="voucher.scheme" /></strong> <s:if
																			test="%{isFieldMandatory('scheme')}">
																			<span class="mandatory1">*</span>
																		</s:if></td>
																	<td class="bluebox"><s:property
																			value="%{billregister.egBillregistermis.scheme.name}" /></td>
																</s:if>
																<s:else>
																	<td class="greybox">
																	<td class="greybox">
																</s:else>
																<s:if test="%{shouldShowHeaderField('subscheme')}">
																	<td class="bluebox"><strong><s:text
																				name="voucher.subscheme" /></strong> <s:if
																			test="%{isFieldMandatory('subscheme')}">
																			<span class="mandatory1">*</span>
																		</s:if></td>
																	<td class="bluebox"><s:property
																			value="%{billregister.egBillregistermis.subScheme.name}" /></td>
																</s:if>
																<s:else>
																	<td class="greybox">
																	<td class="greybox">
																</s:else>
															</tr>
															<tr>
																<td class="greybox">&nbsp;</td>
																<s:if test="%{shouldShowHeaderField('function')}">
																	<td class="greybox"><strong><s:text
																				name="voucher.function" /></strong> <s:if
																			test="%{isFieldMandatory('function')}">
																			<span class="mandatory1">*</span>
																		</s:if></td>
																	<td class="greybox"><s:select name="function"
																			id="function" list="dropdownData.functionList"
																			listKey="id" listValue="name" headerKey="-1"
																			headerValue="----Choose----"
																			value="%{billregister.egBillregistermis.function.id}" />
																		<%--  <s:property
																		value="%{billregister.egBillregistermis.function.name}" /> --%></td>
																</s:if>
																<s:else>
																	<td class="greybox">
																	<td class="greybox">
																</s:else>
																<td class="greybox">&nbsp;</td>
																<s:if test="%{shouldShowHeaderField('field')}">
																	<td class="greybox"><strong><s:text
																				name="voucher.field" /></strong> <s:if
																			test="%{isFieldMandatory('field')}">
																			<span class="mandatory1">*</span>
																		</s:if></td>
																	<td class="greybox" colspan="4"><s:property
																			value="%{billregister.egBillregistermis.fieldid.name}" /></td>
																</s:if>
																<s:else>
																	<td class="greybox">
																	<td class="greybox">
																</s:else>
															</tr>
															<tr>
																<td class="bluebox">&nbsp;</td>
																<td class="bluebox"><strong><s:text
																			name="payment.mode" /></strong></td>
																<td class="bluebox"><s:if
																		test="%{paymentMode == 'cash' || paymentMode == 'Cash'}">
																		<s:text name="cash.consolidated.cheque" />
																	</s:if> <s:else>
																		<s:text name="%{paymentMode}" />
																	</s:else></td>
																<td class="bluebox"><strong><s:text
																			name="payment.amount" /></strong></td>
																<td class="bluebox" colspan="2"><span
																	id="paymentAmountspan" /></td>
															</tr>
															<tr>
																<td class="greybox">&nbsp;</td>
																<s:if test="%{shouldShowHeaderField('vouchernumber')}">
																	<td class="greybox"><s:text
																			name="payment.voucherno" /><span class="mandatory1">*</span></td>
																	<td class="greybox"><s:textfield
																			name="vouchernumber" id="vouchernumber"
																			value="%{vouchernumber}" /></td>
																</s:if>
																<s:else>
																	<td class="greybox" />
																	<td class="greybox" />
																</s:else>
																<td class="greybox"><s:text
																		name="payment.voucherdate" /><span class="mandatory1">*</span></td>
																<td class="greybox" colspan="2"><s:textfield
																		id="voucherdate" name="voucherdate"
																		value="%{voucherdate}" data-date-end-date="0d"
																		onkeyup="DateFormat(this,this.value,event,false,'3')"
																		placeholder="DD/MM/YYYY"
																		class="form-control datepicker"
																		data-inputmask="'mask': 'd/m/y'" /></td>
															</tr>
															<s:if test='%{billSubType.equalsIgnoreCase("TNEB")}'>
																<tr>
																	<td class="bluebox">&nbsp;</td>
																	<td class="bluebox"><s:text name="payment.bank" /><span
																		class="mandatory1">*</span></td>
																	<td class="bluebox"><s:property
																			value="%{bank_branch}" /></td>
																	<td class="bluebox"><s:text
																			name="payment.bankaccount" /><span
																		class="mandatory1">*</span></td>
																	<td class="bluebox"><s:property
																			value="%{bank_account}" /></td>
																	<s:hidden name="bankbranch" id="bankbranch" />
																	<s:hidden name="bank_branch" id="bank_branch" />
																	<s:hidden name="bank_account" id="bank_account" />
																	<s:hidden name="bankaccount" id="bankaccount" />
																</tr>
															</s:if>
															<s:else>
																<tr>
																	<td class="bluebox">&nbsp;</td>
																	<td class="bluebox"><s:text name="payment.bank" /><span
																		class="mandatory1">*</span></td>
																	<td class="bluebox"><s:select name="bankbranch"
																			id="bankbranch" list="dropdownData.bankbranchList"
																			listKey="id" listValue="bank.name+'-'+branchname"
																			headerKey="-1" headerValue="----Choose----"
																			onchange="loadBankAccount(this)"
																			value="%{bankbranch}" /></td>
																	<egov:ajaxdropdown id="bankaccount"
																		fields="['Text','Value']" dropdownId="bankaccount"
																		url="voucher/common-ajaxLoadBankAccounts.action" />
																	<td class="bluebox"><s:text
																			name="payment.bankaccount" /><span
																		class="mandatory1">*</span></td>
																	<td class="bluebox" colspan="2"><s:select
																			name="bankaccount" id="bankaccount"
																			list="dropdownData.bankaccountList" listKey="id"
																			listValue="accountnumber+'---'+accounttype"
																			headerKey="-1" headerValue="----Choose----"
																			onChange="populateAvailableBalance(this);"
																			value="%{bankaccount}" /></td>
																	<egov:updatevalues id="availableBalance"
																		fields="['Text']"
																		url="payment/payment-ajaxGetAccountBalance.action" />
																</tr>
																<tr id="bankbalanceRow">
																	<td class="bluebox">&nbsp;</td>
																	<td class="bluebox">&nbsp;</td>
																	<td class="bluebox">&nbsp;</td>
																	<td class="bluebox" width="15%"><strong><s:text
																				name="payment.balance" />(Rs)</strong></td>
																	<td class="bluebox" colspan="4"><s:textfield
																			name="availableBalance" id="availableBalance"
																			readonly="true" style="text-align:right" /></td>
																</tr>
															</s:else>
															<tr>
																<td class="greybox">&nbsp;</td>
																<td class="greybox" width="15%"><s:text
																		name="payment.narration" /></td>
																<td class="greybox" colspan="4"><s:textarea
																		name="description" id="description" cols="70" rows="4"
																		onblur="checkLength(this)" /></td>
															</tr>
															<tr>
																<td class="bluebox">&nbsp;</td>
																<td class="bluebox" style="visibility: hidden"><s:text
																		name="payment.balance" /></td>
																<td class="bluebox"><s:hidden name="balance"
																		id="balance" readonly="true" style="text-align:right" /></td>
																<s:hidden name="functionSel" id="functionSel"
																	value="%{functionSel}" />

															</tr>
															<tr>
																<td colspan="6" align="center">
																	<div class="buttonbottom">
																		<s:hidden name="hiddenText" id="hiddenText" />
																		<s:hidden name="paymentMode" id="paymentMode"
																			value="%{paymentMode}" />
																		<s:hidden name="contractorIds" id="contractorIds"
																			value="%{contractorIds}" />
																		<s:hidden name="supplierIds" id="supplierIds"
																			value="%{supplierIds}" />
																		<s:hidden name="contingentIds" id="contingentIds"
																			value="%{contingentIds}" />
																		<s:hidden name="salaryIds" id="salaryIds"
																			value="%{salaryIds}" />
																		<s:hidden name="pensionIds" id="pensionIds"
																			value="%{pensionIds}" />

																	</div>
																</td>
															</tr>
														</table>
													</span>
												</div>
												<div class="tabbertab">
													<h2>Bill Details</h2>
													<span>
														<table align="center" border="0" cellpadding="0"
															cellspacing="0" class="newtable">
															<tr>
																<td colspan="7"><div class="subheadsmallnew">Bill
																		Details</div></td>
															</tr>
															<tr>
																<td colspan="7">
																	<div style="float: left; width: 100%;">
																		<table id="billdetailsTable" align="center" border="0"
																			cellpadding="0" cellspacing="0" width="100%">
																			<tr>
																				<th class="bluebgheadtdnew">Bill Number
																				</th>
																				<th class="bluebgheadtdnew">Bill Date
																				</th>
																				<th class="bluebgheadtdnew">Bill Voucher Number
																				</th>
																				<th class="bluebgheadtdnew">Bill Voucher Date
																				</th>
																				<th class="bluebgheadtdnew">Payee Name
																				</th>
																				<th class="bluebgheadtdnew">Net Amount
																				</th>
																				<th class="bluebgheadtdnew">Earlier Payment
																				</th>
																				<th class="bluebgheadtdnew">Payable Amount
																				</th>
																				<th class="bluebgheadtdnew">Payment Amount
																				</th>
																			</tr>
																			<s:if test="%{billList.size>0}">
																				<s:iterator var="p" value="billList" status="s">
																					<tr>
																						<td style="text-align: center"
																							class="blueborderfortdnew"><s:hidden
																								name="billList[%{#s.index}].csBillId"
																								id="csBillId%{#s.index}" value="%{csBillId}" />
																							<s:hidden name="billList[%{#s.index}].billNumber"
																								id="billNumber" value="%{billNumber}" /> <s:property
																								value="%{billNumber}" /></td>
																						<td style="text-align: center"
																							class="blueborderfortdnew"><s:hidden
																								name="billList[%{#s.index}].billDate"
																								id="billDate%{#s.index}" value="%{billDate}" />
																							<s:date name="%{billDate}" format="dd/MM/yyyy" /></td>
																						<td style="text-align: center"
																							class="blueborderfortdnew"><s:hidden
																								name="billList[%{#s.index}].billVoucherId"
																								id="billVoucherId%{#s.index}" value="%{billVoucherId}" />
																							<s:hidden name="billList[%{#s.index}].billVoucherNumber"
																								id="billNumber" value="%{billVoucherNumber}" />
																								<a href="#" onclick="openVoucher('<s:property value='%{billVoucherId}'/>');">
																								 	<s:property value="%{billVoucherNumber}" />
																								 </a>
																						</td>
																						<td style="text-align: center"
																							class="blueborderfortdnew"><s:hidden
																								name="billList[%{#s.index}].billVoucherDate"
																								id="billVoucherDate%{#s.index}" value="%{billVoucherDate}" />
																							<s:date name="%{billVoucherDate}" format="dd/MM/yyyy" /></td>
																						<td style="text-align: center"
																							class="blueborderfortdnew"><s:hidden
																								name="billList[%{#s.index}].expType"
																								id="expType%{#s.index}" value="%{expType}" /> <s:hidden
																								name="billList[%{#s.index}].payTo"
																								id="payTo%{#s.index}" value="%{payTo}" /> <s:property
																								value="%{payTo}" /></td>
																						<td style="text-align: right"
																							class="blueborderfortdnew"><s:hidden
																								name="billList[%{#s.index}].netAmt"
																								id="netAmt%{#s.index}" value="%{netAmt}" /> <s:text
																								name="payment.format.number">
																								<s:param value="%{netAmt}" />
																							</s:text></td>
																						<td style="text-align: right"
																							class="blueborderfortdnew"><s:hidden
																								name="billList[%{#s.index}].earlierPaymentAmt"
																								id="earlierPaymentAmt%{#s.index}"
																								value="%{earlierPaymentAmt}" /> <s:text
																								name="payment.format.number">
																								<s:param value="%{earlierPaymentAmt}" />
																							</s:text></td>
																						<td style="text-align: right"
																							class="blueborderfortdnew"><s:hidden
																								name="billList[%{#s.index}].payableAmt"
																								id="payableAmt%{#s.index}" value="%{payableAmt}" />
																							<s:text name="payment.format.number">
																								<s:param value="%{payableAmt}" />
																							</s:text></td>
																						<s:if
																							test="%{expType == finConstExpendTypeContingency}">

																							<td class="blueborderfortdnew"><div
																									align="center">
																									<input type="text"
																										name='billList[<s:property value="%{#s.index}"/>].paymentAmt'
																										value='<s:text name="payment.format.number" ><s:param value="%{paymentAmt}"/></s:text>'
																										id='paymentAmt<s:property value="%{#s.index}"/>'
																										style="text-align: right" readonly />
																									<!-- <s:textfield name="billList[%{#s.index}].paymentAmt" id="paymentAmt%{#s.index}" value="%{getText('payment.format.number',{'paymentAmt'})}" style="text-align:right" readonly="true"/> -->
																								</div></td>
																						</s:if>
																						<s:else>
																							<td class="blueborderfortdnew"><div
																									align="center">
																									<input type="text"
																										name='billList[<s:property value="%{#s.index}"/>].paymentAmt'
																										value='<s:text name="payment.format.number" ><s:param value="%{paymentAmt}"/></s:text>'
																										id='paymentAmt<s:property value="%{#s.index}"/>'
																										style="text-align: right"
																										onchange="calcGrandTotal(this)"
																										onfocus="updateHidden(this)" />
																									<!-- <s:textfield name="billList[%{#s.index}].paymentAmt" id="paymentAmt%{#s.index}" value="%{paymentAmt}" style="text-align:right" onchange="calcGrandTotal(this)" onfocus="updateHidden(this)"/> -->
																								</div></td>
																						</s:else>
																						<c:set var="totalAmt"
																							value="${totalAmt+paymentAmt}" />
																					</tr>
																				</s:iterator>
																			</s:if>

																			<tr>
																				<td style="text-align: right" colspan="8"
																					class="blueborderfortdnew"><strong>Grand
																						Total</strong></td>
																				<td style="text-align: right"
																					class="blueborderfortdnew"><div align="center">
																						<input type="text" name="grandTotal"
																							id="grandTotal"
																							value='<fmt:formatNumber value='${totalAmt}' pattern='#0.00' />'
																							style="text-align: right" readonly />
																					</div></td>
																				<s:hidden name="billListSize" id="billListSize"
																					value="%{billList.size}" />
																			</tr>
																		</table>
																	</div>
																</td>
															</tr>
														</table>
													</span>
												</div>
												<!-- individual tab -->
												<s:if test="%{disableExpenditureType}">
													<div>
														<s:text name="change.party.name" />
														<s:checkbox name="changePartyName" id="changePartyName"
															checked="checked" />
														<s:textfield name="newPartyName" id="newPartyName" />
													</div>
												</s:if>
											</div> <!-- tabbber div -->
										</td>
									</tr>
								</table>
							</div>
						</td>
					</tr>
				</table>
				<s:hidden name="cutOffDate" id="cutOffDate" />
				<s:hidden name="bankBalanceCheck" id="bankBalanceCheck" value="%{bankBalanceCheck}" />
			 
				<%@ include file='../payment/commonWorkflowMatrix.jsp'%>
				<%@ include file='../workflow/commonWorkflowMatrix-button.jsp'%>
			</div>

			<div class="buttonbottom" id="buttondiv">
				<s:hidden name="paymentid" value="%{paymentheader.id}" />
				<s:hidden name="actionname" id="actionName" value="%{action}" />
				<s:hidden name="billSubType" id="billSubType" value="%{billSubType}" />
			</div>

			<script>
			jQuery(document).ready(function() {
				if(document.getElementById('approverDepartment'))
					document.getElementById('approverDepartment').value = "-1";
				});
			 
		function back(){
			window.location = "/EGF/payment/payment-beforeSearch.action?salaryType";
			return true;
		}
		
		var vFixedDecimal = 2;
		function loadBankAccount(obj)
		{
			var fund = 0;
			<s:if test="%{shouldShowHeaderField('fund')}">
				fund = <s:property value="%{billregister.egBillregistermis.fund.id}"/>;
			</s:if>
			var vTypeOfAccount = '<s:property value="%{typeOfAccount}"/>';
			var billSubType = '<s:property value="%{billSubType}"/>';
			populatebankaccount({branchId:obj.options[obj.selectedIndex].value+'&date='+new Date(), typeOfAccount:vTypeOfAccount,fundId:fund,billSubType:billSubType} );
			//populatebankaccount({branchId:obj.options[obj.selectedIndex].value+'&date='+new Date()});
		}
		function updateHidden(obj)
		{
			if(obj.value=='' || isNaN(obj.value))
				document.getElementById('hiddenText').value=0;
			else
				document.getElementById('hiddenText').value=obj.value;
		}
		
		function calcGrandTotal(obj)
		{
			var vBillListSize = document.getElementById('billListSize').value;
			var index = obj.id.substring(10,obj.id.length);
			var putBackAmount = parseFloat(document.getElementById('payableAmt'+index).value);
			var paymentAmount = obj.value;
			if(paymentAmount == '' || isNaN(paymentAmount)) {
				bootbox.alert('Payment amount should be a numeric value.');
				obj.value = putBackAmount.toFixed(vFixedDecimal);
			}
			
			if(paymentAmount > parseFloat(document.getElementById('payableAmt'+index).value) ) {
				bootbox.alert('Payment amount should not be greater than Payable amount');
				obj.value = putBackAmount.toFixed(vFixedDecimal);
			}
			
			paymentAmount = obj.value;
			var vFinalGrandTotal = 0;
			obj.value = parseFloat(paymentAmount).toFixed(vFixedDecimal);
			for(var i = 0; i < vBillListSize; i++) {
				if(index == i) vFinalGrandTotal += parseFloat(paymentAmount);
				else vFinalGrandTotal += parseFloat(document.getElementById('paymentAmt'+i).value);
			}
			document.getElementById('grandTotal').value = vFinalGrandTotal.toFixed(vFixedDecimal);
			document.getElementById('paymentAmountspan').innerHTML = document.getElementById('grandTotal').value;
		}


		function populateAvailableBalance(accnumObj) 
		{
					if (document.getElementById('voucherdate').value == '') {
						bootbox.alert("Please Select the Voucher Date!!");
						accnumObj.options.value = -1;
						return;
					}
					if (accnumObj.options[accnumObj.selectedIndex].value == -1)
						document.getElementById('availableBalance').value = '';
					else
						populateavailableBalance({
							bankaccount : accnumObj.options[accnumObj.selectedIndex].value,
							voucherDate : document.getElementById('voucherdate').value
									+ '&date=' + new Date()
						});

		}
		var callback = {
				success : function(o) {
				console.log("success");
				document.getElementById('availableBalance').value = o.responseText;
				},
				failure : function(o) {
					console.log("failed");
				}
		}
		function balanceCheck() {

			if (document.getElementById('availableBalance')) {
				console.log("ins did");
				console.log(parseFloat(document.getElementById('grandTotal').value));
				console.log(parseFloat(document.getElementById('availableBalance').value));
				
				if(parseFloat(document.getElementById('grandTotal').value)>parseFloat(document.getElementById('availableBalance').value))
				{
					console.log("ins 44");
					return false;
				}
			}
			return true;
		} 
			
		function onLoad(){
			if (jQuery("#bankBalanceCheck") == null || jQuery("#bankBalanceCheck").val() == "") {
				disableForm();
			}
		}
		function onSubmit()
		{
			doLoadingMask();
			var balanceCheckMandatory='<s:text name="payment.mandatory"/>';
			var balanceCheckWarning='<s:text name="payment.warning"/>';
			var noBalanceCheck='<s:text name="payment.none"/>';
			if(dom.get('department').value=='-1')
			{
				bootbox.alert("Please Select the Department!!");
				undoLoadingMask();
				return false;
			}
			if(dom.get('function').value=='-1')
			{
				bootbox.alert("Please Select the Function!!");
				undoLoadingMask();
				return false;
			}
			
			if(dom.get('vouchernumber') && dom.get('vouchernumber').value=='')
			{
				bootbox.alert('Please Enter voucher number');
				undoLoadingMask();
				return false;
			}
			if(dom.get('voucherdate').value=='')
			{
				bootbox.alert("Please Select the Voucher Date!!");
				undoLoadingMask();
				return false;
			}
			if(dom.get('billSubType').value!='TNEB')
			{
				if(dom.get('bankbranch').options[dom.get('bankbranch').selectedIndex].value==-1)
				{
					bootbox.alert("Please Select the Bank!!");
					undoLoadingMask();
					return false;
				}
				if(dom.get('bankaccount').options[dom.get('bankaccount').selectedIndex].value==-1)
				{
					bootbox.alert("Please Select the Bank Account");
					undoLoadingMask();
					return false;
				}
			}
		
			if(document.getElementById('grandTotal').value==0 || document.getElementById('grandTotal').value=='NaN')
			{
				bootbox.alert('Payment Amount should be greater than zero!');
				dom.get('tabber1').onclick();
				undoLoadingMask();
				return false;
			}
 			<s:if test="%{disableExpenditureType}">
				if(dom.get("changePartyName") && dom.get("changePartyName").checked==true)
				{
					if(dom.get("newPartyName").value=='')
					{
						bootbox.alert('Enter Party Name to Chnage');
						dom.get("newPartyName").focus();
						undoLoadingMask();
						return false;
					}
				}
			</s:if>
			var balanceCheckMandatory='<s:text name="payment.mandatory"/>';
			var balanceCheckWarning='<s:text name="payment.warning"/>';
			var noBalanceCheck='<s:text name="payment.none"/>';
			if(jQuery("#bankBalanceCheck").val()==noBalanceCheck)
			{
			 document.forms[0].action='${pageContext.request.contextPath}/payment/payment-create.action';
			 return true;
			}
			else if(!balanceCheck() && jQuery("#bankBalanceCheck").val()==balanceCheckMandatory){
					 bootbox.alert("Insufficient Bank Balance.");
					 undoLoadingMask();
					 return false;
					}
			else if(!balanceCheck() && jQuery("#bankBalanceCheck").val()==balanceCheckWarning){
					 var msg = confirm("Insufficient Bank Balance. Do you want to process ?");
					 if (msg == true) {
						 document.forms[0].action='${pageContext.request.contextPath}/payment/payment-create.action';
						return true;
					 } else {
						 undoLoadingMask();
					   	return false;
						}
				}
			else
			{
				document.forms[0].action = '${pageContext.request.contextPath}/payment/payment-create.action';
				return true;
			}
		}  
		
		function validateCutOff()
		{
		var cutOffDatePart=document.getElementById("cutOffDate").value.split("/");
		var voucherDatePart=document.getElementById("voucherdate").value.split("/");
		var cutOffDate = new Date(cutOffDatePart[1] + "/" + cutOffDatePart[0] + "/"
				+ cutOffDatePart[2]);
		var voucherDate = new Date(voucherDatePart[1] + "/" + voucherDatePart[0] + "/"
				+ voucherDatePart[2]);
		if(voucherDate<=cutOffDate)
		{
			return true;
		}
		else{
			var msg1='<s:text name="wf.vouchercutoffdate.message"/>';
			var msg2='<s:text name="wf.cutoffdate.msg"/>';
			bootbox.alert(msg1+" "+document.getElementById("cutOffDate").value+" "+msg2);
				return false;
			}
		}
				
		function checkLength(obj)
		{
			if(obj.value.length>250)
			{
				bootbox.alert('Max 250 characters are allowed for comments. Remaining characters are truncated.')
				obj.value = obj.value.substring(1,250);
			}
		}
		function openVoucher(vid)
		{
			var url = "${pageContext.request.contextPath}/voucher/preApprovedVoucher-loadvoucherview.action?vhid="+ vid;
			window.open(url,'','width=900, height=700');
		}
		document.getElementById('paymentAmountspan').innerHTML = document.getElementById('grandTotal').value;
	</script>
		</div>
	</s:form>
</body>
</html>
