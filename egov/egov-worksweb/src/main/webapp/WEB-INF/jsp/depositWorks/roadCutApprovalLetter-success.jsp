<%@ taglib prefix="egov" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld" %>  
<html>

<title>Road Cut Approval Letter</title>
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
			<s:if test="%{currentState != null && currentState.previous.value == 'APPROVED'}">
		            <s:property value="%{model.applicationDetails.applicationRequest.applicationNo}"/>&nbsp; <s:text name="dw.roadCutApprovalLetter.approved" />	             
		       </s:if>        
		       <s:elseif test="%{currentState != null && currentState.value == 'CHECKED'}"> 
		               <s:property value="%{model.applicationDetails.applicationRequest.applicationNo}"/>  <s:text name="dw.roadCutApprovalLetter.checked" />
		               <br>
		               <s:text name="common.forwardmessage" />  <s:property value="%{employeeName}" />(<s:property value="%{designation}" />)                
		       </s:elseif>   
		       <s:elseif test="%{currentState != null && currentState.value == 'REJECTED'}">
		               <s:property value="%{model.applicationDetails.applicationRequest.applicationNo}"/>  <s:text name="dw.roadCutApprovalLetter.rejected" />
		                <br>
		               <s:text name="common.forwardmessage" />  <s:property value="%{employeeName}" />(<s:property value="%{designation}" />)
		       </s:elseif>                                         
		       <s:else>
		               <s:property value="%{model.applicationDetails.applicationRequest.applicationNo}"/>  <s:text name="%{getText(messageKey)}" />
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
		</td>
	</tr>
</table>
<div class="buttonholderwk" id="buttons">
		<s:if
			test="%{(model.egwStatus.code=='APPROVED') && currentState != null && currentState.previous.value == 'APPROVED'}">
			<input type="button"
				onclick="window.open('${pageContext.request.contextPath}/depositWorks/roadCutApprovalLetter!viewRoadCutApprovalPDF.action?appDetailsId=<s:property  value="applicationDetails.id"/>');"
				class="buttonpdf" value="PRINT" id="pdfButton" name="pdfButton" />
		</s:if>
		<input type="button" class="buttonfinal" value="CLOSE"
			id="closeButton" name="button" onclick="window.close();" />
			</div>
	</body>
</html>