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
	<s:if test="%{model.currentState.nextAction!='' && model.currentState.nextAction!=null}">
		<s:property value="%{model.estimateNumber}"/> &nbsp; <s:text name="%{getText(messageKey)}" /> - <s:text name="%{model.currentState.nextAction}"/>
		<br>
	</s:if>
	<s:else>
		<s:property value="%{model.estimateNumber}"/> &nbsp; <s:text name="%{getText(messageKey)}" /> <br>
	</s:else> 
	
	<s:if test="%{model.techSanctionNumber!='' && model.currentState.value=='TECH_SANCTIONED'}">
		<s:text name='estimate.technicalSanction.number'/> &nbsp; <s:property value="%{model.techSanctionNumber}"/><br>
	</s:if>
	<s:if test="%{isSpillOverWorks==true && model.currentState.previous.value=='ADMIN_SANCTIONED' && model.currentState.value=='END'}">
		<s:text name='estimate.technicalSanction.number'/> &nbsp; <s:property value="%{model.techSanctionNumber}"/><br>
	</s:if>
	<s:if test="%{isSpillOverWorks!=true && model.techSanctionNumber!=null && model.currentState.previous.value=='ADMIN_SANCTIONED' && model.currentState.value=='END'}">
		<s:text name='estimate.technicalSanction.number'/> &nbsp; <s:property value="%{model.techSanctionNumber}"/><br>
	</s:if>

	<s:if test="%{model.currentState.previous.value=='ADMIN_SANCTIONED' && model.currentState.value=='END'}">
		<s:if test="%{model.budgetApprNo!=null && skipBudget==false}">
     		The budget appropriation number is <s:property value="%{model.budgetApprNo}" /><br />
    	</s:if>
    	<s:if test="%{model.budgetApprNo!=null && skipBudget==true}">
     		The deposit works appropriation number is <s:property value="%{model.budgetApprNo}" /><br />
    	</s:if>	
		<s:if test="%{model.projectCode!=null && model.currentState.previous.value=='ADMIN_SANCTIONED' && model.currentState.value=='END'}">
				The project code for the estimate is <s:property value="%{model.projectCode.code}" /> <br />
			</s:if>
		<s:if test="%{model.adminsanctionNo!=null && model.currentState.previous.value=='ADMIN_SANCTIONED' && model.currentState.value=='END'}">
				The Admin sanction number is <s:property value="%{model.adminsanctionNo}"/> <br />
			</s:if>
	</s:if>
	<s:else>	
		<s:if test="%{model.currentState.previous.value=='CANCELLED' && model.currentState.value=='END'}">	
			
		</s:if>
		<s:elseif test="%{hideWorkFlowInfo==true}">
		  The File has been forwarded successfully.
		</s:elseif>
		<s:else >
		The File has been forwarded to <s:property value="%{employeeName}" />(<s:property value="%{designation}" />) <br />
	</s:else>	
	</s:else>	
	 

<!--<s:if test="%{model.currentState.value=='REJECTED' && skipBudget==false && (model.currentState.previous.value=='ADMIN_CHECKED'  || model.currentState.previous.value=='FINANCIALLY_SANCTIONED')}">
		Budgetary cancellation number for the estimate is <s:property value="%{model.budgetRejectionNo}" />	<br />
			</s:if>	 
	<s:if test="%{model.currentState.value=='REJECTED' && skipBudget==true && (model.currentState.previous.value=='ADMIN_CHECKED' || model.currentState.previous.value=='FINANCIALLY_SANCTIONED')}">
				Deposit Code cancellation number for the estimate is <s:property value="%{budgetRejectionNo}" /><br />	
			</s:if>	 					

			<s:if test="%{model.currentState.value=='ADMIN_SANCTIONED' || model.currentState.value=='END'}">
		The project code for the estimate is <s:property value="%{model.projectCode.code}" />
	 		</s:if>
-->
	
</body>
</html>
