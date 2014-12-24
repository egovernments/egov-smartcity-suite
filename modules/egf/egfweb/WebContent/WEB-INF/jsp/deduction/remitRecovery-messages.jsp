<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-nested.tld" prefix="nested"%>
<html>  
<head>  
    <title>Remittance Recovery  Approval</title>
   
</head>
	<body onload="refreshInbox()">  
		<s:form action="preApprovedVoucher" theme="simple" >
			<jsp:include page="../budget/budgetHeader.jsp">
        		<jsp:param name="heading" value="Remittance Recovery -Approval" />
			</jsp:include>
			<span class="mandatory">
				<s:actionmessage /> 
			</span>
			<br/>
			<s:submit cssClass="button" id="printPreview" value="Print Preview"  onclick="printVoucher()"/>
			<input type="submit" value="Close" onclick="javascript:window.close()" class="button"/>
			</s:form>
<script>
	function refreshInbox()
	{
		
		if(opener && opener.top.document.getElementById('inboxframe'))
		opener.top.document.getElementById('inboxframe').contentWindow.egovInbox.refresh();
	}
function printVoucher(){
	document.forms[0].action='../report/billPaymentVoucherPrint!print.action?id=<s:property value="paymentheader.id"/>';
	document.forms[0].submit();
}
</script>

	</body>  

</html>