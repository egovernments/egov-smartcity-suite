<%@ taglib prefix="egov" tagdir="/WEB-INF/tags" %>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator"%>
<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld" %>  
<html>

<title>Abstract Estimate</title>
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
	<s:if test="%{estimateId != null &&estimateId != ''}">  
		<s:property value="%{model.estimateNumber}"/> &nbsp;: <s:text name="%{getText(messageKey)}" />
	</s:if>
	<s:else>
		<s:if test="%{model.currentState.nextAction!=''}">
			<s:property value="%{model.estimateNumber}"/> &nbsp; <s:text name="%{getText(messageKey)}" /> - <s:text name="%{model.currentState.nextAction}"/>
		</s:if>
		<s:else>
			<s:property value="%{estimateNumber}"/> &nbsp; <s:text name="%{getText(messageKey)}" />
		</s:else>
		<br />
		 <s:if test="%{model.state.text2!=null}">
		 	Estimate approval number is <s:property value="%{model.state.text2}" /> 
		 </s:if>
		 <s:if test="%{model.egwStatus.code=='ADMIN_SANCTIONED' && model.state.previous.text2!=null}">
		 	Estimate approval number is <s:property value="%{model.state.previous.text2}" />
		 </s:if>	
		<br>
		<s:if test="%{projectCode}">
				<s:if test="%{model.egwStatus.code=='CANCELLED'}">			
				</s:if>
				<s:else>
					The project code for the estimate is <s:property value="%{projectCode.code}" />
					<br/>
					<input type="button" onclick="window.open('${pageContext.request.contextPath}/estimate/abstractEstimatePDF.action?estimateID=<s:property value='%{model.id}'/>');" class="buttonpdf" value="PRINT Estimate PDF" id="pdfButton" name="pdfButton"/>
				</s:else>
		</s:if>
	    <s:else>
		 <br />
				<s:if test="%{model.egwStatus.code=='REJECTED' && model.currentState.nextAction=='Pending Budgetary Appropriation'}">
					Budgetary cancellation number for the estimate is <s:property value="%{budgetRejectionNo}" />	
				<br />
				</s:if>	 
				<s:if test="%{model.egwStatus.code=='REJECTED' && model.currentState.nextAction=='Pending Deposit Code Appropriation'}">
					Deposit Code cancellation number for the estimate is <s:property value="%{budgetRejectionNo}" />	
				<br />
				</s:if>	 					
				<s:if test="%{model.egwStatus.code=='ADMIN_SANCTIONED' || model.currentState.value=='END'}">
		 		</s:if>
				<s:else>	
					The File has been forwarded to <s:property value="%{employeeName}" />(<s:property value="%{designation}" />)
				</s:else>	
		</s:else>
	</s:else>
</body>
</html>
