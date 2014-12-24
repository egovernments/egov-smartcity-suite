<%@ taglib prefix="egov" tagdir="/WEB-INF/tags" %>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator"%>
<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld" %>  
<html>

<title><s:text name='rateContract.search.title'/></title>

<script>
function refreshInbox(){
        var x=opener.top.opener;
        if(x==null){
            x=opener.top;
        }
	    x.document.getElementById('inboxframe').contentWindow.egovInbox.refresh();
}
</script>

<body onload="refreshInbox();">
	<s:if test="%{model.id!=null && mode!='edit'}" >
		<s:if test="%{model.egwStatus != null && model.egwStatus.code == 'APPROVED'}">
			<s:property value="%{model.rcNumber}"/>&nbsp; <s:text name="%{getText(messageKey)}" />
		</s:if>	
		<s:elseif test="%{model.egwStatus != null && model.egwStatus.code == 'CHECKED'}">
			<s:property value="%{model.rcNumber}"/>&nbsp; <s:text name="rateContract.Checked" />
			<br>
			<s:text name="common.forwardmessage" /> &nbsp;<s:property value="%{nextEmployeeName}" />(<s:property value="%{nextDesignation}" />)		
		</s:elseif>		  	 		
		<s:elseif test="%{model.egwStatus != null && model.egwStatus.code == 'CREATED' && model.department.deptName=='Garden'}">
			<s:property value="%{model.rcNumber}"/>&nbsp; The RateContract was submitted successfully-Pending for Approval<br/>
			<s:text name="common.forwardmessage" /> &nbsp;<s:property value="%{nextEmployeeName}" />(<s:property value="%{nextDesignation}" />)
		</s:elseif>
		<s:else>
			<s:property value="%{model.rcNumber}"/>&nbsp; <s:text name="%{getText(messageKey)}" />
			<br>
			<s:if test="%{model.egwStatus != null && model.egwStatus.code != 'CANCELLED'}">
				<s:text name="common.forwardmessage" /> &nbsp;<s:property value="%{nextEmployeeName}" />(<s:property value="%{nextDesignation}" />)
			</s:if>
		</s:else>
	</s:if>
</body>
</html>
