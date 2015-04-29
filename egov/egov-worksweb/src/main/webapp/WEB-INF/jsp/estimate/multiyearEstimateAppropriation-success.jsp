<%@ taglib prefix="egov" tagdir="/WEB-INF/tags" %>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator"%>
<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld" %>  
<html>

<title><s:text name='multiyearEstimateAppr.page.title' /></title>
<body >
<script>
     var x=opener.top.opener;
     if(x==null){
         x=opener.top;
     }
     x.document.getElementById('inboxframe').contentWindow.egovInbox.from = 'Inbox';
  	 x.document.getElementById('inboxframe').contentWindow.egovInbox.refresh();

</script> 
 		<s:if test="%{model.status.code == 'APPROVED'}">
           <s:text name='multiyearEstimateAppr.approved' />	             
       </s:if>        
       <s:elseif test="%{model.status.code == 'REJECTED'}">
               <s:text name="multiyearEstimateAppr.rejected" />
                <br>
               <s:text name="common.forwardmessage" />  <s:property value="%{nextEmployeeName}" />(<s:property value="%{nextDesignation}" />)
       </s:elseif>                                         
       <s:elseif test="%{model.status.code == 'CREATED'}">
               <s:text name="multiyearEstimateAppr.created" />
                <br>
               <s:text name="common.forwardmessage" />  <s:property value="%{nextEmployeeName}" />(<s:property value="%{nextDesignation}" />)
       </s:elseif>                                         
       <s:elseif test="%{model.status.code == 'RESUBMITTED'}">
               <s:text name="multiyearEstimateAppr.resubmitted" />
                <br>
               <s:text name="common.forwardmessage" />  <s:property value="%{nextEmployeeName}" />(<s:property value="%{nextDesignation}" />)
       </s:elseif>                                         
       <s:elseif test="%{model.status.code == 'CHECKED'}">
               <s:text name="multiyearEstimateAppr.checked" />
                <br>
               <s:text name="common.forwardmessage" />  <s:property value="%{nextEmployeeName}" />(<s:property value="%{nextDesignation}" />)
       </s:elseif>                                         
       <s:else>
               <s:text name="multiyearEstimateAppr.cancelled" />
       </s:else> 
</body>
</html>
