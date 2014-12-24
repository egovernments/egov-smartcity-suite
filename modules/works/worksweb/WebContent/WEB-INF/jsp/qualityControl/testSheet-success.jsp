<%@ taglib prefix="egov" tagdir="/WEB-INF/tags" %>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator"%>
<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld" %>  
<html>

<title><s:text name="qualityControl.testSheet.title" /></title>
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
<table align="center">
	<tr>
		<td align="center">
			<s:if test="%{model.egwStatus != null && model.egwStatus.code == 'APPROVED'}">
	             <s:property value="%{model.testSheetNumber}"/> &nbsp;<s:text name='testSheet.approved' />	             
	       </s:if>   
	            
	       <s:elseif test="%{model.egwStatus != null && model.egwStatus.code == 'CHECKED'}"> 
	                <s:property value="%{model.testSheetNumber}"/> &nbsp;<s:text name="testSheet.checked" />
	               <br>
	               <s:text name="common.forwardmessage" />  <s:property value="%{employeeName}" />(<s:property value="%{designation}" />)                
	       </s:elseif>   
	       <s:elseif test="%{model.egwStatus != null && model.egwStatus.code == 'REJECTED'}">
	                <s:property value="%{model.testSheetNumber}"/> &nbsp;<s:text name="testSheet.rejected" />
	                <br>
	               <s:text name="common.forwardmessage" />  <s:property value="%{employeeName}" />(<s:property value="%{designation}" />)
	       </s:elseif>
	                                                
	       <s:elseif test="%{model.egwStatus != null && model.egwStatus.code == 'RESUBMITTED'}">
	      				<s:property value="%{model.testSheetNumber}"/> &nbsp;<s:text name="testSheet.RESUBMITTED" />  <br>  
	      				<s:text name="common.forwardmessage" />  <s:property value="%{employeeName}" />(<s:property value="%{designation}" />)         
	       </s:elseif> 
	       <s:else>
	                 <s:property value="%{model.testSheetNumber}"/> &nbsp; <s:text name="%{getText(messageKey)}" />
	               <s:if test="%{model.egwStatus != null && model.egwStatus.code != 'CANCELLED'}">
	               <br>
	               <s:text name="common.forwardmessage" />  <s:property value="%{employeeName}" />(<s:property value="%{designation}" />)
	               </s:if>
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