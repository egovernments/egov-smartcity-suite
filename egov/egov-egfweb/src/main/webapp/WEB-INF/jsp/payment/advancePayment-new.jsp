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
<title><s:text name="advance.payment.title" /></title>
<script type="text/javascript"
	src="/EGF/resources/javascript/advancePaymentHelper.js?rnd=${app_release_no}"></script>
<script type="text/javascript"
	src="/EGF/resources/javascript/voucherHelper.js?rnd=${app_release_no}"></script>
<script>
window.history.forward(1);
function noBack() {
	window.history.forward(); 
}

function populateAccNum(bankBranch){
	var vTypeOfAccount = '<s:property value="%{typeOfAccount}"/>';
	var fundId = '<s:property  value="%{advanceRequisition.egAdvanceReqMises.fund.id}" />';
	var bankAndBranchIds =  bankBranch.options[bankBranch.selectedIndex].value.split("-");
	var bankId = bankAndBranchIds[0];
	var bankBranchId = bankAndBranchIds[1];
	populateaccountNumber({fundId: fundId,bankId:bankId,branchId:bankBranchId,typeOfAccount:vTypeOfAccount})
}

function validateInputBeforeSubmit() {
	voucherDate = document.advancePaymentForm.voucherDate.value;
	advanceRequisitionDate = '<s:date name="advanceRequisition.advanceRequisitionDate" format="dd/MM/yyyy"/>';
	currentDate = '<%=new java.text.SimpleDateFormat("dd/MM/yyyy").format(new java.util.Date())%>';
	if(voucherDate == '') {
		dom.get("advancePayment_error").innerHTML='<s:text name="arf.payment.voucherdate.required"/>'; 
        dom.get("advancePayment_error").style.display='';
        window.scroll(0,0);
		return false;
	}	 

	if(compareDate(voucherDate,advanceRequisitionDate) == 1 ) {
		dom.get("advancePayment_error").innerHTML='<s:text name="arf.payment.voucherdate.lessthan.advancerequisitiondate" />';
	    dom.get("advancePayment_error").style.display='';
	    window.scroll(0,0);
	    return false;
	}

	if(compareDate(voucherDate,currentDate) == -1 ){
		dom.get("advancePayment_error").innerHTML='<s:text name="arf.validate.payment.voucherdate.greaterthan.currentDate" />'; 
       	dom.get("advancePayment_error").style.display='';
       	window.scroll(0,0);
		return false;
	 }	
	
	bankBranchId = document.getElementById('bankId').value;
	if(bankBranchId == '' || bankBranchId == 0 || bankBranchId == -1) {
		dom.get("advancePayment_error").innerHTML='<s:text name="arf.bankbranch.required"/>'; 
        dom.get("advancePayment_error").style.display='';
        window.scroll(0,0);
		return false;
	}	 

	accountNumber = document.getElementById('accountNumber').value;
	if(accountNumber == '' || accountNumber == 0 || accountNumber == -1) {
		dom.get("advancePayment_error").innerHTML='<s:text name="arf.accountnumber.required"/>'; 
        dom.get("advancePayment_error").style.display='';
        window.scroll(0,0);
		return false;
	}	
	
	dom.get("advancePayment_error").style.display="none";
	return true;	
}

function validateAppoveUser(name,value){
	document.getElementById("actionName").value= name;
	<s:if test="%{wfitemstate =='END'}">
		if(value == 'Approve' || value == 'Reject') {
			document.getElementById("approverUserId").value=-1;
			return true;
		}
	</s:if>
	<s:else>
		if( (value == 'Approve' || value == 'Forward' || value=='Save And Forward' ) && null != document.getElementById("approverUserId") && document.getElementById("approverUserId").value == -1){
			bootbox.alert('<s:text name="arf.approver.required"/>');
			return false;
		}
	</s:else>
	
	return true;
}

</script>
</head>
<body class="yui-skin-sam" onload="noBack();"
	onpageshow="if(event.persisted) noBack();" onunload="">
	<div class="error-block" id="advancePayment_error"
		style="display: none; color: red;"></div>
	<s:if test="%{hasErrors()}">
		<div class="error-block" style="color: red; align: left">
			<s:actionerror />
			<s:fielderror />
			<s:actionmessage />
		</div>
	</s:if>
	<s:form action="advancePayment" theme="css_xhtml"
		name="advancePaymentForm">
		<jsp:include page="../budget/budgetHeader.jsp">
			<jsp:param value="Advance Payment" name="heading" />
		</jsp:include>
		<s:token />
		<s:push value="model">
			<s:if test="%{advanceRequisitionId != null}">
				<s:hidden name="advanceRequisitionId"
					value="%{advanceRequisitionId}" id="advanceRequisitionId" />
			</s:if>
			<div class="formmainbox">
				<div class="formheading" />
				<div class="subheadnew">
					<s:text name="advance.payment.title" />
				</div>
			</div>
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td>
						<table width="100%" border="0" cellspacing="0" cellpadding="0">
							<tr>
								<td>&nbsp;</td>
							</tr>
							<tr>
								<td width="10%" class="bluebox"></td>
								<td class="bluebox"><s:text name="advancepayment.date" />&nbsp;<span
									class="mandatory">*</span></td>
								<td class="bluebox" style="white-space: nowrap;"><s:date
										name="voucherDate" var="voucherDateFormat" format="dd/MM/yyyy" />
									<s:textfield name="voucherDate" value="%{voucherDateFormat}"
										id="voucherDate" maxlength="10" size="15"
										onfocus="javascript:vDateType='3';"
										onkeyup="DateFormat(this,this.value,event,false,'3')" /> <a
									href="javascript:show_calendar('forms[0].voucherDate',null,null,'DD/MM/YYYY');"
									onmouseover="window.status='Date Picker';return true;"
									onmouseout="window.status='';return true;"> <img
										id="voucherDateImage"
										src="/egi/resources/erp2/images/calendaricon.gif"
										alt="Calendar" width="16" height="16" border="0"
										align="absmiddle" /></a></td>
								<td class="bluebox"><s:text name="arf.department" /></td>
								<td class="bluebox"><s:property
										value="%{advanceRequisition.egAdvanceReqMises.egDepartment.deptName}" />
								</td>
							</tr>
							<tr>
								<td width="10%" class="greybox"></td>
								<td class="greybox"><s:text name="arf.arfdate" /></td>
								<td class="greybox"><s:date
										name="advanceRequisition.advanceRequisitionDate"
										format="dd/MM/yyyy" /></td>
								<td class="greybox"><s:text name="arf.arfnumber" /></td>
								<td class="greybox"><a href="#"
									onclick="viewARF('<s:property value='%{advanceRequisition.egAdvanceReqMises.sourcePath}'/>')">
										<s:property
											value="%{advanceRequisition.advanceRequisitionNumber}" />
								</a></td>
							</tr>
							<tr>
								<td width="10%" class="bluebox"></td>
								<td class="bluebox"><s:text name="arf.function" /></td>
								<td class="bluebox"><s:property
										value="%{advanceRequisition.egAdvanceReqMises.function.name}" /></td>
								<td class="bluebox"><s:text name="arf.fund" /></td>
								<td class="bluebox"><s:property
										value="%{advanceRequisition.egAdvanceReqMises.fund.Name}" /></td>
							</tr>
							<tr>
								<td width="10%" class="greybox"></td>
								<td class="greybox"><s:text name="arf.advance.coa" /></td>
								<td class="greybox"><s:iterator
										value="advanceRequisition.egAdvanceReqDetailses" var="detail">
										<s:property value="#detail.chartofaccounts.glcode" /> - <s:property
											value="#detail.chartofaccounts.name" />
									</s:iterator></td>
								<td class="greybox"><s:text name="arf.advanceamount" /></td>
								<td class="greybox" style="text-align: center"><s:text
										name="payment.format.number">
										<s:param name="value"
											value="advanceRequisition.advanceRequisitionAmount" />
									</s:text></td>
							</tr>
							<tr>
								<td width="10%" class="bluebox"></td>
								<td class="bluebox"><s:text name="arf.contractor" /></td>
								<td class="bluebox"><s:property
										value="%{advanceRequisition.egAdvanceReqMises.payto}" /></td>
								<td class="bluebox"></td>
								<td class="bluebox"></td>
							</tr>
							<tr>
								<td width="10%" class="greybox"></td>
								<td class="greybox"><s:text name="arf.bankbranch" />&nbsp;<span
									class="mandatory">*</span></td>
								<td class="greybox"><s:select name="commonBean.bankId"
										id="bankId" list="dropdownData.bankBranchList"
										listKey="bank.id+'-'+id"
										listValue="bank.name+' - '+branchname" headerKey="-1"
										headerValue="----Choose----" onChange="populateAccNum(this);" />
									<egov:ajaxdropdown id="accountNumber" fields="['Text','Value']"
										dropdownId="accountNumber"
										url="voucher/common!ajaxLoadAccNumAndType.action" /></td>
								<td class="greybox"></td>
								<td class="greybox"></td>
							</tr>
							<tr>
								<td width="10%" class="bluebox"></td>
								<td class="bluebox"><s:text name="arf.bank.accountnumber" />&nbsp;<span
									class="mandatory">*</span></td>
								<td class="bluebox"><s:select
										name="commonBean.accountNumberId" id="accountNumber"
										list="dropdownData.accountNumberList" listKey="id"
										listValue="accountnumber+'-'+accounttype" headerKey="-1"
										headerValue="----Choose----" /></td>
							</tr>

							<tr>
								<td width="10%" class="greybox"></td>
								<td class="greybox"><s:text name="modeofpayment" />&nbsp;<span
									class="mandatory">*</span></td>
								<td class="greybox"><s:radio
										name="commonBean.modeOfPayment" id="modeOfPayment"
										list="%{modeOfPaymentMap}" /></td>
								<td class="greybox"></td>
								<td class="greybox"></td>
							</tr>
							<tr>
								<td width="10%" class="bluebox"></td>
								<td class="bluebox"><s:text name="voucher.narration" /></td>
								<td class="bluebox" colspan="3"><s:textarea
										name="description" id="description" style="width:580px"
										onblur="checkVoucherNarrationLen(this)" /></td>
							</tr>
						</table>
					</td>
				</tr>
				<tr>
					<td colspan="5">&nbsp;</td>
				</tr>
				<tr>
					<td><s:if test="%{!wfitemstate.equalsIgnoreCase('END')}">
							<%@include file="../voucher/workflowApproval.jsp"%>
						</s:if></td>
				</tr>
				<tr>
					<td class="bluebox" style="text-align: center"><strong><s:text
								name="arf.wf.label.comments" /></strong> <s:textarea name="comments"
							id="comments" cols="100" rows="3" onblur="checkLength(this)"
							value="%{getComments()}" /></td>
				</tr>
				<tr>
					<td colspan="5"><div align="right" class="mandatory"
							style="font-size: 11px; padding-right: 20px;">
							*
							<s:text name="default.message.mandatory" />
						</div></td>
				</tr>
			</table>
			<table align="center">
				<tr class="buttonbottom" id="buttondiv" style="align: middle">
					<s:hidden name="actionname" id="actionName" value="%{action}" />
					<s:iterator value="%{getValidActions()}" var="p" status="s">
						<td><s:submit type="submit" cssClass="buttonsubmit"
								value="%{description}" id="wfBtn%{#s.index}" name="%{name}"
								method="save"
								onclick="return validateForm('%{name}','%{description}')" /></td>
					</s:iterator>
					<td><input type="button" value="Close"
						onclick="javascript:window.close()" class="button" /></td>
				</tr>
			</table>
			</div>
			<!-- end of formmainbox -->
		</s:push>
	</s:form>
</body>
</html>
