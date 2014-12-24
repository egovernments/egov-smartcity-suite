
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags" %>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator"%>
<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld" %>  
<html>

<title><s:text name="page.title.workorderCompletionExtension" /></title>
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
		<s:property value=""/>
		<s:if test="%{workCompletionDetail.currentState != null && workCompletionDetail.currentState.previous.value == 'APPROVED'}">
            <s:text name="workOrderCompletionDetail"/><s:property value="%{workCompletionDetail.workOrder.workOrderNumber}"/>&nbsp; <s:text name='workOrderCompletion.approved' />	             
       </s:if>        
       <s:elseif test="%{workCompletionDetail.currentState != null && workCompletionDetail.currentState.value == 'CHECKED'}"> 
             <s:text name="workOrderCompletionDetail"/>  <s:property value="%{workCompletionDetail.workOrder.workOrderNumber}"/>  <s:text name="workOrderCompletion.checked" />
               <br>
               <s:text name="common.forwardmessage" />  <s:property value="%{employeeName}" />(<s:property value="%{designation}" />)                
       </s:elseif>   
       <s:elseif test="%{workCompletionDetail.currentState != null && workCompletionDetail.currentState.value == 'REJECTED'}">
               <s:text name="workOrderCompletionDetail"/><s:property value="%{workCompletionDetail.workOrder.workOrderNumber}"/>  <s:text name="workOrderCompletion.rejected" />
                <br>
               <s:text name="common.forwardmessage" />  <s:property value="%{employeeName}" />(<s:property value="%{designation}" />)
       </s:elseif>                                         
       <s:else>
               <s:text name="workOrderCompletionDetail"/><s:property value="%{workCompletionDetail.workOrder.workOrderNumber}"/>  <s:text name="%{getText(messageKey)}" />
               <br>
               <s:if test="%{workCompletionDetail.currentState != null && workCompletionDetail.currentState.previous.value != 'CANCELLED'}">
               <s:text name="common.forwardmessage" />  <s:property value="%{employeeName}" />(<s:property value="%{designation}" />)
               </s:if>
       </s:else>
       	
</body>
</html>
