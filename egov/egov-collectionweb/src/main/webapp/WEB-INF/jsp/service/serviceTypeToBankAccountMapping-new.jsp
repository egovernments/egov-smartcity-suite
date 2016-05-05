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
<title><s:text name="service.master.search.header"></s:text></title>

<script>
	/* function validate(obj){
	 document.getElementById('error_area').innerHTML = '';
	 document.getElementById("error_area").style.display="none"
	 if(document.getElementById('serviceCategoryid').value == -1 || document.getElementById('serviceDetails').value == -1 ||
	 document.getElementById('branchname').value == -1 ||document.getElementById('accountNumber').value == -1 
	 document.getElementById('bankNameid').value == -1 ){
	 document.getElementById("error_area").innerHTML = '<s:text name="error.select.service.category" />';
	 document.getElementById("error_area").style.display="block";
	 return false;
	 }
	 document.forms[0].action=obj;
	 document.forms[0].submit;
	 return true;
	 }  */

	function onSubmit(obj) {
		document.forms[0].action = obj;
		document.forms[0].submit;
		return true;
	}
</script>
</head>

<body>
	<s:form action="serviceTypeToBankAccountMapping.action" theme="simple">

		<div class="errorstyle" id="error_area" style="display: none;"></div>

		<div class="formmainbox">
			<div class="subheadnew">
				<s:text name="service.master.bankmappping.header"></s:text>
			</div>

			<table width="100%" border="0" cellspacing="0" cellpadding="0"
				style="max-width: 960px; margin: 0 auto;">
				<tr>
					<td width="4%" class="bluebox">&nbsp;</td>
					<td width="24%" class="bluebox"><s:text
							name="service.master.search.category" /> <span
						class="mandatory1">*</td>
					<td width="24%" class="bluebox"><s:select headerKey="-1"
							headerValue="----Choose----" name="serviceCategory"
							id="serviceCategory" cssClass="selectwk"
							list="dropdownData.serviceCategoryList" listKey="id"
							listValue="name" value="%{serviceCategory.id}" /></td>
					<td width="21%" class="bluebox"><s:text
							name="service.master.servicetype" /> <span class="mandatory1">*</td>
					<td width="30%" class="bluebox"><s:select headerKey="-1"
							headerValue="----Choose----" name="serviceDetails"
							id="serviceDetails" cssClass="selectwk"
							list="dropdownData.serviceTypeList" listKey="id" listValue="name"
							value="%{serviceDetails}" /></td>
				</tr>
				<tr>
					<td width="4%" class="bluebox">&nbsp;</td>
					<td width="21%" class="bluebox"><s:text
							name="service.master.bankname" /> <span class="mandatory1">*</td>
					<td width="24%" class="bluebox"><s:select headerKey="-1"
							headerValue="----Choose----" name="bankName" id="bankNameid"
							cssClass="selectwk" list="dropdownData.bankNameList" listKey="id"
							listValue="name" value="%{bankName}"
							onchange="populateNumber(this)" /></td>
					<td width="21%" class="bluebox"><s:text
							name="service.master.branchName" /> <span class="mandatory1">*</td>
					<td width="30%" class="bluebox"><s:select headerKey="-1"
							headerValue="----Choose----" name="branchName" id="branchName"
							cssClass="selectwk" list="dropdownData.bankBranchList"
							listKey="id" listValue="branchname" value="%{branchName}" /></td>
				</tr>
				<td width="4%" class="bluebox">&nbsp;</td>
				<td width="21%" class="bluebox"><s:text
						name="service.master.accountnumber" /> <span class="mandatory1">*</td>
				<td width="24%" class="bluebox"><s:select headerKey="-1"
						headerValue="----Choose----" name="accountNumber"
						id="accountNumber" cssClass="selectwk"
						list="dropdownData.bankAcctNoList" listKey="id"
						listValue="accountnumber" value="%{accountNumber}" /></td>
				<tr>
			</table>
			<div align="left" class="mandatorycoll">
				&nbsp;&nbsp;&nbsp;
				<s:text name="common.mandatoryfields" />
			</div>
			<br />
		</div>

		<div class="buttonbottom">
			<s:submit name="button1" cssClass="buttonsubmit" id="button32"
				onclick="return onSubmit('serviceTypeToBankAccountMapping-save.action');"
				value="Create Mapping" />
			<s:reset name="button3" cssClass="button" id="button" value="Reset" />
			<input name="button4" type="button" class="button" id="button"
				onclick="window.close()" value="Close" />
		</div>

	</s:form>
</body>
</html>
