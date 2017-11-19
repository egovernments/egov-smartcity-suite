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

<script type="text/javascript"
	src="/EGF/resources/javascript/calendar.js?rnd=${app_release_no}"></script>
<script type="text/javascript"
	src="/EGF/resources/javascript/dateValidation.js?rnd=${app_release_no}"></script>
<script type="text/javascript"
	src="/EGF/resources/javascript/ajaxCommonFunctions.js?rnd=${app_release_no}"></script>
<link rel="stylesheet" href="/EGF/resources/css/tabber.css?rnd=${app_release_no}"
	TYPE="text/css">
<script type="text/javascript" src="/EGF/resources/javascript/tabber.js?rnd=${app_release_no}"></script>
<script type="text/javascript"
	src="/EGF/resources/javascript/voucherHelper.js?rnd=${app_release_no}"></script>
<link href="common/css/budget.css?rnd=${app_release_no}" rel="stylesheet" type="text/css" />
<link href="common/css/commonegov.css?rnd=${app_release_no}" rel="stylesheet" type="text/css" />

<meta http-equiv="Content-Type"
	content="text/html; charset=windows-1252">
<style type="text/css">
#codescontainer {
	position: absolute;
	left: 11em;
	width: 9%;
	text-align: left;
}

#codescontainer .yui-ac-content {
	position: absolute;
	width: 600px;
	border: 1px solid #404040;
	background: #fff;
	overflow: hidden;
	z-index: 9050;
}

#codescontainer .yui-ac-shadow {
	position: absolute;
	margin: .3em;
	width: 300px;
	background: #a0a0a0;
	z-index: 9049;
}

#codescontainer ul {
	padding: 5px 0;
	width: 100%;
}

#codescontainer li {
	padding: 0 5px;
	cursor: default;
	white-space: nowrap;
}

#codescontainer li.yui-ac-highlight {
	background: #ff0;
}

#codescontainer li.yui-ac-prehighlight {
	background: #FFFFCC;
}

.yui-skin-sam tr.yui-dt-odd {
	background-color: #FFF;
}
</style>

<script>
function loadBank(fund){
	populatebank({fundId:fund.options[fund.selectedIndex].value,typeOfAccount:"PAYMENT,RECEIPTS_PAYMENTS"})	
}
function loadBankForFund(fund){
	populatebank({fundId:fund.options[fund.selectedIndex].value})	
}
function validateFund(){
	var fund = document.getElementById('fund').value;
	var bank = document.getElementById('bank');
	if(fund == -1 && bank.options.length==1){
		bootbox.alert("Please select a Fund")
		return false;
	}
	return true;
}
function populateAccNumbers(bankBranch){
	var fund = document.getElementById('fund');
	id = bankBranch.options[bankBranch.selectedIndex].value.split("-")[1]
	populatebankaccount({branchId:id,fundId:fund.options[fund.selectedIndex].value})	
}
function populateAccNumbersForId(bankBranchId){
	var fund = document.getElementById('fund');
	populatebankaccount({branchId:bankBranchId,fundId:fund.options[fund.selectedIndex].value})	
}
function onLoadTask(){
	var currentTime = new Date()
	var month = currentTime.getMonth() + 1
	var day = currentTime.getDate()
	var year = currentTime.getFullYear()
	var fund = document.getElementById('fund');
	selectedFund = '<s:property value="fund.id"/>';
	for(i=0;i<fund.options.length;i++){
		if(fund.options[i].value==selectedFund){
			fund.options[i].selected = true;
		}
	}
	document.getElementById('fund').disabled=true;
	document.getElementById('voucherDate').value = day + "/" + month + "/" + year ;
	<s:if test="%{bankaccount.id !=null}">
		var bank = document.getElementById('bank');
		selectedBank = '<s:property value="bankaccount.bankbranch.id"/>';
		for(i=0;i<bank.options.length;i++){
			if(bank.options[i].value.split('-')[1]==selectedBank){
				bank.options[i].selected = true;
			}
		}
	</s:if>
	<s:if test="%{bankaccount.id !=null}">
		selectedAccount ='<s:property value="bankaccount.id"/>';
		var bankAccount = document.getElementById('bankaccount');
		for(i=0;i<bankAccount.options.length;i++){
			if(bankAccount.options[i].value==selectedAccount){
				bankAccount.options[i].selected = true;
			}
		}
	</s:if>
}

function populateUser(){
	
	var desgFuncry = document.getElementById("designationId").value;
	var array = desgFuncry.split("-");
	var functionary = array[1];
	var desgId = array[0];
	if(desgId==""){ // when user doesnot selects any value in the designation drop down.
		desgId=-1;
	}
	populateapproverUserId({departmentId:document.getElementById("departmentid").value,
	designationId:desgId,functionaryName:functionary})
		
}
function validateUser(name,value){
	document.getElementById("actionName").value= name;
	document.getElementById('lblError').innerHTML ="";
<s:if test="%{wfitemstate !='END'}">
	 if( (value == 'Approve' || value=='Send for Approval' || value == 'Forward') && null != document.getElementById("approverUserId") && document.getElementById("approverUserId").value == -1){
		document.getElementById('lblError').innerHTML ="Please Select the user";
		return false;
	}
</s:if>
	return true;
}

</script>
</head>

<body onload="onLoadTask();">
	<s:form action="advanceRequisitionPayment" theme="simple"
		name="advanceRequisitionPayment">
		<s:token />
		<jsp:include page="../budget/budgetHeader.jsp">
			<jsp:param value="Advance Requisition Payment" name="heading" />
		</jsp:include>
		<div class="formmainbox">
			<div class="formheading" />
			<div class="subheadnew">Advance Requisition Payment</div>
		</div>
		<div align="center">
			<font style='color: red;'>
				<p class="error-block" id="lblError"></p>
			</font>
		</div>
		<span class="mandatory">
			<div id="Errors">
				<s:actionerror />
				<s:fielderror />
			</div> <s:actionmessage />
		</span>
		<table width="100%" cellpadding="0" cellspacing="0" border="0">
			<tr>
				<td>
					<div align="left">
						<br />
						<table border="0" cellspacing="0" cellpadding="0" width="100%">
							<tr>
								<td>
									<div class="tabber">
										<div class="tabbertab"
											style="border: 1px solid #ccc; height: 350px; overflow-y: scroll; overflow-x: scroll; margin-top: 10px; padding: 0px;">
											<h2>Payment Details</h2>
											<span>
												<table width="100%" border="0" cellspacing="0"
													cellpadding="0">
													<tr>
														<td align="center" colspan="6" class="serachbillhead">Payment
															Details</td>
													</tr>
													<tr>
														<td width="9%" class="bluebox">&nbsp;</td>
														<td width="12%" class="bluebox">&nbsp;</td>
														<td width="20%" class="bluebox">&nbsp;</td>
														<td width="17%" class="bluebox">&nbsp;</td>
														<td width="33%" class="bluebox">&nbsp;</td>
													</tr>
													<tr>
														<td class="greybox">&nbsp;</td>
														<td class="greybox">Fund:</td>
														<td class="greybox"><s:select name="fund" id="fund"
																list="dropdownData.fundList" listKey="id"
																listValue="name" headerKey="-1"
																headerValue="----Choose----"
																onChange="loadBankForFund(this);" /></td>
														<td class="greybox">&nbsp;</td>
														<td class="greybox">&nbsp;</td>
													</tr>
													<tr>
														<td class="bluebox">&nbsp;</td>
														<egov:ajaxdropdown id="bank" fields="['Text','Value']"
															dropdownId="bank"
															url="voucher/common!ajaxLoadBanks.action" />
														<td class="bluebox">Bank Name:<span class="bluebox"><span
																class="mandatory">*</span></span></td>
														<td class="bluebox"><s:select name="bank" id="bank"
																list="dropdownData.bankList" listKey="bank.id+'-'+id"
																listValue="bank.name+' '+branchname" headerKey="-1"
																headerValue="----Choose----" onclick="validateFund()"
																onChange="populateAccNumbers(this);" /></td>
														<egov:ajaxdropdown id="accountNumber"
															fields="['Text','Value']" dropdownId="bankaccount"
															url="voucher/common!ajaxLoadAccountNumbers.action" />
														<td class="bluebox">Account Number:<span
															class="bluebox"><span class="mandatory">*</span></span></td>
														<td class="bluebox"><s:select name="bankaccount.id"
																id="bankaccount" list="dropdownData.accNumList"
																listKey="id"
																listValue="chartofaccounts.glcode+'--'+accountnumber+'--'+accounttype"
																headerKey="-1" headerValue="----Choose----" /></td>
													</tr>
													<tr>
														<td class="greybox">&nbsp;</td>
														<td class="greybox">Mode of Payment:</td>
														<td class="greybox"><s:radio name="paymentMode"
																id="paymentMode" list="%{modeOfCollectionMap}" /></td>
														<td class="greybox">Payment Amount:</td>
														<td class="greybox">&nbsp;<s:text
																name="payment.format.number">
																<s:param name="value"
																	value="advanceRequisition.advanceRequisitionAmount" />
															</s:text>
														</td>
													</tr>
													<tr>
														<td class="bluebox">&nbsp;</td>
														<s:if test="%{shouldShowHeaderField('vouchernumber')}">
															<td class="bluebox"><s:text name="voucher.number" /><span
																class="mandatory">*</span></td>
															<td class="bluebox"><s:textfield
																	name="vouchernumber" id="vouchernumber" /></td>
														</s:if>
														<td class="bluebox">Voucher Date:<span
															class="mandatory">*</span></td>
														<td class="bluebox" colspan="2"><input type="text"
															name="voucherDate" id="voucherDate" style="width: 100px"
															value='<s:property value="%{formatDate(voucherDate)}"/>'
															onkeyup="DateFormat(this,this.value,event,false,'3')" />
															<a
															href="javascript:show_calendar('advanceRequisitionPayment.voucherDate');"
															style="text-decoration: none">&nbsp;<img
																src="/egi/resources/erp2/images/calendaricon.gif"
																border="0" /></a>(dd/mm/yyyy)</td>
													</tr>
													<tr>
														<td class="greybox">&nbsp;</td>
														<td class="greybox">Narration:</td>
														<td class="greybox" colspan="4"><textarea
																name="narration" id="narration" type="text"
																style="width: 580px;"></textarea></td>
														<td></td>
													</tr>
												</table> <%@ taglib prefix="s" uri="/WEB-INF/tags/struts-tags.tld"%>
												<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
												<div align="center">
													<br>
													<div class="subheadnew">Approval Information</div>
													<br>
													<table width="100%" border="0" cellspacing="0"
														cellpadding="0">
														<tr>
															<td class="greybox" id="deptLabel">Approver <s:text
																	name="voucher.department" /><span class="mandatory">*</span></td>
															<td class="greybox"><s:select name="departmentid"
																	id="departmentid" list="dropdownData.departmentList"
																	listKey="id" listValue="name" headerKey="-1"
																	headerValue="----Choose----" value="%{departmentId}"
																	onchange="populateUser()" /></td>
															<td class="greybox">Approver Designation<span
																class="mandatory">*</span></td>
															<td class="greybox"><s:select name="designationId"
																	id="designationId" list="dropdownData.designationList"
																	listKey="designationId" listValue="designationName"
																	headerKey="-1" headerValue="----Choose----"
																	value="designationId" onchange="populateUser()" /></td>
														</tr>

														<tr>
															<egov:ajaxdropdown id="approverUserId"
																fields="['Text','Value']" dropdownId="approverUserId"
																url="voucher/common!ajaxLoadUser.action" />
															<td class="bluebox" width="13%">Approver<span
																class="mandatory">*</span></td>
															<td class="bluebox" width="33%"><s:select
																	id="approverUserId" name="approverUserId"
																	list="dropdownData.userList" headerKey="-1"
																	headerValue="----Choose----" listKey="id"
																	listValue="firstName" value="id" /></td>

														</tr>
													</table>
													<s:hidden name="type" id="type"></s:hidden>

												</div>


												<div class="buttonbottom" align="center">
													<s:submit method="create" value="Generate BPV"
														cssClass="buttonsubmit"
														onclick="return validateUser('approve','%{description}');"></s:submit>
												</div>

											</span>
										</div>


										<div class="tabbertab"
											style="border: 1px solid #ccc; height: 350px; overflow-y: scroll; overflow-x: scroll; margin-top: 10px; padding: 0px;">
											<h2>Advance Details</h2>
											<span>
												<table align="center" border="0" cellpadding="0"
													cellspacing="0">
													<tr>
														<td colspan="11"><div class="subheadsmallnew">Advance
																Details</div></td>
													</tr>
													<tr>
														<td colspan="11">
															<div style="overflow-x: auto; overflow-y: hidden;">
																<table width="100%" cellspacing="0" cellpadding="0"
																	border="0" align="center">
																	<tbody>
																		<tr>
																			<th class="bluebgheadtdnew" rowspan="2" width="25%">Advance
																				Requisition No.</th>
																			<th class="bluebgheadtdnew" rowspan="2" width="15%">Advance
																				Bill Date</th>
																			<th class="bluebgheadtdnew" rowspan="2" width="30%">Party
																				Name</th>
																			<th class="bluebgheadtdnew" colspan="3"
																				style="height: 32px;">Account Head</th>
																			<th class="bluebgheadtdnew" rowspan="2">Advance
																				Amount</th>
																		</tr>
																		<tr>
																			<th class="bluebgheadtdnew">Code</th>
																			<th class="bluebgheadtdnew">Debit Amount</th>
																			<th class="bluebgheadtdnew">Credit Amount</th>
																		</tr>

																		<s:iterator
																			value="advanceRequisition.egAdvanceReqDetailses"
																			var="detail">
																			<tr>
																				<td class="blueborderfortdnew"><div
																						align="center">
																						<s:property
																							value="%{#detail.egAdvanceRequisition.advanceRequisitionNumber}" />
																					</div></td>
																				<td class="blueborderfortdnew"><div
																						align="center">
																						<s:property
																							value="%{formatDate(#detail.egAdvanceRequisition.advanceRequisitionDate)}" />
																					</div></td>
																				<td class="blueborderfortdnew"><div
																						align="center">
																						<s:property
																							value="#detail.egAdvanceRequisition.egAdvanceReqMises.payto" />
																					</div></td>
																				<td class="blueborderfortdnew"><div
																						align="center">
																						<s:property value="#detail.chartofaccounts.glcode" />
																					</div></td>
																				<td class="blueborderfortdnew">
																					<div align="right">
																						<s:if test="#detail.debitamount == null">
						0.00
					</s:if>
																						<s:else>
																							<s:text name="payment.format.number">
																								<s:param name="value"
																									value="#detail.debitamount" />
																							</s:text>
																						</s:else>
																					</div>
																				</td>
																				<td class="blueborderfortdnew">
																					<div align="right">
																						<s:if test="#detail.creditamount == null">
						0.00
					</s:if>
																						<s:else>
																							<s:text name="payment.format.number">
																								<s:param name="value"
																									value="#detail.creditamount" />
																							</s:text>
																						</s:else>
																					</div>
																				</td>
																				<td class="blueborderfortdnew"><div
																						align="right">
																						<s:if
																							test="#detail.egAdvanceRequisition.advanceRequisitionAmount == null">
						0.00
				</s:if>
																						<s:else>
																							<s:text name="payment.format.number">
																								<s:param name="value"
																									value="#detail.egAdvanceRequisition.advanceRequisitionAmount" />
																							</s:text>
																						</s:else>
																					</div></td>
																			</tr>
																		</s:iterator>
																	</tbody>
																</table>
															</div>
															<div style="clear: both"></div>
														</td>
													</tr>
												</table>
											</span>
										</div>
								</td>
							</tr>
						</table>
					</div>
				</td>
			</tr>
		</table>
		</div>
		</div>
		<div class="subheadsmallnew" /></div>
		<div class="mandatory" align="left">* Mandatory Fields</div>
		</div>
		<input type="hidden" name="advanceRequisition.id"
			id="advanceRequisition.id"
			value='<s:property value="advanceRequisition.id"/>' />
		<div id="results" style="display: none"></div>
		<s:hidden name="actionName" id="actionName" />
	</s:form>
</body>
<script>
value = 'approve';
</script>
