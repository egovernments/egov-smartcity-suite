<!-- eGov suite of products aim to improve the internal efficiency,transparency, 
    accountability and the service delivery of the government  organizations.
 
     Copyright (C) <2015>  eGovernments Foundation
 
     The updated version of eGov suite of products as by eGovernments Foundation 
     is available at http://www.egovernments.org
 
     This program is free software: you can redistribute it and/or modify
     it under the terms of the GNU General Public License as published by
     the Free Software Foundation, either version 3 of the License, or
     any later version.
 
     This program is distributed in the hope that it will be useful,
     but WITHOUT ANY WARRANTY; without even the implied warranty of
     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
     GNU General Public License for more details.
 
     You should have received a copy of the GNU General Public License
     along with this program. If not, see http://www.gnu.org/licenses/ or 
     http://www.gnu.org/licenses/gpl.html .
 
     In addition to the terms of the GPL license to be adhered to in using this
     program, the following additional terms are to be complied with:
 
 	1) All versions of this program, verbatim or modified must carry this 
 	   Legal Notice.
 
 	2) Any misrepresentation of the origin of the material is prohibited. It 
 	   is required that all modified versions of this material be marked in 
 	   reasonable ways as different from the original version.
 
 	3) This license does not grant any rights to any user of the program 
 	   with regards to rights under trademark law for use of the trade names 
 	   or trademarks of eGovernments Foundation.
 
   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
-->
<%@ include file="/includes/taglibs.jsp"%>
<html>
<head>
<title><s:text name="service.master.search.header" /></title>

<script type="text/javascript">
	function validate() {
		var valid = true;
		document.getElementById('error_area').innerHTML = '';
		document.getElementById("error_area").style.display = "none";
		if (document.getElementById('serviceCategory').value == "-1") {
			document.getElementById("error_area").innerHTML = '<s:text name="error.select.service.category" />';
			valid = false;
		}
		else if (document.getElementById('serviceDetailsId').value == "-1") {
			document.getElementById("error_area").innerHTML = '<s:text name="error.select.service.type" />';
			valid = false;
		}
		else if (document.getElementById('bankName').value == "-1") {
			document.getElementById("error_area").innerHTML = '<s:text name="error.select.bank" />';
			valid = false;
		}
		else if (document.getElementById('branchName').value == "-1") {
			document.getElementById("error_area").innerHTML = '<s:text name="error.select.bankbranch" />';
			valid = false;
		}
		else if (document.getElementById('bankAccountId').value == "-1") {
			document.getElementById("error_area").innerHTML = '<s:text name="error.select.bankaccount" />';
			valid = false;
		}
		if (valid == false) {
			document.getElementById("error_area").style.display = "block";
			window.scroll(0, 0);
		}
		return valid;
	}

	function onChangeBankBranch(bankId) {
		dom.get("bankAccountId").value = "-1";
		populatebranchName({
			bankId : bankId,
		});
	}

	function onChangeBankAccount(branchId) {
		populatebankAccountId({
			branchId : branchId,
		});
	}
	function populateService(serviceId) {
		document.getElementById('serviceDetailsId').value = "-1"
		populateserviceDetailsId({
			serviceId : serviceId,
		});
	}
</script>
</head>

<body>
	<s:form name="serviceBankMappingForm" method="post" theme="simple">
		<s:push value="model">
			<div class="errorstyle" id="error_area" style="display: none;"></div>

			<div class="formmainbox">
				<div class="subheadnew">
					<s:text name="service.master.bankmappping.header" />
				</div>

				<table width="100%" border="0" cellspacing="0" cellpadding="0"
					style="max-width: 960px; margin: 0 auto;">
					<tr>
						<td class="bluebox">&nbsp;</td>
						<td class="bluebox"><s:text
								name="service.master.search.category" /> <span
							class="mandatory" /></td>
						<td class="bluebox"><s:select headerKey="-1"
								headerValue="----Choose----" name="serviceCategory"
								id="serviceCategory" cssClass="selectwk"
								list="dropdownData.serviceCategoryList" listKey="id"
								listValue="name" value="%{serviceCategory.id}"
								onChange="populateService(this.value);" /> <egov:ajaxdropdown
								id="service" fields="['Text','Value']"
								dropdownId="serviceDetailsId"
								url="receipts/ajaxBankRemittance-serviceListNotMappedToAccount.action" /></td>
						<td class="bluebox"><s:text name="service.master.servicetype" />
							<span class="mandatory" /></td>
						<td class="bluebox"><s:select headerKey="-1"
								headerValue="----Choose----" name="serviceDetails"
								id="serviceDetailsId" cssClass="selectwk"
								list="dropdownData.serviceTypeList" listKey="id"
								listValue="name" value="%{serviceDetails.id}" /></td>
					</tr>
					<tr>
						<td class="bluebox">&nbsp;</td>
						<td class="bluebox"><s:text name="service.master.bankname" />
							<span class="mandatory" /></td>
						<td class="bluebox"><s:select headerKey="-1"
								headerValue="----Choose----" name="bankName" id="bankName"
								cssClass="selectwk" list="dropdownData.bankNameList"
								listKey="id" listValue="name" value="%{bankName}"
								onchange="onChangeBankBranch(this.value)" /> <egov:ajaxdropdown
								id="accountNumberIdDropdown" fields="['Text','Value']"
								dropdownId='branchName'
								url='receipts/ajaxBankRemittance-bankBranchsByBankForReceiptPayments.action' /></td>
						<td class="bluebox"><s:text name="service.master.branchName" />
							<span class="mandatory" /></td>
						<td class="bluebox"><s:select headerKey="-1"
								headerValue="----Choose----" name="branchName" id="branchName"
								cssClass="selectwk" list="dropdownData.bankBranchList"
								listKey="id" listValue="branchname"
								onChange="onChangeBankAccount(this.value)" /> <egov:ajaxdropdown
								id="bankAccountIdDropDown" fields="['Text','Value']"
								dropdownId='bankAccountId'
								url='receipts/ajaxBankRemittance-bankAccountByBankBranch.action' /></td>
					</tr>
					<td class="bluebox">&nbsp;</td>
					<td class="bluebox"><s:text
							name="service.master.accountnumber" /> <span class="mandatory" /></td>
					<td class="bluebox"><s:select headerKey="-1"
							headerValue="----Choose----" name="bankAccountId"
							id="bankAccountId" cssClass="selectwk"
							list="dropdownData.accountNumberList" listKey="id"
							listValue="accountnumber" /></td>
					<tr>
					</tr>
				</table>
				<div align="left" class="mandatorycoll">
					&nbsp;&nbsp;&nbsp;
					<s:text name="common.mandatoryfields" />
				</div>
				<br />
			</div>

			<div class="buttonbottom">
				<s:submit name="sumbit" cssClass="buttonsubmit" id="button32"
					onclick="document.serviceBankMappingForm.action='serviceTypeToBankAccountMapping-create.action'; return validate();"
					value="Create Mapping" />
				<s:reset name="reset" cssClass="button" id="button" value="Reset" />
				<input name="close" type="button" class="button" id="button"
					onclick="window.close()" value="Close" />
			</div>
		</s:push>
	</s:form>
</body>
</html>
