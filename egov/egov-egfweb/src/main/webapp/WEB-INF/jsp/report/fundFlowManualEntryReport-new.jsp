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
	src="${pageContext.request.contextPath}/resources/javascript/contra.js?rnd=${app_release_no}"></script>
<script type="text/javascript"
	src="/EGF/resources/javascript/ajaxCommonFunctions.js?rnd=${app_release_no}"></script>
</head>
<script>
function loadBank(fund){
	//bootbox.alert(fund.value);
	if(fund.value!=-1){
		populatebank({fundId:fund.options[fund.selectedIndex].value})   
	}
}
function populateAccNumbers(bankBranch){
	var fund = document.getElementById('fund');
	id = bankBranch.options[bankBranch.selectedIndex].value.split("-")[1]
	populateaccountNumber({branchId:id,fundId:fund.options[fund.selectedIndex].value})	
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

function validateBank(){
	var bank = document.getElementById('bank').value;
	if(bank == -1){
		bootbox.alert("Please select a Bank")
		return false;
	}
	return true;
}
function validateMandatoryFields(){
		var fund = document.getElementById('fund').value;
		var bankAccount = document.getElementById('accountNumber').value;
		var bank = document.getElementById('bank').value;
		var accountNo="";
		
		if(fund == -1){
			bootbox.alert("Please select a Fund")
			return false;
		}
		if(bank == -1){
			bootbox.alert("Please select a Bank")
			return false;
		}
		if(bankAccount == -1){
			bootbox.alert("Please select a Bank Account")
			return false;
		}else{
			accountNo=document.getElementById("accountNumber").options[document.getElementById("accountNumber").selectedIndex].text
			document.getElementById('selectedAccountNumber').value=accountNo;
		}
		var stDate =  Date.parse(document.getElementById('startDate').value);
		if(isNaN(stDate)){
			bootbox.alert("Please enter a valid start date")
			return false;
		}
		var endDate =  Date.parse(document.getElementById('endDate').value);
		if(isNaN(endDate)){
			bootbox.alert("Please enter a valid end date")
			return false;
		}
		return true;	
}
function doAfterSubmit(){
	document.getElementById('loading').style.display ='block';
}
function loadSearch(){
	document.getElementById('loading').style.display ='block';
	return true;
}


</script>
<body>
	<div class="formmainbox">
		<div class="formheading"></div>
		<div class="subheadnew">Manual Entry Report</div>
	</div>

	<s:form action="fundFlowManualEntryReport" theme="simple"
		name="fundFlowManualEntryReport">
		<span class="mandatory"> <s:actionerror /> <s:fielderror /> <s:actionmessage />
		</span>
		<table width="100%" cellpadding="0" cellspacing="0" border="0">
			<tr>
				<td class="greybox"></td>
				<td class="greybox" width="10%"><s:text name="voucher.fund" /><span
					class="greybox"><span class="mandatory">*</span></span></td>
				<td class="greybox"><s:select name="fund.id" id="fund"
						list="dropdownData.fundList" listKey="id" listValue="name"
						headerKey="0" headerValue="----Choose----"
						onChange="loadBank(this);" /></td>
				<egov:ajaxdropdown id="bank" fields="['Text','Value']"
					dropdownId="bank" url="voucher/common!ajaxLoadAllBanks.action" />

				<td class="greybox" width="10%">Bank Name:<span class="bluebox"><span
						class="mandatory">*</span></span></td>
				<td class="greybox"><s:select name="bank.bankBranchId"
						id="bank" list="dropdownData.bankList" listKey="bankBranchId"
						listValue="bankBranchName" headerKey="-1"
						headerValue="----Choose----" onclick="validateFund()"
						onChange="populateAccNumbers(this);"
						value="%{bank.bankBranchName}" /></td>
			</tr>
			<tr>
				<td class="bluebox"></td>
				<egov:ajaxdropdown id="accountNumber" fields="['Text','Value']"
					dropdownId="accountNumber"
					url="voucher/common!ajaxLoadAccountNumbers.action" />
				<td class="bluebox" width="10%"><s:text name="bankaccount" />:<span
					class="bluebox"><span class="mandatory">*</span></span></td>
				<td class="bluebox"><s:select name="bankAccount.id"
						id="accountNumber" list="dropdownData.accNumList" listKey="id"
						listValue="accountnumber" headerKey="-1"
						headerValue="----Choose----" onclick="validateBank()"
						value="%{bankAccount.id}" /></td>
			</tr>

			<tr>
				<td class="greybox"></td>
				<td class="greybox"><s:text name="voucher.fromdate" /><span
					class="mandatory">*</span></td>
				<s:date name="startDate" format="dd/MM/yyyy" var="tempFromDate" />
				<td class="greybox"><s:textfield name="startDate"
						id="startDate" maxlength="20"
						onkeyup="DateFormat(this,this.value,event,false,'3')"
						value="%{tempFromDate}" /><a
					href="javascript:show_calendar('fundFlowManualEntryReport.startDate');"
					style="text-decoration: none">&nbsp;<img
						src="/egi/resources/erp2/images/calendaricon.gif" border="0" /></a></td>
				<td class="greybox"><s:text name="voucher.todate" /><span
					class="mandatory">*</span></td>
				<s:date name="endDate" format="dd/MM/yyyy" var="tempToDate" />
				<td class="greybox"><s:textfield name="endDate" id="endDate"
						maxlength="20"
						onkeyup="DateFormat(this,this.value,event,false,'3')"
						value="%{tempToDate}" /><a
					href="javascript:show_calendar('fundFlowManualEntryReport.endDate');"
					style="text-decoration: none">&nbsp;<img
						src="/egi/resources/erp2/images/calendaricon.gif" border="0" /></a>(dd/mm/yyyy)</td>
			</tr>
			<s:hidden name="selectedAccountNumber" id="selectedAccountNumber" />
		</table>
		<br />
		<br />
		<div class="buttonbottom">
			<s:submit name="button" type="submit" value="Search"
				cssClass="button" method="search"
				onClick="validateMandatoryFields();loadSearch();" />
			<s:reset name="button" type="submit" cssClass="button" id="button"
				value="Cancel" />
			<s:submit value="Close" onclick="javascript: self.close()"
				cssClass="button" />
		</div>


		<!--	<div id="listid" style="display:none"	> -->
		<table width="100%" border="0" align="center" cellpadding="0"
			cellspacing="0" class="tablebottom">
			<tr>
				<div id="loading" class="loading"
					style="width: 700; height: 700; display: none" align="center">
					<blink style="color: red">Searching processing, Please
						wait...</blink>
				</div>
				<div class="subheadsmallnew">
					<strong><s:property value="heading" /></strong>
				</div>
				</td>

				<s:if test="%{manualEntryReportList.size!=0}">
					<script>             
				document.getElementById('loading').style.display ='none';	
				</script>
			</tr>
			<td style="text-align: right" class="bluebox" colspan="3"><strong>Amounts
					in lakhs</strong></td>
			<tr>
				<th class="bluebgheadtd"><s:text name="voucher.serialno" /></th>
				<th class="bluebgheadtd">Report</th>
				<th class="bluebgheadtd"><s:text name="voucher.amount" /></th>
			</tr>
			<c:set var="trclass" value="greybox" />
			<s:iterator var="p" value="manualEntryReportList" status="s">
				<tr>
					<td style="text-align: center" class="<c:out value="${trclass}"/>">
						<s:property value="#s.index+1" />
					</td>
					<td style="text-align: center" class="<c:out value="${trclass}"/>">
						<s:date name="%{reportDate}" format="dd/MM/yyyy" />
					</td>
					<td style="text-align: right" class="<c:out value="${trclass}"/>">
						<s:text name="format.number">
							<s:param value="%{currentReceipt}" />
						</s:text>
					</td>

					<c:choose>
						<c:when test="${trclass=='greybox'}">
							<c:set var="trclass" value="bluebox" />
						</c:when>
						<c:when test="${trclass=='bluebox'}">
							<c:set var="trclass" value="greybox" />
						</c:when>
					</c:choose>
				</tr>
			</s:iterator>
			<tr>
				<td style="text-align: right" colspan="2" class="blueborderfortdnew"><strong>Grand
						Total</strong></td>
				<td style="text-align: right" colspan="3" class="blueborderfortd"><strong><s:text
							name="format.number">
							<s:param value="%{grandTotal}" /></strong>
				</s:text></td>
			</tr>
		</table>
		<div class="buttonbottom">
			<s:submit method="generatePdf" value="Save As Pdf"
				cssClass="buttonsubmit" id="generatePdf" />
			<s:submit method="generateXls" value="Save As Xls"
				cssClass="buttonsubmit" id="generateXls" />
		</div>
		</s:if>
		<s:elseif test="%{manualEntryReportList.size == 0}">
			<tr>
				<td colspan="5" align="center"><font color="red">No
						record Found</font></td>

			</tr>
		</s:elseif>

	</s:form>


</body>
</html>
