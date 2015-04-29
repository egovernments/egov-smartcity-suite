<%@ taglib prefix="egov" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld" %>  
<html>

<title><s:text name="revisionEstimate.label.title" /></title>
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
	<s:if test="%{model.currentState.nextAction!='' && model.currentState.nextAction!=null}">
		<s:property value="%{model.estimateNumber}"/> &nbsp; <s:text name="%{getText(messageKey)}" /> - <s:text name="%{model.currentState.nextAction}"/>
		<br>
		<s:text name="common.forwardmessage" />  <s:property value="%{employeeName}" />(<s:property value="%{designation}" />)
	</s:if>
	<s:else>
		<s:property value="%{model.estimateNumber}"/> &nbsp; <s:text name="%{getText(messageKey)}" /> <br>
        <s:if test="%{model.currentState != null && model.currentState.value != 'END'}">
        	<s:text name="common.forwardmessage" />  <s:property value="%{employeeName}" />(<s:property value="%{designation}" />)
        </s:if>
	</s:else>
	<s:if test="%{model.egwStatus!=null && model.egwStatus.code=='BUDGETARY_APPROPRIATION_DONE'}" >
		<s:if test="%{budgetApprNo!=null }">
			<br /><s:text name="revision.estimate.budget.appr" />
     		<br /><s:text name="revisionEstimate.budget.appr.no" /><s:property value="%{budgetApprNo}" />
     	</s:if>	
    </s:if>
    <s:if test="%{model.egwStatus!=null && model.egwStatus.code=='REJECTED'}" >
		<s:if test="%{isBudgetRejectionNoPresent==true }">
			<br /><s:text name="revision.estimate.budget.release" />
     		<br /><s:text name="revisionEstimate.budget.rejection.no" /><s:property value="%{budgetRejectionNo}" />
     	</s:if>	
    </s:if>
	
</body>
</html>