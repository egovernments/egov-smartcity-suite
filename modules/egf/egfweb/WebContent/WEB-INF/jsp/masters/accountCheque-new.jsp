<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>

<%@ page language="java"%>
<html>

<head>
<script type="text/javascript" src="/EGF/commonjs/ajaxCommonFunctions.js"></script>
<script type="text/javascript" src="/EGF/javascript/calender.js"></script>
<script type="text/javascript" src="/EGF/script/calendar.js" ></script>
<script type="text/javascript" src="/EGF/javascript/dateValidation.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/jquery.min.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=windows-1252">

<title>Account Cheque Create</title>

</head>

	
<body >

	<jsp:include page="../budget/budgetHeader.jsp">
        		<jsp:param name="heading" value="Account Cheque Create" />
	</jsp:include>
	<s:form theme="simple" name="chequeMaster" >	
	
		<div class="formheading"/><div class="subheadnew">Cheque Master - Search</div>
		<br>
		<font  style='color: red ; font-weight:bold '> 
			<p class="error-block" id="lblError" ></p>
		</font>
		<br>
			<table border="0" width="100%">
				<tr>
					<td class="greybox">Fund </td>
					<td class="greybox"><s:select name="fundId" id="fundId" list="dropdownData.fundList" listKey="id" listValue="name" headerKey="-1" headerValue="----Choose----" onChange="loadBank(this);"/></td>
					<egov:ajaxdropdown id="bankbranchId"fields="['Text','Value']" dropdownId="bankbranchId" url="voucher/common!ajaxLoadBanks.action" />
					<td class="greybox">Bank </td>
					<td class="greybox"><s:select name="bankbranchId" id="bankbranchId" list="dropdownData.bankList" listKey="id" listValue="name" headerKey="-1" headerValue="----Choose----" onChange="loadBankAccount(this);"/></td>
				</tr>
			<tr>
			<egov:ajaxdropdown id="bankAccId"fields="['Text','Value']" dropdownId="bankAccId" url="voucher/common!ajaxLoadBankAccounts.action" />
					<td class="bluebox">Account Number </td>
					<td class="bluebox"><s:select name="bankAccId" id="bankAccId" list="dropdownData.accNumList" listKey="id" listValue="name" headerKey="-1" headerValue="----Choose----" /></td>
					
				</tr>
			</table>
		<div class="buttonbottom" style="padding-bottom:10px;">
		<input type="button" id="Close" value="Add/Modify Cheque" onclick="addModifyChq();" class="buttonsubmit"/>
		<s:reset id="Reset" value="Cancel" cssClass="buttonsubmit"/>
		<input type="button" id="Close" value="Close" onclick="javascript:window.close()" class="button"/>
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
		window.location = "../masters/accountCheque!manipulateCheques.action?bankAccId="+bankAccId;
		
	}

</script>		
</body>
</html>