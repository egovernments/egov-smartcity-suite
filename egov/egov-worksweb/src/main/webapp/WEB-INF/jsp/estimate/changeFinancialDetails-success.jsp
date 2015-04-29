<%@ taglib prefix="egov" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld" %>  
<html>

<title><s:text name="change.gd.page.title" /></title>
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
	<s:if test="%{model.egwStatus!=null && (model.egwStatus.code!='APPROVED' && model.egwStatus.code!='CANCELLED')}">
		&nbsp; <s:text name="%{getText(messageKey)}" /> - <s:text name="%{model.currentState.nextAction}"/>
		<br>
		<s:text name="common.forwardmessage" />  <s:property value="%{employeeName}" />(<s:property value="%{designation}" />)
	</s:if>
	<s:elseif test="%{model.egwStatus!=null && (model.egwStatus.code=='APPROVED' || model.egwStatus.code=='CANCELLED')}">
			<s:text name="%{getText(messageKey)}" />
	</s:elseif>
</body>
</html>