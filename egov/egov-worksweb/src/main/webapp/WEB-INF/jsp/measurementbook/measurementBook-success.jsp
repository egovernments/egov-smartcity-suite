
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags" %>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator"%>
<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld" %>  
<html>

<title>Measurement Book</title>
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
             <s:property value="%{model.mbRefNo}"/> :&nbsp; <s:text name='measurementbook.approved' />	             
       </s:if>        
       <s:elseif test="%{model.egwStatus != null && model.egwStatus.code == 'CHECKED'}"> 
		    <s:if test="%{model.currentState.nextAction!=''}">
				<s:property value="%{model.mbRefNo}"/> : <s:text name="%{model.egwStatus.code}"/> - <s:text name="%{model.currentState.nextAction}"/>
			</s:if>
			<s:else>
				<s:property value="%{model.mbRefNo}"/> : &nbsp; <s:text name="measurementbook.checked" />
			</s:else>               
               <br>
               <s:text name="common.forwardmessage" />  <s:property value="%{dispEmployeeName}" />(<s:property value="%{dispDesignation}" />)                
       </s:elseif>   
       <s:elseif test="%{model.egwStatus != null && model.egwStatus.code == 'REJECTED'}">
               <s:property value="%{model.mbRefNo}"/> : <s:text name="measurementbook.rejected" />
                <br>
               <s:text name="common.forwardmessage" />  <s:property value="%{dispEmployeeName}" />(<s:property value="%{dispDesignation}" />)
       </s:elseif> 
       <s:elseif test="%{model.egwStatus != null && model.egwStatus.code == 'CANCELLED'}">
               <s:property value="%{model.mbRefNo}"/> : <s:text name="measurementbook.cancel" />
       </s:elseif>                                          
       <s:else>
        	<s:if test="%{model.egwStatus!=null && model.currentState.nextAction!=''}">
				<s:property value="%{model.mbRefNo}"/> : <s:text name="%{model.egwStatus.code}"/> - <s:text name="%{model.currentState.nextAction}"/>
			</s:if>
			<s:else>
				<s:if test="%{model.mbRefNo !=null && model.mbRefNo!=''}"> 
					<s:property value="%{model.mbRefNo}"/> : <s:text name="%{getText(messageKey)}" /> 
				</s:if>
				<s:else>
					<s:text name="%{getText(messageKey)}" /> 
				</s:else>
			</s:else>   
               
               <br>
               <s:if test="%{model.egwStatus != null && model.egwStatus.code != 'CANCELLED'}">
               <s:text name="common.forwardmessage" />  <s:property value="%{dispEmployeeName}" />(<s:property value="%{dispDesignation}" />)
               </s:if>
       </s:else> 
		
</body>
</html>
