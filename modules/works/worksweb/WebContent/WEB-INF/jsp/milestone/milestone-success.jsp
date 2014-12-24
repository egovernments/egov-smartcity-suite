<%@ taglib prefix="egov" tagdir="/WEB-INF/tags" %>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator"%>
<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld" %>  
<html>

<title>Milestone</title>
<body >
<script>
        //var x=opener.top.opener;
        //if(x==null){
         //   x=opener.top;
       // }
        //if(x.document.getElementById('inboxframe')!=null){
        	//x.document.getElementById('inboxframe').contentWindow.egovInbox.from = 'Inbox';
	    	//x.document.getElementById('inboxframe').contentWindow.egovInbox.refresh();
	  //  }
</script> 

		<s:if test="%{model.egwStatus.code == 'APPROVED'}">
            <s:property value="%{model.code}"/>&nbsp; <s:text name='milestone.approved' />	             
       </s:if>        
 <!--       <s:elseif test="%{model.egwStatus.code == 'REJECTED'}">
               <s:property value="%{model.code}"/>  <s:text name="milestone.rejected" />
                <br>
               <s:text name="common.forwardmessage" />  <s:property value="%{nextEmployeeName}" />(<s:property value="%{nextDesignation}" />)
       </s:elseif>                                         
       <s:else>
               <s:property value="%{model.code}"/>  <s:text name="%{getText(messageKey)}" />
               <br>
               <s:if test="%{model.egwStatus.code != 'CANCELLED'}">
               <s:text name="common.forwardmessage" />  <s:property value="%{nextEmployeeName}" />(<s:property value="%{nextDesignation}" />)
               </s:if>
       </s:else> --> 

</body>
</html>
