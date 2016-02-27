<!--  #-------------------------------------------------------------------------------
# eGov suite of products aim to improve the internal efficiency,transparency, 
#      accountability and the service delivery of the government  organizations.
#   
#       Copyright (C) <2015>  eGovernments Foundation
#   
#       The updated version of eGov suite of products as by eGovernments Foundation 
#       is available at http://www.egovernments.org
#   
#       This program is free software: you can redistribute it and/or modify
#       it under the terms of the GNU General Public License as published by
#       the Free Software Foundation, either version 3 of the License, or
#       any later version.
#   
#       This program is distributed in the hope that it will be useful,
#       but WITHOUT ANY WARRANTY; without even the implied warranty of
#       MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
#       GNU General Public License for more details.
#   
#       You should have received a copy of the GNU General Public License
#       along with this program. If not, see http://www.gnu.org/licenses/ or 
#       http://www.gnu.org/licenses/gpl.html .
#   
#       In addition to the terms of the GPL license to be adhered to in using this
#       program, the following additional terms are to be complied with:
#   
#   	1) All versions of this program, verbatim or modified must carry this 
#   	   Legal Notice.
#   
#   	2) Any misrepresentation of the origin of the material is prohibited. It 
#   	   is required that all modified versions of this material be marked in 
#   	   reasonable ways as different from the original version.
#   
#   	3) This license does not grant any rights to any user of the program 
#   	   with regards to rights under trademark law for use of the trade names 
#   	   or trademarks of eGovernments Foundation.
#   
#     In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
#-------------------------------------------------------------------------------  -->
<%@ taglib prefix="s" uri="/WEB-INF/tags/struts-tags.tld"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>

<%@ page language="java"%>
<html>

<head>
<script type="text/javascript"
	src="/EGF/resources/javascript/ajaxCommonFunctions.js"></script>
<script type="text/javascript"
	src="/EGF/resources/javascript/calender.js"></script>
<script type="text/javascript"
	src="/EGF/resources/javascript/calendar.js"></script>
<script type="text/javascript"
	src="/EGF/resources/javascript/dateValidation.js"></script>
<script type="text/javascript"
	src="/EGF/resources/javascript/jquery.min.js"></script>
<meta http-equiv="Content-Type"
	content="text/html; charset=windows-1252">

<title>Account Cheque Create</title>

</head>


<body>

	<jsp:include page="../budget/budgetHeader.jsp">
		<jsp:param name="heading" value="Account Cheque Create" />
	</jsp:include>
	<s:form theme="simple" name="chequeMaster">

		<div class="formmainbox" />
		<div class="subheadnew">Cheque Master - Search</div>
		<br>
		<font style='color: red; font-weight: bold'>
			<p class="error-block" id="lblError"></p>
		</font>
		<br>
		<table border="0" width="100%">
			<tr>
				<td class="greybox"></td>
				<td class="greybox">Fund</td>
				<td class="greybox"><s:select name="fundId" id="fundId"
						list="dropdownData.fundList" listKey="id" listValue="name"
						headerKey="-1" headerValue="----Choose----"
						onChange="loadBank(this);" /></td>
				<egov:ajaxdropdown id="bankbranchId" fields="['Text','Value']"
					dropdownId="bankbranchId" url="voucher/common-ajaxLoadBanks.action" />
				<td class="greybox">Bank</td>
				<td class="greybox"><s:select name="bankbranchId"
						id="bankbranchId" list="dropdownData.bankList" listKey="id"
						listValue="name" headerKey="-1" headerValue="----Choose----"
						onChange="loadBankAccount(this);" /></td>
			</tr>
			<tr>
				<td class="bluebox"></td>
				<egov:ajaxdropdown id="bankAccId" fields="['Text','Value']"
					dropdownId="bankAccId"
					url="voucher/common-ajaxLoadBankAccounts.action" />
				<td class="bluebox">Account Number</td>
				<td class="bluebox"><s:select name="bankAccId" id="bankAccId"
						list="dropdownData.accNumList" listKey="id" listValue="name"
						headerKey="-1" headerValue="----Choose----" /></td>

			</tr>
		</table>
		<div class="buttonbottom" style="padding-bottom: 10px;">
			<input type="button" id="Close" value="Add/Modify Cheque"
				onclick="addModifyChq();" class="buttonsubmit" />
			<s:reset id="Reset" value="Cancel" cssClass="buttonsubmit" />
			<input type="button" id="Close" value="Close"
				onclick="javascript:window.close()" class="button" />
		</div>
		</div>
	</s:form>

	<script>
	function loadBank(fund){
		populatebankbranchId({fundId:fund.options[fund.selectedIndex].value})	
		
	}
	function loadBankAccount(branch){
		var fundObj = document.getElementById('fundId');
		var bankbranchId = branch.options[branch.selectedIndex].value;
		var index=bankbranchId.indexOf("-");
		var brId=bankbranchId.substring(index+1,bankbranchId.length);
		populatebankAccId({fundId: fundObj.options[fundObj.selectedIndex].value,branchId:brId})
		
	}
	function addModifyChq(){
		if( document.getElementById("bankAccId").value == -1){
			
			document.getElementById("lblError").innerHTML = "Please select bank account number";
			return false;
		}
		var bankAccId = document.getElementById('bankAccId').value; 
		window.location = "../masters/accountCheque-manipulateCheques.action?bankAccId="+bankAccId;
		
	}

</script>
</body>
</html>
