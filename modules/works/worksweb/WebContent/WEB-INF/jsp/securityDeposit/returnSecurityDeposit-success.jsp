
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags" %>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator"%>
<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld" %>  
<html>

<title><s:text name="returnsecuritydeposit.title" /></title>
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

		<s:if test="%{model.egwStatus != null && model.egwStatus.code == 'APPROVED'}">
             <s:text name='rsd.approved' />	             
       </s:if>   
            
       <s:elseif test="%{model.egwStatus != null && model.egwStatus.code == 'CHECKED'}"> 
                <s:text name="rsd.checked" />
               <br>
               <s:text name="common.forwardmessage" />  <s:property value="%{employeeName}" />(<s:property value="%{designation}" />)                
       </s:elseif>   
       <s:elseif test="%{model.egwStatus != null && model.egwStatus.code == 'REJECTED'}">
                <s:text name="rsd.rejected" />
                <br>
               <s:text name="common.forwardmessage" />  <s:property value="%{employeeName}" />(<s:property value="%{designation}" />)
       </s:elseif>
                                                
       <s:elseif test="%{model.egwStatus != null && model.egwStatus.code == 'RESUBMITTED'}">
      				<s:text name="rsd.RESUBMITTED" />  <br>  
      				<s:text name="common.forwardmessage" />  <s:property value="%{employeeName}" />(<s:property value="%{designation}" />)         
       </s:elseif> 
       <s:else>
                <s:text name="%{getText(messageKey)}" />
               <s:if test="%{model.egwStatus != null && model.egwStatus.code != 'CANCELLED'}">
               <br>
               <s:text name="common.forwardmessage" />  <s:property value="%{employeeName}" />(<s:property value="%{designation}" />)
               </s:if>
       </s:else> 
</body>
</html>