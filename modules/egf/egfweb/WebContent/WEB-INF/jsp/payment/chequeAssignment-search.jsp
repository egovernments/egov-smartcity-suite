<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<html>  
<head>  
	<link rel="stylesheet" type="text/css" href="/EGF/cssnew/ccMenu.css"/>
    <title>Cheque Assignment Search</title>
</head>
	<body>  
		<s:form action="chequeAssignment" theme="simple" >
			<jsp:include page="../budget/budgetHeader.jsp">
				<jsp:param name="heading" value="Cheque Assignment Search" />
			</jsp:include>
 			<span class="mandatory" id="errorSpan">
				<s:actionerror/>  
				<s:fielderror />
				<s:actionmessage />
			</span>
			<div class="formmainbox"><div class="subheadnew"><s:text name="chq.assignment.heading.search"/></div>
			<table align="center" width="100%" cellpadding="0" cellspacing="0">
				<tr>
					<td class="greybox" width="30%"><s:text name="chq.assignment.paymentvoucherdatefrom"/> </td>
					<td class="greybox"><s:textfield name="fromDate" id="fromDate" maxlength="20" value="%{fromDate}" onkeyup="DateFormat(this,this.value,event,false,'3')"/><a href="javascript:show_calendar('forms[0].fromDate');" style="text-decoration:none">&nbsp;<img src="${pageContext.request.contextPath}/image/calendaricon.gif" border="0"/></a><br/>(dd/mm/yyyy)</td>
					<td class="greybox" width="30%"><s:text name="chq.assignment.paymentvoucherdateto"/> </td>
					<td class="greybox"><s:textfield name="toDate" id="toDate" maxlength="20" value="%{toDate}" onkeyup="DateFormat(this,this.value,event,false,'3')"/><a href="javascript:show_calendar('forms[0].toDate');" style="text-decoration:none">&nbsp;<img src="${pageContext.request.contextPath}/image/calendaricon.gif" border="0"/></a>(dd/mm/yyyy)</td>
				</tr>
				<tr>
					<td class="bluebox"><s:text name="payment.mode"/><span class="mandatory">*</span></td>
					<td class="bluebox"><s:radio id="paymentMode" name="paymentMode" list="#{'cheque':'Cheque','cash':'Cash','rtgs':'RTGS'}" value="%{paymentMode}"/></td>
					<td class="bluebox"><s:text name="chq.assignment.paymentvoucherno"/> </td>
					<td class="bluebox"><s:textfield name="voucherNumber" id="voucherNumber" value="%{voucherNumber}"/></td>
				</tr>
				<jsp:include page="../voucher/vouchertrans-filter.jsp"/>
				<tr>
					<egov:ajaxdropdown id="bank_branch" fields="['Text','Value']" dropdownId="bank_branch" url="voucher/common!ajaxLoadBanksWithApprovedPayments.action" />
					<td class="greybox"><s:text name="chq.assignment.bank"/><span class="mandatory">*</span></td>
					<td class="greybox"><s:select name="bank_branch" id="bank_branch" list="bankBranchMap"   headerKey="-1" headerValue="----Choose----" onchange="loadBankAccount(this)" value="%{bank_branch}"/></td>
					<egov:ajaxdropdown id="bankaccount" fields="['Text','Value']" dropdownId="bankaccount" url="voucher/common!ajaxLoadBankAccountsWithApprovedPayments.action"/>
					<td class="greybox"><s:text name="chq.assignment.bankaccount"/><span class="mandatory">*</span></td>
					<td class="greybox"  colspan="2"><s:select name="bankaccount" id="bankaccount" list="dropdownData.bankaccountList" listKey="id" listValue="chartofaccounts.glcode+'--'+accountnumber+'---'+accounttype"  headerKey="-1" headerValue="----Choose----" value="%{bankaccount}"/></td>
				</tr>
				<tr>
				
				<td class="bluebox">
				<s:text name="chq.assignment.re-assignsurrendercheque"/>
				</td class="bluebox">
				<td class="bluebox">
				<s:checkbox id="reassignSurrenderChq" name="reassignSurrenderChq" />
				</td class="bluebox">
				</tr>
			</table>
			<div  class="buttonbottom">
				<s:submit method="search" value="Search" id="searchBtn" cssClass="buttonsubmit" />
				<input type="button" value="Close" onclick="javascript:window.close()" class="button"/>
			</div>
		</div>
		<s:hidden name="bankbranch" id="bankbranch"/>
		</s:form>
			<script>
				var date='<s:date name="currentDate" format="dd/MM/yyyy"/>';
				function loadBank(obj)
				{
				var vTypeOfAccount = '<s:property value="%{typeOfAccount}"/>';
						if(obj.options[obj.selectedIndex].value!=-1)
						populatebank_branch({fundId:obj.options[obj.selectedIndex].value+'&asOnDate='+date});
				}
				function loadBankAccount(obj)
				{
					var vTypeOfAccount = '<s:property value="%{typeOfAccount}"/>';
					var fund = document.getElementById('fundId');
					if(obj.options[obj.selectedIndex].value!=-1)
					{
					var x=	obj.options[obj.selectedIndex].value.split("-");
					document.getElementById("bankbranch").value=x[1];
					populatebankaccount({branchId:x[1]+'&asOnDate='+date,fundId:fund.options[fund.selectedIndex].value});
					}
					
				}
			</script>
			<s:if test="%{!validateUser('chequeassignment')}"> 
				<script>
					document.getElementById('searchBtn').disabled=true;
					document.getElementById('errorSpan').innerHTML='<s:text name="chq.assignment.invalid.user"/>'
				</script>
			</s:if>
	</body>  
</html>
