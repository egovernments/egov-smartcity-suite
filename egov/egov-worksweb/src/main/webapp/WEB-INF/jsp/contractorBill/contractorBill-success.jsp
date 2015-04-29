<%@ taglib prefix="egov" tagdir="/WEB-INF/tags" %>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator"%>
<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld" %>  
<html>
<title>Contractor Bill</title>
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
		<s:property value="%{model.billnumber}"/>	<br>	
		 <s:text name="bill.approved" />
	</s:if>	
	<s:elseif test="%{model.status != null && model.status.code == 'CHECKED'}">
		<s:if test="%{model.currentState != null && model.currentState.nextAction!=''}">
			<s:property value="%{model.billnumber}"/> : <s:text name="%{model.status.code}"/> - <s:text name="%{model.currentState.nextAction}"/>
		</s:if>
		<s:else>
			<s:property value="%{model.billnumber}"/> : &nbsp; <s:text name="bill.checked" />
		</s:else>               
        <br>
        <s:text name="common.forwardmessage" />  <s:property value="%{nextEmployeeName}" />(<s:property value="%{nextDesignation}" />)
	</s:elseif>	
	<s:elseif test="%{model.status != null && model.status.code == 'REJECTED'}">
		<s:property value="%{model.billnumber}"/>&nbsp; <s:text name="bill.reject" />
		<br>
		<s:text name="common.forwardmessage" /> &nbsp;<s:property value="%{nextEmployeeName}" />(<s:property value="%{nextDesignation}" />)		
	</s:elseif>	
	<s:elseif test="%{model.status != null && model.status.code == 'CANCELLED'}">
		<s:property value="%{model.billnumber}"/>&nbsp; <s:text name="bill.cancel"/>
	</s:elseif>	
	 <s:else>
       	<s:if test="%{model.currentState.nextAction!=''}">
			<s:property value="%{model.billnumber}"/> : <s:text name="%{model.currentState.value}"/> - <s:text name="%{model.currentState.nextAction}"/>
		</s:if>
		<s:else>
			<s:property value="%{model.billnumber}"/> : <s:text name="bill.created" />
		</s:else>   
        <br>
        <s:if test="%{model.status != null && model.status.code != 'CANCELLED'}">
        <s:text name="common.forwardmessage" />  <s:property value="%{nextEmployeeName}" />(<s:property value="%{nextDesignation}" />)
        </s:if>
       </s:else> 
	
</body>
</html>
