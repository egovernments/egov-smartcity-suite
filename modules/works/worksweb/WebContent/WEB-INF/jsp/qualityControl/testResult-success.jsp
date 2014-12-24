<%@ taglib prefix="egov" tagdir="/WEB-INF/tags" %>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator"%>
<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld" %>  
<html>

<title><s:text name="test.result.created.title" /></title>
<body onload="refreshInbox();terminateWindowWhenClosed();"> 
<script>
function terminateWindowWhenClosed()
{
	var msgKey = '<s:text name="%{messageKey}" />';
	if(msgKey=='The Test Result was closed')
		window.close();
}
function refreshInbox(){
    var x=opener.top.opener;
    if(x==null){
        x=opener.top;
    }
    x.document.getElementById('inboxframe').contentWindow.egovInbox.refresh();
}
</script> 
<table align="center">
	<tr>
		<td align="center">
			<s:if test="%{model.egwStatus != null && model.egwStatus.code == 'APPROVED'}">
	             <s:text name='test.result.approved' />	             
	       </s:if>   
	            
	       <s:elseif test="%{model.egwStatus != null && model.egwStatus.code == 'CHECKED'}"> 
	               <s:text name="test.result.checked" />
	               <br>
	               <s:text name="common.forwardmessage" />  <s:property value="%{employeeName}" />(<s:property value="%{designation}" />)                
	       </s:elseif>   
	       <s:elseif test="%{model.egwStatus != null && model.egwStatus.code == 'REJECTED'}">
	                <s:text name="test.result.rejected" />
	                <br>
	               <s:text name="common.forwardmessage" />  <s:property value="%{employeeName}" />(<s:property value="%{designation}" />)
	       </s:elseif>
	                                                
	       <s:elseif test="%{model.egwStatus != null && model.egwStatus.code == 'RESUBMITTED'}">
	      				<s:text name="test.result.resubmitted" /><br>  
	      				<s:text name="common.forwardmessage" />  <s:property value="%{employeeName}" />(<s:property value="%{designation}" />)         
	       </s:elseif> 
	       <s:elseif test="%{model.egwStatus != null && model.egwStatus.code == 'CANCELLED'}">
	                   <s:text name="test.result.cancelled" />		
	       </s:elseif>
	       <s:else>
	                   <s:text name="test.result.saved" />					
	       </s:else> 
    	</td>
	</tr>
</table>
<br>
<div class="buttonholderwk" id="buttons">		
		<input type="button" class="buttonfinal" value="CLOSE" id="closeButton" name="button" onclick="window.close();" />
</div>
</body>
</html>