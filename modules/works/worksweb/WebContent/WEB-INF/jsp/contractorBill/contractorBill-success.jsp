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
		<s:if test="%{model.currentState.nextAction!='' && model.currentState.nextAction!=null}">
			<s:property value="%{model.billnumber}"/> &nbsp; <s:text name="%{getText(messageKey)}" /> - <s:text name="%{model.currentState.nextAction}"/>
			<br>
		</s:if>
		<s:else>
			<s:property value="%{model.billnumber}"/> &nbsp; <s:text name="%{getText(messageKey)}" /> <br>
		</s:else>
		<s:if test="%{model.currentState.value=='END'}">
		</s:if>
		<s:else>	
		The File has been forwarded to <s:property value="%{nextEmployeeName}" />(<s:property value="%{nextDesignation}" />) <br />
		</s:else>	
		
	<!-- <s:if test="%{model.currentState != null && model.currentState.previous.value == 'APPROVED'}">
		<s:property value="%{model.billnumber}"/>	<br>	
		 <s:text name="bill.approved" />
	</s:if>	
	<s:elseif test="%{model.currentState != null && model.currentState.value == 'CHECKED'}">
		<s:if test="%{model.currentState.nextAction!=''}">
			<s:property value="%{model.billnumber}"/> : <s:text name="%{model.currentState.value}"/> - <s:text name="%{model.currentState.nextAction}"/>
		</s:if>
		<s:else>
			<s:property value="%{model.billnumber}"/> : &nbsp; <s:text name="bill.checked" />
		</s:else>               
        <br>
        <s:text name="common.forwardmessage" />  <s:property value="%{nextEmployeeName}" />(<s:property value="%{nextDesignation}" />)
	</s:elseif>	
	<s:elseif test="%{model.currentState != null && model.currentState.value == 'REJECTED'}">
		<s:property value="%{model.billnumber}"/>&nbsp; <s:text name="bill.reject" />
		<br>
		<s:text name="common.forwardmessage" /> &nbsp;<s:property value="%{nextEmployeeName}" />(<s:property value="%{nextDesignation}" />)		
	</s:elseif>	
	<s:elseif test="%{model.currentState != null && model.currentState.previous.value == 'CANCELLED'}">
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
        <s:if test="%{model.currentState != null && model.currentState.previous.value != 'CANCELLED'}">
        <s:text name="common.forwardmessage" />  <s:property value="%{nextEmployeeName}" />(<s:property value="%{nextDesignation}" />)
        </s:if>
       </s:else> -->
	
</body>
</html>
