<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-nested.tld" prefix="nested"%>
<html>  
<head>  
    <title>Expense Bill  Approval</title>
    <script>
    function printPreview(){
	document.forms[0].action='../bill/expenseBillPrint!print.action?id=<s:property value="billRegisterId"/>';
	document.forms[0].submit();
}
    </script>
</head>
	<body onload="refreshInbox()">  
		<s:form action="preApprovedVoucher" theme="simple" >
			<jsp:include page="../budget/budgetHeader.jsp">
        		<jsp:param name="heading" value="Expense Bill -Approval" />
			</jsp:include>
			<span class="mandatory">
				<s:actionmessage /> 
			</span>
			<br/>
			<input type="submit" value="Close" onclick="javascript:window.close()" class="button"/>
			
			<input type="button" id="print" value="Print Preview" onclick="printPreview()" class="button"/>
		</s:form>      
<script>
	function refreshInbox()
	{
		
		if(opener && opener.top.document.getElementById('inboxframe'))
		opener.top.document.getElementById('inboxframe').contentWindow.egovInbox.refresh();
	}
</script>
	</body>  

</html>