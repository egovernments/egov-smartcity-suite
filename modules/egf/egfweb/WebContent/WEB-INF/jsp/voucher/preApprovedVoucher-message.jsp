<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-nested.tld" prefix="nested"%>
<html>  
<head>  
<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/voucherHelper.js"></script>
    <title>PJV Approval</title>
</head>
	<body onload="refreshInbox()">  
		<s:form action="preApprovedVoucher" theme="simple" >
			<jsp:include page="../budget/budgetHeader.jsp">
        		<jsp:param name="heading" value="PJV-Approval" />
			</jsp:include>
			<span class="mandatory">
				<s:actionmessage /> 
			</span>
			<br/>
			<s:hidden id="id" name="id" value="%{voucherHeader.id}"/>
			<s:submit cssClass="button" id="print" value="Print Preview" onclick="printVoucher()"/>
			<input type="submit" value="Close" onclick="javascript:window.close()" class="button"/>
		</s:form> 
<script>
function printVoucher()
{
	<s:if test='%{type ==finConstExpendTypeContingency}'>
		document.forms[0].action="../report/expenseJournalVoucherPrint!print.action";
	</s:if>
	<s:else>
		document.forms[0].action="journalVoucherPrint!print.action";		
	</s:else>
	document.forms[0].submit();
}
</script>
	</body>  
</html>
