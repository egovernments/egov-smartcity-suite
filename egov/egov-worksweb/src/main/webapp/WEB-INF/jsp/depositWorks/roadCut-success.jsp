<%@ taglib prefix="egov" tagdir="/WEB-INF/tags" %>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator"%>
<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld" %>  
<html>

<title>Road Cut Application</title>
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

<s:if test="%{sourcepage==''}">
<table align="center">
<tr>
<td align="center">
	<s:if test="%{hasErrors()}">
		<div class="errorstyle">
			<s:actionerror />
			<s:fielderror />
		</div>
	</s:if>
	<s:if test="%{hasActionMessages()}">
		<div class="messagestyle">
			<s:actionmessage theme="simple" />
			<s:if test="%{bpaSuccessMsg != ''}">
				<s:property value="%{bpaSuccessMsg}" escape="false"/>
			</s:if>
			<s:if test="%{emailMsg!=''}">
				<s:property value="%{emailMsg}"/>
			</s:if>
			<br>
			<s:if test="%{smsSuccessMsg!=''}">
				<s:property value="%{smsSuccessMsg}"/>
			</s:if>
		</div>
	</s:if>
	<s:if test="%{(applicationRequest.depositWorksCategory==@org.egov.works.models.depositWorks.DepositWorksCategory@EMERGENCYCUT && egwStatus.code=='APPROVED')}">
            <br />
            <s:text name="applicationDetails.DepostiCode" /><s:property value="%{applicationDetails.depositCode.code}"/>
	        <div class="buttonholderwk" id="buttons">
				<input type="button" onclick="window.open('${pageContext.request.contextPath}/depositWorks/roadCut!viewDamageFeeCommunicationPDF.action?mode=view&sourcepage=damageFeePDF&appDetailsId=<s:property value="id"/>');" class="buttonpdf" value="Damage Fee Communication Letter PDF" id="pdfButton" name="pdfButton"/>
			</div>           
     </s:if> 
</td>
</tr>
<tr>
<td align="center">
<input type="button" class="buttonfinal" value="CLOSE"
		id="closeButton" name="button"
		onclick="window.close();" />
</td></tr>
</table>
</s:if>	
<s:elseif test="%{mode != null && (mode == 'UpdateRoadCutRestorationDate' || mode == 'UpdateRoadCutDate')}">
	<s:actionmessage theme="simple" />
	<br>
</s:elseif>	
<s:elseif test="%{mode != null && mode == 'rejectForResubmission'}">
	<br/>
	<s:if test="%{smsSuccessMsg!=''}">
		<s:property value="%{smsSuccessMsg}"/>
	</s:if>
</s:elseif>
<s:else>
		<s:if test="%{currentState != null && currentState.previous.value == 'APPROVED'}">
            <s:property value="%{applicationRequest.applicationNo}"/>&nbsp; <s:text name="dw.feasibilityReport.approved" />
            <br /><br />
	        <div class="buttonholderwk" id="buttons">
				<input type="button" onclick="window.open('${pageContext.request.contextPath}/depositWorks/roadCut!viewDamageFeeCommunicationPDF.action?mode=view&sourcepage=damageFeePDF&appDetailsId=<s:property value="id"/>');" class="buttonpdf" value="Damage Fee Communication Letter PDF" id="pdfButton" name="pdfButton"/>
				<input type="button" class="buttonfinal" value="CLOSE" id="closeButton" name="closeButton" onclick="window.close();"/>
			</div>           
       </s:if>        
       <s:elseif test="%{currentState != null && currentState.value == 'CHECKED'}"> 
               <s:property value="%{applicationRequest.applicationNo}"/>  <s:text name="dw.feasibilityReport.checked" />
               <br>
               <s:text name="common.forwardmessage" />  <s:property value="%{employeeName}" />(<s:property value="%{designation}" />)                
       </s:elseif>   
       <s:elseif test="%{currentState != null && currentState.value == 'REJECTED'}">
               <s:property value="%{applicationRequest.applicationNo}"/>  <s:text name="dw.feasibilityReport.rejected" />
                <br>
               <s:text name="common.forwardmessage" />  <s:property value="%{employeeName}" />(<s:property value="%{designation}" />)
       </s:elseif>                                         
       <s:else>
       		<s:if test="%{messageKey==''}"> 
       		   <s:text name="applicationDetails.DepostiCode" /><s:property value="%{applicationDetails.depositCode.code}"/> 
       		   <br>
               <s:property value="%{applicationRequest.applicationNo}"/>  <s:text name="applicationDetails.Forward" />
             </s:if>
             <s:else>
              <s:property value="%{applicationRequest.applicationNo}"/>  <s:text name="%{getText(messageKey)}" />
             </s:else>  
               <br>
               <s:if test="%{currentState != null && currentState.previous.value != 'CANCELLED'}">
               <s:text name="common.forwardmessage" />  <s:property value="%{employeeName}" />(<s:property value="%{designation}" />)
               </s:if>
       </s:else>
       <br>
       <s:if test="%{emailMsg!=''}">
			<s:property value="%{emailMsg}"/>
		</s:if>
		<br>
		<s:if test="%{smsSuccessMsg!=''}">
			<s:property value="%{smsSuccessMsg}"/>
		</s:if>
</s:else> 

</body>
</html>