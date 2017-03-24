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
<link href="/EGF/resources/css/budget.css?rnd=${app_release_no}" rel="stylesheet"
	type="text/css" />
<link href="/EGF/resources/css/commonegovnew.css?rnd=${app_release_no}" rel="stylesheet"
	type="text/css" />
<link rel="stylesheet" href="/EGF/resources/css/tabber.css?rnd=${app_release_no}"
	TYPE="text/css">
<script type="text/javascript" src="/EGF/resources/javascript/tabber.js?rnd=${app_release_no}"></script>
<script type="text/javascript"
	src="/EGF/resources/javascript/remitrecovery-helper.js?rnd=${app_release_no}"></script>
<script type="text/javascript"
	src="/EGF/resources/javascript/tabber2.js?rnd=${app_release_no}"></script>
<title><s:text name="remit.recovery.create.title" /></title>
<script>
var vTypeOfAccount="RECEIPTS_PAYMENTS,PAYMENTS";
function loadBank(fundId){
	populatebank({fundId:fundId.options[fundId.selectedIndex].value,typeOfAccount:"PAYMENT,RECEIPTS_PAYMENTS"})	
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
function populateAccNumbers(bankBranch){
	var fund = document.getElementById('fundId');
	id = bankBranch.options[bankBranch.selectedIndex].value.split("-")[1]
	populatebankaccount({branchId:id,fundId:fund.options[fund.selectedIndex].value})	
}
function populateAccNumbersForId(bankBranchId){
	var fund = document.getElementById('fundId');
	populatebankaccount({branchId:bankBranchId,fundId:fund.options[fund.selectedIndex].value})	
}
function onLoadTask(){ 
}
function populateNarration(accnumObj){
   if(accnumObj.options[accnumObj.selectedIndex].value=="")
		document.getElementById('accnumnar').value='';
    else
    {
	var accnum =  accnumObj.options[accnumObj.selectedIndex].value;
	var bankbranchObj=document.getElementById('bank');
	var bankbranchId = bankbranchObj.options[bankbranchObj.selectedIndex].value;
	var index=bankbranchId.indexOf("-");
	var branchId=bankbranchId.substring(index+1,bankbranchId.length);
	var url = '../voucher/common!loadAccNumNarration.action?accnum='+accnum+'&branchId='+branchId;
	YAHOO.util.Connect.asyncRequest('POST', url, postTypeFrom, null);
	}
}

function populateAvailableBalance(accnumObj){
	if(document.getElementById('voucherDate').value=='')
	{
		bootbox.alert("Please Select the Voucher Date!!");
		accnumObj.options.value=-1;
		return;
	}
	if(accnumObj.options[accnumObj.selectedIndex].value=="")
		document.getElementById('availableBalance').value='';
	else
		populateavailableBalance({bankaccount:accnumObj.options[accnumObj.selectedIndex].value,voucherDate:document.getElementById('voucherDate').value+'&date='+new Date()});

}

var callback = {
		success: function(o){
	        bootbox.alert(o.responseText.value);
			document.getElementById('availableBalance').value=o.responseText;
			},
			failure: function(o) {
		    }
		}                  
		
		
function populateAccNum(branch){
	var fundObj = document.getElementById('fundId');

	var bankbranchId = branch.options[branch.selectedIndex].value;
	if(bankbranchId!="")
{

	var index=bankbranchId.indexOf("-");
	var bankId = bankbranchId.substring(0,index);
	var brId=bankbranchId.substring(index+1,bankbranchId.length);
	populatebankaccount({fundId: fundObj.options[fundObj.selectedIndex].value,bankId:bankId,branchId:brId,typeOfAccount:vTypeOfAccount})
}else
{

}


}


var postTypeFrom = {
success: function(o) {
		document.getElementById('accnumnar').value= o.responseText;
		},
    failure: function(o) {
    	bootbox.alert('failure');
    }
}

function checkLength(obj)
{
	if(obj.value.length>1024)
	{
		bootbox.alert('Max 1024 characters are allowed for comments. Remaining characters are truncated.')
		obj.value = obj.value.substring(1,1024);
	}
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
function validateApproveUser(name,value){
	document.getElementById("actionName").value= name;
<s:if test="%{wfitemstate !='END'}">
	 if( (value == 'Approve' || value=='Send for Approval' || value == 'Forward' || value == 'Save And Forward') && null != document.getElementById("approverUserId") && document.getElementById("approverUserId").value == -1){
		bootbox.alert("Please Select the user");
		return false;
	}
</s:if>
	return true;
}

function printVoucher(){
	document.forms[0].action='../report/billPaymentVoucherPrint!print.action?id=<s:property value="paymentheader.id"/>';
	document.forms[0].submit();
}
	function validate(obj,name,value)
		{
	
		document.getElementById('lblError').innerHTML = "";
			if(!validateMIS())
			  return false;
		 if(document.getElementById('bank').value=="")
		   {
		   document.getElementById('lblError').innerHTML='Please select Bank';
		   return false;
		   }
		   if(document.getElementById('bankaccount').value=="")
		   {
		   document.getElementById('lblError').innerHTML='Please select Bank Account';
		   return false;
		   }
		if(document.getElementById('balanceAvl') && document.getElementById('balanceAvl').style.display=="block" )
			{
			if(obj.id!='wfBtn1') // in case of Reject
				{
			if(parseFloat(document.getElementById('totalAmount').value) > parseFloat(document.getElementById('availableBalance').value))
				{

				var insuffiecientBankBalance ='<s:text name="insuffiecientBankBalance"/>';
					bootbox.alert(insuffiecientBankBalance);
					return false;
				}
				}
			}  
			if(!validateApproveUser(name,value))    
				return false;
			
			enableAll();  
			return true;
		}
		
	function	enableAll()
		{
		var frmIndex=0;
		for(var i=0;i<document.forms[frmIndex].length;i++)
		document.forms[frmIndex].elements[i].disabled =false;
		}
	function updateVoucherNumber()
	{
	document.getElementById('voucherNumber').value=document.getElementById('voucherNumberRest').value;
	}

</script>
</head>
<body>
	<s:form action="remitRecovery" theme="simple" name="remittanceForm">
		<s:push value="model">
			<jsp:include page="../budget/budgetHeader.jsp">
				<jsp:param name="heading" value="Remittance Recovery" />
			</jsp:include>
			<span class="mandatory"> <s:actionerror /> <s:fielderror /> <s:actionmessage />
				<div align="center" class="error-block" id="lblError"
					style="font: bold; text-align: center"></div>
			</span>

			<div class="formmainbox">
				<div class="subheadnew">
					<s:text name="remit.recovery.edit.title" />
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
																	<td class="bluebox" width="10%"></td>
																	<s:if test="%{shouldShowHeaderField('vouchernumber')}">
																		<td class="bluebox" width="22%"><s:text
																				name="voucher.number" /> <span class="mandatory">*</span>
																		</td>
																		<td class="bluebox" width="22%">
																			<table width="100%">
																				<tr>
																					<td style="width: 25%"><input type="text"
																						name="voucherNumberPrefix"
																						id="voucherNumberPrefix" readonly="true"
																						style="width: 100%" /></td>
																					<td width="75%"><input type="text"
																						name="voucherNumberRest" id="voucherNumberRest"
																						onblur="updateVoucherNumber();" /></td>
																				</tr>
																			</table>
																		</td>

																		<s:hidden name="voucherNumber" id="voucherNumber" />
																	</s:if>
																	<s:else>
																		<td class="bluebox" width="22%"><s:text
																				name="voucher.number" /> <span class="mandatory">*</span>
																		</td>
																		<td class="bluebox" width="22%">
																			<table width="100%">
																				<tr>
																					<td style="width: 25%"><input type="text"
																						name="voucherNumberPrefix"
																						id="voucherNumberPrefix" readonly="true"
																						style="width: 100%" /></td>
																					<td width="75%"><input type="text"
																						name="voucherNumberRest" id="voucherNumberRest"
																						readonly="true" /></td>
																				</tr>
																			</table>
																		</td>
																		<s:hidden name="voucherNumber" id="voucherNumber" />
																	</s:else>
																	<s:hidden name="id" />
																	<td class="bluebox" width="18%%"><s:text
																			name="voucher.date" /> <span class="mandatory">*</span>
																	</td>
																	<td class="bluebox" width="34%"><s:date
																			name='voucherDate' var="voucherDateId"
																			format='dd/MM/yyyy' /> <s:textfield
																			name="voucherDate" id="voucherDate"
																			value='%{voucherDateId}'
																			onkeyup="DateFormat(this,this.value,event,false,'3')" />
																		<a
																		href="javascript:show_calendar('dbpform.voucherDate');"
																		style="text-decoration: none">&nbsp;<img
																			tabIndex="-1"
																			src="/egi/resources/erp2/images/calendaricon.gif"
																			border="0" />
																	</A></td>
																</tr>
																<tr>
																	<jsp:include
																		page="../voucher/vouchertrans-filter-new.jsp" />

																</tr>
																<tr>
																	<td class="greybox"></td>
																	<td class="greybox"><s:text name="bank" /> <span
																		class="greybox"><span class="mandatory">*</span></span></td>
																	<egov:ajaxdropdown id="bankId"
																		fields="['Text','Value']" dropdownId="bankId"
																		url="/voucher/common!ajaxLoadBanksByFundAndType.action" />
																	<td class="greybox"><s:select
																			name="commonBean.bankId" id="bank"
																			list="dropdownData.bankList" listKey="bankBranchId"
																			listValue="bankBranchName" headerKey=""
																			headerValue="----Choose----"
																			onChange="populateAccNum(this);" /></td>
																	<egov:ajaxdropdown id="bankaccount"
																		fields="['Text','Value']" dropdownId="bankaccount"
																		url="voucher/common!ajaxLoadAccNumAndType.action" />
																	<td class="greybox" width="22%"><s:text
																			name="account.number" /><span class="bluebox"><span
																			class="mandatory">*</span></span></td>
																	<td class="greybox" width="22%"><s:select
																			name="commonBean.accountNumberId" id="bankaccount"
																			list="dropdownData.accNumList" listKey="id"
																			listValue="chartofaccounts.glcode+'--'+accountnumber+'--'+accounttype"
																			headerKey="" headerValue="----Choose----"
																			onChange="populateNarration(this);populateAvailableBalance(this);" />
																		<s:textfield name="commonBean.accnumnar"
																			id="accnumnar" readonly="true" tabindex="-1" /></td>
																</tr>
																<tr>
																	<td class="bluebox">&nbsp;</td>
																	<td class="bluebox">Payment Amount</td>
																	<td class="bluebox"><label name="remitAmount"
																		id="remitAmount" /></td>
																	<egov:updatevalues id="availableBalance"
																		fields="['Text']"
																		url="/payment/payment!ajaxGetAccountBalance.action" />
																	<td class="bluebox" id="balanceText"
																		style="display: none" width="18%"><s:text
																			name="balance.available" /></td>
																	<td class="bluebox" id="balanceAvl"
																		style="display: none" width="32%"><s:textfield
																			name="commonBean.availableBalance"
																			id="availableBalance" readonly="readonly"
																			style="text-align:right"
																			value="%{commonBean.availableBalance}" /></td>
																</tr>
																<tr>
																	<td class="greybox">&nbsp;</td>
																	<td class="greybox">Mode of Payment</td>
																	<td class="greybox"><s:radio name="modeOfPayment"
																			id="paymentMode" list="%{modeOfCollectionMap}" /></td>
																	<td class="greybox"><s:text name="remit.party.to" />&nbsp;</td>
																	<td class="greybox"><s:textfield name="remittedTo"
																			id="remittedTo" />&nbsp;</td>
																</tr>
																<tr>
																	<td class="bluebox">&nbsp;</td>
																	<td class="bluebox">Narration:</td>
																	<td class="bluebox" colspan="4">
																	<td class="bluebox" colspan="4"><s:textarea
																			name="description" id="description"
																			value="%{voucherHeader.description}" type="text"
																			style="width:580px;"></s:textarea>
																	<td class="bluebox" colspan="4"><s:textarea
																			name="description" id="description"
																			value="%{voucherHeader.description}" type="text"
																			style="width:580px;"></s:textarea></td>
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
																	<td colspan="6"><div class="subheadsmallnew">
																			<s:text name="remit.recovery.detais" />
																		</div></td>
																</tr>
																<tr>
																	<td colspan="6">
																		<div style="float: left; width: 100%;">

																			<jsp:include page="remitRecoveryPayment-form.jsp" />
																			<s:hidden name="remittanceBean.recoveryId" />
																			<div class="yui-skin-sam" align="center">
																				<div id="recoveryDetailsTableNew"></div>
																			</div>
																			<script>
								populateRecoveryDetailsForPayment();
								document.getElementById('recoveryDetailsTableNew').getElementsByTagName('table')[0].width="80%"
							 </script>
																			<br>

																			<table align="center" id="totalAmtTable">
																				<tr>
																					<td width="1037"></td>
																					<td>Total Amount</td>
																					<td><s:textfield
																							name="remittanceBean.totalAmount"
																							id="totalAmount" size="14"
																							style='text-align:right' readonly="true"
																							value="0" /></td>
																				</tr>
																			</table>

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
			</div>
			<s:if test="%{wfitemstate !='END'}">

				<%@include file="../voucher/workflowApproval.jsp"%>
			</s:if>
			</div>
			</div>
			<table>
				<tr>
					<td class="bluebox">&nbsp;</td>
					<td class="bluebox">Comments</td>
					<td class="bluebox" colspan="4"><s:textarea name="comments"
							id="comments" cols="100" rows="3" onblur="checkLength(this)"
							value="%{getComments()}" /></td>
				</tr>
			</table>




			<s:if test="%{showApprove}">

				<s:if test="%{paymentheader.state.value != 'NEW'}">
					<s:if test="%{paymentheader.state.id!=null}">
						<div id="labelAD" align="center">
							<div class="subheadsmallnew">
								<strong><s:text name="inbox.payment.history" /></strong>
							</div>
						</div>
						<div id="wfHistoryDiv">
							<c:import url="/WEB-INF/jsp/workflow/workflowHistory.jsp"
								context="/egi">
								<c:param name="stateId" value="${paymentheader.state.id}"></c:param>
							</c:import>
						</div>
					</s:if>
				</s:if>
			</s:if>
			<div class="buttonbottom" id="buttondiv">
				<s:hidden name="paymentid" value="%{paymentheader.id}" />

				<s:hidden name="actionname" id="actionName" value="%{action}" />
				<s:if test="%{showButtons}">
					<s:iterator value="%{getValidActions()}" var="p" status="s">
						<s:submit type="submit" cssClass="buttonsubmit"
							value="%{description}" id="wfBtn%{#s.index}" name="%{name}"
							method="edit"
							onclick="return validate(this,'%{name}','%{description}')" />
					</s:iterator>
				</s:if>
				<s:if test="%{wfitemstate !='END'}">
				</s:if>
				<s:else>
					<s:submit cssClass="button" id="printPreview" value="Print Preview"
						onclick="printVoucher()" />
				</s:else>
				<s:if test="%{showCancel}">
					<s:submit type="submit" cssClass="buttonsubmit"
						value="Cancel Payment" id="cancelPayment" name="cancel"
						method="sendForApproval"
						onclick="document.getElementById('actionName').value='cancelPayment';" />
				</s:if>
				<input type="submit" id="closeButtonNew" value="Close"
					onclick="javascript:window.close()" class="button" />
			</div>
			<script type="text/javascript">
	//bootbox.alert('<s:property value="fund.id"/>');                               
	calcTotalForPayment();
	</script>
		</s:push>
		<SCRIPT type="text/javascript">

   var tempVoucherNumber='<s:property value="voucherHeader.voucherNumber"/>';
			   var prefixLength='<s:property value="voucherNumberPrefixLength"/>';
			   document.getElementById('voucherNumberPrefix').value=tempVoucherNumber.substring(0,prefixLength);
			   document.getElementById('voucherNumberRest').value=tempVoucherNumber.substring(prefixLength,tempVoucherNumber.length-1);
			   updateVoucherNumber();
		var frmIndex=0;
		for(var i=0;i<document.forms[frmIndex].length;i++)
		document.forms[frmIndex].elements[i].disabled =true;
		disableYUIAddDeleteButtons(true);
		if(document.getElementById("closeButton"))
			document.getElementById("closeButton").disabled=false;
		if(document.getElementById("closeButtonNew"))
			document.getElementById("closeButtonNew").disabled=false;
		if(document.getElementById("comments"))
			document.getElementById("comments").disabled=false;
		if(document.getElementById("paymentid"))
			document.getElementById("paymentid").disabled=false;
		if(document.getElementById("actionName"))
			document.getElementById("actionName").disabled=false;
		if(document.getElementById("printPreview"))
			document.getElementById("printPreview").disabled=false;
		if(document.getElementById("cancelPayment"))
			document.getElementById("cancelPayment").disabled=false;	
		if(document.getElementById("remittedTo"))
			document.getElementById("remittedTo").disabled=false;	
	<s:iterator value="listRemitBean" status="stat">
		var index = '<s:property value="#stat.index"/>';
			document.getElementById("listRemitBean["+index+"].partialAmount").disabled=false;
	</s:iterator>
		if(null != document.getElementById("departmentid") ){
			document.getElementById("departmentid").disabled=false;    
			document.getElementById("designationId").disabled=false;
			document.getElementById("approverUserId").disabled=false;
			
		}
		if(document.getElementById("bank"))
		  document.getElementById("bank").disabled=false;
		if(document.getElementById("bankaccount"))
		 	document.getElementById("bankaccount").disabled=false;
		if(document.getElementById("voucherDate"))
		 	document.getElementById("voucherDate").disabled=false;
		 	
				
		
	 <s:if test="%{validateUser('balancecheck')}">
			if(document.getElementById('balanceText'))
			{
				document.getElementById('balanceText').style.display='block';
				document.getElementById('balanceAvl').style.display='block';
				var x=document.getElementById('availableBalance').value;
				x=parseFloat(x);
				document.getElementById('availableBalance').value=x.toFixed(2);
			}
	</s:if>	
		
		
		if(document.getElementById("wfBtn0"))
		{
		document.getElementById("wfBtn0").disabled=false;
		}
		if(document.getElementById("wfBtn1"))
		{
		document.getElementById("wfBtn1").disabled=false;
		}
		if(document.getElementById("wfBtn2"))
		{
		document.getElementById("wfBtn3").disabled=false;
		}
		
		
		
</SCRIPT>

		<s:if test="%{validateUser('balancecheck')}">

			<SCRIPT>
			if(document.getElementById('balanceText'))
			{
				document.getElementById('balanceText').style.display='block';
				document.getElementById('balanceAvl').style.display='block';
			}
		
</SCRIPT>
		</s:if>

	</s:form>

</body>

</html>
