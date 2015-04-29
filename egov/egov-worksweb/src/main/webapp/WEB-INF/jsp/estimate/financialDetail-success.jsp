<%@ taglib prefix="egov" tagdir="/WEB-INF/tags" %>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator"%>
<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld" %>  
<html>
<title>Abstract Estimate</title>
<body onload="opener.opener.top.document.getElementById('inboxframe').contentWindow.egovInbox.refresh();">
	
	<s:if test="%{hasActionMessages()}">
        <div class="messagestyle">
            <s:actionmessage theme="simple"/>
        </div>	
    </s:if>

   <s:if test="%{model.abstractEstimate.budgetApprNo!=null && skipBudget==false}">
     The budget appropriation number is <s:property value="%{model.abstractEstimate.budgetApprNo}" />
    </s:if>	
    <s:if test="%{model.abstractEstimate.budgetApprNo!=null && skipBudget==true}">
     The deposit works appropriation number is <s:property value="%{model.abstractEstimate.budgetApprNo}" />
    </s:if>		
	<br />
	<br />
	<s:if test="%{employeeName!=null}">
		The File has been forwarded to <s:property value="%{employeeName}" />(<s:property value="%{designation}" />)
	</s:if>
	<br />
	<s:if test="%{model.abstractEstimate.state.text2!=null}">
		Estimate approval number is <s:property value="%{model.abstractEstimate.state.text2}" /> 
	</s:if>	
</body>
</html>
