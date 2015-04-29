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
	<s:if test="%{model.egwStatus.code=='CREATED' || model.egwStatus.code=='RESUBMITTED'}">	  
		<s:property value="%{model.estimateNumber}"/> &nbsp;: <s:text name="%{getText(messageKey)}" />
		<br>
		 Budget appropriation done successfully. The budget appropriation number is <s:property value="%{model.budgetApprNo}" />
		 <br>
		 The File has been forwarded to <s:property value="%{employeeName}" />(<s:property value="%{designation}" />)
	</s:if>	
	<s:else>
		<s:property value="%{model.estimateNumber}"/> &nbsp;: <s:text name="%{getText(messageKey)}" />
	</s:else>
	<br>
	<s:if test="%{projectCode}">
		<s:if test="%{model.egwStatus.code=='CANCELLED'}">			
		</s:if>
		<s:else>
			The project code for the estimate is <s:property value="%{projectCode.code}" />
		</s:else>
	</s:if>
	<s:if test="%{model.egwStatus.code=='ADMIN_SANCTIONED'}">
			<br>The Work Order for the estimate is <s:property value="%{workOrder.workOrderNumber}" />
	</s:if>
	<s:if test="%{model.egwStatus.code=='REJECTED'}">
		Budgetary cancellation number for the estimate is <s:property value="%{budgetRejectionNo}" />
		<br>
		The File has been forwarded to <s:property value="%{employeeName}" />(<s:property value="%{designation}" />)
	</s:if>	
<s:if test="%{hasActionMessages()}">
		<div class="messagestyle">
			<s:if test="%{smsSuccessMsg!=''}">
				<s:property value="%{smsSuccessMsg}"/>
			</s:if>
			<br>
			<s:if test="%{emailSuccessMsg!=''}">
				<s:property value="%{emailSuccessMsg}"/>
			</s:if>
		</div>
</s:if>
</body>
</html>
