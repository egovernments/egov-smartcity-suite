<%@ taglib prefix="egov" tagdir="/WEB-INF/tags" %>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator"%>
<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld" %>  
<html>

<title>Tender File</title>
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
			<s:property value="%{model.fileNumber}"/> &nbsp; <s:text name="%{getText(messageKey)}" /> - <s:text name="%{model.currentState.nextAction}"/>
			<br>
		</s:if>
		<s:else>
			<s:property value="%{model.fileNumber}"/> &nbsp; <s:text name="%{getText(messageKey)}" /> <br>
		</s:else>
		<s:if test="%{model.currentState.value=='END'}">
		</s:if>
		<s:else>	
		The File has been forwarded to <s:property value="%{nextEmployeeName}" />(<s:property value="%{nextDesignation}" />) <br />
		</s:else>	
	
	<!-- <s:if test="%{model.egwStatus != null && model.egwStatus.code == 'APPROVED'}">
		<s:property value="%{model.fileNumber}"/>&nbsp; <s:text name="%{getText(messageKey)}" />		
	</s:if>	
	<s:elseif test="%{model.egwStatus != null && model.egwStatus.code == 'CHECKED'}">
		<s:property value="%{model.fileNumber}"/>&nbsp; <s:text name="tenderfile.Checked" />
		<br>
		<s:text name="common.forwardmessage" /> &nbsp;<s:property value="%{nextEmployeeName}" />(<s:property value="%{nextDesignation}" />)		
	</s:elseif>		  	 		
	<s:else>
		<s:property value="%{model.fileNumber}"/>&nbsp; <s:text name="%{getText(messageKey)}" />
		<br>
		<s:if test="%{model.egwStatus != null && model.egwStatus.code != 'CANCELLED'}">
		<s:text name="common.forwardmessage" /> &nbsp;<s:property value="%{nextEmployeeName}" />(<s:property value="%{nextDesignation}" />)
		</s:if>
	</s:else> -->
</body>
</html>
