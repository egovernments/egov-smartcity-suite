<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld" %>  
<html>
<title>
<s:if test="%{isProjectClose}">
<s:text name='page.title.projectCompletionReport'/>
</s:if>
<s:else>
Generate Deposit works/ project code
</s:else> 
</title>
<body>
<align="center">
<br>
<s:if test="%{(depositCode && depCode==true) && !isProjectClose}">
	<h4><s:text name="slDepositCode.success"><s:param  name="value" value="%{code}" /></s:text><br></h4>
</s:if>
<s:if test="%{(projectCode && prjctCode==true) && !isProjectClose}">
	<h4><s:text name="slProjectCode.success"><s:param name="value" value="%{projectCode.getCode()}" /></s:text><br></h4>
</s:if>
<br>
<s:if test="%{isProjectClose}">
	<h4><s:text name="projectCompletionReport.project.close"><s:param name="value" value="%{projectCode.getCode()}" /></s:text><br></h4>
</s:if>
<!-- To Show Back Button -->
<s:if test="%{!isProjectClose}">
	<div class="buttonholderwk">
		<input type="button" class="buttonfinal" value="BACK" id="button" name="button" onclick="self.close();window.open('${pageContext.request.contextPath}/masters/subledgerCode!newform.action?depCode=true','', 'height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');"/>
    </div>
</s:if> 
</align>
</body>
</html>   