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

<meta http-equiv="Content-Type"
	content="text/html; charset=windows-1252">
<link href="/EGF/resources/css/budget.css?rnd=${app_release_no}"
	rel="stylesheet" type="text/css" />
<link href="/EGF/resources/css/commonegovnew.css?rnd=${app_release_no}"
	rel="stylesheet" type="text/css" />
<link rel="stylesheet"
	href="/EGF/resources/css/tabber.css?rnd=${app_release_no}"
	TYPE="text/css">
<script type="text/javascript"
	src="/EGF/resources/javascript/tabber.js?rnd=${app_release_no}"></script>
	<script type="text/javascript"
	src="${pageContext.request.contextPath}/resources/javascript/voucherHelper.js?rnd=${app_release_no}"></script>
<script type="text/javascript"
	src="/EGF/resources/javascript/tabber2.js?rnd=${app_release_no}"></script>
<title><s:text name="remit.recovery.create.title" /></title>
<script>
var vTypeOfAccount="RECEIPTS_PAYMENTS,PAYMENTS"      ;                                 
function loadBank(fundId){
	populatebank({fundId:fundId.options[fundId.selectedIndex].value,typeOfAccount:vTypeOfAccount})	
}
function loadBankForFund(fundId){
	populatebank({fundId:fundId.options[fundId.selectedIndex].value})	
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
function populateAvailableBalance(accnumObj) 
{
			if (document.getElementById('voucherDate').value == '') {
				bootbox.alert("Please Select the Voucher Date!!");
				accnumObj.options.value = -1;
				return;
			}
			if (accnumObj.options[accnumObj.selectedIndex].value == -1)
				document.getElementById('availableBalance').value = '';
			else
				populateavailableBalance({
					bankaccount : accnumObj.options[accnumObj.selectedIndex].value,
					voucherDate : document.getElementById('voucherDate').value
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
		if(parseFloat(document.getElementById('totalAmount').value)>parseFloat(document.getElementById('availableBalance').value))
		{
			console.log("ins 44");
			return false;
		}
	}
	return true;
}
function checkLength(obj)
{
	if(obj.value.length>1024)
	{
		bootbox.alert('Max 1024 characters are allowed for comments. Remaining characters are truncated.')
		obj.value = obj.value.substring(1,1024);
	}
}

function populateAccNumbers(bankBranch){
	var fund = document.getElementById('fundId');
	branchId = bankBranch.options[bankBranch.selectedIndex].value.split("-")[1];
	bankId = bankBranch.options[bankBranch.selectedIndex].value.split("-")[0];
	populatebankaccount({branchId:branchId,bankId:bankId,fundId:fund.options[fund.selectedIndex].value+'&date='+new Date(), typeOfAccount:vTypeOfAccount});	
}
function populateAccNumbersForId(bankBranchId){
	var fund = document.getElementById('fundId');
	populatebankaccount({branchId:bankBranchId,fundId:fund.options[fund.selectedIndex].value})	
}
function onLoadTask(){
	var fund = document.getElementById('fundId');
	selectedFund = '<s:property value="fund.id"/>';
	for(i=0;i<fund.options.length;i++){
		if(fund.options[i].value==selectedFund){
			fund.options[i].selected = true;
		}
	}
	document.getElementById('fundId').disabled=true;
	//document.getElementById('voucherDate').value = day + "/" + month + "/" + year ;
	loadBank(document.getElementById('fundId'))
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

	function validate()
		{
		document.getElementById('lblError').innerHTML = "";
		if(!validateMIS())
			  return false;
		if(document.getElementById('vouchermis.function')!=null && document.getElementById('vouchermis.function').value=='-1')
		   {
		   document.getElementById('lblError').innerHTML='Please select Function';
		   return false;
		   }
		   if(document.getElementById('bank').value=='-1')
		   {
		   document.getElementById('lblError').innerHTML='Please select Bank';
		   return false;
		   }
		   if(document.getElementById('bankaccount').value=='-1')
		   {
		   document.getElementById('lblError').innerHTML='Please select Bank Account';
		   return false;
		   } 
		  
			return true;
		}
function onLoad(){
	var fund = document.getElementById('fundId');
	var scheme = document.getElementById('schemeid');
	var subscheme = document.getElementById('subschemeid');
	var fundsource = document.getElementById('fundsourceId');
	var department = document.getElementById('vouchermis.departmentid');
	var functionid = document.getElementById('vouchermis.function');
	jQuery(fund).attr('disabled', 'disabled');
	jQuery(scheme).attr('disabled', 'disabled');
	jQuery(subscheme).attr('disabled', 'disabled');
	jQuery(fundsource).attr('disabled', 'disabled');
	if(document.getElementById('approverDepartment'))
		document.getElementById('approverDepartment').value = "-1";
	if (jQuery("#bankBalanceCheck") == null || jQuery("#bankBalanceCheck").val() == "") {
		disableForm();
	}
}
function onSubmit()
{
	var balanceCheckMandatory='<s:text name="payment.mandatory"/>';
	var balanceCheckWarning='<s:text name="payment.warning"/>';
	var noBalanceCheck='<s:text name="payment.none"/>';
	if(validate()){
		 var myform = jQuery('#remittanceForm');
		// re-disabled the set of inputs that you previously
		var disabled = myform.find(':input:disabled').removeAttr('disabled'); 
		
		 if(jQuery("#bankBalanceCheck").val()==noBalanceCheck)
		{
			document.remittanceForm.action='${pageContext.request.contextPath}/deduction/remitRecovery-create.action';
		  return true;
		}
	else if(!balanceCheck() && jQuery("#bankBalanceCheck").val()==balanceCheckMandatory){
			 bootbox.alert("Insufficient Bank Balance.....");
			 return false;
			}
	else if(!balanceCheck() && jQuery("#bankBalanceCheck").val()==balanceCheckWarning){
		 var msg = confirm("Insufficient Bank Balance. Do you want to process ?");
		 if (msg == true) {
			 document.remittanceForm.action='${pageContext.request.contextPath}/deduction/remitRecovery-create.action';
			 document.remittanceForm.submit();
			return true;
		 } else {
			 undoLoadingMask();
		   	return false;
			}
		}
	else{
		document.remittanceForm.action='${pageContext.request.contextPath}/deduction/remitRecovery-create.action';
	 	document.remittanceForm.submit();
	}
			
	}
		return false;
		
	
}
function validateCutOff()
{
var cutOffDatePart=document.getElementById("cutOffDate").value.split("/");
var voucherDatePart=document.getElementById("voucherDate").value.split("/");
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
</script>
</head>
<body onload="return onLoad();">
	<s:form action="remitRecovery" theme="simple" name="remittanceForm"
		id="remittanceForm">
		<s:push value="model">
			<jsp:include page="../budget/budgetHeader.jsp">
				<jsp:param name="heading" value="Remittance Recovery" />
			</jsp:include>
			<s:token />
			<div align="center">
				<font style='color: red;'>
					<p class="error-block" id="lblError"></p>
				</font>
			</div>
			<span class="mandatory1">
				<div id="Errors">
					<s:actionerror />
					<s:fielderror />
				</div> <s:actionmessage />
			</span>

			<div class="formmainbox">
				<div class="subheadnew">
					<s:text name="remit.recovery.new.title" />
				</div>
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
													<div class="tabbertab" id="searchtab">
														<h2>
															<s:text name="remit.recovery.header" />
														</h2>
														<span>
															<table width="100%" border="0" cellspacing="0"
																cellpadding="0">

																<tr>
																	<td align="center" colspan="6" class="serachbillhead"><s:text
																			name="remit.recovery.header" /></td>
																</tr>
																<tr>
																	<td class="bluebox">&nbsp;</td>
																	<s:if test="%{shouldShowHeaderField('vouchernumber')}">
																		<td class="bluebox"><s:text name="voucher.number" /><span
																			class="mandatory1">*</span></td>
																		<td class="bluebox"><s:textfield
																				name="voucherNumber" id="vouchernumber" /></td>
																	</s:if>
																	<td class="bluebox" width="18%"><s:text
																			name="voucher.date" />&nbsp;<span class="mandatory1">*</span></td>
																	<s:date name='voucherDate' var="voucherDateId"
																		format='dd/MM/yyyy' />
																	<td class="bluebox" width="34%">
																		<div name="daterow">
																			<s:textfield name="voucherDate" id="voucherDate"
																				maxlength="10" readonly="true"
																				onkeyup="DateFormat(this,this.value,event,false,'3')"
																				size="15" value="%{voucherDateId}" />
																			<A
																				href="javascript:show_calendar('forms[0].voucherDate',null,null,'DD/MM/YYYY');"
																				style="text-decoration: none" align="left"><img
																				width="18" height="18" border="0" align="absmiddle"
																				alt="Date"
																				src="/egi/resources/erp2/images/calendaricon.gif" /></A>
																		</div>
																	</td>
																</tr>
																<tr>
																	<jsp:include
																		page="../voucher/vouchertrans-filter-new.jsp" />

																</tr>
																<tr>
																	<td class="bluebox">&nbsp;</td>
																	<egov:ajaxdropdown id="bank" fields="['Text','Value']"
																		dropdownId="bank"
																		url="voucher/common-ajaxLoadBanksByFundAndType.action" />
																	<td class="bluebox"><s:text name="bank" />&nbsp;<span
																		class="bluebox"><span class="mandatory1">*</span></span></td>
																	<td class="bluebox"><s:select name="bank"
																			id="bank" list="dropdownData.bankList"
																			listKey="bank.id+'-'+id"
																			listValue="bank.name+' '+branchname" headerKey="-1"
																			headerValue="----Choose----" onclick="validateFund()"
																			onChange="populateAccNumbers(this);" /></td>
																	<egov:ajaxdropdown id="accountNumber"
																		fields="['Text','Value']" dropdownId="bankaccount"
																		url="voucher/common-ajaxLoadAccNumAndType.action" />
																	<td class="bluebox"><s:text name="account.number" />&nbsp;<span
																		class="bluebox"><span class="mandatory1">*</span></span></td>
																	<td class="bluebox"><s:select
																			name="commonBean.accountNumberId" id="bankaccount"
																			list="dropdownData.accNumList" listKey="id"
																			listValue="chartofaccounts.glcode+'--'+accountnumber+'--'+accounttype"
																			onChange="populateAvailableBalance(this);"
																			headerKey="-1" headerValue="----Choose----" /></td>
																</tr>
																<tr class="greybox">
																	<td class="greybox">&nbsp;</td>
																	<td class="greybox">Payment Amount&nbsp;</td>
																	<td class="greybox"><label name="remitAmount"
																		id="remitAmount" /></td>
																	<egov:updatevalues id="availableBalance"
																		fields="['Text']"
																		url="/payment/payment-ajaxGetAccountBalance.action" />
																	<td class="greybox"><span id="balanceText"
																		style="display: none" width="18%"><s:text
																				name="balance.available" />&nbsp;</span></td>
																	<td class="greybox"><span id="balanceAvl"
																		width="32%"><s:textfield
																				name="commonBean.availableBalance"
																				id="availableBalance" readonly="true"
																				style="text-align:right"
																				value="%{commonBean.availableBalance}" /></span></td>
																</tr>
																<tr>
																	<td class="bluebox">&nbsp;</td>
																	<td class="bluebox"><s:text name="modeofpayment" />&nbsp;</td>
																	<td class="bluebox"><s:radio name="modeOfPayment"
																			id="paymentMode" list="%{modeOfCollectionMap}" /></td>
																	<td class="bluebox"><s:text name="remit.party.to" />&nbsp;</td>
																	<td class="bluebox"><s:textfield name="remittedTo"
																			id="remittedTo" />&nbsp;</td>
																</tr>
																<tr>
																	<td class="greybox">&nbsp;</td>
																	<td class="greybox">Narration</td>
																	<td class="greybox" colspan="4"><textarea
																			name="description" id="narration" type="text"
																			style="width: 580px;"></textarea></td>
																	<td></td>
																</tr>
															</table>
														</span>
													</div>
													<div class="tabbertab" id="contractortab">
														<h2>
															<s:text name="remit.recovery.detais" />
														</h2>
														<span>
															<table align="center" border="0" cellpadding="0"
																cellspacing="0" class="newtable">
																<tr>
																	<td align="center" colspan="6" class="serachbillhead">
																		<s:text name="remit.recovery.detais" />
																	</td>
																</tr>

																<tr>
																	<td colspan="6">
																		<div style="float: left; width: 100%;">

																			<jsp:include page="remitRecoveryPayment-form.jsp" />
																			<s:hidden name="remittanceBean.recoveryId" />
																			<s:hidden name="typeOfAccount" id="typeOfAccount" value="%{typeOfAccount}" />
																			<div class="yui-skin-sam" align="center">
																				<div id="recoveryDetailsTableNew"></div>
																			</div>

																			<script>
								populateRecoveryDetailsForPayment();
								document.getElementById('recoveryDetailsTableNew').getElementsByTagName('table')[0].width="80%";
							 </script>
																			<br>
															</table>
															<table align="center" id="totalAmtTable">
																<tr>
																	<td width="800"></td>
																	<td>Total Amount</td>
																	<td><s:textfield name="remittanceBean.totalAmount"
																			id="totalAmount" style='width:90px;text-align:right'
																			readonly="true" value="0" /></td>
																</tr>

																</div>
																</td>
																</tr>
															</table>
														</span>
													</div>

												</div> <!-- tabber div -->
											</td>
										</tr>
									</table>
								</div>
							</td>
						</tr>
					</table>

				</div>
				<s:hidden name="cutOffDate" id="cutOffDate" />
				<s:hidden name="bankBalanceCheck" id="bankBalanceCheck" value="%{bankBalanceCheck}" />
				<%@ include file='../payment/commonWorkflowMatrix.jsp'%>
				<%@ include file='../workflow/commonWorkflowMatrix-button.jsp'%>
			</div>


			<script type="text/javascript">
	//bootbox.alert('<s:property value="fund.id"/>');                               
	//populatebank({fundId:<s:property value="fundId.id"/>,typeOfAccount:"PAYMENT,RECEIPTS_PAYMENTS"});
	populatebank({fundId:<s:property value="fundId.id"/>,typeOfAccount:vTypeOfAccount})	
	calcTotalForPayment();
	</script>

			<s:if test="%{!validateUser('createpayment')}">
				<script>
			//document.getElementById('searchBtn').disabled=true;
			document.getElementById('Errors').innerHTML='<s:text name="payment.invalid.user"/>';
			if(document.getElementById('vouchermis.departmentid'))
			{
				var d = document.getElementById('vouchermis.departmentid');
				d.options[d.selectedIndex].text='----Choose----';
				d.options[d.selectedIndex].text.value=-1;
			}
			disableControls(0,true);
			document.getElementById("closeButton").disabled=false;
		</script>
			</s:if>
			<s:if test="%{validateUser('balancecheck')}">
				<script>
			if(document.getElementById('balanceText'))
			{
				document.getElementById('balanceText').style.display='block';
				document.getElementById('balanceAvl').style.display='block';
			}
			
		</script>
			</s:if>
		</s:push>

	</s:form>


</body>

</html>
