<%@ include file="/includes/taglibs.jsp"%>

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<script>
function refreshInbox() {
        var x=opener.top.opener;
        if(x==null){
            x=opener.top;
        }
        x.document.getElementById('inboxframe').contentWindow.egovInbox.from = 'Inbox';
	    x.document.getElementById('inboxframe').contentWindow.egovInbox.refresh();
}
</script>
<title><s:text name="challan.pagetitle" /></title>
</head>
<body onLoad="refreshInbox();">
<s:form theme="simple" name="displayReceiptForm">
	<s:if test="%{hasErrors()}">
		<div class="errorstyle"><s:actionerror /> <s:fielderror /></div>
	</s:if>
	<s:if test="%{hasActionMessages()}">
		<div class="messagestyle"><s:actionmessage theme="simple" /></div>
	</s:if>
	<iframe src="../reportViewer?reportId=<s:property value='reportId'/>" width="98%"
		height="70%">
	<p>Your browser does not support iframes.</p>
	</iframe>
	<br/>
	<div class="buttonbottom">
	<input name="buttonClose" type="button" class="button"	id="buttonClose" value="Close" onclick="window.close()" />&nbsp;
	<s:if test="%{sourcePage=='viewChallan'}">
		<input name="buttonBack" type="button" class="button"
		id="buttonBack" value="Back" onclick="history.back()" /> &nbsp;
	</s:if>
	</div>
</s:form>
</body>
</html>
