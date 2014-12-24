
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags" %>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator"%>
<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld" %>  
<html>

<title><s:text name="page.title.workorder" /></title>
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
		
		<s:if test="%{workOrder.currentState != null && workOrder.currentState.previous.value == 'APPROVED'}">
            <s:property value="%{workOrder.workOrderNumber}"/>&nbsp; <s:text name='workOrder.approved' />	             
       </s:if>        
       <s:elseif test="%{workOrder.currentState != null && workOrder.currentState.value == 'CHECKED'}"> 
               <s:property value="%{workOrder.workOrderNumber}"/>  <s:text name="workorder.checked" />
               <br>
               <s:text name="common.forwardmessage" />  <s:property value="%{employeeName}" />(<s:property value="%{designation}" />)                
       </s:elseif>   
       <s:elseif test="%{workOrder.currentState != null && workOrder.currentState.value == 'REJECTED'}">
               <s:property value="%{workOrder.workOrderNumber}"/>  <s:text name="workorder.rejected" />
                <br>
               <s:text name="common.forwardmessage" />  <s:property value="%{employeeName}" />(<s:property value="%{designation}" />)
       </s:elseif>                                         
       <s:elseif test="%{workOrder.currentState != null && workOrder.currentState.value == 'CREATED' && (workOrder.currentState.nextAction!=null && workOrder.currentState.nextAction=='Pending for Approval')}">
               <s:property value="%{workOrder.workOrderNumber}"/>  <s:text name="workorder.created" />
                <br>
               <s:text name="common.forwardmessage" />  <s:property value="%{employeeName}" />(<s:property value="%{designation}" />)
       </s:elseif>  
        <s:elseif test="%{workOrder.currentState != null && workOrder.currentState.value == 'RESUBMITTED'}">
   			   <s:property value="%{workOrder.workOrderNumber}"/>  <s:text name="workorder.RESUBMITTED" />
                <br>
               <s:text name="common.forwardmessage" />  <s:property value="%{employeeName}" />(<s:property value="%{designation}" />)
       </s:elseif>                                        
       <s:else>
   			   <s:property value="%{workOrder.workOrderNumber}"/>  <s:text name="%{getText(messageKey)}" />
               <br>
               <s:if test="%{workOrder.currentState != null && workOrder.currentState.previous.value != 'CANCELLED'}">
               <s:text name="common.forwardmessage" />  <s:property value="%{employeeName}" />(<s:property value="%{designation}" />)
               </s:if>
       </s:else> 
       <s:if test="%{workOrder.currentState != null && workOrder.currentState.previous.value == 'APPROVED'}">
       		<br>
       		<s:property value="%{emailMsg}"/>
       </s:if>	
</body>
</html>
