<%@ taglib prefix="egov" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld" %>  
<html>
<title>
	<s:if test="%{mode=='edit'}">
		<s:text name='modify.dlp.page.title'/>
	</s:if>
	<s:else>
		<s:text name='dlp.update.page.title'/>
	</s:else>
</title>
<body>
<align="center">
	<s:text name="%{getText(messageKey)}" />
</align>
</body>
</html>