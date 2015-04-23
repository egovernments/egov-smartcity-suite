function setWorkFlowInfo(obj)
{
    var objId=obj.id;
	document.getElementById("workflowBean.actionName").value=objId;
		return true;
}

function checkLength(obj){
	if(obj.value.length>1024)
	{
		alert('Max 1024 characters are allowed for comments. Remaining characters are truncated.')
		obj.value = obj.value.substring(1,1024);
	}
}

function refreshParentInbox(){
	opener.top.document.getElementById('inboxframe').contentWindow.egovInbox.refresh();
}
