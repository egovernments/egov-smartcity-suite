<%@ taglib prefix="egov" tagdir="/WEB-INF/tags" %>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator"%>
<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld" %>  
<html>

<title><s:text name='page.title.estimate.reappropriation' /></title>
<body >
<script>
	<s:if test="%{model.department != null}">
        var x=opener.top.opener;
        if(x==null){
            x=opener.top;
        }
        x.document.getElementById('inboxframe').contentWindow.egovInbox.from = 'Inbox';
	    x.document.getElementById('inboxframe').contentWindow.egovInbox.refresh();
	</s:if>

</script> 

<s:if test="%{model.department == null}">
	<s:text name='estimate.reappropriation.jurisdiction.update' />
</s:if>

<s:else>
 		<s:if test="%{model.status.code == 'APPROVED'}">
           <s:text name='estimate.reappr.approved' />	             
       </s:if>        
       <s:elseif test="%{model.status.code == 'REJECTED'}">
               <s:text name="estimate.reappr.rejected" />
                <br>
               <s:text name="common.forwardmessage" />  <s:property value="%{nextEmployeeName}" />(<s:property value="%{nextDesignation}" />)
       </s:elseif>                                         
       <s:elseif test="%{model.status.code == 'CREATED'}">
               <s:text name="estimate.reappr.created" />
                <br>
               <s:property value="%{nextEmployeeName}" />(<s:property value="%{nextDesignation}" />)
       </s:elseif>                                         
       <s:elseif test="%{model.status.code == 'RESUBMITTED'}">
               <s:text name="estimate.reappr.resubmitted" />
                <br>
               <s:text name="common.forwardmessage" />  <s:property value="%{nextEmployeeName}" />(<s:property value="%{nextDesignation}" />)
       </s:elseif>                                         
       <s:elseif test="%{model.status.code == 'CHECKED'}">
               <s:text name="estimate.reappr.checked" />
                <br>
               <s:text name="common.forwardmessage" />  <s:property value="%{nextEmployeeName}" />(<s:property value="%{nextDesignation}" />)
       </s:elseif>                                         
       <s:else>
               <s:text name="estimate.reappr.cancelled" />
       </s:else> 
</s:else>
</body>
</html>
