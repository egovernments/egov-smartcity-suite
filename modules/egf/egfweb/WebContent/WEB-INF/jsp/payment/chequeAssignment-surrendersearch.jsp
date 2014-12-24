<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<html>  
<head>  
	<link rel="stylesheet" type="text/css" href="/EGF/cssnew/ccMenu.css"/>
    <title><s:text name="surrender.cheque.search"/></title>
</head>
	<body>  
		<s:form action="chequeAssignment" theme="simple" >
			<jsp:include page="../budget/budgetHeader.jsp">
				<jsp:param name="heading" value="Cheque Surrender Search" />
			</jsp:include>
 			<span class="mandatory" id="errorSpan">
				<s:actionerror/>  
				<s:fielderror />
				<s:actionmessage />
			</span>
			<div class="formmainbox"><div class="subheadnew"><s:text name="surrender.cheque.search"/></div>
			<table align="center" width="100%" cellpadding="0" cellspacing="0">
				<tr>
					<td class="greybox" width="30%"><s:text name="chq.assignment.paymentvoucherdatefrom"/> </td>
					<td class="greybox"><s:textfield name="fromDate" id="fromDate" maxlength="20" value="%{fromDate}" onkeyup="DateFormat(this,this.value,event,false,'3')"/><a href="javascript:show_calendar('forms[0].fromDate');" style="text-decoration:none">&nbsp;<img src="${pageContext.request.contextPath}/image/calendaricon.gif" border="0"/></a><br/>(dd/mm/yyyy)</td>
					<td class="greybox" width="30%"><s:text name="chq.assignment.paymentvoucherdateto"/> </td>
					<td class="greybox"><s:textfield name="toDate" id="toDate" maxlength="20" value="%{toDate}" onkeyup="DateFormat(this,this.value,event,false,'3')"/><a href="javascript:show_calendar('forms[0].toDate');" style="text-decoration:none">&nbsp;<img src="${pageContext.request.contextPath}/image/calendaricon.gif" border="0"/></a>(dd/mm/yyyy)</td>
				</tr>
				<tr>
					<td class="bluebox"><s:text name="chq.assignment.paymentvoucherno"/> </td>
					<td class="bluebox"><s:textfield name="voucherNumber" id="voucherNumber" value="%{voucherNumber}"/></td>
					<td class="bluebox"><s:text name="chq.assignment.instrument.no"/> </td>
					<td class="bluebox"><s:textfield name="instrumentNumber" id="instrumentNumber" onkeyup="validateOnlyNumber()"/></td>
				</tr>
				<tr>
					<td class="greybox"><s:text name="bank"/><span class="mandatory">*</span></td>
					<td class="greybox"><s:select name="bank_branch" id="bank_branch" list="dropdownData.bankbranchList" listKey="bankBranchId" listValue="bankBranchName"  headerKey="-1" headerValue="----Choose----" onchange="loadBankAccount(this)" /></td>
					 <egov:ajaxdropdown id="bankaccount" fields="['Text','Value']" dropdownId="bankaccount" url="voucher/common!ajaxLoadBanksAccountsWithAssignedCheques.action" />
					<td class="greybox"><s:text name="bankaccount"/><span class="mandatory">*</span></td>
					<td class="greybox"  colspan="2"><s:select name="bankaccount" id="bankaccount" list="dropdownData.bankaccountList" listKey="id" listValue="chartofaccounts.glcode+'--'+accountnumber+'---'+accounttype"  headerKey="-1" headerValue="----Choose----" value="%{bankaccount}"/></td>
				</tr>
				<tr>
				<s:if test="%{shouldShowHeaderField('department')}">
	<td class="greybox"><s:text name="voucher.department"/>
	<s:if test="%{isFieldMandatory('department')}"><span class="bluebox"><span class="mandatory">*</span></span></s:if></td>
	<td class="greybox"><s:select name="department" id="department" list="dropdownData.departmentList" listKey="id" listValue="deptName" headerKey="" headerValue="----Choose----"  value="%{department}"  /><td>
	</s:if>
				<td></td>
				<td></td>
			</tr>
				
			</table>
			<div  class="buttonbottom">
				<s:submit method="searchChequesForSurrender" value="Search" id="searchBtn" cssClass="buttonsubmit" />
				<input type="button" value="Close" onclick="javascript:window.close()" class="button"/>
			</div>
		</div>
		<s:hidden name="bankbranch" id="bankbranch"/>
		</s:form>
			<s:if test="%{!validateUser('chequeassignment')}">
				<script>
					document.getElementById('searchBtn').disabled=true;
					document.getElementById('errorSpan').innerHTML='<s:text name="chq.assignment.invalid.user"/>'
				</script>
				</s:if>
			<script>
				var date='<s:date name="currentDate" format="dd/MM/yyyy"/>';			
				function loadBankAccount(obj)
				{
						var bankbranchId = obj.options[obj.selectedIndex].value;
						var index=bankbranchId.indexOf("-");
						var bankId = bankbranchId.substring(0,index);
						var brId=bankbranchId.substring(index+1,bankbranchId.length);
						document.getElementById("bankbranch").value=brId;
						populatebankaccount({bankId:bankId,branchId:brId+'&asOnDate='+date});
				}
			</script>
			
	</body>  
</html>