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
<link href="/EGF/resources/css/budget.css?rnd=${app_release_no}" rel="stylesheet"
	type="text/css" />
<link href="/EGF/resources/css/commonegovnew.css?rnd=${app_release_no}" rel="stylesheet"
	type="text/css" />
<link rel="stylesheet" href="/EGF/resources/css/tabber.css?rnd=${app_release_no}"
	TYPE="text/css">
<script type="text/javascript" src="/EGF/resources/javascript/tabber.js?rnd=${app_release_no}"></script>
<script type="text/javascript"
	src="/EGF/resources/javascript/tabber2.js?rnd=${app_release_no}"></script>
</head>

<body>
	<br>
	<s:form action="payment" theme="simple">
		<jsp:include page="../budget/budgetHeader.jsp">
			<jsp:param name="heading" value="Modify Bill Payment" />
		</jsp:include>
		<span class="mandatory"> <s:actionerror /> <s:fielderror /> <s:actionmessage />
		</span>
		<div class="formmainbox">
			<div class="subheadnew">Modify Bill Payment</div>
			<div id="budgetSearchGrid"
				style="display: block; width: 100%; border-top: 1px solid #ccc;">
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
																<td align="center" colspan="7" class="serachbillhead">Payment
																	Details</td>
															</tr>
															<tr>
																<td width="9%" class="bluebox">&nbsp;</td>
																<s:if test="%{shouldShowHeaderField('fund')}">
																	<td width="20%" class="bluebox"><strong><s:text
																				name="voucher.fund" /></strong></td>
																	<td width="20%" class="bluebox"><s:hidden
																			name="paymentheader.voucherheader.fundId.name" />
																		<s:property
																			value="%{paymentheader.voucherheader.fundId.name}" /></td>
																</s:if>
																<td width="10%" class="bluebox" />
																<s:if test="%{shouldShowHeaderField('fundsource')}">
																	<td width="17%" class="bluebox"><strong><s:text
																				name="voucher.fundsource" /></strong></td>
																	<td width="25%" class="bluebox"><s:hidden
																			name="paymentheader.voucherheader.fundsourceId.name" />
																		<s:property
																			value="%{paymentheader.voucherheader.fundsourceId.name}" /></td>
																</s:if>
															</tr>
															<tr>
																<td class="greybox">&nbsp;</td>
																<s:if test="%{shouldShowHeaderField('department')}">
																	<td class="greybox"><strong><s:text
																				name="voucher.department" /></strong></td>
																	<td class="greybox"><s:hidden
																			name="paymentheader.voucherheader.vouchermis.departmentid.deptName" />
																		<s:property
																			value="%{paymentheader.voucherheader.vouchermis.departmentid.deptName}" /></td>
																</s:if>
																<td class="greybox" />
																<s:if test="%{shouldShowHeaderField('functionary')}">
																	<td class="greybox"><strong><s:text
																				name="voucher.functionary" /></strong></td>
																	<td class="greybox" colspan="2"><s:hidden
																			name="paymentheader.voucherheader.vouchermis.functionary.name" />
																		<s:property
																			value="%{paymentheader.voucherheader.vouchermis.functionary.name}" /></td>
																</s:if>
															</tr>
															<tr>
																<td class="bluebox">&nbsp;</td>
																<s:if test="%{shouldShowHeaderField('scheme')}">
																	<td class="bluebox"><strong><s:text
																				name="voucher.scheme" /></strong></td>
																	<td class="bluebox"><s:hidden
																			name="paymentheader.voucherheader.vouchermis.schemeid.name" />
																		<s:property
																			value="%{paymentheader.voucherheader.vouchermis.schemeid.name}" /></td>
																</s:if>
																<td class="bluebox" />
																<s:if test="%{shouldShowHeaderField('subscheme')}">
																	<td class="bluebox"><strong><s:text
																				name="voucher.subscheme" /></strong></td>
																	<td class="bluebox"><s:hidden
																			name="paymentheader.voucherheader.vouchermis.subschemeid.name" />
																		<s:property
																			value="%{paymentheader.voucherheader.vouchermis.subschemeid.name}" /></td>
																</s:if>
															</tr>
															<tr>
																<td class="greybox">&nbsp;</td>
																<s:if test="%{shouldShowHeaderField('function')}">
																	<td class="greybox"><strong><s:text
																				name="voucher.function" /></strong>
																	<s:if test="%{isFieldMandatory('function')}">
																			<span class="mandatory">*</span>
																		</s:if></td>
																	<td class="greybox"><s:hidden
																			name="paymentheader.voucherheader.vouchermis.function.name" />
																		<s:property
																			value="%{paymentheader.voucherheader.vouchermis.function.name}" /></td>
																</s:if>
																<s:if test="%{shouldShowHeaderField('field')}">
																	<td class="greybox"><strong><s:text
																				name="voucher.field" /></strong></td>
																	<td class="greybox" colspan="4"><s:hidden
																			name="paymentheader.voucherheader.vouchermis.divisionid.name" />
																		<s:property
																			value="%{paymentheader.voucherheader.vouchermis.divisionid.name}" /></td>
																</s:if>
																<td class="greybox" />
															</tr>
															<tr>
																<td class="bluebox">&nbsp;</td>
																<s:if test="%{shouldShowHeaderField('vouchernumber')}">
																	<td class="bluebox"><strong><s:text
																				name="payment.voucherno" /></strong><span class="mandatory">*</span></td>
																	<td class="bluebox"><s:textfield
																			name="voucherNumberPrefix" id="voucherNumberPrefix"
																			value="%{voucherNumberPrefix}" size="2"
																			readonly="true" />
																		<s:textfield name="voucherNumberSuffix"
																			id="voucherNumberSuffix"
																			value="%{voucherNumberSuffix}" size="10" /></td>
																</s:if>
																<s:else>
																	<td class="bluebox"><strong><s:text
																				name="payment.voucherno" /></strong></td>
																	<td class="bluebox"><s:property
																			value="paymentheader.voucherheader.voucherNumber" /></td>
																</s:else>
																<td class="bluebox" />
																<td class="bluebox"><strong><s:text
																			name="payment.voucherdate" /></strong><span class="mandatory">*</span></td>
																<td class="bluebox" colspan="2"><s:date
																		name="paymentheader.voucherheader.voucherDate"
																		var="tempDate" format="dd/MM/yyyy" />
																	<s:textfield
																		name="paymentheader.voucherheader.voucherDate"
																		id="voucherDate" maxlength="10" value="%{tempDate}"
																		size="10"
																		onkeyup="DateFormat(this,this.value,event,false,'3')" /><a
																	href="javascript:show_calendar('forms[0].voucherDate');"
																	style="text-decoration: none">&nbsp;<img
																		src="/egi/resources/erp2/images/calendaricon.gif"
																		border="0" /></a>(dd/mm/yyyy)</td>
																<s:hidden name="functionSel" id="functionSel"
																	value="%{functionSel}" />
															</tr>
															<tr>
																<td class="greybox">&nbsp;</td>
																<td class="greybox"><strong><s:text
																			name="payment.bank" /></strong><span class="mandatory">*</span></td>
																<td class="greybox"><s:select
																		name="paymentheader.bankaccount.bankbranch.id"
																		id="bankbranch" list="dropdownData.bankbranchList"
																		listKey="id" listValue="bank.name+'-'+branchname"
																		headerKey="-1" headerValue="----Choose----"
																		onchange="loadBankAccount(this)"
																		value="%{paymentheader.bankaccount.bankbranch.id}" /></td>
																<td class="greybox" />
																<egov:ajaxdropdown id="bankaccount"
																	fields="['Text','Value']" dropdownId="bankaccount"
																	url="voucher/common!ajaxLoadBankAccounts.action" />
																<td class="greybox"><strong><s:text
																			name="payment.bankaccount" /></strong><span class="mandatory">*</span></td>
																<td class="greybox" colspan="2"><s:select
																		name="paymentheader.bankaccount.id" id="bankaccount"
																		list="dropdownData.bankaccountList" listKey="id"
																		listValue="accountnumber+'---'+accounttype"
																		headerKey="-1" headerValue="----Choose----"
																		onchange="loadBalance(this)"
																		value="%{paymentheader.bankaccount.id}" /></td>
																<egov:updatevalues id="balance" fields="['Text']"
																	url="payment/payment!ajaxGetAccountBalance.action" />
															</tr>
															<tr>
																<td class="bluebox">&nbsp;</td>
																<td class="bluebox"><strong><s:text
																			name="payment.mode" /></strong></td>
																<td class="bluebox" colspan="2"><s:if
																		test="%{paymentheader.voucherheader.name == 'Salary Bill Payment' || paymentheader.voucherheader.name == 'Pension Bill Payment' }">
																		<s:radio id="paymentMode" name="paymentheader.type"
																			list="#{'rtgs':'RTGS','cash':'Cash/Consolidated Cheque'}"
																			value="%{paymentheader.type}" />
																	</s:if> <s:else>
																		<s:radio id="paymentMode" name="paymentheader.type"
																			list="#{'cheque':'Cheque','rtgs':'RTGS','cash':'Cash/Consolidated Cheque'}"
																			value="%{paymentheader.type}" />
																	</s:else></td>
																<td class="bluebox"><strong><s:text
																			name="payment.amount" /></strong></td>
																<td class="bluebox" colspan="2"><s:hidden
																		name="paymentheader.paymentAmount"
																		id="paymentheader.paymentAmount"
																		value="%{paymentheader.paymentAmount}" /><span
																	id="paymentAmountspan" /></td>
															</tr>
															<tr>
																<td class="greybox">&nbsp;</td>
																<td class="greybox" width="15%"><strong><s:text
																			name="payment.narration" /></strong></td>
																<td class="greybox" colspan="5"><s:textarea
																		name="paymentheader.voucherheader.description"
																		id="description" cols="70" rows="4"
																		onblur="checkLength(this)"
																		value="%{paymentheader.voucherheader.description}" /></td>
															</tr>

															<tr id="bankbalanceRow" style="visibility: hidden">
																<td class="bluebox">&nbsp;</td>
																<td class="bluebox" width="15%"><strong><s:text
																			name="payment.balance" /></strong></td>
																<td class="bluebox" colspan="4"><span id="balance" /></td>
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
																<td colspan="6"><div class="subheadsmallnew">Bill
																		Details</div></td>
															</tr>
															<tr>
																<td colspan="6">
																	<div style="float: left; width: 100%;">
																		<table id="miscBillTable" align="center" border="0"
																			cellpadding="0" cellspacing="0" width="100%">
																			<tr>
																				<th class="bluebgheadtdnew">Sl No
																				</td>
																				<th class="bluebgheadtdnew">Bill Number
																				</td>
																				<th class="bluebgheadtdnew">Bill Date
																				</td>
																				<th class="bluebgheadtdnew">Payee Name
																				</td>
																				<th class="bluebgheadtdnew">Net Amount
																				</td>
																				<th class="bluebgheadtdnew">Other Payment
																				</td>
																				<th class="bluebgheadtdnew">Payable Amount
																				</td>
																				<th class="bluebgheadtdnew">Payment Amount
																				</td>
																			</tr>
																			<s:if test="%{billList.size>0}">
																				<s:iterator var="p" value="billList" status="s">
																					<tr>
																						<td style="text-align: center"
																							class="blueborderfortdnew"><s:property
																								value="%{#s.index+1}" />
																							<s:hidden name="billList[%{#s.index}].isSelected"
																								value="true" id="isSelected%{#s.index}" /></td>
																						<td style="text-align: center"
																							class="blueborderfortdnew"><s:hidden
																								name="billList[%{#s.index}].billNumber"
																								id="billNumber" value="%{billNumber}" />
																							<s:hidden name="billList[%{#s.index}].csBillId"
																								value="%{csBillId}" />
																							<s:property value="%{billNumber}" /></td>
																						<td style="text-align: center"
																							class="blueborderfortdnew"><s:hidden
																								name="billList[%{#s.index}].billDate"
																								id="billDate%{#s.index}" value="%{billDate}" />
																							<s:date name="%{billDate}" format="dd/MM/yyyy" /></td>
																						<td style="text-align: center"
																							class="blueborderfortdnew"><s:hidden
																								name="billList[%{#s.index}].expType"
																								id="expType%{#s.index}" value="%{expType}" />
																							<s:hidden name="billList[%{#s.index}].payTo"
																								id="payTo%{#s.index}" value="%{payTo}" />
																							<s:property value="%{payTo}" /></td>
																						<td style="text-align: right"
																							class="blueborderfortdnew"><s:hidden
																								name="billList[%{#s.index}].netAmt"
																								id="netAmt%{#s.index}" value="%{netAmt}" />
																							<s:hidden name="billList[%{#s.index}].passedAmt"
																								value="%{passedAmt}" />
																							<s:property value="%{netAmt}" /></td>
																						<td style="text-align: right"
																							class="blueborderfortdnew"><s:hidden
																								name="billList[%{#s.index}].earlierPaymentAmt"
																								id="earlierPaymentAmt%{#s.index}"
																								value="%{earlierPaymentAmt}" />
																							<s:property value="%{earlierPaymentAmt}" /></td>
																						<td style="text-align: right"
																							class="blueborderfortdnew"><s:hidden
																								name="billList[%{#s.index}].payableAmt"
																								id="payableAmt%{#s.index}" value="%{payableAmt}" />
																							<s:property value="%{payableAmt}" /></td>
																						<c:set var="payAmt">
																							<s:property value="%{paymentAmt}" />
																						</c:set>
																						<s:if
																							test="%{expType==finConstExpendTypeContingency }">
																							<td class="blueborderfortdnew"><div
																									align="center">
																									<s:textfield
																										name="billList[%{#s.index}].paymentAmt"
																										id="paymentAmt%{#s.index}"
																										value="%{paymentAmt}" style="text-align:right"
																										readonly="true" />
																								</div></td>
																						</s:if>
																						<s:else>
																							<td class="blueborderfortdnew"><div
																									align="center">
																									<s:textfield
																										name="billList[%{#s.index}].paymentAmt"
																										id="paymentAmt%{#s.index}"
																										value="%{paymentAmt}" style="text-align:right"
																										onchange="calcGrandTotal(this)"
																										onfocus="updateHidden(this)" />
																								</div></td>
																						</s:else>
																						<c:set var="totalAmt" value="${totalAmt+payAmt}" />
																					</tr>
																				</s:iterator>
																			</s:if>
																			<tr>
																				<td style="text-align: right" colspan="7"
																					class="blueborderfortdnew"><strong>Grand
																						Total</strong></td>
																				<td style="text-align: right"
																					class="blueborderfortdnew"><div align="center">
																						<input type="text" name="grandTotal"
																							id="grandTotal"
																							value="<c:out value="${totalAmt}"/>"
																							style="text-align: right" readonly />
																					</div></td>
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
														<s:checkbox name="changePartyName" id="changePartyName" />
														<s:textfield name="newPartyName" id="newPartyName" />
													</div>
												</s:if>
											</div> <!-- tabbber div -->
										</td>
									</tr>
								</table>
								<%@include file="../voucher/workflowApproval.jsp"%>
							</div>
						</td>
					</tr>
				</table>
			</div>
			<div class="buttonbottom" id="buttondiv">
				<s:hidden name="fundId"
					value="%{paymentheader.voucherheader.fundId.id}" />
				<s:hidden name="hiddenText" id="hiddenText" />
				<s:hidden name="paymentheader.voucherheader.id"
					value="%{paymentheader.voucherheader.id}" />
				<s:hidden name="paymentheader.id" value="%{paymentheader.id}" />

				<s:hidden name="paymentid" value="%{paymentheader.id}" />
				<s:hidden name="actionname" id="actionName" value="%{action}" />
				<s:iterator value="%{getValidActions()}" var="p" status="s">
					<s:submit type="submit" cssClass="buttonsubmit"
						value="%{description}" id="wfBtn%{#s.index}" name="%{name}"
						method="edit"
						onclick="return validate('%{name}','%{description}')" />
				</s:iterator>
				<s:submit method="cancelPayment" value="Cancel Payment"
					cssClass="buttonsubmit" id="updatebtnid"
					onclick="document.getElementById('actionName').value='canccelPayment';" />
				<input type="submit" value="Close"
					onclick="javascript:window.close()" class="button" />
			</div>
		</div>
		<script>
		document.getElementById('paymentAmountspan').innerHTML = '<c:out value="${totalAmt}"/>';
	
		function validateAppoveUser(name,value){
			document.getElementById("actionName").value= name;
			<s:if test="%{wfitemstate =='END'}">
				if(value == 'Approve' || value == 'Reject') {
					document.getElementById("approverUserId").value=-1;
					return true;
				}
			</s:if>
			<s:else>
				if( (value == 'Approve' || value == 'Save And Forward' || value == 'Forward' ) && null != document.getElementById("approverUserId") && document.getElementById("approverUserId").value == -1){
					bootbox.alert("please select User");
					//document.getElementById('lblError').innerHTML ="Please Select the user";
					return false;
				}
			</s:else>
			
			return true;
		}
		
		
		function validate(name,value)
		{
		
		if(dom.get('vouchernumber') && dom.get('vouchernumber').value=='')
			{
				bootbox.alert('Please Enter voucher number');
				return false;
			}
		/*	if(dom.get('paymentheader.voucherheader.voucherDate').value=='')
			{
				bootbox.alert("Please Select the Voucher Date!!");
				return false;
			}*/
		
			if(dom.get('bankbranch').options[dom.get('bankbranch').selectedIndex].value==-1)
			{
				bootbox.alert("Please Select the Bank!!");
				return false;
			}
			if(dom.get('bankaccount').options[dom.get('bankaccount').selectedIndex].value==-1)
			{
				bootbox.alert("Please Select the Bank Account");
				return false;
			}
		
			if(document.getElementById('grandTotal').value==0)
			{
				bootbox.alert('Payment Amount should be greater than zero!');
				dom.get('tabber1').onclick();
				return false;
			}
			<s:if test="%{disableExpenditureType}">
				if(dom.get("changePartyName") && dom.get("changePartyName").checked==true)
				{
					if(dom.get("newPartyName").value=='')
					{
						bootbox.alert('Enter Party Name to Chnage');
						dom.get("newPartyName").focus();
						return false;
					}
				}
			</s:if>
			if(!validateAppoveUser(name,value))
			{
			return false;
			}
			return true;
		}
		function checkLength(obj)
		{
			if(obj.value.length>250)
			{
				bootbox.alert('Max 250 characters are allowed for comments. Remaining characters are truncated.')
				obj.value = obj.value.substring(1,250);
			}
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
			if(obj.value=='' || isNaN(obj.value))
				obj.value=0;
			var index = obj.id.substring(10,obj.id.length);	
			if(obj.value>parseFloat(document.getElementById('payableAmt'+index).value))
			{
				bootbox.alert('Payment amount should not be greater than payable amount');
				obj.value=parseFloat(document.getElementById('hiddenText').value);
				document.getElementById('grandTotal').value = (parseFloat(document.getElementById('grandTotal').value) - parseFloat(document.getElementById('hiddenText').value) + parseFloat(obj.value)).toFixed(2);
				return;
			}
			document.getElementById('grandTotal').value = (parseFloat(document.getElementById('grandTotal').value) - parseFloat(document.getElementById('hiddenText').value) + parseFloat(obj.value)).toFixed(2);
			document.getElementById('paymentAmountspan').innerHTML = document.getElementById('grandTotal').value;
		}
		function loadBankAccount(obj)
		{
			var vTypeOfAccount = '<s:property value="%{typeOfAccount}"/>';
			var fund = 0;
			<s:if test="%{shouldShowHeaderField('fund')}">
				fund = <s:property value="%{paymentheader.voucherheader.fundId.id}"/>;
			</s:if>
			if(obj.options[obj.selectedIndex].value==-1)
			{
				var d = dom.get('bankaccount');
				d.options.length=1;
				d.options.value=-1;
			}	
			else
				populatebankaccount({branchId:obj.options[obj.selectedIndex].value+'&date='+new Date(), typeOfAccount:vTypeOfAccount,fundId:fund});
		}
		function loadBalance(obj)
		{
			if(dom.get('voucherDate').value=='')
			{
				bootbox.alert("Please Select the Voucher Date!!");
				obj.options.value=-1;
				return;
			}
			if(obj.options[obj.selectedIndex].value==-1)
				dom.get('balance').value='';
			else
				populatebalance({bankaccount:obj.options[obj.selectedIndex].value,voucherDate:dom.get('voucherDate').value+'&date='+new Date()});
		}
	</script>

		<s:if test="%{validateUser('balancecheck')}">
			if(document.getElementById('balanceText'))
			{
				document.getElementById('balanceText').style.display='block';
				document.getElementById('balanceAvl').style.display='block';
			}
	</s:if>
	</s:form>
</body>
</html>
