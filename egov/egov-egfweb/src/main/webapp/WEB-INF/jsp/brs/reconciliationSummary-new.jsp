<!--
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
  -->
<%@ include file="/includes/taglibs.jsp" %>
<%@ page language="java"%>
<%@ taglib prefix="s" uri="/WEB-INF/tags/struts-tags.tld"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<html>
<head>
<title><s:text name="Reconciliation Summary" /></title>
<link rel="stylesheet" type="text/css"
	href="/EGF/resources/css/jquery-ui/css/smoothness/jquery-ui-1.8.4.custom.css" />

<SCRIPT type="text/javascript" src="../resources/javascript/calendar.js?rnd=${app_release_no}" type="text/javascript" ></SCRIPT>
</head>

<body>
	<div class="formmainbox">
		<div class="subheadnew">
			<s:text name="Reconciliation Summary" />
		</div>
		<div style="color: red">
			<s:actionerror />
			<s:fielderror />
		</div>
		<s:form name="bankReconciliation" action="bankReconciliation" theme="simple">
		 
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr height="25px">
					<td class="bluebox"></td>
				</tr>
				<tr>
					<egov:ajaxdropdown id="bank" fields="['Text','Value']"
						dropdownId="bank" url="voucher/common-ajaxLoadAllBanks.action" />

                    <td style="width: 5%"></td>
					<td class="bluebox" >Bank And Branch:<span
						class="bluebox"><span class="mandatory1">*</span></span></td>
					<td class="bluebox"><s:select name="bank" id="bank"
							list="dropdownData.bankList" listKey="bankBranchId"
							listValue="bankBranchName" headerKey="-1"
							headerValue="----Choose----" 
							onChange="populateAccNumbers(this);" /></td>
					<egov:ajaxdropdown id="accountNumber" fields="['Text','Value']"
						dropdownId="accountNumber"
						url="voucher/common-ajaxLoadAccountNumbers.action" />
					<td class="bluebox" >Account Number:<span
						class="bluebox"><span class="mandatory1">*</span></span></td>
					<td class="bluebox"><s:select name="bankAccount" 
							id="accountNumber" list="dropdownData.accNumList" listKey="id"
							listValue="accountnumber" headerKey="-1"
							headerValue="----Choose----" onclick="validateBank()" /></td>
				</tr>
				<tr>
				 <td style="width: 5%"></td>
				 <td class="bluebox" >Bank Statement Balance:<span
						class="bluebox"><span class="mandatory1">*</span></span></td>
				<td class="bluebox"><s:textfield  name="bankStBalance"id="bankStBalance" /></td>
				<td class="bluebox">Bank Statement Date:<span
						class="bluebox"><span class="mandatory1">*</span></span></td>
					<s:date name="bankStmtDate" format="dd/MM/yyyy" var="BSdate"/>
				<td class="greybox"><s:textfield name="bankStmtDate" id="bankStmtDate"
							cssStyle="width:100px" value='%{BSdate}'
							onkeyup="DateFormat(this,this.value,event,false,'3')" /><a
						href="javascript:show_calendar('bankReconciliation.bankStmtDate');"
						style="text-decoration: none">&nbsp;<img
							src="/egi/resources/erp2/images/calendaricon.gif" border="0" /></a>(dd/mm/yyyy)<br />
					</td>	
				</tr>
			</table>
			<br />
	</div>
	<div class="buttonbottom" style="padding-bottom: 10px;">
		<s:hidden name="mode"></s:hidden>
		<input type="submit" class="buttonsubmit" value="View Summary"
			id="search" name="Search" onclick=" return validateAndSubmit();" /> <input
			type="button" id="Close" value="Close"
			onclick="javascript:window.close()" class="button" />
	</div>
	 
	
	</s:form>
	<script type="text/javascript">
	function populateAccNumbers(bankBranch){
		var fund = document.getElementById('fundId');
		id = bankBranch.options[bankBranch.selectedIndex].value.split("-")[1]
		populateaccountNumber({branchId:id})	
	}

		function validateBank(){
		var bank = document.getElementById('bank').value;
		if(bank == -1){
			bootbox.alert("Please select a Bank");
			return false;
		}
		return true;
	}

  function validateAndSubmit(){
	  
	  var bank = document.getElementById('bank').value;
	  var bankAccount = document.getElementById('accountNumber').value;
	  var amount = document.getElementById('bankStBalance').value;
	  var stmtDate = document.getElementById('bankStmtDate').value;
	  
	  if(bank== -1)
	  {
		bootbox.alert("Please select a Bank");
			return false;
		}
	  if(bankAccount== -1)
	  {
			bootbox.alert("Please select a bank account number");
			return false;
		}

	  if(amount=="")
	  {
		  bootbox.alert("Enter Bank Statement Balance");
			return false;
		}
		
		if(stmtDate == "")
		{
			bootbox.alert("Enter Bank Statement Date");
			return false;
		}
		
	document.forms[0].action='/EGF/brs/bankReconciliation-brsSummary.action?bankAccount.id='+bankAccount;
	document.forms[0].submit();
	return true;
	
}

    </script>
</body>

</html>

