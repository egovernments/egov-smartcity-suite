<%@ include file="/includes/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Collections Workflow - Success</title>
<script type="text/javascript">
function refreshInbox() {
        var x=opener.top.opener;
        if(x==null){
            x=opener.top;
        }
        x.document.getElementById('inboxframe').contentWindow.egovInbox.from = 'Inbox';
	    x.document.getElementById('inboxframe').contentWindow.egovInbox.refresh();
}
</script>
</head>
<body onLoad="refreshInbox();">

<s:form theme="simple" name="collectionsWorkflowForm">

	<div class="subheadnew"><s:if test="%{isSubmitAction == true}">
		<s:text name="collectionsWorkflow.submitSuccess" />
	</s:if> <s:elseif test="%{isApproveAction == true}">
		<s:text name="collectionsWorkflow.approveSuccess" />
	</s:elseif> <s:else>
		<s:text name="collectionsWorkflow.rejectSuccess" />
	</s:else></div>
	<br />

	<div class="buttonbottom">
	<input name="buttonClose" type="button" class="buttonsubmit"
		id="buttonClose" value="Close" onclick="window.close()" />

	<s:if test="%{isSubmitAction == true}">	&nbsp;
		
	<s:submit type="submit" cssClass="buttonsubmit" id="buttonCashReport"
			value="%{getText('collectionsWorkflow.submit.report.cash')}"
			disabled="false"
			onclick="document.collectionsWorkflowForm.action='collectionsWorkflow!submissionReportCash.action'" /> &nbsp;

	<s:submit type="submit" cssClass="buttonsubmit" id="buttonChequeReport"
			value="%{getText('collectionsWorkflow.submit.report.cheque')}"
			disabled="false"
			onclick="document.collectionsWorkflowForm.action='collectionsWorkflow!submissionReportCheque.action'" /> &nbsp;
	</s:if>
	</div>
</s:form>
</body>
</html>