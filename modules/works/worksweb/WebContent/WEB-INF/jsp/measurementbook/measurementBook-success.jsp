
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags" %>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator"%>
<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld" %>  
<html>

<title>Measurement Book</title>
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
				
				
				<s:if test="%{model.currentState != null && model.currentState.previous.value == 'APPROVED'}">
					<s:property value="%{model.mbRefNo}"/>&nbsp; <s:text name="%{getText(messageKey)}" />		
					<s:if test="%{model.revisionEstimate && model.revisionEstimate.egwStatus.code!='APPROVED'}">
						<br>
						<s:text name="revisionEstimate.success.message" />- #: <s:property value="%{model.revisionEstimate.estimateNumber}"/>	
					</s:if>
				</s:if>		  	 		
				<s:else>
				<s:if test="%{model.currentState.nextAction!='' && model.currentState.nextAction!=null}">
					<s:property value="%{model.mbRefNo}"/>&nbsp; <s:text name="%{getText(messageKey)}" />-  <s:text name="%{model.currentState.nextAction}"/>
				</s:if>
				<s:else>
					<s:property value="%{model.mbRefNo}"/>&nbsp; <s:text name="%{getText(messageKey)}" />
				</s:else>					
					<br>
					<s:if test="%{model.currentState.previous.value != 'CANCELLED'}">
					<s:text name="common.forwardmessage" /> &nbsp;<s:property value="%{dispEmployeeName}" />(<s:property value="%{dispDesignation}" />)
					</s:if>
				</s:else>
			
</body>
</html>