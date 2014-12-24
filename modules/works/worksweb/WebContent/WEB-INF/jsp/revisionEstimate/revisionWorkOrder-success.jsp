<%@ taglib prefix="egov" tagdir="/WEB-INF/tags" %>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator"%>
<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld" %>  
<html>

<title>Revision Work Order</title>
<body onload="refreshInbox();">
<script>
function refreshInbox(){
        var x=opener.top.opener;
        if(x==null){
            x=opener.top;
        }
	    x.document.getElementById('inboxframe').contentWindow.egovInbox.refresh();
}
</script>
	<s:if test="%{model.currentState.nextAction!='' && model.currentState.nextAction!=null}">
		<s:property value="%{model.workOrderNumber}"/> &nbsp; <s:text name="%{getText(messageKey)}" /> - <s:text name="%{model.currentState.nextAction}"/>
		<br>
	</s:if>
	<s:else>
		<s:property value="%{model.workOrderNumber}"/> &nbsp; <s:text name="%{getText(messageKey)}" /> <br>
	</s:else>
	<s:if test="%{model.currentState.value=='APPROVED' || model.currentState.value=='END'}">	</s:if>
	<s:else>	
		The File has been forwarded to <s:property value="%{employeeName}" />(<s:property value="%{designation}" />) <br />
	</s:else>
	
</body>
</html>