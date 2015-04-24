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

	<div class="subheadnew">
		Data uploaded successfully.
	</div>
	<br />

</s:form>
</body>
</html>
