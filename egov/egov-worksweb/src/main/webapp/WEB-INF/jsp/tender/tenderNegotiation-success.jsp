
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags" %>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator"%>
<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld" %>  
<html>

<title>Tender Negotiation</title>
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
		
		<s:if test="%{option != null && option == 'setStatus'}">
			<s:text name="%{getText(messageKey)}" /> <br />
			<s:if test="%{workOrder != null && workOrder.workOrderNumber!=null}">	
				Work order number is  &nbsp;&nbsp; <s:property value="%{workOrder.workOrderNumber}"/><br />					
			</s:if>
			
		</s:if>
		<s:else>
				<s:if test="%{model.egwStatus != null && model.egwStatus.code == 'APPROVED'}">
					<s:property value="%{negotiationNumber}"/>&nbsp; <s:text name='tenderResponse.approved' />		
				</s:if>
				<s:elseif test="%{model.egwStatus != null && model.egwStatus.code == 'CANCELLED'}">
					<s:property value="%{negotiationNumber}"/>&nbsp; <s:text name="%{getText(messageKey)}" />	
				</s:elseif>		  	 		
				<s:else>
					<s:property value="%{negotiationNumber}"/>&nbsp; <s:text name="%{getText(messageKey)}" />
					<br>
					<s:if test="%{employeeName != null}"> 
						<s:text name="common.forwardmessage" /> &nbsp;<s:property value="%{employeeName}" />(<s:property value="%{designation}" />)
					</s:if>
				</s:else>
		</s:else>	
</body>
</html>
