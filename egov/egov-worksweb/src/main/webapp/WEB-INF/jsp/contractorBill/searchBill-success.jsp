
<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld" %>  
<html>
<title><s:text name='cancel.bill.title' /></title>  
<body>
		<s:property value="%{billNumber}"/> &nbsp; <s:text name="%{getText(messageKey)}" />		
</body>
</html>
