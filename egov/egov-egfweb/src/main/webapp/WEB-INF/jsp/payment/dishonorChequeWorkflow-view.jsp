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
<script type="text/javascript"
	src="${pageContext.request.contextPath}/resources/javascript/voucherHelper.js?rnd=${app_release_no}"></script>
<link href="/EGF/resources/css/budget.css?rnd=${app_release_no}" rel="stylesheet"
	type="text/css" />
<link href="/EGF/resources/css/commonegovnew.css?rnd=${app_release_no}" rel="stylesheet"
	type="text/css" />
<link rel="stylesheet" href="/EGF/resources/css/tabber.css?rnd=${app_release_no}"
	TYPE="text/css">
<script type="text/javascript" src="/EGF/resources/javascript/tabber.js?rnd=${app_release_no}"></script>
<script type="text/javascript"
	src="/EGF/resources/javascript/tabber2.js?rnd=${app_release_no}"></script>

<script>
	function fetchDeptId() {
		var id = '<s:property value="defaultDept"/>';
		var did = '<s:property value="%{paymentheader.voucherheader.vouchermis.departmentid.id}"/>';
		if (id != null && id != '-1' && id != '') {
			document.getElementById('departmentid').value = id;
		} else if (did != null && did != '-1' && did != '') {
			document.getElementById('departmentid').value = did;
		}
	}

	function populateApprover() {
		getUsersByDesignationAndDept();
	}
	function onloadDishonorCheque() {
		var modeval = document.getElementById('mode').value;
		//bootbox.alert("mode value is");
		if (modeval != null && modeval == 'print') {
			var reversalVhId = '<s:property value="%{paymentVoucher.id}"/>';
			var bankChargesVhId = '<s:property value="%{bankChargesReversalVoucher.id}"/>';

			var reversalAmount = '<s:property value="%{dishonorChequeView.instrumentHeader.instrumentAmount}"/>';
			var bankChargesAmount = '<s:property value="%{dishonorChequeView.bankChargesAmt}"/>';
			window
					.open(
							"../brs/DishonoredChequeEntries.do?submitType=beforePrintDishonoredCheque&reversalVhId="
									+ reversalVhId
									+ "&bankChargesVhId="
									+ bankChargesVhId
									+ "&reversalAmount="
									+ reversalAmount
									+ "&bankChargesAmount="
									+ bankChargesAmount,
							'resizable=yes,height=650,width=900,scrollbars=yes,left=30,top=30,status=no');
		}
	}

	function loadDesignationFromMatrix() {
		var dept = dom.get('approverDepartment').value;
		var currentState = dom.get('currentState').value;

		//var pendingAction=document.getElementById('pendingActions').value;
		loadDesignationByDeptAndType('DishonorCheque', null, currentState,
				null, null, pendingAction);
	}
	function loadDesignationByDeptAndType(typeValue, departmentValue,
			currentStateValue, amountRuleValue, additionalRuleValue,
			pendingActionsValue) {
		var designationObj = document.getElementById('approverDesignation');
		designationObj.options.length = 0;
		designationObj.options[0] = new Option("----Choose----", "-1");
		var approverObj = document.getElementById('approverPositionId');
		approverObj.options.length = 0;
		approverObj.options[0] = new Option("----Choose----", "-1");
		populateapproverDesignation({
			departmentRule : departmentValue,
			type : typeValue,
			amountRule : amountRuleValue,
			additionalRule : additionalRuleValue,
			currentState : currentStateValue,
			pendingAction : pendingActionsValue
		});
	}
	function populateActionName(name) {
		document.getElementById('actionName').value = name;
		//bootbox.alert("actionanem"+name);        
		<s:if test="%{getNextAction()!='END'}">
		var value=document.getElementById("approverDepartment").value;
		if((name=="Forward" || name=="forward") && value=="-1")
		{
			bootbox.alert("Please select the Approver Department");
			document.getElementById("approverDepartment").focus();
			return false;
		}
		var value=document.getElementById("approverDesignation").value;
		if((name=="Forward" || name=="forward") && value=="-1")
		{
			bootbox.alert("Please select the approver designation");
			document.getElementById("approverDesignation").focus();
			return false;
		}
	    if((name=="Forward" || name=="forward") && document.getElementById('approverPositionId').value=="-1")
	    {
	    	bootbox.alert("Please Select the Approver");
			document.getElementById("approverPositionId").focus();
			return false;
	    }
    </s:if>
  
	}
</script>

</head>

<body onload="refreshInbox();">
	<br>
	<s:form action="dishonorChequeWorkflow" form="dishonorChequeWorkflow"
		theme="simple">
		<s:push value="model">
			<s:token />
			<font style='color: red;'>
				<p id="lblError" style="font: bold"></p>
			</font>
			<span class="mandatory"> <s:actionerror /> <s:fielderror />
				<s:actionmessage />
			</span>
			<div class="formmainbox">
				<div class="subheadnew">Dishonor Cheque Workflow</div>
				<div id="budgetSearchGrid"
					style="display: block; width: 100%; border-top: 1px solid #ccc;" />
				<table width="100%" cellpadding="0" cellspacing="0" border="0">
					<tr>
						<td>
							<div align="center">
								<table id="glcodeTable" align="center" border="0"
									cellpadding="0" cellspacing="0" width="100%">
									<tr>
										<s:hidden id="bankChargesAmt" name="bankChargesAmt"
											value="%{bankChargesAmt}" />
										<s:hidden id="bankReferenceNumber" name="bankReferenceNumber"
											value="%{bankReferenceNumber}" />
										<s:hidden id="instrumentDishonorReason"
											name="instrumentDishonorReason"
											value="%{instrumentDishonorReason}" />

										<s:hidden id="id" name="id" value="%{id}" />
										<s:hidden id="status" name="status" value="%{status.id}" />
										<s:hidden id="originalVoucherHeader"
											name="originalVoucherHeader"
											value="%{originalVoucherHeader.id}" />
										<s:hidden id="instrumentHeader" name="instrumentHeader"
											value="%{instrumentHeader.id}" />
										<s:hidden id="bankchargeGlCodeId" name="bankchargeGlCodeId"
											value="%{bankchargeGlCodeId.id}" />
										<s:hidden id="createdBy" name="createdBy"
											value="%{createdBy.id}" />
										<s:hidden id="state" name="state" value="%{state.id}" />
										<s:hidden id="modifiedBy" name="modifiedBy"
											value="%{modifiedBy.id}" />
										<s:hidden id="createdDate" name="createdDate"
											value="%{createdDate}" />
										<s:hidden id="modifiedDate" name="modifiedDate"
											value="%{modifiedDate}" />

										<td width="20%" class="bluebox"><strong>Voucher
												Number</strong></td>
										<td width="20%" class="bluebox"><s:hidden
												name="dishonorChequeView.originalVoucherHeader.voucherNumber" />
											<s:property
												value="%{dishonorChequeView.originalVoucherHeader.voucherNumber}" /></td>

										<td width="20%" class="bluebox"><strong>Voucher
												Date</strong></td>
										<td width="20%" class="bluebox"><s:date
												name="dishonorChequeView.originalVoucherHeader.voucherDate"
												format="dd/MM/yyyy" var="tempVoucherDate" />
											<s:property value="%{tempVoucherDate}" /></td>
									</tr>

									<tr>
										<td width="20%" class="bluebox"><strong>Bank </strong></td>
										<td width="20%" class="bluebox"><s:hidden
												name="dishonorChequeView.instrumentHeader.bankAccountId.bankbranch.bank.name" />
											<s:property
												value="%{dishonorChequeView.instrumentHeader.bankAccountId.bankbranch.bank.name}" /></td>

										<td width="20%" class="bluebox"><strong>Bank
												Branch </strong></td>
										<td width="20%" class="bluebox"><s:hidden
												name="dishonorChequeView.instrumentHeader.bankAccountId.bankbranch.branchname" />
											<s:property
												value="%{dishonorChequeView.instrumentHeader.bankAccountId.bankbranch.branchname}" /></td>
									</tr>
									<tr>
										<td width="20%" class="bluebox"><strong>Bank
												Account Number </strong></td>
										<td width="20%" class="bluebox"><s:hidden
												name="dishonorChequeView.instrumentHeader.bankAccountId.accountnumber" />
											<s:property
												value="%{dishonorChequeView.instrumentHeader.bankAccountId.accountnumber}" /></td>
									</tr>
									<tr>
										<td width="20%" class="bluebox"><strong>Cheque
												Number</strong></td>
										<td width="20%" class="bluebox"><s:hidden
												name="dishonorChequeView.instrumentHeader.instrumentNumber" />
											<s:property
												value="%{dishonorChequeView.instrumentHeader.instrumentNumber}" /></td>
										<td width="20%" class="bluebox"><strong>Cheque
												Date</strong></td>
										<td width="20%" class="bluebox"><s:date
												name="dishonorChequeView.instrumentHeader.instrumentDate"
												format="dd/MM/yyyy" var="tempChequeDate" /> <s:property
												value="%{tempChequeDate}" /></td>
									</tr>

									<tr>

										<td width="20%" class="bluebox"><strong><s:text
													name="chq.dishonor.reversal.amount" /></strong></td>
										<td width="20%" class="bluebox"><s:hidden
												name="dishonorChequeView.instrumentHeader.instrumentAmount" />
											<s:property
												value="%{dishonorChequeView.instrumentHeader.instrumentAmount}" /></td>

										<td width="20%" class="bluebox"><strong>Cheque
												Reversal Date</strong> <span class="mandatory">*</span></td>
										<s:date name='dishonorChequeView.transactionDate'
											format="dd/MM/yyyy" var="tempTransactionDate" />
										<td class="bluebox"><s:textfield
												name="dishonorChequeView.transactionDate"
												id="dishonorChequeView.transactionDate"
												onkeyup="DateFormat(this,this.value,event,false,'3')"
												value="%{tempTransactionDate}" /> <!-- <a tabindex="-1" href="javascript:show_calendar('dishonorChequeWorkflow.dishonorChequeView.transactionDate');"	style="text-decoration: none">&nbsp;<img 
										src="/egi/resources/erp2/images/calendaricon.gif"		border="0" /></a></td>-->
									</tr>
									<tr>
										<td width="20%" class="bluebox"><strong>Remarks</strong></td>
										<td width="20%" class="bluebox"><s:property
												value="%{instrumentDishonorReason}" />
										<td width="20%" class="bluebox"><strong>Reason</strong></td>
										<td width="20%" class="bluebox"><s:property
												value="dishonorChequeView.instrumentHeader.surrendarReason" />
									</tr>
									<table border="1" width="100%" cellspacing="0">
										<tr>
											<th colspan="5"><div class="subheadsmallnew">Account
													Details</div></th>
										</tr>

										<tr>
											<th class="bluebgheadtd" width="18%">Function Name</th>
											<th class="bluebgheadtd" width="17%">Account&nbsp;Code</th>
											<th class="bluebgheadtd" width="19%">Account Head</th>
											<th class="bluebgheadtd" width="17%">Debit&nbsp;Amount(Rs)</th>
											<th class="bluebgheadtd" width="16%">Credit&nbsp;Amount(Rs)</th>
										</tr>
										<tr>
											<td width="18%" class="bluebox"><s:if
													test="%{function!=null}">
													<s:property value="function.name" />
												</s:if></td>
											<td width="17%" class="bluebox"><s:property
													value="dishonorChequeView.instrumentHeader.bankAccountId.chartofaccounts.glcode" />
												<s:property
													value="%{dishonorChequeView.instrumentHeader.bankAccountId.chartofaccounts.glcode" /></td>
											<td width="19%" class="bluebox"><s:property
													value="dishonorChequeView.instrumentHeader.bankAccountId.chartofaccounts.name" />
												<s:property
													value="%{dishonorChequeView.instrumentHeader.bankAccountId.chartofaccounts.name" /></td>
											<td width="17%" class="bluebox" style="text-align: right"><s:text
													name="payment.format.number">
													<s:param value="%{0.0}" />
												</s:text></td>
											<td width="16%" class="bluebox" style="text-align: right"><s:text
													name="payment.format.number">
													<s:param
														value="%{dishonorChequeView.instrumentHeader.instrumentAmount}" />
												</s:text></td>
										</tr>
										<s:set var="totalDebit" value="0" />
										<s:iterator var="p" value="%{dishonorChequeView.details}"
											status="s">
											<tr>
												<td width="18%" class="bluebox"><s:if
														test="%{function!=null}">
														<s:property value="function.name" />
													</s:if></td>
												<td width="17%" class="bluebox"><s:property
														value="glcodeId.glcode" /></td>
												<td width="19%" class="bluebox"><s:property
														value="glcodeId.name" /></td>
												<td width="17%" class="bluebox" style="text-align: right"><s:text
														name="payment.format.number">
														<s:param value="%{debitAmt}" />
													</s:text></td>
												<td width="16%" class="bluebox" style="text-align: right"><s:text
														name="payment.format.number">
														<s:param value="%{0.0}" />
													</s:text></td>
												<s:set var="totalDebit" value="#totalDebit + #p.debitAmt" />
											</tr>
										</s:iterator>
										<tr>
											<td class="greybox" style="text-align: right" colspan="3" />Total
											</td>
											<td class="greybox" style="text-align: right"><s:text
													name="payment.format.number">
													<s:param value="#totalDebit" />
												</s:text></td>
											<td class="greybox" style="text-align: right"><s:text
													name="payment.format.number">
													<s:param
														value="%{dishonorChequeView.instrumentHeader.instrumentAmount}" />
												</s:text></td>
										</tr>
									</table>


									<s:if
										test="dishonorChequeView.bankChargesAmt != null && dishonorChequeView.bankChargesAmt>0">
										<table border="1" width="100%" cellspacing="0">
											<tr>
												<th colspan="5"><div class="subheadsmallnew">Bank
														Charges Details</div></th>
											</tr>
											<br />
											<tr>
												<td width="20%" class="bluebox"><strong>Bank
														Charges Reason</strong></td>
												<td width="20%" class="bluebox"><s:hidden
														name="bankreason" />
													<s:property value="%{bankreason}" />
											</tr>
											<tr>
												<th colspan="5"><div class="subheadsmallnew">Account
														Details</div></th>
											</tr>
											<tr>
												<th class="bluebgheadtd" width="18%">Function Name</th>
												<th class="bluebgheadtd" width="17%">Account&nbsp;Code</th>
												<th class="bluebgheadtd" width="19%">Account Head</th>
												<th class="bluebgheadtd" width="17%">Debit&nbsp;Amount(Rs)</th>
												<th class="bluebgheadtd" width="16%">Credit&nbsp;Amount(Rs)</th>
											</tr>

											<tr>
												<td width="18%" class="bluebox"><s:if
														test="%{function!=null}">
														<s:property value="function.name" />
													</s:if></td>
												<td width="17%" class="bluebox"><s:property
														value="dishonorChequeView.instrumentHeader.bankAccountId.chartofaccounts.glcode" /></td>
												<td width="19%" class="bluebox"><s:property
														value="dishonorChequeView.instrumentHeader.bankAccountId.chartofaccounts.name" /></td>
												<td width="17%" class="bluebox" style="text-align: right"><s:text
														name="payment.format.number">
														<s:param value="%{0.0}" />
													</s:text></td>
												<td width="16%" class="bluebox" style="text-align: right"><s:text
														name="payment.format.number">
														<s:param value="%{bankChargesAmt}" />
													</s:text></td>
											</tr>
											<tr>
												<td width="18%" class="bluebox"><s:if
														test="%{function!=null}">
														<s:property value="function.name" />
													</s:if></td>
												<td width="17%" class="bluebox"><s:property
														value="dishonorChequeView.bankchargeGlCodeId.glcode" /></td>
												<td width="19%" class="bluebox"><s:property
														value="dishonorChequeView.bankchargeGlCodeId.name" /></td>
												<td width="17%" class="bluebox" style="text-align: right"><s:text
														name="payment.format.number">
														<s:param value="%{bankChargesAmt}" />
													</s:text></td>
												<td width="16%" class="bluebox" style="text-align: right"><s:text
														name="payment.format.number">
														<s:param value="%{0.0}" />
													</s:text></td>

											</tr>
											<tr>
												<td class="greybox" style="text-align: right" colspan="3" />Total
												</td>
												<td class="greybox" style="text-align: right"><s:text
														name="payment.format.number">
														<s:param value="%{bankChargesAmt}" />
													</s:text></td>
												<td class="greybox" style="text-align: right"><s:text
														name="payment.format.number">
														<s:param value="%{bankChargesAmt}" />
													</s:text></td>
											</tr>
										</table>
									</s:if>
									<s:hidden name="nextLevel" id="nextLevel"
										value="%{getNextAction()}"></s:hidden>
									<s:hidden name="actionName" id="actionName"></s:hidden>

									<s:hidden name="mode" id="mode" value="%{mode}"></s:hidden>
								</table>

							</div> <s:if test="%{nextLevel!='END'}">
								<%@ include file='commonWorkflowMatrix.jsp'%>
							</s:if>
							<table id="glcodeTable" align="center" border="0" cellpadding="0"
								cellspacing="0" width="100%">
								<tr>
									<s:iterator value="%{getValidActions()}" var="name">
										<s:if test="%{name!=''}">
											<s:submit type="submit" cssClass="buttonsubmit"
												value="%{name}" id="%{name}" name="%{name}" method="save"
												onclick="return populateActionName('%{name}');" />
										</s:if>
									</s:iterator>
								</tr>
							</table>
						</td>
					</tr>
				</table>
			</div>
		</s:push>
	</s:form>
</body>

</html>
