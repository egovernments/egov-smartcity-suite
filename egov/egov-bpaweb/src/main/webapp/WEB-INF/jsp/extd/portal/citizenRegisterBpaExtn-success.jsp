<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/includes/taglibs.jsp" %>
<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="sj" uri="/WEB-INF/struts-jquery-tags.tld"%>

<meta http-equiv="Content-Type" content="text/html; charset=utf-8" /> 
<html>
   
<title><s:text name='NewBPA.title'/></title>
<script>
function showPageHeader(){
	var userRoles= document.getElementById('userRole').value; 
	 
	if(userRoles!="" && userRoles!=null && userRoles!='PORTALUSERSURVEYOR'){
		dom.get("breadcrumbHeader").innerHTML='<s:text name="citizenPortal.header" />';
	}
	else{
		dom.get("breadcrumbHeader").innerHTML='<s:text name="surveyorPortal.header" />'; 
	}
}
</script>

<body onload="showPageHeader();refreshInbox();">
	<s:if test="%{hasActionMessages()}">
		<div class="errorstyle">
			<s:actionmessage />
		</div>
	</s:if>
	
	<s:form theme="simple" name="citizenSuccessForm">
			<s:hidden id="userRole" name="userRole" value="%{userRole}"/>
	</s:form>		
</body>
</html>
