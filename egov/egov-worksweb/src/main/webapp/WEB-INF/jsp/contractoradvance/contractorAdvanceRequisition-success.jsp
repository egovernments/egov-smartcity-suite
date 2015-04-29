<%@ taglib prefix="egov" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld" %>  
<html>

<title><s:text name="contractoradvance.advancerequisition.title" /></title>
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

<s:if test="%{model.status != null && model.status.code == 'APPROVED'}">
		<s:property value="%{model.advanceRequisitionNumber}"/>	: &nbsp; <s:text name="advancerequisition.approved" />
	</s:if>	
	<s:elseif test="%{model.status != null && model.status.code == 'CHECKED'}">
		<s:if test="%{model.currentState != null && model.currentState.nextAction!=''}">
			<s:property value="%{model.advanceRequisitionNumber}"/> : <s:text name="%{model.status.code}"/> - <s:text name="%{model.currentState.nextAction}"/>
		</s:if>
		<s:else>
			<s:property value="%{model.advanceRequisitionNumber}"/> : &nbsp; <s:text name="advancerequisition.checked" />
		</s:else>               
        <br>
        <s:text name="common.forwardmessage" />  <s:property value="%{nextEmployeeName}" />(<s:property value="%{nextDesignation}" />)
	</s:elseif>	
	<s:elseif test="%{model.status != null && model.status.code == 'REJECTED'}">
		<s:property value="%{model.advanceRequisitionNumber}"/> : &nbsp; <s:text name="advancerequisition.reject" />
		<br>
		<s:text name="common.forwardmessage" /> &nbsp;<s:property value="%{nextEmployeeName}" />(<s:property value="%{nextDesignation}" />)		
	</s:elseif>	
	<s:elseif test="%{model.status != null && model.status.code == 'CANCELLED'}">
		<s:property value="%{model.advanceRequisitionNumber}"/> : &nbsp; <s:text name="advancerequisition.cancel"/>
	</s:elseif>	
	 <s:else>
     <s:if test="%{model.currentState.nextAction!=''}">
		<s:property value="%{model.advanceRequisitionNumber}"/> : <s:text name="%{model.currentState.value}"/> - <s:text name="%{model.currentState.nextAction}"/>
	</s:if>
	<s:else>
		<s:property value="%{model.advanceRequisitionNumber}"/> : <s:text name="advancerequisition.created" />
	</s:else>   
       <br>
       <s:if test="%{model.status != null && model.status.code != 'CANCELLED'}">
       	<s:text name="common.forwardmessage" />  <s:property value="%{nextEmployeeName}" />(<s:property value="%{nextDesignation}" />)
       </s:if>
      </s:else>     
</body>
</html>