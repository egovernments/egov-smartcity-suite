<%@ taglib prefix="egov" tagdir="/WEB-INF/tags" %>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator"%>
<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld" %>  
<html>
<title>Abstract Estimate</title>
<body>
	
	<s:property value="%{estimateNumber}"/> &nbsp; <s:text value="%{getText(messageKey)}" />
</body>

</html>